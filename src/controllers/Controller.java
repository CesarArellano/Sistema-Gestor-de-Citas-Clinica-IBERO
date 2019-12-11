/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.io.File;
import java.text.DateFormat;
import models.Citas;
import models.Conexion;
import models.Medico;
import models.Paciente;
import views.*;

public class Controller implements ActionListener {
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
  public void actionPerformed(ActionEvent Evento) {
    if(Evento.getSource() == ClinicaEntradas.BotonAgendar) {
      try {
          String nombre, apellidoPaterno, apellidoMaterno, numero, correo, diaActual, mesActual, anioActual, horario, medico;
          String regexNames = "[a-zA-ZñÑáéíóúÁÉÍÓÚ. ]+";
          String regexMail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
          String regNum = "[0-9]+";
          String ErrorHorariosDisponibles = "No hay horarios disponibles";
          Date fecha = ClinicaEntradas.CuadroFecha.getDate();
          DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
          Date f1, f2;
          String fecha2 = f.format(fecha);
          nombre = ClinicaEntradas.CuadroNombre.getText();
          apellidoPaterno = ClinicaEntradas.CuadroApellidoMaterno.getText();
          apellidoMaterno = ClinicaEntradas.CuadroApellidoPaterno.getText();
          correo = ClinicaEntradas.CuadroCorreo.getText();
          numero = ClinicaEntradas.CuadroNumero.getText();
          diaActual = ClinicaEntradas.LabelDia.getText();
          mesActual = ClinicaEntradas.LabelMes.getText();
          anioActual = ClinicaEntradas.LabelAño.getText();
          horario = (String)ClinicaEntradas.CajaHorarios.getSelectedItem();
          medico = (String)ClinicaEntradas.ListaDoctores.getSelectedValue();
          System.out.println(nombre + apellidoPaterno + apellidoMaterno);
          System.out.println(correo + numero);
          System.out.println("La fecha actual es " + diaActual + "/" + mesActual + "/" + anioActual);
          System.out.println(fecha2);
          System.out.println("Horario: " + horario);
          System.out.println(medico);
          f1 = f.parse(fecha2); // Fecha seleccionada por el usuario
          f2 = f.parse(anioActual + "-" + mesActual + "-" + diaActual); // Fecha actual
          //JOptionPane.showMessageDialog(null, f2.before(f1));
          if(horario.equals(ErrorHorariosDisponibles)){
              JOptionPane.showMessageDialog(null, "Ya no hay horarios disponibles para esta fecha", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(horario == null){
              JOptionPane.showMessageDialog(null, "Ya no hay horarios disponibles para esta fecha", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(f2.after(f1) || f2.equals(f1)){
              JOptionPane.showMessageDialog(null, "La fecha para agendar la cita debe ser posterior a la fecha del dia de hoy):", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(!Pattern.matches(regexNames, nombre)){
              JOptionPane.showMessageDialog(null, "El campo nombre solo puede contener caracteres", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(!Pattern.matches(regexNames, apellidoPaterno)){
              JOptionPane.showMessageDialog(null, "El campo apellido paterno solo puede contener caracteres", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(!Pattern.matches(regexNames, apellidoMaterno)){
              JOptionPane.showMessageDialog(null, "El campo apellido materno solo puede contener caracteres", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(!Pattern.matches(regexMail, correo)){
              JOptionPane.showMessageDialog(null, "El campo correo tiene errores de formato", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if(numero.length() != 10 || !Pattern.matches(regNum, numero)){
              System.out.println(numero.length());
              JOptionPane.showMessageDialog(null, "El numero telefonico debe contener solo 10 digitos", "ERROR", JOptionPane.ERROR_MESSAGE);
              return;
          }
          Paciente pac = new Paciente(nombre, apellidoPaterno, apellidoMaterno, correo, numero);
          Medico med = new Medico(medico);
          Citas cit = new Citas(fecha2, horario);
          Conexion con = new Conexion();
          con.GuardarRegistros(med, pac, cit);
          String mensaje = "Hola "
                  + nombre + " este correo es para confirmar tu cita en la clinica del adulto mayor de la IBERO.\n\nLos datos de tu cita son los siguientes:\n\nLugar: Clinica del adulto mayor, anexo F. Universidad Iberoamericana\nFecha: " + fecha2 + "\nHorario de atención: " + horario + "\nMédico que atenderá: " + medico;
          Correo correoPaciente = new Correo(correo, "Confirmacion de la cita", mensaje);
          correoPaciente.EnviarCorreo();
          JOptionPane.showMessageDialog(null, "Se ha registrado con exito la cita", "Cita", JOptionPane.INFORMATION_MESSAGE);
          ClinicaEntradas.CajaHorarios.removeAllItems();
          Conexion cons = new Conexion();
          int lista[] = cons.validarHorarios(cons.getDoctor(medico), fecha2);
          ClinicaEntradas.HorariosDisponibles = 0;
          for(int i = 0; i < 14; i++){
              if(lista[i] == 0){
                  ClinicaEntradas.CajaHorarios.addItem(horas[i]);
                  ClinicaEntradas.HorariosDisponibles++;
              }
          }
          if(ClinicaEntradas.HorariosDisponibles == 0)
              ClinicaEntradas.CajaHorarios.addItem("No hay horarios disponibles");
      }catch(Exception E) {
        JOptionPane.showMessageDialog(null, "Ha ingresado mal alguno de sus datos", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
} 