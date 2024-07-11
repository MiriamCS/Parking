package com.example.parking.Service;

import com.example.parking.Model.*;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@Service
public class ParkingService {

    private static final double PRECIO_RESIDENTE_MINUTO = 0.002;
    private static final double PRECIO_NO_RESIDENTE_MINUTO = 0.02;
    private static final double PRECIO_NULO = 0.0;
    private Map<String, Vehiculo> vehiculosOficiales = new HashMap<>();
    private Map<String, Vehiculo> vehiculosResidentes = new HashMap<>();
    private Map<String, Estancia> vehiculosEnParking = new HashMap<>();
    private List<Estancia> estanciasOficiales = new ArrayList<>();

    public List<Estancia> getEstanciasActuales(){
        return new ArrayList<>(vehiculosEnParking.values());
    }
    public List<Vehiculo> getVehiculosResidentes(){
        return new ArrayList<>(vehiculosResidentes.values());
    }

    public List<String> getMatriculasOficiales(){
        return new ArrayList<>(vehiculosOficiales.keySet());
    }

    public double getPrecioResidentes(){
        return PRECIO_RESIDENTE_MINUTO;
    }

    public void registrarEntrada(String matricula) {
        Vehiculo vehiculo;
        if(vehiculosOficiales.containsKey(matricula)){
            vehiculo = vehiculosOficiales.get(matricula);
        } else if (vehiculosResidentes.containsKey(matricula)) {
            vehiculo = vehiculosResidentes.get(matricula);
        }else{
            vehiculo = new VehiculoNoResidente(matricula);
        }
        Estancia estancia = new Estancia(vehiculo, System.currentTimeMillis());
        vehiculosEnParking.put(matricula, estancia);
    }


    public Double registrarSalida(String matricula) {
        Double precio = null;
        Estancia estanciaEncontrada = vehiculosEnParking.get(matricula);
        if (estanciaEncontrada!= null) {
            vehiculosEnParking.remove(matricula);
            estanciaEncontrada.setSalida(System.currentTimeMillis());
            long tiempoEstancia = estanciaEncontrada.getSalida() - estanciaEncontrada.getEntrada();
            Vehiculo vehiculo = estanciaEncontrada.getVehiculo();
            vehiculo.setTiempoEstacionado(tiempoEstancia);
            if (vehiculo.getTipo().equals(TipoVehiculo.NO_RESIDENTE)) {
                precio = tiempoEstancia * PRECIO_NO_RESIDENTE_MINUTO;
            } else if (vehiculo.getTipo().equals(TipoVehiculo.OFICIAL)) {
                estanciasOficiales.add(estanciaEncontrada);
                precio = PRECIO_NULO;
            } else {
                precio = PRECIO_NULO;
            }
        }
        return precio;
    }


    public void darDeAltaVehiculoOficial(String matricula){
        vehiculosOficiales.put(matricula, new VehiculoOficial(matricula));
    }

    public void darDeAltaVehiculoResidente(String matricula){
        vehiculosResidentes.put(matricula, new VehiculoResidente(matricula));
    }

    public void comienzaMes() {
        estanciasOficiales.clear();
        for (Vehiculo vehiculo : vehiculosResidentes.values()) {
            vehiculo.setTiempoEstacionado(0);
        }
    }

    public void generaInformePagosResidentes(String nombreFichero) {
        try (PrintWriter writer = new PrintWriter(nombreFichero)) {
            writer.println("Matrícula\tTiempo estacionado (min.)\tCantidad a pagar");
            for (Vehiculo vehiculo : vehiculosResidentes.values()) {
                double importe = (double) vehiculo.getTiempoEstacionado()* PRECIO_RESIDENTE_MINUTO;
                writer.println(vehiculo.getMatricula() + "\t\t\t\t\t" + vehiculo.getTiempoEstacionado()+ "\t\t\t\t\t\t" + importe);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
        }
        System.out.println("Informe generado correctamente.");
    }
}
