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
package com.lp.client.pc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.FocusManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.lp.client.frame.Command;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.component.HvLayoutFocusTraversalPolicy;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.LogonDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.jms.InfoTopic;
import com.lp.client.jms.TopicFert;
import com.lp.client.jms.TopicGf;
import com.lp.client.jms.TopicManage;
import com.lp.client.jms.TopicQs;
import com.lp.client.util.ClientConfiguration;
import com.lp.client.zeiterfassung.f630.ZETimer;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

//@SuppressWarnings("static-access")
/*
 * <P> <b>frame</b>.<BR> Hauptprogramm der Lp-Anwendung, welches ein
 * Anmeldefenster anzeigt. <BR> Singelton. <BR> Fungiert als TheApp. <BR> </P>
 * <p>Erstellungsdatum <I>28.06.2001</I></p>
 * 
 * @version $Revision: 1.126 $
 * 
 * @author Josef Ornetsmueller
 */
public class LPMain implements ICommand {
	// private static LPQueueListener lpqueue = null;

//	protected LpLogger myLogger = null;
	protected Logger myLogger = null;

	// kommt von den Resourcebundels
	private Vector<Locale> vecOfLocUI = null;
	// kommt aus dem lp.properties
	private Locale locUISprache = null;

	private ResourceBundle messagesBundel = null;
//	private ResourceBundle lpBundel = null;
	private Desktop desktop = null;
	private String cNrUser = null;
	private TheClientDto theClientDto = null;
	private String benutzernameRaw = null;
	private char[] kennwortRaw = null;
	private String mandantRaw = null;

	static private LPMain lPMain = null;

	// nur hier stehen fuer den ganzen lpclientpc die Pfade, Dateiname, ...
	static final private String FS = File.separator;
	static final private String DIR_RESOURCE = "." + FS + "classes" + FS
			+ "com" + FS + "lp" + FS + "client" + FS + "res";
	static final private String FILE_NAME_RESOURCE = "messages";
	static final public String RESOURCE_BUNDEL_ALLG = "com.lp.client.res.messages";

	static final public String RESOURCE_BUNDEL_ZESTIFTE = "com.lp.client.zeiterfassung.f630.zestifte";

//	static final private String RESOURCE_BUNDEL_LP = "com.lp.client.res.lp";
	static private String XMLFILEPATH = "com" + FS + "lp" + FS + "client" + FS
			+ "frame" + FS + "stammdatencrud";

	static private Date S_VERSION_HV_EXPIRES = null;
	static private String S_VERSION_HV_MODULNR = null;
	static private String S_VERSION_HV_BUGFIXNR = null;
	static private String S_VERSION_HV_BUILDNR = null;
	static private String S_VERSION_HV = null;
	static private String S_VERSION_HV_ALLTOGETHER = null;

	private Vector<String> vecOfMandanten = null;
	/**
	 * AGILPRO CHANGES BEGIN
	 * 
	 * @author Lukas Lisowski
	 */
	private boolean AGILPRO = false;
	private static PasteBuffer pasteBuffer = null;
	private static InfoTopicBuffer infoTopicBuffer = null;
	private InfoTopic infoTopic = null;
	private TopicQs topicQS = null;
	private TopicFert topicFert = null;
	private TopicManage topicManage = null;
	private TopicGf topicGf = null;

	private String lastImportDirectory = null;

	private Boolean lpadmin;

	private LPMessages errorMessageHandler = null ;
	
	private static Logger Log = Logger.getLogger(LPMain.class) ;
	
	
	private IDesktopController desktopController ;
	
	public String getLastImportDirectory() {
		return lastImportDirectory;
	}

	public void setLastImportDirectory(String directory) {
		lastImportDirectory = directory;
	}

	/**
	 * @return the aGILPRO
	 */
	public boolean isAGILPRO() {
		return AGILPRO;
	}

	/**
	 * @param pAgilpro
	 *            the aGILPRO to set
	 */
	public void setAGILPRO(boolean pAgilpro) {
		AGILPRO = pAgilpro;
	}

	/**
	 * AGILPRO CHANGES END
	 */

	private LPMain() {
		// Singelton
//		myLogger = (LpLogger) com.lp.client.util.logger.LpLogger.getInstance(this.getClass());
		myLogger = Logger.getLogger(this.getClass()) ;
		desktopController = new DesktopController() ;
		
		try {
			getUILocales();
		} catch (Throwable ex) {
			exitClientNowErrorDlg(ex);
		}
	}

