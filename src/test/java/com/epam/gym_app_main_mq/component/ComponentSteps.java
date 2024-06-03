package com.epam.gym_app_main_mq.component;

import com.epam.gym_app_main_mq.api.AuthenticationResponse;
import com.epam.gym_app_main_mq.api.TrainingRegistrationRequest;
import com.epam.gym_app_main_mq.api.TrainingsByTraineeRequest;
import com.epam.gym_app_main_mq.api.UsernamePassword;
import com.epam.gym_app_main_mq.api.stat.DeleteTrainingRequest;
import com.epam.gym_app_main_mq.api.stat.FullStatRequestInMainApp;
import com.epam.gym_app_main_mq.api.stat.MonthlyStatRequestInMainApp;
import com.epam.gym_app_main_mq.controller.TrainingController;
import com.epam.gym_app_main_mq.controller.UserManagementController;
import com.epam.gym_app_main_mq.model.TrainingType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
public class ComponentSteps {

    private final TrainingController trainingController;
    private final UserManagementController userManagementController;

    @Autowired
    public ComponentSteps(TrainingController trainingController, UserManagementController userManagementController) {
        this.trainingController = trainingController;
        this.userManagementController = userManagementController;
    }

    private MockMvc mockMvc;
    private TrainingRegistrationRequest trainingRegistrationRequest;
    private DeleteTrainingRequest deleteTrainingRequest;
    private MonthlyStatRequestInMainApp monthlyStatRequestInMainApp;
    private FullStatRequestInMainApp fullStatRequestInMainApp;
    private TrainingsByTraineeRequest trainingsByTraineeRequest;

    private UsernamePassword loginRequest;
    private String token;

    private UsernamePassword invalidLoginRequest;
    private DeleteTrainingRequest deleteTrainingRequestWithInvalidId;
    private String invalidToken;

    @Given("a login request with valid credentials")
    public void aLoginRequestWithValidCredentials() {
        loginRequest = new UsernamePassword("John.Doe", "123");
    }

