package ccs.perform.util;

import org.slf4j.Logger;

public class PerformSnapshot {
    int seq;
    int noupdate;
    int rewindError;
    int skipError;
    int actualMin = Integer.MAX_VALUE;
    int actualMax = Integer.MIN_VALUE;
    int perform;
    long latency;

    public int getSeq() {
        return seq;
    }
    public int getErr() {
        return rewindError + skipError + noupdate;
    }
    public int getPerform() {
        return perform;
    }
    public long getLatency() {
        return latency;
    }

    public float getElapsedPerOperation(long elapsedTime ) {
        return (float)elapsedTime/getPerform();

    }

    public float getOperationPerMillis(long elapsedTimeNs ) {
        return (float)getPerform() * 1_000_000 / elapsedTimeNs;

    }

    public float getLatencyPerOperation() {
        return (float)getLatency()/getPerform();
    }

    public void print(Logger log, long elapsedNanoSec) {
		log.info("range: {} - {} elapsed: {} ns. process: {} op. {} ns/op. ( {} op/ms ) latency: {} ms/op"
				+ " | errors - total: {} ( noupdate: {} rewind: {} skip: {} )", 
				actualMin, 
				actualMax,
				elapsedNanoSec, 
				this.getPerform(), 
				this.getElapsedPerOperation(elapsedNanoSec),
				this.getOperationPerMillis(elapsedNanoSec),
				this.getLatencyPerOperation(),
				//-- 
				this.getErr(), 
				this.noupdate,
				this.rewindError,
				this.skipError
				);
	}
	public void add(PerformSnapshot p) {
        this.perform += p.perform;
        this.latency += p.latency;

        this.noupdate += p.noupdate;
        this.rewindError += p.rewindError;
        this.skipError += p.skipError;

        this.actualMin = Math.max(this.actualMin, p.actualMin);
        this.actualMax =  Math.max(this.actualMax, p.actualMax);
	}
}
