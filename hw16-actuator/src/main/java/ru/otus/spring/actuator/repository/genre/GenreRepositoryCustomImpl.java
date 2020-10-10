package ru.otus.spring.actuator.repository.genre;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.actuator.domain.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Projections.computed;

@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Genre> update(Genre genre) {
        return
            Optional.ofNullable(
                mongoTemplate.findAndModify(
                    new Query(Criteria.where("id").is(genre.getId())),
                    new Update().set("name", genre.getName()),
                    Genre.class
                )
            );
    }

    @Override
    public Optional<String> delete(String id) {
        return
            Optional.ofNullable(
                mongoTemplate
                    .findAndRemove(
                        new Query(Criteria.where("id").is(id)),
                        Genre.class
                    )
            ).map(Genre::getId);
    }

    @Override
    public List<Genre> findDuplicates() {
        var result = new ArrayList<Document>();

        mongoTemplate
            .getCollection(mongoTemplate.getCollectionName(Genre.class))
            .aggregate(
                Arrays.asList(
                    group(
                        "$name", sum("count", 1)),
                        match(gt("count", 1)),
                        project(computed("name", "$name"))
                )
            ).forEach((Block<? super Document>) result::add)
        ;

        return
            result
                .stream()
                .map(document -> new Genre((String)document.get("_id")))
                .collect(Collectors.toList())
            ;
    }
}
