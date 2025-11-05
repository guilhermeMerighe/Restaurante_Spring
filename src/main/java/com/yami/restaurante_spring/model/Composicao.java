package com.yami.restaurante_spring.model;

public class Composicao {
    private int codIngrediente;
    private int codPrato;
    private int quantidade;
    private double valorUnitario;

    public Composicao() {}

    public Composicao(int codIngrediente, int codPrato, int quantidade, double valorUnitario) {
        this.codIngrediente = codIngrediente;
        this.codPrato = codPrato;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public int getCodIngrediente() { return codIngrediente; }
    public void setCodIngrediente(int codIngrediente) { this.codIngrediente = codIngrediente; }

    public int getCodPrato() { return codPrato; }
    public void setCodPrato(int codPrato) { this.codPrato = codPrato; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
}
