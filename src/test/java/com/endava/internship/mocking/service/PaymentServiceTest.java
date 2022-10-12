package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.InMemPaymentRepository;
import com.endava.internship.mocking.repository.InMemUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private InMemUserRepository userRepository;
    @Mock
    private InMemPaymentRepository paymentRepository;
    @Mock
    private BasicValidationService validationService;
    @InjectMocks
    private PaymentService paymentService;
    @Test
    void createPayment() {
        // Given
        double amount = 1D;
        String message = "Payment from user John";
        User user = new User(1, "John", Status.ACTIVE);
        Payment expectedPayment = new Payment(user.getId(), amount, message);
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);
        paymentService.createPayment(user.getId(), amount);
        verify(paymentRepository).save(paymentCaptor.capture());

        // Then
        assertEquals(expectedPayment.getUserId(), paymentCaptor.getValue().getUserId());
        assertEquals(expectedPayment.getAmount(), paymentCaptor.getValue().getAmount());
        assertEquals(expectedPayment.getMessage(), paymentCaptor.getValue().getMessage());
        verify(validationService).validateUserId(user.getId());
        verify(validationService).validateAmount(amount);
        verify(validationService).validateUser(user);
        verifyNoMoreInteractions(validationService);
    }
    @Test
    void shouldReturnExceptionWhenCreatePayment() {
        //Given
        User user = new User(1, "John", Status.INACTIVE);

        //When
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doThrow(IllegalArgumentException.class).when(validationService).validateUserId(null);
        doThrow(IllegalArgumentException.class).when(validationService).validateAmount(null);
        doThrow(IllegalArgumentException.class).when(validationService).validateUser(user);

        Executable nullIdUser = () -> paymentService.createPayment(null, 1D);
        Executable nullAmount = () -> paymentService.createPayment(user.getId(), null);
        Executable notFoundUser = () -> paymentService.createPayment(2, 1D);
        Executable inactiveUser = () -> paymentService.createPayment(user.getId(), 1D);

        //Then
        assertThrows(IllegalArgumentException.class, nullIdUser);
        assertThrows(IllegalArgumentException.class, nullAmount);
        assertThrows(NoSuchElementException.class, notFoundUser);
        assertThrows(IllegalArgumentException.class, inactiveUser);
        verify(userRepository).findById(user.getId());
        verify(userRepository).findById(2);
        verify(validationService).validateUserId(null);
        verify(validationService).validateAmount(null);
        verify(validationService).validateUser(user);
    }
    @Test
    void editMessage() {
        //Given
        String oldMessage = "old massage";
        String newMessage = "new message";
        Payment paymentOldMsg = new Payment(1, 1D, oldMessage);
        Payment paymentNewMsg = new Payment(1, 1D, newMessage);

        //When
        when(paymentRepository.editMessage(paymentOldMsg.getPaymentId(), newMessage))
                .thenReturn(paymentNewMsg);
        Payment result = paymentService
                .editPaymentMessage(paymentOldMsg.getPaymentId(), newMessage);

        //Then
        assertThat(result).isEqualToComparingFieldByField(paymentNewMsg);
        verify(validationService).validatePaymentId(paymentOldMsg.getPaymentId());
        verify(validationService).validateMessage(paymentNewMsg.getMessage());
    }

    @Test
    void getAllByAmountExceeding() {
        //Given
        List<Payment> payments = Stream.of(
                new Payment(1, 1D, "msg"),
                new Payment(2, 2D, "msg"),
                new Payment(3, 3D, "msg"))
                .collect(Collectors.toList());

        //When
        when(paymentRepository.findAll()).thenReturn(payments);
        List<Payment> result = paymentService.getAllByAmountExceeding(1D);

        //Then
        assertThat(result).containsOnly(payments.get(1), payments.get(2));
    }
}
