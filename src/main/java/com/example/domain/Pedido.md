``` java
package com.example;

public class Pedido {
    public String endereco;
    public double peso;
    public String tipoFrete;
    public String destinatario;

    public double calcularFrete() {
        if (tipoFrete.equals("EXP")) {
            return peso * 1.5 + 10;
        } else if (tipoFrete.equals("PAD")) {
            return peso * 1.2;
        } else if (tipoFrete.equals("ECO")) {
            return peso * 1.1 - 5;
        } else {
            return 0;
        }
    }

    public String gerarEtiqueta() {
        return "Destinatário: " + destinatario + "\nEndereço: " + endereco + "\nValor do Frete: R$" + calcularFrete();
    }

    public String gerarResumoPedido() {
        return "Pedido para " + destinatario + " com frete tipo " + tipoFrete + " no valor de R$" + calcularFrete();
    }

    public void aplicarFretePromocional() {
        if (peso > 10) {
            peso = peso - 1;
        }
    }

    public boolean isFreteGratis() {
        return tipoFrete.equals("ECO") && peso < 2;
    }
}
``` 