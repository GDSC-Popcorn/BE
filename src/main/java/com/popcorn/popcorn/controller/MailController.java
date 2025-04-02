package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.domain.dto.EmailCheckDto;
import com.popcorn.popcorn.domain.dto.EmailRequestDto;
import com.popcorn.popcorn.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping("/mailsend")
    public ResponseEntity<ApiResponse<String>> mailSend(@RequestBody @Valid EmailRequestDto emailRequestDto){
        try {
            mailService.joinEmail(emailRequestDto.getEmail());
            return ResponseEntity.ok(ApiResponse.ok("인증 이메일이 발송되었습니다."));
        } catch (MailException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(500, "fail","메일 발송에 실패함. 다시 시도해주세요."));
        } catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(500, "fail", "잘못된 요청, Redis나 db가 가동되지않습니다."));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(400, "fail", "잘못된 요청, 입력값을 확인해주세요."));
        }
    }


    @PostMapping("/mailauthChk")
    public ResponseEntity<ApiResponse<String>> mailChk(@RequestBody @Valid EmailCheckDto emailCheckDto){
        Boolean chk = mailService.chkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        if(chk){
            return ResponseEntity.ok().body(ApiResponse.ok("인증 번호 일치"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(400,"fail","인증 번호가 틀리거나 만료되었습니다."));
    }



}
