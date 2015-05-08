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
 *******************************************************************************/
package com.lp.client.system;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.frameposition.LocalSettingsPathGenerator;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;

public class PanelPflegeIntern extends PanelBasis {

	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();

	private final String MENUE_PFLEGE_SP2486 = "MENUE_PFLEGE_SP2486";
	private final String MENUE_PFLEGE_SP2597 = "MENUE_PFLEGE_SP2597";
	private final String MENUE_PFLEGE_PJ18612 = "MENUE_PFLEGE_PJ18612";

	public PanelPflegeIntern(InternalFrame internalFrame, String add2TitleI)
			throws Throwable {
		super(internalFrame, add2TitleI, null);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);
		// jetzt meine felder
		jpaWorkingOn = new JPanel(new MigLayout("wrap 3",
				"[fill,60%|fill,20%|fill,20%]",
				"[fill,33%][fill,33%][fill,33%]"));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 1, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		JPanel artikel = new JPanel(new MigLayout("wrap 2",
				"[fill,50%|fill,50%]", "[fill]"));
		artikel.setBorder(BorderFactory.createTitledBorder("Artikel"));
		jpaWorkingOn.add(artikel, "span 1 3");

		JPanel allgemein = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		allgemein.setBorder(BorderFactory.createTitledBorder("Allgemein"));
		jpaWorkingOn.add(allgemein);
		JPanel best = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		best.setBorder(BorderFactory.createTitledBorder("Bestellung"));
		jpaWorkingOn.add(best);

		JPanel rechnung = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		rechnung.setBorder(BorderFactory.createTitledBorder("Rechnung"));
		jpaWorkingOn.add(rechnung);
		JPanel eingangsrechnung = new JPanel(new MigLayout("wrap 1",
				"[fill,100%]"));
		eingangsrechnung.setBorder(BorderFactory
				.createTitledBorder("Eingangsrechnung"));
		jpaWorkingOn.add(eingangsrechnung);

		JPanel fertigung = new JPanel(new MigLayout("wrap 1", "[fill,100%]"));
		fertigung.setBorder(BorderFactory.createTitledBorder("Fertigung"));
		jpaWorkingOn.add(fertigung);

		JPanel hinweis = new JPanel(new MigLayout("wrap 1"));
		hinweis.setBorder(BorderFactory.createTitledBorder("Hinweis"));
		jpaWorkingOn.add(hinweis);

		JButton btnSP2486 = new AutoWrapButton("SP2486 Artikel abbuchen");
		btnSP2486.setActionCommand(MENUE_PFLEGE_SP2486);
		btnSP2486.addActionListener(this);
		artikel.add(btnSP2486);
		
		JButton btnSP2597 = new AutoWrapButton("SP2597 Sperren");
		btnSP2597.setActionCommand(MENUE_PFLEGE_SP2597);
		btnSP2597.addActionListener(this);
		artikel.add(btnSP2597);
		
		JButton btnPJ18612 = new AutoWrapButton("PJ18612 Lieferant von Mandant 001 -> 002");
		btnPJ18612.setActionCommand(MENUE_PFLEGE_PJ18612);
		btnPJ18612.addActionListener(this);
		artikel.add(btnPJ18612);
		

		// Hinweis

		JLabel achtung = new JLabel(
				"<html><font size=\"6\" color=\"#FF0000\">!!!!!!!!!!!!!!!!!</font></html>");

		JLabel lblHinweis1 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis"));
		lblHinweis1.setForeground(Color.RED);

		JLabel lblHinweis2 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis1"));
		lblHinweis2.setForeground(Color.RED);
		JLabel lblHinweis3 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis2"));
		lblHinweis3.setForeground(Color.RED);
		JLabel lblHinweis4 = new JLabel(
				LPMain.getTextRespectUISPr("lp.pflege.hinweis3"));
		lblHinweis4.setForeground(Color.RED);

		hinweis.add(achtung);
		hinweis.add(lblHinweis1);
		hinweis.add(lblHinweis2);
		hinweis.add(lblHinweis3);
		hinweis.add(lblHinweis4);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MATERIAL;
	}

	protected void components2Dto() {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_PFLEGE_SP2486)) {

			File[] files = HelperClient.chooseFile(this,
					HelperClient.FILE_FILTER_XLS, false);
			if (files == null || files.length < 1 || files[0] == null) {
				return;
			}
			File f = files[0];

			Set<Integer> artikelids = new HashSet<Integer>();

			if (f != null) {

				Workbook workbook = Workbook.getWorkbook(f);
				Sheet sheet = workbook.getSheet(0);

				for (int i = 1; i < sheet.getRows(); i++) {
					Cell[] s = sheet.getRow(i);

					if (s[1].getType() == CellType.NUMBER) {
					
					NumberCell n = (jxl.NumberCell) s[1];

					if (n.getValue() == 0) {

						ArtikelDto aDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.artikelFindByCNr(s[0].getContents());

						artikelids.add(aDto.getIId());
					}
					}

				}

			}

			String s=DelegateFactory.getInstance().getPflegeDelegate()
					.sp2486(artikelids);
			
			java.io.File ausgabedatei = getLogFile("sp2486.txt");
			java.io.FileWriter fw = new java.io.FileWriter(ausgabedatei);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
			bw.write(s);
			bw.close();
			

		}else if (e.getActionCommand().equals(MENUE_PFLEGE_SP2597)) {
			DelegateFactory.getInstance().getPflegeDelegate()
			.sp2597();
		}else if (e.getActionCommand().equals(MENUE_PFLEGE_PJ18612)) {
			DelegateFactory.getInstance().getPflegeDelegate()
			.pj18612();
		}
	}

	private File getLogFile(String baseFilename) {
		return new File(new LocalSettingsPathGenerator().getLogPath(), baseFilename);
	}
	
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

	}

	private class AutoWrapButton extends JButton {
		private static final long serialVersionUID = -1988326226807369144L;

		public AutoWrapButton() {
			Insets i = getMargin();
			setMargin(new Insets(i.top, 2, i.bottom, 2));
		}

		public AutoWrapButton(String text) {
			this();
			setText(text);
		}

		@Override
		public void setText(String text) {
			text = text.replaceAll("<\\s*/?(html|p|br)\\s*>", ""); // alle html
																	// und
																	// paragraphen
																	// tags
																	// entfernen

			super.setText("<html><center><p>" + text + "</p></center></html>");
		}
	}

}
