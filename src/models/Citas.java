/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

public class Citas{
    public String Fecha, Hora;

    public Citas(String Fecha, String Hora){
        this.Fecha = Fecha;
        this.Hora = Hora;
    }

    public void SetFecha(String Fecha){
        this.Fecha = Fecha;
    }

    public String GetFecha(){
        return Fecha;
    }

    public void SetHora(String Hora){
        this.Hora = Hora;
    }

    public String GetHora(){
        return Hora;
    }
}