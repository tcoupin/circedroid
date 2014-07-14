package org.circedroid.core.geodesy;

import java.io.Serializable;

import org.circedroid.core.register.Register;

public abstract class AbstractComponent implements Serializable {
	
	private static final long serialVersionUID = 1101704683376315023L;

	protected Register register;
	
	/**
	 * Identifiant unique
	 */
	protected String id;
	
	/**
	 * Nom
	 */
	protected String name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}


}
