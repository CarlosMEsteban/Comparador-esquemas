package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;


/**
 * Clase objeto transferencia para las columnas
 */
public class SeleccionFicheros implements ActionListener 
{
  private JFileChooser chooser = new JFileChooser();
  private JFrame frame;
  private JFormattedTextField campoConFichero;
  public SeleccionFicheros(JFrame frame, JFormattedTextField campoConFichero)
  {
    this.frame = frame;
    this.campoConFichero = campoConFichero;
  }
  
  public void actionPerformed(ActionEvent ae) 
  {
    String textButton = ae.getActionCommand();
    String dialogTitle = "Abrir un fichero";

    if (textButton.equals("Guardar"))
      dialogTitle = "Guardar un fichero";

    chooser.setDialogTitle(dialogTitle);
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int sel = chooser.showOpenDialog(frame);
    if (sel == JFileChooser.APPROVE_OPTION)
    {
      File selectedFile = chooser.getSelectedFile();
      //JOptionPane.showMessageDialog(frame,selectedFile.getAbsolutePath());
      campoConFichero.setText(selectedFile.getAbsolutePath());
    }
    else
    {
      return;
    }
  }
}