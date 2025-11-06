package com.yami.restaurante_spring.model;

import com.yami.restaurante_spring.config.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // =======================================================
    // 🔹 Métodos JDBC
    // =======================================================

    public static List<Ingrediente> listarTodos() {
        List<Ingrediente> lista = new ArrayList<>();
        String sql = "SELECT cod_ingrediente, descricao, unidade, valor_unitario, quantidade_estoque FROM ingredientes ORDER BY descricao ASC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ingrediente i = new Ingrediente(
                        rs.getInt("cod_ingrediente"),
                        rs.getString("descricao"),
                        rs.getBoolean("unidade"),
                        rs.getDouble("valor_unitario"),
                        rs.getInt("quantidade_estoque")
                );
                lista.add(i);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar ingredientes: " + e.getMessage());
        }
        return lista;
    }

    
    public int inserirRetornandoId() {
        String sql = "{CALL sp_ingredientes_insert(?, ?, ?)}";
        int novoId = -1;

        try (Connection conn = Conexao.getConnection();
            CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, this.descricao);
            stmt.setBoolean(2, this.unidade);
            stmt.setDouble(3, this.valorUnitario);

            stmt.execute();

            // ✅ Captura do ID recém-inserido após a procedure
            try (Statement stmt2 = conn.createStatement();
                ResultSet rs = stmt2.executeQuery("SELECT LAST_INSERT_ID()")) {
                if (rs.next()) novoId = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir ingrediente: " + e.getMessage());
        }

        return novoId;
    }





    public static void adicionarAoEstoque(int codIngrediente, int quantidade) {
        String sql = "{CALL sp_estoque_add(?, ?)}";
        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codIngrediente);
            stmt.setInt(2, quantidade);
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("❌ Erro ao adicionar ao estoque: " + e.getMessage());
        }
    }
}
