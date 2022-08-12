package com.kalkanb.authority.user;

import com.kalkanb.authority.jwt.JwtUtils;
import com.kalkanb.entity.UserEntity;
import com.kalkanb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger("Project Logger");

    private JwtUtils jwtUtils;
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String jwt) throws UsernameNotFoundException {
        try {
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserEntity userEntity = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

                return CustomUserDetails.build(userEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot find user authentication!", e);
        }
        throw new UsernameNotFoundException("INVALID-USERNAME");
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
