package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Jogo;
import br.com.unisales.locadora.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    @Autowired
    private JogoRepository repo;

    @GetMapping
    public List <Jogo> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Jogo criar(@RequestBody Jogo jogo) {
        return repo.save(jogo);
    }

    @PutMapping("/{id}")
    public Jogo atualizar(@PathVariable Long id, @RequestBody Jogo jogoDados) {
        Jogo jogoBusca = repo.findById(id).orElse(null);
        if(jogoBusca != null) {
            jogoBusca.setTitulo(jogoDados.getTitulo());
            jogoBusca.setGenero(jogoDados.getGenero());
            jogoBusca.setPrecoDiaria(jogoDados.getPrecoDiaria());
            return repo.save(jogoBusca);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}