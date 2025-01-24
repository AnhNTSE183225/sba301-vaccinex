package com.sba301.vaccinex.configuration;

import com.sba301.vaccinex.pojo.Account;
import com.sba301.vaccinex.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.getAccountByEmail(username);
        if(account != null) {
            return CustomAccountDetail.mapAccountToAccountDetail(account);
        }
        return null;
    }
}
