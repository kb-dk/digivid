package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: abr
 * Date: 8/16/13
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileCreator implements Task {


    /** constants used in computation */
    static Logger log = Logger.getLogger(FileCreator.class.getName());


    private String fileName;
    private String captureStorage = Constants.DEFAULT_RECORDSDIR;

    private String contents;

    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
    public FileCreator(
            String fileName,
            String contents) {
        this.fileName = fileName;
        this.contents = contents;
        log.debug("File directory: " + captureStorage);
        log.debug("File Name: " + fileName);
    }


    public FileCreator(File file, String contents){
        fileName = file.getName();
        captureStorage = file.getParentFile().getPath();
        this.contents = contents;
    }

    public Object execute() {
        log.debug("trying to control the Recorder...");
        return fillFile();
    }

    private Object fillFile() {
        try {
            File f = new File(captureStorage);
            System.out.println("Directory: " + captureStorage);
            boolean flag =  f.isDirectory();
            if(flag) {
                File commentsFile = new File(f, fileName + ".comments");
                FileOutputStream outStream = new FileOutputStream(commentsFile);
                outStream.write(contents.getBytes());
                outStream.close();
            }else {
                return false;
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to write file",e);
        }
        return true;
    }


}
