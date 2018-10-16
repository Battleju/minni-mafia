package club.greenstudio.minnimafia.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import club.greenstudio.minnimafia.states.PlayState;

public class Border extends Entity{

    private boolean open;
    private int timer;

    public Border(){
        open = false;
        texture = new Texture("BorderClosed.png");
        Random random = new Random();
        position = new Vector2(0 , 400 + random.nextInt(PlayState.FIELD_SIZE_HEIGHT/4));
        bounds = new Rectangle(position.x, position.y, texture.getWidth(),texture.getHeight());
    }

    @Override
    public void update(float dt) {
        if(timer > 15000 * dt){
            timer = 0;
            if(open){
                open = false;
                texture = new Texture("BorderClosed.png");
            }else{
                open = true;
                texture = new Texture("BorderOpen.png");
            }
        }
        timer++;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public boolean isOpen() {
        return open;
    }
}
