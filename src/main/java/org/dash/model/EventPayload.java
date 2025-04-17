package org.dash.model;

import org.json.JSONException;
import org.json.JSONObject;

public class EventPayload
{
    private final int  opcode;
    private final Object  data;
    private final Integer sequence;
    private final String  name;

    private final JSONObject json;

    private EventPayload(int opcode, Object data, Integer sequence, String name, JSONObject json)
    {
        this.opcode = opcode;
        this.data = data;
        this.sequence = sequence;
        this.name = name;
        this.json = json;
    }

    public static EventPayload fromJson(String data) throws JSONException
    {
        JSONObject json = new JSONObject(data);

        return new EventPayload(json.getInt("op"),
                json.opt("d"),
                json.optIntegerObject("s"),
                json.optString("t"), json);
    }

    public static class EventPayloadBuilder
    {
        private Integer opcode;
        private Object  data;
        private Integer sequence;
        private String  name;
        private JSONObject json;

        public EventPayloadBuilder(int opcode)
        {
            this.opcode = opcode;
        }

        public EventPayloadBuilder data(Object data)
        {
            this.data = data;
            return this;
        }

        public EventPayloadBuilder sequence(Integer sequence)
        {
            this.sequence = sequence;
            return this;
        }

        public EventPayloadBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public EventPayload build()
        {
            JSONObject json = new JSONObject();
            json.put("op", opcode);

            if(data != null)     json.put("d", data);
            if(sequence != null) json.put("s", sequence);
            if(name != null)     json.put("t", name);

            this.json = json;

            return new EventPayload(opcode, data, sequence, name, json);
        }

    }

    public int getOpcode() {
        return opcode;
    }

    public Object getData() {
        return data;
    }

    public Integer getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }

    public JSONObject getJson() {
        return json;
    }

    @Override
    public String toString()
    {
        return json.toString();
    }


}
