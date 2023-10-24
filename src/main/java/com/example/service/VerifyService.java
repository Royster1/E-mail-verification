package com.example.service;


public interface VerifyService {

    void sendVerifyCode(String mail); // 传入邮箱

    boolean doVerify(String mail, String code);// 验证验证码
}
