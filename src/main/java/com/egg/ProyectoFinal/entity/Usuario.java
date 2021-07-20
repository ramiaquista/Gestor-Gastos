package com.egg.ProyectoFinal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario implements Serializable {

    //__________ Atributos __________
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String clave;
    
    @OneToOne
    private Foto fotoPerfil;
    
    @Column(name = "verification_code", length = 64)
    private String verificationCode;
     
    private boolean enabled;

    //__________ Constructores __________
    
    public Usuario() {
        enabled = false;
    }

    public Usuario(String username, String email, String clave) {
        this.username = username;
        this.email = email;
        this.clave = clave;
        enabled = false;
    }


    //__________ Getters & Setters __________
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Foto getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Foto fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
