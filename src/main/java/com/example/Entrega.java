package com.example;

import java.math.BigDecimal;
import java.util.Objects;

public final class Entrega {
    private final String endereco;
    private final BigDecimal peso;
    private final String destinatario;
    private final TipoFrete tipoFrete;

    public Entrega(String endereco, double peso, String destinatario, TipoFrete tipoFrete) {
        validate(endereco, peso, destinatario, tipoFrete);
        this.endereco = endereco.trim();
        this.peso = BigDecimal.valueOf(peso);
        this.destinatario = destinatario.trim();
        this.tipoFrete = tipoFrete;
    }

    private void validate(String endereco, double peso, String destinatario, TipoFrete tipoFrete) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new InvalidEntregaException("Endereço inválido.");
        }
        if (Double.isNaN(peso) || peso < 0) {
            throw new InvalidEntregaException("Peso inválido: " + peso);
        }
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new InvalidEntregaException("Destinatário inválido.");
        }
        if (tipoFrete == null) {
            throw new InvalidEntregaException("Tipo de frete inválido.");
        }
    }

    public String getEndereco() { 
        return endereco; 
    }

    public BigDecimal getPeso() { 
        return peso; 
    }

    public String getDestinatario() { 
        return destinatario; 
    }
    
    public TipoFrete getTipoFrete() { 
        return tipoFrete; 
    }

    public Entrega withAdjustedPeso(double novoPeso) {
        return new Entrega(this.endereco, novoPeso, this.destinatario, this.tipoFrete);
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "endereco='" + endereco + '\'' +
                ", peso=" + peso +
                ", destinatario='" + destinatario + '\'' +
                ", tipoFrete=" + tipoFrete +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrega)) return false;
        Entrega entrega = (Entrega) o;
        return Objects.equals(endereco, entrega.endereco) &&
                Objects.equals(peso, entrega.peso) &&
                Objects.equals(destinatario, entrega.destinatario) &&
                tipoFrete == entrega.tipoFrete;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco, peso, destinatario, tipoFrete);
    }
}
