package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.dao.TraineeDAO;
import com.epam.gym_app_main_mq.dao.TrainerDAO;
import com.epam.gym_app_main_mq.exception.UnauthorizedException;
import com.epam.gym_app_main_mq.exception.UserBlockedException;
import com.epam.gym_app_main_mq.model.Role;
import com.epam.gym_app_main_mq.model.Trainee;
import com.epam.gym_app_main_mq.model.Trainer;
import com.epam.gym_app_main_mq.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsernamePasswordValidationService {
    private static final int MAX_FAILED_ATTEMPTS = 2;
    private static final int BLOCK_DURATION_MINUTES = 1;

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Role validateUsernamePasswordAndReturnRole(String username, String password) {
        Optional<Trainee> traineeOptional = traineeDAO.findByUsername(username);
        Optional<Trainer> trainerOptional = trainerDAO.findByUsername(username);
        if (traineeOptional.isEmpty() && trainerOptional.isEmpty()) {
            throw new UnauthorizedException("username or password is incorrect");
        }

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            authenticate(trainee, password);
            return Role.TRAINEE;
        }

        authenticate(trainerOptional.get(), password);
        return Role.TRAINER;
    }

    private void authenticate(User user, String password) {
        log.info("\n\n>> UsernamePasswordValidationService > withing authenticate method\n");
        boolean isBlocked = user.getIsBlocked();
        boolean correctPasswordProvided = passwordEncoder.matches(password, user.getPassword());

        if (!isBlocked && correctPasswordProvided) {
            log.info("\n\n>>UsernamePasswordValidationService >authenticate >not blocked, correct pwd provided\n");
            if (user.getFailedLoginAttempts() > 0) {
                user.setFailedLoginAttempts(0);
                saveUser(user);
            }
            return;
        }

        if (isBlocked) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime blockedUntil = user.getBlockStartTime().plusMinutes(BLOCK_DURATION_MINUTES);

            // blocked time not expired
            if (now.isBefore(blockedUntil)) {
                log.info("\n\n>>UsernamePasswordValidationService >authenticate > isBlocked , blocked not expired\n");
                StringBuilder sb = new StringBuilder();
                sb.append("user is still blocked. Try again when ")
                  .append(BLOCK_DURATION_MINUTES)
                  .append(" minutes passed since it was last blocked");
                throw new UserBlockedException(sb.toString());
            }

            // blocked time expired
            user.setIsBlocked(false);
            if (!correctPasswordProvided) {
                log.info("\n\n>>UsernamePasswordValidationService >isBlocked but blocked expired, incorrect pwd\n");
                user.setFailedLoginAttempts(1);
                saveUser(user);
                throw new UnauthorizedException("username or password incorrect");
            }

            log.info("\n\n>>UsernamePasswordValidationService > isBlocked , blocked expired, correct pwd\n");
            user.setBlockStartTime(null);
            user.setFailedLoginAttempts(0);
            saveUser(user);
            return;
        }

        int newFailAttempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(newFailAttempts);
        if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setIsBlocked(true);
            user.setBlockStartTime(LocalDateTime.now());
            saveUser(user);
            throw new UserBlockedException("user blocked for " + BLOCK_DURATION_MINUTES + " minutes");
        }
        saveUser(user);
        throw new UnauthorizedException("username or password incorrect");
    }

    private void saveUser(User user) {
        log.warn("\n\n>>UsernamePasswordValidationService > saveUser() \n");
        if (user instanceof Trainee) {
            traineeDAO.update((Trainee) user);
        } else if (user instanceof Trainer) {
            trainerDAO.update((Trainer) user);
        }
    }
}
