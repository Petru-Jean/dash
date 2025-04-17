package org.dash;

import org.dash.service.GatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Main
{
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    GatewayClient client;

    @EventListener(ApplicationReadyEvent.class)
    void onRun()
    {
        System.out.println("Application has started");
        client.connect();
    }

}