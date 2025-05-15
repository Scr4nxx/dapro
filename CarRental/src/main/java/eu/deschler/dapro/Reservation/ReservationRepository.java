package eu.deschler.dapro.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.modellID = :modelId " +
            "AND (r.beginn BETWEEN :begin AND :end " +
            "OR r.ende BETWEEN :begin AND :end)")
    List<ReservationEntity> findReservationsByIdWithinTimeRange(@Param("modelId") Integer modelId,
                                                                    @Param("begin") LocalDateTime begin,
                                                                    @Param("end") LocalDateTime end);
}
