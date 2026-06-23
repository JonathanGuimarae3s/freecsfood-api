package br.com.jpslg.freecsfood.domain.repository;

import br.com.jpslg.freecsfood.domain.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagamentoRepositorio extends JpaRepository<FormaPagamento, Long> {
}
