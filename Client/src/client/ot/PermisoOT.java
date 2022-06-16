package client.ot;


/**
 * Clase objeto transferencia para las columnas
 */
public class PermisoOT implements java.io.Serializable 
{
protected String grantor     = "";
protected String propietario = "";
protected String tabla       = "";
protected String grantable   = "";
protected String privilegio  = "";


  public void setGrantor(String grantor)
  {
    this.grantor = grantor;
  }


  public String getGrantor()
  {
    return grantor;
  }


  public void setPropietario(String propietario)
  {
    this.propietario = propietario;
  }


  public String getPropietario()
  {
    return propietario;
  }


  public void setTabla(String tabla)
  {
    this.tabla = tabla;
  }


  public String getTabla()
  {
    return tabla;
  }


  public void setGrantable(String grantable)
  {
    this.grantable = grantable;
  }


  public String getGrantable()
  {
    return grantable;
  }


  public void setPrivilegio(String privilegio)
  {
    this.privilegio = privilegio;
  }


  public String getPrivilegio()
  {
    return privilegio;
  }
}
