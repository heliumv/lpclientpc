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
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogGeaenderteKonditionenVK extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton btnBeleg = new JButton();
	private JButton btnKunde = new JButton();

	public boolean bAbgebrochen = true;
	public boolean bKonditionenUnterschiedlich = false;

	BelegVerkaufDto belegVerkaufDto = null;
	KundeDto kundeDto = null;
	KundeDto kundeDtoLiefeadresse = null;

	public boolean bKundeSelektiert = false;

	InternalFrame internalFrame = null;

	public DialogGeaenderteKonditionenVK(BelegVerkaufDto belegVerkaufDto,
			Integer kundeIIdNeu, Integer kundeIIdNeuLieferadresse,
			InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.konditionen"), true);

		this.internalFrame = internalFrame;
		this.belegVerkaufDto = belegVerkaufDto;

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(kundeIIdNeu);
		if (kundeIIdNeuLieferadresse != null) {
			kundeDtoLiefeadresse = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(kundeIIdNeuLieferadresse);
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(700, 290));

	}

	public DialogGeaenderteKonditionenVK(BelegVerkaufDto belegVerkaufDto,
			Integer kundeIIdNeu, InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.konditionen"), true);

		this.internalFrame = internalFrame;
		this.belegVerkaufDto = belegVerkaufDto;

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(kundeIIdNeu);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(700, 290));

	}

	public BelegVerkaufDto getBelegVerkaufDto() {
		return belegVerkaufDto;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnKunde)) {

			bKundeSelektiert = true;
			if (kundeDtoLiefeadresse != null) {
				belegVerkaufDto.setSpediteurIId(kundeDtoLiefeadresse
						.getSpediteurIId());
			} else {
				belegVerkaufDto.setSpediteurIId(kundeDto.getSpediteurIId());
			}

			belegVerkaufDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
			belegVerkaufDto.setLieferartIId(kundeDto.getLieferartIId());

			belegVerkaufDto.setFVersteckterAufschlag(0D);

			double dRabatt = 0;
			if (kundeDto.getFRabattsatz() != null) {
				dRabatt = kundeDto.getFRabattsatz();
			}
			belegVerkaufDto.setFAllgemeinerRabattsatz(dRabatt);

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
		btnKunde.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.kunde"));
		btnBeleg.addActionListener(this);
		btnKunde.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		String lieferartKundeString = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(kundeDto.getLieferartIId())
				.formatBez();

		String zahlungszielKundeString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(kundeDto.getZahlungszielIId())
				.getCBez();

		String lieferartBelegString = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(belegVerkaufDto.getLieferartIId())
				.formatBez();
		String spediteurBelegString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(belegVerkaufDto.getSpediteurIId())
				.getCNamedesspediteurs();
		String zahlungszielBelegString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(
						belegVerkaufDto.getZahlungszielIId()).getCBez();

		int iZeile = 0;

		WrapperLabel wla = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.konditionen.beleg.kunde.weichenab"));
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
						"label.kunde")),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaLieferart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferart"));

		if (!kundeDto.getLieferartIId().equals(
				belegVerkaufDto.getLieferartIId())) {
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
				new WrapperLabel(lieferartKundeString),
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

		if (!kundeDto.getZahlungszielIId().equals(
				belegVerkaufDto.getZahlungszielIId())) {
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
				new WrapperLabel(zahlungszielKundeString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		WrapperLabel wlaSpediteur = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.spediteur"));

		if (kundeDtoLiefeadresse != null) {
			if (!kundeDtoLiefeadresse.getSpediteurIId().equals(
					belegVerkaufDto.getSpediteurIId())) {
				wlaSpediteur.setForeground(Color.RED);
				bKonditionenUnterschiedlich = true;
			}

			this.getContentPane().add(
					wlaSpediteur,
					new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));
			this.getContentPane().add(
					new WrapperLabel(spediteurBelegString),
					new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));

			String spediteurKundeString = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.spediteurFindByPrimaryKey(
							kundeDtoLiefeadresse.getSpediteurIId())
					.getCNamedesspediteurs();

			this.getContentPane().add(
					new WrapperLabel(spediteurKundeString),
					new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));

		} else {
			if (!kundeDto.getSpediteurIId().equals(
					belegVerkaufDto.getSpediteurIId())) {
				wlaSpediteur.setForeground(Color.RED);
				bKonditionenUnterschiedlich = true;
			}

			this.getContentPane().add(
					wlaSpediteur,
					new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));
			this.getContentPane().add(
					new WrapperLabel(spediteurBelegString),
					new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));

			String spediteurKundeString = DelegateFactory.getInstance()
					.getMandantDelegate()
					.spediteurFindByPrimaryKey(kundeDto.getSpediteurIId())
					.getCNamedesspediteurs();
			this.getContentPane().add(
					new WrapperLabel(spediteurKundeString),
					new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2,
									5), 0, 0));

		}

		iZeile++;

		WrapperLabel wlaVersteckterAufschlag = new WrapperLabel(LPMain
				.getInstance()
				.getTextRespectUISPr("label.versteckteraufschlag"));

		if (0 != belegVerkaufDto.getFVersteckterAufschlag()) {
			wlaVersteckterAufschlag.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaVersteckterAufschlag,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(belegVerkaufDto
						.getFVersteckterAufschlag(), 2, LPMain.getTheClient()
						.getLocUi())),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(0D, 2, LPMain.getTheClient()
						.getLocUi())),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		double dKundeRabattsatz = 0;
		if (kundeDto.getFRabattsatz() != null) {
			dKundeRabattsatz = kundeDto.getFRabattsatz();
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

		if (dKundeRabattsatz != belegVerkaufDto.getFAllgemeinerRabattsatz()
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
				new WrapperLabel(Helper.formatZahl(belegVerkaufDto
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
				btnKunde,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
