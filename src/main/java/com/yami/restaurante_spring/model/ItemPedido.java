package com.yami.restaurante_spring.model;

import com.yami.restaurante_spring.config.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedido {
    private int codPedido;
    private int codPrato;
    private String nomePrato;
    private int quantidade;
    private double subtotal;

    // Getters e Setters
    public int getCodPedido() { return codPedido; }
    public void setCodPedido(int codPedido) { this.codPedido = codPedido; }

    public int getCodPrato() { return codPrato; }
    public void setCodPrato(int codPrato) { this.codPrato = codPrato; }

    public String getNomePrato() { return nomePrato; }
    public void setNomePrato(String nomePrato) { this.nomePrato = nomePrato; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    // =====================================
    // 🔹 Métodos JDBC
    // =====================================

    public static List<ItemPedido> listarPorPedido(int codPedido) {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = """
            SELECT i.cod_pedido, i.cod_prato, p.descricao AS nome_prato,
                i.quantidade, (i.quantidade * p.valor_unitario) AS subtotal
            FROM itens_pedido i
            INNER JOIN pratos p ON i.cod_prato = p.cod_prato
            WHERE i.cod_pedido = ?
        """;

        try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemPedido item = new ItemPedido();
                item.setCodPedido(rs.getInt("cod_pedido"));
                item.setCodPrato(rs.getInt("cod_prato"));
                item.setNomePrato(rs.getString("nome_prato"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setSubtotal(rs.getDouble("subtotal"));
                lista.add(item);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar itens do pedido: " + e.getMessage());
        }

        return lista;
    }


    public static boolean atualizarQuantidade(int codPedido, int codPrato, int novaQtd) {
        String sql = "{CALL sp_itens_pedido_update(?, ?, ?)}";
        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codPedido);
            stmt.setInt(2, codPrato);
            stmt.setInt(3, novaQtd);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Falha ao atualizar quantidade: " + e.getMessage());
            return false;
        }
    }

    public static void remover(int codPedido, int codPrato) {
        String sql = "{CALL sp_itens_pedido_delete(?, ?)}";
        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codPedido);
            stmt.setInt(2, codPrato);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("❌ Erro ao remover item: " + e.getMessage());
        }
    }

    public static boolean adicionar(int codPedido, int codPrato, int quantidade) {
        String sql = "{CALL sp_itens_pedido_insert(?, ?, ?)}";
        try (Connection conn = Conexao.getConnection();
            CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codPedido);
            stmt.setInt(2, codPrato);
            stmt.setInt(3, quantidade);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Erro ao adicionar item: " + e.getMessage());
            return false;
        }
    }

}
