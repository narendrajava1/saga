package com.naren.movie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naren.model.movie.MovieDTO;
import com.naren.movie.repo.MovieEntity;
import com.naren.movie.repo.MovieRepository;


@Service("movieService")
public class MovieService {

	@Autowired
	private MovieRepository repository;
	
	public MovieDTO insert(MovieDTO movie) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		MovieEntity movieEntity = modelMapper.map(movie, MovieEntity.class);
		
		movieEntity.setId(UUID.randomUUID().toString());
		
		movieEntity = repository.save(movieEntity);
		
		movie = modelMapper.map(movie, MovieDTO.class);
		
		return movie;
	}
	
	public MovieDTO update(MovieDTO movie) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		MovieEntity movieEntity = modelMapper.map(movie, MovieEntity.class);
		
		movieEntity = repository.save(movieEntity);
		
		movie = modelMapper.map(movie, MovieDTO.class);
		
		return movie;
	}

	public MovieDTO findById(String id) {
		MovieEntity movie = repository.findById(id).orElse(null);
		
		if(movie == null)
			return null;
		
		MovieDTO movieDTO = new ModelMapper().map(movie, MovieDTO.class);
		
		return movieDTO;
	}
	
	public List<MovieDTO> getAll() {
		ArrayList<MovieEntity> movies = (ArrayList<MovieEntity>) repository.findAll();
		
		ModelMapper modelMapper = new ModelMapper();
		
		List<MovieDTO> moviesDTO = movies
				  .stream()
				  .map(movie -> modelMapper.map(movie, MovieDTO.class))
				  .collect(Collectors.toList());
		
		return moviesDTO;
	}

	public void delete(String id) {
		repository.deleteById(id);
	}

}
