package com.tiendagenerica.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.tiendagenerica.tienda.entity.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
	Optional<Proveedor> findByNit(int nit);
    boolean existsByNit(int nit);

}
