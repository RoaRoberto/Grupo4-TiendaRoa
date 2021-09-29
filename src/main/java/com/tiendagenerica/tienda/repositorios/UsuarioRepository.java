package com.tiendagenerica.tienda.repositorios;

import java.util.Optional;

import com.tiendagenerica.tienda.modelos.UsuarioModel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

   
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Long> {
    
    @Query (nativeQuery = true, value ="select * from usuarios where usuario=:usuario and contrasenha=:contrasenha LIMIT 1")
    public abstract Optional<UsuarioModel> login(String usuario,String contrasenha);

    
}
