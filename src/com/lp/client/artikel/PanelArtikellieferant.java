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
package com.lp.client.artikel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperURLField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

public class PanelArtikellieferant extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArtikellieferantDto artikellieferantDto = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private JPanel jpaWorkingOn;
	private JPanel jpaButtonAction = null;
	private WrapperGotoButton wbuLieferant = new WrapperGotoButton(
			WrapperGotoButton.GOTO_LIEFERANT_AUSWAHL);
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperCheckBox wcbHerstellerbezeichnung = new WrapperCheckBox();
	private WrapperCheckBox wcbWebshop = new WrapperCheckBox();

	private WrapperComboBox wcoEinheitVpe = new WrapperComboBox();

	private WrapperLabel wlaArtikelnummerbeilieferant = new WrapperLabel();
	private WrapperLabel wlaEinzelpreis = new WrapperLabel();
	private WrapperLabel wlaNettopreis = new WrapperLabel();
	private WrapperLabel wlaMindestbestellmenge = new WrapperLabel();
	private WrapperLabel wlaFixkosten = new WrapperLabel();
	private WrapperLabel wlaGueltigab = new WrapperLabel();
	private WrapperLabel wlaWaehrungEinzelpreis = new WrapperLabel();
	private WrapperLabel wlaStandardmenge = new WrapperLabel();
	private WrapperLabel wlaVerpackungseinheit = new WrapperLabel();
	private WrapperLabel wlaRabattgruppe = new WrapperLabel();
	private WrapperNumberField wnfRabatt = new WrapperNumberField();
	private WrapperNumberField wnfStandardmenge = new WrapperNumberField();
	private WrapperNumberField wnfVerpackungseinheit = new WrapperNumberField();
	private WrapperTextField wtfRabattgruppe = new WrapperTextField();
	private WrapperDateField wdfgueltigab = new WrapperDateField();
	private WrapperNumberField wnfFixkosten = new WrapperNumberField();
	private WrapperNumberField wnfMindestbestellmenge = new WrapperNumberField();
	private WrapperNumberField wnfNettopreis = new WrapperNumberField();
	private WrapperNumberField wnfEinzelpreis = new WrapperNumberField();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRLieferant = null;
	static final public String ACTION_SPECIAL_LIEFERANT_FROM_LISTE = "action_lieferant_from_liste";
	private WrapperLabel wlaBezbeuilieferant = new WrapperLabel();
	private WrapperTextField wtfBezbeilieferant = new WrapperTextField();
	private WrapperLabel wlaRabatt = new WrapperLabel();
	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperLabel wlaWaehrungNettopreis = new WrapperLabel();
	private WrapperLabel wlaEinheitMindestbestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitStandardmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitVerpackungseinheit = new WrapperLabel();
	private WrapperLabel wlaEinheitFixkosten = new WrapperLabel();
	protected WrapperURLField wtfWeblink = new WrapperURLField();

	private WrapperLabel wlaWiederbeschaffungszeit = new WrapperLabel();
	private WrapperNumberField wnfWiederbeschaffungszeit = new WrapperNumberField();
	private WrapperLabel wlaWiederbeschaffungszeitEinheit = new WrapperLabel();

	private WrapperLabel wlaEinheitBestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitArtikelmenge = new WrapperLabel();
	private WrapperNumberField wnfEinzelpreisBestellmenge = new WrapperNumberField();
	private WrapperNumberField wnfNettopreisBestellmenge = new WrapperNumberField();

	private WrapperLabel wlaMaterialzuschlag = new WrapperLabel();
	private WrapperLabel wlaArtikelMaterial = new WrapperLabel();
	private WrapperNumberField wnfZuschlag = new WrapperNumberField();
	private WrapperNumberField wnfEKPReis = new WrapperNumberField();

	private WrapperRadioButton wrbRabatt = new WrapperRadioButton();
	private WrapperRadioButton wrbNettopreis = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperLabel wlaGueltigbis = new WrapperLabel();
	private WrapperDateField wdfgueltigbis = new WrapperDateField();

	private WrapperLabel wlaAngebotsnummer = new WrapperLabel();
	private WrapperTextField wtfAngebotsnummer = new WrapperTextField();

	private WrapperSelectField wsfZertifikatart = null;

	public PanelArtikellieferant(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfArtikelnummer;
	}

	void dialogQueryPartnerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(getInternalFrame(),
						artikellieferantDto.getLieferantIId(), true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	protected void dto2Components() throws Throwable {
		wcbHerstellerbezeichnung.setShort(artikellieferantDto
				.getBHerstellerbez());
		wcbWebshop.setShort(artikellieferantDto.getBWebshop());
		wdfgueltigab.setTimestamp(artikellieferantDto.getTPreisgueltigab());
		wdfgueltigbis.setTimestamp(artikellieferantDto.getTPreisgueltigbis());
		wnfMindestbestellmenge.setDouble(artikellieferantDto
				.getFMindestbestelmenge());

		wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());
		wnfFixkosten.setBigDecimal(artikellieferantDto.getNFixkosten());
		wnfNettopreis.setBigDecimal(artikellieferantDto.getNNettopreis());

		// PJ 2496
		wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());
		setTooltipMandantenwaehrung(wnfEinzelpreis, artikellieferantDto
				.getLieferantDto().getWaehrungCNr());

		wnfFixkosten.setBigDecimal(artikellieferantDto.getNFixkosten());
		setTooltipMandantenwaehrung(wnfFixkosten, artikellieferantDto
				.getLieferantDto().getWaehrungCNr());

		wnfNettopreis.setBigDecimal(artikellieferantDto.getNNettopreis());
		setTooltipMandantenwaehrung(wnfNettopreis, artikellieferantDto
				.getLieferantDto().getWaehrungCNr());
		wtfWeblink.setText(artikellieferantDto.getCWeblink());

		wnfRabatt.setDouble(artikellieferantDto.getFRabatt());
		wnfStandardmenge.setDouble(artikellieferantDto.getFStandardmenge());

		wtfArtikelnummer.setText(artikellieferantDto.getCArtikelnrlieferant());
		wtfAngebotsnummer.setText(artikellieferantDto.getCAngebotnummer());
		wtfBezbeilieferant.setText(artikellieferantDto.getCBezbeilieferant());
		wbuLieferant.setOKey(artikellieferantDto.getLieferantDto().getIId());
		wtfLieferant.setText(artikellieferantDto.getLieferantDto()
				.getPartnerDto().formatTitelAnrede());

		wlaWaehrungEinzelpreis.setText(artikellieferantDto.getLieferantDto()
				.getWaehrungCNr().trim());
		wlaWaehrungNettopreis.setText(artikellieferantDto.getLieferantDto()
				.getWaehrungCNr().trim());
		wlaEinheitFixkosten.setText(artikellieferantDto.getLieferantDto()
				.getWaehrungCNr().trim());
		wcoEinheitVpe.setKeyOfSelectedItem(artikellieferantDto
				.getEinheitCNrVpe());

		if (Helper.short2boolean(artikellieferantDto.getBRabattbehalten()) == true) {
			wrbRabatt.setSelected(true);
		} else {
			wrbNettopreis.setSelected(true);
		}

		wtfRabattgruppe.setText(artikellieferantDto.getCRabattgruppe());
		wnfVerpackungseinheit.setBigDecimal(artikellieferantDto
				.getNVerpackungseinheit());
		wnfWiederbeschaffungszeit.setInteger(artikellieferantDto
				.getIWiederbeschaffungszeit());
		if (wnfEinzelpreis.getDouble() == null) {
			wnfRabatt.setDouble(null);
			wnfNettopreis.setDouble(null);
			wnfRabatt.setMandatoryField(false);
			wnfNettopreis.setMandatoryField(false);

		} else {
			wnfRabatt.setMandatoryField(true);
			wnfNettopreis.setMandatoryField(true);

		}

		wsfZertifikatart.setKey(artikellieferantDto.getZertifikatartIId());

		this.setStatusbarPersonalIIdAendern(artikellieferantDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(artikellieferantDto.getTAendern());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LieferantDto lieferantDto = DelegateFactory.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey((Integer) key);
				wbuLieferant.setOKey(lieferantDto.getIId());
				wtfLieferant.setText(lieferantDto.getPartnerDto()
						.formatTitelAnrede());

				wlaWaehrungEinzelpreis.setText(lieferantDto.getWaehrungCNr()
						.trim());
				wlaWaehrungNettopreis.setText(lieferantDto.getWaehrungCNr()
						.trim());
				wlaEinheitFixkosten.setText(lieferantDto.getWaehrungCNr()
						.trim());

				artikellieferantDto.setLieferantIId(lieferantDto.getIId());
				artikellieferantDto.setLieferantDto(lieferantDto);
			}
		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wbuLieferant
				.setText(LPMain.getTextRespectUISPr("lp.lieferant") + "...");
		wbuLieferant
				.setActionCommand(PanelArtikellieferant.ACTION_SPECIAL_LIEFERANT_FROM_LISTE);
		wbuLieferant.addActionListener(this);
		wtfLieferant.setText("");
		wtfLieferant.setActivatable(false);
		wtfLieferant.setMandatoryField(true);
		wtfLieferant.setColumnsMax(500);
		wcbHerstellerbezeichnung.setText(LPMain
				.getTextRespectUISPr("artikel.herstellerbezeichnung"));
		wcbWebshop.setText(LPMain.getTextRespectUISPr("lp.webshop"));
		wlaArtikelnummerbeilieferant.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummerdeslieferanten"));
		wlaEinzelpreis.setText(LPMain.getTextRespectUISPr("label.einzelpreis"));
		wlaNettopreis.setText(LPMain.getTextRespectUISPr("label.nettopreis"));
		wlaMindestbestellmenge.setText(LPMain
				.getTextRespectUISPr("artikel.mindestbestellmenge"));
		wlaWiederbeschaffungszeit
				.setText(LPMain
						.getTextRespectUISPr("artikel.artikellieferant.wiederbeschaffungszeit"));

		wcoEinheitVpe.setMap(DelegateFactory.getInstance().getSystemDelegate()
				.getAllEinheiten());

		wcoEinheitVpe
				.addActionListener(new PanelArtikellieferant_wcoEinheitVpe_actionAdapter(
						this));

		wsfZertifikatart = new WrapperSelectField(
				WrapperSelectField.ZERTIFIKATART, getInternalFrame(), true);

		buttonGroup.add(wrbRabatt);
		buttonGroup.add(wrbNettopreis);

		wrbRabatt.setSelected(true);

		wrbRabatt.addActionListener(this);
		wrbNettopreis.addActionListener(this);

		wlaWiederbeschaffungszeitEinheit
				.setHorizontalAlignment(SwingConstants.LEFT);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				wlaWiederbeschaffungszeitEinheit.setText(LPMain
						.getTextRespectUISPr("lp.kw"));
			} else if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				wlaWiederbeschaffungszeitEinheit.setText(LPMain
						.getTextRespectUISPr("lp.tage"));
			} else {
				wlaWiederbeschaffungszeitEinheit.setText("?");
			}
		}

		wlaMaterialzuschlag.setText(LPMain
				.getTextRespectUISPr("label.zuschlag"));

		wlaFixkosten.setText(LPMain.getTextRespectUISPr("lp.fixkosten"));
		wlaGueltigab.setText(LPMain.getTextRespectUISPr("lp.gueltigab"));
		wlaAngebotsnummer.setText(LPMain
				.getTextRespectUISPr("anf.angebotnummer"));

		wlaGueltigbis.setText(LPMain.getTextRespectUISPr("lp.gueltigbis"));
		wlaWaehrungEinzelpreis.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungEinzelpreis.setText("");
		wlaStandardmenge.setText(LPMain
				.getTextRespectUISPr("artikel.standardmenge"));
		wlaVerpackungseinheit.setText(LPMain
				.getTextRespectUISPr("artikel.verpackungseinheit"));
		wlaRabattgruppe.setText(LPMain.getTextRespectUISPr("lp.rabattgruppe"));
		wtfRabattgruppe.setColumnsMax(5);
		wtfRabattgruppe.setText("");

		wtfWeblink.setColumnsMax(300);

		wtfArtikelnummer.setSelectionStart(17);
		wtfArtikelnummer
				.setColumnsMax(ArtikelFac.MAX_ARTIKELLIEFERANT_ARTIKELNUMMERBEILIEFERANT);
		wtfArtikelnummer.setText("");
		wlaBezbeuilieferant.setText(LPMain
				.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezbeilieferant
				.setColumnsMax(ArtikelFac.MAX_ARTIKELLIEFERANT_BEZEICHNUNGBEILIEFERANT);
		wtfBezbeilieferant.setText("");
		wlaRabatt.setText(LPMain.getTextRespectUISPr("label.rabattsumme"));
		wlaProzent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent.setText("%");
		wlaWaehrungNettopreis.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungNettopreis.setText("");
		wlaEinheitMindestbestellmenge
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitMindestbestellmenge.setText("");
		wlaEinheitStandardmenge.setToolTipText("");
		wlaEinheitStandardmenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitStandardmenge.setText("");
		wlaEinheitFixkosten.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitFixkosten.setText("");
		wnfNettopreis.setDependenceField(true);
		wnfWiederbeschaffungszeit.setFractionDigits(0);
		wnfWiederbeschaffungszeit.setMinimumValue(0);
		wlaEinheitMindestbestellmenge.setText(internalFrameArtikel
				.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitStandardmenge.setText(internalFrameArtikel.getArtikelDto()
				.getEinheitCNr().trim());
		wlaEinheitVerpackungseinheit.setText(internalFrameArtikel
				.getArtikelDto().getEinheitCNr().trim());

		wlaEinheitArtikelmenge.setHorizontalAlignment(SwingConstants.CENTER);
		wlaEinheitBestellmenge.setHorizontalAlignment(SwingConstants.CENTER);

		wlaMaterialzuschlag.setHorizontalAlignment(SwingConstants.CENTER);
		wlaArtikelMaterial.setHorizontalAlignment(SwingConstants.CENTER);

		wnfNettopreis
				.addFocusListener(new PanelArtikellieferant_wnfNettopreis_focusAdapter(
						this));
		wnfRabatt.setDependenceField(true);

		wnfRabatt
				.addFocusListener(new PanelArtikellieferant_wnfRabatt_focusAdapter(
						this));
		wdfgueltigab.setMandatoryField(true);
		wnfEinzelpreis
				.addFocusListener(new PanelArtikellieferant_wnfEinzelpreis_focusAdapter(
						this));
		wnfEinzelpreisBestellmenge
				.addFocusListener(new PanelArtikellieferant_wnfEinzelpreisBestelleinheit_focusAdapter(
						this));
		wnfNettopreisBestellmenge
				.addFocusListener(new PanelArtikellieferant_wnfNettopreisBestelleinheit_focusAdapter(
						this));

		// Projekt 2619 Nachkommastellen
		int iNachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseEK();
		wnfEinzelpreis.setFractionDigits(iNachkommastellen);
		wnfNettopreis.setFractionDigits(iNachkommastellen);
		wnfEinzelpreisBestellmenge.setFractionDigits(iNachkommastellen);
		wnfNettopreisBestellmenge.setFractionDigits(iNachkommastellen);

		wnfFixkosten.setFractionDigits(iNachkommastellen);

		wnfZuschlag.setFractionDigits(6);
		wnfZuschlag.setActivatable(false);
		wnfEKPReis.setFractionDigits(iNachkommastellen);
		wnfEKPReis.setActivatable(false);

		jpaWorkingOn = new JPanel(new MigLayout("wrap 10", "[fill, 15%|fill,10%|fill,5%|fill,2%|fill,10%|fill,10%|fill,17%|fill, 8%|5%, fill, 0:0:5%|fill,10%]"));
		
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wbuLieferant);
		jpaWorkingOn.add(wtfLieferant, "span 3");
		jpaWorkingOn.add(wtfWeblink, "span");

		jpaWorkingOn.add(wlaArtikelnummerbeilieferant);
		jpaWorkingOn.add(wtfArtikelnummer, "span 3");
		jpaWorkingOn.add(wcbHerstellerbezeichnung, "span 3");
		jpaWorkingOn.add(wcbWebshop, "span");
		
		jpaWorkingOn.add(wlaBezbeuilieferant);
		jpaWorkingOn.add(wtfBezbeilieferant, "span");

		jpaWorkingOn.add(wlaEinheitArtikelmenge, "skip");
		jpaWorkingOn.add(wlaEinheitBestellmenge, "skip 2");
		jpaWorkingOn.add(wlaMaterialzuschlag, "wrap");
		
		jpaWorkingOn.add(wlaEinzelpreis);
		jpaWorkingOn.add(wnfEinzelpreis);
		jpaWorkingOn.add(wlaWaehrungEinzelpreis, "span 2");
		jpaWorkingOn.add(wnfEinzelpreisBestellmenge);
		jpaWorkingOn.add(wlaArtikelMaterial);
		jpaWorkingOn.add(wlaStandardmenge);
		jpaWorkingOn.add(wnfStandardmenge, "span 2");
		jpaWorkingOn.add(wlaEinheitStandardmenge);
		
		jpaWorkingOn.add(wlaRabatt);
		jpaWorkingOn.add(wnfRabatt);
		jpaWorkingOn.add(wlaProzent);
		jpaWorkingOn.add(wrbRabatt);
		jpaWorkingOn.add(wnfZuschlag, "skip");
		jpaWorkingOn.add(wlaMindestbestellmenge);
		jpaWorkingOn.add(wnfMindestbestellmenge, "span 2");
		jpaWorkingOn.add(wlaEinheitMindestbestellmenge);

		jpaWorkingOn.add(wlaNettopreis);
		jpaWorkingOn.add(wnfNettopreis);
		jpaWorkingOn.add(wlaWaehrungNettopreis);
		jpaWorkingOn.add(wrbNettopreis);
		jpaWorkingOn.add(wnfNettopreisBestellmenge);
		jpaWorkingOn.add(wnfEKPReis);
		jpaWorkingOn.add(wlaVerpackungseinheit);
		jpaWorkingOn.add(wnfVerpackungseinheit);
		jpaWorkingOn.add(wlaEinheitVerpackungseinheit);
		jpaWorkingOn.add(wcoEinheitVpe);

		jpaWorkingOn.add(wlaFixkosten);
		jpaWorkingOn.add(wnfFixkosten);
		jpaWorkingOn.add(wlaEinheitFixkosten);

		jpaWorkingOn.add(wlaWiederbeschaffungszeit, "skip 2, span 2");
		jpaWorkingOn.add(wnfWiederbeschaffungszeit, "span 2");
		jpaWorkingOn.add(wlaWiederbeschaffungszeitEinheit);

		jpaWorkingOn.add(wlaGueltigab);
		jpaWorkingOn.add(wdfgueltigab);
		jpaWorkingOn.add(wlaRabattgruppe, "skip 4");
		jpaWorkingOn.add(wtfRabattgruppe, "span 2, wrap");

		jpaWorkingOn.add(wlaGueltigbis);
		jpaWorkingOn.add(wdfgueltigbis);

		jpaWorkingOn.add(wlaAngebotsnummer, "skip 4");
		jpaWorkingOn.add(wtfAngebotsnummer, "span 2, wrap");

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZERTIFIKATART)) {
			
			jpaWorkingOn.add(wsfZertifikatart.getWrapperButton());
			jpaWorkingOn.add(wsfZertifikatart.getWrapperTextField(), "span 8");

		}

		String[] aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_FROM_LISTE)) {
			dialogQueryPartnerFromListe(e);
		}

		if (e.getSource().equals(wrbNettopreis)
				|| e.getSource().equals(wrbRabatt)) {
			if (wrbNettopreis.isSelected()) {
				wnfRabatt.setEditable(false);
				wnfNettopreis.setEditable(true);
			} else {
				wnfNettopreis.setEditable(false);
				wnfRabatt.setEditable(true);

			}
		}

	}

	protected void setDefaults() {

		// darf Preise sehen
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaEinzelpreis.setVisible(false);
			wlaNettopreis.setVisible(false);
			wlaFixkosten.setVisible(false);
			wlaWaehrungEinzelpreis.setVisible(false);
			wlaRabattgruppe.setVisible(false);
			wnfRabatt.setVisible(false);
			wtfRabattgruppe.setVisible(false);
			wnfFixkosten.setVisible(false);
			wnfNettopreis.setVisible(false);
			wnfEinzelpreis.setVisible(false);
			wlaRabatt.setVisible(false);
			wlaProzent.setVisible(false);
			wlaWaehrungNettopreis.setVisible(false);
			wlaEinheitFixkosten.setVisible(false);
			wrbRabatt.setVisible(false);
			wrbNettopreis.setVisible(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeArtikellieferant(artikellieferantDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		artikellieferantDto = new ArtikellieferantDto();

		wnfRabatt.setMandatoryField(false);
		wnfNettopreis.setMandatoryField(false);
	}

	protected void components2Dto() throws ExceptionLP {

		if (wnfEinzelpreis.getDouble() == null) {
			wnfRabatt.setDouble(null);
			wnfEinzelpreis.setDouble(null);
		}
		artikellieferantDto.setBHerstellerbez(wcbHerstellerbezeichnung
				.getShort());
		artikellieferantDto.setBWebshop(wcbWebshop.getShort());
		artikellieferantDto.setCArtikelnrlieferant(wtfArtikelnummer.getText());
		artikellieferantDto.setCBezbeilieferant(wtfBezbeilieferant.getText());
		artikellieferantDto.setCRabattgruppe(wtfRabattgruppe.getText());
		artikellieferantDto.setTPreisgueltigab(wdfgueltigab.getTimestamp());
		artikellieferantDto.setTPreisgueltigbis(wdfgueltigbis.getTimestamp());
		artikellieferantDto.setFMindestbestelmenge(wnfMindestbestellmenge
				.getDouble());
		artikellieferantDto.setFRabatt(wnfRabatt.getDouble());
		artikellieferantDto.setFStandardmenge(wnfStandardmenge.getDouble());
		artikellieferantDto.setNVerpackungseinheit(wnfVerpackungseinheit
				.getBigDecimal());
		artikellieferantDto
				.setIWiederbeschaffungszeit(wnfWiederbeschaffungszeit
						.getInteger());
		artikellieferantDto.setNEinzelpreis(wnfEinzelpreis.getBigDecimal());
		artikellieferantDto.setNFixkosten(wnfFixkosten.getBigDecimal());
		artikellieferantDto.setNNettopreis(wnfNettopreis.getBigDecimal());
		artikellieferantDto.setCAngebotnummer(wtfAngebotsnummer.getText());
		artikellieferantDto.setZertifikatartIId(wsfZertifikatart.getIKey());
		artikellieferantDto.setCWeblink(wtfWeblink.getText());
		artikellieferantDto.setEinheitCNrVpe((String) wcoEinheitVpe
				.getKeyOfSelectedItem());

		if (wrbRabatt.isSelected() == true) {
			artikellieferantDto.setBRabattbehalten(Helper.boolean2Short(true));
		} else {
			artikellieferantDto.setBRabattbehalten(Helper.boolean2Short(false));
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		eventActionSpecial(new ActionEvent(wrbNettopreis, 0, ""));

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String add2Title = LPMain
				.getTextRespectUISPr("artikel.report.lieferantenpreis");
		getInternalFrame().showReportKriterien(
				new ReportLieferantenpreis(internalFrameArtikel, add2Title));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (wnfEinzelpreis.hasFocus()) {
			wnfEinzelpreis_focusLost(null);
		} else if (wnfRabatt.hasFocus()) {
			wnfRabatt_focusLost(null);
		} else if (wnfNettopreis.hasFocus()) {
			wnfNettopreis_focusLost(null);
		}

		if (allMandatoryFieldsSetDlg()) {

			// PJ 08/14231
			if (artikellieferantDto != null
					&& artikellieferantDto.getIId() != null) {
				if (artikellieferantDto.getTPreisgueltigab() != null) {
					if (Helper.cutTimestamp(
							artikellieferantDto.getTPreisgueltigab()).getTime() == Helper
							.cutDate(wdfgueltigab.getDate()).getTime()) {
						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("artikel.error.ekpreisaenderungmitselbemdatum"));
						if (b == false) {
							return;
						}
					}
				}

			}

			components2Dto();
			if (artikellieferantDto.getIId() == null) {
				artikellieferantDto.setArtikelIId(internalFrameArtikel
						.getArtikelDto().getIId());
				artikellieferantDto.setMandantCNr(LPMain.getTheClient()
						.getMandant());

				artikellieferantDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate()
						.createArtikellieferant(artikellieferantDto));

				setKeyWhenDetailPanel(artikellieferantDto.getIId());

			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateArtikellieferant(artikellieferantDto);
			}

			// PJ 14400
			ArtikellieferantstaffelDto[] mengenstaffelDtos = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikellieferantstaffelFindByArtikellieferantIId(
							artikellieferantDto.getIId());

			if (mengenstaffelDtos != null && mengenstaffelDtos.length > 0) {
				boolean bMeldungMussangezeigtWerden = false;
				for (int i = 0; i < mengenstaffelDtos.length; i++) {
					if (Helper.short2boolean(mengenstaffelDtos[i]
							.getBRabattbehalten()) == false) {
						bMeldungMussangezeigtWerden = true;
					}
				}

				if (bMeldungMussangezeigtWerden) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("artikel.error.staffelpreiseaendern"));
				}

			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getArtikelDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}

	}

	public void setBestellpreisVisible() throws Throwable {
		wnfVerpackungseinheit.setMandatoryField(false);
		if (internalFrameArtikel.getArtikelDto().getEinheitCNrBestellung() != null) {

			String s = internalFrameArtikel.getArtikelDto()
					.getEinheitCNrBestellung().trim()
					+ " (";

			if (internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor() != null) {

				boolean bInvers = Helper.short2boolean(internalFrameArtikel
						.getArtikelDto().getbBestellmengeneinheitInvers());

				if (bInvers) {
					s += Helper.formatZahl(BigDecimal.ONE.divide(
							internalFrameArtikel.getArtikelDto()
									.getNUmrechnungsfaktor(), 2,
							BigDecimal.ROUND_HALF_EVEN), 2, LPMain
							.getTheClient().getLocUi())
							+ " ";
				} else {
					s += Helper.formatZahl(internalFrameArtikel.getArtikelDto()
							.getNUmrechnungsfaktor(), 2, LPMain.getTheClient()
							.getLocUi())
							+ " ";
				}

			}
			s += internalFrameArtikel.getArtikelDto().getEinheitCNr().trim()
					+ ")";
			wlaEinheitBestellmenge.setText(s);

			wlaEinheitBestellmenge.setVisible(true);
			wlaEinheitArtikelmenge.setVisible(true);
			wnfEinzelpreisBestellmenge.setVisible(true);
			wnfNettopreisBestellmenge.setVisible(true);
			wcoEinheitVpe.setVisible(false);
			wcoEinheitVpe.setKeyOfSelectedItem(null);

		} else {

			wcoEinheitVpe.setVisible(true);

			if (wcoEinheitVpe.getKeyOfSelectedItem() == null) {
				wlaEinheitBestellmenge.setVisible(false);
				wlaEinheitArtikelmenge.setVisible(false);
				wnfEinzelpreisBestellmenge.setVisible(false);
				wnfNettopreisBestellmenge.setVisible(false);
			} else {
				String s = ((String) wcoEinheitVpe.getKeyOfSelectedItem())
						.trim() + " (";

				if (wnfVerpackungseinheit.getBigDecimal() != null) {
					s += Helper.formatZahl(wnfVerpackungseinheit
							.getBigDecimal(), 2, LPMain.getTheClient()
							.getLocUi())
							+ " ";
				}

				wlaEinheitVerpackungseinheit.setText(internalFrameArtikel
						.getArtikelDto().getEinheitCNr().trim()
						+ "/");

				s += internalFrameArtikel.getArtikelDto().getEinheitCNr()
						.trim()
						+ ")";
				wlaEinheitBestellmenge.setText(s);

				wlaEinheitBestellmenge.setVisible(true);
				wlaEinheitArtikelmenge.setVisible(true);
				wnfEinzelpreisBestellmenge.setVisible(true);
				wnfNettopreisBestellmenge.setVisible(true);
				wnfVerpackungseinheit.setMandatoryField(true);
			}

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		wlaMaterialzuschlag.setForeground(Color.BLACK);

		wlaEinheitMindestbestellmenge.setText(internalFrameArtikel
				.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitStandardmenge.setText(internalFrameArtikel.getArtikelDto()
				.getEinheitCNr().trim());
		wlaEinheitVerpackungseinheit.setText(internalFrameArtikel
				.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitArtikelmenge.setText(internalFrameArtikel.getArtikelDto()
				.getEinheitCNr().trim());

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			wlaWaehrungEinzelpreis.setText("");
			wlaWaehrungNettopreis.setText("");
			wlaEinheitFixkosten.setText("");
			wlaEinheitBestellmenge.setText("");
			wdfgueltigab.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));

			eventActionSpecial(new ActionEvent(wrbNettopreis, 0, ""));

			setBestellpreisVisible();

			if (internalFrameArtikel.getArtikelDto().getMaterialIId() != null) {
				if (DelegateFactory
						.getInstance()
						.getMaterialDelegate()
						.materialzuschlagFindAktuellenzuschlag(
								internalFrameArtikel.getArtikelDto()
										.getMaterialIId()) == null) {
					wlaArtikelMaterial.setVisible(false);
					wlaMaterialzuschlag.setVisible(false);
					wnfEKPReis.setVisible(false);
					wnfZuschlag.setVisible(false);
				} else {
					wlaArtikelMaterial.setVisible(true);
					wlaMaterialzuschlag.setVisible(true);
					wnfEKPReis.setVisible(true);
					wnfZuschlag.setVisible(true);
				}
			} else {
				wlaArtikelMaterial.setVisible(false);
				wlaMaterialzuschlag.setVisible(false);
				wnfEKPReis.setVisible(false);
				wnfZuschlag.setVisible(false);
			}
			clearStatusbar();
		} else {
			artikellieferantDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikellieferantFindByPrimaryKey((Integer) key);

			if (internalFrameArtikel.getArtikelDto().getMaterialIId() != null) {

				if (DelegateFactory
						.getInstance()
						.getMaterialDelegate()
						.materialzuschlagFindAktuellenzuschlag(
								internalFrameArtikel.getArtikelDto()
										.getMaterialIId()) == null) {
					wlaArtikelMaterial.setVisible(false);
					wlaMaterialzuschlag.setVisible(false);
					wnfEKPReis.setVisible(false);
					wnfZuschlag.setVisible(false);
				} else {

					wlaArtikelMaterial.setVisible(true);
					wlaMaterialzuschlag.setVisible(true);
					wnfEKPReis.setVisible(true);
					wnfZuschlag.setVisible(true);

					MaterialDto mDto = DelegateFactory
							.getInstance()
							.getMaterialDelegate()
							.materialFindByPrimaryKey(
									internalFrameArtikel.getArtikelDto()
											.getMaterialIId());
					wlaArtikelMaterial.setText(mDto.getBezeichnung());

					BigDecimal zuschlag = DelegateFactory
							.getInstance()
							.getMaterialDelegate()
							.getKupferzuschlagInLieferantenwaehrung(
									internalFrameArtikel.getArtikelDto()
											.getIId(),
									artikellieferantDto.getLieferantIId());

					if (zuschlag == null) {
						wlaMaterialzuschlag.setForeground(Color.RED);
					}

					wnfZuschlag.setBigDecimal(zuschlag);
				}
			} else {
				wlaArtikelMaterial.setVisible(false);
				wlaMaterialzuschlag.setVisible(false);
				wnfEKPReis.setVisible(false);
				wnfZuschlag.setVisible(false);
			}

			dto2Components();

			setBestellpreisVisible();

			if (wnfEinzelpreis.getBigDecimal() != null
					&& wnfNettopreis.getBigDecimal() != null) {

				berechneNettopreisMitZuschlag();

				BigDecimal faktor = internalFrameArtikel.getArtikelDto()
						.getNUmrechnungsfaktor();

				boolean bInvers = Helper.short2boolean(internalFrameArtikel
						.getArtikelDto().getbBestellmengeneinheitInvers());
				if (faktor == null
						&& wnfVerpackungseinheit.getBigDecimal() != null  && wcoEinheitVpe.getKeyOfSelectedItem()!=null) {
					faktor = wnfVerpackungseinheit.getBigDecimal();
					bInvers = true;
				}

				if (faktor != null) {
					if (faktor.doubleValue() != 0) {
						if (bInvers) {
							wnfEinzelpreisBestellmenge
									.setBigDecimal(wnfEinzelpreis
											.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(
									wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

							wnfNettopreisBestellmenge
									.setBigDecimal(wnfNettopreis
											.getBigDecimal().multiply(faktor));

							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						} else {
							wnfEinzelpreisBestellmenge
									.setBigDecimal(wnfEinzelpreis
											.getBigDecimal().divide(faktor, 4,
													BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(
									wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

							wnfNettopreisBestellmenge
									.setBigDecimal(wnfNettopreis
											.getBigDecimal().divide(faktor, 4,
													BigDecimal.ROUND_HALF_EVEN));

							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						}
					} else {
						wnfNettopreisBestellmenge.setBigDecimal(new BigDecimal(
								0));
						wnfNettopreisBestellmenge.setToolTipText(null);
						wnfEinzelpreisBestellmenge
								.setBigDecimal(new BigDecimal(0));
						wnfEinzelpreisBestellmenge.setToolTipText(null);
					}
				}
			}
			pruefeMinVK();
		}
	}

	void wnfRabatt_focusLost(FocusEvent e) {
		if (wrbRabatt.isSelected()) {
			try {
				// z.b: Einzelpreis = 88, Rabatt = 22
				// Nettopreis: 68,64 = 88 - ( ( 88 / 100 ) * 22)
				if (wnfEinzelpreis.getBigDecimal() != null
						&& wnfRabatt.getBigDecimal() != null) {
					BigDecimal einzelpreis = wnfEinzelpreis.getBigDecimal();
					BigDecimal rabattpreis = einzelpreis.multiply(
							wnfRabatt.getBigDecimal()).divide(
							new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
					wnfNettopreis.setBigDecimal(einzelpreis
							.subtract(rabattpreis));

					if (wnfEinzelpreisBestellmenge.getBigDecimal() != null) {
						BigDecimal einzelpreisBestellmenge = wnfEinzelpreisBestellmenge
								.getBigDecimal();
						BigDecimal rabattpreistellmenge = einzelpreisBestellmenge
								.multiply(wnfRabatt.getBigDecimal()).divide(
										new BigDecimal(100), 4,
										BigDecimal.ROUND_HALF_EVEN);

						wnfNettopreisBestellmenge
								.setBigDecimal(einzelpreisBestellmenge
										.subtract(rabattpreistellmenge));
						if (artikellieferantDto.getLieferantDto() != null) {
							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						}

					}

				} else {
					wnfNettopreis.setDouble(new Double(0));
				}
				pruefeMinVK();
				berechneNettopreisMitZuschlag();
			} catch (Throwable ex) {
				// nothing here
			}
		}
	}

	void wnfNettopreis_focusLost(FocusEvent e) {
		if (wrbNettopreis.isSelected()) {
			try {
				// z.b: Rabatt = 1 - ( Nettopreis / Einzelpreis )
				if (wnfEinzelpreis.getBigDecimal() != null
						&& wnfNettopreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {
					BigDecimal rabattsatz = new BigDecimal(1)
							.subtract(wnfNettopreis.getBigDecimal().divide(
									wnfEinzelpreis.getBigDecimal(), 4,
									BigDecimal.ROUND_HALF_EVEN));
					wnfRabatt.setBigDecimal(rabattsatz.multiply(new BigDecimal(
							100)));

					BigDecimal faktor = internalFrameArtikel.getArtikelDto()
							.getNUmrechnungsfaktor();

					if (faktor == null
							&& wnfVerpackungseinheit.getBigDecimal() != null) {
						faktor = wnfVerpackungseinheit.getBigDecimal();
					}

					if (faktor != null
							&& wnfEinzelpreisBestellmenge.getBigDecimal() != null
							&& wnfEinzelpreisBestellmenge.getBigDecimal()
									.doubleValue() != 0) {

						wnfNettopreisBestellmenge
								.setBigDecimal(wnfEinzelpreisBestellmenge
										.getBigDecimal().subtract(
												wnfEinzelpreisBestellmenge
														.getBigDecimal()
														.multiply(rabattsatz)));
						if (artikellieferantDto.getLieferantDto() != null) {
							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						}

					}

				} else {
					wnfRabatt.setDouble(new Double(0));

				}

				pruefeMinVK();
				berechneNettopreisMitZuschlag();
			} catch (Throwable ex) {
				// nothing here
			}
		}

	}

	private void setTooltipMandantenwaehrung(WrapperNumberField wnf,
			String lieferantenwaehrung) throws Throwable {
		if (wnf.getBigDecimal() != null) {
			BigDecimal umgerechnet = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.rechneUmInAndereWaehrung(wnf.getBigDecimal(),
							lieferantenwaehrung,
							LPMain.getTheClient().getSMandantenwaehrung());
			wnf.setToolTipText(Helper.formatZahl(umgerechnet,
					wnf.getFractionDigits(), LPMain.getTheClient().getLocUi())
					+ " " + LPMain.getTheClient().getSMandantenwaehrung());
		} else {
			wnf.setToolTipText(null);
		}
	}

	public void wnfEinzelpreis_focusLost(FocusEvent e) {
		try {
			if (wnfEinzelpreis.getDouble() == null) {
				wnfRabatt.setDouble(null);
				wnfNettopreisBestellmenge.setDouble(null);
				wnfEinzelpreisBestellmenge.setDouble(null);
				wnfNettopreis.setDouble(null);
				wnfRabatt.setMandatoryField(false);
				wnfNettopreis.setMandatoryField(false);

			} else {

				wnfRabatt.setMandatoryField(true);
				wnfNettopreis.setMandatoryField(true);

				if (wnfRabatt.getBigDecimal() == null) {
					wnfRabatt.setBigDecimal(new java.math.BigDecimal(0));
					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal());
					setTooltipMandantenwaehrung(wnfNettopreis,
							artikellieferantDto.getLieferantDto()
									.getWaehrungCNr());
				}

				if (wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {
					if (wrbNettopreis.isSelected()) {

						if (wnfNettopreis.getBigDecimal() != null) {
							// Rabatt neu berechnen
							BigDecimal rabattsatz = new BigDecimal(1)
									.subtract(wnfNettopreis.getBigDecimal()
											.divide(wnfEinzelpreis
													.getBigDecimal(), 4,
													BigDecimal.ROUND_HALF_EVEN));
							wnfRabatt.setBigDecimal(rabattsatz
									.multiply(new BigDecimal(100)));
						}

					} else {
						if (wnfRabatt.getBigDecimal() != null) {
							// Nettopreis neu berechnen
							BigDecimal einzelpreis = wnfEinzelpreis
									.getBigDecimal();
							BigDecimal rabattpreis = einzelpreis.multiply(
									wnfRabatt.getBigDecimal()).divide(
									new BigDecimal(100),
									BigDecimal.ROUND_HALF_EVEN);
							wnfNettopreis.setBigDecimal(einzelpreis
									.subtract(rabattpreis));

							if (artikellieferantDto.getLieferantDto() != null) {
								setTooltipMandantenwaehrung(wnfNettopreis,
										artikellieferantDto.getLieferantDto()
												.getWaehrungCNr());
							}
						}
					}

				} else {
					wnfRabatt.setBigDecimal(new java.math.BigDecimal(0));

					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal());
					setTooltipMandantenwaehrung(wnfNettopreis,
							artikellieferantDto.getLieferantDto()
									.getWaehrungCNr());
				}

				BigDecimal faktor = internalFrameArtikel.getArtikelDto()
						.getNUmrechnungsfaktor();

				boolean bInvers = Helper.short2boolean(internalFrameArtikel
						.getArtikelDto().getbBestellmengeneinheitInvers());
				if (faktor == null
						&& wnfVerpackungseinheit.getBigDecimal() != null  && wcoEinheitVpe.getKeyOfSelectedItem()!=null) {
					faktor = wnfVerpackungseinheit.getBigDecimal();
					bInvers = true;
				}

				if (faktor != null) {
					if (faktor.doubleValue() != 0) {

						if (bInvers) {
							wnfEinzelpreisBestellmenge
									.setBigDecimal(wnfEinzelpreis
											.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(
									wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

							wnfNettopreisBestellmenge
									.setBigDecimal(wnfNettopreis
											.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

						} else {

							wnfEinzelpreisBestellmenge
									.setBigDecimal(wnfEinzelpreis
											.getBigDecimal().divide(faktor, 4,
													BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(
									wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());

							wnfNettopreisBestellmenge
									.setBigDecimal(wnfNettopreis
											.getBigDecimal().divide(faktor, 4,
													BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(
									wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto()
											.getWaehrungCNr());
						}
					} else {
						wnfEinzelpreisBestellmenge
								.setBigDecimal(new java.math.BigDecimal(0));
						wnfEinzelpreisBestellmenge.setToolTipText(null);
						wnfNettopreisBestellmenge
								.setBigDecimal(new java.math.BigDecimal(0));
						wnfNettopreisBestellmenge.setToolTipText(null);
					}
				}
				pruefeMinVK();
			}
			berechneNettopreisMitZuschlag();
		} catch (Throwable ex) {
			// nothing here
			ex.printStackTrace();
		}
	}

	private void berechneNettopreisMitZuschlag() throws ExceptionLP {
		if (wnfZuschlag.getBigDecimal() != null) {
			wnfEKPReis.setBigDecimal(wnfNettopreis.getBigDecimal().add(
					wnfZuschlag.getBigDecimal()));
		} else {
			wnfEKPReis.setBigDecimal(null);
		}
	}

	private void pruefeMinVK() throws Throwable {
		wnfNettopreis.setForeground(Color.BLACK);
		wnfNettopreis.setToolTipText(null);
		if (wnfNettopreis.getBigDecimal() != null) {

			BigDecimal gestpreis = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getGemittelterGestehungspreisDesHauptlagers(
							internalFrameArtikel.getArtikelDto().getIId());

			if (gestpreis != null) {

				BigDecimal mindestpreis = new BigDecimal(
						gestpreis.doubleValue()
								* (1 + ((internalFrameArtikel.getArtikelDto()
										.getFMindestdeckungsbeitrag()
										.doubleValue() / 100))));

				if (mindestpreis.doubleValue() < wnfNettopreis.getBigDecimal()
						.doubleValue()) {
					wnfNettopreis.setForeground(Color.RED);
					wnfNettopreis.setToolTipText(LPMain
							.getTextRespectUISPr("artikel.ekpreisueberminvk"));
				}
			}

		}
	}

	public void wnfEinzelpreisBestelleinheit_focusLost(FocusEvent e) {
		try {
			BigDecimal faktor = internalFrameArtikel.getArtikelDto()
					.getNUmrechnungsfaktor();

			boolean bInvers = Helper.short2boolean(internalFrameArtikel
					.getArtikelDto().getbBestellmengeneinheitInvers());

			if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null && wcoEinheitVpe.getKeyOfSelectedItem()!=null) {
				faktor = wnfVerpackungseinheit.getBigDecimal();
				bInvers = true;
			}

			if (faktor != null
					&& wnfEinzelpreisBestellmenge.getBigDecimal() != null) {
				if (bInvers) {
					wnfEinzelpreis.setBigDecimal(wnfEinzelpreisBestellmenge
							.getBigDecimal().divide(faktor, 4,
									BigDecimal.ROUND_HALF_EVEN));

				} else {
					wnfEinzelpreis.setBigDecimal(wnfEinzelpreisBestellmenge
							.getBigDecimal().multiply(faktor));

				}

				setTooltipMandantenwaehrung(wnfEinzelpreis, artikellieferantDto
						.getLieferantDto().getWaehrungCNr());

				wnfEinzelpreis_focusLost(null);

			}
			berechneNettopreisMitZuschlag();
		} catch (Throwable ex) {
			// nix
		}
	}

	public void wnfNettopreisBestelleinheit_focusLost(FocusEvent e) {
		try {
			BigDecimal faktor = internalFrameArtikel.getArtikelDto()
					.getNUmrechnungsfaktor();

			if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null) {
				faktor = wnfVerpackungseinheit.getBigDecimal();
			}

			if (faktor != null
					&& wnfEinzelpreisBestellmenge.getBigDecimal() != null
					&& wnfNettopreisBestellmenge.getBigDecimal() != null
					&& wnfEinzelpreisBestellmenge.getBigDecimal().doubleValue() != 0) {

				BigDecimal rabattsatz = new BigDecimal(1)
						.subtract(wnfNettopreisBestellmenge.getBigDecimal()
								.divide(wnfEinzelpreisBestellmenge
										.getBigDecimal(), 4,
										BigDecimal.ROUND_HALF_EVEN));
				wnfRabatt.setBigDecimal(rabattsatz
						.multiply(new BigDecimal(100)));

				if (wnfEinzelpreis.getBigDecimal() != null
						&& wnfNettopreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {

					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal()
							.subtract(
									wnfEinzelpreis.getBigDecimal().multiply(
											rabattsatz)));

					setTooltipMandantenwaehrung(wnfNettopreis,
							artikellieferantDto.getLieferantDto()
									.getWaehrungCNr());

				}

				pruefeMinVK();
				berechneNettopreisMitZuschlag();
			}
		} catch (Throwable ex) {
			// nix
		}
	}

	public void wcoEinheitVpe_actionPerformed(ActionEvent e) {

		try {
			setBestellpreisVisible();
		} catch (Throwable e1) {
			getInternalFrame().handleException(e1, true);
		}
	}

}

class PanelArtikellieferant_wnfEinzelpreis_focusAdapter extends FocusAdapter {
	private PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfEinzelpreis_focusAdapter(
			PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfEinzelpreis_focusLost(e);
	}
}

class PanelArtikellieferant_wcoEinheitVpe_actionAdapter implements
		ActionListener {
	private PanelArtikellieferant adaptee;

	PanelArtikellieferant_wcoEinheitVpe_actionAdapter(
			PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoEinheitVpe_actionPerformed(e);
	}
}

class PanelArtikellieferant_wnfRabatt_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfRabatt_focusAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfRabatt_focusLost(e);
	}
}

class PanelArtikellieferant_wnfNettopreis_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfNettopreis_focusAdapter(
			PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfNettopreis_focusLost(e);
	}
}

class PanelArtikellieferant_wnfEinzelpreisBestelleinheit_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfEinzelpreisBestelleinheit_focusAdapter(
			PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfEinzelpreisBestelleinheit_focusLost(e);
	}
}

class PanelArtikellieferant_wnfNettopreisBestelleinheit_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfNettopreisBestelleinheit_focusAdapter(
			PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfNettopreisBestelleinheit_focusLost(e);
	}
}
