package com.tiendagenerica.tienda.controller;

import com.tiendagenerica.tienda.entity.*;
import com.tiendagenerica.tienda.service.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    VentaService ventaService;

    @Autowired
    ProductoService productoService;

   

    @GetMapping("lista")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/venta/lista");
        List<Venta> ventas = ventaService.lista();
        mv.addObject("ventas", ventas);
        return mv;
    }

    @GetMapping("crear")
    public String registro(Model model) {
        List<Producto> productos=productoService.list();
        model.addAttribute("venta", new Venta());
        model.addAttribute("detalleVenta", new DetalleVenta());
        model.addAttribute("productos", productos);
        return "/venta/crear";
    }
}
