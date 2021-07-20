
package com.egg.ProyectoFinal.repository;

import com.egg.ProyectoFinal.entity.Categoria;
import com.egg.ProyectoFinal.entity.Color;
import com.egg.ProyectoFinal.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


    //____________________BUSCAR POR NOMBRE__________________________
    @Query("SELECT c FROM Categoria c WHERE c.nombre like :nombre")
    Usuario buscarPorNombre(@Param("nombre") String nombre);


    //____________MODIFICAR EL NOMBRE DE LA CATEGORIA______________
    @Modifying
    @Query("UPDATE Categoria c"
            + " SET c.nombre = :nombre"
            + " WHERE c.id = :id")
    void modificarNombre(
            @Param("id") Long id,
            @Param("nombre") String nombre
    );

    //____________MODIFICAR EL COLOR DE LA CATEGORIA______________
    @Modifying
    @Query("UPDATE Categoria c SET c.color = :color WHERE c.id = :id")
    void modificarColor(@Param("id") Long id, @Param("color") Color color);

    @Modifying
    @Query("UPDATE Categoria c SET c.color = :color WHERE c.grupoCategoria.id = :id")
    void actualizarColor(@Param("color") Color color, @Param("id") Long id);

}
