package br.com.jpslg.freecsfood.api.model.dto;

import java.math.BigDecimal;

public record RestauranteResponse(Long id, String nome, BigDecimal taxaFrete, CozinhaResponse cozinha) {
}
