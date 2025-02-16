package com.blog.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.blog.common.VerificationCode;
import com.blog.model.ResultResponse;
import com.blog.model.dti.LoginDti;
import com.blog.model.dto.LoginDto;
import com.blog.model.entity.UserEntity;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    private UserService service;

//    @GetMapping("/verify")
//    public void verifyCode(HttpServletResponse resp) throws IOException {
//        VerificationCode code = new VerificationCode();
//        BufferedImage image = code.getImage();
//        String text = code.getText().toLowerCase();
//        String md5Text = DigestUtil.md5Hex(text);
//        resp.setHeader("verify", md5Text);
//        VerificationCode.output(image, resp.getOutputStream());
//    }
    @PostMapping("/login")
    public ResultResponse<LoginDto> login(@RequestBody LoginDti loginDti) {
        LoginDto dto = service.login(loginDti);
        if (dto == null) return ResultResponse.error("error", null);
        else return ResultResponse.success("success", dto);
    }
//    @PostMapping("/register")
//    public ResultResponse register(@RequestBody UserEntity userEntity) {
//        boolean res = service.register(userEntity);
//        if(res) return ResultResponse.success("success");
//        return ResultResponse.error("error");
//    }
    @PostMapping("/password")
    public ResultResponse password(@RequestBody UserEntity userEntity) {
        service.password(userEntity);
        return ResultResponse.success("success");
    }
}
