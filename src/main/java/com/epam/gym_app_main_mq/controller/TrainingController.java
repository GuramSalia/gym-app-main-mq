package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.*;
import com.epam.gym_app_main_mq.aspect.LogRestDetails;
import com.epam.gym_app_main_mq.global.EndpointSuccessCounter;
import com.epam.gym_app_main_mq.model.*;
import com.epam.gym_app_main_mq.proxy.TrainingStatsProxy;
import com.epam.gym_app_main_mq.service.TokenService;
import com.epam.gym_app_main_mq.service.TraineeService;
import com.epam.gym_app_main_mq.service.TrainerService;
import com.epam.gym_app_main_mq.service.TrainingService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@LogRestDetails
@RestController
public class TrainingController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final EndpointSuccessCounter endpointSuccessCounter;
    private final TokenService tokenService;

    private final TrainingStatsProxy proxy;

    /*
         TASK2.1
         1. update training stats: when training created. action type is 'ADD'. endpoint: "/training".
         2. update training stats: when training removed. action type is 'DELETE'. endpoint: new DELETE "/training".
         3. get training stats: full stat.  endpoint: new GET "/training/monthly-stat"
         4. get training stats: monthly stat. endpoint: new GET "/training/full-stat"

         TASK3.1
         1. instead of proxy use ActiveMQ
         2. since communication should be asynchronous, I need to use MessageListener.
         3. I assume I can use Artemis as ActiveMQ
    */
    @Autowired
    public TrainingController(
            TrainingService trainingService,
            TraineeService traineeService,
            TrainerService trainerService,
            EndpointSuccessCounter endpointSuccessCounter,
            MeterRegistry meterRegistry, TokenService tokenService,
            TrainingStatsProxy proxy
    ) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.endpointSuccessCounter = endpointSuccessCounter;
        this.tokenService = tokenService;
        this.proxy = proxy;
    }

    // modified POST/training
    @PostMapping("/gym-app/training")
    @Operation(summary = "Register Training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training registered successfully")
    })
    public ResponseEntity<Map<String, Integer>> registerTraining(
            @Valid @RequestBody TrainingRegistrationRequest trainingRegistrationRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        Training training = getTraining(trainingRegistrationRequest);
        trainingService.create(training);
        endpointSuccessCounter.incrementCounter("POST/training");

        UpdateStatRequest updateStatRequest = getUpdateStatRequestFromTraining(training);
        updateStatRequest.setActionType(ActionType.ADD);

        int trainerId = updateStatRequest.getTrainerId();
        String jwtToken = getTokenByTrainerId(trainerId);
        updateStatRequest.setToken(jwtToken);

        log.info("\n\nTrainingController -> update stat  -> correlationId: {}\n\n", correlationId);
        log.info("\n\n - updateStatRequest--\n{}\n\n", updateStatRequest);
        ResponseEntity<Map<String, Integer>> updateStats = proxy.updateTrainerStats(updateStatRequest, correlationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(updateStats.getBody());
    }

    // new DELETE/training
    @DeleteMapping("/gym-app/training")
    @Operation(summary = "Delete Training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training registered successfully")
    })
    public ResponseEntity<Map<String, Integer>> deleteTraining(
            @Valid @RequestBody DeleteTrainingRequest deleteTrainingRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        Integer trainingId = deleteTrainingRequest.getTrainingId();
        trainingService.delete(trainingId);
        endpointSuccessCounter.incrementCounter("POST/training");

        Training training = trainingService.getById(trainingId);

        UpdateStatRequest updateStatRequest = getUpdateStatRequestFromTraining(training);
        updateStatRequest.setActionType(ActionType.DELETE);

        String jwtToken = getTokenByTrainingId(trainingId);
        updateStatRequest.setToken(jwtToken);

        log.info("\n\nTrainingController -> delete stat  -> correlationId: {}\n\n", correlationId);
        ResponseEntity<Map<String, Integer>> response = proxy.updateTrainerStats(updateStatRequest, correlationId);

        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    // new GET/training/monthly-stat
    @GetMapping("/gym-app/trainings/monthly-stat")
    @Operation(summary = "Get monthly stat about total training minutes of a given trainer in a particular month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stat")
    })
    public ResponseEntity<Map<String, Integer>> getTrainerMonthlyStats(
            @Valid @RequestBody MonthlyStatRequest monthlyStatRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        int trainerId = monthlyStatRequest.getTrainerId();
        String jwtToken = getTokenByTrainerId(trainerId);
        monthlyStatRequest.setToken(jwtToken);

        log.info("\n\nTrainingController -> get monthly stat  -> correlationId: {}\n\n", correlationId);
        ResponseEntity<Map<String, Integer>> response = proxy.getTrainerMonthlyStats(monthlyStatRequest, correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    // new GET/training/full-stat
    @GetMapping("/gym-app/trainings/full-stat")
    @Operation(summary = "Get full stat about total training minutes of a given trainer in a particular month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved full stat of a trainer")
    })
    public ResponseEntity<Map<Integer, List<Map<String, Integer>>>> getTrainerFullStats(
            @Valid @RequestBody FullStatRequest fullStatRequest,
            @RequestHeader(name = "gym-app-correlation-id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        log.info("\n\nTrainingController -> get full stat -> correlationId: {}\n\n", correlationId);

        int trainerId = fullStatRequest.getTrainerId();
        String jwtToken = getTokenByTrainerId(trainerId);
        fullStatRequest.setToken(jwtToken);

        ResponseEntity<Map<Integer, List<Map<String, Integer>>>> response = proxy.getTrainerFullStats(fullStatRequest
                , correlationId);
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    private String getTokenByTrainerId(int trainerId) {
        log.info("\n\n TrainingController > getTokenByTrainerId\n\n");
        User trainer = trainerService.getById(trainerId);
        String validTokenByUsername = tokenService.getValidTokenByUsername(trainer);
        log.info("\n\n  validTokenByUsername {}  \n\n", validTokenByUsername);
        return validTokenByUsername;
    }

    private String getTokenByTrainingId(int trainingId) {
        Training training = trainingService.getById(trainingId);
        int trainerId = training.getTrainer().getUserId();
        return getTokenByTrainerId(trainerId);
    }

    private UpdateStatRequest getUpdateStatRequestFromTraining(Training training) {
        Date date = training.getTrainingDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;

        UpdateStatRequest request = new UpdateStatRequest();
        request.setTrainerId(training.getTrainer().getUserId());
        request.setYear(year);
        request.setMonth(month);
        request.setDuration(training.getTrainingDurationInMinutes());

        return request;
    }

    private MonthlyStatRequest getMonthlyStatRequestFromTraining(Training training) {
        MonthlyStatRequest request = new MonthlyStatRequest();
        Date date = training.getTrainingDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;

        request.setTrainerId(training.getTrainer().getUserId());
        request.setYear(year);
        request.setMonth(month);

        return request;
    }

    @GetMapping("/trainings/of-trainee")
    @Operation(summary = "Get Trainings by Trainee and optionally by period from, period to, trainer name, training " +
            "type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainings")
    })
    public ResponseEntity<List<TrainingDTO>> getTrainingsByTraineeAndOtherFilters(
            @Valid @RequestBody TrainingsByTraineeRequest trainingsByTraineeRequest
    ) {
        List<Training> trainings = getTrainingsByTrainee(trainingsByTraineeRequest);
        List<TrainingDTO> trainingDTOs = trainings.stream().map(TrainingDTO::new).toList();
        endpointSuccessCounter.incrementCounter("GET/trainings/of-trainee");
        return ResponseEntity.ok().body(trainingDTOs);
    }

    @GetMapping("/trainings/of-trainer")
    @Operation(summary = "Get Trainings by Trainer and optionally by period from, period to, trainee name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainings")
    })
    public ResponseEntity<List<TrainingDTO>> getTrainingsByTrainerAndOtherFilters(
            @Valid @RequestBody TrainingsByTrainerRequest trainingsByTrainerRequest
    ) {
        List<Training> trainings = getTrainingsByTrainer(trainingsByTrainerRequest);
        List<TrainingDTO> trainingDTOs = trainings.stream().map(TrainingDTO::new).toList();
        endpointSuccessCounter.incrementCounter("GET/trainings/of-trainer");
        return ResponseEntity.ok().body(trainingDTOs);
    }

    private Training getTraining(TrainingRegistrationRequest trainingRegistrationRequest) {

        String traineeUsername = trainingRegistrationRequest.getTraineeUsername();
        Trainee trainee = traineeService.findByUsername(traineeUsername);
        String trainerUsername = trainingRegistrationRequest.getTrainerUsername();
        Trainer trainer = trainerService.findByUsername(trainerUsername);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization());

        String trainingName = trainingRegistrationRequest.getTrainingName();
        Date trainingDate = trainingRegistrationRequest.getTrainingDate();
        int trainingDuration = trainingRegistrationRequest.getTrainingDuration();

        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDurationInMinutes(trainingDuration);

        return training;
    }

    private List<Training> getTrainingsByTrainee(TrainingsByTraineeRequest trainingsByTraineeRequest) {
        String traineeUsername = trainingsByTraineeRequest.getUsername();
        java.sql.Date periodFrom = trainingsByTraineeRequest.getPeriodFrom();
        java.sql.Date periodTo = trainingsByTraineeRequest.getPeriodTo();
        String trainerUsername = trainingsByTraineeRequest.getTrainerUsername();
        TrainingType trainingType = trainingsByTraineeRequest.getTrainingType();

        String trainingTypeString = trainingType == null ? null : String.valueOf(trainingType.getTrainingType());

        return trainingService.getTrainingsByTraineeAndOtherFilters(
                traineeUsername,
                periodFrom,
                periodTo,
                trainerUsername,
                trainingTypeString);
    }

    private List<Training> getTrainingsByTrainer(TrainingsByTrainerRequest trainingsByTrainerRequest) {
        String trainerUsername = trainingsByTrainerRequest.getUsername();
        java.sql.Date periodFrom = trainingsByTrainerRequest.getPeriodFrom();
        java.sql.Date periodTo = trainingsByTrainerRequest.getPeriodTo();
        String traineeUsername = trainingsByTrainerRequest.getTraineeUsername();

        return trainingService.getTrainingsByTrainerAndOtherFilters(
                trainerUsername,
                periodFrom,
                periodTo,
                traineeUsername);
    }
}
