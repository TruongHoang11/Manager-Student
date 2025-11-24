package vn.hoangtojava.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.hoangtojava.dto.response.ResponseData;
import vn.hoangtojava.dto.response.ResponseError;
import vn.hoangtojava.service.MailService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/common")
public class ConmonController{

    private final MailService mailService;

    @PostMapping("/send-email")
    public ResponseData<?> sendEmail(
           @RequestParam String receivers,
           @RequestParam String subject,
           @RequestParam String content,
           @RequestParam(required = false) MultipartFile[] files
    ){

        try{
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), mailService.sendEmail(receivers,subject,content,files));
        } catch(Exception e){
            log.info("Sending Email Failed, errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Send Email Failled");
        }
    }

}