package br.com.unisales.locadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.unisales.locadora.model.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}