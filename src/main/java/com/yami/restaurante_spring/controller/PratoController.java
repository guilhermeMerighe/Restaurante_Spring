package com.yami.restaurante_spring.controller;

import com.yami.restaurante_spring.model.Prato;
import com.yami.restaurante_spring.model.Composicao;
import com.yami.restaurante_spring.model.Ingrediente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pratos")
public class PratoController {

    // 📋 Lista de pratos
    @GetMapping
    public String listarPratos(Model model) {
        List<Prato> pratos = Prato.listarTodos();
        model.addAttribute("pratos", pratos);
        return "pratos";
    }

    // 🔍 Detalhes do prato
    @GetMapping("/{id}")
    public String detalhesPrato(@PathVariable int id, Model model) {
        Prato prato = Prato.buscarPorId(id);
        model.addAttribute("prato", prato);

        // ingredientes (composições)
        List<Composicao> composicoes = Composicao.listarPorPrato(id);
        model.addAttribute("composicoes", composicoes);

        return "pratos_detalhes";
    }

    // ➕ Formulário de criação
    @GetMapping("/criar")
    public String formCriarPrato(Model model) {
        model.addAttribute("ingredientes", Ingrediente.listarTodos());
        return "pratos_criar";
    }

    // 💾 Criar prato e adicionar ingredientes
    @PostMapping("/criar")
    public String criarPrato(
            @RequestParam String descricao,
            @RequestParam double valorUnitario,
            @RequestParam(name = "ingrediente", required = false) List<Integer> ingredientesIds,
            @RequestParam(name = "quantidade", required = false) List<Integer> quantidades
    ) {
        Prato prato = new Prato(0, descricao, valorUnitario);
        int novoId = prato.inserirRetornandoId();

        if (ingredientesIds != null && quantidades != null) {
            for (int i = 0; i < ingredientesIds.size(); i++) {
                Composicao composicao = new Composicao(
                        ingredientesIds.get(i),
                        novoId,
                        quantidades.get(i),
                        0
                );
                composicao.inserir();
            }
        }

        return "redirect:/pratos/" + novoId;
    }
}
