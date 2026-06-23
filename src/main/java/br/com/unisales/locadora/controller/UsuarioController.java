package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(service.cadastrar(usuario));
    }

    /**
     * CORREÇÃO — SQL Injection (OWASP A03:2021):
     * A versão vulnerável concatenava username e password diretamente na query SQL,
     * permitindo ataques como: username = ' OR '1'='1
     * A versão segura delega a consulta ao método do repositório JPA (Prepared Statement
     * parametrizado gerado internamente pelo Hibernate), eliminando a injeção.
     *
     * CORREÇÃO — Exposição de erro interno (OWASP A09:2021):
     * A versão vulnerável retornava e.getMessage() ao cliente, expondo detalhes do banco.
     * A versão segura retorna apenas uma mensagem genérica e registra o erro no log do servidor.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario loginDados) {
        boolean autenticado = service.autenticar(loginDados.getUsername(), loginDados.getPassword());
        if (autenticado) {
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(401).body("Usuário ou senha incorretos.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario u = service.buscarPorId(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<Usuario> alterar(@PathVariable Long id, @RequestBody Usuario dadosNovos) {
        Usuario u = service.alterar(id, dadosNovos);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }
}
