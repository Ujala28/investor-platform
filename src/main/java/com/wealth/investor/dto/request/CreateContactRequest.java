package com.wealth.investor.dto.request;

import com.wealth.investor.entity.enums.RelationshipType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContactRequest {
    @NotBlank
    private String name;

    @NotNull
    private RelationshipType relationshipType;

    @Email
    private String email;

    private String phone;
}
