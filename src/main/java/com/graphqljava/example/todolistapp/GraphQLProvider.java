package com.graphqljava.example.todolistapp;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    // Initializes the GraphQL configuration and Handles Requests from the clients

    private static final String GRAPHQL_SCHEMA_FILE = "BOOT-INF/classes/schema.graphqls";

    private GraphQL graphQL;

    @Autowired
    private GraphQLDataFetcher graphQLDataFetcher;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws Exception {
        FileSystem fileSystem = this.getFileSystem(getClass().getClassLoader().getResource(GRAPHQL_SCHEMA_FILE).toURI());
        Path graphQLSchemaFilePath = fileSystem.getPath(GRAPHQL_SCHEMA_FILE);
        if(graphQLSchemaFilePath != null){
            GraphQLSchema graphQLSchema = buildSchema(graphQLSchemaFilePath);
            this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        }
        fileSystem.close();
    }

    private FileSystem getFileSystem(URI location) throws IOException {
        try {
            return FileSystems.getFileSystem(location);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(location, Collections.emptyMap());
        }
    }

    private GraphQLSchema buildSchema(Path graphQLSchemaFilePath) throws IOException {
        String graphQLSchema = new String(Files.readAllBytes(graphQLSchemaFilePath),StandardCharsets.UTF_8);
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(graphQLSchema);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    // Mapping Various 'queries' and 'mutations' in 'schema' with corresponding 'data-fetcher' methods
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("List")
                        .dataFetcher("items", graphQLDataFetcher.getListItems()))
                .type(newTypeWiring("Item")
                        .dataFetcher("listId", graphQLDataFetcher.getItemListId()))
                .type(newTypeWiring("Query")
                        .dataFetcher("lists", graphQLDataFetcher.getLists()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createItem", graphQLDataFetcher.createItem())
                        .dataFetcher("createList", graphQLDataFetcher.createList())
                        .dataFetcher("updateList", graphQLDataFetcher.updateList())
                        .dataFetcher("moveItem", graphQLDataFetcher.moveItem())
                        .dataFetcher("deleteItem", graphQLDataFetcher.deleteItem())
                        .dataFetcher("deleteList", graphQLDataFetcher.deleteList()))
                .build();
    }
}
