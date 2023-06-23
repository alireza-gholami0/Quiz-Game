import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static javafx.application.Application.launch;

public class Client implements Runnable{
    Socket mSocket;
    int cPort;
    int sPort;
    String serverAddress = "127.0.0.1";
    String name;
    InputStream fromServerStream;
    OutputStream toServerStream;
    BufferedReader reader;
    PrintWriter writer;
    ClientUI clientUI;
    int qTime,breakTime;
    public Client(String name, int cPort, int sPort, ClientUI clientUI) {
        this.cPort = cPort;
        this.sPort = sPort;
        this.name = name;
        this.clientUI = clientUI;
    }
    @Override
    public void run() {
        try {
            mSocket = new Socket(serverAddress,sPort);
            System.out.println("connect to server ....");
            System.out.println("ClientName: " + name);
            System.out.println("ClientPort: " + cPort);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(fromServerStream, "UTF-8"));
            writer = new PrintWriter(toServerStream, true);
            Thread t = new Thread(new ServerMessagesManager(reader, this, clientUI));
            t.start();
            writer.println(name);
            writer.println(cPort);
        } catch (IOException e) { System.out.println(e.getMessage()); }
    }
    public void exit() {
        writer.println("EXIT");
    }

    public void sendGroupCht(String text) {
        writer.println("GCHAT");
        writer.println(text);
    }

    public void sendSingleCht(String to, String text) {
        writer.println("SCHAT");
        writer.println(to);
        writer.println(text);
    }

}