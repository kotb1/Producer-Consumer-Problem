package Messaging;

//1. Open a socket.
//2. Open an input stream and output stream to the socket.
//3. Read from and write to the stream according to the server's protocol.
//4. Close the streams.
//5. Close the socket.

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
* When a client connects the server spawns a thread to handle the client.
* This way the server can handle multiple clients at the same time.
*
* This keyword should be used in setters, passing the object as an argument,
* and to call alternate constructors (a constructor with a different set of
* arguments.
*/

//Runnable is implemented on a class whose instances will be executed by a thread.
public class Handler implements Runnable {

 // Array list of all the threads handling clients so each message can be sent to the client the thread is handling.
 public static ArrayList<Handler> Consumers = new ArrayList<>();
 public static ArrayList<Handler> Producers = new ArrayList<>();
 public static ArrayList<Handler> Partitions = new ArrayList<>();
 // Id that will increment with each new client.

 // Socket for a connection, buffer reader and writer for receiving and sending data respectively.
 private Socket socket;
 private BufferedReader bufferedReader;
 private BufferedWriter bufferedWriter;
 private String clientUsername;

 
 // Creating the client handler from the socket the server passes.
 public Handler(Socket socket) {
     try {
    	// System.out.println("connecteddddd");
         this.socket = socket;
         this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         // When a client connects their username is sent.
         this.clientUsername = bufferedReader.readLine();
         // Add the new client handler to the array so they can receive messages from others.
         if(this.clientUsername.equals("Producer")) 
         {
        	 Producers.add(this);
         }
         else if(this.clientUsername.equals("Consumer")) 
         {
        	 int consumer_number=Consumers.size()+1;
        	 this.clientUsername+=String.valueOf(consumer_number);
        	 Consumers.add(this);
        	 //broadcastMessage();
         }
         else if(this.clientUsername.equals("Partition")) 
         {
        	 Partitions.add(this);
        	 int partition_number=Partitions.size()-1;
        	 File myObj = new File("partition"+partition_number+".txt");
        	 if (myObj.createNewFile()) {
        	        System.out.println("File created: " + myObj.getName());
        	      } else {
        	        System.out.println("File already exists.");
        	      }
        	 //System.out.println(Partitions.size());
         }
         //Handlers.add(this);
         //broadcastMessage("SERVER: " + clientUsername + " has joined the app!");
     } catch (IOException e) {
         // Close everything more gracefully.
        // closeEverything(socket, bufferedReader, bufferedWriter);
     }
 }

 // Everything in this method is run on a separate thread. We want to listen for messages
 // on a separate thread because listening (bufferedReader.readLine()) is a blocking operation.
 // A blocking operation means the caller waits for the callee to finish its operation.
 @Override
 public void run() {
     String messageFromClient;
     int partition_number=0;
     // Continue to listen for messages while a connection with the client is still established.
     while (socket.isConnected()) {
         try {
        	 File file = new File("partition"+partition_number+".txt");
             // Read what the client sent and then send it to every other client.
             if(file.isFile())
             {
            	 messageFromClient = bufferedReader.readLine();
            	 //System.out.println(messageFromClient);
            	 String y = messageFromClient.substring(10);
            	 BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            	 out.write(y);
            	 out.write("\n");
            	 out.close();
            	 partition_number++; 
            	 if(y.equalsIgnoreCase("END")) 
            	 {
            		 //System.out.println("chsk");
            		 broadcastMessage();
            	 }
             }
             else
            	 partition_number=0;
             //System.out.println(messageFromClient);
             //broadcastMessage(messageFromClient);
         } catch (IOException e) {
             // Close everything gracefully.
            // closeEverything(socket, bufferedReader, bufferedWriter);
             break;
         }
         
     }
 }

 // Send a message through each client handler thread so that everyone gets the message.
 // Basically each client handler is a connection to a client. So for any message that
 // is received, loop through each connection and send it down it.
 public void broadcastMessage() {
     for (Handler clientHandler : Consumers) {
         try {
        	 	String data = message();
        	 	clientHandler.bufferedWriter.write(data);
        	 	clientHandler.bufferedWriter.newLine();
        	 	clientHandler.bufferedWriter.flush();
        	 
        	 
        	 
        	 
        	 /*int partition_number=1;
        	 File myObj = new File("partition"+partition_number+".txt");
        	 if(myObj.isFile()) 
        	 {
        		 Scanner myReader = new Scanner(myObj);
                 while (myReader.hasNextLine()) {
                   String data = myReader.nextLine();
                   System.out.println("chsk2");
                   clientHandler.bufferedWriter.write(data);
                   clientHandler.bufferedWriter.newLine();
                   clientHandler.bufferedWriter.flush();
                 }
        	 }
        	 else 
        		 partition_number=1;*/
            
        	 
        	 
        	 
             /*// You don't want to broadcast the message to the user who sent it.
             //if (!clientHandler.clientUsername.equals("Consumer")) {
                 clientHandler.bufferedWriter.write(messageToSend);
                 //System.out.println(Handlers.size());
                 clientHandler.bufferedWriter.newLine();
                 clientHandler.bufferedWriter.flush();
                 clientHandler.bufferedWriter.write(messageToSend);
                 //System.out.println(Handlers.size());
                 clientHandler.bufferedWriter.newLine();
                 clientHandler.bufferedWriter.flush();
             //}*/
         } catch (IOException e) {
             // Gracefully close everything.
            // closeEverything(socket, bufferedReader, bufferedWriter);
      
         }
     }

 }
 public static String message() throws IOException 
	{
		String result="";
		ArrayList <ArrayList<String>> partitons= new ArrayList <ArrayList<String>>();
		for(int i =0;i<Partitions.size();i++) 
		{
			File myObj = new File("partition"+i+".txt");
			ArrayList <String> partition = new ArrayList<String>();
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
	            String data = myReader.nextLine();
	            partition.add(data);
			}
			partitons.add(partition);
		}
		int iteration_number=0;
		ArrayList <Integer> sizes = new ArrayList<Integer>();
		for(int i =0;i<partitons.size();i++) 
		{
			sizes.add(partitons.get(i).size());
		}
		int max = max(sizes);
		//while(partitons.get(partitons.size()-2).size() > iteration_number) 
		while(iteration_number<max)
		{
			for(int i=0;i<partitons.size();i++) 
			{
				//for(int j=iteration_number;j<partitons.get(i).size();j++) 
				//{
					if(partitons.get(i).size()>iteration_number) 
					{
						result+=partitons.get(i).get(iteration_number);
						result+=";";
					}
					//break;
				//}
			}
			iteration_number++;
		}
		System.out.println(partitons.size());
		return result;
	}
 // If the client disconnects for any reason remove them from the list so a message isn't sent down a broken connection.
/* public void removeClientHandler() {
     Handlers.remove(this);
     //broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
 }*/

 // Helper method to close everything so you don't have to repeat yourself.
 /*public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
     // Note you only need to close the outer wrapper as the underlying streams are closed when you close the wrapper.
     // Note you want to close the outermost wrapper so that everything gets flushed.
     // Note that closing a socket will also close the socket's InputStream and OutputStream.
     // Closing the input stream closes the socket. You need to use shutdownInput() on socket to just close the input stream.
     // Closing the socket will also close the socket's input stream and output stream.
     // Close the socket after closing the streams.

     // The client disconnected or an error occurred so remove them from the list so no message is broadcasted.
     removeClientHandler();
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
 }*/
 public static int max(ArrayList<Integer> t) {
	    int maximum = t.get(0);   
	    for (int i=1; i<t.size(); i++) {
	        if (t.get(i) > maximum) {
	            maximum = t.get(i);
	        }
	    }
	    return maximum;
	}
}
