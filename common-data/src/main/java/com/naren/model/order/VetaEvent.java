package com.naren.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(value = Include.NON_NULL)
@ToString
public class VetaEvent<T> {
	
	private String orderType;
	
	private T event;
	
	
	

}