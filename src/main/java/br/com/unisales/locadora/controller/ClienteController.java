package br.com.unisales.locadora.controller;

import br.com.unisales.locadora.model.Cliente;
import br.com.unisales.locadora.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(service.cadastrar(cliente));
    }

    /**
     * CORREÇÃO — XSS Refletido (OWASP A03:2021):
     * A versão vulnerável concatenava o parâmetro "nome" diretamente no HTML de resposta,
     * permitindo injeção de scripts: ?nome=<script>alert('XSS')</script>
     * A versão segura escapa os caracteres especiais HTML antes de inserir o valor,
     * tornando qualquer tag injetada inofensiva no navegador.
     */
    @GetMapping(value = "/boasvindas", produces = MediaType.TEXT_HTML_VALUE)
    public String boasVindas(@RequestParam String nome) {
        String nomeSeguro = escapeHtml(nome);
        return "<html><body><h1>Bem-vindo, " + nomeSeguro + "!</h1></body></html>";
    }

    /**
     * Escapa caracteres especiais HTML para prevenir XSS.
     * Substitui os cinco caracteres críticos: & < > " '
     */
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok("Cliente removido com sucesso.");
    }
}
