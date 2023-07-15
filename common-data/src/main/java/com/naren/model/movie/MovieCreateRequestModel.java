package com.naren.model.movie;

import lombok.Data;

@Data
public class MovieCreateRequestModel {

	private String title;

	/**
	 * Category id
	 */
	private String categoryId;

}
