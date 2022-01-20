package main;

import helper.HttpRequestException;
import res.DataController;
import res.Event;

public class EditEventController extends CreateEventController{

    private Event currentEvent;

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    @Override
    protected void sendHttpRequest(Event event) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.deleteEvent(currentEvent.getOwnerId(), currentEvent.getId(), currentEvent.getDate());
        dataController.createEvent(event);
    }
}
