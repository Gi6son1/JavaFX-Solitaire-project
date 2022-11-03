import java.util.ArrayList;

/**
 * Abstract class to specify how a Deck should behave and what it should have
 * It is abstract because this class has no use by itself, and is only really useful
 * as a template for its subclasses, which both share some similarities but also differences
 *
 * @author Owain Gibson
 * @version 1.0
 */
abstract class Deck {
    private ArrayList<Card> cards;

    /**
     * Constructor for initialising the deck object, mainly the arraylist that will store the cards
     */
    public Deck(){
        this.cards = new ArrayList<>();
    }

    /**
     * Method to return the card at the specific given index
     * @param cardIndex holds the index
     * @return returns the card if the index given is within the range of card indexes, otherwise null
     */
    public Card getCard(int cardIndex){
        if (cardIndex >= cards.size() || cardIndex < 0) {
            return null;
        }
        return cards.get(cardIndex);
    }

    /**
     * Method from "popping" a card from the arrayList.
     * This means that the card can be returned while also being removed from the list (inspired by Linked Lists)
     * @param cardIndex holds the card to pop
     * @return returns the popped card
     */
    public Card popCard(int cardIndex){
        Card poppedCard = cards.get(cardIndex);
        cards.remove(cardIndex);
        return poppedCard;
    }

    /**
     * Method to return the number of cards in the card arraylist
     * @return returns the number
     */
    public int numCards(){
        return cards.size();
    }

    /**
     * Method for adding a card to the arrayList
     * @param card holds the card to add
     */
    public void addCard(Card card){
        cards.add(card);
    }

    /**
     * Method for returning all the cards in the arrayList to the calling function
     * It is passed by reference, rather than copy, so that the subclasses can call perform operations on it if needed
     * @return returns the arrayList
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

}
