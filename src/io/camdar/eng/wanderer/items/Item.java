package io.camdar.eng.wanderer.items;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class Item {
    public static final int NUM_ITEMTYPES = 5;
    
    private static ArrayList<String> names = new ArrayList<>();
    private static ArrayList<String> descs = new ArrayList<>();
    
    static {
        try {
            ArrayList<String> strs = new ArrayList<>();
            Scanner sc = new Scanner(new File("res/itemdata.txt"));
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                strs.add(str.replace(" \\n ", "\n"));
            }
            if (strs.size() % 2 != 0) { throw new IOException("wtf"); }
            
            for (int i = 0 ; i < strs.size()-1 ; i+=2) {
                names.add(strs.get(i));
                descs.add(strs.get(i+1));
            }
        }
        catch (IOException e) {
            System.err.println("Can't load the item text. Aborting.");
            System.exit(-4);
        }
    }
    
    // I hate java
    private static ArrayList<Integer> news =
            new ArrayList<Integer>() {{
                for (int i = 0 ; i < 21 ; i++) { add(i); }
            }};
    
    private static ArrayList<Integer> tech =
            new ArrayList<Integer>() {{
                for (int i = 21 ; i < 21+3 ; i++) { add(i); }
            }};
    
    private static ArrayList<Integer> kaminoJournal =
            new ArrayList<Integer>() {{
                for (int i = 24 ; i < 24+8 ; i++) { add(i); }
            }};
    private static ArrayList<Integer> unknownJournal =
            new ArrayList<Integer>() {{
                for (int i = 32 ; i < 32+6 ; i++) { add(i); }
            }};
            
    private static ArrayList<Integer> tiffanyJournal =
            new ArrayList<Integer>() {{
                for (int i = 38 ; i < 38+6 ; i++) { add(i); }
            }};
    
    private static ArrayList<ArrayList<Integer>> journals = 
            new ArrayList<ArrayList<Integer>>() {{
                add(kaminoJournal);
                add(unknownJournal);
                add(tiffanyJournal);
            }};
            
    public static int getRandomItem() {
        if (done()) {
            return -1;
        }

        int which = (int) (Math.random() * NUM_ITEMTYPES);
        while (true) {
            if (which == 0) {
                if (news.size() != 0) {
                    return news.remove((int) (Math.random() * news.size()));
                }
            }
            if (which == 1) {
                if (tech.size() != 0) {
                    return tech.remove((int) (Math.random() * tech.size()));
                }
            }
            if (which < 2) { which = 2; }
            ArrayList<Integer> journal = journals.get(which-2);
            if (journal.isEmpty()) {
                which += 1;
                which %= NUM_ITEMTYPES;
                continue;
            }
            return journal.remove(0);
        }
    }
    
    public static boolean done() {
        for (ArrayList<Integer> journal : journals) {
            if (!journal.isEmpty()) {
                return false;
            }
        }
        return news.isEmpty() && tech.isEmpty();
    }
    
    public static String getName(int i) {
        if (i == -1) { return "error"; }
        if (i < names.size()) { return names.get(i); }
        return "Unknown Item";
    }
    
    public static String getDescription(int i) {
        if (i == -1) { return "error"; }
        if (i < descs.size()) { return descs.get(i); }
        return "I've never seen anything like this before...";
    }
}
