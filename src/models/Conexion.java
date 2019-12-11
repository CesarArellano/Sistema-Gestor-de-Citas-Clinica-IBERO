package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.mysql.jdbc.Statement;
import controllers.Correo;
import java.sql.PreparedStatement;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Conexion{

    static int UltimoID;
    //clase que me permite realizar la conexión y las consultas SQL;
    static Connection conn;
    //Java Database Connectivity (JDBC) Permite a un programa en java, conectarse a un sistema manejador de base de datos y generar consultas SQL.
    static final String driver = "com.mysql.jdbc.Driver";
    //Parámetros para entrar al servidor;
    static final String user = "root";
    static final String password = "admin";
    //nombre de la base de datos a conectar;
    static final String url = "jdbc:mysql://localhost/Clinica?useSSL=false";

    public Conexion(){
        conn = null;
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,password);
            if(conn != null)
                System.out.println("Se conectó a la base de datos: Clinica");
        }
        //Error en el driver;
        catch(ClassNotFoundException e){
            System.out.println("Error al cargar el controlador.");
            e.printStackTrace();
        }
        //Error de conexión;
        catch(SQLException e){
            System.out.println("No se conectó");
            System.out.println(e);
        }
    }
    public Connection getConnection(){
        return conn;
    }
    //Terminar la conexión a BD;
    public void DesConnection(){
        conn = null;
        if(conn == null){
            System.out.println("Terminar conexión");
        }
    }
    public void GuardarRegistros(Medico DatosMedico, Paciente DatosPaciente, Citas DatosCitas) throws SQLException{
        try{
            int idMed = 0;
            String mail = "";
            String mensaje2 = "¡Hola " + DatosMedico.NombreMedico + "!\nTienes una cita con " + DatosPaciente.NombrePaciente + " " + DatosPaciente.APaternoPaciente + " " + DatosPaciente.AMaternoPaciente + ". Los datos de la cita son los siguientes: \nFecha: " + DatosCitas.Fecha + "\nHorario: " + DatosCitas.Hora + "\n ¡No lo olvides!";
          
            PreparedStatement InsertarDatosPaciente = conn.prepareStatement("INSERT INTO Paciente VALUES(null,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement InsertarDatosCitas = conn.prepareStatement("INSERT INTO Citas VALUES(null,?,?,?,?)");
            Statement stmt = (Statement) conn.createStatement();
            ResultSet rs,LastID;
            rs = stmt.executeQuery("SELECT * FROM Medico WHERE Nombre = '" + DatosMedico.NombreMedico + "'");
            if(rs.next()){
                idMed = Integer.parseInt(rs.getString("IDMedico"));
                mail = rs.getString("Correo");
            }
            // Inserto parámetros de paciente al comando INSERT INTO;
            InsertarDatosPaciente.setString(1, DatosPaciente.GetNombrePaciente());
            InsertarDatosPaciente.setString(2, DatosPaciente.GetAPaternoPaciente());
            InsertarDatosPaciente.setString(3, DatosPaciente.GetAMaternoPaciente());
            InsertarDatosPaciente.setString(4, DatosPaciente.GetCorreoPaciente());
            InsertarDatosPaciente.setString(5, DatosPaciente.GetTContacto());
            // Agrego registro a la tabla Paciente en la base de datos;
            InsertarDatosPaciente.executeUpdate();
            
            // Obtener último ID del Medico para pasarselo como referencia al nuevo paciente.
            LastID = InsertarDatosPaciente.getGeneratedKeys();
            if (LastID != null && LastID.next()) 
                UltimoID = LastID.getInt(1);
            
            // Inserto parámetros de citas al comando INSERT INTO;
            InsertarDatosCitas.setInt(1, idMed);
            InsertarDatosCitas.setInt(2, UltimoID);
            InsertarDatosCitas.setString(3, DatosCitas.GetFecha());
            InsertarDatosCitas.setString(4, DatosCitas.GetHora()); 
            // Agrego registro a la tabla Citas en la base de datos;
            InsertarDatosCitas.executeUpdate();
            Correo cor = new Correo(mail, "Cita nueva", mensaje2);
            cor.EnviarCorreo();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public int[] validarHorarios(int IDMedico, String Fecha) throws SQLException{
        String horas[] = {
            "9:00-9:30",
            "9:30-10:00",
            "10:00-10:30",
            "10:30-11:00",
            "11:00-11:30",
            "11:30-12:00",
            "12:00-12:30",
            "12:30-13:00",
            "13:00-13:30",
            "13:30-14:00",
            "14:00-14:30",
            "14:30-15:00",
            "15:00-15:30",
            "15:30-16:00"
        };
        String horas2;
        int horarios[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Statement stmt = (Statement) conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Citas WHERE IDMedico = " + IDMedico + " AND Fecha = '" + Fecha + "'");
        while(rs.next()){
            horas2 = rs.getString("Hora");
            System.out.println(horas2);
            for(int i = 0; i < horas.length; i++){
                if(horas2.equals(horas[i]))
                    horarios[i] = 1;
            }
        }
        // 9:00-9:30, 9:30-10:00, 10:00-10:30, 10:30-11:00, 11:00-11:30, 11:30-12:00, 12:00-12:30, 12:30-13:00, 13:00-13:30, 13:30-14:00, 14:00-14:30, 14:30-15:00, 15:00-15:30, 15:30-16:00
        return horarios;
    }
    public int getDoctor(String Medico) throws SQLException{
        int id = 0;
        Statement stmt = (Statement) conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Medico WHERE Nombre = '" + Medico + "'");
        if(rs.next())
            id = Integer.parseInt(rs.getString("IDMedico"));
        return id;
    }
}