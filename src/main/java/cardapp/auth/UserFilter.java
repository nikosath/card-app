package cardapp.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
//@Order(1)
@RequiredArgsConstructor
class UserFilter implements jakarta.servlet.Filter {

    private final UserProfileDao userProfileDao;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfileEntity userProfile = userProfileDao.findByUsername(username);
        UserContext.setCurrentUser(userProfile);

        try {
            chain.doFilter(request, response);
        } finally {
            // reset current user when request processing is over
            UserContext.setCurrentUser(null);
        }
    }

}