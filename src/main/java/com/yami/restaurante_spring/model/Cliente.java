package com.yami.restaurante_spring.model;

import com.yami.restaurante_spring.config.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int codCliente;
    private String nome;
    private String telefone;
    private String cpf;
    private String rg;
    private String endereco;
    private String email;

    public Cliente() {}

    public Cliente(int codCliente, String nome, String telefone, String cpf, String rg, String endereco, String email) {
        this.codCliente = codCliente;
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.email = email;
    }

    // Getters e Setters
    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    // ================================================
    // 🔹 MÉTODOS JDBC (SELECTs manuais, procedures p/ DML)
    // ================================================

    // 📜 Obter todos os clientes
    public static List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT cod_cliente, nome, telefone, cpf, rg, endereco, email FROM clientes ORDER BY nome ASC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("cod_cliente"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getString("rg"),
                        rs.getString("endereco"),
                        rs.getString("email")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar clientes: " + e.getMessage());
        }

        return lista;
    }

    // 📜 Obter cliente por ID
    public static Cliente buscarPorId(int id) {
        Cliente c = null;
        String sql = "SELECT * FROM clientes WHERE cod_cliente = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Cliente(
                        rs.getInt("cod_cliente"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("cpf"),
                        rs.getString("rg"),
                        rs.getString("endereco"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar cliente: " + e.getMessage());
        }

        return c;
    }

    // ⚙️ Inserir cliente via procedure
    public void inserir() {
        String sql = "{CALL sp_clientes_insert(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, this.nome);
            stmt.setString(2, this.telefone);
            stmt.setString(3, this.cpf);
            stmt.setString(4, this.rg);
            stmt.setString(5, this.endereco);
            stmt.setString(6, this.email);

            stmt.execute();

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir cliente: " + e.getMessage());
        }
    }

    // ⚙️ Atualizar cliente via procedure
    public void atualizar() {
        String sql = "{CALL sp_clientes_update(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, this.codCliente);
            stmt.setString(2, this.nome);
            stmt.setString(3, this.telefone);
            stmt.setString(4, this.cpf);
            stmt.setString(5, this.rg);
            stmt.setString(6, this.endereco);
            stmt.setString(7, this.email);

            stmt.execute();

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    // ⚙️ Excluir cliente via procedure
    public static void excluir(int id) {
        String sql = "{CALL sp_clientes_delete(?)}";

        try (Connection conn = Conexao.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("❌ Erro ao excluir cliente: " + e.getMessage());
        }
    }
}
