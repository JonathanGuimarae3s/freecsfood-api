package br.com.jpslg.freecsfood.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CozinhaRequest(@NotBlank String nome) {
}
