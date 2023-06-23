import javafx.application.Application;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ClientMain{
    static int ClientNum = 2;
    static String data[] = new String[3];
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("users.json"));
            JSONArray users = (JSONArray) object;
            for (int i = 0; i <= ClientNum + 1; i++) {
                JSONObject user = (JSONObject) users.get(i);
                String type = (String) user.get("type");
                String port = String.valueOf(user.get("port"));
                String name = (String) user.get("name");
                if (type.equals("client") && i == ClientNum + 1){
                    data[0] = name;
                    data[1] = port;
                }
                else if (type.equals("host")){
                    data[2] = port;
                }
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        if (data[0] == null){
            System.out.println("user not found");
        }
        else Application.launch(ClientUI.class, data);
    }
}
