package com.wealth.investor.dto.response;

import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
public class InvestorResponse implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private InvestorType investorType;
    private String panNumber;
    private KycStatus kycStatus;
}