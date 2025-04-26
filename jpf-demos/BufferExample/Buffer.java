class Buffer implements BufferInterface {

    protected static final int SIZE = 3;
    protected Object[] array = new Object[SIZE];

    protected int putPtr = 0;
    protected int getPtr = 0;
    protected int usedSlots = 0;
    protected boolean halted = false;

    private final Object lockPut = new Object();
    private final Object lockGet = new Object();

    public void put(Object x) {
        synchronized (lockPut) {
            synchronized (lockGet) {
                while (usedSlots == SIZE) {
                    try {
                        lockPut.wait();
                    } catch (InterruptedException e) {}
                }

                array[putPtr] = x;
                putPtr = (putPtr + 1) % SIZE;
                usedSlots++;

                lockGet.notifyAll(); // wake up getters
            }
        }
    }

    public Object get() throws HaltException {
        synchronized (lockGet) {
            synchronized (lockPut) {
                while (usedSlots == 0 && !halted) {
                    try {
                        lockGet.wait();
                    } catch (InterruptedException e) {}
                }

                if (usedSlots == 0) {
                    throw new HaltException();
                }

                Object x = array[getPtr];
                array[getPtr] = null;
                getPtr = (getPtr + 1) % SIZE;
                usedSlots--;

                lockPut.notifyAll(); // wake up putters
                return x;
            }
        }
    }

    public synchronized void halt() {
        halted = true;
        notifyAll();
    }
}

interface BufferInterface {
    void put(Object x);
    Object get() throws HaltException;
    void halt();
}



class Consumer extends Thread {
    private final Buffer buffer;

    public Consumer(Buffer b) {
        this.buffer = b;
    }

    public void run() {
        int count = 0;
        AttrData[] received = new AttrData[10];

        try {
            while (count < 10) {
                received[count] = (AttrData) buffer.get();
                count++;
            }
        } catch (HaltException e) {}

        // Optional asserts (good if you want to catch wrong behavior)
        assert count == 6 : "Expected 6 items, got " + count;
        for (int i = 0; i < count; i++) {
            assert received[i].attr == i : "Attribute mismatch at index " + i;
        }
    }
}


class Producer extends Thread {
    private final Buffer buffer;

    public Producer(Buffer b) {
        this.buffer = b;
    }

    public void run() {
        for (int i = 0; i < 6; i++) {
            AttrData ad = new AttrData(i, i * i);
            buffer.put(ad);
            yield(); // give Consumer chance
        }
        buffer.halt();
    }
}


class Attribute{
    public int attr;
    public Attribute(int attr){
        this.attr = attr;
    }
}

class AttrData extends Attribute{
    public int data;
    public AttrData(int attr,int data){
        super(attr);
        this.data = data;
    }
}

class HaltException extends Exception{}


class Main {
    public static void main(String[] args) {
        Buffer b = new Buffer();
        Producer p = new Producer(b);
        Consumer c = new Consumer(b);

        p.start();
        c.start();
    }
}
