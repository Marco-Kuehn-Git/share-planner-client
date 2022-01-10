package res;

import com.sun.jdi.event.StepEvent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Event {

    private int id;
    private String name;
    private int priority;
    private boolean isFullDay;
    private boolean isPrivate;
    private String start;
    private String end;

    private LocalDateTime date;

    private int ownerId;
    private String ownerName;

    /*
    Constructor for SELECT:
    e.id AS eid,
    e.name AS ename,
    e.start,
    e.end,
    e.priority,
    e.is_full_day,

    ue.date,

    u.id AS uid,
    u.forename,
    u.name AS uname
     */

    public Event(ArrayList<Object> arr) {
        id = (int) arr.get(0);
        name = (String) arr.get(1);
        start = (String) arr.get(2);
        end = (String) arr.get(3);
        priority = (int) arr.get(4);
        isFullDay = (Boolean) arr.get(5); //((String)arr.get(5)).equals("true");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        date = LocalDateTime.parse(arr.get(6) + " 00:00", formatter);

        ownerId = (int) arr.get(7);
        ownerName = arr.get(8) + " " + arr.get(9);
    }

    public Event(String name,
                 int priority,
                 boolean isFullDay,
                 boolean isPrivate,
                 String start,
                 String end,
                 LocalDateTime date,
                 int ownerId
    ) throws IllegalArgumentException{
        if(name.length() < 3){
            throw new IllegalArgumentException("Der Name muss eine L\u00e4nge von 3 haben.");
        }
        Pattern pattern = Pattern.compile("[A-Za-zÄÖÜäöü0-9 =!?+*/$%€.:,;_<>()-]*");
        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()){
            throw new IllegalArgumentException("Der Name darf nur aus Zahlen, Buchstaben und folgenden Sonderzeichen bestehen: =!?+*/$%€.:,;_ <>()-");
        }
        if(priority < 0){
            throw new IllegalArgumentException("Bitte eine Priorit\u00e4t w\u00e4hlen.");
        }
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        if(Duration.between(today, date).isNegative()){
            throw new IllegalArgumentException("Das Datum muss in der Zukunft liegen.");
        }

        this.name = name;
        this.priority = priority;
        this.isFullDay = isFullDay;
        this.isPrivate = isPrivate;
        this.start = start;
        this.end = end;
        this.date = date;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isFullDay() {
        return isFullDay;
    }

    public void setFullDay(boolean fullDay) {
        isFullDay = fullDay;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return name +
                "\nVon: " + start +
                "\nBis: " + start +
                (isFullDay ? "\nDen ganzen Tag lang" : "");

    }

    public String getAsUrlParam() {
        return "userId=" + getOwnerId() +
                "&date=" + getDate().toLocalDate() +
                "&name=" + getName() +
                "&start=" + getStart() +
                "&end=" + getEnd() +
                "&prority=" + getPriority() +
                "&isFullDay=" + isFullDay() +
                "&isPrivate=" + isPrivate();
    }
}
