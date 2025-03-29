package org.dash.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatController
{
    final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public HeartbeatController()
    {

    }

    private void sendHeartbeat()
    {
        System.out.println("Sent the heartbeat event to Discord gateway API");
    }

    public void Start(int interval) {
        if(!scheduler.isShutdown()) {
            System.out.println("Failed to start heartbeat process as the scheduler is already running");

            return;
        }

        System.out.println("Heartbeat process started, sending a heartbeat event every " + interval + " ms");

        scheduler.scheduleAtFixedRate( () -> { this.sendHeartbeat(); }, 0, interval, TimeUnit.MILLISECONDS);
    }

    public void Stop() {
        scheduler.shutdownNow();

        System.out.println("Heartbeat process stopped");
    }


}
