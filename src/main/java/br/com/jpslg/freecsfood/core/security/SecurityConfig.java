package br.com.jpslg.freecsfood.core.security;

import br.com.jpslg.freecsfood.domain.model.Usuario;
import br.com.jpslg.freecsfood.domain.repository.UsuarioRepositorio;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/restaurantes/**").hasAuthority("CONSULTAR_RESTAURANTES")
                        .requestMatchers(HttpMethod.POST, "/restaurantes/**").hasAuthority("EDITAR_RESTAURANTES")
                        .requestMatchers(HttpMethod.PUT, "/restaurantes/**").hasAuthority("EDITAR_RESTAURANTES")
                        .requestMatchers(HttpMethod.DELETE, "/restaurantes/**").hasAuthority("EDITAR_RESTAURANTES")
                        .requestMatchers(HttpMethod.POST, "/cidades/**", "/estados/**").hasAuthority("EDITAR_CIDADES")
                        .requestMatchers(HttpMethod.PUT, "/cidades/**", "/estados/**").hasAuthority("EDITAR_CIDADES")
                        .requestMatchers(HttpMethod.DELETE, "/cidades/**", "/estados/**").hasAuthority("EDITAR_CIDADES")
                        .requestMatchers(HttpMethod.POST, "/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
                        .requestMatchers(HttpMethod.PUT, "/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
                        .requestMatchers(HttpMethod.DELETE, "/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) ->
                                writeProblem(response, objectMapper, 401, "Autenticação necessária"))
                        .accessDeniedHandler((request, response, exception) ->
                                writeProblem(response, objectMapper, 403, "Acesso negado")));
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(UsuarioRepositorio repositorio) {
        return email -> {
            Usuario usuario = repositorio.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(email));
            List<SimpleGrantedAuthority> authorities = usuario.getGrupos().stream()
                    .flatMap(grupo -> grupo.getPermissoes().stream())
                    .map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
                    .distinct()
                    .toList();
            return User.withUsername(usuario.getEmail()).password(usuario.getSenha()).authorities(authorities).build();
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins}") String allowedOrigins) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.stream(allowedOrigins.split(",")).map(String::trim).toList());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private static void writeProblem(HttpServletResponse response, ObjectMapper mapper, int status, String detail)
            throws java.io.IOException {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(org.springframework.http.HttpStatus.valueOf(status), detail);
        problem.setTitle(detail);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), problem);
    }
}
