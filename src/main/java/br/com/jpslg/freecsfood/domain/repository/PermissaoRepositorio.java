package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissaoRepositorio extends JpaRepository<Permissao, Long> {
    Optional<Permissao> findByNome(String nome);
}
