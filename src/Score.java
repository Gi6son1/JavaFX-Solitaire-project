/**
 * Class to blueprint what a score looks like
 *
 * @author Owain Gibson
 * @version 1.0
 */
public class Score implements Comparable<Score>{
    private int result;
    private String name;

    /**
     * Constructor to initialise the object correctly
     * @param result holds the result to set
     * @param name holds the name to set
     */
    public Score(int result, String name){
        this.result = result;
        this.name = name;
    }

    /**
     * Method to return the result of the score
     * @return returns the result
     */
    public int getResult() {
        return result;
    }

    /**
     * Method to return the scorer's name
     * @return returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * Method used to compare scores to one another, it is set up in such a way that it returns >0 if the result is LOWER than the other object's result
     * This allows for lower piles left to be ranked above scores with more piles left
     * @param o holds the object to compare
     * @return  returns the result of the comparison
     */
    @Override
    public int compareTo(Score o) {
        return Integer.compare(this.result, o.getResult());
    }

    /**
     * Method to return the score and name of the scorer to the calling function
     * @return returns the summary as a string
     */
    @Override
    public String toString() {
        return "Piles left: " + result + "  Scored by: " + name;
    }
}
