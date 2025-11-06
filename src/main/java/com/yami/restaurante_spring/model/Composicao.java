package com.yami.restaurante_spring.model;

import com.yami.restaurante_spring.config.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // 📜 Listar ingredientes de um prato
    public static List<Composicao> listarPorPrato(int codPrato) {
        List<Composicao> lista = new ArrayList<>();
        String sql = """
                SELECT c.cod_ingrediente, i.descricao, c.quantidade, i.valor_unitario
                FROM composicao c
                JOIN ingredientes i ON c.cod_ingrediente = i.cod_ingrediente
                WHERE c.cod_prato = ?
                """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codPrato);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Composicao comp = new Composicao();
                comp.setCodIngrediente(rs.getInt("cod_ingrediente"));
                comp.setCodPrato(codPrato);
                comp.setQuantidade(rs.getInt("quantidade"));
                comp.setValorUnitario(rs.getDouble("valor_unitario"));
                lista.add(comp);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar composição: " + e.getMessage());
        }

        return lista;
    }

    // ⚙️ Inserir via procedure
    public void inserir() {
        String sql = "{CALL sp_composicao_insert(?, ?, ?)}";

        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, codIngrediente);
            stmt.setInt(2, codPrato);
            stmt.setInt(3, quantidade);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir composição: " + e.getMessage());
        }
    }
}
