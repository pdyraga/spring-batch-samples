package com.ontheserverside.batch.bank.processing;

import org.springframework.batch.item.*;

public final class SynchronizedItemReaderDecorator<T> implements ItemStream, ItemReader<T> {

    private final ItemReader<T> delegate;

    public SynchronizedItemReaderDecorator(ItemReader<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public synchronized T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return delegate.read();
    }

    @Override
    public synchronized void open(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream) delegate).open(executionContext);
        }
    }

    @Override
    public synchronized void update(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream) delegate).update(executionContext);
        }
    }

    @Override
    public synchronized void close() throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream) delegate).close();
        }
    }
}
