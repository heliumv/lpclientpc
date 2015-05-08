package com.lp.client.util;

import java.awt.Color;
import java.util.ResourceBundle;

public class ClientConfiguration {
	private static ResourceBundle cfgBundle = null;
	private static final String CONFIG_BUNDLE_LP = "com.lp.client.res.lpconfig";

	private static ResourceBundle versionBundle = null ;
	private static final String VERSION_BUNDLE_LP = "com.lp.client.res.lp";
	
	private static ResourceBundle config() {
		if (cfgBundle == null) {
			cfgBundle = ResourceBundle.getBundle(CONFIG_BUNDLE_LP);
		}
		return cfgBundle ;
	}

	private static ResourceBundle version() {
		if (versionBundle == null) {
			versionBundle = ResourceBundle.getBundle(VERSION_BUNDLE_LP);
		}
		return versionBundle ;
	}
	
	private static Color toColor(String s) {
		int iColor = Integer.parseInt(s);
		return new Color(iColor);
	}
	
	/**
	 * Hintergrundfarbe nicht editierbarer Felder
	 * @return die Hintergrundfarbe von nichteditierbaren Feldern
	 */
	public static String getNotEditableColorString() {
		return config().getString("ui.controls.color.noteditable") ;
	}
	
	public static Color getNotEditableColor() {
		try {
			return toColor(getNotEditableColorString()) ;
		} catch(Throwable t) {
			return new Color(240, 240, 240) ;  // default: ein helles grau
		}
	}
	
	/**
	 * Hintergrundfarbe editierbarer Felder
	 * 
	 * @return die konfigurierte Farbe falls auswertbar, ansonsten weiss
	 */
	public static Color getEditableColor() {
		try {
			return toColor(getEditableColorString()) ;
		} catch(Throwable e) {
			return new Color(255, 255, 255) ; // default weiss
		}
	}
	
	/**
	 * Hintergrundfarbe editierbarer Felder
	 * 
	 * @return die konfigurierte Hintergrundfarbe editierbarer Felder oder null wenn nicht vorhanden
	 */
	public static String getEditableColorString() {
		return config().getString("ui.controls.color.editable") ;
	}
	
	
	/**
	 * Die Farbe wenn das Feld den Focus hat
	 * 
	 * @return die konfigurierte Farbe falls auswertbar, ansonsten weiss
	 */
	public static Color getColorOnFocus() {
		try {
			return toColor(getColorOnFocusString()) ;
		} catch(Throwable e) {
			return new Color(255, 255, 255);			
		}
	}
	
	public static String getColorOnFocusString() {
		return config().getString("color.TextField.onfocus") ;		
	}
	
	public static Color getInactiveForegroundColor() {
		return toColor(getInactiveForegroundColorString()) ;
	}
	
	public static String getInactiveForegroundColorString() {
		return config().getString("color.TextField.inactiveForeground") ;
	}
	
	/**
	 * Die Breite des InternalFrame
	 * @return die Breite des InternalFrame bzw. Default wenn nicht vorhanden
	 */
	public static Integer getInternalFrameSizeWidth() {
		try {
			return Integer.parseInt(config().getString("internalframe.size.x")) ;
		} catch(Throwable t) {
			return 700 ;
		}
	}

	/**
	 * H&ouml;he des InternalFrame
	 * 
	 * @return die H&ouml;he des InternalFrame bzw. Default wenn nicht gesetzt
	 */
	public static Integer getInternalFrameSizeHeight() {
		try {
			return Integer.parseInt(config().getString("internalframe.size.y")) ;
		} catch(Throwable t) {
			return 600 ;
		}
	}
	
	public static String getUiFontName() {
		return config().getString("ui.font.name") ;
	}

	public static String getUiFontStyle() {
		return config().getString("ui.font.style") ;
	}
	
	public static String getUiFontSize() {
		return config().getString("ui.font.size") ;
	}
	
	public static Integer getUiControlHeight() {
		return Integer.parseInt(config().getString("ui.controls.height")) ;
	}
	
	/**
	 * Report Zoom-Faktor
	 * @throws NullPointerException wenn der Wert nicht konfiguriert ist
	 * @throws NumberFormatException wenn der konfigurierte Wert nicht parseable ist
	 * @return den konfigurierten ZoomFaktor. 
	 */
	public static int getReportZoom() {
		return Integer.parseInt(config().getString("report.zoom")) ;
	}
	
	/**
	 * Die Rahmenfarbe eines Mandatory Fields 
	 * 
	 * @param colorVision
	 * @return die entsprechende Rahmenfarbe sofern konfiguriert, ansonsten rot
	 */
	public static Color getMandatoryFieldBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.mandatoryfield.border." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(207, 0, 104) ;
		}
	}
	
	public static String getLogpScan() {
		return config().getString("doc.logpscantool") ;
	}
	
	public static String getQuickLoginUserName() {
		return config().getString("benutzer") ;
	}
	
	public static String getQuickLoginPassword() {
		return config().getString("kennwort") ;
	}
	
	
	public static String getUiSprLocale() {
		return config().getString("locale.uisprache") ;
	}
	
	/**
	 * Der Benutzername des Admin-Benutzers
	 * @return der Name (Account-Name) des Admin-Users
	 */
	public static String getAdminUsername() {
		return config().getString("user.admin") ;
	}
		
	/**
	 * Die Client Buildnummer
	 * @return die Buildnummer
	 */
	public static Integer getBuildNumber() {
		return Integer.parseInt(version().getString("lp.version.client.build")) ;
	}
	
	/**
	 * Die Client Versionsnummer
	 * @return die Versionsnummer
	 */
	public static String getVersion() {
		return version().getString("lp.version.client") ;
	}
	
	/**
	 * Die Client Modul Nummer, also 11, oder 12
	 * @return die Client Modulnummer
	 */
	public static String getModul() {
		return version().getString("lp.version.client.modul") ;
	}
	
	public static String getBugfixNr() {
		return version().getString("lp.version.client.bugfix") ;
	}
}
