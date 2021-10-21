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
        // model.addAttribute("venta", new Venta());
        // model.addAttribute("detalleVenta", new DetalleVenta());
        model.addAttribute("productos", productos);
        model.addAttribute("producto", new Producto());
        float total = 0;
        ArrayList<ProductoParaVenderDto> carrito = this.obtenerCarrito(request);
        for (ProductoParaVenderDto p : carrito)
            total += p.getTotal();
        model.addAttribute("total", total);
        return "/venta/crear";
    }

    private ArrayList<ProductoParaVenderDto> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoParaVenderDto> carrito = (ArrayList<ProductoParaVenderDto>) request.getSession()
                .getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    private void guardarCarrito(ArrayList<ProductoParaVenderDto> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarrito(new ArrayList<>(), request);
    }

    @PostMapping(value = "/agregar")
    public String agregarAlCarrito(@RequestParam String prodId, HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        System.out.println(prodId);
        ArrayList<ProductoParaVenderDto> carrito = this.obtenerCarrito(request);
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
        for (ProductoParaVenderDto productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.get().getCodigo())) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            carrito.add(new ProductoParaVenderDto(productoBuscadoPorCodigo.get().getId(),
                    productoBuscadoPorCodigo.get().getNombre(), productoBuscadoPorCodigo.get().getCodigo(),
                    productoBuscadoPorCodigo.get().getNitProveedor(), productoBuscadoPorCodigo.get().getPrecioCompra(),
                    productoBuscadoPorCodigo.get().getPrecioVenta(), productoBuscadoPorCodigo.get().getIvaCompra(), 1));
        }
        this.guardarCarrito(carrito, request);
        return "redirect:/venta/crear/";
    }

    @PostMapping(value = "/quitar/{indice}")
    public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
        ArrayList<ProductoParaVenderDto> carrito = this.obtenerCarrito(request);
        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            carrito.remove(indice);
            this.guardarCarrito(carrito, request);
        }
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
        ArrayList<ProductoParaVenderDto> carrito = this.obtenerCarrito(request);
        // Si no hay carrito o está vacío, regresamos inmediatamente
        if (carrito == null || carrito.size() <= 0) {
            return "redirect:/venta/crear/";
        }
        float total = 0f;
        Date myDate = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(myDate);
        for (ProductoParaVenderDto p : carrito)
            total += p.getTotal();
        Optional<Cliente> cliente = clienteService.getOne(1);
        Optional<Usuario> usuario = usuarioService.getById(1);
        Venta v = ventaService.save(new Venta(0, usuario.get(), cliente.get(), total, fecha));
        // Recorrer el carrito
        for (ProductoParaVenderDto productoParaVender : carrito) {
            // Obtener el producto fresco desde la base de datos
            Producto p = productoService.getOne(productoParaVender.getId()).orElse(null);
            if (p == null)
                continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
            // Le restamos existencia
            p.setCantidad(p.getCantidad() - productoParaVender.getCantidadVenta());
            // Lo guardamos con la existencia ya restada
            productoService.save(p);
            // Creamos un nuevo detalleventa que será el que se guarda junto con la venta
            DetalleVenta detalleVentaItem = new DetalleVenta(0, v, p, 0.19f, productoParaVender.getCantidadVenta(),
                    productoParaVender.getPrecioVenta());
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
