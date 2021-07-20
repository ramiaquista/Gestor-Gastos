package com.egg.ProyectoFinal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Gasto implements Serializable {

    //__________ Atributos __________
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monto;
    private String comentario;
    private Boolean fuePagado;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToOne // Varios gastos pueden tener una misma categoría
    private Categoria categoria;


    //__________ Constructores __________
    
    public Gasto() {
        comentario = "";
        fuePagado = false;
    }

    public Gasto(Double monto, String comentario, Categoria categoria, Date fecha, Boolean fuePagado) {
        this.monto = monto;
        this.comentario = comentario;
        this.categoria = categoria;
        this.fecha = fecha;
        //this.fuePagado = fuePagado;
    }

    //__________ Getters & Setters __________
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean fuePagado() {
        return fuePagado;
    }

    public void setFuePagado(Boolean fuePagado) {
        this.fuePagado = fuePagado;
    }

//    @Override
//    public String toString() {
//        return "Gasto{" + "id=" + id + ", monto=" + monto + ", comentario=" + comentario + ", fuePagado=" + fuePagado + ", fecha=" + fecha + ", categoria=" + categoria + '}';
//    }
    
    
}
