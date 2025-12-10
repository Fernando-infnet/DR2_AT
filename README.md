# DR2_AT

## Pedido antigo analisado:

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

## Respostas das atividades analíticas

1. Abstrações e distribuição em camadas

Responsável por representar o estado e regras do negócio, sem depender de apresentação ou infraestrutura externa. Inclui:

Entrega — entidade imutável que encapsula dados como endereço, peso, destinatário e tipo de frete.
TipoFrete — enum que implementa CalculadoraFrete, encapsulando a lógica específica de cálculo de cada tipo de frete.
CalculadoraFrete — interface que define o contrato de cálculo de frete.

Exemplo de abstração:

``` java
public interface CalculadoraFrete {
    BigDecimal calcular(Entrega entrega) throws CalculadoraFreteException;
}

public enum TipoFrete implements CalculadoraFrete {
    EXP {
        @Override
        public BigDecimal calcular(Entrega e) {
            return e.getPeso().multiply(BigDecimal.valueOf(1.5)).add(BigDecimal.valueOf(10));
        }
    },
    PAD { ... },
    ECO { ... };
}
```

Aqui substituímos o antigo if-else encadeado (EXP/PAD/ECO) por polimorfismo, permitindo adicionar novos tipos de frete sem modificar código existente.

Responsáveis por processamento e regras de negócio que envolvem múltiplas entidades, como:

EtiquetaService — gera etiquetas e resumos para pedidos, aplicando promoções e calculando o frete.
PromocaoStrategy — interface para aplicar promoções de forma extensível.
PromocaoPeso — implementação que reduz peso quando a entrega excede determinado limite.

Exemplo:

``` java
public class EtiquetaService {
    private final PromocaoStrategy promocao;

    public String gerarResumoPedido(Entrega entrega) {
        Entrega e = promocao.aplicar(entrega);
        BigDecimal valorFrete = e.getTipoFrete().calcular(e);

        return String.format("Pedido para %s com frete %s no valor de %s",
                e.getDestinatario(),
                e.getTipoFrete(),
                currency.format(valorFrete));
    }
}
```

Essa abstração permite trocar a promoção sem alterar EtiquetaService e sem reescrever cálculos de frete.

2. Validações para integridade do domínio

Para evitar estados inválidos, todas as entidades validam entradas no construtor:

``` java
public Entrega(String endereco, double peso, String destinatario, TipoFrete tipoFrete) {
    if (endereco == null || endereco.trim().isEmpty())
        throw new InvalidEntregaException("Endereço inválido.");
    if (peso < 0 || Double.isNaN(peso))
        throw new InvalidEntregaException("Peso inválido.");
    if (destinatario == null || destinatario.trim().isEmpty())
        throw new InvalidEntregaException("Destinatário inválido.");
    if (tipoFrete == null)
        throw new InvalidEntregaException("Tipo de frete inválido.");

    this.endereco = endereco.trim();
    this.peso = BigDecimal.valueOf(peso);
    this.destinatario = destinatario.trim();
    this.tipoFrete = tipoFrete;
}
```
Com essa robustez no código, entradas que anteriormente gerariam erros silenciosos, ou respostas inválidas enviadas diretamente para o usuário. Agora temos um sistema que não deixa passar tais dados com esses problemas, mantendo a integridade do método, pois são utilizada as exception que interrompem a execução e te indicam se algo de errado ocorrer.

3. Boas práticas de nomenclatura e estrutura de código

Clareza e coesão nos nomes:

Entrega → representa uma entrega concreta.
TipoFrete → enum de tipos de frete.
CalculadoraFrete → contrato para calcular frete.
PromocaoStrategy → interface que define promoção extensível.
EtiquetaService → serviço responsável por gerar etiquetas e resumos.

Métodos claros e curtos:

gerarEtiqueta, gerarResumoPedido, aplicar — descrevem exatamente o que fazem.

Variáveis significativas:

peso, destinatario, valorFrete, promocao — sem abreviações ambíguas.

4. Organização de arquivos e pacotes

Sugestão de estrutura usada:

``` bash
src/main/java/com/example/
 ├─ domain/
 │   ├─ Entrega.java
 │   ├─ TipoFrete.java
 │   ├─ CalculadoraFrete.java
 │   └─ exception/
 │       ├─ InvalidEntregaException.java
 │       ├─ CalculadoraFreteException.java
 │       └─ FreteNaoEncontradoException.java
 ├─ service/
 │   ├─ EtiquetaService.java
 │   └─ PromocaoStrategy.java
 │       └─ PromocaoPeso.java
 └─ Main.java
```

Facilita manutenção e extensões, caso novos tipos, serviços, sejam necessários.

5. Substituição de estruturas rígidas

Antes, o código original tinha:

``` java
if (tipoFrete.equals("EXP")) { ... }
else if (tipoFrete.equals("PAD")) { ... }
else if (tipoFrete.equals("ECO")) { ... }
```

No novo design:

Cada tipo de frete implementa CalculadoraFrete diretamente (TipoFrete.EXP, TipoFrete.PAD, TipoFrete.ECO).
