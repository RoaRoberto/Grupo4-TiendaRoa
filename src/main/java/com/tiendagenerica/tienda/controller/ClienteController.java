package com.tiendagenerica.tienda.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.tiendagenerica.tienda.entity.Cliente;
import com.tiendagenerica.tienda.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("lista")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/cliente/lista");
        List<Cliente> cliente = clienteService.list();
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("nuevo")
    public String nuevo() {
        return "cliente/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public ModelAndView crear(@RequestParam int cedula, @RequestParam String nombre, @RequestParam String direccion,
            @RequestParam int telefono, @RequestParam String email) {
        ModelAndView mv = new ModelAndView();
        if (StringUtils.isBlank(nombre)) {
            mv.setViewName("cliente/nuevo");
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }

        if (clienteService.existsById(cedula)) {
            mv.setViewName("cliente/nuevo");
            mv.addObject("error", "CEDULA ya Registrada en el Sistema");
            return mv;
        }
        Cliente cliente = new Cliente(cedula, nombre, direccion, telefono, email);
        clienteService.save(cliente);
        mv.setViewName("redirect:/cliente/lista");
        return mv;
    }

    @GetMapping("/detalle/{id}")
    public ModelAndView detalle(@PathVariable("id") int id) {
        if (!clienteService.existsById(id))
            return new ModelAndView("redirect:/cliente/lista");
        Cliente cliente = clienteService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/cliente/detalle");
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") int id) {
        if (!clienteService.existsById(id))
            return new ModelAndView("redirect:/cliente/lista");
        Cliente cliente = clienteService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/cliente/editar");
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/actualizar")
    public ModelAndView actualizar(@RequestParam int id, @RequestParam int cedula, @RequestParam String nombre,
            @RequestParam String direccion, @RequestParam int telefono, @RequestParam String email) {
        if (!clienteService.existsById(id))
            return new ModelAndView("redirect:/cliente/lista");
        ModelAndView mv = new ModelAndView();
        Cliente cliente = clienteService.getOne(id).get();
        if (StringUtils.isBlank(nombre)) {
            mv.setViewName("cliente/editar");
            mv.addObject("cliente", cliente);
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }

        if (clienteService.existsByNombre(nombre) && clienteService.getByNombre(nombre).get().getId() != id) {
            mv.setViewName("cliente/editar");
            mv.addObject("error", "ese nombre ya existe");
            mv.addObject("cliente", cliente);
            return mv;
        }

        cliente.setCedula(cedula);
        cliente.setNombre(nombre);
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        clienteService.save(cliente);
        return new ModelAndView("redirect:/cliente/lista");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/borrar/{id}")
    public ModelAndView borrar(@PathVariable("id") int id) {
        if (clienteService.existsById(id)) {
            clienteService.delete(id);
            return new ModelAndView("redirect:/cliente/lista");
        }
        return null;
    }

}
