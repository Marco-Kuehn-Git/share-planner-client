package res;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataController {

    private static final String ALL_EVENTS_ENDPOINT = "http://localhost:8080/vpr/all-events";
    private static final String ALL_USERS_ENDPOINT = "http://localhost:8080/vpr/all-users";
    private static final String ADD_EVENT_ENDPOINT = "http://localhost:8080/vpr/add-event";
    private static final String DELETE_EVENT_ENDPOINT = "http://localhost:8080/vpr/del-event";

    private final HttpRequest httpRequest;

    public DataController(){
        httpRequest = new HttpRequest();
    }

    public void createEvent(Event event){
        try {
            System.out.println(httpRequest.sendPostRequest(ADD_EVENT_ENDPOINT, event.getAsUrlParam()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(int eventId){
        try {
            System.out.println(httpRequest.sendPostRequest(DELETE_EVENT_ENDPOINT, "eventId=" + eventId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Event> getAllVisibleEvents() {
        ArrayList<Event> eventList = new ArrayList<>();

        try {
            String jsonResponse = httpRequest.sendPostRequest(ALL_EVENTS_ENDPOINT, "userId=1");
            System.out.println(jsonResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            //String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

            for (Object obj : objectMapper.readValue(jsonResponse, Object[].class)){
                ArrayList<Object> list = new ArrayList<>();
                if (obj.getClass().isArray()) {
                    list = (ArrayList<Object>) Arrays.asList((Object[])obj);
                } else if (obj instanceof Collection) {
                    list = new ArrayList<>((Collection<?>) obj);
                }
                eventList.add(new Event(list));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventList;
    }

    public Event[] getAllEvents() {
        Event[] eventList = null;

        try {
            String jsonResponse = httpRequest.sendGetRequest("http://localhost:8080/vpr/all-events-test");
            eventList = parseJsonToEventList(jsonResponse);
            for (Event e : eventList) {
                System.out.println(e);
            }
        } catch (Exception e) {
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
