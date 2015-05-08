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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperPasswordField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Organisation: </p>
 *
 * Author   $Author: christian $
 * Revision $Revision: 1.9 $
 * Letzte Aenderung: $Date: 2012/10/10 07:31:28 $
 */
public class DialogLogin extends JDialog {

	private static final long serialVersionUID = 1L;

	// JVM Message
	private JOptionPane jOptionPane = new JOptionPane();
	
	private WrapperPasswordField wpwdKennwort = new WrapperPasswordField();
	private WrapperTextField wtfBenutzer = new WrapperTextField();
	private WrapperLabel wlaBenutzer = new WrapperLabel();
	private WrapperLabel wlaKennwort = new WrapperLabel();
	private WrapperButton wbuAnmelden = new WrapperButton();
	private WrapperCheckBox wcbKennwortaendern = new WrapperCheckBox();
	
	private JTextArea taOut = new JTextArea();
	private JScrollPane jspTaOut = new JScrollPane(taOut,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	private JPanel panel = new JPanel();
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	
	private WrapperComboBox wcoResourcebundels = null;
	private WrapperLabel wlaUILocale = new WrapperLabel();
	private WrapperLabel wlaMandant = new WrapperLabel();
	private Border border = null;
	private WrapperComboBox wcoMandant = null;

	private boolean bShowMandant = false;
	private JButton btnInfo = new JButton();
	private Frame frame = null;
	private char[] neuesKennwort = null;
	
	private Font currentFont = UIManager.getDefaults().getFont("Label.font");
	private int  fontSize = currentFont.getSize();
	private int maxwidth = LPMain.getInstance().getDesktop().getWidth();
	private int maxheight = LPMain.getInstance().getDesktop().getHeight();
	
	protected final LpLogger myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	public DialogLogin(Frame frame, String title, boolean modal,
			boolean bShowMandantI) throws Throwable {

		super(frame, title, modal);
		this.frame = frame;
		bShowMandant = bShowMandantI;

		jbInit();
		
		initComponents();

		checkJVM();
		
		pack();
		
		wtfBenutzer.requestFocus();
	}

//	private void checkJVM() {
//		String sJVM = System.getProperty("java.version");
//		String tmp = sJVM;
//		if (sJVM != null) {
//			if (sJVM.length() < 8) {
//				tmp = sJVM.substring(0);
//			} else {
//				tmp = sJVM.substring(0, 8);
//			}
//		}
//		sJVM = tmp;
////		if ((sJVM != null && (sJVM.startsWith("1.6.") || sJVM
////				.startsWith("1.7.")) == false)) {
//		if(sJVM != null && !sJVM.startsWith("1.7.")) {			
//			
//			String tmp2 = LPMain.getInstance().getTextRespectUISPr(
//					"lp.jvm.versionen.freigebenen");
//			
//			taOut.setText(LPMain.getInstance().getTextRespectUISPr(
//					"lp.jvm.version")
//					+ " ("
//					+ LPMain.getInstance().getTextRespectUISPr(
//							"lp.jvm.version.installiert")
//					+ ": "
//					+ sJVM
//					+ " "
//					+ LPMain.getInstance().getTextRespectUISPr(
//							"lp.jvm.versionen.freigebenen")
//					+ ": "
////					+ "1.6.*, 1.7.* " + ")");
//					+ "1.7.* " + ")");
//		}
//
//	}
	
	
	// *** wp ***
	// ***
	
	private void checkJVM() {
		if (LPMain.getInstance().getBenutzernameRaw() == null) {
			// String clientJavaVersion = "1.6.0";
			String clientJavaVersion = System.getProperty("java.version");

			String[] javaVersion = new String[4];
			javaVersion = clientJavaVersion.split("\\.|_");

			final String javaMinVersionApproved = "1.7.0_20";
			final String javaVersionNext = "1.8.0";
			final int javaMajorReleaseApproved = 7;
			final int javaUpdateApproved = 20;

			int javaMajorRelease = 0;
			int javaUpdate = 0;
			try {

				javaMajorRelease = Integer.parseInt(javaVersion[1]);
				// wenn kein update vorhanden dann setze auf 0
				javaUpdate = 0;
				if (javaVersion.length > 3) {
					javaUpdate = Integer.parseInt(javaVersion[3]);
				}
			} catch (NumberFormatException e) {
				myLogger.error(
						"Java Version konnte nicht sauber festgestellt werden!",
						e);
			}

			String outputText = LPMain.getTextRespectUISPr("lp.jvm.version")
					+ " ("
					+ LPMain.getTextRespectUISPr("lp.jvm.version.installiert")
					+ ": "
					+ clientJavaVersion
					+ " "
					+ LPMain.getTextRespectUISPr("lp.jvm.versionen.freigebenen")
					+ ", ab: " + javaMinVersionApproved + ", unter: "
					+ javaVersionNext + ")";

			// Unter 1.7.0_20 und ab 1.8.0 Fehlermeldung
			if (javaMajorRelease < javaMajorReleaseApproved
					|| javaMajorRelease == javaMajorReleaseApproved
					&& javaUpdate < javaUpdateApproved
					|| javaMajorRelease > javaMajorReleaseApproved) {

				jOptionPane.showMessageDialog(
						LPMain.getInstance().getDesktop(),
						"<html><body><p style='width: 200px;'>" + outputText
								+ "</p></body></html>", "JVM Hinweis",
						jOptionPane.WARNING_MESSAGE);

			}
		}

	}

	private void initComponents() {
		HelperClient.setComponentNames(this);
	}

	private void jbInit() throws Throwable {
		String locParameter = System.getProperty("loc");

		Locale locUi = Locale.getDefault();

		if (locParameter != null && locParameter.length() >= 4) {
			if (locParameter.charAt(2) == '_') {
				locParameter = locParameter.replaceFirst("_", "");
			}

			locParameter = Helper.fitString2Length(locParameter, 10, ' ');

			locUi = Helper.string2Locale(locParameter);
		}
		
		
		// Dialog
		setTitle(LPMain.getInstance().getTextRespectUISPr("lp.anmeldung"));
		setIconImage(ImageIO.read(getClass().getResource(
				"/com/lp/client/res/heliumv.png")));
			
		Dimension dimensionLogin = new Dimension(400,225);
		Dimension dimensionMandantenwechsel = new Dimension(400,140);
		
		if (fontSize > 12) {
			dialogSetDynamicSize(fontSize, dimensionLogin, maxwidth, maxheight);
		} else {
			
		setMinimumSize(new Dimension(dimensionLogin.width, dimensionLogin.height));
		setMaximumSize(new Dimension(dimensionLogin.width, dimensionLogin.height));
		setPreferredSize(new Dimension(dimensionLogin.width, dimensionLogin.height));
		
		}
		
		if (bShowMandant) {
			
			setMinimumSize(new Dimension(dimensionMandantenwechsel.width, dimensionMandantenwechsel.height));
			setMaximumSize(new Dimension(dimensionMandantenwechsel.width, dimensionMandantenwechsel.height));
			setPreferredSize(new Dimension(dimensionMandantenwechsel.width, dimensionMandantenwechsel.height));
		}
		
		if (bShowMandant && fontSize > 12) {
			dialogSetDynamicSize(fontSize, dimensionMandantenwechsel, maxwidth, maxheight);
		}
		
		
		setModal(true);
		setLayout(new BorderLayout(5, 5));
		setResizable(false);
		addWindowListener(new DialogLogin_this_windowAdapter(this));

		// Panel
		panel.setLayout(gbl);
		border = BorderFactory.createEmptyBorder(20, 20, 0, 20);
		panel.setBorder(border);
		
		// Benutzer
		wlaBenutzer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.benutzer"));
		wlaBenutzer.setRequestFocusEnabled(true);

		wtfBenutzer.setText("");
		wtfBenutzer
				.addKeyListener(new DialogLogin_wtfBenutzer_keyAdapter(this));
		wtfBenutzer.addFocusListener(new DialogLogin_wtfBenutzer_focusAdapter(
				this));

		// Kennwort
		wlaKennwort.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kennwort"));

		wpwdKennwort
				.addFocusListener(new DialogLogin_wpwdKennwort_focusAdapter(
						this));
		wpwdKennwort.addKeyListener(new DialogLogin_wpwdKennwort_keyAdapter(
				this));
		
		// Sprache
		wlaUILocale.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uisprache"));
		
		Map<?, ?> m = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllLocales(locUi);

		wcoResourcebundels = new WrapperComboBox();
		wcoResourcebundels.setMandatoryField(true);
		wcoResourcebundels.setBorder(new WrapperComboBox().getBorder());
		wcoResourcebundels.setMap(m);

		wcoResourcebundels.setKeyOfSelectedItem(Helper.locale2String(locUi));

		wtfBenutzer.setText("");
		wtfBenutzer
				.addKeyListener(new DialogLogin_wtfBenutzer_keyAdapter(this));
		wtfBenutzer.addFocusListener(new DialogLogin_wtfBenutzer_focusAdapter(
				this));
		// wtfBenutzer.setColumns(12);
		wtfBenutzer.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNUNG);

		// Anmelden
		wbuAnmelden.setEnabled(false);
		wbuAnmelden.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.anmeldung"));
		
		wbuAnmelden
				.addActionListener(new DialogLogin_brnAnmelden_actionAdapter(
						this));

		// Info Button
		javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(getClass()
				.getResource("/com/lp/client/res/info16x16.png"));
		
		btnInfo.setMaximumSize(new Dimension(25, 25));
		btnInfo.setMinimumSize(new Dimension(25, 25));
		btnInfo.setPreferredSize(new Dimension(25, 25));
		btnInfo.setIcon(imageIcon);
		btnInfo.setFocusable(false);
		btnInfo.addActionListener(new DialogLogin_btnInfo_actionAdapter(this));
//		updateBtnInfoEnabled();
		
		wcbKennwortaendern.setText(LPMain
				.getTextRespectUISPr("pers.benutzer.kennwortaendern"));

		// Fehlermeldung
		taOut.setText("");
        taOut.setForeground(Color.RED);
        taOut.setBackground(null);
        taOut.setMargin(new Insets(5, 5, 5, 5));
        taOut.setCaretPosition(0);
        taOut.setEditable(false);
        taOut.setLineWrap(true);
		taOut.setRows(2);
		taOut.setWrapStyleWord(true);
        
        jspTaOut.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jspTaOut.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspTaOut.setBorder(BorderFactory.createEmptyBorder(0, 22, 10, 20));
        jspTaOut.setAlignmentX(CENTER_ALIGNMENT);

		if (bShowMandant) {
			wlaMandant.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.mandant"));
			wcoMandant = new WrapperComboBox(LPMain.getInstance().getMandanten(
					LPMain.getInstance().getBenutzernameRaw(),
					new String(LPMain.getInstance().getKennwortRaw())));
		}

			
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		int yZeile = 0;
		if (!bShowMandant) {
			
			// Benutzereingabe
			gbc.gridx = 0;
			gbc.gridy = yZeile;
			gbc.weightx = 0.3;
			gbl.setConstraints(wlaBenutzer, gbc);
			panel.add(wlaBenutzer);
			
			gbc.gridx = 1;
			gbc.gridy = yZeile;
			gbc.weightx = 0.7;
			gbl.setConstraints(wtfBenutzer, gbc);
			panel.add(wtfBenutzer);
			yZeile++;
			
			// Kennwortabfrage
			gbc.gridx = 0;
			gbc.gridy = yZeile;
			gbc.weightx = 0.3;
			gbl.setConstraints(wlaKennwort, gbc);
			panel.add(wlaKennwort);
			
			gbc.gridx = 1;
			gbc.gridy = yZeile;
			gbc.weightx = 0.7;
			gbl.setConstraints(wpwdKennwort, gbc);
			panel.add(wpwdKennwort);
			yZeile++;
			
			// Sprachauswahl
			gbc.gridx = 0;
			gbc.gridy = yZeile;
			gbc.weightx = 0.3;
			gbl.setConstraints(wlaUILocale, gbc);
			panel.add(wlaUILocale);
			
			gbc.gridx = 1;
			gbc.gridy = yZeile;
			gbc.weightx = 0.7;
			gbl.setConstraints(wcoResourcebundels, gbc);
			panel.add(wcoResourcebundels);
			yZeile++;
			
			// Kennwort aendern
			gbc.gridx = 1;
	        gbc.gridy = yZeile;
	        gbc.gridwidth = 2;
	        gbl.setConstraints(wcbKennwortaendern, gbc);
	        panel.add(wcbKennwortaendern);
	        yZeile++;
	        
		}

		else {
			// Mandanteneingabe
			gbc.gridx = 0;
			gbc.gridy = yZeile;
			gbc.weightx = 0.3;
			gbl.setConstraints(wlaMandant, gbc);
			panel.add(wlaMandant);
			
			gbc.gridx = 1;
			gbc.gridy = yZeile;
			gbc.weightx = 0.7;
			gbl.setConstraints(wcoMandant, gbc);
			panel.add(wcoMandant);
			yZeile++;

			wbuAnmelden.setEnabled(true);
		}


        // Button Info
		gbc.gridx = 0;
        gbc.gridy = yZeile;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.WEST;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbl.setConstraints(btnInfo, gbc);
        panel.add(btnInfo);
        
        // Button Anmeldung
		gbc.gridx = 1;
        gbc.gridy = yZeile;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbl.setConstraints(wbuAnmelden, gbc);
        panel.add(wbuAnmelden);

		getContentPane().add(BorderLayout.PAGE_START, panel);
        
		//***
        //*** wp ***

		
	}

