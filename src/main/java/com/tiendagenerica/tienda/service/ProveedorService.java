package com.tiendagenerica.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.tiendagenerica.tienda.entity.Proveedor;

import com.tiendagenerica.tienda.repository.ProveedorRepository;

@Service
@Transactional
public class ProveedorService {
	
	@Autowired
    ProveedorRepository  proveedorRepository;
	
	public List<Proveedor> list(){
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> getOne(int id){
        return proveedorRepository.findById(id);
    }

    public Optional<Proveedor> getByNit(int nit){
        return proveedorRepository.findByNit(nit);
    }

    public void  save(Proveedor Proveedor){
    	proveedorRepository.save(Proveedor);
    }

    public void delete(int id){
    	proveedorRepository.deleteById(id);
    }

    public boolean existsById(int id){
        return proveedorRepository.existsById(id);
    }

    public boolean existsByNit(int nit){
        return proveedorRepository.existsByNit(nit);
    }

}
