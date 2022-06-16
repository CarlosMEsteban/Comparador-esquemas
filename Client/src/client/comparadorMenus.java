package client;

import client.ot.OrdenadoOT;
import client.ot.ProcesoOT;
import client.ot.RolOT;

import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JProgressBar;


public class comparadorMenus 
{
    BufferedReader brDes = null;
    BufferedReader brOtro = null;
    ArrayList rolesFaltan = new ArrayList();
    ArrayList rolesSobran = new ArrayList();
    ArrayList asigFaltan = new ArrayList();
    ArrayList asigSobran = new ArrayList();
  JProgressBar jProgressBar1;
  Logger bw;
  
  public comparadorMenus(JProgressBar jPB, Logger bWriter)    
  {
    jPB.setStringPainted(true);      
    jProgressBar1 = jPB;    
    bw = bWriter;
  }
    public void comparaMenus(String menusDes, String menusOtro) throws Exception
    {
      System.out.println("//////////////////////////Comenzamos menus//////////////////////////");
      bw.escribeEncabezado("Comenzamos menus");
      
      try
      {
        
        brDes = Utiles.abreParaLeer(menusDes);
        brOtro = Utiles.abreParaLeer(menusOtro); 
        
        int pct = 0;jProgressBar1.setToolTipText("Procesos");jProgressBar1.setValue(pct);
        
        
        System.out.println("Vamos a sacar la lista de procesos de desarrollo");
        ArrayList todosProcesosDes = procesos(brDes);
        System.out.println("Vamos a sacar la lista de procesos del otro entorno");
        ArrayList todosProcesosOtro = procesos(brOtro);
        
        
        System.out.println("Vamos a sacar la lista de asignaciones");
        ArrayList rolesDes = roles(brDes, todosProcesosDes);
        ArrayList rolesOtro = roles(brOtro, todosProcesosOtro);
        
        System.out.println("Ordenamos las listas de roles");
        ArrayList rolesDesOrdenada = ordenaRoles(rolesDes);
        ArrayList rolesOtroOrdenada = ordenaRoles(rolesOtro);
        
        System.out.println("Ordenamos las listas de asignaciones");
        for (int i = 0; i < rolesDesOrdenada.size(); i++)
        {
          RolOT aux = (RolOT) rolesDesOrdenada.get(i);
          ArrayList lAsigSinOrdenar = aux.getLProcesos();
          ArrayList lAsigOrdenadas = ordenaAsig(lAsigSinOrdenar);          
          aux.setLProcesos(lAsigOrdenadas);
        }
        for (int i = 0; i < rolesOtroOrdenada.size(); i++)
        {
          RolOT aux = (RolOT) rolesOtroOrdenada.get(i);
          ArrayList lAsigSinOrdenar = aux.getLProcesos();
          ArrayList lAsigOrdenadas = ordenaAsig(lAsigSinOrdenar);          
          aux.setLProcesos(lAsigOrdenadas);
        }
        
        System.out.println("Comparamos");
        compara(rolesDesOrdenada, rolesOtroOrdenada);

        System.out.println("Escribimos las diferencias");
        escribeFaltasYSobras();
      
        
        System.out.println("Cerramos los ficheros");
        
        brDes.close();
        brOtro.close();
        
                
        
        jProgressBar1.setToolTipText("fin");jProgressBar1.setValue(100);
      }
      catch (Exception e)
      {
        System.out.println("Error en comparaMenus: " + e);
        
        bw.escribeLinea("Error en comparaMenus: " + e);
        brDes.close();
        brOtro.close();
        bw.close();
      }

        System.out.println("//////////////////////////TERMINAMOS BD//////////////////////////");
      
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
        
        bw.escribeLinea("Error en procesos: " + e);
        throw e;
      }

      
    }          
    
    /** Devuelve la lista de roles con sus correspondientes procesos*/
    private ArrayList roles(BufferedReader br, ArrayList lProcesos) throws Exception
    {
      System.out.println("Entramos roles");
      ArrayList resultado = new ArrayList();
      try
      {
        String ROLES_FUNCIONES = "<rolesFunciones>";
        String ASIGNACION = "<asignacion";
        String linea = "";
        boolean fin = false;
        // Avanzamos hasta la sección de asignaciones
        while (linea.indexOf(ROLES_FUNCIONES) == -1 && !fin)
        {
          linea = br.readLine();
          if (linea.indexOf(Utiles.fin(ROLES_FUNCIONES)) != -1)
            fin = true;
        }
        if (!fin)
        // hay sección de asignaciones
        {
          while (linea.indexOf(Utiles.fin(ROLES_FUNCIONES)) == -1)
          // Recorremos toda la sección de asignaciones
          {
            // Avanzamos hasta la siguiente asignación
            while (linea.indexOf(ASIGNACION) == -1)
              linea = br.readLine();
          
            String cRol = Utiles.valorAtributo(linea, "c_grupo_id");
            // buscamos el rol en la lista de roles
              RolOT rOT = busca(resultado, cRol);
            
            // Cogemos el identificador del proceso
            String cProcesoId = Utiles.valorAtributo(linea, "c_funcion_id");
            
            // Buscamos la información sobre el proceso, para usar el dProceso
            ProcesoOT aux = buscaProceso(lProcesos, cProcesoId);
            
            // Creamos una copia del proceso de la lista
            ProcesoOT pOT = new ProcesoOT(aux);
            
            // Comprobamos que el proceso es, para el rolo,  una opción del menú
            String esMenu = Utiles.valorAtributo(linea, "c_menu_id");
            if (esMenu.equals("S"))
              pOT.setMenu(true);
            else
              pOT.setMenu(false);

            
            rOT.getLProcesos().add(pOT);
            
            
          
            // Salgo del apartado de la tabla que acabo de leer
            linea = br.readLine();
          }
        }
      }
      catch (Exception e)
        {
          System.out.println("Error en roles: " + e);
          
          bw.escribeLinea("Error en roles: " + e);
          throw e;
        }
      System.out.println("Salimos roles");
      return resultado;
      
    }


