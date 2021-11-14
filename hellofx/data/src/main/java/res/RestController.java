package res;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RestController {

    private static final String ALL_EVENTS_ENDPOINT = "http://localhost:8080/vpr/all-events";
    private static final String ALL_USERS_ENDPOINT = "http://localhost:8080/vpr/all-users";

    public Event[] getAllEvents(){
        Event[] eventList = null;

        try {
            URL url = new URL(ALL_EVENTS_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            if(status == 200){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                eventList = parseJsonToEventList(content.toString());
                for (Event e : eventList){
                    System.out.println(e);
                }
            }else{
                System.out.println("Status: " + status);
            }
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

        return eventList;
    }

    private Event[] parseJsonToEventList(String jsonString) throws JsonProcessingException {
        ArrayList<Event> eventList;

        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        //String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

        return objectMapper.readValue(jsonString, Event[].class);
    }
}
