package dk.frontit.learning.service;

import dk.frontit.learning.domain.Role;
import dk.frontit.learning.domain.UserState;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class DelegatingAuthenticationProvider implements AuthenticationProvider {

    protected final UserRepositoryImpl userRepository;
    protected final BCryptPasswordEncoderService passwordEncoderService;
    protected final RoleRepositoryImpl roleRepository;
    protected final UserRoleRepositoryImpl userRoleRepository;
    protected final Scheduler scheduler;

    public DelegatingAuthenticationProvider(UserRepositoryImpl userRepository, BCryptPasswordEncoderService passwordEncoderService, RoleRepositoryImpl roleRepository, UserRoleRepositoryImpl userRoleRepository, Scheduler scheduler) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.scheduler = scheduler;
    }


    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        return Flowable.create(emitter ->
        {
            UserState userState = fetchUserState(authenticationRequest);
            Optional<AuthenticationFailed> authenticationFailed = validate(userState, authenticationRequest);
            if (authenticationFailed.isPresent()) {
                emitter.onError(new AuthenticationException(authenticationFailed.get().getMessage().get()));
            } else {
                emitter.onNext(createSuccessfulAuthentication(authenticationRequest, userState));
            }
            emitter.onComplete();
        }, BackpressureStrategy.ERROR).subscribeOn(scheduler).cast(AuthenticationResponse.class);
    }

    private AuthenticationResponse createSuccessfulAuthentication(AuthenticationRequest authenticationRequest, UserState userState) {
        return (AuthenticationResponse) userRoleRepository.findAllAuthoritiesByUsername(userState.getUsername()).stream()
                .map(s -> new UserDetails(userState.getUsername(), Collections.singleton(s)));
    }

    private Optional<AuthenticationFailed> validate(UserState userState, AuthenticationRequest authenticationRequest) {
        AuthenticationFailed authenticationFailed = null;
        if (Objects.isNull(userState)) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND);
        } else if (!userState.isEnabled()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_DISABLED);

        } else if (userState.isAccountExpired()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_EXPIRED);

        } else if (userState.isAccountLocked()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_LOCKED);

        } else if (userState.isPasswordExpired()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.PASSWORD_EXPIRED);

        } else if (!passwordEncoderService.matches(authenticationRequest.getSecret().toString(), userState.getPassword())) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
        return Optional.ofNullable(authenticationFailed);
    }

    private UserState fetchUserState(AuthenticationRequest authenticationRequest) {
        return userRepository.findByUsername(authenticationRequest.getIdentity().toString()).orElse(null);
    }

}
