package com.company.multithreadings;

import java.util.concurrent.Semaphore;

//clasa ce actioneaza ca un buffer sincronizat
//se folosesc doua semafoare pentru aceasta sincronizare: full si empty
//full -> se poate accesa in momentul in care bufferul este plin
//empty -> se poate accesa in momentul in care bufferul este gol

public class BufferImage implements BufferInt<byte[]> {
    private byte[] currentImageBuffer;
    private final Semaphore full;
    private final Semaphore empty;

    public BufferImage() {
        full = new Semaphore(0);
        empty = new Semaphore(1);
    }

    //se introduce in buffer valorea array-ului de tip byte
    //operatie permisa in momentul in care semaforul ce indica ca bufferul este gol este activ

    @Override
    public void insertIntoBuffer(byte[] value) {
        try {
            empty.acquire();
            currentImageBuffer = value;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        full.release();
    }

    //se extrage din buffer valorea array-ului de tip byte
    //operatie permisa in momentul in care semaforul ce indica ca bufferul este plin este activ

    @Override
    public byte[] consumeFromBuffer() {
        byte[] consumedValue = new byte[1];
        try {
            full.acquire();
            consumedValue = currentImageBuffer;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        empty.release();
        return consumedValue;
    }
}
