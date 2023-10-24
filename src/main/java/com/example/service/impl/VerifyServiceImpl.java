package com.example.service.impl;

import com.example.service.VerifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyServiceImpl implements VerifyService {

    @Resource
    JavaMailSender sender;

    @Resource
    StringRedisTemplate template;

    @Value("${spring.mail.username}")
    String from;

    @Override
    public void sendVerifyCode(String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[广东科技学院] 您的注册码");
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        template.opsForValue().set("verify:code:" + mail, code + "", 3, TimeUnit.MINUTES);
        message.setText("您的验证码为:"+ code + ", 三分钟内有效, 请及时完成注册! 如果不是本人操作, 请忽略!");
        message.setTo(mail);
        message.setFrom(from);
        sender.send(message);
    }

    @Override
    public boolean doVerify(String email, String code) {
        String string = template.opsForValue().get("verify:code:" + email);
        // redis 中 key是 verify:code:royster61@163.com, value是验证码
        template.delete("verify:code:" + email); // 验证码只能获取一次
        if (string == null) return false; // 邮箱对应不上
        if (!string.equals(code))   return false; // 验证码对应不上
        template.delete("verify:code:" + email); // 验证码成功只能用一次
        return true; // redis中的code与表单传入过来的code进行对比
    }
}
