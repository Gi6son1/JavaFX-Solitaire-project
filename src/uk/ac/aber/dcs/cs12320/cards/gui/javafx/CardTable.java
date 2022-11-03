/**
 * Displays the card images on a table (the Javafx stage)
 * @author Faisal Rezwan, Chris Loftus and Lynda Thomas
 * @version 3.0
 */
package uk.ac.aber.dcs.cs12320.cards.gui.javafx;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class CardTable {
    private final Stage stage;
    private String[] cards;
    private boolean done;
    private int width;

    public CardTable(Stage stage) {
        this.stage = stage;
        stage.setTitle("The Cards");
    }

    /**
     * Called when the user quits the game. It results
     * in the face-down pack of cards not being displayed.
     */
    public void allDone() {
        done = true;
    }

    /**
     * Displays all the face-up cards (just the top showing cards)
     * and if the game is not over then also displays the face-down deck.
     * @param c the list of face-up cards
     */
    public void cardDisplay(List<String> c, String cardCover) {

        // We need to do this within the GUI thread. We assume
        // that the method is called by a non-GUI thread
        Platform.runLater(() -> {
            cards = new String[c.size()];
            cards = c.toArray(cards);

            ScrollPane sp = new ScrollPane();

            //FLAIR COMMAND - Dynamic Stage Sizing
            width = (c.size() < 9)? 125 + (100 * c.size()) : 1025; //changes the width of the window depending on the number of cards.
                                                        // Width has a max size of 10 cards, before going to scroll
            Scene scene = new Scene(sp, width, 155);

            scene.setFill(Color.BLACK);
            HBox box = new HBox();
            Image image = null;

            if (cards != null) {
                for (String card : cards) {
                    String file = "cards/" + card;
                    image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                    drawCards(box, image);
                }
            }

            if (!done) {
                // Draws the face-down top card of our pack of cards
                String file = "cards/" + cardCover;
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                drawCards(box, image);
            }

            sp.setContent(box);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            stage.setScene(scene);
            stage.show();
        });

    }

    private void drawCards(HBox box, Image image) {
        ImageView iv;
        iv = new ImageView();
        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        box.getChildren().add(iv);
    }


}
