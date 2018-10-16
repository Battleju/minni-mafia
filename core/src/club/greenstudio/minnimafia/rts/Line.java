package club.greenstudio.minnimafia.rts;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Line {

    Vector2 p1, p2;
    int distance;

    public enum Type{
        horizontal, vertical
    }
    private Type type;

    public Line(Vector2 p1, Vector2 p2){
        this.p1 = p1;
        this.p2 = p2;
        calculateType();
    }

    public void changePoints(){
        Vector2 oldP1 = p1;
        Vector2 oldP2 = p2;

        p1 = oldP2;
        p2 = oldP1;

        calculateType();
    }

    private void calculateType() {
        if(p1.x == p2.x){
            type = Type.vertical;
            distance = Math.round(p2.y + p1.y);
        }else {
            type = Type.horizontal;
            distance = Math.round(p2.x + p1.x);
        }
    }

    public boolean compare(Line line){
        if(p1.x == line.getP1().x && p2.x == line.getP2().x && p1.y == line.getP1().y && p2.y == line.getP2().y){
            return true;
        }
        return false;
    }

    public Rectangle forJunction(){
        if(type == Type.horizontal){
            return new Rectangle(p1.x, p1.y - 25, p2.x - p1.x, 50);
        }else {
            return new Rectangle(p1.x - 5, p1.y - 40, 10, (p2.y - p1.y) + 40);
        }

    }

    public Vector2 getP1() {
        return p1;
    }

    public void setP1(Vector2 p1) {
        this.p1 = p1;
    }

    public Vector2 getP2() {
        return p2;
    }

    public void setP2(Vector2 p2) {
        this.p2 = p2;
    }

    public int getDistance() {
        return distance;
    }

    public Type getType() {
        return type;
    }
}