    @When("the client gets the login request to {string}")
    public void theClientPostsTheLoginRequestTo(String url) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userManagementController).build();
        String loginJson = asJsonString(loginRequest);
        MvcResult result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON).content(loginJson))
                                  .andExpect(status().isOk())
                                  .andReturn();

        String response = result.getResponse().getContentAsString();
        token = extractTokenFromResponse(response);
    }

    @Then("the token is stored")
    public void theTokenIsStored() {
        assert token != null;
    }

    @Given("a training registration request")
    public void aTrainingRegistrationRequest() {
        String username = "John.Doe";
        String password = "123";
        String trainee = "John.Doe";
        String trainer = "Sam.Jones";
        String trainingName = "new training added for testing via cucumber";
        String dateString = "2024-05-21";
        int duration = 120;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateString);
            trainingRegistrationRequest = new TrainingRegistrationRequest(
                    username, password, trainee, trainer, trainingName, date, duration
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Given("a delete training request")
    public void aDeleteTrainingRequest() {
        deleteTrainingRequest = new DeleteTrainingRequest();
        deleteTrainingRequest.setTrainingId(2);
    }

    @Given("a monthly stat request")
    public void aMonthlyStatRequest() {
        monthlyStatRequestInMainApp = new MonthlyStatRequestInMainApp();
        monthlyStatRequestInMainApp.setYear(2024);
        monthlyStatRequestInMainApp.setMonth(2);
        monthlyStatRequestInMainApp.setTrainerId(1);
    }

    @Given("a full stat request")
    public void aFullStatRequest() {
        fullStatRequestInMainApp = new FullStatRequestInMainApp();
        fullStatRequestInMainApp.setTrainerId(1);
    }

    @Given("a request for trainings by trainee")
    public void aRequestForTrainingsByTrainee() {
        String username = "John.Doe";
        String password = "123";
        String dateFromString = "1999-02-21";
        String dateToString = "2040-02-21";
        String trainerUsername = "Tim.Smith";
        TrainingType trainingType = new TrainingType("CARDIO");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateFrom = sdf.parse(dateFromString);
            Date dateTo = sdf.parse(dateToString);
            java.sql.Date sqlDateFrom = new java.sql.Date(dateFrom.getTime());
            java.sql.Date sqlDateTo = new java.sql.Date(dateTo.getTime());

            trainingsByTraineeRequest = new TrainingsByTraineeRequest(
                    username, password, sqlDateFrom, sqlDateTo, trainerUsername, trainingType
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @When("the client posts the request to {string} with the token")
    public void theClientPostsTheRequestToWithTheToken(String url) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        mockMvc.perform(post(url)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(trainingRegistrationRequest)))
               .andExpect(status().isCreated());
    }

    @When("the client deletes the request to {string} with the token")
    public void theClientDeletesTheRequestToWithTheToken(String url) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        mockMvc.perform(delete(url)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(deleteTrainingRequest)));
        //               .andExpect(status().isNoContent());
    }

    @When("the client gets the request to {string} with the token")
    public void theClientGetsTheRequestToWithTheToken(String url) throws Exception {
        Object request;
        if (url.contains("monthly-stat")) {
            request = monthlyStatRequestInMainApp;
        } else if (url.contains("full-stat")) {
            request = fullStatRequestInMainApp;
        } else {
            request = trainingsByTraineeRequest;
        }
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        mockMvc.perform(get(url)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(request)));
    }

    @Then("the response status should be {int} for login")
    public void theResponseStatusShouldBeForLoginEndpoint(int expectedStatus) throws Exception {
        mockMvc.perform(
                       get("/gym-app/public/user/login")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(asJsonString(loginRequest)))
               .andExpect(status().is(expectedStatus));
    }

    @Then("the response status should be {int} for creating training")
    public void theResponseStatusShouldBeForTrainingEndpoint(int expectedStatus) throws Exception {
        mockMvc.perform(post("/gym-app/training")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(trainingRegistrationRequest)))
               .andExpect(status().is(expectedStatus));
    }

    @Then("the response status should be {int} for deleting training")
    public void theResponseStatusShouldBeForDeletingTraining(int expectedStatus) throws Exception {
        deleteTrainingRequest.setTrainingId(1);
        mockMvc.perform(delete("/gym-app/training")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(deleteTrainingRequest)))
               .andExpect(status().is(expectedStatus));
    }

    @Then("the response status should be {int} for monthly stat requests")
    public void theResponseStatusShouldBeForMonthlyStatEndpoint(int expectedStatus) throws Exception {
        mockMvc.perform(get("/gym-app/trainings/monthly-stat")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(monthlyStatRequestInMainApp)))
               .andExpect(status().is(expectedStatus));
    }

    @Then("the response status should be {int} for full stat requests")
    public void theResponseStatusShouldBeForFullStatRequests(int expectedStatus) throws Exception {
        mockMvc.perform(get("/gym-app/trainings/full-stat")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(fullStatRequestInMainApp)))
               .andExpect(status().is(expectedStatus));
    }

    @Then("the response status should be {int} for trainee training")
    public void theResponseStatusShouldBeForTraineeTraining(int expectedStatus) throws Exception {

        mockMvc.perform(get("/trainings/of-trainee")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(trainingsByTraineeRequest)))
               .andExpect(status().is(expectedStatus));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String extractTokenFromResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AuthenticationResponse authenticationResponse = mapper.readValue(response, AuthenticationResponse.class);
            return authenticationResponse.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Given("a login request with invalid credentials")
    public void aLoginRequestWithInvalidCredentials() {
        invalidLoginRequest = new UsernamePassword("invalidUsername", "invalidPassword");
    }

    @When("the client gets the login request to {string} with invalid credentials")
    public void theClientGetsTheLoginRequestToWithInvalidCredentials(String url) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userManagementController).build();
        String loginJson = asJsonString(loginRequest);
        MvcResult result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON).content(loginJson))
                                  //                                  .andExpect(status().isOk())
                                  .andReturn();

        result.getResponse().getContentAsString();
    }

    @Then("the response status should be {int} for invalid login")
    public void theResponseStatusShouldBeForInvalidLogin(int expectedStatus) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userManagementController).build();
        mockMvc.perform(get("/gym-app/public/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(invalidLoginRequest)))
               .andExpect(status().is(expectedStatus));
    }

    @Given("a delete training request for a non-existing training ID")
    public void aDeleteTrainingRequestForANonExistingTrainingID() {
        deleteTrainingRequestWithInvalidId = new DeleteTrainingRequest();
        deleteTrainingRequestWithInvalidId.setTrainingId(1000000);
    }

    @Then("the response status should be {int} for not found training")
    public void theResponseStatusShouldBeForNotFoundTraining(int expectedStatus) {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        try {
            mockMvc.perform(delete("/gym-app/training")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(deleteTrainingRequestWithInvalidId)))
                   .andExpect(status().is(expectedStatus));
        } catch (Exception expected) {
            // expected
        }
    }

    @Given("a request to get trainings with an invalid or expired token")
    public void aRequestToGetTrainingsWithAnInvalidOrExpiredToken() {
        invalidToken = "invalidToken";
    }

    @When("the client posts the request to {string} with an invalid token")
    public void theClientPostsTheRequestToWithAnInvalidToken(String url) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        mockMvc.perform(post(url)
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(trainingRegistrationRequest)));
    }

    @Then("the response status should be {int} for Invalid Token")
    public void theResponseStatusShouldBeForUnauthorized(int expectedStatus) throws Exception {
        mockMvc.perform(post("/gym-app/training")
                                .header("Authorization", "Bearer " + invalidToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(trainingRegistrationRequest)))
               .andExpect(status().is(expectedStatus));
    }
}
