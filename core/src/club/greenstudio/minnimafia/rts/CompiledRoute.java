package club.greenstudio.minnimafia.rts;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CompiledRoute {
    ArrayList<Vector2> points;

    public CompiledRoute() {
        points = new ArrayList<Vector2>();
    }

    public void addPoint(Vector2 p){
        points.add(p);
    }

    public Vector2 getPoint(int index){
            return  points.get(index);
    }
    public int getSize(){
        return points.size();
    }

    public void removePoint(int index){
        points.remove(index);
    }
}
