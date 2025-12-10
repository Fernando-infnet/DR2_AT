package com.example;

public class Main {
    public static void main(String[] args) {
        PromocaoStrategy promocao = new PromocaoPeso(10.0, 1.0);
        EtiquetaService etiquetaService = new EtiquetaService(promocao);

        Entrega e1 = new Entrega("Rua A, 1", 12.5, "Ana", TipoFrete.EXP);
        System.out.println(etiquetaService.gerarEtiqueta(e1));
        System.out.println(etiquetaService.gerarResumoPedido(e1));

        Entrega e2 = new Entrega("Rua B, 45", 5, "Maria", TipoFrete.PAD);
        System.out.println(etiquetaService.gerarEtiqueta(e2));
        System.out.println(etiquetaService.gerarResumoPedido(e2));

        Entrega e3 = new Entrega("Rua C, 90", 1.5, "Fernando", TipoFrete.ECO);
        System.out.println(etiquetaService.gerarEtiqueta(e3));
        System.out.println(etiquetaService.gerarResumoPedido(e3));

        //Teste regra frete grátis
        Entrega e4 = new Entrega("Rua C, 90", 10, "Fernando", TipoFrete.ECO);
        System.out.println(etiquetaService.gerarEtiqueta(e4));
        System.out.println(etiquetaService.gerarResumoPedido(e4));
    }
}