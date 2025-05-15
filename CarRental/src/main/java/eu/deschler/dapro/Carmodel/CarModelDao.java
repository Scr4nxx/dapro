package eu.deschler.dapro.Carmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarModelDao {
    private final CarModelRepository carModelRepository;

    @Autowired
    public CarModelDao(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public List<CarModelEntity> findAll() {
       return this.carModelRepository.findAll();
    }
}
