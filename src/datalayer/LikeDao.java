package datalayer;

import models.LikeModel;

import java.io.*;
import java.util.ArrayList;

public class LikeDao {

    public static boolean didUserLikeParty(int partyId, String username) {
        ArrayList<LikeModel> likes = getLikes();
        for (LikeModel like: likes)  {
            if (like.getPartyId() == partyId && like.getUsername().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }
    private static ArrayList<LikeModel>getLikes(){
        File file = new File(getFilePath());
        return getLikes(file);
    }

    private static ArrayList<LikeModel> getLikes(File file) {
        ArrayList<LikeModel> likes = null;
        try {
            likes = new ArrayList<LikeModel>();

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                likes = (ArrayList<LikeModel>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e){
            return null;
        }

        return likes;
    }

    public static void saveLikes(ArrayList<LikeModel> likes){
            try {
                File file = new File(getFilePath());
                file.createNewFile();
                FileOutputStream fos;
                fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(likes);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public static void saveLike(int partyId, String username){
        if (didUserLikeParty(partyId, username)){
            return;
        }
        ArrayList<LikeModel> likes = getLikes();
        likes.add(new LikeModel(partyId, username));
        saveLikes(likes);
    }

    public static void deleteLike(int partyId, String username){
        File file = new File(getFilePath());
        file.delete();
    }

    public static int getNumberOfLikes(int partyId) {
        int nLikes = 0;
        ArrayList<LikeModel> likes = getLikes();

        for (LikeModel like: likes){
            if (like.getPartyId() == partyId)
                nLikes++;
        }
        return nLikes;
    }

    private static String getFilePath() {
        return DaoUtils.storageDirectoryName() + File.separator + "likes" + ".txt";
    }
}


