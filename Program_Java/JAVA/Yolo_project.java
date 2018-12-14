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

    int delta = 50;  //odległość miedzy środkami 
    int bus_lifetime = 50; // czas życia busa 
    ArrayList<Bus> c_buses = new ArrayList<>(); //Busy w pamięci
    ArrayList<Bus> buses = new ArrayList<>();   //Busy które przejechały (zliczone)
    ArrayList<Bus> r_buses = new ArrayList<>(); // Busy do usunięcia 
    boolean flag = false;

    public static void main(String[] args) {
        Yolo_project yolo = new Yolo_project();
        yolo.input();
        int i = 0;
        for (Bus b : yolo.buses) {
            i++;
            System.out.println("Bus "+i);
        }
    }

    // Metoda zwraca wynik czy dany bus_new jest tym samym busem
    public boolean compareBus(Bus b_new, Bus b) {
        int x = (b_new.center.x - b.center.x) * (b_new.center.x - b.center.x);
        int y = (b_new.center.y - b.center.y) * (b_new.center.y - b.center.y);
        // System.out.println("Odległość od środka " + Math.sqrt((double) (x + y)));
        return Math.sqrt((double) (x + y)) < delta;
    }

    public void newBus(Bus b_new) {
        for (Bus b : c_buses) {
            // Czy to jest ten sam bus ? 
            if (compareBus(b_new, b)) {
                b = b_new;              //aktualizacja danych o busie
                System.out.println("Ten sam bus");
                return;
            }
             // Jeśli bus w pamięci był ostatnio aktualizoawny później niż bus_lifetime
            if (b_new.life_time - b.life_time > bus_lifetime) {
                buses.add(b);
                r_buses.add(b);
                System.out.println("Będę usuwał");
                // continue;
            }
        }
        c_buses.removeAll(r_buses);
        r_buses = new ArrayList<>();
       // System.out.println("dodaję busa " + b_new.life_time);
        c_buses.add(b_new);             //if not adding new bus

    }

    //Parser danych z pliku 
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
                        //Tworzy busa 
                        Bus b = new Bus(Integer.parseInt(str[0]), Integer.parseInt(str[1]),
                                Integer.parseInt(str[2]), Integer.parseInt(str[3]), line_counter);
                        // Jeśli nie ma obecnie w pamieci żadnych busów dodaje busa 
                        if (c_buses.isEmpty()) {
                            c_buses.add(b);
                            // System.out.println("dodaję busa ");

                        } else {
                            // W przypadku gdy są jakieś busy sprawdza czy to nie jest ten sam metodą newBus
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
