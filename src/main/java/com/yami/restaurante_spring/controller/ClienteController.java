package com.yami.restaurante_spring.controller;

import com.yami.restaurante_spring.model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    // 📋 Listar todos os clientes
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = Cliente.listarTodos();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    // 🔍 Detalhes de um cliente
    @GetMapping("/{id}")
    public String detalhesCliente(@PathVariable int id, Model model) {
        Cliente cliente = Cliente.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes_detalhes";
    }

    // 🗑️ Excluir cliente (via procedure)
    @PostMapping("/{id}/excluir")
    public String excluirCliente(@PathVariable int id) {
        Cliente.excluir(id);
        return "redirect:/clientes";
    }

    // ✏️ Atualizar cliente (via procedure)
    @PostMapping("/{id}/atualizar")
    public String atualizarCliente(@PathVariable int id,
                                   @RequestParam String nome,
                                   @RequestParam String telefone,
                                   @RequestParam String cpf,
                                   @RequestParam String rg,
                                   @RequestParam String endereco,
                                   @RequestParam String email) {
        Cliente cliente = new Cliente(id, nome, telefone, cpf, rg, endereco, email);
        cliente.atualizar();
        return "redirect:/clientes/" + id;
    }

    // 🆕 Exibir tela de criação de cliente
    @GetMapping("/criar")
    public String exibirFormularioCriar() {
        return "clientes_criar";
    }

    // 💾 Inserir novo cliente (via procedure)
    @PostMapping("/criar")
    public String criarCliente(@RequestParam String nome,
                            @RequestParam(required = false) String telefone,
                            @RequestParam(required = false) String cpf,
                            @RequestParam(required = false) String rg,
                            @RequestParam(required = false) String endereco,
                            @RequestParam(required = false) String email,
                            Model model) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                model.addAttribute("erro", "⚠️ O campo 'Nome' é obrigatório.");
                return "clientes_criar";
            }

            Cliente cliente = new Cliente(0, nome, telefone, cpf, rg, endereco, email);
            cliente.inserir();

            // 🔹 Após inserir, buscar o último cliente cadastrado (pelo maior ID)
            List<Cliente> clientes = Cliente.listarTodos();
            if (!clientes.isEmpty()) {
                Cliente ultimo = clientes.get(clientes.size() - 1);
                return "redirect:/clientes/" + ultimo.getCodCliente();
            }

            // fallback: se algo der errado, volta pra lista
            return "redirect:/clientes";

        } catch (Exception e) {
            model.addAttribute("erro", "❌ Erro ao criar cliente: " + e.getMessage());
            return "clientes_criar";
        }
    }

}
