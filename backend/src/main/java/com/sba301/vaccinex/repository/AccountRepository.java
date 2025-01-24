package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account getAccountByEmail(String email);

    Account getAccountById(int accountID);

    Account getAccountByPhoneNumber(String phone);

    Page<Account> findAllByEnabledTrue(Pageable pageable);

    @Modifying
    @Query("update Account set enabled = true where id = ?1")
    void enabled(int accountID);

    @Modifying
    @Query("update Account set nonLocked = false where id = ?1")
    void locked(int accountID);

    @Modifying
    @Query("update Account set nonLocked = true where id = ?1")
    void unLocked(int accountID);

    @Modifying
    @Query("update Account set password = ?1 where email = ?2")
    void setPasswordByEmail(String password, String email);

    @Modifying
    @Query("update Account set password = ?1 where phoneNumber = ?2")
    void setPasswordByPhone(String password, String phone);

    @Modifying
    @Query("update Account set nonLocked = false where email = ?1")
    void lockedByEmail(String email);

}
