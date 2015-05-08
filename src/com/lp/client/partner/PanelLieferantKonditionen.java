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
package com.lp.client.partner;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Lieferantkonditionen.</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17.02.05</I></p>
 * 
 * @author $Author: christian $
 * 
 * @version $Revision: 1.30 $
 */
public class PanelLieferantKonditionen extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaRabattsatz = null;
	private WrapperNumberField wnfRabattsatz = null;
	private WrapperNumberField wnfKreditlimit = null;
	private WrapperLabel wlaMindestbestellwert = null;
	private WrapperNumberField wnfMindestbestellwert = null;
	private WrapperLabel wlaTransportkostenJeLieferung = null;
	private WrapperNumberField wnfTransportkostenJeLieferung = null;
	private WrapperLabel wlaJahresbonus = null;
	private WrapperNumberField wnfJahresbonus = null;
	private WrapperLabel wlaAbUmsatz = null;
	private WrapperNumberField wnfAbUmsatz = null;
	private WrapperLabel wlaMindermengenzuschlag = null;
	private WrapperNumberField wnfMindermengenzuschlag = null;
	private WrapperLabel wlaKupferzahl = null;
	private WrapperNumberField wnfKupferzahl = null;
	private WrapperLabel wlaHinweistextintern = null;
	private WrapperTextField wtfHinweistextintern = null;
	private WrapperLabel wlaHinweistextextern = null;
	private WrapperTextField wtfHinweistextextern = null;
	private WrapperLabel wlaEinheitProzentRabatt = null;
	private WrapperButton wbuKontowaren = null;
	private WrapperTextField wtfKontowaren = null;
	private WrapperLabel wlaKreditlimit = null;
	private WrapperLabel wlaLieferantenWhg0 = null;
	private WrapperLabel wlaLieferantenWhg1 = null;
	private WrapperLabel wlaLieferantenWhg2 = null;
	private WrapperLabel wlaLieferantenWhg3 = null;
	private WrapperLabel wlaLieferantenWhg4 = null;
	private WrapperLabel wlaLieferantenWhg5 = null;
	private WrapperLabel wlaLieferantenWhg6 = null;
	private WrapperLabel wlaWaehrung = null;
	private WrapperComboBox wcoWaehrung = null;
	private JPanel jpaWorkingOn = null;
	private WrapperTextField wtfZahlungsziel = null;
	private WrapperCheckBox wcbBeurteilen = null;
	private WrapperCheckBox wcbZollimportpapiere = null;
	private WrapperTextNumberField wtnfKontokreditoren = null;
	private WrapperLabel wlaKontokreditorenRangeVonBis = null;
	private WrapperLabel wlaKommentar = null;
	private WrapperEditorField wefKommentar = null;
	private WrapperLabel wlaBeurteilung = new WrapperLabel();
	private WrapperLabel wlaAbweichendesfinanzamt = null;
	private WrapperTextField wtfKostenstelle = null;
	private WrapperButton wbtKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private WrapperCheckBox wcbIgErwerb = null;
	private WrapperCheckBox wcbReversecharge = null;
	private boolean bReversecharge = true;
	private PanelQueryFLR panelQueryFLRLieferart;
	private WrapperButton wbuLieferart = null;
	private WrapperTextField wtfLieferart = null;

	private WrapperTextField wtfSpedition = null;
	private WrapperButton wbuSpedition = null;
	private WrapperGotoButton wbuKreditorenkontoAutomatisch = null;

	private WrapperGotoButton wbuVersteckterKunde = null;

	private WrapperButton wbuAbweichendesustland = null;
	private WrapperTextField wtfAbweichendesustland = null;
	private PanelQueryFLR panelQueryFLRAbweichendesustland = null;
	private PanelQueryFLR panelQueryFLRLager = null;

	private WrapperButton wbuZiellager = null;
	private WrapperTextField wtfZiellager = null;

	private WrapperLabel wlaBestellsperream = null;
	private WrapperDateField wdfBestellsperream = null;

	private WrapperLabel wlaFreigabe = new WrapperLabel();
	private WrapperLabel wlaFreigabeAb = new WrapperLabel();
	private WrapperDateField wdfFreigabe = new WrapperDateField();
	private WrapperTextField wtfFreigabe = new WrapperTextField();
	private WrapperLabel wlaFreigabePerson = new WrapperLabel();
	private WrapperSelectField wsfLager = null;

	static final private String ACTION_SPECIAL_FLR_USTLAND = "action_special_flr_ustland";
	static final private String ACTION_SPECIAL_FLR_LIEFERART = "action_special_flr_lieferart";
	static final private String ACTION_SPECIAL_FLR_KONTOWAREN = "action_special_flr_kontowaren";
	static final private String ACTION_SPECIAL_FLR_KOSTENSTELLE = "action_special_flr_kostenstelle";
	static final private String ACTION_SPECIAL_FLR_ZAHLUNGSZIEL = "action_special_flr_zahlungsziel";
	static final private String ACTION_SPECIAL_FLR_SPEDITEUR = "action_special_flr_spedituer";
	static final private String ACTION_SPECIAL_KREDITORENKONTOAUTOMATISCH = "action_special_kreditorenkontoautomatisch";
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";

	private WrapperButton wbtZahlungsziel = null;
	private PanelQueryFLR panelQueryFLRZahlungsziel = null;
	private PanelQueryFLR panelQueryFLRKontowaren = null;
	private PanelQueryFLR panelQueryFLRSpediteur = null;

	public PanelLieferantKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initPanel();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
		
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);
		bReversecharge = (Boolean) parametermandantDto.getCWertAsObject();
		// das Aussenpanel hat immer das Gridbaglayout.
		GridBagLayout gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		JPanel jpaButtonAction = getToolsPanel();
		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jpaWorkingOn = new JPanel();
		GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wsfLager = new WrapperSelectField(WrapperSelectField.LAGER,
				getInternalFrame(), true);

		wsfLager.setMandatoryField(true);
		wsfLager.setText(LPMain.getTextRespectUISPr("button.zubuchungslager"));

		wlaRabattsatz = new WrapperLabel();
		wlaRabattsatz.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.rabattsumme"));
		wnfRabattsatz = new WrapperNumberField();
		wnfRabattsatz.setMaximumValue(LieferantFac.MAX_RABATTSATZ);
		wnfRabattsatz.setMinimumValue(LieferantFac.MIN_RABATTSATZ);
		wnfRabattsatz.setFractionDigits(LieferantFac.FRACTION_RABATTSATZ);

		wlaAbweichendesfinanzamt = new WrapperLabel();
		wlaAbweichendesfinanzamt.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.abwfinanzamt"));

		wcbIgErwerb = new WrapperCheckBox();
		wcbIgErwerb.setText(LPMain.getTextRespectUISPr("lp.igerwerb"));

		wcbReversecharge = new WrapperCheckBox();
		wcbReversecharge
				.setText(LPMain.getTextRespectUISPr("lp.reversecharge"));

		wlaKreditlimit = new WrapperLabel();
		wlaKreditlimit.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.kreditlimit"));
		wnfKreditlimit = new WrapperNumberField();
		wnfKreditlimit.setMaximumValue(new BigDecimal(
				LieferantFac.MAX_KREDITLIMIT));
		wnfKreditlimit.setMinimumValue(LieferantFac.MIN_KREDITLIMIT);
		wnfKreditlimit.setFractionDigits(LieferantFac.FRACTION_KREDITLIMIT);

		wlaLieferantenWhg0 = new WrapperLabel();
		wlaLieferantenWhg0.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLieferantenWhg1 = new WrapperLabel();
		wlaLieferantenWhg1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLieferantenWhg2 = new WrapperLabel();
		wlaLieferantenWhg2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLieferantenWhg3 = new WrapperLabel();
		wlaLieferantenWhg3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLieferantenWhg4 = new WrapperLabel();
		wlaLieferantenWhg4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLieferantenWhg5 = new WrapperLabel();
		wlaLieferantenWhg5.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLieferantenWhg6 = new WrapperLabel();
		wlaLieferantenWhg6.setHorizontalAlignment(SwingConstants.LEFT);

		wlaWaehrung = new WrapperLabel();
		wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.waehrung"));
		wcoWaehrung = new WrapperComboBox();
		wcoWaehrung.setMandatoryFieldDB(true);

		wbuZiellager = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.ziellager"));

		wbuZiellager.setActionCommand(ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuZiellager.addActionListener(this);

		wtfZiellager = new WrapperTextField();
		wtfZiellager.setActivatable(false);

		wtfZahlungsziel = new WrapperTextField(
				MandantFac.MAX_ZAHLUNGSZIEL_C_BEZ);
		wtfZahlungsziel.setMandatoryFieldDB(true);

		wbtZahlungsziel = new WrapperButton();
		wbtZahlungsziel.setActionCommand(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL);
		wbtZahlungsziel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.zahlungsziel"));
		wbtZahlungsziel.addActionListener(this);

		wbuAbweichendesustland = new WrapperButton();
		wbuAbweichendesustland.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.abwustland"));
		wbuAbweichendesustland.setActionCommand(ACTION_SPECIAL_FLR_USTLAND);
		wbuAbweichendesustland.addActionListener(this);

		wtfAbweichendesustland = new WrapperTextField();
		wtfAbweichendesustland.setActivatable(false);

		wlaMindestbestellwert = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.mindestbestellwert"));
		wnfMindestbestellwert = new WrapperNumberField();

		wlaJahresbonus = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.jahresbonus"));
		wnfJahresbonus = new WrapperNumberField();

		wlaAbUmsatz = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.ab_umsatz"));
		wnfAbUmsatz = new WrapperNumberField();

		wlaMindermengenzuschlag = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.mindermengenzuschlag"));
		wnfMindermengenzuschlag = new WrapperNumberField();

		wlaFreigabe = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.lieferant.freigabe"));

		wlaKupferzahl = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.kupferzahl"));
		wnfKupferzahl = new WrapperNumberField();
		wnfKupferzahl.setFractionDigits(6);

		wlaTransportkostenJeLieferung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.tpkostenjelieferung"));
		wnfTransportkostenJeLieferung = new WrapperNumberField();

		wlaHinweistextextern = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.hinweistext.extern"));
		wtfHinweistextextern = new WrapperTextField(LieferantFac.MAX_HINWEIS);
		wlaHinweistextintern = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.hinweistext.intern"));
		wtfHinweistextintern = new WrapperTextField(LieferantFac.MAX_HINWEIS);

		wtnfKontokreditoren = new WrapperTextNumberField();
		Integer kontonummerMaxStellen = DelegateFactory
				.getInstance()
				.getFinanzDelegate()
				.getAnzahlStellenVonKontoNummer(
						FinanzServiceFac.KONTOTYP_KREDITOR);
		wtnfKontokreditoren.setMaximumDigits(kontonummerMaxStellen.intValue());

		wlaKontokreditorenRangeVonBis = new WrapperLabel();
		wlaKontokreditorenRangeVonBis
				.setHorizontalAlignment(SwingConstants.LEFT);

		wlaFreigabePerson.setHorizontalAlignment(SwingConstants.LEFT);

		wtfKontowaren = new WrapperTextField();
		wtfKontowaren.setActivatable(false);
		wbuKontowaren = new WrapperButton();
		wbuKontowaren.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.warenkonto"));
		// FLR Kontoerloese lokal
		wbuKontowaren.setActionCommand(ACTION_SPECIAL_FLR_KONTOWAREN);
		wbuKontowaren.addActionListener(this);

		wlaKommentar = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kommentar"));
		wlaKommentar.setVerticalAlignment(SwingConstants.NORTH);
		wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
		wefKommentar.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wbtKostenstelle = new WrapperButton();
		wbtKostenstelle.setActionCommand(ACTION_SPECIAL_FLR_KOSTENSTELLE);
		wbtKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbtKostenstelle.addActionListener(this);

		wtfKostenstelle = new WrapperTextField();
		wtfKostenstelle.setActivatable(false);

		wlaEinheitProzentRabatt = new WrapperLabel();
		wlaEinheitProzentRabatt.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaEinheitProzentRabatt.setHorizontalAlignment(SwingConstants.LEFT);

		wlaFreigabeAb.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.lieferant.freigabeab"));
		wlaFreigabeAb.setHorizontalAlignment(SwingConstants.LEFT);

		wcbBeurteilen = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("lp.beurteilen"));

		wcbZollimportpapiere = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr(
						"part.lieferant.zollimportpapiere.erforderlich"));

		wtfLieferart = new WrapperTextField();
		wtfLieferart.setActivatable(false);
		wtfLieferart.setMandatoryField(true);

		wbuLieferart = new WrapperButton();
		wbuLieferart.addActionListener(this);
		wbuLieferart.setActionCommand(ACTION_SPECIAL_FLR_LIEFERART);
		wbuLieferart.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferart"));

		wbuSpedition = new WrapperButton();
		wbuSpedition.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.spediteur"));
		wbuSpedition.setActionCommand(ACTION_SPECIAL_FLR_SPEDITEUR);
		wbuSpedition.addActionListener(this);
		wtfSpedition = new WrapperTextField();
		wtfSpedition.setMandatoryFieldDB(true);

		boolean bFibuInstalliert = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG);
		wbuKreditorenkontoAutomatisch = new WrapperGotoButton(
				WrapperGotoButton.GOTO_KREDITORENKONTO_AUSWAHL);

		wbuKreditorenkontoAutomatisch.getWrapperButtonGoTo().setVisible(
				bFibuInstalliert);
		wbuKreditorenkontoAutomatisch.setText(LPMain.getInstance()
				.getTextRespectUISPr("button.kreditorenkontoanlegen"));
		wbuKreditorenkontoAutomatisch.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.kreditorenkontoanlegen.tooltip"));
		wbuKreditorenkontoAutomatisch
				.setActionCommand(ACTION_SPECIAL_KREDITORENKONTOAUTOMATISCH);
		wbuKreditorenkontoAutomatisch.addActionListener(this);

		wlaBestellsperream = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.lieferant.bestellsperream"));
		wdfBestellsperream = new WrapperDateField();

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbtKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelle, new GridBagConstraints(1, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaWaehrung, new GridBagConstraints(3, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWaehrung, new GridBagConstraints(4, iZeile, 2, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuKontowaren, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontowaren, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		if (DelegateFactory.getInstance().getFinanzDelegate()
				.getAnzahlDerFinanzaemter() > 1) {
			// CK Projekt 7930

			if (DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

				jpaWorkingOn.add(wlaAbweichendesfinanzamt,
						new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
				jpaWorkingOn.add(wtfAbweichendesustland,
						new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
			} else {
				jpaWorkingOn.add(wbuAbweichendesustland,
						new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
				jpaWorkingOn.add(wtfAbweichendesustland,
						new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));
			}
		}
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuKreditorenkontoAutomatisch, new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtnfKontokreditoren, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKontokreditorenRangeVonBis, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbIgErwerb, new GridBagConstraints(4, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		if (bReversecharge == true) {
			jpaWorkingOn.add(wcbReversecharge, new GridBagConstraints(5,
					iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		// Zeile.
		iZeile++;

		// Zeile.
		iZeile++;

		jpaWorkingOn.add(wcbZollimportpapiere, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbBeurteilen, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBeurteilung, new GridBagConstraints(4, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaHinweistextextern, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHinweistextextern, new GridBagConstraints(1,
				iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaHinweistextintern, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHinweistextintern, new GridBagConstraints(1,
				iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(1, iZeile, 5, 1,
				0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFreigabe, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFreigabe, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFreigabeAb, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfFreigabe, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), -20, 0));
		jpaWorkingOn.add(wlaFreigabePerson, new GridBagConstraints(4, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaRabattsatz, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfRabattsatz, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheitProzentRabatt, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaJahresbonus, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfJahresbonus, new GridBagConstraints(4, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg2, new GridBagConstraints(6, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaKreditlimit, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKreditlimit, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg0, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaAbUmsatz, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAbUmsatz, new GridBagConstraints(4, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg3, new GridBagConstraints(6, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaMindestbestellwert, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMindestbestellwert, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg1, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMindermengenzuschlag, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMindermengenzuschlag, new GridBagConstraints(4,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg4, new GridBagConstraints(6, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaTransportkostenJeLieferung, new GridBagConstraints(
				0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfTransportkostenJeLieferung, new GridBagConstraints(
				1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg5, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaKupferzahl, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKupferzahl, new GridBagConstraints(4, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLieferantenWhg6, new GridBagConstraints(6, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 35, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbtZahlungsziel, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZahlungsziel, new GridBagConstraints(1, iZeile, 3,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile.
		iZeile++;

		jpaWorkingOn.add(wbuLieferart, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferart, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wsfLager.getWrapperButton(), new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wsfLager.getWrapperTextField(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 95, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuZiellager, new GridBagConstraints(4, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wtfZiellager, new GridBagConstraints(4, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 95, 2, 2), 0, 0));

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wbuSpedition, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSpedition, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBestellsperream, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBestellsperream, new GridBagConstraints(4, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wbuVersteckterKunde = new WrapperGotoButton(
				WrapperGotoButton.GOTO_KUNDE_KONDITIONEN);
		wbuVersteckterKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.lieferant.versteckterkunde.konditionen"));
		wbuVersteckterKunde.setActivatable(false);

		wbuVersteckterKunde.setMaximumSize(new Dimension(150, 30));
		wbuVersteckterKunde.setMinimumSize(new Dimension(150, 30));
		wbuVersteckterKunde.setPreferredSize(new Dimension(150, 30));

		getToolsPanel().add(wbuVersteckterKunde);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KONTOWAREN)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = FinanzFilterFactory.getInstance()
					.createFKSachkonten();
			panelQueryFLRKontowaren = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.konto.waren"));
			FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
					.createFKDKontonummer();
			FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
					.createFKDKontobezeichnung();
			FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
					.createFKVKonto();
			panelQueryFLRKontowaren
					.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1,
							fkDirekt2, fkVersteckt);
			new DialogQuery(panelQueryFLRKontowaren);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_USTLAND)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_LEEREN };

			panelQueryFLRAbweichendesustland = new PanelQueryFLR(null, null,
					QueryParameters.UC_ID_LAND, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("title.landauswahlliste"));

			new DialogQuery(panelQueryFLRAbweichendesustland);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LIEFERART)) {
			panelQueryFLRLieferart = SystemFilterFactory.getInstance()
					.createPanelFLRLieferart(getInternalFrame(),
							getLieferantDto().getLieferartIId());
			new DialogQuery(panelQueryFLRLieferart);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ZAHLUNGSZIEL)) {
			panelQueryFLRZahlungsziel = SystemFilterFactory.getInstance()
					.createPanelFLRZahlungsziel(getInternalFrame(),
							getLieferantDto().getZahlungszielIId());
			new DialogQuery(panelQueryFLRZahlungsziel);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KOSTENSTELLE)) {
			panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
					.createPanelFLRKostenstelle(getInternalFrame(), false,
							false);
			// vorbesetzen
			if (getLieferantDto().getIIdKostenstelle() != null) {
				panelQueryFLRKostenstelle.setSelectedId(getLieferantDto()
						.getIIdKostenstelle());
			}
			new DialogQuery(panelQueryFLRKostenstelle);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SPEDITEUR)) {
			panelQueryFLRSpediteur = SystemFilterFactory.getInstance()
					.createPanelFLRSpediteur(getInternalFrame(),
							getLieferantDto().getIdSpediteur());
			new DialogQuery(panelQueryFLRSpediteur);
		}

		else if (e.getActionCommand().equals(
				ACTION_SPECIAL_KREDITORENKONTOAUTOMATISCH)) {
			boolean bKontoAnlegen = true;
			if (getLieferantDto().getKontoIIdKreditorenkonto() != null) {
				bKontoAnlegen = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getInstance()
										.getTextRespectUISPr(
												"part.lieferant.warning.hatschoneinkreditorenkonto"),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.frage"));
			}
			if (bKontoAnlegen) {
				String kontoNr = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.createKreditorenkontoNummerZuLieferantenAutomatisch(
								getLieferantDto().getIId(),
								wtnfKontokreditoren.getText());
				wtnfKontokreditoren.setText(kontoNr);
			}
			if (getLieferantDto().getKontoIIdKreditorenkonto() != null) {
				if (DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.darfAnwenderAufModulZugreifen(
								LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

					KontoDto kontoDto = DelegateFactory
							.getInstance()
							.getFinanzDelegate()
							.kontoFindByPrimaryKey(
									getLieferantDto()
											.getKontoIIdKreditorenkonto());

					MandantDto mandant = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mandantFindByPrimaryKey(
									LPMain.getTheClient().getMandant());

					if (kontoDto.getFinanzamtIId().equals(
							mandant.getPartnerIIdFinanzamt())) {
						wtfAbweichendesustland.setText(null);
					} else {
						wtfAbweichendesustland.setText(DelegateFactory
								.getInstance()
								.getFinanzDelegate()
								.finanzamtFindByPrimaryKey(
										kontoDto.getFinanzamtIId(),
										mandant.getCNr()).getPartnerDto()
								.formatName());
					}
				}
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		}

	}

	protected void dto2Components() throws Throwable {

		// PJ17838

		KundeDto kDto = DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(
						getLieferantDto().getPartnerIId(),
						LPMain.getTheClient().getMandant());

		// PJ17838 auch anzeigen, wenn Kunde vorhanden und dieser nicht
		// versteckt ist
		if (kDto != null) {
			wbuVersteckterKunde.setVisible(true);
			wbuVersteckterKunde.setOKey(kDto.getIId());
		} else {
			wbuVersteckterKunde.setVisible(false);
		}

		wcoWaehrung.setKeyOfSelectedItem(getLieferantDto().getWaehrungCNr());
		wnfRabattsatz.setDouble(getLieferantDto().getNRabatt());
		wnfKreditlimit.setBigDecimal(getLieferantDto().getNKredit());
		wcbBeurteilen.setShort(getLieferantDto().getBBeurteilen());
		wcbZollimportpapiere.setShort(getLieferantDto().getBZollimportpapier());

		wdfFreigabe.setTimestamp(getLieferantDto().getTFreigabe());
		wtfFreigabe.setText(getLieferantDto().getCFreigabe());

		wbuKreditorenkontoAutomatisch.setOKey(getLieferantDto()
				.getKontoIIdKreditorenkonto());
		wcbIgErwerb.setSelected(getLieferantDto().getBIgErwerbBoolean());
		wcbReversecharge.setShort(getLieferantDto().getBReversecharge());

		wsfLager.setKey(getLieferantDto().getLagerIIdZubuchungslager());

		if (getLieferantDto().getTPersonalFreigabe() != null) {

			String ausg = "";

			ausg += Helper.formatDatum(
					getLieferantDto().getTPersonalFreigabe(), LPMain
							.getTheClient().getLocUi());

			if (getLieferantDto().getPersonalIIdFreigabe() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								getLieferantDto().getPersonalIIdFreigabe());
				ausg += " (" + personalDto.getCKurzzeichen() + ")";
			}

			wlaFreigabePerson.setText(ausg);

		} else {
			wlaFreigabePerson.setText("");
		}

		String sZ = null;
		if (getLieferantDto().getZahlungszielIId() != null) {
			sZ = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(
							getLieferantDto().getZahlungszielIId()).getCBez();
		}
		wtfZahlungsziel.setText(sZ);

		wnfMindestbestellwert.setBigDecimal(getLieferantDto()
				.getNMindestbestellwert());
		wnfJahresbonus.setBigDecimal(getLieferantDto().getNJahrbonus());
		wnfAbUmsatz.setBigDecimal(getLieferantDto().getNAbumsatz());
		wnfMindermengenzuschlag.setBigDecimal(getLieferantDto()
				.getNMindermengenzuschlag());
		wtfHinweistextextern.setText(getLieferantDto().getCHinweisextern());
		wtfHinweistextintern.setText(getLieferantDto().getCHinweisintern());
		wefKommentar.setText(getLieferantDto().getXKommentar());
		wnfTransportkostenJeLieferung.setBigDecimal(getLieferantDto()
				.getNTransportkostenprolieferung());
		wlaBeurteilung.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.letztebeurteilung")
				+ DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantbeurteilungfindByLetzteBeurteilung(
								getLieferantDto().getIId()));

		if (getLieferantDto().getKontoIIdWarenkonto() != null) {
			KontoDtoSmall kontoDtoSmall = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKeySmall(
							getLieferantDto().getKontoIIdWarenkonto());
			wtfKontowaren.setText(kontoDtoSmall.getCNr());
		} else {
			wtfKontowaren.setText(null);
		}
		// Ziellager
		if (getLieferantDto().getPartnerDto().getLagerIIdZiellager() != null) {
			LagerDto lagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							getLieferantDto().getPartnerDto()
									.getLagerIIdZiellager());
			wtfZiellager.setText(lagerDto.getCNr());
		} else {
			wtfZiellager.setText(null);
		}
		if (getLieferantDto().getLieferartIId() != null) {
			LieferartDto lieferartDto = DelegateFactory
					.getInstance()
					.getLocaleDelegate()
					.lieferartFindByPrimaryKey(
							getLieferantDto().getLieferartIId());
			wtfLieferart.setText(lieferartDto.getCNr());
		}

		// Abweichendes UST-Land
		if (getLieferantDto().getPartnerDto().getLandIIdAbweichendesustland() != null) {
			LandDto landDto = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.landFindByPrimaryKey(
							getLieferantDto().getPartnerDto()
									.getLandIIdAbweichendesustland());

			wtfAbweichendesustland.setText(landDto.getCLkz());
		} else {
			wtfAbweichendesustland.setText(null);
		}

		if (DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

			if (getLieferantDto().getKontoIIdKreditorenkonto() != null) {

				KontoDto kontoDto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey(
								getLieferantDto().getKontoIIdKreditorenkonto());

				MandantDto mandant = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getTheClient().getMandant());

				if (kontoDto.getFinanzamtIId().equals(
						mandant.getPartnerIIdFinanzamt())) {
					wtfAbweichendesustland.setText(null);
				} else {
					wtfAbweichendesustland.setText(DelegateFactory
							.getInstance()
							.getFinanzDelegate()
							.finanzamtFindByPrimaryKey(
									kontoDto.getFinanzamtIId(),
									mandant.getCNr()).getPartnerDto()
							.formatName());
				}
			}
		}

		KontoDto k = null;
		if (getLieferantDto().getKontoIIdKreditorenkonto() != null) {
			k = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKey(
							getLieferantDto().getKontoIIdKreditorenkonto());
		}
		Integer ik = null;
		if (k != null && k.getCNr() != null) {
			ik = new Integer(Integer.parseInt(k.getCNr()));
		}
		boolean bhatBuchungenDrauf = false;
		if (getLieferantDto().getKontoIIdKreditorenkonto() != null) {
			bhatBuchungenDrauf = DelegateFactory
					.getInstance()
					.getBuchenDelegate()
					.hatPartnerBuchungenAufKonto(
							getLieferantDto().getPartnerIId(),
							getLieferantDto().getKontoIIdKreditorenkonto());
		}
		// --true: lf hat bereits buchungen drauf
		wtnfKontokreditoren.setActivatable(!bhatBuchungenDrauf);
		wbuKreditorenkontoAutomatisch.setActivatable(!bhatBuchungenDrauf);
		wtnfKontokreditoren.setInteger(ik);

		if (getLieferantDto().getIIdKostenstelle() != null) {
			KostenstelleDto kostenstelleDto = DelegateFactory
					.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(
							getLieferantDto().getIIdKostenstelle());
			wtfKostenstelle.setText(kostenstelleDto.getCBez());
		}

		if (getLieferantDto().getZahlungszielIId() != null) {
			ZahlungszielDto zahlungszielDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.zahlungszielFindByPrimaryKey(
							getLieferantDto().getZahlungszielIId());
			if (zahlungszielDto != null) {
				if (zahlungszielDto.getZahlungszielsprDto() != null) {
					wtfZahlungsziel.setText(zahlungszielDto
							.getZahlungszielsprDto().getCBezeichnung());
				} else {
					wtfZahlungsziel.setText(zahlungszielDto.getCBez());
				}
			} else {
				wtfZahlungsziel.setText(null);
			}
		}

		SpediteurDto s = DelegateFactory.getInstance().getMandantDelegate()
				.spediteurFindByPrimaryKey(getLieferantDto().getIdSpediteur());
		wtfSpedition.setText(s.getCNamedesspediteurs());
		wdfBestellsperream
				.setTimestamp(getLieferantDto().getTBestellsperream());

		wnfKupferzahl.setBigDecimal(getLieferantDto().getNKupferzahl());

		wlaLieferantenWhg0.setText(getLieferantDto().getWaehrungCNr());
		wlaLieferantenWhg1.setText(getLieferantDto().getWaehrungCNr());
		wlaLieferantenWhg2.setText(getLieferantDto().getWaehrungCNr());
		wlaLieferantenWhg3.setText(getLieferantDto().getWaehrungCNr());
		wlaLieferantenWhg4.setText(getLieferantDto().getWaehrungCNr());
		wlaLieferantenWhg5.setText(getLieferantDto().getWaehrungCNr());
		wlaLieferantenWhg6.setText(getLieferantDto().getWaehrungCNr() + "/1kg");
	}

	private LieferantDto getLieferantDto() {

		return getInternalFrameLieferant().getLieferantDto();
	}

	protected void components2Dto() throws Throwable {

		getLieferantDto().setWaehrungCNr(
				(String) wcoWaehrung.getKeyOfSelectedItem());
		getLieferantDto().setNRabatt(wnfRabattsatz.getDouble());
		getLieferantDto().setNKredit(wnfKreditlimit.getBigDecimal());
		getLieferantDto().setBBeurteilen(wcbBeurteilen.getShort());
		getLieferantDto().setBZollimportpapier(wcbZollimportpapiere.getShort());
		getLieferantDto().setNMindestbestellwert(
				wnfMindestbestellwert.getBigDecimal());
		getLieferantDto().setNJahrbonus(wnfJahresbonus.getBigDecimal());
		getLieferantDto().setNAbumsatz(wnfAbUmsatz.getBigDecimal());
		getLieferantDto().setNMindermengenzuschlag(
				wnfMindermengenzuschlag.getBigDecimal());
		getLieferantDto().setCHinweisintern(wtfHinweistextintern.getText());
		getLieferantDto().setCHinweisextern(wtfHinweistextextern.getText());
		getLieferantDto().setXKommentar(wefKommentar.getText());
		getLieferantDto().setIKreditorenkontoAsIntegerNotiId(
				wtnfKontokreditoren.getInteger());
		getLieferantDto()
				.setTBestellsperream(wdfBestellsperream.getTimestamp());
		getLieferantDto().setNTransportkostenprolieferung(
				wnfTransportkostenJeLieferung.getBigDecimal());
		getLieferantDto().setNKupferzahl(wnfKupferzahl.getBigDecimal());
		getLieferantDto().setCFreigabe(wtfFreigabe.getText());
		getLieferantDto().setTFreigabe(wdfFreigabe.getTimestamp());
		getLieferantDto().setLagerIIdZubuchungslager(wsfLager.getIKey());

		getLieferantDto().setBIgErwerb(wcbIgErwerb.isSelected());
		if (bReversecharge == true) {
			getLieferantDto().setBReversecharge(wcbReversecharge.getShort());
		} else {
			getLieferantDto().setBReversecharge(Helper.boolean2Short(false));
		}

	}

	private InternalFrameLieferant getInternalFrameLieferant() {
		return (InternalFrameLieferant) getInternalFrame();
	}

	private void initPanel() throws Throwable {

		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen());

		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_LIEFERANT,
						ParameterFac.PARAMETER_KREDITOREN_VON);

		Integer iiKontoKreditorenVon = (Integer) parametermandantDto
				.getCWertAsObject();

		parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_LIEFERANT,
						ParameterFac.PARAMETER_KREDITOREN_BIS);

		Integer iiKontoKreditorenBis = (Integer) parametermandantDto
				.getCWertAsObject();

		wlaKontokreditorenRangeVonBis.setText(iiKontoKreditorenVon + " - "
				+ iiKontoKreditorenBis);

		wtnfKontokreditoren.setMinimumValue(new Integer(iiKontoKreditorenVon
				.intValue()));
		wtnfKontokreditoren.setMaximumValue(new Integer(iiKontoKreditorenBis
				.intValue()));

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERANT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {

			leereAlleFelder(this);
			Object key = getLieferantDto().getIId();
			if (key == null) {
				throw new Exception("key == null");
			}
			getInternalFrameLieferant().setLieferantDto(
					DelegateFactory.getInstance().getLieferantDelegate()
							.lieferantFindByPrimaryKey((Integer) key));
			initPanel();
			dto2Components();
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getLieferantDto().getPartnerDto()
							.formatFixTitelName1Name2());
			setStatusbar();
		}
	}

	private void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getLieferantDto()
				.getPersonalIIdAnlegen());
		setStatusbarPersonalIIdAendern(getLieferantDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(getLieferantDto().getTAendern());
		setStatusbarTAnlegen(getLieferantDto().getTAnlegen());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			// PJ 16695
			if ((getLieferantDto().getCFreigabe() != null && getLieferantDto()
					.getTFreigabe() == null)
					|| (getLieferantDto().getCFreigabe() == null && getLieferantDto()
							.getTFreigabe() != null)) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("part.lieferant.freigabe.error"));
				return;
			}

			try {
				DelegateFactory.getInstance().getLieferantDelegate()
						.updateLieferant(getLieferantDto());
			} catch (ExceptionLP efc) {
				int iJNC = -1;
				if (efc.getICode() == EJBExceptionLP.WARNUNG_KTO_BESETZT) {
					// einzmehrz: 0
					String sMsg = "";
					String sP = "";
					sP += " | ";
					for (int i = 0; i < efc.getAlInfoForTheClient().size(); i++) {
						// svr2clt: 1 hier werden die Daten gelesen.
						if (sP.length() < 1000) {
							sP += efc.getAlInfoForTheClient().get(i) + " | ";
						} else {
							sP += ".....";
							break;
						}
					}
					Object pattern[] = { sP };
					String sFS[] = {
							LPMain.getInstance().getTextRespectUISPr(
									"part.lf.konto_besetzt_1"),
							LPMain.getInstance().getTextRespectUISPr(
									"part.lf.konto_besetzt_n") };
					double limits[] = { 1, 2 };
					ChoiceFormat cf = new ChoiceFormat(limits, sFS);
					sMsg = cf.format(efc.getAlInfoForTheClient().size() > 1 ? 2
							: 1);
					sMsg = MessageFormat.format(sMsg, pattern);
					iJNC = DialogFactory.showMeldung(sMsg, "",
							JOptionPane.YES_NO_CANCEL_OPTION);
				} else {
					throw efc;
				}
				if (iJNC == javax.swing.JOptionPane.YES_OPTION) {
					getLieferantDto().setUpdateModeKreditorenkonto(
							LieferantDto.I_UPD_KREDITORENKONTO_UPDATE);
					DelegateFactory.getInstance().getLieferantDelegate()
							.updateLieferant(getLieferantDto());
				} else if (iJNC == javax.swing.JOptionPane.NO_OPTION) {
					return;
					// getLieferantDto().setIKreditorenkonto(null);
					// getLieferantDto().setIKreditorenkonto(null);
				} else {
					return;
				}
			}

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			dto2Components();
		}
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(
						getInternalFrame(),
						getLieferantDto().getPartnerDto()
								.getLagerIIdZiellager(), true, false);

		new DialogQuery(panelQueryFLRLager);
	}

	protected void eventItemchanged(EventObject eI) throws Exception, Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					getLieferantDto().setIIdKostenstelle(iId);
					wtfKostenstelle.setText(iId + "");

					KostenstelleDto kostenstelleDto = null;
					kostenstelleDto = DelegateFactory.getInstance()
							.getSystemDelegate()
							.kostenstelleFindByPrimaryKey(iId);
					wtfKostenstelle.setText(kostenstelleDto.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRAbweichendesustland) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandDto landDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.landFindByPrimaryKey((Integer) key);
				getLieferantDto().getPartnerDto()
						.setLandIIdAbweichendesustland(landDto.getIID());
				wtfAbweichendesustland.setText(landDto.getCLkz());
			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfZiellager.setText(lagerDto.getCNr());
				getLieferantDto().getPartnerDto().setLagerIIdZiellager(
						lagerDto.getIId());
			} else if (e.getSource() == panelQueryFLRSpediteur) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getLieferantDto().setIdSpediteur(key);
					SpediteurDto s = DelegateFactory.getInstance()
							.getMandantDelegate()
							.spediteurFindByPrimaryKey(key);

					wtfSpedition.setText(s.getCNamedesspediteurs());
				}
			}

			else if (e.getSource() == panelQueryFLRLieferart) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getLieferantDto().setLieferartIId(key);

					LieferartDto lieferartDto = DelegateFactory.getInstance()
							.getLocaleDelegate().lieferartFindByPrimaryKey(key);
					wtfLieferart.setText(lieferartDto.formatBez());
				}
			}

			else if (e.getSource() == panelQueryFLRZahlungsziel) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getLieferantDto().setZahlungszielIId(key);

					ZahlungszielDto zahlungszielDto = DelegateFactory
							.getInstance().getMandantDelegate()
							.zahlungszielFindByPrimaryKey(key);
					wtfZahlungsziel.setText(zahlungszielDto.getCBez());
				}
			}

			else if (e.getSource() == panelQueryFLRKontowaren) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					getLieferantDto().setKontoIIdWarenkonto(key);

					KontoDto kontoDto = DelegateFactory.getInstance()
							.getFinanzDelegate().kontoFindByPrimaryKey(key);
					wtfKontowaren.setText(kontoDto.getCNr());

				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAbweichendesustland) {
				wtfAbweichendesustland.setText(null);
				getLieferantDto().getPartnerDto()
						.setLandIIdAbweichendesustland(null);
			} else if (e.getSource() == panelQueryFLRLager) {
				wtfZiellager.setText(null);
				getLieferantDto().getPartnerDto().setLagerIIdZiellager(null);
			} else if (e.getSource() == panelQueryFLRKontowaren) {
				wtfKontowaren.setText(null);
				getLieferantDto().setKontoIIdWarenkonto(null);
			}

		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbtKostenstelle;
	}

}
