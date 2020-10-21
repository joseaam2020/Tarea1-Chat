package tarea.tareaChat;

import java.io.IOException;
import java.util.logging.*;

public class UserLog {

    private static Logger ulog = null;
    private static ConsoleHandler uCH = null;
    private static FileHandler uFH = null;
    private static Exception eLog = null;
    private static Level uLevel = null;
    private static String uMensaje = null;

    public UserLog(Exception e, String className, Level errorLevel, String mensaje){
        try {
            ulog = Logger.getLogger(className);
            uCH = new ConsoleHandler();
            uFH = new FileHandler("UserLog.log");
            uCH.setLevel(Level.FINEST);
            uFH.setLevel(Level.FINEST);
            ulog.addHandler(uCH);

            eLog = e;
            uLevel = errorLevel;
            uMensaje = mensaje;

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void log(){
        ulog.log(uLevel, uMensaje, eLog);
    }
}
