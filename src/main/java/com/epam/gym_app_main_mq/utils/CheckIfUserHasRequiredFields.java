package com.epam.gym_app_main_mq.utils;

import com.epam.gym_app_main_mq.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckIfUserHasRequiredFields {
    public boolean checkHasRequiredFields(User user) {
        if (user == null) {
            log.error("Customer is null");
            return false;
        }

        boolean valid = true;
        if (user.getFirstName() == null) {
            valid = false;
            log.error("First name cannot be null");
        }
        if (user.getLastName() == null) {
            valid = false;
            log.error("Last name cannot be null");
        }
        if (user.getUsername() == null) {
            valid = false;
            log.error("Username cannot be null");
        }
        if (user.getPassword() == null) {
            valid = false;
            log.error("Password cannot be null");
        }
        if (user.getIsActive() == null) {
            valid = false;
            log.error("isActive cannot be null");
        }

        return valid;
    }

    public boolean isInvalidUser(User user) {
        return !checkHasRequiredFields(user);
    }

    public User findUserByUsername(String username) {
        log.info("'findUserByUsername' method is not implemented in UserService");
        return null;
    }
}
