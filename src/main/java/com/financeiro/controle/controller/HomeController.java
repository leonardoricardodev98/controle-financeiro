package com.financeiro.controle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;



@Controller
public class HomeController {

    private List<String> gastos = new ArrayList<>();

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("gastos", gastos);
        return "home";
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam String nome,
                            @RequestParam double valor,
                            Model model) {

        gastos.add(nome + " - R$ " + valor);

        model.addAttribute("gastos", gastos);

        return "home";
    }

    @PostMapping("/remover")
    public String remover(@RequestParam int index, Model model) {

        if(index >= 0 && index < gastos.size()) {
            gastos.remove(index);
        }

        model.addAttribute("gastos", gastos);
        return "home";
    }
}
