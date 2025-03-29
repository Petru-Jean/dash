package org.dash.model;

import org.json.JSONObject;

import java.util.Optional;

public class EventPayload
{
    int               opcode;
    Optional<Object>  data;
    Optional<Integer> sequence;
    Optional<String>  name;

    JSONObject json;

    public EventPayload(int opcode, Optional<Object> data, Optional<Integer> sequence, Optional<String> name)
    {
        this.opcode = opcode;
        this.data = data;
        this.sequence = sequence;
        this.name = name;

        json = new JSONObject();
        json.put("op", opcode);

        data.ifPresent    (d -> json.put("d", d));
        sequence.ifPresent(s -> json.put("s", s));
        name.ifPresent    (t -> json.put("t", t));
    }

    public JSONObject getJson()
    {
        return json;
    }

    public int getOpcode() {
        return opcode;
    }

    public Optional<Object> getData() {
        return data;
    }

    public Optional<Integer> getSequence() {
        return sequence;
    }

    public Optional<String> getName() {
        return name;
    }


}
