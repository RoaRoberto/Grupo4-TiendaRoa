package com.tiendagenerica.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendagenerica.tienda.entity.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	Optional<Cliente> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

}
