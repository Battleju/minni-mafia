package club.greenstudio.minnimafia.rts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class RoadTile {

    protected Texture texture;
    protected Rectangle field;
    protected Vector2 position;

    public RoadTile(int x, int y) {
        texture = new Texture("road.png");
        position = new Vector2(x, y);
        field = new Rectangle(Math.round(position.x), Math.round(position.y), texture.getWidth(), texture.getHeight());
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getField() {
        return field;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose(){
        texture.dispose();
    }
}
