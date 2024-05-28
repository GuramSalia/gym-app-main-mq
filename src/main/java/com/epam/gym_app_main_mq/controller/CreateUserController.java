package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.TraineeRegistrationRequest;
import com.epam.gym_app_main_mq.api.TrainerRegistrationRequest;
import com.epam.gym_app_main_mq.api.UsernamePassword;
import com.epam.gym_app_main_mq.aspect.LogDetails;
import com.epam.gym_app_main_mq.global.EndpointSuccessCounter;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.TrainingType;
import com.epam.gym_app_main_mq.service.TraineeService;
import com.epam.gym_app_main_mq.service.TrainerService;
import com.epam.gym_app_main_mq.service.TrainingTypeService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@LogDetails
@RestController
public class CreateUserController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    private final EndpointSuccessCounter endpointSuccessCounter;

    public CreateUserController(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingTypeService trainingTypeService,
            EndpointSuccessCounter endpointSuccessCounter,
            MeterRegistry meterRegistry
    ) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.endpointSuccessCounter = endpointSuccessCounter;
    }

    @PostMapping("/gym-app/public/create/trainee")
    @Operation(summary = "Create Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee Registered")
    })
    public ResponseEntity<UsernamePassword> registerTrainee(
            @Valid @RequestBody TraineeRegistrationRequest traineeRegistrationRequest
    ) {

        Trainee newTrainee = getTrainee(traineeRegistrationRequest);
        UsernamePassword usernamePassword = traineeService.create(newTrainee);
        endpointSuccessCounter.incrementCounter("POST/public/create/trainee");
        return ResponseEntity.status(HttpStatus.CREATED).body(usernamePassword);
    }

    private static Trainee getTrainee(TraineeRegistrationRequest traineeRegistrationRequest) {
        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName(traineeRegistrationRequest.getFirstName());
        newTrainee.setLastName(traineeRegistrationRequest.getLastName());
        newTrainee.setAddress(traineeRegistrationRequest.getAddress());
        newTrainee.setDob(traineeRegistrationRequest.getDateOfBirth());
        newTrainee.setIsActive(true);
        return newTrainee;
    }

    @PostMapping("/gym-app/public/create/trainer")
    @Operation(summary = "Create Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee Registered")
    })
    public ResponseEntity<UsernamePassword> registerTrainer(
            @Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest
    ) {
        Trainer newTrainer = getTrainer(trainerRegistrationRequest);
        UsernamePassword usernamePassword = trainerService.create(newTrainer);
        endpointSuccessCounter.incrementCounter("POST/public/create/trainer");
        return ResponseEntity.status(HttpStatus.CREATED).body(usernamePassword);
    }

    private Trainer getTrainer(TrainerRegistrationRequest trainerRegistrationRequest) {
        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName(trainerRegistrationRequest.getFirstName());
        newTrainer.setLastName(trainerRegistrationRequest.getLastName());

        TrainingType.TrainingTypeEnum typeEnum =
                TrainingType.TrainingTypeEnum.valueOf(trainerRegistrationRequest.getSpecialization());

        TrainingType specialization = trainingTypeService.getTrainingType(typeEnum);
        newTrainer.setSpecialization(specialization);
        newTrainer.setIsActive(true);

        return newTrainer;
    }

    @GetMapping("/gym-app/")
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/gym-app/home")
    public String home() {
        log.info("\n\n ----------- /home called --------\n\n");
        return "Hello World!";
    }
}
