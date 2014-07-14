package org.circedroid.core.value;

public class DefaultValue implements Value {

	private Double value;
	
	public DefaultValue(Double value){
		this.value = value;
	}
	
	public Double getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "DefaultValue [value=" + value + "]";
	}

}
