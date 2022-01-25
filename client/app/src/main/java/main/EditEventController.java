//Marc Beyer//
package main;

import customUI.Converter;
import helper.HttpRequestException;
import javafx.fxml.FXML;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import res.DataController;
import res.Event;

import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;

public class EditEventController extends CreateEventController{

    private Event currentEvent;

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;

        textName.setText(Converter.convertString(currentEvent.getName()));
        datePickerDate.setValue(currentEvent.getDate().toLocalDate());
        ComboBoxPriotity.getSelectionModel().select(currentEvent.getPriority());

        //timeEnd.setValue(currentEvent.getEnd());
    }




    @Override
    protected void sendHttpRequest(Event event) throws HttpRequestException {
        DataController dataController = new DataController();
        dataController.deleteEvent(currentEvent.getOwnerId(), currentEvent.getId(), currentEvent.getDate());
        dataController.createEvent(event);
    }
}
