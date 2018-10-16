package club.greenstudio.minnimafia.rts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

import club.greenstudio.minnimafia.minnimafiaExceptions.InvalidDirectionException;
import club.greenstudio.minnimafia.MinniMafia;
import club.greenstudio.minnimafia.states.PlayState;

public class RoadTravelSystem {

    private ArrayList<RoadTile> road;
    private PlayState world;
    private DrugSpawnSpot[] drugSpawnSpots;
    private Rectangle objStart;
    private RoadTile sample;
    private RoadTile lastRoadTile;
    private BetterRouteNavigator betterRouteNavigator;
    private ArrayList<RoadTile> roadsH;
    private ArrayList<RoadTile> roadsV;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    public RoadTravelSystem(PlayState ps, Rectangle start) {
        sample = new RoadTile(0, 0);
        objStart = start;
        road = new ArrayList<RoadTile>();
        world = ps;
        betterRouteNavigator = new BetterRouteNavigator(this, objStart);
        roadsH = new ArrayList<RoadTile>();
        roadsV = new ArrayList<RoadTile>();
    }

    public RoadTravelSystem(PlayState ps, Rectangle start, boolean autogenerate) {
        this(ps, start);
        if (autogenerate) {
            generateRoad();
        }
    }

    public void generateRoad() {
        Random random = new Random();

        //Generate DrugSpawnSpots (End of Road)
        drugSpawnSpots = new DrugSpawnSpot[3];
        for (int i = 0; i < drugSpawnSpots.length; i++) {
            drugSpawnSpots[i] = new DrugSpawnSpot(0, 0);
        }
        int x, y;
        int dis_obj2_obj1 = ((MinniMafia.WIDTH / 3 - 20) / 2 - ((MinniMafia.WIDTH / 3 - 20) / 2) / 2) + random.nextInt(((MinniMafia.WIDTH / 3 - 20) / 2 + ((MinniMafia.WIDTH / 3 - 20) / 2) / 2) - ((MinniMafia.WIDTH / 3 - 20) / 2 - ((MinniMafia.WIDTH / 3 - 20) / 2) / 2));
        int dis_obj3_obj2 = ((MinniMafia.WIDTH / 3 - 20) / 2 - ((MinniMafia.WIDTH / 3 - 20) / 2) / 2) + random.nextInt(((MinniMafia.WIDTH / 3 - 20) / 2 + ((MinniMafia.WIDTH / 3 - 20) / 2) / 2) - ((MinniMafia.WIDTH / 3 - 20) / 2 - ((MinniMafia.WIDTH / 3 - 20) / 2) / 2));
        int edge = (MinniMafia.WIDTH - (dis_obj2_obj1 + dis_obj3_obj2 + drugSpawnSpots[0].getTexture().getWidth() * 3)) / 2;
        for (int i = 0; i < drugSpawnSpots.length; i++) {
            y = 750 + random.nextInt(PlayState.FIELD_SIZE_HEIGHT / 10);
            if (i == 0) {
                x = edge;
            } else if (i == 1) {
                x = edge + drugSpawnSpots[0].getTexture().getWidth() + dis_obj2_obj1;
            } else if (i == 2) {
                x = edge + drugSpawnSpots[0].getTexture().getWidth() + dis_obj2_obj1 + drugSpawnSpots[1].getTexture().getWidth() + dis_obj3_obj2;
            } else {
                x = -1;
            }
            if (x == -1) {
                throw new NullPointerException();
            } else {
                drugSpawnSpots[i] = new DrugSpawnSpot(x, y);
            }
        }


        //Generate start of road
        if (objStart.x + objStart.width / 2 > PlayState.FIELD_SIZE_WIDTH / 2) {
            //Road starts at the left side
            roadsV.add(createRoadTile(Math.round(objStart.x - sample.texture.getWidth()), Math.round(objStart.y)));
        } else {
            //Road starts at the right side
            roadsV.add(createRoadTile(Math.round(objStart.x + objStart.width), Math.round(objStart.y)));
        }


        //Generate road up to first horizontal road
        for (int i = 0; i < 4 + random.nextInt(4); i++) {
            roadsV.add(buildTileRoad(getLastRoadTile().position, Direction.UP));
        }


        //Generate first horizontal Road
        buildHorizontalRoad(getLastRoadTile().position, 65, 410);

        //Generate vertical road
        float horizontalRoadY = getLastRoadTile().getPosition().y;
        buildVerticalRoad(new Vector2(65, horizontalRoadY), Direction.UP, 700);
        buildVerticalRoad(new Vector2(180, horizontalRoadY), Direction.UP, 700);
        buildVerticalRoad(new Vector2(295, horizontalRoadY), Direction.UP, 700);
        buildVerticalRoad(new Vector2(410, horizontalRoadY), Direction.UP, 700);

        //Generate second horizontal road
        buildHorizontalRoad(getLastRoadTile().position, 65, 410);

        //Generate road up to end
        float horizontalRoad2Y = getLastRoadTile().getPosition().y;

        if (drugSpawnSpots[0].position.x < 65) {
            buildVerticalRoad(new Vector2(65, horizontalRoad2Y), Direction.UP, Math.round(drugSpawnSpots[0].position.y));
        } else {
            buildVerticalRoad(new Vector2(drugSpawnSpots[0].position.x, horizontalRoad2Y), Direction.UP, Math.round(drugSpawnSpots[0].position.y));
        }
        buildVerticalRoad(new Vector2(drugSpawnSpots[1].position.x, horizontalRoad2Y), Direction.UP, Math.round(drugSpawnSpots[1].position.y));
        if (drugSpawnSpots[2].position.x > 410) {
            buildVerticalRoad(new Vector2(410, horizontalRoad2Y), Direction.UP, Math.round(drugSpawnSpots[2].position.y));
        } else {
            buildVerticalRoad(new Vector2(drugSpawnSpots[2].position.x, horizontalRoad2Y), Direction.UP, Math.round(drugSpawnSpots[2].position.y));
        }

        //routeNavigator.generateRoutes();
        betterRouteNavigator.generateRoutes();
    }

