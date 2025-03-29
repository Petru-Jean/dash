package org.dash;

import org.dash.service.GatewayClient;

public class Main
{
    public static void main(String[] args)
    {
        GatewayClient client = new GatewayClient();
        client.start();

    }


}