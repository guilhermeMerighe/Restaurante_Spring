package com.yami.restaurante_spring;

import com.yami.restaurante_spring.config.Conexao;
import java.sql.Connection;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection conn = Conexao.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexão bem-sucedida!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
