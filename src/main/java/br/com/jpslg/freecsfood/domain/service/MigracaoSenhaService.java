package br.com.jpslg.freecsfood.domain.service;

import br.com.jpslg.freecsfood.domain.repository.UsuarioRepositorio;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Order(2)
public class MigracaoSenhaService implements ApplicationRunner {
    private final UsuarioRepositorio usuarios;
    private final PasswordEncoder encoder;

    public MigracaoSenhaService(UsuarioRepositorio usuarios, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        usuarios.findComSenhaSemPrefixo("$2").stream()
                .forEach(usuario -> usuario.setSenha(encoder.encode(usuario.getSenha())));
    }
}
