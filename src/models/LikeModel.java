package models;

import java.io.Serializable;

public class LikeModel implements Serializable {
    private int partyId; // ID of party that was liked
    private String username; // Name of person that liked story

    public LikeModel(int partyId, String username) {
        this.partyId = partyId;
        this.username = username;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}