/** Ordena la lista de roles pasada por parámetro */
private ArrayList ordenaRoles(ArrayList lista) throws Exception
  {
    System.out.println("Entrando ordenaRoles");
    ArrayList listaRolesOrdenada = new ArrayList();

    try 
    {
      int indice = -1;
      ArrayList listaOrdenada = new ArrayList();
      // Meto los roles en una lista de elementos ordenables
      for (int i = 0; i < lista.size(); i++)
      {
        RolOT rOT = (RolOT) lista.get(i);
        OrdenadoOT oOT = new OrdenadoOT();
        oOT.setIndice(i);
        oOT.setOrden(rOT.getCGrupoId());
        listaOrdenada.add(oOT);
      }
      
      // Ordeno la lista
      Collections.sort(listaOrdenada);
      
      // Creo una nueva lista con los roles, pero ordenados
      for (int i = 0; i < listaOrdenada.size(); i++)
      {
        OrdenadoOT oOT = (OrdenadoOT) listaOrdenada.get(i);
        listaRolesOrdenada.add(lista.get(oOT.getIndice()));      
      }
      
      
    } 
    catch (Exception e)
      {
        System.out.println("Error en ordenaRoles: " + e);
        
        bw.escribeLinea("Error en ordenaRoles: " + e);
        throw e;
      }
    System.out.println("Saliendo de ordenaRoles");
    return listaRolesOrdenada;
  }
        


  /** Ordena la lista de asignaciones pasada por parámetro */
  private ArrayList ordenaAsig(ArrayList lista) throws Exception
    {
      System.out.println("Entrando ordenaAsig");
      ArrayList listaAsigOrdenada = new ArrayList();

      try 
      {
        int indice = -1;
        ArrayList listaOrdenada = new ArrayList();
        // Meto las asignaciones en una lista de elementos ordenables
        for (int i = 0; i < lista.size(); i++)
        {
          ProcesoOT pOT = (ProcesoOT) lista.get(i);
          OrdenadoOT oOT = new OrdenadoOT();
          oOT.setIndice(i);
          oOT.setOrden(pOT.getCFuncionId());
          listaOrdenada.add(oOT);
        }
        
        // Ordeno la lista
        Collections.sort(listaOrdenada);
        
        // Creo una nueva lista con los procesos, pero ordenados
        for (int i = 0; i < listaOrdenada.size(); i++)
        {
          OrdenadoOT oOT = (OrdenadoOT) listaOrdenada.get(i);
          listaAsigOrdenada.add(lista.get(oOT.getIndice()));      
        }
        
        
      } 
      catch (Exception e)
        {
          System.out.println("Error en ordenaAsig: " + e);
          
          bw.escribeLinea("Error en ordenaAsig: " + e);
          throw e;
        }
      System.out.println("Saliendo de ordenaAsig");
      return listaAsigOrdenada;
    }


    private void compara(ArrayList lDes, ArrayList lOtro)  throws Exception
    {
      System.out.println("Entrando compara");
      try 
      {
        /* Busco los roles y asignaciones que están en Des y no están en el Otro */
        for (int i = 0; i < lDes.size(); i++)
        // Recorro todos los roles de desarrollo
        {
          RolOT rDesOT = (RolOT) lDes.get(i);
          // Busco este rol en el Otro
          boolean enc = false;
          int  j;
          for (j = 0; j < lOtro.size() && ! enc; j++)
          {
            RolOT rOtroOT = (RolOT) lOtro.get(j);
            if (rOtroOT.getCGrupoId().equals(rDesOT.getCGrupoId()))
              enc = true;
          }
          if (enc)
          // El rol de Des se ha encontrado en el Otro
          {
            RolOT rOtroOT = (RolOT) lOtro.get(j - 1);            
            /* Busco las asignaciones que hay en des que no están en Otro */
            
            for (int k = 0; k < rDesOT.getLProcesos().size(); k++)
            // Recorremos todos los procesos que tiene el rol de Des actual
            {
              ProcesoOT pOT = (ProcesoOT) rDesOT.getLProcesos().get(k);
              
              // Busco esta signación en el Otro
              int l;
              enc = false;
              for (l = 0; l < rOtroOT.getLProcesos().size() & !enc; l++)
              // Recorro todas las asignaciones en el otro
              {
                ProcesoOT pOtroOT = (ProcesoOT) rOtroOT.getLProcesos().get(l);
                if (pOT.getDProceso().equals(pOtroOT.getDProceso()))
                  enc = true;                
              }
              if (!enc)
              {
                if (pOT.isMenu())
                  asigFaltan.add("Hay que añadir al rol '" + rDesOT.getCGrupoId() + "' la función de menú '" + pOT. getDProceso() +  "'(" + pOT.getCFuncionId() + ")");
                else
                  asigFaltan.add("Hay que añadir al rol '" + rDesOT.getCGrupoId() + "' la función '" + pOT. getDProceso() + "'(" + pOT.getCFuncionId() + ")");
              }
            }            
          }
          else
          // el rol Des no se ha encontrado en el Otro
          {
            rolesFaltan.add("HAY QUE AÑADIR EL ROL '" + rDesOT.getCGrupoId());
          }

        }
        
        
        
        
        
        /* Busco los roles y asignaciones que están en Otro y no están en Des */
        for (int i = 0; i < lOtro.size(); i++)
        // Recorro todos los roles del otro
        {
          RolOT rOtroOT = (RolOT) lOtro.get(i);
          // Busco este rol en Des
          boolean enc = false;
          int  j;
          for (j = 0; j < lDes.size() && ! enc; j++)
          {
            RolOT rDesOT = (RolOT) lDes.get(j);
            if (rOtroOT.getCGrupoId().equals(rDesOT.getCGrupoId()))
              enc = true;
          }
          if (enc)
          // El rol de Otro se ha encontrado en Des
          {
            RolOT rDesOT = (RolOT) lDes.get(j - 1);            
            /* Busco las asignaciones que hay en otro que no están en Des */            
            for (int k = 0; k < rOtroOT.getLProcesos().size(); k++)
            // Recorremos todos los procesos que tiene el rol de Otro actual
            {
              ProcesoOT pOT = (ProcesoOT) rOtroOT.getLProcesos().get(k);
              
              // Busco esta signación en Des
              int l;
              enc = false;
              for (l = 0; l < rDesOT.getLProcesos().size() & !enc; l++)
              // Recorro todas las asignaciones en Des
              {
                ProcesoOT pDesOT = (ProcesoOT) rDesOT.getLProcesos().get(l);
                if (pOT.getDProceso().equals(pDesOT.getDProceso()))
                  enc = true;                
              }
              if (!enc)
              {
                if (pOT.isMenu())
                  asigSobran.add("Hay que borrar del rol '" + rOtroOT.getCGrupoId() + "' la función de menú '" + pOT. getDProceso() + "'(" + pOT.getCFuncionId() + ")");
                else
                  asigSobran.add("Hay que borrar del rol '" + rOtroOT.getCGrupoId() + "' la función '" + pOT. getDProceso() + "'(" + pOT.getCFuncionId() + ")");
              }
            }            
          }
          else
          // el rol Otro no se ha encontrado en Des
          {
            rolesSobran.add("HAY QUE BORRAR EL ROL '" + rOtroOT.getCGrupoId());
          }

        }
        
      } 
      catch (Exception e)
        {
          System.out.println("Error en compara: " + e);
          
          bw.escribeLinea("Error en compara: " + e);
          throw e;
        }
      System.out.println("Saliendo de compara");
      
    }    
    
    /** Escribe el contenido de los array de faltas y sobras */
    private void escribeFaltasYSobras() throws Exception
    {
      System.out.println("Entrando escribeFaltasYSobras");
      try 
      {
        bw.escribeEncabezado("Roles que faltan");
        for (int i = 0; i < this.rolesFaltan.size(); i++)
          bw.escribeLinea((String) rolesFaltan.get(i));
        bw.escribeEncabezado("Asignaciones que faltan");
        for (int i = 0; i < this.asigFaltan.size(); i++)
          bw.escribeLinea((String) asigFaltan.get(i));
        bw.escribeEncabezado("Roles que sobran");
        for (int i = 0; i < this.rolesSobran.size(); i++)
          bw.escribeLinea((String) rolesSobran.get(i));
        bw.escribeEncabezado("asignaciones que sobran");
        for (int i = 0; i < this.asigSobran.size(); i++)
          bw.escribeLinea((String) asigSobran.get(i));
        
      } 
      catch (Exception e)
        {
          System.out.println("Error en escribeFaltasYSobras: " + e);
          
          bw.escribeLinea("Error en escribeFaltasYSobras: " + e);
          throw e;
        }
      System.out.println("Saliendo de escribeFaltasYSobras");
      
    }    
    
    /** Busca un rol en la lista de roles */
    private RolOT busca(ArrayList lRoles, String cRol) throws Exception
    {
      System.out.println("Entramos en busca");
      RolOT resultado = null;
      try
      {
        boolean enc = false;
        int i;
        for (i = 0; i  < lRoles.size() && !enc; i++)
        {
          RolOT aux = (RolOT) lRoles.get(i);
          if (aux.getCGrupoId().equals(cRol))
            enc = true;
        }
        if (enc)
          resultado = (RolOT) lRoles.get(i - 1);
        else
        {
          resultado = new RolOT();
          resultado.setCGrupoId(cRol);
          lRoles.add(resultado);
        }
      }
      catch (Exception e)
        {
          System.out.println("Error en busca: " + e);
          
          bw.escribeError("Error en busca: " + e);
          throw e;
        }
      System.out.println("salimos de busca");
      return resultado;
    }


    /** Devuelve el procesoOT que corresponde al id */
    private ProcesoOT buscaProceso(ArrayList lProcesos, String cProcesoId) throws Exception
    {
      System.out.println("Entramos en buscaProceso");
      ProcesoOT resultado = null;
      try
      {
        boolean enc = false;
        int i = 0;
        for (i = 0; i < lProcesos.size() && !enc; i++)
        {
          ProcesoOT aux = (ProcesoOT) lProcesos.get(i);
          if (aux.getCFuncionId().equals(cProcesoId))
            enc = true;            
        }
        if (enc)
          resultado = (ProcesoOT) lProcesos.get(i - 1);
          
        }          
        catch (Exception e)
          {
            System.out.println("Error en buscaProceso: " + e);
            
            bw.escribeError("Error en buscaProceso: " + e);
            throw e;
          }
        System.out.println("Entramos en buscaProceso");
        return resultado;
      }
          
}
