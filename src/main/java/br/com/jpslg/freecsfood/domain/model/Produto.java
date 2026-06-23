package br.com.jpslg.freecsfood.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Produto extends EntidadeBase {

    @NotBlank @Column(nullable = false)
    private String nome;
    private String descricao;

    @NotNull @PositiveOrZero
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Restaurante restaurante;

    @Column(nullable = false)
    private boolean ativo;

    @Version
    private Long version;
}
