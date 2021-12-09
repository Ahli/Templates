package com.ahli.example.demo;

import com.ahli.example.demo.in.DefaultPetsApiDelegate;
import com.ahli.example.demo.in.RestResponseEntityExceptionHandler;
import com.ahli.example.gen.api.PetsApi;
import com.ahli.example.gen.model.PetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { RestResponseEntityExceptionHandler.class, DefaultPetsApiDelegate.class })
@ComponentScan(basePackageClasses = { PetsApi.class })
@Import(RestApiTest.Context.class)
@EnableWebMvc
@AutoConfigureMockMvc
class RestApiTest {
	
	static final String ENDPOINT_PETS = "/v1/pets";
	final PetDto dogDto = PetDto.builder().id(1L).name("Rex").tag("Dog").build();
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	DefaultPetsApiDelegate delegate;
	
	@Test
	void testShouldListPetsWithoutLimit() throws Exception {
		
		when(delegate.listPets(isNull())).thenReturn(new ResponseEntity<>(List.of(dogDto), HttpStatus.OK));
		
		mockMvc.perform(get(ENDPOINT_PETS)).andExpect(status().isOk()).andExpect(content().json("""
		                                                                                          [{
		                                                                                             "name" : "Rex",
		                                                                                             "id" : 1,
		                                                                                             "tag" : "Dog"
		                                                                                          }]
		                                                                                        """));
	}
	
	@Test
	void testShouldRespondAsExpectedAfterUnexpectedException() throws Exception {
		when(delegate.listPets(any())).thenThrow(new NullPointerException("test"));
		
		mockMvc.perform(get(ENDPOINT_PETS)).andExpect(status().isInternalServerError()).andExpect(content().string("""
		                                                                                                             {
		                                                                                                                "code": 500,
		                                                                                                                "message": "Service encountered unexpected error"
		                                                                                                             }
		                                                                                                           """));
	}
	
	@Test
	void testShouldReturnBadRequestOnMissingBody() throws Exception {
		mockMvc.perform(post(ENDPOINT_PETS).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().json("""
				                            {
				                               "code": 400,
				                               "message": "Request could not be validated: Payload could not be processed"
				                            }
				                            """));
	}
	
	@TestConfiguration
	static class Context {
		// override Beans
	}
}
