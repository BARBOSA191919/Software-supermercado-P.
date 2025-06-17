package com.gestion.inventario.entidades;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailFileDTO {

    @NotEmpty(message = "El campo 'toUser' no puede estar vacío")
    private String[] toUser;

    @NotEmpty(message = "El campo 'subject' no puede estar vacío")
    private String subject;

    @NotEmpty(message = "El campo 'message' no puede estar vacío")
    private String message;

    @NotNull(message = "El archivo no puede estar vacío")
    private MultipartFile file;
}