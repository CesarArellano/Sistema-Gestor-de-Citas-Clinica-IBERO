package clinica;

import controllers.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import views.*;

public class Clinica{
  public static int DiaActual, MesActual, AñoActual;
  public static void main (String[] args) throws ParseException, SQLException{
    views FramePrincipal = new views();
    FramePrincipal.setVisible(true);
  }
  public static void setDate(){
    Date Fecha = new Date();
    SimpleDateFormat ObjetoFormato = new SimpleDateFormat("dd");
    SimpleDateFormat ObjetoFormato2 = new SimpleDateFormat("MM");
    SimpleDateFormat ObjetoFormato3 = new SimpleDateFormat("yyyy");
    DiaActual = Integer.parseInt(ObjetoFormato.format(Fecha));
    MesActual = Integer.parseInt(ObjetoFormato2.format(Fecha));
    AñoActual = Integer.parseInt(ObjetoFormato3.format(Fecha));
    ClinicaEntradas.LabelDia.setText("" + DiaActual);
    ClinicaEntradas.LabelMes.setText("" + MesActual);
    ClinicaEntradas.LabelAño.setText("" + AñoActual);
  }
}