package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.UsernamePassword;
import com.epam.gym_app_main_mq.aspect.LogRestDetails;
import com.epam.gym_app_main_mq.global.EndpointSuccessCounter;
import com.epam.gym_app_main_mq.model.TrainingType;
import com.epam.gym_app_main_mq.service.TrainingTypeService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@LogRestDetails
@RestController
public class TrainingTypesController {

    private final TrainingTypeService trainingTypeService;

    private final EndpointSuccessCounter endpointSuccessCounter;

    @Autowired
    public TrainingTypesController(
            TrainingTypeService trainingTypeService,
            EndpointSuccessCounter endpointSuccessCounter,
            MeterRegistry meterRegistry
    ) {
        this.trainingTypeService = trainingTypeService;
        this.endpointSuccessCounter = endpointSuccessCounter;
    }

    @GetMapping("/gym-app/training-types")
    @Operation(summary = "get training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee activated successfully")
    })
    public ResponseEntity<List<TrainingType>> getTrainingTypes(
            @Valid @RequestBody UsernamePassword usernamePassword
    ) {
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        endpointSuccessCounter.incrementCounter("GET/training-types");
        return ResponseEntity.ok(trainingTypes);
    }
}
