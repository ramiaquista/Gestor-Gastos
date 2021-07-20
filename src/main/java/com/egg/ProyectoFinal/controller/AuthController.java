
package com.egg.ProyectoFinal.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthController {
    
    @GetMapping("/login")
    public ModelAndView login(ModelMap model, @RequestParam(required=false) String error,
            @RequestParam(required=false) String logout, HttpServletRequest request, Principal usuarioLogeado){

        if (usuarioLogeado != null) {
            return new ModelAndView(new RedirectView("/gastos"));
            //return new ModelAndView("redirect:/gastos"));// equivalente
        }
        if (error != null) {
            model.put("errorLogin","El nombre de Usuario o la contraseña son incorrectos.");
        }
//        if(logout != null){
//            model.put("logout","Ha salido de la sesión exitosamente.");
//        }
        return new ModelAndView("login");
    }
    
    @RequestMapping("/registro")
    public ModelAndView registro(){
        return new ModelAndView("registro");
    }
    
    @RequestMapping("/registration_success")
    public ModelAndView registro_exitoso(){
        return new ModelAndView("registration_success");
    }
    
    @RequestMapping("/successful_verification")
    public ModelAndView successful_verification(){
        return new ModelAndView("successful_verification");
    }
    
    @RequestMapping("/verification_failed")
    public ModelAndView verification_failed(){
        return new ModelAndView("verification_failed");
    }
    
}
