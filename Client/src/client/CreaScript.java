package client;

import client.ot.ColumnaOT;

import client.ot.ConstraintOT;

import client.ot.SinonimoOT;

import client.ot.VistaOT;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.ArrayList;


public class CreaScript 
{

public static String directorio;
public static String directorioScripts;

private static PrintWriter pw;

public static void setDirectorio(String fichResultado)
{
  directorio = Utiles.extraeDir(fichResultado);
  directorioScripts = directorio + File.separator + "Scripts";
  new File(directorioScripts).mkdir();
}
/** Pone el comando para eliminar las columnas que sobran */
public static void sobranColumnas(ArrayList lista) throws Exception
{
  FileWriter fw = null;
  String tablaActual = "";
  String cadena = "";
  System.out.println("Entramos en sobranColumnas");
  try
  {
    for (int i = 0; i < lista.size(); i++)
    {
      ColumnaOT colOT = (ColumnaOT) lista.get(i);
      if (! colOT.getNbTabla().equals(tablaActual))
      {
        if (! tablaActual.equals(""))
        // No es la primera vuelta. Acabamos el fichero anterior
        {
          cadena = cadena.substring(0, cadena.length() - 2); // Quitamos la última coma
          cadena += ");";
          pw.println(cadena); // Escribimos lo que había
          pw.close();
          fw.close();
        }
        // Abrimos el nuevo fichero
        String nbFich = directorioScripts + File.separator + colOT.getNbTabla() + ".sql";
        File f = new File(nbFich);
        if (f.exists())
          fw = new FileWriter(nbFich, true);    
        else
          fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
          
        cadena = "ALTER TABLE " + colOT.getNbTabla() + " DROP (";
      
        // Apuntamos la tabla actual
        tablaActual = colOT.getNbTabla();
      
      }
    
      cadena += colOT.getNbCol() + ", ";

    
    }
    if (!tablaActual.equals(""))
    // Ponemos el final de la última sentencia
    {
      cadena = cadena.substring(0, cadena.length() - 1); // Quitamos la última coma
      cadena += ");";
      pw.println(cadena); // Escribimos lo que había
      pw.close();
      fw.close();
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de sobranColumnas");
}



  
/** Pone el comando para crear las columnas que faltan */
public static void faltanColumnas(ArrayList lista) throws Exception
{
  FileWriter fw = null;
  String tablaActual = "";
  String cadena = "";
  System.out.println("Entramos en faltanColumnas");
  try
  {
    for (int i = 0; i < lista.size(); i++)
    {
      ColumnaOT colOT = (ColumnaOT) lista.get(i);
      if (! colOT.getNbTabla().equals(tablaActual))
      {
        if (! tablaActual.equals(""))
        // No es la primera vuelta. Acabamos el fichero anterior
        {
          cadena = cadena.substring(0, cadena.length() - 2); // Quitamos la última coma
          cadena += ");";
          pw.println(cadena); // Escribimos lo que había
          pw.close();
          fw.close();
        }
        // Abrimos el nuevo fichero
        String nbFich = directorioScripts + File.separator + colOT.getNbTabla() + ".sql";
        File f = new File(nbFich);
        if (f.exists())
          fw = new FileWriter(nbFich, true);    
        else
          fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
          
        cadena = "ALTER TABLE " + colOT.getNbTabla() + " ADD (";
      
        // Apuntamos la tabla actual
        tablaActual = colOT.getNbTabla();
      
      }
      
      cadena += anadirColumna(colOT.getNbCol(), colOT.getTipo(), colOT.getLongitud(), colOT.getPrecision(), colOT.getEscala(), colOT.getAnulable(), colOT.getDefecto()) + ", ";  
    
    }
    if (!tablaActual.equals(""))
    // Ponemos el final de la última sentencia
    {
      cadena = cadena.substring(0, cadena.length() - 2); // Quitamos la última coma
      cadena += ");";
      pw.println(cadena); // Escribimos lo que había
      pw.close();
      fw.close();
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de faltanColumnas");
}

public static void cambianColumnas(ArrayList cambian, ArrayList des, ArrayList otro) throws Exception
{
  System.out.println("Entramos en cambianColumnas");
  FileWriter fw = null;
  String tablaActual = "";
  ColumnaOT colCambiaOT = null;
  ColumnaOT colDesOT = null;
  ColumnaOT colOtroOT = null;
  String cadena = "";
  try
  {
    for (int i= 0; i < cambian.size(); i++)
    // Recorremos las columnas que cambian    
    {
      colCambiaOT = (ColumnaOT) cambian.get(i);
      if (! colCambiaOT.getNbTabla().equals(tablaActual))
      {
        if (! tablaActual.equals(""))
        // No es la primera vuelta. Acabamos el fichero anterior
        {
          cadena = cadena.substring(0, cadena.length() - 2); // Quitamos la última coma
          cadena += ");";
          pw.println(cadena); // Escribimos lo que había
          pw.close();
          fw.close();
        }
        // Abrimos el nuevo fichero
        String nbFich = directorioScripts + File.separator + colCambiaOT.getNbTabla() + ".sql";
        File f = new File(nbFich);
        if (f.exists())
          fw = new FileWriter(nbFich, true);    
        else
          fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
          
        cadena = "ALTER TABLE " + colCambiaOT.getNbTabla() + " MODIFY (";
      
        // Apuntamos la tabla actual
        tablaActual = colCambiaOT.getNbTabla();
      
      }
      
      cadena += colCambiaOT.getNbCol() + " ";
      
      boolean enc = false;
      int j = 0;
      // Buscamos la columna en la lista des
      while (! enc)
      {
        colDesOT = (ColumnaOT) des.get(j);
        if (colDesOT.getNbTabla().equals(colCambiaOT.getNbTabla()) && colDesOT.getNbCol().equals(colCambiaOT.getNbCol()))
          enc = true;
        else
          j++;
      }
      enc = false;
      j = 0;
      // Buscamos la columna en la lista otro
      while (! enc)
      {
        colOtroOT = (ColumnaOT) otro.get(j);
        if (colOtroOT.getNbTabla().equals(colCambiaOT.getNbTabla()) && colOtroOT.getNbCol().equals(colCambiaOT.getNbCol()))
          enc = true;
        else
          j++;
      }
      
      if (!colDesOT.getTipo().equals(colOtroOT.getTipo()))
      // Ha cambiado el tipo de la columna
      {
        if (colDesOT.getTipo().equals("BLOB"))
          cadena += colDesOT.getTipo();
        else if (colDesOT.getTipo().equals("CHAR"))
          cadena += colDesOT.getTipo() + "(" + colDesOT.getLongitud() + ")";
        else if (colDesOT.getTipo().equals("DATE"))
          cadena += colDesOT.getTipo();
        else if (colDesOT.getTipo().equals("NUMBER"))
          cadena += colDesOT.getTipo() + "(" + colDesOT.getPrecision() + ", " + colDesOT.getEscala() + ")";
       else if (colDesOT.getTipo().equals("VARCHAR2"))
        cadena += colDesOT.getTipo() + "(" + colDesOT.getLongitud() + ")";
      }
      else if (!colDesOT.getLongitud().equals(colOtroOT.getLongitud()))
      // Ha cambiado la longitud del campo, pero no el tipo
      {
          cadena += colDesOT.getTipo() + "(" + colDesOT.getLongitud() + ")";
          //cadena += colDesOT.getTipo() + "(" + colDesOT.getPrecision() + ", " + colDesOT.getEscala() + ")";
      }
      else if (!colDesOT.getPrecision().equals(colOtroOT.getPrecision()) || !colDesOT.getEscala().equals(colOtroOT.getEscala()))
      // Ha cambiado la precisión o la escala de la columna
      {
        cadena += colDesOT.getTipo() + "(" + colDesOT.getPrecision() + ", " + colDesOT.getEscala() + ")";
      }
      
      
      if (!colDesOT.getAnulable().equals(colOtroOT.getAnulable()))
      // Ha cambiado la obligatoriedad de la columna
      {
        if (colDesOT.getAnulable().equals("Y"))
          cadena += " NULL";          
        else
          cadena += " NOT NULL";
          
      }
      if (!colDesOT.getDefecto().equals(colOtroOT.getDefecto()))
      {
        if (colDesOT.getDefecto().equals(""))
          // Tiene que dejar de tener valor por defecto
          cadena += " DEFAULT NULL";
        else 
          // Ahora va a tener un valor por defecto que está en desarrollo
          cadena += " DEFAULT " + colDesOT.getDefecto();          
      }
      
      
      cadena += ", ";  
    
    }
    if (!tablaActual.equals(""))
    // Ponemos el final de la última sentencia
    {
      cadena = cadena.substring(0, cadena.length() - 2); // Quitamos la última coma
      cadena += ");";
      pw.println(cadena); // Escribimos lo que había
      pw.close();
      fw.close();
    }
      
    
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de cambianColumnas");
  
}

/** Pone el comando para eliminar las constraints que sobran */
public static void sobranConstraints(ArrayList lista) throws Exception
{
  FileWriter fw = null;
  String tablaActual = "";
  String cadena = "";
  System.out.println("Entramos en sobranConstraints");
  try
  {
    for (int i = 0; i < lista.size(); i++)
    {
      ConstraintOT consOT = (ConstraintOT) lista.get(i);
      if (! consOT.getNbCons().equals(Utiles.CONSTRAINT_SISTEMA_LARGA) && !consOT.getNbCons().contains("$"))
      {
      
        if (! consOT.getNbTabla().equals(tablaActual))
        {
          if (! tablaActual.equals(""))
          {
            pw.println(cadena); // Escribimos lo que había
            pw.close();
            fw.close();
            cadena = "";
          }
        
          // Abrimos el nuevo fichero
          String nbFich = directorioScripts + File.separator + consOT.getNbTabla() + ".sql";
          File f = new File(nbFich);
          if (f.exists())
            fw = new FileWriter(nbFich, true);    
          else
            fw = new FileWriter(nbFich);
          pw = new PrintWriter(fw);
          
          // Apuntamos la tabla actual
          tablaActual = consOT.getNbTabla();
      
        }
        
        cadena += "ALTER TABLE " + consOT.getNbTabla() + " DROP CONSTRAINT " + consOT.getNbCons() + ";" + Utiles.retCarro;
    
        

      }
    }
    if (!tablaActual.equals(""))
    // Ponemos el final de la última sentencia
    {
      pw.println(cadena); // Escribimos lo que había
      pw.close();
      fw.close();
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de sobranConstraints");
}


/** Pone el comando para crear las constraints que faltan */
public static void faltanConstraints(ArrayList lista) throws Exception
{
  FileWriter fw = null;
  String tablaActual = "";
  String cadena = "";
  System.out.println("Entramos en faltanConstraints");
  try
  {
    for (int i = 0; i < lista.size(); i++)
    {
      ConstraintOT consOT = (ConstraintOT) lista.get(i);
      
      if (!consOT.getNbCons().contains("$"))
      {
        if (! consOT.getNbTabla().equals(tablaActual))
        {
          if (! tablaActual.equals(""))
          // No es la primera vuelta. Acabamos el fichero anterior
          {
            pw.println(cadena); // Escribimos lo que había
            pw.close();
            fw.close();
            cadena = "";
          }
          // Abrimos el nuevo fichero
          String nbFich = directorioScripts + File.separator + consOT.getNbTabla() + ".sql";
          File f = new File(nbFich);
          if (f.exists())
            fw = new FileWriter(nbFich, true);    
          else
            fw = new FileWriter(nbFich);
          pw = new PrintWriter(fw);
            
          cadena += "ALTER TABLE " + consOT.getNbTabla() + " ADD (CONSTRAINT " + consOT.getNbCons() + " ";
          if (consOT.getTipo().equals(Utiles.CONSTRAINT_SISTEMA))
          // CONSTRAINT DE SISTEMA
            if (consOT.getSearch().endsWith("IS NOT NULL"))
              cadena += consOT.getSearch();
            else
              cadena += "CHECK (" + consOT.getSearch() + ")";
          else if (consOT.getTipo().equals(Utiles.PRIMARY_KEY))
            cadena += Utiles.PRIMARY_KEY_LARGA + " (Lista de campos)"; 
          else if (consOT.getTipo().equals(Utiles.UNIQUE))
            cadena += "UNIQUE (Lista de campos)";
          else if (consOT.getTipo().equals(Utiles.FOREIGN_KEY))
            cadena += Utiles.FOREIGN_KEY_LARGA + "(Lista de campos) REFERENCES " + consOT.getFKOwner() + ".la otra tabla (lista de campos de la otra tabla)";
            
          cadena += ");" + Utiles.retCarro;
        
          // Apuntamos la tabla actual
          tablaActual = consOT.getNbTabla();
        }
      }
    }
    if (!tablaActual.equals(""))
    // Ponemos el final de la última sentencia
    {
      pw.println(cadena); // Escribimos lo que había
      pw.close();
      fw.close();
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de faltanConstraints");
}
public static void cambianConstraints(ArrayList cambian, ArrayList des, ArrayList otro) throws Exception
{
  System.out.println("Entramos en cambianConstraints");
  FileWriter fw = null;
  String tablaActual = "";
  ConstraintOT conCambiaOT = null;
  ConstraintOT conDesOT = null;
  ConstraintOT conOtroOT = null;
  String cadena = "";
  try
  {
    for (int i= 0; i < cambian.size(); i++)
    // Recorremos las constraints que cambian    
    {
      ConstraintOT consOT = (ConstraintOT) cambian.get(i);
      if ( !consOT.getNbCons().contains("$"))
      {
        if (! consOT.getNbTabla().equals(tablaActual))
        {
          if (! tablaActual.equals(""))
          // No es la primera vuelta. Acabamos el fichero anterior
          {
            pw.println(cadena); // Escribimos lo que había
            pw.close();
            fw.close();
            cadena = "";
          }
          // Abrimos el nuevo fichero
          String nbFich = directorioScripts + File.separator + consOT.getNbTabla() + ".sql";
          File f = new File(nbFich);
          if (f.exists())
            fw = new FileWriter(nbFich, true);    
          else
            fw = new FileWriter(nbFich);
          pw = new PrintWriter(fw);
        }
          cadena += "ALTER TABLE " + consOT.getNbTabla() + " DROP CONSTRAINT " + consOT.getNbCons() + ";" + Utiles.retCarro;
          cadena += "ALTER TABLE " + consOT.getNbTabla() + " ADD (CONSTRAINT " + consOT.getNbCons() + " ";
          if (consOT.getTipo().equals(Utiles.CONSTRAINT_SISTEMA))
          // CONSTRAINT DE SISTEMA
            if (consOT.getSearch().endsWith("IS NOT NULL"))
              cadena += consOT.getSearch();
            else
              cadena += "CHECK (" + consOT.getSearch() + ")";
          else if (consOT.getTipo().equals(Utiles.PRIMARY_KEY))
            cadena += Utiles.PRIMARY_KEY_LARGA + " (Lista de campos)"; 
          else if (consOT.getTipo().equals(Utiles.UNIQUE))
            cadena += "UNIQUE (Lista de campos)";
          else if (consOT.getTipo().equals(Utiles.FOREIGN_KEY))
            cadena += Utiles.FOREIGN_KEY_LARGA + "(Lista de campos) REFERENCES " + consOT.getFKOwner() + ".la otra tabla (lista de campos de la otra tabla)";
            
          cadena += ");" + Utiles.retCarro;
        
          // Apuntamos la tabla actual
          tablaActual = consOT.getNbTabla();
      }
    }
    if (!tablaActual.equals(""))
    // Ponemos el final de la última sentencia
    {
      cadena = cadena.substring(0, cadena.length() - 2); // Quitamos la última coma
      pw.println(cadena); // Escribimos lo que había
      pw.close();
      fw.close();
    }
      
    
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de cambianColumnas");
  
}


  /** Pone el comando para eliminar los sinónimos que sobran */
  public static void sobranSinonimos(ArrayList lista) throws Exception
  {
    FileWriter fw = null;
    String cadena = "";
    System.out.println("Entramos en sobranSinonimos");
    try
    {
      if (lista.size() != 0)
      {
    
        // Abrimos el fichero de sinónimos
        String nbFich = directorioScripts + File.separator + "sinonimos.sql";
        File f = new File(nbFich);
        fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
        
        for (int i = 0; i < lista.size(); i++)
        {
          SinonimoOT sinOT = (SinonimoOT) lista.get(i);
          
          cadena += "DROP SYNONYM " + sinOT.getNombreSinonimo() + ";" + Utiles.retCarro;
        }
        pw.println(cadena); // Escribimos lo que había
        pw.close();
        fw.close();
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      throw e;
    }
    System.out.println("Salimos de sobranSinonimos");
  }
  
  /** Pone el comando para CREAR los sinónimos que faltan */
  public static void faltanSinonimos(ArrayList lista) throws Exception
  {
    FileWriter fw = null;
    String cadena = "";
    System.out.println("Entramos en faltanSinonimos");
    try
    {
      if (lista.size() != 0)
      {
        // Abrimos el fichero de sinónimos
        String nbFich = directorioScripts + File.separator + "sinonimos.sql";
        File f = new File(nbFich);
        if (f.exists())
          fw = new FileWriter(nbFich, true);
        else
          fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
        
        for (int i = 0; i < lista.size(); i++)
        {
          SinonimoOT sinOT = (SinonimoOT) lista.get(i);
          
          cadena += "CREATE SYNONYM " + sinOT.getNombreSinonimo() + " FOR " + sinOT.getPropTabla() + "." + sinOT.getNombreTabla() + ";" + Utiles.retCarro;
        }
        pw.println(cadena); // Escribimos lo que había
        pw.close();
        fw.close();
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      throw e;
    }
    System.out.println("Salimos de faltanSinonimos");
  }  
  /** Pone el comando para CAMBIAR los sinónimos que faltan */
  public static void cambianSinonimos(ArrayList cambian, ArrayList des, ArrayList otro) throws Exception
  {
    FileWriter fw = null;
    String cadena = "";
    System.out.println("Entramos en cambianSinonimos");
    try
    {
      if (cambian.size() != 0)
      {
      
        // Abrimos el fichero de sinónimos
        String nbFich = directorioScripts + File.separator + "Sinonimos.sql";
        File f = new File(nbFich);
        if (f.exists())
          fw = new FileWriter(nbFich, true);
        else
          fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
        
        for (int i = 0; i < cambian.size(); i++)
        {
          SinonimoOT sinOT = (SinonimoOT) cambian.get(i);
          
          cadena += "DROP SYNONYM " + sinOT.getNombreSinonimo() + ";" + Utiles.retCarro;
          cadena += "CREATE SYNONYM " + sinOT.getNombreSinonimo() + " FOR " + sinOT.getPropTabla() + "." + sinOT.getNombreTabla() + ";" + Utiles.retCarro;
        }
        pw.println(cadena); // Escribimos lo que había
        pw.close();
        fw.close();
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      throw e;
    }
    System.out.println("Salimos de cambianSinonimos");
  }  
  
  /** Pone el comando para eliminar las secuencias que sobran */
  public static void sobranSecuencias(ArrayList lista) throws Exception
  {
    FileWriter fw = null;
    String cadena = "";
    System.out.println("Entramos en sobranSecuencias");
    try
    {
      if (lista.size() != 0)
      {
    
        // Abrimos el fichero de sinónimos
        String nbFich = directorioScripts + File.separator + "Secuencias.sql";
        File f = new File(nbFich);
        fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
        
        for (int i = 0; i < lista.size(); i++)
        {
          String secuencia = (String) lista.get(i);
          
          cadena += "DROP SEQUENCE " + secuencia + ";" + Utiles.retCarro;
        }
        pw.println(cadena); // Escribimos lo que había
        pw.close();
        fw.close();
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      throw e;
    }
    System.out.println("Salimos de sobranSecuencias");
  }
  
  /** Pone el comando para CREAR las secuencias que faltan */
  public static void faltanSecuencias(ArrayList lista) throws Exception
  {
    FileWriter fw = null;
    String cadena = "";
    System.out.println("Entramos en faltanSecuencias");
    try
    {
      if (lista.size() != 0)
      {
    
        // Abrimos el fichero de secuencias
        String nbFich = directorioScripts + File.separator + "Secuencias.sql";
        File f = new File(nbFich);
        if (f.exists())
          fw = new FileWriter(nbFich, true);
        else
          fw = new FileWriter(nbFich);
        pw = new PrintWriter(fw);
        
        for (int i = 0; i < lista.size(); i++)
        {
          String secuencia = (String) lista.get(i);
          
          cadena += "CREATE SEQUENCE " + secuencia + ";" + Utiles.retCarro;
        }
        pw.println(cadena); // Escribimos lo que había
        pw.close();
        fw.close();
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      throw e;
    }
    System.out.println("Salimos de faltanSecuencias");
  }    
  
  
  

    /** Pone el comando para eliminar las vistas que sobran */
    public static void sobranVistas(ArrayList lista) throws Exception
    {
      FileWriter fw = null;
      String cadena = "";
      System.out.println("Entramos en sobranVistas");
      try
      {
        if (lista.size() != 0)
        {
      
          // Abrimos el fichero de sinónimos
          String nbFich = directorioScripts + File.separator + "Vistas.sql";
          File f = new File(nbFich);
          fw = new FileWriter(nbFich);
          pw = new PrintWriter(fw);
          
          for (int i = 0; i < lista.size(); i++)
          {
            VistaOT visOT = (VistaOT) lista.get(i);
            
            cadena += "DROP VIEW " + visOT.getNombre() + ";" + Utiles.retCarro;
          }
          pw.println(cadena); // Escribimos lo que había
          pw.close();
          fw.close();
        }
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
        throw e;
      }
      System.out.println("Salimos de sobranVistas");
    }
    
    /** Pone el comando para CREAR llas vistas que faltan */
    public static void faltanVistas(ArrayList lista) throws Exception
    {
      FileWriter fw = null;
      String cadena = "";
      System.out.println("Entramos en faltanVistas");
      try
      {
        if (lista.size() != 0)
        {
          // Abrimos el fichero de sinónimos
          String nbFich = directorioScripts + File.separator + "Vistas.sql";
          File f = new File(nbFich);
          if (f.exists())
            fw = new FileWriter(nbFich, true);
          else
            fw = new FileWriter(nbFich);
          pw = new PrintWriter(fw);
          
          for (int i = 0; i < lista.size(); i++)
          {
            VistaOT visOT = (VistaOT) lista.get(i);
            
            cadena += "CREATE VIEW " + visOT.getNombre() + " AS " + Utiles.retCarro + visOT.getTexto() + ";" + Utiles.retCarro + "/" + Utiles.retCarro;
          }
          pw.println(cadena); // Escribimos lo que había
          pw.close();
          fw.close();
        }
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
        throw e;
      }
      System.out.println("Salimos de faltanVistas");
    }  
    /** Pone el comando para CAMBIAR las vistas que cambian */
    public static void cambianVistas(ArrayList cambian, ArrayList des, ArrayList otro) throws Exception
    {
      FileWriter fw = null;
      String cadena = "";
      System.out.println("Entramos en cambianVistas");
      try
      {
        if (cambian.size() != 0)
        {
          // Abrimos el fichero de sinónimos
          String nbFich = directorioScripts + File.separator + "Vistas.sql";
          File f = new File(nbFich);
          if (f.exists())
            fw = new FileWriter(nbFich, true);
          else
            fw = new FileWriter(nbFich);
          pw = new PrintWriter(fw);
          
          for (int i = 0; i < cambian.size(); i++)
          {
            VistaOT visOT = (VistaOT) cambian.get(i);
            
            cadena += "REPLACE VIEW " + visOT.getNombre() + " AS " + Utiles.retCarro + visOT.getTexto() + ";" + Utiles.retCarro + "/" + Utiles.retCarro;
          }
          pw.println(cadena); // Escribimos lo que había
          pw.close();
          fw.close();
        }
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
        throw e;
      }
      System.out.println("Salimos de cambianVistas");
    }  




 


/** Pone el comando para crear las TABLAS que faltan */
public static void faltanTablas(ArrayList<String> lista, ArrayList<ColumnaOT> lCols) throws Exception
{
  FileWriter fw = null;
  System.out.println("Entramos en faltanTablas");
  try
  {
    for (String nbTabla: lista)
    {
      // TODO Factorizar
      String nbFich = directorioScripts + File.separator + nbTabla + ".sql";
      File f = new File(nbFich);
      if (f.exists())
        fw = new FileWriter(nbFich, true);    
      else
        fw = new FileWriter(nbFich);
      pw = new PrintWriter(fw);
      
      pw.println("CREATE TABLE " + nbTabla + "\n("); 
      
      for (ColumnaOT colOT : lCols)
      {
System.out.println(colOT.getNbTabla());
        if (colOT.getNbTabla().equals(nbTabla))
        {
          pw.println(anadirColumna(colOT.getNbCol(), colOT.getTipo(), colOT.getLongitud(), colOT.getPrecision(), colOT.getEscala(), colOT.getAnulable(), colOT.getDefecto()) + ", ");
        }
      }
      
      
      
      pw.close();
      fw.close();
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de faltanTablas");
}    
    
/** Pone el comando para borrar las TABLAS que sobran */
public static void sobranTablas(ArrayList<String> lista) throws Exception
{
  FileWriter fw = null;
  System.out.println("Entramos en sobranTablas");
  try
  {
    for (String nbTabla: lista)
    {
      // TODO Factorizar
      String nbFich = directorioScripts + File.separator + nbTabla + ".sql";
      File f = new File(nbFich);
      if (f.exists())
        fw = new FileWriter(nbFich, true);    
      else
        fw = new FileWriter(nbFich);
      pw = new PrintWriter(fw);
      
      pw.println("DROP TABLE " + nbTabla); 
      pw.close();
      fw.close();
    }
  }
  catch (Exception e)
  {
    System.out.println(e.getMessage());
    throw e;
  }
  System.out.println("Salimos de sobranTablas");
}       

  private static String anadirColumna(String nbCol, String tipo, String longitud, String precision, String escala, String anulable, String defecto)
  {
    String resultado = "";
    if (tipo.equals("BLOB"))
      resultado += nbCol + " " + tipo;
    else if (tipo.equals("CHAR"))
      resultado += nbCol + " " + tipo + "(" + longitud + ")";
    else if (tipo.equals("DATE"))
      resultado += nbCol + " " + tipo;
    else if (tipo.equals("NUMBER"))
      resultado += nbCol + " " + tipo + "(" + precision + ", " + escala + ")";
    else if (tipo.equals("VARCHAR2"))
      resultado += nbCol + " " + tipo + "(" + longitud + ")";
    
    if (! anulable.equals("Y"))
      resultado += " NOT NULL";
    if (! defecto.equals(""))
      resultado += " DEFAULT " + defecto;
    
    return resultado;
  }
}
