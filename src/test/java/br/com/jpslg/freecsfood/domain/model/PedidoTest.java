package br.com.jpslg.freecsfood.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoTest {
    @Test
    void deveRecalcularTotaisAPartirDosItens() {
        Pedido pedido = new Pedido();
        pedido.setTaxaFrete(new BigDecimal("8.50"));

        ItemPedido primeiro = item(2, "12.00");
        ItemPedido segundo = item(1, "5.50");
        pedido.adicionarItem(primeiro);
        pedido.adicionarItem(segundo);

        pedido.recalcularTotais();

        assertThat(pedido.getSubtotal()).isEqualByComparingTo("29.50");
        assertThat(pedido.getValorTotal()).isEqualByComparingTo("38.00");
        assertThat(primeiro.getPedido()).isSameAs(pedido);
    }

    private ItemPedido item(int quantidade, String preco) {
        ItemPedido item = new ItemPedido();
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(new BigDecimal(preco));
        return item;
    }
}
