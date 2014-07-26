package org.circedroid.core.geodesy;

import java.util.HashMap;
import java.util.Map;

import org.circedroid.core.value.Bbox;
import org.circedroid.core.value.Value;

/**
 * Classe pour representer une opération de conversion. Une opération est une
 * instance d'un noyau de projection. Cette classe contient un noyau et ses
 * paramètres.
 * 
 * @author thibbo
 * 
 */
public class Operation extends AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4591160345168175282L;

	public enum OperationType {

		Conversion(), Transformation();
	}

	private OperationType type;

	private String methodId;

	private Map<String, Value> usesValues;

	private Bbox bbox;

	private String sourceCRS;
	private String targetCRS;

	public Operation() {
		this.usesValues = new HashMap<>();
		this.bbox = new Bbox();
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	public Map<String, Value> getUsesValues() {
		return usesValues;
	}

	public void addUsesValue(String name, Value value) {
		this.usesValues.put(name, value);
	}

	public Bbox getBbox() {
		return bbox;
	}

	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	public String getSourceCRS() {
		return sourceCRS;
	}

	public void setSourceCRS(String sourceCRS) {
		this.sourceCRS = sourceCRS;
	}

	public String getTargetCRS() {
		return targetCRS;
	}

	public void setTargetCRS(String targetCRS) {
		this.targetCRS = targetCRS;
	}

	public static class OperationParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2882993592420697081L;

		public OperationParseException(String message) {
			super(message);
		}
	}

	public void check() throws OperationParseException {
		if (null == id) {
			throw new OperationParseException("Id null" + this);
		}
		if (null == name) {
			throw new OperationParseException("Name null" + this);
		}
		if (null == type) {
			throw new OperationParseException("Type null" + this);
		}
		if (null == methodId) {
			throw new OperationParseException("MethodId null" + this);
		}
		if (null == usesValues) {
			throw new OperationParseException("UsesValues null" + this);
		}
		if (usesValues.size() == 0) {
			throw new OperationParseException("UsesValues vide" + this);
		}
		for (String key : usesValues.keySet()) {
			if (usesValues.get(key) == null) {
				throw new OperationParseException(
						"UsesValues wrong value for key " + key + " " + this);
			}
		}
		if (type == OperationType.Transformation && null == sourceCRS) {
			throw new OperationParseException("SourceCRS vide" + this);
		}
		if (type == OperationType.Transformation && null == targetCRS) {
			throw new OperationParseException("TargetCRS vide" + this);
		}
	}

	@Override
	public String toString() {
		return "Operation [id=" + id + ", type=" + type + ", name=" + name
				+ ", methodId=" + methodId + ", usesValues=" + usesValues
				+ ", bbox=" + bbox + ", sourceCRS=" + sourceCRS
				+ ", targetCRS=" + targetCRS + "]";
	}

}
