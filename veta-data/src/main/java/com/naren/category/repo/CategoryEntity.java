package com.naren.category.repo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.naren.movie.repo.MovieEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class CategoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary key
	 */
	@Id
	@Column(name = "id")
	private String id;
	
	/**
	 * Category
	 */
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	/**
	 * List of movies
	 */
//	@JsonManagedReference
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<MovieEntity> movies = new ArrayList<>();

	
	
}
