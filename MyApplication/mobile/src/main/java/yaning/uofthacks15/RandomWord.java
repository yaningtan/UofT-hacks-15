package yaning.uofthacks15;

import android.content.res.AssetManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by andrew on 01/02/15.
 */
public class RandomWord {
    private ArrayList<String> word_list = new ArrayList<String>();
    private Random r = new Random();

    public RandomWord(AssetManager am) {
        try {
            Scanner s = new Scanner(am.open("words.txt"));
            while (s.hasNext())
                word_list.add(s.nextLine());
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public int numWords() {
        return word_list.size();
    }

    public String getString() {
        return word_list.get(r.nextInt(this.numWords()));
    }
}
