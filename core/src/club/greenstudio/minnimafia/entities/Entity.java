package club.greenstudio.minnimafia.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected Vector2 position;
    protected Rectangle bounds;
    protected Texture texture;

    public abstract void update(float dt);

    public boolean collides(Rectangle obj){
        return this.bounds.overlaps(obj);
    }

    public abstract void dispose();

    public Texture getTexture(){
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
