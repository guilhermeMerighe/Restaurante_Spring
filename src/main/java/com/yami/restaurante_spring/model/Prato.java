package com.yami.restaurante_spring.model;

import com.yami.restaurante_spring.config.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Prato {
    private int codPrato;
    private String descricao;
    private double valorUnitario;

    public Prato() {}

    public Prato(int codPrato, String descricao, double valorUnitario) {
        this.codPrato = codPrato;
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
    }

    public int getCodPrato() { return codPrato; }
    public void setCodPrato(int codPrato) { this.codPrato = codPrato; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    // =====================================================
    // 🔹 Métodos JDBC - integração com o banco de dados
    // =====================================================

    /**
     * Lista todos os pratos disponíveis, ordenados por descrição.
     */
    public static List<Prato> listarTodos() {
        List<Prato> lista = new ArrayList<>();
        String sql = "SELECT cod_prato, descricao, valor_unitario FROM pratos ORDER BY descricao";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Prato p = new Prato();
                p.setCodPrato(rs.getInt("cod_prato"));
                p.setDescricao(rs.getString("descricao"));
                p.setValorUnitario(rs.getDouble("valor_unitario"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar pratos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Busca um prato específico pelo ID.
     */
    public static Prato buscarPorId(int codPrato) {
        Prato p = null;
        String sql = "SELECT cod_prato, descricao, valor_unitario FROM pratos WHERE cod_prato = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codPrato);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Prato();
                p.setCodPrato(rs.getInt("cod_prato"));
                p.setDescricao(rs.getString("descricao"));
                p.setValorUnitario(rs.getDouble("valor_unitario"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar prato: " + e.getMessage());
        }

        return p;
    }
}
