package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @EntityGraph(attributePaths = {"grupos", "grupos.permissoes"})
    Optional<Usuario> findByEmailIgnoreCase(String email);

    @Query("select usuario from Usuario usuario where usuario.senha not like concat(:prefixo, '%')")
    List<Usuario> findComSenhaSemPrefixo(@Param("prefixo") String prefixo);
}
