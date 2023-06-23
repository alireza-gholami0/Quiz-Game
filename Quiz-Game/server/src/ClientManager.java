import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class ClientManager implements Runnable {
    Socket client;
    Server server;
    InputStream fromClientStream;
    OutputStream toClientStream;
    BufferedReader reader;
    PrintWriter writer;
    String name;
    int cPort = 0;
    Game game;
    public ClientManager(Server server, Socket client) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            fromClientStream = client.getInputStream();
            toClientStream = client.getOutputStream();
            writer  = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(
                            toClientStream, "UTF-8")), true);
            reader = new BufferedReader(new InputStreamReader(fromClientStream, "UTF-8"));
            name = reader.readLine();
            cPort = Integer.parseInt(reader.readLine());
            System.out.println("Connected : " + name);
            server.addClientManager(name,this);
            game = new Game(server, client);
            Thread t = new Thread(game);
            t.start();
            server.clients++;
            while (true){
                String command = reader.readLine();
                if (command.equals("SCHAT")){
                    String to = reader.readLine();
                    String text = reader.readLine();
                    sendTextToAnotherClient(to,text);
                }else if (command.equals("GCHAT")){
                    String text = reader.readLine();
                    sendTextToAll(text);
                }else if (command.equals("AN")){
                    int answer = Integer.parseInt(reader.readLine());
                    game.updateData(answer);
                    game.sendBoard();
                }
                else if (command.equals("EXIT")){
                    System.out.println(name + " Disconnected!");
                    writer.println("EXIT");
                //    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendTextToAll(String text) {
        System.out.println(name  + " To: All [" + text + "]");
        for (ClientManager clientManager : server.findAllClientManagers()){
            if (this == clientManager) this.sendText("(G)Me", text);
            else clientManager.sendText("(G)" + name, text);
        }
    }

    private void sendTextToAnotherClient(String to, String text) {
        ClientManager anotherClient = server.findClient(to);
        if (anotherClient == null){
            writer.println("ERROR");
            writer.println("user not found");
            return;
        }
        System.out.println(name  + " To: " + to + " [" + text + "]");
        anotherClient.sendText("(P)" + name, text);
        this.sendText("(P)Me", text);
    }

    private void sendText(String from, String text) {
        writer.println("CHT");
        writer.println(from);
        writer.println(text);
    }
}