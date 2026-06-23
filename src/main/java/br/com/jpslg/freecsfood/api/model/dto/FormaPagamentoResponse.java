package br.com.jpslg.freecsfood.api.model.dto;

import br.com.jpslg.freecsfood.domain.model.FormaPagamento;

/**
 * DTO for {@link FormaPagamento}
 */
public record FormaPagamentoResponse(Long id, String descricao)  {
}