package br.com.recargapay.walletservice.repository;


import br.com.recargapay.walletservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("""
        SELECT t FROM Transaction t
        WHERE (t.sourceWallet.id = :walletId OR t.destinationWallet.id = :walletId)
        AND t.timestamp BETWEEN :from AND :to
        ORDER BY t.timestamp ASC
    """)
    List<Transaction> findAllByWalletIdAndTimestampBetween(
            @Param("walletId") UUID walletId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}