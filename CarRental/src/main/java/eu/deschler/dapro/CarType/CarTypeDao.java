package eu.deschler.dapro.CarType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarTypeDao {
    private final CarTypeRepository carTypeRepository;

    @Autowired
    public CarTypeDao(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public List<CarTypeEntity> findAll() {
        return this.carTypeRepository.findAll();
    }

    public CarTypeEntity findById(int id){
    	return this.carTypeRepository.findById(id).get();
    }
}
