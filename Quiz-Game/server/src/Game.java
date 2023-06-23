import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Game extends ClientManager implements Runnable{
    public static int LIMIT = 3;
    public static int QUESTION_TIME = 10;
    public static int BREAK_TIME = 5;
    int answerQuestion;
    int points = 0;
    int answered = 0;

    public Game(Server server, Socket client) throws IOException {
        super(server, client);
        fromClientStream = client.getInputStream();
        toClientStream = client.getOutputStream();
        writer = new PrintWriter(toClientStream,true);
        reader = new BufferedReader(new InputStreamReader(fromClientStream, "UTF-8"));
    }
    @Override
    public void run() {
        sendata();
        while (server.clients < LIMIT){
            System.out.printf("");
        }

        for (Question question : server.questions){
            writer.println("QUESTION");
            writer.println(question.question);
            writer.println(question.options[0]);
            writer.println(question.options[1]);
            writer.println(question.options[2]);
            writer.println(question.options[3]);
            answerQuestion = question.answer;
            try {
                for (int i = 0; i < QUESTION_TIME; i++) {
                    writer.println("TIME");
                    writer.println(i);
                    TimeUnit.SECONDS.sleep(1);
                }
                TimeUnit.SECONDS.sleep(BREAK_TIME);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        writer.println("ENDGAME");
        writer.println("End Game");
    }

    private void sendata() {
        writer.println("DATA");
        writer.println(QUESTION_TIME);
        writer.println(BREAK_TIME);
    }

    public void sendBoard() {
        writer.println("BOARD");
        String board[] = server.setBoard();
        writer.println(board.length + 2);
        writer.println("Name \t Answered \t Points");
        for (int i = 0; i < board.length; i++) {
            writer.println(board[i]);
        }
        writer.println("-----------------------------------");
    }
    public void updateData(int answer) {
        answered++;
        if (answer == answerQuestion) points++;
    }
}
