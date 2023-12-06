package Messaging;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class GUI_Consumer {

	private JFrame frame;
	private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static GUI_Consumer window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Socket socket = new Socket("localhost", 1234);
					window = new GUI_Consumer(socket,"Consumer");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_Consumer(Socket socket,String username) throws IOException {
		this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private void initialize() throws IOException {
		Socket socket = new Socket("localhost", 1234);
		Consumer client = new Consumer(socket,"Consumer");
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//client.listenForMessage();
				if(e.getSource()==btnNewButton) 
				{
					ArrayList<String> b = new ArrayList(); 
		            try {
		            	 ArrayList<String> a =window.listenForMessage();
		            	 int size = b.size();
		            	 {
		            		 for(int i=size;i<a.size();i++) 
		                	 {
		                		 System.out.println(a.get(i));
		                	 }
		                	 b=a;
		            	 }
		         
		            	}
		            catch(IOException e1) 
		            	{
		            		System.out.println("ERRor");
		            	}
				}
				
			}
		});
		btnNewButton.setBounds(83, 67, 238, 109);
		frame.getContentPane().add(btnNewButton);
	}
	public ArrayList<String> listenForMessage() throws IOException {
        		String someString = bufferedReader.readLine();
        		ArrayList <String> output = new ArrayList();
        		char someChar = ';';
        		int count = 0;
        		 
        		for (int i = 0; i < someString.length(); i++) {
        		    if (someString.charAt(i) == someChar) {
        		        count++;
        		    }
        		}
        		for(int i =0;i<count;i++) 
        		{
        			String split;
        			int endpos =Position(someString,i+1);
        			int startpos=Position(someString,i);
        			if(startpos == 0) 
        			{
        				 split=someString.substring(startpos, endpos);
        			}
        			else
        				 split=someString.substring(startpos+1, endpos);
        			if(!(split.equalsIgnoreCase("END"))) 
        			{
        				output.add(split);
        			}
        			
        		}
        return output;
}
private static int Position(String s, int n) {
        int result = -1;
        int dotsToFind = n;
        char[] ca = s.toCharArray();
        for (int i = 0; i < ca.length; ++i) {
            if (ca[i] == ';') --dotsToFind;
            if (dotsToFind == 0) return i;
        }
        return result;
    }
}
