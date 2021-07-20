
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.ColorService;
import com.egg.ProyectoFinal.service.GrupoCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/grupos")
public class GrupoCategoriaController {
    
    @Autowired
    private GrupoCategoriaService grupoCategoriaService;
    
    
    // MÉTODOS
    
    @PostMapping("/crear")
    public RedirectView crear(
            @RequestParam String nombre,
            @RequestParam Long color_id)
            throws ErrorServicio {
        grupoCategoriaService.nuevoGrupoCategoria(nombre, color_id);
        return new RedirectView("/categorias");
    }
    
    @PostMapping("/modificar/{id}")
    public RedirectView modificar(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam Long color_id)
            throws ErrorServicio {
        grupoCategoriaService.modificar(id, nombre, color_id);
        return new RedirectView("/categorias");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Long id) {
        grupoCategoriaService.eliminar(id);
        return new RedirectView("/categorias");
    }
}
