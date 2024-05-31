Feature: TrainingController Component Tests

  Scenario: Log in and get token
    Given a login request with valid credentials
    When the client gets the login request to "/gym-app/public/user/login"
    Then the response status should be 200 for login
    And the token is stored

  Scenario: Register a training
    Given a training registration request
    When the client posts the request to "/gym-app/training" with the token
    Then the response status should be 201 for creating training

  Scenario: Delete a training
    Given a delete training request
    When the client deletes the request to "/gym-app/training" with the token
    Then the response status should be 204 for deleting training

  Scenario: Get monthly stats
    Given a monthly stat request
    When the client gets the request to "/gym-app/trainings/monthly-stat" with the token
    Then the response status should be 204 for monthly stat requests

  Scenario: Get full stats
    Given a full stat request
    When the client gets the request to "/gym-app/trainings/full-stat" with the token
    Then the response status should be 204 for full stat requests

  Scenario: Get trainings by trainee
    Given a request for trainings by trainee
    When the client gets the request to "/trainings/of-trainee" with the token
    Then the response status should be 200 for trainee training

    #additional cases
  Scenario: Invalid Login Credentials
    Given a login request with invalid credentials
    When the client gets the login request to "/gym-app/public/user/login" with invalid credentials
    Then the response status should be 401 for invalid login


  Scenario: Delete Non-Existing Training
    Given a delete training request for a non-existing training ID
    When the client deletes the request to "/gym-app/training" with the token
    Then the response status should be 404 for not found training

  Scenario: Get Trainings with Invalid Token
    Given a request to get trainings with an invalid or expired token
    When the client posts the request to "/gym-app/training" with an invalid token
    Then the response status should be 400 for Invalid Token


