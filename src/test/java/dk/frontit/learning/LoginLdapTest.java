package dk.frontit.learning;

import io.micronaut.http.*;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.generator.claims.JwtClaims;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.validator.JwtTokenValidator;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import javax.inject.Inject;

@MicronautTest
public class LoginLdapTest {

    @Client("/")
    @Inject
    RxHttpClient httpClient;

    @Inject
    JwtTokenValidator jwtTokenValidator;

    @Test
    public void testLdapLogin(){
        HttpRequest httpRequest = HttpRequest.create(HttpMethod.POST, "/login")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(new UsernamePasswordCredentials("euler", "password"));
        HttpResponse<AccessRefreshToken> exchange = httpClient.toBlocking().exchange(httpRequest, AccessRefreshToken.class);
        Assertions.assertNotNull(exchange.body());
        Assertions.assertNotNull(exchange.body().getAccessToken());
        Publisher<Authentication> authenticationPublisher = jwtTokenValidator.validateToken(exchange.body().getAccessToken());
        Object o = Flowable.fromPublisher(authenticationPublisher).blockingFirst().getAttributes().get(JwtClaims.EXPIRATION_TIME);

    }
}
