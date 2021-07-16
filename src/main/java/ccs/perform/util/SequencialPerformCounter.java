package ccs.perform.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SequencialPerformCounter {
    private AtomicInteger seq = new AtomicInteger();
    private AtomicInteger noupdate = new AtomicInteger();
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
    
    /** use {@link PerformSnapshot#getErr()} instead. */
    @Deprecated
    public int getErr() {
        return rewindError.get();
    }

    /** use {@link PerformSnapshot#getPerform()} instead. */
    @Deprecated
    public int getPerform() {
        return perform.get();
    }

    /** use {@link SequencialPerformCounter#reset()} instead. */
    @Deprecated
    public int retrievePerform() {
        int v = perform.getAndSet(0);
        rewindError.set(0);
        return v;
    }

    public void perform(int actual) {
        AtomicBoolean isOk = new AtomicBoolean(false);
        AtomicBoolean isNoUpdate = new AtomicBoolean(false);
        int nextExpected = seq.getAndUpdate( (expected)->{ 
        	isOk.set(actual == expected);
        	isNoUpdate.set(actual == expected-1);
            return actual+1;
        });
        if( !isOk.get() ) {
        	if( isNoUpdate.get() ) {
        		noupdate.getAndIncrement();
        	}else if( actual < nextExpected-1 ) {
	            rewindError.getAndIncrement();
	        }else {
	        	skipError.getAndIncrement();
	        }
        }
        perform.getAndIncrement();

        actualMin.getAndAccumulate(actual, Math::min);
        actualMax.getAndAccumulate(actual, Math::max);
    }

    public void addLatency(long n) {
        latency.addAndGet(n);
    }

    public PerformSnapshot reset() {
        PerformSnapshot snap = new PerformSnapshot();
        snap.noupdate = noupdate.getAndSet(0);
        snap.rewindError = rewindError.getAndSet(0);
        snap.skipError = skipError.getAndSet(0);
        snap.actualMin = actualMin.getAndSet(Integer.MAX_VALUE);
        snap.actualMax = actualMax.getAndSet(Integer.MIN_VALUE);
        snap.perform = perform.getAndSet(0);
        snap.latency = latency.getAndSet(0);
        return snap;

    }
}