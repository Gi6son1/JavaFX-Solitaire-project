/**
 * The Game of patience main class
 *
 * @author Faisal Rezwan, Chris Loftus, Lynda Thomas, and Owain Gibson
 * @version 4.0
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.application.Application;

import javafx.stage.Stage;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

public class Game extends Application {

    private Scanner inputScanner;

    private TurnoverDeck tDeck;
    private TableDeck currentPiles;
    private ScoreFile scoreFile;

    /**
     * These are final because they hold the filename of the acheivement cover cards and there is no need to change them aby accident and cause errors
     */
    private final String specialCover = "bultima.gif";
    private final String goldCover = "bgold.gif";
    private final String silverCover = "bsilver.gif";
    private final String bronzeCover = "bbronze.gif";

    /**
     * These booleans are used to enforce fair play i.e. stopping player from reshuffling cards once started,
     * or stopping them from saving their score if a computer was used to play for them.
     */
    private boolean gameStarted;
    private boolean cheated;

    private CardTable cardTable;

    /**
     * Constructor to instantiate all the objects required to run the game
     * It also calls the checkDeckCover so that the correct card cover is loaded from the beginning
     * @throws FileNotFoundException if the TurnoverDeck object cannot find the card file
     */
    public Game() throws FileNotFoundException{
        inputScanner = new Scanner(System.in);
        currentPiles = new TableDeck();
        scoreFile = new ScoreFile();
        tDeck = new TurnoverDeck();
        checkDeckCover();
    }

    /**  FLAIR METHOD
     *
     *  This method is used to check the top score in the file, and then change the face-downn card cover depending on the score
     *  If the highest score is less than 30, the egyptian-style cover is replaced with a bronze card cover
     *  If the highest score is less than 20, the egyptian-style cover is replaced with a silver card cover
     *  If the highest score is less than 10, the egyptian-style cover is replaced with a gold card cover
     *  If the highest score is the best possible score, the egyptian-style cover is replaced with a special card cover
     */
    private void checkDeckCover(){
        int score = scoreFile.getTopScore();
        if (score != -1){
            if (score == 1){
                tDeck.setCardCover(specialCover);
            }
            else if (score < 10){
                tDeck.setCardCover(goldCover);
            }
            else if (score <20){
                tDeck.setCardCover(silverCover);
            }
            else if (score < 30){
                tDeck.setCardCover(bronzeCover);
            }
        }
    }

    /**
     * Method to initialise the boolean "fair play" functions, and then immediately start the game thread
     */
    private void playGame() {
        gameStarted = false;
        cheated = false;
        Application.launch();
    }

    /**
     * FLAIR METHOD
     * <p>
     * This is a method for checking the user's input when selecting options that require a specific string input.
     * Since the function repeats until a valid input is received, it means the calling method is GUARANTEED a valid input to use, and does not require its own error checking.
     *
     * @param acceptableValues holds the list of acceptable values to be entered
     * @return returns the accepted input from the user
     */
    private String readOptionInput(String... acceptableValues) {
        String input;
        boolean badInput = true;
        do {
            System.out.println();
            System.out.print("| Enter input: ");
            input = inputScanner.nextLine().toUpperCase();
            for (String value : acceptableValues) {
                if (input.equals(value)) {
                    badInput = false;
                    break;
                }
            }
            if (badInput) {
                System.out.println("Invalid answer, please try again");
            }

        } while (badInput);
        return input;
    }

    /**
     * FLAIR METHOD
     * <p>
     * This is a method for checking the user's input when inputting an answer that has to be an integer
     * Since the function repeats until a valid input is received, it means the calling method is GUARANTEED a valid input to use, and does not require its own error checking.
     *
     * @return returns the accepted int from the user
     */
    private int readIntInput() {
        boolean acceptableInput = false;
        int input = 0;
        while (!acceptableInput) {
            System.out.print("| Enter number: ");
            try {
                input = inputScanner.nextInt();
                acceptableInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Your input must be an integer, try again");
            } finally {
                inputScanner.nextLine();
            }
        }
        return input;
    }

    /**
     * Method to merge one card with another, given by their index.
     * If they are deemed merge-able, then this is done within the TableDeck class, for cohesion
     *
     * @param cardToMove hold the index of the card to merge on top of the othe card
     * @param numCardsOver holds the number of cards over that the bottom card is, so that it may be found
     * @throws IllegalMoveException throws if the two cards aren't deemed merge-able
     */
    private void moveCards(int cardToMove, int numCardsOver) throws IllegalMoveException {
        int mergeCardIndex = cardToMove - numCardsOver;

        Card topCard = currentPiles.getCard(cardToMove);
        Card cardToMerge = currentPiles.getCard(mergeCardIndex);

        if (topCard.equals(cardToMerge)) {
            currentPiles.popMergeCards(cardToMove, mergeCardIndex);
        } else {
            throw new IllegalMoveException("Illegal move: The cards aren't of the same suit or value, so cannot be stacked");
        }
    }

    /**
     * Method to move the last card onto the card that came before it.
     * It does this using the moveCards function, but beforehand it checks if there are two cards on the table to move first
     * @throws IllegalMoveException if there aren't enough cards on the table to perform the move
     */
    private void moveOntoPrevious() throws IllegalMoveException {
        if (currentPiles.numCards() < 2) {
            throw new IllegalMoveException("There needs to be at least 2 piles on the table to make this move.");
        }

        moveCards(currentPiles.numCards() - 1, 1);
    }

    /**
     * Method to move the last card onto the card that is 2 across from it.
     * It does this using the moveCards function, but beforehand it checks if there are enough cards to skip over on the table first
     * @throws IllegalMoveException if there aren't enough cards on the table to perform the move
     */
    private void moveOverTwo() throws IllegalMoveException {
        if (currentPiles.numCards() < 4) {
            throw new IllegalMoveException("There needs to be at least 4 piles on the table to make this move.");
        }

        moveCards(currentPiles.numCards() - 1, 3);
    }

    /**
     * Method to let the computer perform a move for the user.
     * If the computer cannot find a move to play, then it draws a card instead
     * Since this method means the computer helps the player, it is considered cheating,
     * so the boolean is set to true and the score will not be saved
     * @throws IllegalMoveException if there are no more moves to play and no more cards to draw
     */
    private void playForMeOnce() throws IllegalMoveException {
        cheated = true;
        if (!testAmalgamate()) { //checks if there are any cards to merge
            try {
                dealCard();
            } catch (IllegalMoveException e) { //if this is the first move of the game and the player hasn't shuffled,
                // the computer cannot do a move and waits for the player to shuffle first
                if (!tDeck.isShuffled()) {
                    System.out.println(e.getMessage());
                } else {
                    throw new IllegalMoveException("No more moves available");
                }
            }
        }
    }

    /**
     * Method to check if an amalgamate move is possible and the furthest move at that
     * If it is, it completes the move and returns back to the calling function
     *
     * @return true if it completed a move, false if there were none
     */
    private boolean testAmalgamate() {
        int totalCurrentPiles = currentPiles.numCards() - 1;
        Card currentCard;
        Card cardToCompare;

        for (int i = 0; i < totalCurrentPiles; i++) { //starts checking from the beginning rather than the end
            currentCard = currentPiles.getCard(i);

            for (int j = i + 3; j > i; j -= 2) { //starts by looking at the "furthest move" for that card
                cardToCompare = currentPiles.getCard(j);

                if (currentCard.equals(cardToCompare)) {
                    try {
                        amalgamate(j, i);
                        return true;
                    } catch (IllegalMoveException ignored) { //this is ignored because it will never show up
                                                                // - all risks are checked beforehand (i.e. card distance and equality)

                    }
                }
            }
        }
        return false;
    }

    /** FLAIR FUNCTION - i think?
     * Method to call the "play for me once" function as many times as the user specifies.
     * Also displays the number of successfully completed moves, for the user's convenience
     * in case they want to know how many worked before no more moves could be done
     */
    private void playForMeMany() {
        System.out.println("Enter the number of moves you want the computer to play:");
        int numMoves = readIntInput();

        int movesPlayed = 0;
        try {
            for (int i = 0; i < numMoves; i++) {
                playForMeOnce();
                movesPlayed++;
            }
        } catch (IllegalMoveException e) {
            System.out.println("Unable to play all of requested moves.");
        } finally {
            System.out.println("Played " + movesPlayed + " out of " + numMoves + " requested moves.");
        }
    }

    /**
     * Method to deal a card from the TurnoverDeck
     * The card is then placed on the TableDeck
     * If the card drawn was the last card, it conveys this to the CardTable object to remove the face-down card cover from the stage
     * @throws IllegalMoveException if a) the user hasn't shuffled the cards yet or b) there are no more cards to draw
     */
    private void dealCard() throws IllegalMoveException {
        if (tDeck.isShuffled()) {
            gameStarted = true;
        } else {
            throw new IllegalMoveException("The cards must be shuffled at least once before starting.");
        }
        if (tDeck.numCards() > 0) {
            Card newCard = tDeck.nextCard();
            currentPiles.addCard(newCard);
        } else {
            throw new IllegalMoveException("There are no more cards in the pack.");
        }
        if (tDeck.numCards() == 0) {
            cardTable.allDone();
        }
    }


    /**
     * Method to amalgamate piles in the middle.
     * Before amalgamating, it performs a series of checks to make sure the move is valid
     * @param pileLocation holds the index of the pile to move
     * @param moveLocation holds the index of the pile to merge onto
     * @throws IllegalMoveException if the indexes are outside the range of pile indexes, the user tries to stack backwards,
     *      or the distances between the piles means the move is illegal
     */
    private void amalgamate(int pileLocation, int moveLocation) throws IllegalMoveException {
        if (pileLocation >= currentPiles.numCards() || moveLocation < 0) {
            throw new IllegalMoveException("Illegal move: Pile numbers cannot be outside the range of piles on the table.");
        }

        int numPilesOver = pileLocation - moveLocation;

        if (numPilesOver < 0) {
            throw new IllegalMoveException("Illegal move: You cannot stack an earlier pile onto a later pile.");
        }

        if (numPilesOver == 1 || numPilesOver == 3) {
            moveCards(pileLocation, numPilesOver);
        } else {
            throw new IllegalMoveException("Cannot move piles: must be either 1 apart or 3 apart");
        }
    }

    /**
     * Method to print the top 10 scores in the score file
     * It only prints up to the number of actual results in the file
     * i.e. if only 6 scores, rank up to 6 rather than 10
     */
    private void showTop10() {
        System.out.println(scoreFile.toString());
    }

    /**
     * Method to run the game menu for the player
     * This is where the user interacts with the game, they are able to perform moves using the menu
     * @return true if the user still wants to play (i.e. they haven't pressed quit), if they want to stop, return false
     */
    private boolean runMenu() {
        String response;
        printMenuOptions();
        response = readOptionInput("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Q");
        try {
            switch (response) {
                case "1":
                    System.out.print(tDeck);
                    break;

                case "2":
                    if (gameStarted) {
                        System.out.println("You cannot shuffle the cards once the game has started.");
                    } else {
                        tDeck.shuffle();
                        System.out.println("The cards have been shuffled.");
                    }
                    break;
                case "3":
                    dealCard();
                    break;

                case "4":
                    moveOntoPrevious();
                    break;

                case "5":
                    moveOverTwo();
                    break;

                case "6":
                    System.out.println("Enter the pile you want to move:");
                    int pileLocation = readIntInput() - 1;
                    System.out.println("Where would you like to move it:");
                    int moveLocation = readIntInput() - 1;

                    amalgamate(pileLocation, moveLocation);
                    break;

                case "7":
                    System.out.print(currentPiles);
                    break;

                case "8":
                    playForMeOnce();
                    break;

                case "9":
                    playForMeMany();
                    break;

                case "10":
                    showTop10();
                    break;

                case "Q":
                    return false;
            }
        } catch (IllegalMoveException e) { //putting the whole switch statement inside a try-catch block means that only one
                                                //block is needed to catch all the forms of illegalmoveException that may arise (polymorphism)
            System.out.println(e.getMessage());
        }

        System.out.println();
        return true;
    }

    /**
     * Method to save the player's score to the scoreFile
     * The player enters their name (must be alphanumeric otherwise they will be asked to enter a different name that is alphanumeric)
     */
    private void saveScore() {
        int pilesLeft = currentPiles.numCards() + tDeck.numCards();
        String playerName = null;
        boolean badName = true;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");

        while (badName) {
            System.out.println("Please enter your name to save the score:");
            playerName = inputScanner.nextLine();

            if (playerName.isEmpty() || (!p.matcher(playerName).find())) {
                System.out.println("Name must be contain numbers and letters ONLY with no spaces.");
                System.out.println();
            } else {
                badName = false;
            }
        }

        try {
            scoreFile.addScore(new Score(pilesLeft, playerName));
            System.out.println("| SCORE SAVED");
        } catch (IOException e) { //if there is an error saving the score (due to IO error), this is conveyed to the user
            System.err.println("An error occurred whilst saving the score");
        }
    }

    /**
     * Very short method to display the end message to the user, asking them to close the GUI window to close properly
     */
    private void endMessage() {
        System.out.println();
        System.out.println("| TO FULLY CLOSE, PLEASE CLOSE THE GUI WINDOW");
    }


    /**
     * Method that prints the title of the game in ASCII art - because FLAIR
     */
    private void printTitle() {
        System.out.println("|========================================================================================|");
        System.out.println("|    ___       __  _                  _                          __                      |");
        System.out.println("|   / _ \\___ _/ /_(_)__ ___  _______ (_) ___ _  _______ ________/ / ___ ____ ___ _  ___  |");
        System.out.println("|  / ___/ _ `/ __/ / -_) _ \\/ __/ -_)   / _ `/ / __/ _ `/ __/ _  / / _ `/ _ `/  ' \\/ -_) |");
        System.out.println("| /_/   \\_,_/\\__/_/\\__/_//_/\\__/\\__(_)  \\_,_/  \\__/\\_,_/_/  \\_,_/  \\_, /\\_,_/_/_/_/\\__/  |");
        System.out.println("|                                                                 /___/                  |");
        System.out.println("|========================================================================================|");
        System.out.println();
    }

    /**
     * Method to print all the menu options to the user
     */
    private void printMenuOptions() {
        System.out.println("| Choose an option below by entering the key corresponding to your chosen option:");
        System.out.println("| 1  -  Print the pack out");
        System.out.println("| 2  -  Shuffle cards");
        System.out.println("| 3  -  Deal a card");
        System.out.println("| 4  -  Make a move (last card onto previous pile)");
        System.out.println("| 5  -  Make a move (last card onto pile skipping over 2 piles)");
        System.out.println("| 6  -  Amalgamate piles in middle (by giving pile numbers - start with 1)");
        System.out.println("| 7  -  Print displayed cards in text form");
        System.out.println("| 8  -  Play for me once (if 2 possible moves, makes the 'furthest' one)");
        System.out.println("| 9  -  Play for me many (if 2 possible moves, makes the 'furthest' one)");
        System.out.println("| 10 -  Show top 10 scores");
        System.out.println("| Q  -  Quit game (and save score)");
        System.out.println("| NOTE: USING \"PLAY FOR ME\" OPTIONS WILL MEAN YOU CANNOT SAVE YOUR SCORE");
    }

    /**
     * Method that starts the commandLine thread, so that it can run concurrently with the GUI
     * @param stage holds the stage to be used for displaying the cards
     */
    @Override
    public void start(Stage stage){

        cardTable = new CardTable(stage);


        // The interaction with this game is from a command line
        // menu. We need to create a separate non-GUI thread
        // to run this in. DO NOT REMOVE THIS.
        Runnable commandLineTask = () -> {
            // REPLACE THE FOLLOWING EXAMPLE WITH YOUR CODE

            printTitle();
            do {
                cardTable.cardDisplay(currentPiles.getCardStrings(), tDeck.getCardCover());
            } while (runMenu());

            if (gameStarted && !cheated) {
                saveScore();
            }
            endMessage();
        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();

    }

    // //////////////////////////////////////////////////////////////
    public static void main(String args[]) {

        try { //attempts to create a new game
            Game game = new Game();
            game.playGame();
        } catch (FileNotFoundException e){ //if there is a game-breaking bug (card file not found), this is conveyed and the program ends
            System.err.println("It seems that the file used to load the cards cannot be found, and thus the game cannot run.");
        }

        System.out.println("GAME OVER");
    }
}