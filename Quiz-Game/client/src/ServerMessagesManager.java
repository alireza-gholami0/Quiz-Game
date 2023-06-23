import java.io.*;

public class ServerMessagesManager implements Runnable {
    BufferedReader readerHolder;
    Client client;
    ClientUI clientUI;
    public ServerMessagesManager(BufferedReader reader, Client client, ClientUI clientUI) {
        readerHolder = reader;
        this.client = client;
        this.clientUI = clientUI;
    }

    @Override
    public void run() {
        while (true){
            try {
                String command = readerHolder.readLine();
                if (command.equals("DATA")){
                    client.qTime = Integer.parseInt(readerHolder.readLine());
                    client.breakTime = Integer.parseInt(readerHolder.readLine());
                }
                else if (command.equals("CHT")) {
                    String from = readerHolder.readLine();
                    String text = readerHolder.readLine();
                    clientUI.showMessage(from + " : [" + text + "]");
                }else if (command.equals("QUESTION")){
                    String q = readerHolder.readLine();
                    String op[] = new String[4];
                    op[0] = readerHolder.readLine();
                    op[1] = readerHolder.readLine();
                    op[2] = readerHolder.readLine();
                    op[3]= readerHolder.readLine();
                    clientUI.showQuestion(q);
                    clientUI.showOptions(op);
                    clientUI.setTimer((Integer) client.qTime);
                } else if (command.equals("ENDGAME")){
                    String endgame = readerHolder.readLine();
                    clientUI.showMessage(endgame);
                } else if (command.equals("ERROR")){
                    String error = readerHolder.readLine();
                    clientUI.showMessage(error);
                    System.out.println(error);
                }else if (command.equals("EXIT")){
                    return;
                }else if (command.equals("TIME")){
                    int time = Integer.parseInt(readerHolder.readLine());
                    clientUI.setTimer(time);
                }else if (command.equals("BOARD")){
                    int size = Integer.parseInt(readerHolder.readLine());
                    for (int i = 0; i < size; i++) {
                        String board = readerHolder.readLine();
                        clientUI.showBoard(board);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}