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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PositionNumberHelperAuftrag;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/**
 * <p>
 * In diesem Detailfenster bekommt man einen Blick auf alle mengenbehafteten
 * Auftragpositionen jenes Auftrags, zu dem dieser Lieferschein gehoert.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2004-10-13
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.16 $
 */
public abstract class PanelPositionenSichtAuftrag extends PanelPositionen2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaGeliefertArtikel = null;
	private WrapperLabel wlaOffenimauftragArtikel = null;

	protected WrapperNumberField wnfGeliefertArtikel = null;
	protected WrapperNumberField wnfOffenimauftragArtikel = null;

	private WrapperLabel wlaGeliefertHand = null;
	private WrapperLabel wlaOffenimauftragHand = null;

	protected WrapperNumberField wnfGeliefertHand = null;
	protected WrapperNumberField wnfOffenimauftragHand = null;

	protected AuftragpositionDto oAuftragpositionDto = null;

	private WrapperTextField wtfAuftragNummer = null;
	private WrapperTextField wtfAuftragProjekt = null;
	private WrapperTextField wtfAuftragBestellnummer = null;
	public boolean bOffeneMengeVorschlagen = true;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Auftrags
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelPositionenSichtAuftrag(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR); // VKPF

		zwController.setPositionNumberHelper(new PositionNumberHelperAuftrag());

		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		// braucht nur refresh, save und aendern
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// zusaetzliche Felder am PanelArtikel
		panelArtikel.wlaMenge.setText(LPMain
				.getTextRespectUISPr("ls.label.mengels"));

		// panelArtikel.wnfMenge.setMinimumValue(1); // Eingabe muss > 0 sein

		wlaGeliefertArtikel = new WrapperLabel();
		wlaGeliefertArtikel.setText(LPMain
				.getTextRespectUISPr("label.geliefert"));

		wlaOffenimauftragArtikel = new WrapperLabel();
		wlaOffenimauftragArtikel.setText(LPMain
				.getTextRespectUISPr("label.offenimauftrag"));

		wnfGeliefertArtikel = new WrapperNumberField();

		wnfOffenimauftragArtikel = new WrapperNumberField();

		wnfGeliefertArtikel.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfOffenimauftragArtikel.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		panelArtikel.add(wlaGeliefertArtikel, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wlaOffenimauftragArtikel, new GridBagConstraints(0, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wnfGeliefertArtikel, new GridBagConstraints(1, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelArtikel.add(wnfOffenimauftragArtikel, new GridBagConstraints(1, 3,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// zusaetzliche Felder am PanelHandeingabe
		panelHandeingabe.wlaMenge.setText(LPMain
				.getTextRespectUISPr("ls.label.mengels"));

		wlaGeliefertHand = new WrapperLabel();
		wlaGeliefertHand.setText(LPMain.getTextRespectUISPr("label.geliefert"));

		wlaOffenimauftragHand = new WrapperLabel();
		wlaOffenimauftragHand.setText(LPMain
				.getTextRespectUISPr("label.offenimauftrag"));

		wnfGeliefertHand = new WrapperNumberField();

		wnfOffenimauftragHand = new WrapperNumberField();

		wnfGeliefertHand.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfOffenimauftragHand.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		panelHandeingabe.add(wlaOffenimauftragHand, new GridBagConstraints(0,
				3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wlaGeliefertHand, new GridBagConstraints(0, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfGeliefertHand, new GridBagConstraints(1, 4, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelHandeingabe.add(wnfOffenimauftragHand, new GridBagConstraints(1,
				3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// UW 21.06.06 keine VKPF
		panelArtikel.wnfMenge
				.removeFocusListener(((PanelPositionenArtikelVerkauf) panelArtikel).wnfMengeFocusListener);

		// zusaetzliche Felder fuer den Auftrag beim Artikel
		iZeile++;
		WrapperLabel wlaAuftrag = new WrapperLabel(
				LPMain.getTextRespectUISPr("auft.auftrag"));
		wlaAuftrag.setMinimumSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
				Defaults.getInstance().getControlHeight()));
		wlaAuftrag.setPreferredSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
				Defaults.getInstance().getControlHeight()));
		wtfAuftragNummer = new WrapperTextField();
		wtfAuftragNummer.setActivatable(false);
		wtfAuftragNummer.setMinimumSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wtfAuftragNummer.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wtfAuftragProjekt = new WrapperTextField();
		wtfAuftragProjekt.setActivatable(false);
		wtfAuftragBestellnummer = new WrapperTextField();
		wtfAuftragBestellnummer.setActivatable(false);
		JPanel jpaAuftrag = new JPanel(new GridBagLayout());
		jpaAuftrag.add(wlaAuftrag, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragNummer, new GridBagConstraints(1, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragProjekt, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragBestellnummer, new GridBagConstraints(3, 0, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(jpaAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory.getInstance()
				.getAuftragServiceDelegate()
				.auftragpositionartFindAll(LPMain.getTheClient().getLocUi()));
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_OFFENE_MENGE_IN_SICHT_AUFTRAG_VORSCHLAGEN,
						ParameterFac.KATEGORIE_LIEFERSCHEIN,
						LPMain.getTheClient().getMandant());
		bOffeneMengeVorschlagen = ((Boolean) parameter.getCWertAsObject());

	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		leereAlleFelder(this);

		wcoPositionsart.setActivatable(false);

		// default Positionsart ist Ident in der UI Sprache des Benutzers
		wcoPositionsart
				.setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);

		// in dieser Ansicht kann nur die Menge geaendert werden
		panelArtikel.wnfEinzelpreis.setActivatable(false);
		panelArtikel.wnfRabattsumme.setActivatable(false);
		panelArtikel.getWnfZusatzrabattsumme().setActivatable(false);
		panelArtikel.wnfNettopreis.setActivatable(false);
		panelArtikel.wnfMwstsumme.setActivatable(false);
		panelArtikel.wnfBruttopreis.setActivatable(false);
		panelArtikel.wbuArtikelauswahl.setActivatable(false);
		panelArtikel.wbuArtikelauswahl.getWrapperButtonGoTo().setActivatable(
				true);
		panelArtikel.wbuArtikelauswahl.getWrapperButtonGoTo().setEnabled(true);
		panelArtikel.getWtfArtikel().setActivatable(false);
		// panelArtikel.wtfArtikel.setMandatoryField(false);
		panelArtikel.wtfBezeichnung.setActivatable(false);
		panelArtikel.wtfZusatzbezeichnung.setActivatable(false);
		panelArtikel.wcoEinheit.setActivatable(false);
		panelArtikel.getWnfRabattsatz().setActivatable(false);
		// in der Basisklasse wurde der Zusatzrabattsatz als Mandatory definiert
		// panelArtikel.getWnfZusatzrabattsatz().setMandatoryField(false);
		panelArtikel.getWnfZusatzrabattsatz().setActivatable(false);
		panelArtikel.wcoMwstsatz.setActivatable(false);

		wnfGeliefertArtikel.setActivatable(false);
		wnfOffenimauftragArtikel.setActivatable(false);

		panelHandeingabe.wnfEinzelpreis.setActivatable(false);
		panelHandeingabe.wnfRabattsumme.setActivatable(false);
		panelHandeingabe.getWnfZusatzrabattsumme().setActivatable(false);
		panelHandeingabe.wnfNettopreis.setActivatable(false);
		panelHandeingabe.wnfMwstsumme.setActivatable(false);
		panelHandeingabe.wnfBruttopreis.setActivatable(false);
		panelHandeingabe.wtfBezeichnung.setActivatable(false);
		panelHandeingabe.wtfZusatzbezeichnung.setActivatable(false);
		panelHandeingabe.wcoEinheit.setActivatable(false);
		panelHandeingabe.getWnfRabattsatz().setActivatable(false);
		panelHandeingabe.getWnfZusatzrabattsatz().setActivatable(false);
		panelHandeingabe.wcoMwstsatz.setActivatable(false);

		wnfGeliefertHand.setActivatable(false);
		wnfOffenimauftragHand.setActivatable(false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // buttons schalten

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		// position neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = getKeyWhenDetailPanel();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			oAuftragpositionDto = DelegateFactory.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey((Integer) oKey);
			auftragpositionDto2Components();
		}
	}

	/**
	 * Anzeige saemtlicher Felder der Auftragposition. <br>
	 * Es kann nichts veraendert werden, ausser die Menge im Lieferschein.
	 * 
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void auftragpositionDto2Components() throws Throwable {
		wcoPositionsart.setKeyOfSelectedItem(oAuftragpositionDto
				.getPositionsartCNr());
		// Falls Auftrags- und Lieferscheinwaehrung unterschiedlich sind, den
		// Kurs bestimmen
		AuftragDto auftragDto = DelegateFactory.getInstance()
				.getAuftragDelegate()
				.auftragFindByPrimaryKey(oAuftragpositionDto.getBelegIId());
		String sBelegwaehrung = getWaehrungCNrBeleg();
		BigDecimal bdKurs = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.getWechselkurs2(auftragDto.getCAuftragswaehrung(),
						sBelegwaehrung);
		panelArtikel.wbuArtikelauswahl.setOKey(oAuftragpositionDto
				.getArtikelIId());
		//SP2881
		panelArtikel.setUebersteuertesLagerIId(null);
		// wenn der Wechselkurs zwischen Auftragwaehrung und
		// Lieferscheinwaehrung nicht hinterlegt ist .
		if (bdKurs == null) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							"Zwischen "
									+ auftragDto.getCAuftragswaehrung()
									+ " und "
									+ sBelegwaehrung
									+ " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		if (oAuftragpositionDto.getPositionsartCNr().equalsIgnoreCase(
				AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							oAuftragpositionDto.getArtikelIId());
			panelArtikel.setArtikelDto(artikelDto);

			// die Artikelbezeichnung oder die Artikelzusatzbezeichnung kann
			// uebersteuert sein
			if (Helper.short2boolean(oAuftragpositionDto
					.getBArtikelbezeichnunguebersteuert()) == false) {
				if (artikelDto.getArtikelsprDto() != null) {
					panelArtikel.wtfBezeichnung.setText(artikelDto
							.getArtikelsprDto().getCBez());
					panelArtikel.wtfZusatzbezeichnung.setText(artikelDto
							.getArtikelsprDto().getCZbez());
				}
			} else {
				panelArtikel.wtfBezeichnung.setText(oAuftragpositionDto
						.getCBez());
				panelArtikel.wtfZusatzbezeichnung.setText(oAuftragpositionDto
						.getCZusatzbez());
			}

			boolean bAktuell = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.pruefeObMwstsatzNochAktuell(oAuftragpositionDto,
							panelArtikel.getTBelegdatumMwstsatz());

			if (bAktuell == false) {
				// Mwstbetrag neu berechnen
				MwstsatzDto mwstSatzDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mwstsatzFindByPrimaryKey(
								oAuftragpositionDto.getMwstsatzIId());
				BigDecimal mwstBetrag = Helper
						.getProzentWert(oAuftragpositionDto
								.getNNettoeinzelpreis(), new BigDecimal(
								mwstSatzDto.getFMwstsatz()), Defaults
								.getInstance().getIUINachkommastellenPreiseVK());

				oAuftragpositionDto.setNMwstbetrag(mwstBetrag);
				oAuftragpositionDto.setNBruttoeinzelpreis(oAuftragpositionDto
						.getNNettoeinzelpreis().add(mwstBetrag));
			}

			panelArtikel.wnfMenge.setBigDecimal(new BigDecimal(0));
			wnfOffenimauftragArtikel.setBigDecimal(oAuftragpositionDto
					.getNOffeneMenge());
			wnfGeliefertArtikel.setBigDecimal(oAuftragpositionDto.getNMenge()
					.subtract(oAuftragpositionDto.getNMenge()));

			panelArtikel.wcoEinheit.setKeyOfSelectedItem(oAuftragpositionDto
					.getEinheitCNr());
			panelArtikel.wsfKostentraeger.setKey(oAuftragpositionDto
					.getKostentraegerIId());
			panelArtikel.wtfLVPosition.setText(oAuftragpositionDto
					.getCLvposition());
			if (panelArtikel.wnfMaterialzuschlag != null) {
				panelArtikel.wnfMaterialzuschlag
						.setBigDecimal(oAuftragpositionDto
								.getNMaterialzuschlag());
			}

			PanelPositionenArtikelVerkauf panel = (PanelPositionenArtikelVerkauf) panelArtikel;
			// den Verkaufspreis hinterlegen und anzeigen
			panel.verkaufspreisDtoInZielwaehrung = new VerkaufspreisDto();

			panel.verkaufspreisDtoInZielwaehrung.einzelpreis = oAuftragpositionDto
					.getNEinzelpreis().multiply(bdKurs);
			panel.verkaufspreisDtoInZielwaehrung.rabattsumme = oAuftragpositionDto
					.getNRabattbetrag().multiply(bdKurs);
			if (oAuftragpositionDto.getNNettoeinzelpreis() != null) {
				panel.verkaufspreisDtoInZielwaehrung.nettopreis = oAuftragpositionDto
						.getNNettoeinzelpreis().multiply(bdKurs);
			}
			if (oAuftragpositionDto.getNMaterialzuschlag() != null) {
				panel.verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag = oAuftragpositionDto
						.getNMaterialzuschlag().multiply(bdKurs);
			} else {
				panel.verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag = new BigDecimal(
						0);
			}
			panel.verkaufspreisDtoInZielwaehrung.mwstsumme = oAuftragpositionDto
					.getNMwstbetrag().multiply(bdKurs);
			panel.verkaufspreisDtoInZielwaehrung.bruttopreis = oAuftragpositionDto
					.getNBruttoeinzelpreis().multiply(bdKurs);
			panel.verkaufspreisDtoInZielwaehrung.rabattsatz = oAuftragpositionDto
					.getFRabattsatz();

			panel.verkaufspreisDtoInZielwaehrung
					.setDdZusatzrabattsatz(oAuftragpositionDto
							.getFZusatzrabattsatz());

			BigDecimal nZusatzrabattsumme = oAuftragpositionDto
					.getNEinzelpreis()
					.subtract(oAuftragpositionDto.getNRabattbetrag())
					.multiply(
							new BigDecimal(oAuftragpositionDto
									.getFZusatzrabattsatz().doubleValue()))
					.movePointLeft(2);

			panel.verkaufspreisDtoInZielwaehrung
					.setNZusatzrabattsumme(nZusatzrabattsumme.multiply(bdKurs));

			panel.verkaufspreisDtoInZielwaehrung.mwstsatzIId = oAuftragpositionDto
					.getMwstsatzIId();

			panel.verkaufspreisDto2components();
			if (Helper.short2boolean(oAuftragpositionDto
					.getBNettopreisuebersteuert()))
				panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(true);
			// in dieser Sicht gilt immer der Mwstsatz der Auftragposition
			panelArtikel.wcoMwstsatz.setKeyOfSelectedItem(oAuftragpositionDto
					.getMwstsatzIId());
			if (panelArtikel.wcoVerleih != null) {
				panelArtikel.wcoVerleih
						.setKeyOfSelectedItem(oAuftragpositionDto
								.getVerleihIId());
			}
		} else if (oAuftragpositionDto.getPositionsartCNr()
				.equalsIgnoreCase(LocaleFac.POSITIONSART_HANDEINGABE)) {

			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							oAuftragpositionDto.getArtikelIId());
			panelHandeingabe.setArtikelDto(artikelDto);

			if (artikelDto.getArtikelsprDto() != null) {
				panelHandeingabe.wtfBezeichnung.setText(artikelDto
						.getArtikelsprDto().getCBez());
			}

			if (artikelDto.getArtikelsprDto() != null) {
				panelHandeingabe.wtfZusatzbezeichnung.setText(artikelDto
						.getArtikelsprDto().getCZbez());
			}

			// panelArtikel.wnfMenge.setBigDecimal(oAuftragpositionDto.getNMenge(
			// ));
			wnfOffenimauftragHand.setBigDecimal(oAuftragpositionDto
					.getNOffeneMenge());
			wnfGeliefertHand.setBigDecimal(oAuftragpositionDto.getNMenge()
					.subtract(oAuftragpositionDto.getNMenge()));

			panelHandeingabe.wcoEinheit
					.setKeyOfSelectedItem(oAuftragpositionDto.getEinheitCNr());
			panelHandeingabe.wtfLVPosition.setText(oAuftragpositionDto
					.getCLvposition());
			// den Verkaufspreis anzeigen
			panelHandeingabe.wnfEinzelpreis.setBigDecimal(oAuftragpositionDto
					.getNEinzelpreis().multiply(bdKurs));
			panelHandeingabe.wnfRabattsumme.setBigDecimal(oAuftragpositionDto
					.getNRabattbetrag().multiply(bdKurs));
			panelHandeingabe.wnfNettopreis.setBigDecimal(oAuftragpositionDto
					.getNNettoeinzelpreis().multiply(bdKurs));
			panelHandeingabe.wnfMwstsumme.setBigDecimal(oAuftragpositionDto
					.getNMwstbetrag().multiply(bdKurs));
			panelHandeingabe.wnfBruttopreis.setBigDecimal(oAuftragpositionDto
					.getNBruttoeinzelpreis().multiply(bdKurs));

			panelHandeingabe.getWnfRabattsatz().setDouble(
					oAuftragpositionDto.getFRabattsatz());
			panelHandeingabe.wcoMwstsatz
					.setKeyOfSelectedItem(oAuftragpositionDto.getMwstsatzIId());

			// den Zusatzrabattsatz setzen
			if (oAuftragpositionDto.getFZusatzrabattsatz() != null) {
				panelHandeingabe.wnfZusatzrabattsatz
						.setDouble(oAuftragpositionDto.getFZusatzrabattsatz());

				BigDecimal nZusatzrabattsumme = oAuftragpositionDto
						.getNEinzelpreis()
						.subtract(oAuftragpositionDto.getNRabattbetrag())
						.multiply(
								new BigDecimal(oAuftragpositionDto
										.getFZusatzrabattsatz().doubleValue()))
						.movePointLeft(2);

				panelHandeingabe.wnfZusatzrabattsumme
						.setBigDecimal(nZusatzrabattsumme.multiply(bdKurs));
			}
		} else if (oAuftragpositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_BETRIFFT)) {
			panelBetreff.wtfBetreff.setText(oAuftragpositionDto.getCBez());
		} else if (oAuftragpositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_URSPRUNGSLAND)) {
			panelUrsprung.wtfUrsprung.setText(oAuftragpositionDto.getCBez());
		} else if (oAuftragpositionDto.getPositionsartCNr()
				.equalsIgnoreCase(LocaleFac.POSITIONSART_TEXTEINGABE)) {
			panelTexteingabe.setText(
					oAuftragpositionDto.getXTextinhalt());
			panelTexteingabe.setEditable(false);
		} else if (oAuftragpositionDto.getPositionsartCNr()
				.equalsIgnoreCase(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
			panelTextbaustein.oMediastandardDto = DelegateFactory
					.getInstance()
					.getMediaDelegate()
					.mediastandardFindByPrimaryKey(
							oAuftragpositionDto.getMediastandardIId());
			panelTextbaustein.dto2Components();
		} else if (oAuftragpositionDto.getPositionsartCNr()
				.equalsIgnoreCase(
						LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
			panelIntZwischensumme.dto2Components(oAuftragpositionDto);
		}

		// Auftrag anzeigen
		wtfAuftragNummer.setText(auftragDto.getCNr());
		wtfAuftragProjekt.setText(auftragDto.getCBezProjektbezeichnung());
		wtfAuftragBestellnummer.setText(auftragDto.getCBestellnummer());
	}

	/**
	 * getWaehrungCNrBeleg
	 * 
	 * @return String
	 */
	protected abstract String getWaehrungCNrBeleg();

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}

	public AuftragpositionDto getAuftragpositionDto() {
		return oAuftragpositionDto;
	}

	// @todo ich muss auch den zugehoerigen Auftrag locken PJ 5058
}
