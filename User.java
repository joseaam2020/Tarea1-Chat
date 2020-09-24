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
        UserFrame newFrame = new UserFrame();
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class UserFrame extends JFrame{
    public UserFrame(){
        setBounds(600,300,280,350);
        UserPanel newUser = new UserPanel();
        add(newUser);
        setVisible(true);
    }
}
class UserPanel extends JPanel implements Runnable{

    private JTextField campoTexto;
    private JTextField campoNombre;
    private JButton boton1;
    private JTextArea campoMensajes;

    public UserPanel(){
        JLabel nombre = new JLabel("Nombre:");
        add(nombre);
        campoNombre = new JTextField(20);
        add(campoNombre);
        campoMensajes = new JTextArea(10,20);
        campoMensajes.setEditable(false);
        add(campoMensajes);
        campoTexto = new JTextField(20);
        add(campoTexto);
        boton1 = new JButton("Enviar");
        TextoBoton newTextoBoton = new TextoBoton();
        boton1.addActionListener(newTextoBoton);
        add(boton1);
        Thread userThread = new Thread(this);
        userThread.start();
    }

    public void run() {
        ServerSocket userServer = null;
        String inputText;
        try {
            userServer = new ServerSocket(5555);
            Socket inputSocket;
            while(true){
                inputSocket = userServer.accept();
                DataInputStream input = new DataInputStream(inputSocket.getInputStream());
                 inputText = input.readUTF();
                 campoMensajes.append(inputText + "\n");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private class TextoBoton implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            try {
                Socket newSocket = new Socket("127.0.0.1",5555);
                DataOutputStream output = new DataOutputStream(newSocket.getOutputStream());
                output.writeUTF(campoNombre.getText() + ": " + campoTexto.getText());
                output.close();
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());;
            }
        }
    }

}

//class PaqueteMensaje implementes Serializable