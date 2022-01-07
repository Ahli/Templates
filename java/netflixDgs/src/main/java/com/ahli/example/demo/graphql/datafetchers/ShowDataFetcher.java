package com.ahli.example.demo.graphql.datafetchers;

import com.ahli.example.demo.graphql.generated.types.Show;
import com.ahli.example.demo.graphql.generated.types.ShowInput;
import com.ahli.example.demo.model.ShowService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;

@DgsComponent
public class ShowDataFetcher {
	
	private final ShowService showService;
	
	public ShowDataFetcher(final ShowService showService) {
		this.showService = showService;
	}
	
	@DgsQuery
	public List<Show> shows(@InputArgument final String titleFilter) {
		if (titleFilter == null) {
			return showService.shows();
		}
		return showService.shows(titleFilter);
	}
	
	@DgsMutation
	public Show createShow(final ShowInput showInput) {
		return showService.add(showInput);
	}
	
}
