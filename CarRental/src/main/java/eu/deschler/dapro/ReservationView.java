package eu.deschler.dapro;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import eu.deschler.dapro.Reservation.ReservationDao;
import eu.deschler.dapro.Reservation.ReservationEntity;

public class ReservationView extends VerticalLayout {
    private static final long serialVersionUID = 829384928349823L;
    private ReservationDao reservationDao;
    private Grid<ReservationEntity> grid;

    private DataProvider<ReservationEntity, String> dp =
            DataProvider.fromFilteringCallbacks(
                    query -> reservationDao.findAll().stream(),
                    query -> reservationDao.count()
            );

    private ConfigurableFilterDataProvider<ReservationEntity, Void, String> configurableFilterDataProvider =
            dp.withConfigurableFilter();

    public ReservationView(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        grid = new Grid<>();

        grid.setDataProvider(configurableFilterDataProvider);
        grid.addColumn(ReservationEntity::getId).setHeader("Id").setSortable(true);
        grid.addColumn(ReservationEntity::getKundeID).setHeader("KundeId").setSortable(true);
        grid.addColumn(ReservationEntity::getModellID).setHeader("ModellId").setSortable(true);
        grid.addColumn(ReservationEntity::getBeginn).setHeader("Beginn").setSortable(true);
        grid.addColumn(ReservationEntity::getEnde).setHeader("Ende").setSortable(true);

        HorizontalLayout mainContent = new HorizontalLayout(grid);
        mainContent.setSizeFull();
        grid.setWidthFull();
        add(mainContent);
    }
}
