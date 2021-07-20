
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.entity.Gasto;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.UsuarioService;
import com.egg.ProyectoFinal.service.CategoriaService;
import com.egg.ProyectoFinal.service.GastoService;
import com.egg.ProyectoFinal.service.GrupoCategoriaService;
import com.egg.ProyectoFinal.service.utils.Dates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

//     @Elián

@Controller
@RequestMapping("/gastos")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private GrupoCategoriaService grupoCategoriaService;
        
    @Autowired
    private CategoriaService categoriaService;
    

    // MÉTODOS
    
    @GetMapping
    public ModelAndView mostrarTodos(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("/gastos");
        
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        
        // Objetos/variables que se envían a la vista cuando se filtra por fecha:
        if (inputFlashMap != null) {
            mav.addObject("seSeleccionoFecha", inputFlashMap.get("seSeleccionoFecha"));
            mav.addObject("mesSeleccionado", inputFlashMap.get("mesSeleccionado"));
            mav.addObject("anioSeleccionado", inputFlashMap.get("anioSeleccionado"));
            mav.addObject("primerDiaMesSeleccionado", inputFlashMap.get("primerDiaMesSeleccionado"));
            mav.addObject("ultimoDiaMesSeleccionado", inputFlashMap.get("ultimoDiaMesSeleccionado"));
            mav.addObject("gastosMesSeleccionado", inputFlashMap.get("gastosMesSeleccionado"));
            mav.addObject("existenGastosMesSeleccionado", inputFlashMap.get("existenGastosMesSeleccionado"));
            mav.addObject("sumaTotalGastosMesSeleccionado", inputFlashMap.get("sumaTotalGastosMesSeleccionado"));
            mav.addObject("errorCrearGasto", inputFlashMap.get("errorCrearGasto"));
        }
        
        // Objetos/variables que se envían siempre a la vista:
            mav.addObject("grupos", grupoCategoriaService.buscarTodos());
            mav.addObject("categorias", categoriaService.buscarTodas());
            mav.addObject("mesActual", Dates.getCurrentMonthName());
            mav.addObject("anioActual", Dates.getCurrentYear());
            mav.addObject("primerDiaMesActual", Dates.getFirstOfCurrentMonth());
            mav.addObject("ultimoDiaMesActual", Dates.getLastOfCurrentMonth());
            mav.addObject("gastosMesActual", gastoService.buscarGastosMesActual());
            mav.addObject("existenGastosMesActual", gastoService.existenGastosMesActual());
            mav.addObject("sumaTotalGastosMesActual", gastoService.obtenerSumaTotalMesActual());
            // usuario
            mav.addObject("usuarioNombre", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getUsername());
            mav.addObject("usuarioId", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getId());
            mav.addObject("usuarioFoto", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getFotoPerfil());
        
        return mav;
    }

    @PostMapping("/crear")
    public RedirectView crear(
            RedirectAttributes model,
            @RequestParam Double monto,
            @RequestParam Long categoria_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam(required = false) Integer periodicidad,
            @RequestParam(required = false) Integer vecesARepetir,
            @RequestParam(required = false) String comentario)
            throws ErrorServicio {
        try {
            gastoService.crear(monto, fecha, periodicidad, vecesARepetir, categoria_id, comentario);
            return new RedirectView("/gastos");
        } 
        catch (ErrorServicio e) {
            model.addFlashAttribute("errorCrearGasto", e.getMessage());
            return new RedirectView("/gastos/#error-al-crear-gasto");
        }
//        gastoService.crear(monto, fecha, categoria_id, comentario);
    }

    @PostMapping("/modificar/{id}")
    public RedirectView modificar(
            @PathVariable Long id,
            @RequestParam Double monto,
            @RequestParam Long categoria_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam String comentario)
            throws ErrorServicio {

        gastoService.modificar(id, monto, categoria_id, fecha, comentario);
        return new RedirectView("/gastos");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarGasto(@PathVariable(value = "id") Long id){
        gastoService.eliminar(id);
        return new RedirectView("/gastos");
    }
    
    @PostMapping("/filtrarPorFecha")
    public RedirectView filtrarPorFecha(
            RedirectAttributes model,
            @RequestParam Integer anioSeleccionado,
            @RequestParam Integer mesSeleccionado) throws ErrorServicio {

        List<Gasto> gastosMesSeleccionado = 
                gastoService.buscarGastosMesEspecifico(anioSeleccionado, mesSeleccionado);
        
        model.addFlashAttribute("mesSeleccionado", Dates.getMonthName(mesSeleccionado));
        model.addFlashAttribute("anioSeleccionado", anioSeleccionado);
        model.addFlashAttribute("seSeleccionoFecha", (anioSeleccionado != null && mesSeleccionado != null));
        model.addFlashAttribute("primerDiaMesSeleccionado", Dates.getFirstOfMonth(anioSeleccionado, mesSeleccionado));
        model.addFlashAttribute("ultimoDiaMesSeleccionado", Dates.getLastOfMonth(anioSeleccionado, mesSeleccionado));
        model.addFlashAttribute("gastosMesSeleccionado", gastosMesSeleccionado);
        model.addFlashAttribute("existenGastosMesSeleccionado", gastoService.existenGastosMesEspecifico(anioSeleccionado, mesSeleccionado));
        model.addFlashAttribute("sumaTotalGastosMesSeleccionado", gastoService.obtenerSumaTotalMesEspecífico(anioSeleccionado, mesSeleccionado));

        return new RedirectView("/gastos");
    }
}
