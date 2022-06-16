package client.ot;


/**
 * Clase objeto transferencia para los trigger
 */
public class TriggerOT implements java.io.Serializable 
{
protected String nombreTabla 		= "";
protected String tipoAccion 		= "";
protected String tipoObjeto 			= "";
protected String columna     		= "";
protected String descripcion 		= "";
protected String referencia 		= "";
protected String estado 			 	= "";
protected String nombreTrigger 	= "";
protected String tipo   				= "";
protected String evento 				= "";
protected String when 					= "";


  public void setNombreTabla(String nombreTabla)
  {
    this.nombreTabla = nombreTabla;
  }


  public String getNombreTabla()
  {
    return nombreTabla;
  }


  public void setTipoAccion(String tipoAccion)
  {
    this.tipoAccion = tipoAccion;
  }


  public String getTipoAccion()
  {
    return tipoAccion;
  }



  public void setColumna(String columna)
  {
    this.columna = columna;
  }


  public String getColumna()
  {
    return columna;
  }


  public void setDescripcion(String descripcion)
  {
    this.descripcion = descripcion;
  }


  public String getDescripcion()
  {
    return descripcion;
  }


  public void setReferencia(String referencia)
  {
    this.referencia = referencia;
  }


  public String getReferencia()
  {
    return referencia;
  }


  public void setEstado(String estado)
  {
    this.estado = estado;
  }


  public String getEstado()
  {
    return estado;
  }


  public void setNombreTrigger(String nombreTrigger)
  {
    this.nombreTrigger = nombreTrigger;
  }


  public String getNombreTrigger()
  {
    return nombreTrigger;
  }


  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }


  public String getTipo()
  {
    return tipo;
  }


  public void setEvento(String evento)
  {
    this.evento = evento;
  }


  public String getEvento()
  {
    return evento;
  }


  public void setWhen(String when)
  {
    this.when = when;
  }


  public String getWhen()
  {
    return when;
  }


  public void setTipoObjeto(String tipoObjeto)
  {
    this.tipoObjeto = tipoObjeto;
  }


  public String getTipoObjeto()
  {
    return tipoObjeto;
  }
  }
