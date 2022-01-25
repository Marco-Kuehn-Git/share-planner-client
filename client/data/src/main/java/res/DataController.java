package res;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import helper.HttpRequestException;
import helper.Tuple;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

public class DataController {

    public static long USER_ID = -1;

    private static final String ALL_EVENTS_ENDPOINT = "http://localhost:8080/event/all";
    private static final String ADD_EVENT_ENDPOINT = "http://localhost:8080/event/add";
    private static final String DELETE_EVENT_ENDPOINT = "http://localhost:8080/event/del";
    private static final String EDIT_EVENT_ENDPOINT = "http://localhost:8080/event/edit";

    private static final String ALL_USER_ENDPOINT = "http://localhost:8080/user/all";
    private static final String ADD_USER_ENDPOINT = "http://localhost:8080/user/add";
    private static final String DELETE_USER_ENDPOINT = "http://localhost:8080/user/del";
    private static final String EDIT_USER_ENDPOINT = "http://localhost:8080/user/edit";

    private static final String LOGIN_ENDPOINT = "http://localhost:8080/user/login";
    private static final String LOGIN_WITH_TOKEN_ENDPOINT = "http://localhost:8080/user/login-with-token";
    private static final String HEADER_TEST_ENDPOINT = "http://localhost:8080/vpr/header-test";

    private final HttpRequest httpRequest;

    public DataController() {
        httpRequest = new HttpRequest();
    }

    public boolean login(String username, String password) {
        try {
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    LOGIN_ENDPOINT,
                    "login=" + username
                            + "&password=" + password,
                    false
            );
            String[] data = response.getValue().split("\\s+");

            USER_ID = Long.parseLong(data[1]);
            HttpRequest.TOKEN = data[0];
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return USER_ID >= 0;
    }

    public boolean loginWithToken(long userId, String token) {
        try {
            HttpRequest.TOKEN = token;
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    LOGIN_WITH_TOKEN_ENDPOINT,
                    "userId=" + userId,
                    true
            );

            System.out.println(response.getKey() + " " + response.getValue());

            if (response.getKey() != 200) return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        USER_ID = userId;
        HttpRequest.TOKEN = token;
        return USER_ID >= 0;
    }

    /*********
     * Event *
     *********/
    public void createEvent(Event event) throws HttpRequestException {
        sendBasicHttpRequest(
                ADD_EVENT_ENDPOINT,
                event.getAsUrlParam(),
                true
        );
    }

    public void deleteEvent(int userId, int eventId, LocalDateTime date) throws HttpRequestException {
        sendBasicHttpRequest(
                DELETE_EVENT_ENDPOINT,
                "userId=" + userId + "&eventId=" + eventId + "&date=" + date.toLocalDate(),
                true
        );
    }

    public void editEvent(Event oldEvent, Event event) throws HttpRequestException {
        sendBasicHttpRequest(
                EDIT_EVENT_ENDPOINT,
                "eventId=" + oldEvent.getId() +
                        "&userId=" + oldEvent.getOwnerId() +
                        "&date=" + oldEvent.getDate().toLocalDate() +
                        "&newDate=" + event.getDate().toLocalDate() +
                        "&newName=" + event.getName() +
                        "&newStart=" + event.getStart() +
                        "&newEnd=" + event.getEnd() +
                        "&newPriority=" + event.getPriority() +
                        "&newIsFullDay=" + event.isFullDay() +
                        "&newIsPrivate=" + event.isPrivate(),
                true
        );
    }

    public ArrayList<Event> getAllVisibleEvents(LocalDateTime startDate, LocalDateTime endDate) throws HttpRequestException {
        ArrayList<Event> eventList = new ArrayList<>();
        try {
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    ALL_EVENTS_ENDPOINT,
                    "userId=" + USER_ID + "&startDate=" + startDate.toLocalDate() + "&endDate=" + endDate.toLocalDate(),
                    true
            );
            if (response.getKey() != 200) {
                throw new HttpRequestException(response);
            }
            String jsonResponse = response.getValue();
            System.out.println(jsonResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            eventList = (ArrayList<Event>) objectMapper.readValue(jsonResponse, new TypeReference<List<Event>>() {
            });


        } catch (HttpRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpRequestException("Es konnte keine Verbindung mit dem Server hergestellt werden.", 600);
        }

        return eventList;
    }

    /********
     * User *
     ********/

    /*
    public void createUser(User user) throws HttpRequestException {
        sendBasicHttpRequest(
                ADD_USER_ENDPOINT,
                user.getAsUrlParam(),
                true
        );
    }

    public void deleteUser(User user) throws HttpRequestException {
        sendBasicHttpRequest(
                DELETE_USER_ENDPOINT,
                user.getAsUrlParam(),
                true
        );
    }

    public void editUser(User oldUser, User user) throws HttpRequestException {
        sendBasicHttpRequest(
                EDIT_USER_ENDPOINT,
                user.getAsUrlParam(),
                true
        );
    }
     */

    private void sendBasicHttpRequest(String urlString, String urlParameters, boolean sendAuth) throws HttpRequestException {
        try {
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    urlString,
                    urlParameters,
                    sendAuth
            );
            if (response.getKey() != 200) {
                throw new HttpRequestException(response);
            }
        } catch (HttpRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpRequestException("Es konnte keine Verbindung mit dem Server hergestellt werden.", 600);
        }
    }
}