package com.tiendagenerica.tienda.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.tiendagenerica.tienda.dto.ProductoDto;
import com.tiendagenerica.tienda.entity.Producto;
import com.tiendagenerica.tienda.service.ProductoService;
import com.tiendagenerica.utilidades.ValidadorUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("lista")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/producto/lista");
        List<Producto> productos = productoService.list();
        mv.addObject("productos", productos);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("nuevo")
    public String nuevo() {
        return "producto/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("cargarlistaproductos")
    public String cargarListaProductos() {
        return "producto/cargarlistaproductos";
    }

    @PostMapping("/carga-archivo-csv")
    public ModelAndView cargarAarchivoCsv(@RequestParam("archivo") MultipartFile archivo) {
        ModelAndView mv = new ModelAndView();
        // validando el archivo
        if (archivo.isEmpty()) {
            mv.setViewName("producto/cargarlistaproductos");
            mv.addObject("error", "Por favor seleccione un archivo a cargar.");
            return mv;
        } else {

            // convertir el archivo para crear una lista de
            try (Reader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {

                // create csv bean reader
                CsvToBean<ProductoDto> csvToBean = new CsvToBeanBuilder(reader).withType(ProductoDto.class)
                        .withSkipLines(1).withSeparator(';').withIgnoreLeadingWhiteSpace(true).build();

                // convert `CsvToBean` object to list of ProductoDto
                List<ProductoDto> productosDtos = csvToBean.parse();

                // guardar usuarios en base de datos

                for (ProductoDto itemProducto : productosDtos) {
                    Producto productoEntidad = new Producto(0, itemProducto.getNombre(), itemProducto.getCodigo(),
                            itemProducto.getNitProveedor(), ValidadorUtil.priceConvert(itemProducto.getPrecioCompra()),
                            ValidadorUtil.priceConvert(itemProducto.getPrecioVenta()),
                            ValidadorUtil.priceConvert(itemProducto.getIvaCompra()),
                            Integer.parseInt(itemProducto.getCantidad()));
                    productoService.save(productoEntidad);
                }

                mv.setViewName("redirect:/producto/lista");
                return mv;

            } catch (Exception ex) {
                mv.setViewName("producto/cargarlistaproductos");
                mv.addObject("error", "Ocurrio un error procesando el archivo csv");
                return mv;

            }
        }

    }

    // Todo: aumentar los campos en la vista y enviar un solo objeto para guardar
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public ModelAndView crear(@RequestParam String nombre, @RequestParam float precioCompra) {
        ModelAndView mv = new ModelAndView();
        if (StringUtils.isBlank(nombre)) {
            mv.setViewName("producto/nuevo");
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
        if (precioCompra < 1) {
            mv.setViewName("producto/nuevo");
            mv.addObject("error", "el precio debe ser mayor que cero");
            return mv;
        }
        if (productoService.existsByNombre(nombre)) {
            mv.setViewName("producto/nuevo");
            mv.addObject("error", "ese nombre ya existe");
            return mv;
        }
        Producto producto = new Producto(0, nombre, "", "", precioCompra, 0, 19, 0);
        productoService.save(producto);
        mv.setViewName("redirect:/producto/lista");
        return mv;
    }

    @GetMapping("/detalle/{id}")
    public ModelAndView detalle(@PathVariable("id") int id) {
        if (!productoService.existsById(id))
            return new ModelAndView("redirect:/producto/lista");
        Producto producto = productoService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/producto/detalle");
        mv.addObject("producto", producto);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") int id) {
        if (!productoService.existsById(id))
            return new ModelAndView("redirect:/producto/lista");
        Producto producto = productoService.getOne(id).get();
        ModelAndView mv = new ModelAndView("/producto/editar");
        mv.addObject("producto", producto);
        return mv;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/actualizar")
    public ModelAndView actualizar(@RequestParam int id, @RequestParam String nombre,
            @RequestParam float precioCompra) {
        if (!productoService.existsById(id))
            return new ModelAndView("redirect:/producto/lista");
        ModelAndView mv = new ModelAndView();
        Producto producto = productoService.getOne(id).get();
        if (StringUtils.isBlank(nombre)) {
            mv.setViewName("producto/editar");
            mv.addObject("producto", producto);
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
        if (precioCompra < 1) {
            mv.setViewName("producto/editar");
            mv.addObject("error", "el precio debe ser mayor que cero");
            mv.addObject("producto", producto);
            return mv;
        }
        if (productoService.existsByNombre(nombre) && productoService.getByNombre(nombre).get().getId() != id) {
            mv.setViewName("producto/editar");
            mv.addObject("error", "ese nombre ya existe");
            mv.addObject("producto", producto);
            return mv;
        }
        // todo: colocar todos
        producto.setNombre(nombre);
        producto.setPrecioCompra(precioCompra);
        productoService.save(producto);
        return new ModelAndView("redirect:/producto/lista");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/borrar/{id}")
    public ModelAndView borrar(@PathVariable("id") int id) {
        if (productoService.existsById(id)) {
            productoService.delete(id);
            return new ModelAndView("redirect:/producto/lista");
        }
        return null;
    }

}
