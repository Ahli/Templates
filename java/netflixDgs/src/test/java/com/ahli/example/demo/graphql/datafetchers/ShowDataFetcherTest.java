package com.ahli.example.demo.graphql.datafetchers;

import com.ahli.example.demo.graphql.generated.client.ShowsGraphQLQuery;
import com.ahli.example.demo.graphql.generated.client.ShowsProjectionRoot;
import com.ahli.example.demo.graphql.generated.types.Show;
import com.ahli.example.demo.model.ShowService;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { DgsAutoConfiguration.class, ShowDataFetcher.class })
class ShowDataFetcherTest {
	
	@Autowired
	DgsQueryExecutor dgsQueryExecutor;
	
	@MockBean
	ShowService showService;
	
	@Test
	void shows() {
		when(showService.shows()).thenReturn(List.of(new Show("1", "Ozark", 2017)));
		
		final List<String> titles =
				dgsQueryExecutor.executeAndExtractJsonPath(" { shows { title releaseYear }}", "data.shows[*].title");
		
		assertThat(titles).containsExactly("Ozark");
	}
	
	@Test
	void showsWithQueryApi() {
		when(showService.shows("Oz")).thenReturn(List.of(new Show("1", "Ozark", 2017)));
		
		final ShowsGraphQLQuery query = new ShowsGraphQLQuery.Builder().titleFilter("Oz").build();
		final ShowsProjectionRoot projection = new ShowsProjectionRoot().title();
		final List<String> titles =
				dgsQueryExecutor.executeAndExtractJsonPath(new GraphQLQueryRequest(query, projection).serialize(),
						"data.shows[*].title");
		
		assertThat(titles).containsExactly("Ozark");
	}
	
}