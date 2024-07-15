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
public class AltasOficialesController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/altasOficial")
    public String index(Model model) {
        model.addAttribute("estanciasOficiales", parkingService.getNumEstanciasVehiculosOficial());
        return "altasOficial";

    }

    @PostMapping("/altaOficial")
    public String darDeAltaVehiculoOficial(@RequestParam String matricula, RedirectAttributes redirectAttributes) {
        Boolean done = parkingService.darDeAltaVehiculoOficial(matricula);
        redirectAttributes.addFlashAttribute("done", done);
        return "redirect:/altasOficial";
    }

}
