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

public class CarReservationForm extends FormLayout {

    private static final Logger log = LoggerFactory.getLogger(CarReservationForm.class);

    private final ReservationDao reservationDao;
    private final CarDao carDao;
    private final CustomerDao customerDao;

    private final Dialog dialog = new Dialog();
    private final Label label = new Label();

    private CarModelEntity carModel;
    private ReservationEntity reservation;

    public CarReservationForm(ReservationDao reservationDao, CarDao carDao, CustomerDao customerDao) {
        this.reservationDao = reservationDao;
        this.carDao = carDao;
        this.customerDao = customerDao;

        DatePicker startDatumPicker = new DatePicker("Startdatum");
        DatePicker endDatumPicker = new DatePicker("Enddatum");
        NumberField kundenNummerField = new NumberField("Kundennummer");

        startDatumPicker.addValueChangeListener(e -> {
            if (reservation != null && e.getValue() != null)
                reservation.setBeginn(e.getValue().atStartOfDay());
        });

        endDatumPicker.addValueChangeListener(e -> {
            if (reservation != null && e.getValue() != null)
                reservation.setEnde(e.getValue().atTime(23, 59, 59));
        });

        kundenNummerField.addValueChangeListener(e -> {
            if (reservation != null && e.getValue() != null)
                reservation.setKundeID(e.getValue().intValue());
        });

        Button reservierenButton = new Button("Reservieren", e -> handleReservation());

        dialog.add(new VerticalLayout(label, startDatumPicker, endDatumPicker, kundenNummerField, reservierenButton));
    }

    public void openDialog(CarModelEntity selectedCarModel) {
        reservation = new ReservationEntity();
        carModel = selectedCarModel;
        reservation.setModellID(carModel.getId());
        label.setText("Reservierung für: " + carModel.getBezeichnung());
        dialog.open();
    }

    private void handleReservation() {
        try {
            if (isReservationPossible(carModel, reservation.getKundeID(), reservation.getBeginn(), reservation.getEnde())) {
                reservationDao.add(reservation);
                dialog.close();
                showDialog("Erfolg", "Die Reservierung wurde erfolgreich abgeschlossen.");
            } else {
                dialog.close();
                showDialog("Nicht möglich", "Keine Reservierung für '" + carModel.getBezeichnung() + "' im gewählten Zeitraum möglich.");
            }
        } catch (FieldsNotFilledException | CustomerNotFoundException | DriverLicenseNotValidException ex) {
            log.error(ex.getMessage(), ex);
            showDialog("Fehler", ex.getMessage());
        }
    }

    private boolean isReservationPossible(CarModelEntity model, Integer kundenNummer, LocalDateTime begin, LocalDateTime end)
            throws FieldsNotFilledException, CustomerNotFoundException, DriverLicenseNotValidException {

        if (model == null || kundenNummer == null || begin == null || end == null)
            throw new FieldsNotFilledException("Bitte füllen Sie alle Felder aus.");

        CustomerEntity customer = customerDao.findByCustomerNo(kundenNummer);
        if (customer == null)
            throw new CustomerNotFoundException("Kunde nicht gefunden.");

        String requiredLicense = model.getFuehrerschein();
        Set<String> licenses = customer.getDrivingLicenseClasses();

        if (requiredLicense == null || !licenses.contains(requiredLicense))
            throw new DriverLicenseNotValidException("Führerschein nicht gültig für dieses Modell.");

        int available = carDao.countByModell(model.getId());
        int reserved = reservationDao.findByIdAndTimeRange(model.getId(), begin, end).size();

        log.info("Verfügbare Fahrzeuge: {}, Bereits reserviert: {}", available, reserved);
        return available > reserved;
    }

    private void showDialog(String title, String message) {
        Dialog infoDialog = new Dialog();
        infoDialog.add(new VerticalLayout(new Label(title), new Label(message)));
        infoDialog.open();
    }
}
