package ShipsInTunnelNew;

import java.util.concurrent.*;
import java.util.Random;

enum CargoType {
    BREAD, BANANA, CLOTHES
}

class Ship implements Runnable {
    private final CargoType cargoType;
    private final int capacity;
    private final Semaphore tunnelSemaphore;
    private final BlockingQueue<Ship> breadDock;
    private final BlockingQueue<Ship> bananaDock;
    private final BlockingQueue<Ship> clothesDock;

    public Ship(CargoType cargoType, int capacity, Semaphore tunnelSemaphore,
                BlockingQueue<Ship> breadDock, BlockingQueue<Ship> bananaDock, BlockingQueue<Ship> clothesDock) {
        this.cargoType = cargoType;
        this.capacity = capacity;
        this.tunnelSemaphore = tunnelSemaphore;
        this.breadDock = breadDock;
        this.bananaDock = bananaDock;
        this.clothesDock = clothesDock;
    }

    @Override
    public void run() {
        try {
            tunnelSemaphore.acquire();
            System.out.println("Ship with cargo " + cargoType + " and capacity " + capacity + " is entering the tunnel.");
            Thread.sleep(5000);
            System.out.println("Ship with cargo " + cargoType + " and capacity " + capacity + " has exited the tunnel.");
            tunnelSemaphore.release();

            switch (cargoType) {
                case BREAD:
                    breadDock.put(this);
                case BANANA:
                    bananaDock.put(this);
                case CLOTHES:
                    clothesDock.put(this);

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public int getCapacity() {
        return capacity;
    }
}

class Dock implements Runnable {
    private final CargoType cargoType;
    private final BlockingQueue<Ship> dockQueue;

    public Dock(CargoType cargoType, BlockingQueue<Ship> dockQueue) {
        this.cargoType = cargoType;
        this.dockQueue = dockQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Ship ship = dockQueue.take();
                System.out.println("Loading ship with cargo  " + cargoType + " and capacity " + ship.getCapacity() + " at dock.");
                Thread.sleep(ship.getCapacity() / 10 * 1000);
                System.out.println("Ship with cargo  " + cargoType + " and capacity " + ship.getCapacity() + " has been loaded.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

class VirtualPort {
    private static final int MAX_TUNNEL_CAPACITY = 5;
    private static final Semaphore tunnelSemaphore = new Semaphore(MAX_TUNNEL_CAPACITY);
    private static final BlockingQueue<Ship> breadDock = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Ship> bananaDock = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Ship> clothesDock = new LinkedBlockingQueue<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        executor.submit(new Dock(CargoType.BREAD, breadDock));
        executor.submit(new Dock(CargoType.BANANA, bananaDock));
        executor.submit(new Dock(CargoType.CLOTHES, clothesDock));

        generateShips();
    }

    private static void generateShips() {
        Random random = new Random();
        CargoType[] cargoTypes = CargoType.values();
        int[] capacities = {10, 50, 100 };

        while (true) {
            CargoType cargoType = cargoTypes[random.nextInt(cargoTypes.length)];
            int capacity = capacities[random.nextInt(capacities.length)];
            Ship ship = new Ship(cargoType, capacity, tunnelSemaphore, breadDock, bananaDock, clothesDock);
            executor.submit(ship);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}


