package tarea.tareaChat;

import java.io.IOException;
import java.util.logging.*;

public class UserLog {

    public static Logger ulog = null;
    public static ConsoleHandler uCH = null;
    public static FileHandler uFH = null;

    public UserLog(){
        try {
            ulog = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            uCH = new ConsoleHandler();
            uFH = new FileHandler("UserLog.txt");
            uFH.setFormatter(new SimpleFormatter());
            uCH.setLevel(Level.FINEST);
            uFH.setLevel(Level.FINEST);
            //ulog.addHandler(uCH);
            Handler[] ulogHandlers = ulog.getHandlers();
            for(Handler uloghandler : ulogHandlers){
                ulog.removeHandler(uloghandler);
            }
            ulog.addHandler(uFH);

        } catch (IOException ioException) {
            //ioException.printStackTrace();
        }

    }

    public void log(Exception e, Level errorLevel, String mensaje){
        ulog.log(errorLevel, mensaje, e);
    }
}
