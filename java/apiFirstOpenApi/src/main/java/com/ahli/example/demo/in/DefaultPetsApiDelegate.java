package com.ahli.example.demo.in;

import com.ahli.example.gen.api.PetsApiDelegate;
import com.ahli.example.gen.model.PetDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultPetsApiDelegate implements PetsApiDelegate {
	
	@Override
	public ResponseEntity<Void> createPets(final PetDto petDto) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<List<PetDto>> listPets(final Integer limit) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<PetDto> showPetById(final String petId) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
}
