package vn.hoangtojava.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.hoangtojava.model.User;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Getter
@Slf4j
public class MailService{

    private  final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;



    @Value("${spring.mail.from}")
    private String emailFrom;

    public String sendEmail(String receivers, String subject, String content, MultipartFile[] files) throws MessagingException {
        log.info("Sending");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        if(receivers.contains(",")){
            InternetAddress internetAddresses[] = InternetAddress.parse(receivers);
            helper.setTo(internetAddresses);
        } else{
            helper.setTo(receivers);
        }
        helper.setFrom(emailFrom);
        helper.setSubject(subject);
        helper.setText(content, true);

        if(files != null){
            for(MultipartFile file : files){
                helper.addAttachment(file.getOriginalFilename(), file);
            }
        }

        javaMailSender.send(message);
        log.info("Email has been sent successfull, receivers={}", receivers);
        return "Sent";

    }


    public void confirmEmailLink(String emailTo, Long userId, String secretCode) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending email confirm account");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setFrom(emailFrom, "HoangToz");

        Context context = new Context();

        String linkConfirm = String.format("http://localhost:80/user/confirm/%s?secretCode=%s", userId, secretCode);
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm",linkConfirm);

        context.setVariables(properties);

        helper.setTo(emailTo);
        helper.setSubject("Please confirm your account");

        String html = springTemplateEngine.process("confirm-email.html", context);

        helper.setText(html, true);
        javaMailSender.send(message);

        log.info("Email Sent, user={}",emailTo);

    }
}
