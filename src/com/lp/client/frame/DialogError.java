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
package com.lp.client.frame;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ServerJavaAndOSInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * <p>
 * Organisation:
 * </p>
 * 
 * Author $Author: robert $ Revision $Revision: 1.14 $ Letzte Aenderung:
 * $Date: 2012/10/19 13:19:04 $
 */
@SuppressWarnings("static-access")
public class DialogError extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jpaOben = null;
	private JTextArea wlaMessage = null;
	private JToggleButton jtbuDetails = null;
	private JButton jbuSchliessen = null;
	private JButton jbuKopieren = null;

	private JPanel jpaUnten = null;
	private JScrollPane jspScrollPane = null;
	private JTextArea wtaInfo = null;
	private JLabel jlaIcon = null;

	private final static int HEIGHT_WITHOUT_DETAIL = 105;
	private final static int HEIGHT_WITH_DETAIL = 470;
	private final static int WIDTH = 650;

	private final static String ACTION_DETAIL_MESSAGE = "action_detail_message";
	private final static String ACTION_CLOSE = "action_close";
	private final static String ACTION_COPY_TO_CLIPBOARD = "action_copy_to_clipboard";

	public final static int TYPE_ERROR = JOptionPane.ERROR_MESSAGE;
	public final static int TYPE_WARNING = JOptionPane.WARNING_MESSAGE;
	public final static int TYPE_INFORMATION = JOptionPane.INFORMATION_MESSAGE;
	public final static int TYPE_PLAIN = JOptionPane.PLAIN_MESSAGE;
	public final static int TYPE_QUESTION = JOptionPane.QUESTION_MESSAGE;

	private ExceptionLP exceptionLP = null;
	private int iType;

	public DialogError(Frame frame, Throwable t, int iType) {
		super(frame, getTitle(iType), true);

		try {
			if (t != null && t instanceof ExceptionLP) {
				this.exceptionLP = (ExceptionLP) t;
			} else {
				this.exceptionLP = new ExceptionLP(EJBExceptionLP.FEHLER, t);
			}
			this.iType = iType;
			jbInit();
			setDefaults();

			try {
				if (iType == DialogError.TYPE_ERROR) {
					LPMain.getInstance()
							.getDesktop()
							.screenshotVersenden(frame, "[HV Error Info] ",
									buildDetailMessage());
				}
			} catch (Throwable e) {
				// ABSICHT
				e.printStackTrace();
			}
			
			setVisible(true);
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(null, ex);
		}
	}

	private void setDefaults() {
		this.setResizable(false);
		this.setSize(new Dimension(Defaults.getInstance().bySizeFactor(WIDTH), Defaults.getInstance().bySizeFactor(HEIGHT_WITHOUT_DETAIL)));
		// in die Mitte des Frames plazieren
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(this);

		String sText = null;
		try {
			sText = LPMain.getInstance().getMsg(exceptionLP);
			if (sText == null) {
				sText = LPMain.getInstance().getTextRespectUISPr(
						"lp.error.fatal");
			}
//			sText = Helper.wrap(sText,
//					com.lp.client.pc.Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
			String sZusatzinformation = getZusatzinformation(exceptionLP);
			if (sZusatzinformation != null) {
				sText = sText + "\n\n" + sZusatzinformation;
			}
		} catch (Exception ex) {
		}
		wlaMessage.setText(sText);
		wtaInfo.setText(buildDetailMessage());
		wtaInfo.setCaretPosition(0);
	}

	private String getZusatzinformation(ExceptionLP exceptionLP) {
		String sInfo = null;
		switch (exceptionLP.getICode()) {
//		case EJBExceptionLP.FEHLER_BEIM_LOESCHEN: {
//			if (exceptionLP.getAlInfoForTheClient() != null
//					&& exceptionLP.getAlInfoForTheClient().size() > 0) {
//				sInfo = LPMain.getInstance().getTextRespectUISPr(
//						exceptionLP.getAlInfoForTheClient().get(0) + "");
//			}
//		}
//			break;
		case EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT: {
			ArrayList<?> aMsg = exceptionLP.getAlInfoForTheClient();
			if (aMsg != null && !aMsg.isEmpty() && aMsg.get(0) != null) {
				sInfo = aMsg.get(0).toString();
			}
		}

		}
		return sInfo;
	}

	private String buildDetailMessage() {
		StringBuffer sb = new StringBuffer();


		// Fehlercode
		sb.append("Fehlerbeschreibung: " + wlaMessage.getText());
		sb.append('\n');
		sb.append("Fehlercode: " + exceptionLP.getICode());
		sb.append('\n');
		
		sb.append("Java-Version: " +  System.getProperty("java.vm.version"));
		sb.append('\n');
		// Uhrzeit am Client
		sb.append("Clientuhrzeit: ");
		Locale locale;
		try {
			locale = LPMain.getInstance().getTheClient().getLocUi();
		} catch (Throwable ex) {
			locale = Locale.GERMAN;
		}
		java.sql.Timestamp t = new java.sql.Timestamp(
				System.currentTimeMillis());
		sb.append(Helper.formatTimestamp(t, DateFormat.MEDIUM, DateFormat.LONG,
				locale));
		sb.append('\n');
		// Benutzerkennung
		try {
			sb.append("User: "
					+ LPMain.getInstance().getTheClient().getIDUser());
			sb.append('\n');
		} catch (Throwable ex1) {
			// nix hier. wenn ich das nicht krieg, ist sowieso alles kaputt.
		}
		// All Info For The Client anzeigen
		ArrayList<?> aMsg = exceptionLP.getAlInfoForTheClient();
		if (aMsg != null) {
			sb.append("Zusatzinformation: ");
			for (Iterator<?> iter = aMsg.iterator(); iter.hasNext();) {
				Object item = (Object) iter.next();
				sb.append(item.toString() + "\n");
			}
		}
		sb.append("\n");
		sb.append("==Client Java and OS info==\n");		
		ServerJavaAndOSInfo clientInfo = new ServerJavaAndOSInfo();
		clientInfo.initProperties();
		sb.append(clientInfo.formatString() + "\n");
		
		try {
			sb.append("==Server Java and OS info==\n");
			ServerJavaAndOSInfo serverInfo = DelegateFactory.getInstance().getSystemDelegate().getJavaAndOSInfo();
			sb.append(serverInfo.formatString());
		} catch (Throwable e) {
			sb.append("Serverinfo not available\n");
		}

		// noch einen abstand dazwischen
		sb.append('\n');
		// Klasse
		Class<?> clazz;
		String sMessage;
		// Message
		if (exceptionLP != null) {
			StackTraceElement[] st;
			if (exceptionLP.getThrowable() != null) {
				st = exceptionLP.getThrowable().getStackTrace();
				clazz = exceptionLP.getThrowable().getClass();
				sMessage = exceptionLP.getThrowable().getMessage();
			} else {
				st = exceptionLP.getStackTrace();
				clazz = exceptionLP.getClass();
				if (exceptionLP.getSMsg() != null) {
					sMessage = exceptionLP.getSMsg();
				} else {
					sMessage = exceptionLP.getMessage();
				}
			}
			sb.append(clazz);
			sb.append('\n');
			sb.append(sMessage);
			sb.append('\n');
			// auf maximal 20 Zeilen beschraenken
			for (int i = 0; (i < st.length) && (i < 15); i++) {
				sb.append(st[i].toString());
				sb.append("\n");
			}
		} else {
			sb.append("kein Stacktrace verf\u00FCgbar");
		}
		// Ursache ebenfalls - da ist v.a. ein Server-Stacktrace ganz wichtig.
		if (exceptionLP.getCause() != null) {
			sb.append("\n\nStacktrace Verursacher:\n");
			StackTraceElement[] st = exceptionLP.getCause().getStackTrace();
			for (int i = 0; (i < st.length); i++) {
				sb.append(st[i].toString());
				sb.append("\n");
			}
			
			
			if (exceptionLP.getCause().getCause() != null) {
				sb.append("\n\nStacktrace weiterer Verursacher:\n");
				StackTraceElement[] st2 = exceptionLP.getCause().getCause().getStackTrace();
				for (int i = 0; (i < st2.length); i++) {
					sb.append(st2[i].toString());
					sb.append("\n");
				}
			}
			
		}
		
		
		
		return sb.toString();
	}

	private static String getTitle(int iType) {
		String s = null;
		switch (iType) {
		case TYPE_ERROR: {
			s = LPMain.getInstance().getTextRespectUISPr("lp.error");
		}
			break;
		case TYPE_WARNING: {
			s = LPMain.getInstance().getTextRespectUISPr("lp.warning");
		}
			break;
		case TYPE_INFORMATION: {
			s = LPMain.getInstance().getTextRespectUISPr("lp.hint");
		}
			break;
		case TYPE_PLAIN: {
			s = "";
		}
			break;
		case TYPE_QUESTION: {
			s = LPMain.getInstance().getTextRespectUISPr("lp.frage");
		}
			break;
		}
		return s;
	}

	private void jbInit() throws Throwable {
		wlaMessage = new JTextArea();
		// Fabe anpassen, damits wie ein label aussieht
		wlaMessage.setEditable(false);
		wlaMessage.setLineWrap(true);
		wlaMessage.setWrapStyleWord(true);
		wlaMessage.setForeground(new JLabel().getForeground());
		wlaMessage.setBackground(new JLabel().getBackground());
		// wlaMessage.setHorizontalAlignment(SwingConstants.CENTER);
		jtbuDetails = new JToggleButton();

		wtaInfo = new JTextArea();
		wtaInfo.setEditable(false);
		jtbuDetails.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.detail"));
		jtbuDetails.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		jtbuDetails.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		jtbuDetails.setActionCommand(ACTION_DETAIL_MESSAGE);
		jtbuDetails.addActionListener(new DialogError_actionAdapter(this));
		jbuSchliessen = new JButton();
		jbuSchliessen.setActionCommand(ACTION_CLOSE);
		jbuSchliessen.addActionListener(new DialogError_actionAdapter(this));
		jbuSchliessen.setText(LPMain.getInstance().getTextRespectUISPr("OK"));
		jbuKopieren = new JButton();
		jbuKopieren.setActionCommand(ACTION_COPY_TO_CLIPBOARD);
		jbuKopieren.addActionListener(new DialogError_actionAdapter(this));
		jbuKopieren.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.inzwischenablagekopieren"));
		jlaIcon = new JLabel();
		jlaIcon.setMinimumSize(new Dimension(30, 30));
		jlaIcon.setPreferredSize(new Dimension(30, 30));
		Icon icon = HelperClient.getIconForType(iType);
		if (icon != null) {
			jlaIcon.setIcon(icon);
		}
		// oberes Panel
		jpaOben = new JPanel(new GridBagLayout());
		jpaOben.add(jlaIcon, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaOben.add(wlaMessage, new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						3, 20, 3, 20), 0, 0));
		jpaOben.add(jbuSchliessen, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaOben.add(jtbuDetails, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(
				jpaOben,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10,
								10), 0, 0));
		// unteres Panel
		jpaUnten = new JPanel(new GridBagLayout());
		getContentPane().add(
				jpaUnten,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 10, 10, 10), 0, 0));
		jspScrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspScrollPane.getViewport().add(wtaInfo, null);
		jpaUnten.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaUnten.add(jbuKopieren, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jspScrollPane.setVisible(false);
	}

	protected void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_CLOSE)) {
			dispose();
		} else if (e.getActionCommand().equals(ACTION_DETAIL_MESSAGE)) {
			boolean bVisible = jtbuDetails.isSelected();
			this.setSize(new Dimension(Defaults.getInstance().bySizeFactor(WIDTH), Defaults.getInstance().bySizeFactor(bVisible ? HEIGHT_WITH_DETAIL : HEIGHT_WITHOUT_DETAIL)));
			jspScrollPane.setVisible(bVisible);
			wtaInfo.validate();
			jspScrollPane.validate();
			validate();
		} else if (e.getActionCommand().equals(ACTION_COPY_TO_CLIPBOARD)) {
			final Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clipboard.setContents(new StringSelection(wtaInfo.getText()), null);
		}
	}
}

class DialogError_actionAdapter implements java.awt.event.ActionListener {
	DialogError adaptee = null;

	DialogError_actionAdapter(DialogError adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.actionPerformed(e);
	}
}
