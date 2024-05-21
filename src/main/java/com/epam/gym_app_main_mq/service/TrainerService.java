package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.api.UsernamePassword;
import com.epam.gym_app_main_mq.dao.TraineeDAO;
import com.epam.gym_app_main_mq.dao.TrainerDAO;
import com.epam.gym_app_main_mq.exception.UnauthorizedException;
import com.epam.gym_app_main_mq.exception.UserNotCreatedException;
import com.epam.gym_app_main_mq.exception.UserNotFoundException;
import com.epam.gym_app_main_mq.exception.UserNotUpdatedException;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.utils.CheckIfUserHasRequiredFields;
import com.epam.gym_app_main_mq.utils.CheckIfUsernameExists;
import com.epam.gym_app_main_mq.utils.RandomPasswordGenerator;
import com.epam.gym_app_main_mq.utils.UsernameGenerator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainerService {

    private final CheckIfUsernameExists checkIfUsernameExists;
    private final CheckIfUserHasRequiredFields checkIfUserHasRequiredFields;
    private final UsernameGenerator usernameGenerator;
    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public TrainerService(
            TrainerDAO trainerDAO,
            TraineeDAO traineeDAO,
            CheckIfUsernameExists checkIfUsernameExists,
            CheckIfUserHasRequiredFields checkIfUserHasRequiredFields,
            UsernameGenerator usernameGenerator
    ) {
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
        this.checkIfUsernameExists = checkIfUsernameExists;
        this.checkIfUserHasRequiredFields = checkIfUserHasRequiredFields;
        this.usernameGenerator = usernameGenerator;
    }

    public Trainer getById(int id, String username, String password) {
        if (!checkIfUsernameExists.usernameExists(trainerDAO, username, password)) {
            throw new UnauthorizedException("no trainer with such username or password");
        }
        log.info(">>>> Getting trainer with id: " + id);
        Optional<Trainer> trainerOptional = trainerDAO.getById(id);
        if (trainerOptional.isEmpty()) {
            throw new UserNotFoundException("no such trainer");
        }
        return trainerOptional.get();
    }

    public Trainer getById(int id) {

        log.info(">>>> Getting trainer with id: " + id);
        Optional<Trainer> trainerOptional = trainerDAO.getById(id);
        if (trainerOptional.isEmpty()) {
            throw new UserNotFoundException("no such trainer");
        }
        return trainerOptional.get();
    }

    public List<Trainer> getTrainers() {
        log.info(">>>> Getting trainers");
        return trainerDAO.getTrainers();
    }

    @Transactional
    public UsernamePassword create(Trainer trainer) {
        trainer.setIsBlocked(false);
        trainer.setFailedLoginAttempts(0);
        trainer.setBlockStartTime(null);
        trainer.setUsername(usernameGenerator.generateUsername(trainer));

        String generateRandomPassword = RandomPasswordGenerator.generateRandomPassword();
        UsernamePassword usernamePassword = new UsernamePassword(trainer.getUsername(), generateRandomPassword);
        trainer.setPassword(passwordEncoder.encode(generateRandomPassword));

        log.info(">>>> Creating trainer with username: " + trainer.getUsername());
        if (checkIfUserHasRequiredFields.isInvalidUser(trainer)) {
            log.error("invalid customer");
            throw new IllegalStateException("invalid user");
        }
        Optional<Trainer> trainerOptional = trainerDAO.create(trainer);
        if (trainerOptional.isEmpty()) {
            throw new UserNotCreatedException("no such trainer");
        }
        return usernamePassword;
    }

    // InMemory implementation doesn't require 3 arguments
    public Trainer update(Trainer trainer) {
        Optional<Trainer> trainerOptional = trainerDAO.update(trainer);
        if (trainerOptional.isEmpty()) {
            throw new UserNotUpdatedException("error updating trainer");
        }
        return trainerOptional.get();
    }

    @Transactional
    public Trainer update(Trainer trainer, String username, String password) {

        if (!checkIfUsernameExists.usernameExists(trainerDAO, username, password)) {
            log.error("invalid username ^^^");
            throw new UnauthorizedException("no trainer with such username or password");
        }

        if (checkIfUserHasRequiredFields.isInvalidUser(trainer)) {
            log.info("invalid user :::");
            throw new IllegalStateException("invalid user");
        }

        log.info(">>>> Updating trainer with username: " + trainer.getUsername());
        Optional<Trainer> trainerOptional = trainerDAO.update(trainer);
        if (trainerOptional.isEmpty()) {
            throw new UserNotUpdatedException("error updating trainer");
        }
        return trainerOptional.get();
    }

    // Is this not redundant, because we only encoded password in db?
    public Trainer findByUsernameAndPassword(String username, String password) {

        if (!checkIfUsernameExists.usernameExists(trainerDAO, username, password)) {
            throw new UnauthorizedException("no trainer with such username or password");
        }

        log.info(">>>> Getting trainer using getByUsername: " + username);
        Optional<Trainer> trainerOptional = trainerDAO.findByUsername(username);
        if (trainerOptional.isEmpty()) {
            log.error("invalid username or password");
            throw new UserNotFoundException("no such trainer");
        }

        return trainerOptional.get();
    }

    public Trainer findByUsername(String username) {
        log.info(">>>> Getting trainer using getByUsername: " + username);
        Optional<Trainer> trainerOptional = trainerDAO.findByUsername(username);
        if (trainerOptional.isEmpty()) {
            log.error("invalid username or password");
            throw new UserNotFoundException("no such trainer");
        }

        return trainerOptional.get();
    }

    @Transactional
    public Trainer updatePassword(
            Trainer trainer,
            String username,
            String currentPassword,
            String newPassword
    ) {
        log.info(">>>> Updating trainer with username: " + trainer.getUsername());
        if (!checkIfUsernameExists.usernameExists(trainerDAO, username, currentPassword)) {
            throw new UnauthorizedException("no trainer with such username or password");
        }

        if (checkIfUserHasRequiredFields.isInvalidUser(trainer)) {
            throw new IllegalStateException("invalid user");
        }

        trainer.setPassword(passwordEncoder.encode(newPassword));
        //        trainer.setPassword(newPassword);

        return update(trainer);
    }

    @Transactional
    public boolean activateTrainer(Trainer trainer, String username, String password) {

        if (!checkIfUsernameExists.usernameExists(trainerDAO, username, password)) {
            throw new UnauthorizedException("no trainer with such username or password");
        }

        if (trainer.getIsActive()) {
            log.info("trainer is already active");
            return false;
        }

        trainer.setIsActive(true);
        update(trainer);
        log.info("trainer is active");
        return true;
    }

    @Transactional
    public boolean deactivateTrainer(Trainer trainer, String username, String password) {

        if (!checkIfUsernameExists.usernameExists(trainerDAO, username, password)) {
            throw new UnauthorizedException("no trainer with such username or password");
        }

        if (!trainer.getIsActive()) {
            log.info("trainer is already deactivated");
            return false;
        }

        trainer.setIsActive(false);
        update(trainer);
        log.info("trainer is deactivated");
        return true;
    }

    public List<Trainer> findUnassignedTrainersByTraineeUsername(String traineeUsername, String password) {

        if (!checkIfUsernameExists.usernameExists(traineeDAO, traineeUsername, password)) {
            throw new UnauthorizedException("no trainee with such username or password");
        }

        List<Integer> ids = trainerDAO.findIdsOfUnassignedTrainersByTraineeUsername(traineeUsername);
        List<Trainer> trainers = new ArrayList<>();
        for (Integer id : ids) {
            Trainer trainer = getById(id);
            trainers.add(trainer);
        }

        return trainers;
    }
}
