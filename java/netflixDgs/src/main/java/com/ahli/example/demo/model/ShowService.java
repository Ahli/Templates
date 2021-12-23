package com.ahli.example.demo.model;

import com.ahli.example.demo.graphql.generated.types.Show;
import com.ahli.example.demo.graphql.generated.types.ShowInput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShowService {
	
	private final List<Show> shows = List.of(
			new Show("Stranger Things", 2016),
			new Show("Ozark", 2017),
			new Show("The Crown", 2016),
			new Show("Dead to Me", 2019),
			new Show("Orange is the New Black", 2013));
	
	
	public List<Show> shows() {
		return shows;
	}
	
	public Show add(ShowInput input) {
		Show show = Show.newBuilder()
				.id(UUID.randomUUID())
				.title(input.getTitle())
				.releaseYear(input.getReleaseYear())
				.build();
		
		shows.add(show);
		
		return show;
	}
}
