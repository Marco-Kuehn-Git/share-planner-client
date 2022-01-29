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

        textName.setText(currentEvent.getName());
        datePickerDate.setValue(currentEvent.getDate().toLocalDate());
        ComboBoxPriotity.getSelectionModel().select(currentEvent.getPriority());

        //timeEnd.setValue(currentEvent.getEnd());
    }

    @Override
    protected void sendHttpRequest(Event event) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.editEvent(currentEvent, event);
    }
}
