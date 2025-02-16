package com.blog.service;

import com.blog.model.dti.LoginDti;
import com.blog.model.dto.LoginDto;
import com.blog.model.entity.UserEntity;

public interface UserService {
    public boolean register(UserEntity userEntity);
    public LoginDto login(LoginDti loginDti);
    public  boolean updateHeadUrl(String userName, String headUrl);
    public boolean password(UserEntity userEntity);
}
