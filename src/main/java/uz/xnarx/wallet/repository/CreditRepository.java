package uz.xnarx.wallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.wallet.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, String> {

    Page<Credit> findAllByOrderByEndDateDesc(Pageable pageable);

    Page<Credit> findAllByNameContainingIgnoreCase(String name, Pageable pageable);


}
