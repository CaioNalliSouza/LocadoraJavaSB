package br.com.unisales.locadora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Configura o Spring Security para permitir acesso às rotas da API sem autenticação HTTP Basic,
     * mantendo disponível apenas o BCryptPasswordEncoder para uso no serviço.
     * Em produção, este ponto seria expandido com JWT ou sessão gerenciada.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
