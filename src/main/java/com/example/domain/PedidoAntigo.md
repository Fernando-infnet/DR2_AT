``` java
package com.example;

public class Pedido {
    //Estrutura de públics que não é ideal para a classe
    public String endereco;
    public double peso; //não é o tipo ideal de se usar
    public String tipoFrete; // Melhor definir os tipos possíveis de frete ao invés de string
    public String destinatario;

    //falta de tratamento de casos de exceção
    public double calcularFrete() { // Leitura confusa, separar tipagem e adicionar no objeto o preço do tipo por peso
        if (tipoFrete.equals("EXP")) {
            return peso * 1.5 + 10; // Números mágicos
        } else if (tipoFrete.equals("PAD")) {
            return peso * 1.2; // Números mágicos
        } else if (tipoFrete.equals("ECO")) {
            return peso * 1.1 - 5; // Números mágicos
        } else {
            return 0;
        }
    }

    public String gerarEtiqueta() { // Retorno confuso, não há separação de valor e verificações
    //o retorno pode não funcionar em casos de erros e entradas não esperadas
        return "Destinatário: " + destinatario + "\nEndereço: " + endereco + "\nValor do Frete: R$" + calcularFrete();
    }

    public String gerarResumoPedido() { // Retorno confuso, não há separação de valor e verificações
    //o retorno pode não funcionar em casos de erros e entradas não esperadas
        return "Pedido para " + destinatario + " com frete tipo " + tipoFrete + " no valor de R$" + calcularFrete();
    }

    public void aplicarFretePromocional() { // Números mágicos
        if (peso > 10) {
            peso = peso - 1;
        }
    }

    public boolean isFreteGratis() { // Adicionar no objeto ECO regras para 0 frete
        return tipoFrete.equals("ECO") && peso < 2;
    }
}
``` 