/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.frame;

import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.border.Border;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TextDto;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um Defaultwerte im LP5-Client</I>
 * </p>
 * <p>
 * Hier konnen dem Clientframwork Daten, Vorbelegungen wie zB. das Locale aller
 * aller Componenten, gesetzt werden.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>08.03.05</I>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.11 $
 */
public class Defaults {
	private Locale locUI = null;
	private int iControlHeight = 20;
	public static final int LOG_LENGTH = 100;
	private BetriebskalenderDto[] betriebsurlaub = null;
	private HashMap<?, ?> mapPersonalKurzzeichen = null;
	private HashMap<?, ?> mapStatiUebersetzt = null;
	private Map<Timestamp, String> mapFeiertage = null;
	private HashMap<String, String> mapTexte = null;

	private String toolTipLeeren = "";
	private String sComboboxEmptyEntry = "<LEER>";
	private String sResourceBundleAllgemein = null;

	public String usernameAusBatch = null;
	public String passwordAusBatch = null;
	public String localeAusBatch = null;
	public double sizeFactor = 1.0;
	
	private boolean loadLayoutOnLogon = true;

	private Border mandatoryFieldBorder = null;

	static private Defaults defaults = null;

	private int iUINachkommastellenPreise = -1; // MB: wird erst bei bedarf
												// initialisiert.
	private int iUINachkommastellenPreiseEK = -1; // MB: wird erst bei bedarf
	
	private int iUINachkommastellenPreiseWE = -1; // MB: wird erst bei bedarf
	// initialisiert.
	private int iUINachkommastellenPreiseVK = -1; // MB: wird erst bei bedarf
	// initialisiert.
	private int iUINachkommastellenMenge = -1; // MB: wird erst bei bedarf
												// initialisiert.

	// Das Recht "Darf Einkaufs-Preise sehen" gilt clientseitig fuer die
	// Lebensdauer des InternalFrames.
	private boolean bRechtDarfRechteSehen = false;

	// Componenten werden benannt, oder nicht (fuer Testumgebung und Direkthilfe)
	private boolean bComponentNamingEnabled = true;

	// Verbose logging. -> z.B.: Serverzugriffe
	private boolean bVerbose = false;
	
	// GUI-Constraints zur Laufzeit bearbeitbar
	private boolean bDebugGUI = false;

	// Componenten werden nach altem Schema benannt
	// (deprecated, aber noch notwendig fuer alte Tests.
	// Wird an der stelle gesetzt, wo frueher das abbot-file
	// abgefragt wurde.
	private boolean bOldComponentNamingEnabled = false;

	// Wert ob Background verwendet oder nicht
	private boolean bBackgroundEnabled = true;

	private String uebersteuerterMandant = null;

	/**
	 * Helium Desktop maximiert darstellen?
	 */
	private boolean maximized = false;
	
	private boolean showIIdColumn = false;
	
	private String colorVision = HelperClient.COLOR_NORMAL;
	
	private boolean useWaitCursor = true ;
	private boolean refreshTitle = true ;
	
	public String getUebersteuerterMandant() {
		return uebersteuerterMandant;
	}

	public void setUebersteuerterMandant(String uebersteuerterMandant) {
		this.uebersteuerterMandant = uebersteuerterMandant;
	}

	// default LookAndFeel -> ueber Parameter gesetzt
	private String defaultLookAndFeel = null;
	private boolean dirkethilfeEnabled = true;
	private boolean dirkethilfeVisible = true;

	/**
	 * @todo JO 5.8.05; kommen bald wieder raus!
	 */
	static public long lUhrQuickDirtyBS = 0;
	static public long lUhrQuickDirtyART = 0;

	private Defaults() {
		mapFeiertage = new HashMap<Timestamp, String>() ;
	}
	
	/**
	 * Hole das Singelton Defaults.
	 * 
	 * @return Defaults
	 */
	static public Defaults getInstance() {
		if (defaults == null) {
			defaults = new Defaults();
		}
		return defaults;
	}

	public Locale getLocUI() {
		return locUI;
	}

	public boolean getBackground() {
		return bBackgroundEnabled;
	}

	public void setBackground(boolean backgroundEnabled) {
		this.bBackgroundEnabled = backgroundEnabled;
	}

	public Map<Timestamp, String> getFeiertage() {
		return mapFeiertage;
	}

	public BetriebskalenderDto[] getBetriebsurlaub() {
		return betriebsurlaub;
	}

	public String getToolTipLeeren() {
		return toolTipLeeren;
	}

	public String getSComboboxEmptyEntry() {
		return sComboboxEmptyEntry;
	}

	public void setLocUI(Locale locUI) {
		this.locUI = locUI;
	}

	public void setFeiertage(BetriebskalenderDto[] feiertage) {
		mapFeiertage = new HashMap<Timestamp, String>();
		for(BetriebskalenderDto dto:feiertage) {
			mapFeiertage.put(dto.getTDatum(), dto.getCBez());
		}
	}

