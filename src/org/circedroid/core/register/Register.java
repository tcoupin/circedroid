package org.circedroid.core.register;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.circedroid.core.RegisterManager;
import org.circedroid.core.database.RegistryFTS;
import org.circedroid.core.exception.NotFoundException;
import org.circedroid.core.geodesy.Axis;
import org.circedroid.core.geodesy.Axis.AxisParseException;
import org.circedroid.core.geodesy.CRS;
import org.circedroid.core.geodesy.CoordinateSystem;
import org.circedroid.core.geodesy.CoordinateSystem.CSParseException;
import org.circedroid.core.geodesy.CoordinateSystem.CSType;
import org.circedroid.core.geodesy.Datum;
import org.circedroid.core.geodesy.Datum.DatumParseException;
import org.circedroid.core.geodesy.Datum.DatumType;
import org.circedroid.core.geodesy.Ellipsoid;
import org.circedroid.core.geodesy.Ellipsoid.EllipsoidParseException;
import org.circedroid.core.geodesy.Method;
import org.circedroid.core.geodesy.Method.MethodParseException;
import org.circedroid.core.geodesy.Operation;
import org.circedroid.core.geodesy.Operation.OperationParseException;
import org.circedroid.core.geodesy.Operation.OperationType;
import org.circedroid.core.geodesy.PrimeMeridian;
import org.circedroid.core.geodesy.PrimeMeridian.PrimeMeridianParseException;
import org.circedroid.core.tools.StringUtils;
import org.circedroid.core.tools.Tools;
import org.circedroid.core.value.Angle;
import org.circedroid.core.value.DefaultValue;
import org.circedroid.core.value.Angle.AngleUoM;
import org.circedroid.core.value.Value;
import org.circedroid.log.LogManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.util.Xml;

public class Register implements Serializable {
	private static final String LOG_TAG = Register.class.getSimpleName();

