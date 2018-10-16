package club.greenstudio.minnimafia.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import club.greenstudio.minnimafia.rts.BetterRouteNavigator;
import club.greenstudio.minnimafia.rts.CompiledRoute;
import club.greenstudio.minnimafia.states.PlayState;

public class Player extends Entity{
    private PlayState world;
    private Vector2 movement;
    private int speed;
    private boolean stop, boost;
    private int timer;
    private boolean gotDrug;
    private BetterRouteNavigator navi;
    private int actualRoutePart;
    private int numberRoutePart;
    private CompiledRoute actualRoute;
    private int drugType;
    private Rectangle naviBounds;
    private Vector2 target;
    private boolean isNavi;

    public enum Direction {
        UP, DOWN, RIGHT, LEFT, STOP
    }

    private Direction dir;

    public Player(PlayState world, int x, int y, BetterRouteNavigator navi) {
        this.world = world;
        this.navi = navi;
        position = new Vector2(x, y);
        texture = new Texture("player.png");
        dir = Direction.STOP;
        speed = 10;
        movement = new Vector2(0, 0);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        Vector2 boundsCenter = new Vector2();
        boundsCenter.set(bounds.getCenter(boundsCenter));
        naviBounds = new Rectangle(boundsCenter.x - 10,boundsCenter.y - 10,10,10);
        stop = false;
        boost = false;
        timer = 0;
        gotDrug = false;
        actualRoutePart = 0;
        setRoute(navi.getAwayRoutes().get(world.getDrug().getType()));
        isNavi = true;
        drugType = 1;
    }

    public void update(float dt) {
        if(timer > 2000 * dt){
            timer = 0;
            boost = false;
        }
        if(boost == true){
            texture = new Texture("playerboosted.png");
            speed = 20;
            timer++;
        }else{
            speed = 10;
            texture = new Texture("player.png");
        }
        position.add(movement);
        /*
        if (position.y < 0) {
            position.y = 0;
        }
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.y > PlayState.FIELD_SIZE_HEIGHT - texture.getHeight()) {
            position.y = PlayState.FIELD_SIZE_HEIGHT - texture.getHeight();
        }
        if (position.x > PlayState.FIELD_SIZE_WIDTH - texture.getWidth()) {
            position.x = PlayState.FIELD_SIZE_WIDTH - texture.getWidth();
        }
        */
        bounds.setPosition(position.x, position.y);
        Vector2 boundsCenter = new Vector2();
        boundsCenter.set(bounds.getCenter(boundsCenter));
        naviBounds.setPosition(boundsCenter.x - 10, boundsCenter.y - 10);
        if(!stop) {
            if(isNavi) {
                goRoute(dt);
            }else {
                goTo(target, dt);
            }
        }else{
            move(Direction.STOP, dt);
            boost = true;
        }

    }

    public void dispose() {
        texture.dispose();
    }

    public Texture getPlayer() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void move(Direction dir, float dt) {

        if (dir == Direction.DOWN) {
            movement.y = speed * -10 * dt;
            movement.x = speed * 0 * dt;
        }
        if (dir == Direction.UP) {
            movement.y = speed * 10 * dt;
            movement.x = speed * 0 * dt;
        }
        if (dir == Direction.LEFT) {
            movement.y = speed * 0 * dt;
            movement.x = speed * -10 * dt;
        }
        if (dir == Direction.RIGHT) {
            movement.y = speed * 0 * dt;
            movement.x = speed * 10 * dt;
        }
        if (dir == Direction.STOP) {
            movement.y = speed * 0;
            movement.x = speed * 0;
        }
    }

    private void setRoute(CompiledRoute route){
        startRoute(route);
        drugType = world.getDrug().getType();
    }

    private void startRoute(CompiledRoute route){
        numberRoutePart = route.getSize();
        actualRoutePart = 0;
        actualRoute = route;
    }

    private void goRoute(float dt){
        Rectangle point = new Rectangle(actualRoute.getPoint(actualRoutePart).x - 5, actualRoute.getPoint(actualRoutePart).y -5, 5, 5);
        if(naviBounds.overlaps(point) == true){
            if((actualRoutePart + 1) < numberRoutePart){
                actualRoutePart++;
            }else{
                if(gotDrug){
                    target = world.getHouse().getPosition();
                    isNavi = false;
                }else {
                    target = world.getDrug().getPosition();
                    isNavi = false;
                }
            }
        }else {
            goTo(actualRoute.getPoint(actualRoutePart), dt);
        }
    }

    private void goTo(Vector2 pos, float dt) {
        Vector2 boundsCenter = new Vector2();
        boundsCenter.set(naviBounds.getCenter(boundsCenter));

        if (boundsCenter.x > pos.x + 1) {
            move(Direction.LEFT, dt);
        } else if (boundsCenter.x < pos.x - 1) {
            move(Direction.RIGHT, dt);
        } else {

        }


        if (boundsCenter.y > pos.y + 1) {
            move(Direction.DOWN, dt);
        } else if (boundsCenter.y < pos.y - 1) {
            move(Direction.UP, dt);
        } else {

        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setGotDrug(boolean gotDrug) {
        if(gotDrug == true) {
            setRoute(navi.getWaybackRoutes().get(drugType));
            this.gotDrug = gotDrug;
            isNavi = true;
        }else {
            setRoute(navi.getAwayRoutes().get(world.getDrug().getType()));
            this.gotDrug = gotDrug;
            isNavi = true;
        }
    }

    public boolean isGotDrug() {
        return gotDrug;
    }
}
