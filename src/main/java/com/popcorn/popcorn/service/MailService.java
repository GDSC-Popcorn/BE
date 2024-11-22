package com.popcorn.popcorn.service;

import com.popcorn.popcorn.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String username;

    private final String EMAIL_TITLE = "[PopCorn]회원 가입 인증 이메일 입니다.";
    private final String BLUR_OPEN = "<b>";
    private final String BLUR_CLOSE = "</b>";

    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;
    private int authNumber;

    public void makeRandomNumber(){
        Random random = new Random();
        StringBuilder randNum = new StringBuilder();

        for(int i=0;i<6;i++){
            randNum.append(random.nextInt(10));
        }

        authNumber = Integer.parseInt(randNum.toString());
    }

    public void joinEmail(String email) {
        makeRandomNumber();
        String title = EMAIL_TITLE; // 이메일 제목
        String content = BLUR_OPEN + authNumber + BLUR_CLOSE;
        mailSend(username, email, title, content);
    }

    private void mailSend(String setFrom, String toEmail, String title, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
        redisUtil.setDataExpire(Integer.toString(authNumber), toEmail, 60*3L);
    }

    public Boolean chkAuthNum(String email, String authNum) {
        if(redisUtil.getData(authNum) == null){
            return false;
        } else if(redisUtil.getData(authNum).equals(email)){
            return true;
        } else {
            return false;
        }

    }
}
