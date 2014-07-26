package org.circedroid.core.math;

import org.circedroid.core.geodesy.Method;

public abstract class AbstractMethodKernel implements MethodKernel {

	public static final String EQRC001from2Dto2D = "EQRC001from2Dto2D";
	public static final String EQRC001from3Dto2D = "EQRC001from3Dto2D";
	public static final String Geocentric3DtoGeographic3D = "Geocentric3DtoGeographic3D";
	public static final String Geographic3DtoGravityRelatedHeight_IGN = "Geographic3DtoGravityRelatedHeight_IGN";
	public static final String MILL001from2Dto2D = "MILL001from2Dto2D";
	public static final String PRCM012from2Dto2D = "PRCM012from2Dto2D";
	public static final String PRCM012from3Dto2D = "PRCM012from3Dto2D";
	public static final String PRCM013from2Dto2D = "PRCM013from2Dto2D";
	public static final String PRCM014from2Dto2D = "PRCM014from2Dto2D";
	public static final String PRCM015from2Dto2D = "PRCM015from2Dto2D";
	public static final String PRCM020from2Dto2D = "PRCM020from2Dto2D";
	public static final String PRCM030from2Dto2D = "PRCM030from2Dto2D";
	public static final String PRCM030from3Dto2D = "PRCM030from3Dto2D";
	public static final String PRCM040from2Dto2D = "PRCM040from2Dto2D";
	public static final String PRCM053from2Dto2D = "PRCM053from2Dto2D";
	public static final String PRCM060from2Dto2D = "PRCM060from2Dto2D";
	public static final String PRCM070from2Dto2D = "PRCM070from2Dto2D";
	public static final String PRCM093from2Dto2D = "PRCM093from2Dto2D";
	public static final String PRCM094from2Dto2D = "PRCM094from2Dto2D";
	public static final String PRCM095from2Dto2D = "PRCM095from2Dto2D";
	public static final String PVPM001From2Dto2D = "PVPM001From2Dto2D";
	public static final String TSGM110 = "TSGM110";
	public static final String TSGM111 = "TSGM111";
	public static final String TSGM112 = "TSGM112";
	public static final String TSGM120 = "TSGM120";
	public static final String TSGM510 = "TSGM510";
	public static final String TSGMGeoToGeo = "TSGMGeoToGeo";

	public static MethodKernel getKernel(Method method) {
		switch (method.getId()) {
		case EQRC001from2Dto2D:
		case EQRC001from3Dto2D:
		case Geocentric3DtoGeographic3D:
		case Geographic3DtoGravityRelatedHeight_IGN:
		case MILL001from2Dto2D:
		case PRCM012from2Dto2D:
		case PRCM012from3Dto2D:
		case PRCM013from2Dto2D:
		case PRCM014from2Dto2D:
		case PRCM015from2Dto2D:
		case PRCM020from2Dto2D:
		case PRCM030from2Dto2D:
		case PRCM030from3Dto2D:
		case PRCM040from2Dto2D:
		case PRCM053from2Dto2D:
		case PRCM060from2Dto2D:
		case PRCM070from2Dto2D:
		case PRCM093from2Dto2D:
		case PRCM094from2Dto2D:
		case PRCM095from2Dto2D:
		case PVPM001From2Dto2D:
		case TSGM110:
		case TSGM111:
		case TSGM112:
		case TSGM120:
		case TSGM510:
		case TSGMGeoToGeo:
		default:
			return null;
		}

	}

}
