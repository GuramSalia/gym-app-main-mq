package com.epam.gym_app_main_mq.utils;

import com.epam.gym_app_main_mq.model.User;

import java.util.Optional;

public interface ImplementsFindByUsernameAndPassword {
    public <T extends User> Optional<T> findByUsernameAndPassword(String username, String password);

    public <T extends User> Optional<T> findByUsername(String username);
}
