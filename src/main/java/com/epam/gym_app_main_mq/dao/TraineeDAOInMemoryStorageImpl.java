package com.epam.gym_app_main_mq.dao;

import com.epam.gym_app_main_mq.config.storage.InMemoryStorage;
import com.epam.gym_app_main_mq.config.storage.idgeneration.UserIdForInMemoryStorage;
import com.epam.gym_app_main_mq.model.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Profile("dev_in_memory")
@Repository("TRAINEE_IN_MEMORY")
public class TraineeDAOInMemoryStorageImpl implements TraineeDAO {
//    private final Map<Integer, Trainee> trainees;
    private final InMemoryStorage storage;

    @Autowired
    public TraineeDAOInMemoryStorageImpl(InMemoryStorage storage) {
        log.info("\n\nstorage in TraineeDAOInMemoryStorageImpl: " + storage+"\n");
        this.storage = storage;
        log.info("\n\ntrainees in StorageInitializer : " + storage.getTrainees().hashCode()+"\n");
    }

    public Optional<Trainee> create(Trainee trainee) {
        log.info("<<>>>> TraineeDAOInMemoryStorageImpl create()");

//        int id = trainee.getUserId();
        int id = UserIdForInMemoryStorage.getNewId();
        trainee.setUserId(id);
        if (storage.getTrainees().containsKey(id)) {
            log.error("Trainee with id {} already exists", id);
            return Optional.empty();
        } else {
            storage.getTrainees().put(id, trainee);
            return Optional.of(storage.getTrainees().get(id));
        }
    }

    public Optional<Trainee> update(Trainee trainee) {
        int id = trainee.getUserId();
        if (storage.getTrainees().containsKey(id)) {
            storage.getTrainees().put(id, trainee);
            return Optional.of(storage.getTrainees().get(id));
        } else {
            log.error("Trainee with id {} does not exist", id);
            return Optional.empty();
        }
    }

    public boolean delete(Trainee trainee) {
        storage.getTrainees().remove(trainee.getUserId());
        // when connected to DB need to check if deletion was successful in DB.
        return !storage.getTrainees().containsKey(trainee.getUserId());
    }

    public Optional<Trainee> getById(int id) {return Optional.ofNullable(storage.getTrainees().get(id));}

    @Override
    public List<Trainee> getTrainees() {return new ArrayList<>(storage.getTrainees().values());}

    @Override
    public Optional<Trainee> findByUsername(String username) {
        log.info("'findByUsername' from TraineeDaoInMemoryStorageImpl");
        for (Trainee currentTrainee : storage.getTrainees().values()) {
            if (currentTrainee.getUsername().equals(username)) {
                return Optional.of(currentTrainee);
            }
        }

        return Optional.empty();

    }

    @Override
    public Optional<Trainee> findByUsernameAndPassword(String username, String password) {
        log.info("\n\ntrainees in StorageInitializer>findByUsernameAndPassword : " + storage.getTrainees().hashCode()+"\n");
        log.info("\n\ntrainees: \n" + storage.getTrainees().values());
        log.info("\n\ntrainees: \n" + storage.getTrainees().values());

        log.info("\n\n'findByUsernameAndPassword' from TraineeDaoInMemoryStorageImpl\n");
        log.info("\n\nREQUESTED username='" + username +", password=*****\n");

        for (Trainee currentTrainee : storage.getTrainees().values()) {
            String traineeUsername = currentTrainee.getUsername();
            String traineePassword = currentTrainee.getPassword();
            log.info("\n\ntraineeUsername=" + traineeUsername +", traineePassword=" + traineePassword+"\n");
            if (currentTrainee.getUsername().equals(username) && currentTrainee.getPassword().equals(password)) {
                return Optional.of(currentTrainee);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Trainee> findByUsernameWithQuery(String username) {
        log.info("'findWithUsername' from TraineeDaoInMemoryStorageImpl");
        return findByUsername(username);
    }
}
