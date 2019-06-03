package socket;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private BufferedReader input = null;
    private PrintWriter output = null;

    // constructor to set IP address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server!");

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException i)
        {
            System.out.println("Error while establishing connection: " + i);
        }

        String answer = "";

        Scanner keyboard = new Scanner(System.in);

        while (!answer.equals("0"))
        {
            try
            {
                String response;
                while((response = input.readLine()) != null) {
                    if (response.equals("endRead"))
                        break;
                    System.out.println(response);
                }

                answer = keyboard.nextLine();

                output.println(answer);
            }
            catch(IOException i)
            {
                System.out.println("IOException: " + i);
            }
        }

        try
        {
            input.close();
            output.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println("IOException: " + i);
        }
    }
}
