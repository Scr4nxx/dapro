package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CustomerForm extends FormLayout {
    private static final long serialVersionUID = 1L;

    private final TextField firstName = new TextField("Vorname");
    private final TextField lastName = new TextField("Name");
    private final DatePicker dateOfBirth = new DatePicker("Geburtsdatum");
    private final CheckboxGroup<String> driverLicenseClasses = new CheckboxGroup<>();
    private final Button save = new Button("Speichern", e -> save());
    private final Button delete = new Button("Löschen", e -> delete());
    private final HorizontalLayout buttons = new HorizontalLayout(save, delete);

    private final Binder<CustomerEntity> binder = new Binder<>(CustomerEntity.class);
    private final CustomerView customerView;
    private final CustomerDao dao;


    public CustomerForm(CustomerView customerView, CustomerDao dao) {
        this.customerView = customerView;
        this.dao = dao;
        driverLicenseClasses.setLabel("Führerscheinklassen");
        driverLicenseClasses.setItems("A", "B", "C", "D");
        HorizontalLayout buttons = new HorizontalLayout(save, delete);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(firstName, lastName, dateOfBirth, driverLicenseClasses, buttons);

        binder.bindInstanceFields(this);
        binder.forField(driverLicenseClasses)
                .bind(CustomerEntity::getDrivingLicenseClasses, CustomerEntity::setDrivingLicenseClasses);
    }

    public void setCustomer(CustomerEntity customer) {
        binder.setBean(customer);
        setVisible(customer != null);
        if (customer != null) firstName.focus();
    }

    private void save() {
        dao.updateCustomer(binder.getBean());
        customerView.updateList(null);
        setCustomer(null);
    }

    private void delete() {
        dao.deleteCustomer(binder.getBean());
        customerView.updateList(null);
        setCustomer(null);
    }
}