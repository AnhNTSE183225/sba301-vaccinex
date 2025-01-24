package com.sba301.vaccinex.service;

import com.sba301.vaccinex.configuration.CustomAccountDetail;
import com.sba301.vaccinex.configuration.JWTAuthenticationFilter;
import com.sba301.vaccinex.configuration.JWTToken;
import com.sba301.vaccinex.dto.AccountDTO;
import com.sba301.vaccinex.dto.request.AccountCreateRequest;
import com.sba301.vaccinex.dto.request.AccountRegisterRequest;
import com.sba301.vaccinex.dto.request.AccountUpdateRequest;
import com.sba301.vaccinex.dto.request.AccountUpdateRoleRequest;
import com.sba301.vaccinex.dto.response.PagingResponse;
import com.sba301.vaccinex.dto.response.TokenResponse;
import com.sba301.vaccinex.exception.AuthenticationException;
import com.sba301.vaccinex.exception.ElementExistException;
import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.exception.UnchangedStateException;
import com.sba301.vaccinex.mapper.AccountMapper;
import com.sba301.vaccinex.pojo.Account;
import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.pojo.enums.EnumRoleNameType;
import com.sba301.vaccinex.pojo.enums.EnumTokenType;
import com.sba301.vaccinex.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl extends BaseServiceImpl<Account, Integer> implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountMapper accountMapper;
    private final JWTToken jwtToken;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationManager authenticationManager;

    public AccountServiceImpl(AccountRepository accountRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder,
                              AccountMapper accountMapper, JWTToken jwtToken, JWTAuthenticationFilter jwtAuthenticationFilter, AuthenticationManager authenticationManager) {
        super(accountRepository);
        this.accountRepository = accountRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountMapper = accountMapper;
        this.jwtToken = jwtToken;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public PagingResponse findAllAccounts(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = accountRepository.findAll(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all accounts paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(accountMapper::accountToAccountDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all accounts paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(accountMapper::accountToAccountDTO)
                                .toList())
                        .build();
    }

    @Override
    public PagingResponse getAllAccountsActive(Integer currentPage, Integer pageSize) {

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = accountRepository.findAllByEnabledTrue(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all accounts active paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(accountMapper::accountToAccountDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all accounts active paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(accountMapper::accountToAccountDTO)
                                .toList())
                        .build();
    }

    private String getRoleByRoleID(Integer roleID) {
        if (roleID == null) {
            throw new ElementNotFoundException("Role ID is null");
        }
        return switch (roleID) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_STAFF";
            case 3 -> "ROLE_USER";
            case 4 -> "ROLE_MANAGER";
            case 5 -> "ROLE_SELLER";
            default -> throw new ElementNotFoundException("Role ID is not valid");
        };
    }

    @Override
    public List<Account> getAccountsByRole(Integer roleID) {

        String role = getRoleByRoleID(roleID);

        List<Account> listsByRole = accountRepository.findAll();
        Role role_admin = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_ADMIN);
        Role role_manager = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_MANAGER);

        if (role.equals(EnumRoleNameType.ROLE_STAFF.name())) {
            for (Account account : accountRepository.findAll()) {
                if (account.getRole().equals(role_admin) || account.getRole().equals(role_manager)) {
                    listsByRole.remove(account);
                }
            }
        }  else if (role.equals(EnumRoleNameType.ROLE_ADMIN.name())) {
                return accountRepository.findAll();
        }
        return listsByRole;
    }

    @Override
    public boolean lockedAccount(int id) {
        Account account = accountRepository.getAccountById(id);
        if (account != null && account.isNonLocked()) {
            accountRepository.locked(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean unLockedAccount(int id) {
        Account account = accountRepository.getAccountById(id);
        if (account != null && !account.isNonLocked()) {
            accountRepository.unLocked(id);
            return true;
        }
        return false;
    }


    @Override
    public AccountDTO getAccountByEmail(String email) {
        Account account = accountRepository.getAccountByEmail(email);
        if (account == null) {
            throw new ElementNotFoundException("Account with email " + email + " not found");
        }
        return accountMapper.accountToAccountDTO(account);
    }

    @Override
    public boolean getAccountByPhone(String phone) {
        Account account = accountRepository.getAccountByPhoneNumber(phone);
        if (account == null || !account.isNonLocked() || !account.isEnabled()) {
            return false;
        }
        return true;
    }

    @Override
    public void lockedAccountByEmail(String email) {
        Account account = accountRepository.getAccountByEmail(email);
        if (account != null && account.isNonLocked()) {
            accountRepository.lockedByEmail(email);
        }
    }

    @Override
    public boolean checkEmailOrPhone(String s) {
        Account account = null;
        boolean check = false;
        char c = s.toLowerCase().charAt(0);
        if (c >= 'a' && c <= 'z') {
            account = accountRepository.getAccountByEmail(s);
            check = true;
        } else if (c >= '0' && c <= '9') {
            account = accountRepository.getAccountByPhoneNumber(s);
            check = false;
        }
        return check;
    }

    @Override
    public AccountDTO registerAccount(AccountRegisterRequest accountRegisterRequest) {
        Account checkExistingAccount = accountRepository.getAccountByEmail(accountRegisterRequest.getEmail());
        if (checkExistingAccount != null) {
            throw new ElementExistException("Account already exists");
        }
        Role role = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_USER);
        Account account = Account.builder()
                .email(accountRegisterRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(accountRegisterRequest.getPassword()))
                .accessToken(null)
                .refreshToken(null)
                .enabled(true)
                .nonLocked(true)
                .role(role)
                .build();
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO updateAccount(AccountUpdateRequest accountUpdateRequest, int accountID) {

        CustomAccountDetail customAccountDetail = (CustomAccountDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (customAccountDetail.getId() != accountID) {
            throw new AuthenticationException("You are not allowed to update other account");
        }

        Account account = accountRepository.getAccountById(accountID);
        if (account != null) {
            if (accountUpdateRequest.getPassword() != null) {
                account.setPassword(bCryptPasswordEncoder.encode(accountUpdateRequest.getPassword()));
            }
            if (accountUpdateRequest.getPhone() != null) {
                account.setPhoneNumber(accountUpdateRequest.getPhone());
            }
            if(accountUpdateRequest.getFirstName() != null) {
                account.setFirstName(accountUpdateRequest.getFirstName());
            }
            if(accountUpdateRequest.getLastName() != null) {
                account.setLastName(accountUpdateRequest.getLastName());
            }
            return accountMapper.accountToAccountDTO(accountRepository.save(account));
        } else {
            throw new ElementNotFoundException("Account not found");
        }
    }

    @Override
    public AccountDTO updateAccountRole(AccountUpdateRoleRequest accountUpdateRoleRequest, int accountID) {
        Account account = accountRepository.getAccountById(accountID);

        if (account == null) {
            throw new ElementNotFoundException("Account not found");
        }
        if (accountUpdateRoleRequest.getPassword() != null) {
            account.setPassword(bCryptPasswordEncoder.encode(accountUpdateRoleRequest.getPassword()));
        }
        if (accountUpdateRoleRequest.getPhone() != null) {
            account.setPhoneNumber(accountUpdateRoleRequest.getPhone());
        }
        if(accountUpdateRoleRequest.getFirstName() != null) {
            account.setFirstName(accountUpdateRoleRequest.getFirstName());
        }
        if(accountUpdateRoleRequest.getLastName() != null) {
            account.setLastName(accountUpdateRoleRequest.getLastName());
        }
        if(accountUpdateRoleRequest.getRoleID() > 0) {
            Role role = roleService.getRoleByRoleId(accountUpdateRoleRequest.getRoleID());
            if (role == null) {
                throw new ElementNotFoundException("Role not found");
            }
            account.setRole(role);
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        TokenResponse tokenResponse = new TokenResponse("Failed", "Refresh token failed", null, null);
        String email = jwtToken.getEmailFromJwt(refreshToken, EnumTokenType.REFRESH_TOKEN);
        Account account = accountRepository.getAccountByEmail(email);
        if (account != null) {
            if (StringUtils.hasText(refreshToken) && account.getRefreshToken().equals(refreshToken)) {
                if (jwtToken.validate(refreshToken, EnumTokenType.REFRESH_TOKEN)) {
                    CustomAccountDetail customAccountDetail = CustomAccountDetail.mapAccountToAccountDetail(account);
                    if (customAccountDetail != null) {
                        String newToken = jwtToken.generatedToken(customAccountDetail);
                        account.setAccessToken(newToken);
                        accountRepository.save(account);
                        tokenResponse = new TokenResponse("Success", "Refresh token successfully", newToken, refreshToken);
                    }
                }
            }
        }
        return tokenResponse;
    }

    @Override
    public TokenResponse login(String email, String password) {
        TokenResponse tokenResponse = new TokenResponse("Failed", "Login failed", null, null);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        CustomAccountDetail accountDetail = (CustomAccountDetail) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtToken.generatedToken(accountDetail);
        String refreshToken = jwtToken.generatedRefreshToken(accountDetail);
        Account account = accountRepository.getAccountByEmail(accountDetail.getEmail());
        if (account != null) {
            account.setRefreshToken(refreshToken);
            account.setAccessToken(token);
            accountRepository.save(account);
            tokenResponse = new TokenResponse("Success", "Login successfully", token, refreshToken);
        }
        return tokenResponse;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.getToken(request);
        String email = jwtToken.getEmailFromJwt(token, EnumTokenType.TOKEN);
        Account account = accountRepository.getAccountByEmail(email);
        if (account == null) {
            throw new ElementNotFoundException("Account not found");
        }
        account.setAccessToken(null);
        account.setRefreshToken(null);
        Account checkAccount = accountRepository.save(account);

        return checkAccount.getAccessToken() == null;
    }

    @Override
    public AccountDTO createAccountWithRole(AccountCreateRequest accountCreateRequest) {
        Account checkExistingAccount = accountRepository.getAccountByEmail(accountCreateRequest.getEmail());
        if (checkExistingAccount != null) {
            throw new ElementExistException("Account already exists");
        }
        Role role = roleService.getRoleByRoleId(accountCreateRequest.getRoleID());
        Account account = Account.builder()
                .email(accountCreateRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(accountCreateRequest.getPassword()))
                .accessToken(null)
                .refreshToken(null)
                .enabled(true)
                .nonLocked(true)
                .role(role)
                .build();
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO undeletedAccount(int accountID) {
        Account account = accountRepository.getAccountById(accountID);
        if (account == null) {
            throw new ElementNotFoundException("Account not found");
        }
        if(account.isNonLocked() && account.isEnabled() && !account.isDeleted()) {
            throw new UnchangedStateException("Account already deleted");
        }
        account.setNonLocked(true);
        account.setEnabled(true);
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO deleteAccount(int accountID) {
        Account account = accountRepository.getAccountById(accountID);
        if(account == null) {
            throw new ElementNotFoundException("Account not found");
        }
        account.setEnabled(false);
        account.setNonLocked(false);

        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

}
