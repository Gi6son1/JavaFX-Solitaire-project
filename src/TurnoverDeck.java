import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Class to blueprint the turnover deck.
 * Since this is a form of deck (Collection of face up cards), it inherits attributes and methods from the deck class
 *
 * @author Owain Gibson
 * @version 1.0
 */
public class TurnoverDeck extends Deck {
    private String cardCover;

    /**
     * These maps are used to hold the different card suits and values.
     * They are used to convert the singular letters read from the card file into enumerators.
     * They are final so that they cannot be modified once instantiated
     */
    private final HashMap<String, Value> valueMap;
    private final HashMap<String, Suit> suitMap;
    private boolean shuffled;

    /**
     * Constructor to instantiate the turnoverDeck object
     * The default card back cover is applied, which can then be changed later if need be
     * @throws FileNotFoundException if the cards.txt file cannot be found
     */
    public TurnoverDeck() throws FileNotFoundException {
        super();
        this.cardCover = "b.gif";
        valueMap = new HashMap<>();
        suitMap = new HashMap<>();
        shuffled = false;
        initialiseCards();
        populateCardDeck();
    }

    /**
     * Method to fill the hashmaps with corresponding string-enumerator key-value pairs, to prepare for file reading
     * The reason why the suits and values are kept in different maps is so that,
     * for example, if a suit is meant to be read, it will only look in that specific map.
     * This means that if a letter corresponding to a value is read instead, the program knows that the data must be corrupt, since the suit is missing
     */
    private void initialiseCards(){
        valueMap.put("a", Value.ACE);
        valueMap.put("2", Value.TWO);
        valueMap.put("3", Value.THREE);
        valueMap.put("4", Value.FOUR);
        valueMap.put("5", Value.FIVE);
        valueMap.put("6", Value.SIX);
        valueMap.put("7", Value.SEVEN);
        valueMap.put("8", Value.EIGHT);
        valueMap.put("9", Value.NINE);
        valueMap.put("t", Value.TEN);
        valueMap.put("j", Value.JACK);
        valueMap.put("q", Value.QUEEN);
        valueMap.put("k", Value.KING);

        suitMap.put("s", Suit.SPADES);
        suitMap.put("h",Suit.HEARTS);
        suitMap.put("c", Suit.CLUBS);
        suitMap.put("d", Suit.DIAMONDS);

    }

    /**
     * Method for populating the turnover deck
     * @throws FileNotFoundException if neither the card file or its copy (in case the first one is missing) can be read
     */
    private void populateCardDeck() throws FileNotFoundException {
        Scanner infileTest;
        try {
            infileTest = new Scanner(new BufferedReader(new FileReader("../cards.txt")));
        }
        catch (FileNotFoundException e){
            infileTest = new Scanner(new BufferedReader(new FileReader("../cardscopy.txt")));
        }

        try (Scanner infile = infileTest) {
            infile.useDelimiter("\r?\n|\r");

            String rawValue;
            String rawSuit;

            Suit cardSuit;
            Value cardValue;

            Card cardToAdd;


            while (infile.hasNext()) {
                rawValue = infile.next().toLowerCase();
                cardValue = valueMap.get(rawValue);

                if (cardValue==null){ continue; } //if the value isn't in the map, it means that specific piece of data is out of place
                                                        //the program will wait until it finds a value first before assigning it to a card (robust)
                                                            //otherwise, it may mix up all the reading orders

                rawSuit = infile.next();
                cardSuit = suitMap.get(rawSuit);

                if (cardSuit==null){ continue; } //same applies for a null suit find. If it found a value but then no suit next,
                                                        // it ignores the pair and looks for the next one that actually has its data in the right order
                cardToAdd = new Card(cardValue, cardSuit);
                addCard(cardToAdd);
            }
        }
    }

    /**
     * Method to return all the cards left in the pack
     * @return returns them as a string
     */
    @Override
    public String toString() {
        int counter = 0;
        StringBuilder sb = new StringBuilder("\n");
        for (Card c : getCards()){
            counter++;
            sb.append(c).append(" ");
            if (counter >= 13){
                sb.append("\n");
                counter = 0;
            }
        }
        return sb.toString();
    }

    /**
     * Method for popping the first card off the deck and giving to the calling function
     * Also to save memory, every 10 card pops, the arrayList is trimmed to size (otherwise there would be a situation with 2 cards in a size 52 list)
     * @return returns the card
     */
    public Card nextCard() {
        Card next = popCard(0);

        if (numCards() % 10 == 0) {
            getCards().trimToSize();
        }
        return next;
    }

    /**
     * Method for shuffling the cards in the deck
     */
    public void shuffle() {
        Collections.shuffle(getCards());
        shuffled = true;
    }

    /**
     * Method for setting the Card Cover for the deck
     * @param cardCover holds the name of the new card cover
     */
    public void setCardCover(String cardCover) {
        this.cardCover = cardCover;
    }

    /**
     * Method for returning the card cover filename
     * @return returns the filename as a string
     */
    public String getCardCover() {
        return cardCover;
    }

    /**
     * Method for checking if the deck has been shuffled
     * @return returns the result
     */
    public boolean isShuffled() {
        return shuffled;
    }
}
