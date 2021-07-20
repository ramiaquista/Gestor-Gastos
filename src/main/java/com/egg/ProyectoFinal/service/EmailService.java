
package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Usuario;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender sender;
    
    @Value("spring.mail.username")
    private String from;
    
    public void enviarCorreo(String to, String asunto, String cuerpo) throws MessagingException{
        
        new Thread(() -> {
            try{
                String[] para = {to};
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(asunto);
                helper.setText(cuerpo, true);

                sender.send(message);
            } catch(MessagingException e){
                System.out.println("Error al enviar correo a: " + to);
            }
        }).start();
    }
    
    public void enviarCorreoActivacion(Usuario nuevoUsuario, String siteURL) throws MessagingException{
        
        new Thread(() -> {
            try{
                
                String content =  "Estimado [[name]],<br>"
                    + "Por favor haga click en el siguiente link para verificar su cuenta:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Verificar cuenta</a></h3>"
                    + "Muchas gracias.<br>"
                    + "<b>El equipo de Gastegg App.</b>";
                
                String verifyURL = siteURL + "/verify?code=" + nuevoUsuario.getVerificationCode();
                content = content.replace("[[name]]", nuevoUsuario.getUsername());
                content = content.replace("[[URL]]", verifyURL);
                
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                
                helper.setFrom(from);
                helper.setTo(nuevoUsuario.getEmail());
                helper.setSubject("Activación de cuenta: Gastegg App");
                helper.setText(content, true);

                sender.send(message);
            } catch(MessagingException e){
                System.out.println("Error al enviar correo a: " + nuevoUsuario.getEmail());
            }
        }).start();
    }
}
