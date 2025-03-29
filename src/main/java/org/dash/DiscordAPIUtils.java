package org.dash;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DiscordAPIUtils
{
    public static URI getGatewayURI() {

        final URI defaultGatewayURI = URI.create("wss://gateway.discord.gg");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/gateway"))
                .GET()
                .build();

        HttpResponse<String> response;

        try
        {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage() + " when requesting discord API gateway URL, defaulting to " + defaultGatewayURI);

            return defaultGatewayURI;
        }

        if (response.statusCode() != 200)
        {
            System.out.println("GET request to https://discord.com/api failed with response code " + response.statusCode() + ", defaulting to " + defaultGatewayURI + " gateway URI");

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


}
