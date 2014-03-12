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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.lp.client.anfrage.AnfrageFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.artikel.TabbedPaneArtikel;
import com.lp.client.eingangsrechnung.EingangsrechnungFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.instandhaltung.InstandhaltungFilterFactory;
import com.lp.client.kueche.KuecheFilterFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneKunde;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.stueckliste.TabbedPaneStueckliste;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.anfrage.service.ZertifikatartDto;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.LosbereichDto;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.instandhaltung.service.AnlageDto;
import com.lp.server.instandhaltung.service.GeraetetypDto;
import com.lp.server.instandhaltung.service.GewerkDto;
import com.lp.server.instandhaltung.service.HalleDto;
import com.lp.server.instandhaltung.service.IskategorieDto;
import com.lp.server.instandhaltung.service.IsmaschineDto;
import com.lp.server.instandhaltung.service.StandortDto;
import com.lp.server.kueche.service.KassaartikelDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.personal.service.BereitschaftartDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.LohnartDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalgruppeDto;
import com.lp.server.personal.service.TagesartDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.KostentraegerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;

/**
 * <p>
 * Gewrappter JButton<br/>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.43 $
 */
public class WrapperSelectField extends JPanel implements ActionListener,
		ItemChangedListener, IControl, IDirektHilfe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperButton wrapperButton = null;
	private WrapperGotoButton wrapperGotoButton = null;
	private WrapperTextField wrapperTextField = null;
	private Object oKey = null;
	private WrapperButton buttonGoto = null;
	private String sTyp = null;
	private InternalFrame internalFrame = null;
	private boolean bMitLeerenButton = true;
	boolean bMitGotoButton = false;

	private static final String ACTION_GOTO = "ACTION_GOTO";
	private static final String ACTION_SELECT = "ACTION_SELECT";

	private PanelQueryFLR panelQueryFLR = null;

	private Integer[] ausgeschlosseneIds = null;

	public static final String ARTIKEL_OHNE_ARBEISZEIT = "ARTIKEL_OHNE_ARBEISZEIT";
	public static final String LAGER = "LAGER";
	public static final String ARTIKELGRUPPE = "ARTIKELGRUPPE";
	public static final String ARTIKELKLASSE = "ARTIKELKLASSE";
	public static final String LAGERPLATZ = "LAGERPLATZ";
	public static final String VKPREISLISTE = "VKPREISLISTE";
	public static final String SPEISEKASSA = "SPEISEKASSA";
	public static final String KOSTENSTELLE = "KOSTENSTELLE";
	public static final String STUECKLISTE = "STUECKLISTE";
	public static final String PERSONAL = "PERSONAL";
	public static final String LAND = "LAND";
	public static final String HERSTELLER = "HERSTELLER";
	public static final String ZERTIFIKATART = "ZERTIFIKATART";
	public static final String LOHNART = "LOHNART";
	public static final String BRANCHE = "BRANCHE";
	public static final String PARTNERKLASSE = "PARTNERKLASSE";
	public static final String KUNDE = "KUNDE";
	public static final String LIEFERANT = "LIEFERANT";
	public static final String PARTNER = "PARTNER";
	public static final String STANDORT = "STANDORT";
	public static final String HALLE = "HALLE";
	public static final String ANLAGE = "ANLAGE";
	public static final String ISMASCHINE = "ISMASCHINE";
	public static final String ISKATEGORIE = "ISKATEGORIE";
	public static final String GERAETETYP = "GERAETETYP";
	public static final String PERSONALGRUPPE = "PERSONALGRUPPE";
	public static final String TAGESART = "TAGESART";
	public static final String LIEFERGRUPPE = "LIEFERGRUPPE";
	public static final String KOSTENTRAEGER = "KOSTENTRAEGER";
	public static final String GEWERK = "GEWERK";
	public static final String ARTIKELKOMMENTARART = "ARTIKELKOMMENTARART";
	public static final String BELEGART = "BELEGART";
	public static final String MASCHINE = "MASCHINE";
	public static final String PROJEKT = "PROJEKT";
	public static final String MAHNSTUFE = "MAHNSTUFE";
	public static final String LOSBEREICH = "LOSBEREICH";
	public static final String SHOPGRUPPE = "SHOPGRUPPE";
	public static final String SACHKONTO = "SACHKONTO";
	public static final String FINANZAMT = "FINANZAMT";
	public static final String BEREITSCHAFTSART = "BEREITSCHAFTSART";
	public static final String EINGANGSRECHNUNGEN = "EINGANGSRECHNUNGEN";
	public static final String FAHRZEUG = "FAHRZEUG";

	// Der Code muss zusaetzlich an 3 Stellen erweitert werden: in jbInit(), in
	// actionPerformed() und in changed()

	public WrapperSelectField(String sTyp, InternalFrame internalFrame,
			boolean bMitLeerenButton) {
		this.sTyp = sTyp;
		this.internalFrame = internalFrame;
		this.bMitLeerenButton = bMitLeerenButton;
		jbInit();
	}

	public WrapperButton getWrapperButton() {
		return wrapperButton;
	}

	public WrapperGotoButton getWrapperGotoButton() {
		return wrapperGotoButton;
	}

	public WrapperButton getWrapperButtonGoTo() {
		return buttonGoto;
	}

	public String getSTyp() {
		return sTyp;
	}

	public void setActionCommand(String command) {
		wrapperButton.setActionCommand(command);
	}

	public void addActionListener(ActionListener l) {
		wrapperButton.addActionListener(l);
	}

	public Object getOKey() {
		return oKey;
	}

	public Integer getIKey() {
		return (Integer) oKey;
	}

	public String getSKey() {
		return (String) oKey;
	}

	public void setKey(Object key) throws Throwable {
		this.oKey = key;

		if (wrapperGotoButton != null) {
			wrapperGotoButton.setOKey(key);
		}

		wrapperTextField.setText(null);

		internalFrame.fireItemChanged(this, ItemChangedEvent.GOTO_DETAIL_PANEL);

	}

	public void setEnabled(boolean bEnabled) {
		wrapperButton.setEnabled(bEnabled);

		if (oKey == null) {
			buttonGoto.setEnabled(false);
		} else {
			buttonGoto.setEnabled(!bEnabled);
		}
	}

	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		try {

			if (eI.getSource().equals(panelQueryFLR)
					|| eI.getSource().equals(this)) {

				if (!eI.getSource().equals(this)) {
					oKey = ((ISourceEvent) e.getSource()).getIdSelected();
				}

				if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL
						&& oKey != null) {
					buttonGoto.setEnabled(true);
					if (sTyp.equals(ARTIKEL_OHNE_ARBEISZEIT)) {

						ArtikelDto artikelDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.artikelFindByPrimaryKey(getIKey());

						artikelDto = DelegateFactory
								.getInstance()
								.getArtikelkommentarDelegate()
								.pruefeArtikel(artikelDto,
										internalFrame.getBelegartCNr(),
										internalFrame);
						wrapperTextField.setText(artikelDto
								.formatArtikelbezeichnung());
					} else if (sTyp.equals(LAGER)) {
						LagerDto lagerDto = DelegateFactory.getInstance()
								.getLagerDelegate()
								.lagerFindByPrimaryKey(getIKey());
						wrapperTextField.setText(lagerDto.getCNr());
					} else if (sTyp.equals(ARTIKELGRUPPE)) {
						ArtgruDto artgruDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.artgruFindByPrimaryKey(getIKey());
						wrapperTextField.setText(artgruDto.getCNr());
					} else if (sTyp.equals(ARTIKELKLASSE)) {
						ArtklaDto artklaDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.artklaFindByPrimaryKey(getIKey());
						wrapperTextField.setText(artklaDto.getCNr());
					} else if (sTyp.equals(LAGERPLATZ)) {
						LagerplatzDto lagerplatzDto = DelegateFactory
								.getInstance().getLagerDelegate()
								.lagerplatzFindByPrimaryKey(getIKey());
						wrapperTextField
								.setText(lagerplatzDto.getCLagerplatz());
					} else if (sTyp.equals(VKPREISLISTE)) {
						VkpfartikelpreislisteDto vkpfartikelpreislisteDto = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.vkpfartikelpreislisteFindByPrimaryKey(
										getIKey());
						wrapperTextField.setText(vkpfartikelpreislisteDto
								.getCNr());
					} else if (sTyp.equals(SPEISEKASSA)) {
						KassaartikelDto speisekassaDto = DelegateFactory
								.getInstance().getKuecheDelegate()
								.kassaartikelFindByPrimaryKey(getIKey());
						wrapperTextField.setText(speisekassaDto
								.getCArtikelnummerkassa()
								+ " "
								+ speisekassaDto.getCBez());
					} else if (sTyp.equals(KOSTENSTELLE)) {
						KostenstelleDto kostenstelleDto = DelegateFactory
								.getInstance().getSystemDelegate()
								.kostenstelleFindByPrimaryKey(getIKey());
						wrapperTextField.setText(kostenstelleDto.getCNr());
					} else if (sTyp.equals(STUECKLISTE)) {
						StuecklisteDto stuecklisteDto = DelegateFactory
								.getInstance().getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey(getIKey());
						wrapperTextField.setText(stuecklisteDto.getArtikelDto()
								.formatArtikelbezeichnung());
					} else if (sTyp.equals(PERSONAL)) {
						PersonalDto personalDto = DelegateFactory.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey(getIKey());
						wrapperTextField.setText(personalDto.formatAnrede());
					}

					else if (sTyp.equals(HERSTELLER)) {
						HerstellerDto hstDto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.herstellerFindBdPrimaryKey(getIKey());
						wrapperTextField.setText(hstDto.getCNr());
					}

					else if (sTyp.equals(LAND)) {

						LandDto landDto = DelegateFactory.getInstance()
								.getSystemDelegate()
								.landFindByPrimaryKey(getIKey());

						wrapperTextField.setText(landDto.getCLkz());
					} else if (sTyp.equals(ZERTIFIKATART)) {

						ZertifikatartDto landDto = DelegateFactory
								.getInstance().getAnfrageServiceDelegate()
								.zertifikatartFindByPrimaryKey(getIKey());

						wrapperTextField.setText(landDto.getCBez());
					} else if (sTyp.equals(LOHNART)) {

						LohnartDto landDto = DelegateFactory.getInstance()
								.getPersonalDelegate()
								.lohnartFindByPrimaryKey(getIKey());

						wrapperTextField.setText(landDto.getCBez());
					} else if (sTyp.equals(ARTIKELKOMMENTARART)) {

						ArtikelkommentarartDto artikelkommentarartDto = DelegateFactory
								.getInstance().getArtikelkommentarDelegate()
								.artikelkommentarartFindByPrimaryKey(getIKey());
						wrapperTextField.setText(artikelkommentarartDto
								.getCNr());
					} else if (sTyp.equals(BRANCHE)) {

						BrancheDto brancheDto = DelegateFactory.getInstance()
								.getPartnerDelegate()
								.brancheFindByPrimaryKey(getIKey());

						wrapperTextField.setText(brancheDto.getBezeichnung());
					} else if (sTyp.equals(PARTNERKLASSE)) {

						PartnerklasseDto partnerklasseDto = DelegateFactory
								.getInstance().getPartnerDelegate()
								.partnerklasseFindByPrimaryKey(getIKey());

						wrapperTextField.setText(partnerklasseDto
								.getBezeichnung());
					} else if (sTyp.equals(KUNDE)) {

						KundeDto kundeDto = DelegateFactory.getInstance()
								.getKundeDelegate()
								.kundeFindByPrimaryKey(getIKey());

						wrapperTextField.setText(kundeDto.getPartnerDto()
								.formatAnrede());
					} else if (sTyp.equals(LIEFERANT)) {

						LieferantDto lieferantDto = DelegateFactory
								.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(getIKey());

						wrapperTextField.setText(lieferantDto.getPartnerDto()
								.formatAnrede());
					} else if (sTyp.equals(PARTNER)) {

						PartnerDto partnerDto = DelegateFactory.getInstance()
								.getPartnerDelegate()
								.partnerFindByPrimaryKey(getIKey());

						wrapperTextField.setText(partnerDto.formatAnrede());
					} else if (sTyp.equals(STANDORT)) {

						StandortDto standortDto = DelegateFactory.getInstance()
								.getInstandhaltungDelegate()
								.standortFindByPrimaryKey(getIKey());

						PartnerDto partnerDto = DelegateFactory
								.getInstance()
								.getPartnerDelegate()
								.partnerFindByPrimaryKey(
										standortDto.getPartnerIId());

						wrapperTextField.setText(partnerDto.formatAnrede());
					} else if (sTyp.equals(HALLE)) {

						HalleDto halleDto = DelegateFactory.getInstance()
								.getInstandhaltungDelegate()
								.halleFindByPrimaryKey(getIKey());

						StandortDto standortDto = DelegateFactory
								.getInstance()
								.getInstandhaltungDelegate()
								.standortFindByPrimaryKey(
										halleDto.getStandortIId());

						PartnerDto partnerDto = DelegateFactory
								.getInstance()
								.getPartnerDelegate()
								.partnerFindByPrimaryKey(
										standortDto.getPartnerIId());

						wrapperTextField.setText(halleDto.getCBez() + " "
								+ partnerDto.formatAnrede());
					} else if (sTyp.equals(ANLAGE)) {
						AnlageDto anlageDto = DelegateFactory.getInstance()
								.getInstandhaltungDelegate()
								.anlageFindByPrimaryKey(getIKey());
						/*
						 * StandortDto standortDto = DelegateFactory
						 * .getInstance() .getInstandhaltungDelegate()
						 * .standortFindByPrimaryKey(
						 * halleDto.getStandortIId());
						 * 
						 * PartnerDto partnerDto = DelegateFactory
						 * .getInstance() .getPartnerDelegate()
						 * .partnerFindByPrimaryKey(
						 * standortDto.getPartnerIId());
						 */

						wrapperTextField.setText(anlageDto.getCBez());
					} else if (sTyp.equals(ISMASCHINE)) {
						IsmaschineDto ismaschineDto = DelegateFactory
								.getInstance().getInstandhaltungDelegate()
								.ismaschineFindByPrimaryKey(getIKey());

						/*
						 * HalleDto halleDto = DelegateFactory.getInstance()
						 * .getInstandhaltungDelegate()
						 * .halleFindByPrimaryKey(ismaschineDto.getHalleIId());
						 * 
						 * 
						 * StandortDto standortDto = DelegateFactory
						 * .getInstance() .getInstandhaltungDelegate()
						 * .standortFindByPrimaryKey(
						 * halleDto.getStandortIId());
						 * 
						 * PartnerDto partnerDto = DelegateFactory
						 * .getInstance() .getPartnerDelegate()
						 * .partnerFindByPrimaryKey(
						 * standortDto.getPartnerIId());
						 */

						wrapperTextField.setText(ismaschineDto.getCBez());
					} else if (sTyp.equals(GERAETETYP)) {
						GeraetetypDto geraetetypDto = DelegateFactory
								.getInstance().getInstandhaltungDelegate()
								.geraetetypFindByPrimaryKey(getIKey());

						wrapperTextField.setText(geraetetypDto.getCBez());
					} else if (sTyp.equals(PERSONALGRUPPE)) {
						PersonalgruppeDto personalgruppeDto = DelegateFactory
								.getInstance().getPersonalDelegate()
								.personalgruppeFindByPrimaryKey(getIKey());

						wrapperTextField.setText(personalgruppeDto.getCBez());
					} else if (sTyp.equals(TAGESART)) {
						TagesartDto tagesartDto = DelegateFactory.getInstance()
								.getZeiterfassungDelegate()
								.tagesartFindByPrimaryKey(getIKey());

						wrapperTextField.setText(tagesartDto.getBezeichnung());
					}else if (sTyp.equals(EINGANGSRECHNUNGEN)) {
						EingangsrechnungDto erDto = DelegateFactory.getInstance()
								.getEingangsrechnungDelegate()
								.eingangsrechnungFindByPrimaryKey(getIKey());

						wrapperTextField.setText(erDto.getCNr());
					} else if (sTyp.equals(BEREITSCHAFTSART)) {
						BereitschaftartDto tagesartDto = DelegateFactory
								.getInstance().getPersonalDelegate()
								.bereitschaftartFindByPrimaryKey(getIKey());
						wrapperTextField.setText(tagesartDto.getCBez());
					} else if (sTyp.equals(LIEFERGRUPPE)) {
						LfliefergruppeDto liefergruppeDto = DelegateFactory
								.getInstance().getLieferantServicesDelegate()
								.lfliefergruppeFindByPrimaryKey(getIKey());

						wrapperTextField.setText(liefergruppeDto
								.getBezeichnung());
					} else if (sTyp.equals(ISKATEGORIE)) {
						IskategorieDto iskategorieDto = DelegateFactory
								.getInstance().getInstandhaltungDelegate()
								.iskategorieFindByPrimaryKey(getIKey());

						wrapperTextField.setText(iskategorieDto.getCBez());
					} else if (sTyp.equals(KOSTENTRAEGER)) {
						KostentraegerDto kostentraegerDto = DelegateFactory
								.getInstance().getMandantDelegate()
								.kostentraegerFindByPrimaryKey(getIKey());
						internalFrame.letzteKostentraegerIId = kostentraegerDto
								.getIId();
						wrapperTextField.setText(kostentraegerDto.getCBez());
					} else if (sTyp.equals(GEWERK)) {
						GewerkDto gewerkDto = DelegateFactory.getInstance()
								.getInstandhaltungDelegate()
								.gewerkFindByPrimaryKey(getIKey());

						wrapperTextField.setText(gewerkDto.getCBez());
					} else if (sTyp.equals(MASCHINE)) {
						MaschineDto maschineDto = DelegateFactory.getInstance()
								.getZeiterfassungDelegate()
								.maschineFindByPrimaryKey(getIKey());

						wrapperTextField.setText(maschineDto.getBezeichnung());
					} else if (sTyp.equals(PROJEKT)) {
						ProjektDto projektDto = DelegateFactory.getInstance()
								.getProjektDelegate()
								.projektFindByPrimaryKey(getIKey());
						wrapperTextField.setText(DelegateFactory
								.getInstance()
								.getProjektServiceDelegate()
								.bereichFindByPrimaryKey(
										projektDto.getBereichIId()).getCBez()
								+ " " + projektDto.getCNr());
					} else if (sTyp.equals(MAHNSTUFE)) {

						wrapperTextField.setText(oKey.toString());
					} else if (sTyp.equals(LOSBEREICH)) {

						LosbereichDto losbereichDto = DelegateFactory
								.getInstance().getFertigungServiceDelegate()
								.losbereichFindByPrimaryKey(getIKey());
						wrapperTextField.setText(losbereichDto.getIId() + " "
								+ losbereichDto.getCBez());
					} else if (sTyp.equals(SHOPGRUPPE)) {

						ShopgruppeDto dto = DelegateFactory.getInstance()
								.getArtikelDelegate()
								.shopgruppeFindByPrimaryKey(getIKey());
						wrapperTextField.setText(dto.getBezeichnung());
					} else if (sTyp.equals(SACHKONTO)) {

						KontoDto dto = DelegateFactory.getInstance()
								.getFinanzDelegate()
								.kontoFindByPrimaryKey(getIKey());
						wrapperTextField.setText(dto.getKontonrBezeichnung());
					} else if (sTyp.equals(FINANZAMT)) {

						FinanzamtDto dto = DelegateFactory
								.getInstance()
								.getFinanzDelegate()
								.finanzamtFindByPrimaryKey(getIKey(),
										LPMain.getTheClient().getMandant());
						wrapperTextField.setText(dto.getPartnerDto()
								.formatFixTitelName1Name2());
					} else if (sTyp.equals(FAHRZEUG)) {

						FahrzeugDto dto = DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.fahrzeugFindByPrimaryKey(getIKey());
						wrapperTextField.setText(dto.getCBez());
					} else if (sTyp.equals(BELEGART)) {

						wrapperTextField.setText(getOKey() + "");
					}

				} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
					if (e.getSource() == panelQueryFLR) {
						wrapperTextField.setText(null);
						oKey = null;
					}
				}
			}

		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(internalFrame, t);
		}

	}

	public PanelQueryFLR getPanelQueryFLR() {
		return panelQueryFLR;
	}

	public void setText(String sText) {
		wrapperButton.setText(sText);
		wrapperGotoButton.setText(sText);
	}

	public void setToolTipText(String sText) {
		wrapperButton.setToolTipText(sText);
		wrapperGotoButton.setToolTipText(sText);
	}

	public void setActivatable(boolean isActivatable) {
		wrapperButton.setActivatable(isActivatable);
	}

	public boolean isActivatable() {
		return wrapperButton.isActivatable();
	}

	public void setMandatoryField(boolean isMandatoryField) {
		wrapperTextField.setMandatoryField(isMandatoryField);
	}

	public boolean isMandatoryField() {
		return wrapperTextField.isMandatoryField();
	}

	public void setSTyp(String sTyp) {
		this.sTyp = sTyp;
	}

	public void removeContent() {
		oKey = null;
	}

	public WrapperTextField getWrapperTextField() {
		return wrapperTextField;
	}

	private void jbInit() {
		wrapperButton = new WrapperButton();
		buttonGoto = new WrapperButton();

		buttonGoto.setIcon(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/data_into.png")));
		buttonGoto.setActionCommand(ACTION_GOTO);
		buttonGoto.addActionListener(this);

		wrapperButton.setActionCommand(ACTION_SELECT);
		wrapperButton.addActionListener(this);

		wrapperGotoButton = new WrapperGotoButton(-1);
		wrapperGotoButton.setActionCommand(ACTION_SELECT);
		wrapperGotoButton.addActionListener(this);

		wrapperTextField = new WrapperTextField();
		wrapperTextField.setActivatable(false);
		wrapperTextField.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		
		wrapperTextField.setZugehoerigesSelectField(this);
		
		internalFrame.addItemChangedListener(this);

		if (sTyp.equals(ARTIKEL_OHNE_ARBEISZEIT)) {
			bMitGotoButton = true;
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.artikel"));
			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("button.artikel"));
			wrapperGotoButton
					.setWhereToGo(WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);

		} else if (sTyp.equals(LAGER)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.lager"));
			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("button.lager"));
		} else if (sTyp.equals(ARTIKELGRUPPE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.artikelgruppe"));
		} else if (sTyp.equals(ARTIKELKLASSE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.artikelklasse"));
		} else if (sTyp.equals(LAGERPLATZ)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.lagerplatz"));
		} else if (sTyp.equals(VKPREISLISTE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("artikel.report.preisliste"));
		} else if (sTyp.equals(SPEISEKASSA)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("kue.kassaartikel") + "...");
		} else if (sTyp.equals(KOSTENSTELLE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("label.kostenstelle"));
		} else if (sTyp.equals(STUECKLISTE)) {
			bMitGotoButton = true;
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.stueckliste"));

			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("button.stueckliste"));
			wrapperGotoButton
					.setWhereToGo(WrapperGotoButton.GOTO_STUECKLISTE_AUSWAHL);

		} else if (sTyp.equals(PERSONAL)) {
			wrapperButton
					.setText(LPMain.getTextRespectUISPr("button.personal"));

		} else if (sTyp.equals(LAND)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.land"));
		} else if (sTyp.equals(HERSTELLER)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.hersteller")
					+ "...");
		} else if (sTyp.equals(ZERTIFIKATART)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("anf.zertifikatart") + "...");
		} else if (sTyp.equals(LOHNART)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("pers.lohnart")
					+ "...");
		} else if (sTyp.equals(BRANCHE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.branche"));
		} else if (sTyp.equals(PARTNERKLASSE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.partnerklasse"));
		} else if (sTyp.equals(KUNDE)) {
			bMitGotoButton = true;
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.kunde"));

			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("button.kunde"));
			wrapperGotoButton
					.setWhereToGo(WrapperGotoButton.GOTO_KUNDE_AUSWAHL);

		} else if (sTyp.equals(LIEFERANT)) {
			bMitGotoButton = true;
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.lieferant"));

			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("button.lieferant"));
			wrapperGotoButton
					.setWhereToGo(WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);

		} else if (sTyp.equals(PARTNER)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.partner"));

			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("button.partner"));
			wrapperGotoButton
					.setWhereToGo(WrapperGotoButton.GOTO_PARTNER_AUSWAHL);

		} else if (sTyp.equals(STANDORT)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.standort")
					+ "...");
		} else if (sTyp.equals(HALLE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.halle")
					+ "...");
		} else if (sTyp.equals(ANLAGE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.anlage")
					+ "...");
		} else if (sTyp.equals(ISMASCHINE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.maschine")
					+ "...");
		} else if (sTyp.equals(GERAETETYP)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.geraetetyp")
					+ "...");
		} else if (sTyp.equals(PERSONALGRUPPE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("pers.personalgruppe") + "...");
		} else if (sTyp.equals(TAGESART)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.tagesart")
					+ "...");
		} else if (sTyp.equals(EINGANGSRECHNUNGEN)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("button.eingangsrechnung"));
		} else if (sTyp.equals(BEREITSCHAFTSART)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("pers.bereitschaftsart") + "...");
		} else if (sTyp.equals(LIEFERGRUPPE)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("part.liefergruppe.flr"));
		} else if (sTyp.equals(ISKATEGORIE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.kategorie")
					+ "...");
		} else if (sTyp.equals(KOSTENTRAEGER)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("lp.kostentraeger") + "...");
		} else if (sTyp.equals(GEWERK)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("is.gewerk")
					+ "...");
		} else if (sTyp.equals(ARTIKELKOMMENTARART)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.kommentarart")
					+ "...");
		} else if (sTyp.equals(BELEGART)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("system.dokumentenlink.belegart")
					+ "...");
		} else if (sTyp.equals(MASCHINE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.maschine")
					+ "...");
		} else if (sTyp.equals(PROJEKT)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("label.projekt")
					+ "...");
			wrapperGotoButton.setText(LPMain
					.getTextRespectUISPr("label.projekt") + "...");
			wrapperGotoButton
					.setWhereToGo(WrapperGotoButton.GOTO_PROJEKT_AUSWAHL);
		} else if (sTyp.equals(MAHNSTUFE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.mahnstufe")
					+ "...");

		} else if (sTyp.equals(LOSBEREICH)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("fert.bereich")
					+ "...");

		} else if (sTyp.equals(SHOPGRUPPE)) {
			wrapperButton.setText(LPMain.getTextRespectUISPr("lp.shopgruppe")
					+ "...");

		} else if (sTyp.equals(SACHKONTO)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.sachkonto"));

		} else if (sTyp.equals(FINANZAMT)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("button.finanzamt"));

		}else if (sTyp.equals(FAHRZEUG)) {
			wrapperButton.setText(LPMain
					.getTextRespectUISPr("pers.fahrzeug")+"...");

		}

		this.setLayout(new GridBagLayout());

		this.add(wrapperButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		if (bMitGotoButton) {
			this.add(buttonGoto, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 10, 0));
		}

		if (sTyp != null)
			setToken(sTyp.toLowerCase());
	}

	public void setAusgeschlosseneIds(Integer[] ausgeschlosseneIds) {
		this.ausgeschlosseneIds = ausgeschlosseneIds;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_GOTO)) {
			if (oKey != null) {
				// UND hier einsprungspunkt einbauen
				try {
					// gotobutton: 2 Fuer die Konstante ein neues Ziel
					// definieren
					if (sTyp.equals(ARTIKEL_OHNE_ARBEISZEIT)) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_ARTIKEL)) {
							InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_ARTIKEL);
							ifArtikel
									.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
											TabbedPaneArtikel.IDX_PANEL_AUSWAHL,
											oKey,
											null,
											ArtikelFilterFactory.getInstance()
													.createFKArtikellisteKey(
															(Integer) oKey));
						}
					}
					if (sTyp.equals(STUECKLISTE)) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_STUECKLISTE)) {
							InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_STUECKLISTE);
							ifStueckliste
									.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
											TabbedPaneStueckliste.IDX_PANEL_AUSWAHL,
											oKey,
											null,
											StuecklisteFilterFactory
													.getInstance()
													.createFKStuecklisteKey(
															(Integer) oKey));
						}
					}
					if (sTyp == KUNDE) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_KUNDE)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_KUNDE);
							ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE,
									TabbedPaneKunde.IDX_PANE_KUNDE, oKey, null,
									PartnerFilterFactory.getInstance()
											.createFKPartnerKey((Integer) oKey));
						}
					}
					if (sTyp == LIEFERANT) {
						if (LPMain
								.getInstance()
								.getDesktop()
								.darfAnwenderAufModulZugreifen(
										LocaleFac.BELEGART_LIEFERANT)) {
							InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
									.getInstance().getDesktop()
									.holeModul(LocaleFac.BELEGART_LIEFERANT);
							ifKunde.geheZu(
									InternalFrameLieferant.IDX_PANE_LIEFERANT,
									InternalFrameLieferant.IDX_PANE_LIEFERANT,
									oKey, null,
									PartnerFilterFactory.getInstance()
											.createFKPartnerKey((Integer) oKey));
						}
					}
				} catch (Throwable t) {
					LPMain.getInstance().exitClientNowErrorDlg(t);
				}
			}
		} else if (e.getActionCommand().equals(ACTION_SELECT)) {
			try {

				if (sTyp.equals(ARTIKEL_OHNE_ARBEISZEIT)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikelOhneArbeitszeit(
									internalFrame, (Integer) oKey,
									bMitLeerenButton);
				} else if (sTyp.equals(LAGER)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRLager(internalFrame, (Integer) oKey,
									bMitLeerenButton, false);
				} else if (sTyp.equals(ARTIKELGRUPPE)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikelgruppe(internalFrame,
									getIKey());
				} else if (sTyp.equals(ARTIKELKLASSE)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikelklasse(internalFrame,
									getIKey());
				} else if (sTyp.equals(LAGERPLATZ)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRLagerplatz(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(VKPREISLISTE)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRPreisliste(internalFrame, getIKey());
				} else if (sTyp.equals(SPEISEKASSA)) {
					panelQueryFLR = KuecheFilterFactory.getInstance()
							.createPanelFLRKassaartikel(internalFrame,
									getIKey());
				} else if (sTyp.equals(KOSTENSTELLE)) {
					panelQueryFLR = SystemFilterFactory.getInstance()
							.createPanelFLRKostenstelle(internalFrame, false,
									false);
				} else if (sTyp.equals(STUECKLISTE)) {
					panelQueryFLR = StuecklisteFilterFactory.getInstance()
							.createPanelFLRStueckliste(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(PERSONAL)) {
					panelQueryFLR = PersonalFilterFactory.getInstance()
							.createPanelFLRPersonal(internalFrame, true,
									bMitLeerenButton, getIKey());
				} else if (sTyp.equals(LAND)) {
					panelQueryFLR = SystemFilterFactory.getInstance()
							.createPanelFLRLand(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(HERSTELLER)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRHersteller(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(ZERTIFIKATART)) {
					panelQueryFLR = AnfrageFilterFactory.getInstance()
							.createPanelFLRZertifikatart(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(LOHNART)) {
					panelQueryFLR = PersonalFilterFactory.getInstance()
							.createPanelFLRLohnart(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(BRANCHE)) {
					panelQueryFLR = PartnerFilterFactory.getInstance()
							.createPanelFLRBranche(internalFrame, false,
									bMitLeerenButton, getIKey());
				} else if (sTyp.equals(PARTNERKLASSE)) {
					panelQueryFLR = PartnerFilterFactory.getInstance()
							.createPanelFLRPartnerPartnerklassen(internalFrame,
									false, bMitLeerenButton, getIKey());
				} else if (sTyp.equals(KUNDE)) {
					panelQueryFLR = PartnerFilterFactory.getInstance()
							.createPanelFLRKunde(internalFrame, false,
									bMitLeerenButton, getIKey());
				} else if (sTyp.equals(LIEFERANT)) {
					panelQueryFLR = PartnerFilterFactory.getInstance()
							.createPanelFLRLieferantGoto(internalFrame,
									getIKey(), false, bMitLeerenButton);
				} else if (sTyp.equals(PARTNER)) {
					panelQueryFLR = PartnerFilterFactory.getInstance()
							.createPanelFLRPartner(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(FINANZAMT)) {
					panelQueryFLR = FinanzFilterFactory.getInstance()
							.createPanelFLRFinanzamt(internalFrame, getIKey(),
									bMitLeerenButton);

				} else if (sTyp.equals(STANDORT)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRStandort(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(HALLE)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRHalle(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(ANLAGE)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRAnlage(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(ISMASCHINE)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRIsmaschine(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(GERAETETYP)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRGeraetetyp(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(PERSONALGRUPPE)) {
					panelQueryFLR = PersonalFilterFactory.getInstance()
							.createPanelFLRPersonalgruppe(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(TAGESART)) {
					panelQueryFLR = PersonalFilterFactory.getInstance()
							.createPanelFLRTagesart(internalFrame, getIKey(),
									bMitLeerenButton);
				}  else if (sTyp.equals(EINGANGSRECHNUNGEN)) {
					panelQueryFLR = EingangsrechnungFilterFactory.getInstance()
							.createPanelFLREingangsrechnungGoto(internalFrame, getIKey(), null, true, bMitLeerenButton);
				} else if (sTyp.equals(BEREITSCHAFTSART)) {
					panelQueryFLR = ZeiterfassungFilterFactory.getInstance()
							.createPanelFLRBereitschaftsart(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(LIEFERGRUPPE)) {
					panelQueryFLR = PartnerFilterFactory.getInstance()
							.createPanelFLRLiefergruppe(internalFrame, false,
									bMitLeerenButton);
				} else if (sTyp.equals(ISKATEGORIE)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRIskategorie(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(GEWERK)) {
					panelQueryFLR = InstandhaltungFilterFactory.getInstance()
							.createPanelFLRGewerk(internalFrame, getIKey(),
									bMitLeerenButton);
				} else if (sTyp.equals(KOSTENTRAEGER)) {
					panelQueryFLR = SystemFilterFactory.getInstance()
							.createPanelFLRKostentraeger(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(ARTIKELKOMMENTARART)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikelkommentarart(internalFrame,
									getIKey(), bMitLeerenButton);
				} else if (sTyp.equals(BELEGART)) {
					panelQueryFLR = SystemFilterFactory.getInstance()
							.createPanelFLRBelegart(internalFrame,
									(String) getOKey());
				} else if (sTyp.equals(MASCHINE)) {
					panelQueryFLR = ZeiterfassungFilterFactory.getInstance()
							.createPanelFLRMaschinen(internalFrame,
									(Integer) getOKey());
				} else if (sTyp.equals(PROJEKT)) {
					panelQueryFLR = ProjektFilterFactory.getInstance()
							.createPanelFLRProjekt(internalFrame,
									(Integer) getOKey(), bMitLeerenButton);
				} else if (sTyp.equals(MAHNSTUFE)) {
					panelQueryFLR = SystemFilterFactory.getInstance()
							.createPanelFLRMahnstufe(internalFrame,
									(Integer) getOKey(), bMitLeerenButton);
				} else if (sTyp.equals(SHOPGRUPPE)) {
					panelQueryFLR = ArtikelFilterFactory.getInstance()
							.createPanelFLRShopgruppe(internalFrame,
									(Integer) getOKey(), bMitLeerenButton,
									ausgeschlosseneIds);
				} else if (sTyp.equals(SACHKONTO)) {
					panelQueryFLR = FinanzFilterFactory.getInstance()
							.createPanelFLRSachKonto(internalFrame,
									(Integer) getOKey(), bMitLeerenButton);
				} else if (sTyp.equals(LOSBEREICH)) {
					panelQueryFLR = FertigungFilterFactory.getInstance()
							.createPanelFLRLosbereich(internalFrame,
									(Integer) getOKey(), bMitLeerenButton);
				}else if (sTyp.equals(FAHRZEUG)) {
					panelQueryFLR = PersonalFilterFactory.getInstance()
							.createPanelFLRFahrzeug(internalFrame,
									(Integer) getOKey(), bMitLeerenButton);
				}

				new DialogQuery(panelQueryFLR);
			} catch (Throwable ex) {
				internalFrame.handleException(ex, true);
			}
		}
	}

	public boolean requestFocusInWindow() {
		return getWrapperButton().requestFocusInWindow();
	}

	public void setRechtCNr(String rechtCNr) {
		getWrapperButton().setRechtCNr(rechtCNr);

	}

	public void setMnemonic(int toSet) {
		wrapperButton.setMnemonic(toSet);
	}

	public void setMnemonic(char toSet) {
		wrapperButton.setMnemonic(toSet);
	}

	@Override
	public void setToken(String token) {
		wrapperButton.setToken(token);
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(this);
	}

	@Override
	public void removeCib() {
		wrapperButton.removeCib();
	}
	@Override
	public String getToken() {
		return wrapperButton.getToken();
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		return wrapperTextField.hasContent();
	}
}
