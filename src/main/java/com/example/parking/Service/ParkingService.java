package com.example.parking.Service;

import com.example.parking.DAO.EstanciaDAO;
import com.example.parking.DAO.VehiculoDAO;
import com.example.parking.Model.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.*;
import java.util.*;

@Service
public class ParkingService {

    private static final double PRECIO_RESIDENTE_MINUTO = 0.002;
    private static final double PRECIO_NO_RESIDENTE_MINUTO = 0.02;
    private static final double PRECIO_NULO = 0.0;

    private final VehiculoDAO vehiculoDAO;
    private final EstanciaDAO estanciaDAO;

    @Autowired
    public ParkingService(VehiculoDAO vehiculoDAO, EstanciaDAO estanciaDAO) {
        this.vehiculoDAO = vehiculoDAO;
        this.estanciaDAO = estanciaDAO;
    }

    public List<Estancia> getEstanciasActuales(){
        return estanciaDAO.getAllEstanciasActuales();
    }
    public List<Vehiculo> getVehiculosResidentes(){
        return vehiculoDAO.getAllVehiculosByTipo(TipoVehiculo.RESIDENTE);
    }
    public List<Vehiculo> getVehiculosActuales(){
        return  vehiculoDAO.getVehiculosActuales();
    }

    public double getPrecioResidentes(){
        return PRECIO_RESIDENTE_MINUTO;
    }

    public Estancia registrarEntrada(String matricula) {
        Vehiculo vehiculo = vehiculoDAO.getVehiculoByMatricula(matricula);
        if (vehiculo == null) {
            vehiculo = new VehiculoNoResidente(matricula);
            vehiculoDAO.createVehiculo(vehiculo);
        }
        if (estanciaDAO.getEstanciaActualByVehiculoId(matricula) == null){
            Estancia estancia = new Estancia(matricula, System.currentTimeMillis());
            estanciaDAO.createEstancia(estancia);
            return estancia;
        }
        return null;
    }


    public Double registrarSalida(String matricula) {
        Double precio = null;
        Estancia estanciaEncontrada = estanciaDAO.getEstanciaActualByVehiculoId(matricula);
        if (estanciaEncontrada!= null) {
            estanciaEncontrada.setSalida(System.currentTimeMillis());
            estanciaDAO.updateEstancia(estanciaEncontrada);
            long tiempoEstancia = estanciaEncontrada.getSalida() - estanciaEncontrada.getEntrada();

            Vehiculo vehiculo = vehiculoDAO.getVehiculoByMatricula(matricula);
            vehiculo.setTiempoEstacionado(tiempoEstancia);
            vehiculoDAO.updateVehiculo(vehiculo);
            if (vehiculo.getTipo().equals(TipoVehiculo.NO_RESIDENTE)) {
                precio = tiempoEstancia * PRECIO_NO_RESIDENTE_MINUTO;
                estanciaDAO.deleteEstancia(estanciaEncontrada.getId());
            } else if (vehiculo.getTipo().equals(TipoVehiculo.OFICIAL)) {
                precio = PRECIO_NULO;
            } else if (vehiculo.getTipo().equals(TipoVehiculo.RESIDENTE)){
                estanciaDAO.deleteEstancia(estanciaEncontrada.getId());
                precio = PRECIO_NULO;
            }
        }
        return precio;
    }


    public boolean darDeAltaVehiculoOficial(String matricula){
        if(vehiculoDAO.getMatriculasByTipo(TipoVehiculo.OFICIAL).contains(matricula) || vehiculoDAO.getMatriculasByTipo(TipoVehiculo.RESIDENTE).contains(matricula)){
            return false;
        }
        Vehiculo vehiculo = new VehiculoOficial(matricula);
        vehiculoDAO.createVehiculo(vehiculo);
        return true;
    }

