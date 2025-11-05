package com.yami.restaurante_spring.model;

public class Ingrediente {
    private int codIngrediente;
    private String descricao;
    private boolean unidade; // 0 = unidade, 1 = kg
    private double valorUnitario;
    private int quantidadeEstoque;

    public Ingrediente() {}

    public Ingrediente(int codIngrediente, String descricao, boolean unidade, double valorUnitario, int quantidadeEstoque) {
        this.codIngrediente = codIngrediente;
        this.descricao = descricao;
        this.unidade = unidade;
        this.valorUnitario = valorUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters e Setters
    public int getCodIngrediente() { return codIngrediente; }
    public void setCodIngrediente(int codIngrediente) { this.codIngrediente = codIngrediente; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isUnidade() { return unidade; }
    public void setUnidade(boolean unidade) { this.unidade = unidade; }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
}
