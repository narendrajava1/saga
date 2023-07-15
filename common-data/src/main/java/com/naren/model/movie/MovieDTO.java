package com.naren.model.movie;

import com.naren.model.category.CategoryDTO;

import lombok.Data;
@Data
public class MovieDTO {
	
	/**
	 * Primary key
	 */
	private String id;
	
	/**
	 * Movie title
	 */
	private String title;

	/**
	 * Category
	 */
	private CategoryDTO category;

	
}
