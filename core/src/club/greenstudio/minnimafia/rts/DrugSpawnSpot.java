package club.greenstudio.minnimafia.rts;

import com.badlogic.gdx.graphics.Texture;

public class DrugSpawnSpot extends RoadTile{

    public DrugSpawnSpot(int x, int y) {
        super(x, y);
        texture = new Texture("drugspawnpoint.png");
        field.set(x, y, texture.getWidth(), texture.getHeight());
    }

}
