package eu.deschler.dapro;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "customers")
public class CustomerEntity {
    @Id
    private ObjectId id;
    @Field(name = "customer_no")
    private Integer customerNo;
    private String gender;
    @Field(name = "first_name")
    private String firstName;
    @Field(name = "last_name")
    private String lastName;
    private Contact contact;
    @Field(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Field(name = "driving_license_classes")
    private Set<String> drivingLicenseClasses = new HashSet<>();

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId objectId) {
        this.id = objectId;
    }

    public Integer getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(Integer customerNo) {
        this.customerNo = customerNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<String> getDrivingLicenseClasses() {
        return drivingLicenseClasses;
    }

    public void setDrivingLicenseClasses(Set<String> drivingLicenseClasses) {
        this.drivingLicenseClasses = drivingLicenseClasses != null ? drivingLicenseClasses : new HashSet<>();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Customer ["
                + "contact=" + contact
                + ", customerNo=" + customerNo
                + ", dateOfBirth=" + sdf.format(dateOfBirth)
                + ", firstName=" + firstName
                + ", gender=" + gender
                + ", lastName=" + lastName
                + ", drivingLicenseClasses=" + drivingLicenseClasses
                + ", objectId=" + id
                + "]";
    }

}