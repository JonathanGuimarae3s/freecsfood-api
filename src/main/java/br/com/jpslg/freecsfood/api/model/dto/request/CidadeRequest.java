package br.com.jpslg.freecsfood.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CidadeRequest(@NotBlank String nome, @NotNull Long estadoId) {
}
