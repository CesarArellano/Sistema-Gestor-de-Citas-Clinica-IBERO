package models;

public class Medico{
    public String NombreMedico, CorreoMedico;
    
    public Medico(String NombreMedico){
        this.NombreMedico = NombreMedico;
    }

    public void SetNombreMedico(String NombreMedico){
        this.NombreMedico = NombreMedico;
    }

    public String GetNombreMedico(){
        return NombreMedico;
    }

}