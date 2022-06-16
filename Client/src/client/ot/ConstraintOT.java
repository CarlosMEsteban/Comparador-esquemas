package client.ot;


/**
 * Clase objeto transferencia para las columnas
 */
public class ConstraintOT implements java.io.Serializable 
{
    protected String nbTabla = "";
    protected String nbCons = "";
    protected String tipo = "";
    protected String search = "";
    protected String fKOwner = "";
    protected String fKCons = "";
    protected String delete = "";
    protected String estado = "";
    protected String deferrable = "";
    protected String deferred = "";
    protected String validado = "";


  public void setNbTabla(String nbTabla)
  {
    this.nbTabla = nbTabla;
  }


  public String getNbTabla()
  {
    return nbTabla;
  }


  public void setNbCons(String nbCons)
  {
    this.nbCons = nbCons;
  }


  public String getNbCons()
  {
    return nbCons;
  }


  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }


  public String getTipo()
  {
    return tipo;
  }


  public void setSearch(String search)
  {
    this.search = search;
  }


  public String getSearch()
  {
    return search;
  }


  public void setFKOwner(String fKOwner)
  {
    this.fKOwner = fKOwner;
  }


  public String getFKOwner()
  {
    return fKOwner;
  }


  public void setFKCons(String fKCons)
  {
    this.fKCons = fKCons;
  }


  public String getFKCons()
  {
    return fKCons;
  }


  public void setDelete(String delete)
  {
    this.delete = delete;
  }


  public String getDelete()
  {
    return delete;
  }


  public void setEstado(String estado)
  {
    this.estado = estado;
  }


  public String getEstado()
  {
    return estado;
  }


  public void setDeferrable(String deferrable)
  {
    this.deferrable = deferrable;
  }


  public String getDeferrable()
  {
    return deferrable;
  }


  public void setDeferred(String deferred)
  {
    this.deferred = deferred;
  }


  public String getDeferred()
  {
    return deferred;
  }


  public void setValidado(String validado)
  {
    this.validado = validado;
  }


  public String getValidado()
  {
    return validado;
  }
    
    

}
