package br.com.unisales.locadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.unisales.locadora.model.Cliente;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}