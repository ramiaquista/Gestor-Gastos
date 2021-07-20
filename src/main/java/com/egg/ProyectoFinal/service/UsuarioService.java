
package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Foto;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.entity.Usuario;
import com.egg.ProyectoFinal.repository.UsuarioRepository;
import com.egg.ProyectoFinal.service.utils.LevenshteinDistance;

import java.util.*;
import javax.mail.MessagingException;
import net.bytebuddy.utility.RandomString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService implements UserDetailsService{
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Autowired 
    private EmailService emailService;

    @Autowired
    private FotoService fotoService;
    
    
    // MÉTODOS
    
    @Transactional
    public void registrar(String username, String clave, String email, String siteURL) throws ErrorServicio{
        
        // Usuario usuario = usuarioRepository.buscarPorNombre(username);
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario != null) { throw new ErrorServicio("Este usuario ya está registrado."); }
        
        validar(username, email, clave);
        
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username.trim());
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setClave(encoder.encode(clave));
        
        // for account verification:
        nuevoUsuario.setVerificationCode(RandomString.make(64));
        
        usuarioRepository.save(nuevoUsuario);
        
        try {
            emailService.enviarCorreoActivacion(nuevoUsuario, siteURL);
        } catch (MessagingException ex) {
            System.out.println("Falló envio del correo de activación de cuenta.");
        }
        
//        try {
//            emailService.enviarCorreo(email, "Gestor Gastos App", "Bienvenido a la App!, " + username + ". <br><b>El equipo de Gastegg.<b>");
//        } catch (MessagingException ex) {
//            System.out.println("Falló envio correo bienvenida.");
//        }
        
    }

        
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
   }
    
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
   }
    
    @Transactional
    public void modificarNombre(Long id, String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isBlank()) {
            throw new ErrorServicio("El nombre ingresado no puede ser nulo.");
        }
        
        Optional<Usuario> respuesta = usuarioRepository.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setUsername(nombre);
            
            usuarioRepository.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario.");
        }

    }
    
    @Transactional
    public void modificarContraseña(String username, String email, String claveActual, String claveNueva, String claveNuevaConfirmacion) throws ErrorServicio{
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null) { throw new ErrorServicio("Error al identificar el usuario."); }
        
        validarClave(username, email, claveNueva);

        if (encoder.matches(claveActual, usuario.getClave())){
            if (claveNuevaConfirmacion.equals(claveNueva)){
                usuario.setClave(encoder.encode(claveNueva));
                usuarioRepository.save(usuario);
            } else {
                throw new ErrorServicio("Las contraseñas escritas no coinciden. Inténtelo denuevo.");
            }
        } else {
            throw new ErrorServicio("La clave ingresada no coincide con su clave actual.");
        }
    }

    @Transactional
    public void modificarFoto(Long usuario_id, MultipartFile archivo) throws ErrorServicio{
        
        Optional<Usuario> respuesta = usuarioRepository.findById(usuario_id);
        
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
        
            Long fotoId = null;
            if (usuario.getFotoPerfil() != null){
                fotoId = usuario.getFotoPerfil().getId();
            } 
            
            Foto foto = fotoService.actualizar(fotoId, archivo);
            usuario.setFotoPerfil(foto);

            usuarioRepository.save(usuario);
        } else {
             throw new ErrorServicio("No se encontró el usuario.");
        }
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usuario usuario = usuarioRepository.buscarPorNombre(username);
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        
        if (usuario == null){
            // throw new UsernameNotFoundException("No existe ningún usuario con nombre: '"+username+"'.");
            throw new UsernameNotFoundException("No existe ningún usuario con el email: '"+email+"'.");
        }
        
        if (!usuario.isEnabled()){
            throw new UsernameNotFoundException("Tu cuenta no ha sido verificada");
        }
        return new User(usuario.getEmail(), usuario.getClave(), Collections.emptyList());
        //return new User(usuario.getUsername(), usuario.getClave(), Collections.emptyList());
    }
    
    public boolean verify(String verificationCode) {
        Usuario usuario = usuarioRepository.findByVerificationCode(verificationCode);
     
        if (usuario == null || usuario.isEnabled()) {
            return false;
        } else {
            usuario.setVerificationCode(null);
            usuario.setEnabled(true);
            usuarioRepository.save(usuario);

            return true;
        }
    }
    
    
    // Validaciones
    
    public void validar(String nombre, String email, String clave) throws ErrorServicio{
        
        if (nombre == null || nombre.isBlank()){
            throw new ErrorServicio("El nombre del usuario no puede ser nulo.");
        }
        if (email == null || email.isBlank()){ 
            throw new ErrorServicio("El email del usuario no puede ser nulo.");
        }
        
        validarClave(nombre, email, clave);
    }
    
    private boolean validarClave(String nombre, String email, String clave) throws ErrorServicio{
        String invalidPasswordRegex = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$";
        
        if (clave == null || clave.isBlank()){
            throw new ErrorServicio("La clave de usuario no puede ser nula.");
        }
        if (clave.matches(invalidPasswordRegex)){
            throw new ErrorServicio("La clave debe tener al menos: \n"
                    + "8 caracteres, 1 letra mayúscula, 1 número y 1 símbolo.");
        }
        if (passwordIsCommon(nombre, clave, email)){
            throw new ErrorServicio("Por seguridad, la clave debe ser más original.");
        }
        
        return true;
    }
    
    private List<String> commonPasswords(){
        //
        return Arrays.asList("password", "password1", "password_1234", "contraseña", "1q2w3e4r",
              "1111111", "123123", "123456", "12345678", "123456789", "abc123", "qwerty", "qwerty123",
              "zaq12wsx", "boca", "river", "admin", "tequiero", "iloveyou");
    }
        
    private boolean passwordIsCommon(String nombre, String clave, String email){
        List<String> weakPasswords = new ArrayList<>();
        List<String> usernameWords = Arrays.asList(nombre.split("( |_)"));
        List<String> emailWords = Arrays.asList(email.split("(@|_|\\.|\\-)"));
        
        //String emailUsername = email.substring(0, email.indexOf("@"));
        //List<String> emailUsernameWords = Arrays.asList(emailUsername.split("(_|\\.|\\-)"));

        weakPasswords.addAll(usernameWords);
        weakPasswords.addAll(emailWords);
        weakPasswords.addAll(commonPasswords());
        
        String clave_ = clave.toLowerCase();

        for (String password : weakPasswords) {
            // calculo que tan parecida es la clave ingresada respecto a 
            // las claves comunmente usadas, cuánto más parecida, más débil.
            double dist = (double)LevenshteinDistance.compute_dist(password,clave);
            double factorSimilitud = 1 - (dist/Math.max(password.length(),clave.length()));

            if(clave_.contains(password.toLowerCase()) && factorSimilitud >= 0.35){
                return true;// rechazo la contraseña
            }
        }
        return false;
    }
}
