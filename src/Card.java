import java.util.Objects;

/**
 * Class to blueprint what a card looks like
 *
 * @author Owain Gibson
 * @version 1.0
 */
public class Card {
    /**
     * These are final because the characteristics of a playing card never change
     */
    private final Suit suit;
    private final Value value;

    /**
     * Constructor to instantiate the object properly
     * @param value holds the value of the card (Rank)
     * @param suit holds the suit of the card
     */
    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    /**
     * Method to return the card's value and rank as a string (used to reference its GIF image filename)
     * @return returns the string
     */
    @Override
    public String toString() {
        return value.toString() + suit;
    }

    /**
     * Used to compare cards, to check if they are able to merge with eachother
     * @param o holds the object to compare
     * @return returns the result of the comparison (true if equal)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit || value == card.value; //either statement can be true
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }
}
