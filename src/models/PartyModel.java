package models;

import java.io.Serializable;

public class PartyModel implements Serializable {
    private int partyId = 0;
    private String party;
    private String username;
    private int commentOnPartyID;

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCommentOnPartyID() {
        return commentOnPartyID;
    }

    public void setCommentOnPartyID(int commentOnPartyID) {
        this.commentOnPartyID = commentOnPartyID;
    }
}