package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;


class BasicValidationServiceTest {

    private final BasicValidationService basicValidationService = new BasicValidationService();

    @Test
    void testValidateAmount() {
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
    void testValidatePaymentId() {
        // Given

        // When
        Executable nullPaymentId = () -> basicValidationService.validatePaymentId(null);

        // Then
        assertThrows(IllegalArgumentException.class, nullPaymentId);
    }

    @Test
    void testValidateUserId() {
        // Given

        // When
        Executable nullUserId = () -> basicValidationService.validateUserId(null);
        // Then
        assertThrows(IllegalArgumentException.class, nullUserId);
    }

    @Test
    void testValidateUser() {
        // Given
        User user = new User(1, "Name", Status.INACTIVE);

        // When
        Executable inactiveUser = () -> basicValidationService.validateUser(user);

        // Then
        assertThrows(IllegalArgumentException.class, inactiveUser);
    }

    @Test
    void testValidateMessage() {
        // Given
        String message = null;

        // When
        Executable nullMessage = () -> basicValidationService.validateMessage(message);

        // Then
        assertThrows(IllegalArgumentException.class, nullMessage);
    }
}
