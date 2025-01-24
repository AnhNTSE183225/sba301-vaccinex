package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.AccountDTO;
import com.sba301.vaccinex.dto.request.AccountCreateRequest;
import com.sba301.vaccinex.dto.request.AccountUpdateRequest;
import com.sba301.vaccinex.dto.request.AccountUpdateRoleRequest;
import com.sba301.vaccinex.dto.response.ObjectResponse;
import com.sba301.vaccinex.dto.response.PagingResponse;
import com.sba301.vaccinex.exception.AuthenticationException;
import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.exception.UnchangedStateException;
import com.sba301.vaccinex.pojo.Account;
import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.service.AccountService;
import com.sba301.vaccinex.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final RoleService roleService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    @GetMapping("/test")
    public String testne() {
        return "testne";
    }

    // lấy tất cả các account
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<PagingResponse> getAllAccounts(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = accountService.findAllAccounts(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // lấy tất cả roles
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-roles")
    public ResponseEntity<ObjectResponse> getAllRoles() {
        List<Role> results = roleService.getAllRoles();
        return !results.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all roles successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all roles failed", null));
    }

    // lấy tất cả các account active
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-active")
    public ResponseEntity<PagingResponse> getAllAccountsActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = accountService.getAllAccountsActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // update account bằng id
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/update/{account-id}")
    public ResponseEntity<ObjectResponse> updateAccount(@PathVariable("account-id") int accountID, @RequestBody AccountUpdateRequest accountUpdateRequest) {
        try {
            AccountDTO account = accountService.updateAccount(accountUpdateRequest, accountID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update account successfully", account));
        } catch (AuthenticationException e) {
            log.error("Error while updating account", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ObjectResponse("Fail", "Update account failed. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update account failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update account failed", null));
        }
    }

    // update account với role
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-role/{account-id}")
    public ResponseEntity<ObjectResponse> updateAccountRole(@PathVariable("account-id") int accountID, @RequestBody AccountUpdateRoleRequest accountUpdateRoleRequest) {
        try {
            AccountDTO account = accountService.updateAccountRole(accountUpdateRoleRequest, accountID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update account successfully", account));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update account failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update account failed", null));
        }
    }

//    // lấy ra tất cả account bằng role
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/get-accounts-by-role/{role-id}")
//    public ResponseEntity<ObjectResponse> getAccountByRole(@PathVariable("role-id") Integer roleID) {
//        List<Account> results = accountService.getAccountsByRole(roleID);
//        return results != null ?
//                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get account by role name successfully", results)) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get account by role name failed", null));
//    }


    // khôi phục lại account đó, bao gồm unlock và undelete
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/undelete/{account-id}")
    public ResponseEntity<ObjectResponse> unDeleteAccountByID(@PathVariable("account-id") int accountID) {
        try {
            AccountDTO account = accountService.undeletedAccount(accountID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete account successfully", account));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete account failed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete account failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete account failed", null));
        }
    }

    // tạo ra account với role
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createAccountWithRole(@Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        try {
            AccountDTO account = accountService.createAccountWithRole(accountCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", account));
        } catch (ElementNotFoundException e) {
            log.error("Error creating account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create account failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create account failed", null));
        }
    }

    // set deleted = true and enabled = false
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lock/{account-id}")
    public ResponseEntity<ObjectResponse> lockAccountByID(@PathVariable("account-id") int accountID) {
        try {
            Account account = accountService.findById(accountID);
            if(account != null) {
                account.setEnabled(false);
                account.setNonLocked(false);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lock account successfully", accountService.save(account)));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Lock account failed", null));
        } catch (Exception e) {
            log.error("Error locking account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Lock account failed", null));
        }
    }

    // xóa account, tức set deleted = true and enabled = false và xóa luôn Customer
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{account-id}")
    public ResponseEntity<ObjectResponse> deleteAccountByID(@PathVariable("account-id") int accountID) {
        try {
            AccountDTO account = accountService.deleteAccount(accountID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete account successfully", account));
        } catch (Exception e) {
            log.error("Error deleting account", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete account failed", null));
        }
    }

}
