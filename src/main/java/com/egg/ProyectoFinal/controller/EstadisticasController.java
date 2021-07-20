
package com.egg.ProyectoFinal.controller;

import com.egg.ProyectoFinal.entity.Gasto;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.service.CategoriaService;
import com.egg.ProyectoFinal.service.GastoService;
import com.egg.ProyectoFinal.service.GrupoCategoriaService;
import com.egg.ProyectoFinal.service.UsuarioService;
import com.egg.ProyectoFinal.service.utils.Dates;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticasController {
    
        
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private GastoService gastoService;
    
    @Autowired
    private GrupoCategoriaService grupoCategoriaService;
    
    @Autowired
    private CategoriaService categoriaService;

    // variables globales
    private Boolean seSeleccionoFecha;
    private Integer mesConsultado;
    private Integer anioConsultado;
    
    // constantes
    private final int CURRENT_YEAR = Dates.getCurrentYear();
    private final int CURRENT_MONTH = Dates.getCurrentMonth();
    
    
    
    @GetMapping
    public ModelAndView mostrar(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("/estadisticas");
        
        seSeleccionoFecha = false;
        
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        
        if (inputFlashMap != null) {
            seSeleccionoFecha = true;
            
            mav.addObject("seSeleccionoFecha", inputFlashMap.get("seSeleccionoFecha"));
            mav.addObject("mesSeleccionado", inputFlashMap.get("mesSeleccionado"));
            mav.addObject("anioSeleccionado", inputFlashMap.get("anioSeleccionado"));
//            mav.addObject("primerDiaMesSeleccionado", inputFlashMap.get("primerDiaMesSeleccionado"));
//            mav.addObject("ultimoDiaMesSeleccionado", inputFlashMap.get("ultimoDiaMesSeleccionado"));
            mav.addObject("gastosMesSeleccionado", inputFlashMap.get("gastosMesSeleccionado"));
//            mav.addObject("existenGastosMesSeleccionado", inputFlashMap.get("existenGastosMesSeleccionado"));
            mav.addObject("sumaTotalGastosMesSeleccionado", inputFlashMap.get("sumaTotalGastosMesSeleccionado"));
            
            if (seSeleccionoFecha) {
                grupoCategoriaService.calcularGastoTotalDeTodosLosGruposMesEspecífico(anioConsultado, mesConsultado);
                categoriaService.calcularGastoTotalDeTodasLasCategoriasMesEspecífico(anioConsultado, mesConsultado);
                mav.addObject("grupos", grupoCategoriaService.buscarTodos());
            }
            
        }

        if (!seSeleccionoFecha) {
            grupoCategoriaService.calcularGastoTotalDeTodosLosGruposMesEspecífico(CURRENT_YEAR, CURRENT_MONTH);
            categoriaService.calcularGastoTotalDeTodasLasCategoriasMesEspecífico(CURRENT_YEAR, CURRENT_MONTH);
            mav.addObject("grupos", grupoCategoriaService.buscarTodos());
        } 
        
            mav.addObject("categorias", categoriaService.buscarTodas());
            mav.addObject("mesActual", Dates.getCurrentMonthName());
            mav.addObject("anioActual", Dates.getCurrentYear());
            mav.addObject("gastosMesActual", gastoService.buscarGastosMesActual());
            mav.addObject("sumaTotalGastosMesActual", gastoService.obtenerSumaTotalMesActual());
            mav.addObject("gastos", gastoService.buscarTodos());
            mav.addObject("gastosAnualesPorMes", gastoService.obtenerGastosTotalesPorMesAñoActual());
            mav.addObject("usuarioNombre", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getUsername());
            mav.addObject("usuarioId", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getId());
            mav.addObject("usuarioFoto", usuarioService.buscarPorEmail(request.getUserPrincipal().getName()).getFotoPerfil());

        return mav;
    }
    
    
    @PostMapping("/filtrarPorFecha")
    public RedirectView filtrarPorFecha(
            RedirectAttributes model,
            @RequestParam Integer anioSeleccionado,
            @RequestParam Integer mesSeleccionado) throws ErrorServicio {

        List<Gasto> gastosMesSeleccionado = 
                gastoService.buscarGastosMesEspecifico(anioSeleccionado, mesSeleccionado);
        
        grupoCategoriaService.calcularGastoTotalDeTodosLosGruposMesEspecífico(anioSeleccionado, mesSeleccionado);
        categoriaService.calcularGastoTotalDeTodasLasCategoriasMesEspecífico(anioSeleccionado, mesSeleccionado);
        
        if (anioSeleccionado != null && mesSeleccionado != null){
            seSeleccionoFecha = true;
            anioConsultado = anioSeleccionado;
            mesConsultado = mesSeleccionado;
        } 
        
        model.addFlashAttribute("mesSeleccionado", Dates.getMonthName(mesSeleccionado));
        model.addFlashAttribute("anioSeleccionado", anioSeleccionado);
        model.addFlashAttribute("seSeleccionoFecha", seSeleccionoFecha);
//        model.addFlashAttribute("primerDiaMesSeleccionado", Dates.getFirstOfMonth(anioSeleccionado, mesSeleccionado));
//        model.addFlashAttribute("ultimoDiaMesSeleccionado", Dates.getLastOfMonth(anioSeleccionado, mesSeleccionado));
        model.addFlashAttribute("gastosMesSeleccionado", gastosMesSeleccionado);
//        model.addFlashAttribute("existenGastosMesSeleccionado", gastoService.existenGastosMesEspecifico(anioSeleccionado, mesSeleccionado));
        model.addFlashAttribute("sumaTotalGastosMesSeleccionado", gastoService.obtenerSumaTotalMesEspecífico(anioSeleccionado, mesSeleccionado));

        return new RedirectView("/estadisticas");
    }
}
