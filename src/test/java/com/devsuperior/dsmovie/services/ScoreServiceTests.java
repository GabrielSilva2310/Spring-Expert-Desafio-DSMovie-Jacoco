package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	
	@Mock
	private UserService userService;
	
	@Mock
	private MovieRepository movieRepository;
	
	@Mock
	private ScoreRepository scoreRepository;
	
	private Long existId, noExistId;
	private UserEntity userEntity;
	private MovieEntity movieEntity;
	private ScoreEntity scoreEntity;
	private ScoreDTO scoreDTO;
	
	
	@BeforeEach
	void set() throws Exception {
		existId=1L;
		noExistId=2L;
		userEntity=UserFactory.createUserEntity();
		movieEntity=MovieFactory.createMovieEntity();
		scoreEntity=ScoreFactory.createScoreEntity();
		movieEntity.getScores().add(scoreEntity);
		scoreDTO=ScoreFactory.createScoreDTO();
		
		
		Mockito.when(userService.authenticated()).thenReturn(userEntity);
		
		Mockito.when(movieRepository.findById(existId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(movieRepository.findById(noExistId)).thenReturn(Optional.empty());
		Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);
		
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);

	}
	
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result=service.saveScore(scoreDTO);
		
		Assertions.assertEquals(result.getId(), scoreDTO.getMovieId());
		
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		movieEntity.setId(noExistId);
		scoreEntity.setMovie(movieEntity);
		scoreDTO=new ScoreDTO(scoreEntity);
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			MovieDTO result=service.saveScore(scoreDTO);
		});
	}
	
}
