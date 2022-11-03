import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Class to blueprint the turnover deck.
 * Since this is a form of deck (Collection of face down cards), it inherits attributes and methods from the deck class
 *
 * @author Owain Gibson
 * @version 1.0
 */
public class TableDeck extends Deck{
    private ArrayList<String> tableCardString;

    /**
     * Constructor to instantiate the tableDeck object
     */
    public TableDeck(){
        super();
        tableCardString = new ArrayList<>();
    }

    /**
     * Method for merging two cards.
     * Once the bottom card is overwritten by the top card, the copy of the top card in its previous place is removed
     * @param topCardIndex  holds the index of the top card
     * @param cardToReplace holds the index of the card to replace
     */
    public void popMergeCards(int topCardIndex, int cardToReplace){
        Card topCard = popCard(topCardIndex);
        getCards().set(cardToReplace, topCard);

        String topCardString = tableCardString.get(topCardIndex);
        tableCardString.set(cardToReplace , topCardString);
        tableCardString.remove(topCardIndex);
    }

    /**
     * Method to add a card to the arrayList.
     * This overrides its super's method because it also has to add the card's filename string to an arrayList as well
     * @param card holds the card to add
     */
    @Override
    public void addCard(Card card) {
        super.addCard(card);
        tableCardString.add(card.toString() + ".gif");
    }

    /**
     * Function to return all the current piles on the table as a string.
     * If there are no current cards, this is conveyed to the player
     * @return returns the string
     */
    @Override
    public String toString() {
        if (numCards() < 1) {
            return "There are no current piles on the table.";
        } else {
            StringBuilder stringBuilder = new StringBuilder("Cards showing on the table:\n");
            for (Card c : getCards()) {
                stringBuilder.append(c);
                stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }
    }

    /**
     * Method to return the arrayList of card string to the calling function
     * @return returns the arrayList
     */
    public ArrayList<String> getCardStrings(){
        return tableCardString;
    }
}
