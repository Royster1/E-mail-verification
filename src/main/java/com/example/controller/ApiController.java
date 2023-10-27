package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.VerifyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController // 不是返回页面的用这个
@RequestMapping("/api/auth")
public class ApiController {

    @Resource
    VerifyService service;

    @RequestMapping("/verify-code")
    public RestBean verifyCode(@RequestParam("reason") String email){
        try {
            service.sendVerifyCode(email);
            return new RestBean(200, "邮件发送成功");
        } catch (Exception e) {
            return new RestBean(500, "邮件发送失败");
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RestBean register(String username,
                             String password,
                             String email,
                             String verify
                         ){
        if (service.doVerify(email, verify)) {
            System.out.println("注册成功!");
            return new RestBean(200, "注册成功");
        } else {
            System.out.println("注册失败");
            return new RestBean(500, "注册失败, 验证码失败");
        }
    }
}
