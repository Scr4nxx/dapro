package eu.deschler.dapro.Car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarDao {
    private final CarRepository carRepository;

    @Autowired
    public CarDao(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarEntity> findAll() {
        return this.carRepository.findAll();
    }

    public int countByModell(int modelId) {
        return this.carRepository.countByModell(modelId);
    }
}
