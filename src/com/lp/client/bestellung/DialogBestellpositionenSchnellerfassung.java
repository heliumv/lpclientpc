package com.lp.client.bestellung;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogBestellpositionenSchnellerfassung extends JDialog implements
		KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	WrapperLabel wlaArtikelnummer = new WrapperLabel();
	JTextField wtfArtikelnummer = new JTextField();
	WrapperLabel wlaBezeichnung = new WrapperLabel();
	JTextField wtfBezeichnung = new JTextField();
	WrapperLabel wlaMenge = new WrapperLabel();
	WrapperNumberField wnfMenge = new WrapperNumberField();
	JButton wbuFertig = new JButton();

	ArtikelDto letzter_gebuchter_artikelDto = null;
	ArtikelDto artikelDto = null;
	BestellungDto bestellungDto = null;
	PanelSplit panelPositionen = null;

	public DialogBestellpositionenSchnellerfassung(
			PanelSplit panelInventurliste, BestellungDto bestellungDto)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("auftrag.positionen.schnelleingabe"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panelPositionen = panelInventurliste;
		this.bestellungDto = bestellungDto;
		jbInit();
		pack();
		wtfArtikelnummer.requestFocus();
	}

	public void dispose() {
		try {

			if (wtfArtikelnummer.getText() != null
					&& wtfArtikelnummer.getText().length() > 0
					&& artikelDto != null && wnfMenge.getBigDecimal() != null) {

				KeyEvent k = new KeyEvent(wnfMenge, 0, 0, 0, KeyEvent.VK_ENTER,
						' ');

				keyPressed(k);
			}

		} catch (Throwable ex) {
			panelPositionen.handleException(ex, true);
		}

		super.dispose();
	}

	private void jbInit() throws Throwable {
		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setEnabled(false);

		wtfArtikelnummer.addKeyListener(this);

		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfMenge.addKeyListener(this);

		wbuFertig.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.beenden"));
		wbuFertig
				.addActionListener(new DialogBestellpositionenSchnellerfassung_wbuFertig_actionAdapter(
						this));
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panelUrlaubsanspruch,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 250, 50));

		panelUrlaubsanspruch.add(wtfArtikelnummer, new GridBagConstraints(2, 1,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaArtikelnummer, new GridBagConstraints(0, 1,
				1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wtfBezeichnung, new GridBagConstraints(2, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaBezeichnung, new GridBagConstraints(0, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaMenge, new GridBagConstraints(0, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfMenge, new GridBagConstraints(2, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wbuFertig, new GridBagConstraints(2, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {

		this.dispose();
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == wtfArtikelnummer) {

				try {
					artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByCNr(wtfArtikelnummer.getText());

				} catch (Throwable ex) {
					if (ex instanceof ExceptionLP) {
						ExceptionLP exLP = (ExceptionLP) ex;
						if (exLP.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
							DialogFactory.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
									"Artikel konnte nicht gefunden werden.");
							artikelDto = null;
							wtfArtikelnummer.setText(null);
							wtfBezeichnung.setText(null);
							wtfArtikelnummer.requestFocus();
						} else {
							panelPositionen.handleException(ex, true);
						}
					}

					else {
						panelPositionen.handleException(ex, true);
					}

				}

				if (artikelDto != null) {
					wtfBezeichnung.setText(artikelDto
							.formatArtikelbezeichnung());

					wnfMenge.requestFocus();

				}
			} else if (e.getSource() == wnfMenge) {

				// Auftragsposition speichern

				try {
					if (artikelDto != null && wnfMenge.getBigDecimal() != null) {

						BestellpositionDto bestposDto = new BestellpositionDto();
						bestposDto.setBelegIId(bestellungDto.getIId());
						bestposDto.setArtikelIId(artikelDto.getIId());
						bestposDto
								.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
						bestposDto.setNMenge(wnfMenge.getBigDecimal());
						bestposDto.setBNettopreisuebersteuert(Helper
								.boolean2Short(false));

						bestposDto.setBArtikelbezeichnunguebersteuert(Helper
								.boolean2Short(false));
						
						bestposDto.setEinheitCNr(artikelDto.getEinheitCNr());

						ArtikellieferantDto alDto = DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.getArtikelEinkaufspreis(
										artikelDto.getIId(),
										bestellungDto
												.getLieferantIIdBestelladresse(),
										wnfMenge.getBigDecimal(),
										bestellungDto.getWaehrungCNr(),
										bestellungDto.getDBelegdatum());

						if (alDto != null && alDto.getNNettopreis() != null) {
							bestposDto.setNNettoeinzelpreis(alDto
									.getNEinzelpreis());
							bestposDto.setDRabattsatz(alDto.getFRabatt());
							bestposDto.setNNettogesamtpreis(alDto
									.getNNettopreis());
							bestposDto.setNMaterialzuschlag(alDto.getNMaterialzuschlag());
							bestposDto.setNRabattbetrag(Helper
							.getProzentWert(alDto.getNNettopreis(),
									new BigDecimal(alDto.getFRabatt()),
									Defaults.getInstance()
											.getIUINachkommastellenPreiseEK()));
							
							

						} else {
							bestposDto.setNNettoeinzelpreis(BigDecimal.ZERO);
							bestposDto.setDRabattsatz(0D);
							bestposDto.setNNettogesamtpreis(BigDecimal.ZERO);
							bestposDto.setNRabattbetrag(BigDecimal.ZERO);
							bestposDto.setNMaterialzuschlag(BigDecimal.ZERO);
						}

						DelegateFactory
								.getInstance()
								.getBestellungDelegate()
								.createBestellposition(
										bestposDto,
										BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
										null);
						wtfArtikelnummer.setText(null);
						wtfBezeichnung.setText(null);

						wnfMenge.setDouble(null);

					}
				} catch (Throwable ex) {
					panelPositionen.handleException(ex, true);
				}
				wtfArtikelnummer.requestFocus();

			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}

class DialogBestellpositionenSchnellerfassung_wbuFertig_actionAdapter implements
		ActionListener {
	private DialogBestellpositionenSchnellerfassung adaptee;

	DialogBestellpositionenSchnellerfassung_wbuFertig_actionAdapter(
			DialogBestellpositionenSchnellerfassung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuFertig_actionPerformed(e);
	}
}
