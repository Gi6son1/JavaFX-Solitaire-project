/**
 *  Enumerator for defining what different values a card can have
 *  I used enumerator because the card's value won't ever change and also it helps eliminate errors
 *  Also it is quick to compare enumerators
 *
 * @author Owain Gibson
 * @version 1.0
 */
public enum Value {

    ACE("a"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("t"),
    JACK("j"),
    QUEEN("q"),
    KING("k");

    /**
     * Each enumerator has a value, so that when called, it can be used to reference the link to the GIF of the card
     * It is private because it won't ever change
     */
    private final String value;

    /**
     * Constructor to help instantiate the enumerator
     * @param value holds the value to set
     */
    Value(String value){
        this.value = value;
    }

    /**
     * Method used to return the value's corresponding letter. Used to reference the card's GIF
     * @return returns the letter
     */
    @Override
    public String toString() {
        return value;
    }
}
