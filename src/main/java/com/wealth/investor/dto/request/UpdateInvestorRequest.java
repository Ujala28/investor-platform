package com.wealth.investor.dto.request;

import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateInvestorRequest {

    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private InvestorType investorType;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$")
    private String panNumber;

    private KycStatus kycStatus;
}
