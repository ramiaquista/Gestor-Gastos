
package com.egg.ProyectoFinal.repository;

import com.egg.ProyectoFinal.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    

}
