package com.example.link.reduce.controller.auth.dto;

import lombok.Data;

@Data
public class SignUpProperties {
    private String login;
    private String name;
    private String email;
    private String password;
}
