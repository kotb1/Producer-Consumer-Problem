package Messaging;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextField;

import Messaging.Producer;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI_Producer {

	private JFrame frame;
	private JTextField txtPleaseEnterYour;
	private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    public static GUI_Producer client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//GUI_Producer window = new GUI_Producer();
					Socket socket = new Socket("localhost", 1234);
					client = new GUI_Producer(socket, "Producer");
					client.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public GUI_Producer(Socket socket,String username) throws IOException{
		
		this.socket = socket;
        this.username = username;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException {
		//Socket socket = new Socket("localhost", 1234);
		//GUI_Producer client = new GUI_Producer(socket, "Producer");
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtPleaseEnterYour = new JTextField();
		txtPleaseEnterYour.setText("Please Enter your message");
		txtPleaseEnterYour.setBounds(73, 50, 254, 35);
		frame.getContentPane().add(txtPleaseEnterYour);
		txtPleaseEnterYour.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==btnNewButton) 
				{
					client.sendMessage(txtPleaseEnterYour.getText());
				}
			}
		});
		btnNewButton.setBounds(124, 118, 150, 57);
		frame.getContentPane().add(btnNewButton);
	}
	public void sendMessage(String message) {
        try {
                bufferedWriter.write(username + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
        } catch (IOException e) {
        	System.out.println("ERRor");
        }
    }
}
