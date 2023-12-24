package com.example.demo.config;

import com.example.demo.graphql.BookByTitleDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class GraphqlConfiguration {

    private final BookByTitleDataFetcher bookByTitleDataFetcher;

    public GraphqlConfiguration(BookByTitleDataFetcher bookByTitleDataFetcher) {
        this.bookByTitleDataFetcher = bookByTitleDataFetcher;
    }

    @Bean
    public GraphQLSchema graphQLSchema() throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        typeRegistry.merge(schemaParser.parse(new ClassPathResource("schema.graphqls").getInputStream()));

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("bookByTitle", bookByTitleDataFetcher))
                .build();

        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
