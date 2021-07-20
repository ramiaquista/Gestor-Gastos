
package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Foto;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.repository.FotoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {
    
    @Autowired
    private FotoRepository fotoRepository;
    
    @Transactional
    public Foto guardar(MultipartFile archivo){
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepository.save(foto);
            } catch(Exception e){
                System.err.println(e.getMessage());
            }
        } 
        return null;
        
    }

    @Transactional
    public Foto actualizar(Long id, MultipartFile archivo){
    
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                if (id != null){
                    Optional<Foto> respuesta = fotoRepository.findById(id);
                    if (respuesta.isPresent()){
                        foto = respuesta.get();
                    }
                }

                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepository.save(foto);
                
            } catch(Exception e){
                System.err.println("\nERROR: " + e.getMessage());
            }
        }
        return null;    
    }
}