	public void setBetriebsurlaub(BetriebskalenderDto[] betriebsurlaub) {
		this.betriebsurlaub = betriebsurlaub;
	}

	public void setToolTipLeeren(String toolTipLeeren) {
		this.toolTipLeeren = toolTipLeeren;
	}

	public void setSComboboxEmptyEntry(String sComboboxEmptyEntry) {
		this.sComboboxEmptyEntry = sComboboxEmptyEntry;
	}

	public int getControlHeight() {
		return iControlHeight;
	}

	public void setControlHeight(int iControlHeight) {
		this.iControlHeight = iControlHeight;
	}
	
	public double getSizeFactor() {
		return sizeFactor;
	}

	public int bySizeFactor(int original) {
		return (int)(sizeFactor*original);
	}

	public Dimension bySizeFactor(Dimension original) {
		return bySizeFactor(original.width, original.height);
	}
	
	public Dimension bySizeFactor(int width, int height) {
		return new Dimension(bySizeFactor(width),bySizeFactor(height));
	}
	
	public void setSizeFactor(double sizeFactor) {
		this.sizeFactor = sizeFactor;
	}

	public String getResourceBundleRespectUILocale() {
		return sResourceBundleAllgemein + "_" + getLocUI().toString();
	}

	public void setSResourceBundleAllgemein(String sResourceBundleAllgemein) {
		this.sResourceBundleAllgemein = sResourceBundleAllgemein;
	}

	public void setVerbose(boolean bVerbose) {
		this.bVerbose = bVerbose;
	}

	public boolean isVerbose() {
		return bVerbose;
	}

	public void setOldComponentNamingEnabled(boolean bOldComponentNamingEnabled) {
		this.bOldComponentNamingEnabled = bOldComponentNamingEnabled;
	}

	public boolean isOldComponentNamingEnabled() {
		return bOldComponentNamingEnabled;
	}

	public void setComponentNamingEnabled(boolean bComponentNamingEnabled) {
		this.bComponentNamingEnabled = bComponentNamingEnabled;
	}

	public boolean isComponentNamingEnabled() {
		return bComponentNamingEnabled;
	}

	public void setDefaultLookAndFeel(String defaultLookAndFeel) {
		this.defaultLookAndFeel = defaultLookAndFeel;
	}

	public String getDefaultLookAndFeel() {
		return defaultLookAndFeel;
	}

	public void reloadPersonalKurzzeichen() throws Throwable {
		mapPersonalKurzzeichen = DelegateFactory.getInstance()
				.getPersonalDelegate().getAllKurzzeichen();
	}

	public void reloadStati() throws Throwable {
		mapStatiUebersetzt = DelegateFactory.getInstance().getSystemDelegate()
				.getAllStatiMitUebersetzung();
	}

	public String getPersonalKurzzeichen(Integer personalIId) throws Throwable {
		String sKurzzeichen = null;
		if (personalIId != null) {
			// Map laden, falls noch nicht vorhanden
			if (mapPersonalKurzzeichen == null) {
				reloadPersonalKurzzeichen();
			}
			// maximal 2 Versuche, da die Map evtl. neu geladen werden muss
			for (int i = 0; i < 2; i++) {
				sKurzzeichen = (String) mapPersonalKurzzeichen.get(personalIId);
				// wenn der in der map nicht vorhanden ist, dann map neu laden
				if (sKurzzeichen == null) {
					reloadPersonalKurzzeichen();
				} else {
					// ansonsten abbruch
					break;
				}
			}
		}
		return sKurzzeichen;
	}

	public String getStatusUebersetzt(String statusCNr) throws Throwable {
		String sUebersetzung = null;
		if (statusCNr != null) {
			// Map laden, falls noch nicht vorhanden
			if (mapStatiUebersetzt == null) {
				reloadStati();
			}
			// maximal 2 Versuche, da die Map evtl. neu geladen werden muss
			for (int i = 0; i < 2; i++) {
				sUebersetzung = (String) mapStatiUebersetzt.get(statusCNr);
				// wenn der in der map nicht vorhanden ist, dann map neu laden
				if (sUebersetzung == null) {
					reloadStati();
				} else {
					// ansonsten abbruch
					break;
				}
			}
		}
		return sUebersetzung;
	}
	
	

	public Border getMandatoryFieldBorder() {
		if (mandatoryFieldBorder == null) {
			mandatoryFieldBorder = new javax.swing.border.LineBorder(
					HelperClient.getMandatoryFieldBorderColor(getColorVision()));
		}
		return mandatoryFieldBorder;
	}
	
	public String getSpezifischenText(String sToken) throws Throwable {
		String sTexte = null;
		if (sToken != null) {
			// Map laden, falls noch nicht vorhanden
			if (mapTexte == null) {
				reloadSpezifischeTexte();
			}
			sTexte = mapTexte.get(sToken);
		}
		return sTexte;
	}

