package yolo_project;

/**
 *
 * @author jakub
 */
public class Bus {

    int left, right, top, bottom;
    point center;
    double vector;
    int line;

    public Bus(int left, int top, int right, int bottom,int line) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.line = line;
        center = center();
        vector = vector();

    }

    public final point center() {
         System.out.println("Środek x="+(right + left)/2+" y="+ (bottom + top) / 2);
        return new point((right + left) / 2, (bottom + top) / 2);
       
    }

    public final double vector() {
        return Math.sqrt((double) (right - center.x) * (right - center.x) + (top - center.y) * (top - center.y));
    }
}
