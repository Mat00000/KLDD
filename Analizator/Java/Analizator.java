import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Jakub Duda, Mateusz Laskowski, Pjoter, Mikis
 *
 */
public class Analizator {

    int delta = 36;  //odległość miedzy środkami
    int bus_lifetime = 80; // czas życia busa
    ArrayList<Bus> c_buses = new ArrayList<>(); //Busy w pamięci/ pamięta tylko poprzednią klatke
    ArrayList<Bus> buses = new ArrayList<>();   //Busy które przejechały (zliczone)
    ArrayList<Bus> r_buses = new ArrayList<>(); // Busy do usunięcia
    boolean flag = false;

    public static void main(String[] args) throws IOException {
        Analizator yolo = new Analizator();
        yolo.input();
        int i = 0;
        String temp;
        BufferedWriter writer = new BufferedWriter(new FileWriter("outputAnalizator.txt"));
        for (Bus b : yolo.buses) {
            i++;
            //System.out.println("Bus "+i);
            System.out.println("LT "+ b.life_time);
            temp = "LT " + b.life_time + "\n";
            writer.write(temp);
            switch(b.direction) {
                case -1:    System.out.println("Direction: L -> R");
                            temp = "Direction: L -> R\n";
                    break;
                case 0:     System.out.println("Direction: S");
                            temp = "Direction: S\n";
                    break;
                case 1:     System.out.println("Direction: R -> L");
                            temp = "Direction: R -> L\n";
                    break;
                default:    System.out.println("Direction: undefinded");
                            temp = "Direction: undefinded\n";
                    break;
            }
            writer.write(temp);
        }
        System.out.println("Bus "+i);
        temp = "Bus " + i;
        writer.write(temp);

        writer.close();
    }

    // Metoda zwraca wynik czy dany bus_new jest tym samym busem
    public boolean compareBus(Bus b_new, Bus b) {
        int x = (b_new.center.x - b.center.x) * (b_new.center.x - b.center.x);
        int y = (b_new.center.y - b.center.y) * (b_new.center.y - b.center.y);
        double d = Math.sqrt((x + y));
        //System.out.println("Odległość miedzy busami" + d);
        return (d < delta);
    }

    public boolean overlapFileter(Bus b_new, Bus b){//true gdy się nakłada
        //sprawdzenie przypadku nakładania sie obiektów
        if(b.life_time==b_new.life_time){
            //b to ten wiekszy
            if((b.top<=b_new.top) && (b.bottom>=b_new.bottom) && (b.left<=b_new.left)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public void copyBus(Bus b_new, Bus b){
        b.center = b_new.center;
        b.life_time = b_new.life_time;
        b.right = b_new.right;
        b.top = b_new.top;
        b.bottom = b_new.bottom;
        b.left = b_new.left;
        b.vector = b_new.vector;
        b.direction = b_new.direction;
        b.surface = b_new.surface;
        b.lastFrame = b_new.lastFrame;
    }

    public int checkDirection(Bus b_new, Bus b) {
        // System.out.println("OLD " + b.vector);
        // System.out.println("NEW " + b_new.vector);
        // if(b.direction != 0) return b.direction;    // jeżeli miał już kierunek to uznaje że nie zawróci
        
        if(b.vector == b_new.vector) return 0;
        else if(b.vector < b_new.vector) return -1;
        else return 1;
    }
    //wywołanie z numerem klatki
    //dla przypadków skrajnych dobrać algorytm
    //w analizie problemu dać pseudokod
    //uszczegółowić przypadki urzycia o algorytm
    //nagrać własny film
    public void newBus(Bus b_new) {
        for (Bus b : c_buses) {
            // Czy to jest ten sam bus ?
            if(overlapFileter(b_new,b)){
                if(b.surface<b_new.surface){
                    copyBus(b_new,b);
                }
            }
            else if (compareBus(b_new, b)) {
                b_new.direction = checkDirection(b_new, b);
                copyBus(b_new,b);
                //b = b_new;              //aktualizacja danych o busie
                //System.out.println("Ten sam bus");
                return;
            }
            // Jeśli bus w pamięci był ostatnio aktualizoawny później niż bus_lifetime
            if (b_new.life_time - b.life_time > bus_lifetime) {
                buses.add(b);
                r_buses.add(b);
                //System.out.println("Będę usuwał");
                // continue;
            }
        }
        c_buses.removeAll(r_buses);
        r_buses = new ArrayList<>();
        // System.out.println("dodaję busa " + b_new.life_time);
        //c_buses.add(b_new);             //if not adding new bus

    }

    //Parser danych z pliku
    public void input() {
        File file = new File("mpk_output");///home/jakub/darknet/mpk
        try {
            int line_counter = 0;//licznik klatek
            int undefinded_direction = 0;   // 0 - standing
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String i = sc.nextLine();
                    if(i.contains("FPS")){
                        line_counter++;
                    }
                    if (flag && !i.contains("%")) {
                        //System.out.println(line_counter);
                        //System.out.println(i + " " + line_counter);
                        String[] str = i.split(" ");
                        //Tworzy busa
                        Bus b = new Bus(Integer.parseInt(str[0]), Integer.parseInt(str[1]),
                                Integer.parseInt(str[2]), Integer.parseInt(str[3]), line_counter, undefinded_direction);
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
                        //System.out.println(i);
                        flag = true;
                    }
                }
                for (Bus b : c_buses) {
                    buses.add(b);
                }
            }
        } catch (FileNotFoundException e) {
        }
    }
}