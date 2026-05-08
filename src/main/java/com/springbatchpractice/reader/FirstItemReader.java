package com.springbatchpractice.reader;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FirstItemReader implements ItemReader<Integer> {

    List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    int i = 0;

    @Override
    public @Nullable Integer read() throws Exception {
        System.out.println("Inside Item Reader");
        Integer item;
        if(i < list.size()){
            item = list.get(i);
            i++;
            return item;
        }
        i=0;
        return null;
    }
}
