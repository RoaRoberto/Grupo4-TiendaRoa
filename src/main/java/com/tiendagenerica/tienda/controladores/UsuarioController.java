package com.tiendagenerica.tienda.controladores;

import java.util.ArrayList;
import java.util.Optional;

import com.tiendagenerica.tienda.modelos.UsuarioModel;
import com.tiendagenerica.tienda.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({ "/login", "/" })
    public String login(Model model, String error, String logout) {

        return "login";
    }

    @PostMapping("/validar")
    public String validar(@RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password, Model model, String error,
            String logout) {

        Optional<UsuarioModel> usuarioOp = usuarioService.login(username, password);
        if (usuarioOp.isPresent()) {
            model.addAttribute("usuario", usuarioOp.get());
            ArrayList<UsuarioModel> listaUsuarios = usuarioService.obtenerUsuarios();
            model.addAttribute("listaUsuarios", listaUsuarios);

            return "gestionUsuarios";
        }
        return "redirect:/";

    }

    @GetMapping("/salir")
    public String salir(Model model, String error, String logout) {

        return "redirect:/";

    }

    @PostMapping("/mantenimiento")
    public String mantenimiento(UsuarioModel usuarioDto, Model model) {
        if (usuarioDto.getId() == null) {
            usuarioService.InsertarUsuario(usuarioDto);
        } else {
            usuarioService.editarUsuario(usuarioDto);
        }
        ArrayList<UsuarioModel> listaUsuarios = usuarioService.obtenerUsuarios();
        model.addAttribute("listaUsuarios", listaUsuarios);
        return "gestionUsuarios";
    }

    @GetMapping("/mantenimiento/{id}")
    public String mantenimientoCrud(@PathVariable("id") Long id, Model model) {
        if (id != null && id != 0) {
            model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        } else {
            model.addAttribute("usuario", new UsuarioModel());
        }
        return "mantenimiento";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {
        usuarioService.eliminarUsuario(id);
        ArrayList<UsuarioModel> listaUsuarios = usuarioService.obtenerUsuarios();
        model.addAttribute("listaUsuarios", listaUsuarios);
        return "gestionUsuarios";
    }

}
