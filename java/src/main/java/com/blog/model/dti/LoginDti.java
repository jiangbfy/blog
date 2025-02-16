package com.blog.model.dti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDti {
    private String name;
    private String password;
    private String verify;
    private String verifyMd5;
    private String token;
}
