package com.yami.restaurante_spring.controller;

import com.yami.restaurante_spring.model.Pedido;
import com.yami.restaurante_spring.model.Prato;
import com.yami.restaurante_spring.model.ItemPedido; // 👈 não esqueça de importar!
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    // 📋 Lista de pedidos
    @GetMapping
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = Pedido.listarTodos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos";
    }


    // ➕ Exibir tela para criar novo pedido
    @GetMapping("/novo")
    public String novoPedidoForm(Model model) {
        List<com.yami.restaurante_spring.model.Cliente> clientes = com.yami.restaurante_spring.model.Cliente.listarTodos();
        model.addAttribute("clientes", clientes);
        return "pedidos_criar";
    }

    // ➕ Criar pedido (via seleção de cliente)
    @PostMapping("/criar")
    public String criarPedidoPorSelecao(@RequestParam int codCliente, Model model) {
        if (codCliente <= 0) {
            model.addAttribute("erro", "Selecione um cliente válido.");
            model.addAttribute("clientes", com.yami.restaurante_spring.model.Cliente.listarTodos());
            return "pedidos_criar";
        }

        boolean sucesso = Pedido.criar(codCliente);
        if (!sucesso) {
            model.addAttribute("erro", "Erro ao criar o pedido.");
            model.addAttribute("clientes", com.yami.restaurante_spring.model.Cliente.listarTodos());
            return "pedidos_criar";
        }

        return "redirect:/pedidos";
    }

    // ➕ Criar pedido (via ID digitado)
    @PostMapping("/criar-por-id")
    public String criarPedidoPorId(@RequestParam int codClienteId, Model model) {
        if (codClienteId <= 0) {
            model.addAttribute("erro", "Informe um ID válido.");
            model.addAttribute("clientes", com.yami.restaurante_spring.model.Cliente.listarTodos());
            return "pedidos_criar";
        }

        boolean sucesso = Pedido.criar(codClienteId);
        if (!sucesso) {
            model.addAttribute("erro", "Erro ao criar o pedido.");
            model.addAttribute("clientes", com.yami.restaurante_spring.model.Cliente.listarTodos());
            return "pedidos_criar";
        }

        return "redirect:/pedidos";
    }




    // 🔍 Detalhes do pedido
    @GetMapping("/{id}")
    public String detalhesPedido(@PathVariable int id, Model model) {
        Pedido pedido = Pedido.buscarPorId(id);
        List<ItemPedido> itens = ItemPedido.listarPorPedido(id);

        model.addAttribute("pedido", pedido);
        model.addAttribute("itens", itens);

        return "pedidos_detalhes";
    }

    // 🔁 Atualizar quantidade
    @PostMapping("/{id}/itens/{codPrato}/atualizar")
    public String atualizarItem(@PathVariable int id,
                                @PathVariable int codPrato,
                                @RequestParam int novaQuantidade,
                                Model model) {
        if (novaQuantidade <= 0) {
            model.addAttribute("erro", "Valor inválido.");
            return detalhesPedido(id, model);
        }

        boolean sucesso = ItemPedido.atualizarQuantidade(id, codPrato, novaQuantidade);
        if (!sucesso) {
            model.addAttribute("erro", "Ingredientes insuficientes para essa quantidade.");
        }

        return "redirect:/pedidos/" + id;
    }

    // ❌ Remover item
    @PostMapping("/{id}/itens/{codPrato}/remover")
    public String removerItem(@PathVariable int id, @PathVariable int codPrato) {
        ItemPedido.remover(id, codPrato);
        return "redirect:/pedidos/" + id;
    }

    // ✅ Finalizar pedido
    @PostMapping("/{id}/finalizar")
    public String finalizarPedido(@PathVariable int id) {
        Pedido.finalizar(id);
        return "redirect:/pedidos/" + id;
    }












    // ➕ Exibir tela para adicionar item
    @GetMapping("/{id}/itens/novo")
    public String adicionarItemForm(@PathVariable int id, Model model) {
        Pedido pedido = Pedido.buscarPorId(id);
        List<Prato> pratos = Prato.listarTodos();

        model.addAttribute("pedido", pedido);
        model.addAttribute("pratos", pratos);
        return "pedidos_adicionar_item";
    }

    // ➕ Inserir novo item no pedido
    @PostMapping("/{id}/itens/adicionar")
    public String adicionarItem(@PathVariable int id,
                                @RequestParam int codPrato,
                                @RequestParam int quantidade,
                                Model model) {

        Pedido pedido = Pedido.buscarPorId(id);

        // validações básicas
        if (codPrato == 0) {
            model.addAttribute("erro", "Selecione um prato válido.");
            model.addAttribute("pedido", pedido);
            model.addAttribute("pratos", Prato.listarTodos());
            return "pedidos_adicionar_item";
        }
        if (quantidade <= 0) {
            model.addAttribute("erro", "Valor inválido para quantidade.");
            model.addAttribute("pedido", pedido);
            model.addAttribute("pratos", Prato.listarTodos());
            return "pedidos_adicionar_item";
        }

        boolean sucesso = ItemPedido.adicionar(id, codPrato, quantidade);

        if (!sucesso) {
            model.addAttribute("erro", "Ingredientes insuficientes ou prato já existente.");
            model.addAttribute("pedido", pedido);
            model.addAttribute("pratos", Prato.listarTodos());
            return "pedidos_adicionar_item";
        }

        return "redirect:/pedidos/" + id;
    }


    
}
