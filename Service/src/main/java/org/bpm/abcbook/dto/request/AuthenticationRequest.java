package org.bpm.abcbook.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    @NotNull(message = "create.user.email.empty")
    @Email(message = "create.user.email.wrong.format")
    private String email;

    @NotNull(message = "create.user.password.empty")
    private String password;
}
