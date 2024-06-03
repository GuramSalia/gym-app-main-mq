package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.TrainingDTO;
import com.epam.gym_app_main_mq.api.TrainingRegistrationRequest;
import com.epam.gym_app_main_mq.api.TrainingsByTraineeRequest;
import com.epam.gym_app_main_mq.api.TrainingsByTrainerRequest;
import com.epam.gym_app_main_mq.api.stat.*;
import com.epam.gym_app_main_mq.aspect.LogDetails;
import com.epam.gym_app_main_mq.global.EndpointSuccessCounter;
import com.epam.gym_app_main_mq.messaging.Senders;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.Training;
import com.epam.gym_app_main_mq.model.TrainingType;
import com.epam.gym_app_main_mq.service.TraineeService;
import com.epam.gym_app_main_mq.service.TrainerService;
import com.epam.gym_app_main_mq.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@LogDetails
@RestController
public class TrainingController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final EndpointSuccessCounter endpointSuccessCounter;
    private final Senders mqSenders;

    @Autowired
    public TrainingController(
            TrainingService trainingService,
            TraineeService traineeService,
            TrainerService trainerService,
            EndpointSuccessCounter endpointSuccessCounter,
            Senders mqSenders
    ) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.endpointSuccessCounter = endpointSuccessCounter;
        this.mqSenders = mqSenders;
    }


    @PostMapping("/gym-app/training")
    @Operation(summary = "Register Training")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Training registered successfully")
            }
    )
    public ResponseEntity<TrainingDTO> registerTraining(
            @Valid @RequestBody TrainingRegistrationRequest trainingRegistrationRequest,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        logCorrelationId(correlationId);
        correlationId = checkAndIfInvalidUpdateCorrelationId(correlationId);
        logUpdatedCorrelationId(correlationId);

        Training training = getTraining(trainingRegistrationRequest);
        trainingService.create(training);
        TrainingDTO trainingDTO = new TrainingDTO(training);
        endpointSuccessCounter.incrementCounter("POST/training");

        UpdateStatRequestInMainApp updateStatRequestInMainApp = getUpdateStatRequestFromTraining(training);
        updateStatRequestInMainApp.setActionType(ActionType.ADD);
        log.info("\n\n - updateStatRequestInMainApp--\n{}\n\n", updateStatRequestInMainApp);

        mqSenders.requestStatUpdate(updateStatRequestInMainApp, correlationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(trainingDTO);
    }


    @DeleteMapping("/gym-app/training")
    @Operation(summary = "Delete Training")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Training registered successfully")
            }
    )
    public ResponseEntity<Map<String, Integer>> deleteTraining(
            @Valid @RequestBody DeleteTrainingRequest deleteTrainingRequest,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        logCorrelationId(correlationId);
        correlationId = checkAndIfInvalidUpdateCorrelationId(correlationId);
        logUpdatedCorrelationId(correlationId);

        Integer trainingId = deleteTrainingRequest.getTrainingId();
        Training training = trainingService.getById(trainingId);
        trainingService.delete(trainingId);
        endpointSuccessCounter.incrementCounter("POST/training");

        UpdateStatRequestInMainApp updateStatRequestInMainApp = getUpdateStatRequestFromTraining(training);
        updateStatRequestInMainApp.setActionType(ActionType.DELETE);

        mqSenders.requestStatUpdate(updateStatRequestInMainApp, correlationId);

        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @GetMapping("/gym-app/trainings/monthly-stat")
    @Operation(summary = "Get monthly stat about total training minutes of a given trainer in a particular month")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "no content returned, but successfully requested monthly stat")
            }
    )
    public ResponseEntity<Map<String, Integer>> getTrainerMonthlyStats(
            @Valid @RequestBody MonthlyStatRequestInMainApp monthlyStatRequestInMainApp,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        logCorrelationId(correlationId);
        correlationId = checkAndIfInvalidUpdateCorrelationId(correlationId);
        logUpdatedCorrelationId(correlationId);

        mqSenders.requestMonthlyStat(monthlyStatRequestInMainApp, correlationId);

        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @GetMapping("/gym-app/trainings/full-stat")
    @Operation(summary = "Get full stat about total training minutes of a given trainer in a particular month")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "no content returned, but successfully requested full stat of a trainer")})
    public ResponseEntity<Map<Integer, List<Map<String, Integer>>>> getTrainerFullStats(
            @Valid @RequestBody FullStatRequestInMainApp fullStatRequestInMainApp,
            @RequestHeader(name = "gym_app_correlation_id", required = false, defaultValue = "no-correlation-id") String correlationId
    ) {
        logCorrelationId(correlationId);
        correlationId = checkAndIfInvalidUpdateCorrelationId(correlationId);
        logUpdatedCorrelationId(correlationId);

        mqSenders.requestFullStat(fullStatRequestInMainApp, correlationId);

        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    private UpdateStatRequestInMainApp getUpdateStatRequestFromTraining(Training training) {

        Date date = training.getTrainingDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;

        String username = training.getTrainer().getUsername();
        String firstName = training.getTrainer().getFirstName();
        String lastName = training.getTrainer().getLastName();
        Boolean status = training.getTrainer().getIsActive();

        UpdateStatRequestInMainApp request = new UpdateStatRequestInMainApp();
        request.setTrainerId(training.getTrainer().getUserId());
        request.setYear(year);
        request.setMonth(month);
        request.setDuration(training.getTrainingDurationInMinutes());

        request.setUserName(username);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setStatus(status);

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

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

    private String checkAndIfInvalidUpdateCorrelationId(String correlationId) {
        if (correlationId == null || correlationId.isEmpty() || "no-correlation-id".equals(correlationId)) {
            correlationId = generateCorrelationId();
        }
        return correlationId;
    }

    private void logCorrelationId(String correlationId) {
        log.info("\n\nTrainingController -> update stat  -> initial correlationId: {}\n\n", correlationId);
    }

    private void logUpdatedCorrelationId(String correlationId) {
        log.info("\n\nTrainingController -> update stat  -> updated correlationId: {}\n\n", correlationId);
    }
}
