package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Utiles 
{

  public static final String retCarro = "\n";

  public static final String PRIMARY_KEY = "P"; public static final String PRIMARY_KEY_LARGA = "PRIMARY KEY"; 
  public static final String FOREIGN_KEY = "R"; public static final String FOREIGN_KEY_LARGA = "FOREIGN KEY"; 
  public static final String CONSTRAINT_SISTEMA = "C";public static final String CONSTRAINT_SISTEMA_LARGA = "Constraint de sistema";
  public static final String UNIQUE = "U";
  
    public static BufferedReader abreParaLeer(String fich)  throws Exception
    {
        File archivo = new File (fich);
        FileReader fr = new FileReader (archivo);
        return new BufferedReader(fr);
    }
    
    public static String autoacabado(String etq)
    {
      return etq.substring(0, etq.length() - 1) + "/" + etq.substring(etq.length() - 1, etq.length());
    }
    
    public static boolean colIgualNombre(ColumnaOT col1, ColumnaOT col2)
    {
      if (col1.getNbTabla().equals(col2.getNbTabla()) && col1.getNbCol().equals(col2.getNbCol()))
        return true;
      else
        return false;
    }
    
    /** Devuelve TRUE si la estructura de dos columnas es distinta */
    public static boolean difCols(ColumnaOT col1, ColumnaOT col2)
    {
      if ((!col1.getAnulable().equals(col2.getAnulable()) 
      || (!col1.getDefecto().equals(col2.getDefecto())) 
      || (!col1.getEscala().equals(col2.getEscala())) 
      || (!col1.getLongitud().equals(col2.getLongitud())) 
      || (!col1.getPrecision().equals(col2.getPrecision())) 
      || (!col1.getTipo().equals(col2.getTipo()))))
        return true;
      else
        return false;
    }
    
    /** Devuelve TRUE si las dos constraints pasadas son la misma */
    public static boolean consIgual(ConstraintOT cons1, ConstraintOT cons2)
    {
      if (cons1.getNbTabla().equals(cons2.getNbTabla()) && cons1.getNbCons().equals(cons2.getNbCons()))
        if (cons1.getNbCons().equals("Constraint de sistema"))
          if (cons1.getSearch().equals(cons2.getSearch()))
            return true;
          else
            return false;
        else
          return true;
      else
        return false;
        
    }
    /** Devuelve TRUE si la estructura de dos constraints es distinta */
    public static boolean difCons(ConstraintOT cons1, ConstraintOT cons2)
    {
      if ((!cons1.getDeferrable().equals(cons2.getDeferrable())) 
      || (!cons1.getDeferred().equals(cons2.getDeferred()))
      || (!cons1.getDelete().equals(cons2.getDelete()))
      || (!cons1.getEstado().equals(cons2.getEstado()))
      || (!cons1.getFKCons().equals(cons2.getFKCons()))
      || (!cons1.getFKOwner().equals(cons2.getFKOwner()))
      || (!cons1.getSearch().equals(cons2.getSearch()))
      || (!cons1.getTipo().equals(cons2.getTipo()))
      || (!cons1.getValidado().equals(cons2.getValidado()))
      )
        return true;
      else
        return false;
    }

    /** Devuelve TRUE si la estructura de dos índices es distinta */
    public static boolean difInd(IndiceOT ind1, IndiceOT ind2)
    {
      if ((!ind1.getCompresion().equals(ind2.getCompresion()))
      || (!ind1.getPrefijo().equals(ind2.getPrefijo()))
      || (!ind1.getTipo().equals(ind2.getTipo()))
      || (!ind1.getTipoTabla().equals(ind2.getTipoTabla()))
      || (!ind1.getUnico().equals(ind2.getUnico()))
      )
        return true;
      else
        return false;
    }

    /** Devuelve TRUE si la estructura de dos sinónimos es distinta */
    public static boolean difSin(SinonimoOT sin1, SinonimoOT sin2)
    {
      if ((!sin1.getNombreTabla().equals(sin2.getNombreTabla()))
      || (!sin1.getPropTabla().equals(sin2.getPropTabla()))
      )
        return true;
      else
        return false;
    }
    /** Devuelve TRUE si la estructura de dos procedimientos es distinta */
    public static boolean difProc(ProcedimientoOT proc1, ProcedimientoOT proc2)
    {
      if ((!proc1.getEstado().equals(proc2.getEstado()))
      || (!proc1.getLineas().equals(proc2.getLineas()))
      || (!proc1.getTipo().equals(proc2.getTipo()))
      )
        if (proc1.getTipo().startsWith("PACKAGE") && proc2.getTipo().startsWith("PACKAGE"))
          return false;
        else
          return true;
      else
        return false;
    }
    /** Devuelve TRUE si los dos permisos pasados son los mismos */
    public static boolean perIgual(PermisoOT per1, PermisoOT per2)
    {
      if (per1.getPrivilegio().equals(per2.getPrivilegio()) 
      && per1.getPropietario().equals(per2.getPropietario()) 
      && per1.getTabla().equals(per2.getTabla())
      )
        return true;
      else
        return false;
    
    }
    /** Devuelve TRUE si los dos permisos cambian */
    public static boolean difPer(PermisoOT per1, PermisoOT per2)
    {
      if ((!per1.getGrantable().equals(per2.getGrantable()))
      || (!per1.getGrantor().equals(per2.getGrantor()))
      || (!per1.getTabla().equals(per2.getTabla()))
      )
        return true;
      else
        return false;
    
    }
    
    /** Devuelve el nombre del directorio de una ruta completa */
    public static String extraeDir(String nbFich)
    {
      System.out.println("Entramos en extraeDir");
      int posDir = nbFich.lastIndexOf('\\');
      String dir = nbFich.substring(0, posDir);
      System.out.println("Salimos de extraeDir");
      return dir;
      
    }
    
    /** Devuelve TRUE si los dos parámetros pasados son el mismo */
    public static boolean parIgual(ParametroOT par1, ParametroOT par2)
    {
      if (par1.getNombre().equals(par2.getNombre()))
        return true;
      else
        return false;
        
    }
    
    /** Devuelve TRUE si el valor de dos parámetros es distinto */
    public static boolean difPar(ParametroOT par1, ParametroOT par2)
    {
      if ((!par1.getValor().equals(par2.getValor())))
        return true;
      else
        return false;
    }

  /** devuelve TRUE si el string pasado es nulo o vacío */
  public static boolean stringConContenido(String a)
  {
    if (a != null)
      if (! a.equals(""))
        return true;
      else
        return false;
    else
      return false;
  }
  
  /** Devuelve el correspondiente tag de autoterminación de un tag pasado por parámetro */
  public static String fAutotermina(String tag) throws Exception
  {
//    System.out.println("entramos en fAutotermina");
    String resultado = "";
    try
    {
      resultado = tag.substring(0, tag.length() - 1) + "/>";
    }
    catch (Exception e)
    {
      System.out.println("Error en fAutotermina: " + e.getMessage());            
      throw e;            
    }
//    System.out.println("Salimos de fAutotermina");
    return resultado;
  }
    

    /** Devuelve la etiqueta de fin de la etiqueta pasada por parámetro */
    public static String fin(String etq)
    {
      return etq.substring(0, 1) + "/" + etq.substring(1);
    }
    
    /** Devuelve el texto que hay entre dos textos dentro de una línea */
    public static String extraeEntre(String inicio, String linea, String fin) throws Exception
    {
//System.out.println("Entramos en extraeEntre");
      
      String resultado = "";
      try
      {
      
        String autoacabado = Utiles.autoacabado(inicio);
        if (linea.indexOf(autoacabado) == -1)
        {
          int posInicio = linea.indexOf(inicio);
          int posFinal = linea.indexOf(fin, posInicio);
      
          resultado = linea.substring(posInicio + inicio.length(), posFinal);
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en extraeEntre" + e.getMessage());
        throw e;
      }
//      System.out.println("Salimos de extraeEntre");
      return resultado;        
      
    }


    /** Devuelve el valor de un atributo en una línea */
    public static String valorAtributo(String linea, String atributo)
    {
      int posAtr = linea.indexOf(atributo);
      int posComillas1 = linea.indexOf("\"", posAtr);
      int posComillas2 = linea.indexOf("\"", posComillas1 + 1);
      String resultado = linea.substring(posComillas1 + 1, posComillas2);
      return resultado;
    }        

  /** Cambia los &... por su correspondiente carácter */
  public static String cambiaSimbolos(String a)
  {
    return a.replaceAll("&lt;", "<");
  }

}
