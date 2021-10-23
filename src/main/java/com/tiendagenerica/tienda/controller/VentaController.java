package com.tiendagenerica.tienda.controller;

import java.util.Optional;
import com.tiendagenerica.tienda.entity.*;
import com.tiendagenerica.tienda.dto.*;
import com.tiendagenerica.tienda.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    VentaService ventaService;

    @Autowired
    ProductoService productoService;

    @Autowired
    DetalleVentaService detalleVentaService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("lista")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/venta/lista");
        List<Venta> ventas = ventaService.lista();
        mv.addObject("ventas", ventas);
        return mv;
    }

    @GetMapping("crear")
    public String registro(Model model, HttpServletRequest request) {
        List<Producto> productos = productoService.list();
        List<Cliente> clientes = clienteService.list();
        model.addAttribute("productos", productos);
        model.addAttribute("clientes", clientes);

        VentaDto carrito = this.obtenerCarrito(request);
        guardarCarrito(carrito, request);
        return "/venta/crear";
    }

    @GetMapping("/detalle/{id}")
    public ModelAndView detalle(@PathVariable("id") int id) {

        Optional<Venta> ventaOpt = ventaService.getById(id);

        if (!ventaOpt.isPresent())
            return new ModelAndView("redirect:/venta/lista");

        VentaDto venta = new VentaDto();
        venta.setCliente(ventaOpt.get().getCliente());

        Collection<Producto> productos = productoService.getProductosVenta(ventaOpt.get().getId());
        for (Producto item : productos) {
            venta.agregarItem(item);
        }

        ModelAndView mv = new ModelAndView("/venta/detalle");
        mv.addObject("venta", venta);
        return mv;
    }

    private VentaDto obtenerCarrito(HttpServletRequest request) {
        VentaDto carrito = (VentaDto) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new VentaDto();
        }
        return carrito;
    }

    private void guardarCarrito(VentaDto carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarrito(new VentaDto(), request);
    }

    @PostMapping(value = "/agregar")
    public String agregarAlCarrito(@RequestParam String prodId, HttpServletRequest request,
            RedirectAttributes redirectAttrs) {

        if (prodId == "") {
            redirectAttrs.addFlashAttribute("mensaje", "El producto no existe").addFlashAttribute("clase", "warning");
            return "redirect:/venta/crear/";
        }

        VentaDto carrito = this.obtenerCarrito(request);
        Optional<Producto> productoBuscadoPorCodigo = productoService.getOne(Integer.parseInt(prodId));

        if (!productoBuscadoPorCodigo.isPresent()) {
            redirectAttrs.addFlashAttribute("mensaje", "El producto con el código " + prodId + " no existe")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/venta/crear/";
        }

        if (productoBuscadoPorCodigo.get().getCantidad() == 0) {
            redirectAttrs.addFlashAttribute("mensaje", "El producto está agotado").addFlashAttribute("clase",
                    "warning");
            return "redirect:/vender/";
        }

        boolean encontrado = false;
        for (ProductoParaVenderDto productoParaVenderActual : carrito.getDetalleVenta()) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.get().getCodigo())) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            productoBuscadoPorCodigo.get().setCantidad(1);
            carrito.agregarItem(productoBuscadoPorCodigo.get());
        }
        this.guardarCarrito(carrito, request);
        return "redirect:/venta/crear/";
    }

    @PostMapping(value = "/seleccioncliente")
    public String seleccionCliente(@RequestParam String clienteId, HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        if (clienteId == "") {
            redirectAttrs.addFlashAttribute("mensaje", "El cliente  no existe").addFlashAttribute("clase", "warning");
            return "redirect:/venta/crear/";
        }
        Optional<Cliente> clienteBuscadoPorCodigo = clienteService.getOne(Integer.parseInt(clienteId));

        if (!clienteBuscadoPorCodigo.isPresent()) {
            redirectAttrs.addFlashAttribute("mensaje", "El cliente con el código " + clienteId + " no existe")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/venta/crear/";
        }
        VentaDto carrito = this.obtenerCarrito(request);
        carrito.setCliente(clienteBuscadoPorCodigo.get());
        this.guardarCarrito(carrito, request);
        return "redirect:/venta/crear/";
    }

    @PostMapping(value = "/quitar/{indice}")
    public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
        VentaDto carrito = this.obtenerCarrito(request);
        carrito.removerItem(indice);
        this.guardarCarrito(carrito, request);
        return "redirect:/venta/crear/";
    }

    @GetMapping(value = "/limpiar")
    public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        this.limpiarCarrito(request);
        redirectAttrs.addFlashAttribute("mensaje", "Venta cancelada").addFlashAttribute("clase", "info");
        return "redirect:/venta/crear/";
    }

    @PostMapping(value = "/terminar")
    public String terminarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        VentaDto carrito = this.obtenerCarrito(request);
        // Si no hay carrito o está vacío, regresamos inmediatamente
        if (carrito.estaVacio()) {
            redirectAttrs.addFlashAttribute("mensaje", "Debe Agregar almenos un producto para continuar")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/venta/crear/";
        }
        if (!carrito.tieneCliente()) {
            redirectAttrs.addFlashAttribute("mensaje", "Debe seleccionar un cliente para continuar")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/venta/crear/";
        }

        Optional<Usuario> usuario = usuarioService.getById(1);
        Optional<Cliente> cliente = clienteService.getOne(carrito.getCliente().getId());
        Venta n = new Venta(usuario.get(), cliente.get(), carrito.getTotal(), carrito.getSubTotal(), carrito.getIva(),
                carrito.getTotalIva(), carrito.getFechaFactura());
        Venta nuevaVenta = ventaService.save(n);

        // Recorrer el carrito
        for (ProductoParaVenderDto productoParaVender : carrito.getDetalleVenta()) {
            // Obtener el producto fresco desde la base de datos
            Producto p = productoService.getOne(productoParaVender.getId()).orElse(null);
            if (p == null)
                continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
            // Le restamos existencia
            p.setCantidad(p.getCantidad() - productoParaVender.getCantidadVenta());
            // Lo guardamos con la existencia ya restada
            productoService.save(p);
            // Creamos un nuevo detalleventa que será el que se guarda junto con la venta
            DetalleVenta detalleVentaItem = new DetalleVenta(0, nuevaVenta, p, 0.19f,
                    productoParaVender.getCantidadVenta(), productoParaVender.getPrecioVenta());
            // Y lo guardamos
            detalleVentaService.save(detalleVentaItem);
        }

        // Al final limpiamos el carrito
        this.limpiarCarrito(request);
        // e indicamos una venta exitosa
        redirectAttrs.addFlashAttribute("mensaje", "Venta realizada correctamente").addFlashAttribute("clase",
                "success");
        return "redirect:/venta/crear/";
    }
}
