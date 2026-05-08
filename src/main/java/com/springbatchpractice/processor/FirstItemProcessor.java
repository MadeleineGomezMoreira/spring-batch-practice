package com.springbatchpractice.processor;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

//Input data type for the processor depends on what the read method from the ItemReader returns
@Component
public class FirstItemProcessor implements ItemProcessor<Integer,Long> {

    @Override
    public @Nullable Long process(Integer item) throws Exception {
        System.out.println("Inside Item Processor");
        return (long) (item + 20);
    }
}
