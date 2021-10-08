package com.tiendagenerica.tienda.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.tiendagenerica.tienda.entity.Proveedor;
import com.tiendagenerica.tienda.service.ProveedorService;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {
	
	@Autowired
    ProveedorService proveedorService;

    @GetMapping("lista")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/proveedor/lista");
        List<Proveedor> proveedor = proveedorService.list();
        mv.addObject("proveedor", proveedor);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("nuevo")
    public String nuevo() {
        return "proveedor/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public ModelAndView crear(@RequestParam int nit, @RequestParam String nombre, @RequestParam String direccion, @RequestParam  int telefono, @RequestParam String ciudad) {
        ModelAndView mv = new ModelAndView();
        if (StringUtils.isBlank(nombre)) {
            mv.setViewName("proveedor/nuevo");
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
    
        if (proveedorService.existsById(nit)) {
            mv.setViewName("proveedor/nuevo");
            mv.addObject("error", "NIT ya Registrado en el Sistema");
            return mv;
        }
        Proveedor proveedor = new Proveedor(nit, nombre, direccion, telefono, ciudad);
        proveedorService.save(proveedor);
        mv.setViewName("redirect:/proveedor/lista");
        return mv;
    }

    @GetMapping("/detalle/{id}")
    public ModelAndView detalle(@PathVariable("id") int id) {
        if (!proveedorService.existsById(id))
            return new ModelAndView("redirect:/proveedor/lista");
        Proveedor proveedor = proveedorService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/proveedor/detalle");
        mv.addObject("proveedor", proveedor);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") int id) {
        if (!proveedorService.existsById(id))
            return new ModelAndView("redirect:/proveedor/lista");
        Proveedor proveedor = proveedorService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/proveedor/editar");
        mv.addObject("proveedor", proveedor);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/actualizar")
    public ModelAndView actualizar(@RequestParam int id, @RequestParam int nit, @RequestParam String nombre, @RequestParam String direccion, @RequestParam  int telefono, @RequestParam String ciudad) {
        if (!proveedorService.existsById(id))
            return new ModelAndView("redirect:/proveedor/lista");
        ModelAndView mv = new ModelAndView();
        Proveedor proveedor = proveedorService.getOne(id).get();
        if (StringUtils.isBlank(nombre)) {
            mv.setViewName("proveedor/editar");
            mv.addObject("proveedor", proveedor);
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
        
        if (proveedorService.existsByNit(nit) && proveedorService.getByNit(nit).get().getId() != id) {
            mv.setViewName("proveedor/editar");
            mv.addObject("error", "NIT ya existe");
            mv.addObject("proveedor", proveedor);
            return mv;
        }
        
        proveedor.setNit(nit);
        proveedor.setNombre(nombre);
        proveedor.setDireccion(direccion);
        proveedor.setTelefono(telefono);
        proveedor.setCiudad(ciudad);
        proveedorService.save(proveedor);
        return new ModelAndView("redirect:/proveedor/lista");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/borrar/{id}")
    public ModelAndView borrar(@PathVariable("id") int id) {
        if (proveedorService.existsById(id)) {
            proveedorService.delete(id);
            return new ModelAndView("redirect:/proveedor/lista");
        }
        return null;
    }


}
