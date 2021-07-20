
package com.egg.ProyectoFinal.service;

import com.egg.ProyectoFinal.entity.Color;
import com.egg.ProyectoFinal.repository.ColorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColorService {
    
    @Autowired
    private ColorRepository colorRepository;
    
    @Transactional(readOnly = true)
    public List<Color> buscarTodos() {
        return colorRepository.findAll();
    }
}
