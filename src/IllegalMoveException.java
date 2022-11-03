/**
 * Class to hold create a new exception called IllegalMoveException
 * This will be thrown when the player or computer attempt a move that breaks the game's rules
 *
 * @author Owain Gibson
 * @version 1.0
 */
public class IllegalMoveException extends Exception{
    public IllegalMoveException(String message){
        super(message);
    }
}
