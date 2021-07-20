package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Categoria;
import com.egg.ProyectoFinal.entity.Color;
import com.egg.ProyectoFinal.entity.Gasto;
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

import java.util.Optional;
import java.util.List;


@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private GastoRepository gastoRepository;
    
    @Autowired
    private ColorRepository colorRepository;
    
    @Autowired
    private GrupoCategoriaRepository grupoRepository;

    // MÉTODOS
    
    @Transactional
    public void crear(String nombre, Long grupo_id, Long color_id) throws ErrorServicio {

        Categoria categoria = new Categoria();

        validar(nombre, color_id, grupo_id);

        categoria.setNombre(nombre);
        categoria.setColor(colorRepository.findById(color_id).orElse(null));
        categoria.setGrupoCategoria(grupoRepository.findById(grupo_id).orElse(null));

        categoriaRepository.save(categoria);

    }

    @Transactional
    public void modificar(Long id, String nombre, Long grupo_id, Long color_id) throws ErrorServicio{

        Optional<Categoria> respuesta = categoriaRepository.findById(id);

        validar(nombre, color_id, grupo_id);

        if (respuesta.isPresent()){
            Categoria categoria = respuesta.get();
            
            categoria.setNombre(nombre);
            categoria.setColor(colorRepository.findById(color_id).orElse(null));
            categoria.setGrupoCategoria(grupoRepository.findById(grupo_id).orElse(null));
            
//            categoria.setColor(color);
//            categoria.setGrupoCategoria(grupoCategoria);
            
            categoriaRepository.save(categoria);
        } else {
            throw new ErrorServicio("Error al editar la categoria");
        }
    }

    @Transactional
    public void eliminar(Long id){
        categoriaRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Categoria> buscarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public void calcularGastoTotalDeCategoriaMesEspecífico(Long categoria_id, int anio, int mes){
        Double suma = 0.;
        
        Optional<Categoria> categoria = categoriaRepository.findById(categoria_id);
        
        if (categoria.isPresent()){
            
            for (Gasto gasto : categoria.get().getGastos()) {
                    Date firstOfMonth = Dates.getFirstOfMonth(anio, mes);
                    Date lastOfMonth = Dates.getLastOfMonth(anio, mes);
                    
                    List<Gasto> gastosDelMes = gastoRepository.buscarEntreFechas(firstOfMonth, lastOfMonth);
                    
                    if (gastosDelMes.contains((gasto))){
                        suma += gasto.getMonto();
                    }
            }
            categoria.get().setGastoTotalMesConsultado(suma);
        }    
    }
    
    
    @Transactional(readOnly = true)
    public void calcularGastoTotalDeTodasLasCategoriasMesEspecífico(int anio, int mes){
        
        List<Categoria> categorias = categoriaRepository.findAll();
        
        if (categorias.size() > 0){
            for (Categoria categoria : categorias) {
                calcularGastoTotalDeCategoriaMesEspecífico(categoria.getId(), anio, mes);
            }
        }
    }
//    @Transactional(readOnly = true)
//    public Double totalGastosCategoria(Long categoria_id) {
//        Double suma = 0.0;
//        Optional<Categoria> categoria = categoriaRepository.findById(categoria_id);
//        if (categoria.isPresent()){
//            List<Gasto> gastos = categoria.get().getGastos();
//            for (Gasto gasto : gastos) {
//                suma += gasto.getMonto();
//            }
//        }
//        return suma;
//    }

    
    //VALIDACIONES

    private void validar(String nombre, Long color_id, Long grupoCategoria_id) throws ErrorServicio{

        if (nombre == null){
            throw new ErrorServicio("Ingrese un nombre");
        }else if (nombre.length() > 35){
            throw new ErrorServicio("El nombre ingresado es demasiado largo");
        }

        if (color_id == null){
            throw new ErrorServicio("Ingrese un color");
        }

        if (grupoCategoria_id == null){
            throw new ErrorServicio("No se ha detectado un grupo");
        }
    }

}
