package eu.deschler.dapro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, String> {
    Optional<CustomerEntity> findByCustomerNo(Integer customerNo);
    Page<CustomerEntity> findAllByLastNameContainsIgnoreCase(String filter, Pageable pageable);
    long countByLastNameContainsIgnoreCase(String filter);
    @Override
    @NonNull
    Page<CustomerEntity> findAll(@NonNull Pageable pageable);
}
