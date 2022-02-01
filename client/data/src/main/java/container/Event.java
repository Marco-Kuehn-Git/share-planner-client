package container;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            throw new IllegalArgumentException("Der Name muss eine Länge von 3 haben.");
        }
        Pattern pattern = Pattern.compile("[A-Za-zäöüÄÖÜß0-9 =!?+*/$.:,;_<>()-]*");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Der Name darf nur aus Zahlen, Buchstaben und folgenden Sonderzeichen bestehen: äöü ÄÖÜ ß =!?+*/$.:,;_ <>()-");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("Bitte eine Priorität wählen.");
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
        if (end != null) this.end = end.toString();
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
