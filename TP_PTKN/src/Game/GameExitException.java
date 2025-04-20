package Game;

public class GameExitException extends RuntimeException{
    private final int status;
    private final int winner;
    private final String message;

    public GameExitException(int status, int winner, String message) {
        super(message);

        this.status = status;
        this.winner = winner;
        this.message = message;
    }

    public int getStatus() { return status; }
    public int getWinner() { return winner; }

    @Override
    public String getMessage() { return message; }
}
