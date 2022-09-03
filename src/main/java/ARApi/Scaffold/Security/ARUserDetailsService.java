package ARApi.Scaffold.Security;

import ARApi.Scaffold.Database.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ARUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public ARUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var all = userRepository.findAll();
        var optUser = userRepository.findByEmail(username);
        if(optUser.isPresent()){
            return optUser.get();
        }

        throw new UsernameNotFoundException(username);
    }
}
