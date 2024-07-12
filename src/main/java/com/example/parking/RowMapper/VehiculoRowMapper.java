package com.example.parking.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.parking.Model.*;
import org.springframework.jdbc.core.RowMapper;

public class VehiculoRowMapper implements RowMapper<Vehiculo> {
    @Override
    public Vehiculo mapRow(ResultSet rs, int rowNum) throws SQLException {
        TipoVehiculo tipo = TipoVehiculo.valueOf(rs.getString("tipo"));
        Vehiculo vehiculo;
        if(tipo.equals(TipoVehiculo.OFICIAL)){
            vehiculo = new VehiculoOficial();
        }else if(tipo.equals(TipoVehiculo.RESIDENTE)){
            vehiculo = new VehiculoResidente();
        }else{
            vehiculo = new VehiculoNoResidente();
        }
        vehiculo.setMatricula(rs.getString("matricula"));
        vehiculo.setTipo(TipoVehiculo.valueOf(rs.getString("tipo")));
        vehiculo.setTiempoEstacionado(rs.getLong("tiempo_estacionado"));
        return vehiculo;
    }
}