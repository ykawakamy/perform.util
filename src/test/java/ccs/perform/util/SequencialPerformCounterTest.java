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

        Assertions.assertEquals(0, pc.getErr());
    }

    @Test
    void testSame() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(0);
        pc.perform(0);
        pc.perform(0);

        Assertions.assertEquals(0, pc.getErr());
    }

    @Test
    void testSkipOk() {
        SequencialPerformCounter pc = new SequencialPerformCounter();

        pc.perform(1);
        pc.perform(1);
        pc.perform(1);

        Assertions.assertEquals(0, pc.getErr());
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
    }

}
