package br.com.unisales.locadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.unisales.locadora.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Gera automaticamente um Prepared Statement parametrizado — sem risco de SQL Injection
    Optional<Usuario> findByUsername(String username);
}
