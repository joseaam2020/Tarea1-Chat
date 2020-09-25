package tarea.tareaChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class User {
    public static void main(int[] args) {
        UserFrame newFrame = new UserFrame(args);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class UserFrame extends JFrame{
    public UserFrame(int[] ports){
        setBounds(600,300,280,350);
        UserPanel newUser = new UserPanel(ports);
        add(newUser);
        setVisible(true);
    }
}
class UserPanel extends JPanel implements Runnable{

    private JTextField campoTexto;
    private JTextField campoNombre;
    private JTextField ipAddress;
    private JButton boton1;
    private JTextArea campoMensajes;
    private JScrollPane marcoMensajes;
    private int inputPort;
    private int outputPort;

    public UserPanel(int[] ports){
        inputPort = ports[0];
        outputPort = ports [1];
        JLabel nombre = new JLabel("Nombre:");
        add(nombre);
        campoNombre = new JTextField(20);
        add(campoNombre);
        JLabel ip = new JLabel("Ip de destinatario:");
        add(ip);
        ipAddress = new JTextField(10);
        add(ipAddress);
        campoMensajes = new JTextArea(12,26);
        campoMensajes.setEditable(false);
        marcoMensajes = new JScrollPane();
        marcoMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Color marco = new Color(0,102,255);
        marcoMensajes.getViewport().setBackground(marco);
        marcoMensajes.getViewport().add(campoMensajes);
        add(marcoMensajes);
        campoTexto = new JTextField(20);
        add(campoTexto);
        boton1 = new JButton("Enviar");
        TextoBoton newTextoBoton = new TextoBoton(this.outputPort,ipAddress.getText());
        boton1.addActionListener(newTextoBoton);
        add(boton1);
        Thread userThread = new Thread(this);
        userThread.start();
    }

    private class TextoBoton implements ActionListener{

        private int outputPort;
        private String ip;

        public TextoBoton(int newOutport,String ip){
            this.outputPort = newOutport;
            this.ip = ip;
        }

        public void actionPerformed(ActionEvent e) {
            StringBuilder texto = new StringBuilder(campoNombre.getText() + ": " + campoTexto.getText());
            campoMensajes.append(String.valueOf(texto)+"\n");
            campoTexto.setText("");
            try {
                Socket newSocket = new Socket(this.ip,this.outputPort);
                DataOutputStream output = new DataOutputStream(newSocket.getOutputStream());
                output.writeUTF(String.valueOf(texto));
                output.close();
            } catch (IOException ioException) {
                System.out.println("Here");
                System.out.println(ioException.getMessage());;
            }
        }
    }

    public void run() {
        try {
            ServerSocket newSS = new ServerSocket(this.inputPort);
            while(true){
                StringBuilder texto = new StringBuilder();
                Socket inputSocket = newSS.accept();
                DataInputStream input = new DataInputStream(inputSocket.getInputStream());
                String inputString = input.readUTF();
                texto.append(inputString);
                campoMensajes.append(String.valueOf(texto)+"\n");
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}