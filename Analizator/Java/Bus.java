public class Bus {

    int left, right, top, bottom, surface, lastFrame;
    int direction;      // -1 from L to R, 0 - standing (started value), 1 - from R to L
    point center;
    double vector;
    int life_time;
    //Construktor klasy bus
    public Bus(int left, int top, int right, int bottom, int life_time, int direction) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.life_time = life_time;
        this.direction = direction;
        this.surface = (right - left)*(bottom - top);
        this.lastFrame = life_time;
        center = center();
        vector = vector();

    }
    // Nadaje środek bounding box'a busa
    public final point center() {
        //System.out.println("Środek x=" + (right + left) / 2 + " y=" + (bottom + top) / 2);
        return new point((right + left) / 2, (bottom + top) / 2);

    }
    // Nadaje połowa przekątnej bounding bus'a
    public final double vector() {
        return Math.sqrt((double) (right - center.x) * (right - center.x) + (top - center.y) * (top - center.y));
    }
}