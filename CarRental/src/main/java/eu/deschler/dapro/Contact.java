package eu.deschler.dapro;

public class Contact {
    private Address address;
    private String email;
    private String phone;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact [address=" + address + ", email=" + email + ", phone=" + phone + "]";
    }
}