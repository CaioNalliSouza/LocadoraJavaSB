package br.com.unisales.locadora;

import br.com.unisales.locadora.controller.ClienteController;
import br.com.unisales.locadora.controller.UsuarioController;
import br.com.unisales.locadora.model.Usuario;
import br.com.unisales.locadora.repository.UsuarioRepository;
import br.com.unisales.locadora.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SegurancaTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteController clienteController;

    @Autowired
    private UsuarioController usuarioController;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // -----------------------------------------------------------------------
    // TESTES: SQL Injection
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("SQL Injection: payload classico nao deve autenticar")
    void sqlInjection_payloadClassico_naoAutentica() {
        Usuario u = new Usuario();
        u.setUsername("admin");
        u.setPassword("senha123");
        u.setRole("ADMIN");
        usuarioService.cadastrar(u);

        boolean resultado = usuarioService.autenticar("' OR '1'='1", "qualquer");
        assertFalse(resultado, "Payload de SQL Injection nao deve autenticar");
    }

    @Test
    @DisplayName("SQL Injection: usuario inexistente nao deve autenticar")
    void sqlInjection_usuarioInexistente_naoAutentica() {
        boolean resultado = usuarioService.autenticar("naoexiste", "senha");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("SQL Injection: credenciais validas devem autenticar normalmente")
    void sqlInjection_credenciaisValidas_autentica() {
        Usuario u = new Usuario();
        u.setUsername("caio");
        u.setPassword("minhasenha");
        u.setRole("USER");
        usuarioService.cadastrar(u);

        boolean resultado = usuarioService.autenticar("caio", "minhasenha");
        assertTrue(resultado, "Credenciais validas devem autenticar");
    }

    // -----------------------------------------------------------------------
    // TESTES: BCrypt
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("BCrypt: senha salva no banco nao deve ser texto puro")
    void bcrypt_senhaNaoBancaNaoETextoPlano() {
        Usuario u = new Usuario();
        u.setUsername("teste_hash");
        u.setPassword("senha_secreta");
        u.setRole("USER");
        Usuario salvo = usuarioService.cadastrar(u);

        assertNotEquals("senha_secreta", salvo.getPassword(),
                "A senha armazenada nao deve ser texto puro");
    }

    @Test
    @DisplayName("BCrypt: hash armazenado deve ser verificavel via BCrypt.matches()")
    void bcrypt_hashVerificavel() {
        Usuario u = new Usuario();
        u.setUsername("teste_bcrypt");
        u.setPassword("abc123");
        u.setRole("USER");
        Usuario salvo = usuarioService.cadastrar(u);

        assertTrue(encoder.matches("abc123", salvo.getPassword()),
                "BCrypt.matches() deve validar a senha original contra o hash");
    }

    @Test
    @DisplayName("BCrypt: senhas diferentes geram hashes diferentes")
    void bcrypt_senhasDiferentesGeramHashesDiferentes() {
        String hash1 = encoder.encode("senha1");
        String hash2 = encoder.encode("senha2");
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("BCrypt: mesma senha gera hashes diferentes por salt aleatorio")
    void bcrypt_mesmaSenhaGeraHashesDiferentes() {
        String hash1 = encoder.encode("minhaSenha");
        String hash2 = encoder.encode("minhaSenha");
        assertNotEquals(hash1, hash2, "BCrypt deve gerar salt diferente a cada encode");
    }

    // -----------------------------------------------------------------------
    // TESTES: XSS
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("XSS: tag script deve ser escapada na resposta")
    void xss_tagScriptEscapada() {
        String resposta = clienteController.boasVindas("<script>alert('xss')</script>");
        assertFalse(resposta.contains("<script>"),
                "Tag script nao deve aparecer sem escape na resposta");
        assertTrue(resposta.contains("&lt;script&gt;"),
                "Tag deve ser substituida por entidades HTML");
    }

    @Test
    @DisplayName("XSS: nome normal nao e alterado")
    void xss_nomeNormalNaoAlterado() {
        String resposta = clienteController.boasVindas("Caio");
        assertTrue(resposta.contains("Caio"),
                "Nome sem caracteres especiais deve aparecer normalmente");
    }

    @Test
    @DisplayName("XSS: aspas e apostrofos sao escapados")
    void xss_aspasEscapadas() {
        String resposta = clienteController.boasVindas("\" onmouseover=\"alert(1)");
        assertFalse(resposta.contains("\""),
                "Aspas duplas devem ser escapadas para &quot;");
    }

    @Test
    @DisplayName("XSS: entrada nula retorna string vazia no nome")
    void xss_entradaNulaRetornaVazio() {
        String resposta = clienteController.boasVindas(null);
        assertNotNull(resposta);
        assertTrue(resposta.contains("Bem-vindo,"));
    }

    // -----------------------------------------------------------------------
    // TESTES: UsuarioController e UsuarioService (cobertura adicional)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("UsuarioController: cadastro retorna usuario salvo")
    void controller_cadastrarUsuario_retornaOk() {
        Usuario u = new Usuario();
        u.setUsername("novo_user");
        u.setPassword("pass123");
        u.setRole("USER");

        ResponseEntity<Usuario> resp = usuarioController.cadastrar(u);
        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals("novo_user", resp.getBody().getUsername());
    }

    @Test
    @DisplayName("UsuarioController: login com credenciais validas retorna 200")
    void controller_login_credenciaisValidas_retorna200() {
        Usuario u = new Usuario();
        u.setUsername("login_user");
        u.setPassword("senha456");
        u.setRole("USER");
        usuarioService.cadastrar(u);

        Usuario loginDados = new Usuario();
        loginDados.setUsername("login_user");
        loginDados.setPassword("senha456");

        ResponseEntity<String> resp = usuarioController.login(loginDados);
        assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    @DisplayName("UsuarioController: login com credenciais invalidas retorna 401")
    void controller_login_credenciaisInvalidas_retorna401() {
        Usuario loginDados = new Usuario();
        loginDados.setUsername("ninguem");
        loginDados.setPassword("errada");

        ResponseEntity<String> resp = usuarioController.login(loginDados);
        assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    @DisplayName("UsuarioController: buscar por ID inexistente retorna 404")
    void controller_buscarPorId_inexistente_retorna404() {
        ResponseEntity<Usuario> resp = usuarioController.buscarPorId(9999L);
        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    @DisplayName("UsuarioController: listar todos retorna lista")
    void controller_listarTodos_retornaLista() {
        Usuario u = new Usuario();
        u.setUsername("list_user");
        u.setPassword("pass");
        u.setRole("USER");
        usuarioService.cadastrar(u);

        List<Usuario> lista = usuarioController.listarTodos();
        assertFalse(lista.isEmpty());
    }

    @Test
    @DisplayName("UsuarioService: alterar usuario atualiza senha com hash")
    void service_alterar_atualizaSenhaComHash() {
        Usuario u = new Usuario();
        u.setUsername("alter_user");
        u.setPassword("senha_original");
        u.setRole("USER");
        Usuario salvo = usuarioService.cadastrar(u);

        Usuario dadosNovos = new Usuario();
        dadosNovos.setUsername("alter_user");
        dadosNovos.setPassword("nova_senha");
        dadosNovos.setRole("USER");

        Usuario atualizado = usuarioService.alterar(salvo.getId(), dadosNovos);
        assertNotNull(atualizado);
        assertNotEquals("nova_senha", atualizado.getPassword());
        assertTrue(encoder.matches("nova_senha", atualizado.getPassword()));
    }
}