package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: abr
 * Date: 8/16/13
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileReader implements Task {


    /** constants used in computation */
    static Logger log = Logger.getLogger(FileReader.class.getName());


    private String fileName;
    private String captureStorage = Constants.DEFAULT_RECORDSDIR;


    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
    public FileReader(
            String fileName) {
        this.fileName = fileName;
        log.debug("File directory: " + captureStorage);
        log.debug("File Name: " + fileName);
    }

    public Object execute() {
        log.debug("trying to control the Recorder...");
        return readFile();
    }

    private String readFile() {
        try {
            File f = new File(captureStorage);
            System.out.println("Directory: " + captureStorage);
            boolean flag =  f.isDirectory();
            if(flag) {
                StringBuilder result = new StringBuilder();
                File commentsFile = new File(f, fileName + ".comments");
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(commentsFile)));
                } catch (FileNotFoundException e) {
                    return "";
                }
                while (true){
                    String line = reader.readLine();
                    if (line == null){
                        break;
                    }
                    result.append(line).append("\n");

                }
                reader.close();
                return result.toString();
            }else {
                return null;
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to write file",e);
        }
    }
}
