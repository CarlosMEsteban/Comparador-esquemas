package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;



public class SeleccionDirectorios implements ActionListener 
{
  private JFileChooser chooser = new JFileChooser();
  private JFrame frame;
  private JFormattedTextField campoConFichero;
  public SeleccionDirectorios(JFrame frame, JFormattedTextField campoConFichero)
  {
    this.frame = frame;
    this.campoConFichero = campoConFichero;
  }
  
  public void actionPerformed(ActionEvent ae) 
  {
    String textButton = ae.getActionCommand();
    String dialogTitle = "Seleccionar un directorio";

    if (textButton.equals("Guardar"))
      dialogTitle = "Guardar un fichero";

    chooser.setDialogTitle(dialogTitle);
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

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