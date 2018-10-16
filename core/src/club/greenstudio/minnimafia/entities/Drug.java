package club.greenstudio.minnimafia.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import club.greenstudio.minnimafia.states.PlayState;

public class Drug extends Entity {

    private Vector2 position;
    private Rectangle bounds;
    private int type;

    public Drug(int[] spawnData) {
        texture = new Texture("drug.png");
        this.position = new Vector2(spawnData[1], spawnData[2]);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.position.x += -getTexture().getWidth()/2;
        this.position.y += -getTexture().getHeight()/2;
        type = spawnData[0];
    }

    public void dispose() {
        texture.dispose();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public boolean collides(Rectangle obj) {
        if (obj.overlaps(bounds)) {
            return true;
        } else {
            return false;
        }
    }

    public Texture getDrug() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
