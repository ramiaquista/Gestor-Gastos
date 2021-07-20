package com.egg.ProyectoFinal.repository;

import com.egg.ProyectoFinal.entity.Categoria;
import com.egg.ProyectoFinal.entity.Gasto;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("SELECT g FROM Gasto g WHERE g.fecha >= :desde AND g.fecha <= :hasta order by g.fecha")
    List<Gasto> buscarEntreFechas(@Param("desde") Date desde, @Param("hasta") Date hasta);

    @Query("SELECT sum(g.monto) FROM Gasto g WHERE g.fecha >= :desde AND g.fecha <= :hasta order by g.fecha")
    Double obtenerSumaTotalEntreFechas(@Param("desde") Date desde, @Param("hasta") Date hasta);
    
    @Modifying
    @Query("UPDATE Gasto g"
            + " SET g.monto = :monto,"
            + " g.categoria = :categoria,"
            + " g.fecha = :fecha,"
            + " g.comentario = :comentario"
            + " WHERE g.id = :id")
    void modificar(
            @Param("id") Long id,
            @Param("monto") Double monto,
            @Param("categoria") Categoria categoria,
            @Param("fecha") Date fecha,
            @Param("comentario") String comentario
            );
}
