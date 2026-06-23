package br.com.jpslg.freecsfood.api.assembler;

import br.com.jpslg.freecsfood.api.model.dto.*;
import br.com.jpslg.freecsfood.domain.model.*;

public final class ApiMapper {
    private ApiMapper() {
    }

    public static CozinhaResponse toResponse(Cozinha cozinha) {
        return new CozinhaResponse(cozinha.getId(), cozinha.getNome());
    }

    public static EstadoResponse toResponse(Estado estado) {
        return new EstadoResponse(estado.getId(), estado.getNome());
    }

    public static CidadeResponse toResponse(Cidade cidade) {
        return new CidadeResponse(cidade.getId(), cidade.getNome(), toResponse(cidade.getEstado()));
    }

    public static RestauranteResponse toResponse(Restaurante restaurante) {
        return new RestauranteResponse(restaurante.getId(), restaurante.getNome(), restaurante.getTaxaFrete(),
                toResponse(restaurante.getCozinha()));
    }

    public static FormaPagamentoResponse toResponse(FormaPagamento formaPagamento) {
        return new FormaPagamentoResponse(formaPagamento.getId(), formaPagamento.getDescricao());
    }
}
