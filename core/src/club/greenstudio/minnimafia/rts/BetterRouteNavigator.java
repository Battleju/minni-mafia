package club.greenstudio.minnimafia.rts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import club.greenstudio.minnimafia.entities.Entity;
import club.greenstudio.minnimafia.states.PlayState;
import club.greenstudio.minnimafia.utils.DebugClass;

public class BetterRouteNavigator {

    private RoadTravelSystem rts;
    private ArrayList<RoadTile> roadH;
    private ArrayList<RoadTile> roadV;
    private DrugSpawnSpot[] drugSpawnSpots;
    private ArrayList<BetterRoute> start_dsp_group;
    private ArrayList<BetterRoute> dsp_start_group;
    private ArrayList<CompiledRoute> awayRoutes;
    private ArrayList<CompiledRoute> waybackRoutes;
    private Rectangle objStart;
    private final RoadTile sampleRoadTile;
    private ArrayList<Vector2> centerPointsH;
    private ArrayList<Vector2> centerPointsV;
    private ArrayList<Line> linesH;
    private ArrayList<Line> linesV;
    private ArrayList<Line> lines;
    private Comparator<Vector2> sorterX;
    private Comparator<Vector2> sorterY;

    public BetterRouteNavigator(RoadTravelSystem rts, Rectangle start) {
        this.rts = rts;
        objStart = start;
        sampleRoadTile = new RoadTile(0, 0);

        sorterX = new Comparator<Vector2>() {
            @Override
            public int compare(Vector2 cP1, Vector2 cP2) {
                if (cP1 != null && cP2 != null) {
                    if (cP1.x > cP2.x) {
                        return -1;
                    }
                    return 1;
                }
                return 0;
            }
        };
        sorterY = new Comparator<Vector2>() {
            @Override
            public int compare(Vector2 cP1, Vector2 cP2) {
                if (cP1 != null && cP2 != null) {
                    if (cP1.y > cP2.y) {
                        return -1;
                    }
                    return 1;
                }
                return 0;
            }
        };
    }

    public void generateRoutes() {
        roadH = rts.getRoadsH();
        roadV = rts.getRoadsV();
        centerPointsH = new ArrayList<Vector2>();
        centerPointsV = new ArrayList<Vector2>();
        linesH = new ArrayList<Line>();
        linesV = new ArrayList<Line>();
        start_dsp_group = new ArrayList<BetterRoute>();
        dsp_start_group = new ArrayList<BetterRoute>();

        //calculate centerPoints
        for (int i = 0; i < roadH.size(); i++) {
            Vector2 centerPoint = new Vector2();
            centerPoint.set(roadH.get(i).getField().getCenter(centerPoint));
            centerPointsH.add(centerPoint);
        }
        for (int i = 0; i < roadV.size(); i++) {
            Vector2 centerPoint = new Vector2();
            centerPoint.set(roadV.get(i).getField().getCenter(centerPoint));
            centerPointsV.add(centerPoint);
        }

        //genrate horizontal lines
        ArrayList<Integer> linesYH = new ArrayList<Integer>();
        for (int i = 0; i < centerPointsH.size(); i++) {
            linesYH.add(Math.round(centerPointsH.get(i).y));
        }
        Set<Integer> setH = new LinkedHashSet<Integer>(linesYH);
        linesYH = new ArrayList<Integer>(setH);
        Vector2 maxX = new Vector2(0, 0);
        Vector2 minX = new Vector2(1000, 1000);
        for (int i = 0; i < linesYH.size(); i++) {

            for (int c = 0; c < centerPointsH.size(); c++) {
                if (centerPointsH.get(c).y == linesYH.get(i) && centerPointsH.get(c).x < minX.x) {
                    minX = centerPointsH.get(c);
                }
                if (centerPointsH.get(c).y == linesYH.get(i) && centerPointsH.get(c).x > maxX.x) {
                    maxX = centerPointsH.get(c);
                }
            }

        }

        for (int i = 0; i < linesYH.size(); i++) {
            linesH.add(new Line(new Vector2(minX.x, linesYH.get(i)), new Vector2(maxX.x, linesYH.get(i))));
        }

        //Generate vertical Lines
        ArrayList<Integer> linesXV = new ArrayList<Integer>();
        for (int i = 0; i < centerPointsV.size(); i++) {
            linesXV.add(Math.round(centerPointsV.get(i).x));
        }
        Set<Integer> setV = new LinkedHashSet<Integer>(linesXV);
        linesXV = new ArrayList<Integer>(setV);
        Vector2 maxY = new Vector2(0, 0);
        Vector2 minY = new Vector2(1000, 1000);
        for (int i = 0; i < linesXV.size(); i++) {
            maxY = new Vector2(0, 0);
            minY = new Vector2(1000, 1000);
            for (int c = 0; c < centerPointsV.size(); c++) {
                if (centerPointsV.get(c).x == linesXV.get(i) && centerPointsV.get(c).y < minY.y) {
                    minY = centerPointsV.get(c);
                }
                if (centerPointsV.get(c).x == linesXV.get(i) && centerPointsV.get(c).y > maxY.y) {
                    maxY = centerPointsV.get(c);
                }
            }
            linesV.add(new Line(minY, maxY));
        }

        lines = new ArrayList<Line>();
        for (int i = 0; i < linesH.size(); i++) {
            lines.add(linesH.get(i));
        }
        for (int i = 0; i < linesV.size(); i++) {
            lines.add(linesV.get(i));
        }
        calculateRoutes();
        calculateReversedRoutes();
    }

