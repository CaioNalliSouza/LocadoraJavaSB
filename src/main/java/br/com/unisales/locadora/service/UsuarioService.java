package br.com.unisales.locadora.service;

import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    /**
     * CORREÇÃO — Armazenamento de senha em texto puro (OWASP A02:2021):
     * A versão vulnerável salvava a senha diretamente no banco sem qualquer hash.
     * A versão segura aplica BCrypt (fator de custo 12) antes de persistir,
     * tornando inviável a recuperação da senha mesmo em caso de vazamento do banco.
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Usuario cadastrar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    /**
     * CORREÇÃO — SQL Injection (OWASP A03:2021):
     * Autenticação feita via JPA (findByUsername gera Prepared Statement parametrizado)
     * + BCrypt.matches() para comparar o hash armazenado com a senha fornecida.
     * Não há concatenação de strings em SQL em nenhum ponto deste fluxo.
     */
    public boolean autenticar(String username, String senhaFornecida) {
        return repository.findByUsername(username)
                .map(u -> passwordEncoder.matches(senhaFornecida, u.getPassword()))
                .orElse(false);
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
            // Hash aplicado também na alteração de senha
            user.setPassword(passwordEncoder.encode(dadosNovos.getPassword()));
            user.setRole(dadosNovos.getRole());
            return repository.save(user);
        }
        return null;
    }
}
