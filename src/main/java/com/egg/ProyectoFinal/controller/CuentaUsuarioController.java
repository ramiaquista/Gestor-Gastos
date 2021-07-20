
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.UsuarioService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/cuentaUsuario")
public class CuentaUsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;


    
    @GetMapping
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("cuentaUsuario");

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (inputFlashMap != null) {
            mav.addObject("errorAlModificarFoto", inputFlashMap.get("errorAlModificarFoto"));
            mav.addObject("errorAlModificarClave", inputFlashMap.get("errorAlModificarClave"));
            mav.addObject("errorAlModificarNombre", inputFlashMap.get("errorAlModificarNombre"));
            mav.addObject("claveModificadaExitosamente", inputFlashMap.get("claveModificadaExitosamente"));
        }
        
        mav.addObject("usuarioNombre", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getUsername());
        mav.addObject("usuarioId", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getId());
        mav.addObject("usuarioFoto", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getFotoPerfil());

        return mav;
    }
    
    
    @PostMapping("/modificarFoto")
    public RedirectView modificarFoto(RedirectAttributes model, @RequestParam Long usuario_id,
            @RequestParam MultipartFile foto){
        try{
            usuarioService.modificarFoto(usuario_id, foto);
            return new RedirectView("/cuentaUsuario");
        }
        catch(ErrorServicio e){
            //System.out.println("\n"+ e.getMessage());
            model.addFlashAttribute("errorAlModificarFoto", e.getMessage());
            return new RedirectView("/cuentaUsuario/#error-al-modificar-foto");
        }
    }
    
   
    @PostMapping("/modificarNombre")
    public RedirectView modificarNombre(RedirectAttributes model, @RequestParam Long usuario_id, @RequestParam String nombre){
        try{
            usuarioService.modificarNombre(usuario_id, nombre);
            return new RedirectView("/cuentaUsuario");
        }
        catch(ErrorServicio e){
            model.addFlashAttribute("errorAlModificarNombre", e.getMessage());
            return new RedirectView("/cuentaUsuario/#error-al-modificar-nombre");
        }
    }

    @PostMapping("/modificarContraseña")
    public RedirectView modificarContraseña(
            RedirectAttributes model, HttpServletRequest request,
            @RequestParam String username, @RequestParam String email,
            @RequestParam String claveActual, @RequestParam String claveNueva,
            @RequestParam String claveNuevaConfirmacion){
            model.addFlashAttribute("claveModificadaExitosamente", "Contraseña modificada exitosamente.");
        
        try{
            usuarioService.modificarContraseña(username, email, claveActual, claveNueva, claveNuevaConfirmacion);
            return new RedirectView("/cuentaUsuario");
        }
        catch(ErrorServicio e){
            //System.out.println("\n"+ e.getMessage());
            model.addFlashAttribute("errorAlModificarClave", e.getMessage());
            return new RedirectView("/cuentaUsuario/#error-al-modificar-clave");
        }

    }
   
}
