package com.example.parking.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.parking.Model.*;
import org.springframework.jdbc.core.RowMapper;

public class EstanciaRowMapper implements RowMapper<Estancia> {
    @Override
    public Estancia mapRow(ResultSet rs, int rowNum) throws SQLException {
        Estancia estancia = new Estancia();
        estancia.setId(rs.getLong("id"));
        estancia.setVehiculoId(rs.getString("matricula"));
        estancia.setEntrada(rs.getLong("entrada"));
        estancia.setSalida(rs.getLong("salida"));
        return estancia;
    }
}