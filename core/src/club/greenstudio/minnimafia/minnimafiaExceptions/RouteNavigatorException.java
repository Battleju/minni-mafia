package club.greenstudio.minnimafia.minnimafiaExceptions;

public class RouteNavigatorException extends Exception {

    public RouteNavigatorException(String message) {
        super(message);
    }

    public RouteNavigatorException() {
        super("Faild to generate a Route!");
    }

}
