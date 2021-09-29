package com.tiendagenerica.tienda.servicios;

import java.util.ArrayList;
import java.util.Optional;

import com.tiendagenerica.tienda.modelos.UsuarioModel;
import com.tiendagenerica.tienda.repositorios.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    String pass="admin123456";


    public boolean eliminarUsuario(Long id) {
        try{
            usuarioRepository.deleteById(id);
            return true;
        }catch(Exception err){
            return false;
        }
    }


    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return (ArrayList<UsuarioModel>)usuarioRepository.findAll();
    }

    public UsuarioModel editarUsuario(UsuarioModel u){
        u.setContrasenha(pass);
        return usuarioRepository.save(u);
    }

    public UsuarioModel InsertarUsuario(UsuarioModel u){
        u.setContrasenha(pass);
        return usuarioRepository.save(u);
    }

    
    public Optional<UsuarioModel> obtenerPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public Optional<UsuarioModel> login(String usuario,String contrasenha){
        return usuarioRepository.login(usuario,contrasenha);
        
    }

    


}
