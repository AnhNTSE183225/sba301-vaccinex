package com.sba301.vaccinex.service;

import com.sba301.vaccinex.dto.AccountDTO;
import com.sba301.vaccinex.dto.request.AccountCreateRequest;
import com.sba301.vaccinex.dto.request.AccountRegisterRequest;
import com.sba301.vaccinex.dto.request.AccountUpdateRequest;
import com.sba301.vaccinex.dto.request.AccountUpdateRoleRequest;
import com.sba301.vaccinex.dto.response.PagingResponse;
import com.sba301.vaccinex.dto.response.TokenResponse;
import com.sba301.vaccinex.pojo.Account;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AccountService extends BaseService<Account, Integer> {

    PagingResponse findAllAccounts(Integer currentPage, Integer pageSize);

    List<Account> getAccountsByRole(Integer roleID);

    PagingResponse getAllAccountsActive(Integer currentPage, Integer pageSize);

    AccountDTO getAccountByEmail(String email);

    boolean lockedAccount(int id);

    boolean unLockedAccount(int id);

    boolean getAccountByPhone(String phone);

    void lockedAccountByEmail(String email);

    boolean checkEmailOrPhone(String s);

    AccountDTO registerAccount(AccountRegisterRequest accountRegisterRequest);

    AccountDTO updateAccount(AccountUpdateRequest accountUpdateRequest, int accountID);

    AccountDTO updateAccountRole(AccountUpdateRoleRequest accountUpdateRoleRequest, int accountID);

    TokenResponse refreshToken(String refreshToken);

    TokenResponse login(String email, String password);

    boolean logout(HttpServletRequest request);

    AccountDTO createAccountWithRole(AccountCreateRequest accountCreateRequest);

    AccountDTO undeletedAccount(int accountID);

    AccountDTO deleteAccount(int accountID);

}