	/**
	 * Neu initialisieren. z.b. bei Mandantenwechsel.
	 * 
	 * @throws Throwable
	 */
	public void initialize() throws Throwable {
		reloadSpezifischeTexte();
		iUINachkommastellenPreise = -1;
		iUINachkommastellenMenge = -1;
		// Benutzerrecht "Darf Verkaufs-Preise sehen" holen.
		bRechtDarfRechteSehen = DelegateFactory.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_RECHTE_SEHEN);
	}

	public void reloadSpezifischeTexte() throws Throwable {
		mapTexte = new HashMap<String, String>();
		if (LPMain.getTheClient() != null) {
			TextDto[] texte = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.textFindByMandantCNrLocaleCNr(
							LPMain.getTheClient().getMandant(),
							LPMain.getTheClient().getLocUiAsString());
			for (int i = 0; i < texte.length; i++) {
				mapTexte.put(texte[i].getCToken(), texte[i].getCText());
			}
		}
	}

	public final int getIUINachkommastellenPreiseAllgemein() throws Throwable {
		if (iUINachkommastellenPreise < 0) {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN);
			iUINachkommastellenPreise = ((Integer) parameter.getCWertAsObject())
					.intValue();
		}
		return iUINachkommastellenPreise;
	}

	public final int getIUINachkommastellenPreiseVK() throws Throwable {
		if (iUINachkommastellenPreiseVK < 0) {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
			iUINachkommastellenPreiseVK = ((Integer) parameter.getCWertAsObject())
					.intValue();
		}
		return iUINachkommastellenPreiseVK;
	}

	public final int getIUINachkommastellenPreiseEK() throws Throwable {
		if (iUINachkommastellenPreiseEK < 0) {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK);
			iUINachkommastellenPreiseEK = ((Integer) parameter.getCWertAsObject())
					.intValue();
		}
		return iUINachkommastellenPreiseEK;
	}

	public final int getIUINachkommastellenPreiseWE() throws Throwable {
		if (iUINachkommastellenPreiseWE < 0) {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_WE);
			iUINachkommastellenPreiseWE = ((Integer) parameter.getCWertAsObject())
					.intValue();
		}
		return iUINachkommastellenPreiseWE;
	}

	public final int getIUINachkommastellenMenge() throws Throwable {
		if (iUINachkommastellenMenge < 0) {
			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_MENGE_UI_NACHKOMMASTELLEN);
			iUINachkommastellenMenge = ((Integer) parameter.getCWertAsObject())
					.intValue();
		}
		return iUINachkommastellenMenge;
	}

	public boolean darfRechteSehen() {
		return bRechtDarfRechteSehen;
	}

	/**
	 * Wird der Helium Desktop maximiert dargestellt?
	 * 
	 * @return true/false
	 */
	public boolean isMaximized() {
		return maximized;
	}

	/**
	 * Den Helium V Desktop maximiert darstellen?
	 * 
	 * @param maximized
	 */
	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	public void setDirekthilfeEnabled(boolean b) {
		this.dirkethilfeEnabled  = b;
	}
	
	public boolean isDirekthilfeEnabled() {
		return this.dirkethilfeEnabled;
	}

	public boolean getLoadLayoutOnLogon() {
		return loadLayoutOnLogon;
	}

	public void setLoadLayoutOnLogon(boolean loadLayoutOnLogon) {
		this.loadLayoutOnLogon = loadLayoutOnLogon;
	}

	public boolean isShowIIdColumn() {
		return showIIdColumn;
	}

	public void setShowIIdColumn(boolean showIIdColumn) {
		this.showIIdColumn = showIIdColumn;
	}

	public String getColorVision() {
		return colorVision;
	}

	public void setColorVision(String colorVision) {
		mandatoryFieldBorder = null;
		this.colorVision = colorVision == null ? HelperClient.COLOR_NORMAL : colorVision;
	}

	public boolean isbDebugGUI() {
		return bDebugGUI;
	}

	public void setbDebugGUI(boolean bDebugGUI) {
		this.bDebugGUI = bDebugGUI;
	}

	public boolean isUseWaitCursor() {
		return useWaitCursor;
	}

	public void setUseWaitCursor(boolean useWaitCursor) {
		this.useWaitCursor = useWaitCursor;
	}

	public boolean isRefreshTitle() {
		return refreshTitle;
	}

	public void setRefreshTitle(boolean refreshTitle) {
		this.refreshTitle = refreshTitle;
	}

	public boolean isDirekthilfeVisible() {
		return dirkethilfeVisible;
	}

	public void setDirekthilfeVisible(boolean dirkethilfeVisible) {
		this.dirkethilfeVisible = dirkethilfeVisible;
	}
}
