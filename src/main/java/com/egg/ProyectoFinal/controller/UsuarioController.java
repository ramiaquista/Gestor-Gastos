
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.UsuarioService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired 
    private UsuarioService usuarioService;
    
    // MÉTODOS
    
    @PostMapping("/registrar")
    public RedirectView registrar(RedirectAttributes model, HttpServletRequest request,
            @RequestParam String username, @RequestParam String clave, @RequestParam String email){
        
        try{
            usuarioService.registrar(username, clave, email, getSiteURL(request));
            return new RedirectView("/registration_success");
        }
        catch(ErrorServicio e){
            //System.out.println("\n"+ e.getMessage());
            model.addFlashAttribute("errorRegistro", e.getMessage());
            return new RedirectView("/registro");
        }

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    
    
    
}
