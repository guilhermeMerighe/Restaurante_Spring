package com.yami.restaurante_spring.controller;

import com.yami.restaurante_spring.model.Ingrediente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ingredientes")
public class IngredientesController {

    // 📋 Listar todos os ingredientes
    @GetMapping
    public String listarIngredientes(Model model) {
        List<Ingrediente> ingredientes = Ingrediente.listarTodos();
        model.addAttribute("ingredientes", ingredientes);
        return "ingredientes";
    }

    // ➕ Exibir formulário de criação
    @GetMapping("/criar")
    public String formCriarIngrediente() {
        return "ingredientes_criar";
    }

    // 💾 Criar ingrediente
    @PostMapping("/criar")
    public String criarIngrediente(@RequestParam String descricao,
                                   @RequestParam boolean unidade,
                                   @RequestParam double valorUnitario,
                                   @RequestParam int quantidadeEstoque) {

        Ingrediente ingrediente = new Ingrediente(0, descricao, unidade, valorUnitario, 0);
        int novoId = ingrediente.inserirRetornandoId();

        // adiciona a quantidade inicial no estoque
        Ingrediente.adicionarAoEstoque(novoId, quantidadeEstoque);

        return "redirect:/ingredientes";
    }

    // ⚙️ Acrescentar ao estoque
    @PostMapping("/{id}/acrescentar")
    public String acrescentarAoEstoque(@PathVariable int id,
                                       @RequestParam int quantidade) {

        Ingrediente.adicionarAoEstoque(id, quantidade);
        return "redirect:/ingredientes";
    }
}
