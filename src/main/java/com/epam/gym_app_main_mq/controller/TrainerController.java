package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.*;
import com.epam.gym_app_main_mq.aspect.LogDetails;
import com.epam.gym_app_main_mq.exception.UnauthorizedException;
import com.epam.gym_app_main_mq.global.EndpointSuccessCounter;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.service.TrainerService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@LogDetails
@RestController
public class TrainerController {

    private final TrainerService trainerService;

    private final EndpointSuccessCounter endpointSuccessCounter;

    public TrainerController(
            TrainerService trainerService,
            MeterRegistry meterRegistry,
            EndpointSuccessCounter endpointSuccessCounter
    ) {
        this.trainerService = trainerService;
        this.endpointSuccessCounter = endpointSuccessCounter;
    }

    @GetMapping("/gym-app/trainer-get")
    @Operation(summary = "Get Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainer")
    })
    public ResponseEntity<TrainerDTOWithTraineeList> getTrainer(
            @Valid @RequestBody UsernamePassword usernamePassword
    ) {
        String username = usernamePassword.getUsername();
        checkUsernameAgainstJwtUsername(username);
        Trainer trainer = trainerService.findByUsername(username);
        TrainerDTOWithTraineeList trainerDTO = getTrainerDTOWithTraineeList(trainer);
        endpointSuccessCounter.incrementCounter("GET/trainer-get");
        return ResponseEntity.ok().body(trainerDTO);
    }

    @PutMapping("/gym-app/trainer")
    @Operation(summary = "Update Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully")
    })
    public ResponseEntity<TrainerDTOUpdated> updateTrainer(
            @Valid @RequestBody TrainerUpdateRequest trainerUpdateRequest
    ) {
        String username = trainerUpdateRequest.getUsername();
        checkUsernameAgainstJwtUsername(username);
        Trainer trainer = trainerService.findByUsername(username);
        TrainerDTOUpdated trainerDTOupdated = getTrainerDTOupdated(trainerUpdateRequest, trainer);
        endpointSuccessCounter.incrementCounter("PUT/trainer");
        return ResponseEntity.ok().body(trainerDTOupdated);
    }

    @GetMapping("/gym-app/trainers/get-not-assigned-to-trainee")
    @Operation(summary = "Get Trainers Not Assigned to Trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainers")
    })
    public ResponseEntity<List<TrainerDTOForTrainersList>> getNotAssignedTrainers(
            @Valid @RequestBody UsernamePassword usernamePassword
    ) {
        String username = usernamePassword.getUsername();
        checkUsernameAgainstJwtUsername(username);
        String password = usernamePassword.getPassword();
        List<Trainer> trainerList = trainerService.findUnassignedTrainersByTraineeUsername(username, password);
        List<TrainerDTOForTrainersList> trainerDTOs = getTrainerDTOForTrainersLists(trainerList);
        endpointSuccessCounter.incrementCounter("GET/trainers/get-not-assigned-to-trainee");
        return ResponseEntity.ok().body(trainerDTOs);
    }

    @PatchMapping("/gym-app/trainer/activate")
    @Operation(summary = "Activate Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer activated successfully")
    })
    public ResponseEntity<Void> activateTrainer(
            @Valid @RequestBody UsernamePassword usernamePassword
    ) {
        String username = usernamePassword.getUsername();
        checkUsernameAgainstJwtUsername(username);
        Trainer trainer = trainerService.findByUsername(username);
        trainer.setIsActive(true);
        trainerService.update(trainer);
        endpointSuccessCounter.incrementCounter("PATCH/trainer/activate");
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @PatchMapping("/gym-app/trainer/deactivate")
    @Operation(summary = "Deactivate Trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainer deactivated successfully")
    })
    public ResponseEntity<Void> deactivateTrainer(
            @Valid @RequestBody UsernamePassword usernamePassword
    ) {
        String username = usernamePassword.getUsername();
        checkUsernameAgainstJwtUsername(username);
        Trainer trainer = trainerService.findByUsername(username);
        trainer.setIsActive(false);
        trainerService.update(trainer);
        endpointSuccessCounter.incrementCounter("PATCH/trainer/deactivate");
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    private static void checkUsernameAgainstJwtUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameFromJwt = authentication.getName();
        if (!username.equals(usernameFromJwt)) {
            throw new UnauthorizedException("you have no access to the data of this username");
        }
    }

    private TrainerDTOWithTraineeList getTrainerDTOWithTraineeList(Trainer trainer) {

        TrainerDTOWithTraineeList trainerDTO = new TrainerDTOWithTraineeList();
        trainerDTO.setFirstName(trainer.getFirstName());
        trainerDTO.setLastName(trainer.getLastName());
        trainerDTO.setSpecialization(trainer.getSpecialization());
        trainerDTO.setIsActive(trainer.getIsActive());

        List<TraineeDTOForTraineesList> trainees;

        if (trainer.getTrainees() != null) {
            trainees = trainer
                    .getTrainees()
                    .stream()
                    .map(trainee -> {
                        TraineeDTOForTraineesList traineeDTO = new TraineeDTOForTraineesList();
                        traineeDTO.setUsername(trainee.getUsername());
                        traineeDTO.setFirstName(trainee.getFirstName());
                        traineeDTO.setLastName(trainee.getLastName());
                        return traineeDTO;
                    }).toList();
        } else {
            trainees = Collections.emptyList();
        }

        Set<TraineeDTOForTraineesList> traineesSet = new HashSet<>(trainees);
        trainerDTO.setTrainees(traineesSet);

        return trainerDTO;
    }

    private TrainerDTOUpdated getTrainerDTOupdated(TrainerUpdateRequest trainerUpdateRequest, Trainer trainer) {
        trainer.setFirstName(trainerUpdateRequest.getFirstName());
        trainer.setLastName(trainerUpdateRequest.getLastName());
        trainer.setSpecialization(trainerUpdateRequest.getSpecialization());
        trainer.setIsActive(trainerUpdateRequest.getIsActive());
        Trainer updatedTrainer = trainerService.update(trainer);
        TrainerDTOUpdated trainerDTOupdated = new TrainerDTOUpdated();
        trainerDTOupdated.setUsername(updatedTrainer.getUsername());
        trainerDTOupdated.setFirstName(updatedTrainer.getFirstName());
        trainerDTOupdated.setLastName(updatedTrainer.getLastName());
        trainerDTOupdated.setSpecialization(updatedTrainer.getSpecialization());
        trainerDTOupdated.setIsActive(updatedTrainer.getIsActive());

        Set<Trainee> traineeSet = trainer.getTrainees();
        Set<TraineeDTOForTraineesList> traineeDTOList = null;

        if (traineeSet != null) {
            traineeDTOList = traineeSet
                    .stream()
                    .map(trainee -> {
                        TraineeDTOForTraineesList traineeDTO = new TraineeDTOForTraineesList();
                        traineeDTO.setUsername(trainee.getUsername());
                        traineeDTO.setFirstName(trainee.getFirstName());
                        traineeDTO.setLastName(trainee.getLastName());
                        return traineeDTO;
                    }).collect(Collectors.toSet());
        }
        trainerDTOupdated.setTrainees(traineeDTOList);
        return trainerDTOupdated;
    }

    private List<TrainerDTOForTrainersList> getTrainerDTOForTrainersLists(List<Trainer> trainerList) {
        return trainerList
                .stream()
                .map(trainer -> {
                    TrainerDTOForTrainersList trainerDTO = new TrainerDTOForTrainersList();
                    trainerDTO.setUsername(trainer.getUsername());
                    trainerDTO.setFirstName(trainer.getFirstName());
                    trainerDTO.setLastName(trainer.getLastName());
                    trainerDTO.setSpecialization(trainer.getSpecialization());
                    return trainerDTO;
                }).toList();
    }
}
