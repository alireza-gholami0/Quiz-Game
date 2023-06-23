import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Server {
    ServerSocket mServer;
    String serverName;
    int serverPort;
    ArrayList<Question> questions;
    ArrayList<Thread> threads = new ArrayList<Thread>();
    HashMap<String, ClientManager> clientMap = new HashMap<String, ClientManager>();
    int clients = 0;

    public Server(int serverPort, String serverName) {
        this.serverPort = serverPort;
        this.serverName = serverName;
        System.out.println("ServerName: " + serverName);
        System.out.println("ServerPort: " + serverPort);
        this.questions = questionReader();
        try {
            mServer = new ServerSocket((int) this.serverPort);
            System.out.println("Server Created!");
            while (true) {
                Socket client = mServer.accept();
                System.out.println("Connect New Client!");
                Thread t = new Thread(new ClientManager(this, client));
                threads.add(t);
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Question> questionReader() {
        ArrayList<Question> questions1 = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("questions.json"));
            JSONArray questions = (JSONArray) object;
            for (int i = 0; i < questions.size(); i++) {
                JSONObject q = (JSONObject) questions.get(i);
                JSONArray options = (JSONArray) q.get("options");
                String question = (String) q.get("question");
                long answer = (long) q.get("answer");
                String[] op = (String[]) options.toArray(new String[4]);
                questions1.add(new Question(question, op, (int) answer));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return questions1;
    }

    public void addClientManager(String clientName, ClientManager clientManager) {
        clientMap.put(clientName, clientManager);
    }

    public ClientManager findClient(String clientName) {
        return clientMap.get(clientName);
    }

    public ArrayList<ClientManager> findAllClientManagers() {
        ArrayList<ClientManager> result = new ArrayList<>();
        for (Map.Entry<String, ClientManager> entry : clientMap.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    public String[] setBoard() {
        String board[] = new String[clients];
        int i = 0;
        for (ClientManager clientManager : findAllClientManagers()) {
            board[i] = clientManager.name + "\t " + clientManager.game.answered + "\t" + clientManager.game.points;
            System.out.println(board[i]);
            i++;
        }
        return board;
    }
}
