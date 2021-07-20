package com.egg.ProyectoFinal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class GrupoCategoria implements Serializable {
    
    //__________ Atributos __________
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    @OneToOne
    private Color color;
    
    @JsonIgnore
    @OneToMany(mappedBy = "grupoCategoria", cascade = {CascadeType.ALL})
    private List<Categoria> listaCategorias;
    
    private Double gastoTotalMesConsultado;
    
    
    //__________ Constructores __________
    
    public GrupoCategoria() { 
        this.listaCategorias = new ArrayList<>();
    }

    public GrupoCategoria(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
        this.listaCategorias = new ArrayList<>();
    }

    
    //__________ Getters & Setters __________
    
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


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Categoria> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public Boolean tieneGastosAsociados(){
        for (Categoria categoria : listaCategorias) {
            if (categoria.getGastos().size() > 0) {
                return true;
            }
        }
        return false;
    }

    public Double getGastoTotalMesConsultado() {
        return gastoTotalMesConsultado;
    }

    public void setGastoTotalMesConsultado(Double gastoTotalMesConsultado) {
        this.gastoTotalMesConsultado = gastoTotalMesConsultado;
    }
    
//    @Override
//    public String toString() {
//        return "id:" + id + ", nombre:" + nombre + ", color:" + color;
//    }
    
}
