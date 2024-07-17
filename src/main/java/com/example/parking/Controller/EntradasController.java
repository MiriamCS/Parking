package com.example.parking.Controller;

import com.example.parking.Model.Estancia;
import com.example.parking.Service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EntradasController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/entradasParking")
    public String index(Model model) {
        model.addAttribute("vehiculosActuales", parkingService.getVehiculosActuales());
        return "entradasParking";
    }

    @PostMapping("/entrada")
    public String registrarEntrada(@RequestParam String matricula, RedirectAttributes redirectAttributes) {
        Estancia estancia = parkingService.registrarEntrada(matricula);
        if (estancia == null) {
            redirectAttributes.addFlashAttribute("error", "El vehículo ya tiene una entrada registrada");
        }
        return "redirect:/entradasParking";
    }
}
