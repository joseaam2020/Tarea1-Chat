package tarea.tareaChat;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        ServerFrame newServer = new ServerFrame();
        newServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

class ServerFrame extends JFrame implements Runnable{

    private JTextArea texto;
    public ServerFrame(){
        setBounds(1200, 300,280,350);
        JPanel serverPanel = new JPanel();
        serverPanel.setLayout(new BorderLayout());
        texto = new JTextArea();
        serverPanel.add(texto, BorderLayout.CENTER);
        add(serverPanel);
        setVisible(true);
        Thread newThread = new Thread(this);
        newThread.start();
    }

    public void run() {
        try {
            ServerSocket newSS = new ServerSocket(4444);
            while(true){
                Socket inputSocket = newSS.accept();
                DataInputStream input = new DataInputStream(inputSocket.getInputStream());
                String inputString = input.readUTF();
                texto.append(inputString + "\n");
                Socket enviaUser = new Socket("127.0.0.1",4444);
                DataOutputStream outputUser = new DataOutputStream(enviaUser.getOutputStream());
                outputUser.writeUTF(texto.getText());
                outputUser.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
