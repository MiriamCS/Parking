package com.example.parking.Controller;

import com.example.parking.Model.TipoArchivo;
import com.example.parking.Service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class GenerarInformeController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/registrosResidentes")
    public String index(Model model) {
        model.addAttribute("residentes", parkingService.getVehiculosResidentes());
        model.addAttribute("precioMinutoResidentes", parkingService.getPrecioResidentes());
        return "registrosResidentes";
    }


    @PostMapping("/informePagosResidentes")
    public ResponseEntity<byte[]> generaInformePagosResidentes(@RequestParam String fichero, @RequestParam("format") String format) throws IOException {
        File file;
        if (format == null || format.isEmpty()) {
            return ResponseEntity.badRequest().body("No se ha seleccionado un formato".getBytes());
        }
        if (format.equals("txt")) {
            file = parkingService.generaInformePagosResidentes(fichero, TipoArchivo.txt);
        } else if (format.equals("csv")) {
            file = parkingService.generaInformePagosResidentes(fichero, TipoArchivo.csv);
        } else if (format.equals("pdf")) {
            file = parkingService.generaInformePagosResidentes(fichero, TipoArchivo.pdf);
        } else {
            return ResponseEntity.badRequest().body("Formato no válido".getBytes());
        }
        byte[] bytes = Files.readAllBytes(file.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fichero);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}