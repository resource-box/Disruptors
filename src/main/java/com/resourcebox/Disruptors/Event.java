package com.resourcebox.Disruptors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Event<T> {

	private T data;

	public void clear() {
		this.data = null;
	}

}
