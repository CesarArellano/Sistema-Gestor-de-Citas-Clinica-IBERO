/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.toedter.calendar.JTextFieldDateEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import clinica.Clinica;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import models.Conexion;

/**
 *
 * @author raulgonzalezportillo
 */
public class ClinicaEntradas extends javax.swing.JPanel {
    public static JButton BotonAgendar;
    public static JTextField CuadroNombre, CuadroApellidoMaterno, CuadroApellidoPaterno, CuadroCorreo, CuadroNumero;
    public static JLabel LabelDia, LabelMes, LabelAño;
    public static int HorariosDisponibles;
    public String horas[] = {
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
    /**
     * Creates new form ClinicaEntradas
     */
    public ClinicaEntradas() throws ParseException, SQLException {
        initComponents();
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        JLabel LabelFondo, LabelBanner, LabelInterfaz;
        Icon IconoFondo, IconoBanner, IconoInterfaz, IconoBoton;
        CuadroNombre = new JTextField("", 1);
        CuadroNombre.setBounds(50, 151, 160, 25);
        add(CuadroNombre);
        CuadroApellidoMaterno = new JTextField("", 1);
        CuadroApellidoMaterno.setBounds(225, 151, 190, 25);
        add(CuadroApellidoMaterno);
        CuadroApellidoPaterno = new JTextField("", 1);
        CuadroApellidoPaterno.setBounds(430, 151, 200, 25);
        add(CuadroApellidoPaterno);
        CuadroCorreo = new JTextField("", 1);
        CuadroCorreo.setBounds(434, 345, 153, 26);
        add(CuadroCorreo);
        CuadroNumero = new JTextField("", 1);
        CuadroNumero.setBounds(433, 432, 153, 26);
        add(CuadroNumero);
        LabelDia = new JLabel("31");
        LabelDia.setBounds(685, 162, 50, 25);
        add(LabelDia);
        LabelMes = new JLabel("12");
        LabelMes.setBounds(744, 162, 50, 25);
        add(LabelMes);
        LabelAño = new JLabel("2019");
        LabelAño.setBounds(793, 162, 50, 25);
        add(LabelAño);
        BotonAgendar = new JButton("");
        IconoBoton = new ImageIcon(getClass().getResource("ClinicaBoton.png"));
        BotonAgendar.setIcon(IconoBoton);
        BotonAgendar.setBounds(665, 485, 175, 50);
        add(BotonAgendar);
        LabelBanner = new JLabel();
        IconoBanner = new ImageIcon(getClass().getResource("ClinicaBanner.png"));
        LabelBanner.setIcon(IconoBanner);
        LabelBanner.setBounds(0, 25, 900, 75);
        add(LabelBanner);
        LabelInterfaz = new JLabel();
        IconoInterfaz = new ImageIcon(getClass().getResource("ClinicaInterfaz.png"));
        LabelInterfaz.setIcon(IconoInterfaz);
        LabelInterfaz.setBounds(0, 0, 900, 600);
        add(LabelInterfaz);
        LabelFondo = new JLabel();
        IconoFondo = new ImageIcon(getClass().getResource("ClinicaFondo.png"));
        LabelFondo.setIcon(IconoFondo);
        LabelFondo.setBounds(0, -200, 900, 900);
        add(LabelFondo);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) CuadroFecha.getDateEditor();
        editor.setEditable(false);
        Clinica.setDate();
        ListaDoctores.setSelectedIndex(0);
        String day = Clinica.DiaActual + "";
        String month = Clinica.MesActual + "";
        String year = Clinica.AñoActual + "";
        String fe = year + "-" + month + "-" + day;
        Date das = f.parse(fe);
        CuadroFecha.setDate(das);
        ListaDoctores.addListSelectionListener(new javax.swing.event.ListSelectionListener (){

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                Conexion con = new Conexion();
                Date fecha = ClinicaEntradas.CuadroFecha.getDate();
                DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                String fecha2 = f.format(fecha);
                try {
                    CajaHorarios.removeAllItems();
                    int id = con.getDoctor(ListaDoctores.getSelectedValue());
                    int valores[] = con.validarHorarios(id, fecha2);
                    HorariosDisponibles = 0;
                    for(int i = 0; i < 14; i++){
                        if(valores[i] == 0){
                            CajaHorarios.addItem(horas[i]);
                            HorariosDisponibles++;
                        }
                    }
                    if(HorariosDisponibles == 0)
                        CajaHorarios.addItem("No hay horarios disponibles");
                } catch (SQLException ex) {
                    Logger.getLogger(ClinicaEntradas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            
        });
        
        CuadroFecha.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Conexion con = new Conexion();
                try {
                    Date fecha = ClinicaEntradas.CuadroFecha.getDate();
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    String fecha2 = f.format(fecha);
                    CajaHorarios.removeAllItems();
                    int id = con.getDoctor(ListaDoctores.getSelectedValue());
                    System.out.println("ID:" + id);
                    int vals[] = con.validarHorarios(id, fecha2);
                    HorariosDisponibles = 0;
                    for(int i = 0; i < 14; i++){
                        if(vals[i] == 0){
                            CajaHorarios.addItem(horas[i]);
                            HorariosDisponibles++;
                        }
                    }
                    if(HorariosDisponibles == 0)
                        CajaHorarios.addItem("No hay horarios disponibles");
                } catch (SQLException ex) {
                    Logger.getLogger(ClinicaEntradas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Conexion con = new Conexion();
        int id = con.getDoctor(ListaDoctores.getSelectedValue());
        int valores[] = con.validarHorarios(id, fe);
        HorariosDisponibles = 0;
        for(int i = 0; i < 14; i++){
           if(valores[i] == 0){
               CajaHorarios.addItem(horas[i]);
               HorariosDisponibles++;
           }
        }
        if(HorariosDisponibles == 0)
            CajaHorarios.addItem("No hay horarios disponibles");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CuadroFecha = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListaDoctores = new javax.swing.JList<String>();
        CajaHorarios = new javax.swing.JComboBox<String>();

        setPreferredSize(new java.awt.Dimension(900, 600));

        CuadroFecha.setDateFormatString("y-MM-dd");
        CuadroFecha.setFont(new java.awt.Font("Loma", 0, 13)); // NOI18N
        CuadroFecha.setMaxSelectableDate(new java.util.Date(1609480799000L));
        CuadroFecha.setMinSelectableDate(new java.util.Date(1546322401000L));
        CuadroFecha.setOpaque(false);
        CuadroFecha.setRequestFocusEnabled(false);

        ListaDoctores.setFont(new java.awt.Font("Loma", 0, 18)); // NOI18N
        ListaDoctores.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Bajo", "Eddy", "Allan Jair Escamilla Hernández" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(ListaDoctores);

        CajaHorarios.setFont(new java.awt.Font("Loma", 0, 13)); // NOI18N
        CajaHorarios.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "9:00-9:30", "9:30-10:00", "10:00-10:30", "10:30-11:00", "11:00-11:30", "11:30-12:00", "12:00-12:30", "12:30-13:00", "13:00-13:30", "13:30-14:00", "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00" }));
        CajaHorarios.setName(""); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(CuadroFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(CajaHorarios, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(257, 257, 257)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(CajaHorarios, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(CuadroFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(142, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> CajaHorarios;
    public static com.toedter.calendar.JDateChooser CuadroFecha;
    public static javax.swing.JList<String> ListaDoctores;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
