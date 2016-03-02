package com.qingruan.museum.msg.constantth;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.msg.DataContent;

@Getter
@Setter
public class ThDataContent extends DataContent {
	public ShowCase showCase;

	// 展柜序号
	public static enum ShowCase {

		ZEROTH(0), FIRST(1), SECOND(2), THIRD(3);

		private final Integer value;

		private ShowCase(Integer value) {
			this.value = value;
		}

		public Integer value() {
			return value;
		}

		public static ShowCase from(Integer value) {
			for (ShowCase item : ShowCase.values()) {
				if (item.value.equals(value))
					return item;
			}

			return null;
		}

	}
}
