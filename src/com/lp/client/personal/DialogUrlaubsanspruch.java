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
package com.lp.client.personal;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.personal.service.UrlaubsabrechnungDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class DialogUrlaubsanspruch extends JDialog implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	WrapperLabel wrapperLabel1 = new WrapperLabel();
	WrapperLabel wrapperLabel3 = new WrapperLabel();
	WrapperLabel wrapperLabel4 = new WrapperLabel();
	WrapperLabel wrapperLabel5 = new WrapperLabel();
	WrapperLabel wrapperLabel6 = new WrapperLabel();
	WrapperLabel wrapperLabel7 = new WrapperLabel();
	WrapperLabel wlaAnspruch = new WrapperLabel();
	WrapperNumberField wnfAnspruchAltTage = null;
	WrapperNumberField wnfAnspruchAktuellTage = null;
	WrapperNumberField wnfAnspruchAktuellAliquotTage = null;
	WrapperNumberField wnfAnspruchAktuellVerbrauchtTage = null;
	WrapperNumberField wnfUrlaubGeplantTage = null;
	WrapperNumberField wnfUrlaubVerfuegbarTage = null;
	WrapperLabel wrapperLabel8 = new WrapperLabel();
	WrapperLabel wrapperLabel9 = new WrapperLabel();
	WrapperLabel wrapperLabel11 = new WrapperLabel();
	WrapperLabel wrapperLabel12 = new WrapperLabel();
	WrapperNumberField wnfAnspruchAltStunden = null;
	WrapperNumberField wnfAnspruchAktuellStunden = null;
	WrapperNumberField wnfAnspruchAktuellAliquotStunden = null;
	WrapperNumberField wnfAnspruchAktuellVerbrauchtStunden = null;
	WrapperNumberField wnfUrlaubGeplantStunden = null;
	WrapperNumberField wnfUrlaubVerfuegbarStunden = null;
	WrapperLabel wlaTitel = new WrapperLabel();

	private WrapperNumberField wnfAnspruchAktuellVerbrauchtZusTage = null;
	private WrapperNumberField wnfAnspruchAktuellVerbrauchtZusStunden = null;
	private WrapperNumberField wnfFreierUrlaubTage = null;
	private WrapperNumberField wnfFreierUrlaubStunden = null;

	private WrapperLabel wlaAnspruchAktuellVerbrauchtZus = new WrapperLabel();
	private WrapperLabel wlaFreierUrlaub = new WrapperLabel();

	GridBagLayout gridBagLayout2 = new GridBagLayout();

	public DialogUrlaubsanspruch() {

	}

	public DialogUrlaubsanspruch(UrlaubsabrechnungDto urlaubsabrechnungDto)
			throws Exception {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("pers.urlaubsanspruch.urlaubsabrechnung"),
				true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		pack();

		Calendar c = Calendar.getInstance();
		c.setTime(urlaubsabrechnungDto.getDAbrechnungszeitpunkt());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);

		wlaTitel.setText(wlaTitel.getText()
				+ " "
				+ com.lp.util.Helper.formatDatum(c.getTime(), LPMain
						.getInstance().getUISprLocale()) + " inkl.:");

		wnfAnspruchAktuellStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubsanspruchStunden());
		wnfAnspruchAktuellTage.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubsanspruchTage());
		wnfAnspruchAktuellVerbrauchtStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubVerbrauchtStunden());
		wnfAnspruchAktuellVerbrauchtTage.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubVerbrauchtTage());
		wnfAnspruchAltStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchStunden());
		wnfAnspruchAltTage.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchTage());
		wnfUrlaubGeplantStunden.setBigDecimal(urlaubsabrechnungDto
				.getNGeplanterUrlaubStunden());
		wnfUrlaubGeplantTage.setBigDecimal(urlaubsabrechnungDto
				.getNGeplanterUrlaubTage());
		wnfUrlaubVerfuegbarStunden.setBigDecimal(urlaubsabrechnungDto
				.getNVerfuegbarerUrlaubStunden());
		wnfUrlaubVerfuegbarTage.setBigDecimal(urlaubsabrechnungDto
				.getNVerfuegbarerUrlaubTage());
		wnfAnspruchAktuellAliquotTage.setBigDecimal(urlaubsabrechnungDto
				.getNAliquoterAnspruchTage());
		wnfAnspruchAktuellAliquotStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAliquoterAnspruchStunden());

		wnfAnspruchAktuellVerbrauchtZusStunden
				.setBigDecimal(urlaubsabrechnungDto
						.getNAktuellerUrlaubVerbrauchtStunden());
		wnfAnspruchAktuellVerbrauchtZusTage.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubVerbrauchtTage());

		wnfFreierUrlaubTage.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchTage()
				.add(urlaubsabrechnungDto.getNAktuellerUrlaubsanspruchTage())
				.subtract(urlaubsabrechnungDto.getNGeplanterUrlaubTage())
				.subtract(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubVerbrauchtTage()));

		wnfFreierUrlaubStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchStunden().add(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubsanspruchStunden())
				.subtract(urlaubsabrechnungDto.getNGeplanterUrlaubStunden())
				.subtract(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubVerbrauchtStunden()));

	}

	private void jbInit() throws Exception {

		wnfAnspruchAltTage = new WrapperNumberField();
		wnfAnspruchAktuellTage = new WrapperNumberField();
		wnfAnspruchAktuellAliquotTage = new WrapperNumberField();
		wnfAnspruchAktuellVerbrauchtTage = new WrapperNumberField();
		wnfUrlaubGeplantTage = new WrapperNumberField();
		wnfUrlaubVerfuegbarTage = new WrapperNumberField();
		wnfAnspruchAltStunden = new WrapperNumberField();
		wnfAnspruchAktuellStunden = new WrapperNumberField();
		wnfAnspruchAktuellAliquotStunden = new WrapperNumberField();
		wnfAnspruchAktuellVerbrauchtStunden = new WrapperNumberField();
		wnfUrlaubGeplantStunden = new WrapperNumberField();
		wnfUrlaubVerfuegbarStunden = new WrapperNumberField();

		wnfAnspruchAktuellVerbrauchtZusTage = new WrapperNumberField();
		wnfAnspruchAktuellVerbrauchtZusStunden = new WrapperNumberField();
		wnfFreierUrlaubTage = new WrapperNumberField();
		wnfFreierUrlaubStunden = new WrapperNumberField();

		panelUrlaubsanspruch.setLayout(gridBagLayout1);
		wrapperLabel1.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.anspruchalt")
				+ ":");
		wrapperLabel3.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.anspruchaktuell")
				+ ":");
		wrapperLabel4
				.setText("- "
						+ LPMain
								.getInstance()
								.getTextRespectUISPr(
										"pers.urlaubsanspruch.anspruchaktuellverbraucht")
						+ ":");
		wrapperLabel5.setText("- "
				+ LPMain.getInstance().getTextRespectUISPr(
						"pers.urlaubsanspruch.urlaubgeplant") + ":");
		wrapperLabel6.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.urlaubverfuegbar")
				+ ":");
		wrapperLabel7.setText("+ "
				+ LPMain.getInstance().getTextRespectUISPr(
						"pers.urlaubsanspruch.anspruchaliquot") + ":");
		wlaAnspruch.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.betrachtungsart")
				+ ":");
		wrapperLabel8
				.setText("----------------------------------------------------------------------------------------------");
		wrapperLabel9
				.setText("----------------------------------------------------------------------------------------------");
		wrapperLabel11.setHorizontalAlignment(SwingConstants.CENTER);
		wrapperLabel11.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.tage"));
		wrapperLabel12.setHorizontalAlignment(SwingConstants.CENTER);
		wrapperLabel12.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.stunden"));
		wlaTitel.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
		wlaTitel.setHorizontalAlignment(SwingConstants.CENTER);
		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.anspruchzum"));

		wlaAnspruchAktuellVerbrauchtZus
				.setText("- "
						+ LPMain
								.getInstance()
								.getTextRespectUISPr(
										"pers.urlaubsanspruch.anspruchaktuellverbraucht")
						+ ":");

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 31);
		c.set(Calendar.MONTH, Calendar.DECEMBER);

		wlaFreierUrlaub.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.freierurlaub")
				+ " "
				+ com.lp.util.Helper.formatDatum(c.getTime(), LPMain
						.getInstance().getUISprLocale()) + ":");

		wnfAnspruchAltStunden.setEditable(false);
		wnfAnspruchAktuellStunden.setEditable(false);
		wnfAnspruchAktuellVerbrauchtStunden.setEditable(false);
		wnfAnspruchAktuellAliquotStunden.setEditable(false);
		wnfUrlaubGeplantStunden.setEditable(false);
		wnfUrlaubVerfuegbarStunden.setEditable(false);

		wnfAnspruchAltTage.setEditable(false);
		wnfAnspruchAktuellTage.setEditable(false);
		wnfAnspruchAktuellAliquotTage.setEditable(false);
		wnfAnspruchAktuellVerbrauchtTage.setEditable(false);
		wnfUrlaubGeplantTage.setEditable(false);
		wnfUrlaubVerfuegbarTage.setEditable(false);

		wnfAnspruchAktuellVerbrauchtZusTage.setEditable(false);
		wnfAnspruchAktuellVerbrauchtZusStunden.setEditable(false);
		wnfFreierUrlaubTage.setEditable(false);
		wnfFreierUrlaubStunden.setEditable(false);

		this.getContentPane().setLayout(gridBagLayout2);
		this.getContentPane().add(
				panelUrlaubsanspruch,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 200, 150));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellVerbrauchtTage,
				new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfUrlaubVerfuegbarTage,
				new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAltStunden, new GridBagConstraints(
				2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellVerbrauchtStunden,
				new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfUrlaubVerfuegbarStunden,
				new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel6, new GridBagConstraints(0, 8, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel9, new GridBagConstraints(0, 7, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel4, new GridBagConstraints(0, 6, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel11, new GridBagConstraints(1, 1,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel12, new GridBagConstraints(2, 1,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAltTage, new GridBagConstraints(1,
				2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaAnspruch, new GridBagConstraints(0, 1, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaTitel, new GridBagConstraints(0, 0, 3, 1,
				0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel1, new GridBagConstraints(0, 2, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellAliquotTage,
				new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel7, new GridBagConstraints(0, 5, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellAliquotStunden,
				new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel5, new GridBagConstraints(0, 11,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wrapperLabel3, new GridBagConstraints(0, 10,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellTage,
				new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellStunden,
				new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfUrlaubGeplantTage, new GridBagConstraints(
				1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfUrlaubGeplantStunden,
				new GridBagConstraints(2, 11, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wlaAnspruchAktuellVerbrauchtZus,
				new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		panelUrlaubsanspruch.add(wnfAnspruchAktuellVerbrauchtZusTage,
				new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellVerbrauchtZusStunden,
				new GridBagConstraints(2, 12, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel8, new GridBagConstraints(0, 13,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(new WrapperLabel(), new GridBagConstraints(0,
				9, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaFreierUrlaub, new GridBagConstraints(0, 14,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wnfFreierUrlaubTage, new GridBagConstraints(1,
				14, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfFreierUrlaubStunden,
				new GridBagConstraints(2, 14, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));

		Component[] comps = panelUrlaubsanspruch.getComponents();

		for (int i = 0; i < comps.length; i++) {
			comps[i].addKeyListener(this);
		}

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		}

	}

	public void keyReleased(KeyEvent e) {
	}

}
