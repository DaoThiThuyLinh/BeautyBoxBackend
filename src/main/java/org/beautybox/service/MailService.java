package org.beautybox.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface MailService {
    void sendMail(String subject, String email, String content, boolean isHtml) throws UnsupportedEncodingException, MessagingException;
}
