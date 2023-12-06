package Messaging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Partition {

	// A client has a socket to connect to the server and a reader and writer to receive and send messages respectively.
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Partition(Socket socket,String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            // Gracefully close everything.
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Sending a message isn't blocking and can be done without spawning a thread, unlike waiting for a message.
    /*public void sendMessage() {
        try {
            // Initially send the username of the client.
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            // Create a scanner for user input.
            Scanner scanner = new Scanner(System.in);
            // While there is still a connection with the server, continue to scan the terminal and then send the message.
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            // Gracefully close everything.
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }*/

    // Listening for a message is blocking so need a separate thread for that.
    public String listenForMessage() throws IOException {
                //String msg="a";
    			String msg="a";
    			
                // While there is still a connection with the server, continue to listen for messages on a separate thread.
               
                        // Get the messages sent from other users and print it to the console.
               // while(!(msg.equals("")))
                //{
                	msg = bufferedReader.readLine();
                //}
                        
                return msg;
    }

    // Helper method to close everything so you don't have to repeat yourself.
    public static void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Note you only need to close the outer wrapper as the underlying streams are closed when you close the wrapper.
        // Note you want to close the outermost wrapper so that everything gets flushed.
        // Note that closing a socket will also close the socket's InputStream and OutputStream.
        // Closing the input stream closes the socket. You need to use shutdownInput() on socket to just close the input stream.
        // Closing the socket will also close the socket's input stream and output stream.
        // Close the socket after closing the streams.
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run the program.
    public static void main(String[] args) throws IOException {

        // Get a username for the user and a socket connection.
        //Scanner scanner = new Scanner(System.in);
       // System.out.print("Enter your username for the group chat: ");
        //String username = scanner.nextLine();
        // Create a socket to connect to the server.
        Socket socket = new Socket("localhost", 1234);

        // Pass the socket and give the client a username.
        //System.out.println("Please Enter your name");
        //String username=scanner.nextLine();
        //username+=" Consumer";
        //Consumer client = new Consumer(socket,username);
        Partition client = new Partition(socket,"Partition");
        // Infinite loop to read and send messages.
        /*Scanner scanner = new Scanner(System.in);
        
        while (client.socket.isConnected()) {
        	 System.out.println("List Messages or Quit");
             String choice = scanner.nextLine();
             if(choice.equalsIgnoreCase("list")) 
             {
             	System.out.println(client.listenForMessage());
             }
             else 
             {
             	closeEverything(client.socket,client.bufferedReader,client.bufferedWriter);
             }
        }
        */
       
       // client.sendMessage();

    }

}
