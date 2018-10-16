package club.greenstudio.minnimafia.rts;

import java.util.ArrayList;

public class BetterRoute {

    private ArrayList<Line> lines;

    public BetterRoute() {
        lines = new ArrayList<Line>();
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public int getSize() {
        return lines.size();
    }

    public int getDistance() {
        int distance = 0;
        for (int i = 0; i < lines.size() - 1; i++) {
            distance += Math.sqrt(Math.pow(lines.get(i).p2.x - lines.get(i + 1).p2.x, 2) + Math.pow(lines.get(i).p2.y - lines.get(i + 1).p2.y, 2));
        }
        return distance;
    }

    public Line getLastLine() {
        return lines.get(lines.size() - 1);
    }

    public Line get(int index){
        return lines.get(index);
    }
}
