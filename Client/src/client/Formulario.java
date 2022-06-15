package client;



import client.comparador;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class Formulario extends JFrame {
  
  private JFormattedTextField directorio = new JFormattedTextField();


  private JButton compara = new JButton();
  private JFormattedTextField bDDes = new JFormattedTextField();
  private JFormattedTextField bDOtro = new JFormattedTextField();
  private JFormattedTextField menusDes = new JFormattedTextField();
  private JFormattedTextField menusOtro = new JFormattedTextField();
  private JFormattedTextField ficheroRes = new JFormattedTextField();
  
  
  
  /** Nuevo INI */
  private SeleccionDirectorios sfDirectorio = new SeleccionDirectorios(this, directorio);
  private JButton botonDirectorio = new JButton();
  
  private JButton botonBDDes = new JButton();
  private JButton botonBDOtro = new JButton();
  private JButton botonRes = new JButton();
  private SeleccionFicheros sfBDDes = new SeleccionFicheros(this, bDDes);
  private SeleccionFicheros sfBDOtro = new SeleccionFicheros(this, bDOtro);
  private SeleccionFicheros sfMenusDes = new SeleccionFicheros(this, menusDes);
  private SeleccionFicheros sfMenusOtro = new SeleccionFicheros(this, menusOtro);
  private SeleccionFicheros resultado = new SeleccionFicheros(this, ficheroRes);
  private JProgressBar jProgressBar1 = new JProgressBar();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JButton botonMenusDes = new JButton();
  private JButton botonMenusOtro = new JButton();
/** Nuevo fin */

    public Formulario() {
        try {
            jbInit();
        } catch (Exception e) {
            System.out.println("Error en formulario: " + e.getMessage());

            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception 
    {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(684, 627));
        this.setTitle("Compara esquemas");

      botonDirectorio.setText("SeleccDirectorio");    
      botonDirectorio.setBounds(new Rectangle(545, 35, 100, 25));
      botonDirectorio.addActionListener(sfDirectorio);
      

    
      botonBDDes.setText("SeleccDes");    
      botonBDDes.setBounds(new Rectangle(540, 215, 100, 25));
        
      botonBDOtro.setText("SeleccOtro");    
      botonBDOtro.setBounds(new Rectangle(540, 250, 100, 25));
      
      botonRes.setText("SeleccRes");    
      botonRes.setBounds(new Rectangle(540, 475, 100, 25));

      botonBDDes.addActionListener(sfBDDes);
      botonBDOtro.addActionListener(sfBDOtro);
      botonMenusDes.addActionListener(sfMenusDes);
      botonMenusOtro.addActionListener(sfMenusOtro);
      botonRes.addActionListener(resultado);
      
      jProgressBar1.setBounds(new Rectangle(25, 560, 515, 15));
      jProgressBar1.setMaximum(100);
      jProgressBar1.setMinimum(0);
    jLabel1.setText("BASES DE DATOS");
    jLabel1.setBounds(new Rectangle(15, 150, 625, 45));
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setPreferredSize(new Dimension(93, 35));
    jLabel1.setFont(new Font("Serif", Font.PLAIN, 24));
    jLabel2.setText("MENÚS Y FUNCIONES");
    jLabel2.setBounds(new Rectangle(5, 305, 625, 45));
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setPreferredSize(new Dimension(93, 35));
    jLabel2.setFont(new Font("Serif", Font.PLAIN, 24));
    menusDes.setBounds(new Rectangle(25, 350, 520, 25));
    menusOtro.setBounds(new Rectangle(25, 395, 520, 25));
    botonMenusDes.setText("SeleccDes");
    botonMenusDes.setBounds(new Rectangle(545, 350, 100, 25));
    botonMenusOtro.setText("SeleccOtro");
    botonMenusOtro.setBounds(new Rectangle(550, 395, 100, 25));
      
      
        compara.setText("Compara");
        compara.setBounds(new Rectangle(200, 520, 175, 35));
        compara.setActionCommand("Compara");
        compara.addActionListener(new ActionListener() 
        {
                public void actionPerformed(ActionEvent e) 
                {
                try
                {
                    compara_actionPerformed(e);
                   }catch  (Exception ex)
                {
                  ex.printStackTrace();
                }
                }
            });
    
    directorio.setBounds(new Rectangle(15, 35, 520, 25));

    bDDes.setBounds(new Rectangle(20, 215, 520, 25));
    bDOtro.setBounds(new Rectangle(15, 250, 520, 25));
    ficheroRes.setBounds(new Rectangle(15, 475, 520, 25));

    this.getContentPane().add(botonDirectorio, null);
    this.getContentPane().add(directorio, null);
    this.getContentPane().add(botonMenusOtro, null);
    this.getContentPane().add(botonMenusDes, null);
    this.getContentPane().add(menusOtro, null);
    this.getContentPane().add(menusDes, null);
    this.getContentPane().add(jLabel2, null);
    this.getContentPane().add(jLabel1, null);
    this.getContentPane().add(jProgressBar1, null);
    this.getContentPane().add(botonBDDes, null);
    
    this.getContentPane().add(botonBDOtro, null);
    this.getContentPane().add(botonRes, null);
    this.getContentPane().add(bDDes, null);
    this.getContentPane().add(bDOtro, null);
    this.getContentPane().add(ficheroRes, null);
    this.getContentPane().add(compara, null);


  }
    private void compara_actionPerformed(ActionEvent e) throws Exception
    {
        //String fichDes = "C:" + File.separator + "J2EE" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "estructuraDES.xml";
        //String otroFich = "C:" + File.separator + "J2EE" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "estructuraEXP.xml";
        //String a = ficheroDes.getText();
        
        if (!directorio.getText().equals(""))
        {
          bDDes.setText(directorio.getText() + File.separator + "estructuraDES.xml");
          bDOtro.setText(directorio.getText() + File.separator +"estructuraPRU.xml");
          menusDes.setText(directorio.getText() + File.separator + "MenusDES.xml");
          menusOtro.setText(directorio.getText() + File.separator + "MenusTST.xml");
          ficheroRes.setText(directorio.getText() + File.separator + "resultado.txt");          
        }
        else
        {
          if (bDDes.getText().equals(""))
            bDDes.setText("c:" + File.separator + "j2ee" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "estructuraDES.xml");
          if (bDOtro.getText().equals(""))
            bDOtro.setText("c:" + File.separator + "j2ee" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator +"estructuraPRU.xml");
          if (menusDes.getText().equals(""))
            menusDes.setText("c:" + File.separator + "j2ee" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "Des.xml");
          if (menusOtro.getText().equals(""))
            menusOtro.setText("c:" + File.separator + "j2ee" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "Pru.xml");
          if (ficheroRes.getText().equals(""))
            ficheroRes.setText("c:" + File.separator + "j2ee" + File.separator + "Compara esquemas" + File.separator + "Client" + File.separator + "Ejemplos" + File.separator + "resultado.txt");
        }          

        comparador comp = new comparador(jProgressBar1);
        comp.compara(bDDes.getText(), bDOtro.getText(), menusDes.getText(), menusOtro.getText(),  ficheroRes.getText());
        //comp.compara(fichDes, otroFich);
    }

 

    
}

