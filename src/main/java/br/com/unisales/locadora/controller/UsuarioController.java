package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public Usuario cadastrar(@RequestBody Usuario usuario) {
        return repository.save(usuario);
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario loginDados) {
        String sql = "SELECT username FROM usuario WHERE username = '"
        + loginDados.getUsername() + "' AND password = '"
        + loginDados.getPassword() + "'";

        try {
            List<String> usuarios = jdbcTemplate.queryForList(sql, String.class);
            if (!usuarios.isEmpty()) {
                return "Login realizado! Bem-vindo, " + usuarios.get(0); 
            } else {
                return "Usuário ou senha incorretos.";
            }
        } catch (Exception e) {
            return "Erro no banco: " + e.getMessage();
        }
    }
}