import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Class to blueprint what a score file looks like
 *
 * @author Owain Gibson
 * @version 1.0
 */
public class ScoreFile {
    private ArrayList<Score> allScores;
    private int numScores;

    /**
     * Constructor to help initialise the class object
     */
    public ScoreFile() {
        allScores = new ArrayList<>();
        try {
            loadScores(); //loads in the score
        }
        catch (FileNotFoundException e){ //if the scorefile is missing, it creates a new one
            System.err.println("The score file seems to be missing, a new one will be created instead.");
        }
        numScores = allScores.size();
    }

    /**
     * Function to return the highest score in the file
     * If there are no scores, return -1
     * @return returns the score
     */
    public int getTopScore(){
        Score score;
        if (numScores > 0) {
            score = allScores.get(0);
            return score.getResult();
        }
        return -1;
    }

    /**
     * Function for loading in the scores from a specified score file
     * @throws FileNotFoundException if the file cannot be found
     */
    private void loadScores() throws FileNotFoundException {
        try (Scanner infile = new Scanner(new BufferedReader(new FileReader("../scores.txt")))) {
            allScores.clear();

            int scoreResult;
            String scoreName;

            Score scoreToAdd;

            infile.useDelimiter("\r?\n|\r");

            while (infile.hasNext()) {
                scoreResult = infile.nextInt();
                scoreName = infile.next();

                scoreToAdd = new Score(scoreResult, scoreName);
                allScores.add(scoreToAdd);
            }
        }
        Collections.sort(allScores); //sorts the scores to make sure they are in the correct order
    }

    /**
     * Function to save the scores to a score file
     * @throws IOException if there is an IO error
     */
    private void saveScores() throws IOException {

        try (PrintWriter outfile = new PrintWriter(new BufferedWriter(new FileWriter("../scores.txt")))) {

            for (int i = 0; i<numScores-1; i++){
                outfile.println(allScores.get(i).getResult());
                outfile.println(allScores.get(i).getName());
            }
            outfile.println(allScores.get(numScores-1).getResult());
            outfile.print(allScores.get(numScores-1).getName()); //the last score name get printed without a line at the end so that the file don't have an erroneous empty line
        }
    }

    /**
     * Method for adding a score to the arrayList
     * @param s holds the score to add
     * @throws IOException if there is an IO error
     */
    public void addScore(Score s) throws IOException {
        allScores.add(s);
        numScores++;
        Collections.sort(allScores); //sorts scores before saving them
        saveScores();
    }

    /**
     * Method for returning the top 10 (if possible) scores to the calling function as a string
     * @return returns the string
     */
    @Override
    public String toString() {
        int offset = 1; //needed so that the first score rank doesn't start at 0.
        StringBuilder sb = new StringBuilder("| The top scores are as follows:\n");
        for (int i=0; i<numScores; i++){
            sb.append("|").append(i + offset).append(".   ").append(allScores.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