	private void dialogSetDynamicSize(int fontSize, Dimension d, int maxwidth,
			int maxheight) {
		d.width = d.width / 15 * fontSize;
		d.height = d.height / 15 * fontSize;
		
		if (d.width > maxwidth)
			d.width = maxwidth;
		
		if (d.height > maxheight)
			d.height = maxheight;
		
		setMinimumSize(new Dimension(d.width, d.height));	
		setMaximumSize(new Dimension(d.width, d.height));
		setPreferredSize(new Dimension(d.width, d.height));
	}
	
	public boolean kennwortAendern() {
		return wcbKennwortaendern.isSelected();
	}

	public char[] getNeuesKennwort() {
		return neuesKennwort;
	}

	void brnAnmelden_actionPerformed(ActionEvent e) {
		setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

		if (wcbKennwortaendern.isSelected()) {
			DialogNeueskennwort dialog = new DialogNeueskennwort(frame, "");
			neuesKennwort = dialog.getNeuesKennwort();
		}

		if (!bShowMandant) {
			LPMain.getInstance().setBenutzernameRaw(wtfBenutzer.getText());
			LPMain.getInstance().setKennwortRaw(wpwdKennwort.getPassword());
			LPMain.getInstance().setMandantRaw(null);
			LPMain.getInstance().setUISprLocale(
					Helper.string2Locale((String) wcoResourcebundels
							.getKeyOfSelectedItem()));

		} else {
			String m = (String) wcoMandant.getSelectedItem();
			LPMain.getInstance().setMandantRaw(m.substring(0, m.indexOf("|")));
		}
		setCursor(java.awt.Cursor
				.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		dispose();
	}

	void wtfBenutzerFocusLost(FocusEvent e) {
		checkBenutzer();
		checkAnmelden();
	}

	private void checkBenutzer() {
		String benutzer = wtfBenutzer.getText();
		if ((benutzer == null)
				|| ((benutzer != null) && (benutzer.length() < BenutzerFac.MIN_BENUTZER_KENNUNG))) {
			showInfo(LPMain.getInstance().getTextRespectUISPr(
					"lp.error.benutzer"));
		}
	}

	void wpwdKennwortFocusLost(FocusEvent e) {
		checkKennwort();
		checkAnmelden();
	}

	private void checkKennwort() {
		char[] password = wpwdKennwort.getPassword();
		if ((password == null)
				|| (password != null && password.length < BenutzerFac.MIN_BENUTZER_KENNWORT)) {
			showInfo(LPMain.getInstance().getTextRespectUISPr(
					"lp.error.kennwort"));
		}
	}

	/**
	 * Pruefe ob Anmeldebutton enabled werden kann.
	 */
	private void checkAnmelden() {
		boolean bAnmelden = (wtfBenutzer.getText() != null
				&& wpwdKennwort.getPassword() != null
				&& wtfBenutzer.getText().length() >= BenutzerFac.MIN_BENUTZER_KENNUNG && wpwdKennwort
				.getPassword().length >= BenutzerFac.MIN_BENUTZER_KENNWORT);

		wbuAnmelden.setEnabled(bAnmelden);
		getRootPane().setDefaultButton(wbuAnmelden);
	}


	
	public void showInfo(String info) {
		Dimension dimensionScrollpane = new Dimension(350,270);
		taOut.setText(info);
		taOut.setCaretPosition(0);
		getContentPane().add(BorderLayout.CENTER, jspTaOut);

			if (fontSize > 12) {
				dialogSetDynamicSize(fontSize, dimensionScrollpane, maxwidth, maxheight);
				}
			
			else {
				setMinimumSize(new Dimension(dimensionScrollpane.width, dimensionScrollpane.height));
				setMaximumSize(new Dimension(dimensionScrollpane.width, dimensionScrollpane.height));
				setPreferredSize(new Dimension(dimensionScrollpane.width, dimensionScrollpane.height));
				
			}
		
	}

	void this_windowClosing(WindowEvent e) {
		((Desktop) getOwner()).setBAbbruch(true);
		LPMain.getInstance().getDesktop().exitClientDlg();
	}

	public boolean isBShowMandant() {
		return bShowMandant;
	}

	public void setBShowMandantI(boolean bShowMandantI) {
		bShowMandant = bShowMandantI;
	}

	void wtfBenutzerKeyPressed(KeyEvent e) {
		checkAnmelden();
	}

	void wpwdKennwortKeyPressed(KeyEvent e) {
		checkAnmelden();
	}
	

	// Funktion "Exit Client" vom Info-Button entfernt und auf das "Window Closing Event" gelegt
	public void btnInfo_actionPerformed(ActionEvent e) {

			try {
				LPMain.getInstance().getDesktop().showAboutDialog();
			} catch (Throwable e1) {
				e1.printStackTrace();
			}

	}
	
//	private boolean isUserAvailable() throws Throwable {
//		return LPMain.getTheClient() != null ;
//	}
	
	//***
	//*** wp ***

}

class DialogLogin_btnInfo_actionAdapter implements ActionListener {
	private DialogLogin adaptee;

