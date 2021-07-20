
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PrincipalController {
    
        @Autowired 
    private UsuarioService usuarioService;
    
    @GetMapping()
    public ModelAndView inicio() {
        return new ModelAndView("landing");
    }
    
    @GetMapping("/")
    public ModelAndView landing(){
        return new ModelAndView("landing");
    }
    
    @GetMapping("/verify")
    public RedirectView verifyUser(@Param("code") String code) {
        boolean userAccountIsVerified = usuarioService.verify(code); 
        if (userAccountIsVerified) {
            return new RedirectView("/successful_verification");
        } else {
            return new RedirectView("/verification_failed");
        }
    }
}
