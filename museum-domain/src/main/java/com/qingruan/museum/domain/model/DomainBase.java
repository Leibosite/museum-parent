package com.qingruan.museum.domain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainBase implements Serializable {
	private static final long serialVersionUID = -3587908699583581484L;
	private Integer resultCode;
	private String msg;

}
