package com.tiendagenerica.tienda.repository;

import com.tiendagenerica.tienda.entity.Venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
}
