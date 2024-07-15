package com.example.parking.DAO;

import java.util.ArrayList;
import java.util.List;

import com.example.parking.Model.TipoVehiculo;
import com.example.parking.Model.Vehiculo;
import com.example.parking.RowMapper.EstanciaRowMapper;
import com.example.parking.RowMapper.VehiculoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VehiculoDAO {
    private final JdbcTemplate jdbcTemplate;

    public VehiculoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Vehiculo> getAllVehiculosByTipo(TipoVehiculo tipo) {
        String sql = "SELECT * FROM vehiculo WHERE tipo = ?";
        return jdbcTemplate.query(sql, new VehiculoRowMapper(), tipo.name());
    }

    public List<String> getMatriculasByTipo(TipoVehiculo tipo) {
        String sql = "SELECT matricula FROM vehiculo WHERE tipo = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("matricula"), tipo.name());
    }

    public Vehiculo getVehiculoByMatricula(String matricula) {
        String sql = "SELECT * FROM vehiculo WHERE matricula = ?";
        List<Vehiculo> result = jdbcTemplate.query(sql, new Object[]{matricula}, new VehiculoRowMapper());
        return result.isEmpty() ? null : result.get(0);
    }

    public void createVehiculo(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculo (matricula, tipo, tiempo_estacionado) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{vehiculo.getMatricula(), vehiculo.getTipo().name(), vehiculo.getTiempoEstacionado()});
    }

    public void updateVehiculo(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculo SET tipo = ?, tiempo_estacionado = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{vehiculo.getTipo().name(), vehiculo.getTiempoEstacionado(), vehiculo.getMatricula()});
    }

    public List<Vehiculo> getVehiculosActuales(){
        String sql = "SELECT v.* FROM estancia e LEFT JOIN vehiculo v ON e.matricula = v.matricula WHERE salida IS NULL;";
        return jdbcTemplate.query(sql, new VehiculoRowMapper());
    }
}