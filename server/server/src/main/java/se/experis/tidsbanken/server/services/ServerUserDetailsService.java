package se.experis.tidsbanken.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.AppUser;
import se.experis.tidsbanken.server.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class ServerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        AppUser user = userRepository.getByEmail(userEmail);
        return new User(user.email, user.password, new ArrayList<>());
    }
}
