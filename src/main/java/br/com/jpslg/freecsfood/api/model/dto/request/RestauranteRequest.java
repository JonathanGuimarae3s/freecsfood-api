package br.com.jpslg.freecsfood.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RestauranteRequest(
        @NotBlank String nome,
        @NotNull @PositiveOrZero BigDecimal taxaFrete,
        @NotNull Long cozinhaId) {
}
