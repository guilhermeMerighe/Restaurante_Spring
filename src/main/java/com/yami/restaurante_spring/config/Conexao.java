package com.yami.restaurante_spring.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // URL do banco de dados — ajuste conforme seu sistema
    private static final String URL = "jdbc:mysql://localhost:3306/restaurante_laravel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Método para obter uma conexão
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
