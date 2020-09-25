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
    public static void main(String[] args) {
        UserFrame newFrame = new UserFrame("Chat");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class UserFrame extends JFrame{
    public UserFrame(String title){
        super(title);
        ImageIcon icon = new ImageIcon("C:\\Users\\Z420\\" +
                "IdeaProjects\\TareaChat\\src\\tarea\\tareaChat\\chat-bubble.png");
        this.setIconImage(icon.getImage());
        setBounds(600,300,280,350);
        UserPanel newUser = new UserPanel();
        add(newUser);
        setVisible(true);
    }
}
class UserPanel extends JPanel implements Runnable{

    private JTextField campoTexto;
    private JTextField campoNombre;
    private JTextField ipAddress;
    private JTextField campoOut;
    private JButton boton1;
    private JTextArea campoMensajes;
    private JLabel userPort;
    private JScrollPane marcoMensajes;

    public UserPanel(){
        Color panelBackground = Color.decode("#292F36");
        Color labelColor = Color.decode("#FFFFFF");
        Color portColor = Color.decode("#FF6B6B");
        Color botonColor = Color.decode("#4ECDC4");
        this.setBackground(panelBackground);
        JLabel nombre = new JLabel("Nombre:");
        nombre.setForeground(labelColor);
        add(nombre);
        campoNombre = new JTextField(20);
        add(campoNombre);
        JLabel ip = new JLabel("Ip del destinatario:");
        ip.setForeground(labelColor);
        add(ip);
        ipAddress = new JTextField(10);
        add(ipAddress);
        JLabel out = new JLabel("Puerto del destinatario:");
        out.setForeground(labelColor);
        add(out);
        campoOut = new JTextField(10);
        campoOut.setText("0");
        add(campoOut);
        campoMensajes = new JTextArea(10,26);
        campoMensajes.setEditable(false);
        marcoMensajes = new JScrollPane();
        marcoMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        marcoMensajes.getViewport().add(campoMensajes);
        add(marcoMensajes);
        campoTexto = new JTextField(20);
        add(campoTexto);
        boton1 = new JButton("Enviar");
        TextoBoton newTextoBoton = new TextoBoton();
        boton1.addActionListener(newTextoBoton);
        boton1.setBackground(botonColor);
        boton1.setForeground(labelColor);
        add(boton1);
        Thread userThread = new Thread(this);
        userThread.start();
        userPort = new JLabel("");
        userPort.setForeground(portColor);
        add(userPort);
        campoOut.setText("");
    }

    private class TextoBoton implements ActionListener{ ;

        public TextoBoton(){

        }

        public void actionPerformed(ActionEvent e) {
            StringBuilder texto = new StringBuilder(campoNombre.getText() + ": " + campoTexto.getText());
            campoMensajes.append(String.valueOf(texto)+"\n");
            campoTexto.setText("");
            try {
                int port = Integer.parseInt(campoOut.getText());
                Socket newSocket = new Socket(ipAddress.getText(),port);
                DataOutputStream output = new DataOutputStream(newSocket.getOutputStream());
                output.writeUTF(String.valueOf(texto));
                output.close();
            } catch (IOException ioException) {
                System.out.println("Sending");
                System.out.println(ioException.getMessage());;
            } catch (NumberFormatException numberFormatException){
                System.out.println("Port error");
                System.out.println(numberFormatException.getMessage());
            }
        }
    }

    public void run() {
        try {
            ServerSocket newSS = new ServerSocket(0);
            StringBuilder port = new StringBuilder();
            port.append(String.valueOf(newSS.getLocalPort()));
            userPort.setText("Utilizando puerto:" + String.valueOf(port));
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