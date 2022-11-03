/**
 *  Enumerator for defining what different suits a card can have
 *  I used enumerator because the card's suit won't ever change and also it helps eliminate errors
 *  Also it is quick to compare enumerators
 *
 * @author Owain Gibson
 * @version 1.0
 */
public enum Suit {
    SPADES("s"),
    HEARTS("h"),
    CLUBS("c"),
    DIAMONDS("d");

    /**
     * Each enumerator has a suit, so that when called, it can be used to reference the link to the GIF of the card
     * It is private because it won't ever change
     */
    private final String suit;

    /**
     * Constructor to help instantiate the enumerator
     * @param suit holds the suit to set
     */
    Suit(String suit){
        this.suit = suit;
    }

    /**
     * Method used to return the suit's corresponding letter. Used to reference the card's GIF
     * @return returns the letter
     */
    @Override
    public String toString() {
        return suit;
    }


}