	private static final String GML_NAMESPACE = "gml";
	private static final String CRS_TAG = "crs";
	private static final String PROJECTEDCRS_TAG = "ProjectedCRS";
	private static final String GEOGRAPHICCRS_TAG = "GeographicCRS";
	private static final String GEOCENTRICCRS_TAG = "GeocentricCRS";
	private static final String VERTICALCRS_TAG = "VerticalCRS";
	private static final String COMPOUNDCRS_TAG = "CompoundCRS";
	private static final String NAME_TAG = "name";
	private static final String REGISTRE_IGN_FR_TAG = "registre.ign.fr";
	private static final String EPSG_TAG = "EPSG";
	private static final String WESTBOUNDLONGITUDE_TAG = "westBoundLongitude";
	private static final String EASTBOUNDLONGITUDE_TAG = "eastBoundLongitude";
	private static final String SOUTHBOUNDLATITUDE_TAG = "southBoundLatitude";
	private static final String NORTHBOUNDLATITUDE_TAG = "northBoundLatitude";
	private static final String USESGEODETICDATUM_TAG = "usesGeodeticDatum";
	private static final String USESVERTICALDATUM_TAG = "usesVerticalDatum";
	private static final String BASEGEOGRAPHICCRS_TAG = "baseGeographicCRS";
	private static final String DEFINEDBYCONVERSION_TAG = "definedByConversion";
	private static final String INCLUDESSINGLECRS_TAG = "includesSingleCRS";
	private static final String USESCARTESIANCS_TAG = "usesCartesianCS";
	private static final String USESELLIPSOIDALCS_TAG = "usesEllipsoidalCS";
	private static final String USESVERTICALCS_TAG = "usesVerticalCS";
	private static final String ID_TAG = "id";
	private static final String HREF_TAG = "href";
	private static final String DATUM_TAG = "datum";
	private static final String GEODETICDATUM_TAG = "GeodeticDatum";
	private static final String VERTICALDATUM_TAG = "VerticalDatum";
	private static final String USESPRIMEMERIDIAN_TAG = "usesPrimeMeridian";
	private static final String USESELLIPSOID_TAG = "usesEllipsoid";
	private static final String PRIMEMERIDIAN_TAG = "primeMeridian";
	private static final String PRIMEMERIDIAN_TAG_UPPER = "PrimeMeridian";
	private static final String GREENWICHLONGITUDE_TAG = "greenwichLongitude";
	private static final String UOM_TAG = "uom";
	private static final String ELLIPSOID_TAG = "ellipsoid";
	private static final String ELLIPSOID_TAG_UPPER = "Ellipsoid";
	private static final String SEMIMAJORAXIS_TAG = "semiMajorAxis";
	private static final String INVERSEFLATTENING_TAG = "inverseFlattening";
	private static final String SEMIMINORAXIS_TAG = "semiMinorAxis";
	private static final String ISSPHERE_TAG = "isSphere";
	private static final String COORDINATESYSTEM_TAG = "coordinateSystem";
	private static final String CARTESIANCS_TAG = "CartesianCS";
	private static final String USESAXIS_TAG = "usesAxis";
	private static final String ELLIPSOIDALCS_TAG = "EllipsoidalCS";
	private static final String VERTICALCS_TAG = "VerticalCS";
	private static final String AXIS_TAG = "axis";
	private static final String COORDINATESYSTEMAXIS_TAG = "CoordinateSystemAxis";
	private static final String AXISABBREV_TAG = "axisAbbrev";
	private static final String OPERATION_TAG = "operation";
	private static final String CONVERSION_TAG = "Conversion";
	private static final String USESMETHOD_TAG = "usesMethod";
	private static final String USESVALUE_TAG = "usesValue";
	private static final String DMSANGLEVALUE_TAG = "dmsAngleValue";
	private static final String DEGREES_TAG = "degrees";
	private static final String DIRECTION_TAG = "direction";
	private static final String MINUTES_TAG = "minutes";
	private static final String SECONDES_TAG = "secondes";
	private static final String VALUE_TAG = "value";
	private static final String VALUEOFPARAMETER_TAG = "valueOfParameter";
	private static final String E_TAG = "E";
	private static final String N_TAG = "N";
	private static final String W_TAG = "W";
	private static final String S_TAG = "S";
	private static final String TRANSFORMATION_TAG = "Transformation";
	private static final String SOURCECRS_TAG = "sourceCRS";
	private static final String TARGETCRS_TAG = "targetCRS";
	private static final String OPERATIONMETHOD_TAG = "operationMethod";
	private static final String OPERATIONMETHOD_TAG_UPPER = "OperationMethod";
	private static final String SOURCEDIMENSIONS_TAG = "sourceDimensions";
	private static final String TARGETDIMENSIONS_TAG = "targetDimensions";
	private static final String METHODFORMULA_TAG = "methodFormula";

	private Map<String, CRS> crslist;
	private Map<String, Datum> datumlist;
	private Map<String, PrimeMeridian> meridianlist;
	private Map<String, Ellipsoid> ellipsoidlist;
	private Map<String, CoordinateSystem> coordinatesystemslist;
	private Map<String, Axis> axislist;
	private Map<String, Operation> operationlist;
	private Map<String, Method> methodlist;

	public Register(Context context, InputStream is) {
		crslist = new HashMap<String, CRS>();
		datumlist = new HashMap<String, Datum>();
		meridianlist = new HashMap<String, PrimeMeridian>();
		ellipsoidlist = new HashMap<String, Ellipsoid>();
		coordinatesystemslist = new HashMap<String, CoordinateSystem>();
		axislist = new HashMap<String, Axis>();
		operationlist = new HashMap<String, Operation>();
		methodlist = new HashMap<String, Method>();
		parseFile(is);
		RegistryFTS.getInstance(context).indexCRS(crslist);

	}
	
	private void log2user(String message){
		LogManager.getInstance().log(message);
	}

