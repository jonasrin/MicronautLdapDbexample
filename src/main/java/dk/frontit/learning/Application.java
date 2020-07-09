package dk.frontit.learning;

import dk.frontit.learning.service.RegisterService;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.event.ServerStartupEvent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Application implements ApplicationEventListener<ServerStartupEvent> {

    @Inject
    RegisterService registerService;

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_DETECTIVE");
        registerService.register("kazys@mail.com", "kazys", "pass", authorities);
    }
}