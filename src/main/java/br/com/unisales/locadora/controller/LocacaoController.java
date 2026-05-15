package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Locacao;
import br.com.unisales.locadora.service.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

    @Autowired
    private LocacaoService service;

    @PostMapping
    public Locacao criar(@RequestBody Locacao locacao) {
        return service.criar(locacao);
    }

    @GetMapping
    public List<Locacao> listar() {
        return service.listar();
    }

    @PutMapping("/{id}/devolucao")
    public Locacao devolver(@PathVariable Long id) {
        return service.devolver(id);
    }
}