package client.ot;


/**
 * Clase objeto transferencia para las columnas
 */
public class ColumnaOT implements java.io.Serializable 
{
    protected String nbTabla = "";
    protected String nbCol = "";
    protected String tipo = "";
    protected String longitud = "";
    protected String precision = "";
    protected String escala = "";
    protected String anulable = "";
    protected String defecto = "";
    


  public void setNbTabla(String nbTabla)
  {
    this.nbTabla = nbTabla;
  }


  public String getNbTabla()
  {
    return nbTabla;
  }


  public void setNbCol(String nbCol)
  {
    this.nbCol = nbCol;
  }


  public String getNbCol()
  {
    return nbCol;
  }


  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }


  public String getTipo()
  {
    return tipo;
  }


  public void setLongitud(String longitud)
  {
    this.longitud = longitud;
  }


  public String getLongitud()
  {
    return longitud;
  }


  public void setPrecision(String precision)
  {
    this.precision = precision;
  }


  public String getPrecision()
  {
    return precision;
  }


  public void setEscala(String escala)
  {
    this.escala = escala;
  }


  public String getEscala()
  {
    return escala;
  }


  public void setAnulable(String anulable)
  {
    this.anulable = anulable;
  }


  public String getAnulable()
  {
    return anulable;
  }


  public void setDefecto(String defecto)
  {
    this.defecto = defecto;
  }


  public String getDefecto()
  {
    return defecto;
  }
}
