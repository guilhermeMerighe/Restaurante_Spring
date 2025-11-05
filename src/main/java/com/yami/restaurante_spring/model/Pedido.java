package com.yami.restaurante_spring.model;

import com.yami.restaurante_spring.config.Conexao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int codPedido;
    private int codCliente;
    private String nomeCliente; // 🔹 útil pra exibir na tela
    private LocalDateTime dataPedido;
    private double valorTotal;
    private boolean status; // false = em andamento, true = finalizado

    public Pedido() {}

    public Pedido(int codPedido, int codCliente, LocalDateTime dataPedido, double valorTotal, boolean status) {
        this.codPedido = codPedido;
        this.codCliente = codCliente;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    // =========================
    // Getters e Setters
    // =========================
    public int getCodPedido() { return codPedido; }
    public void setCodPedido(int codPedido) { this.codPedido = codPedido; }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    // =========================
    // 🔹 Métodos JDBC
    // =========================

    // Listar todos os pedidos
    public static List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = """
            SELECT p.cod_pedido, p.cod_cliente, c.nome AS nome_cliente, p.data_pedido, p.valor_total, p.status
            FROM pedidos p
            INNER JOIN clientes c ON p.cod_cliente = c.cod_cliente
            ORDER BY p.data_pedido DESC
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setCodPedido(rs.getInt("cod_pedido"));
                p.setCodCliente(rs.getInt("cod_cliente"));
                p.setNomeCliente(rs.getString("nome_cliente"));
                Timestamp ts = rs.getTimestamp("data_pedido");
                if (ts != null) p.setDataPedido(ts.toLocalDateTime());
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setStatus(rs.getBoolean("status"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar pedidos: " + e.getMessage());
        }

        return lista;
    }

    // Buscar pedido por ID
    public static Pedido buscarPorId(int codPedido) {
        Pedido p = null;
        String sql = """
            SELECT p.cod_pedido, p.cod_cliente, c.nome AS nome_cliente, p.data_pedido, p.valor_total, p.status
            FROM pedidos p
            INNER JOIN clientes c ON p.cod_cliente = c.cod_cliente
            WHERE p.cod_pedido = ?
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codPedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Pedido();
                p.setCodPedido(rs.getInt("cod_pedido"));
                p.setCodCliente(rs.getInt("cod_cliente"));
                p.setNomeCliente(rs.getString("nome_cliente"));
                Timestamp ts = rs.getTimestamp("data_pedido");
                if (ts != null) p.setDataPedido(ts.toLocalDateTime());
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setStatus(rs.getBoolean("status"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar pedido: " + e.getMessage());
        }

        return p;
    }

    // Finalizar pedido (chama a procedure)
    public static void finalizar(int codPedido) {
        String sql = "{CALL sp_pedidos_finalize(?)}";
        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codPedido);
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("❌ Erro ao finalizar pedido: " + e.getMessage());
        }
    }

    public static boolean criar(int codCliente) {
    String sql = "{CALL sp_pedidos_insert(?)}";

    try (Connection conn = Conexao.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

        stmt.setInt(1, codCliente);
        stmt.execute();
        return true;

    } catch (SQLException e) {
        System.out.println("❌ Erro ao criar pedido: " + e.getMessage());
        return false;
    }
}
}
