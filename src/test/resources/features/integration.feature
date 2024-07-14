Feature: Update Stat Integration

  Scenario: App 1 sends an update stat request and App 2 processes it correctly
    Given App 1 sends an update stat request with correlation ID "test-correlation-id-update-stat"
    Then App 1 receives the update stat response

  Scenario: App 1 sends an full stat request and App 2 processes it correctly
    Given App 1 sends a full stat request with correlation ID "test-correlation-id-full-stat"
    Then App 1 receives the full stat response


  Scenario: App 1 sends an monthly stat request and App 2 processes it correctly
    Given App 1 sends a monthly request with correlation ID "test-correlation-id-monthly-stat"
    Then App 1 receives the monthly stat response

