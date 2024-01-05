package com.example.demo.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Node("BookType")
public class BookType {

    @Id
    private Long id;

    private String type;

    @Property("ids")
    private List<Integer> ids;

    @Relationship(type = "RELATED_TO", direction = Relationship.Direction.OUTGOING)
    private List<BookType> relatedBookTypes;

    public BookType(String type) {
        this.type = type;
    }

    // 标准的 getter 和 setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<BookType> getRelatedBookTypes() {
        return relatedBookTypes;
    }

    public void setRelatedBookTypes(List<BookType> relatedBookTypes) {
        this.relatedBookTypes = relatedBookTypes;
    }
}
