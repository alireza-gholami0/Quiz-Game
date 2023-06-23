import javafx.application.Application;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        int sPort = 0;
        String sName = null;
        try {
            Object object = parser.parse(new FileReader("users.json"));
            JSONArray users = (JSONArray) object;
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                String type = (String) user.get("type");
                String port = String.valueOf(user.get("port"));
                String name = (String) user.get("name");
                if (type.equals("host")){
                    sPort = Integer.parseInt(port);
                    sName = name;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        new Server(sPort, sName);
    }
}