	private void parseFile(InputStream is) {
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	    factory.setNamespaceAware(true);
		//XmlPullParser parser = Xml.newPullParser();
	    XmlPullParser parser;
		try {
			parser = factory.newPullParser();
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {
			parser.setInput(is, null);
			int eventType = parser.getEventType();
			String startTag;
			CRS crs;
			Datum datum;
			PrimeMeridian meridian;
			Ellipsoid ell;
			CoordinateSystem cs;
			Axis axis;
			Operation operation;
			Method method;
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					startTag = parser.getName();
					if (startTag.equals(CRS_TAG)) {
						Log.d(LOG_TAG, "Found crs tag");
						try {
							crs = parseCRS(parser);
							crs.setRegister(this);
							crslist.put(crs.getId(), crs);
							log2user("-CRS "+crs.getId());
							
						} catch (CRS.CRSParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(DATUM_TAG)) {
						Log.d(LOG_TAG, "Found datum tag");
						try {
							datum = parseDatum(parser);
							datumlist.put(datum.getId(), datum);
							log2user("-Datum "+datum.getId());
						} catch (DatumParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(PRIMEMERIDIAN_TAG)) {
						Log.d(LOG_TAG, "Found meridian tag");
						try {
							meridian = parsePrimeMeridian(parser);
							meridianlist.put(meridian.getId(), meridian);
							log2user("-Meridian "+meridian.getId());
						} catch (PrimeMeridianParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(ELLIPSOID_TAG)) {
						Log.d(LOG_TAG, "Found ellipsoid tag");
						try {
							ell = parseEllipsoid(parser);
							ellipsoidlist.put(ell.getId(), ell);
							log2user("-Ellip. "+ell.getId());
						} catch (EllipsoidParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(COORDINATESYSTEM_TAG)) {
						Log.d(LOG_TAG, "Found coordinatesystem tag");
						try {
							cs = parseCoordinateSystem(parser);
							coordinatesystemslist.put(cs.getId(), cs);
							log2user("-CoordinateSyst. "+cs.getId());
						} catch (CSParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(AXIS_TAG)) {
						Log.d(LOG_TAG, "Found axis tag");
						try {
							axis = parseAxis(parser);
							axislist.put(axis.getId(), axis);
							log2user("-Axis "+axis.getId());
						} catch (AxisParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(OPERATION_TAG)) {
						Log.d(LOG_TAG, "Found operation tag");
						try {
							operation = parseOperation(parser);
							operationlist.put(operation.getId(), operation);
							log2user("-Operation "+operation.getId());
						} catch (OperationParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					} else if (startTag.equals(OPERATIONMETHOD_TAG)) {
						Log.d(LOG_TAG, "Found operationMethod tag");
						try {
							method = parseMethod(parser);
							methodlist.put(method.getId(), method);
							log2user("-Method "+method.getId());
						} catch (MethodParseException e) {
							e.printStackTrace();
							Log.e(LOG_TAG, e.getMessage());
						}
					}
				}
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}

	}

	private CRS parseCRS(XmlPullParser parser) throws CRS.CRSParseException {
		CRS crs = new CRS();
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !CRS_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (GEOCENTRICCRS_TAG.equals(parser.getName())) {
						crs.setType(CRS.CRSType.GeocentricCRS);
						crs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (GEOGRAPHICCRS_TAG.equals(parser.getName())) {
						crs.setType(CRS.CRSType.GeographicCRS);
						crs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (VERTICALCRS_TAG.equals(parser.getName())) {
						crs.setType(CRS.CRSType.VerticalCRS);
						crs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (PROJECTEDCRS_TAG.equals(parser.getName())) {
						crs.setType(CRS.CRSType.ProjectedCRS);
						crs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (INCLUDESSINGLECRS_TAG.equals(parser.getName())) {
						crs.addIncludedSingleCRSId(Tools.url2id(parser
								.getAttributeValue(null, HREF_TAG)));
					} else if (COMPOUNDCRS_TAG.equals(parser.getName())) {
						crs.setType(CRS.CRSType.CompoundCRS);
						crs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (NAME_TAG.equals(parser.getName())) {
						eventType = parser.next();
						tmpStr = parser.getText();
						if (tmpStr == null) {
							continue;
						}
						if (tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							Log.d(LOG_TAG, "found ignf alias:" + tmpStr);
							String[] splits = tmpStr.split("/");
							crs.addAlias("IGNF:" + splits[splits.length - 1]);
						} else if (tmpStr.contains(EPSG_TAG)) {
							Log.d(LOG_TAG, "found epsg alias:" + tmpStr);
							String[] splits = tmpStr.split(":");
							crs.addAlias("EPSG:" + splits[splits.length - 1]);
						} else {
							Log.d(LOG_TAG, "found name:" + tmpStr);
							crs.setName(tmpStr);
						}
					} else if (WESTBOUNDLONGITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						crs.getBbox().setWest(Double.parseDouble(tmpStr));
					} else if (EASTBOUNDLONGITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						crs.getBbox().setEast(Double.parseDouble(tmpStr));
					} else if (NORTHBOUNDLATITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						crs.getBbox().setNorth(Double.parseDouble(tmpStr));
					} else if (SOUTHBOUNDLATITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						crs.getBbox().setSouth(Double.parseDouble(tmpStr));
					} else if (USESGEODETICDATUM_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						crs.setDatumId(Tools.url2id(tmpStr));
					} else if (USESVERTICALDATUM_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						crs.setDatumId(Tools.url2id(tmpStr));
					} else if (BASEGEOGRAPHICCRS_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						crs.setBaseGeographicalCRSId(Tools.url2id(tmpStr));
					} else if (DEFINEDBYCONVERSION_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						crs.setConversionId(Tools.url2id(tmpStr));
					} else if (USESCARTESIANCS_TAG.equals(parser.getName()) || USESELLIPSOIDALCS_TAG.equals(parser.getName()) || USESVERTICALCS_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						crs.setCoordinateSystemId(Tools.url2id(tmpStr));
					}
				}
			}
			Log.d(LOG_TAG, crs.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new CRS.CRSParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new CRS.CRSParseException(e.getMessage());
		}

		crs.check();

		return crs;
	}

	private Datum parseDatum(XmlPullParser parser)
			throws Datum.DatumParseException {
		Datum datum = new Datum();
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !DATUM_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (GEODETICDATUM_TAG.equals(parser.getName())) {
						datum.setType(DatumType.GeodeticDatum);
						datum.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (VERTICALDATUM_TAG.equals(parser.getName())) {
						datum.setType(DatumType.VerticalDatum);
						datum.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (NAME_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						if (!tmpStr.contains(EPSG_TAG)
								&& !tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							datum.setName(tmpStr);
						}
					} else if (USESELLIPSOID_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						datum.setEllipsoidId(Tools.url2id(tmpStr));
					} else if (USESPRIMEMERIDIAN_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						datum.setPrimeMeridianId(Tools.url2id(tmpStr));
					}
				}
			}
			Log.d(LOG_TAG, datum.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new Datum.DatumParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Datum.DatumParseException(e.getMessage());
		}

		datum.check();

		return datum;
	}

	private PrimeMeridian parsePrimeMeridian(XmlPullParser parser)
			throws PrimeMeridian.PrimeMeridianParseException {
		PrimeMeridian meridian = new PrimeMeridian();
		AngleUoM uom;
		double dbltmp;
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !PRIMEMERIDIAN_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (PRIMEMERIDIAN_TAG_UPPER.equals(parser.getName())) {
						meridian.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (NAME_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						if (!tmpStr.contains(EPSG_TAG)
								&& !tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							meridian.setName(tmpStr);
						}
					} else if (GREENWICHLONGITUDE_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, UOM_TAG);
						uom = null;
						for (AngleUoM it : AngleUoM.values()) {
							if (it.equals(tmpStr)) {
								uom = it;
							}
						}
						if (null == uom) {
							continue;
						}
						tmpStr = parser.nextText();
						dbltmp = Double.parseDouble(tmpStr);
						meridian.setGreenwichLongitude(new Angle(uom, dbltmp));
					}
				}
			}
			Log.d(LOG_TAG, meridian.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new PrimeMeridian.PrimeMeridianParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new PrimeMeridian.PrimeMeridianParseException(e.getMessage());
		}

		meridian.check();

		return meridian;
	}

	private Ellipsoid parseEllipsoid(XmlPullParser parser)
			throws Ellipsoid.EllipsoidParseException {
		Ellipsoid ell = new Ellipsoid();
		double dbltmp;
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !ELLIPSOID_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (ELLIPSOID_TAG_UPPER.equals(parser.getName())) {
						ell.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (NAME_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						if (!tmpStr.contains(EPSG_TAG)
								&& !tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							ell.setName(tmpStr);
						}
					} else if (SEMIMAJORAXIS_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						dbltmp = Double.parseDouble(tmpStr);
						ell.setA(dbltmp);
					} else if (SEMIMINORAXIS_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						dbltmp = Double.parseDouble(tmpStr);
						ell.setB(dbltmp);
					} else if (INVERSEFLATTENING_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						dbltmp = Double.parseDouble(tmpStr);
						ell.setF(dbltmp);
					} else if (ISSPHERE_TAG.equals(parser.getName())) {
						ell.setSphere();
					}
				}
			}
			Log.d(LOG_TAG, ell.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new Ellipsoid.EllipsoidParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Ellipsoid.EllipsoidParseException(e.getMessage());
		}

		ell.check();

		return ell;
	}

	private CoordinateSystem parseCoordinateSystem(XmlPullParser parser)
			throws CoordinateSystem.CSParseException {
		CoordinateSystem cs = new CoordinateSystem();
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !COORDINATESYSTEM_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (CARTESIANCS_TAG.equals(parser.getName())) {
						cs.setType(CSType.CartesianCS);
						cs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (ELLIPSOIDALCS_TAG.equals(parser.getName())) {
						cs.setType(CSType.EllipsoidalCS);
						cs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (VERTICALCS_TAG.equals(parser.getName())) {
						cs.setType(CSType.VerticalCS);
						cs.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (NAME_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						if (!tmpStr.contains(EPSG_TAG)
								&& !tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							cs.setName(tmpStr);
						}
					} else if (USESAXIS_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						cs.addAxisId(Tools.url2id(tmpStr));
					}
				}
			}
			Log.d(LOG_TAG, cs.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new CoordinateSystem.CSParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new CoordinateSystem.CSParseException(e.getMessage());
		}

		cs.check();

		return cs;
	}

	private Axis parseAxis(XmlPullParser parser) throws Axis.AxisParseException {
		Axis axis = new Axis();
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !AXIS_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (COORDINATESYSTEMAXIS_TAG.equals(parser.getName())) {
						axis.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
						tmpStr = Tools.url2id(parser.getAttributeValue(null,
								UOM_TAG));
						Axis.UoM uom = null;
						for (Axis.UoM it : Axis.UoM.values()) {
							if (it.equals(tmpStr)) {
								uom = it;
							}
						}
						axis.setUom(uom);
					} else if (AXISABBREV_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						axis.setAbbrev(tmpStr);
					}
				}
			}
			Log.d(LOG_TAG, axis.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new Axis.AxisParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Axis.AxisParseException(e.getMessage());
		}

		axis.check();

		return axis;
	}

	private Operation parseOperation(XmlPullParser parser)
			throws Operation.OperationParseException {
		Operation ope = new Operation();
		String tmpStr;
		Double dbltmp;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !OPERATION_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (TRANSFORMATION_TAG.equals(parser.getName())) {
						ope.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
						ope.setType(OperationType.Transformation);
					} else if (CONVERSION_TAG.equals(parser.getName())) {
						ope.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
						ope.setType(OperationType.Conversion);
					} else if (NAME_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						if (!tmpStr.contains(EPSG_TAG)
								&& !tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							ope.setName(tmpStr);
						}
					} else if (WESTBOUNDLONGITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						ope.getBbox().setWest(Double.parseDouble(tmpStr));
					} else if (EASTBOUNDLONGITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						ope.getBbox().setEast(Double.parseDouble(tmpStr));
					} else if (NORTHBOUNDLATITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						ope.getBbox().setNorth(Double.parseDouble(tmpStr));
					} else if (SOUTHBOUNDLATITUDE_TAG.equals(parser.getName())) {
						parser.nextTag();
						tmpStr = parser.nextText();
						ope.getBbox().setSouth(Double.parseDouble(tmpStr));
					} else if (SOURCECRS_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						ope.setSourceCRS(Tools.url2id(tmpStr));
					} else if (TARGETCRS_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						ope.setTargetCRS(Tools.url2id(tmpStr));
					} else if (USESMETHOD_TAG.equals(parser.getName())) {
						tmpStr = parser.getAttributeValue(null, HREF_TAG);
						ope.setMethodId(Tools.url2id(tmpStr));
					} else if (USESVALUE_TAG.equals(parser.getName())) {
						Value val;
						String valueOfParam = null;
						String uom = null;
						double anglesign = 1d;
						double value = 0d;
						while ((eventType = parser.next()) != XmlPullParser.END_TAG
								|| !USESVALUE_TAG.equals(parser.getName())) {
							if (eventType == XmlPullParser.START_TAG) {
								if (VALUEOFPARAMETER_TAG.equals(parser
										.getName())) {
									valueOfParam = Tools.url2id(parser
											.getAttributeValue(null, HREF_TAG));
								} else if (VALUE_TAG.equals(parser.getName())) {
									uom = parser.getAttributeValue(null,
											UOM_TAG);
									value = Double.parseDouble(parser
											.nextText());
								} else if (DEGREES_TAG.equals(parser.getName())) {
									tmpStr = parser.getAttributeValue(null,
											DIRECTION_TAG);
									if (W_TAG.equals(tmpStr)
											|| S_TAG.equals(tmpStr)) {
										anglesign = -1d;
									}
									uom = AngleUoM.DegDec.getUoM();
									dbltmp = Double.parseDouble(parser
											.nextText());
									value = dbltmp;
								} else if (MINUTES_TAG.equals(parser.getName())) {
									dbltmp = Double.parseDouble(parser
											.nextText());
									value += dbltmp / 60d;
								} else if (SECONDES_TAG
										.equals(parser.getName())) {
									dbltmp = Double.parseDouble(parser
											.nextText());
									value += dbltmp / 3600d;
								}

							}
						}
						value *= anglesign;
						AngleUoM auom = AngleUoM.getByUom(uom);
						if (auom == null) {
							val = new DefaultValue(value);
						} else {
							val = new Angle(auom, value);
						}
						ope.addUsesValue(valueOfParam, val);
					}

				}
			}
			Log.d(LOG_TAG, ope.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new Operation.OperationParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Operation.OperationParseException(e.getMessage());
		}

		ope.check();

		return ope;
	}

	private Method parseMethod(XmlPullParser parser)
			throws Method.MethodParseException {
		Method method = new Method();
		String tmpStr;
		try {
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_TAG
					|| !OPERATIONMETHOD_TAG.equals(parser.getName())) {
				if (eventType == XmlPullParser.START_TAG) {
					if (OPERATIONMETHOD_TAG_UPPER.equals(parser.getName())) {
						method.setId("IGNF:"
								+ parser.getAttributeValue(null, ID_TAG));
					} else if (NAME_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						if (!tmpStr.contains(EPSG_TAG)
								&& !tmpStr.contains(REGISTRE_IGN_FR_TAG)) {
							method.setName(tmpStr);
						}
					} else if (SOURCEDIMENSIONS_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						method.setSourceDimensions(Integer.parseInt(tmpStr));
					} else if (TARGETDIMENSIONS_TAG.equals(parser.getName())) {
						tmpStr = parser.nextText();
						method.setTargetDimensions(Integer.parseInt(tmpStr));
					}
				}
			}
			Log.d(LOG_TAG, method.toString());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new Method.MethodParseException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Method.MethodParseException(e.getMessage());
		}

		method.check();

		return method;
	}

	public CRS getCRSById(String id) throws NotFoundException {
		Log.d(LOG_TAG, "getCRSById "+id);
		id = id.toUpperCase();
		CRS crs = null;
		crs = crslist.get(id);
		if (crs != null) {
			Log.d(LOG_TAG, "getCRSById return"+crs);
			return crs;
		}
		for (CRS it : crslist.values()) {
			if (it.getAliases().contains(id)) {
				Log.d(LOG_TAG, "getCRSById return"+it);
				return it;
			}
		}
		throw new NotFoundException("CRS "+id+" non trouvé");
	}
	
	public Datum getDatumById(String id) throws NotFoundException {
		Log.d(LOG_TAG, "getDatumById "+id);
		Datum datum = datumlist.get(id);
		if (datum == null){
			Log.e(LOG_TAG,"Datum "+id+" non trouvé");
			throw new NotFoundException("Datum "+id+" non trouvé");
		}
		return datum;
	}
	
	public Ellipsoid getEllipsoidById(String id) throws NotFoundException {
		Ellipsoid ell = ellipsoidlist.get(id);
		if (ell == null){
			throw new NotFoundException("Ellipsoid "+id+" non trouvé");
		}
		return ell;
	}
	
	public PrimeMeridian getPrimeMeridianById(String id) throws NotFoundException {
		PrimeMeridian meri = meridianlist.get(id);
		if (meri == null){
			throw new NotFoundException("PrimeMeridian "+id+" non trouvé");
		}
		return meri;
	}
	public Axis getAxisById(String id) throws NotFoundException {
		Axis axis = axislist.get(id);
		if (axis == null){
			throw new NotFoundException("Axis "+id+" non trouvé");
		}
		return axis;
	}
	
	public Method getMethodById(String id) throws NotFoundException {
		Method method = methodlist.get(id);
		if (method == null){
			throw new NotFoundException("Method "+id+" non trouvé");
		}
		return method;
	}
	
	public Operation getOperationById(String id) throws NotFoundException {
		Operation operation = operationlist.get(id);
		if (operation == null){
			throw new NotFoundException("Operation "+id+" non trouvé");
		}
		return operation;
		
	}
	
	public CoordinateSystem getCoordinateSystemById(String id) throws NotFoundException {
		CoordinateSystem cs = coordinatesystemslist.get(id);
		if (cs == null){
			throw new NotFoundException("CoordinateSystem "+id+" non trouvé");
		}
		return cs;
		
	}

	public List<SearchResult<CRS>> findCRS(Context context, String search) {
		int minimumlength = 2;
		float minimumscore = 0.1f;
		int maximumCandidate = 100;
		search = search.trim();
		ArrayList<SearchResult<CRS>> results = null;

		if (search.length() < minimumlength) {
			Log.w(LOG_TAG, " - string too small");
			return results;
		}

		results = new ArrayList<SearchResult<CRS>>();

		float score;
		RegistryFTS fts = RegistryFTS.getInstance(context);
		Cursor cursor = fts.search(search);

		if (cursor.getCount() == 0) {
			Log.w(LOG_TAG, " - no result");
			return results;
		}
		cursor.moveToFirst();

		String id = cursor.getString(0);
		score = StringUtils.leven(search, id);
		for (String alias : crslist.get(id).getAliases()) {
			score = Math.max(score, StringUtils.leven(search, alias));
		}
		score *= 2f;
		score += StringUtils.leven(search, crslist.get(id).getName());
		score /= 3f;
		if (score >= minimumscore) {
			results.add(new SearchResult<CRS>(score, crslist.get(id)));
		}

		while (cursor.moveToNext()) {
			id = cursor.getString(0);
			score = StringUtils.leven(search, id);
			for (String alias : crslist.get(id).getAliases()) {
				score = Math.max(score, StringUtils.leven(search, alias));
			}
			score *= 2f;
			score += StringUtils.leven(search, crslist.get(id).getName());
			score /= 3f;
			if (score >= minimumscore) {
				results.add(new SearchResult<CRS>(score, crslist.get(id)));
			}
		}
		for (SearchResult<CRS> res : results){
			Log.v(LOG_TAG, "Result : "+res);
		}

		Collections.sort(results, new Comparator<SearchResult<CRS>>() {

			@Override
			public int compare(SearchResult<CRS> lhs, SearchResult<CRS> rhs) {
				//Log.v(LOG_TAG, "Comparaison de "+lhs+" et "+rhs);
				int compare = (int) (-(lhs.getScore() - rhs.getScore()) * 10000);
				
				//Hack pour éviter le cas score = mais crs !=
				// java.lang.IllegalArgumentException: Comparison method violates its general contract!
				if (compare==0){
					compare += lhs.getItem().getId().compareTo(rhs.getItem().getId());
				}
						
				return compare;
			}

		});

		fts.closeDB();
		while (results.size()>maximumCandidate){
			results.remove(results.size()-1);
		}
		return results;
	}

	public List<CRS> getAllCRS() {
		List<CRS> list = new ArrayList<CRS>();
		for (CRS it : crslist.values()) {
			list.add(it);
		}
		return list;
	}

}
