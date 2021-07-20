
package com.egg.ProyectoFinal.repository;

import com.egg.ProyectoFinal.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    

}
