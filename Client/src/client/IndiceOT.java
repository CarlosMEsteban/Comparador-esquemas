package client;


/**
 * Clase objeto transferencia para las columnas
 */
public class IndiceOT implements java.io.Serializable 
{
 protected String nombreTabla = "";
 protected String nombreInd   = "";
 protected String compresion  = "";
 protected String tipo        = "";
 protected String prefijo     = "";
 protected String tipoTabla   = "";
 protected String unico       = "";


  public void setNombreTabla(String nombreTabla)
  {
    this.nombreTabla = nombreTabla;
  }


  public String getNombreTabla()
  {
    return nombreTabla;
  }


  public void setNombreInd(String nombreInd)
  {
    this.nombreInd = nombreInd;
  }


  public String getNombreInd()
  {
    return nombreInd;
  }


  public void setCompresion(String compresion)
  {
    this.compresion = compresion;
  }


  public String getCompresion()
  {
    return compresion;
  }


  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }


  public String getTipo()
  {
    return tipo;
  }


  public void setPrefijo(String prefijo)
  {
    this.prefijo = prefijo;
  }


  public String getPrefijo()
  {
    return prefijo;
  }


  public void setTipoTabla(String tipoTabla)
  {
    this.tipoTabla = tipoTabla;
  }


  public String getTipoTabla()
  {
    return tipoTabla;
  }


  public void setUnico(String unico)
  {
    this.unico = unico;
  }


  public String getUnico()
  {
    return unico;
  }
}
