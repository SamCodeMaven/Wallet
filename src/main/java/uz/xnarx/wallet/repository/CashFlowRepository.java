package uz.xnarx.wallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.xnarx.wallet.entity.CashFlow;

public interface CashFlowRepository extends JpaRepository<CashFlow, String> {

    Page<CashFlow> findAllByOrderByDateDesc(Pageable pageable);
    Page<CashFlow> findAllByCashFlowTypeOrderByDateDesc(Boolean cashFlowType, Pageable pageable);

    @Query("SELECT c FROM CashFlow c WHERE c.cashFlowType = :cashFlowType AND MONTH(c.date) = :month AND YEAR(c.date) = :year ORDER BY c.date DESC")
    Page<CashFlow> findAllByCashFlowTypeAndMonthAndYearOrderByDateDesc(@Param("cashFlowType") Boolean cashFlowType, @Param("month") int month, @Param("year") int year, Pageable pageable);

    @Query("SELECT c FROM CashFlow c WHERE MONTH(c.date) = :month AND YEAR(c.date) = :year ORDER BY c.date DESC")
    Page<CashFlow> findAllByMonthAndYearOrderByDateDesc( @Param("month") int month, @Param("year") int year, Pageable pageable);

    Page<CashFlow> findAllByNameContainingIgnoreCase(String name, Pageable pageable);


    @Query("SELECT c FROM CashFlow c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND MONTH(c.date) = :month AND YEAR(c.date) = :year ORDER BY c.date DESC")
    Page<CashFlow> findAllByNameContainingIgnoreCaseAndMonthAndYear(@Param("name") String name, @Param("month") int month, @Param("year") int year, Pageable pageable);


    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = TRUE AND c.cashFlowType=TRUE")
    Double sumOfIncomeInUSD();

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = FALSE AND c.cashFlowType=TRUE")
    Double sumOfIncomeInUZS();

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = TRUE AND c.cashFlowType=FALSE ")
    Double sumOfOutcomeInUSD();

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = FALSE AND c.cashFlowType=FALSE ")
    Double sumOfOutcomeInUZS();
}
