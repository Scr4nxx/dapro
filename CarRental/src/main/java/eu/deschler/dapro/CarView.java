package eu.deschler.dapro;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import eu.deschler.dapro.Car.CarDao;
import eu.deschler.dapro.CarType.CarTypeDao;
import eu.deschler.dapro.CarType.CarTypeEntity;
import eu.deschler.dapro.Carmodel.CarModelDao;
import eu.deschler.dapro.Carmodel.CarModelEntity;
import eu.deschler.dapro.Reservation.ReservationDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarView extends VerticalLayout {
    private static final long serialVersionUID = -5150635303857129026L;

    private CarModelDao carModelDao;
    private CarTypeDao carTypeDao;
    private Map<Integer, String> autoartMap;
    private CarFilter carFilter;
    private Grid<CarModelEntity> grid;
    private CarReservationForm form;

    private DataProvider<CarModelEntity, CarFilter> dp = DataProvider.fromFilteringCallbacks(
            query -> {
                CarFilter filter = query.getFilter().orElse(new CarFilter());
                return carModelDao.findAll().stream()
                        .filter(car -> filter.searchText.isEmpty() ||
                                car.getBezeichnung().toLowerCase().contains(filter.searchText.toLowerCase()) ||
                                car.getHersteller().toLowerCase().contains(filter.searchText.toLowerCase()))
                        .filter(car -> filter.selectedAutoart == null ||
                                car.getAutoart().equals(filter.selectedAutoart))
                        .filter(car -> filter.sitzplaetze == null ||
                                car.getSitzplaetze().equals(filter.sitzplaetze))
                        .filter(car -> filter.treibstoff.isEmpty() ||
                                car.getTreibstoff().equalsIgnoreCase(filter.treibstoff));
            },
            query -> (int) carModelDao.findAll().stream()
                    .filter(car -> query.getFilter().map(filter ->
                            (filter.searchText.isEmpty() ||
                                    car.getBezeichnung().toLowerCase().contains(filter.searchText.toLowerCase()) ||
                                    car.getHersteller().toLowerCase().contains(filter.searchText.toLowerCase()))
                                    &&
                                    (filter.selectedAutoart == null || car.getAutoart().equals(filter.selectedAutoart))
                                    &&
                                    (filter.sitzplaetze == null || car.getSitzplaetze().equals(filter.sitzplaetze))
                                    &&
                                    (filter.treibstoff.isEmpty() || car.getTreibstoff().equalsIgnoreCase(filter.treibstoff))
                    ).orElse(true))
                    .count()
    );

    private ConfigurableFilterDataProvider<CarModelEntity, Void, CarFilter> configurableFilterDataProvider =
            dp.withConfigurableFilter();

    public CarView(CarModelDao carModelDao, CarTypeDao carTypeDao, ReservationDao reservationDao, CarDao carDao, CustomerDao customerDao) {
        this.carModelDao = carModelDao;
        this.carTypeDao = carTypeDao;
        autoartMap = this.carTypeDao.findAll().stream()
                .collect(Collectors.toMap(CarTypeEntity::getId, CarTypeEntity::getArt));
        this.grid = new Grid<>();
        this.carFilter = new CarFilter();

        configurableFilterDataProvider.setFilter(carFilter);

        TextField searchField = new TextField("Bezeichnung oder Hersteller");
        searchField.setPlaceholder("Suche...");

        List<String> autoartOptions = new ArrayList<>();
        autoartOptions.add("Alle");
        autoartOptions.addAll(autoartMap.values());

        ComboBox<String> autoartFilter = new ComboBox<>("Autoart");
        autoartFilter.setItems(autoartOptions);
        autoartFilter.setValue("Alle");

        NumberField sitzplaetzeFilter = new NumberField("Sitzplätze");
        ComboBox<String> treibstoffFilter = new ComboBox<>("Treibstoff");
        List<String> treibstoffOptions = carModelDao.findAll().stream()
                .map(CarModelEntity::getTreibstoff)
                .filter(t -> t != null && !t.isEmpty()) // Filtert NULL und leere Werte raus
                .distinct() // Entfernt doppelte Einträge
                .sorted(String::compareTo) // Sortiert alphabetisch
                .collect(Collectors.toList());
        treibstoffOptions.add(0, "Alle"); // Fügt "Alle" als erste Option hinzu
        treibstoffFilter.setItems(treibstoffOptions);
        treibstoffFilter.setValue("Alle");
        searchField.addValueChangeListener(event -> {
            carFilter.searchText = event.getValue().trim().toLowerCase();
            configurableFilterDataProvider.refreshAll();
        });

        autoartFilter.addValueChangeListener(event -> {
            String selectedArt = event.getValue();
            carFilter.selectedAutoart = "Alle".equals(selectedArt) ? null :
                    autoartMap.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(selectedArt))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);
            configurableFilterDataProvider.refreshAll();
        });

        sitzplaetzeFilter.addValueChangeListener(event -> {
            carFilter.sitzplaetze = event.getValue() == null ? null : event.getValue().intValue();
            configurableFilterDataProvider.refreshAll();
        });

        treibstoffFilter.addValueChangeListener(event -> {
            carFilter.treibstoff = "Alle".equals(event.getValue()) ? "" : event.getValue();
            configurableFilterDataProvider.refreshAll();
        });

        grid.setDataProvider(configurableFilterDataProvider);

        grid.addColumn(CarModelEntity::getId).setHeader("Id");
        grid.addColumn(CarModelEntity::getBezeichnung).setHeader("Bezeichnung");
        grid.addColumn(CarModelEntity::getHersteller).setHeader("Hersteller");
        grid.addColumn(CarModelEntity::getAutoartBezeichnung).setHeader("Autoart");
        grid.addColumn(CarModelEntity::getSitzplaetze).setHeader("Anzahl Sitzplätze");
        grid.addColumn(CarModelEntity::getKw).setHeader("Leistung in kW");
        grid.addColumn(CarModelEntity::getTreibstoff).setHeader("Treibstoffart");
        grid.addColumn(CarModelEntity::getPreisTag).setHeader("Preis pro Tag");
        grid.addColumn(CarModelEntity::getPreisKM).setHeader("Preis pro Kilometer");
        grid.addColumn(CarModelEntity::getAchsen).setHeader("Anzahl Achsen");
        grid.addColumn(CarModelEntity::getLadevolumen).setHeader("Ladevolumen");
        grid.addColumn(CarModelEntity::getZuladung).setHeader("Zuladung");
        grid.addColumn(CarModelEntity::getFuehrerschein).setHeader("erforderlicher Führerschein");
        grid.addItemClickListener(e -> {
            CarModelEntity selectedCarModel = e.getItem();
            if (selectedCarModel != null) {
                form = new CarReservationForm(reservationDao, carDao, customerDao);
                add(form);
                form.openDialog(selectedCarModel);
            }
        });

        HorizontalLayout filterContent = new HorizontalLayout(searchField, autoartFilter, sitzplaetzeFilter, treibstoffFilter);
        HorizontalLayout mainContent = new HorizontalLayout(grid);
        mainContent.setSizeFull();
        grid.setWidthFull();
        add(filterContent, mainContent);
    }
}
