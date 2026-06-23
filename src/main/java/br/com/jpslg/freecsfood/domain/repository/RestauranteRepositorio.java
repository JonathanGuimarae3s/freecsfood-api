package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepositorio extends JpaRepository<Restaurante, Long> {
    @Override
    @EntityGraph(attributePaths = "cozinha")
    Page<Restaurante> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "cozinha")
    java.util.Optional<Restaurante> findById(Long id);
}
