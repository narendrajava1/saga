package com.naren.model.movie;

import lombok.Data;

/**
 * Movie request class for update
 * 
 * @author Marcelo Soares <marceloh.web@gmail.com>
 *
 */
@Data
public class MovieUpdateRequestModel extends MovieCreateRequestModel {
	
	/**
	 * Primary key
	 */
	private String id;

	/**
	 * @return the id
	 */

}
