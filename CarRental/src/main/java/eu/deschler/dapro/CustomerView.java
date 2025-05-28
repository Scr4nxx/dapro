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
    private Button addCustomerButton = new Button("Neuen Kunden hinzufügen");

    private DataProvider<CustomerEntity, String> dp =
        DataProvider.fromFilteringCallbacks(
            // First callback fetches items based on a query
            query -> {
                // The index of the first item to load
                int offset = query.getOffset();
                // The number of items to load
                int limit = query.getLimit();
                String filter = query.getFilter().orElse(null);

                List<CustomerEntity> persons = dao
                        .fetchCustomers(filter, offset, limit);

                return persons.stream();
            },
            // Second callback fetches the number of items
            // for a query
            query -> dao.getCustomerCount(query.getFilter().orElse(null))
        );

    private ConfigurableFilterDataProvider<CustomerEntity, Void, String> configurableFilterDataProvider =
        dp.withConfigurableFilter();

    public CustomerView(CustomerDao customerDao) {
        this.dao = customerDao;
        this.form = new CustomerForm(this, customerDao);
        filterText.setPlaceholder("Suche nach Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList(e.getValue()));

        addCustomerButton.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new CustomerEntity());
        });

        grid.setDataProvider(configurableFilterDataProvider);
        grid.addColumn(CustomerEntity::getCustomerNo).setHeader("Kundennr.");
        grid.addColumn(CustomerEntity::getLastName).setHeader("Name");
        grid.addColumn(CustomerEntity::getFirstName).setHeader("Vorname");
        grid.addColumn(new LocalDateRenderer<>(CustomerEntity::getDateOfBirth, "dd.MM.yyyy")).setHeader("Geburtsdatum");
        grid.asSingleSelect().addValueChangeListener(e ->
            form.setCustomer(grid.asSingleSelect().getValue())
        );
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setWidthFull();
        add(filterText, mainContent, addCustomerButton);
        updateList(null);
        form.setCustomer(null);
    }

    public void updateList(String value) {
        configurableFilterDataProvider.setFilter(value);
        configurableFilterDataProvider.refreshAll();
        //grid.setItems(service.fetchCustomers(filterText.getValue()));
    }
}
