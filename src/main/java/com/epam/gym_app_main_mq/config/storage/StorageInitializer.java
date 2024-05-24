package com.epam.gym_app_main_mq.config.storage;

import com.epam.gym_app_main_mq.model.JsonDataContainer;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("dev_in_memory")
public class StorageInitializer {

    @Value("${file.path.initialData}")
    private Resource initialDataResource;

    private final InMemoryStorage storage;

    public StorageInitializer(InMemoryStorage storage) {
        log.info("\n\nstorage in StorageInitializer : {}\n", storage);
        log.info("\n\ntrainees in StorageInitializer : {}\n", storage.getTrainees().hashCode());
        this.storage = storage;
    }

    @PostConstruct
    public void initialize() {
        log.info("\n\n\n>>>> Storage Initialization 1 / 3 \n\n\n");

        ObjectMapper om = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        om.setDateFormat(dateFormat);

        try (InputStream inputStream = initialDataResource.getInputStream()) {
            log.info("\n\n\n>>>> Storage Initialization 2 / 3\n\n\n");
            JsonDataContainer root = om.readValue(inputStream, JsonDataContainer.class);

            // Assuming you have appropriate getters in your InMemoryStorage class
            storage.setTrainers(root.trainer.stream().collect(Collectors.toMap(Trainer::getUserId, Function.identity())));
            storage.setTrainees(root.trainee.stream().collect(Collectors.toMap(Trainee::getUserId, Function.identity())));
            storage.setTrainings(root.training.stream()
                                              .collect(Collectors.toMap(Training::getTrainingId, Function.identity())));
            log.info("\n\n\n>>>> Storage Initialization 3 / 3 trainees.size\n\n\n" + storage.getTrainees().size());
            log.info("\n\n\n>>>> Storage Initialization 3 / 3 trainees.size\n\n\n" + storage.getTrainees().values());


            log.info("\n\n\n>>>> Storage Initialization 3 / 3 trainers.size\n\n\n" + storage.getTrainers().size());
            log.info("\n\n\n>>>> Storage Initialization 3 / 3 trainings.size\n\n\n" + storage.getTrainings().size());
        } catch (IOException e) {
            log.error("Error initializing InMemoryStorage", e);
        }
    }
}

