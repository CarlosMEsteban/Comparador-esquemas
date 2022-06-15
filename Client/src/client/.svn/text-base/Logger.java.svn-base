package client;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Logger extends BufferedWriter 
{
  private final String retCarro = "\n";

  public Logger(FileWriter fw)
  {
    super(fw);
  }
  
    public Logger(String fich) throws Exception
    {
        super(new BufferedWriter(new FileWriter (new File (fich))));
    }
  
  public void escribeLinea(String l) throws Exception
  {
    write(l + retCarro);
  }
  
  public void escribeEncabezado(String e) throws Exception
  {
    escribeLinea("---------" + e + "-----------------" + retCarro);
  }


  public void escribeError(String e) throws Exception
  {
    escribeLinea("*************************************************" + e + retCarro);
  }
  
 
  
}