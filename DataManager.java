package project0;

import java.io.*;

public class DataManager {

    private static final String FILE_NAME = "watchlist.dat";

    public static void save(Watchlist watchlist) {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_NAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(watchlist);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    public static Watchlist load() {
        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Watchlist watchlist = (Watchlist) ois.readObject();
            ois.close();
            fis.close();
            return watchlist;
        } catch (IOException | ClassNotFoundException e) {
            return new Watchlist();
        }
    }
}