package com.moneyApp.payee;

import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.payee.dto.PayeeWithIdAndNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
interface SqlPayeeQueryRepository extends PayeeQueryRepository, JpaRepository<PayeeSnapshot, Long>
{
    @Override
    @Query(value = "SELECT p.id " +
                   "FROM PayeeSnapshot p " +
                   "WHERE p.name = :payeeName AND p.user.id = :userId")
    Optional<Long> findIdByNameAndUserId(String payeeName, Long userId);

    @Override
    @Query(value = "SELECT p.name " +
                   "FROM PayeeSnapshot p " +
                   "WHERE p.id = :payeeId")
    Optional<String> findNameById(long payeeId);

    @Override
    @Query(value = "FROM PayeeSnapshot p " +
                   "WHERE p.name IN :payeeNames AND p.user.id = :userId")
    Set<PayeeSnapshot> findPayeesByNamesAndUserId(Set<String> payeeNames, Long userId);

    @Override
    @Query(value = "SELECT p.id AS id, p.name AS name " +
                   "FROM PayeeSnapshot p " +
                   "WHERE p.id IN :payeeIds")
    List<PayeeWithIdAndNameDTO> findPayeesIdsAndNamesByIds(List<Long> payeeIds);
}
