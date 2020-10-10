package ru.otus.spring.actuator.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
abstract public class BaseUniquenessHealthCheckTest<T> {
    private static final String INITIAL_GENRE_NAME = "horror";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongo;

    private final T item = testItem();

    @DisplayName(" отдавать успешный статус работы при отсутствии дубликатов")
    @Test
    public void noDuplicates() throws Exception {
        assertItemsStored(1);

        testHealthCheck(HttpStatus.OK, serviceUpContent());
    }

    @DisplayName(" отдавать неуспешный статус работы при наличии дубликатов")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void duplicatesFound() throws Exception {
        mongo.insert(item);

        assertItemsStored(2);

        testHealthCheck(HttpStatus.SERVICE_UNAVAILABLE, serviceDownContent());
    }

    private void testHealthCheck(HttpStatus expectedStatus, String expectedContent) throws Exception {
        mvc
            .perform(get("/actuator/health"))
            .andExpect(status().is(expectedStatus.value()))
            .andExpect(content().string(containsString(expectedContent)))
        ;
    }

    private void assertItemsStored(int size) {
        var items = mongo.findAll(item.getClass());

        assertThat(items).hasSize(size);
        assertThat(items)
            .allSatisfy(item -> assertThat(checkItem((T) item)).isTrue());
    }

    abstract protected String serviceUpContent();

    abstract protected String serviceDownContent();

    abstract protected boolean checkItem(T item);

    abstract protected T testItem();
}
