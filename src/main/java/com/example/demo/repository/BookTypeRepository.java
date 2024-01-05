package com.example.demo.repository;

import com.example.demo.entity.BookType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTypeRepository extends Neo4jRepository<BookType, Long> {

    @Query("MATCH (bt:BookType {type: $type})-[:RELATED_TO*1..2]->(related:BookType) RETURN related")
    List<BookType> findRelatedBookTypesWithinTwoSteps(String type);

    BookType findByType(String type);

}