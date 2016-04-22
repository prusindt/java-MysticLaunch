/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysticLaunch.util;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;

/**
 *
 * @author IM01DXP
 */
public class RunProcess {
    
    
    private Process p;
    private String exeLoc;
    
    /** Creates a new instance of RunCommand */
    public RunProcess() {
    }
    
    public Process execute(List<String> commandList, String workingDir) {
        exeLoc = commandList.get(0);
        if (! exeLoc.equals("")) 
        {  try 
           {  //p = Runtime.getRuntime().exec(exeLoc);
              ProcessBuilder pb = new ProcessBuilder();
              pb.command(commandList);
              if (workingDir != null)
              {  pb.directory(new File(workingDir));
              }
              p = pb.start();
           }
           catch (IOException ioe) 
           { System.err.println("An error has occurred executing:" + exeLoc);
           }
        }
        else { System.err.println("Must specify an executable file."); }
        return p;
    }
    
}