	public void setDesktopController(IDesktopController desktopController) {
		this.desktopController = desktopController ;
	}

	public IDesktopController getDesktopController() {
		return desktopController ;
	}
	
	/**
	 * Hole das Singelton LPMain.
	 * 
	 * @return LPMain
	 */
	static public LPMain getInstance() {
		if (lPMain == null) {
			lPMain = new LPMain();
		}
		return lPMain;
	}

	private static void setSystemPropertyFromJnlp(String argumentString) {
		int index = argumentString.indexOf("=") ;
		String key = argumentString.substring(0, index) ;
		String value = argumentString.substring(index + 1) ;

		Log.debug("Setting SystemProperty '" + key + "' to value '" + value + "'") ;
		System.setProperty(key, value) ;
	}
	
	private static void setSystemProperties() {
		setSystemPropertyImpl("jnlp.java.naming.provider.url");
		setSystemPropertyImpl("jnlp.java.naming.factory.initial");
	}

	private static void setSystemPropertyImpl(String key) {
		String value = System.getProperty(key) ;
		Log.debug("Getting property'" + key + "' with value '" + value + "'") ;		
		if(value != null) {
			setSystemPropertyFromJnlp(key.substring(5) + "=" + value);
		}
	}
	
	
	/**
	 * The App: ClientPC. Pleased to meet you.
	 * 
	 * @param args
	 *            String[]
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {

		Log.debug("Starting main with '" + args.length + "' parameters ...") ;
		
		// read application parameters
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				Log.debug("Param " + i + " [" + args[i] + "]") ;

				// neue Komponenten-Benennung?
				if (args[i].startsWith("--enable-component-naming")) {
					String s = args[i];
					boolean enabled = true;
					if(s.indexOf("=") > -1) {
						s = s.substring(s.indexOf("=") + 1);
						enabled = !s.equals("0");
					}
					Defaults.getInstance().setComponentNamingEnabled(enabled);
				}

				// Verbose Logging?
				else if (args[i].equalsIgnoreCase("--verbose")) {
					Defaults.getInstance().setVerbose(true);
				}

				else if(args[i].startsWith("--jnlp.")) {
					setSystemPropertyFromJnlp(args[i].substring(7)) ;
				}
				
				// System - Look and feel
				else if (args[i].equalsIgnoreCase("--laf-system")) {
					Defaults.getInstance().setDefaultLookAndFeel(
							javax.swing.UIManager
									.getSystemLookAndFeelClassName());
				}

				// Cross-platform LookAndFeel
				else if (args[i].equalsIgnoreCase("--laf-java")) {
					Defaults.getInstance().setDefaultLookAndFeel(
							javax.swing.UIManager
									.getCrossPlatformLookAndFeelClassName());
				}

				// Kunststoff LookAndFeel
				else if (args[i].equalsIgnoreCase("--laf-kunststoff")) {
					Defaults.getInstance().setDefaultLookAndFeel(
							Desktop.LAF_CLASS_NAME_KUNSTSTOFF);
				}
				// Hintergrundbild deaktivieren
				else if (args[i].equalsIgnoreCase("--disable-background")) {
					Defaults.getInstance().setBackground(false);
				}

				else if (args[i].startsWith("--mandant")) {
					String s = args[i];
					s = s.substring(s.indexOf("=") + 1);

					Defaults.getInstance().setUebersteuerterMandant(s);
				} else if (args[i].startsWith("--username")) {
					String s = args[i];
					s = s.substring(s.indexOf("=") + 1);

					Defaults.getInstance().usernameAusBatch = s;
				} else if (args[i].startsWith("--password")) {
					String s = args[i];
					s = s.substring(s.indexOf("=") + 1);

					Defaults.getInstance().passwordAusBatch = s;
				} else if (args[i].startsWith("--nolayout")) {

					Defaults.getInstance().setLoadLayoutOnLogon(false);
				} else if (args[i].startsWith("--locale")) {
					String s = args[i];
					s = s.substring(s.indexOf("=") + 1);

					Defaults.getInstance().localeAusBatch = s;
				} else if (args[i].equalsIgnoreCase("--maximized")) {
					Defaults.getInstance().setMaximized(true);
				} else if (args[i].startsWith("--enabledirekthilfe")) {
//					String s = args[i];
//					s = s.substring(s.indexOf("=") + 1);
//					boolean enabled = !s.equals("0");
//					Defaults.getInstance().setDirekthilfeEnabled(enabled);
					Defaults.getInstance().setDirekthilfeEnabled(
							getBooleanFromArgs(args[i], true));
				} else if (args[i].startsWith("--showiids")) {
					Defaults.getInstance().setShowIIdColumn(true);
				} else if (args[i].startsWith("--debuggui")) {
					Defaults.getInstance().setbDebugGUI(true);
				} else  if(args[i].startsWith("--refreshtitle")) {
					Defaults.getInstance().setRefreshTitle(
							getBooleanFromArgs(args[i], true)) ;					
				} else if(args[i].startsWith("--usewaitcursor")) {
					Defaults.getInstance().setUseWaitCursor(
							getBooleanFromArgs(args[i], true)) ;					
				}
			}
		}

		setSystemProperties() ;
		
		try {
			
			boolean bLafSet = false;

			
			if (Defaults.getInstance().getDefaultLookAndFeel() != null) {
				try {
					javax.swing.UIManager.setLookAndFeel(Defaults.getInstance()
							.getDefaultLookAndFeel());
					bLafSet = true;
				} catch (Exception ex) {
					System.out.println("LookAndFeel "
							+ Defaults.getInstance().getDefaultLookAndFeel()
							+ "konnte nicht gesetzt werden.");
				}
			}

			if (!bLafSet) {
				javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
						.getSystemLookAndFeelClassName());
			}
			if(UIManager.getLookAndFeel() instanceof WindowsLookAndFeel) {
				((WindowsLookAndFeel)UIManager.getLookAndFeel()).setMnemonicHidden(false); 
			}
			// lp.version.client = HELIUM V 5
			setSVersionHV(ClientConfiguration.getVersion());
			// lp.version.client.modul = 09
			setVersionHVModulnr(ClientConfiguration.getModul()) ;
			// lp.version.client.bugfix = 004
			setVersionHVBugfixnr(ClientConfiguration.getBugfixNr());
			try {
				// lp.version.client.build = 2415
				setVersionHVBuildnr(ClientConfiguration.getBuildNumber().toString());
			} catch (NumberFormatException ex2) {
				// nothing here
			}
			S_VERSION_HV_ALLTOGETHER = getSVersionHV() + " "
					+ getVersionHVModulnr() + " " + getVersionHVBugfixnr()
					+ " " + getVersionHVBuildnr();

			getInstance().getUISprLocaleFromResource();

			installAutoLogOffHandler();

			// wennzestifte.properties vorhanden, dann wird Client im
			// ZE-Stift-Import-Modus gestartet
			try {
				ResourceBundle zestiftresource = ResourceBundle
						.getBundle(RESOURCE_BUNDEL_ZESTIFTE);

				String benutzer = zestiftresource.getString("zestift.username");
				String password = zestiftresource.getString("zestift.password");
				TheClientDto theClientDto = DelegateFactory
						.getInstance()
						.getLogonDelegate()
						.logon(benutzer + LogonFac.USERNAMEDELIMITER
								+ Helper.getPCName(),
								/* Helper.getMD5Hash((benutzer + */password
										.toCharArray()/* ).toCharArray()) */,
								LPMain.getInstance().getUISprLocale(), null);

				LPMain.getInstance().setIdUser(theClientDto.getIDUser());
				LPMain.getInstance().setTheClient(theClientDto);

				new ZETimer(zestiftresource);

			} catch (MissingResourceException ex) {
				// TODO-AGILCHANGES
				/**
				 * AGILPRO CHANGES BEGIN
				 * 
				 * @author Lukas Lisowski
				 */
				if (args != null && args.length > 0
						&& args[0].compareToIgnoreCase("agilpro") == 0) {
					LPMain.getInstance().setAGILPRO(true);
					Desktop frame = new com.lp.client.pc.Desktop(true);
					LPMain.getInstance().setDesktop(frame);
				} else {

					Runnable showModalDialog = new Runnable() {
						public void run() {
							Desktop frame = null;
							try {
								frame = new com.lp.client.pc.Desktop(LPMain.getInstance().getDesktopController());
								LPMain.getInstance().setDesktop(frame);

								frame.setVisible(true);
							} catch (Throwable ex) {
								System.out.println("Got Exception "
										+ ex.getMessage());
							}
						}
					};
					SwingUtilities.invokeAndWait(showModalDialog);
				}
				
				FocusManager.getCurrentManager().setDefaultFocusTraversalPolicy(new HvLayoutFocusTraversalPolicy());
				
				/*
				 * // damit werden die queues aktiviert! // InfoTopic immer
				 * abonnieren LPMain.getInstance().getInfoTopic();
				 * 
				 * // andere Topics abh&auml;ngig von Rolle String[] themen; try {
				 * themen = DelegateFactory.getInstance().getBenutzerDelegate().
				 * getThemenDesAngemeldetenBenutzers(); if (themen != null) {
				 * for (int i=0; i<themen.length; i++) { if
				 * (themen[i].trim().equals(LPTopicQsBean.TOPIC_NAME_QS))
				 * LPMain.getInstance().getTopicQs(); if
				 * (themen[i].trim().equals(LPTopicFertBean.TOPIC_NAME_FERT))
				 * LPMain.getInstance().getTopicFert(); if
				 * (themen[i].trim().equals
				 * (LPTopicManageBean.TOPIC_NAME_MANAGE))
				 * LPMain.getInstance().getTopicManage(); if
				 * (themen[i].trim().equals(LPTopicGfBean.TOPIC_NAME_GF))
				 * LPMain.getInstance().getTopicGf(); } } } catch (Exception e)
				 * { e.printStackTrace(); } lpqueue = new LPQueueListener();
				 */
				/**
				 * AGILPRO CHANGES END
				 */
			}

		} catch (Throwable t) {
			LPMain.getInstance().exitClientNowErrorDlg(t);
		}
	}

	public static boolean getBooleanFromArgs(String argument, boolean defaultValue) {
		int index = argument.indexOf("=") ;
		if(index == -1) return defaultValue ;
		
		String s = argument.substring(index + 1);
		boolean enabled = !s.equals("0");		
		return enabled ;
	}
	
	public Vector<String> getMandanten(String cBenutzerI, String cKennwortI)
			throws Throwable {
		vecOfMandanten = new Vector<String>();
		BenutzerDto benutzerDto = DelegateFactory
				.getInstance()
				.getBenutzerDelegate()
				.benutzerFindByCBenutzerkennung(cBenutzerI,
						Helper.getMD5Hash(cBenutzerI + cKennwortI).toString());
		BenutzermandantsystemrolleDto[] bds = DelegateFactory
				.getInstance()
				.getBenutzerDelegate()
				.benutzermandantsystemrolleFindByBenutzerIId(
						benutzerDto.getIId());
		for (int i = 0; i < bds.length; i++) {
			MandantDto m = DelegateFactory.getInstance().getMandantDelegate()
					.mandantFindByPrimaryKey(bds[i].getMandantCNr());
			vecOfMandanten.add(bds[i].getMandantCNr() + "|" + m.getCKbez());
		}
		return vecOfMandanten;
	}

	public Vector<Locale> getUILocales() throws Throwable {
		if (vecOfLocUI == null) {

			String[] fileNamen = null;
			try {
				// 1. versuch: beim kunden, hat ja ein jar
				fileNamen = getResourcebundelnameOutOfJar();

				if (fileNamen == null || fileNamen.length == 0) {
					throw new Exception("fileNamen == null");
				}
				myLogger.debug("Beim Anwender (extern).");
			} catch (Exception ex) {
				// 2. versuch: ausgepackt, bei uns, d.h. im JBuilder
				File pathName = new File(DIR_RESOURCE);
				myLogger.error(">> " + pathName.getAbsolutePath(), null);
				fileNamen = pathName.list(new ResourcebundelFilter());
			}
			vecOfLocUI = new Vector<Locale>();

			// messages.properties ist locale: enEN (SpracheLand): dh. fix
			// dabei!
			Locale loc = null;

			if (null == fileNamen)
				return vecOfLocUI;

			// jetzt alle gekauften "Locales" hinzufuegen
			for (int i = 0; i < fileNamen.length; i++) {
				int iM = fileNamen[i].indexOf(FILE_NAME_RESOURCE + "_");
				if (iM > -1) {
					//
					// hat zB. noch com/lp/client/res/messages_de_AT.properties
					// ist locale: deDE
					// 012345678901234567890
					// hat zB. noch messages_pl_PL.properties ist locale: plPL
					String ssLanguage = fileNamen[i].substring(iM + 9,
							iM + 9 + 2);
					String ssCountry = fileNamen[i].substring(iM + 12,
							iM + 12 + 2);
					loc = new Locale(ssLanguage, ssCountry);
					myLogger.debug("UI-Locale found: " + loc.getDisplayName());
					if (!vecOfLocUI.contains(loc)) {
						vecOfLocUI.add(loc);
					}
				}
			}
		}
		return vecOfLocUI;
	}

	private String[] getResourcebundelnameOutOfJar() throws ZipException,
			IOException, Exception {

		// --beim kunden: in einem lpclientpc.jar lesen.
		File pathName = new File("." + FS + "lpclientpc.jar");
		if (!pathName.exists()) {
			throw new Exception("kein lpclientpc.jar");
		}
		ZipFile zf = null;
		zf = new ZipFile(pathName);
		Enumeration<?> e = zf.entries();
		ArrayList<String> fn = new ArrayList<String>();
		while (e.hasMoreElements()) {
			ZipEntry ze = (ZipEntry) e.nextElement();
			if (ze.getName().indexOf("messages_") > 0
					&& ze.getName().endsWith("properties")) {

				fn.add(ze.getName());

			}
		}
		String[] s = (String[]) fn.toArray(new String[0]);
		zf.close();
		return s;
	}

	/**
	 * Loesche jInternalFrameMe vom Desktop und logge dies.
	 * 
	 * @param internalFrameMe
	 *            JInternalFrameModul
	 * @param t
	 *            ExceptionForLPClients
	 */
	public void exitFrame(InternalFrame internalFrameMe, Throwable t) {
		if (desktop != null) {
			desktop.exitFrameDlg(internalFrameMe, t, null);
		} else {
			myLogger.error(t.getMessage(), t);
			System.exit(1);
		}
	}

	/**
	 * Loesche eine InternalFrame (LP-Modul) vom Desktop.
	 * 
	 * @param internalFrameMe
	 *            JInternalFrameModul
	 */
	public void exitFrame(InternalFrame internalFrameMe) {
		if (desktop != null) {
			desktop.exitFrameDlg(internalFrameMe, null,
					getTextRespectUISPr("lp.modul.beenden"));
		}
	}

	/**
	 * Loesche einen InternalFrame (LP-Modul) vom Desktop ohne weitere
	 * Benachrichtigung des Benutzers
	 * 
	 * @param internalFrameMe
	 */
	public void exitFrameSilent(InternalFrame internalFrameMe) {
		if (desktop != null) {
			desktop.exitFrameDlg(internalFrameMe, null, null);
		}
	}

	/**
	 * Beende den Client und logge dies.
	 * 
	 * @param t
	 *            ExceptionForLPClients
	 */
	public void exitClientNowErrorDlg(Throwable t) {

		if (desktop != null) {
			desktop.exitClientNowErrorDlg(t);
		} else {
			myLogger.error(t.getMessage(), t);
			System.exit(1);
		}
	}

	/**
	 * Einen Text mit Parametern formatieren. Beispiel: Im Token ist hinterlegt:
	 * "Sie haben {0} St&uuml;ck erhalten"
	 * 
	 * @param token
	 *            ist der Name des zu verwendenden Tokens. Beispiel:
	 *            fb.konten.result
	 * @param values
	 *            nimmt die einzusetzenden Werte auf
	 * @return den formatierten String
	 */
	public static String getMessageTextRespectUISPr(String token,
			Object... values) {
		String msg = getTextRespectUISPr(token);
		return MessageFormat.format(msg, values);
	}

	/**
	 * clientres: 1 hole fuer einen token je nach sparche den text.
	 * 
	 * @param token
	 *            String
	 * @return String
	 */
	public static String getTextRespectUISPr(String token) {
		String sText = getTextRespectUISPrWithNull(token);
		return sText == null ? ("resourcebundletext fehlt: " + token) : sText;
	}

	public static String getTextRespectUISPrWithNull(String token) {
		String sText = null;
		try {
			// zuerst schaun, obs eine spezielle Uebersetzung gibt
			sText = Defaults.getInstance().getSpezifischenText(token);
		} catch (Throwable ex1) {
			// nothing here
		}
		// wenn noch keiner gefunden wurde, dann aus den properties
		if (sText == null) {
			if (getInstance().messagesBundel == null) {
				getInstance().messagesBundel = ResourceBundle.getBundle(
						RESOURCE_BUNDEL_ALLG, getInstance().getUISprLocale());
			}
			try {
				if (getInstance().messagesBundel.containsKey(token))
					sText = getInstance().messagesBundel.getString(token);
			} catch (Exception ex) {
				// getInstance().myLogger.error(ex.getMessage(), ex);
			}
		}
		return sText;
	}

	public static String getTextRespectSpezifischesLocale(String token,
			Locale locale) {
		String sText = null;

		// wenn noch keiner gefunden wurde, dann aus den properties

		ResourceBundle r = ResourceBundle.getBundle(RESOURCE_BUNDEL_ALLG,
				locale);

		try {
			sText = r.getString(token);
		} catch (Exception ex) {
			getInstance().myLogger.error(ex.getMessage(), ex);
			sText = "resourcebundletext fehlt: " + token;
		}
		return sText;

	}

	/**
	 * Hole einen Klientparameter.
	 * 
	 * @param lPparameter
	 *            String; steht im lp.properties
	 * @return String
	 */