	DialogLogin_btnInfo_actionAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnInfo_actionPerformed(e);
	}
}

// ****************************************************************************************
class DialogLogin_brnAnmelden_actionAdapter implements
		java.awt.event.ActionListener {
	DialogLogin adaptee;

	DialogLogin_brnAnmelden_actionAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.brnAnmelden_actionPerformed(e);
	}
}

class DialogLogin_this_windowAdapter extends java.awt.event.WindowAdapter {
	DialogLogin adaptee;

	DialogLogin_this_windowAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}

class DialogLogin_wtfBenutzer_focusAdapter extends java.awt.event.FocusAdapter {
	DialogLogin adaptee;

	DialogLogin_wtfBenutzer_focusAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfBenutzerFocusLost(e);
	}
}

class DialogLogin_wpwdKennwort_focusAdapter extends java.awt.event.FocusAdapter {
	DialogLogin adaptee;

	DialogLogin_wpwdKennwort_focusAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wpwdKennwortFocusLost(e);
	}
}

class DialogLogin_wtfBenutzer_keyAdapter extends java.awt.event.KeyAdapter {
	DialogLogin adaptee;

	DialogLogin_wtfBenutzer_keyAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e) {
		adaptee.wtfBenutzerKeyPressed(e);
	}
}

class DialogLogin_wpwdKennwort_keyAdapter extends java.awt.event.KeyAdapter {
	DialogLogin adaptee;

	DialogLogin_wpwdKennwort_keyAdapter(DialogLogin adaptee) {
		this.adaptee = adaptee;
	}

	public void keyReleased(KeyEvent e) {
		adaptee.wpwdKennwortKeyPressed(e);
	}

}
