package com.ontheserverside.batch.bank.processing;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ModuloPartitionerTest {

    private ModuloPartitioner partitioner;

    @Before
    public void setUp() {
        this.partitioner = new ModuloPartitioner();
    }

    @Test
    public void shouldProduceNoContextspForZeroSizeGrid() {
        Map<String, ExecutionContext> map = partitioner.partition(0);

        assertThat(map, is(not(nullValue())));
        assertThat(map.isEmpty(), is(true));
    }

    @Test
    public void shouldProduceContextsForNonEmptyGrid() {
        Map<String, ExecutionContext> map = partitioner.partition(3);

        assertThat(map, is(not(nullValue())));
        assertThat(map.size(), is(3));

        ExecutionContext context0 = map.get("partition-0");
        ExecutionContext context1 = map.get("partition-1");
        ExecutionContext context2 = map.get("partition-2");

        assertThat(context0, is(not(nullValue())));
        assertThat(context1, is(not(nullValue())));
        assertThat(context2, is(not(nullValue())));

        assertThat((int) context0.get(ModuloPartitioner.MOD_REMAINDER_KEY), is(0));
        assertThat((int) context1.get(ModuloPartitioner.MOD_REMAINDER_KEY), is(1));
        assertThat((int) context2.get(ModuloPartitioner.MOD_REMAINDER_KEY), is(2));

        assertThat((int) context0.get(ModuloPartitioner.MOD_DIVISOR_KEY), is(3));
        assertThat((int) context1.get(ModuloPartitioner.MOD_DIVISOR_KEY), is(3));
        assertThat((int) context2.get(ModuloPartitioner.MOD_DIVISOR_KEY), is(3));
    }
}
