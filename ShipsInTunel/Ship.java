package ShipsInTunel;

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
