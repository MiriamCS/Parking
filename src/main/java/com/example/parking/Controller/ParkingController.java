package com.example.parking.Controller;

import com.example.parking.Service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("estancias", parkingService.getEstanciasActuales());
        return "index";
    }

    @PostMapping("/enParking")
    public String registrosActuales() {
        return "redirect:/entradasSalidas";
    }

    @PostMapping("/addOficial")
    public String darDeAltaVehiculoOficial() {
        return "redirect:/altasOficial";
    }

    @PostMapping("/addResidente")
    public String darDeAltaVehiculoResidente() {
        return "redirect:/altasResidentes";
    }


    @PostMapping("/nuevoMes")
    public String comienzaMes() {
        parkingService.comienzaMes();
        return "redirect:/";
    }

    @PostMapping("/pagosResidentes")
    public String generaInformePagosResidentes() {
        return "redirect:/registrosResidentes";
    }
}
