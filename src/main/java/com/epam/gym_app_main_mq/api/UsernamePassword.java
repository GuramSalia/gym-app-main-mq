package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class UsernamePassword {
    @NotBlank(message = "username not specified")
    @Size(min = 1, message = "username must not be blank")
    protected String username;
    @NotBlank(message = "password not specified")
    @Size(min = 1, message = "password must not be blank")
    protected String password;

    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsernamePassword{" +
                "username='" + username + '\'' +
                ", password=*****}";
    }
}
