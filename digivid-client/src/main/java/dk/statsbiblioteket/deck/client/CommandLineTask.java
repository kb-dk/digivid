/* File:        $RCSfile: GenericTask.java,v $
 * Revision:    $Revision: 1.3 $
 * Author:      $Author: csr $
 * Date:        $Date: 2007/03/22 13:50:42 $
 *
 * Copyright Det Kongelige Bibliotek og Statsbiblioteket, Danmark
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runs a generic remote task and returns a List<String>
 * containing its output
 * Usage: Task my_task = new GenericTask("ps");
 * List<String> output = (List<String>) my_task.execute();
 *
 * @author csr
 * @since Feb 22, 2007
 */

public class CommandLineTask implements Task {
    static Logger log = Logger.getLogger(CommandLineTask.class.getName());
    private Integer[] returncodes;

    private String unixExecutable;
    private boolean is_daemon;

    public CommandLineTask(String command, boolean is_daemon) {
        if (command == null) {
            throw new RuntimeException("Attempt to create a GenericTask with " +
                    "null executtable");
        }
        unixExecutable = command;
        this.is_daemon = is_daemon;
        returncodes = new Integer[]{0};
    }

    public CommandLineTask(String command) {
        this(command, false);
    }

    public CommandLineTask(String unix_command, boolean is_daemon, Integer[] returncodes) {
        this(unix_command, is_daemon);
        if (returncodes != null){
            this.returncodes = returncodes;
        }
    }

    public Object execute() {
        if (!is_daemon) {
            return execute_nondaemon();
        } else {
            execute_daemon();
            return  new ArrayList<String>();
        }
    }

    public void execute_daemon() {
        try {
            Process  p = Runtime.getRuntime().exec(unixExecutable);
            logOutput(p, log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object execute_nondaemon() {
        List<String> result = new ArrayList<String>();
        log.debug("Command: " + unixExecutable);
        try {
            String s;
            String er;
            Process  p = Runtime.getRuntime().exec(unixExecutable);
            BufferedReader stdInput =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError =
                    new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                result.add(s);
            }
            while ((er = stdError.readLine()) != null) {
                System.out.println(er);
                log.error(er);
            }
            int returncode = p.waitFor();

            if ( ! Arrays.asList(returncodes).contains(returncode)){
                throw new RuntimeException("Error executing " + unixExecutable +", returned "+p.exitValue());
            }
        } catch(Exception e) {
            throw new RuntimeException("Error executing " + unixExecutable, e);
        }
        return result;
    }

    public static void logOutput(final Process p, final Logger log) {
        Runnable standard_output_thread = new Runnable() {
            public void run() {
                BufferedReader stdInput =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s = null;
                try {
                    while (((s = stdInput.readLine()) != null)) {
                        log.debug(s);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } ;

        Runnable standard_error_thread = new Runnable() {
            public void run() {
                BufferedReader stdError =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String s = null;
                try {
                    while (((s = stdError.readLine()) != null)) {
                        log.error(s);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } ;

        (new Thread(standard_output_thread)).start();
        (new Thread(standard_error_thread)).start();

    }


    public String getUnixExecutable() {
        return unixExecutable;
    }

    public void setUnixExecutable(String unixExecutable) {
        this.unixExecutable = unixExecutable;
    }

    public boolean isDaemon() {
        return is_daemon;
    }

    @Override
    public String toString() {
        return "GenericTask{" +
                "unixExecutable='" + unixExecutable + '\'' +
                ", is_daemon=" + is_daemon +
                '}';
    }
}
