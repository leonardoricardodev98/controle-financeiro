package com.financeiro.controle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
import com.financeiro.controle.repository.GastoRepository;
import com.financeiro.controle.model.Gasto;



@Controller
public class HomeController {

    private final GastoRepository gastoRepository;

    public HomeController(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }



    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("gastos", gastoRepository.findAll());
        return "home";
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam String nome,
                            @RequestParam double valor,
                            @RequestParam String categoria,
                            @RequestParam String tipo,
                            @RequestParam String data) {

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
}
