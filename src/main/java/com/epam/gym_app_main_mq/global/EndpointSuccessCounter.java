package com.epam.gym_app_main_mq.global;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EndpointSuccessCounter {

    private final Counter user_login_get_requests_success_counter;
    private final Counter user_login_put_requests_success_counter;
    private final Counter user_logout_get_requests_success_counter;

    private final Counter create_user_trainee_post_requests_success_counter;
    private final Counter create_user_trainer_post_requests_success_counter;

    private final Counter trainee_get_requests_success_counter;
    private final Counter trainee_put_requests_success_counter;
    private final Counter trainee_delete_requests_success_counter;
    private final Counter trainee_update_trainers_list_requests_success_counter;
    private final Counter trainee_activate_patch_requests_success_counter;
    private final Counter trainee_deactivate_patch_requests_success_counter;

    private final Counter trainer_get_requests_success_counter;
    private final Counter trainer_put_requests_success_counter;
    private final Counter trainers_get_not_assigned_to_trainee_requests_success_counter;
    private final Counter trainer_activate_patch_requests_success_counter;
    private final Counter trainer_deactivate_patch_requests_success_counter;

    private final Counter training_post_requests_success_counter;
    private final Counter trainings_of_trainee_get_requests_success_counter;
    private final Counter trainings_of_trainer_get_requests_success_counter;

    private final Counter training_types_get_requests_success_counter;

    private final Map<String, Counter> counterMap;

    @Autowired
    public EndpointSuccessCounter(MeterRegistry meterRegistry) {
        this.user_login_get_requests_success_counter = Counter
                .builder("user_login_get_requests_success_counter")
                .description("number of successful hits: GET public/user/login")
                .register(meterRegistry);
        this.user_login_put_requests_success_counter = Counter
                .builder("user_login_put_requests_success_counter")
                .description("number of successful hits: PUT /user/login")
                .register(meterRegistry);
        this.user_logout_get_requests_success_counter = Counter
                .builder("user_logout_get_requests_success_counter")
                .description("number of successful hits: GET /user/logout")
                .register(meterRegistry);
        this.create_user_trainee_post_requests_success_counter = Counter
                .builder("create_user_trainee_post_requests_success_counter")
                .description("number of successful hits: POST public/create/trainee")
                .register(meterRegistry);
        this.create_user_trainer_post_requests_success_counter = Counter
                .builder("create_user_trainer_post_requests_success_counter")
                .description("number of successful hits: POST public/create/trainer")
                .register(meterRegistry);
        this.trainee_get_requests_success_counter = Counter
                .builder("trainee_get_requests_success_counter")
                .description("number of successful hits: GET /trainee-get")
                .register(meterRegistry);
        this.trainee_put_requests_success_counter = Counter
                .builder("trainee_put_requests_success_counter")
                .description("number of successful hits: PUT /trainee")
                .register(meterRegistry);
        this.trainee_delete_requests_success_counter = Counter
                .builder("trainee_delete_requests_success_counter")
                .description("number of successful hits: DELETE /trainee-delete")
                .register(meterRegistry);
        this.trainee_update_trainers_list_requests_success_counter = Counter
                .builder("trainee_update_trainers_list_requests_success_counter")
                .description("number of successful hits: PUT /trainee/update-trainers-list")
                .register(meterRegistry);
        this.trainee_activate_patch_requests_success_counter = Counter
                .builder("trainee_activate_patch_requests_success_counter")
                .description("number of successful hits: PATCH /trainee/activate")
                .register(meterRegistry);
        this.trainee_deactivate_patch_requests_success_counter = Counter
                .builder("trainee_deactivate_patch_requests_success_counter")
                .description("number of successful hits: PATCH /trainee/deactivate")
                .register(meterRegistry);
        this.trainer_get_requests_success_counter = Counter
                .builder("trainer_get_requests_success_counter")
                .description("number of successful hits: GET /trainer-get")
                .register(meterRegistry);
        this.trainer_put_requests_success_counter = Counter
                .builder("trainer_put_requests_success_counter")
                .description("number of successful hits: PUT /trainer")
                .register(meterRegistry);
        this.trainers_get_not_assigned_to_trainee_requests_success_counter = Counter
                .builder("trainers_get_not_assigned_to_trainee_requests_success_counter")
                .description("number of successful hits: GET /trainers/get-not-assigned-to-trainee")
                .register(meterRegistry);
        this.trainer_activate_patch_requests_success_counter = Counter
                .builder("trainer_activate_patch_requests_success_counter")
                .description("number of successful hits: PATCH /trainer/activate")
                .register(meterRegistry);
        this.trainer_deactivate_patch_requests_success_counter = Counter
                .builder("trainer_deactivate_patch_requests_success_counter")
                .description("number of successful hits: PATCH /trainer/deactivate")
                .register(meterRegistry);
        this.training_post_requests_success_counter = Counter
                .builder("training_post_requests_success_counter")
                .description("number of successful hits: POST /training")
                .register(meterRegistry);
        this.trainings_of_trainee_get_requests_success_counter = Counter
                .builder("trainings_of_trainee_get_requests_success_counter")
                .description("number of successful hits: GET /trainings/of-trainee")
                .register(meterRegistry);
        this.trainings_of_trainer_get_requests_success_counter = Counter
                .builder("trainings_of_trainer_get_requests_success_counter")
                .description("number of successful hits: GET /trainings/of-trainer")
                .register(meterRegistry);
        this.training_types_get_requests_success_counter = Counter
                .builder("training_types_get_requests_success_counter")
                .description("number of successful hits: GET /training-types")
                .register(meterRegistry);
        this.counterMap = getCounterMap();
    }

    private Map<String, Counter> getCounterMap() {
        Map<String, Counter> countersMap = new HashMap<String, Counter>();
        countersMap.put("GET/public/user/login", user_login_get_requests_success_counter);
        countersMap.put("GET/user/logout", user_logout_get_requests_success_counter);
        countersMap.put("PUT/user/login", user_login_put_requests_success_counter);
        countersMap.put("POST/public/create/trainee", create_user_trainee_post_requests_success_counter);
        countersMap.put("POST/public/create/trainer", create_user_trainer_post_requests_success_counter);
        countersMap.put("GET/trainee-get", trainee_get_requests_success_counter);
        countersMap.put("PUT/trainee", trainee_put_requests_success_counter);
        countersMap.put("DELETE/trainee-delete", trainee_delete_requests_success_counter);
        countersMap.put("PUT/trainee/update-trainers-list", trainee_update_trainers_list_requests_success_counter);
        countersMap.put("PATCH/trainee/activate", trainee_activate_patch_requests_success_counter);
        countersMap.put("PATCH/trainee/deactivate", trainee_deactivate_patch_requests_success_counter);
        countersMap.put("GET/trainer-get", trainer_get_requests_success_counter);
        countersMap.put("PUT/trainer", trainer_put_requests_success_counter);
        countersMap.put("GET/trainers/get-not-assigned-to-trainee",
                        trainers_get_not_assigned_to_trainee_requests_success_counter);
        countersMap.put("PATCH/trainer/activate", trainer_activate_patch_requests_success_counter);
        countersMap.put("PATCH/trainer/deactivate", trainer_deactivate_patch_requests_success_counter);
        countersMap.put("POST/training", training_post_requests_success_counter);
        countersMap.put("GET/trainings/of-trainee", trainings_of_trainee_get_requests_success_counter);
        countersMap.put("GET/trainings/of-trainer", trainings_of_trainer_get_requests_success_counter);
        countersMap.put("GET/training-types", training_types_get_requests_success_counter);
        return countersMap;
    }

    public void incrementCounter(String methodAndUri) {
        log.info("\n\n>> from EndpointSuccessCounter >> method/uri: " + methodAndUri + "\n");
        Counter counter = this.counterMap.get(methodAndUri);
        log.info("\n\n>> "+counter.toString()+"\n");
        if (counter == null) {
            log.info("\n\n>> no counter found for method/uri: " + methodAndUri + "\n");
            return;
        }
        counter.increment();
    }
}
