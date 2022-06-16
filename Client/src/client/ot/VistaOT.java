package client.ot;


/**
 * Clase objeto transferencia para las columnas
 */
public class VistaOT implements java.io.Serializable 
{
    protected String nombre = "";
    protected String texto = "";


  public void setNombre(String nombre)
  {
    this.nombre = nombre;
  }


  public String getNombre()
  {
    return nombre;
  }


  public void setTexto(String texto)
  {
    this.texto = texto;
  }


  public String getTexto()
  {
    return texto;
  }
}
