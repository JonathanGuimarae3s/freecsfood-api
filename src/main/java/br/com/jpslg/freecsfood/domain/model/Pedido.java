package br.com.jpslg.freecsfood.domain.model;

import br.com.jpslg.freecsfood.domain.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Pedido extends EntidadeBase {

    @NotNull @PositiveOrZero
    @Column(name = "sub_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @NotNull @PositiveOrZero
    @Column(name = "taxa_frete", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxaFrete = BigDecimal.ZERO;

    @NotNull @PositiveOrZero
    @Column(name = "valor_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConfirmacao;
    private LocalDateTime dataCancelamento;
    private LocalDateTime dataEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status = StatusPedido.CRIADO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Restaurante restaurante;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @NotNull @Valid @Embedded
    private Endereco endereco;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private FormaPagamento formaPagamento;

    @Version
    private Long version;

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }

    public void recalcularTotais() {
        itens.forEach(ItemPedido::recalcularTotal);
        subtotal = itens.stream().map(ItemPedido::getPrecoTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        valorTotal = subtotal.add(taxaFrete);
    }
}
