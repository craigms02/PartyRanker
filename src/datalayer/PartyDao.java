package datalayer;

import models.PartyModel;

import java.io.*;
import java.util.ArrayList;

public class PartyDao {

    /**
     * Given a party ID, return the party.
     */
    public static PartyModel getParty(int PartyId) {
        File file = new File(getFilePath(PartyId));
        return getParty(file);
    }

    /*
     * Given a party ID, delete it from storage.
     */
    public static void deleteParty(int PartyId) {
        File file = new File(getFilePath(PartyId));
        file.delete();
    }

    /*
     * Save the given party model.  Make sure you've set
     * the ID in the party model.
     */
    public static void saveParty(PartyModel PartyModel){
        try {
            File file = new File(getFilePath(PartyModel.getPartyId()));
            file.createNewFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(PartyModel);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a party ID and party text, make a party model
     * and save it.
     */
    public static void saveParty(int PartyId, String PartyText, String username, int commentOnPartyId, String partyDate, String partyLocation) {
        PartyModel Party = new PartyModel();
        Party.setPartyId(UniqueIdDao.getID());
        Party.setParty(PartyText);
        Party.setDate(partyDate);
        Party.setLocation(partyLocation);
        Party.setUsername(username);
        Party.setCommentOnPartyID(commentOnPartyId);
        saveParty(Party);
    }

    /**
     * Return all saved parties.
     */

    public static ArrayList<PartyModel> getPartiesThatAreComments(int commentsOnPartyId) {
        ArrayList<PartyModel> parties = new ArrayList<>();
        String dir = DaoUtils.storageDirectoryName();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
            if(listOfFiles[i].getName().startsWith("party") &&
                    listOfFiles[i].getName().endsWith(".txt")){
                PartyModel party = getParty(listOfFiles[i]);
                if (party.getCommentOnPartyID() == commentsOnPartyId) {
                parties.add(getParty(listOfFiles[i]));
            }
        }
    }
        return parties;
    }



    public static ArrayList<PartyModel> getParties() {
        ArrayList<PartyModel> Parties = new ArrayList<>();
        String dir = DaoUtils.storageDirectoryName();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
            if(listOfFiles[i].getName().startsWith("party") &&
                    listOfFiles[i].getName().endsWith(".txt")){
                Parties.add(getParty(listOfFiles[i]));
            }
        }

        return Parties;
    }

    /**
     * Given a party ID, where are we saving it to storage (file name)?
     */
    private static String getFilePath(int partyId) {
        return DaoUtils.storageDirectoryName() + File.separator + "party" + partyId + ".txt";
    }

    /*
     * Given a party filename, return the party that's saved in the file.
     */
    private static PartyModel getParty(File file) {
        PartyModel Party = null;
        try {
            Party = new PartyModel();

            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            else{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Party = (PartyModel) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Party;
    }

    /**
     * Unit test program for Sprint 2.
     *
     * @param args
     */
    public static void main(String[] args) {
        testPartyDao();
    }

    private static void testPartyDao() {
        int partyId = 100;
        String text = "Towhouse 7 was lit";
        PartyDao dao = new PartyDao();
        PartyModel party = new PartyModel();
        party.setPartyId(partyId);
        party.setParty("Towhouse 7");
        party.setUsername("craig");
        party.setCommentOnPartyID(0);
        dao.saveParty(party);

        party = dao.getParty(partyId);
        assert(party.getPartyId() == 100);
        assert(party.getParty().compareTo(text) == 0);

        ArrayList<PartyModel> parties = dao.getParties();
        assert(parties != null && parties.size() >= 5);

        dao.deleteParty(partyId);
    }

}
