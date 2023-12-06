package Messaging;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

// A client sends messages to the server, the server spawns a thread to communicate with the client.
// Each communication with a client is added to an array list so any message sent gets sent to every other client
// by looping through it.

public class Consumer {

    // A client has a socket to connect to the server and a reader and writer to receive and send messages respectively.
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Consumer(Socket socket,String username) {
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
    public ArrayList<String> listenForMessage() throws IOException {
                //String msg="a";
    			//ArrayList<String> msg= new ArrayList<String>();
                // While there is still a connection with the server, continue to listen for messages on a separate thread.
               
                        // Get the messages sent from other users and print it to the console.
               // while(!(msg.equals("")))
                //{
                	//msg = bufferedReader.readLine();
    				
                	//while(!(bufferedReader.readLine().equalsIgnoreCase("END"))) 
                	//{
                		String someString = bufferedReader.readLine();
                		ArrayList <String> output = new ArrayList();
                		//String someString = "elephant";
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
                		//System.out.println(someString);
                		/*for(int i =0;i<output.size();i++) 
                		{
                			System.out.println(output.get(i));
                		}*/
                		//msg.add(bufferedReader.readLine());
                	//}
                //}
                        
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
        Consumer client = new Consumer(socket,"Consumer");
        //int index =0;
        // Infinite loop to read and send messages.
        ArrayList<String> b = new ArrayList();
        Scanner scanner = new Scanner(System.in);
        while (client.socket.isConnected()) {
        	 System.out.println("List Messages or Quit");
        	 
        	 
             String choice = scanner.nextLine();
             if(choice.equalsIgnoreCase("list")) 
             {
            	 ArrayList<String> a =client.listenForMessage();
            	 int size = b.size();
            	/*// if(a.size()==b.size()) 
            	 {
            		 System.out.println("There is No new Messages");
            	 }
            	// else */
            	 
            	 {
            		 for(int i=size;i<a.size();i++) 
                	 {
                		 System.out.println(a.get(i));
                	 }
                	 b=a;
            	 }
            	 
            	 /*ArrayList <String> output=client.listenForMessage();
            	 for(int i =0;i<output.size();i++) 
            	 {
            		 System.out.println(output.get(i));
            		 System.out.println("chks");
            	 }
            	 System.out.println(output.size());*/
             }
             else 
             {
             	closeEverything(client.socket,client.bufferedReader,client.bufferedWriter);
             }
        }
       
       // client.sendMessage();
   
    }
}

