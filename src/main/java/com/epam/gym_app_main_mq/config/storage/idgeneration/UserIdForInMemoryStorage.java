package com.epam.gym_app_main_mq.config.storage.idgeneration;

public class UserIdForInMemoryStorage {
    private static int userId = 5;
    public static int getNewId() {return userId++;}
}
