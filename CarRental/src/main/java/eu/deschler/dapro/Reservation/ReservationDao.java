package eu.deschler.dapro.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationDao {

    ReservationRepository reservationRepository;

    @Autowired
    public ReservationDao(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void add(ReservationEntity reservation) {
        reservationRepository.save(reservation);
    }

    public List<ReservationEntity> findAll() {
        return reservationRepository.findAll();
    }

    public int count() {
        return (int) reservationRepository.count();
    }

    public List<ReservationEntity> findByIdAndTimeRange(int id, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findReservationsByIdWithinTimeRange(id, start, end);
    }
}
