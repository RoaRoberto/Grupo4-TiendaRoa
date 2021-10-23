package com.tiendagenerica.tienda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tiendagenerica.tienda.entity.Cliente;

import com.tiendagenerica.tienda.entity.Usuario;

import com.tiendagenerica.tienda.service.ClienteService;

import com.tiendagenerica.tienda.service.UsuarioService;

@Controller
@RequestMapping("/reporte")

public class ReporteController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ClienteService clienteService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("nuevo")
    public String nuevo() {
        return "reporte/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("ReporteCliente")
    public ModelAndView listCliente() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/reporte/ReporteCliente");
        List<Cliente> clientes = clienteService.list();
        mv.addObject("clientes", clientes);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("ReporteUsuario")
    public ModelAndView listUsuario() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/reporte/ReporteUsuario");
        List<Usuario> usuarios = usuarioService.list();
        mv.addObject("usuarios", usuarios);
        return mv;
    }

    // esta parte rellena el informe
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("ReporteVentasPorCliente")
    public String reporteVentasPorCliente() {
        return "reporte/ReporteVentasPorCliente";
    }

}