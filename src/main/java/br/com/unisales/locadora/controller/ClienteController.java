package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Cliente;
import br.com.unisales.locadora.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @PostMapping
    public Cliente cadastrar(@RequestBody Cliente cliente) {
        return service.cadastrar(cliente);
    }

    @GetMapping(value = "/boasvindas", produces = MediaType.TEXT_HTML_VALUE)
    public String boasVindas(@RequestParam String nome) {
        return "<html><body><h1>Bem-vindo, " + nome + "!</h1></body></html>";
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id) {
        service.deletar(id);
        return "Cliente com ID " + id + " deletado com sucesso! (Mesmo sem checar se você é ADMIN)";
    }
}