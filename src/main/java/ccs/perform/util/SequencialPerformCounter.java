package ccs.perform.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SequencialPerformCounter {
    private AtomicInteger seq = new AtomicInteger();
    private AtomicInteger skipError = new AtomicInteger();
    private AtomicInteger rewindError = new AtomicInteger();
    private AtomicInteger perform = new AtomicInteger();
    private AtomicLong latency = new AtomicLong();

    private AtomicInteger actualMin = new AtomicInteger(Integer.MAX_VALUE);
    private AtomicInteger actualMax = new AtomicInteger(Integer.MIN_VALUE);

    public SequencialPerformCounter() {
    }

    public int getSeq() {
        return seq.get();
    }

    public int getErr() {
        return rewindError.get();
    }

    public int getPerform() {
        return perform.get();
    }

    public int retrievePerform() {
        int v = perform.getAndSet(0);
        rewindError.set(0);
        return v;
    }

    public void perform(int actual) {
        AtomicBoolean isOk = new AtomicBoolean(false);
        int expected = seq.getAndUpdate( (p)->{ boolean is = actual == p;
            isOk.set(is);
            return is ? actual+1 : actual+1;
        });
        actualMin.getAndAccumulate(actual, Math::min);
        if( actual < expected-1 ) {
            rewindError.getAndIncrement();
        }
        perform.getAndIncrement();
    }

    public void addLatency(long n) {
        latency.addAndGet(n);
    }

    public PerformSnapshot reset() {
        PerformSnapshot snap = new PerformSnapshot();
        snap.err = rewindError.getAndSet(0);
        snap.perform = perform.getAndSet(0);
        snap.latency = latency.getAndSet(0);
        return snap;

    }
}