package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.utils.ImplementsFindByUsernameAndPassword;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO extends ImplementsFindByUsernameAndPassword {
    public Optional<Trainer> create(Trainer trainer);

    public Optional<Trainer> update(Trainer trainer);

    public Optional<Trainer> getById(int id);

    public List<Trainer> getTrainers();

    public Optional<Trainer> findByUsername(String username);

    public Optional<Trainer> findByUsernameAndPassword(String username, String password);

    public List<Integer> findIdsOfUnassignedTrainersByTraineeUsername(String traineeUsername);
}
