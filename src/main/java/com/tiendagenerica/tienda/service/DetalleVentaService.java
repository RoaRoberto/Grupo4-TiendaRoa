package com.tiendagenerica.tienda.service;

import com.tiendagenerica.tienda.entity.DetalleVenta;
import com.tiendagenerica.tienda.entity.Venta;
import com.tiendagenerica.tienda.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService {

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    public List<DetalleVenta> lista() {
        return detalleVentaRepository.findAll();
    }

    public Optional<DetalleVenta> getById(int id) {
        return detalleVentaRepository.findById(id);
    }

    public void save(DetalleVenta venta) {
        detalleVentaRepository.save(venta);
    }


}
