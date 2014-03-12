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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.service.BelegDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogGeaenderteKonditionenEK extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton btnBeleg = new JButton();
	private JButton btnLieferant = new JButton();

	public boolean bAbgebrochen = true;
	public boolean bKonditionenUnterschiedlich = false;

	BelegDto belegDto = null;
	LieferantDto lieferantDto = null;

	public boolean bKundeSelektiert = false;

	InternalFrame internalFrame = null;

	public DialogGeaenderteKonditionenEK(BelegDto belegDto,
			Integer lieferantIIdNeu, InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.konditionen"), true);

		this.internalFrame = internalFrame;
		this.belegDto = belegDto;

		lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(lieferantIIdNeu);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(700, 290));

	}

	public BelegDto getBelegDto() {
		return belegDto;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnLieferant)) {

			bKundeSelektiert = true;

			belegDto.setSpediteurIId(lieferantDto.getIdSpediteur());

			belegDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
			belegDto.setLieferartIId(lieferantDto.getLieferartIId());

			

			double dRabatt = 0;
			if (lieferantDto.getNRabatt() != null) {
				dRabatt = lieferantDto.getNRabatt();
			}
			belegDto.setFAllgemeinerRabattsatz(dRabatt);

			bAbgebrochen = false;
			this.setVisible(false);
		} else if (e.getSource().equals(btnBeleg)) {
			bAbgebrochen = false;
			this.setVisible(false);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnBeleg.setText(LPMain.getInstance().getTextRespectUISPr("lp.beleg"));
		btnLieferant.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferant"));
		btnBeleg.addActionListener(this);
		btnLieferant.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		String lieferartLieferantString = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(lieferantDto.getLieferartIId())
				.formatBez();

		String zahlungszielLieferantString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(lieferantDto.getZahlungszielIId())
				.getCBez();

		String lieferartBelegString = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(belegDto.getLieferartIId())
				.formatBez();
		String spediteurBelegString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(belegDto.getSpediteurIId())
				.getCNamedesspediteurs();
		String zahlungszielBelegString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(
						belegDto.getZahlungszielIId()).getCBez();

		int iZeile = 0;

		WrapperLabel wla = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.konditionen.beleg.lieferant.weichenab"));
		wla.setHorizontalAlignment(SwingConstants.LEFT);
		this.getContentPane().add(
				wla,
				new GridBagConstraints(0, iZeile, 4, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"lp.beleg")),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"label.lieferant")),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaLieferart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferart"));

		if (!lieferantDto.getLieferartIId().equals(
				belegDto.getLieferartIId())) {
			wlaLieferart.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaLieferart,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));

		this.getContentPane().add(
				new WrapperLabel(lieferartBelegString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						250, 0));
		this.getContentPane().add(
				new WrapperLabel(lieferartLieferantString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						250, 0));
		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						100, 0));

		iZeile++;

		WrapperLabel wlaZahlungsziel = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.zahlungsziel"));

		if (!lieferantDto.getZahlungszielIId().equals(
				belegDto.getZahlungszielIId())) {
			wlaZahlungsziel.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaZahlungsziel,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zahlungszielBelegString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zahlungszielLieferantString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		WrapperLabel wlaSpediteur = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.spediteur"));

		if (!lieferantDto.getIdSpediteur().equals(
				belegDto.getSpediteurIId())) {
			wlaSpediteur.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaSpediteur,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(spediteurBelegString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		String spediteurKundeString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(lieferantDto.getIdSpediteur())
				.getCNamedesspediteurs();
		this.getContentPane().add(
				new WrapperLabel(spediteurKundeString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		iZeile++;

	

		double dKundeRabattsatz = 0;
		if (lieferantDto.getNRabatt() != null) {
			dKundeRabattsatz = lieferantDto.getNRabatt();
		}

		// SP1290
		if (internalFrame instanceof InternalFrameRechnung) {
			if (internalFrame.getTabbedPaneRoot().getSelectedIndex() == InternalFrameRechnung.IDX_TABBED_PANE_RECHNUNG) {
				RechnungDto rDto = ((InternalFrameRechnung) internalFrame)
						.getTabbedPaneRechnung().getRechnungDto();
				if (rDto != null) {
					boolean b = DelegateFactory.getInstance()
							.getInseratDelegate()
							.istInseratInEinerRechnungEnthalten(rDto.getIId());
					if (b == true) {
						dKundeRabattsatz = 0;
					}
				}
			}

		}

		WrapperLabel wlaAllgRabatt = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.allgemeinerrabatt"));

		if (dKundeRabattsatz != belegDto.getFAllgemeinerRabattsatz()
				.doubleValue()) {
			wlaAllgRabatt.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaAllgRabatt,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(belegDto
						.getFAllgemeinerRabattsatz(), 2, LPMain.getTheClient()
						.getLocUi())),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(dKundeRabattsatz, 2, LPMain
						.getTheClient().getLocUi())),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaWelche = new WrapperLabel(
				LPMain.getInstance()
						.getTextRespectUISPr(
								"ls.sammellieferschein.konditionenweichenab.welche.uebernehmen"));
		wlaWelche.setHorizontalAlignment(SwingConstants.LEFT);

		this.getContentPane().add(
				wlaWelche,
				new GridBagConstraints(0, iZeile, 3, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------

		iZeile++;

		this.getContentPane().add(
				btnBeleg,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnLieferant,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
