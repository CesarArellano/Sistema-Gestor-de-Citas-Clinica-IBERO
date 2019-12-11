/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import controllers.*;
import java.sql.SQLException;
import java.text.ParseException;

public class views extends JFrame {
  public views() throws ParseException, SQLException {
    super("Administrador de citas - Clinica del Adulto Mayor");
    setSize(900, 600);
    setLocation (150, 100);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setIconImage(new ImageIcon(getClass().getResource("ClinicaLogoBorde.png")).getImage());
    ClinicaEntradas Entradas = new ClinicaEntradas();
    this.add(Entradas);
    Controller Manejador = new Controller();
    ClinicaEntradas.BotonAgendar.addActionListener(Manejador);
  }
}