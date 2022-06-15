package client;

public class ProcesoOT
{
  private static final String C_FUNCION_ID = "c_funcion_id";
  private static final String D_PROCESO = "d_proceso";
  private static final String D_NOMBRE = "d_nombre";
  protected String cFuncionId = "";
  protected String dProceso = "";
  protected String dNombre = "";
  protected boolean menu;
  
  public ProcesoOT()
  {
  }
  public ProcesoOT(ProcesoOT aux)
  {
    setCFuncionId(aux.getCFuncionId());
    setDNombre(aux.getDNombre());
    setDProceso(aux.getDProceso());
  }
  /** Recibe una línea y extrae el nombre y el código del proceso */
  public ProcesoOT(String linea)
  {
    setCFuncionId(Utiles.valorAtributo(linea, C_FUNCION_ID));

    setDProceso(Utiles.valorAtributo(linea, D_PROCESO));    
    
    setDNombre(Utiles.valorAtributo(linea, D_NOMBRE));
    
    
  }

  public void setDProceso(String dProceso)
  {
    this.dProceso = dProceso;
  }

  public String getDProceso()
  {
    return dProceso;
  }

  
  public void setCFuncionId(String cFuncionId)
  {
    this.cFuncionId = cFuncionId;
  }

  public String getCFuncionId()
  {
    return cFuncionId;
  }

  public void setDNombre(String dNombre)
  {
    this.dNombre = dNombre;
  }

  public String getDNombre()
  {
    return dNombre;
  }


  public void setMenu(boolean menu)
  {
    this.menu = menu;
  }

  public boolean isMenu()
  {
    return menu;
  }
}
