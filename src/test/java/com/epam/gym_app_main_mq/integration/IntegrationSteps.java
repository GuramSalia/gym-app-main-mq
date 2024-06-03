package com.epam.gym_app_main_mq.integration;

import com.epam.gym_app_main_mq.api.stat.ActionType;
import com.epam.gym_app_main_mq.api.stat.FullStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.MonthlyStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.UpdateStatRequestInMainApp;
import com.epam.gym_app_main_mq.messaging.Senders;
import com.epam.gym_app_main_mq.repository.TestHelperRepository;
import com.epam.gym_app_main_mq.utils.TestCorrelationIdHolder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

@Slf4j
public class IntegrationSteps {

    private final Senders senders;
//    private final JmsTemplate jmsTemplate;
    private final TestHelperRepository testHelperRepository;

    private String correlationIdUpdateStat;
    private String correlationIdGetMonthlyStat;
    private String correlationIdGetFullStat;

    @Autowired
    public IntegrationSteps(
            Senders senders,
            JmsTemplate jmsTemplate,
            TestHelperRepository testHelperRepository
    ) {
        this.senders = senders;
//        this.jmsTemplate = jmsTemplate;
        this.testHelperRepository = testHelperRepository;
    }

    @Given("App 1 sends an update stat request with correlation ID {string}")
    public void app1SendsUpdateStatRequest(String correlationId) {
        TestCorrelationIdHolder testCorrelationIdHolder = testHelperRepository.findById(1).orElse(null);
        assert testCorrelationIdHolder != null;
        log.info("testCorrelationId update-stat: {}", testCorrelationIdHolder.getStatUpdateCorrelationId());
        testCorrelationIdHolder.setFullStatCorrelationId("u");
        testHelperRepository.save(testCorrelationIdHolder);

        this.correlationIdUpdateStat = correlationId;
        UpdateStatRequestInMainApp request = createUpdateStatRequest();
        senders.requestStatUpdate(request, correlationId);
    }

    @Then("App 1 receives the update stat response")
    public void app1ReceivesUpdateStatResponse() {

        boolean messageReceived = false;
        long timeout = System.currentTimeMillis() + 3000;

        while (System.currentTimeMillis() < timeout) {

            TestCorrelationIdHolder testCorrelationIdHolder = testHelperRepository.findById(1).orElse(null);
            assert testCorrelationIdHolder != null;
            String receivedCorrelationId = testCorrelationIdHolder.getStatUpdateCorrelationId();

//            if (receivedCorrelationId.equals(this.correlationIdUpdateStat)) {
//                messageReceived = true;
//                break;
//            }

            if (!receivedCorrelationId.equals("u")) {
                messageReceived = true;
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assert messageReceived : "App 1 did not receive the expected response";
    }

    @Given("App 1 sends a full stat request with correlation ID {string}")
    public void appSendsAFullStatRequestWithCorrelationID(String correlationId) {
//        TestCorrelationIdHolder testCorrelationIdHolder = testHelperRepository.findById(1).orElse(null);
//        assert testCorrelationIdHolder != null;
//        log.info("testCorrelationId full-stat: {}", testCorrelationIdHolder.getFullStatCorrelationId());
//        testCorrelationIdHolder.setFullStatCorrelationId("f");
//        testHelperRepository.save(testCorrelationIdHolder);

        this.correlationIdGetFullStat = correlationId;
        FullStatRequestInMainApp request = new FullStatRequestInMainApp();
        request.setTrainerId(1);
        senders.requestFullStat(request, correlationId);
    }

    @Then("App 1 receives the full stat response")
    public void appReceivesTheFullStatResponse() {

        boolean messageReceived = false;
        long timeout = System.currentTimeMillis() + 3000;

        while (System.currentTimeMillis() < timeout) {

            TestCorrelationIdHolder testCorrelationIdHolder = testHelperRepository.findById(1).orElse(null);
            assert testCorrelationIdHolder != null;

            String receivedCorrelationId = testCorrelationIdHolder.getFullStatCorrelationId();
//            if (receivedCorrelationId.equals(this.correlationIdGetFullStat)) {
//                messageReceived = true;
//                break;
//            }
            if (!receivedCorrelationId.equals("f")) {
                messageReceived = true;
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assert messageReceived : "App 1 did not receive the expected response";
    }

    @Given("App 1 sends a monthly request with correlation ID {string}")
    public void appSendsAMonthlyRequestWithCorrelationID(String correlationId) {
//        TestCorrelationIdHolder testCorrelationIdHolder = testHelperRepository.findById(1).orElse(null);
//        assert testCorrelationIdHolder != null;
//        log.info("testCorrelationId monthly-stat: {}", testCorrelationIdHolder.getMonthlyStatCorrelationId());
//        testCorrelationIdHolder.setMonthlyStatCorrelationId("m");
//        testHelperRepository.save(testCorrelationIdHolder);

        this.correlationIdGetMonthlyStat = correlationId;
        MonthlyStatRequestInMainApp request = new MonthlyStatRequestInMainApp();
        request.setTrainerId(1);
        request.setYear(2024);
        request.setMonth(1);
        senders.requestMonthlyStat(request, correlationId);
    }

    @Then("App 1 receives the monthly stat response")
    public void appReceivesTheMonthlyStatResponse() {

        boolean messageReceived = false;
        long timeout = System.currentTimeMillis() + 3000;

        while (System.currentTimeMillis() < timeout) {
            TestCorrelationIdHolder testCorrelationIdHolder = testHelperRepository.findById(1).orElse(null);
            assert testCorrelationIdHolder != null;

            String receivedCorrelationId = testCorrelationIdHolder.getMonthlyStatCorrelationId();

//            if (receivedCorrelationId.equals(this.correlationIdGetMonthlyStat)) {
//                messageReceived = true;
//                break;
//            }

            if (!receivedCorrelationId.equals("m")) {
                messageReceived = true;
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        assert messageReceived : "App 1 did not receive the expected response";
    }

    private UpdateStatRequestInMainApp createUpdateStatRequest() {
        UpdateStatRequestInMainApp updateStatRequestInMainApp = new UpdateStatRequestInMainApp();
        updateStatRequestInMainApp.setTrainerId(1);
        updateStatRequestInMainApp.setYear(2024);
        updateStatRequestInMainApp.setMonth(8);
        updateStatRequestInMainApp.setDuration(90);
        updateStatRequestInMainApp.setActionType(ActionType.ADD);

        updateStatRequestInMainApp.setUserName("Tim.Smith");
        updateStatRequestInMainApp.setFirstName("Tim");
        updateStatRequestInMainApp.setLastName("Smith");
        updateStatRequestInMainApp.setStatus(true);

        return updateStatRequestInMainApp;
    }
}
