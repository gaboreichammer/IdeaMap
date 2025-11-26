// java
package org.ideamap.user;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit5.JMockitExtension;
import org.ideamap.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JMockitExtension.class)
class UserServiceTest {

    @Tested
    UserService userService;

    @Injectable
    UserRepository userRepository;

    @Injectable
    org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Injectable
    MongoUser mongoUser;

    @Test
    void authenticate_returns_user_when_credentials_valid() {
        new Expectations() {{
            userRepository.findByUsername("alice"); result = mongoUser;
            mongoUser.getPassword(); result = "encodedPw";
            passwordEncoder.matches("secret", "encodedPw"); result = true;
        }};

        MongoUser result = userService.authenticate("alice", "secret");

        assertSame(mongoUser, result);

        new Verifications() {{
            userRepository.findByUsername("alice"); times = 1;
            passwordEncoder.matches("secret", "encodedPw"); times = 1;
        }};
    }

    @Test
    void authenticate_returns_null_when_user_not_found() {
        new Expectations() {{
            userRepository.findByUsername("bob"); result = null;
        }};

        assertNull(userService.authenticate("bob", "whatever"));

        new Verifications() {{
            userRepository.findByUsername("bob"); times = 1;
        }};
    }

    @Test
    void authenticate_returns_null_when_password_mismatch() {
        new Expectations() {{
            userRepository.findByUsername("carol"); result = mongoUser;
            mongoUser.getPassword(); result = "encodedPw2";
            passwordEncoder.matches("bad", "encodedPw2"); result = false;
        }};

        assertNull(userService.authenticate("carol", "bad"));

        new Verifications() {{
            userRepository.findByUsername("carol"); times = 1;
            passwordEncoder.matches("bad", "encodedPw2"); times = 1;
        }};
    }
}