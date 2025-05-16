package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import eu.deschler.dapro.Car.CarDao;
import eu.deschler.dapro.Carmodel.CarModelEntity;
import eu.deschler.dapro.Exception.CustomerNotFoundException;
import eu.deschler.dapro.Exception.DriverLicenseNotValidException;
import eu.deschler.dapro.Exception.FieldsNotFilledException;
import eu.deschler.dapro.Reservation.ReservationDao;
import eu.deschler.dapro.Reservation.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CarReservationForm  extends FormLayout {

    private static final Logger log = LoggerFactory.getLogger(CarReservationForm.class);
    private final Dialog dialog;
    private final ReservationDao reservationDao;
    private final CarDao carDao;
    private final CustomerDao customerDao;

    private Label label;
    private CarModelEntity carModel;
    private ReservationEntity reservation;

    private Dialog errorDialog;
    private Dialog successDialog;

    public boolean isReservationPossible(CarModelEntity carModel, Integer kundenNummer, LocalDateTime begin, LocalDateTime end) throws FieldsNotFilledException, CustomerNotFoundException, DriverLicenseNotValidException {

        if (carModel == null || kundenNummer == null || begin == null || end == null) {
            throw new FieldsNotFilledException("Eingabe unvollständig: Alle Felder müssen ausgefüllt sein.");
        }

        CustomerEntity customer = customerDao.findByCustomerNo(kundenNummer);
        if (customer == null) {
            throw new CustomerNotFoundException("Kunde nicht gefunden.");
        }

        List<String> licenses = customer.getLicenseClasses();
        String requiredLicense = carModel.getFuehrerschein();
        if (requiredLicense == null || !licenses.contains(requiredLicense)) {
            throw new DriverLicenseNotValidException("Führerschein nicht gültig für dieses Modell.");
        }

        int availableCars = carDao.countByModell(carModel.getId());

        List<ReservationEntity> existingReservations = reservationDao.findByIdAndTimeRange(carModel.getId(), begin, end);

        log.info("Anzahl der verfügbaren Autos: {}", availableCars);
        log.info("Anzahl der vorhandenen Reservierungen: {}", existingReservations.size());

        return availableCars > existingReservations.size();
    }

    public CarReservationForm(ReservationDao reservationDao, CarDao carDao, CustomerDao customerDao) {
        dialog = new Dialog();
        label = new Label();
        this.reservationDao = reservationDao;
        this.carDao = carDao;
        this.customerDao = customerDao;

        DatePicker startDatumPicker = new DatePicker("Startdatum");
        DatePicker endDatumPicker = new DatePicker("Enddatum");
        NumberField kundenNummerField = new NumberField("Kundennummer");

        Button reservierenButton = new Button("Reservieren", e -> {
            try {
                Integer kundenNummer = kundenNummerField.getValue() != null ? kundenNummerField.getValue().intValue() : null;
                LocalDateTime beginn = startDatumPicker.getValue() != null ? startDatumPicker.getValue().atStartOfDay() : null;
                LocalDateTime ende = endDatumPicker.getValue() != null ? endDatumPicker.getValue().atTime(23, 59, 59) : null;

                if (isReservationPossible(carModel, kundenNummer, beginn, ende)) {
                    reservation.setBeginn(beginn);
                    reservation.setEnde(ende);
                    reservation.setKundeID(kundenNummer);

                    this.reservationDao.add(reservation);
                    dialog.close();
                    openSuccessDialog("Die Reservierung wurde erfolgreich abgeschlossen.");
                } else {
                    dialog.close();
                    openErrorDialog("Es ist leider keine Reservierung für '" + carModel.getBezeichnung() +
                            "' im angegebenen Zeitraum möglich.");
                }
            } catch (FieldsNotFilledException fnfe) {
                openErrorDialog("Bitte füllen Sie alle Felder aus.");
                log.error(fnfe.getMessage());
            } catch (CustomerNotFoundException cnfe) {
                openErrorDialog("Es wurde kein Kunde mit der angegebenen Kundennummer gefunden.");
                log.error(cnfe.getMessage());
            } catch (DriverLicenseNotValidException dlnve) {
                openErrorDialog("Der angegebene Kunde besitzt nicht den erforderlichen Führerschein.");
                log.error(dlnve.getMessage());
            }
        });


        VerticalLayout dialogContent = new VerticalLayout(label, startDatumPicker, endDatumPicker, kundenNummerField, reservierenButton);
        dialog.add(dialogContent);
    }

    public void openDialog(CarModelEntity selectedCarModel) {
        dialog.open();
        reservation = new ReservationEntity();
        reservation.setModellID(selectedCarModel.getId());
        carModel = selectedCarModel;
        label.setText(carModel.getBezeichnung());
    }

    public void openErrorDialog(String message) {
        errorDialog = new Dialog();
        errorDialog.open();
        Label errorLabel = new Label(message);
        VerticalLayout dialogContent = new VerticalLayout(errorLabel);
        errorDialog.add(dialogContent);
    }

    public void openSuccessDialog(String message) {
        successDialog = new Dialog();
        successDialog.open();
        Label successLabel = new Label(message);
        VerticalLayout dialogContent = new VerticalLayout(successLabel);
        successDialog.add(dialogContent);
    }
}
