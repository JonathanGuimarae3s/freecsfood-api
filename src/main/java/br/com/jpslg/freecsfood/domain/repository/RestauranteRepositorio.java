package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.FormaPagamento;
import br.com.jpslg.freecsfood.domain.model.Restaurante;
import org.hibernate.annotations.processing.SQL;
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

    @SQL("SELECT forma_pagamento FROM forma_pagamento WHERE forma_pagamento.restaurante_id = :idRestaurante")
    Page<FormaPagamento> listarFormaPagamentoPorRestaurante(Pageable pageable, Long idRestaurante);
}
