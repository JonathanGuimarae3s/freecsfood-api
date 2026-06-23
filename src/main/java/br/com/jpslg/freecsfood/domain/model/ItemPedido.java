package br.com.jpslg.freecsfood.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ItemPedido extends EntidadeBase {

    @NotNull @Positive
    private Integer quantidade;

    @NotNull @PositiveOrZero
    @Column(name = "preco_unitario", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoUnitario;

    @NotNull @PositiveOrZero
    @Column(name = "preco_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoTotal = BigDecimal.ZERO;

    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Pedido pedido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Produto produto;

    public void recalcularTotal() {
        precoTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
