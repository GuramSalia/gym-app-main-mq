CREATE TABLE IF NOT EXISTS TRAINING_TYPES (
    TRAINING_TYPE_ID SERIAL PRIMARY KEY,
    TRAINING_TYPE_NAME VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS GYM_USERS (
    USER_ID SERIAL PRIMARY KEY,
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255) NOT NULL,
    USERNAME VARCHAR(255) NOT NULL UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL,
    IS_ACTIVE BOOLEAN NOT NULL,
    FAILED_LOGIN_ATTEMPTS INT DEFAULT 0,
    BLOCK_STATUS BOOLEAN DEFAULT false,
    BLOCK_START_TIME DATETIME DEFAULT NULL
);


CREATE TABLE IF NOT EXISTS TRAINEES (
    USER_ID INT PRIMARY KEY REFERENCES GYM_USERS(USER_ID) ON DELETE CASCADE,
    DATE_OF_BIRTH DATE,
    ADDRESS VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS TRAINERS (
    USER_ID INT PRIMARY KEY REFERENCES GYM_USERS(USER_ID) ON DELETE CASCADE,
    TRAINING_TYPE_ID INT,
    FOREIGN KEY (TRAINING_TYPE_ID) REFERENCES TRAINING_TYPES(TRAINING_TYPE_ID)
);

CREATE TABLE IF NOT EXISTS TRAININGS (
    TRAINING_ID SERIAL PRIMARY KEY,
    TRAINEE_ID INT,
    TRAINER_ID INT,
    TRAINING_NAME VARCHAR(255) NOT NULL,
    TRAINING_TYPE_ID INT,
    TRAINING_DATE DATE NOT NULL,
    TRAINING_DURATION INT NOT NULL,
    FOREIGN KEY (TRAINING_TYPE_ID) REFERENCES TRAINING_TYPES(TRAINING_TYPE_ID),
    FOREIGN KEY (TRAINER_ID) REFERENCES TRAINERS(USER_ID),
    FOREIGN KEY (TRAINEE_ID) REFERENCES TRAINEES(USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TRAINEES_TRAINERS (
    TRAINEE_ID INT,
    TRAINER_ID INT,
    PRIMARY KEY (TRAINEE_ID, TRAINER_ID),
    FOREIGN KEY (TRAINEE_ID) REFERENCES TRAINEES(USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (TRAINER_ID) REFERENCES TRAINERS(USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TOKENS (
    TOKEN_ID SERIAL PRIMARY KEY,
    TOKEN VARCHAR(255) NOT NULL,
    TOKEN_TYPE VARCHAR(50) NOT NULL,
    REVOKED BOOLEAN NOT NULL,
    EXPIRED BOOLEAN NOT NULL,
    USERNAME VARCHAR(255) NOT NULL,
    FOREIGN KEY (USERNAME) REFERENCES GYM_USERS(USERNAME) ON DELETE CASCADE
);

-- TEST CORRELATION ID HOLDER
CREATE TABLE IF NOT EXISTS TEST_HELPER (
    HELPER_ID SERIAL PRIMARY KEY,
    UPDATE_STAT VARCHAR(255) NOT NULL,
    MONTHLY_STAT VARCHAR(255) NOT NULL,
    FULL_STAT VARCHAR(255) NOT NULL
);


