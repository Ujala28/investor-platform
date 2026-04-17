package com.wealth.investor.service;

import com.wealth.investor.dto.request.CreateInvestorRequest;
import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import com.wealth.investor.repository.InvestorRepository;
import com.wealth.investor.service.InvestorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvestorServiceTest {

    @Mock
    private InvestorRepository investorRepository;

    @InjectMocks
    private InvestorService investorService;

    public InvestorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {

        CreateInvestorRequest request = new CreateInvestorRequest();
        request.setEmail("test@example.com");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setInvestorType(InvestorType.INDIVIDUAL);
        request.setPanNumber("ABCDE1234F");
        request.setKycStatus(KycStatus.PENDING);
        request.setFullName("Test User");
        request.setPhone("9999999999");

        when(investorRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                investorService.createInvestor(request));
    }

    @Test
    void shouldThrowExceptionWhenAgeLessThan18() {

        CreateInvestorRequest request = new CreateInvestorRequest();
        request.setEmail("test@example.com");
        request.setDateOfBirth(LocalDate.now()); // age < 18
        request.setInvestorType(InvestorType.INDIVIDUAL);
        request.setPanNumber("ABCDE1234F");
        request.setKycStatus(KycStatus.PENDING);
        request.setFullName("Test User");
        request.setPhone("9999999999");

        when(investorRepository.existsByEmail(any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                investorService.createInvestor(request));
    }
}
