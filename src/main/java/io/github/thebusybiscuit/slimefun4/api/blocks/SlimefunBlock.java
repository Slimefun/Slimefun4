package io.github.thebusybiscuit.slimefun4.api.blocks;

import com.google.gson.JsonObject;

public class SlimefunBlock extends JsonDataHolder {

    private String id;

    public SlimefunBlock(String id) {
        this(id, null);
    }

    public SlimefunBlock(String id, JsonObject data) {
        super(data);

        this.id = id;
    }

    public String getID() {
        return id;
    }

}
