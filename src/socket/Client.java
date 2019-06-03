package socket;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private BufferedReader input = null;
    private PrintWriter out = null;

    // constructor to set IP address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server!");

            // takes input from terminal
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // sends output to the socket
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException i)
        {
            System.out.println("Error while establishing connection: " + i);
        }

        // string to read message from input
        String answer = "";

        Scanner keyboard = new Scanner(System.in);
        // keep reading until "0" is input
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

                try {
                    int command = Integer.parseInt(answer);
                } catch (Exception e) {
                    System.out.println("Invalid command!");
                    return;
                }

                out.println(answer);
            }
            catch(IOException i)
            {
                System.out.println("IOException: " + i);
            }
        }

        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println("IOException: " + i);
        }
    }
}
