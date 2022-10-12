package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;


class BasicValidationServiceTest {

    private BasicValidationService basicValidationService;

    @BeforeEach
    void StartUp() {
        basicValidationService = new BasicValidationService();
    }
    @Test
    void shouldReturnExceptionOnValidateAmount() {
        // Given

        // When
        Executable nullAmount = () -> basicValidationService.validateAmount(null);
        Executable zeroAmount = () -> basicValidationService.validateAmount(0D);
        Executable negativeAmount = () -> basicValidationService.validateAmount(-10D);

        // Then
        assertThrows(IllegalArgumentException.class, nullAmount);
        assertThrows(IllegalArgumentException.class, zeroAmount);
        assertThrows(IllegalArgumentException.class, negativeAmount);
    }

    @Test
    void testValidateAmount() {
        // Given
        double validAmount = 10D;

        // When
        basicValidationService.validateAmount(validAmount);

        // Then
    }
    @Test
    void shouldReturnExceptionOnValidatePaymentId() {
        // Given

        // When
        Executable nullPaymentId = () -> basicValidationService.validatePaymentId(null);

        // Then
        assertThrows(IllegalArgumentException.class, nullPaymentId);
    }

    @Test
    void testValidatePaymentId() {
        // Given
        UUID validPaymentId = UUID.randomUUID();

        // When
        basicValidationService.validatePaymentId(validPaymentId);

        // Then
    }
    @Test
    void shouldReturnExceptionOnValidateUserId() {
        // Given

        // When
        Executable nullUserId = () -> basicValidationService.validateUserId(null);
        // Then
        assertThrows(IllegalArgumentException.class, nullUserId);
    }
    @Test
    void testValidateUserId() {
        // Given
        int validUserId = 10;

        // When
        basicValidationService.validateUserId(validUserId);

        // Then
    }

    @Test
    void shouldReturnExceptionOnValidateUser() {
        // Given
        User user = new User(1, "John", Status.INACTIVE);

        // When
        Executable inactiveUser = () -> basicValidationService.validateUser(user);

        // Then
        assertThrows(IllegalArgumentException.class, inactiveUser);
    }
    @Test
    void testValidateUser() {
        // Given
        User user = new User(1, "John", Status.ACTIVE);

        // When
        basicValidationService.validateUser(user);

        // Then
    }
    @Test
    void shouldReturnExceptionOnValidateMessage() {
        // Given
        String message = null;

        // When
        Executable nullMessage = () -> basicValidationService.validateMessage(message);

        // Then
        assertThrows(IllegalArgumentException.class, nullMessage);
    }
    @Test
    void testValidateMessage() {
        // Given
        String validMessage = "message";

        // When
        basicValidationService.validateMessage(validMessage);

        // Then
    }
}
