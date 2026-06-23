package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoRepositorio extends JpaRepository<Grupo, Long> {
    Optional<Grupo> findByNome(String nome);
}
