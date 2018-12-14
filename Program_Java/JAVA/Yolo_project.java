package yolo_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Jakub Duda
 *
 */
public class Yolo_project {

    int current, counter;
    int delta = 50, but_lifetime = 80;
    ArrayList<Bus> c_buses = new ArrayList<>();
    ArrayList<Bus> buses = new ArrayList<>();
    ArrayList<Bus> r_buses = new ArrayList<>();
    boolean flag = false;

    public static void main(String[] args) {
        Yolo_project yolo = new Yolo_project();
        yolo.input();
        int i = 0;
        for (Bus b : yolo.buses) {
            i++;
            System.out.println("Hej zliczyłem skurczysyna " + i);
            System.out.println("Bus ");
        }
    }

    public boolean compareBus(Bus b_new, Bus b) {
        int x = (b_new.center.x - b.center.x) * (b_new.center.x - b.center.x);
        int y = (b_new.center.y - b.center.y) * (b_new.center.y - b.center.y);
        System.out.println("Odległość od środka " + Math.sqrt((double) (x + y)));
        return Math.sqrt((double) (x + y)) < delta;
    }

    public void newBus(Bus b_new) {
        for (Bus b : c_buses) {
            if (b_new.line - b.line > but_lifetime) {
                buses.add(b);
                r_buses.add(b);
                System.out.println("Będę usówał");
                // continue;
            } else if (compareBus(b_new, b)) {
                b = b_new;              //b_new is the same bus
                System.out.println("Ten sam bus");
                c_buses.removeAll(r_buses);
                c_buses.add(b_new);  
                return;
            }
        }
        c_buses.removeAll(r_buses);
        System.out.println("dodaję busa " + b_new.line);
        c_buses.add(b_new);             //if not adding new bus

    }

    public void input() {
        File file = new File("/home/jakub/darknet/mpk");
        try {
            int line_counter = 0;
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String i = sc.nextLine();
                    line_counter++;
                    if (flag && !i.contains("%")) {
                        System.out.println(i + " " + line_counter);
                        String[] str = i.split(" ");
                        Bus b = new Bus(Integer.parseInt(str[0]), Integer.parseInt(str[1]),
                                Integer.parseInt(str[2]), Integer.parseInt(str[3]), line_counter);
                        if (c_buses.isEmpty()) {
                            c_buses.add(b);
                            System.out.println("dodaję busa ");
                        } else {
                            newBus(b);
                        }
                        flag = false;
                    }
                    if (i.contains("bus")) {
                        System.out.println(i);
                        flag = true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
        }
    }
}
