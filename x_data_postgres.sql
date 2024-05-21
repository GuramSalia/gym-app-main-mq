-- Insert Trainers
INSERT INTO gym_users (first_name, last_name, username, password, is_active, failed_login_attempts, block_status, block_start_time) VALUES
('Tim', 'Smith', 'Tim.Smith', '$2a$10$A1s2lroG4kwrn/vU3OqWV.tcrYhZ3BVD0N1m8mfdZVMAQalmOlw4S', TRUE, 0, FALSE, NULL),
('Sam', 'Jones', 'Sam.Jones', '$2a$10$A1s2lroG4kwrn/vU3OqWV.tcrYhZ3BVD0N1m8mfdZVMAQalmOlw4S', TRUE, 0, FALSE, NULL),
('John', 'Doe', 'John.Doe', '$2a$10$A1s2lroG4kwrn/vU3OqWV.tcrYhZ3BVD0N1m8mfdZVMAQalmOlw4S', TRUE, 0, FALSE, NULL),
('Jane', 'Smith', 'Jane.Smith', '$2a$10$A1s2lroG4kwrn/vU3OqWV.tcrYhZ3BVD0N1m8mfdZVMAQalmOlw4S', TRUE, 0, FALSE, NULL);

INSERT INTO trainees (date_of_birth, address, user_id) VALUES
(DATE '2000-02-21', 'address of TRAINEE 1', 3),
(DATE '2000-01-21', 'address of TRAINEE 2', 4);

-- Insert TrainingTypes
INSERT INTO TRAINING_TYPES (TRAINING_TYPE_NAME) VALUES
('CARDIO'),
('STRENGTH'),
('HIIT'),
('YOGA'),
('PILATES'),
('GROUP'),
('PERSONAL');

-- Insert Trainers
INSERT INTO trainers (user_id, training_type_id) VALUES
(1, 1), -- Tim is a CARDIO trainer
(2, 2); -- Sam is a STRENGTH trainer

-- Insert Trainings
INSERT INTO trainings (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES
(3, 1, 'Cardio Workout', 1, DATE '2024-02-21', 60),
(4, 2, 'Strength Training', 2, DATE '2024-02-22', 45);

INSERT INTO trainees_trainers (trainee_id, trainer_id) VALUES
(3, 1),
(3, 2),
(4, 1);
