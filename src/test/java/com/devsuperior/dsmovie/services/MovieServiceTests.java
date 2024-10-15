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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	
	@Mock
	private MovieRepository repository;
	
	private Long existId, noExistId, dependentId;
	private MovieEntity movieEntity;
	private MovieDTO movieDTO;
	private Page<MovieEntity> page;
	private String title;

	
	
	@BeforeEach
	void set() throws Exception {
		existId=1L;
		noExistId=2L;
		dependentId=3L;
		movieEntity=MovieFactory.createMovieEntity();
		movieDTO=MovieFactory.createMovieDTO();
		page=new PageImpl<>(List.of(movieEntity));
		title=movieEntity.getTitle();

		
		Mockito.when(repository.searchByTitle(any(), (Pageable)any())).thenReturn(page);
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(repository.findById(noExistId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(any())).thenReturn(movieEntity);
		
		Mockito.when(repository.getReferenceById(existId)).thenReturn(movieEntity);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getReferenceById(noExistId);
		
		Mockito.when(repository.existsById(existId)).thenReturn(true);
		Mockito.when(repository.existsById(noExistId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);

		Mockito.doNothing().when(repository).deleteById(existId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
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
	
	@Test
	public void insertShouldReturnMovieDTO() {
		
		MovieDTO result=service.insert(movieDTO);
		
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals(result.getId(), movieDTO.getId());
		
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		
		MovieDTO result=service.update(existId, movieDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existId);
		Assertions.assertEquals(result.getTitle(), movieDTO.getTitle());
		
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			MovieDTO result=service.update(noExistId, movieDTO);
		});
		
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()->{
			service.delete(existId);
		});
		
		Mockito.verify(repository).deleteById(existId);
			
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(noExistId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, ()->{
			service.delete(dependentId);
		});
	}
	
}
