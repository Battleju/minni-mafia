package club.greenstudio.minnimafia.minnimafiaExceptions;

public class InvalidDirectionException extends Exception{

    public InvalidDirectionException(String message){
        super(message);
    }

    public InvalidDirectionException(){
        super("Invalid Direction chosen!");
    }

}
