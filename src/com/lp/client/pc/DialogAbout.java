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
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.frameposition.LocalSettingsPathGenerator;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.util.ClientConfiguration;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.JavaInfoController;
import com.lp.server.system.service.JavaInfoDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ServerLocaleInfo;
import com.lp.server.system.service.SystemFac;

public class DialogAbout extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JPanel panelLizeninfo = new JPanel();
	JLabel lblAdresse = new JLabel();
	JLabel lblIconLogistikpur = new JLabel();
	JLabel lblScch = new JLabel();
	JLabel lblErp = new JLabel();
	JLabel lblLinie = new JLabel();
	JLabel lblSprachen = new JLabel();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel lblIpadresse = new JLabel();
	JButton buttonLogfileAnzeigen = new JButton();
	DauerPingWrapperTable jTableVersion = null;
	JPanel jPanelTable = new JPanel();
	
	Vector<Vector> vectorData = new Vector<Vector>();
	
	DecimalFormat nf = new DecimalFormat();
	

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

				File logFile = new File(new LocalSettingsPathGenerator().getLogPath(), "lpclient_");
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
				DialogFactory.showModalDialog("Fehler",
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

		ServerLocaleInfo serverLocaleInfo = infoController.getServerLocaleInfo() ;
		ServerLocaleInfo clientLocaleInfo = infoController.getClientLocaleInfo() ;
		String serverWebPort = DelegateFactory.getInstance().getSystemDelegate().getServerWebPort() ;
		
		JavaInfoController javaInfoController = new JavaInfoController();
		JavaInfoDto clientJavaInfo = javaInfoController.getJavaInfo();
		JavaInfoDto serverJavaInfo = DelegateFactory.getInstance().getSystemDelegate().getServerJavaInfo();
		
		
		//*** wp ***
		//***
		
		// Tabelleninhalt
		Vector<String> vectorColumns = new Vector<String>();
		vectorColumns.add("1");
		vectorColumns.add("2");
		vectorColumns.add("3");
		vectorColumns.add("4");
		
		// HELIUM V Benutzer
		Vector<String> row0 = new Vector<String>();
		if (isUserAvailable()) {
			String[] s = LPMain.getTheClient().getBenutzername()
					.trim().split("\\|");

			row0.add(Texts.txtHvUser());
			row0.add(s[0] + (LPMain.getInstance().isLPAdmin() ? "(true)" : ""));
			row0.add(Texts.txtLoggedOnServer());
			row0.add(server + " " + Texts.msgHttp(serverWebPort));
			
			vectorData.addElement(row0);
		}
		
		// System Benutzer
		Vector<String> row10 = new Vector<String>();
		row10.add(Texts.txtSysUser());
		row10.add(System.getProperty("user.name"));
		row10.add(Texts.txtComputerName());
		row10.add(InetAddress.getLocalHost().getHostName());
		vectorData.addElement(row10);

		// Leerzeile
		Vector<String> row11 = new Vector<String>();
		vectorData.add(row11);
		
		// HELIUM V Client Version
		Vector<String> row20 = new Vector<String>();
//		row20.add(LPMain.getInstance().getTextRespectUISPr("lp.server"));
		row20.add("HELIUM V Client Version");
		row20.add(anwenderDto.getCVersionServer() + "."
				+ anwenderDto.getIBuildnummerServer());
		row20.add("Von: " + ClientConfiguration.getVersion() + "."
				+ anwenderDto.getIBuildnummerClienVon());
		row20.add("Bis: " + ClientConfiguration.getVersion() + "."
				+ anwenderDto.getIBuildnummerClientBis());
		vectorData.addElement(row20);
		
		// Datenbank Version
		Vector<String> row30 = new Vector<String>();
//		row30.add(LPMain.getInstance().getTextRespectUISPr("lp.datenbank"));
		row30.add("HELIUM V Server Version");
		row30.add(anwenderDto.getCVersionDB() + "."
				+ anwenderDto.getIBuildnummerDB());
		row30.add("Von: " + anwenderDto.getCVersionServer() + "."
				+ anwenderDto.getIBuildnummerServerVon());
		row30.add("Bis: " + anwenderDto.getCVersionServer() + "."
				+ anwenderDto.getIBuildnummerServerBis());
		vectorData.addElement(row30);
		

		// Leerzeile
		Vector<String> row31 = new Vector<String>();
		vectorData.add(row31);
		
		// Client - Java Info
		Vector<String> row40 = new Vector<String>();
		row40.add(Texts.txtClientJavaInfo());
		vectorData.addElement(row40);
		
		// JVM Arbeitsspeicher
		Vector<String> row41 = buildMemoryInfo(clientJavaInfo);
		vectorData.addElement(row41);
		
		Vector<String> row50 = buildJreInfo(clientJavaInfo);
		vectorData.addElement(row50);
		
		// JRE Pfade
		Vector<String> row51 = buildJrePfadInfo(clientJavaInfo);
		vectorData.addElement(row51);
		
		// Betriebssystem
		Vector<String> row60 = buildOsInfo(clientJavaInfo);
		vectorData.addElement(row60);
				
		// Locale
		Vector<String> row70 = buildLocaleInfo(clientLocaleInfo);
		vectorData.addElement(row70);

		// Leerzeile
		Vector<String> row71 = new Vector<String>();
		vectorData.add(row71);
		
		// Server - Java Info
		Vector<String> row80 = new Vector<String>();
		row80.add(Texts.txtServerJavaInfo());
		vectorData.addElement(row80);
		
		Vector<String> row100 = buildMemoryInfo(serverJavaInfo);
		vectorData.addElement(row100);
		
		Vector<String> row200 = buildJreInfo(serverJavaInfo);
		vectorData.addElement(row200);
		
		Vector<String> row300 = buildJrePfadInfo(serverJavaInfo);
		vectorData.addElement(row300);
		
		Vector<String> row400 = buildOsInfo(serverJavaInfo);
		vectorData.addElement(row400);
				
		Vector<String> row500 = buildLocaleInfo(serverLocaleInfo);
		vectorData.addElement(row500);
		
		// Leerzeile
		Vector<String> row501 = new Vector<String>();
		vectorData.add(row501);
		
		// Ping
		// leere zeile die vom Ping ueberschrieben wird  
		Vector<String> rowPing = new Vector<String>();
		vectorData.addElement(rowPing);
		
		
		//***
		//*** wp ***
		
		
		// Erweitere WrapperTable und fuege DauerPing hinzu
		jTableVersion = new DauerPingWrapperTable(vectorData, vectorColumns, getVectorDataSize());
		
		
		// Uebergebe WrapperTable an DauerPing und berechne Pinginformation im Hintergrund
		final DauerPing ping = new DauerPing(jTableVersion) ;
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				ping.stop();
			}
		});
		
		ping.run() ;
		
		//*** wp ***
		//***
		
		// Create Layout
		// PanelAbout
		JPanel panelAbout = new JPanel();
		panelAbout.setLayout( new BoxLayout(panelAbout, BoxLayout.LINE_AXIS));
		panelAbout.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		
		String strLabelLeft = LPMain.getTextRespectUISPr("lp.vertrieb.at");
		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new BorderLayout(1,1));
		JLabel labelLeft = new JLabel(strLabelLeft);
		panelLeft.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
		panelLeft.add(labelLeft, BorderLayout.WEST);
		panelAbout.add(panelLeft);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter,BoxLayout.PAGE_AXIS));
		
		JLabel labelCenterImage = new JLabel();		
		labelCenterImage.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/heliumv_info.png")));
		labelCenterImage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		labelCenterImage.setAlignmentX(CENTER_ALIGNMENT);
		panelCenter.add(labelCenterImage);
	
		final JEditorPane labelCenterText = new JEditorPane();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

            	Font currentFont = UIManager.getDefaults().getFont("Label.font");
            	
            	String fontStyle = "normal";
            	if (currentFont.getStyle() == 1) {
            		fontStyle = "bold";
            	}
            	
                labelCenterText.setContentType("text/html");
                String str = 
                		"<div style=\"text-align: center; font-family: \'"
                		+ currentFont.getFamily() + "\', sans-serif; "
                		+ "font-size: " + currentFont.getSize() + "pt;"
                		+ "font-weight: " + fontStyle + ";\">"
                		+ "<a style=\"text-decoration: none;\" href=\"mailto:support@HeliumV.com\">support@HeliumV.com</a><br>"
                		+ "<a style=\"text-decoration: none;\" href=\"http://www.HeliumV.com/\">http://www.HeliumV.com</a><br/>"
                		+ "&#169; HELIUM V IT-Solutions GmbH 2005 - "
                		+ Calendar.getInstance().get(Calendar.YEAR)
                		+ "</div>";
                labelCenterText.setText(str);

                labelCenterText.setEditable(false);//so its not editable
                labelCenterText.setOpaque(false);//so we dont see whit background

                labelCenterText.addHyperlinkListener(new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent hle) {
                        if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                            System.out.println(hle.getURL());
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(hle.getURL().toURI());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        
        labelCenterText.setAlignmentX(CENTER_ALIGNMENT);
        panelCenter.setAlignmentX(CENTER_ALIGNMENT);
        panelCenter.add(labelCenterText);
		
		panelAbout.add(panelCenter);
		
		String strLabelRight = LPMain.getTextRespectUISPr("lp.vertrieb.de");
		JPanel panelRight = new JPanel();
		panelRight.setLayout(new BorderLayout(1,1));
		JLabel labelRight = new JLabel(strLabelRight);
		panelRight.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
		panelRight.add(labelRight, BorderLayout.EAST);
		panelAbout.add(panelRight);
		
		//***
		//*** wp ***
		

		// PanelTable
		jTableVersion.setTableHeader(null);
//		jTableVersion.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jTableVersion.setGridColor(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(jTableVersion);
//		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
//		scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
			
		
		// PanelLizenzInfo
		panelLizeninfo.setLayout(new GridBagLayout());
		WrapperKeyValueField wkvLizenzierfuer = new WrapperKeyValueField(150);
		wkvLizenzierfuer.getWlaValue().setHorizontalAlignment(
				SwingConstants.LEFT);
		wkvLizenzierfuer.setKey(LPMain.getTextRespectUISPr("lp.lizenziertfuer")
				+ " ");
		wkvLizenzierfuer.setValue(mandantDto_Hauptmandant.getPartnerDto()
				.formatAnrede()
				+ ", "
				+ mandantDto_Hauptmandant.getPartnerDto().formatAdresse());

		panelLizeninfo.add(wkvLizenzierfuer, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 10), 0, 0));

		WrapperKeyValueField wkvSprachen = new WrapperKeyValueField(150);
		wkvSprachen.getWlaValue().setHorizontalAlignment(SwingConstants.LEFT);

		wkvSprachen.setKey(LPMain.getTextRespectUISPr("lp.installiertesprachen")
				+ " ");

		String locales = "";
		Map<?, ?> map = DelegateFactory.getInstance().getLocaleDelegate()
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


		WrapperKeyValueField wkvMandanten = new WrapperKeyValueField(150);
		wkvMandanten.getWlaValue().setHorizontalAlignment(SwingConstants.LEFT);
		wkvMandanten.setKey(LPMain.getTextRespectUISPr("lp.about.installiertemandanten")
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


		WrapperKeyValueField wkvHauptmandant = new WrapperKeyValueField(150);
		wkvHauptmandant.getWlaValue().setHorizontalAlignment(
				SwingConstants.LEFT);
		wkvHauptmandant.setKey(LPMain.getTextRespectUISPr("part.ist_hautpmandant")
				+ " ");
		wkvHauptmandant.setValue(mandantDto_Hauptmandant.getCNr());

		panelLizeninfo.add(wkvHauptmandant, new GridBagConstraints(0, 3, 1, 1,
				1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 10), 0, 0));

		buttonLogfileAnzeigen.setText(LPMain.getTextRespectUISPr("lp.aboutdialog.clientlog"));
		buttonLogfileAnzeigen.addActionListener(this);
		
		panelLizeninfo.add(buttonLogfileAnzeigen, new GridBagConstraints(0, 4,
				1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 5, 10), 100, 0));

		
		// Add Layout
		Dimension d = LPMain.getInstance().getDesktop().getSize();
		int width = (int) (d.width * 0.7);
		int height = (int) (d.height * 0.7);
		this.getContentPane().setPreferredSize(new Dimension(width, height));
			
		this.getContentPane().add(panelAbout, BorderLayout.NORTH);

