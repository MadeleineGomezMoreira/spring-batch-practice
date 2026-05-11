package com.springbatchpractice.writer;

import com.springbatchpractice.model.StudentJson;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class JsonItemWriter implements ItemWriter<StudentJson> {

    //Chunk contains a list of items. The size of the list in the method arguments depends on the chunk size
    @Override
    public void write(Chunk<? extends StudentJson> chunk) {
        System.out.println("Inside Item Writer");
        chunk.getItems().forEach(System.out::println);
    }
}
