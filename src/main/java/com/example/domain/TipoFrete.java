package com.example;

import java.math.BigDecimal;

public enum TipoFrete implements CalculadoraFrete {

    EXP {
        @Override
        public BigDecimal calcular(Entrega e) {
            return e.getPeso()
                    .multiply(BigDecimal.valueOf(1.5))
                    .add(BigDecimal.valueOf(10));
        }
    },

    PAD {
        @Override
        public BigDecimal calcular(Entrega e) {
            return e.getPeso()
                    .multiply(BigDecimal.valueOf(1.2));
        }
    },

    ECO {
        @Override
        public BigDecimal calcular(Entrega e) {
            if (e.getPeso().doubleValue() < 2.0) {
                return BigDecimal.ZERO;
            }
            BigDecimal valor = e.getPeso()
                    .multiply(BigDecimal.valueOf(1.1))
                    .subtract(BigDecimal.valueOf(5));

            return valor.max(BigDecimal.ZERO);
        }
    };

    //Fácil modo de adicionar novos fretes
}