//		this.getContentPane().add(jPanelTable, java.awt.BorderLayout.CENTER);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		this.getContentPane().add(panelLizeninfo, BorderLayout.SOUTH);

	}

	private Vector<String> buildMemoryInfo(JavaInfoDto javaInfo) {
		Vector<String> row = new Vector<String>();
		row.add("JVM Arbeitsspeicher");
		row.add(Texts.msgFreeMemory(javaInfo.getFreeMemory()));
		row.add(Texts.msgMaxMemory(javaInfo.getMaxMemory()));
		row.add(Texts.msgTotMemory(javaInfo.getTotMemory()));
		return row;
	}

	private Vector<String> buildLocaleInfo(ServerLocaleInfo localeInfo) {
		Vector<String> row = new Vector<String>();
		row.add("Locale");
		row.add(Texts.msgCountry(localeInfo.getCountry()));
		row.add(Texts.msgLanguage(localeInfo.getLanguage()));
		row.add(localeInfo.getTimezone());
		return row;
	}

	private Vector<String> buildOsInfo(JavaInfoDto javaInfo) {
		Vector<String> row = new Vector<String>();
		row.add(Texts.txtOs());
		row.add(javaInfo.getOsName());
		row.add(javaInfo.getOsArchitecture());
		row.add(javaInfo.getOsVersion());
		return row;
	}

	private Vector<String> buildJrePfadInfo(JavaInfoDto javaInfo) {
		Vector<String> row = new Vector<String>();
		row.add("JRE Pfade (Class | Path)");
		row.add(javaInfo.getJavaClathPath());
		row.add(javaInfo.getJavaLibraryPath());
		row.add("");
		return row;
	}

	private Vector<String> buildJreInfo(JavaInfoDto javaInfo) {
		// JRE Versionen
		Vector<String> row = new Vector<String>();
		row.add("JRE-Version");
		row.add(javaInfo.getJavaVersion() + ", " + javaInfo.getJavaVendor());
		row.add(javaInfo.getJavaVmName() + ", " + javaInfo.getJavaVmVendor());
		
		int clientDesktopSupported = 0;
		int clientMailSupported = 0;
		int clientBrowserSupported = 0;
		int clientPrinterSupported = 0;
		int clientOpenFileSupported = 0;
		if (!javaInfo.isHeadless()) {
			
			if (javaInfo.getDesktopSupported()) {

				clientDesktopSupported = 1;

				if (javaInfo.isMailSupported())
					clientMailSupported = 1;
				if (javaInfo.isBrowserSupported())
					clientBrowserSupported = 1;
				if (javaInfo.isPrinterSupported())
					clientPrinterSupported = 1;
				if (javaInfo.isOpenFileSupported())
					clientOpenFileSupported = 1;

			}
		}
	
		row.add("D: " + clientDesktopSupported + " | M: " + clientMailSupported +  " | B: " + clientBrowserSupported
				+ " | P: " + clientPrinterSupported + " | O: " + clientOpenFileSupported);
		return row;
	}
	
	private boolean isUserAvailable() throws Throwable {
		return LPMain.getTheClient() != null ;
	}
	
	public int getVectorDataSize(){
		int vectorDataSize = vectorData.size();
		return vectorDataSize;
	}

}
