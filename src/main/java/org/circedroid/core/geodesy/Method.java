package org.circedroid.core.geodesy;

public class Method extends AbstractComponent {

	private static final long serialVersionUID = -4831992982065538608L;
	private int sourceDimensions = 0;
	private int targetDimensions = 0;

	public int getSourceDimensions() {
		return sourceDimensions;
	}

	public void setSourceDimensions(int sourceDimensions) {
		this.sourceDimensions = sourceDimensions;
	}

	public int getTargetDimensions() {
		return targetDimensions;
	}

	public void setTargetDimensions(int targetDimensions) {
		this.targetDimensions = targetDimensions;
	}

	public String toString() {
		return "Method [id=" + id + ", name=" + name + ", sourceDimensions="
				+ sourceDimensions + ", targetDimensions=" + targetDimensions
				+ "]";
	}

	public static class MethodParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6620396530656226540L;

		public MethodParseException(String message) {
			super(message);
		}
	}

	public void check() throws MethodParseException {
		if (null == id) {
			throw new MethodParseException("Id null" + this);
		}
		if (null == name) {
			throw new MethodParseException("Name null" + this);
		}
		if (0 == sourceDimensions) {
			throw new MethodParseException("SourceDimensions null" + this);
		}
		if (0 == targetDimensions) {
			throw new MethodParseException("TargetDimensions null" + this);
		}
	}

}
