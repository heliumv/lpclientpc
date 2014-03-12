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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.instandhaltung.service.GeraetDto;
import com.lp.server.instandhaltung.service.GeraetehistorieDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogWartungenerfassen extends JDialog implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();

	private PanelQuery panelQueryHistorie = null;
	
	private WrapperSelectField wsfPersonal_Techniker = null;

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperLabel wlaGeraet = new WrapperLabel();
	private WrapperNumberField wnfGeraet = new WrapperNumberField();

	private WrapperCheckBox wcbNichtMoeglich = new WrapperCheckBox();

	JButton wbuFertig = new JButton();

	TabbedPane tabbedpane = null;

	public DialogWartungenerfassen(TabbedPane tabbedpane) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("is.wartungenerfassen"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.tabbedpane = tabbedpane;
		jbInit();
		pack();
		wdfDatum.requestFocus();
	}

	public void dispose() {
		super.dispose();
	}

	private void jbInit() throws Throwable {
		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		wsfPersonal_Techniker = new WrapperSelectField(
				WrapperSelectField.PERSONAL, tabbedpane.getInternalFrame(),
				false);

		wlaDatum.setText(LPMain.getTextRespectUISPr("is.wartungenerfassen.am"));
		wcbNichtMoeglich.setText(LPMain
				.getTextRespectUISPr("is.historie.nichtmoeglich"));
		wdfDatum.setMandatoryField(true);

		wlaGeraet.setText(LPMain
				.getTextRespectUISPr("is.wartungenerfassen.geraetenummer"));
		wnfGeraet.setFractionDigits(0);
		wnfGeraet.addKeyListener(this);
		wsfPersonal_Techniker.setMandatoryField(true);

		wbuFertig.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.beenden"));
		wbuFertig
				.addActionListener(new DialogWartungenerfassen_wbuFertig_actionAdapter(
						this));
		
		panelQueryHistorie = new PanelQuery(null,
				null,
				QueryParameters.UC_ID_GERAETEHISTORIE_MIT_GERAET, null,
				tabbedpane.getInternalFrame(), LPMain.getInstance()
						.getTextRespectUISPr("is.geraetehistorie"), true);

		
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelUrlaubsanspruch,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 250, 50));

		int iZeile = 0;
		panelUrlaubsanspruch.add(wsfPersonal_Techniker, new GridBagConstraints(
				0, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wsfPersonal_Techniker.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		panelUrlaubsanspruch.add(wlaDatum, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wdfDatum, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		panelUrlaubsanspruch.add(wlaGeraet, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfGeraet, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wcbNichtMoeglich, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		panelUrlaubsanspruch.add(wbuFertig, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		
		iZeile++;
		
		panelUrlaubsanspruch.add(panelQueryHistorie, new GridBagConstraints(0, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		panelQueryHistorie.eventYouAreSelected(false);
		
		

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {
		try {
			if (wnfGeraet.getInteger() != null) {

				KeyEvent k = new KeyEvent(wnfGeraet, 0, 0, 0,
						KeyEvent.VK_ENTER, ' ');

				keyPressed(k);
			}
		} catch (Throwable ex) {
			tabbedpane.handleException(ex, true);
		}

		this.dispose();
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == wnfGeraet) {

				try {
					if (wnfGeraet.getInteger() == null
							|| wdfDatum.getTimestamp() == null
							|| wsfPersonal_Techniker.getIKey() == null) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}
				} catch (ExceptionLP e2) {
					tabbedpane.handleException(e2, true);
				}

				GeraetDto geraetDto = null;
				try {
					geraetDto = DelegateFactory.getInstance()
							.getInstandhaltungDelegate()
							.geraetFindByPrimaryKey(wnfGeraet.getInteger());

				} catch (Throwable ex) {
					if (ex instanceof ExceptionLP) {
						ExceptionLP exLP = (ExceptionLP) ex;
						if (exLP.getICode() == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
							DialogFactory.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
									"Ger\u00E4t konnte nicht gefunden werden.");
							geraetDto = null;
							return;
							

						} else {
							tabbedpane.handleException(ex, true);
						}
					}

					else {
						tabbedpane.handleException(ex, true);
					}

				}

				if (geraetDto != null) {

					try {
						GeraetehistorieDto[] ghDtos = DelegateFactory
								.getInstance()
								.getInstandhaltungDelegate()
								.geraetehistorieFindByGeraetIIdPersonalIIdTechnikerTWartung(
										geraetDto.getIId(),
										wsfPersonal_Techniker.getIKey(),
										wdfDatum.getTimestamp());

						if (ghDtos != null && ghDtos.length > 0) {
							Object[] aOptionen = new Object[3];
							aOptionen[0] = LPMain
									.getInstance()
									.getTextRespectUISPr(
											"is.wartungenerfassen.doppelt.abbrechen");
							aOptionen[1] = LPMain
									.getInstance()
									.getTextRespectUISPr(
											"is.wartungenerfassen.doppelt.loeschen");
							aOptionen[2] = LPMain
									.getInstance()
									.getTextRespectUISPr(
											"is.wartungenerfassen.doppelt.trotzdem");

							int iOption = DialogFactory.showModalDialog(
									tabbedpane.getInternalFrame(),
									LPMain.getInstance().getTextRespectUISPr(
											"is.wartungenerfassen.doppelt"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.info"), aOptionen, 0);

							if (iOption == 0) {
								return;
							} else if (iOption == 1) {
								for (int i = 0; i < ghDtos.length; i++) {
									DelegateFactory.getInstance()
											.getInstandhaltungDelegate()
											.removeGeraetehistorie(ghDtos[i]);
								}
							}

						}

					} catch (Throwable e1) {
						tabbedpane.handleException(e1, true);
					}

					GeraetehistorieDto ghDto = new GeraetehistorieDto();
					ghDto.setBNichtmoeglich(wcbNichtMoeglich.getShort());
					ghDto.setGeraetIId(geraetDto.getIId());
					ghDto.setPersonalIIdTechniker(wsfPersonal_Techniker
							.getIKey());
					ghDto.setTWartung(Helper.cutTimestamp(wdfDatum
							.getTimestamp()));
					try {
						Integer newId=DelegateFactory.getInstance()
								.getInstandhaltungDelegate()
								.createGeraetehistorie(ghDto);
						panelQueryHistorie.eventYouAreSelected(false);
						panelQueryHistorie.setSelectedId(newId);
					} catch (Throwable ex) {
						tabbedpane.handleException(ex, true);
					}

					wdfDatum.requestFocus();
				
				}
			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}

class DialogWartungenerfassen_wbuFertig_actionAdapter implements ActionListener {
	private DialogWartungenerfassen adaptee;

	DialogWartungenerfassen_wbuFertig_actionAdapter(
			DialogWartungenerfassen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuFertig_actionPerformed(e);
	}
}
