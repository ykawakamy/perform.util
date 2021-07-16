package ccs.perform.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SequencialPerformCounterTest {

    @Test
    void test() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(0);
        pc.perform(1);
        pc.perform(2);

        PerformSnapshot snapshot = pc.reset();
        Assertions.assertEquals(3, pc.getSeq());
        Assertions.assertEquals(0, snapshot.getErr());
        Assertions.assertEquals(0, snapshot.noupdate);
        Assertions.assertEquals(0, snapshot.skipError);
        Assertions.assertEquals(0, snapshot.rewindError);
    }

    @Test
    void testSame() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(0);
        pc.perform(0);
        pc.perform(0);

        PerformSnapshot snapshot = pc.reset();
        Assertions.assertEquals(1, pc.getSeq());
        Assertions.assertEquals(2, snapshot.getErr());
        Assertions.assertEquals(2, snapshot.noupdate);
        Assertions.assertEquals(0, snapshot.skipError);
        Assertions.assertEquals(0, snapshot.rewindError);
    }

    @Test
    void testSkipOk() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(1);
        pc.perform(1);
        pc.perform(1);

        PerformSnapshot snapshot = pc.reset();
        Assertions.assertEquals(2, pc.getSeq());
        Assertions.assertEquals(3, snapshot.getErr());
        Assertions.assertEquals(2, snapshot.noupdate);
        Assertions.assertEquals(1, snapshot.skipError);
        Assertions.assertEquals(0, snapshot.rewindError);
    }

    @Test
    void testErr2() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(2);
        Assertions.assertEquals(0, pc.getErr());
        pc.perform(1);
        Assertions.assertEquals(1, pc.getErr());
        pc.perform(0);
        Assertions.assertEquals(2, pc.getErr());

        PerformSnapshot snapshot = pc.reset();
        Assertions.assertEquals(1, pc.getSeq());
        Assertions.assertEquals(3, snapshot.getErr());
        Assertions.assertEquals(0, snapshot.noupdate);
        Assertions.assertEquals(1, snapshot.skipError);
        Assertions.assertEquals(2, snapshot.rewindError);
}

    @Test
    void testErr3() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(2);
        Assertions.assertEquals(0, pc.getErr());
        pc.perform(2);
        Assertions.assertEquals(0, pc.getErr());
        pc.perform(0);
        Assertions.assertEquals(1, pc.getErr());
        PerformSnapshot snapshot = pc.reset();

        Assertions.assertEquals(1, pc.getSeq());
        Assertions.assertEquals(3, snapshot.getErr());
        Assertions.assertEquals(1, snapshot.noupdate);
        Assertions.assertEquals(1, snapshot.skipError);
        Assertions.assertEquals(1, snapshot.rewindError);

    }

    
    @Test
    void testErr4() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(2);
        Assertions.assertEquals(0, pc.getErr());
        pc.perform(0);
        Assertions.assertEquals(1, pc.getErr());
        pc.perform(1);
        Assertions.assertEquals(1, pc.getErr());
        PerformSnapshot snapshot = pc.reset();

        Assertions.assertEquals(2, pc.getSeq());
        Assertions.assertEquals(2, snapshot.getErr());
        Assertions.assertEquals(0, snapshot.noupdate);
        Assertions.assertEquals(1, snapshot.skipError);
        Assertions.assertEquals(1, snapshot.rewindError);

    }

    @Test
    void testErr5() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(2);
        Assertions.assertEquals(0, pc.getErr());
        pc.perform(0);
        Assertions.assertEquals(1, pc.getErr());
        pc.perform(2);
        Assertions.assertEquals(1, pc.getErr());
        PerformSnapshot snapshot = pc.reset();

        Assertions.assertEquals(3, pc.getSeq());
        Assertions.assertEquals(3, snapshot.getErr());
        Assertions.assertEquals(0, snapshot.noupdate);
        Assertions.assertEquals(2, snapshot.skipError);
        Assertions.assertEquals(1, snapshot.rewindError);

    }

}
