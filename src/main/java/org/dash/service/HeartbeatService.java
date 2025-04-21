package org.dash.service;


import org.dash.client.GatewayClientPipeline;
import org.dash.client.WebSocketSubscriber;
import org.dash.event.outgoing.HeartbeatEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class HeartbeatService extends WebSocketSubscriber
{
    GatewayClientPipeline gatewayClientPipeline;

    ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    Optional<ScheduledFuture> scheduledFuture;

    int interval;
    int sequence;

    public HeartbeatService(@Autowired GatewayClientPipeline gatewayClientPipeline)
    {
        this.gatewayClientPipeline = gatewayClientPipeline;
        this.scheduledFuture   = Optional.empty();
        this.interval = 45000;
        this.sequence = 0;
    }

    private void sendHeartbeat()
    {
        gatewayClientPipeline.sendEvent(new HeartbeatEvent(sequence));
    }

    public void scheduleHeartbeat()
    {
        if(scheduledFuture.isPresent() && !scheduledFuture.get().isDone()) {
            System.out.println("[Heartbeat Controller] Failed to schedule outgoing heartbeat event as the scheduler is already active");
            return;
        }

        scheduledFuture = Optional.of(scheduler.schedule(this::sendHeartbeat, interval, TimeUnit.MILLISECONDS));
        System.out.println("[Heartbeat Controller] Scheduled the outgoing heartbeat event in " + interval + " ms");
    }

    public void cancelHeartbeat()
    {
        scheduledFuture.ifPresent( sf-> {
            System.out.println("[Heartbeat Controller] Cancelled the outgoing heartbeat event");
            sf.cancel(false);
        });
    }

    public void setSequence(int sequence)
    {
//        System.out.println("[Heartbeat Controller] Sequence changed to " + sequence);
        this.sequence = sequence;
    }

    public int getSequence()
    {
        return this.sequence;
    }

    public void updateInterval(int interval)
    {
        System.out.println("[Heartbeat Controller] Heartbeat schedule interval changed to " + interval + " ms");
        this.interval = interval;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        cancelHeartbeat();
    }
}
