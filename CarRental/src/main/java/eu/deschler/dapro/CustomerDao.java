package eu.deschler.dapro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Objects;

@Component
public class CustomerDao {
    private final CustomerRepository customerRepository;
    private String filter = "";

    @Autowired
    public CustomerDao(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    List<CustomerEntity> fetchCustomers(String filter) {
        if (filter == null || filter.isEmpty()) {
            return StreamSupport.stream(customerRepository
                                    .findAll().spliterator(),
                            false)
                    .collect(Collectors.toList());
        } else {
            return StreamSupport.stream(customerRepository
                                    .findAll().spliterator(),
                            false)
                    .filter(c -> c.getLastName().equals(filter))
                    .collect(Collectors.toList());
        }
    }

    List<CustomerEntity> fetchAllCustomers() {
        return StreamSupport.stream(customerRepository
                                .findAll().spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    List<CustomerEntity> fetchCustomers(String filter, int offset, int limit) {
        Pageable p = PageRequest.of(offset / limit, limit);
        if(filter != null && !filter.isEmpty()) {
            return customerRepository
                    .findAllByLastNameContainsIgnoreCase(filter, p)
                    .getContent();
        } else {
            return customerRepository
                    .findAll(p)
                    .getContent();
        }
    }

    int getCustomerCount(String filter) {
        if(filter != null && !filter.isEmpty()) {
            return (int) customerRepository.countByLastNameContainsIgnoreCase(filter);
        } else {
            return (int) customerRepository.count();
        }

    }

    void deleteCustomer(CustomerEntity c) {
        customerRepository.delete(c);
    }
    private Integer generateNextCustomerNo() {
        List<CustomerEntity> allCustomers = StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return allCustomers.stream()
                .map(CustomerEntity::getCustomerNo)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .map(maxNo -> maxNo + 1)
                .orElse(1); // Start with 1000 if no customers exist
    }

    void updateCustomer(CustomerEntity c) {
        if (c.getCustomerNo() == null) {
            c.setCustomerNo(generateNextCustomerNo());
        }
        customerRepository.save(c);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
