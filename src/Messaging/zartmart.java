package Messaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class zartmart {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*File fileName = new File("Geek.txt");
		BufferedWriter out = new BufferedWriter(
                new FileWriter(fileName, true));
 
            // Writing on output stream
            out.write("chks");
            // Closing the connection
            out.close();*/
		//String m = message();
		//System.out.println(m);
		String m = "sdadsadsaaend";
		System.out.println(m.substring(m.length()-3));
		
	}
	public static String message() throws IOException 
	{
		String result="";
		ArrayList <ArrayList<String>> partitons= new ArrayList <ArrayList<String>>();
		for(int i =1;i<4;i++) 
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
		while(partitons.get(partitons.size()-2).size() > iteration_number) 
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
}
