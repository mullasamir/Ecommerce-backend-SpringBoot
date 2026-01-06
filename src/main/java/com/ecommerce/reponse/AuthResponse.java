package com.ecommerce.reponse;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {

    private String jwt;
    private String message;
}

