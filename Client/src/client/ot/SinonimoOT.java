package client.ot;


/**
 * Clase objeto transferencia para las columnas
 */
public class SinonimoOT implements java.io.Serializable 
{
 protected String propTabla      = "";
protected String nombreTabla    = "";
protected String nombreSinonimo = "";


  public void setPropTabla(String propTabla)
  {
    this.propTabla = propTabla;
  }


  public String getPropTabla()
  {
    return propTabla;
  }


  public void setNombreTabla(String nombreTabla)
  {
    this.nombreTabla = nombreTabla;
  }


  public String getNombreTabla()
  {
    return nombreTabla;
  }


  public void setNombreSinonimo(String nombreSinonimo)
  {
    this.nombreSinonimo = nombreSinonimo;
  }


  public String getNombreSinonimo()
  {
    return nombreSinonimo;
  }
}
