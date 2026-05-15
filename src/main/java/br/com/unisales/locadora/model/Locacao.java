package br.com.unisales.locadora.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Locacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private Long jogoId;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;
    private String status;
}