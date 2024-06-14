package ShipsInTunnel;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private int count;
    private int size;
    private int type;

    public Ship(int size, int type) {
        this.size = size;
        this.type = type;
    }

    public void add(int count) {

        this.count += count;
    }

    public boolean countCheck() {
        if (count >= size/*.getValue()*/) {
            return false;
        }
        return true;
    }

    public int getCount() {
        return count;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
 class Tunnel {
     private List<Ship> store;
     private int maxNumberOfShips = 5;
     private int minNumberOfShips = 0;
     private int counterShips = 0;

     public Tunnel() {
         store = new ArrayList<>();
     }

     public synchronized boolean add(Ship element) {
         try {
             if (counterShips < maxNumberOfShips) {
                 notifyAll();
                 store.add(element);
                 String info = String.format("%s + The ship arrived in the tunnel: %s %s %s",
                         store.size(), element.getType(), element.getSize(), Thread.currentThread().getName());
                 System.out.println(info);
                 counterShips++;
             } else {
                 System.out.println("There is no place for a ship in the tunnel:" + Thread.currentThread().getName());
                 wait();
                 return false;
             }
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return true;
     }

     public synchronized Ship get(int shipType) {
         try {
             if (counterShips > minNumberOfShips) {
                 notifyAll();
                 for (Ship ship : store) {
                     if (ship.getType() == shipType) {
                         counterShips--;
                         System.out.println(store.size() + "- The ship is taken from the tunnel: " + Thread.currentThread().getName());
                         store.remove(ship);
                         return ship;
                     }
                 }
             }
             System.out.println("0 < There are no ships in the tunnel");
             wait();

         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return null;
     }
 }


