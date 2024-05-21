package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.config.storage.InMemoryStorage;
import com.epam.gym_app_main_mq.config.storage.idgeneration.UserIdForInMemoryStorage;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Profile("dev_in_memory")
@Repository("TRAINER_IN_MEMORY")
public class TrainerDAOInMemoryStorageImpl implements TrainerDAO {
    //    private final Map<Integer, Trainer> trainers;
    private final InMemoryStorage storage;
    private int index = 2;

    @Autowired
    public TrainerDAOInMemoryStorageImpl(InMemoryStorage storage) {
        this.storage = storage;
        //        this.trainers = storage.getTrainers();
    }

    @Override
    public Optional<Trainer> create(Trainer trainer) {

//        int id = trainer.getUserId();
        int id = UserIdForInMemoryStorage.getNewId();
        trainer.setUserId(id);
        log.info("id: " + trainer.toString());
        log.info("trainer: " + trainer.getFirstName());
        if (storage.getTrainers().containsKey(id)) {
            log.error("Trainer with id {} already exists", id);
            return Optional.empty();
        }
        storage.getTrainers().put(id, trainer);
        return Optional.of(storage.getTrainers().get(id));
    }

    @Override
    public Optional<Trainer> update(Trainer trainer) {
        int id = trainer.getUserId();
        if (storage.getTrainers().containsKey(id)) {
            storage.getTrainers().put(id, trainer);
            return Optional.of(storage.getTrainers().get(id));
        } else {
            log.error("Trainer with id {} does not exist", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Trainer> getById(int id) {return Optional.ofNullable(storage.getTrainers().get(id));}

    @Override
    public List<Trainer> getTrainers() {return new ArrayList<>(storage.getTrainers().values());}

    @Override
    public Optional<Trainer> findByUsername(String username) {
        log.info("'findByUsername' from TrainerDaoInMemoryStorageImpl");
        for (Trainer currentTrainer : storage.getTrainers().values()) {
            if (currentTrainer.getUsername().equals(username)) {
                return Optional.of(currentTrainer);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Trainer> findByUsernameAndPassword(String username, String password) {
        log.info("'findByUsernameAndPassword' from TrainerDaoInMemoryStorageImpl");
        for (Trainer currentTrainer : storage.getTrainers().values()) {
            if (currentTrainer.getUsername().equals(username) && currentTrainer.getPassword().equals(password)) {
                return Optional.of(currentTrainer);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Integer> findIdsOfUnassignedTrainersByTraineeUsername(String traineeUsername) {
        log.info("'findUnassignedTrainersByTraineeUsername' method from TrainerDaoInMemoryStorageImpl");
        List<Integer> resultIds = new ArrayList<Integer>();
        Collection<Trainee> traineeMap = storage.getTrainees().values();
        Collection<Trainer> trainerMap = storage.getTrainers().values();
        List<Integer> idsOfTrainersOfTrainee = new ArrayList<Integer>();
        Optional<Trainee> traineeOptional = storage.getTrainees()
                                                   .values()
                                                   .stream()
                                                   .filter(trainee -> trainee.getUsername().equals(traineeUsername))
                                                   .findFirst();
        if (traineeOptional.isEmpty()) {
            resultIds.addAll(storage.getTrainers().values().stream().map(Trainer::getUserId).toList());
            return resultIds;
        }

        Trainee trainee = traineeOptional.get();
        idsOfTrainersOfTrainee.addAll(trainee.getTrainers().stream().map(Trainer::getUserId).toList());
        for (Trainer trainer : trainerMap) {
            if (!idsOfTrainersOfTrainee.contains(trainer.getUserId())) {
                resultIds.add(trainer.getUserId());
            }
        }

        return resultIds;
    }
}
