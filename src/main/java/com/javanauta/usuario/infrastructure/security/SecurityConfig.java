package com.javanauta.usuario.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. DESABILITAR O CSRF: Resolve o 403 em requisições POST/PUT/DELETE no Postman.
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        // 2. Rotas Públicas: Permite acesso sem autenticação.
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()      // POST para Registro
                        .requestMatchers("/usuario/login").permitAll()                  // POST para Login

                        // Esta rota "/auth" é um placeholder e pode ser removida se não for usada.
                        .requestMatchers(HttpMethod.GET, "/auth").permitAll()

                        // 3. Rotas Protegidas: Todas as outras requerem autenticação (JWT).
                        .anyRequest().authenticated()
                )

                // 4. Configuração de Sessão como STATELESS (necessário para JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Adicionar o filtro JWT antes do filtro padrão de autenticação
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // O PasswordEncoder foi movido para EncoderConfig.java, o que está correto.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        // O Spring encontra o UserDetailsService (UsuarioService) e
        // o PasswordEncoder automaticamente.
        return authenticationConfiguration.getAuthenticationManager();
    }
}