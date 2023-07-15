package com.naren.model.order;

import lombok.Data;

@Data
public class VetaEvent<T> {

	private String type;

	private T event;

}