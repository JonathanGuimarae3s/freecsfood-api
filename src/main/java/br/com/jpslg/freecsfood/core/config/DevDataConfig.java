package br.com.jpslg.freecsfood.core.config;

import br.com.jpslg.freecsfood.domain.model.Grupo;
import br.com.jpslg.freecsfood.domain.model.Permissao;
import br.com.jpslg.freecsfood.domain.model.Usuario;
import br.com.jpslg.freecsfood.domain.repository.GrupoRepositorio;
import br.com.jpslg.freecsfood.domain.repository.PermissaoRepositorio;
import br.com.jpslg.freecsfood.domain.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashSet;
import java.util.List;

@Configuration
@Profile("dev")
public class DevDataConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DevDataConfig.class);
    private static final String ADMIN_EMAIL = "admin@freecsfood.local";

    @Bean
    @Order(1)
    ApplicationRunner criarAdministradorDeDesenvolvimento(
            UsuarioRepositorio usuarios,
            GrupoRepositorio grupos,
            PermissaoRepositorio permissoes,
            PasswordEncoder encoder,
            @Value("${app.dev.admin-password}") String adminPassword) {
        return args -> {
            if (usuarios.findByEmailIgnoreCase(ADMIN_EMAIL).isPresent()) {
                LOGGER.info("Administrador de desenvolvimento já existe: {}", ADMIN_EMAIL);
                return;
            }
            if (adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException("Defina DEV_ADMIN_PASSWORD para iniciar o perfil dev");
            }

            List<Permissao> todas = List.of(
                    buscarOuCriarPermissao(permissoes, "CONSULTAR_RESTAURANTES", "Permite consultar restaurantes"),
                    buscarOuCriarPermissao(permissoes, "EDITAR_RESTAURANTES", "Permite editar restaurantes"),
                    buscarOuCriarPermissao(permissoes, "EDITAR_CIDADES", "Permite editar cidades e estados"),
                    buscarOuCriarPermissao(permissoes, "EDITAR_COZINHAS", "Permite editar cozinhas"));

            Grupo administradores = grupos.findByNome("Administradores").orElseGet(() -> {
                Grupo novoGrupo = new Grupo();
                novoGrupo.setNome("Administradores");
                return novoGrupo;
            });
            administradores.setPermissoes(new LinkedHashSet<>(todas));
            grupos.save(administradores);

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail(ADMIN_EMAIL);
            admin.setSenha(encoder.encode(adminPassword));
            admin.getGrupos().add(administradores);
            usuarios.save(admin);
            LOGGER.info("Administrador de desenvolvimento criado: {}", ADMIN_EMAIL);
        };
    }

    private Permissao buscarOuCriarPermissao(
            PermissaoRepositorio repositorio, String nome, String descricao) {
        return repositorio.findByNome(nome).orElseGet(() -> {
            Permissao permissao = new Permissao();
            permissao.setNome(nome);
            permissao.setDescricao(descricao);
            return repositorio.save(permissao);
        });
    }
}
