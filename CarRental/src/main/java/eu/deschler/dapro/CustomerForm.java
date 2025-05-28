package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CustomerForm extends FormLayout{
    private static final long serialVersionUID = 1L;

    private final CustomerView customerView;

    private final TextField firstName = new TextField("Vorname");
    private final TextField lastName = new TextField("Name");
    private final DatePicker dateOfBirth = new DatePicker("Geburtsdatum");
    private final CheckboxGroup<String> driverLicenseClasses = new CheckboxGroup<>();
    private final Button save = new Button("Speichern");
    private final Button delete = new Button("Löschen");

    private final Binder<CustomerEntity> binder = new Binder<>(CustomerEntity.class);

    private final CustomerDao dao;

    public CustomerForm(CustomerView customerView, CustomerDao dao) {
        // status.setItems(CustomerStatus.values());
        this.customerView = customerView;
        this.dao = dao;
        driverLicenseClasses.setLabel("Führerscheinklassen");
        driverLicenseClasses.setItems("A", "B", "C", "D");
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(firstName, lastName, dateOfBirth, driverLicenseClasses, buttons);

        binder.forField(driverLicenseClasses)
                .bind(CustomerEntity::getDrivingLicenseClasses, CustomerEntity::setDrivingLicenseClasses);
        binder.bindInstanceFields(this);
        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
    }

    public void setCustomer(CustomerEntity customerEntity) {
        binder.setBean(customerEntity);

        if (customerEntity == null) {
            setVisible(false);
        } else {
            setVisible(true);
            firstName.focus();
        }
    }

    private void save() {
        CustomerEntity customerEntity = binder.getBean();
        dao.updateCustomer(customerEntity);
        customerView.updateList(null);
        setCustomer(null);
    }

    private void delete() {
        CustomerEntity customerEntity = binder.getBean();
        dao.deleteCustomer(customerEntity);
        customerView.updateList(null);
        setCustomer(null);
    }

}