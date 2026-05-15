package br.com.unisales.locadora.service;

import br.com.unisales.locadora.model.Locacao;
import br.com.unisales.locadora.repository.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository repository;

    public Locacao criar(Locacao locacao) {
        locacao.setDataLocacao(LocalDate.now());
        locacao.setStatus("ATIVO");
        return repository.save(locacao);
    }

    public List<Locacao> listar() {
        return repository.findAll();
    }

    public Locacao devolver(Long id) {
        Locacao locacao = repository.findById(id).orElse(null);
        if (locacao != null) {
            locacao.setDataDevolucao(LocalDate.now());
            locacao.setStatus("DEVOLVIDO");
            return repository.save(locacao);
        }
        return null;
    }
}