package com.epam.gym_app_main_mq.utils;

import com.epam.gym_app_main_mq.dao.TraineeDAO;
import com.epam.gym_app_main_mq.dao.TrainerDAO;
import com.epam.gym_app_main_mq.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@Getter
@Setter
public class UsernameGenerator {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    public UsernameGenerator(TrainerDAO trainerDAO, TraineeDAO traineeDAO) {
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
    }

    public String generateUsername(User user) {

        String base = user.getFirstName() + "." + user.getLastName();
        int counter = 1;
        String username = base;
        Function<String, Boolean> isUniqueInTrainees = u -> isUnique(user, u, traineeDAO.getTrainees());
        Function<String, Boolean> isUniqueInTrainers = u -> isUnique(user, u, trainerDAO.getTrainers());

        while (!isUniqueInTrainees.apply(username) || !isUniqueInTrainers.apply(username)) {
            username = base + counter++;
        }

        log.info(">>>> Generating username");
        return username;
    }

    private <T extends User> boolean isUnique(User targetUser, String username, List<T> users) {
        return users.stream().noneMatch(user -> user.getUsername().equals(username) && !user.equals(targetUser));
    }
}
