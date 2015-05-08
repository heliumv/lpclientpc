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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.util.Helper;

/**
 * @todo MB eigentlich koennte man diese klasse loeschen.
 * 
 * 
 *       <p>
 *       Panel fuer Artikelpositionen in der Anfrage
 *       </p>
 *       <p>
 *       Copyright Logistik Pur Software GmbH (c) 2004-2008
 *       </p>
 *       <p>
 *       Erstellungsdatum 22.07.05
 *       </p>
 *       <p>
 *       </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelPositionenArtikelAnfrage extends
		PanelPositionenArtikelEinkauf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrapperLabel wlaGewichtbestellmenge = null;
	public WrapperNumberField wnfGewichtbestellmenge = null;
	public WrapperLabel wlaGewichtmengeEinheit = null;

	public WrapperLabel wlaGewichtPreis = null;
	public WrapperNumberField wnfGewichtPreis = null;
	public WrapperLabel wlaGewichtWaehrung = null;

	private TabbedPaneAnfrage tPAnfrage = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param sLockMeWer
	 *            String
	 * @param iSpaltenbreite1I
	 *            die Breite der ersten Spalte
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenArtikelAnfrage(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key, sLockMeWer, iSpaltenbreite1I);
		tPAnfrage = ((InternalFrameAnfrage) internalFrame)
				.getTabbedPaneAnfrage();
		wcoEinheit.setActivatable(false);
		jbInitPanel();
		initPanel();
	}

	protected void wnfMenge_focusLost(FocusEvent e) {
		try {
			super.wnfMenge_focusLost(e);
			wnfEinzelpreis_focusLost(e);
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	protected void wnfEinzelpreis_focusLost(FocusEvent e) throws Throwable {
		super.wnfEinzelpreis_focusLost(e);

		BigDecimal umrechnungsfaktor = getArtikelDto().getNUmrechnungsfaktor();
		boolean bInvers = Helper.short2boolean(getArtikelDto()
				.getbBestellmengeneinheitInvers());

		if (umrechnungsfaktor == null) {

			if (getArtikellieferantDto() != null
					&& getArtikellieferantDto().getNVerpackungseinheit() != null
					&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
				umrechnungsfaktor = getArtikellieferantDto()
						.getNVerpackungseinheit();
				bInvers = true;
			}

		}

		if (wnfEinzelpreis.getBigDecimal() != null && umrechnungsfaktor != null) {
			if (umrechnungsfaktor.compareTo(new BigDecimal(0)) != 0) {

				if (bInvers) {
					wnfGewichtbestellmenge.setBigDecimal(wnfMenge
							.getBigDecimal().divide(
									umrechnungsfaktor,
									Defaults.getInstance()
											.getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));
					
					BigDecimal nPreisPerEinheit = wnfEinzelpreis
							.getBigDecimal().multiply(umrechnungsfaktor);

					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				} else {
					wnfGewichtbestellmenge.setBigDecimal(wnfMenge
							.getBigDecimal().multiply(umrechnungsfaktor));
					BigDecimal nPreisPerEinheit = (wnfEinzelpreis
							.getBigDecimal().divide(umrechnungsfaktor, Defaults
							.getInstance().getIUINachkommastellenPreiseEK(),
							BigDecimal.ROUND_HALF_EVEN));

					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				}
			}
			if (getArtikelDto().getEinheitCNrBestellung() != null) {
				wlaGewichtmengeEinheit.setText(getArtikelDto()
						.getEinheitCNrBestellung().trim() + "  \u00E0");
			} else if (getArtikellieferantDto() != null
					&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
				wlaGewichtmengeEinheit.setText(getArtikellieferantDto()
						.getEinheitCNrVpe().trim() + "  \u00E0");
			}

		}
	}

	private void jbInitPanel() throws Throwable {
		wlaGewichtbestellmenge = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.alternative.bestellmengeneinheit"));
		// wlaGewichtbestellmenge.addMouseListener(new
		// MouseAdapterChangeMenge(this));
		wnfGewichtbestellmenge = new WrapperNumberField();
		wlaGewichtmengeEinheit = new WrapperLabel();
		wlaGewichtmengeEinheit.setHorizontalAlignment(SwingConstants.LEADING);
		wlaGewichtPreis = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.preis"));
		wnfGewichtPreis = new WrapperNumberField();
		wnfGewichtPreis.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wlaGewichtWaehrung = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaGewichtWaehrung, 20);
		wlaGewichtWaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		iZeile++;
		iZeile = 13;
		add(wlaGewichtbestellmenge, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfGewichtbestellmenge, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaGewichtmengeEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wnfGewichtPreis, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		add(wlaGewichtWaehrung, new GridBagConstraints(4, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		wnfGewichtbestellmenge.addFocusListener(new FocusAdapterGewichtArtikel(
				this));
		wnfGewichtPreis.addFocusListener(new FocusAdapterGewichtPreisArtikel(
				this));
	}

	private void initPanel() {
		// Wenn der Benutzer keine Preise sehen darf, sind einige Felder nicht
		// sichtbar.
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaGewichtPreis.setVisible(false);
			wnfGewichtPreis.setVisible(false);
			wlaGewichtWaehrung.setVisible(false);
		}
	}
	
	public void setArtikelDto(ArtikelDto artikelDto) throws Throwable {
		super.setArtikelDto(artikelDto);
		boolean bGewichtVisible = false;

		BigDecimal umrechnungsfaktor = getArtikelDto().getNUmrechnungsfaktor();
		boolean bInvers = Helper.short2boolean(getArtikelDto()
				.getbBestellmengeneinheitInvers());

		if (umrechnungsfaktor == null) {

			if (getArtikellieferantDto() != null
					&& getArtikellieferantDto().getNVerpackungseinheit() != null
					&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
				umrechnungsfaktor = getArtikellieferantDto()
						.getNVerpackungseinheit();
				bInvers = true;
			}

		}

		if (artikelDto != null && umrechnungsfaktor != null) {
			bGewichtVisible = true;
			if (wnfMenge.getBigDecimal() != null) {

				if (bInvers) {

					wnfGewichtbestellmenge.setBigDecimal(wnfMenge
							.getBigDecimal().divide(
									umrechnungsfaktor,
									Defaults.getInstance()
											.getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));
					BigDecimal nPreisPerEinheit = (wnfEinzelpreis
							.getBigDecimal().multiply(umrechnungsfaktor));
					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				} else {
					wnfGewichtbestellmenge.setBigDecimal(wnfMenge
							.getBigDecimal().multiply(umrechnungsfaktor));
					BigDecimal nPreisPerEinheit = (wnfEinzelpreis
							.getBigDecimal().divide(umrechnungsfaktor, Defaults
							.getInstance().getIUINachkommastellenPreiseEK(),
							BigDecimal.ROUND_HALF_EVEN));
					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				}
			} else {
				wnfGewichtbestellmenge.setBigDecimal(umrechnungsfaktor);

				wnfGewichtPreis.setBigDecimal(null);
			}
			
			
			if (getArtikelDto().getEinheitCNrBestellung() != null) {
				wlaGewichtmengeEinheit.setText(getArtikelDto()
						.getEinheitCNrBestellung().trim());
			} else if (getArtikellieferantDto() != null) {
				wlaGewichtmengeEinheit.setText(getArtikellieferantDto()
						.getEinheitCNrVpe().trim());
			}
			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				wlaGewichtmengeEinheit.setText(wlaGewichtmengeEinheit.getText()
						+ "  \u00E0");
			}
			wlaGewichtWaehrung.setText(tPAnfrage.getAnfrageDto().getWaehrungCNr());
			
		} else {
			wnfGewichtbestellmenge.setBigDecimal(null);
			wnfGewichtPreis.setBigDecimal(null);
		}
		wlaGewichtbestellmenge.setVisible(bGewichtVisible);
		wnfGewichtbestellmenge.setVisible(bGewichtVisible);
		wlaGewichtmengeEinheit.setVisible(bGewichtVisible);

		// nur Anzeigen wenn auch Recht fuer Preise sehen
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaGewichtPreis.setVisible(false);
			wnfGewichtPreis.setVisible(false);
			wlaGewichtWaehrung.setVisible(false);
		} else {
			wlaGewichtPreis.setVisible(bGewichtVisible);
			wnfGewichtPreis.setVisible(bGewichtVisible);
			wlaGewichtWaehrung.setVisible(bGewichtVisible);
		}

	}
	
}

class FocusAdapterGewichtPreisArtikel implements FocusListener {
	private PanelPositionenArtikelAnfrage adaptee = null;

	FocusAdapterGewichtPreisArtikel(PanelPositionenArtikelAnfrage adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {

			if (adaptee.wnfGewichtPreis.getBigDecimal() != null) {

				BigDecimal umrechnungsfaktor = adaptee.getArtikelDto()
						.getNUmrechnungsfaktor();
				boolean bInvers = Helper.short2boolean(adaptee.getArtikelDto()
						.getbBestellmengeneinheitInvers());

				if (umrechnungsfaktor == null) {

					if (adaptee.getArtikellieferantDto() != null
							&& adaptee.getArtikellieferantDto()
									.getNVerpackungseinheit() != null
							&& adaptee.getArtikellieferantDto()
									.getEinheitCNrVpe() != null) {
						umrechnungsfaktor = adaptee.getArtikellieferantDto()
								.getNVerpackungseinheit();
						bInvers = true;
					}

				}

				if (umrechnungsfaktor != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal preis = adaptee.wnfGewichtPreis.getBigDecimal();

					if (preis != null || umrechnungsfaktor != null) {
						if (bInvers) {
							umrechnung = preis.divide(umrechnungsfaktor,
									Defaults.getInstance()
											.getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							umrechnung = preis.multiply(umrechnungsfaktor);
						}

						adaptee.wnfEinzelpreis.setBigDecimal(umrechnung);
						// noch ein focuslost auf den Einzelpreis erzeugen,
						// damit sich der nettopreis mit aendert
						adaptee.wnfEinzelpreis.requestFocusInWindow();
						adaptee.wtfBezeichnung.requestFocusInWindow();
						// den Focus auf den Einzelpreis setzen
						adaptee.wnfEinzelpreis.requestFocusInWindow();
					}
				}
			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class FocusAdapterGewichtArtikel implements FocusListener {
	private PanelPositionenArtikelAnfrage adaptee = null;

	FocusAdapterGewichtArtikel(PanelPositionenArtikelAnfrage adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {

			if (adaptee.wnfGewichtbestellmenge.getBigDecimal() != null) {

				BigDecimal umrechnungsfaktor = adaptee.getArtikelDto()
						.getNUmrechnungsfaktor();
				boolean bInvers = Helper.short2boolean(adaptee.getArtikelDto()
						.getbBestellmengeneinheitInvers());

				if (umrechnungsfaktor == null) {

					if (adaptee.getArtikellieferantDto() != null
							&& adaptee.getArtikellieferantDto()
									.getNVerpackungseinheit() != null
							&& adaptee.getArtikellieferantDto()
									.getEinheitCNrVpe() != null) {
						umrechnungsfaktor = adaptee.getArtikellieferantDto()
								.getNVerpackungseinheit();
						bInvers = true;
					}

				}

				if (umrechnungsfaktor != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal gewicht = adaptee.wnfGewichtbestellmenge
							.getBigDecimal();

					if (gewicht != null || umrechnungsfaktor != null) {

						if (bInvers) {

							umrechnung = gewicht.multiply(umrechnungsfaktor);
						} else {
							umrechnung = gewicht.divide(umrechnungsfaktor, 4,
									BigDecimal.ROUND_HALF_EVEN);
						}
						adaptee.wnfMenge.setBigDecimal(umrechnung);
					}
				}
			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}