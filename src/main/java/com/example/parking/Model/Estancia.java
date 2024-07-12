package com.example.parking.Model;

public class Estancia {
    private Long id;
    private String vehiculo_id;
    private Long entrada;
    private Long salida;

    public Estancia() {
    }
    public Estancia(String vehiculo_id, Long entrada) {
        this.vehiculo_id = vehiculo_id;
        this.entrada = convertToMinutes(entrada);
        this.salida = null;
    }

    public Long getId() {
        return id;
    }
    public String getVehiculoId() {
        return vehiculo_id;
    }

    public Long getEntrada() {
        return entrada;
    }

    public Long getSalida() {
        return salida;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setVehiculoId(String vehiculo_id) {
        this.vehiculo_id = vehiculo_id;
    }

    public void setEntrada(Long entrada) {
        this.entrada = entrada;
    }

    public void setSalida(Long salida) {
        this.salida= convertToMinutes(salida);
    }

    private long convertToMinutes(Long milliseconds) {
        return milliseconds / 60000;
    }
}