    private void calculateReversedRoutes() {
        drugSpawnSpots = rts.getDrugSpawnSpots();
        for (int dspI = 0; dspI < drugSpawnSpots.length; dspI++) {
            BetterRoute shortestRoute = null;
            for (int i = 0; i < 1000; i++) {
                if (shortestRoute == null) {
                    shortestRoute = randomRoute(drugSpawnSpots[dspI].field, objStart);
                } else {
                    BetterRoute route = randomRoute(drugSpawnSpots[dspI].field, objStart);
                    if (route.getDistance() < shortestRoute.getDistance()) {
                        shortestRoute = route;
                    }
                }
            }
            dsp_start_group.add(shortestRoute);
        }

        waybackRoutes = new ArrayList<CompiledRoute>(compileRoutes(dsp_start_group));

    }

    private void calculateRoutes() {
        drugSpawnSpots = rts.getDrugSpawnSpots();
        for (int dspI = 0; dspI < drugSpawnSpots.length; dspI++) {
            BetterRoute shortestRoute = null;
            for (int i = 0; i < 1000; i++) {
                if (shortestRoute == null) {
                    shortestRoute = randomRoute(objStart, drugSpawnSpots[dspI].field);
                } else {
                    BetterRoute route = randomRoute(objStart, drugSpawnSpots[dspI].field);
                    if (route.getDistance() < shortestRoute.getDistance()) {
                        shortestRoute = route;
                    }
                }
            }
            start_dsp_group.add(shortestRoute);
        }

        awayRoutes = new ArrayList<CompiledRoute>(compileRoutes(start_dsp_group));
        waybackRoutes = new ArrayList<CompiledRoute>(awayRoutes);
        Collections.reverse(waybackRoutes);

    }

    private ArrayList<CompiledRoute> compileRoutes(ArrayList<BetterRoute> group) {
        ArrayList<CompiledRoute> arrayList = new ArrayList<CompiledRoute>();
        for (int index = 0; index < group.size(); index++) {
            arrayList.add(new CompiledRoute());
            for (int i = 0; i < group.get(index).getSize(); i++) {
                if (i == 0) {
                    arrayList.get(index).addPoint(group.get(index).get(i).p1);

                }
                if (group.get(index).get(i).getType() == Line.Type.vertical) {
                    arrayList.get(index).addPoint(group.get(index).get(i).p2);
                } else {
                    try {
                        arrayList.get(index).addPoint(group.get(index).get(i + 1).p1);
                    } catch (IndexOutOfBoundsException ex) {
                        Vector2 p = new Vector2();
                        p = drugSpawnSpots[index].getField().getCenter(p);
                        arrayList.get(index).addPoint(new Vector2(p.x, group.get(index).get(i).p1.y));
                    }

                }

            }
        }
        return arrayList;
    }

    private BetterRoute randomRoute(Rectangle start, Rectangle end) {
        Random random = new Random();
        //StartLine
        ArrayList<Line> linePool = new ArrayList<Line>(lines);
        Line startLine = null;
        BetterRoute route = new BetterRoute();
        for (int i = 0; i < linePool.size(); i++) {
            if (startLine == null) {
                startLine = lines.get(i);
            }
            if (lines.get(i).p1.y < startLine.p1.y) {
                startLine = lines.get(i);
            }
        }
        route.addLine(startLine);
        linePool.remove(startLine);

        //Endline
        Line endLine = null;
        for (int i = 0; i < linePool.size(); i++) {
            if (linePool.get(i).forJunction().overlaps(end)) {
                endLine = linePool.get(i);
            }
        }
        //Lines between
        ArrayList<Line> possibilities = new ArrayList<Line>();
        while (route.getLastLine() != endLine) {
            possibilities = new ArrayList<Line>();
            for (int i = 0; i < linePool.size(); i++) {
                Collections.shuffle(linePool);
                if (route.getLastLine().forJunction().overlaps(linePool.get(i).forJunction())) {
                    possibilities.add(linePool.get(i));
                    linePool.remove(i);
                }
            }
            if (possibilities.size() >= 1) {
                route.addLine(possibilities.get(0));
            } else if (possibilities.size() == 0) {
                Vector2 p2 = new Vector2();
                p2 = end.getCenter(p2);
                route.addLine(new Line(route.getLastLine().p2, p2));
                break;
            }
        }

        return route;
    }

    public ArrayList<Line> getLinesH() {
        return linesH;
    }

    public ArrayList<Line> getLinesV() {
        return linesV;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<BetterRoute> getDsp_start_group() {
        return dsp_start_group;
    }

    public ArrayList<BetterRoute> getStart_dsp_group() {
        return start_dsp_group;
    }

    public ArrayList<CompiledRoute> getAwayRoutes() {
        return awayRoutes;
    }

    public ArrayList<CompiledRoute> getWaybackRoutes() {
        return waybackRoutes;
    }
}
