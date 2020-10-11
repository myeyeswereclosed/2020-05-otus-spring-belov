package ru.otus.spring.app_container.repository.author;

import com.mongodb.Block;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.app_container.domain.Author;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.computed;

@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {
    private final MongoTemplate mongo;

    @Override
    public Optional<Author> update(Author author) {
        return
            Optional.ofNullable(
                mongo.findAndModify(
                    new Query(Criteria.where("id").is(author.getId())),
                    new Update()
                        .set("firstName", author.getFirstName())
                        .set("lastName", author.getLastName()),
                    Author.class
                )
            );
    }

    @Override
    public Optional<String> delete(String id) {
        return
            Optional.ofNullable(
                mongo.findAndRemove(
                    new Query(Criteria.where("id").is(id)),
                    Author.class
                )
            ).map(Author::getId);
    }

    @Override
    public List<Author> findDuplicates() {
        var result = new ArrayList<Document>();

        mongo
            .getCollection(mongo.getCollectionName(Author.class))
            .aggregate(
                Arrays.asList(
                    group(
                        and(eq("firstName", "$firstName"), eq("lastName", "$lastName")),
                        sum("count", 1)
                    ),
                    match(gt("count", 1)),
                    project(computed("_id", "$_id"))
                )
            ).forEach((Block<? super Document>) result::add)
        ;

        return
            result
                .stream()
                .map(document -> {
                    var authorDocument = (Document)document.get("_id");

                    return new Author((String)authorDocument.get("firstName"), (String)authorDocument.get("lastName"));
                })
                .collect(Collectors.toList())
            ;
    }
}
