package com.example.parking.Controller;

import com.example.parking.Service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AltasOficialesController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/altasOficial")
    public String index(Model model) {
        model.addAttribute("matriculasOficiales", parkingService.getMatriculasOficiales());
        return "altasOficial";
    }

    @PostMapping("/altaOficial")
    public String darDeAltaVehiculoOficial(@RequestParam String matricula) {
        parkingService.darDeAltaVehiculoOficial(matricula);
        return "redirect:/altasOficial";
    }

}
