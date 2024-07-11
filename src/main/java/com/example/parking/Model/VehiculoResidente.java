package com.example.parking.Model;

public class VehiculoResidente extends Vehiculo {
    public VehiculoResidente(String matricula) {
        super(matricula);
        tipo = TipoVehiculo.RESIDENTE;
        tiempoEstacionado = 0;
    }

    @Override
    public void setTiempoEstacionado(long tiempoEstacionado) {
        this.tiempoEstacionado += tiempoEstacionado;
    }

}
