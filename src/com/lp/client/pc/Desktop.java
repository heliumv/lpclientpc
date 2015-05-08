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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.help.SwingHelpUtilities;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrinterName;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.benutzer.InternalFrameBenutzer;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.frame.Command;
import com.lp.client.frame.Command2IFNebeneinander;
import com.lp.client.frame.CommandCreateIF;
import com.lp.client.frame.CommandSetFocus;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.DirekthilfeCache;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogFontStyle;
import com.lp.client.frame.component.ParameterCache;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.ToolbarButton;
import com.lp.client.frame.component.cib.CibIconWrapper;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.inserat.InternalFrameInserat;
import com.lp.client.instandhaltung.InternalFrameInstandhaltung;
import com.lp.client.jms.LPQueueListener;
import com.lp.client.kueche.InternalFrameKueche;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.media.InternalFrameMedia;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.personal.InternalFramePersonal;
import com.lp.client.projekt.InternalFrameProjekt;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.reklamation.InternalFrameReklamation;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.system.InternalFrameSystem;
import com.lp.client.util.ChangesShownController;
import com.lp.client.util.ClientConfiguration;
import com.lp.client.util.logger.LpLogger;
import com.lp.client.zeiterfassung.InternalFrameZeiterfassung;
import com.lp.client.zutritt.InternalFrameZutritt;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.jms.service.LPTopicFertBean;
import com.lp.server.system.jms.service.LPTopicGfBean;
import com.lp.server.system.jms.service.LPTopicManageBean;
import com.lp.server.system.jms.service.LPTopicQsBean;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.InstallerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * frame<BR>
 *
 * Diese Klasse haelt den Desktop.<BR> Alle LP-Module (JInternalFrames) werden
 * von hier aus verwaltet.
 *
 * <p>Erstellungsdatum 07.09.04</p>
 *
 * @version 2.0
 *
 * @author Josef Ornetsmueller
 */

