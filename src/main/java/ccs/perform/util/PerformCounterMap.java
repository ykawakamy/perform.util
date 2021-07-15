package ccs.perform.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerformCounterMap {

    Map<String, SequencialPerformCounter> pcMap = new ConcurrentHashMap<>();

    public PerformCounterMap() {
    }

    public void perform(String key, Integer actual) {
        SequencialPerformCounter pc = pcMap.computeIfAbsent(key, (k)->new SequencialPerformCounter());
        pc.perform(actual);
    }

    public void addLatency(String key, long n) {
        SequencialPerformCounter pc = pcMap.computeIfAbsent(key, (k)->new SequencialPerformCounter());
        pc.addLatency(n);
    }

    public PerformSnapshot reset() {
        PerformSnapshot snap = new PerformSnapshot();
        for( var it : pcMap.values() ) {
            var p = it.reset();
            snap.err += p.err;
            snap.perform += p.perform;
            snap.latency += p.latency;
        }

        return snap;

    }
}