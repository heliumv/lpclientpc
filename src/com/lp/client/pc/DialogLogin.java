/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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

import java.awt.Color;
import java.awt.Dimension;
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	private JPanel jpaOben = new JPanel();
	private GridBagLayout gridBagLayout = new GridBagLayout();
	private WrapperComboBox wcoResourcebundels = null;
	private GridBagLayout gridBagLayoutAussen = new GridBagLayout();
	private WrapperLabel wlaUILocale = new WrapperLabel();
	private WrapperLabel wlaMandant = new WrapperLabel();
	private Border border = null;
	private WrapperComboBox wcoMandant = null;

	private boolean bShowMandant = false;
	private JButton btnInfo = new JButton();
	private Border borderSpTaOut = null;
	private Frame frame = null;
	private char[] neuesKennwort = null;

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

	public boolean kennwortAendern() {
		return wcbKennwortaendern.isSelected();
	}

	private void checkJVM() {
		String sJVM = System.getProperty("java.version");
		String tmp = sJVM;
		if (sJVM != null) {
			if (sJVM.length() < 8) {
				tmp = sJVM.substring(0);
			} else {
				tmp = sJVM.substring(0, 8);
			}
		}
		sJVM = tmp;
		if ((sJVM != null && (sJVM.startsWith("1.6.") || sJVM
				.startsWith("1.7.")) == false)) {
			taOut.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.jvm.version")
					+ " ("
					+ LPMain.getInstance().getTextRespectUISPr(
							"lp.jvm.version.installiert")
					+ ": "
					+ sJVM
					+ " "
					+ LPMain.getInstance().getTextRespectUISPr(
							"lp.jvm.versionen.freigebenen")
					+ ": "
					+ "1.6.*, 1.7.* " + ")");
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

		Map m = DelegateFactory.getInstance().getLocaleDelegate()
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

		wlaBenutzer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.benutzer"));
		wlaBenutzer.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		wlaBenutzer.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
		wlaBenutzer.setRequestFocusEnabled(true);
		wlaBenutzer.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaBenutzer.setHorizontalTextPosition(SwingConstants.RIGHT);

		wlaKennwort.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kennwort"));
		wlaKennwort.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		wlaKennwort.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
		wlaKennwort.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaKennwort.setHorizontalTextPosition(SwingConstants.RIGHT);
		wtfBenutzer.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNUNG);

		wbuAnmelden.setEnabled(false);
		wbuAnmelden.setHorizontalAlignment(SwingConstants.CENTER);
		wbuAnmelden.setHorizontalTextPosition(SwingConstants.CENTER);
		wbuAnmelden.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.anmeldung"));
		wbuAnmelden
				.addActionListener(new DialogLogin_brnAnmelden_actionAdapter(
						this));

		wcbKennwortaendern.setText(LPMain
				.getTextRespectUISPr("pers.benutzer.kennwortaendern"));
		wcbKennwortaendern.setHorizontalAlignment(SwingConstants.RIGHT);

		// getRootPane().setDefaultButton(wbuAnmelden);

		setModal(true);
		setResizable(false);
		setTitle(LPMain.getInstance().getTextRespectUISPr("lp.anmeldung"));
		getContentPane().setLayout(gridBagLayoutAussen);
		addWindowListener(new DialogLogin_this_windowAdapter(this));

		// taOut.setAlignmentX( (float) 1);
		// taOut.setAlignmentY( (float) 1);
		// taOut.setRequestFocusEnabled(false);
		taOut.setForeground(Color.RED);
		taOut.setEditable(false);
		taOut.setText("");
		taOut.setLineWrap(true);
		taOut.setRows(4);
		taOut.setWrapStyleWord(true);
		taOut.setBackground(UIManager.getColor("control"));
		borderSpTaOut = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		jspTaOut.setBorder(borderSpTaOut);

		javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(getClass()
				.getResource("/com/lp/client/res/message.png"));
		btnInfo.setMaximumSize(new Dimension(25, 25));
		btnInfo.setMinimumSize(new Dimension(25, 25));
		btnInfo.setPreferredSize(new Dimension(25, 25));
		btnInfo.setIcon(imageIcon);
		updateBtnInfoEnabled();

		jpaOben.setLayout(gridBagLayout);
		jpaOben.setAlignmentX((float) 0.0);
		jpaOben.setAlignmentY((float) 0.0);
		border = BorderFactory.createEmptyBorder(0, 20, 0, 20);
		jpaOben.setBorder(border);

		int w = 320;
		int h = 160;
		jpaOben.setMaximumSize(new Dimension(w, h));
		jpaOben.setMinimumSize(new Dimension(w, h));
		jpaOben.setPreferredSize(new Dimension(w, h));

		wlaUILocale.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.uisprache"));

		if (bShowMandant) {
			wlaMandant.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.mandant"));
			wcoMandant = new WrapperComboBox(LPMain.getInstance().getMandanten(
					LPMain.getInstance().getBenutzernameRaw(),
					new String(LPMain.getInstance().getKennwortRaw())));
		}

		wpwdKennwort
				.addFocusListener(new DialogLogin_wpwdKennwort_focusAdapter(
						this));
		wpwdKennwort.addKeyListener(new DialogLogin_wpwdKennwort_keyAdapter(
				this));

		btnInfo.addActionListener(new DialogLogin_btnInfo_actionAdapter(this));

		getContentPane().add(
				jspTaOut,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 5, 0, 2), 0, 0));
		getContentPane().add(
				jpaOben,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		int iZeile = 0;
		if (!bShowMandant) {
			jpaOben.add(wlaBenutzer, new GridBagConstraints(0, iZeile, 1, 1,
					0.4, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaOben.add(wtfBenutzer, new GridBagConstraints(1, iZeile, 1, 1,
					0.6, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			iZeile++;
			jpaOben.add(wlaKennwort, new GridBagConstraints(0, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaOben.add(wpwdKennwort, new GridBagConstraints(1, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			iZeile++;
			jpaOben.add(wlaUILocale, new GridBagConstraints(0, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			jpaOben.add(wcoResourcebundels, new GridBagConstraints(1, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			iZeile++;
			jpaOben.add(wcbKennwortaendern,
					new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.EAST,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

		} else {
			iZeile++;
			jpaOben.add(wlaMandant, new GridBagConstraints(0, iZeile, 1, 1,
					0.4, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaOben.add(wcoMandant, new GridBagConstraints(1, iZeile, 1, 1,
					0.6, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			// Hier immer einschalten.
			wbuAnmelden.setEnabled(true);
		}
		iZeile++;
		jpaOben.add(wbuAnmelden, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		getContentPane().add(
				btnInfo,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
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
		taOut.setText(info);
	}

	void this_windowClosing(WindowEvent e) {
		((Desktop) getOwner()).setBAbbruch(true);
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

	/**
	 * About-Dialog (Systeminfo) anzeigen.
	 * 
	 * Die Methode verl&auml;sst sich darauf, dass Desktop != null ist
	 * 
	 * @param e
	 */
	public void btnInfo_actionPerformed(ActionEvent e) {
		try {
			LPMain.getInstance().getDesktop().showAboutDialog();
		} catch (Throwable ex) {
			LPMain.getInstance().getDesktop().exitClientDlg();
		}
	}

	/**
	 * Beim allerersten Login gibt es noch keinen Desktop, deshalb macht es auch
	 * keinen Sinn den About-Button freizuschalten.
	 */
	private void updateBtnInfoEnabled() {
		btnInfo.setEnabled(LPMain.getInstance().getDesktop() != null);
	}
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
