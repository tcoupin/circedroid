package org.circedroid.core.math;

import java.util.List;
import java.util.Map;

import org.circedroid.core.exception.InitialisationFailException;
import org.circedroid.core.exception.OperationFailException;
import org.circedroid.core.geodesy.Method;

public interface MethodKernel {
	public boolean initialize(Map<String, Double> params)
			throws InitialisationFailException;

	public List<Double> direct(List<Double> coords) throws OperationFailException;

	public List<Double> reverse(List<Double> coords) throws OperationFailException;
	
}