    private void buildVerticalRoad(Vector2 connectionPoint, Direction dir, int endY) {
        if (dir == Direction.UP) {
            setLastRoadTile(new RoadTile(Math.round(connectionPoint.x), Math.round(connectionPoint.y)));
            while (getLastRoadTile().position.y < endY) {
                roadsV.add(buildTileRoad(getLastRoadTile().position, Direction.UP));
            }
            if (getLastRoadTile().position.y < endY) {
                getLastRoadTile().position.y = endY;
            }
        } else if (dir == Direction.DOWN) {
            setLastRoadTile(new RoadTile(Math.round(connectionPoint.x), Math.round(connectionPoint.y)));
            while (getLastRoadTile().position.y > endY) {
                roadsV.add(buildTileRoad(getLastRoadTile().position, Direction.DOWN));
            }
            if (getLastRoadTile().position.y > endY) {
                getLastRoadTile().position.y = endY;
            }
        } else {
            try {
                throw new InvalidDirectionException();
            } catch (InvalidDirectionException e) {
                e.printStackTrace();
            }
        }

    }

    private void buildHorizontalRoad(Vector2 connectionPoint, int leftEndX, int rightEndX) {
        //Left Part
        setLastRoadTile(new RoadTile(Math.round(connectionPoint.x), Math.round(connectionPoint.y)));
        while (getLastRoadTile().position.x > leftEndX) {
            roadsH.add(buildTileRoad(getLastRoadTile().position, Direction.LEFT));
        }
        if (getLastRoadTile().position.x < leftEndX) {
            getLastRoadTile().position.x = leftEndX;
        }

        //Right Part
        setLastRoadTile(new RoadTile(Math.round(connectionPoint.x), Math.round(connectionPoint.y)));
        while (getLastRoadTile().position.x < rightEndX) {
            roadsH.add(buildTileRoad(getLastRoadTile().position, Direction.RIGHT));
        }
        if (getLastRoadTile().position.x > rightEndX) {
            getLastRoadTile().position.x = rightEndX;
        }
    }

    private RoadTile buildTileRoad(Vector2 connectionPoint, Direction dir) {
        if (dir == Direction.UP) {
            return createRoadTile(Math.round(connectionPoint.x), Math.round(connectionPoint.y + sample.getTexture().getHeight()));
        } else if (dir == Direction.DOWN) {
            return createRoadTile(Math.round(connectionPoint.x), Math.round(connectionPoint.y - sample.getTexture().getHeight()));
        } else if (dir == Direction.LEFT) {
            return createRoadTile(Math.round(connectionPoint.x - sample.getTexture().getWidth()), Math.round(connectionPoint.y));
        } else if (dir == Direction.RIGHT) {
            return createRoadTile(Math.round(connectionPoint.x + sample.getTexture().getWidth()), Math.round(connectionPoint.y));
        }
        return null;
    }

    private RoadTile createRoadTile(int x, int y) {
        RoadTile o = new RoadTile(x, y);
        addRoadTile(o);
        return o;
    }

    private RoadTile addRoadTile(RoadTile o) {
        road.add(o);
        lastRoadTile = o;
        return o;
    }

    private RoadTile getLastRoadTile() {
        return lastRoadTile;
    }

    private void setLastRoadTile(RoadTile lastRoadTile) {
        this.lastRoadTile = lastRoadTile;
    }

    public void renderRoad(SpriteBatch sb) {
        for (int i = 0; i < road.size(); i++) {
            sb.draw(road.get(i).getTexture(), road.get(i).getPosition().x, road.get(i).getPosition().y);
        }
        for (int i = 0; i < drugSpawnSpots.length; i++) {
            sb.draw(drugSpawnSpots[i].getTexture(), drugSpawnSpots[i].getPosition().x, drugSpawnSpots[i].getPosition().y);
        }
    }

    public void dispose() {
        for (int i = 0; i < road.size(); i++) {
            road.get(i).dispose();
        }
    }

    public DrugSpawnSpot[] getDrugSpawnSpots() {
        return drugSpawnSpots;
    }

    public ArrayList<RoadTile> getRoad() {
        return road;
    }

    public BetterRouteNavigator getBetterRouteNavigator() {
        return betterRouteNavigator;
    }

    public ArrayList<RoadTile> getRoadsH() {
        return roadsH;
    }

    public ArrayList<RoadTile> getRoadsV() {
        return roadsV;
    }
}
