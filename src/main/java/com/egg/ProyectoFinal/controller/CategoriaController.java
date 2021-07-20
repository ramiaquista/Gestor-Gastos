
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.CategoriaService;
import com.egg.ProyectoFinal.service.ColorService;
import com.egg.ProyectoFinal.service.GrupoCategoriaService;
import com.egg.ProyectoFinal.service.UsuarioService;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private GrupoCategoriaService grupoCategoriaService;
    
    // MÉTODOS
    
    @GetMapping
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("categorias");
        mav.addObject("categorias", categoriaService.buscarTodas());
        mav.addObject("colores", colorService.buscarTodos());
        mav.addObject("gruposDeCategorias", grupoCategoriaService.buscarTodos());
        mav.addObject("usuarioNombre", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getUsername());
        mav.addObject("usuarioId", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getId());
        mav.addObject("usuarioFoto", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getFotoPerfil());
        
        return mav;
    }
    
    @PostMapping("/crear")
    public RedirectView crear(
            @RequestParam String nombre,
            @RequestParam Long grupo_id,
            @RequestParam Long color_id)
            throws ErrorServicio {
        categoriaService.crear(nombre, grupo_id, color_id);
        return new RedirectView("/categorias");
    }
    
    @PostMapping("/modificar/{id}")
    public RedirectView modificar(
            @PathVariable Long id,
            @RequestParam Long grupo_id,
            @RequestParam String nombre,
            @RequestParam Long color_id)
            throws ErrorServicio {
        categoriaService.modificar(id, nombre, grupo_id, color_id);
        return new RedirectView("/categorias");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return new RedirectView("/categorias");
    }

}
