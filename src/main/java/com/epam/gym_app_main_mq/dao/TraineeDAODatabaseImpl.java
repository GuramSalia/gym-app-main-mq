package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.repository.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Profile("!dev_in_memory")
@Repository("TRAINEE_DATABASE")
public class TraineeDAODatabaseImpl implements TraineeDAO {
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeDAODatabaseImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Optional<Trainee> create(Trainee trainee) {
        log.info("<<>> TraineeDAODatabaseImpl create()");
        return Optional.of(traineeRepository.save(trainee));
    }

    @Override
    public Optional<Trainee> update(Trainee trainee) {
        return Optional.of(traineeRepository.save(trainee));
    }

    @Override
    public boolean delete(Trainee trainee) {
        traineeRepository.delete(trainee);
        return traineeRepository.findById(trainee.getUserId()).isEmpty();

    }

    @Override
    public Optional<Trainee> getById(int id) {
        return traineeRepository.findById(id);
    }

    @Override
    public List<Trainee> getTrainees() {
        return traineeRepository.findAll();
    }

    public Optional<Trainee> findByUsername(String username) {
        return traineeRepository.findByUsername(username);
    }

    public Optional<Trainee> findByUsernameAndPassword(String username, String password) {
        return traineeRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<Trainee> findByUsernameWithQuery(String username) {
        return traineeRepository.findByUsernameWithQuery(username);
    }
}


