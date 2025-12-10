package com.example;

public class PromocaoPeso implements PromocaoStrategy {
    private final double limitePeso;
    private final double descontoPeso;

    public PromocaoPeso(double limitePeso, double descontoPeso) {
        this.limitePeso = limitePeso;
        this.descontoPeso = descontoPeso;
    }

    @Override
    public Entrega aplicar(Entrega entrega) {
        double peso = entrega.getPeso().doubleValue();
        if (peso > limitePeso) {
            double novoPeso = Math.max(0, peso - descontoPeso);
            return entrega.withAdjustedPeso(novoPeso);
        }
        return entrega;
    }
}
