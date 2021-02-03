package com.aj.authentication;

import com.aj.dao.AccountDaoI;
import com.aj.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyDBAuthenticationService implements UserDetailsService
{
    @Autowired
    private AccountDaoI accountdao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        System.out.println(username + "1");
        Account account = accountdao.findAccount(username);

        if(account == null)
        {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        String role = account.getUserRole();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);

        grantedAuthorities.add(grantedAuthority);

        boolean enabled = account.isActive();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        UserDetails userDetails = (UserDetails) new User(account.getUserName(), account.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return userDetails;
    }
}
