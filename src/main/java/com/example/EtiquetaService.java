package com.example;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class EtiquetaService {
    private final PromocaoStrategy promocao;
    private final NumberFormat currency;

    public EtiquetaService(PromocaoStrategy promocao) {
        // Permite passar uma promoção nula, usando lambda identity como padrão, garantindo comportamento consistente
        this.promocao = promocao == null ? e -> e : promocao;

        // Uso de NumberFormat com Locale pt-BR para formatar valores monetários corretamente, evitando hardcode de formatação
        this.currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }

    public String gerarEtiqueta(Entrega entrega) {
        // Aplicação da promoção antes do cálculo do frete garante que o valor final reflita regras de negócio
        Entrega e = promocao.aplicar(entrega);
        BigDecimal valorFrete = e.getTipoFrete().calcular(e);

        // Uso de String.format com placeholders claros melhora legibilidade e facilita manutenção futura
        return String.format("Destinatário: %s%nEndereço: %s%nPeso: %skg%nTipo: %s%nValor do Frete: %s",
                e.getDestinatario(),
                e.getEndereco(),
                e.getPeso(),
                e.getTipoFrete(),
                currency.format(valorFrete));
    }

    public String gerarResumoPedido(Entrega entrega) {
        Entrega e = promocao.aplicar(entrega);
        BigDecimal valorFrete = e.getTipoFrete().calcular(e);

        //Mantive a apresentação dos dados separado do cálculo propriamente para que existam mais passos no fluxo impedindo entradas inválidas
        return String.format("Pedido para %s com frete %s no valor de %s",
                e.getDestinatario(),
                e.getTipoFrete(),
                currency.format(valorFrete));
    }
}
