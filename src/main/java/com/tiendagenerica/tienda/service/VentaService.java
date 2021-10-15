package com.tiendagenerica.tienda.service;

import com.tiendagenerica.tienda.entity.Venta;
import com.tiendagenerica.tienda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService {

    @Autowired
    VentaRepository ventaRepository;

    public List<Venta> lista() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> getById(int id) {
        return ventaRepository.findById(id);
    }

    public void save(Venta venta) {
        ventaRepository.save(venta);
    }


}
