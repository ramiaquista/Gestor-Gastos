package com.egg.ProyectoFinal.repository;

import com.egg.ProyectoFinal.entity.Categoria;
import com.egg.ProyectoFinal.entity.Color;
import com.egg.ProyectoFinal.entity.GrupoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoCategoriaRepository extends JpaRepository<GrupoCategoria, Long> {

    @Modifying
    @Query("UPDATE GrupoCategoria g"
            + " SET g.nombre = :nombre,"
            + " g.color = :color,"
            + " g.listaCategorias = :listaCategorias"
            + " WHERE g.id = :id")
    void modificar(
            @Param("id") Long id,
            @Param("nombre") String nombre,
            @Param("color") Color color,
            @Param("listaCategorias") List<Categoria> listaCategorias
    );

}
