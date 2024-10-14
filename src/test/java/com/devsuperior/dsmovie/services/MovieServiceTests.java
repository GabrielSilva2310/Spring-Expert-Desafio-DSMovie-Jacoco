package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	
	@Mock
	private MovieRepository repository;
	
	private Long existId, noExistId;
	private MovieEntity movieEntity;
	private Page<MovieEntity> page;
	private String title;

	
	
	@BeforeEach
	void set() throws Exception {
		existId=1L;
		noExistId=2L;
		movieEntity=MovieFactory.createMovieEntity();
		page=new PageImpl<>(List.of(movieEntity));
		title=movieEntity.getTitle();

		
		Mockito.when(repository.searchByTitle(any(), (Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(repository.findById(noExistId)).thenReturn(Optional.empty());

	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		
		Pageable pageable=PageRequest.of(0, 12);
		
		Page<MovieDTO> result=service.findAll(title, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getId(), movieEntity.getId());
	}

	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		
		MovieDTO result=service.findById(existId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), movieEntity.getId());
		Assertions.assertEquals(result.getTitle(), title);

		
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(noExistId);
		});
		
	}
	/*
	@Test
	public void insertShouldReturnMovieDTO() {
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
	*/
}
