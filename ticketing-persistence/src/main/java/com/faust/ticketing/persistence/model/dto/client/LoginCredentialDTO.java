package com.faust.ticketing.persistence.model.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentialDTO {
    private String usernameOrEmail;
    private String password;
}
