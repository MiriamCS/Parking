package com.example.parking.Controller;

import com.example.parking.Service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EntradasSalidasController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/entradasSalidas")
    public String index(Model model) {
        model.addAttribute("estanciasActuales", parkingService.getEstanciasActuales());
        return "entradasSalidas";
    }

    @PostMapping("/entrada")
    public String registrarEntrada(@RequestParam String matricula) {
        parkingService.registrarEntrada(matricula);
        return "redirect:/entradasSalidas";
    }

    @PostMapping("/salida")
    public String registrarSalida(@RequestParam String matricula, RedirectAttributes redirectAttributes) {
        Double precio = parkingService.registrarSalida(matricula);
        redirectAttributes.addFlashAttribute("precio", precio);
        redirectAttributes.addFlashAttribute("matricula", matricula);
        return "redirect:/entradasSalidas";
    }
}
