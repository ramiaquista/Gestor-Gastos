package com.egg.ProyectoFinal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Proxy;

@Entity
public class Categoria implements Serializable {

    //__________ Atributos __________
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    @ManyToOne
    private GrupoCategoria grupoCategoria;

    @OneToOne
    private Color color;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", cascade = {CascadeType.REMOVE})
    private List<Gasto> gastos;
    
    private Double gastoTotal;
    private Double gastoTotalMesConsultado;

    
    
    //__________ Constructores __________
    
    public Categoria() {
        this.gastos = new ArrayList<>();
        gastoTotal = calcularGastoTotal();
    }

    public Categoria(String nombre, Color color, GrupoCategoria grupoCategoria) {
        this.nombre = nombre;
        this.color = color;
        this.grupoCategoria = grupoCategoria;
        this.gastos = new ArrayList<>();
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

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    public void nuevoGasto(Gasto gasto){
        this.gastos.add(gasto);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public GrupoCategoria getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(GrupoCategoria grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public Double getGastosTotales() {
        return calcularGastoTotal();
    }

    public void setSumaTotalGastos(Double gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

    public Double getGastoTotalMesConsultado() {
        return gastoTotalMesConsultado;
    }

    public void setGastoTotalMesConsultado(Double gastoTotalMesConsultado) {
        this.gastoTotalMesConsultado = gastoTotalMesConsultado;
    }

    
    
    // otros métodos
    
    private Double calcularGastoTotal(){
        Double suma = 0.;
        for (Gasto gasto : this.gastos) {
           suma += gasto.getMonto();
        }
        return suma;
    }
    
//    @Override
//    public String toString() {
//        return "Categoria{" + "id=" + id + ", nombre=" + nombre + ", grupoCategoria=" + grupoCategoria + ", color=" + color + '}';
//    }
    
    
}
