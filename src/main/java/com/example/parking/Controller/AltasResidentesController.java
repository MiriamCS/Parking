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
public class AltasResidentesController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/altasResidentes")
    public String index(Model model) {
        model.addAttribute("vehiculosResidentes", parkingService.getVehiculosResidentes());
        return "altasResidentes";
    }

    @PostMapping("/altaResidente")
    public String darDeAltaVehiculoResidente(@RequestParam String matricula, RedirectAttributes redirectAttributes) {
        Boolean done = parkingService.darDeAltaVehiculoResidente(matricula);
        redirectAttributes.addFlashAttribute("done", done);
        return "redirect:/altasResidentes";
    }

}
