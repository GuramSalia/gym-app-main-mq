package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Profile("!dev_in_memory")
@Repository("TRAINER_DATABASE")
public class TrainerDAODatabaseImpl implements TrainerDAO {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerDAODatabaseImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Optional<Trainer> create(Trainer trainer) {
        return Optional.of(trainerRepository.save(trainer));
    }

    @Override
    public Optional<Trainer> update(Trainer trainer) {
        return Optional.of(trainerRepository.save(trainer));
    }

    @Override
    public Optional<Trainer> getById(int id) {
        return trainerRepository.findById(id);
    }

    @Override
    public List<Trainer> getTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return trainerRepository.findByUsername(username);
    }

    @Override
    public Optional<Trainer> findByUsernameAndPassword(String username, String password) {
        return trainerRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<Integer> findIdsOfUnassignedTrainersByTraineeUsername(String traineeUsername) {
        return trainerRepository.findIdsOfUnassignedTrainersByTraineeUsername(traineeUsername);
    }


}