    public boolean darDeAltaVehiculoResidente(String matricula){
        if(vehiculoDAO.getMatriculasByTipo(TipoVehiculo.RESIDENTE).contains(matricula) || vehiculoDAO.getMatriculasByTipo(TipoVehiculo.OFICIAL).contains(matricula)){
            return false;
        }
        Vehiculo vehiculo = new VehiculoResidente(matricula);
        vehiculoDAO.createVehiculo(vehiculo);
        return true;
    }

    public void comienzaMes() {
        estanciaDAO.deleteAllEstancias();
        for (Vehiculo vehiculo : vehiculoDAO.getAllVehiculosByTipo(TipoVehiculo.RESIDENTE)) {
            vehiculo.resetTiempoEstacionado();
            vehiculoDAO.updateVehiculo(vehiculo);
        }
    }

    public Map<String, Integer> getNumEstanciasVehiculosOficial(){
        Map<String, Integer> mapa = new HashMap<>();
        for (String mOficial : vehiculoDAO.getMatriculasByTipo(TipoVehiculo.OFICIAL)){
            mapa.put(mOficial, estanciaDAO.getEstanciasByVehiculoId(mOficial));
        }
        return mapa;
    }

    public File generaInformePagosResidentes(String nombreFichero, TipoArchivo tipoArchivo) {
        File file = new File(nombreFichero+ "." + tipoArchivo.name());
        if(tipoArchivo.equals(TipoArchivo.txt)){
            file = generaInformeTxt(file);
        } else if (tipoArchivo.equals(TipoArchivo.csv)) {
            file = generaInformeCsv(file);
        } else if (tipoArchivo.equals(TipoArchivo.pdf)) {
            file = generaInformePdf(file);
        }else{
            System.out.println("Invalid file type. Please try again.");
            return null;
        }
        return file;
    }

    public File generaInformeTxt(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Matrícula\tTiempo estacionado (min.)\tCantidad a pagar");
            for (Vehiculo vehiculo : vehiculoDAO.getAllVehiculosByTipo(TipoVehiculo.RESIDENTE)) {
                double importe = (double) vehiculo.getTiempoEstacionado()* PRECIO_RESIDENTE_MINUTO;
                writer.println(vehiculo.getMatricula() + "\t\t\t" + vehiculo.getTiempoEstacionado()+ "\t\t\t\t" + importe);
            }
        }catch (FileNotFoundException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
            return null;
        }
        System.out.println("Informe generado correctamente.");
        return file;
    }
    public File generaInformeCsv(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Matrícula;Tiempo estacionado (min.);Cantidad a pagar");
            for (Vehiculo vehiculo : vehiculoDAO.getAllVehiculosByTipo(TipoVehiculo.RESIDENTE)) {
                double importe = (double) vehiculo.getTiempoEstacionado() * PRECIO_RESIDENTE_MINUTO;
                writer.println(vehiculo.getMatricula() + ";" + vehiculo.getTiempoEstacionado() + ";" + importe);
            }
        }catch (FileNotFoundException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
            return null;
        }
        System.out.println("Informe generado correctamente.");
        return file;
    }
    public File generaInformePdf(File file) {
        try (PdfWriter writer = new PdfWriter(file)) {
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            Paragraph header = new Paragraph("Informe de vehículos residentes");
            header.setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(header);

            float[] columnWidths = {5, 5, 5};
            Table table = new Table(columnWidths);
            table.setWidth(500);

            table.addCell("Matrícula");
            table.addCell("Tiempo estacionado (min.)");
            table.addCell("Cantidad a pagar");

            for (Vehiculo vehiculo : vehiculoDAO.getAllVehiculosByTipo(TipoVehiculo.RESIDENTE)) {
                double importe = (double) vehiculo.getTiempoEstacionado() * PRECIO_RESIDENTE_MINUTO;
                table.addCell(vehiculo.getMatricula());
                table.addCell(String.valueOf(vehiculo.getTiempoEstacionado()));
                table.addCell(String.valueOf(importe));
            }

            doc.add(table);

            doc.close();
        } catch (Exception e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
            return null;
        }
        System.out.println("Informe generado correctamente.");
        return file;
    }
}
