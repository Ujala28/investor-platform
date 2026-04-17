package com.wealth.investor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealth.investor.dto.request.CreateInvestorRequest;
import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(properties = "spring.cache.type=none")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InvestorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void shouldCreateInvestor() throws Exception {

        CreateInvestorRequest request = new CreateInvestorRequest();
        request.setFullName("Test User");
        request.setEmail("test" + System.currentTimeMillis() + "@gmail.com"); // unique email
        request.setPhone("9999999999");
        request.setDateOfBirth(LocalDate.of(1995, 5, 20)); // age > 18
        request.setInvestorType(InvestorType.INDIVIDUAL);
        request.setPanNumber("ABCDE1234F"); // valid PAN
        request.setKycStatus(KycStatus.PENDING);

        mockMvc.perform(post("/api/investors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) // helpful debug
                .andExpect(status().isCreated());
    }
}