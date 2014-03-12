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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ServerLocaleInfo;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
public class DialogAbout extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelAbout = new JPanel();
	JPanel panelLizeninfo = new JPanel();
	javax.swing.ImageIcon imageIconLogistikpur = new javax.swing.ImageIcon(
			getClass().getResource("/com/lp/client/res/heliumv_info.png"));
	JLabel lblAdresse = new JLabel();
	GridBagLayout gbLayout1 = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	GridBagLayout gbLayout2 = new GridBagLayout();
	JLabel lblIconLogistikpur = new JLabel();
	JLabel lblScch = new JLabel();
	JLabel lblErp = new JLabel();
	JLabel lblLinie = new JLabel();
	JLabel lblSprachen = new JLabel();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel lblIpadresse = new JLabel();
	JButton buttonLogfileAnzeigen = new JButton();
	WrapperTable jTableVersion = null;
	javax.swing.JPanel jPanelTable = new JPanel();

	public DialogAbout(Frame owner, String title, boolean modal)
			throws Throwable {
		super(owner, title, modal);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonLogfileAnzeigen)) {
			try {

				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				File logFile = new File("log", "lpclient_");
				BufferedReader in = new BufferedReader(new FileReader(logFile));
				String zeile = null;
				StringBuffer text = new StringBuffer();
				try {
					String crlf = new String(new byte[] { 13, 10 });
					while ((zeile = in.readLine()) != null) {

						text.append(zeile).append(crlf);
					}

					try {
						DialogFactory.showMessageMitScrollbar(
								logFile.getAbsolutePath(), new String(text),
								true);
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} catch (FileNotFoundException e1) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				DialogFactory
						.showModalDialog("Fehler",
								"Die angegebene Datei 'log/lpclient_' exisitert nicht.");
				return;
			} finally {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

		}
	}

	private void jbInit() throws Throwable {

		AnwenderDto anwenderDto = DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.anwenderFindByPrimaryKey(
						new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

		MandantDto mandantDto_Hauptmandant = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.mandantFindByPrimaryKey(
						anwenderDto.getMandantCNrHauptmandant());

		String server = System.getProperty("java.naming.provider.url");
		try {
			int iB = server.indexOf("//") + 2;
			int iE = server.length();
			server = server.substring(iB, iE);
		} catch (Exception ex) {
			server = "?";
		}

		SystemInfoController infoController = new SystemInfoController(
				DelegateFactory.getInstance().getSystemDelegate());
		PingSystemInfo pingInfo = infoController.getPingInfo();
		ServerLocaleInfo localeInfo = infoController.getServerLocaleInfo() ;
		ServerLocaleInfo clientLocaleInfo = infoController.getClientLocaleInfo() ;

		String[] s = LPMain.getInstance().getTheClient().getBenutzername()
				.trim().split("\\|");

		String[] columnNames = { "1", "2", "3", "4" };
		Object[][] data = {
				{
						LPMain.getInstance().getTextRespectUISPr(
								"lp.about.hvbenutzer"),
						s[0],
						LPMain.getInstance().getTextRespectUISPr(
								"lp.about.angemeldetanserver"), server },
				{
						LPMain.getInstance().getTextRespectUISPr(
								"lp.about.systembenutzer"),
						System.getProperty("user.name"),
						LPMain.getInstance().getTextRespectUISPr(
								"lp.about.computername"), s[1] },
				{ "", "", "", "" },
				{
						LPMain.getInstance().getTextRespectUISPr(
								"lp.aktuelleversion"), "",
						System.getProperty("java.class.path"),
						System.getProperty("java.library.path") },

				{
						LPMain.getInstance().getTextRespectUISPr("lp.client"),
						LPMain.getInstance()
								.getLPParameter("lp.version.client")
								+ "."
								+ LPMain.getInstance().getLPParameter(
										"lp.version.client.build"),
						LPMain.getInstance().getTextRespectUISPr("lp.cur")
								+ " "
								+ Runtime.getRuntime().freeMemory()
								/ 1000
								+ LPMain.getInstance().getTextRespectUISPr(
										"lp.kb"),
						LPMain.getInstance().getTextRespectUISPr("lp.max")
								+ Runtime.getRuntime().maxMemory()
								/ 1000
								+ " "
								+ LPMain.getInstance().getTextRespectUISPr(
										"lp.kb")
								+ ", "
								+ LPMain.getInstance().getTextRespectUISPr(
										"lp.tot")
								+ Runtime.getRuntime().totalMemory()
								/ 1000
								+ " "
								+ LPMain.getInstance().getTextRespectUISPr(
										"lp.kb") },

				{
						LPMain.getInstance().getTextRespectUISPr("lp.server"),
						anwenderDto.getCVersionServer() + "."
								+ anwenderDto.getIBuildnummerServer(),
						LPMain.getInstance()
								.getLPParameter("lp.version.client")
								+ "."
								+ anwenderDto.getIBuildnummerClienVon(),
						LPMain.getInstance()
								.getLPParameter("lp.version.client")
								+ "."
								+ anwenderDto.getIBuildnummerClientBis() },

				{
						LPMain.getInstance()
								.getTextRespectUISPr("lp.datenbank"),
						anwenderDto.getCVersionDB() + "."
								+ anwenderDto.getIBuildnummerDB(),
						anwenderDto.getCVersionServer() + "."
								+ anwenderDto.getIBuildnummerServerVon(),
						anwenderDto.getCVersionServer() + "."
								+ anwenderDto.getIBuildnummerServerBis() },

				{ "", "", "", "" },

				{ "JRE", System.getProperty("java.version"),
						System.getProperty("java.vendor"),
						System.getProperty("java.version") },

				{ "JVSM", System.getProperty("java.vm.specification.version"),
						System.getProperty("java.vm.specification.vendor"),
						System.getProperty("java.vm.specification.name") },

				{ "JVM", System.getProperty("java.vm.version"),
						System.getProperty("java.vm.vendor"),
						System.getProperty("java.vm.name") },

				{
						LPMain.getInstance().getTextRespectUISPr(
								"lp.betriebssystem"),
						System.getProperty("os.name"),
						System.getProperty("os.arch"),
						System.getProperty("os.version") + " ("
								+ java.awt.Desktop.isDesktopSupported() + ")" },
				{ "Client Locale", 
						"country: "  + clientLocaleInfo.getCountry(), 
						"language: " + clientLocaleInfo.getLanguage(),
						"timezone: " + clientLocaleInfo.getTimezone()
				},
				{ "Server Locale", 
						"country: "  + localeInfo.getCountry(), 
						"language: " + localeInfo.getLanguage(),
						"timezone: " + localeInfo.getTimezone()
				}, 
				{ "Ping (" + pingInfo.getPingsReceived() + ")",
						"min " + pingInfo.getMinTime() + "ms",
						"max " + pingInfo.getMaxTime() + "ms",
						"med " + pingInfo.getMedianTime() + "ms" } };
		jTableVersion = new WrapperTable(null, data, columnNames);
		panelAbout.setLayout(gbLayout1);
		panelLizeninfo.setLayout(gbLayout2);

		lblIconLogistikpur.setIcon(imageIconLogistikpur);

		jTableVersion.setMaximumSize(new Dimension(700, 250));
		jTableVersion.setMinimumSize(new Dimension(700, 250));
		jTableVersion.setPreferredSize(new Dimension(700, 250));
		jPanelTable.setAlignmentX((float) 0.0);
		jPanelTable.setAlignmentY((float) 0.0);
		jPanelTable.setMaximumSize(new Dimension(700, 32767));
		jPanelTable.setMinimumSize(new Dimension(710, 205));
		jPanelTable.setPreferredSize(new Dimension(710, 205));
		panelAbout.setMaximumSize(new Dimension(710, 220));
		panelAbout.setMinimumSize(new Dimension(710, 220));
		panelAbout.setPreferredSize(new Dimension(710, 220));

		gbLayout1.setConstraints(lblIconLogistikpur, new GridBagConstraints(1,
				0, 1, 2, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(20, 40, 0, 40), 0, 0));
		panelAbout.add(lblIconLogistikpur);

		// JLabel voe = new
		// JLabel("<html><u>Vertrieb und Support Oesterreich:</u></html>");
		JLabel voe = new JLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.vertrieb.at"));
		gbLayout1.setConstraints(voe, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(voe);

		// JLabel vdeu = new
		// JLabel("<html><u>Vertrieb und Support Deutschland:</u></html>");
		JLabel vdeu = new JLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.vertrieb.de"));
		gbLayout1.setConstraints(vdeu, new GridBagConstraints(2, 1, 1, 1, 0, 0,
				GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(vdeu);

		JLabel lognam = new JLabel("HELIUM V IT-Solutions GmbH");
		gbLayout1.setConstraints(lognam, new GridBagConstraints(0, 2, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(lognam);

		JLabel hvman = new JLabel("Helium V ERP-Systeme GmbH");
		gbLayout1.setConstraints(hvman, new GridBagConstraints(2, 2, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(hvman);

		JLabel logstr = new JLabel("Nordstra\u00DFe 4");
		gbLayout1.setConstraints(logstr, new GridBagConstraints(0, 3, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(logstr);

		JLabel hvstr = new JLabel("Wankelstra\u00DFe 14");
		gbLayout1.setConstraints(hvstr, new GridBagConstraints(2, 3, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(hvstr);

		JLabel logort = new JLabel("A-5301 Eugendorf bei Salzburg");
		gbLayout1.setConstraints(logort, new GridBagConstraints(0, 4, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(logort);

		JLabel hvort = new JLabel("D-70563 Stuttgart");
		gbLayout1.setConstraints(hvort, new GridBagConstraints(2, 4, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(hvort);

		JLabel logtel = new JLabel("Tel.: +43-6225-28088-0");
		gbLayout1.setConstraints(logtel, new GridBagConstraints(0, 5, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(logtel);

		JLabel hvemail = new JLabel("Email: support@HeliumV.com");
		gbLayout1.setConstraints(hvemail, new GridBagConstraints(1, 5, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(hvemail);

		JLabel hvtel = new JLabel("Tel.: +49-711-2172 0058-0");
		gbLayout1.setConstraints(hvtel, new GridBagConstraints(2, 5, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(hvtel);

		JLabel logfax = new JLabel("Fax: +43-6225-28088-99");
		gbLayout1.setConstraints(logfax, new GridBagConstraints(0, 6, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 20, 0), 0, 0));
		panelAbout.add(logfax);

		JLabel hvhome = new JLabel("www.HeliumV.com");
		gbLayout1.setConstraints(hvhome, new GridBagConstraints(1, 6, 1, 1, 0,
				0, GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(hvhome);

		JLabel hvfax = new JLabel("Fax: +49-711-2172 0058-9");
		gbLayout1.setConstraints(hvfax, new GridBagConstraints(2, 6, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 20, 0), 0, 0));
		panelAbout.add(hvfax);

		JLabel copyright = new JLabel(
				"(c) Copyright HELIUM V IT-Solutions GmbH" + " 2005 - "
						+ Calendar.getInstance().get(Calendar.YEAR));

		copyright.setHorizontalAlignment(SwingConstants.CENTER);

		gbLayout1.setConstraints(copyright, new GridBagConstraints(1, 7, 1, 1,
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panelAbout.add(copyright);

		buttonLogfileAnzeigen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.aboutdialog.clientlog"));
		buttonLogfileAnzeigen.addActionListener(this);

		/*
		 * {
		 * LPMain.getInstance().getTextRespectUISPr("lp.installiertesprachen"),
		 * "", "", ""},
		 * 
		 * { LPMain.getInstance().getTextRespectUISPr("lp.installiertemodule"),
		 * "",
		 * LPMain.getInstance().getTextRespectUISPr("part.ist_hautpmandant"),
		 * DelegateFactory
		 * .getInstance().getSystemDelegate().anwenderFindByPrimaryKey(new
		 * Integer(SystemFac.
		 * PK_HAUPTMANDANT_IN_LP_ANWENDER)).getMandantCNrHauptmandant()},
		 */
		panelLizeninfo.setMaximumSize(new Dimension(710, 150));
		panelLizeninfo.setMinimumSize(new Dimension(710, 150));
		panelLizeninfo.setPreferredSize(new Dimension(710, 150));

		WrapperKeyValueField wkvLizenzierfuer = new WrapperKeyValueField(150);
		wkvLizenzierfuer.getWlaValue().setHorizontalAlignment(
				SwingConstants.LEFT);
		wkvLizenzierfuer.setKey(LPMain.getInstance().getTextRespectUISPr(
				"lp.lizenziertfuer")
				+ " ");
		wkvLizenzierfuer.setValue(mandantDto_Hauptmandant.getPartnerDto()
				.formatAnrede()
				+ ", "
				+ mandantDto_Hauptmandant.getPartnerDto().formatAdresse());

		panelLizeninfo.add(wkvLizenzierfuer, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 10), 0, 0));

		// ////////////////////////
		WrapperKeyValueField wkvSprachen = new WrapperKeyValueField(150);
		wkvSprachen.getWlaValue().setHorizontalAlignment(SwingConstants.LEFT);

		wkvSprachen.setKey(LPMain.getInstance().getTextRespectUISPr(
				"lp.installiertesprachen")
				+ " ");

		String locales = "";
		Map map = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllLocales(null);

		Iterator<?> it = map.keySet().iterator();

		while (it.hasNext()) {
			locales += ((String) it.next()).trim();
			if (it.hasNext()) {
				locales += ", ";
			}
		}

		wkvSprachen.setValue(locales);

		panelLizeninfo.add(wkvSprachen, new GridBagConstraints(0, 1, 1, 1, 1,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 10), 0, 0));

		// //////////

		WrapperKeyValueField wkvMandanten = new WrapperKeyValueField(150);
		wkvMandanten.getWlaValue().setHorizontalAlignment(SwingConstants.LEFT);
		wkvMandanten.setKey(LPMain.getInstance().getTextRespectUISPr(
				"lp.about.installiertemandanten")
				+ " ");
		String mandanten = "";

		MandantDto[] mDtos = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindAll();
		for (int i = 0; i < mDtos.length; i++) {
			mandanten += mDtos[i].getCNr() + " " + mDtos[i].getCKbez()
					+ ", Benutzer: " + mDtos[i].getIBenutzermax() + " | ";
		}

		wkvMandanten.setValue(mandanten);

		panelLizeninfo.add(wkvMandanten, new GridBagConstraints(0, 2, 1, 1, 1,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 10), 0, 0));

		// //////////

		WrapperKeyValueField wkvHauptmandant = new WrapperKeyValueField(150);
		wkvHauptmandant.getWlaValue().setHorizontalAlignment(
				SwingConstants.LEFT);
		wkvHauptmandant.setKey(LPMain.getInstance().getTextRespectUISPr(
				"part.ist_hautpmandant")
				+ " ");
		wkvHauptmandant.setValue(mandantDto_Hauptmandant.getCNr());

		panelLizeninfo.add(wkvHauptmandant, new GridBagConstraints(0, 3, 1, 1,
				1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 250), 0, 0));

		panelLizeninfo.add(buttonLogfileAnzeigen, new GridBagConstraints(0, 3,
				1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 5, 10), 100, 0));

		JScrollPane scrollPane = new JScrollPane(jTableVersion);
		scrollPane.setMaximumSize(new Dimension(700, 200));
		scrollPane.setMinimumSize(new Dimension(700, 200));
		scrollPane.setPreferredSize(new Dimension(700, 200));
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jTableVersion.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableVersion.setTableHeader(null);

		jPanelTable.add(scrollPane /* jTableVersion */);

		this.getContentPane().add(panelAbout, java.awt.BorderLayout.NORTH);
		this.getContentPane().add(jPanelTable, java.awt.BorderLayout.CENTER);
		this.getContentPane().add(panelLizeninfo, java.awt.BorderLayout.SOUTH);
	}
}
