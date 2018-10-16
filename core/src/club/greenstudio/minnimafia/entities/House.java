package club.greenstudio.minnimafia.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import club.greenstudio.minnimafia.states.PlayState;

public class House extends Entity{

    public House(){
        texture = new Texture("house.png");
        Random random = new Random();
        position = new Vector2(random.nextInt(PlayState.FIELD_SIZE_WIDTH - 150) , 10);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(),texture.getHeight());
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
