package com.tiendagenerica.tienda.entity;


import com.sun.istack.NotNull;
import javax.persistence.*;


@Entity
public class Cliente {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    @NotNull
	    @Column(unique = true)
	    
	    private int cedula;
	    private String nombre;
	    private String direccion;
	    private int telefono;
	    private String email;
	    
	    
	    
	    
		public Cliente() {
			
		}
		public Cliente(int cedula, String nombre, String direccion, int telefono, String email) {
			this.cedula = cedula;
			this.nombre = nombre;
			this.direccion = direccion;
			this.telefono = telefono;
			this.email = email;
			
		
			
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getCedula() {
			return cedula;
		}
		public void setCedula(int cedula) {
			this.cedula = cedula;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getDireccion() {
			return direccion;
		}
		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}
		public int getTelefono() {
			return telefono;
		}
		public void setTelefono(int telefono) {
			this.telefono = telefono;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	    
	    
	    

}
