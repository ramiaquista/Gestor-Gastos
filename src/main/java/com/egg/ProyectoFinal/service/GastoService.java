package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Gasto;
import com.egg.ProyectoFinal.errores.ErrorServicio;
import com.egg.ProyectoFinal.repository.CategoriaRepository;
import com.egg.ProyectoFinal.repository.GastoRepository;
import com.egg.ProyectoFinal.service.utils.Dates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

   // MÉTODOS
    
    @Transactional
    public void crear(Double monto, Date fecha, Integer periodicidad, Integer vecesARepetir,
            Long categoria_id, String comentario) throws ErrorServicio {

        validar(monto, categoria_id, comentario, fecha);

        Gasto gasto = new Gasto();

        gasto.setMonto(monto);
        gasto.setFecha(fecha);
        gasto.setCategoria(categoriaRepository.findById(categoria_id).orElse(null));
        gasto.setComentario(comentario);

        gastoRepository.save(gasto);
        
        if (periodicidad != null && vecesARepetir != null) {
            // programo gasto recurrente

            if (periodicidad == 30) {
                periodicidad = Dates.daysInMonth(fecha);
                
                for (int i = 0; i < vecesARepetir; i++) {
                    Date proximaFecha = Dates.getDatePlusDays(fecha, periodicidad);

                    Gasto gastoRecurrente = new Gasto();

                    gastoRecurrente.setMonto(monto);
                    gastoRecurrente.setFecha(proximaFecha);
                    gastoRecurrente.setCategoria(categoriaRepository.findById(categoria_id).orElse(null));
                    gastoRecurrente.setComentario(comentario);
                    gastoRepository.save(gastoRecurrente);

                    fecha = proximaFecha;
                    periodicidad = Dates.daysInMonth(fecha);
                }
            } else {
            
                for (int i = 0; i < vecesARepetir; i++) {
                    Date proximaFecha = Dates.getDatePlusDays(fecha, periodicidad);

                    Gasto gastoRecurrente = new Gasto();

                    gastoRecurrente.setMonto(monto);
                    gastoRecurrente.setFecha(proximaFecha);
                    gastoRecurrente.setCategoria(categoriaRepository.findById(categoria_id).orElse(null));
                    gastoRecurrente.setComentario(comentario);
                    gastoRepository.save(gastoRecurrente);

                    fecha = proximaFecha;
                }
            }
        }
    }

    @Transactional
    public void modificar(Long id, Double monto, Long categoria_id, Date fecha, String comentario) throws ErrorServicio{

        validar(monto, categoria_id, comentario, fecha);

        Optional<Gasto> respuesta = gastoRepository.findById(id);
        if (respuesta.isPresent()) {
            Gasto gasto = respuesta.get();
            gasto.setMonto(monto);
            gasto.setFecha(fecha);
            gasto.setComentario(comentario);

            gasto.setCategoria(categoriaRepository.findById(categoria_id).orElse(null));

            gastoRepository.save(gasto);

        } else {
            throw new ErrorServicio("El id del gasto que se intenta editar no existe.");
        }
    }

    @Transactional
    public void eliminar(Long id){
        gastoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Gasto> buscarTodos() {
        return gastoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Gasto> buscarGastosMesActual() {
        Date firstOfMonth = Dates.getFirstOfCurrentMonth();
        Date lastOfMonth = Dates.getLastOfCurrentMonth();
        
        return gastoRepository.buscarEntreFechas(firstOfMonth, lastOfMonth);
    }

    @Transactional(readOnly = true)
    public List<Gasto> buscarGastosMesEspecifico(int anio, int mes) {
        Date firstOfMonth = Dates.getFirstOfMonth(anio, mes);
        Date lastOfMonth = Dates.getLastOfMonth(anio, mes);
        
        return gastoRepository.buscarEntreFechas(firstOfMonth, lastOfMonth);
    }
    
    @Transactional(readOnly = true)
    public Boolean existenGastosMesActual() {
        return buscarGastosMesActual().size() > 0;
    }
    
    @Transactional(readOnly = true)
    public Boolean existenGastosMesEspecifico(int anio, int mes) {
        return buscarGastosMesEspecifico(anio, mes).size() > 0;
    }

    @Transactional(readOnly = true)
    public List<Gasto> buscarEntreFechas(Date desde, Date hasta) {
        return gastoRepository.buscarEntreFechas(desde, hasta);
    }
    
    @Transactional(readOnly = true)
    public Gasto buscarPorId(Long id) {
        Optional<Gasto> usuarioOptional = gastoRepository.findById(id);
        return usuarioOptional.orElse(null); 
    }

    @Transactional(readOnly = true)
    public Double obtenerSumaTotalMesActual() {
        Date firstOfMonth = Dates.getFirstOfCurrentMonth();
        Date lastOfMonth = Dates.getLastOfCurrentMonth();
        
        return gastoRepository.obtenerSumaTotalEntreFechas(firstOfMonth, lastOfMonth);
    }
    
    @Transactional(readOnly = true)
    public Double obtenerSumaTotalMesEspecífico(int anio, int mes) {
        Date firstOfMonth = Dates.getFirstOfMonth(anio, mes);
        Date lastOfMonth = Dates.getLastOfMonth(anio, mes);
        
        return gastoRepository.obtenerSumaTotalEntreFechas(firstOfMonth, lastOfMonth);
    }

    @Transactional(readOnly = true)
    public Double[] obtenerGastosTotalesPorMesAño(int anio) {
        Double[] gastosMes = new Double[12];
        for (int i = 1; i <= 12; i++) {
            Date firstOfMonth = Dates.getFirstOfMonth(anio, i);
            Date lastOfMonth = Dates.getLastOfMonth(anio, i);
            gastosMes[i] = gastoRepository.obtenerSumaTotalEntreFechas(firstOfMonth, lastOfMonth);
        }
        
        return gastosMes;
    }

    @Transactional(readOnly = true)
    public Double[] obtenerGastosTotalesPorMesAñoActual() {
        Double[] gastosMes = new Double[12];
        int anioActual = Dates.getCurrentYear();
        for (int i = 0; i < 12; i++) {
            Date firstOfMonth = Dates.getFirstOfMonth(anioActual, i + 1);
            Date lastOfMonth = Dates.getLastOfMonth(anioActual, i + 1);
            
            Double sumaTotal = gastoRepository.obtenerSumaTotalEntreFechas(firstOfMonth, lastOfMonth);
            sumaTotal = ((sumaTotal != null) ? sumaTotal : 0.0);
            
            gastosMes[i] = sumaTotal;
        }
        
        return gastosMes;
    }
    
    // Otros métodos / validaciones
    
    private void validar(Double monto, Long categoria_id, String comentario, Date fecha) throws ErrorServicio{

        validarFecha(fecha);

        if (monto < 0 || monto == null){
            throw new ErrorServicio("El monto debe ser un número positivo");
        }

        if (categoria_id == null) {
            throw new ErrorServicio("La categoria no puede ser nulao");
        }
        
        // El comentario es opcional :)
        if (comentario != null && comentario.length() > 20){
            throw new ErrorServicio("La longitud del comentario no debe superar los 20 caracteres.");
        }

    }

    private Date validarFecha(Date fecha){ // revisar
        if (fecha == null) {
            fecha = new Date(); 
        } 
        return fecha;
    }


}