public class Desktop extends JFrame implements ActionListener,
		VetoableChangeListener, ItemListener, ComponentListener, IDesktop,
		ICommand, InternalFrameListener {

//	TheClientDto theClientDto = null;

	public static int einfuegenAusZwischenablage = 0;
	private static final long serialVersionUID = 1L;

	private static final String DESKTOP_NAME = "desktopHelium";
	// private static final String HIDE_MODULBTN = "hide";

	public static final String MODULNAME_LOGIN = "module_login";
	public static final String MODULNAME_MANDANT = "module_mandant";
	public static final String MODULNAME_LOGOUT = "module_logout";
	public static final String MODULNAME_BEENDEN = "module_beenden";
	public static final String MODULNAME_LOS_SCHNELLANLAGE = "module_los_schnellanlage";

	public static final int MAX_CHARACTERS_UNTIL_WORDWRAP = 80;

	public static final String LAF_CLASS_NAME_KUNSTSTOFF = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";

	protected final LpLogger myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	private HashMap<String, LPModul> hmOflPModule = new HashMap<String, LPModul>();

	// private String UI_CUR = "Kunststoff";
	private JPanel jpaContentPane = null;
	private JDesktopPane desktopPane = null;
	private JToolBar toolbar;

	private int nextFrameX;
	private int nextFrameY;
	private int frameDistance;

	// private ModulberechtigungDto[] modulberechtigungDtos = null;
	// private ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungDtos =
	// null;

	private BackgroundPanel jpaBackground = null;
	// controls

	// diese menuItems werden durch initComponents automatisch
	// benamst.
	// die modul-eintraege muessen extra benamst werden.
	private JMenuItem menuItemAnsichtExit = null;
	private JMenuItem menuItemAnsichtNext = null;
	private JMenuItem menuItemAnsichtCascade = null;
	private JMenuItem menuItemAnsichtTile = null;
	private JMenuItem menuItemAnsichtSave = null;
	private JMenuItem menuItemAnsichtLoad = null;
	private JMenuItem menuItemAnsichtReset = null;
	private JMenuItem menuItemSchriftart = null;
	private JMenuItem menuItemDirekthilfe = null;
	private JMenuItem menuItemHilfeAbout = null;
	private JMenuItem menuItemHilfeOnline = null;
	private JMenuItem menuItemHilfeScreenshotsenden = null;
	private JMenuItem menuItemHilfeScreenshotDrucken = null;
	private JMenuItem menuItemHilfeAnwenderspezifisch = null;
	private JRadioButtonMenuItem menuItemAnsichtLFJava;
	// private JRadioButtonMenuItem motifItem;
	// private JRadioButtonMenuItem winItem;
	private JRadioButtonMenuItem menuItemLFKunststoff;
	private JRadioButtonMenuItem menuItemLFSystem;
	// private JRadioButtonMenuItem skinItem;
	private ButtonGroup buttonGroupLF;
	// private JMenuItem statusItem;
	private JMenuItem menuItemAnsichtToolbar;
	// private JMenuItem menuItemTooltip;
	private DialogLogin dialogLogin = null;
	private boolean bAbbruch = false;
	private PanelDesktopStatusbar desktopStatusBar = null;
	private int iUntereLasche = 0;
	private JProgressBar updateBar = new JProgressBar();
	private JOptionPane barPane = new JOptionPane();
	private boolean hasBG = true;

	// TODO: Sinnvollerweise gehoert der DesktopController dem Desktop
	// uebergeben
//	private IDesktopController dc = new DesktopController();
	private IDesktopController dc ;
	private static boolean fontChanged = false;


	public Desktop(boolean pAgilPro) throws Throwable {

		if (!pAgilPro) {
			return;
		}
		setDefaultPropertiesAndColors();

		GraphicsConfiguration gc = getGraphicsConfiguration();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Toolkit.getDefaultToolkit();
		// Rectangle rect = gc.getBounds();
		// int scWidth = rect.width - (insets.left + insets.right);
		// int scHeight = rect.height - (insets.top + insets.bottom);
		setLocation(insets.left, insets.top);

		String lcOSName = System.getProperty("os.name").toLowerCase();
		boolean MAC_OS_X = lcOSName.startsWith("mac os x");

		if (MAC_OS_X == true) {
			setSize(1024, 773);
			/** @todo JO -> lp.properties PJ 5408 */
		} else {
			setSize(1024, 740);
			/** @todo JO -> lp.properties PJ 5408 */
		}

		// Window Close Listener
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				// TODO-AGIL:
				// exitClientDlg();
			}
		});

		toolbar = new javax.swing.JToolBar();

		// Arbeitsfenster einrichten
		jpaContentPane = (javax.swing.JPanel) getContentPane();
		jpaContentPane.setLayout(new java.awt.BorderLayout());

		// Desktop einrichten
		desktopPane = new javax.swing.JDesktopPane();

		// Fenster ohne Umrisse ziehen
		desktopPane.putClientProperty("JDesktopPane.dragMode", "outline");
		jpaContentPane.add(desktopPane, "Center");
		setVisible(true);
		initComponents();
	}

	/**
	 * AGILPRO CHANGES END
	 *
	 * @throws Throwable
	 */
	public Desktop() throws Throwable {
		this(new DesktopController()) ;
	}

	public Desktop(IDesktopController desktopController) throws Throwable {
		dc = desktopController;
		
		setDefaultPropertiesAndColors();

		// Setze Icon
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/heliumv.png"));
		setIconImage(imageIcon.getImage());

		GraphicsConfiguration gc = getGraphicsConfiguration();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Toolkit.getDefaultToolkit();
		// Rectangle rect = gc.getBounds();
		// int scWidth = rect.width - (insets.left + insets.right);
		// int scHeight = rect.height - (insets.top + insets.bottom);
		setLocation(insets.left, insets.top);
		// setSize(scWidth, scHeight);

		String lcOSName = System.getProperty("os.name").toLowerCase();
		boolean MAC_OS_X = lcOSName.startsWith("mac os x");

		if (MAC_OS_X == true) {
			setSize(1024, 773);
			/** @todo JO -> lp.properties PJ 5408 */
		} else {
			setSize(1024, 740);
			/** @todo JO -> lp.properties PJ 5408 */
		}

		if (Defaults.getInstance().isMaximized()) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}

		// Window Close Listener
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				exitClientDlg();
			}
		});

		toolbar = new JToolBar();
		// Menues einrichten
		createMenuBar();
		hmOflPModule.get(MODULNAME_LOGIN)
			.setStatus(LPModul.STATUS_ENABLED);
		hmOflPModule.get(MODULNAME_BEENDEN)
				.setStatus(LPModul.STATUS_ENABLED);
		hmOflPModule.get(MODULNAME_LOGOUT)
				.setStatus(LPModul.STATUS_DISABLED);
		hmOflPModule.get(MODULNAME_MANDANT)
				.setStatus(LPModul.STATUS_DISABLED);

		// Arbeitsfenster einrichten
		jpaContentPane = (JPanel) getContentPane();
		jpaContentPane.setLayout(new java.awt.BorderLayout());

		// Desktop einrichten
		desktopPane = new JDesktopPane();

		// Fenster ohne Umrisse ziehen
		desktopPane.putClientProperty("JDesktopPane.dragMode", "outline");

		// einzelne Komponenten dem Arbeitsfenster hinzufuegen
		jpaContentPane.add(toolbar, "North");
		desktopPane.addComponentListener(this);
		// Check ob die Mindestaufloesung(1024x768) erfuellt ist
		Double dAufloesungx = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().getWidth();
		Double dAufloesungy = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().getHeight();
		if ((dAufloesungx < 1024.0) || dAufloesungy < 768.0) {
			showModalDialog("",
					LPMain.getTextRespectUISPr("lp.error.falscheAufloesung"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);

		}
		// Hintergrundbild einfuegen
		ArbeitsplatzparameterDto parameter = null;
		try {
			parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_BACKGROUND_ENABLED);
		} catch (Throwable t) {
			t.printStackTrace();
			showModalDialog("",
					LPMain.getTextRespectUISPr("lp.error.no_server") + " ("
							+ System.getProperty("java.naming.provider.url")
							+ ")", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		if (parameter != null && parameter.getCWert() != null) {
			if (parameter.getCWert().equals("0")) {
				hasBG = false;
			} else {
				hasBG = true;
			}
		} else {
			if (Defaults.getInstance().getBackground()) {
				hasBG = true;
			} else {
				hasBG = false;
			}
		}

		if (hasBG) {
			jpaBackground = new BackgroundPanel(
					"/com/lp/client/res/heliumvdesktop.jpg");
			jpaBackground.setSize(1020, 500);
			jpaBackground.setLayout(new FlowLayout());
			desktopPane.add(jpaBackground);
		}
		jpaContentPane.add(desktopPane, "Center");
		setVisible(true);

		setTitle(LPMain.getSVersionHVAllTogether());

		LPMain.getInstance().setDesktop(this);
		java.awt.event.ActionEvent avt = new ActionEvent(this, 5,
				MODULNAME_LOGIN);
		actionPerformed(avt);

		setVisible(true);
		initComponents();
		initJms();		
	}

	private void addStatusbar() throws Throwable {
		desktopStatusBar = new PanelDesktopStatusbar();
		jpaContentPane.add(desktopStatusBar, BorderLayout.SOUTH);
	}

	/**
	 * Initialierungen fuer die Komponenten. Muss zu einem Zeitpunkt aufgerufen
	 * werden, wenn die Komponenten nicht mehr null sind.
	 *
	 * @throws Throwable
	 */
	private final void initComponents() throws Throwable {

		// eigenen namen setzen
		setName(DESKTOP_NAME);

		// namen der Komponenten setzen
		HelperClient.setComponentNames(this);
	}

	/**
	 * Create Menuebars.
	 *
	 * @throws Throwable
	 */
	private void createMenuBar() throws Throwable {

		JMenuBar menuBar = new JMenuBar();
		// menuBar.setName(HelperClient.COMP_NAME_MENUBAR);
		super.setJMenuBar(menuBar);

		// Programm
		createMenueProgram(menuBar);
	}

	/**
	 * createMenueExtra
	 *
	 * @param menuBar
	 *            JMenuBar
	 * @throws Throwable
	 */
	private void createMenueExtra(JMenuBar menuBar) throws Throwable {
		JMenu menue = new JMenu(
				LPMain.getTextRespectUISPr("rechnung.menu.extras"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menue.setName(HelperClient.COMP_NAME_MENU_EXTRAS);
		}
		menuBar.add(menue);

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_PARTNER_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_PARTNER_CUD))) {
			createALPModulmenueentry(menue, LocaleFac.BELEGART_PARTNER);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_BENUTZER)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_BENUTZER_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_BENUTZER_CUD))) {
			createALPModulmenueentry(menue, LocaleFac.BELEGART_BENUTZER);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_SYSTEM)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_SYSTEM_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_SYSTEM_CUD))) {
			createALPModulmenueentry(menue, LocaleFac.BELEGART_SYSTEM);
		}

		// JMenu menuEmail = null ;
		// if
		// (darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT))
		// {
		// menuEmail = new JMenu(
		// LPMain.getTextRespectUISPr("lp.menu.email"));
		// if (Defaults.getInstance().isComponentNamingEnabled()) {
		// menuEmail.setName(HelperClient.COMP_NAME_MENU_EMAIL);
		// }
		// menuBar.add(menuEmail);
		// }

		if (darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT)) {
			if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_EMAIL)
					&& (DelegateFactory.getInstance().getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_MEDIA_EMAIL_R) || DelegateFactory
							.getInstance().getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_MEDIA_EMAIL_CUD))) {
				createALPModulmenueentry(menue, LocaleFac.BELEGART_EMAIL);
			}
		}

		if (menue.getItemCount() == 0) {
			menuBar.remove(menue);
		}
	}

	/**
	 * Zeige einen modalen Warndialog mit msg als Inhalt und Titel titel.
	 *
	 * @param sTitleI
	 *            String
	 * @param sMsgI
	 *            String
	 * @param iModeI
	 *            int
	 * @throws IOException
	 */
	public void showModalDialog(String sTitleI, String sMsgI, int iModeI) {
		JOptionPane pane = InternalFrame
				.getNarrowOptionPane(MAX_CHARACTERS_UNTIL_WORDWRAP);
		String nl = "\r\n";
		String s = sMsgI.replace("\\n", nl);
		s = s.replace("\\r", "");
		pane.setMessage(s);
		pane.setMessageType(iModeI);
		JDialog dialog = pane.createDialog(this, sTitleI);
		try {
			dialog.setIconImage(ImageIO.read(getClass().getResource(
					"/com/lp/client/res/heliumv.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dialog.setVisible(true);
	}

	private void createMenueProgram(JMenuBar menuBar) throws Throwable {

		JMenu menueProgram = new JMenu(LPMain.getSVersionHV());
		menueProgram.setMnemonic(java.awt.event.KeyEvent.VK_P);
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menueProgram.setName(HelperClient.COMP_NAME_MENU_PROGRAM);
		}
		menuBar.add(menueProgram);

		// Login
		createALPModulmenueentry(menueProgram, MODULNAME_LOGIN);

		// Beenden
		createALPModulmenueentry(menueProgram, MODULNAME_BEENDEN);

		// Logout
		createALPModulmenueentry(menueProgram, MODULNAME_LOGOUT);

		// Mandantwechsel
		createALPModulmenueentry(menueProgram, MODULNAME_MANDANT);
	}

	public boolean darfAnwenderAufModulZugreifen(String whichModul) {
		return dc.darfAnwenderAufModulZugreifen(whichModul);
	}

	public boolean darfAnwenderAufZusatzfunktionZugreifen(
			String whichZusatzfunktion) {
		return dc.darfAnwenderAufZusatzfunktionZugreifen(whichZusatzfunktion);
	}

	public boolean darfDirekthilfeTexteEditieren() {
		return dc.darfDirekthilfeTexteEditieren();
	}

	private void createModulmenueentries(JMenuBar menuBar) throws Throwable {
		JMenu menuWarenwirtschaft = new JMenu(
				LPMain.getTextRespectUISPr("lp.menu.warenwirtschaft"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuWarenwirtschaft
					.setName(HelperClient.COMP_NAME_MENU_WARENWIRTSCHAFT);
		}
		menuBar.add(menuWarenwirtschaft);

		JMenu menuEinkauf = new JMenu(
				LPMain.getTextRespectUISPr("lp.menu.einkauf"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuEinkauf.setName(HelperClient.COMP_NAME_MENU_EINKAUF);
		}
		menuBar.add(menuEinkauf);

		JMenu menuFertigung = new JMenu(
				LPMain.getTextRespectUISPr("lp.menu.fertigung"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuFertigung.setName(HelperClient.COMP_NAME_MENU_FERTIGUNG);
		}
		menuBar.add(menuFertigung);

		JMenu menuVerkauf = new JMenu(
				LPMain.getTextRespectUISPr("lp.menu.verkauf"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuVerkauf.setName(HelperClient.COMP_NAME_MENU_VERKAUF);
		}
		menuBar.add(menuVerkauf);

		JMenu menuManagement = new JMenu(
				LPMain.getTextRespectUISPr("lp.menu.management"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuManagement.setName(HelperClient.COMP_NAME_MENU_MANAGEMENT);
		}
		menuBar.add(menuManagement);

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ARTIKEL)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_ARTIKEL_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_ARTIKEL_CUD))) {
			createALPModulmenueentry(menuWarenwirtschaft,
					LocaleFac.BELEGART_ARTIKEL);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_STK_STUECKLISTE_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_STK_STUECKLISTE_CUD))) {
			createALPModulmenueentry(menuWarenwirtschaft,
					LocaleFac.BELEGART_STUECKLISTE);
		}
		toolbar.addSeparator();
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERANT)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_LIEFERANT_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_LIEFERANT_CUD))) {
			createALPModulmenueentry(menuEinkauf, LocaleFac.BELEGART_LIEFERANT);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANFRAGE)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ANF_ANFRAGE_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ANF_ANFRAGE_CUD))) {
			createALPModulmenueentry(menuEinkauf, LocaleFac.BELEGART_ANFRAGE);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_BESTELLUNG)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_BES_BESTELLUNG_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_BES_BESTELLUNG_CUD))) {
			createALPModulmenueentry(menuEinkauf, LocaleFac.BELEGART_BESTELLUNG);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_EINGANGSRECHNUNG)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ER_EINGANGSRECHNUNG_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD))) {
			createALPModulmenueentry(menuEinkauf,
					LocaleFac.BELEGART_EINGANGSRECHNUNG);
		}
		toolbar.addSeparator();
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FERT_LOS_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FERT_LOS_CUD))) {
			createALPModulmenueentry(menuFertigung, LocaleFac.BELEGART_LOS);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUECHE)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_KUE_KUECHE_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_KUE_KUECHE_CUD))) {
			createALPModulmenueentry(menuFertigung, LocaleFac.BELEGART_KUECHE);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_REKLAMATION)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_REKLA_REKLAMATION_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_REKLA_REKLAMATION_CUD))) {
			createALPModulmenueentry(menuFertigung,
					LocaleFac.BELEGART_REKLAMATION);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSTANDHALTUNG)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_IS_INSTANDHALTUNG_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_IS_INSTANDHALTUNG_CUD))) {
			createALPModulmenueentry(menuFertigung,
					LocaleFac.BELEGART_INSTANDHALTUNG);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD))) {
			createALPModulmenueentry(menuFertigung,
					LocaleFac.BELEGART_ZEITERFASSUNG);
		}
		if ((DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_PERSONAL_R) || DelegateFactory
				.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_PERSONAL_CUD))) {
			createALPModulmenueentry(menuFertigung, LocaleFac.BELEGART_PERSONAL);
		}
		toolbar.addSeparator();
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_KUNDE_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_KUNDE_CUD))) {
			createALPModulmenueentry(menuVerkauf, LocaleFac.BELEGART_KUNDE);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PROJ_PROJEKT_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PROJ_PROJEKT_CUD))) {
			createALPModulmenueentry(menuVerkauf, LocaleFac.BELEGART_PROJEKT);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_CUD))) {
			createALPModulmenueentry(menuVerkauf, LocaleFac.BELEGART_ANGEBOT);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AS_ANGEBOTSTKL_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AS_ANGEBOTSTKL_CUD))) {
			createALPModulmenueentry(menuVerkauf,
					LocaleFac.BELEGART_AGSTUECKLISTE);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSERAT)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_IV_INSERAT_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_IV_INSERAT_CUD))) {
			createALPModulmenueentry(menuVerkauf, LocaleFac.BELEGART_INSERAT);
		}

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AUFT_AUFTRAG_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AUFT_AUFTRAG_CUD))) {
			createALPModulmenueentry(menuVerkauf, LocaleFac.BELEGART_AUFTRAG);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERSCHEIN)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD))) {
			createALPModulmenueentry(menuVerkauf,
					LocaleFac.BELEGART_LIEFERSCHEIN);
		}
		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_RECHNUNG)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_RECH_RECHNUNG_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_RECH_RECHNUNG_CUD))) {
			createALPModulmenueentry(menuVerkauf, LocaleFac.BELEGART_RECHNUNG);
		}
		toolbar.addSeparator();

		if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZUTRITT)
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZUTRITT_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZUTRITT_CUD))) {
			createALPModulmenueentry(menuManagement, LocaleFac.BELEGART_ZUTRITT);
		}

		if ((darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_EINGANGSRECHNUNG)
				|| darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_RECHNUNG) || darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG))
				&& (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FB_FINANZ_R) || DelegateFactory
						.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FB_FINANZ_CUD))) {
			createALPModulmenueentry(menuManagement,
					LocaleFac.BELEGART_FINANZBUCHHALTUNG);
		}

		// Wenn in einem Menue nichts ist, dann weg damit:
		if (menuWarenwirtschaft.getItemCount() == 0) {
			menuBar.remove(menuWarenwirtschaft);
		}
		if (menuEinkauf.getItemCount() == 0) {
			menuBar.remove(menuEinkauf);
		}
		if (menuFertigung.getItemCount() == 0) {
			menuBar.remove(menuFertigung);
		}
		if (menuVerkauf.getItemCount() == 0) {
			menuBar.remove(menuVerkauf);
		}
		if (menuManagement.getItemCount() == 0) {
			menuBar.remove(menuManagement);
		}
	}

	private void createALPModulmenueentry(JMenu menu, String which)
			throws Throwable {
		if (which.equals(LocaleFac.BELEGART_AUFTRAG)) {
			// Auftrag
			createLPModul(menu, LocaleFac.BELEGART_AUFTRAG,
					"/com/lp/client/res/auftrag24x24.png",
					LPMain.getTextRespectUISPr("auft.auftrag"),
					LPMain.getTextRespectUISPr("auft.modulname.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_ARTIKEL)) {
			// Artikel
			createLPModul(menu, LocaleFac.BELEGART_ARTIKEL,
					"/com/lp/client/res/nut_and_bolt24x24.png",
					LPMain.getTextRespectUISPr("lp.artikel"),
					LPMain.getTextRespectUISPr("tooltip.artikel"));
		} else if (which.equals(LocaleFac.BELEGART_STUECKLISTE)) {
			// Artikel
			createLPModul(menu, LocaleFac.BELEGART_STUECKLISTE,
					"/com/lp/client/res/text_code_colored24x24.png",
					LPMain.getTextRespectUISPr("stkl.stueckliste"),
					LPMain.getTextRespectUISPr("stkl.stueckliste"));
		} else if (which.equals(LocaleFac.BELEGART_PERSONAL)) {
			// Personal
			createLPModul(menu, LocaleFac.BELEGART_PERSONAL,
					"/com/lp/client/res/worker24x24.png",
					LPMain.getTextRespectUISPr("menueentry.personal"),
					LPMain.getTextRespectUISPr("tooltip.personal"));
		} else if (which.equals(LocaleFac.BELEGART_BENUTZER)) {
			// Personal
			createLPModul(menu, LocaleFac.BELEGART_BENUTZER,
					"/com/lp/client/res/user1_monitor24x24.png",
					LPMain.getTextRespectUISPr("benutzer.modulname"),
					LPMain.getTextRespectUISPr("benutzer.modulname"));
		} else if (which.equals(LocaleFac.BELEGART_INSERAT)) {
			// Inserat
			createLPModul(menu, LocaleFac.BELEGART_INSERAT,
					"/com/lp/client/res/news24x24.png",
					LPMain.getTextRespectUISPr("iv.inserat.modulname"),
					LPMain.getTextRespectUISPr("iv.inserat.modulname"));
		} else if (which.equals(LocaleFac.BELEGART_ZEITERFASSUNG)) {
			// Personal
			createLPModul(menu, LocaleFac.BELEGART_ZEITERFASSUNG,
					"/com/lp/client/res/clock24x24.png",
					LPMain.getTextRespectUISPr("zeiterfassung.modulname"),
					LPMain.getTextRespectUISPr("zeiterfassung.modulname"));
		} else if (which.equals(LocaleFac.BELEGART_PARTNER)) {
			// Partner
			createLPModul(menu, LocaleFac.BELEGART_PARTNER,
					"/com/lp/client/res/businessmen24x24.png",
					LPMain.getTextRespectUISPr("part.partner"),
					LPMain.getTextRespectUISPr("tooltip.partner"));
		} else if (which.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
			// Lieferschein
			createLPModul(menu, LocaleFac.BELEGART_LIEFERSCHEIN,
					"/com/lp/client/res/truck_red24x24.png",
					LPMain.getTextRespectUISPr("ls.modulname"),
					LPMain.getTextRespectUISPr("ls.modulname.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_RECHNUNG)) {
			// Rechnung
			createLPModul(menu, LocaleFac.BELEGART_RECHNUNG,
					"/com/lp/client/res/calculator24x24.png",
					LPMain.getTextRespectUISPr("menueentry.rechnung"),
					LPMain.getTextRespectUISPr("tooltip.rechnung"));
		} else if (which.equals(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			// Finanzbuchhaltung
			createLPModul(menu, LocaleFac.BELEGART_FINANZBUCHHALTUNG,
					"/com/lp/client/res/books24x24.png",
					LPMain.getTextRespectUISPr("menueentry.finanz"),
					LPMain.getTextRespectUISPr("tooltip.finanz"));
		} else if (which.equals(MODULNAME_LOGIN)) {
			// Login
			createLPModul(menu, MODULNAME_LOGIN,
					"/com/lp/client/res/key1_add24x24.png",
					LPMain.getTextRespectUISPr("menueentry.login"),
					LPMain.getTextRespectUISPr("lp.tooltip.anmeldung"));
		} else if (which.equals(MODULNAME_LOGOUT)) {
			// Logout
			createLPModul(menu, MODULNAME_LOGOUT,
					"/com/lp/client/res/key1_delete24x24.png",
					LPMain.getTextRespectUISPr("menueentry.logout"),
					LPMain.getTextRespectUISPr("lp.tooltip.abmeldung"));
		} else if (which.equals(MODULNAME_BEENDEN)) {
			// Beenden
			String pattern = LPMain.getTextRespectUISPr("lp.beenden.lp");
			String sMsg = MessageFormat.format(pattern,
					new Object[] { LPMain.getSVersionHVAllTogether() });
			createLPModul(menu, MODULNAME_BEENDEN,
					"/com/lp/client/res/door224x24.png",
					LPMain.getTextRespectUISPr("lp.beenden"), sMsg);
		} else if (which.equals(LocaleFac.BELEGART_SYSTEM)) {
			// System
			createLPModul(menu, LocaleFac.BELEGART_SYSTEM,
					"/com/lp/client/res/toolbox24x24.png",
					LPMain.getTextRespectUISPr("lp.system.modulname"),
					LPMain.getTextRespectUISPr("lp.system.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_KUNDE)) {
			// Kunde
			createLPModul(menu, LocaleFac.BELEGART_KUNDE,
					"/com/lp/client/res/handshake24x24.png",
					LPMain.getTextRespectUISPr("lp.kunde.modulname"),
					LPMain.getTextRespectUISPr("lp.kunde.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_LIEFERANT)) {
			// Lieferant
			createLPModul(menu, LocaleFac.BELEGART_LIEFERANT,
					"/com/lp/client/res/address_book224x24.png",
					LPMain.getTextRespectUISPr("lp.lieferant.modulname"),
					LPMain.getTextRespectUISPr("lp.lieferant.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_ANFRAGE)) {
			// Anfrage
			createLPModul(menu, LocaleFac.BELEGART_ANFRAGE,
					"/com/lp/client/res/note_find24x24.png",
					LPMain.getTextRespectUISPr("anf.anfrage"),
					LPMain.getTextRespectUISPr("anf.modulname.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_BESTELLUNG)) {
			// Bestellung
			createLPModul(menu, LocaleFac.BELEGART_BESTELLUNG,
					"/com/lp/client/res/shoppingcart_full24x24.png",
					LPMain.getTextRespectUISPr("lp.bestellung"),
					LPMain.getTextRespectUISPr("bes.bestellung.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
			// Bestellung
			createLPModul(menu, LocaleFac.BELEGART_EINGANGSRECHNUNG,
					"/com/lp/client/res/hand_money24x24.png",
					LPMain.getTextRespectUISPr("er.modulname"),
					LPMain.getTextRespectUISPr("er.modulname.tooltip"));
		} else if (which.equals(MODULNAME_MANDANT)) {
			// Mandant wechsel
			createLPModul(menu, MODULNAME_MANDANT,
					"/com/lp/client/res/hat_gray24x24.png",
					LPMain.getTextRespectUISPr("lp.mandantwechsel"),
					LPMain.getTextRespectUISPr("lp.mandantwechsel"));
		} else if (which.equals(LocaleFac.BELEGART_ANGEBOT)) {
			// Angebot
			createLPModul(menu, LocaleFac.BELEGART_ANGEBOT,
					"/com/lp/client/res/presentation_chart24x24.png",
					LPMain.getTextRespectUISPr("angb.angebot"),
					LPMain.getTextRespectUISPr("angb.modulname.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_AGSTUECKLISTE)) {
			// Angebot
			createLPModul(menu, LocaleFac.BELEGART_AGSTUECKLISTE,
					"/com/lp/client/res/note_add24x24.png",
					LPMain.getTextRespectUISPr("lp.angebotsstueckliste"),
					LPMain.getTextRespectUISPr("lp.angebotsstueckliste"));
		} else if (which.equals(LocaleFac.BELEGART_LOS)) {
			// Fertigung
			createLPModul(menu, LocaleFac.BELEGART_LOS,
					"/com/lp/client/res/factory24x24.png",
					LPMain.getTextRespectUISPr("fert.modulname"),
					LPMain.getTextRespectUISPr("fert.modulname.tooltip"));

			if (darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LOS)
					&& (DelegateFactory.getInstance().getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_FERT_LOS_SCHNELLANLAGE))) {
				createLPModul(
						menu,
						MODULNAME_LOS_SCHNELLANLAGE,
						"/com/lp/client/res/flash.png",
						LPMain.getTextRespectUISPr("fert.los.schnellanlage.menu"),
						LPMain.getTextRespectUISPr("fert.los.schnellanlage.menu"));
			}

		} else if (which.equals(LocaleFac.BELEGART_KUECHE)) {
			// Fertigung
			createLPModul(menu, LocaleFac.BELEGART_KUECHE,
					"/com/lp/client/res/office-building24x24.png",
					LPMain.getTextRespectUISPr("kue.modulname"),
					LPMain.getTextRespectUISPr("kue.modulname"));
		} else if (which.equals(LocaleFac.BELEGART_INSTANDHALTUNG)) {
			// Instandhaltung
			createLPModul(menu, LocaleFac.BELEGART_INSTANDHALTUNG,
					"/com/lp/client/res/houses.png",
					LPMain.getTextRespectUISPr("is.instandhaltung"),
					LPMain.getTextRespectUISPr("is.instandhaltung"));
		} else if (which.equals(LocaleFac.BELEGART_PROJEKT)) {
			// Projekt
			createLPModul(menu, LocaleFac.BELEGART_PROJEKT,
					"/com/lp/client/res/briefcase2_document24x24.png",
					LPMain.getTextRespectUISPr("lp.projekt.modulname"),
					LPMain.getTextRespectUISPr("lp.projekt.tooltip"));
		} else if (which.equals(LocaleFac.BELEGART_ZUTRITT)) {
			// Zutritt
			createLPModul(menu, LocaleFac.BELEGART_ZUTRITT,
					"/com/lp/client/res/id_card.png",
					LPMain.getTextRespectUISPr("pers.zutritt.modulname"),
					LPMain.getTextRespectUISPr("pers.zutritt.modulname"));
		} else if (which.equals(LocaleFac.BELEGART_REKLAMATION)) {
			// Zutritt
			createLPModul(menu, LocaleFac.BELEGART_REKLAMATION,
					"/com/lp/client/res/exchange24x24.png",
					LPMain.getTextRespectUISPr("rekla.modulname"),
					LPMain.getTextRespectUISPr("rekla.modulname"));
		} else if (which.equals(LocaleFac.BELEGART_EMAIL)) {
			// Partner
			createLPModul(menu, LocaleFac.BELEGART_EMAIL,
					"/com/lp/client/res/mail_earth24x24.png",
					LPMain.getTextRespectUISPr("media.inbox.auswahl"),
					LPMain.getTextRespectUISPr("tooltip.email"));
		}
	}

	private void createMenueAnsicht(JMenuBar menuBar)
			throws UnsupportedLookAndFeelException, IllegalAccessException,
			InstantiationException, ClassNotFoundException {

		JMenu ansichtMenu = new JMenu(LPMain.getTextRespectUISPr("lp.ansicht"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			ansichtMenu.setName(HelperClient.COMP_NAME_MENU_ANSICHT);
		}
		menuBar.add(ansichtMenu);

		// Ansicht / Funktionsleiste
		menuItemAnsichtToolbar = new JCheckBoxMenuItem(
				LPMain.getTextRespectUISPr("lp.symbolleiste"));
		menuItemAnsichtToolbar.setSelected(true);
		menuItemAnsichtToolbar.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtToolbar);

		// Ansicht / Statusleiste
		// statusItem = new JCheckBoxMenuItem(LPMain.getInstance().
		// getTextRespectUISPr("lp.statusleiste"));
		// statusItem.setSelected(true);
		// statusItem.addActionListener(this);
		// ansichtMenu.add(statusItem);
		// ansichtMenu.addSeparator();

		// Ansicht / Naechstes
		menuItemAnsichtNext = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.naechstes"));
		menuItemAnsichtNext.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtNext);

		// Ansicht / Ueberlappend
		menuItemAnsichtCascade = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.ueberlappend"));
		menuItemAnsichtCascade.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtCascade);

		// Ansicht / Nebeneinander
		menuItemAnsichtTile = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.nebeneinander"));
		menuItemAnsichtTile.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtTile);
		ansichtMenu.addSeparator();

		// Ansicht / speichern
		menuItemAnsichtSave = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.layout.speichern"));
		menuItemAnsichtSave.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtSave);

		// Ansicht / ladem
		menuItemAnsichtLoad = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.layout.load"));
		menuItemAnsichtLoad.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtLoad);

		// Ansicht / loeschen
		menuItemAnsichtReset = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.layout.reset"));
		menuItemAnsichtReset.addActionListener(this);
		ansichtMenu.add(menuItemAnsichtReset);
		ansichtMenu.addSeparator();
		// Schrifteinstellungen / loeschen
		menuItemSchriftart = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.layout.schrift.art"));
		menuItemDirekthilfe = new JCheckBoxMenuItem(
				LPMain.getTextRespectUISPr("lp.label.direkthilfe"));
		menuItemSchriftart.addActionListener(this);
		menuItemDirekthilfe.addActionListener(this);
		menuItemDirekthilfe.setSelected(Defaults.getInstance().isDirekthilfeVisible());
		menuItemDirekthilfe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		menuItemDirekthilfe.setEnabled(Defaults.getInstance().isDirekthilfeEnabled());
		ansichtMenu.add(menuItemSchriftart);
		ansichtMenu.add(menuItemDirekthilfe);
		ansichtMenu.addSeparator();

		// Skin
		// skinItem = new JRadioButtonMenuItem(LPMain.getInstance().
		// getTextRespectUISPr("lp.look.skin"), true);
		// skinItem.addItemListener(this);
		// ansichtMenu.add(skinItem);
		// UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
		// SwingUtilities.updateComponentTreeUI(this);

		// Kunststoff
		menuItemLFKunststoff = new JRadioButtonMenuItem(
				LPMain.getTextRespectUISPr("lp.look.kunststoff"));
		menuItemLFKunststoff.addItemListener(this);
		ansichtMenu.add(menuItemLFKunststoff);

		// default
		// UIManager.setLookAndFeel(UIManager.
		// getSystemLookAndFeelClassName());
		// UIManager.setLookAndFeel(
		// "com.incors.plaf.kunststoff.KunststoffLookAndFeel");
		SwingUtilities.updateComponentTreeUI(this);

		// System
		menuItemLFSystem = new JRadioButtonMenuItem(
				LPMain.getTextRespectUISPr("lp.look.system"));
		menuItemLFSystem.addItemListener(this);
		ansichtMenu.add(menuItemLFSystem);

		// Java
		menuItemAnsichtLFJava = new JRadioButtonMenuItem(
				LPMain.getTextRespectUISPr("lp.look.java"));
		menuItemAnsichtLFJava.addItemListener(this);
		ansichtMenu.add(menuItemAnsichtLFJava);
		/*
		 * // Motif motifItem = new JRadioButtonMenuItem(LPMain.getInstance().
		 * getTextRespectUISPr( "lp.look.motif"));
		 * motifItem.addItemListener(this); ansichtMenu.add(motifItem);
		 *
		 * // Windows winItem = new JRadioButtonMenuItem(LPMain.getInstance().
		 * getTextRespectUISPr("lp.look.windows"));
		 * winItem.addItemListener(this); ansichtMenu.add(winItem);
		 */
		// ButtonGroup erstellen
		buttonGroupLF = new ButtonGroup();
		buttonGroupLF.add(menuItemLFKunststoff);
		buttonGroupLF.add(menuItemAnsichtLFJava);
		buttonGroupLF.add(menuItemLFSystem);

		// buttonGroup.add(motifItem);
		// buttonGroup.add(winItem);
		// buttonGroup.add(skinItem);

		// aktuell gesetzten LAF im Menu aktivieren.
		String sCurrentLafClassName = javax.swing.UIManager.getLookAndFeel()
				.getClass().getName();
		if (sCurrentLafClassName.equals(javax.swing.UIManager
				.getSystemLookAndFeelClassName())) {
			menuItemLFSystem.setSelected(true);
		} else if (sCurrentLafClassName.equals(javax.swing.UIManager
				.getCrossPlatformLookAndFeelClassName())) {
			menuItemAnsichtLFJava.setSelected(true);
		} else if (sCurrentLafClassName.equals(LAF_CLASS_NAME_KUNSTSTOFF)) {
			menuItemLFKunststoff.setSelected(true);
		} else {
			myLogger.warn("Kein Men\u00FCeintrag f\u00FCr LookAndFeel: "
					+ sCurrentLafClassName);
		}
	}

	private void createMenueHelp(JMenuBar menuBar) {
		JMenu hilfeMenu = new JMenu(LPMain.getTextRespectUISPr("lp.hilfe"));
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			hilfeMenu.setName(HelperClient.COMP_NAME_MENU_HILFE);
		}
		menuBar.add(hilfeMenu);

		menuItemHilfeOnline = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.info.online"));
		menuItemHilfeOnline.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F1, 0));
		menuItemHilfeOnline.addActionListener(this);
		hilfeMenu.add(menuItemHilfeOnline);

		menuItemHilfeAnwenderspezifisch = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.anwenderspezifischehilfe"));
		menuItemHilfeAnwenderspezifisch.addActionListener(this);
		hilfeMenu.add(menuItemHilfeAnwenderspezifisch);

		hilfeMenu.addSeparator();

		menuItemHilfeScreenshotDrucken = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.info.screenshotdrucken"));
		menuItemHilfeScreenshotDrucken.addActionListener(this);
		menuItemHilfeScreenshotDrucken.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F12, 0));

		if (java.awt.Desktop.isDesktopSupported()
				&& java.awt.Desktop.getDesktop().isSupported(
						java.awt.Desktop.Action.PRINT)) {
			String s = System.getProperty("os.name");
			if (s != null && !s.equals("Windows XP")) {
				hilfeMenu.add(menuItemHilfeScreenshotDrucken);
			}
		}

		menuItemHilfeScreenshotsenden = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.info.screenshotversenden"));
		menuItemHilfeScreenshotsenden.addActionListener(this);

		if (darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAILVERSAND)) {
			hilfeMenu.add(menuItemHilfeScreenshotsenden);

			hilfeMenu.addSeparator();
		}

		menuItemHilfeAbout = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.info"));
		menuItemHilfeAbout.addActionListener(this);
		hilfeMenu.add(menuItemHilfeAbout);
	}

	/**
	 * createLPModul
	 *
	 * @param modulMenu
	 *            JMenu
	 * @param lPModulename
	 *            String
	 * @param iconFilename
	 *            String
	 * @param lPModulenameItem
	 *            String
	 * @param lpModuleTooltip
	 *            String
	 * @throws Throwable
	 */
	private void createLPModul(JMenu modulMenu, String lPModulename,
			String iconFilename, String lPModulenameItem, String lpModuleTooltip)
			throws Throwable {

		LPModul lpModule = new LPModul();
		hmOflPModule.put(lPModulename, lpModule);

		JCheckBoxMenuItem menueItem = new JCheckBoxMenuItem(lPModulenameItem);
		menueItem.setActionCommand(lPModulename);
		menueItem.setToolTipText(lpModuleTooltip);
		menueItem.addActionListener(this);

		ImageIcon iicon = new ImageIcon(getClass().getResource(iconFilename));
		menueItem.setIcon(iicon);

		modulMenu.add(menueItem);
		((LPModul) hmOflPModule.get(lPModulename)).setJMenuItem(menueItem);

		ToolbarButton toolbarButton = null;
		toolbarButton = new ToolbarButton(lPModulename, lpModuleTooltip, iicon);
		toolbarButton.getJbuRestore().addActionListener(this);
		toolbarButton.getJbuStart().addActionListener(this);
		((LPModul) hmOflPModule.get(lPModulename))
				.setToolbarButton(toolbarButton);

		// final JPopupMenu popup = new JPopupMenu();
		// JMenuItem hideItem = new
		// JMenuItem(LPMain.getTextRespectUISPr("lp.verstecke.modulbutton"));
		// hideItem.setActionCommand(HIDE_MODULBTN + lPModulename);
		// hideItem.addActionListener(this);
		//
		// popup.add(hideItem);
		// toolbarButton.getJbuStart().addMouseListener(new MouseAdapter() {
		// @Override
		// public void mousePressed(MouseEvent e) {
		// showPopup(e);
		// }
		//
		// @Override
		// public void mouseReleased(MouseEvent e) {
		// showPopup(e);
		// }
		//
		// private void showPopup(MouseEvent e) {
		// if(e.isPopupTrigger())
		// popup.show(e.getComponent(), e.getX(), e.getY());
		// }
		// });

		toolbar.add(toolbarButton);
	}

	public InternalFrame getLPModul(String which) {
		JInternalFrame jIF = (JInternalFrame) ((LPModul) hmOflPModule
				.get(which)).getLPModule();
		return (InternalFrame) jIF;
	}

	public boolean isModulEnabled(String which) {
		LPModul modul = hmOflPModule.get(which);
		return modul == null ? false
				: modul.getStatus() == LPModul.STATUS_ENABLED;
	}

	/**
	 * createLPModul
	 *
	 * @param which
	 *            JMenu
	 * @return JInternalFrame
	 */
	public JInternalFrame createALPModul(String which) {

		JInternalFrame jif = null;
		try {
			jif = null;
			if (which.equals(LocaleFac.BELEGART_AUFTRAG)) {
				// Auftrag
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AUFT_AUFTRAG_CUD)) {
					jif = new InternalFrameAuftrag(
							LPMain.getTextRespectUISPr("auft.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameAuftrag(
							LPMain.getTextRespectUISPr("auft.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_ARTIKEL)) {
				// Artikel
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_ARTIKEL_CUD)) {
					jif = new InternalFrameArtikel(
							LPMain.getTextRespectUISPr("artikel.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameArtikel(
							LPMain.getTextRespectUISPr("artikel.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_STUECKLISTE)) {
				// Stueckliste
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_STK_STUECKLISTE_CUD)) {
					jif = new InternalFrameStueckliste(
							LPMain.getTextRespectUISPr("stkl.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameStueckliste(
							LPMain.getTextRespectUISPr("stkl.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_PERSONAL)) {
				// Personal
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_PERSONAL_CUD)) {
					jif = new InternalFramePersonal(
							LPMain.getTextRespectUISPr("personal.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFramePersonal(
							LPMain.getTextRespectUISPr("personal.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_REKLAMATION)) {
				// Personal
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_REKLA_REKLAMATION_CUD)) {
					jif = new InternalFrameReklamation(
							LPMain.getTextRespectUISPr("rekla.modulname"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameReklamation(
							LPMain.getTextRespectUISPr("rekla.modulname"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			}

			else if (which.equals(LocaleFac.BELEGART_ZUTRITT)) {
				// Personal
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZUTRITT_CUD)) {
					jif = new InternalFrameZutritt(
							LPMain.getTextRespectUISPr("pers.zutritt.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameZutritt(
							LPMain.getTextRespectUISPr("pers.zutritt.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_BENUTZER)) {
				// Zeiterfassung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_BENUTZER_CUD)) {
					jif = new InternalFrameBenutzer(
							LPMain.getTextRespectUISPr("benutzer.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameBenutzer(
							LPMain.getTextRespectUISPr("benutzer.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_ZEITERFASSUNG)) {
				// Personal
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD)) {
					jif = new InternalFrameZeiterfassung(
							LPMain.getTextRespectUISPr("zeiterfassung.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameZeiterfassung(
							LPMain.getTextRespectUISPr("zeiterfassung.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_PARTNER)) {
				// Partner
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_PARTNER_CUD)) {
					jif = new InternalFramePartner(
							LPMain.getTextRespectUISPr("part.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFramePartner(
							LPMain.getTextRespectUISPr("part.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
				// Lieferschein
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)) {
					jif = new InternalFrameLieferschein(
							LPMain.getTextRespectUISPr("ls.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameLieferschein(
							LPMain.getTextRespectUISPr("ls.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_RECHNUNG)) {
				// Rechnung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_RECH_RECHNUNG_CUD)) {
					jif = new InternalFrameRechnung(
							LPMain.getTextRespectUISPr("rechnung.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameRechnung(
							LPMain.getTextRespectUISPr("rechnung.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
				// Finanzbuchhaltung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FB_FINANZ_CUD)) {
					jif = new InternalFrameFinanz(
							LPMain.getTextRespectUISPr("finanz.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameFinanz(
							LPMain.getTextRespectUISPr("finanz.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_SYSTEM)) {
				// System
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_SYSTEM_CUD)) {
					jif = new InternalFrameSystem(
							LPMain.getTextRespectUISPr("lp.system.modulname.kurz"), // titlp
							// :
							// 0
							// Modulname
							// setzen
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameSystem(
							LPMain.getTextRespectUISPr("lp.system.modulname.kurz"), // titlp
							// :
							// 0
							// Modulname
							// setzen
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_KUNDE)) {
				// Kunde
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_KUNDE_CUD)) {
					jif = new InternalFrameKunde(
							LPMain.getTextRespectUISPr("part.kund.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameKunde(
							LPMain.getTextRespectUISPr("part.kund.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_LIEFERANT)) {
				// Lieferant
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_LIEFERANT_CUD)) {
					jif = new InternalFrameLieferant(
							LPMain.getTextRespectUISPr("lp.lieferant.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameLieferant(
							LPMain.getTextRespectUISPr("lp.lieferant.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_ANFRAGE)) {
				// Anfrage
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ANF_ANFRAGE_CUD)) {
					jif = new InternalFrameAnfrage(
							LPMain.getTextRespectUISPr("anf.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameAnfrage(
							LPMain.getTextRespectUISPr("anf.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_BESTELLUNG)) {
				// Bestellung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_BES_BESTELLUNG_CUD)) {
					jif = new InternalFrameBestellung(
							LPMain.getTextRespectUISPr("bes.modulname.bestellung.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameBestellung(
							LPMain.getTextRespectUISPr("bes.modulname.bestellung.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
				// Bestellung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD)) {
					jif = new InternalFrameEingangsrechnung(
							LPMain.getTextRespectUISPr("er.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameEingangsrechnung(
							LPMain.getTextRespectUISPr("er.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_ANGEBOT)) {
				// Bestellung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_CUD)) {
					jif = new InternalFrameAngebot(
							LPMain.getTextRespectUISPr("angb.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameAngebot(
							LPMain.getTextRespectUISPr("angb.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_AGSTUECKLISTE)) {
				// Bestellung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AS_ANGEBOTSTKL_CUD)) {
					jif = new InternalFrameAngebotstkl(
							LPMain.getTextRespectUISPr("as.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameAngebotstkl(
							LPMain.getTextRespectUISPr("as.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_LOS)) {
				// Bestellung
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {
					jif = new InternalFrameFertigung(
							LPMain.getTextRespectUISPr("fert.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameFertigung(
							LPMain.getTextRespectUISPr("fert.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_KUECHE)) {
				// Kueche
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_KUE_KUECHE_CUD)) {
					jif = new InternalFrameKueche(
							LPMain.getTextRespectUISPr("kue.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameKueche(
							LPMain.getTextRespectUISPr("kue.modulname.kurz"),
							which, RechteFac.RECHT_KUE_KUECHE_R);
				}
			} else if (which.equals(LocaleFac.BELEGART_INSTANDHALTUNG)) {
				// Kueche
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_IS_INSTANDHALTUNG_CUD)) {
					jif = new InternalFrameInstandhaltung(
							LPMain.getTextRespectUISPr("is.instandhaltung.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameInstandhaltung(
							LPMain.getTextRespectUISPr("is.instandhaltung.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_INSERAT)) {
				// Inserat
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_IV_INSERAT_CUD)) {
					jif = new InternalFrameInserat(
							LPMain.getTextRespectUISPr("iv.inserat.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameInserat(
							LPMain.getTextRespectUISPr("iv.inserat.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_PROJEKT)) {
				// Projekt
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PROJ_PROJEKT_CUD)) {
					jif = new InternalFrameProjekt(
							LPMain.getTextRespectUISPr("lp.projekt.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameProjekt(
							LPMain.getTextRespectUISPr("lp.projekt.modulname.kurz"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			} else if (which.equals(LocaleFac.BELEGART_EMAIL)) {
				// Personal
				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_MEDIA_EMAIL_CUD)) {
					jif = new InternalFrameMedia(
							LPMain.getTextRespectUISPr("media.inbox.modulname"),
							which, RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {
					jif = new InternalFrameMedia(
							LPMain.getTextRespectUISPr("media.inbox.modulname"),
							which, RechteFac.RECHT_MODULWEIT_READ);
				}
			}
		} catch (Throwable ex) {
			new DialogError(this, ex, DialogError.TYPE_ERROR);
			myLogger.error("", ex);
		}
		return jif;
	}

	public void exitClientNowErrorDlg(Throwable t) {
		myLogger.error(LPMain.getTextRespectUISPr("lp.error.fatal"), t);
		if (t != null) {
			new DialogError(this, t, DialogError.TYPE_ERROR);
		}
		System.exit(0);
	}

	public void exitClientDlg() {
		String pattern = LPMain.getTextRespectUISPr("lp.beenden.lp");
		String sMsg = MessageFormat.format(pattern,
				new Object[] { LPMain.getSVersionHV() });

		Object[] options = { LPMain.getTextRespectUISPr("lp.ja"),
				LPMain.getTextRespectUISPr("lp.nein") };
		if ((JOptionPane.showOptionDialog(this, sMsg, "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0])) == JOptionPane.YES_OPTION) {
			if (LPMain.getInstance().getCNrUser() != null) {
				try {
					if (doLogout() == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} catch (Throwable t) {
					exitClientNowErrorDlg(t);
				}
			} else {
				System.exit(0);
			}
		}
	}

	private void placeInternalFrameOnFrame(JInternalFrame iframe)
			throws PropertyVetoException {

		// Groesse setzen und Layout darauf neu erstellen

		ClientPerspectiveManager.getInstance().addInternalFrame(
				(InternalFrame) iframe);
		iframe.setSize(getInternalFrameSize());
		// FEHLER - bringt Fehler bei TabbedPanes, welche Null-Components
		// enthalten
		// iframe.pack();

		desktopPane.add(iframe);

		// Listener hinzufuegen, um schliessen des Rahmens zu bestaetigen
		iframe.addVetoableChangeListener(this);
		iframe.addInternalFrameListener(this);

		// Rahmen positionieren und anzeigen
		int width = getInternalFrameSize().width;
		int height = getInternalFrameSize().height;
		if (nextFrameX + width > desktopPane.getWidth()) {
			nextFrameX = 0;
		}
		if (nextFrameY + height > desktopPane.getHeight()) {
			nextFrameY = 0;
			// Beim ersten Mal den Abstand zwischen den ueberlappenden Rahmen
			// berechnen
		}

		if (frameDistance == 0) {
			frameDistance = (iframe.getHeight() - iframe.getContentPane()
					.getHeight()) / 2;
		}
		iframe.reshape(nextFrameX, nextFrameY, getInternalFrameSize().width,
				getInternalFrameSize().height);
		iframe.show();

		// Position fuer naechsten Rahmen berechnen
		nextFrameX += frameDistance;
		nextFrameY += frameDistance;

		// Rahmen auswaehlen - kann abgelehnt werden
		iframe.setSelected(true);
	}

	/**
	 * kaskadiere Windows
	 *
	 * @throws PropertyVetoException
	 */
	private void cascadeWindows() throws PropertyVetoException {
		JInternalFrame[] iframes = desktopPane.getAllFrames();
		int x = 0;
		int y = 0;
		int width = HelperClient.getInternalFrameSize().width;
		int height = HelperClient.getInternalFrameSize().height;

		for (int i = 0; i < iframes.length; i++) {
			if (!iframes[i].isIcon()) {
				iframes[i].setMaximum(false);
				iframes[i].reshape(x, y,
						HelperClient.getInternalFrameSize().width,
						HelperClient.getInternalFrameSize().height);
				iframes[i].setSelected(true);
				iframes[i].toFront();

				x += frameDistance;
				y += frameDistance;
				// Am Desktoprand, wieder von vorn beginnen
				if (x + width > desktopPane.getWidth()) {
					x = 0;
				}
				if (y + height > desktopPane.getHeight()) {
					y = 0;
				}
			}
		}
	}

	/**
	 * Alle Windows nebeneinander anzeigen.
	 *
	 * @throws Throwable
	 */
	private void tileWindows() throws Throwable {

		JInternalFrame[] nOfFrames = desktopPane.getAllFrames();
		if (nOfFrames.length > 1) {
			// Rahmen zaehlen, die nicht minimiert sind
			int nOfOpenFrames = 0;
			for (int i = 0; i < nOfFrames.length; i++) {
				if (!nOfFrames[i].isIcon()) {
					nOfOpenFrames++;
				}
			}
			if (nOfOpenFrames > 1) {

				int rows = (int) Math.sqrt(nOfOpenFrames);
				int cols = nOfOpenFrames / rows;
				int extra = nOfOpenFrames % rows;
				// Anzahl der Spalten mit einer zusaetzlichen Zeile
				int width = desktopPane.getWidth() / cols;
				int height = desktopPane.getHeight() / rows;
				int r = 0;
				int c = 0;
				for (int i = 0; i < nOfFrames.length; i++) {
					if (!nOfFrames[i].isIcon()) {
						nOfFrames[i].setMaximum(false);
						nOfFrames[i].reshape(c * width, r * height, width,
								height);
						r++;
						if (r == rows) {
							r = 0;
							c++;
							if (c == cols - extra) {
								// Zusaetzliche Zeile hinzufuegen
								rows++;
								height = desktopPane.getHeight() / rows;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Naechste nicht iconisiertes Window anzeigen
	 *
	 * @throws Throwable
	 */
	private void selectNextWindow() throws Throwable {

		JInternalFrame[] nOfFrames = desktopPane.getAllFrames();
		if (nOfFrames.length > 1) {
			JInternalFrame js = desktopPane.getSelectedFrame();
			int l = desktopPane.getComponentCount();
			int i = 0;
			while (i < l) {
				if (desktopPane.getComponent(i) == js) {
					break;
				}
				i++;
			}
			int next = i + 1;
			boolean found = false;
			while (next != i && !found) {
				try {
					found = !((JInternalFrame) desktopPane.getComponent(next))
							.isIcon();
				} catch (Throwable t) {
					found = false;
					// isIcon; nothing here
				}
				if (!found) {
					next = (++next) % l;
				}
			}

			if (!((JInternalFrame) desktopPane.getComponent(next)).isIcon()) {
				((JInternalFrame) desktopPane.getComponent(next))
						.setSelected(true);
				((JInternalFrame) desktopPane.getComponent(next)).toFront();
			}

			// JO buggy ;-) wurde aus Buch abgeschrieben!
			// JInternalFrame[] iframes = desktopPane.getAllFrames();
			// int l = iframes.length;
			//
			// if (l > 1) {
			// for (int i = 0; i < l; i++) {
			// if (iframes[i].isSelected()) {
			// // Suchen nach naechstem Rahmen, der nicht minimiert ist
			// // und ausgewaehlt werden kann.
			// int next = (i + 1) % l;
			// while (next != i && iframes[next].isIcon()) {
			// next = (next + 1) % l;
			// }
			// if (next == i) {
			// return;
			// }
			// iframes[next].setSelected(true);
			// iframes[next].toFront();
			// return;
			// }
			// }
			// }
		}
	}

	/**
	 * evtvet: Vetoereignisse behandeln.
	 *
	 * @param event
	 *            PropertyChangeEvent
	 * @throws PropertyVetoException
	 */
	public void vetoableChange(java.beans.PropertyChangeEvent event)
			throws PropertyVetoException {

		if (event.getNewValue() != null
				&& ((Boolean) event.getNewValue()).booleanValue() == true) {
			String lPModulename = null;
			InternalFrame jInternalFrameModul = (InternalFrame) (JInternalFrame) event
					.getSource();
			lPModulename = jInternalFrameModul.getBelegartCNr();

			LPModul lPModul = (LPModul) hmOflPModule.get(lPModulename);

			if (event.getPropertyName().equals(
					JInternalFrame.IS_SELECTED_PROPERTY)) {
				// lPModul.setStatus(LPModul.STATUS_DISABLED);
			} else if (event.getPropertyName().equals(
					JInternalFrame.IS_CLOSED_PROPERTY)) {
				PropertyVetoException pve = null;
				try {
					pve = jInternalFrameModul.vetoableChangeLP();
				} catch (Throwable ex) {
					myLogger.error(ex.getMessage(), ex);

//					lPModul.setStatus(LPModul.STATUS_ENABLED);
//					lPModul.setLPModule(null);

//					jInternalFrameModul.dispose();
//					jInternalFrameModul.handleException(ex, false) ;

					new DialogError(this, ex, DialogError.TYPE_INFORMATION);

					throw new PropertyVetoException("", null);
				}

				if (pve == null) {
					lPModul.setStatus(LPModul.STATUS_ENABLED);
					lPModul.setOpenModules(null);
					allePanelsFinalizen(jInternalFrameModul);
					jInternalFrameModul.dispose();
				} else {
					throw new PropertyVetoException("", null);
				}
			}
		}
	}

	private void allePanelsFinalizen(InternalFrame internalFrame) {

		JTabbedPane tabbedPaneRoot = internalFrame.getTabbedPaneRoot();

		if (internalFrame.getTabbedPaneRoot().getComponentCount() > 0) {
			for (int i = 0; i < tabbedPaneRoot.getComponents().length; i++) {
				if (tabbedPaneRoot.getComponents()[i] instanceof TabbedPane) {

					TabbedPane tabbedpane = (TabbedPane) tabbedPaneRoot
							.getComponents()[i];
					for (int u = 0; u < tabbedpane.getComponents().length; u++) {
						if (tabbedpane.getComponents()[u] instanceof PanelBasis) {
							PanelBasis pb = (PanelBasis) tabbedpane
									.getComponents()[u];
							try {
								pb.finalize();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	public void removeItems() {
		JMenuBar menu = super.getJMenuBar();
		try {
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
			menu.remove(menu.getComponent(1));
		} catch (ArrayIndexOutOfBoundsException ex) {
			// nothing here
		}

		for (int i = toolbar.getComponentCount(); i > 0; i--) {

			if (toolbar.getComponentAtIndex(i) instanceof ToolbarButton) {
				ToolbarButton tb = (ToolbarButton) toolbar
						.getComponentAtIndex(i);
				String ac = tb.getActionCommand();

				if (ac.equals(LocaleFac.BELEGART_ANFRAGE)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_ANGEBOT)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_ARTIKEL)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_STUECKLISTE)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_AUFTRAG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_AGSTUECKLISTE)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_BENUTZER)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_BESTELLUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_KUNDE)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_LIEFERANT)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_LOS)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_KUECHE)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_INSTANDHALTUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_INSERAT)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_REKLAMATION)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_PARTNER)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_PERSONAL)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_PROFORMARECHNUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_RECHNUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_REPARATUR)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_RUECKLIEFERUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_RUECKSCHEIN)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_SYSTEM)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_ZEITERFASSUNG)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_PROJEKT)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_ZUTRITT)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_REKLAMATION)) {
					toolbar.remove(i);
				} else if (ac.equals(MODULNAME_LOS_SCHNELLANLAGE)) {
					toolbar.remove(i);
				} else if (ac.equals(LocaleFac.BELEGART_EMAIL)) {
					toolbar.remove(i);
				}

			} else if (toolbar.getComponentAtIndex(i) instanceof JSeparator) {
				toolbar.remove(i);
			}
		}
		setTitle(LPMain.getSVersionHVAllTogether());
	}


	// private void setToolbarButtonVisible(boolean state, String modulname) {
	// LPModul modul = (LPModul) hmOflPModule.get(modulname);
	// if(modul != null) modul.getToolbarButton().setVisible(state);
	// }

	/**
	 * Actionlistener.
	 *
	 * @param evt
	 *            ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent evt) {
		long tStart = System.currentTimeMillis();
		myLogger.info("action start: "
				+ Helper.cutString(evt.toString(), Defaults.LOG_LENGTH));
		// Cursor auf Warten
		if (Defaults.getInstance().isUseWaitCursor()) {
			setCursor(java.awt.Cursor
					.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
		}
		// wenn Backgroundimage vorhanden, dann in den Hinertgrund schieben
		if (jpaBackground != null) {
			desktopPane.moveToBack(jpaBackground);
		}

		Object source = evt.getSource();
		String action = evt.getActionCommand();
		try {
			if (action != null) {
				/*
				 * if (source == menuItemTooltip) { if
				 * (menuItemTooltip.isSelected()) {
				 * ToolTipManager.sharedInstance().setEnabled(true); } else {
				 * ToolTipManager.sharedInstance().setEnabled(false); } } else
				 */
				if (source == menuItemAnsichtToolbar) {
					toolbar.setVisible(!(toolbar.isVisible()));
				} else if (source == menuItemAnsichtNext) {
					selectNextWindow();
				} else if (source == menuItemAnsichtCascade) {
					cascadeWindows();
				} else if (source == menuItemAnsichtTile) {
					tileWindows();
				} else if (source == menuItemAnsichtSave) {
					ClientPerspectiveManager.getInstance().save();
				} else if (source == menuItemAnsichtLoad) {
					if (ClientPerspectiveManager.getInstance()
							.doLayoutSettingsExist())
						closeAllFrames();
					ClientPerspectiveManager.getInstance().reinit();
					if (!ClientPerspectiveManager.getInstance().load(this)) {
						JOptionPane
								.showMessageDialog(
										this,
										LPMain.getTextRespectUISPr("lp.layout.nicht.gefunden"));
					}
				} else if (source == menuItemAnsichtReset) {
					ClientPerspectiveManager.getInstance().deleteSettings();
					ClientPerspectiveManager.getInstance().reinit();
				} else if (source == menuItemSchriftart) {
					if (JOptionPane.YES_OPTION == JOptionPane
							.showConfirmDialog(
									this,
									LPMain.getTextRespectUISPr("lp.layout.schrift.allemoduleschliessen"),
									LPMain.getTextRespectUISPr("lp.warning"),
									JOptionPane.YES_NO_OPTION)) {
						closeAllFrames();
						new PanelDialogFontStyle(this);
					}
				} else if (source == menuItemDirekthilfe) {
					ClientPerspectiveManager.getInstance().setDirekthilfeEnabled(menuItemDirekthilfe.isSelected());
					Defaults.getInstance().setDirekthilfeVisible(menuItemDirekthilfe.isSelected());
					SwingUtilities.updateComponentTreeUI(this);
				} else if (source == menuItemAnsichtExit) {
					exitClientNowErrorDlg(null);
				} else if (source == menuItemHilfeAbout) {
					showAboutDialog();
				} else if (source == menuItemHilfeOnline) {
					showOnlineHelp();
				} else if (source == menuItemHilfeScreenshotsenden) {
					screenshotVersenden(
							this,
							"[Screenshot aus HeliumV \u00FCber Hilfe-Men\u00FC] ",
							"");
				} else if (source == menuItemHilfeScreenshotDrucken) {

					// Screenshot erstellen
					Rectangle screenRectangle = new Rectangle(
							this.getLocation(), this.getSize());
					Robot robot = new Robot();
					BufferedImage image = robot
							.createScreenCapture(screenRectangle);
					Helper.imageToByteArray(image);
					File f = File.createTempFile("scr", ".png");
					javax.imageio.ImageIO.write(image, "png", f);
					f.length();
					java.awt.Desktop.getDesktop().print(f);
					f.deleteOnExit();
				} else if (source == menuItemHilfeAnwenderspezifisch) {
					showAnwenderspezifischeHilfe();
				}

				else {
					try {
						if (action.endsWith(ToolbarButton.ACTION_RESTORE)) {
							JInternalFrame jif = ((LPModul) hmOflPModule
									.get(action.replaceAll(
											ToolbarButton.ACTION_RESTORE, "")))
									.getLPModule();
							jif.setSelected(true);
							desktopPane.getDesktopManager().deiconifyFrame(jif);
							desktopPane.getDesktopManager().activateFrame(jif);
							((InternalFrame) jif).setFirstFocusableComponent();
							// } else if(action.startsWith(HIDE_MODULBTN)) {
							// setToolbarButtonVisible(false,
							// action.replaceAll(HIDE_MODULBTN, ""));
						} else if (action.equals(MODULNAME_LOS_SCHNELLANLAGE)) {
							// PJ17775

							boolean b = DialogFactory
									.showModalJaNeinDialog(
											null,
											LPMain.getTextRespectUISPr("fert.los.schnellanlage.frage"));

							if (b == true) {
								LosDto losDto = new LosDto();
								losDto.setTProduktionsbeginn(Helper
										.cutDate(new java.sql.Date(System
												.currentTimeMillis())));
								losDto.setTProduktionsende(Helper
										.cutDate(new java.sql.Date(System
												.currentTimeMillis())));
								losDto.setMandantCNr(LPMain.getTheClient()
										.getMandant());

								MandantDto mandantDto = DelegateFactory
										.getInstance()
										.getMandantDelegate()
										.mandantFindByPrimaryKey(
												LPMain.getTheClient()
														.getMandant());
								losDto.setKostenstelleIId(mandantDto
										.getIIdKostenstelle());

								losDto.setPartnerIIdFertigungsort(mandantDto
										.getPartnerIId());

								losDto.setNLosgroesse(new BigDecimal(1));
								losDto.setLagerIIdZiel(DelegateFactory
										.getInstance().getLagerDelegate()
										.getHauptlagerDesMandanten().getIId());
								FertigungsgruppeDto[] dtos = DelegateFactory
										.getInstance().getStuecklisteDelegate()
										.fertigungsgruppeFindByMandantCNr();

								if (dtos != null && dtos.length > 0) {
									losDto.setFertigungsgruppeIId(dtos[0]
											.getIId());
								} else {
									DialogFactory
											.showModalDialog(
													"Fehler",
													"Es konnte kein Los angelegt werden, da f\u00FCr diesen Benutzer keine Fertigungsgruppe definiert ist.");
									return;
								}

								losDto = DelegateFactory.getInstance()
										.getFertigungDelegate()
										.updateLos(losDto);

								DelegateFactory
										.getInstance()
										.getFertigungDelegate()
										.gebeLosAus(losDto.getIId(), false,
												null);

								JasperPrintLP print = DelegateFactory
										.getInstance()
										.getFertigungDelegate()
										.printFertigungsbegleitschein(
												losDto.getIId(), true);
								PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
								PrintService psDefault = PrintServiceLookup
										.lookupDefaultPrintService();

								if (psDefault != null) {

									int printableWidth = 0;
									int printableHeight = 0;
									if (print.getPrint().getOrientationValue() == OrientationEnum.LANDSCAPE) {
										printableWidth = print.getPrint()
												.getPageHeight();
										printableHeight = print.getPrint()
												.getPageWidth();
									} else {
										printableWidth = print.getPrint()
												.getPageWidth();
										printableHeight = print.getPrint()
												.getPageHeight();
									}

									if ((printableWidth != 0)
											&& (printableHeight != 0)) {
										printRequestAttributeSet
												.add(new MediaPrintableArea(
														0.0F,
														0.0F,
														printableWidth / 72.0F,
														printableHeight / 72.0F,
														MediaPrintableArea.INCH));
									}

									PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
									printServiceAttributeSet
											.add(new PrinterName(psDefault
													.getName(), null));

									JRPrintServiceExporter exporter = new JRPrintServiceExporter();

									exporter.setParameter(
											JRExporterParameter.JASPER_PRINT,
											print.getPrint());
									exporter.setParameter(
											JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
											printRequestAttributeSet);
									exporter.setParameter(
											JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
											printServiceAttributeSet);
									exporter.setParameter(
											JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
											Boolean.FALSE);
									exporter.setParameter(
											JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
											Boolean.FALSE);
									exporter.setParameter(
											JRPrintServiceExporterParameter.PRINT_SERVICE,
											psDefault);
									exporter.exportReport();

								} else {
									DialogFactory
											.showModalDialog("Fehler",
													"Es ist kein Standarddrucker definiert!");
								}
							}
						} else if (action.equals(MODULNAME_LOGIN)) {
							// MB 15.05.06 wegen mehrfachem dr
							if (dialogLogin == null || !dialogLogin.isVisible()) {

								new ChangesShownController()
										.dialogChangeLogIfNeeded(this);

								// Login
								doLogon(false);

								if (LPMain.getInstance().getBenutzernameRaw() != null) {
									if (!isBAbbruch()) {

										// CK: NAchsehen, ob neuerer Client
										// vorhanden ist:
										if (DelegateFactory.getInstance()
												.getTheClientDelegate()
												.istNeuerClientVerfuegbar()) {

											String lcOSName = System
													.getProperty("os.name")
													.toLowerCase();
											boolean MAC_OS_X = lcOSName
													.startsWith("mac os x");

											if (MAC_OS_X == true) {
												JOptionPane
														.showMessageDialog(
																this,
																"Es ist ein Client-Update verf\u00FCgar. Bitte wenden Sie sich an Ihren Systemadministrator.");
												DelegateFactory
														.getInstance()
														.getLogonDelegate()
														.logout(LPMain
																.getTheClient());
												System.exit(0);

											} else {

												JOptionPane pane = InternalFrame
														.getNarrowOptionPane(Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
												pane.setMessage("Es ist ein Client-Update verf\u00FCgar, wollen Sie es installieren?");
												pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
												pane.setOptionType(JOptionPane.YES_NO_OPTION);
												JDialog dialog = pane
														.createDialog(this,
																"Update");
												dialog.setVisible(true);
												if (pane.getValue() != null
														&& ((Integer) pane
																.getValue())
																.intValue() == JOptionPane.YES_OPTION) {
													// Wenn ja, Version pruefen:
													// Runable Thread fuer
													// Update
													Runnable updateRun = new Runnable() {
														public void run() {
															File f = null;
															try {
																f = File.createTempFile(
																		"hvi",
																		".jar");

																java.io.FileOutputStream out = new java.io.FileOutputStream(
																		f);
																InstallerDto installerDto = null;

																updateBar
																		.setVisible(true);
																updateBar
																		.setValue(0);
																updateBar
																		.setStringPainted(true);
																for (int i = 0; i < 100; i++) {
																	try {
																		installerDto = DelegateFactory
																				.getInstance()
																				.getTheClientDelegate()
																				.getInstallerPart(
																						i);
																	} catch (ExceptionLP e) {
																	}
																	out.write(installerDto
																			.getOClientpc());
																	updateBar
																			.setValue(i);
																}
																out.close();
																ProcessBuilder pb = new ProcessBuilder(
																		"java",
																		"-jar",
																		f.getAbsolutePath(),
																		"-default.install.path="
																				+ System.getProperty("user.dir"));

																String javahome = System
																		.getProperty("java.home");

																pb.directory(new File(
																		javahome
																				+ "/bin"));
																pb.start();

																DelegateFactory
																		.getInstance()
																		.getLogonDelegate()
																		.logout(LPMain
																				.getTheClient());
																System.exit(0);
															} catch (Throwable ex1) {
																ex1.printStackTrace();
															}

														}
													};
													Thread updatethread = new Thread(
															updateRun);
													updatethread.start();
													// Anzeigen des
													// Fortschrittbalkens
													updateBar.setValue(0);
													barPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
													barPane.setOptions(new Object[0]);
													barPane.setMessage("Updating...");
													barPane.setIcon(null);
													barPane.add(updateBar);
													JDialog barDialog = barPane
															.createDialog(
																	barPane,
																	"Update");
													barDialog.setVisible(true);
												} else if (pane.getValue() != null
														&& ((Integer) pane
																.getValue())
																.intValue() == JOptionPane.NO_OPTION) {
													// mit altem Client darf man
													// sich nicht anmelden
													// koennen
													DelegateFactory
															.getInstance()
															.getLogonDelegate()
															.logout(LPMain
																	.getTheClient());
													System.exit(0);
												}

											}

										}
										dc.setModulBerechtigung(DelegateFactory
												.getInstance()
												.getMandantDelegate()
												.modulberechtigungFindByMandantCNr());
										dc.setZusatzFunktionen(DelegateFactory
												.getInstance()
												.getMandantDelegate()
												.zusatzfunktionberechtigungFindByMandantCNr());
										dc.setDarfDirekthilfeTexteEditieren(DelegateFactory
												.getInstance()
												.getTheJudgeDelegate()
												.hatRecht(
														RechteFac.RECHT_LP_DARF_DIREKTHILFETEXTE_BEARBEITEN));

										JMenuBar menuBar = super.getJMenuBar();
										// Modul
										toolbar.addSeparator();
										createModulmenueentries(menuBar);
										toolbar.addSeparator();

										// Extras
										createMenueExtra(menuBar);

										// Ansicht
										createMenueAnsicht(menuBar);

										// Hilfe
										createMenueHelp(menuBar);
										enableAllModule(true);
										((LPModul) hmOflPModule
												.get(MODULNAME_LOGIN))
												.setStatus(LPModul.STATUS_DISABLED);
										ClientPerspectiveManager.getInstance()
												.reinit();
										loadUserSpecificSettings();
										SwingUtilities
												.updateComponentTreeUI(this);
										if (Defaults.getInstance()
												.getLoadLayoutOnLogon())
											ClientPerspectiveManager
													.getInstance().load(this);
									}
								}
							}
						} else if (action.equals(MODULNAME_LOGOUT)) {
							// Logout
							if (doLogout() == JOptionPane.YES_OPTION) {
								// echt logout
								if (fontChanged) {
									if (JOptionPane
											.showConfirmDialog(
													null,
													LPMain.getTextRespectUISPr("lp.layout.schrift.beibehalten"),
													"",
													JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
										ClientPerspectiveManager.getInstance()
												.resetFont();
									}
								}
								reloadFont(false);
								SwingUtilities.updateComponentTreeUI(this);
								removeItems();
								// Logout Timestamp setzen

							}
						} else if (action.equals(MODULNAME_MANDANT)) {
							// Mandantwechsel

//							if (FehlmengenAufloesen.getAufgeloesteFehlmengen().size() > 0) {
//							boolean bOption = DialogFactory
//									.showModalJaNeinDialog(
//											null,
//												LPMain.getTextRespectUISPr("lp.frage.fehlmengenaufloesendrucken"),
//												LPMain.getTextRespectUISPr("fert.los.schnellanlage.frage"));
//
//							if (bOption) {
//
////								getLPModul(null).showReportKriterien(
////								new ReportAufgeloestefehlmengen(null, FehlmengenAufloesen.getAufgeloesteFehlmengen()));
//
//								Desktop.ge
//								new ReportAufgeloestefehlmengen(null, FehlmengenAufloesen.getAufgeloesteFehlmengen()));
//
//							}
//
//
//							}

							if (doLogout() == JOptionPane.YES_OPTION) {
							removeItems();
							doLogon(true);
							if (!isBAbbruch()) {
								dc.setModulBerechtigung(DelegateFactory
										.getInstance()
										.getMandantDelegate()
										.modulberechtigungFindByMandantCNr());
								dc.setZusatzFunktionen(DelegateFactory
										.getInstance()
										.getMandantDelegate()
										.zusatzfunktionberechtigungFindByMandantCNr());
								dc.setDarfDirekthilfeTexteEditieren(DelegateFactory
										.getInstance()
										.getTheJudgeDelegate()
										.hatRecht(
												RechteFac.RECHT_LP_DARF_DIREKTHILFETEXTE_BEARBEITEN));

								DirekthilfeCache.reload();
								JMenuBar menuBar = super.getJMenuBar();
								// Modul
								toolbar.addSeparator();
								createModulmenueentries(menuBar);
								toolbar.addSeparator();

								// Extras
								createMenueExtra(menuBar);
								toolbar.addSeparator();

								// Ansicht
								createMenueAnsicht(menuBar);
								toolbar.addSeparator();

								// Hilfe
								createMenueHelp(menuBar);
								enableAllModule(true);
								((LPModul) hmOflPModule
										.get(MODULNAME_LOGIN))
										.setStatus(LPModul.STATUS_DISABLED);
								ParameterCache.resetAll();
								ClientPerspectiveManager.getInstance()
										.reinit();
								ClientPerspectiveManager.getInstance()
										.load(this);
							}

						}

						} else if (action.equals(MODULNAME_BEENDEN)) {
							// Beenden
							exitClientDlg();
						} else {
							// setToolbarButtonVisible(true, action);
							refreshModul(action);
						}
					} catch (Exception e) {
						if (e.getMessage() != null
								&& e.getMessage()
										.startsWith("Could not obtain")) {
							showModalDialog(
									"",
									LPMain.getTextRespectUISPr("lp.error.no_server")
											+ " ("
											+ System.getProperty("java.naming.provider.url")
											+ ")", JOptionPane.ERROR_MESSAGE);
						} else if (e instanceof ExceptionLP
								&& ((ExceptionLP) e).getICode() == 97) {
							showModalDialog("",
									LPMain.getInstance()
											.getMsg((ExceptionLP) e),
									JOptionPane.ERROR_MESSAGE);
							exitClientNowErrorDlg(null);
						} else {
							exitClientNowErrorDlg(e);
						}
					} catch (Throwable t) {
						exitClientNowErrorDlg(t);
					}
				}
				// aktualisiere Anzeige, funktioniert besser als
				// update(this.getGraphics())
				repaint();
				// Cursor wieder normal
				setCursor(java.awt.Cursor.getDefaultCursor());
			}
		} catch (Throwable t) {
			long tEnd = System.currentTimeMillis();
			myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
			// clientexc: 2 Client gepflegt abbrechen.
			exitClientNowErrorDlg(t);
		} finally {
			long tEnd = System.currentTimeMillis();
			myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
		}

		HelperClient.setComponentNamesMenuBar(super.getJMenuBar());

	}

	/**
	 * Versucht einen Link im Standardbrowser zu &ouml;ffnen, sollte das nicht
	 * klappen, kommt ein Dialog mit der Aufforderung den Link zu kopieren und
	 * in einen Browser einzuf&uuml;gen.
	 *
	 * @param uri
	 * @return true wenn der Browser ge&ouml;ffnet werden konnte, sonst false
	 */
	public static boolean browseURI(String uri) {

		boolean browseWorked = false;
		String errorToken = null;
		if (SystemProperties.isMacOs()) {
			Class<?> fileMgr;
			try {
				fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL",
						new Class[] { String.class });
				openURL.invoke(null, new Object[] { uri });
				browseWorked = true;
			} catch (ClassNotFoundException e) {
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			if (!browseWorked) {
				Runtime runtime = Runtime.getRuntime();
				String[] args = { "osascript", "-e",
						"open location \"" + uri + "\"" };
				try {
					runtime.exec(args);
					browseWorked = true;
				} catch (IOException e) {
					errorToken = "lp.browse.nichtunterstuetzt";
				}
			}
		} else {
			if (java.awt.Desktop.isDesktopSupported()
					&& java.awt.Desktop.getDesktop().isSupported(
							java.awt.Desktop.Action.BROWSE)) {
				try {
					java.awt.Desktop.getDesktop().browse(new URI(uri));
					browseWorked = true;
				} catch (IOException e) {
					errorToken = "lp.browse.nichtunterstuetzt";
				} catch (URISyntaxException e) {
					errorToken = "lp.fehlerhafteurl";
				}
			} else {
				errorToken = "lp.browse.nichtunterstuetzt";
			}
		}
		if (!browseWorked) {
			new DialogBrowseError(LPMain.getInstance().getDesktop(),
					LPMain.getTextRespectUISPrWithNull(errorToken), uri);
		}
		return browseWorked;
	}

	public static void setFontChanged(boolean b) {
		fontChanged = b;
	}

	public InternalFrame holeModul(String belegart) throws Throwable {
		return (InternalFrame) refreshModul(belegart);
	}

	private JInternalFrame refreshModul(String action)
			throws PropertyVetoException, Exception {
		JInternalFrame jif = ((LPModul) hmOflPModule.get(action)).getLPModule();
		if (jif == null) {
			jif = createALPModul(action);
			if (jif != null) {
//				((LPModul) hmOflPModule.get(action)).setLPModule(jif);
				((LPModul) hmOflPModule.get(action)).getOpenModules(jif);
				placeInternalFrameOnFrame(jif);
				((InternalFrame) jif).setFirstFocusableComponent();
			}
		} else {
			jif.setSelected(true);
			desktopPane.getDesktopManager().deiconifyFrame(jif);
			desktopPane.getDesktopManager().activateFrame(jif);
			desktopPane.moveToFront(jif);
			jif.setIcon(false);
			((InternalFrame) jif).setFirstFocusableComponent();
			// der menueeintrag bleibt trotzdem selektiert
			((LPModul) hmOflPModule.get(action)).getJMenuItem().setSelected(
					true);
		}
		return jif;
	}

	/**
	 * showAboutDialog
	 *
	 * @throws Throwable
	 */
	public void showAboutDialog() throws Throwable {

		DialogAbout about = new DialogAbout(this, "Info", true);

		about.setResizable(true);

//		Point p = this.getLocation();
//		double dX = p.getX();
//		double dY = p.getY();
//		p.setLocation(dX + 150, dY + 100);
//		about.setLocation(p);

		// zentriert Fenster relativ zum Hauptfenster
		about.setLocationRelativeTo(desktopPane);

		about.setVisible(true);

	}

	public void showAnwenderspezifischeHilfe() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_URL_ANWENDERSPEZIFISCHE_HILFE,
						ParameterFac.KATEGORIE_ALLGEMEIN,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null
				&& parameter.getCWert().trim().length() > 0) {
			try {

				int i = parameter.getCWert().indexOf("://");
				URI uri = new URI(i < 0 ? "http://" : ""
						+ parameter.getCWert().trim());
				if (uri != null && java.awt.Desktop.isDesktopSupported()) {
					java.awt.Desktop.getDesktop().browse(uri);
				}
			} catch (URISyntaxException ex1) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.fehlerhafteurl"));
			} catch (IOException ex1) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						ex1.getMessage());
			}
		} else {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("lp.keineanwenderspezifischehilfe.definiert"));
		}
	}

	/**
	 * Online Hilfe anzeigen
	 *
	 * @throws Throwable
	 */
	public void showOnlineHelp() throws Throwable {

		try {
			new OnlineHelp();
		} catch (java.lang.ClassFormatError e) {
			// Default Swing Viewer verwenden
			SwingHelpUtilities
					.setContentViewerUI("javax.help.plaf.basic.BasicContentViewerUI");
			new OnlineHelp();
		} catch (Exception e) {
			// Default Swing Viewer verwenden
			SwingHelpUtilities
					.setContentViewerUI("javax.help.plaf.basic.BasicContentViewerUI");
			new OnlineHelp();
		}
	}

	/**
	 *
	 * @return int JOptionPane.YES_OPTION oder JOptionPane.NO_OPTION
	 * @throws Exception
	 */
	public int doLogout() throws Throwable {

		boolean bCloseAll = true;
		int ret = JOptionPane.YES_OPTION;

		dc.behandleOffeneAenderungen(getStatusOffeneLPModule()); 

		if (areLPModuleOpen()) {
			List<ModulStatus> listOffeneModule = getStatusOffeneLPModule();
			Collections.sort(listOffeneModule, new Comparator<ModulStatus>() {
				@Override
				public int compare(ModulStatus o1, ModulStatus o2) {
					return o1.getModulName().compareTo(o2.getModulName());
				}
			}) ;

			StringBuilder sbOffen = new StringBuilder();
			StringBuilder sbLocked = new StringBuilder() ;
			StringBuilder sbMessage = new StringBuilder();

			int i = 0;
			int j = 0;
			for (ModulStatus modulStatus : listOffeneModule) {
				appendString(sbOffen, modulStatus.getLpModule().getModuleName()) ;
				i++;
				if(modulStatus.isLocked()) {
					appendString(sbLocked, modulStatus.getLpModule().getModuleName());
					j++;
				}
			}

			String offeneModule = sbOffen.toString();
			String lockedModule = sbLocked.toString() ;

			JPanel panel = new JPanel();
			panel.setLayout(new MigLayout(
					"width 420, height 200, gap 10 10 10 10"
					));

			appendString(sbMessage, Texts.msgClientLogoutOpenModules(i));
			appendString(sbMessage, Texts.msgClientLogoutLockedModules(j));
			appendString(sbMessage, LPMain.getTextRespectUISPr("lp.warning.open.lpmodule.abmelden"));


			String message = sbMessage.toString();

			JTextArea msgTextArea = new JTextArea(message);
		    msgTextArea.setBackground(null);
		    msgTextArea.setLineWrap(true);
		    msgTextArea.setWrapStyleWord(true);
		    JScrollPane msgScrollPane = new JScrollPane(msgTextArea);
		    msgScrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

			JTextArea offeneModuleTextArea = new JTextArea(offeneModule);
		    offeneModuleTextArea.setBackground(null);
		    offeneModuleTextArea.setLineWrap(true);
		    offeneModuleTextArea.setWrapStyleWord(true);
		    JScrollPane offeneModuleScrollPane = new JScrollPane(offeneModuleTextArea);
		    offeneModuleScrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

			JTextArea lockTextArea = new JTextArea(lockedModule);
		    lockTextArea.setBackground(null);
		    lockTextArea.setLineWrap(true);
		    lockTextArea.setWrapStyleWord(true);
		    JScrollPane lockScrollPane = new JScrollPane(lockTextArea);
		    lockScrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		    JLabel lblOffeneModule = new JLabel("<html><b>" + LPMain.getTextRespectUISPr("lp.warning.open.titel.offen") + "</b></html>");
		    JLabel lblLock = new JLabel("<html><b>" + LPMain.getTextRespectUISPr("lp.warning.open.titel.gesperrt") + "</b></html>");

			panel.add(msgScrollPane, "span, h 65px:65px:80px, growx, wrap");
			panel.add(lblOffeneModule, "spanx 2, top");
			panel.add(lblLock, "spanx 2, top, wrap");
			panel.add(offeneModuleScrollPane, "span 2 2, w 100%, top" );
			panel.add(lockScrollPane, "span2 2, w 100%, top, wrap");

		    JOptionPane op = new JOptionPane(
		    		panel,
		    		JOptionPane.WARNING_MESSAGE,
		    		JOptionPane.YES_NO_OPTION
    		);

		    JDialog dialog = op.createDialog(null, LPMain.getTextRespectUISPr("lp.warning.open.titel"));

		    dialog.pack();

		    platziereDialogInDerMitteDesFensters(dialog);

		    dialog.setVisible(true);

		    ret = op.getValue() == null ? -1 : (int) (Integer) op.getValue() ;
		    if (ret != JOptionPane.YES_OPTION) return ret ;
		}

		if (bCloseAll) {
			try {
				disableAllModule();
				DelegateFactory.getInstance().getTheJudgeDelegate().removeMyLocks(LPMain.getTheClient());
				DelegateFactory.getInstance().getLogonDelegate()
					.logout(LPMain.getTheClient());
			} catch (Throwable ex) {
				throw new Exception(ex);
			}
			dc.setDarfDirekthilfeTexteEditieren(false);

			((LPModul) hmOflPModule.get(MODULNAME_LOGIN))
					.setStatus(LPModul.STATUS_ENABLED);
			((LPModul) hmOflPModule.get(MODULNAME_BEENDEN))
					.setStatus(LPModul.STATUS_ENABLED);
		}

//		ClientPerspectiveManager.getInstance().save();

		return ret;
	}

	/**
	 * gibt die ge&ouml;ffneten module sortiert nach frame z-order zur&uuml;ck
	 *
	 * @return die ge&ouml;ffneten module sortiert nach frame z-order
	 */
	public List<String> getOffeneLPModule() {
		List<String> offeneModule = new ArrayList<String>();
		Collection<LPModul> c = hmOflPModule.values();
		Iterator<LPModul> iter = c.iterator();
		while (iter.hasNext()) {
			LPModul item = (LPModul) iter.next();
			if (item.getLPModule() != null) {
				offeneModule.add(((InternalFrame) item.getLPModule())
						.getBelegartCNr());
			}
		}

		// nach z-order sortieren..
		Collections.sort(offeneModule, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				int i1 = desktopPane.getComponentZOrder(getLPModul(s1));
				int i2 = desktopPane.getComponentZOrder(getLPModul(s2));
				i1 = i1 == -1 ? desktopPane.getComponentZOrder(getLPModul(s1)
						.getDesktopIcon()) : i1;
				i2 = i2 == -1 ? desktopPane.getComponentZOrder(getLPModul(s2)
						.getDesktopIcon()) : i2;
				return Integer.signum(i2 - i1);
			}
		});
		return offeneModule;
	}

	private void appendString(StringBuilder builder, String value) {
		if(value.length() > 0) {
			builder.append(value + System.getProperty("line.separator"));
		}
	}

	public class ModulStatus {
		private LPModul lpModul;
		private String modulName ;
		private boolean locked ;

		public ModulStatus(LPModul lpModul, String modulName, boolean locked) {
			this.setLpModul(lpModul);
			this.setModulName(modulName) ;
			this.setLocked(locked) ;
		}


		public LPModul getLpModule() {
			return lpModul;
		}

		private void setLpModul(LPModul lpModul) {
			this.lpModul = lpModul;
		}

		public String getModulName() {
			return modulName;
		}

		public void setModulName(String modulName) {
			this.modulName = modulName;
		}

		public boolean isLocked() {
			return locked;
		}

		public void setLocked(boolean locked) {
			this.locked = locked;
		}
	}

	public List<ModulStatus> getStatusOffeneLPModule() {
		List<ModulStatus> offeneModule = new ArrayList<ModulStatus>();
		Collection<LPModul> c = hmOflPModule.values();
		for (LPModul lpModul : c) {
			if(lpModul.hasInternalFrame()) {
				InternalFrame internalFrame = (InternalFrame) lpModul.getLPModule() ;
				offeneModule.add(new ModulStatus(lpModul, internalFrame.getBelegartCNr(), internalFrame.isLocked())) ;
			}
		}

		return offeneModule ;
	}

	private boolean areLPModuleOpen() throws Exception {
		boolean modulesOpen = false;
		for(Entry<String, LPModul> entry : hmOflPModule.entrySet()) {
			LPModul item = entry.getValue();
			if (item.hasInternalFrame()){
				modulesOpen = true;
				break;
			}
		}
		return modulesOpen;

//		Collection<LPModul> c = hmOflPModule.values();
//		int offeneModule = 0;
//		for (Iterator<LPModul> iter = c.iterator(); iter.hasNext();) {
//			LPModul item = (LPModul) iter.next();
//			if (item.getLPModule() != null) {
//				++offeneModule;
//			}
//		}
//		return offeneModule > 0;
	}

	public void doLogon(boolean bShowMandantI) throws Throwable {

		TheClientDto theClientDto = null;

		boolean abbruch = false;
		bAbbruch = false;

		try {

			String user = System.getenv("HV_USER");
			String passwort = System.getenv("HV_KENNWORT");

			if ((user != null && passwort != null)
					|| (Defaults.getInstance().usernameAusBatch != null && Defaults
							.getInstance().passwordAusBatch != null)
					&& bShowMandantI == false) {
				if (bShowMandantI == false) {
					if (Defaults.getInstance().usernameAusBatch != null
							&& Defaults.getInstance().passwordAusBatch != null) {
						Locale l = LPMain.getInstance().getUISprLocale();

						if (Defaults.getInstance().localeAusBatch != null) {
							l = new Locale(
									Defaults.getInstance().localeAusBatch);
						}

						theClientDto = DelegateFactory
								.getInstance()
								.getLogonDelegate()
								.logon(Defaults.getInstance().usernameAusBatch
										+ LogonFac.USERNAMEDELIMITER
										+ Helper.getPCName()
										+ LogonFac.USERNAMEDELIMITER,
										Defaults.getInstance().passwordAusBatch
												.toCharArray(),
										l,
										Defaults.getInstance()
												.getUebersteuerterMandant());
						LPMain.getInstance().setUISprLocale(l);
						LPMain.getInstance().setBenutzernameRaw(
								Defaults.getInstance().usernameAusBatch);
						LPMain.getInstance().setKennwortRaw(
								Defaults.getInstance().passwordAusBatch
										.toCharArray());
					} else {
						theClientDto = DelegateFactory
								.getInstance()
								.getLogonDelegate()
								.logon(user + LogonFac.USERNAMEDELIMITER
										+ Helper.getPCName()
										+ LogonFac.USERNAMEDELIMITER,
										passwort.toCharArray(),
										LPMain.getInstance().getUISprLocale(),
										Defaults.getInstance()
												.getUebersteuerterMandant());
						LPMain.getInstance().setBenutzernameRaw(user);
						LPMain.getInstance().setKennwortRaw(
								passwort.toCharArray());
					}

					Defaults.getInstance().setUebersteuerterMandant(null);

				}

			} else {
				// quicklogin: 2 probieren
				String benutzer = ClientConfiguration.getQuickLoginUserName() ;
				String kennwort = ClientConfiguration.getQuickLoginPassword() ;

				theClientDto = DelegateFactory
						.getInstance()
						.getLogonDelegate()
						.logon(benutzer + LogonFac.USERNAMEDELIMITER
								+ Helper.getPCName(),
								Helper.getMD5Hash((benutzer + kennwort)
										.toCharArray()),
								LPMain.getInstance().getUISprLocale(),
								Defaults.getInstance()
										.getUebersteuerterMandant());
			}

		} catch (Exception ex) {
			dialogLogin = new DialogLogin(this, "", true, bShowMandantI);
			if (Defaults.getInstance().isComponentNamingEnabled()) {
				dialogLogin.setName("dialogLogin");
			}
			platziereDialogInDerMitteDesFensters(dialogLogin);
			dialogLogin.pack();

			while (theClientDto == null && !abbruch) {
				dialogLogin.setVisible(true);

				if (!isBAbbruch()) {
					try {
						String sUser = LPMain.getInstance()
								.getBenutzernameRaw();

						theClientDto = DelegateFactory
								.getInstance()
								.getLogonDelegate()
								.logon(sUser + LogonFac.USERNAMEDELIMITER
										+ Helper.getPCName()
										+ LogonFac.USERNAMEDELIMITER,
										// Helper.getMD5Hash((sUser + new
										// String(
										// LPMain.getInstance().getKennwortRaw
										// ())).toCharArray()),
										LPMain.getInstance().getKennwortRaw(),
										LPMain.getInstance().getUISprLocale(),
										LPMain.getInstance().getMandantRaw());

						if (Defaults.getInstance().getUebersteuerterMandant() != null) {
							LPMain.getInstance().setTheClient(theClientDto);
							theClientDto = DelegateFactory
									.getInstance()
									.getLogonDelegate()
									.logon(sUser + LogonFac.USERNAMEDELIMITER
											+ Helper.getPCName()
											+ LogonFac.USERNAMEDELIMITER,
											// Helper.getMD5Hash((sUser + new
											// String(
											// LPMain.getInstance().getKennwortRaw
											// ())).toCharArray()),
											LPMain.getInstance()
													.getKennwortRaw(),
											LPMain.getInstance()
													.getUISprLocale(),
											Defaults.getInstance()
													.getUebersteuerterMandant());
						}

						LPMain.getInstance().setTheClient(null);

						// Nun Kennwort aendern
						if (dialogLogin.kennwortAendern()) {
							char[] neuesKennwort = dialogLogin
									.getNeuesKennwort();

							BenutzerDto benutzerDto = DelegateFactory
									.getInstance()
									.getBenutzerDelegate()
									.benutzerFindByCBenutzerkennung(
											LPMain.getInstance()
													.getBenutzernameRaw(),
											new String(Helper.getMD5Hash(LPMain
													.getInstance()
													.getBenutzernameRaw()
													+ new String(LPMain
															.getInstance()
															.getKennwortRaw()))));
							if (Helper.short2Boolean(benutzerDto.getBAendern()) == true) {
								LPMain.getInstance().setIdUser(
										theClientDto.getIDUser());
								benutzerDto
										.setCKennwort(Helper.getMD5Hash((benutzerDto
												.getCBenutzerkennung() + new String(
												neuesKennwort)).toCharArray()));
								DelegateFactory.getInstance()
										.getBenutzerDelegate()
										.updateBenutzer(benutzerDto);
								showModalDialog(
										LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr("pers.benutzer.kennwortgeaendert"),
										JOptionPane.WARNING_MESSAGE);
								LPMain.getInstance().setKennwortRaw(
										neuesKennwort);

							} else {

								showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("pers.benutzer.darfkennwortnichtaendern"),
										JOptionPane.WARNING_MESSAGE);

							}

						}

					}
					// except: 4 Exception fangen und auswerten; Meldungstexte
					// kommen nur mehr vom Client.
					catch (ExceptionLP efc) {
						int code = efc.getICode();
						if (code == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzername"));
						} else if (code == EJBExceptionLP.FEHLER_UNGUELTIGE_INSTALLATION) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.ungueltigeinstallation"));
						} else if (code == EJBExceptionLP.FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.maximalebenutzer"));
						} else if (code == EJBExceptionLP.FEHLER_LIZENZ_ABGELAUFEN) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.lizenzabgelaufen"));
						} else if (code == EJBExceptionLP.FEHLER_IM_KENNWORT) {
							// clientres: 2 eine textresource holen.
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.kennwortname"));
						} else if (code == EJBExceptionLP.FEHLER_FALSCHES_KENNWORT) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.kennwortname"));
						} else if (code == EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzer.gesperrt"));
						} else if (code == EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzer.abgelaufen"));
						} else if (code == EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzer.anmeldung"));
						} else if (code == EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzer.keinbenutzermandant"));
						} else if (code == EJBExceptionLP.FEHLER_BUILDNUMMER_CLIENT_SERVER) {
							// EJBExceptionLP exc = efc;
							Object pattern[] = {
									efc.getAlInfoForTheClient().get(
											LogonFac.IDX_BUILDNUMMERCLIENT),
									efc.getAlInfoForTheClient().get(
											LogonFac.IDX_BUILDNUMMERCLIENTVON),
									efc.getAlInfoForTheClient().get(
											LogonFac.IDX_BUILDNUMMERCLIENTBIS) };
							MessageFormat mf = new MessageFormat(
									LPMain.getTextRespectUISPr("lp.version.client.fehler"));
							dialogLogin.showInfo(mf.format(pattern));
						} else if (code == EJBExceptionLP.FEHLER_BUILDNUMMER_SERVER_DB) {
							// EJBExceptionLP exc = (EJBExceptionLP)
							// efc.getThrowable();
							Object pattern[] = {

									efc.getAlInfoForTheClient().get(
											LogonFac.IDX_BUILDNUMMERSERVER),
									efc.getAlInfoForTheClient().get(
											LogonFac.IDX_BUILDNUMMERSERVERVON),
									efc.getAlInfoForTheClient().get(
											LogonFac.IDX_BUILDNUMMERSERVERBIS) };
							MessageFormat mf = new MessageFormat(
									LPMain.getTextRespectUISPr("lp.version.server.fehler"));
							dialogLogin.showInfo(mf.format(pattern));

						} else if (code == EJBExceptionLP.FEHLER_BUILD_CLIENT) {
							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.version.client.fehler2"));
						} else if (code == EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_IN_DIESER_SPRACHE_NICHT_ANMELDEN) {
							ArrayList<?> al = efc.getAlInfoForTheClient();
							String zusatz = "";
							if (al != null && al.size() > 0
									&& al.get(0) instanceof Locale) {
								Locale loc = (Locale) al.get(0);
								zusatz = "(" + loc.getDisplayLanguage() + "|"
										+ loc.getDisplayCountry() + ")";
							}

							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzer.sprache")
											+ " " + zusatz);
						} else if (code == EJBExceptionLP.FEHLER_SPRACHE_NICHT_AKTIV) {
							ArrayList<?> al = efc.getAlInfoForTheClient();
							String zusatz = "";
							if (al.size() > 0 && al.get(0) instanceof Locale) {
								Locale loc = (Locale) al.get(0);
								zusatz = "(" + loc.getDisplayLanguage() + "|"
										+ loc.getDisplayCountry() + ")";
							}

							dialogLogin
									.showInfo(LPMain
											.getTextRespectUISPr("lp.error.benutzer.sprache.nichtaktiv")
											+ " " + zusatz);

						} else {
							LPMain.getInstance().exitClientNowErrorDlg(efc);
						}
					} catch (EJBExceptionLP efc) {
					}

				} else {
					abbruch = true;
				}
			} // end while

			dialogLogin = null;
		}
		// jo end: entwicklung only
		if (!abbruch) {
			// --eingeloggt!
			LPMain.getInstance().setIdUser(theClientDto.getIDUser());

			LPMain.getTheClient();
			// Defaults setzen
			// UI-Locale
			Defaults.getInstance().setLocUI(
					LPMain.getInstance().getUISprLocale());
			// Feiertage fuer den Kalender
			BetriebskalenderDto[] dtosFeiertage = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.betriebskalenderFindByMandantCNrTagesartCNr(
							ZeiterfassungFac.TAGESART_FEIERTAG);
			Defaults.getInstance().setFeiertage(dtosFeiertage);
			// Betriebsurlaubstage fuer den Kalender
			BetriebskalenderDto[] dtosBetriebsurlaub = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.betriebskalenderFindByMandantCNrTagesartCNr(
							ZeiterfassungFac.TAGESART_BETRIEBSURLAUB);
			Defaults.getInstance().setBetriebsurlaub(dtosBetriebsurlaub);
			// Defaults neu initialisieren.
			Defaults.getInstance().initialize();
			// Hoehe der Client-Controls
			Integer iSizeX = ClientConfiguration.getUiControlHeight() ;
			Defaults.getInstance().setControlHeight(iSizeX);
			// <LEER> - Uebersetzung
			Defaults.getInstance().setSComboboxEmptyEntry(
					LPMain.getTextRespectUISPr("wrappercombobox.emptyentry"));
			Defaults.getInstance().setSResourceBundleAllgemein(
					LPMain.RESOURCE_BUNDEL_ALLG);

			// wenn per parameter nicht die neue Komponenten-Benennung
			// eingeschaltet wurde,
			// dann wird nach dem alten Schema benannt.
			// Einschalten muss an dieser Stelle geschehen, damit sich der
			// Client genau
			// gleich verhaelt (z.B.: werden die ersten vier tolbar-buttons
			// nicht
			// benannt etc.)
			// (KF) 21.11.2007
			// @todo KF->KF Wenn alte Tests migriert, dann weg damit!
			if (!Defaults.getInstance().isComponentNamingEnabled()) {
				Defaults.getInstance().setOldComponentNamingEnabled(true);
				myLogger.info("Alte Componenten-Benennung ist aktiviert.");
			}

			String server = System.getProperty("java.naming.provider.url");
			try {
				int iB = server.indexOf("//") + 2;
				int iE = server.length();
				server = server.substring(iB, iE);
			} catch (Exception ex) {
				server = "?";
			}
			// Mandant Kurzbezeichnung
			MandantDto mandantDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

			setTitle(LPMain.getTheClient().getMandant().trim() + " "
					+ mandantDto.getCKbez().trim() + " | "
					+ LPMain.getTheClient().getBenutzername().trim() + " - "
					+ LPMain.getTextRespectUISPr("lp.uisprache") + ": "
					+ LPMain.getTheClient().getLocUiAsString().trim() + " | "
					+ "server: " + server + " - " + getTitle());

			initDefaults();

			enableAllModule(true);
			((LPModul) hmOflPModule.get(MODULNAME_LOGIN))
					.setStatus(LPModul.STATUS_DISABLED);
			// Tooltips im UI-Locale
			((LPModul) hmOflPModule.get(MODULNAME_LOGIN))
					.getToolbarButton()
					.getJbuStart()
					.setToolTipText(
							LPMain.getTextRespectUISPr("lp.tooltip.anmeldung"));
			((LPModul) hmOflPModule.get(MODULNAME_LOGOUT))
					.getToolbarButton()
					.getJbuStart()
					.setToolTipText(
							LPMain.getTextRespectUISPr("lp.tooltip.abmeldung"));
			((LPModul) hmOflPModule.get(MODULNAME_BEENDEN)).getToolbarButton()
					.getJbuStart()
					.setToolTipText(LPMain.getTextRespectUISPr("lp.beenden"));
			((LPModul) hmOflPModule.get(MODULNAME_MANDANT))
					.getToolbarButton()
					.getJbuStart()
					.setToolTipText(
							LPMain.getTextRespectUISPr("lp.mandantwechsel"));

			check();

			if (desktopStatusBar != null) {
				jpaContentPane.remove(desktopStatusBar);
			}

			addStatusbar();
			// PJ 16258
			if (mandantDto.getPartnerDto().getOBild() != null) {
				ImageIcon i = new ImageIcon(mandantDto.getPartnerDto()
						.getOBild());
				getDesktopStatusBar().setBildMandant(i);
			} else {
				getDesktopStatusBar().setBildMandant(null);
			}
			aktualisiereAnzahlJMSMessages();

		}
	}

	private void check() throws Throwable {

		StringBuffer sb = new StringBuffer();
		TimeZoneValidationInfo validationInfo = new SystemInfoController()
				.getValidationInfo();

		if (!validationInfo.isTimezoneAccepted()) {
			sb.append(LPMain.getMessageTextRespectUISPr(
					"lp.warning.zeitzoneweichtab",
					validationInfo.getClientTimezone(),
					validationInfo.getServerTimezone()));
			sb.append(Helper.CR_LF);
		} else if (!validationInfo.isDaySavingTimeAccepted()) {
			if (validationInfo.isServerHasDST())
				sb.append(LPMain
						.getTextRespectUISPr("lp.warning.serverhatsommerzeit"));
			else
				sb.append(LPMain
						.getTextRespectUISPr("lp.warning.serverhatnormalzeit"));
			sb.append(Helper.CR_LF);
		} else if (!validationInfo.isTimeDeviationAccepted()) {
			sb.append(LPMain.getTextRespectUISPr("lp.warnung.uhrzeit"));
			sb.append(Helper.CR_LF);
			sb.append("Serverzeit:");
			sb.append(Helper.formatDatumZeit(
					new Date(validationInfo.getServerTime()), getLocale()));
			sb.append(Helper.CR_LF);
			sb.append("Clientzeit:");
			sb.append(Helper.formatDatumZeit(
					new Date(System.currentTimeMillis()), getLocale()));
		}

		if (sb.length() > 0)
			myLogger.debug(sb.toString());

		// SK auskommentiert nach absprache mit AD damit Tests wieder gehen
		// Integer benutzerFrei =
		// DelegateFactory.getInstance().getLogonDelegate().
		// getIBenutzerFrei(LPMain.getTheClient());
		// if (benutzerFrei.intValue() <= 1) {
		// String msg1 = LPMain.getInstance()
		// .getTextRespectUISPr("lp.warning.benutzer")
		// + benutzerFrei + "/n";
		// if (!msg.equals("")) msg += "\n";
		// msg += msg1;
		// myLogger.debug(msg1);
		// }

		// *** server jvm

		if (LPMain.getTheClient().getIStatus() != null) {
			if ((LPMain.getTheClient().getIStatus() != null && LPMain
					.getTheClient().getIStatus().intValue() == EJBExceptionLP.WARNUNG_SERVER_JVM)) {
				String sM = LPMain.getTextRespectUISPr("lp.jvm.version.server");
				sb.append(Helper.CR_LF + Helper.CR_LF);
				sb.append(sM);
			} else {
				sb.append(Helper.CR_LF + Helper.CR_LF);
				sb.append("Unbekannter Fehler!");
			}
		}

		// *** expires; expirehv: auswerten
		boolean bExit = false;
		String sM = "";
		Timestamp now = new Timestamp(System.currentTimeMillis());

		try {
			AnwenderDto anwenderDtoNF = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.anwenderFindByPrimaryKeyNoFilter(
							new Integer(
									SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

			Timestamp tSubscription = anwenderDtoNF.getTSubscription();
			Timestamp tAblauf = anwenderDtoNF.getTAblauf();

			if (tSubscription != null && tAblauf == null) {
				LPMain.setVersionHVExpires(tSubscription);

				if (now.after(tSubscription)) {
					sM = dialogExpires(sM, "lp.expires.now");
				}

				else if (now.after(Helper.addiereTageZuDatum(tSubscription, -14))) {
					sM = dialogExpires(sM, "lp.expires.ro");
				}
			}

			else if (tSubscription != null && tAblauf != null && tAblauf.after(tSubscription)) {
				if (now.after(tSubscription)) {
					sM = dialogExpiresAblaufAndSubscription(LPMain.getMessageTextRespectUISPr("lp.expires.now.02", tSubscription, tAblauf));

				}

				else if (now.after(Helper.addiereTageZuDatum(tSubscription, -14))) {
					sM = dialogExpiresAblaufAndSubscription(LPMain.getMessageTextRespectUISPr("lp.expires.ro.02", tSubscription, tAblauf));
				}
			}

			else if ((tAblauf != null && tSubscription == null) || (tAblauf != null && tSubscription != null && tSubscription.after(tAblauf)))  {
				LPMain.setVersionHVExpires(tAblauf);
				 if (now.after(Helper.addiereTageZuDatum(tAblauf, -14))) {
						sM = dialogExpires(sM, "lp.expires");
				 }
			}

		} catch (Throwable ex) {
			sM = dialogExpires(sM, "lp.isexpired");
		}
		sb.append(sM);

		// *** jetzt alles gesammelte ausgeben.
		if (!sb.toString().isEmpty()) {
			sb.append(Helper.CR_LF);
			sb.append("(" + System.getProperty("java.naming.provider.url")
					+ ")");
			showModalDialog("Lizenz", sb.toString(), JOptionPane.WARNING_MESSAGE);
			if (bExit) {
				LPMain.getInstance().exitClientNowErrorDlg(null);
			}
		}
	}

	private String dialogExpires(String sM, String sMsg) {
		Object[] arguments = { LPMain.getVersionHVExpires() };
		String result = MessageFormat.format(LPMain.getTextRespectUISPr(sMsg),
				arguments);

		sM += result + Helper.CR_LF + Helper.CR_LF;
		return dialogExpiresMin(sM);
	}

	private String dialogExpiresAblaufAndSubscription(String sM) {
		sM += Helper.CR_LF + Helper.CR_LF;
		return dialogExpiresMin(sM);
	}

	private String dialogExpiresMin(String sM) {
		Integer year = Calendar.getInstance().get(Calendar.YEAR);
		sM += LPMain.getMessageTextRespectUISPr("lp.logistikpur", year.toString()) + Helper.CR_LF;
		sM += LPMain.getTextRespectUISPr("lp.logistikpuradresse")
				+ Helper.CR_LF;
		sM += LPMain.getTextRespectUISPr("lp.logistikpuremail") + Helper.CR_LF;
		sM += LPMain.getTextRespectUISPr("lp.logistikpurkontakt")
				+ Helper.CR_LF;
		return sM;
	}

	/**
	 * Initialisiere alle Frameworkdefaults!
	 */
	private void initDefaults() {
		Defaults.getInstance().setToolTipLeeren(
				LPMain.getTextRespectUISPr("lp.leeren"));
	}

	public boolean isBAbbruch() {
		return bAbbruch;
	}

	public void aktualisiereAnzahlJMSMessages() {

		try {
			int iUnbearbeitet = DelegateFactory.getInstance()
					.getBenutzerDelegate()
					.getAnzahlDerUnbearbeitetenMeldungen();
			int iUebernommen = DelegateFactory
					.getInstance()
					.getBenutzerDelegate()
					.getAnzahlDerNochNichtErledigtenAberNochZuBearbeitendenMeldungen();

			getDesktopStatusBar()
					.setLPTopic(iUnbearbeitet + "/" + iUebernommen);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public PanelDesktopStatusbar getDesktopStatusBar() {
		return desktopStatusBar;
	}

	/**
	 * Listener fuer alle Chexboxen und Radiobuttons.
	 *
	 * @param e
	 *            ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent e) {
		if (Defaults.getInstance().isUseWaitCursor()) {
			this.setCursor(java.awt.Cursor
					.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
		}
		JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
		try {
			/** @todo JO en version? PJ 5418 */
			if (rb.isSelected() && rb.getText().equals("Windows Look")) {
				// UI_CUR = "Windows";
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} else if (rb.isSelected() && rb.getText().equals("Motif Look")) {
				// UI_CUR = "Motif";
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			} else if (rb.isSelected() && rb.getText().equals("System Look")) {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} else if (rb.isSelected() && rb.getText().equals("Java Look")) {
				// UI_CUR = "Metal";
				UIManager
						.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} else if (rb.isSelected()
					&& rb.getText().equals("Kunststoff Look")) {
				// UI_CUR = "Kunststoff";
				UIManager.setLookAndFeel(LAF_CLASS_NAME_KUNSTSTOFF);
			} else if (rb.isSelected() && rb.getText().equals("Skin Look")) {
				// UI_CUR = "Skin";
				UIManager
						.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
			}
			loadUserSpecificSettings();
			SwingUtilities.updateComponentTreeUI(this);
		} catch (UnsupportedLookAndFeelException exc) {
			rb.setEnabled(false);
			System.err.println(LPMain.getTextRespectUISPr("lp.error.look")
					+ rb.getText());
			try {
				// UI_CUR = "Metal";
				menuItemAnsichtLFJava.setSelected(true);
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(this);
			} catch (Exception exc2) {
				LPMain.getInstance().exitClientNowErrorDlg(
						new ExceptionLP(EJBExceptionLP.FEHLER, exc2));
			}
		} catch (Throwable exc) {
			rb.setEnabled(false);
			LPMain.getInstance().exitClientNowErrorDlg(exc);
		}

		if (Defaults.getInstance().isUseWaitCursor()) {
			this.setCursor(java.awt.Cursor
					.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Entferne jInternalFrameMe sauber aus der Struktur.
	 *
	 * @param jInternalFrameMe
	 *            InternalFrame
	 * @param t
	 *            Throwable
	 * @param sMsgI
	 *            String
	 */
	public void exitFrameDlg(InternalFrame jInternalFrameMe, Throwable t,
			String sMsgI) {
		try {
			if (t != null) {
				myLogger.error(t.getMessage(), t);

				new DialogError(this, t, DialogError.TYPE_ERROR);
				// JOptionPane.showMessageDialog(this, msgTemp, "",
				// JOptionPane.INFORMATION_MESSAGE);
				closeFrame(jInternalFrameMe);
			} else {
				if (sMsgI != null) {
					closeFrame(jInternalFrameMe);
					new DialogError(this, t, DialogError.TYPE_ERROR);
					// JOptionPane.showMessageDialog(this, sMsgI, "",
					// JOptionPane.INFORMATION_MESSAGE);
				} else {
					closeFrame(jInternalFrameMe);
				}
			}
		} catch (Exception ex) {
			myLogger.error("", ex);
		}
	}

	public void closeFrame(InternalFrame jInternalFrameMe) {
		String lPModulname = jInternalFrameMe.getBelegartCNr();
		LPModul lPModul = (LPModul) hmOflPModule.get(lPModulname);
		/** @todo toolbar PJ 5419 */
		lPModul.setStatus(LPModul.STATUS_ENABLED);
		InternalFrame jif = (InternalFrame) lPModul.getLPModule();

		try {
			jif.vetoableChangeLP();
		} catch (Throwable ex) {
//			exitClientNowErrorDlg(ex);

//			new DialogError(this, ex, DialogError.TYPE_WARNING);
			if(ex != null) {
				throw new RuntimeException(ex) ;
			}

		}
		closeModuleAndFrame(lPModul, jif);
//		try {
//			jif.setClosed(true);
//		} catch (PropertyVetoException e) {
//			ClientPerspectiveManager.getInstance().internalFrameClosing(
//					new InternalFrameEvent(jif, 0));
//			jif.dispose();
//		}
////		lPModul.setLPModule(null);
//		lPModul.setOpenModules(null);
	}


	private void closeModuleAndFrame(LPModul modul, InternalFrame frame) {
		try {
			frame.setClosed(true);
		} catch (PropertyVetoException e) {
			ClientPerspectiveManager.getInstance().internalFrameClosing(
					new InternalFrameEvent(frame, 0));
			frame.dispose();
		}
		modul.setOpenModules(null);
	}

	/**
	 * Das Modul ohne weitere Benachrichtigung schliessen
	 *
	 * @param modul das zu beendende Modul
	 */
	private void closeFrame(LPModul modul) {
		modul.setStatus(LPModul.STATUS_ENABLED);
		InternalFrame frame = (InternalFrame) modul.getLPModule();
		frame.dispose();
		modul.setOpenModules(null);
//		closeModuleAndFrame(modul, frame);
	}

	public void closeAllFrames() {

		Collection<LPModul> c = hmOflPModule.values();
		for (Iterator<LPModul> iter = c.iterator(); iter.hasNext();) {
			LPModul item = (LPModul) iter.next();
			if (item.getLPModule() != null) {
				InternalFrame iFrame = (InternalFrame) item.getLPModule();
				try {
					iFrame.setMaximum(false);
				} catch (PropertyVetoException e) {
					// egal
				}
				closeFrame(iFrame);
			}
		}
	}

	private void enableAllModule(boolean enable) throws Throwable {
		Collection<LPModul> c = hmOflPModule.values();
		for (Iterator<LPModul> iter = c.iterator(); iter.hasNext();) {
			LPModul item = (LPModul) iter.next();
			if (!enable && (item.getLPModule() != null)) {
				closeFrame((InternalFrame) item.getLPModule());
			}
			/** @todo toolbar PJ 5419 */
			if (enable) {

				item.setStatus(LPModul.STATUS_ENABLED);
				if (MODULNAME_MANDANT.equals(item.getToolbarButton()
						.getActionCommand())) {
					int i = DelegateFactory
							.getInstance()
							.getBenutzerDelegate()
							.getAnzahlDerMandantenEinesBenutzers(
									LPMain.getInstance().getBenutzernameRaw(),
									new String(LPMain.getInstance()
											.getKennwortRaw()));
					if (i < 2) {
						item.setStatus(LPModul.STATUS_DISABLED);
					}
				}

			} else {
				item.setStatus(LPModul.STATUS_DISABLED);
			}
		}
	}

	private void disableAllModule() throws Throwable {
		for (LPModul modul : hmOflPModule.values()) {
			if(!modul.hasInternalFrame()) continue ;

			closeFrame(modul) ;
//			modul.setStatus(LPModul.STATUS_DISABLED);
		}
	}

	public void screenshotVersenden(Frame frame, String prefixBetreff,
			String detailMessage) throws ExceptionLP, Throwable, AWTException {
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_EMAILVERSAND)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_SCHWERWIEGENDE_FEHLER_VERSENDEN,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							LPMain.getTheClient().getMandant());
			boolean bSenden = ((Boolean) parameter.getCWertAsObject());

			if (bSenden == true) {
				// Screenshot erstellen
				Rectangle screenRectangle = new Rectangle(frame.getLocation(),
						frame.getSize());
				Robot robot = new Robot();
				BufferedImage image = robot
						.createScreenCapture(screenRectangle);

				// Versandauftrag erstellen
				MandantDto mandantDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getTheClient().getMandant());

				VersandauftragDto dto = new VersandauftragDto();
				dto.setCEmpfaenger("support1@heliumv.com");

				parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_MAILADRESSE_ADMIN,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG,
								LPMain.getTheClient().getMandant());

				if (parameter.getCWert() != null
						&& parameter.getCWert().length() > 0) {
					dto.setCAbsenderadresse(parameter.getCWert());
				} else {

					if (mandantDto.getPartnerDto() != null
							&& mandantDto.getPartnerDto().getCEmail() != null) {
						dto.setCAbsenderadresse(mandantDto.getPartnerDto()
								.getCEmail());
					} else {
						dto.setCAbsenderadresse("admin@heliumv.com");
					}

				}

				String sBetreff = prefixBetreff
						+ mandantDto.getCKbez()
						+ " , Mandant "
						+ mandantDto.getCNr()
						+ ", HV-Version: "
						+ mandantDto.getAnwenderDto()
								.getIBuildnummerClientBis();

				dto.setCBetreff(sBetreff);

				dto.setCText(detailMessage);
				dto = DelegateFactory.getInstance().getVersandDelegate()
						.updateVersandauftrag(dto, false);

				// Screenshot als Anhang
				VersandanhangDto versandanhangDto = new VersandanhangDto();

				versandanhangDto.setVersandauftragIId(dto.getIId());
				versandanhangDto.setCDateiname("screenshot.png");

				versandanhangDto.setOInhalt(Helper.imageToByteArray(image));
				DelegateFactory.getInstance().getVersandDelegate()
						.createVersandanhang(versandanhangDto);

			}
		}
	}

	public void reloadFont(boolean customFont) {
		Font font = null;
		if (customFont)
			font = ClientPerspectiveManager.getInstance().readFont();
		if (font == null)
			font = HelperClient.getDefaultFont();

		UIDefaults uiDef = UIManager.getDefaults();
		Enumeration<Object> keys = uiDef.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Font oldFont = uiDef.getFont(key);
			if (oldFont != null) {
				uiDef.put(key, new FontUIResource(font));
			}
		}
		Defaults.getInstance().setSizeFactor(
				font.getSize2D() / HelperClient.getDefaultFont().getSize2D());

		Integer height = ClientConfiguration.getUiControlHeight() ;
		Defaults.getInstance().setControlHeight(
				Defaults.getInstance().bySizeFactor(height));

		Dimension ifSize = HelperClient.getInternalFrameSize();
		for (LPModul m : hmOflPModule.values()) {
			if (m.getLPModule() != null) {
				JInternalFrame frame = m.getLPModule();
				HelperClient.updateComponentHeight(frame);
				if (frame.getWidth() < ifSize.width) {
					frame.setSize(ifSize.width, frame.getHeight());
				}
				if (frame.getHeight() < ifSize.height) {
					frame.setSize(frame.getWidth(), ifSize.height);
				}
			}
		}
		Defaults.getInstance().setColorVision(
				ClientPerspectiveManager.getInstance().readColorVision());
	}

	protected void loadUserSpecificSettings() {

		boolean direkthilfe =  true;
		if(menuItemDirekthilfe != null) {
			// auf mac haut es einfach nicht hin, die Icons kommen aus AquaNativeResources.getRadioButtonSizerImage()
			//was man nicht ueberschreiben kann
			direkthilfe = !UIManager.getLookAndFeel().getName().contains("Mac OS");
			menuItemDirekthilfe.setEnabled(direkthilfe);
			menuItemDirekthilfe.setSelected(direkthilfe);
		}

		reloadFont(true);
		Defaults.getInstance().setDirekthilfeVisible(ClientPerspectiveManager.getInstance().isDirekthilfeVisible() && direkthilfe);

		LookAndFeel laf = UIManager.getLookAndFeel();
		if(!laf.getName().contains("Windows")) {
			// windows verwendet seine eigenen Icons
			// fuer RadioButtons und CheckBoxes
			laf = new MetalLookAndFeel();
		}


		Icon icon = laf.getDefaults().getIcon("CheckBox.icon");
		if(!(icon instanceof CibIconWrapper))
			UIManager.put("CheckBox.icon", new CibIconWrapper(icon));

		icon = laf.getDefaults().getIcon("RadioButton.icon");
		if(!(icon instanceof CibIconWrapper))
			UIManager.put("RadioButton.icon", new CibIconWrapper(icon));
	}

	protected void setDefaultPropertiesAndColors() {
		// UIManager.put("TextField.font", font);
		// UIManager.put("FormattedTextField.font", font);
		// UIManager.put("Table.font", font);
		// UIManager.put("TableHeader.font", font);
		// UIManager.put("ComboBox.font", font);
		// UIManager.put("CheckBox.font", font);
		// UIManager.put("TextArea.font", font);
		// UIManager.put("Label.font", font);
		// UIManager.put("RadioButton.font", font);
		// UIManager.put("Button.font", font);
		// UIManager.put("TabbedPane.font", font);
		// UIManager.put("Menu.font", font);
		// UIManager.put("MenuItem.font", font);
		// UIManager.put("CheckBoxMenuItem.font", font);
		// UIManager.put("RadioButtonMenuItem.font", font);

		// Font f = UIManager.getFont("Button.font") ;
		// if(f != null) {
		// UIManager.put("Button.font", new FontUIResource(f.getName(),
		// f.getStyle(), 16)) ;
		// }
		// f = UIManager.getFont("TextArea.font") ;
		// if(f != null) {
		// UIManager.put("TextArea.font", new FontUIResource(f.getName(),
		// f.getStyle(), 24)) ;
		// }

		loadUserSpecificSettings();

		// DefaultFarbe fuer Textfeld-Hintergrund
		try {
			Color inactiveForegroundColor = 
					ClientConfiguration.getInactiveForegroundColor() ;
			UIManager.put("TextField.inactiveForeground",
					inactiveForegroundColor);
		} catch (Throwable e) {
			myLogger.error("could not initialize: color.inactiveForeground\n",
					e);
		}

		UIManager
				.put("TextField.inactiveBackground", java.awt.SystemColor.info);
	}

	public Dimension getDesktopPaneSize() {
		validate();
		return desktopPane.getSize();
	}

	private Dimension getInternalFrameSize() {
		// Dimension d = this.getSize();

		int width = desktopPane.getWidth();
		int height = desktopPane.getHeight();

		return new Dimension(new Double(width / 1.3).intValue(), height);
	}

	class BackgroundPanel extends JPanel {

		/**
	 *
	 */
		private static final long serialVersionUID = 1L;
		private Image bgImage;
		private Image org;
		private ImageIcon icon;

		/*
		 * BackgroundPanel() throws InterruptedException { this(null); }
		 */

		public BackgroundPanel(String fileName) throws Throwable {
			super();
			if (fileName == null) {
				return;
			}

			setOpaque(false);

			byte[] bildAusDb = DelegateFactory.getInstance()
					.getSystemDelegate().getHintergrundbild();

			if (bildAusDb != null) {

				MediaTracker mt = new MediaTracker(this);
				bgImage = new ImageIcon(bildAusDb).getImage();
				mt.addImage(bgImage, 0);

				mt.waitForAll();

				icon = new ImageIcon(bgImage);
			} else {
				icon = loadImage(fileName);
			}

			if (bgImage == null) {
				throw new RuntimeException();
			}
			org = bgImage;

			return;
		}

		public void paintComponent(Graphics g) {

			if (bgImage != null) {
				g.drawImage(bgImage, 0, 0, (int) icon.getIconWidth(),
						(int) icon.getIconHeight(), this);
			}
			super.paintComponent(g);
		}

		private ImageIcon loadImage(String imgLoc) throws InterruptedException {

			MediaTracker mt = new MediaTracker(this);
			bgImage = new ImageIcon(getClass().getResource(imgLoc)).getImage();
			mt.addImage(bgImage, 0);

			mt.waitForAll();

			return new ImageIcon(bgImage);
		}

		public void setBounds(int x, int y, int width, int height) {
			bgImage = org.getScaledInstance(width, height - 30,
					Image.SCALE_SMOOTH);
			icon = new ImageIcon(bgImage);
			super.setBounds(0, 0, width, height);
		}

	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		if (hasBG) {
			Rectangle r = super.getBounds();
			r.width = r.width - 5;
			r.height = r.height - 80;
			jpaBackground.setBounds(r);
		}
	}

	public void componentShown(ComponentEvent e) {
	}

	public void setBAbbruch(boolean bAbbruch) {
		this.bAbbruch = bAbbruch;
	}

	public final static String MY_OWN_NEW_ZEIT_START = PanelBasis.ACTION_MY_OWN_NEW
			+ "ZEIT_START";
	public final static String MY_OWN_NEW_ZEIT_STOP = PanelBasis.ACTION_MY_OWN_NEW
			+ "ZEIT_STOP";

	public void zeitbuchungAufBeleg(String actionCommand, String belegartCNr,
			Integer belegartIId) throws UnknownHostException, Throwable,
			ExceptionLP {
		ZeitdatenDto zDto = new ZeitdatenDto();
		zDto.setCWowurdegebucht("Client: " + Helper.getPCName());
		zDto.setPersonalIId(LPMain.getTheClient().getIDPersonal());

		if (actionCommand.equals(MY_OWN_NEW_ZEIT_START)) {

			ParametermandantDto parameterPV = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_ARBEITSZEITARTIKEL_AUS_PERSONALVERFUEGBARKEIT,
							ParameterFac.KATEGORIE_PERSONAL,
							LPMain.getTheClient().getMandant());

			boolean bArbeitszeitartikelauspersonalverfuegbarkeit = (Boolean) parameterPV
					.getCWertAsObject();

			if (bArbeitszeitartikelauspersonalverfuegbarkeit == true) {
				Integer artikelIId = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(
								LPMain.getTheClient().getIDPersonal());
				if (artikelIId != null) {
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey(artikelIId);

					zDto.setArtikelIId(artikelDto.getIId());
				}
			}

			if (zDto.getArtikelIId() == null) {

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getParametermandant(
								ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL,
								ParameterFac.KATEGORIE_ALLGEMEIN,
								LPMain.getTheClient().getMandant());
				if (parameter.getCWert() != null
						&& !parameter.getCWertAsObject().equals("")) {
					try {
						ArtikelDto artikelDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.artikelFindByCNr(parameter.getCWert());
						zDto.setArtikelIId(artikelDto.getIId());
					} catch (Throwable ex) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
								new Exception(
										"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
					}
				} else {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
							new Exception(
									"FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT"));
				}
			}

			zDto.setCBelegartnr(belegartCNr);
			zDto.setIBelegartid(belegartIId);

			if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
				com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = DelegateFactory
						.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByAuftrag(belegartIId);

				if (auftragpositionDtos != null
						&& auftragpositionDtos.length > 0) {
					for (int i = 0; i < auftragpositionDtos.length; i++) {
						AuftragpositionDto dto = auftragpositionDtos[i];

						if (dto.getAuftragpositionstatusCNr() != null) {
							zDto.setIBelegartpositionid(auftragpositionDtos[0]
									.getIId());
						}
					}

				} else {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.auftragkeinepositionen"));
				}
			}

		} else {
			zDto.setTaetigkeitIId(DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE)
					.getIId());
		}

		zDto.setTZeit(new Timestamp(System.currentTimeMillis()));
		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.createZeitdaten(zDto);

		Double d = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.getSummeZeitenEinesBeleges(
						belegartCNr,
						belegartIId,
						null,
						LPMain.getTheClient().getIDPersonal(),
						Helper.cutTimestamp(new Timestamp(System
								.currentTimeMillis())), null);

		if (actionCommand.equals(MY_OWN_NEW_ZEIT_STOP)) {

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("proj.bisherigezeit")
							+ " "
							+ Helper.formatZahl(d, 2, LPMain.getTheClient()
									.getLocUi()) + " "
							+ LPMain.getTextRespectUISPr("lp.stunden"));

		}
	}

	/**
	 *
	 * @return int
	 */
	public int getSelectedIndex() {
		return iUntereLasche;
	}

	/**
	 * cmd: 0
	 *
	 * @param commandI
	 *            Command
	 * @throws Throwable
	 * @return int
	 */
	public int execute(Command commandI) throws Throwable {

		int iRetCmd = ICommand.COMMAND_NOT_DONE;

		if (commandI instanceof CommandCreateIF) {
			CommandCreateIF command = (CommandCreateIF) commandI;
			refreshModul(command.getsInternalFrame());
			iRetCmd = ICommand.COMMAND_DONE;
		}

		else if (commandI instanceof CommandSetFocus) {
			CommandSetFocus command = (CommandSetFocus) commandI;
			JInternalFrame jIF = ((LPModul) hmOflPModule.get(command
					.getsInternalFrame())).getLPModule();
			setSelectedIF(jIF);
			iRetCmd = ICommand.COMMAND_DONE;
		}

		else if (commandI instanceof Command2IFNebeneinander) {
			Command2IFNebeneinander command = (Command2IFNebeneinander) commandI;
			JInternalFrame jif1 = ((LPModul) hmOflPModule.get(command
					.getsInternalFrame())).getLPModule();
			if (jif1 == null) {
				throw new Exception(command.getsInternalFrame()
						+ " does not exist!");
			}
			JInternalFrame jif2 = ((LPModul) hmOflPModule.get(command
					.getSInternalFrame2I())).getLPModule();
			if (jif2 == null) {
				throw new Exception(command.getSInternalFrame2I()
						+ " does not exist!");
			}

			iconifyWindows();
			jif1.setIcon(false);
			jif2.setIcon(false);
			tileWindows();
			iRetCmd = ICommand.COMMAND_DONE;
		} else {
			// weiterrouten
			for (LPModul elem : hmOflPModule.values()) {
				if (elem.getLPModule() != null) {
					if (elem.getLPModule().equals(
							((LPModul) hmOflPModule.get(commandI
									.getsInternalFrame())).getLPModule())) {
						;

						if (((InternalFrame) elem.getLPModule())
								.execute(commandI) == ICommand.COMMAND_DONE) {
							iRetCmd = ICommand.COMMAND_DONE;
							break;
						}
					}
				}
			}
		}
		return iRetCmd;
	}

	/**
	 * iconifyWindows
	 *
	 * @throws PropertyVetoException
	 */
	private void iconifyWindows() throws PropertyVetoException {

		JInternalFrame[] aFrames = desktopPane.getAllFrames();
		int nFrames = aFrames.length;
		for (int xFrames = 0; xFrames < nFrames; xFrames++) {
			if (!aFrames[xFrames].isIcon()) {
				aFrames[xFrames].setIcon(true);
			}
		}
	}

	private void setSelectedIF(JInternalFrame jIFI)
			throws PropertyVetoException {

		jIFI.setSelected(true);
		jIFI.toFront();
	}

	public void platziereDialogInDerMitteDesFensters(JDialog jDialog) {
		jDialog.setLocationRelativeTo(this);
//		java.awt.Dimension frameSize = this.getSize();
//		java.awt.Dimension dialogSize = jDialog.getSize();
//		java.awt.Point frameLocation = this.getLocation();
//		// Position des Dialogs genau in der Mitte des Frames
//		int iX = (int) frameLocation.getX()
//				+ ((frameSize.width - dialogSize.width) / 2);
//		int iY = (int) frameLocation.getY()
//				+ ((frameSize.height - dialogSize.height) / 2);
//		if (iX < 0) {
//			iX = 0;
//		}
//		if (iY < 0) {
//			iY = 0;
//		}
//		jDialog.setLocation(iX, iY);
	}

	public void platziereFrameInDerMitteDesFensters(JFrame jFrame) {
		java.awt.Dimension frameSize = this.getSize();
		java.awt.Dimension dialogSize = jFrame.getSize();
		java.awt.Point frameLocation = this.getLocation();
		// Position des Dialogs genau in der Mitte des Frames
		int iX = (int) frameLocation.getX()
				+ ((frameSize.width - dialogSize.width) / 2);
		int iY = (int) frameLocation.getY()
				+ ((frameSize.height - dialogSize.height) / 2);
		if (iX < 0) {
			iX = 0;
		}
		if (iY < 0) {
			iY = 0;
		}
		jFrame.setLocation(iX, iY);
	}

	public void platziereDialogRechtsObenImFenster(JDialog jDialog) {
		java.awt.Dimension frameSize = this.getSize();
		java.awt.Dimension dialogSize = jDialog.getSize();
		java.awt.Point frameLocation = this.getLocation();
		// Position des Dialogs genau in der Mitte des Frames
		int iX = (int) frameLocation.getX()
				+ ((frameSize.width - dialogSize.width)) - 3;
		int iY = (int) frameLocation.getY()
				+ ((frameSize.height - dialogSize.height) / 2);
		if (iX < 0) {
			iX = 0;
		}
		if (iY < 0) {
			iY = 0;
		}
		jDialog.setLocation(iX, iY);
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		// Auto-generated method stub
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		// Auto-generated method stub

	}

	public void internalFrameClosing(InternalFrameEvent e) {
		// Auto-generated method stub

	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		// Auto-generated method stub

	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		// Auto-generated method stub

	}

	public void internalFrameIconified(InternalFrameEvent e) {
		if (jpaBackground != null) {
			desktopPane.moveToBack(jpaBackground);
		}
	}

	public void internalFrameOpened(InternalFrameEvent e) {
		// Auto-generated method stub

	}

	private void initJms() throws Throwable {
		// damit werden die queues aktiviert!
		// InfoTopic immer abonnieren
		LPMain.getInstance().initInfoTopic(this);

		// andere Topics abhaengig von Rolle
		String[] themen;
		try {
			themen = DelegateFactory.getInstance().getBenutzerDelegate()
					.getThemenDesAngemeldetenBenutzers();
//			if (themen != null) {
				for (int i = 0; i < themen.length; i++) {
					if (themen[i].trim().equals(LPTopicQsBean.TOPIC_NAME_QS))
						LPMain.getInstance().initTopicQs(this);
					if (themen[i].trim()
							.equals(LPTopicFertBean.TOPIC_NAME_FERT))
						LPMain.getInstance().initTopicFert(this);
					if (themen[i].trim().equals(
							LPTopicManageBean.TOPIC_NAME_MANAGE))
						LPMain.getInstance().initTopicManage(this);
					if (themen[i].trim().equals(LPTopicGfBean.TOPIC_NAME_GF))
						LPMain.getInstance().initTopicGf(this);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LPQueueListener();

	}
}
