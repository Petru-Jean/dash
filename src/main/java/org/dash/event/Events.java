package org.dash.event;

import java.util.HashMap;
import java.util.Map;

import netscape.javascript.JSObject;
import org.dash.event.Event;

public record Events()
{
    private static Map<String, Event> events()
    {
        var map = new HashMap<String, Event>();

        map.put("", new HelloEvent());

        return map;
    }

    public static void Receive(JSObject json)
    {

    }

}
