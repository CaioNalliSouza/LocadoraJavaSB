package br.com.unisales.locadora.service;

import br.com.unisales.locadora.model.Jogo;
import br.com.unisales.locadora.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JogoService {

    @Autowired
    private JogoRepository repository;

    public List<Jogo> listar() {
        return repository.findAll();
    }

    public Jogo criar(Jogo jogo) {
        return repository.save(jogo);
    }

    public Jogo atualizar(Long id, Jogo jogoDados) {
        Jogo jogo = repository.findById(id).orElse(null);
        if (jogo != null) {
            jogo.setTitulo(jogoDados.getTitulo());
            jogo.setGenero(jogoDados.getGenero());
            jogo.setPrecoDiaria(jogoDados.getPrecoDiaria());
            return repository.save(jogo);
        }
        return null;
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}