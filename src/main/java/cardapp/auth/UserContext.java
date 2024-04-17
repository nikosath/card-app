package cardapp.auth;

import lombok.extern.slf4j.Slf4j;

// TODO: make UserContext a spring bean
@Slf4j
public class UserContext {

    private static final ThreadLocal<UserProfileEntity> CURRENT_USER = new ThreadLocal<>();

    public static UserProfileEntity getCurrentUser() {
        UserProfileEntity currentUser = CURRENT_USER.get();
        log.debug("currentUser: {}", currentUser);
        return currentUser;
    }

    public static void setCurrentUser(UserProfileEntity user) {
        log.debug("setCurrentUser: {}", user);
        CURRENT_USER.set(user);
    }
}