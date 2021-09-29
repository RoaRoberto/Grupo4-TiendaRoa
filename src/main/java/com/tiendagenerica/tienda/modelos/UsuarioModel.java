package com.tiendagenerica.tienda.modelos;
import javax.persistence.*;



@Entity
@Table(name="usuarios",uniqueConstraints=@UniqueConstraint(columnNames={"cedula","usuario"}))
public class UsuarioModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCompleto;
    
    
    private String cedula;
    private String correo;

    
    private String usuario;
    private String contrasenha;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public String getCedula() {
        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getContrasenha() {
        return contrasenha;
    }
    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    
    
}
