package com.faust.ticketing.persistence.model.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsePasswordResetDTO {
    @Id
    private String tokenHash;
    private String newPassword;
}
