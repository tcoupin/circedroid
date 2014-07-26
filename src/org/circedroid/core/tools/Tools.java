package org.circedroid.core.tools;

import org.circedroid.core.register.Register;

public class Tools {

	
	/**
	 * Convertit une url du registre IGNF en identifiant
	 *     Ex : url=http://registre.ign.fr/ign/IGNF/crs/IGNF/LAMB93
	 *          return=IGNF:LAMB93
	 * @param url
	 * @return
	 */
	private static String ignurl2id(String url){
		if (null == url){
			return null;
		}
		String id;
		String[] splits = url.split("/");
		id = splits[splits.length-1];
		splits = id.split("#");
		id = "IGNF:"+splits[0];
		return id;
	}
	
	private static String epsgurl2id(String url){
		if (null == url){
			return null;
		}
		String id;
		String[] splits = url.split(":");
		id = "EPSG:"+splits[splits.length-1];
		return id;
	}
	
	public static String url2id(String url){
		if (null == url){
			return null;
		}
		if (url.contains("registre.ign.fr")){
			return ignurl2id(url);
		} else if (url.contains("EPSG")){
			return epsgurl2id(url);
		} else {
			return null;
		}
	}
}
