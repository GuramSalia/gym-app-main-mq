package com.epam.gym_app_main_mq.global;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EndpointFailCounter {

    private final Counter user_login_get_requests_fail_counter;
    private final Counter user_login_put_requests_fail_counter;
    private final Counter user_logout_get_requests_fail_counter;

    private final Counter create_user_trainee_post_requests_fail_counter;
    private final Counter create_user_trainer_post_requests_fail_counter;

    private final Counter trainee_get_requests_fail_counter;
    private final Counter trainee_put_requests_fail_counter;
    private final Counter trainee_delete_requests_fail_counter;
    private final Counter trainee_update_trainers_list_requests_fail_counter;
    private final Counter trainee_activate_patch_requests_fail_counter;
    private final Counter trainee_deactivate_patch_requests_fail_counter;

    private final Counter trainer_get_requests_fail_counter;
    private final Counter trainer_put_requests_fail_counter;
    private final Counter trainers_get_not_assigned_to_trainee_requests_fail_counter;
    private final Counter trainer_activate_patch_requests_fail_counter;
    private final Counter trainer_deactivate_patch_requests_fail_counter;

    private final Counter training_post_requests_fail_counter;
    private final Counter trainings_of_trainee_get_requests_fail_counter;
    private final Counter trainings_of_trainer_get_requests_fail_counter;

    private final Counter training_types_get_requests_fail_counter;

    private final Map<String, Counter> counterMap;

    public EndpointFailCounter(MeterRegistry meterRegistry) {
        this.user_login_get_requests_fail_counter = Counter
                .builder("user_login_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET public/user/login")
                .register(meterRegistry);
        this.user_logout_get_requests_fail_counter = Counter
                .builder("user_logout_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET user/logout")
                .register(meterRegistry);
        this.user_login_put_requests_fail_counter = Counter
                .builder("user_login_put_requests_fail_counter")
                .description("number of unsuccessful hits: PUT /user/login")
                .register(meterRegistry);
        this.create_user_trainee_post_requests_fail_counter = Counter
                .builder("create_user_trainee_post_requests_fail_counter")
                .description("number of unsuccessful hits: POST public/create/trainee")
                .register(meterRegistry);
        this.create_user_trainer_post_requests_fail_counter = Counter
                .builder("create_user_trainer_post_requests_fail_counter")
                .description("number of unsuccessful hits: POST public/create/trainer")
                .register(meterRegistry);
        this.trainee_get_requests_fail_counter = Counter
                .builder("trainee_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET /trainee-get")
                .register(meterRegistry);
        this.trainee_put_requests_fail_counter = Counter
                .builder("trainee_put_requests_fail_counter")
                .description("number of unsuccessful hits: PUT /trainee")
                .register(meterRegistry);
        this.trainee_delete_requests_fail_counter = Counter
                .builder("trainee_delete_requests_fail_counter")
                .description("number of unsuccessful hits: DELETE /trainee-delete")
                .register(meterRegistry);
        this.trainee_update_trainers_list_requests_fail_counter = Counter
                .builder("trainee_update_trainers_list_requests_fail_counter")
                .description("number of unsuccessful hits: PUT /trainee/update-trainers-list")
                .register(meterRegistry);
        this.trainee_activate_patch_requests_fail_counter = Counter
                .builder("trainee_activate_patch_requests_fail_counter")
                .description("number of unsuccessful hits: PATCH /trainee/activate")
                .register(meterRegistry);
        this.trainee_deactivate_patch_requests_fail_counter = Counter
                .builder("trainee_deactivate_patch_requests_fail_counter")
                .description("number of unsuccessful hits: PATCH /trainee/deactivate")
                .register(meterRegistry);
        this.trainer_get_requests_fail_counter = Counter
                .builder("trainer_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET /trainer-get")
                .register(meterRegistry);
        this.trainer_put_requests_fail_counter = Counter
                .builder("trainer_put_requests_fail_counter")
                .description("number of unsuccessful hits: PUT /trainer")
                .register(meterRegistry);
        this.trainers_get_not_assigned_to_trainee_requests_fail_counter = Counter
                .builder("trainers_get_not_assigned_to_trainee_requests_fail_counter")
                .description("number of unsuccessful hits: GET /trainers/get-not-assigned-to-trainee")
                .register(meterRegistry);
        this.trainer_activate_patch_requests_fail_counter = Counter
                .builder("trainer_activate_patch_requests_fail_counter")
                .description("number of unsuccessful hits: PATCH /trainer/activate")
                .register(meterRegistry);
        this.trainer_deactivate_patch_requests_fail_counter = Counter
                .builder("trainer_deactivate_patch_requests_fail_counter")
                .description("number of unsuccessful hits: PATCH /trainer/deactivate")
                .register(meterRegistry);
        this.training_post_requests_fail_counter = Counter
                .builder("training_post_requests_fail_counter")
                .description("number of unsuccessful hits: POST /training")
                .register(meterRegistry);
        this.trainings_of_trainee_get_requests_fail_counter = Counter
                .builder("trainings_of_trainee_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET /trainings/of-trainee")
                .register(meterRegistry);
        this.trainings_of_trainer_get_requests_fail_counter = Counter
                .builder("trainings_of_trainer_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET /trainings/of-trainer")
                .register(meterRegistry);
        this.training_types_get_requests_fail_counter = Counter
                .builder("training_types_get_requests_fail_counter")
                .description("number of unsuccessful hits: GET /training-types")
                .register(meterRegistry);
        this.counterMap = getCounterMap();
    }

    private Map<String, Counter> getCounterMap() {
        Map<String, Counter> countersMap = new HashMap<String, Counter>();
        countersMap.put("GET/public/user/login", user_login_get_requests_fail_counter);
        countersMap.put("GET/user/logout", user_logout_get_requests_fail_counter);
        countersMap.put("PUT/user/login", user_login_put_requests_fail_counter);
        countersMap.put("POST/public/create/trainee", create_user_trainee_post_requests_fail_counter);
        countersMap.put("POST/public/create/trainer", create_user_trainer_post_requests_fail_counter);
        countersMap.put("GET/trainee-get", trainee_get_requests_fail_counter);
        countersMap.put("PUT/trainee", trainee_put_requests_fail_counter);
        countersMap.put("DELETE/trainee-delete", trainee_delete_requests_fail_counter);
        countersMap.put("PUT/trainee/update-trainers-list", trainee_update_trainers_list_requests_fail_counter);
        countersMap.put("PATCH/trainee/activate", trainee_activate_patch_requests_fail_counter);
        countersMap.put("PATCH/trainee/deactivate", trainee_deactivate_patch_requests_fail_counter);
        countersMap.put("GET/trainer-get", trainer_get_requests_fail_counter);
        countersMap.put("PUT/trainer", trainer_put_requests_fail_counter);
        countersMap.put("GET/trainers/get-not-assigned-to-trainee",
                        trainers_get_not_assigned_to_trainee_requests_fail_counter);
        countersMap.put("PATCH/trainer/activate", trainer_activate_patch_requests_fail_counter);
        countersMap.put("PATCH/trainer/deactivate", trainer_deactivate_patch_requests_fail_counter);
        countersMap.put("POST/training", training_post_requests_fail_counter);
        countersMap.put("GET/trainings/of-trainee", trainings_of_trainee_get_requests_fail_counter);
        countersMap.put("GET/trainings/of-trainer", trainings_of_trainer_get_requests_fail_counter);
        countersMap.put("GET/training-types", training_types_get_requests_fail_counter);
        return countersMap;
    }

    public Counter getCounterByMethodAndUri(String method, String uri) {
        return counterMap.getOrDefault(method + uri, null);
    }
}
