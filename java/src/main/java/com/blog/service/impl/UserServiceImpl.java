package com.blog.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.common.TokenUtils;
import com.blog.dao.UserMapper;
import com.blog.model.dti.LoginDti;
import com.blog.model.dto.LoginDto;
import com.blog.model.entity.UserEntity;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(UserEntity userEntity) {
        if(userEntity.getName() == null || userEntity.getPassword() == null) return false;
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        String password = DigestUtil.md5Hex( userEntity.getName() + userEntity.getPassword());
        userEntity.setPassword(password);
        userMapper.insert(userEntity);
        return true;
    }

    @Override
    public LoginDto login(LoginDti loginDti) {
        String token = "";
        if(loginDti.getToken() == null) {
//            String md5Text = DigestUtil.md5Hex(loginDti.getVerify());
//            if (!md5Text.equals(loginDti.getVerifyMd5())) return "";
            String password = DigestUtil.md5Hex(loginDti.getName() + loginDti.getPassword());
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", loginDti.getName());
            UserEntity user = userMapper.selectOne(queryWrapper);
            if (user == null) return null;
            if (!password.equals(user.getPassword())) return null;

            token = TokenUtils.sign(loginDti.getName() + loginDti.getPassword());

            LoginDto dto = LoginDto.builder()
                    .name(user.getName())
                    .headUrl(user.getHeadUrl())
                    .token(token)
                    .role(user.getRole())
                    .build();
            return dto;
        } else {
            String password = TokenUtils.verify(loginDti.getToken());
            if(!password.equals("")) {
                token = loginDti.getToken();
                password = DigestUtil.md5Hex(password);
                QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("password", password);
                UserEntity user = userMapper.selectOne(queryWrapper);
                if (user == null) return null;

                LoginDto dto = LoginDto.builder()
                        .name(user.getName())
                        .headUrl(user.getHeadUrl())
                        .token(token)
                        .role(user.getRole())
                        .build();
                return dto;
            }
            else return null;
        }
    }

    @Override
    public  boolean updateHeadUrl(String userName, String headUrl) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userName);
        UserEntity user = userMapper.selectOne(queryWrapper);
        user.setHeadUrl(headUrl);
        userMapper.update(user, queryWrapper);
        return true;
    }

    @Override
    public boolean password(UserEntity userEntity) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userEntity.getName());
        UserEntity user = userMapper.selectOne(queryWrapper);

        String password = DigestUtil.md5Hex( userEntity.getName() + userEntity.getPassword());
        user.setPassword(password);
        userMapper.update(user, queryWrapper);
        return true;
    }
}
