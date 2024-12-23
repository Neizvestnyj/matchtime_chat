package com.example.matchtimechat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}