package com.ahli.example.demo.model;

import com.ahli.example.demo.graphql.generated.types.Show;
import com.ahli.example.demo.graphql.generated.types.ShowInput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ShowService {
	
	private final List<Show> shows = new ArrayList<>(List.of(
			new Show(UUID.randomUUID().toString(), "Stranger Things", 2016),
			new Show(UUID.randomUUID().toString(), "Ozark", 2017),
			new Show(UUID.randomUUID().toString(), "The Crown", 2016),
			new Show(UUID.randomUUID().toString(), "Dead to Me", 2019),
			new Show(UUID.randomUUID().toString(), "Orange is the New Black", 2013)));
	
	public List<Show> shows() {
		return shows;
	}
	
	public List<Show> shows(final String titleFilter) {
		return shows.stream().filter(s -> s.getTitle().contains(titleFilter)).toList();
	}
	
	public Show add(final ShowInput input) {
		final Show show = Show.newBuilder()
				.id(UUID.randomUUID().toString())
				.title(input.getTitle())
				.releaseYear(input.getReleaseYear())
				.build();
		shows.add(show);
		return show;
	}
}
