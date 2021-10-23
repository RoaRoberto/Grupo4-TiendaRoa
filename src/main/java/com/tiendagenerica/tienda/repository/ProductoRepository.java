package com.tiendagenerica.tienda.repository;

import com.tiendagenerica.tienda.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    @Query(value = "select p.* from detalle_venta dv inner join producto p on dv.producto_id =p.id where dv.venta_id =:idVenta", nativeQuery = true)
    Collection<Producto> getProductosVenta(@Param("idVenta") Integer idVenta);
}
