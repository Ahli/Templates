package com.ahli.example.demo.graphql.datafetchers;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShowsDataFetcherViaClientTest {
	final MonoGraphQLClient monoGraphQLClient;
	
	public ShowsDataFetcherViaClientTest(@LocalServerPort final Integer port) {
		final WebClient webClient = WebClient.create("http://localhost:" + port.toString() + "/graphql");
		this.monoGraphQLClient = MonoGraphQLClient.createWithWebClient(webClient);
	}
	
	@Test
	void shows() {
		final String query = "{ shows { title releaseYear }}";
		
		// Read more about executeQuery() at https://netflix.github.io/dgs/advanced/java-client/
		final GraphQLResponse response = monoGraphQLClient.reactiveExecuteQuery(query).block();
		
		final List<?> titles = response.extractValueAsObject("shows[*].title", List.class);
		
		assertTrue(titles.contains("Ozark"));
	}
}
