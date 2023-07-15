package com.naren.movie.repo;

import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<MovieEntity, String> {

}
