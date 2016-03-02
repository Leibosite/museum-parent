package com.qingruan.museum.domain.model.app;

/**
 * @author leibosite
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowroomModel {
	private Long id;
	private double score;
	private String name;
	private String advice;
	private String value1;
	private String value2;
	private String value3;
	private int advice_icon;
}
