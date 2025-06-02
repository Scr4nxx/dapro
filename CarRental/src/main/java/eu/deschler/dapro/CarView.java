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

import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class CarView extends VerticalLayout {

    private final CarModelDao carModelDao;
    private final Map<Integer, String> autoartMap;
    private final CarFilter carFilter = new CarFilter();
    private final Grid<CarModelEntity> grid = new Grid<>();
    private final ConfigurableFilterDataProvider<CarModelEntity, Void, CarFilter> filterProvider;

    public CarView(CarModelDao carModelDao, CarTypeDao carTypeDao, ReservationDao reservationDao, CarDao carDao, CustomerDao customerDao) {
        this.carModelDao = carModelDao;

        autoartMap = carTypeDao.findAll().stream()
                .collect(Collectors.toMap(CarTypeEntity::getId, CarTypeEntity::getArt));
        // Datenquelle mit Filter
        DataProvider<CarModelEntity, CarFilter> dataProvider = DataProvider.fromFilteringCallbacks(
                (query) -> carModelDao.findAll().stream()
                        .filter(car -> matchesFilter(car, query.getFilter().orElse(new CarFilter()))),
                (query) -> (int) carModelDao.findAll().stream()
                        .filter(car -> matchesFilter(car, query.getFilter().orElse(new CarFilter()))).count()
        );

        this.filterProvider = dataProvider.withConfigurableFilter();
        filterProvider.setFilter(carFilter);

        // UI
        HorizontalLayout filterBar = createFilterBar();
        setupGrid(reservationDao, carDao, customerDao);

        add(filterBar, grid);
        setSizeFull();
    }

    private boolean matchesFilter(CarModelEntity car, CarFilter filter) {
        return (filter.searchText.isEmpty() || car.getBezeichnung().toLowerCase().contains(filter.searchText.toLowerCase())
                || car.getHersteller().toLowerCase().contains(filter.searchText.toLowerCase()))
                && (filter.selectedAutoart == null || car.getAutoart().equals(filter.selectedAutoart))
                && (filter.sitzplaetze == null || Objects.equals(car.getSitzplaetze(), filter.sitzplaetze))
                && (filter.treibstoff.isEmpty() || filter.treibstoff.equalsIgnoreCase(car.getTreibstoff()));
    }

    private HorizontalLayout createFilterBar() {
        // Textsuche
        TextField searchField = new TextField("Bezeichnung / Hersteller");
        searchField.setPlaceholder("Suche...");
        searchField.addValueChangeListener(e -> {
            carFilter.searchText = e.getValue().trim().toLowerCase();
            refresh();
        });

        // Autoart-Filter
        ComboBox<String> autoartFilter = new ComboBox<>("Autoart");
        autoartFilter.setItems(getAutoartOptions());
        autoartFilter.setValue("Alle");
        autoartFilter.addValueChangeListener(e -> {
            carFilter.selectedAutoart = "Alle".equals(e.getValue()) ? null :
                    autoartMap.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(e.getValue()))
                            .map(Map.Entry::getKey)
                            .findFirst().orElse(null);
            refresh();
        });

        // Sitzplätze
        NumberField sitzplaetzeFilter = new NumberField("Sitzplätze");
        sitzplaetzeFilter.setStep(1);
        sitzplaetzeFilter.addValueChangeListener(e -> {
            carFilter.sitzplaetze = e.getValue() == null ? null : e.getValue().intValue();
            refresh();
        });

        // Treibstoff
        ComboBox<String> treibstoffFilter = new ComboBox<>("Treibstoff");
        List<String> treibstoffOptions = carModelDao.findAll().stream()
                .map(CarModelEntity::getTreibstoff)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        treibstoffOptions.add(0, "Alle");
        treibstoffFilter.setItems(treibstoffOptions);
        treibstoffFilter.setValue("Alle");
        treibstoffFilter.addValueChangeListener(e -> {
            carFilter.treibstoff = "Alle".equals(e.getValue()) ? "" : e.getValue();
            refresh();
        });

        return new HorizontalLayout(searchField, autoartFilter, sitzplaetzeFilter, treibstoffFilter);
    }

    private List<String> getAutoartOptions() {
        List<String> options = new ArrayList<>();
        options.add("Alle");
        options.addAll(autoartMap.values());
        return options;
    }

    private void setupGrid(ReservationDao reservationDao, CarDao carDao, CustomerDao customerDao) {
        grid.setDataProvider(filterProvider);
        grid.setWidthFull();

        grid.addColumn(CarModelEntity::getId).setHeader("Id");
        grid.addColumn(CarModelEntity::getBezeichnung).setHeader("Bezeichnung");
        grid.addColumn(CarModelEntity::getHersteller).setHeader("Hersteller");
        grid.addColumn(CarModelEntity::getAutoartBezeichnung).setHeader("Autoart");
        grid.addColumn(CarModelEntity::getSitzplaetze).setHeader("Sitzplätze");
        grid.addColumn(CarModelEntity::getKw).setHeader("kW");
        grid.addColumn(CarModelEntity::getTreibstoff).setHeader("Treibstoff");
        grid.addColumn(CarModelEntity::getPreisTag).setHeader("Preis/Tag");
        grid.addColumn(CarModelEntity::getPreisKM).setHeader("Preis/KM");
        grid.addColumn(CarModelEntity::getAchsen).setHeader("Achsen");
        grid.addColumn(CarModelEntity::getLadevolumen).setHeader("Ladevolumen");
        grid.addColumn(CarModelEntity::getZuladung).setHeader("Zuladung");
        grid.addColumn(CarModelEntity::getFuehrerschein).setHeader("Führerschein");

        grid.addItemClickListener(e -> {
            CarReservationForm form = new CarReservationForm(reservationDao, carDao, customerDao);
            form.openDialog(e.getItem());
        });
    }

    private void refresh() {
        filterProvider.refreshAll();
    }

}
