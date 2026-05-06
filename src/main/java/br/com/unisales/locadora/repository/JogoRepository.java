package br.com.unisales.locadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.unisales.locadora.model.Jogo;
import org.springframework.stereotype.Repository;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
     
}