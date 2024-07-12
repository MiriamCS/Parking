package com.example.parking.Model;

public abstract class Vehiculo {
    protected String matricula;
    protected TipoVehiculo tipo;
    protected long tiempoEstacionado;

    public Vehiculo() {
    }

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

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public void setTiempoEstacionado(long tiempoEstacionado) {
        this.tiempoEstacionado = tiempoEstacionado;
    }
}