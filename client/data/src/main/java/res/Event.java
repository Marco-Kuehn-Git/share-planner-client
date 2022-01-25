package res;

import com.sun.jdi.event.StepEvent;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public Event() {}

    public Event(String name,
                 int priority,
                 boolean isFullDay,
                 boolean isPrivate,
                 LocalTime start,
                 LocalTime end,
                 LocalDateTime date,
                 int ownerId
    ) throws IllegalArgumentException {

        System.out.println("Create Event");
        if (name.length() < 3) {
            throw new IllegalArgumentException("Der Name muss eine L\u00e4nge von 3 haben.");
        }
        Pattern pattern = Pattern.compile("[A-Za-z\u00e4\u00f6\u00fc\u00c4\u00d6\u00dc\u00df0-9 =!?+*/$.:,;_<>()-]*");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Der Name darf nur aus Zahlen, Buchstaben und folgenden Sonderzeichen bestehen: \u00e4\u00f6\u00fc \u00c4\u00d6\u00dc \u00df =!?+*/$.:,;_ <>()-");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("Bitte eine Priorit\u00e4t w\u00e4hlen.");
        }
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        if (Duration.between(today, date).isNegative()) {
            throw new IllegalArgumentException("Das Datum muss in der Zukunft liegen.");
        }

        this.name = name;
        this.priority = priority;
        this.isFullDay = isFullDay;
        this.isPrivate = isPrivate;
        if (start != null) this.start = start.toString();
        if (start != null) this.end = end.toString();
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
        System.out.println(name);
        this.name = name;
        System.out.println(this.name);
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

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.date = LocalDateTime.parse(date + " 00:00", formatter);
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
                "&priority=" + getPriority() +
                "&isFullDay=" + isFullDay() +
                "&isPrivate=" + isPrivate();
    }
}
