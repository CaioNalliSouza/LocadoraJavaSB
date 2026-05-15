package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Jogo;
import br.com.unisales.locadora.service.JogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    @Autowired
    private JogoService service;

    @GetMapping
    public List<Jogo> listar() {
        return service.listar();
    }

    @PostMapping
    public Jogo criar(@RequestBody Jogo jogo) {
        return service.criar(jogo);
    }

    @PutMapping("/{id}")
    public Jogo atualizar(@PathVariable Long id, @RequestBody Jogo jogoDados) {
        return service.atualizar(id, jogoDados);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}