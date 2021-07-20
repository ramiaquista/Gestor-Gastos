
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.entity.Usuario;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.UsuarioService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable Long id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioService.buscarPorId(id);

        
        if (respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            
            if (usuario.getFotoPerfil() == null){
                throw new ErrorServicio ("El usuario no tiene foto asociada.");
            }
            
            String imageType = usuario.getFotoPerfil().getMime();
            byte[] foto = usuario.getFotoPerfil().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            if (imageType.contains("jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (imageType.contains("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
            
        } else {            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
