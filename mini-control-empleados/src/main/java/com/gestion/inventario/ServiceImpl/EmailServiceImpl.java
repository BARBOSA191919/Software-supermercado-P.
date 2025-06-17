package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.servicio.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements IEmailService {
    @Value("${email.sender}")
    private String emailUser;

    @Autowired
    private JavaMailSender mailSender;

    private String createSimpleTextContent(String content) {
        StringBuilder text = new StringBuilder();
        text.append("Historial de Alertas\n\n");
        text.append(content.replaceAll("<[^>]*>", "").trim());
        return text.toString();
    }

    @Override
    public void sendEmail(String[] toUser, String subject, String message, boolean isHtml) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom(emailUser);
            helper.setTo(toUser);
            helper.setSubject(subject);

            String formattedContent = createSimpleTextContent(message);
            helper.setText(formattedContent, false); // false para enviar como texto plano

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }

    @Override
    public void sendEmailWithFile(String[] toUser, String subject, String message, File file) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom(emailUser);
            helper.setTo(toUser);
            helper.setSubject(subject);

            String formattedContent = createSimpleTextContent(message);
            helper.setText(formattedContent, false);
            helper.addAttachment(file.getName(), file);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo con archivo adjunto", e);
        }
    }
}