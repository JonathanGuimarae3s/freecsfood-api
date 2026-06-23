package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.Cidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepositorio extends JpaRepository<Cidade, Long> {
    @Override
    @EntityGraph(attributePaths = "estado")
    Page<Cidade> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "estado")
    java.util.Optional<Cidade> findById(Long id);
}
