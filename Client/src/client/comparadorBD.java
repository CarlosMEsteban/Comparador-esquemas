package client;

import client.ot.ColumnaOT;

import client.ot.ConstraintOT;

import client.ot.IndiceOT;

import client.ot.ParametroOT;
import client.ot.PermisoOT;
import client.ot.ProcedimientoOT;
import client.ot.ProcesoOT;
import client.ot.SinonimoOT;
import client.ot.TriggerOT;

import client.ot.VistaOT;

import java.io.BufferedReader;

import java.util.ArrayList;

import javax.swing.JProgressBar;


public class comparadorBD 
{
  BufferedReader brDes = null;
  BufferedReader brOtro = null;
  JProgressBar jProgressBar1;
  Logger bw;
  
    private ArrayList<String> lFaltan = new ArrayList<String>();
    private ArrayList<String> lSobran = new ArrayList<String>();
    private ArrayList faltanCols = new ArrayList();
    private ArrayList sobranCols = new ArrayList();
    private ArrayList cambianCols = new ArrayList();
    private ArrayList sobranInd = new ArrayList();
    
    private ArrayList<ColumnaOT> lColsDes = null;
  

  public comparadorBD(JProgressBar jPB, Logger bWriter)    
  {
    jPB.setStringPainted(true);      
    jProgressBar1 = jPB;    
    bw = bWriter;
  }
  
    public void comparaBD(String bDDes, String bDOtro) throws Exception
    {
      System.out.println("//////////////////////////Comenzamos BD//////////////////////////");
      bw.escribeEncabezado("Comenzamos BD");
      
      try
      {
        
        brDes = Utiles.abreParaLeer(bDDes);
        brOtro = Utiles.abreParaLeer(bDOtro); 
        
        
        //String fichResultado = "C:" + File.separator + "J2EE" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "resultado.txt";
  
        int pct = 0;jProgressBar1.setToolTipText("Tablas");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("Tablas");
        
        tablas();
        
        pct+=10;jProgressBar1.setToolTipText("columnas");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("columnas");
        
        columnas();
        
        pct+=10;jProgressBar1.setToolTipText("constraints");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("constraints");
        constraints();
  
        pct+=10;jProgressBar1.setToolTipText("indices");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("índices");
        
        indices();
        
        pct+=10;jProgressBar1.setToolTipText("triggers");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("triggers");
        triggers();
  
        pct+=10;jProgressBar1.setToolTipText("sinonimos");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("sinonimos");
        sinonimos();
  
        pct+=10;jProgressBar1.setToolTipText("secuencias");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("secuencias");
        secuencias();
        
        pct+=10;jProgressBar1.setToolTipText("procedimientos");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("procedimientos");
        procedimientos();
  
        pct+=10;jProgressBar1.setToolTipText("vistas");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("vistas");
        vistas();        
  
        pct+=10;jProgressBar1.setToolTipText("permisos");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("permisos");
        permisos();
        
        pct+=10;jProgressBar1.setToolTipText("parámetros");jProgressBar1.setValue(pct);
        bw.escribeEncabezado("parametros");
        parametros();
        
        
        System.out.println("Cerramos los ficheros");
        
        brDes.close();
        brOtro.close();
        
        jProgressBar1.setToolTipText("fin");jProgressBar1.setValue(100);
      }
      catch (Exception e)
      {
        System.out.println("Error en comparaBD: " + e);
        
        bw.escribeError("Error en comparaBD: " + e);
        brDes.close();
        brOtro.close();
        
      }

        System.out.println("//////////////////////////TERMINAMOS BD//////////////////////////");
      
    }
    
    /** Compara las tablas de uno y otro esquema */
    private void tablas() throws Exception
    {
    System.out.println("Entramos en tablas");
    try
    {
      ArrayList listaTablasDes = cogeLista(brDes);
      ArrayList listaTablasOtro = cogeLista(brOtro);
      faltan(listaTablasDes, listaTablasOtro);
      sobran(listaTablasDes, listaTablasOtro);

      
      for (String nbTabla : lFaltan)
      {
        bw.escribeLinea("Crear la tabla " + nbTabla);
      }
        
      for (String nbTabla : lSobran)
        bw.escribeLinea("Borrar la tabla " + nbTabla);
        
      if (lFaltan != null && lFaltan.size() > 0)
      {
        // TODO Coger la lista de columnas de Des sólo una vez
        lColsDes = cogeListaCol(brDes);
        CreaScript.faltanTablas(lFaltan, lColsDes);
      }
      CreaScript.sobranTablas(lSobran);
      
      System.out.println("Salimos de tablas");
      
      
    }
      
    catch (Exception e)
      {
        System.out.println("Error en compara: " + e);
        
        bw.escribeError("Error en compara: " + e);
        throw e;
      }

      
    }
    
    private void crearTabla(String nbTabla)
    {
      
    }
    
