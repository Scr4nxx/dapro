package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

public class CustomerView extends VerticalLayout {
    private static final long serialVersionUID = -70572001777680901L;
    private CustomerDao dao;
    private Grid<CustomerEntity> grid = new Grid<>();
    private TextField filterText = new TextField();
    private CustomerForm form;

    private DataProvider<CustomerEntity, String> dp =
            DataProvider.fromFilteringCallbacks(
                    query -> {
                        int offset = query.getOffset();
                        int limit = query.getLimit();
                        String filter = query.getFilter().orElse(null);
                        List<CustomerEntity> persons = dao.fetchCustomers(filter, offset, limit);
                        return persons.stream();
                    },
                    query -> dao.getCustomerCount(query.getFilter().orElse(null))
            );

    private ConfigurableFilterDataProvider<CustomerEntity, Void, String> configurableFilterDataProvider =
            dp.withConfigurableFilter();

    public CustomerView(CustomerDao customerDao) {
        this.dao = customerDao;
        this.form = new CustomerForm(this, customerDao);

        // Suchfeld konfigurieren
        filterText.setPlaceholder("Suche nach Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList(e.getValue()));

        // Neuer Kunde Button
        Button addCustomerButton = new Button("Neuer Kunde");
        addCustomerButton.addClickListener(e -> form.setCustomer(new CustomerEntity()));

        // Toolbar mit Filter & Button
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerButton);

        // Grid konfigurieren
        grid.setDataProvider(configurableFilterDataProvider);
        grid.addColumn(CustomerEntity::getCustomerNo).setHeader("Kundennr.");
        grid.addColumn(CustomerEntity::getLastName).setHeader("Name");
        grid.addColumn(CustomerEntity::getFirstName).setHeader("Vorname");
        grid.addColumn(new LocalDateRenderer<>(CustomerEntity::getDateOfBirth, "dd.MM.yyyy"))
                .setHeader("Geburtsdatum");

        grid.asSingleSelect().addValueChangeListener(e ->
                form.setCustomer(grid.asSingleSelect().getValue())
        );

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setWidthFull();

        // Layout zusammensetzen
        add(toolbar, mainContent);

        updateList(null);
        form.setCustomer(null);
    }

    public void updateList(String value) {
        configurableFilterDataProvider.setFilter(value);
        configurableFilterDataProvider.refreshAll();
    }
}
