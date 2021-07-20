package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Categoria;
import com.egg.ProyectoFinal.entity.Gasto;
import com.egg.ProyectoFinal.entity.Color;
import com.egg.ProyectoFinal.entity.GrupoCategoria;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.repository.CategoriaRepository;
import com.egg.ProyectoFinal.repository.ColorRepository;
import com.egg.ProyectoFinal.repository.GastoRepository;
import com.egg.ProyectoFinal.repository.GrupoCategoriaRepository;
import com.egg.ProyectoFinal.service.utils.Dates;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoCategoriaService {

    @Autowired
    private GrupoCategoriaRepository grupoRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private GastoRepository gastoRepository;

    // CRUD

    @Transactional
    public void nuevoGrupoCategoria(String nombre, Long color_id) {

        GrupoCategoria grupoCategoria = new GrupoCategoria();

        grupoCategoria.setNombre(nombre);
        grupoCategoria.setColor(colorRepository.findById(color_id).orElse(null));

        grupoRepository.save(grupoCategoria);
    }

    @Transactional(readOnly = true)
    public List<GrupoCategoria> buscarTodos() {
        return grupoRepository.findAll();
    }

    @Transactional
    public void modificar(Long id, String nombre, Long color_id) throws ErrorServicio {

        Optional<GrupoCategoria> respuestaGrupo = grupoRepository.findById(id);
        Optional<Color> respuestaColor = colorRepository.findById(color_id);

        validar(nombre, color_id);

        if (respuestaGrupo.isPresent() && respuestaColor.isPresent()) {
            GrupoCategoria grupoCategoria = respuestaGrupo.get();
            grupoCategoria.setNombre(nombre);
            grupoCategoria.setColor(respuestaColor.get());
            
            categoriaRepository.actualizarColor(respuestaColor.get(), id);
            grupoRepository.save(grupoCategoria);
        } else {
            throw new ErrorServicio("Error al modificar el Grupo");
        }

    }

    @Transactional
    public void eliminar(Long id) {
        grupoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Boolean tieneGastosAsociados(GrupoCategoria grupo){
        return grupo.tieneGastosAsociados();
    }
    
    @Transactional
    public void calcularGastoTotalDeGrupoMesEspecífico(Long grupo_id, int anio, int mes){
        Double suma = 0.;
        
        Optional<GrupoCategoria> grupo = grupoRepository.findById(grupo_id);
        
        if (grupo.isPresent()){
            
            for (Categoria categoria : grupo.get().getListaCategorias()) {
                for (Gasto gasto : categoria.getGastos()) {
                    Date firstOfMonth = Dates.getFirstOfMonth(anio, mes);
                    Date lastOfMonth = Dates.getLastOfMonth(anio, mes);
                    
                    List<Gasto> gastosDelMes = gastoRepository.buscarEntreFechas(firstOfMonth, lastOfMonth);
                    
                    if (gastosDelMes.contains((gasto))){
                        suma += gasto.getMonto();
                    }
                }
            }
            grupo.get().setGastoTotalMesConsultado(suma);
        }    
    }
    
//    public Set<Entry<GrupoCategoria, Double>> asd(int anio, int mes){
//        
//        Map<GrupoCategoria, Double> mapa = new HashMap<>();
//        
//        List<GrupoCategoria> grupos = grupoRepository.findAll();
//        
//        if (grupos.size() > 0){
//        
//            for (GrupoCategoria grupo : grupos) {
//                Double gastosTotales = calcularGastoTotalDeGrupoMesEspecífico(grupo.getId(), anio, mes);
//
//                mapa.put(grupo, gastosTotales);
//            }
//        }
//        return mapa.entrySet();
//    }
    
    @Transactional(readOnly = true)
    public void calcularGastoTotalDeTodosLosGruposMesEspecífico(int anio, int mes){
        
        List<GrupoCategoria> grupos = grupoRepository.findAll();
        
        if (grupos.size() > 0){
        
            for (GrupoCategoria grupo : grupos) {
                calcularGastoTotalDeGrupoMesEspecífico(grupo.getId(), anio, mes);
            }
        }
    }
    
    // validaciones
    private void validar(String nombre, Long color_id) throws ErrorServicio {

        if (nombre == null) {
            throw new ErrorServicio("Ingrese un nombre");
        } else if (nombre.length() > 35) {
            throw new ErrorServicio("El nombre ingresado es demasiado largo");
        }

        if (color_id == null) {
            throw new ErrorServicio("Elija un color");
        }
    }
}
