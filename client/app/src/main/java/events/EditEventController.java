package events;

import helper.HttpRequestException;
import container.DataController;
import container.Event;

import java.time.LocalTime;

public class EditEventController extends CreateEventController{

    private Event currentEvent;

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;

        textName.setText(currentEvent.getName());
        datePickerDate.setValue(currentEvent.getDate().toLocalDate());
        comboBoxPriority.getSelectionModel().select(currentEvent.getPriority());

        try{
            timeStart.setValue(LocalTime.parse(currentEvent.getStart()));
        }catch (Exception e){}


        try{
            timeEnd.setValue(LocalTime.parse(currentEvent.getEnd()));
        }catch (Exception e){}

        toggleBtnIsFullDay.setSelected(currentEvent.isFullDay());
        toggleBtnIsPrivate.setSelected(currentEvent.isPrivate());
    }

    @Override
    protected void sendHttpRequest(Event event) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.editEvent(currentEvent, event);
    }
}
