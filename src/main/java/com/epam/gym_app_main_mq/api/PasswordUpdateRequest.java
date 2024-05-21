package com.epam.gym_app_main_mq.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordUpdateRequest extends UsernamePassword {
    @NotBlank(message = "newPassword not specified")
    @Size(min = 1, message = "newPassword must not be blank")
    private String newPassword;

    public PasswordUpdateRequest(String username, String password, String newPassword) {
        super(username, password);
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "PasswordUpdateRequest{" +
                "newPassword='" + newPassword + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
