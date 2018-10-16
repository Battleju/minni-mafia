package club.greenstudio.minnimafia.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Comparator;

public class EntityManager {

    ArrayList<Entity> entities;
    private Comparator<Entity> renderSorter;

    public EntityManager() {
        entities = new ArrayList<Entity>();
        renderSorter = new Comparator<Entity>() {
            @Override
            public int compare(Entity entity, Entity t1) {
                if (entity.position != null && t1.position != null) {
                    if (entity.position.y + entity.getTexture().getHeight() > t1.position.y + t1.getTexture().getHeight()) {
                        return -1;
                    }
                    return 1;
                }
                return 0;
            }


        };
    }

    public void addEntity(Entity e){
        entities.add(e);
    }

    public void disposeEntity(Entity e){
        entities.remove(e);
        e.dispose();
    }

    public void renderEntities(SpriteBatch sb){
        //needs android 6 support

        for(int i = 0; i < entities.size(); i++){
            sb.draw(entities.get(i).getTexture(), entities.get(i).getPosition().x, entities.get(i).getPosition().y);
        }
        entities.sort(renderSorter);
    }

    public void updateEntities(float dt){

        for(int i = 0; i < entities.size(); i++){
            entities.get(i).update(dt);
        }
    }

    public void disposeAll() {
        for(int i = 0; i < entities.size(); i++){
            entities.get(i).dispose();
            entities.remove(i);
        }
    }
}
