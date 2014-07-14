package org.circedroid.core.geodesy;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectionReleaseTrigger;
import org.circedroid.core.exception.CompletementConException;
import org.circedroid.core.exception.NotFoundException;
import org.circedroid.core.geodesy.Axis.UoM;
import org.circedroid.core.value.Bbox;

import android.util.Log;

public class CRS extends AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8924310268453114588L;

	/**
	 * Liste des types de CRS.
	 * 
	 * @author thibbo
	 * @see CRS
	 * @see CRS#type
	 */

	public enum CRSType {

		/**
		 * CRS géocentrique
		 */
		GeocentricCRS(),

		/**
		 * CRS géographique
		 */
		GeographicCRS(),

		/**
		 * CRS projetés
		 */
		ProjectedCRS(),

		/**
		 * CRS vertical
		 */
		VerticalCRS(),

		/**
		 * CRS composé
		 */
		CompoundCRS();

	}

	/**
	 * Liste d'identifiant alias
	 */
	private ArrayList<String> aliases;

	/**
	 * Domaine de validité
	 */
	private Bbox bbox;

	/**
	 * Type de CRS
	 */
	private CRSType type;

	/**
	 * Identifiant du datum.
	 */
	private String datumId;

	/**
	 * Identifiant du CRS géographique associé. Uniquement pour les CRS projeté.
	 * 
	 * @see CRSType#ProjectedCRS
	 */
	private String baseGeographicalCRSId;

	/**
	 * Identifiant de la conversion.
	 */
	private String conversionId;

	private String coordinateSystemId;

	private List<String> includedSingleCRSIds;

	public String getDatumId() {
		return datumId;
	}

	public void setDatumId(String datumId) {
		this.datumId = datumId;
	}

	public Datum getDatum() throws NotFoundException {
		Log.d("CRS", "getDatum for " + this);
		if (this.type == CRSType.GeocentricCRS
				|| this.type == CRSType.GeographicCRS) {
			return this.register.getDatumById(this.datumId);
		} else if (this.type == CRSType.ProjectedCRS) {
			return this.register.getCRSById(this.baseGeographicalCRSId)
					.getDatum();
		} else if (this.type == CRSType.CompoundCRS) {
			for (String singleId : this.includedSingleCRSIds) {
				if (this.register.getCRSById(singleId).getType() != CRSType.VerticalCRS) {
					return this.register.getCRSById(singleId).getDatum();
				}
			}
		}
		return null;
	}

	public Datum getVerticalDatum() throws NotFoundException {
		if (this.type == CRSType.VerticalCRS) {
			return this.register.getDatumById(this.datumId);
		} else if (this.type == CRSType.CompoundCRS) {
			for (String singleId : this.includedSingleCRSIds) {
				if (this.register.getCRSById(singleId).getType() == CRSType.VerticalCRS) {
					return this.register.getCRSById(singleId)
							.getVerticalDatum();
				}
			}
		}
		return null;
	}

	public Ellipsoid getEllipsoid() throws NotFoundException {
		return this.register.getEllipsoidById(this.getDatum().getEllipsoidId());
	}

	public PrimeMeridian getPrimeMeridian() throws NotFoundException {
		return this.register.getPrimeMeridianById(this.getDatum()
				.getPrimeMeridianId());
	}

	public String getBaseGeographicalCRSId() {
		return baseGeographicalCRSId;
	}

	public void setBaseGeographicalCRSId(String baseGeographicalCRSId) {
		this.baseGeographicalCRSId = baseGeographicalCRSId;
	}

	public CRS getBaseGeographicalCRS() throws NotFoundException {
		return this.register.getCRSById(this.baseGeographicalCRSId);
	}

	public String getConversionId() {
		return conversionId;
	}

	public void setConversionId(String conversionId) {
		this.conversionId = conversionId;
	}

	public Operation getConversion() throws NotFoundException, CompletementConException {
		if (this.type == CRSType.ProjectedCRS) {
			return this.register.getOperationById(this.conversionId);
		} else if (this.type == CRSType.CompoundCRS) {
			for (String singleId : this.includedSingleCRSIds) {
				if (this.register.getCRSById(singleId).getType() != CRSType.VerticalCRS) {
					return this.register.getCRSById(singleId).getConversion();
				}
			}
		}
		throw new CompletementConException("Not a projected CRS");
	}

	public String getCoordinateSystemId() {
		return coordinateSystemId;
	}

	public void setCoordinateSystemId(String coordinateSystemId) {
		this.coordinateSystemId = coordinateSystemId;
	}
	
	public CoordinateSystem getCoordinateSystem() throws NotFoundException, CompletementConException{
		//TODO
		if (this.type == CRSType.GeocentricCRS || this.type == CRSType.GeographicCRS || this.type == CRSType.ProjectedCRS){
			return this.register.getCoordinateSystemById(this.coordinateSystemId);
		} else if (this.type == CRSType.CompoundCRS){
			for (String singleId : this.includedSingleCRSIds) {
				if (this.register.getCRSById(singleId).getType() != CRSType.VerticalCRS) {
					return this.register.getCRSById(singleId).getCoordinateSystem();
				}
			}
		}
		throw new CompletementConException(this.id+" is a VerticalCS!");
	}
	
	public CoordinateSystem getVerticalCoordinateSystem() throws NotFoundException, CompletementConException{
		//TODO
		if (this.type == CRSType.VerticalCRS){
			return this.register.getCoordinateSystemById(this.coordinateSystemId);
		} else if (this.type == CRSType.CompoundCRS){
			for (String singleId : this.includedSingleCRSIds) {
				if (this.register.getCRSById(singleId).getType() == CRSType.VerticalCRS) {
					return this.register.getCRSById(singleId).getCoordinateSystem();
				}
			}
		}
		throw new CompletementConException(this.id+" is not a VerticalCS!");
	}
	public List<Axis> getAxis() throws NotFoundException, CompletementConException{
		ArrayList<Axis> axis = new ArrayList<>();
		if (this.type != CRSType.CompoundCRS){
			if (this.type != CRSType.VerticalCRS){
				for (String id : this.getCoordinateSystem().getAxisIds()){
					axis.add(this.register.getAxisById(id));
				}
				return axis;
			} else {
				axis.add(this.register.getAxisById(this.getVerticalCoordinateSystem().getAxisIds().get(0)));
				return axis;
			}
		} else {
			CRS horiCRS = null;
			CRS vertCRS = null;
			for (String id : this.getIncludedSingleCRSIds()){
				CRS crs = this.register.getCRSById(id);
				if (crs.getType() != CRSType.VerticalCRS){
					horiCRS = crs;
				} else {
					vertCRS = crs;
				}
			}
			if (horiCRS != null){
				for (Axis maxis : horiCRS.getAxis()){
					axis.add(maxis);
				}
			}
			if (vertCRS != null){
				for (Axis maxis : vertCRS.getAxis()){
					axis.add(maxis);
				}
			}
			return axis;
		}
	}
	
	public UoM getUoM() throws CompletementConException{
		if (this.type == CRSType.GeocentricCRS || this.type == CRSType.GeographicCRS || this.type == CRSType.ProjectedCRS){
			
		}
		throw new CompletementConException(this.id+" is a VerticalCS!");
	}

	public CRS() {
		bbox = new Bbox();
		aliases = new ArrayList<>();
		includedSingleCRSIds = new ArrayList<String>();

	}

	public ArrayList<String> getAliases() {
		return aliases;
	}

	public void addAlias(String alias) {
		this.aliases.add(alias);
	}

	public CRSType getType() {
		return type;
	}

	public void setType(CRSType type) {
		this.type = type;
	}

	public Bbox getBbox() {
		return bbox;
	}

	public List<String> getIncludedSingleCRSIds() {
		return includedSingleCRSIds;
	}

	public void addIncludedSingleCRSId(String includedSingleCRSId) {
		this.includedSingleCRSIds.add(includedSingleCRSId);
	}

	@Override
	public String toString() {
		return "CRS [id=" + id + ", aliases=" + aliases + ", bbox=" + bbox
				+ ", name=" + name + ", type=" + type + ", datumId=" + datumId
				+ ", baseGeographicalCRSId=" + baseGeographicalCRSId
				+ ", conversionId=" + conversionId + "]";
	}

	public static class CRSParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3037139714847314945L;

		public CRSParseException(String message) {
			super(message);
		}
	}

	public void check() throws CRSParseException {
		if (null == id) {
			throw new CRSParseException("Id null" + this);
		}
		if (null == name) {
			throw new CRSParseException("Name null" + this);
		}
		if (null == type) {
			throw new CRSParseException("Type null" + this);
		}
		if ((type == CRSType.GeocentricCRS || type == CRSType.GeographicCRS || this.type == CRSType.VerticalCRS)
				&& null == datumId) {
			throw new CRSParseException("Datum null" + this);
		}
		if (type == CRSType.ProjectedCRS && null == baseGeographicalCRSId) {
			throw new CRSParseException("BaseGeographicalCRS null" + this);
		}
		if (type == CRSType.ProjectedCRS && null == conversionId) {
			throw new CRSParseException("Conversion null" + this);
		}
		if (type == CRSType.CompoundCRS && null == includedSingleCRSIds) {
			throw new CRSParseException("includedSingleCRSIds null" + this);
		}
		if (type == CRSType.CompoundCRS && includedSingleCRSIds.size() == 0) {
			throw new CRSParseException("includedSingleCRSIds empty" + this);
		}
		if ((type != CRSType.CompoundCRS)
				&& null == this.coordinateSystemId) {
			throw new CRSParseException("coordinateSystemId null" + this);
		}
	}

}
