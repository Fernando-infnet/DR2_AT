package com.example;

import java.text.NumberFormat;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EntregaTest {

    private PromocaoStrategy promocao;
    private EtiquetaService etiquetaService;

    @BeforeEach
    void setup() {
        promocao = new PromocaoPeso(10.0, 1.0);
        etiquetaService = new EtiquetaService(promocao);
    }

    @Test
    void testFreteExpresso() {
        Entrega e = new Entrega("Rua A", 12.5, "Ana", TipoFrete.EXP);
        BigDecimal frete = e.getTipoFrete().calcular(e);
        // Peso 12.5 * 1.5 + 10 = 28.75
        assertEquals(new BigDecimal("28.75"), frete.setScale(2));
    }

    @Test
    void testFretePadrao() {
        Entrega e = new Entrega("Rua B", 5, "Maria", TipoFrete.PAD);
        BigDecimal frete = e.getTipoFrete().calcular(e);
        // 5 * 1.2 = 6.0
        assertEquals(new BigDecimal("6.00"), frete.setScale(2));
    }

    @Test
    void testFreteEconomicoNormal() {
        Entrega e = new Entrega("Rua C", 10, "Fernando", TipoFrete.ECO);
        BigDecimal frete = e.getTipoFrete().calcular(e);
        // 10 * 1.1 - 5 = 6
        assertEquals(new BigDecimal("6.00"), frete.setScale(2));
    }

    @Test
    void testFreteEconomicoGratis() {
        Entrega e = new Entrega("Rua D", 1.5, "Maria", TipoFrete.ECO);
        BigDecimal frete = e.getTipoFrete().calcular(e);
        assertEquals(BigDecimal.ZERO.setScale(2), frete.setScale(2));
    }

    @Test
    void testPromocaoPesoDesconto() {
        Entrega e = new Entrega("Rua E", 12.0, "João", TipoFrete.EXP);
        Entrega promocional = promocao.aplicar(e);
        // desconto de 1kg -> peso 11
        assertEquals(new BigDecimal("11.0"), promocional.getPeso().setScale(1));
        // Frete recalculado com 11kg
        BigDecimal frete = promocional.getTipoFrete().calcular(promocional);
        assertEquals(new BigDecimal("26.50"), frete.setScale(2));
    }

    @Test
    void testInvalidEntregaExceptionPesoNegativo() {
        assertThrows(InvalidEntregaException.class,
                () -> new Entrega("Rua X", -1, "Ana", TipoFrete.EXP));
    }

    @Test
    void testInvalidEntregaExceptionEnderecoVazio() {
        assertThrows(InvalidEntregaException.class,
                () -> new Entrega("   ", 5, "Ana", TipoFrete.PAD));
    }

    @Test
    void testInvalidEntregaExceptionDestinatarioNulo() {
        assertThrows(InvalidEntregaException.class,
                () -> new Entrega("Rua Y", 5, null, TipoFrete.PAD));
    }

    @Test
    void testInvalidEntregaExceptionTipoFreteNulo() {
        assertThrows(InvalidEntregaException.class,
                () -> new Entrega("Rua Z", 5, "Ana", null));
    }

    @Test
    void testEtiquetaServiceGeraEtiqueta() {
        Entrega e = new Entrega("Rua A", 5, "Maria", TipoFrete.PAD);
        String etiqueta = etiquetaService.gerarEtiqueta(e);

        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorEsperado = currency.format(e.getTipoFrete().calcular(e));

        assertTrue(etiqueta.contains("Maria"));
        assertTrue(etiqueta.contains(valorEsperado));
    }

    @Test
    void testResumoPedido() {
        Entrega e = new Entrega("Rua B", 12.5, "Ana", TipoFrete.EXP);
        String resumo = etiquetaService.gerarResumoPedido(e);
        assertTrue(resumo.contains("Ana"));
        assertTrue(resumo.contains("EXP"));
        assertTrue(resumo.contains("R$"));
    }
}
