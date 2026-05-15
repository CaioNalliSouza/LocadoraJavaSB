package br.com.unisales.locadora.service;

import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario cadastrar(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Usuario alterar(Long id, Usuario dadosNovos) {
        Usuario user = repository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(dadosNovos.getUsername());
            user.setPassword(dadosNovos.getPassword());
            user.setRole(dadosNovos.getRole());
            return repository.save(user);
        }
        return null;
    }
}