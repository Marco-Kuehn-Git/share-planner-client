package container;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import helper.HttpRequestException;
import helper.Tuple;

import java.time.LocalDateTime;
import java.util.*;

public class DataController {

    public static long USER_ID = -1;
    public static String SERVER_URL = "http://localhost:8080";

    private static final String ALL_EVENTS_ENDPOINT =           "/event/all";
    private static final String ADD_EVENT_ENDPOINT =            "/event/add";
    private static final String DELETE_EVENT_ENDPOINT =         "/event/del";
    private static final String EDIT_EVENT_ENDPOINT =           "/event/edit";

    private static final String ALL_USER_ENDPOINT =             "/user/all";
    private static final String ADD_USER_ENDPOINT =             "/user/add";
    private static final String DELETE_USER_ENDPOINT =          "/user/del";
    private static final String EDIT_USER_ENDPOINT =            "/user/edit";

    private static final String LOGIN_ENDPOINT =                "/user/login";
    private static final String LOGIN_WITH_TOKEN_ENDPOINT =     "/user/login-with-token";
    private static final String HEADER_TEST_ENDPOINT =          "/vpr/header-test";

    private final HttpRequest httpRequest;

    public DataController() {
        httpRequest = new HttpRequest();
    }

    public boolean login(String username, String password) {
        try {
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    SERVER_URL + LOGIN_ENDPOINT,
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
                    SERVER_URL + LOGIN_WITH_TOKEN_ENDPOINT,
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
                SERVER_URL + ADD_EVENT_ENDPOINT,
                event.getAsUrlParam(),
                true
        );
    }

    public void deleteEvent(int userId, int eventId, LocalDateTime date) throws HttpRequestException {
        sendBasicHttpRequest(
                SERVER_URL + DELETE_EVENT_ENDPOINT,
                "userId=" + userId + "&eventId=" + eventId + "&date=" + date.toLocalDate(),
                true
        );
    }

    public void editEvent(Event oldEvent, Event event) throws HttpRequestException {
        sendBasicHttpRequest(
                SERVER_URL + EDIT_EVENT_ENDPOINT,
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
        try {
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    SERVER_URL + ALL_EVENTS_ENDPOINT,
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
            return (ArrayList<Event>) objectMapper.readValue(jsonResponse, new TypeReference<List<Event>>() {
            });

        } catch (HttpRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpRequestException("Es konnte keine Verbindung mit dem Server hergestellt werden.", 600);
        }
    }

    /********
     * User *
     ********/

    public List<User> getAllUser() throws HttpRequestException {
        String userJSON = sendBasicHttpRequest(
                SERVER_URL + ALL_USER_ENDPOINT,
                "",
                true
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            List<User> list = objectMapper.readValue(userJSON, new TypeReference<>() {
            });

            for(User u : list){
                System.out.println(u);
            }
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void createUser(User user) throws HttpRequestException {
        sendBasicHttpRequest(
                SERVER_URL + ADD_USER_ENDPOINT,
                "name=" + user.getName() +
                        "&forename=" + user.getForename() +
                        "&login=" + user.getLogin() +
                        "&password=" + user.getPassword() +
                        "&isAdmin=" + user.isAdmin(),
                true
        );
    }

    public void deleteUser(User user) throws HttpRequestException {
        sendBasicHttpRequest(
                SERVER_URL + DELETE_USER_ENDPOINT,
                "userId=" + user.getUserId(),
                true
        );
    }

    public void editUser(User user) throws HttpRequestException {
        String urlParam = "userId=" + user.getUserId() +
                "&name=" + user.getName() +
                "&forename=" + user.getForename() +
                "&login=" + user.getLogin() +
                "&isAdmin=" + user.isAdmin() +
                (user.getPassword() == null ? "" : "&password=" + user.getPassword());

        System.out.println(urlParam);
        sendBasicHttpRequest(
                SERVER_URL + EDIT_USER_ENDPOINT,
                urlParam,
                true
        );
    }

    private String sendBasicHttpRequest(String urlString, String urlParameters, boolean sendAuth) throws HttpRequestException {
        try {
            Tuple<Integer, String> response = httpRequest.sendPostRequest(
                    urlString,
                    urlParameters,
                    sendAuth
            );
            if (response.getKey() != 200) {
                throw new HttpRequestException(response);
            }

            return response.getValue();
        } catch (HttpRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpRequestException("Es konnte keine Verbindung mit dem Server hergestellt werden.", 600);
        }
    }
}