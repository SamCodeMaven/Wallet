package uz.xnarx.wallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.xnarx.wallet.entity.CashFlow;

public interface CashFlowRepository extends JpaRepository<CashFlow, String> {

    Page<CashFlow> findAllByOrderByDateDesc(Pageable pageable);
    Page<CashFlow> findAllByCashFlowTypeAndOrderByDateDesc(Boolean cashFlowType, Pageable pageable);

    Page<CashFlow> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = TRUE AND c.cashFlowType=TRUE")
    Double sumOfIncomeInUSD();

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = FALSE AND c.cashFlowType=TRUE")
    Double sumOfIncomeInUZS();

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = TRUE AND c.cashFlowType=FALSE ")
    Double sumOfOutcomeInUSD();

    @Query("SELECT COALESCE(SUM(c.amount),0.0) FROM CashFlow c WHERE c.curType = FALSE AND c.cashFlowType=FALSE ")
    Double sumOfOutcomeInUZS();
}
