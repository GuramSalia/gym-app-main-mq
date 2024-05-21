package com.epam.gym_app_main_mq.utils;

import com.epam.gym_app_main_mq.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CheckIfUsernameExists {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public <T extends ImplementsFindByUsernameAndPassword, U extends User> boolean usernameExists(
            T t, String username, String password
    ) {

        U userInDb;
        Optional<U> uOptional = t.findByUsername(username);
        if (uOptional.isEmpty()) {
            log.error("wrong username or password");
            return false;
        }

        userInDb = uOptional.get();
        return passwordEncoder.matches(password, userInDb.getPassword());
    }
}
