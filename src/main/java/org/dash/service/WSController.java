package org.dash.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;


public class WSController extends WebSocketClient
{
    private static final URI defaultGatewayURI = URI.create("wss://gateway.discord.gg");

    URI getGatewayURI()
    {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/gateway"))
                .GET()
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage() + " when requesting discord API gateway URL, defaulting to " + defaultGatewayURI);

            return defaultGatewayURI;
        }

        if (response.statusCode() != 200)
        {
            System.out.println("Request to https://discord.com/api failed with response code " + response.statusCode() + ", defaulting to " + defaultGatewayURI + " gateway URI");

            return defaultGatewayURI;
        }

        JSONObject jsonResponse = new JSONObject(response.body());

        try {
            URI uri = new URI(jsonResponse.getString("url"));

            System.out.println("Fetched gateway URL from discord api: " + uri);

            return uri;

        } catch (Exception e) {
            System.out.println("Failed to fetch gateway URL from discord api response: " + e.getMessage());
        }

        return defaultGatewayURI;
    }

    public WSController()
    {
        super(defaultGatewayURI);
        super.uri = getGatewayURI();

        System.out.println("Starting WebSocket connection to " + super.uri);
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }


}
