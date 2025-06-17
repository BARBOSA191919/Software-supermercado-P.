package com.gestion.inventario.servicio;

import java.io.File;

public interface IEmailService {

    void sendEmail(String[] toUser, String subject, String message, boolean html);

    void sendEmailWithFile(String[] toUser, String subject, String message, File file);
}