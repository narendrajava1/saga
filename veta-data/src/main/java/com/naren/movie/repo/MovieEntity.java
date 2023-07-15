package com.naren.movie.repo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.naren.category.repo.CategoryEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "movie")
@Data
public class MovieEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary key
	 */
	@Id
	@Column(name = "id", nullable = false, unique = true)
	private String id;
	
	/**
	 * Movie
	 */
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	/**
	 * Category
	 */
//	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;

	
}
