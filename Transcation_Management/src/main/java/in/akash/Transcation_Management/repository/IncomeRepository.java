package in.akash.Transcation_Management.repository;

import in.akash.Transcation_Management.entity.ExpenseEntity;
import in.akash.Transcation_Management.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity,Long> {

    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<IncomeEntity>  findTopFiveByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(e.amount) FROM  IncomeEntity  e WHERE e.profile.id=:profileID")
    BigDecimal findTotalExpenseByProfileId(@Param("profileID")Long profileId);

    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);
}
