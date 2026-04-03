package com.financeiro.controle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.financeiro.controle.repository.GastoRepository;
import com.financeiro.controle.model.Gasto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final GastoRepository gastoRepository;

    public HomeController(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    @GetMapping("/editar")
    public String editar(@RequestParam Long id, Model model) {

        Gasto gasto = gastoRepository.findById(id).orElse(null);

        model.addAttribute("gasto", gasto);

        return "editar";
    }



    @GetMapping("/")
    public String home(@RequestParam(required = false) String mes, Model model) {

        // Pega todos os gastos do banco
        List<Gasto> gastos = gastoRepository.findAll()
                .stream()
                .sorted((g1, g2) -> g2.getData().compareTo(g1.getData()))
                .toList();

        if (mes != null && !mes.isEmpty()) {

            gastos = gastos.stream()
                    .filter(g -> g.getData() != null && g.getData().startsWith(mes))
                    .toList();
        }

        Map<String, Double> gastosPorMes = gastos.stream()
                .filter(g -> "SAIDA".equals(g.getTipo()))
                .collect(Collectors.groupingBy(
                        g -> g.getData().substring(5,7),
                        Collectors.summingDouble(Gasto::getValor)
                ));

        model.addAttribute("meses", gastosPorMes.keySet());
        model.addAttribute("valoresMeses", gastosPorMes.values());

        // Calcula entradas
        double totalEntradas = gastos.stream()
                .filter(g -> "ENTRADA".equals(g.getTipo()))
                .mapToDouble(Gasto::getValor)
                .sum();

        // Calcula saídas
        double totalSaidas = gastos.stream()
                .filter(g -> "SAIDA".equals(g.getTipo()))
                .mapToDouble(Gasto::getValor)
                .sum();

        // Calcula saldo
        double saldo = totalEntradas - totalSaidas;

        Map<String, Double> gastosPorCategoria = gastos.stream()
                .filter(g -> "SAIDA".equals(g.getTipo()))
                .collect(Collectors.groupingBy(
                        Gasto::getCategoria,
                        Collectors.summingDouble(Gasto::getValor)
                ));

        model.addAttribute("categorias", gastosPorCategoria.keySet());
        model.addAttribute("valoresCategorias", gastosPorCategoria.values());

        // Adiciona os valores ao model para o Thymeleaf
        model.addAttribute("gastos", gastos);
        model.addAttribute("entradas", totalEntradas);
        model.addAttribute("saidas", totalSaidas);
        model.addAttribute("saldo", saldo);

        return "home";
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam String nome,
                            @RequestParam double valor,
                            @RequestParam String categoria,
                            @RequestParam String tipo,
                            @RequestParam String data) {

        if(valor <= 0){
            return "redirect:/";
        }

        Gasto gasto = new Gasto();
        gasto.setNome(nome);
        gasto.setValor(valor);
        gasto.setCategoria(categoria);
        gasto.setTipo(tipo);
        gasto.setData(data);

        gastoRepository.save(gasto);

        return "redirect:/";
    }

    @PostMapping("/remover")
    public String remover(@RequestParam Long id) {
        gastoRepository.deleteById(id);
        return "redirect:/";
    }


        @PostMapping("/atualizar")
        public String atualizar(Gasto gasto){

            gastoRepository.save(gasto);

            return "redirect:/";
        }
    }




