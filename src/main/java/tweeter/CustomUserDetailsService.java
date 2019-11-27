package tweeter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import tweeter.domain.Account;
import tweeter.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
//        List<SimpleGrantedAuthority> author = account.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
        account.getUsername(),
        account.getPassword(),
        true,
        true,
        true,
        true,
        Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
