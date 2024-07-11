package com.example.parking.Model;

public class VehiculoOficial extends Vehiculo {
    public VehiculoOficial(String matricula) {
        super(matricula);
        tipo = TipoVehiculo.OFICIAL;
    }

}