package org.example.project_busticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendTicketConfirmation(String to, String ticketCode) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Xác nhận vé xe");
        message.setText("Vé của bạn đã được đặt thành công. Mã vé: " + ticketCode);

        mailSender.send(message);
    }
}