package com.example.parking.Model;

public class VehiculoNoResidente extends Vehiculo {
    public VehiculoNoResidente() {
    }
    public VehiculoNoResidente(String matricula) {
        super(matricula);
        tipo = TipoVehiculo.NO_RESIDENTE;
    }

}
