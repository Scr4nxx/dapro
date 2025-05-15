package eu.deschler.dapro;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import eu.deschler.dapro.Car.CarDao;
import eu.deschler.dapro.CarType.CarTypeDao;
import eu.deschler.dapro.Carmodel.CarModelDao;
import eu.deschler.dapro.Reservation.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout{

    private static final long serialVersionUID = -7852974499417275175L;
    @Autowired
    public MainView(CustomerDao customerDao, CarModelDao carmodelDao, CarTypeDao carTypeDao, ReservationDao reservationDao, CarDao carDao) {
        CarView carView = new CarView(carmodelDao, carTypeDao, reservationDao, carDao, customerDao);
        CustomerView customerView = new CustomerView(customerDao);
        ReservationView reservationView = new ReservationView(reservationDao);
        Tab tabCustomers = new Tab("Kunden");
        Tab tabCars = new Tab("Fahrzeuge");
        Tab tabReservations = new Tab("Reservierungen");
        Tabs tabs = new Tabs(tabCustomers, tabCars, tabReservations);
        Map<Tab,Component> tabMap = new HashMap<>();
        tabMap.put(tabCustomers, customerView);
        tabMap.put(tabCars, carView);
        tabMap.put(tabReservations, reservationView);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(
                e -> {
                    customerView.setVisible(false);
                    carView.setVisible(false);
                    reservationView.setVisible(false);
                    Component c = tabMap.get(e.getSelectedTab());
                    if (c!=null) {
                        c.setVisible(true);
                    }
                }
        );
        tabs.setSelectedTab(tabCustomers);
        carView.setVisible(false);
        reservationView.setVisible(false);
        add(tabs, customerView, carView, reservationView);
    }
}