
package com.egg.ProyectoFinal.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Color implements Serializable{
    
    //__________ Atributos __________
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // modo de color necesario: hsl (hue-saturation-luminosity)
    private String nombre;
    private Integer h; 
    private Integer s;  
    private Integer l;  


    //__________ Constructores __________
    
    public Color() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Integer getL() {
        return l;
    }

    public void setL(Integer l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return "Color{" + "id=" + id + ", nombre=" + nombre + ", h=" + h + ", s=" + s + ", l=" + l + '}';
    }
    
    
    
}
