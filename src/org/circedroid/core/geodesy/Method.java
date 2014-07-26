package org.circedroid.core.geodesy;

import org.circedroid.core.math.AbstractMethodKernel;
import org.circedroid.core.math.MethodKernel;

public class Method extends AbstractComponent {

	private static final long serialVersionUID = -4831992982065538608L;
	private int sourceDimensions = 0;
	private int targetDimensions = 0;

	private String methodFormula = null;

	private MethodKernel methodKernel = null;

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

	public String getMethodFormula() {
		return methodFormula;
	}

	public void setMethodFormula(String methodFormula) {
		this.methodFormula = methodFormula;
	}

	@Override
	public String toString() {
		return "Method [sourceDimensions=" + sourceDimensions
				+ ", targetDimensions=" + targetDimensions + ", methodFormula="
				+ methodFormula + "]";
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
		if (null == methodFormula) {
			throw new MethodParseException("MethodFormula null" + this);
		}
	}

	public MethodKernel getKernelInstance() {
		if (methodKernel == null) {
			this.methodKernel = AbstractMethodKernel.getKernel(this);
		}
		return methodKernel;
	}

}