    /** Dado un fichero devolvemos la lista de tablas */
    private ArrayList cogeLista(BufferedReader br) throws Exception
    {
      System.out.println("Entramos en cogeLista");
      ArrayList resultado = new ArrayList();
      try
      {
        String tabla = "<TABLAS>";
        String nombre = "<NOMBRE>";
        String linea = "";
        String nombreTabla = "";
        boolean apartadoVacio = false;
        
        // Avanzamos hasta la sección de tablas
        while (linea.indexOf(tabla) == -1 && !apartadoVacio)
        {
          linea = br.readLine();
          if (linea.indexOf(Utiles.fAutotermina(tabla)) != -1)
            apartadoVacio = true;
        }
        if (!apartadoVacio)
        {
          while (linea.indexOf(Utiles.fin(tabla)) == -1)
          // Recorremos toda la sección de tablas
          {
            // Avanzamos hasta el siguiente nombre
            while (linea.indexOf(nombre) == -1)
              linea = br.readLine();
          
            // linea contiene el nombre de una tabla
            resultado.add(Utiles.extraeEntre(nombre, linea, Utiles.fin(nombre)));
          
            // Salgo del apartado de la tabla que acabo de leer
            linea = br.readLine();linea = br.readLine();
          }
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en cogeLista" + e);
        bw.escribeError("Error en cogeLista: " + e);
        throw e;
      }
      System.out.println("Salimos de cogeLista");
      return resultado;        
    }
 
           /** Compara la lista de columnas de los dos entornos */
    private void columnas() throws Exception
    {
      System.out.println("Entramos en columnas");
      try
      {
        // Si en el otro entorno falta alguna tabla, ya he cogido las columnas de des
        if (lColsDes == null || lColsDes.isEmpty())
          lColsDes = cogeListaCol(brDes);
        ArrayList listaOtro = cogeListaCol(brOtro); 



        faltanCols(lColsDes, listaOtro);
        sobranCols(lColsDes, listaOtro);
        difCols(lColsDes, listaOtro);
        
        CreaScript.sobranColumnas(sobranCols);
      
        for (int i = 0; i < sobranCols.size(); i++)
        {
          ColumnaOT colOT = (ColumnaOT) sobranCols.get(i);
          //CreaScript.columna("-", colOT);
          bw.escribeLinea("Hay que eliminar las columnas: " + colOT.getNbTabla() + "." + colOT.getNbCol());
        }
        
        CreaScript.faltanColumnas(faltanCols);
        
        for (int i = 0; i < faltanCols.size(); i++)
        {
          ColumnaOT colOT = (ColumnaOT) faltanCols.get(i);
          bw.escribeLinea("Hay que añadir las columnas: " + colOT.getNbTabla() + "." + colOT.getNbCol());
        }
        
        CreaScript.cambianColumnas(cambianCols, lColsDes, listaOtro);
        
        for (int i = 0; i < cambianCols.size(); i++)
        {
          ColumnaOT colOT = (ColumnaOT) cambianCols.get(i);
          bw.escribeLinea("Hay que cambiar la estructura de las columnas: " + colOT.getNbTabla() + "." + colOT.getNbCol());
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en columnas" + e);
        bw.escribeError("Error en columnas: " + e);
        throw e;
      }
      System.out.println("Salimos de columnas");
        
      
    }
    
    /** Pone en la lista faltan las tablas que hay en la primera lista que no están en la segunda*/
    private void faltan(ArrayList lista1, ArrayList lista2) throws Exception
    {
      System.out.println("Entramos en faltan");
      try
      {
        String tabla;
      
        for (int i = 0; i < lista1.size(); i++)
        {
          tabla = new String((String) lista1.get(i) );
          int j = 0;
          boolean enc = false;
          String aux = "";
          while ((!aux.equals(tabla)) && j < lista2.size())
          {
            aux = (String) lista2.get(j);
            j++;
          }
          if (!aux.equals(tabla))
            lFaltan.add(tabla);
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en faltan" + e);
        bw.escribeError("Error en faltan: " + e);
        throw e;
      }
      System.out.println("Salimos de faltan");
      
    }
    /** Pone en la lista sobran las tablas que hay en la segunda lista que no están en la primera */
    private void sobran(ArrayList lista1, ArrayList lista2)throws Exception
    {
      System.out.println("Entramos en sobran");
      try
      {
        String tabla;
      
        for (int i = 0; i < lista2.size(); i++)
        {
          tabla = new String((String) lista2.get(i) );
          int j = 0;
          boolean enc = false;
          String aux = "";
          while ((!aux.equals(tabla)) && j < lista1.size())
          {
            aux = (String) lista1.get(j);
            j++;
          }
          if (!aux.equals(tabla))
            lSobran.add(tabla);
        
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en sobran" + e);
        bw.escribeError("Error en sobran: " + e);
        throw e;
      }
      System.out.println("Salimos de sobran");
        
    }
    

    
    /** Devuelve la lista de columnas del fichero pasado por parámetro */
    private ArrayList<ColumnaOT> cogeListaCol(BufferedReader br) throws Exception
    {
      final String columnas = "<COLUMNAS>";
      final String columna = "<COLUMNA>";
      final String nombreTabla = "<NOMBRE_TAB>";
      final String nombreCol = "<NOMBRE_COL>";
      final String tipo = "<DATA_TYPE>";
      final String longitud = "<DATA_LENGTH>";
      final String precision = "<DATA_PRECISION>";
      final String escala = "<DATA_SCALE>";
      final String anulable = "<NULLABLE>";
      final String defecto = "<DATA_DEFAULT>";
      ArrayList resultado = new ArrayList();
      
      System.out.println("Entramos en cogeListaCol");
      

      boolean apartadoVacio = false;
      
      String linea = "";
      
      try
      {
      
      
        // Avanzo hasta la sección de columnas
        while (linea.indexOf(columnas) == -1 && !apartadoVacio)
        {
          linea = br.readLine();
          if (linea.indexOf(Utiles.fAutotermina(columnas)) != -1)
            apartadoVacio = true;
        }
        if (!apartadoVacio)
        {
          // Paso al primer apartado de una columna
          linea = br.readLine();
          while (linea.indexOf(Utiles.fin(columnas)) == -1)
          {
            // Paso a la línea del nombre de la tabla
            linea = br.readLine();
            // Extraigo el nombre de la tabla
            String nbTabla = Utiles.extraeEntre(nombreTabla, linea, Utiles.fin(nombreTabla));
        
/*            if (! descartarTabla(nbTabla))
            // Apunto la columna
            {
            */
              ColumnaOT colOT = new ColumnaOT();
              colOT.setNbTabla(nbTabla);
              //Paso a la línea con el nombre de la columna
              linea = br.readLine();
              colOT.setNbCol(Utiles.extraeEntre(nombreCol, linea, Utiles.fin(nombreCol)));
              // Paso a línea con el tipo
              linea = br.readLine();
              colOT.setTipo(Utiles.extraeEntre(tipo, linea, Utiles.fin(tipo)));
              // Paso a línea con la longitud
              linea = br.readLine();
              colOT.setLongitud(Utiles.extraeEntre(longitud, linea, Utiles.fin(longitud)));
              // Paso a línea con la precision
              linea = br.readLine();
              colOT.setPrecision(Utiles.extraeEntre(precision, linea, Utiles.fin(precision)));
              // Paso a línea con la escala
              linea = br.readLine();
              colOT.setEscala(Utiles.extraeEntre(escala, linea, Utiles.fin(escala)));
              // Paso a línea con si es anulable
              linea = br.readLine();
              colOT.setAnulable(Utiles.extraeEntre(anulable, linea, Utiles.fin(anulable)));
              // Paso a línea con el defecto
              linea = br.readLine();
              colOT.setDefecto(Utiles.extraeEntre(defecto, linea, Utiles.fin(defecto)));
          
              resultado.add(colOT);
          
              // Avanzo para salir del apartado de esta columna
              linea = br.readLine();linea = br.readLine();        
/*            }
            else
            // Descarto la columna
            {
              for (int i = 1; i <= 9; i++)
                linea = br.readLine();
            }
*/            
          }
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en cogeListaCol" + e);
        bw.escribeError("Error en cogeListaCol: " + e);
        throw e;
      }
      System.out.println("Salimos de cogeListaCol");
      return resultado;
    }
    
    /** Busca un nombre de la tabla en las listas sobran y faltan */
    private boolean descartarTabla(String nombre) throws Exception
    {
    
      int i = 0;
      boolean enc = false;
      System.out.println("Entramos en descartarTabla");
      try
      {
        while (i < lFaltan.size() && !enc)
        {
          if (lFaltan.get(i).equals(nombre))
            enc = true;
          else
            i++;
        }
        i = 0;
        while (i < lSobran.size() && !enc)
        {
          if (lSobran.get(i).equals(nombre))
            enc = true;
          else
            i++;
      
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en descartarTabla" + e);
        bw.escribeError("Error en descartarTabla: " + e);
        throw e;
      }
      System.out.println("Salimos de descartarTabla");
      
      return enc;
      
    }

  /** Apunta las columnas que faltan en la lista de columnas del otro esquema */
  private void faltanCols(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("Entramos en faltanCols");
    try
    {
      ColumnaOT colOT = new ColumnaOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        colOT = (ColumnaOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          ColumnaOT aux = (ColumnaOT) lista2.get(j);
          if (Utiles.colIgualNombre(colOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          faltanCols.add(colOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanCols" + e);
        bw.escribeError("Error en faltanCols: " + e);
      throw e;
    }
    System.out.println("Salimos de faltanCols");
  }
  
  /** Apunta las columnas que sobran en la lista de columnas del otro esquema */
  private void sobranCols(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("Entramos en sobranCols");
    try
    {
      ColumnaOT colOT = new ColumnaOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        colOT = (ColumnaOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          ColumnaOT aux = (ColumnaOT) lista1.get(j);
          if (Utiles.colIgualNombre(colOT, aux))
              enc = true;
          else
            j++;
        }
        if (!enc)
        {
          sobranCols.add(colOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranCols" + e);
        bw.escribeError("Error en sobranCols: " + e);
      throw e;
    }
    System.out.println("Salimos de sobranCols");
  }

  /** Apunta las columnas que tienen distinta estructura */
  private void difCols(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("Entramos en difCols");
    try
    {
      ColumnaOT colOT = new ColumnaOT();
      ColumnaOT aux = null;
      for (int i = 0; i < lista2.size(); i++)
      {
        colOT = (ColumnaOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (ColumnaOT) lista1.get(j);
          if (Utiles.colIgualNombre(colOT, aux))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difCols(colOT, aux))
            cambianCols.add(colOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difCols" + e);
        bw.escribeError("Error en difCols: " + e);
      throw e;
    }
    System.out.println("Salimos de difCols");

  }
  
  /** Compara las constraints de los esquemas */
  private void constraints() throws Exception
  {
    System.out.println("Entramos en constraints");
    try
    {
      ArrayList listaDes = cogeListaCons(brDes);
      ArrayList listaOtro = cogeListaCons(brOtro); 
      
      ArrayList faltanCons = faltanCons(listaDes, listaOtro);
      ArrayList sobranCons = sobranCons(listaDes, listaOtro);
      ArrayList difCons = difCons(listaDes, listaOtro);
      
      for (int i = 0; i < faltanCons.size(); i++)
      {
        ConstraintOT consOT = (ConstraintOT) faltanCons.get(i);
        bw.escribeLinea("Hay que crear la constraint: " + consOT.getNbTabla() + "." + consOT.getNbCons() + "->" + consOT.getSearch());
      }
      for (int i = 0; i < sobranCons.size(); i++)
      {
        ConstraintOT consOT = (ConstraintOT) sobranCons.get(i);
        bw.escribeLinea("Hay que borrar la constraint: " + consOT.getNbTabla() + "." + consOT.getNbCons() + consOT.getSearch());
      }
      for (int i = 0; i < difCons.size(); i++)
      {
        ConstraintOT consOT = (ConstraintOT) difCons.get(i);
        bw.escribeLinea("Hay que cambiar la constraint: " + consOT.getNbTabla() + "." + consOT.getNbCons() + consOT.getSearch());
      }
      
      CreaScript.sobranConstraints(sobranCons);
      CreaScript.faltanConstraints(faltanCons);
      CreaScript.cambianConstraints(difCons, listaDes, listaOtro);
       
    }
    catch (Exception e)
    {
      System.out.println("Error en constraints" + e);
        bw.escribeError("Error en constraints: " + e);
      throw e;
    }
    System.out.println("Salimos de constraints");
    
  }
  
    /** Devuelve la lista de constraints del fichero pasado por parámetro */
    private ArrayList cogeListaCons(BufferedReader br) throws Exception
    {
      System.out.println("Entramos en cogeListaCons");
      final String constraints = "<CONSTRAINTS>";
      final String constraint = "<CONSTRAINT>";
      final String nombreTabla = "<TABLE_NAME>";
      final String nombreCons = "<CONSTRAINT_NAME>";
      final String tipo = "<CONSTRAINT_TYPE>";
      final String search = "<SEARCH_CONDITION>";
      final String fKOwner = "<R_OWNER>";
      final String fKCons = "<R_CONSTRAINT_NAME>";
      final String delete = "<DELETE_RULE>";
      final String estado = "<STATUS>";
      final String deferrable = "<DEFERRABLE>";
      final String deferred = "<DEFERRED>";
      final String validado = "<VALIDATED>";
      
      boolean apartadoVacio = false;
      
      String nbCons = ""; // Variable para control de errores
      
      ArrayList resultado = new ArrayList();
      
      String linea = "";
      
      try
      {
      
      // Avanzo hasta la sección de columnas
      while (linea.indexOf(constraints) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(constraints)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        
        // Paso al primer apartado de una columna
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(constraints)) == -1)
        {
          // Paso a la línea del nombre de la tabla
          linea = br.readLine();
          // Extraigo el nombre de la tabla
          String nbTabla = Utiles.extraeEntre(nombreTabla, linea, Utiles.fin(nombreTabla));
      
          if (! descartarTabla(nbTabla))
          // Apunto la constraint
          {
            nbCons = "";
            ConstraintOT consOT = new ConstraintOT();
            consOT.setNbTabla(nbTabla);
            //Paso a la línea con el nombre de la columna
            linea = br.readLine();
            consOT.setNbCons(Utiles.extraeEntre(nombreCons, linea,Utiles.fin(nombreCons)));
            nbCons = consOT.getNbCons();
            // Paso a línea con el tipo
            linea = br.readLine();
            consOT.setTipo(Utiles.extraeEntre(tipo, linea,Utiles.fin(tipo)));
            // Paso a línea con la condición de búsqueda
            linea = br.readLine();
            
            consOT.setSearch(Utiles.cambiaSimbolos(   Utiles.extraeEntre(search, linea,Utiles.fin(search))));
            // Paso a línea con el propiestario de la clave foránea
            linea = br.readLine();
            consOT.setFKOwner(Utiles.extraeEntre(fKOwner, linea,Utiles.fin(fKOwner)));
            // Paso a línea con la constraint foránea
            linea = br.readLine();
            consOT.setFKCons(Utiles.extraeEntre(fKCons, linea,Utiles.fin(fKCons)));
            // Paso a línea con la regla de borrado
            linea = br.readLine();
            consOT.setDelete(Utiles.extraeEntre(delete, linea,Utiles.fin(delete)));
            // Paso a línea con el estado
            linea = br.readLine();
            consOT.setEstado(Utiles.extraeEntre(estado, linea,Utiles.fin(estado)));
            // Paso a línea con el deferrable
            linea = br.readLine();
            consOT.setDeferrable(Utiles.extraeEntre(deferrable, linea,Utiles.fin(deferrable)));
            // Paso a línea con el deferred
            linea = br.readLine();
            consOT.setDeferred(Utiles.extraeEntre(deferred, linea,Utiles.fin(deferred)));
            // Paso a línea con el validado
            linea = br.readLine();
            consOT.setValidado(Utiles.extraeEntre(validado, linea,Utiles.fin(validado)));
          
            resultado.add(consOT);
          
            // Avanzo para salir del apartado de esta constraint
            linea = br.readLine();linea = br.readLine();        
          }
          else
          // Descarto la columna
          {
            for (int i = 1; i <= 12; i++)
              linea = br.readLine();
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en cogeListaCons: " + e);      
      bw.escribeError("Error en cogeListaCons: " + e);
      throw e;      
    }
      return resultado;
    }
      

  /** Devuelvo la lista de constraints que están en la primera lista, pero no en la segunda */
  private ArrayList faltanCons(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanCons");
    try
    {
      ConstraintOT consOT = new ConstraintOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        consOT = (ConstraintOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          ConstraintOT aux = (ConstraintOT) lista2.get(j);
          if (Utiles.consIgual(consOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(consOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanCons: " + e);      
      bw.escribeError("Error en faltanCons: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanCons");
    return resultado;
  }
  /** Devuelvo la lista de constraints que están en la segunda lista, pero no en la primera */
  private ArrayList sobranCons(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranCons");
    try
    {
    
      ConstraintOT consOT = new ConstraintOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        consOT = (ConstraintOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          ConstraintOT aux = (ConstraintOT) lista1.get(j);
          if (Utiles.consIgual(consOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(consOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranCons: " + e);      
      bw.escribeError("Error en sobranCons: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranCons");
      
    return resultado;
  }
  
  /** Devuelvo la lista de constraints que varían en estructura entre la primera lsita y la segunda */
  private ArrayList difCons(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en difCons");
    try
    {
    
      ConstraintOT aux = null;
      ConstraintOT consOT = new ConstraintOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        consOT = (ConstraintOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (ConstraintOT) lista1.get(j);
          if (Utiles.consIgual(consOT, aux))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difCons(consOT, aux))
            resultado.add(consOT);        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difCons: " + e);      
      bw.escribeError("Error en difCons: " + e);
      throw e;            
    }
    System.out.println("Salimos de difCons");
    
    return resultado;
  }
  
  /** Compara los índices de los esquemas */
  private void indices() throws Exception
  {
    System.out.println("entramos en indices");
    try
    {
  
      ArrayList listaDes = cogeListaInd(brDes);
      ArrayList listaOtro = cogeListaInd(brOtro); 
      
      ArrayList faltanInd = faltanInd(listaDes, listaOtro);
      sobranInd(listaDes, listaOtro);
      ArrayList difInd = difInd(listaDes, listaOtro);
      
      for (int i = 0; i < faltanInd.size(); i++)
      {
        IndiceOT indOT = (IndiceOT) faltanInd.get(i);
        bw.escribeLinea("Hay que crear el índice: " + indOT.getNombreTabla() + "." + indOT.getNombreInd());
      }
      for (int i = 0; i < sobranInd.size(); i++)
      {
        IndiceOT indOT = (IndiceOT) sobranInd.get(i);
        bw.escribeLinea("Hay que borrar el índice: " + indOT.getNombreTabla() + "." + indOT.getNombreInd());
      }
      for (int i = 0; i < difInd.size(); i++)
      {
        IndiceOT indOT = (IndiceOT) difInd.get(i);
        bw.escribeLinea("Hay que cambiar el índice: " + indOT.getNombreTabla() + "." + indOT.getNombreInd());
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en indices: " + e);      
      bw.escribeError("Error en indices: " + e);
      throw e;            
    }
    System.out.println("Salimos de indices");

  }
  
    /** Devuelve la lista de índices del fichero pasado por parámetro */
    private ArrayList cogeListaInd(BufferedReader br) throws Exception
    {
    System.out.println("entramos en cogeListaInd");
    ArrayList resultado = new ArrayList();
    try
    {
    
      final String indices = "<INDICES>";
      final String indice = "<INDICE>";
      final String nombreTabla = "<TABLE_NAME>";
      final String nombreInd = "<INDEX_NAME>";
      final String compresion = "<COMPRESSION>";
      final String tipo = "<INDEX_TYPE>";
      final String prefijo = "<PREFIX_LENGTH>";
      final String tipoTabla = "<TABLE_TYPE>";
      final String unico = "<UNIQUENESS>";
      
      boolean apartadoVacio = false;
      
      
      String linea = "";
      
      // Avanzo hasta la sección de índices
      while (linea.indexOf(indices) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(indices)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de un índice
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(indices)) == -1)
        {
          // Paso a la línea del nombre de la tabla
          linea = br.readLine();
          // Extraigo el nombre de la tabla
          String nbTabla = Utiles.extraeEntre(nombreTabla, linea,Utiles.fin(nombreTabla));
      
          if (! descartarTabla(nbTabla))
          // Apunto la columna
          {
            IndiceOT indOT = new IndiceOT();
            indOT.setNombreTabla(nbTabla);
            //Paso a la línea con el nombre del índice
            linea = br.readLine();
            indOT.setNombreInd(Utiles.extraeEntre(nombreInd, linea,Utiles.fin(nombreInd)));
            // Paso a línea con la compresión
            linea = br.readLine();
            indOT.setCompresion(Utiles.extraeEntre(compresion, linea,Utiles.fin(compresion)));
            // Paso a línea con el tipo
            linea = br.readLine();
            indOT.setTipo(Utiles.extraeEntre(tipo, linea,Utiles.fin(tipo)));
            // Paso a línea con el prefijo
            linea = br.readLine();
            indOT.setPrefijo(Utiles.extraeEntre(prefijo, linea,Utiles.fin(prefijo)));
            // Paso a línea con el tipo de tabla
            linea = br.readLine();
            indOT.setTipoTabla(Utiles.extraeEntre(tipoTabla, linea,Utiles.fin(tipoTabla)));
          // Paso a línea con si es único
            linea = br.readLine();
            indOT.setUnico(Utiles.extraeEntre(unico, linea,Utiles.fin(unico)));
            
            resultado.add(indOT);
            
            // Avanzo para salir del apartado de este índice
            linea = br.readLine();linea = br.readLine();        
          }
          else
          // Descarto el índice
          {
            for (int i = 1; i <= 8; i++)
              linea = br.readLine();
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en cogeListaInd: " + e);      
      bw.escribeError("Error en cogeListaInd: " + e);
      throw e;            
    }
    System.out.println("Salimos de cogeListaInd");
      
      return resultado;
    }  
  /** Apunta los índices que sobran en la lista de índices del otro esquema */
  private void sobranInd(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("Entramos en sobranInd");
    try
    {
      IndiceOT indOT = new IndiceOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        indOT = (IndiceOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          IndiceOT aux = (IndiceOT) lista1.get(j);
          if (indOT.getNombreInd().equals(aux.getNombreInd()))
              enc = true;
          else
            j++;
        }
        if (!enc)
        {
          sobranCols.add(indOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranInd" + e);
        bw.escribeError("Error en sobranInd: " + e);
      throw e;
    }
    System.out.println("Salimos de sobranInd");
  }    
  /** Devuelvo la lista de índices que están en la primera lista, pero no en la segunda */
  private ArrayList faltanInd(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("entramos en faltanInd");
    ArrayList resultado = new ArrayList();
    try
    {
    
      
      IndiceOT indOT = new IndiceOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        indOT = (IndiceOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          IndiceOT aux = (IndiceOT) lista2.get(j);
          if (indOT.getNombreInd().equals(aux.getNombreInd()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(indOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanInd: " + e);      
      bw.escribeError("Error en faltanInd: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanInd");

    return resultado;
  }

  /** Devuelvo la lista de índices que varían en estructura entre la primera lista y la segunda */
  private ArrayList difInd(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("entramos en difInd");
    ArrayList resultado = new ArrayList();
    try
    {
      
      IndiceOT aux = null;
      IndiceOT indOT = new IndiceOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        indOT = (IndiceOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (IndiceOT) lista1.get(j);
          if (indOT.getNombreInd().equals(aux.getNombreInd()))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difInd(indOT, aux))
            resultado.add(indOT);        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difInd: " + e);      
      bw.escribeError("Error en difInd: " + e);
      throw e;            
    }
    System.out.println("Salimos de difInd");
    return resultado;
  }
  
  /** Compara los triggers de los esquemas */
  private void triggers() throws Exception
  {
    System.out.println("entramos en triggers");
    try
    {
      ArrayList listaDes = cogeListaTrig(brDes);
      ArrayList listaOtro = cogeListaTrig(brOtro); 
      
      ArrayList faltanTrig = faltanTrig(listaDes, listaOtro);
      ArrayList sobranTrig = sobranTrig(listaDes, listaOtro);
      
      for (int i = 0; i < faltanTrig.size(); i++)
      {
        TriggerOT trigOT = (TriggerOT) faltanTrig.get(i);
        bw.escribeLinea("Hay que crear el trigger: " + trigOT.getNombreTabla() + "." + trigOT.getNombreTrigger());
      }
      for (int i = 0; i < sobranTrig.size(); i++)
      {
        TriggerOT trigOT = (TriggerOT) sobranTrig.get(i);
        bw.escribeLinea("Hay que borrar el trigger: " + trigOT.getNombreTabla() + "." + trigOT.getNombreTrigger());
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en triggers: " + e);      
      bw.escribeError("Error en triggers: " + e);
      throw e;            
    }
    System.out.println("Salimos de triggers");
      
  }
  
  
    /** Devuelve la lista de triggers del fichero pasado por parámetro */
    private ArrayList cogeListaTrig(BufferedReader br) throws Exception
    {
    final String triggers = "<TRIGGERS>";
    final String trigger = "<TRIGGER>";
    final String nombreTabla = "<TABLE_NAME>";
    final String tipoAccion = "<ACTION_TYPE>";
    final String tipoObjeto = "<BASE_OBJECT_TYPE>";
    final String columna = "<COLUMN_NAME>";
    final String descripcion = "<DESCRIPTION>";
    final String referencia = "<REFERENCING_NAMES>";
    final String estado = "<STATUS>";
    final String nombreTrigger = "<TRIGGER_NAME>";
    final String tipo = "<TRIGGER_TYPE>";
    final String evento = "<TRIGGERING_EVENT>";
    final String when = "<WHEN_CLAUSE>";
      
    boolean apartadoVacio = false;
            
    ArrayList resultado = new ArrayList();
      
    String linea = "";
    System.out.println("entramos en cogeListaTrig");
    try
    {
      
      // Avanzo hasta la sección de triggers
      while (linea.indexOf(triggers) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(triggers)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de un trigger
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(triggers)) == -1)
        {
          // Paso a la línea del nombre de la tabla
          linea = br.readLine();
          // Extraigo el nombre de la tabla
          String nbTabla = Utiles.extraeEntre(nombreTabla, linea,Utiles.fin(nombreTabla));
        
          if (! descartarTabla(nbTabla))
          // Apunto la columna
          {
            TriggerOT trigOT = new TriggerOT();
            trigOT.setNombreTabla(nbTabla);
            //Paso a la línea con el tipo de acción
            linea = br.readLine();
            trigOT.setTipoAccion(Utiles.extraeEntre(tipoAccion, linea,Utiles.fin(tipoAccion)));
            // Paso a línea con tipo de objeto
            linea = br.readLine();
            trigOT.setTipoObjeto(Utiles.extraeEntre(tipoObjeto, linea,Utiles.fin(tipoObjeto)));
            // Paso a línea con el nombre de la columna
            linea = br.readLine();
            trigOT.setColumna(Utiles.extraeEntre(columna, linea,Utiles.fin(columna)));
            // Paso a línea con la descripción
            linea = br.readLine();
            int a =linea.indexOf(Utiles.fin (descripcion)); 
            while (linea.indexOf(Utiles.fin (descripcion)) == -1)
              linea = linea + br.readLine();
            trigOT.setDescripcion(Utiles.extraeEntre(descripcion, linea,Utiles.fin(descripcion)));
            // Paso a línea con la referencia
            linea = br.readLine();
            trigOT.setReferencia(Utiles.extraeEntre(referencia, linea,Utiles.fin(referencia)));
            // Paso a línea con el estado
            linea = br.readLine();
            trigOT.setEstado(Utiles.extraeEntre(estado, linea,Utiles.fin(estado)));
            // Paso a línea con el nombre
            linea = br.readLine();
            trigOT.setNombreTrigger(Utiles.extraeEntre(nombreTrigger, linea,Utiles.fin(nombreTrigger)));
            // Paso a línea con el tipo
            linea = br.readLine();
            trigOT.setTipo(Utiles.extraeEntre(tipo, linea,Utiles.fin(tipo)));
            // Paso a línea con el evento
            linea = br.readLine();
            trigOT.setEvento(Utiles.extraeEntre(evento, linea,Utiles.fin(evento)));
            // Paso a línea con el When
            linea = br.readLine();
            trigOT.setWhen(Utiles.extraeEntre(when, linea,Utiles.fin(when)));
            
            resultado.add(trigOT);
          
            // Avanzo para salir del apartado de este índice
            linea = br.readLine();linea = br.readLine();        
          }
          else
          // Descarto el trigger
          {
            while (linea.indexOf(Utiles.fin(trigger)) == -1)
            // Salto hasta el fin del trigger
              linea = br.readLine();
            // Salto hata el siguiente trigger
            linea = br.readLine();
          }
        }
      }
          
    }
    catch (Exception e)
    { 
      System.out.println("Error en cogeListaTrig: " + e);
      bw.escribeError("Error en cogeListaTrig: " + e);
      throw e;
    }
System.out.println("Salimos de cogeListaTrig");
      return resultado;
      
    }  
  /** Devuelvo la lista de triggers que están en la primera lista, pero no en la segunda */
  private ArrayList faltanTrig(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanTrig");
    try
    {
      TriggerOT trigOT = new TriggerOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        trigOT = (TriggerOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          TriggerOT aux = (TriggerOT) lista2.get(j);
          if (trigOT.getNombreTrigger().equals(aux.getNombreTrigger()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(trigOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanTrig: " + e);      
      bw.escribeError("Error en faltanTrig: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanTrig");
    return resultado;
  }
  /** Devuelvo la lista de triggers que están en la primera lista, pero no en la segunda */
  private ArrayList sobranTrig(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranTrig");
    try
    {
    
      TriggerOT trigOT = new TriggerOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        trigOT = (TriggerOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          TriggerOT aux = (TriggerOT) lista1.get(j);
          if (trigOT.getNombreTrigger().equals(aux.getNombreTrigger()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(trigOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranTrig: " + e);      
      bw.escribeError("Error en sobranTrig: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranTrig");
    return resultado;
  }
  
  /** Compara los sinonimos de los esquemas */
  private void sinonimos() throws Exception
  {
    System.out.println("entramos en sinonimos");
    try
    {
      ArrayList listaDes = cogeListaSin(brDes);
      ArrayList listaOtro = cogeListaSin(brOtro); 
      
      ArrayList faltan = faltanSin(listaDes, listaOtro);
      ArrayList sobran = sobranSin(listaDes, listaOtro);
      ArrayList dif = difSin(listaDes, listaOtro);
      
      for (int i = 0; i < faltan.size(); i++)
      {
        SinonimoOT sinOT = (SinonimoOT) faltan.get(i);
        bw.escribeLinea("Hay que crear el sinónimo: " + sinOT.getNombreSinonimo());
      }
      for (int i = 0; i < sobran.size(); i++)
      {
        SinonimoOT sinOT = (SinonimoOT) sobran.get(i);
        bw.escribeLinea("Hay que borrar el sinonimo: " + sinOT.getNombreSinonimo());
      }
      for (int i = 0; i < dif.size(); i++)
      {
        SinonimoOT sinOT = (SinonimoOT) dif.get(i);
        bw.escribeLinea("Hay que cambiar el sinonimo: " + sinOT.getNombreSinonimo());
      }
      
      CreaScript.sobranSinonimos(sobran);
      CreaScript.faltanSinonimos(faltan);
      CreaScript.cambianSinonimos(dif, listaDes, listaOtro);
    }
    catch (Exception e)
    {
      System.out.println("Error en sinonimos: " + e);      
      bw.escribeError("Error en sinonimos: " + e);
      throw e;            
    }
    System.out.println("Salimos de sinonimos");

  }
    /** Devuelve la lista de sinónimos del fichero pasado por parámetro */
    private ArrayList cogeListaSin(BufferedReader br) throws Exception
    {
      final String sinonimos = "<SINONIMOS>";
      final String sinonimo = "<SINONIMOS>";
      final String propTabla = "<TABLE_OWNER>";
      final String nombreTabla = "<TABLE_NAME>";
      final String nombreSinonimo = "<SYNONYM_NAME>";
      
      ArrayList resultado = new ArrayList();
    System.out.println("entramos en cogeListaSin");
    try
    {
      
      boolean apartadoVacio = false;

      String linea = "";
      
      // Avanzo hasta la sección de sinónimos
      while (linea.indexOf(sinonimos) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(sinonimos)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de un sinónimo
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(sinonimos)) == -1)
        {
          SinonimoOT sinOT = new SinonimoOT();
          // Paso a la línea del propietario de la tabla
          linea = br.readLine();
          sinOT.setPropTabla(Utiles.extraeEntre(propTabla, linea,Utiles.fin(propTabla)));
          //Paso a la línea con el nombre de la tabla
          linea = br.readLine();
          sinOT.setNombreTabla(Utiles.extraeEntre(nombreTabla, linea,Utiles.fin(nombreTabla)));
          // Paso a línea con el nombre del sinónimo
          linea = br.readLine();
          sinOT.setNombreSinonimo(Utiles.extraeEntre(nombreSinonimo, linea,Utiles.fin(nombreSinonimo)));
          
          resultado.add(sinOT);
          
          // Avanzo para salir del apartado de este índice
          linea = br.readLine();linea = br.readLine();        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en cogeListaSin: " + e);      
      bw.escribeError("Error en cogeListaSin: " + e);
      throw e;            
    }
    System.out.println("Salimos de cogeListaSin");
      return resultado;
    }  
  /** Devuelvo la lista de sinónimos que están en la primera lista, pero no en la segunda */
  private ArrayList faltanSin(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanSin");
    try
    {
      SinonimoOT sinOT = new SinonimoOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        sinOT = (SinonimoOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          SinonimoOT aux = (SinonimoOT) lista2.get(j);
          if (sinOT.getNombreSinonimo().equals(aux.getNombreSinonimo()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(sinOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanSin: " + e);      
      bw.escribeError("Error en faltanSin: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanSin");

    return resultado;
  }  
  /** Devuelvo la lista de sinónimos que están en la segunda lista, pero no en la primera */
  private ArrayList sobranSin(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranSin");
    try
    {
      SinonimoOT sinOT = new SinonimoOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        sinOT = (SinonimoOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          SinonimoOT aux = (SinonimoOT) lista1.get(j);
          if (sinOT.getNombreSinonimo().equals(aux.getNombreSinonimo()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(sinOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranSin: " + e);      
      bw.escribeError("Error en sobranSin: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranSin");
    
    return resultado;
  }
  
  /** Devuelvo la lista de sinónimos que varían en estructura entre la primera lista y la segunda */
  private ArrayList difSin(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("entramos en difSin");
    ArrayList resultado = new ArrayList();
    try
    {
      
      SinonimoOT aux = null;
      SinonimoOT sinOT = new SinonimoOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        sinOT = (SinonimoOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (SinonimoOT) lista1.get(j);
          if (sinOT.getNombreSinonimo().equals(aux.getNombreSinonimo()))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difSin(sinOT, aux))
            resultado.add(sinOT);        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difSin: " + e);      
      bw.escribeError("Error en difSin: " + e);
      throw e;            
    }
    System.out.println("Salimos de difSin");
      
    return resultado;
  }

  /** Compara las secuencias de los esquemas */
  private void secuencias() throws Exception
  {
    System.out.println("entramos en secuencias");
    try
    {
  
      ArrayList listaDes = cogeListaSec(brDes);
      ArrayList listaOtro = cogeListaSec(brOtro); 
      
      ArrayList faltan = faltanSec(listaDes, listaOtro);
      ArrayList sobran = sobranSec(listaDes, listaOtro);
      
      for (int i = 0; i < faltan.size(); i++)
      {
        String sec = (String) faltan.get(i);
        bw.escribeLinea("Hay que crear la secuencia: " + sec);
      }
      for (int i = 0; i < sobran.size(); i++)
      {
        String sec = (String) sobran.get(i);
        bw.escribeLinea("Hay que borrar la secuencia: " + sec);
      }
      
      CreaScript.sobranSecuencias(sobran);
      CreaScript.faltanSecuencias(faltan);
    }
    catch (Exception e)
    {
      System.out.println("Error en secuencias: " + e);      
      bw.escribeError("Error en secuencias: " + e);
      throw e;            
    }
    System.out.println("Salimos de secuencias");
  }
    /** Devuelve la lista de secuencias del fichero pasado por parámetro */
    private ArrayList cogeListaSec(BufferedReader br) throws Exception
    {
      final String secuencias = "<SECUENCIAS>";
      final String secuencia = "<SECUENCIA>";
      final String nombre = "<NOMBRE>";
      
      String nbSec = ""; // Variable para control de errores
      
      boolean apartadoVacio = false;
      
      ArrayList resultado = new ArrayList();
    System.out.println("entramos en cogeListaSec");
      
      String linea = "";
      
      try
      {
      
      // Avanzo hasta la sección de secuencias
      while (linea.indexOf(secuencias) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(secuencias)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de una secuencia
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(secuencias)) == -1)
        {
          // Paso a la línea del nombre de la secuencia
          linea = br.readLine();
          // Extraigo el nombre de la secuencia
          nbSec = Utiles.extraeEntre(nombre, linea,Utiles.fin(nombre));
        
          resultado.add(nbSec);
          
          // Avanzo para salir del apartado de esta secuencia
          linea = br.readLine();linea = br.readLine();        
        }
      }
    }
    catch (Exception e)
    { 
      System.out.println("Error en cogeListaSec: " + e);      
      bw.escribeError("Error en cogeListaSec: " + e);
      throw e;            
    }
    System.out.println("Salimos de cogeListaSec");
      return resultado;
    }
  
  /** Devuelvo la lista de secuencias que están en la primera lista, pero no en la segunda */
  private ArrayList faltanSec(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanSec");
    try
    {
      String sec = "";
      for (int i = 0; i < lista1.size(); i++)
      {
        sec = (String) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          String aux = (String) lista2.get(j);
          if (sec.equals(aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(sec);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanSec: " + e);      
      bw.escribeError("Error en faltanSec: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanSec");
    return resultado;
  }
  /** Devuelvo la lista de secuencias que están en la segunda lista, pero no en la primera */
  private ArrayList sobranSec(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranSec");
    try
    {
      String sec = "";
      for (int i = 0; i < lista2.size(); i++)
      {
        sec = (String) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          String aux = (String) lista1.get(j);
          if (sec.equals(aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(sec);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranSec: " + e);      
      bw.escribeError("Error en sobranSec: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranSec");
    return resultado;
  }
  
  /** Compara las procedimientos de los esquemas */
  private void procedimientos() throws Exception
  {
    System.out.println("entramos en procedimientos");
    try
    {
      ArrayList listaDes = cogeListaProc(brDes);
      ArrayList listaOtro = cogeListaProc(brOtro); 
      
      ArrayList faltan = faltanProc(listaDes, listaOtro);
      ArrayList sobran = sobranProc(listaDes, listaOtro);
      ArrayList dif = difProc(listaDes, listaOtro);
      
      for (int i = 0; i < faltan.size(); i++)
      {
        ProcedimientoOT procOT = (ProcedimientoOT) faltan.get(i);
        bw.escribeLinea("Hay que crear el procedimiento: " + procOT.getNombre());
      }
      for (int i = 0; i < sobran.size(); i++)
      {
        ProcedimientoOT procOT = (ProcedimientoOT) sobran.get(i);
        bw.escribeLinea("Hay que quitar el procedimiento: " + procOT.getNombre());
      }
      for (int i = 0; i < dif.size(); i++)
      {
        ProcedimientoOT procOT = (ProcedimientoOT) dif.get(i);
        bw.escribeLinea("Hay que modificar el procedimiento: " + procOT.getNombre());
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en procedimientos: " + e);      
      bw.escribeError("Error en procedimientos: " + e);
      throw e;            
    }
    System.out.println("Salimos de procedimientos");
       
    
  }
  
    /** Devuelve la lista de procedimientos del fichero pasado por parámetro */
    private ArrayList cogeListaProc(BufferedReader br) throws Exception
    {
      final String procs = "<PROCS>";
      final String proc = "<PROC>";
      final String nombre = "<OBJECT_NAME>";
      final String tipo = "<OBJECT_TYPE>";
      final String lineas = "<LINEAS>";
      final String estado = "<STATUS>";
      
      boolean apartadoVacio = false;
      
      ArrayList resultado = new ArrayList();
      
    System.out.println("entramos en cogeListaProc");
    try
    {
      String nombreProc = "";
      
      String linea = "";
      
      // Avanzo hasta la sección de procedimientos
      while (linea.indexOf(procs) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(procs)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de un procedimiento
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(procs)) == -1)
        {
          ProcedimientoOT procOT = new ProcedimientoOT();
          // Paso a la línea del nombre del procedimiento
          linea = br.readLine();
          nombreProc = Utiles.extraeEntre(nombre, linea,Utiles.fin(nombre));
          procOT.setNombre(nombreProc);
          
          //Paso a la línea con el tipo de proc
          linea = br.readLine();
          procOT.setTipo(Utiles.extraeEntre(tipo, linea,Utiles.fin(tipo)));
          // Paso a línea con las líneas
          linea = br.readLine();
          procOT.setLineas(Utiles.extraeEntre(lineas, linea,Utiles.fin(lineas)));
          // Paso a línea con el estado
          linea = br.readLine();
          procOT.setEstado(Utiles.extraeEntre(estado, linea,Utiles.fin(estado)));
            
          resultado.add(procOT);
            
          // Avanzo para salir del apartado de este índice
          linea = br.readLine();linea = br.readLine();        
        }
      }
     }
    catch (Exception e)
    {
      System.out.println("Error en cogeListaProc: " + e);      
      bw.escribeError("Error en cogeListaProc: " + e);
      throw e;            
    }
    System.out.println("Salimos de cogeListaProc");
     
      return resultado;
    }  
  /** Devuelvo la lista de procediminetos que están en la primera lista, pero no en la segunda */
  private ArrayList faltanProc(ArrayList lista1, ArrayList lista2) throws Exception
  {
    System.out.println("entramos en faltanProc");
    ArrayList resultado = new ArrayList();
    try
    {
      
      ProcedimientoOT procOT = new ProcedimientoOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        procOT = (ProcedimientoOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          ProcedimientoOT aux = (ProcedimientoOT) lista2.get(j);
          if (procOT.getNombre().equals(aux.getNombre()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(procOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanProc: " + e);      
      bw.escribeError("Error en faltanProc: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanProc");

    return resultado;
  }  
  /** Devuelvo la lista de procedimientos que están en la segunda lista, pero no en la primera */
  private ArrayList sobranProc(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranProc");
    try
    {
    
      ProcedimientoOT procOT = new ProcedimientoOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        procOT = (ProcedimientoOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          ProcedimientoOT aux = (ProcedimientoOT) lista1.get(j);
          if (procOT.getNombre().equals(aux.getNombre()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(procOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranProc: " + e);      
      bw.escribeError("Error en sobranProc: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranProc");
      
    return resultado;
  }
  /** Devuelvo la lista de procedimientos que varían en estructura entre la primera lista y la segunda */
  private ArrayList difProc(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en difProc");
    try
    {
      ProcedimientoOT aux = null;
      ProcedimientoOT procOT = new ProcedimientoOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        procOT = (ProcedimientoOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (ProcedimientoOT) lista1.get(j);
          if (procOT.getNombre().equals(aux.getNombre()))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difProc(procOT, aux))
            resultado.add(procOT);        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difProc: " + e);      
      bw.escribeError("Error en difProc: " + e);
      throw e;            
    }
    System.out.println("Salimos de difProc");
    return resultado;
  }
  
  /** Compara las vistas de los esquemas */
  private void vistas() throws Exception
  {
    System.out.println("entramos en vistas");
    try
    {
      ArrayList listaDes = cogeListaVis(brDes);
      ArrayList listaOtro = cogeListaVis(brOtro); 
      
      ArrayList faltan = faltanVis(listaDes, listaOtro);
      ArrayList sobran = sobranVis(listaDes, listaOtro);
      ArrayList dif = difVis(listaDes, listaOtro);
      
      for (int i = 0; i < faltan.size(); i++)
      {
        VistaOT vistaOT = (VistaOT) faltan.get(i);
        bw.escribeLinea("Hay que crear la vista: " + vistaOT.getNombre());
      }
      for (int i = 0; i < sobran.size(); i++)
      {
        VistaOT vistaOT = (VistaOT) sobran.get(i);
        bw.escribeLinea("Hay que borrar la vista: " + vistaOT.getNombre());
      }
      for (int i = 0; i < dif.size(); i++)
      {
        VistaOT vistaOT = (VistaOT) dif.get(i);
        bw.escribeLinea("Hay que modificar la vista: " + vistaOT.getNombre());
      }
      
      CreaScript.faltanVistas(faltan);
      CreaScript.sobranVistas(sobran);
      CreaScript.cambianVistas(dif, new ArrayList(), new ArrayList());
    }
    catch (Exception e)
    {
      System.out.println("Error en vistas: " + e);      
      bw.escribeError("Error en vistas: " + e);
      throw e;            
    }
    System.out.println("Salimos de vistas");
  }
  
    /** Devuelve la lista de vistas del fichero pasado por parámetro */
    private ArrayList cogeListaVis(BufferedReader br) throws Exception
    {
      final String vistas = "<VISTAS>";
      final String vista = "<VISTA>";
      final String nombre = "<NOMBRE>";
      final String texto = "<TEXT>";
      
      boolean apartadoVacio = false;
      
      ArrayList resultado = new ArrayList();
      
    System.out.println("entramos en cogeListaVis");
    try
    {
      String linea = "";
      
      // Avanzo hasta la sección de vistas
      while (linea.indexOf(vistas) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(vistas)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de una vista
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(vistas)) == -1)
        {
          VistaOT visOT = new VistaOT();
          // Paso a la línea del nombre de la vista
          linea = br.readLine();
          visOT.setNombre(Utiles.extraeEntre(nombre, linea,Utiles.fin(nombre)));
          //Paso a la línea con el texto de la vista
          linea = br.readLine();
          while (linea.indexOf(Utiles.fin(texto)) == -1)
            linea = linea + br.readLine();
          visOT.setTexto(Utiles.extraeEntre(texto, linea,Utiles.fin(texto)));
            
          resultado.add(visOT);
          
          // Avanzo para salir del apartado de esta vista
          linea = br.readLine();linea = br.readLine();        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en cogeListaVis: " + e);      
      bw.escribeError("Error en cogeListaVis: " + e);
      throw e;            
    }
    System.out.println("Salimos de cogeListaVis");
      return resultado;
    }  
  /** Devuelvo la lista de vistas que están en la primera lista, pero no en la segunda */
  private ArrayList faltanVis(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanVis");
    try
    {
      VistaOT visOT = null;
      for (int i = 0; i < lista1.size(); i++)
      {
        visOT = (VistaOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          VistaOT aux = (VistaOT) lista2.get(j);
          if (visOT.getNombre().equals(aux.getNombre()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(visOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanVis: " + e);      
      bw.escribeError("Error en faltanVis: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanVis");
    return resultado;
  }
  /** Devuelvo la lista de vistas que están en la segunda lista, pero no en la primera */
  private ArrayList sobranVis(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranVis");
    try
    {
      VistaOT visOT = null;
      for (int i = 0; i < lista2.size(); i++)
      {
        visOT = (VistaOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          VistaOT aux = (VistaOT) lista1.get(j);
          if (visOT.getNombre().equals(aux.getNombre()))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(visOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranVis: " + e);      
      bw.escribeError("Error en sobranVis: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranVis");
    return resultado;
  }
  /** Devuelvo la lista de vistas que están diferentes */
  private ArrayList difVis(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en difVis");
    try
    {
      VistaOT visOT = null;
      VistaOT aux = null;
      for (int i = 0; i < lista2.size(); i++)
      {
        visOT = (VistaOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (VistaOT) lista1.get(j);
          if (visOT.getNombre().equals(aux.getNombre()))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (! visOT.getTexto().equals(aux.getTexto()))
            resultado.add(visOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difVis: " + e);      
      bw.escribeError("Error en difVis: " + e);
      throw e;            
    }
    System.out.println("Salimos de difVis");
      
    return resultado;
  }
  /** Compara los permisos de los esquemas */
  private void permisos() throws Exception
  {
    System.out.println("entramos en permisos");
    try
    {
      ArrayList listaDes = cogeListaPer(brDes);
      ArrayList listaOtro = cogeListaPer(brOtro); 
      
      ArrayList faltan = faltanPer(listaDes, listaOtro);
      ArrayList sobran = sobranPer(listaDes, listaOtro);
      ArrayList dif = difPer(listaDes, listaOtro);
      
      for (int i = 0; i < faltan.size(); i++)
      {
        PermisoOT perOT = (PermisoOT) faltan.get(i);
        bw.escribeLinea("Hay que crear el permiso: " + perOT.getPrivilegio() + " sobre " + perOT.getPropietario() + "." + perOT.getTabla());
      }
      for (int i = 0; i < sobran.size(); i++)
      {
        PermisoOT perOT = (PermisoOT) sobran.get(i);
        bw.escribeLinea("Hay que eliminar el permiso: " + perOT.getPrivilegio() + " sobre " + perOT.getPropietario() + "." + perOT.getTabla());
      }
      for (int i = 0; i < dif.size(); i++)
      {
        PermisoOT perOT = (PermisoOT) dif.get(i);
        bw.escribeLinea("Hay que modificar el permiso: " + perOT.getPrivilegio() + " sobre " + perOT.getPropietario() + "." + perOT.getTabla());
      }
      
    }
    catch (Exception e)
    {
      System.out.println("Error en permisos: " + e);      
      bw.escribeError("Error en permisos: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanCons");

    
  }
    /** Devuelve la lista de permisos del fichero pasado por parámetro */
    private ArrayList cogeListaPer(BufferedReader br) throws Exception
    {
      final String permisos = "<PERMISOS>";
      final String permiso = "<PERMISO>";
      final String grantor = "<GRANTOR>";
      final String propietario = "<OWNER>";
      final String tabla = "<TABLE_NAME>";
      final String grantable = "<GRANTABLE>";
      final String privilegio = "<PRIVILEGE>";
      
      boolean apartadoVacio = false;
      
      ArrayList resultado = new ArrayList();
      
    System.out.println("entramos en cogeListaPer");
    String linea = "";
    try
    {
      
      
      // Avanzo hasta la sección de permisos
      while (linea.indexOf(permisos) == -1 && !apartadoVacio)
      {
        linea = br.readLine();
        if (linea.indexOf(Utiles.fAutotermina(permisos)) != -1)
          apartadoVacio = true;
      }
      if (!apartadoVacio)
      {
        // Paso al primer apartado de un permiso
        linea = br.readLine();
        while (linea.indexOf(Utiles.fin(permisos)) == -1)
        {
          PermisoOT perOT = new PermisoOT();
          // Paso a la línea del nombre del grantor
          linea = br.readLine();
          perOT.setGrantor(Utiles.extraeEntre(grantor, linea,Utiles.fin(grantor)));
          
          //Paso a la línea con el propietario
          linea = br.readLine();
          perOT.setPropietario(Utiles.extraeEntre(propietario, linea,Utiles.fin(propietario)));
          // Paso a línea con la tabla
          linea = br.readLine();
          perOT.setTabla(Utiles.extraeEntre(tabla, linea,Utiles.fin(tabla)));
          // Paso a línea con lo de Grantable
          linea = br.readLine();
          perOT.setGrantable(Utiles.extraeEntre(grantable, linea,Utiles.fin(grantable)));
          // Paso a línea con el privilegio
          linea = br.readLine();
          perOT.setPrivilegio(Utiles.extraeEntre(privilegio, linea,Utiles.fin(privilegio)));
            
          resultado.add(perOT);
            
          // Avanzo para salir del apartado de este índice
          linea = br.readLine();linea = br.readLine();        
        }
      }  
    }
    catch (Exception e)
    {
      System.out.println("Error en cogeListaPer: " + e);      
      bw.escribeError("Error en cogeListaPer: " + e);
      throw e;            
    }
    System.out.println("Salimos de cogeListaPer");
      return resultado;
    }  
  
  /** Devuelvo la lista de permisos que están en la primera lista, pero no en la segunda */
  private ArrayList faltanPer(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanPer");
    try
    {
      PermisoOT perOT = new PermisoOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        perOT = (PermisoOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          PermisoOT aux = (PermisoOT) lista2.get(j);
          if (Utiles.perIgual(perOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(perOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanPer: " + e);      
      bw.escribeError("Error en faltanPer: " + e);
      throw e;            
    }
    System.out.println("Salimos de faltanPer");
    return resultado;
  }
  /** Devuelvo la lista de permisos que están en la segunda lista, pero no en la primera */
  private ArrayList sobranPer(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranPer");
    try
    {
      PermisoOT perOT = new PermisoOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        perOT = (PermisoOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          PermisoOT aux = (PermisoOT) lista1.get(j);
          if (Utiles.perIgual(perOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(perOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranPer: " + e);      
      bw.escribeError("Error en sobranPer: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranPer");
    return resultado;
  }
  /** Devuelvo la lista de permisos que varían en estructura entre la primera lista y la segunda */
  private ArrayList difPer(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en difPer");
    try
    {
      PermisoOT aux = null;
      PermisoOT perOT = new PermisoOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        perOT = (PermisoOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          aux = (PermisoOT) lista1.get(j);
          if (Utiles.perIgual(perOT, aux))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difPer(perOT, aux))
            resultado.add(perOT);        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difPer: " + e);      
      bw.escribeError("Error en difPer: " + e);
      throw e;            
    }
    System.out.println("Salimos de difPer");

    return resultado;
  }
      
      

    /** Devuelve la lista de parámetros del fichero pasado por parámetro */
    private ArrayList cogeListaPar(BufferedReader br) throws Exception
    {
      final String parametros = "<PARAMETROS>";
      final String parametro = "<PARAMETRO>";
      final String nombreParametro = "<NOMBRE>";
      final String valorPar = "<VALOR>";
      
      ArrayList resultado = new ArrayList();
      
      System.out.println("Entramos en cogeListaPar");
      

      boolean apartadoVacio = false;
      
      String linea = "";
      
      try
      {
        // Avanzo hasta la sección de parámetros
        while (linea.indexOf(parametros) == -1 && !apartadoVacio)
        {
          linea = br.readLine();
          if (linea.indexOf(Utiles.fAutotermina(parametros)) != -1)
            apartadoVacio = true;
        }
        if (!apartadoVacio)
        {
          // Paso al primer apartado de un parámetro
          linea = br.readLine();
          while (linea.indexOf(Utiles.fin(parametros)) == -1)
          {
            ParametroOT parOT = new ParametroOT();
            
            // Paso a la línea del nombre del parámetro
            linea = br.readLine();            
            
            // Extraigo el nombre del parámetro
            parOT.setNombre(Utiles.extraeEntre(nombreParametro, linea,Utiles.fin(nombreParametro)));        
            
            //Paso a la línea con el valor del parámetro
            linea = br.readLine();
            parOT.setValor(Utiles.extraeEntre(valorPar, linea,Utiles.fin(valorPar)));
        
            resultado.add(parOT);
        
            // Avanzo para salir del apartado de este parámetro
              linea = br.readLine();linea = br.readLine();        
          }
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en cogeListaPar" + e);
        bw.escribeError("Error en cogeListaPar: " + e);
        throw e;
      }
      System.out.println("Salimos de cogeListaPar");
      return resultado;
    }


  /** Devuelvo la lista de parámetros que están en la primera lista, pero no en la segunda */
  private ArrayList faltanPar(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en faltanPar");
    try
    {
      ParametroOT parOT = new ParametroOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        parOT = (ParametroOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          ParametroOT aux = (ParametroOT) lista2.get(j);
          if (Utiles.parIgual(parOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(parOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en faltanPar: " + e);      
      bw.escribeError("Error en faltanPar: " + e + Utiles.retCarro);
      throw e;            
    }
    System.out.println("Salimos de faltanPar");
    return resultado;
  }


  /** Devuelvo la lista de parámetros que están en la segunda lista, pero no en la primera */
  private ArrayList sobranPar(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en sobranPar");
    try
    {
    
      ParametroOT parOT = new ParametroOT();
      for (int i = 0; i < lista2.size(); i++)
      {
        parOT = (ParametroOT) lista2.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista1.size() && !enc)
        {
          ParametroOT aux = (ParametroOT) lista1.get(j);
          if (Utiles.parIgual(parOT, aux))
            enc = true;
          else
            j++;
        }
        if (!enc)
        {
          resultado.add(parOT);
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en sobranPar: " + e);      
      bw.escribeError("Error en sobranPar: " + e);
      throw e;            
    }
    System.out.println("Salimos de sobranPar");
      
    return resultado;
  }    
  
  /** Devuelvo la lista de parámetros que varían en valor entre la primera lsita y la segunda */
  private ArrayList difPar(ArrayList lista1, ArrayList lista2) throws Exception
  {
    ArrayList resultado = new ArrayList();
    System.out.println("entramos en difPar");
    try
    {
    
      ParametroOT aux = null;
      ParametroOT parOT = new ParametroOT();
      for (int i = 0; i < lista1.size(); i++)
      {
        parOT = (ParametroOT) lista1.get(i);
        boolean enc = false;
        int j = 0;
        while (j < lista2.size() && !enc)
        {
          aux = (ParametroOT) lista2.get(j);
          if (Utiles.parIgual(parOT, aux))
            enc = true;
          else
            j++;
        }
        if (enc)
        {
          if (Utiles.difPar(parOT, aux))
            resultado.add(parOT);        
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error en difPar: " + e);      
      bw.escribeError("Error en difPar: " + e);
      throw e;            
    }
    System.out.println("Salimos de difPar");
    
    return resultado;
  }  
  
    /** Abre la lista de procesos para tenerlos todos apuntados */
    private ArrayList procesos(BufferedReader br) throws Exception
    {
      System.out.println("Entramos en procesos");
      ArrayList resultado = new ArrayList();
      String linea = "";
      try
      {
        String procesos = "<funciones>";
        String proceso = "<funcion ";
        
        String nombreFuncion = "";
        boolean fin = false;
        
        // Avanzamos hasta la sección de procesos
        while (linea.indexOf(procesos) == -1 && !fin)
        {
          linea = br.readLine();
          if (linea.indexOf(Utiles.fin(procesos)) != -1)
            fin = true;
        }
        if (!fin)
        // hay sección de procesos
        {

          while (linea.indexOf(Utiles.fin(procesos)) == -1)
          // Recorremos toda la sección de procesos
          {
            // Avanzamos hasta el siguiente proceso
            while (linea.indexOf(proceso) == -1)
              linea = br.readLine();
          
            // linea contiene el nombre de un proceso
            ProcesoOT pOT = new ProcesoOT(linea);
            
            resultado.add( pOT);
          
            // Salgo del apartado de la tabla que acabo de leer
            linea = br.readLine();
            System.out.println(linea);
          }
        }

      System.out.println("Salimos de procesos");
      return resultado;
    }
      
    catch (Exception e)
      {
        System.out.println("Error en procesos: " + e);
        
        bw.escribeError("Error en procesos: " + e);
        throw e;
      }

      
    }


    /** Compara la lista de parámetros de los dos entornos */
    private void parametros() throws Exception
    {
      System.out.println("Entramos en parametros");
      try
      {
        ArrayList listaDes = cogeListaPar(brDes);
        ArrayList listaOtro = cogeListaPar(brOtro); 



        ArrayList faltanPars = faltanPar(listaDes, listaOtro);
        ArrayList sobranPars = sobranPar(listaDes, listaOtro);
        ArrayList cambianPars = difPar(listaDes, listaOtro);
        
        for (int i = 0; i < sobranPars.size(); i++)
        {
          ParametroOT parOT = (ParametroOT) sobranPars.get(i);
          bw.escribeLinea("Hay que eliminar el parámetro: " + parOT.getNombre());
        }
        
        for (int i = 0; i < faltanPars.size(); i++)
        {
          ParametroOT parOT = (ParametroOT) faltanPars.get(i);
          bw.escribeLinea("Hay que añadir el parámetro: " + parOT.getNombre() + ". Valor: " + parOT.getValor());
        }
        
        for (int i = 0; i < cambianPars.size(); i++)
        {
          ParametroOT parOT = (ParametroOT) cambianPars.get(i);
          bw.escribeLinea("Hay que cambiar el valor del parámetro: " + parOT.getNombre() + ". Debe tener el valor " + parOT.getValor());
        }
      }
      catch (Exception e)
      {
        System.out.println("Error en parametros" + e);
        bw.escribeError("Error en columnas: " + e);
        throw e;
      }
      System.out.println("Salimos de parametros");
        
      
    }  
          
}
