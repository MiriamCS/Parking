package com.example.parking.DAO;


import java.util.List;
import com.example.parking.Model.Estancia;
import com.example.parking.RowMapper.EstanciaRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstanciaDAO {
    private final JdbcTemplate jdbcTemplate;

    public EstanciaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Estancia> getAllEstancias() {
        String sql = "SELECT * FROM estancia";
        return jdbcTemplate.query(sql, new EstanciaRowMapper());
    }

    public List<Estancia> getAllEstanciasActuales() {
        String sql = "SELECT * FROM estancia WHERE salida IS NULL";
        return jdbcTemplate.query(sql, new EstanciaRowMapper());
    }

    public int getEstanciasByVehiculoId(String vehiculoId) {
        String sql = "SELECT COUNT(*) FROM estancia WHERE matricula = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{vehiculoId}, Integer.class);
    }

    public Estancia getEstanciaActualByVehiculoId(String vehiculoId) {
        String sql = "SELECT * FROM estancia WHERE matricula = ? and salida IS NULL";
        List<Estancia> result = jdbcTemplate.query(sql, new Object[]{vehiculoId}, new EstanciaRowMapper());
        return result.isEmpty() ? null : result.get(0);
    }

    public void createEstancia(Estancia estancia) {
        String sql = "INSERT INTO estancia (matricula, entrada, salida) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{estancia.getVehiculoId(), estancia.getEntrada(), estancia.getSalida()});
    }

    public void updateEstancia(Estancia estancia) {
        String sql = "UPDATE estancia SET matricula = ?, entrada = ?, salida = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{estancia.getVehiculoId(), estancia.getEntrada(), estancia.getSalida(), estancia.getId()});
    }

    public void deleteEstancia(Long id) {
        String sql = "DELETE FROM estancia WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{id});
    }

    public void deleteAllEstancias() {
        String sql = "DELETE FROM estancia";
        jdbcTemplate.update(sql);
    }
}
