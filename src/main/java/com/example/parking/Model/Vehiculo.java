package com.example.parking.Model;

public abstract class Vehiculo {
    protected String matricula;
    protected TipoVehiculo tipo;
    protected long tiempoEstacionado;

    public Vehiculo(String matricula) {
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public long getTiempoEstacionado() {
        return tiempoEstacionado;
    }

    public void setTiempoEstacionado(long tiempoEstacionado) {
        this.tiempoEstacionado = tiempoEstacionado;
    }
}