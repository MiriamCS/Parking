package com.example.parking.Model;

public class Estancia {
    private Vehiculo vehiculo;
    private Long entrada;
    private Long salida;

    public Estancia(Vehiculo vehiculo, Long entrada) {
        this.vehiculo = vehiculo;
        this.entrada = convertToMinutes(entrada);
        this.salida = null;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Long getEntrada() {
        return entrada;
    }

    public Long getSalida() {
        return salida;
    }

    public void setSalida(Long salida) {
        this.salida= convertToMinutes(salida);
    }

    private long convertToMinutes(Long milliseconds) {
        return milliseconds / 60000;
    }
}