//	public String getLPParameter(String lPparameter) {
//		if (lpBundel == null) {
//			lpBundel = ResourceBundle.getBundle(RESOURCE_BUNDEL_LP);
//		}
//		return lpBundel.getString(lPparameter);
//	}

	/**
	 * Hole den Desktop.
	 * 
	 * @return Desktop
	 */
	public com.lp.client.pc.Desktop getDesktop() {
		return desktop;
	}

	/**
	 * Setze den Desktop.
	 * 
	 * @param desktop
	 *            Desktop
	 */
	public void setDesktop(com.lp.client.pc.Desktop desktop) {
		this.desktop = desktop;
	}

	/**
	 * Hole das UI-Sparchenlocale.
	 * 
	 * @return Locale
	 * @throws Exception
	 */
	private Locale getUISprLocaleFromResource() throws Exception {
		String lo = ClientConfiguration.getUiSprLocale() ;
		if (lo.length() != 4) {
			throw new Exception("lo.length() != 4");
		}

		// auf 10 auffuellen
		setUISprLocale(Helper.string2Locale(lo + "      "));
		return locUISprache;
	}

	/**
	 * Setze das UI-Sprachenlocale.
	 * 
	 * @param uISprLocale
	 *            Locale
	 */
	public void setUISprLocale(Locale uISprLocale) {
		this.locUISprache = uISprLocale;
		this.messagesBundel = null;
	}

	public static TheClientDto getTheClient() throws Throwable {
		// lazy initialization
		if (getInstance().theClientDto == null
				&& getInstance().getCNrUser() != null) {
			getInstance().theClientDto = DelegateFactory.getInstance()
					.getTheClientDelegate()
					.theClientFindByPrimaryKey(getInstance().getCNrUser());
		}
		return getInstance().theClientDto;
	}

	public void setTheClient(com.lp.server.system.service.TheClientDto theClient) {
		this.theClientDto = theClient;
		lpadmin = null;
	}

	/**
	 * Hole die User-ID.
	 * 
	 * @return String
	 */
	public String getCNrUser() {
		return cNrUser;
	}

	/**
	 * Setze die User-ID.
	 * 
	 * @param idUser
	 *            String
	 */
	public void setIdUser(String idUser) {
		this.cNrUser = idUser;
		// TheClient muss neu geladen werden!
		theClientDto = null;
	}

	/**
	 * Hole den eingegebenen (rohen) Benutzernamen. Internal use only.
	 * 
	 * @return String
	 */
	public String getBenutzernameRaw() {
		return benutzernameRaw;
	}

	/**
	 * Setze den eingegebenen (rohen) Benutzernamen. Internal use only.
	 * 
	 * @param benutzernameRaw
	 *            String
	 */
	public void setBenutzernameRaw(String benutzernameRaw) {
		this.benutzernameRaw = benutzernameRaw;
	}

	static public String getLockMeForNew() throws Throwable {
		return "new" + "|"
				+ LPMain.getTheClient().getBenutzername() + "|"
				+ LPMain.getInstance().getCNrUser();
	}

	/**
	 * hole die infotopic queue; jeder client hat genau eine.
	 * 
	 * @return InfoTopic
	 * @throws Throwable
	 */

	public InfoTopic getInfoTopic() throws Throwable {
		return infoTopic;
	}

	public InfoTopic initInfoTopic(Desktop desktopI) throws Throwable {
		if (infoTopic == null) {
			infoTopic = new InfoTopic(desktopI);
		}
		return infoTopic;
	}

	/**
	 * hole die QS-topic queue; jeder client hat genau eine.
	 * 
	 * @return TopicQs
	 * @throws Throwable
	 */
	public TopicQs getTopicQs() throws Throwable {
		return topicQS;
	}

	public TopicQs initTopicQs(Desktop desktopI) throws Throwable {
		if (topicQS == null) {
			topicQS = new TopicQs(desktopI);
		}
		return topicQS;
	}

	/**
	 * hole die Fertigung-topic queue; jeder client hat genau eine.
	 * 
	 * @return TopicFert
	 * @throws Throwable
	 */
	public TopicFert getTopicFert() throws Throwable {
		return topicFert;
	}

	public TopicFert initTopicFert(Desktop desktopI) throws Throwable {
		if (topicFert == null) {
			topicFert = new TopicFert(desktopI);
		}
		return topicFert;
	}

	/**
	 * hole die Management-topic queue; jeder client hat genau eine.
	 * 
	 * @return TopicManage
	 * @throws Throwable
	 */
	public TopicManage getTopicManage() throws Throwable {
		return topicManage;
	}

	public TopicManage initTopicManage(Desktop desktopI) throws Throwable {
		if (topicManage == null) {
			topicManage = new TopicManage(desktopI);
		}
		return topicManage;
	}

	/**
	 * hole die GF-topic queue; jeder client hat genau eine.
	 * 
	 * @return TopicGf
	 * @throws Throwable
	 */
	public TopicGf getTopicGf() throws Throwable {
		return topicGf;
	}

	public TopicGf initTopicGf(Desktop desktopI) throws Throwable {
		if (topicGf == null) {
			topicGf = new TopicGf(desktopI);
		}
		return topicGf;
	}

	/**
	 * Hole das eingegebene (rohe) Kennwort. Internal use only.
	 * 
	 * @return char[]
	 */
	public char[] getKennwortRaw() {
		return kennwortRaw;
	}

	public String getMandantRaw() {
		return mandantRaw;
	}

	static public Date getVersionHVExpires() {
		return S_VERSION_HV_EXPIRES;
	}

	public Locale getUISprLocale() {
		return locUISprache;
	}

	/**
	 * Setze das eingegeben (rohe) Kennwort.
	 * 
	 * @param kennwortRaw
	 *            char[]
	 */
	public void setKennwortRaw(char[] kennwortRaw) {
		this.kennwortRaw = kennwortRaw;
	}

	public void setMandantRaw(String mandantRaw) {
		this.mandantRaw = mandantRaw;
	}

	static public void setVersionHVExpires(Date S_VERSION_HV_EXPIRES_I) {
		S_VERSION_HV_EXPIRES = S_VERSION_HV_EXPIRES_I;
	}

	class ResourcebundelFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.startsWith("messages_") && name
					.endsWith(".properties"));
		}
	}

	class LpclientpcJarFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.matches("lpclientpc.jar"));
		}
	}

	public void setXMLFilePath(String xmlFilePathI) {
		LPMain.XMLFILEPATH = xmlFilePathI;
	}

	public String getXMLFilePath() {
		return LPMain.XMLFILEPATH;
	}

	public File getXMLFileByName(String fileNameI) {
		// ausgepackt, bei uns
		return new File("." + FS + "classes" + FS + XMLFILEPATH + FS
				+ fileNameI);
	}

	public InputStream getXMLFileFromJar(String fileNameI) throws ZipException,
			IOException, Exception {
		InputStream is = getClass( ).getResourceAsStream("/com/lp/client/frame/stammdatencrud/"
				+ fileNameI);
		return is ;
		
		// --beim kunden: in einem lpclientpc.jar lesen.
//		File f = new File("lpclientpc.jar");
//		if (f.exists()) {
//			ZipFile zf = new ZipFile(f);
//			ZipEntry entry = zf.getEntry("com/lp/client/frame/stammdatencrud/"
//					+ fileNameI);
//			return zf.getInputStream(entry);
//		}
//		return null;
	}

	public boolean isLPAdmin() {
		if (lpadmin != null) {
			return lpadmin;
		}

		try {
			String benutzername = getTheClient().getBenutzername();
			if (benutzername == null)
				return false;

			benutzername = benutzername.trim().substring(0,
					benutzername.indexOf("|"));
			lpadmin = benutzername.equalsIgnoreCase(ClientConfiguration.getAdminUsername());
			return lpadmin.booleanValue();
		} catch (ExceptionLP e) {
		} catch (Throwable t) {
		}

		return false;
	}


		
	public String getMsg(ExceptionLP ec) {
		if(errorMessageHandler == null) {
			errorMessageHandler = new LPMessages() ;
		}
		
		return errorMessageHandler.getMsg(ec) ;
	}
	
	
	static public void setVersionHVModulnr(String iVersionHVModulnrI) {
		S_VERSION_HV_MODULNR = iVersionHVModulnrI;
	}

	static public void setVersionHVBugfixnr(String iVersionHVBugfixnrI) {
		S_VERSION_HV_BUGFIXNR = iVersionHVBugfixnrI;
	}

	static public void setVersionHVBuildnr(String iVersionHVBuildnrI) {
		S_VERSION_HV_BUILDNR = iVersionHVBuildnrI;
	}

	static public void setSVersionHV(String sVersionHVI) {
		S_VERSION_HV = sVersionHVI;
	}

	static public String getVersionHVModulnr() {
		return S_VERSION_HV_MODULNR;
	}

	static public String getVersionHVBugfixnr() {
		return S_VERSION_HV_BUGFIXNR;
	}

	static public String getVersionHVBuildnr() {
		return S_VERSION_HV_BUILDNR;
	}

	static public String getSVersionHV() {
		return S_VERSION_HV;
	}

	static public String getSVersionHVAllTogether() {
		return S_VERSION_HV_ALLTOGETHER;
	}

	/**
	 * Schreiben einer Datei.
	 * 
	 * @param internalFrame
	 *            InternalFrame
	 * @param sFilename
	 *            String
	 * @param content
	 *            byte[]
	 * @param bASCII 
	 * @return boolean wurde geschrieben?
	 */
	public boolean saveFile(InternalFrame internalFrame, String sFilename,
			byte[] content, boolean bASCII) {
		boolean bSchreiben = true;
		boolean bGeschrieben = false;
		File file = new File(sFilename);
		if (file.exists()) {
			boolean bUeberschreiben = DialogFactory.showModalJaNeinDialog(
					internalFrame,
					getTextRespectUISPr("lp.frage.dateiueberschreiben") + "\n"
							+ sFilename, getTextRespectUISPr("lp.frage"));
			if (!bUeberschreiben) {
				bSchreiben = false;
			}
		}
		// solange ich noch was zu schreiben hab und noch nicht geschrieben
		// wurde
		while (bSchreiben && !bGeschrieben) {
			try {
				if(file.getParent() != null)
					file.getParentFile().mkdirs();
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				if (bASCII) {
					for (int i = 0; i < content.length; i++) {
						if (content[i] == (byte) 196) {
							content[i] = (byte) 142;
						}
						if (content[i] == (byte) 214) {
							content[i] = (byte) 153;
						}
						if (content[i] == (byte) 220) {
							content[i] = (byte) 154;
						}
						if (content[i] == (byte) 228) {
							content[i] = (byte) 132;
						}
						if (content[i] == (byte) 246) {
							content[i] = (byte) 148;
						}
						if (content[i] == (byte) 252) {
							content[i] = (byte) 129;
						}
						if (content[i] == (byte) 223) {
							content[i] = (byte) 225;
						}
					}
				}
				out.write(content);
				out.close();
				bGeschrieben = true;
				// Dialog anzeigen
				DialogFactory.showModalDialog(getTextRespectUISPr("lp.hint"),
						getTextRespectUISPr("lp.hint.dateiwurdegespeichert")
								+ "\n" + sFilename);
			} catch (IOException ex) {
				bSchreiben = DialogFactory
						.showModalJaNeinDialog(
								internalFrame,
								getTextRespectUISPr("lp.error.dateikannnichterzeugtwerden")
										+ "\n"
										+ sFilename
										+ "\n"
										+ "\n"
										+ getTextRespectUISPr("lp.frage.erneutversuchen"),
								getTextRespectUISPr("lp.error"));
			}
		}
		return bGeschrieben;
	}

	static final public PasteBuffer getPasteBuffer() throws IOException {

		if (pasteBuffer == null) {
			pasteBuffer = new PasteBuffer();
			// pasteBuffer.init();
		}

		return pasteBuffer;
	}

	static final public InfoTopicBuffer getInfoTopicBuffer() throws IOException {

		if (infoTopicBuffer == null) {
			infoTopicBuffer = new InfoTopicBuffer();
			// infoTopicBuffer.init();
		}

		return infoTopicBuffer;
	}

	public int execute(Command commandI) throws Throwable {
		return getDesktop().execute(commandI);
	}

	private static void installAutoLogOffHandler() {
		Thread shutdownHook = new Thread(new AutoLogOffHandler());
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	private static class AutoLogOffHandler implements Runnable {
		public void run() {
			if (null == lPMain)
				return;

			try {
				if(null == lPMain.theClientDto) return ;

				LogonDelegate delegate = DelegateFactory.getInstance()
						.getLogonDelegate();
				if (null == delegate)
					return;
				delegate.logout(lPMain.theClientDto);
			} catch (ExceptionLP e) {
			} catch (Exception e) {
			} catch (Throwable t) {
			}
		}
	}
}
