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
package com.lp.client.partner;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperButtonZusammenfuehren;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperCheckBoxZusammenfuehren;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextAreaZusammenfuehren;
import com.lp.client.frame.component.WrapperTextFieldZusammenfuehren;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um das Zusammenf&uuml;ren von Partnern</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 * 
 * <p>Erstellung: Vorname Nachname; 01.08</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/11/07 09:41:44 $
 */
public class PanelDialogPartnerZusammenfuehren extends PanelDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED
			+ "save";
	private final String ACTION_ZIELPARTNER_AUSWAEHLEN = "ACTION_ZIELPARTNER_AUSWAEHLEN";
	private final String ACTION_QUELLPARTNER_AUSWAEHLEN = "ACTION_QUELLPARTNER_AUSWAEHLEN";
	private final String ACTION_ZIELPARTNERANSPRECHPARTNER_AUSWAEHLEN = "ACTION_ZIELPARTNERANSPRECHPARTNER_AUSWAEHLEN";
	private final String ACTION_QUELLPARTNERANSPRECHPARTNER_AUSWAEHLEN = "ACTION_QUELLPARTNERANSPRECHPARTNER_AUSWAEHLEN";
	private final String ACTION_DATENRICHTUNG_WAEHLEN = "ACTION_DATENRICHTUNG_WAEHLEN";
	private final String ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN = "ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN";
	// mind. alle Texte, die nvarchar > 40 haben, hier definieren bzw. Felder,
	// deren Inhalt kombiniert werden kann
	private final int MAX_PARTNERTITEL = 80;
	private final int MAX_PARTNERFIRMENBUCHNR = 50;
	private final int MAX_BLZ = 11;
	private final int MAX_BIC = 11;
	private final int MAX_PARTNERBEMERKUNG = 3000; // ntext 16 - wie gross ist
	// die max. anzahl an
	// zeichen?
	private final int MAX_ORTNAME = 50;
	private final int MAX_BRANCHENAME = 50;
	private final int MAX_PARTNERKOMMUNIKATIONSBEZ = 80;

	protected JPanel jpaAutomatischZusammenfuehren = null;
	private GridBagLayout gridBagLayoutAutomatischZusammenfuehren = null;

	private JButton saveButton = null;

	// in diesen vector schreiben wir die wrapperbuttonzusammenfuehren, die
	// partnerdetails beinhalten
	private Vector<WrapperButtonZusammenfuehren> vWrapperButtonsZusammenfuehren = new Vector<WrapperButtonZusammenfuehren>();
	private Vector<JComponent> vZielfelder = new Vector<JComponent>();
	private Vector<JComponent> vQuellfelder = new Vector<JComponent>();

	private Vector<JComponent> vPartnerkomponenten = new Vector<JComponent>();
	private Vector<JComponent> vAnsprechpartnerkomponenten = new Vector<JComponent>();

	private PartnerDto partnerZielDto = null;
	private PartnerDto partnerQuellDto = null;
	private AnsprechpartnerDto ansprechpartnerZielDto = null;
	private AnsprechpartnerDto ansprechpartnerQuellDto = null;
	private PartnerDto partnerVonAnsprechpartnerZielDto = null;

	private String sActionToDoBeimZusammenfuehren = PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_PARTNER;

	private PanelQueryFLR panelPartner = null;
	private PanelQueryFLR panelAnsprechpartner = null;
	protected boolean bHandleEventInSuperklasse = true;

	protected JPanel jpaWorkingOnScroll = null;
	private JScrollPane jScrollPanePartner = null;

	private WrapperButton wbuZielPartnerAuswaehlen = null;
	private WrapperButton wbuQuellPartnerAuswaehlen = null;
	private WrapperButton wbuZielPartnerAnsprechpartnerAuswaehlen = null;
	private WrapperButton wbuQuellPartnerAnsprechpartnerAuswaehlen = null;

	private WrapperButtonZusammenfuehren wbuAuswahlReset = null;

	private WrapperLabel wlaAnrede = null;
	private WrapperButtonZusammenfuehren wbuAnredeDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerAnrede = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerAnrede = null;

	private WrapperLabel wlaTitel = null;
	private WrapperButtonZusammenfuehren wbuTitelDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerTitel = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerTitel = null;

	private WrapperLabel wlaName1 = null;
	private WrapperButtonZusammenfuehren wbuName1Datenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerName1 = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerName1 = null;

	private WrapperLabel wlaName2 = null;
	private WrapperButtonZusammenfuehren wbuName2Datenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerName2 = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerName2 = null;

	private WrapperLabel wlaName3 = null;
	private WrapperButtonZusammenfuehren wbuName3Datenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerName3 = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerName3 = null;

	private WrapperLabel wlaStrasse = null;
	private WrapperButtonZusammenfuehren wbuStrasseDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerStrasse = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerStrasse = null;

	private WrapperLabel wlaOrt = null;
	private WrapperButtonZusammenfuehren wbuOrtDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerOrt = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerOrt = null;

	private WrapperLabel wlaPostfachort = null;
	private WrapperButtonZusammenfuehren wbuPostfachortDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerPostfachort = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerPostfachort = null;

	private WrapperLabel wlaPostfachnr = null;
	private WrapperButtonZusammenfuehren wbuPostfachnrDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerPostfachnr = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerPostfachnr = null;

	private WrapperLabel wlaKurzbez = null;
	private WrapperButtonZusammenfuehren wbuKurzbezDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerKurzbez = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerKurzbez = null;

	private WrapperLabel wlaPartnerart = null;
	private WrapperButtonZusammenfuehren wbuPartnerartDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerPartnerart = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerPartnerart = null;

	private WrapperLabel wlaUid = null;
	private WrapperButtonZusammenfuehren wbuUidDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerUid = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerUid = null;

	private WrapperLabel wlaKommunikationsspr = null;
	private WrapperButtonZusammenfuehren wbuKommunikationssprDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerKommunikationsspr = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerKommunikationsspr = null;

	private WrapperLabel wlaIln = null;
	private WrapperButtonZusammenfuehren wbuIlnDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerIln = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerIln = null;

	private WrapperLabel wlaFilialnr = null;
	private WrapperButtonZusammenfuehren wbuFilialnrDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerFilialnr = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerFilialnr = null;

	private WrapperLabel wlaGerichtsstand = null;
	private WrapperButtonZusammenfuehren wbuGerichtsstandDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerGerichtsstand = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerGerichtsstand = null;

	private WrapperLabel wlaFirmenbuchnr = null;
	private WrapperButtonZusammenfuehren wbuFirmenbuchnrDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerFirmenbuchnr = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerFirmenbuchnr = null;

	private WrapperLabel wlaPartnerklasse = null;
	private WrapperButtonZusammenfuehren wbuPartnerklasseDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerPartnerklasse = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerPartnerklasse = null;

	private WrapperLabel wlaBranche = null;
	private WrapperButtonZusammenfuehren wbuBrancheDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerBranche = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerBranche = null;

	private WrapperLabel wlaTelefon = null;
	private WrapperButtonZusammenfuehren wbuTelefonDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerTelefon = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerTelefon = null;

	private WrapperLabel wlaHandy = null;
	private WrapperButtonZusammenfuehren wbuHandyDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerHandy = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerHandy = null;

	private WrapperLabel wlaEmail = null;
	private WrapperButtonZusammenfuehren wbuEmailDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerEmail = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerEmail = null;

	private WrapperLabel wlaFax = null;
	private WrapperButtonZusammenfuehren wbuFaxDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerFax = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerFax = null;

	private WrapperLabel wlaHomepage = null;
	private WrapperButtonZusammenfuehren wbuHomepageDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerHomepage = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerHomepage = null;

	private WrapperLabel wlaDirektfax = null;
	private WrapperButtonZusammenfuehren wbuDirektfaxDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerDirektfax = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerDirektfax = null;

	private WrapperLabel wlaPartnerbemerkung = null;
	private WrapperButtonZusammenfuehren wbuPartnerbemerkungDatenrichtung = null;
	private WrapperTextAreaZusammenfuehren wtaZielPartnerPartnerbemerkung = null;
	private WrapperTextAreaZusammenfuehren wtaQuellPartnerPartnerbemerkung = null;
	private JScrollPane jScrollPaneZielPartnerBemerkung = null;
	private JScrollPane jScrollPaneQuellPartnerBemerkung = null;

	private WrapperLabel wlaAnsprGeburtsdatum = null;
	private WrapperButtonZusammenfuehren wbuAnsprGeburtsdatumDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerAnsprGeburtsdatum = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerAnsprGeburtsdatum = null;

	private WrapperLabel wlaAnsprechpartnerfunktion = null;
	private WrapperButtonZusammenfuehren wbuAnsprechpartnerfunktionDatenrichtung = null;
	private WrapperTextFieldZusammenfuehren wtfZielPartnerAnsprechpartnerfunktion = null;
	private WrapperTextFieldZusammenfuehren wtfQuellPartnerAnsprechpartnerfunktion = null;

	private WrapperLabel wlaBLZ = null;
	private WrapperButtonZusammenfuehren wbuBLZ = null;
	private WrapperTextFieldZusammenfuehren wtfZielBLZ = null;
	private WrapperTextFieldZusammenfuehren wtfQuellBLZ = null;

	private WrapperLabel wlaBIC = null;
	private WrapperButtonZusammenfuehren wbuBIC = null;
	private WrapperTextFieldZusammenfuehren wtfZielBIC = null;
	private WrapperTextFieldZusammenfuehren wtfQuellBIC = null;

	private WrapperLabel wlaAutomatischZusammenfuehren = null;
	private WrapperCheckBox wcbKundeZusammenfuehren = null;
	private WrapperCheckBox wcbLieferantZusammenfuehren = null;

	// private WrapperCheckBox wcbPersonalZusammenfuehren = null;
	// private WrapperCheckBox wcbBankZusammenfuehren = null;

	public PanelDialogPartnerZusammenfuehren(PartnerDto partnerZielDto,
			InternalFrame internalFrame, String add2TitleI) throws Throwable {

		super(internalFrame, add2TitleI + " | "
				+ partnerZielDto.formatFixTitelName1Name2());

		partnerZielDto.setBankDto(DelegateFactory.getInstance()
				.getPartnerbankDelegate().bankFindByPrimaryKeyOhneExc(
						partnerZielDto.getIId()));

		this.partnerZielDto = partnerZielDto;
		jbInit(); // zuerst QuellFelder leer, erst nach Quellauswahl Werte drin
		initComponents();
		setSaveButtonStatus();
	}

	private void setSaveButtonStatus() throws Throwable {
		if (this.partnerQuellDto != null && this.partnerZielDto != null) {
			this.saveButton.setEnabled(true);
		} else {
			this.saveButton.setEnabled(false);
		}
	}

	private void jbInit() throws Throwable {

		jScrollPanePartner = new javax.swing.JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		jpaWorkingOnScroll = new JPanel();
		jpaWorkingOnScroll.setLayout(new GridBagLayout());

		// Ziel-Partner-Auswahlbutton
		wbuZielPartnerAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuZielPartnerAuswaehlen.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.partner")
				+ "...");
		wbuZielPartnerAuswaehlen.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("title.partnerauswahlliste"));
		wbuZielPartnerAuswaehlen.addActionListener(this);
		wbuZielPartnerAuswaehlen
				.setActionCommand(ACTION_ZIELPARTNER_AUSWAEHLEN);
		wbuZielPartnerAuswaehlen.setMaximumSize(new Dimension(125, Defaults
				.getInstance().getControlHeight()));
		wbuZielPartnerAuswaehlen.setPreferredSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wbuZielPartnerAuswaehlen.setMinimumSize(new Dimension(110, Defaults
				.getInstance().getControlHeight()));
		wbuZielPartnerAuswaehlen.setFocusable(true);

		// Quell-Partner-Auswahlbutton
		wbuQuellPartnerAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuQuellPartnerAuswaehlen.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.partner")
				+ "...");
		wbuQuellPartnerAuswaehlen.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("title.partnerauswahlliste"));
		wbuQuellPartnerAuswaehlen.addActionListener(this);
		wbuQuellPartnerAuswaehlen
				.setActionCommand(ACTION_QUELLPARTNER_AUSWAEHLEN);
		wbuQuellPartnerAuswaehlen.setMaximumSize(new Dimension(125, Defaults
				.getInstance().getControlHeight()));
		wbuQuellPartnerAuswaehlen.setPreferredSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wbuQuellPartnerAuswaehlen.setMinimumSize(new Dimension(110, Defaults
				.getInstance().getControlHeight()));
		wbuQuellPartnerAuswaehlen.setFocusable(true);

		// ZielPartneransprechpartner-Auswahlbutton
		wbuZielPartnerAnsprechpartnerAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuZielPartnerAnsprechpartnerAuswaehlen.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.ansprechpartner")
				+ "...");
		wbuZielPartnerAnsprechpartnerAuswaehlen.setToolTipText(LPMain
				.getInstance().getTextRespectUISPr(
						"title.ansprechpartnerauswahlliste"));
		wbuZielPartnerAnsprechpartnerAuswaehlen.addActionListener(this);
		wbuZielPartnerAnsprechpartnerAuswaehlen
				.setActionCommand(ACTION_ZIELPARTNERANSPRECHPARTNER_AUSWAEHLEN);
		wbuZielPartnerAnsprechpartnerAuswaehlen.setMaximumSize(new Dimension(
				125, Defaults.getInstance().getControlHeight()));
		wbuZielPartnerAnsprechpartnerAuswaehlen.setPreferredSize(new Dimension(
				115, Defaults.getInstance().getControlHeight()));
		wbuZielPartnerAnsprechpartnerAuswaehlen.setMinimumSize(new Dimension(
				110, Defaults.getInstance().getControlHeight()));
		wbuZielPartnerAnsprechpartnerAuswaehlen.setEnabled(false);
		wbuZielPartnerAnsprechpartnerAuswaehlen.setActivatable(false);

		// QuellPartneransprechpartner-Auswahlbutton
		wbuQuellPartnerAnsprechpartnerAuswaehlen = new WrapperButtonZusammenfuehren();
		wbuQuellPartnerAnsprechpartnerAuswaehlen.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.ansprechpartner")
				+ "...");
		wbuQuellPartnerAnsprechpartnerAuswaehlen.setToolTipText(LPMain
				.getInstance().getTextRespectUISPr(
						"title.ansprechpartnerauswahlliste"));
		wbuQuellPartnerAnsprechpartnerAuswaehlen.addActionListener(this);
		wbuQuellPartnerAnsprechpartnerAuswaehlen
				.setActionCommand(ACTION_QUELLPARTNERANSPRECHPARTNER_AUSWAEHLEN);
		wbuQuellPartnerAnsprechpartnerAuswaehlen.setMaximumSize(new Dimension(
				125, Defaults.getInstance().getControlHeight()));
		wbuQuellPartnerAnsprechpartnerAuswaehlen
				.setPreferredSize(new Dimension(115, Defaults.getInstance()
						.getControlHeight()));
		wbuQuellPartnerAnsprechpartnerAuswaehlen.setMinimumSize(new Dimension(
				110, Defaults.getInstance().getControlHeight()));
		wbuQuellPartnerAnsprechpartnerAuswaehlen.setEnabled(false);
		wbuQuellPartnerAnsprechpartnerAuswaehlen.setActivatable(false);

		// AuswahlReset der Datenrichtung
		wbuAuswahlReset = new WrapperButtonZusammenfuehren();
		wbuAuswahlReset.setButtonIsAlle(true);
		wbuAuswahlReset.addActionListener(this);
		wbuAuswahlReset.setActionCommand(ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN);

		// Anrede
		wlaAnrede = new WrapperLabel();
		wlaAnrede.setText(LPMain.getInstance().getTextRespectUISPr("lp.anrede")
				+ ": ");
		wlaAnrede.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaAnrede.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAnrede.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaAnrede);
		vAnsprechpartnerkomponenten.addElement(wlaAnrede);

		wbuAnredeDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuAnredeDatenrichtung.addActionListener(this);
		wbuAnredeDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuAnredeDatenrichtung);
		vPartnerkomponenten.addElement(wbuAnredeDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuAnredeDatenrichtung);

		wtfZielPartnerAnrede = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerAnrede.setText("");
		wtfZielPartnerAnrede.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerAnrede.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerAnrede.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerAnrede.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerAnrede);
		vPartnerkomponenten.addElement(wtfZielPartnerAnrede);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerAnrede);

		wtfQuellPartnerAnrede = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerAnrede.setText("");
		wtfQuellPartnerAnrede.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerAnrede.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerAnrede.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerAnrede.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerAnrede);
		vPartnerkomponenten.addElement(wtfQuellPartnerAnrede);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerAnrede);

		// Titel
		wlaTitel = new WrapperLabel();
		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr("lp.titel")
				+ ": ");
		wlaTitel.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaTitel.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaTitel.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaTitel);
		vAnsprechpartnerkomponenten.addElement(wlaTitel);

		wbuTitelDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuTitelDatenrichtung.addActionListener(this);
		wbuTitelDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuTitelDatenrichtung);
		vPartnerkomponenten.addElement(wbuTitelDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuTitelDatenrichtung);

		wtfZielPartnerTitel = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerTitel.setText("");
		wtfZielPartnerTitel.setColumnsMax(MAX_PARTNERTITEL);
		wtfZielPartnerTitel.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerTitel.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerTitel.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerTitel.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerTitel);
		vPartnerkomponenten.addElement(wtfZielPartnerTitel);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerTitel);

		wtfQuellPartnerTitel = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerTitel.setText("");
		wtfQuellPartnerTitel.setColumnsMax(MAX_PARTNERTITEL);
		wtfQuellPartnerTitel.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerTitel.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerTitel.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerTitel.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerTitel);
		vPartnerkomponenten.addElement(wtfQuellPartnerTitel);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerTitel);

		// Name1
		wlaName1 = new WrapperLabel();
		wlaName1.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.firma_nachname")
				+ ": ");
		wlaName1.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaName1.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaName1.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaName1);
		vAnsprechpartnerkomponenten.addElement(wlaName1);

		wbuName1Datenrichtung = new WrapperButtonZusammenfuehren();
		wbuName1Datenrichtung.addActionListener(this);
		wbuName1Datenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuName1Datenrichtung);
		vPartnerkomponenten.addElement(wbuName1Datenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuName1Datenrichtung);

		wtfZielPartnerName1 = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerName1.setText("");
		wtfZielPartnerName1.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName1.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName1.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName1.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerName1);
		vPartnerkomponenten.addElement(wtfZielPartnerName1);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerName1);

		wtfQuellPartnerName1 = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerName1.setText("");
		wtfQuellPartnerName1.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName1.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName1.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName1.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerName1);
		vPartnerkomponenten.addElement(wtfQuellPartnerName1);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerName1);

		// Name2
		wlaName2 = new WrapperLabel();
		wlaName2.setText(LPMain.getInstance().getTextRespectUISPr("lp.name")
				+ ": "); // hole Text aus messages.properties
		wlaName2.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaName2.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaName2.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaName2);
		vAnsprechpartnerkomponenten.addElement(wlaName2);

		wbuName2Datenrichtung = new WrapperButtonZusammenfuehren();
		wbuName2Datenrichtung.addActionListener(this);
		wbuName2Datenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuName2Datenrichtung);
		vPartnerkomponenten.addElement(wbuName2Datenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuName2Datenrichtung);

		wtfZielPartnerName2 = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerName2.setText("");
		wtfZielPartnerName2.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName2.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName2.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName2.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerName2);
		vPartnerkomponenten.addElement(wtfZielPartnerName2);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerName2);

		wtfQuellPartnerName2 = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerName2.setText("");
		wtfQuellPartnerName2.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName2.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName2.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName2.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerName2);
		vPartnerkomponenten.addElement(wtfQuellPartnerName2);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerName2);

		// Name 3
		wlaName3 = new WrapperLabel();
		wlaName3.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abteilung")
				+ ": ");
		wlaName3.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaName3.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaName3.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaName3);

		wbuName3Datenrichtung = new WrapperButtonZusammenfuehren();
		wbuName3Datenrichtung.addActionListener(this);
		wbuName3Datenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuName3Datenrichtung);
		vPartnerkomponenten.addElement(wbuName3Datenrichtung);

		wtfZielPartnerName3 = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerName3.setText("");
		wtfZielPartnerName3.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName3.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName3.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerName3.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerName3);
		vPartnerkomponenten.addElement(wtfZielPartnerName3);

		wtfQuellPartnerName3 = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerName3.setText("");
		wtfQuellPartnerName3.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName3.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName3.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerName3.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerName3);
		vPartnerkomponenten.addElement(wtfQuellPartnerName3);

		// Strasse
		wlaStrasse = new WrapperLabel();
		wlaStrasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.strasse")
				+ ": ");
		wlaStrasse.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaStrasse.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaStrasse.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaStrasse);

		wbuStrasseDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuStrasseDatenrichtung.addActionListener(this);
		wbuStrasseDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuStrasseDatenrichtung);
		vPartnerkomponenten.addElement(wbuStrasseDatenrichtung);

		wtfZielPartnerStrasse = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerStrasse.setText("");
		wtfZielPartnerStrasse.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerStrasse.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerStrasse.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerStrasse.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerStrasse);
		vPartnerkomponenten.addElement(wtfZielPartnerStrasse);

		wtfQuellPartnerStrasse = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerStrasse.setText("");
		wtfQuellPartnerStrasse.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerStrasse.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerStrasse.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerStrasse.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerStrasse);
		vPartnerkomponenten.addElement(wtfQuellPartnerStrasse);

		// Ort
		wlaOrt = new WrapperLabel();
		wlaOrt.setText(LPMain.getInstance().getTextRespectUISPr("lp.label.ort")
				+ ": ");
		wlaOrt.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaOrt.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaOrt.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaOrt);

		wbuOrtDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuOrtDatenrichtung.addActionListener(this);
		wbuOrtDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuOrtDatenrichtung);
		vPartnerkomponenten.addElement(wbuOrtDatenrichtung);

		wtfZielPartnerOrt = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerOrt.setText("");
		wtfZielPartnerOrt.setColumnsMax(MAX_ORTNAME);
		wtfZielPartnerOrt.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerOrt.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerOrt.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerOrt.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerOrt);
		vPartnerkomponenten.addElement(wtfZielPartnerOrt);

		wtfQuellPartnerOrt = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerOrt.setText("");
		wtfQuellPartnerOrt.setColumnsMax(MAX_ORTNAME);
		wtfQuellPartnerOrt.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerOrt.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerOrt.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerOrt.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerOrt);
		vPartnerkomponenten.addElement(wtfQuellPartnerOrt);

		// Postfachort
		wlaPostfachort = new WrapperLabel();
		wlaPostfachort.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.postfach")
				+ ": ");
		wlaPostfachort.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaPostfachort.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaPostfachort.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaPostfachort);

		wbuPostfachortDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuPostfachortDatenrichtung.addActionListener(this);
		wbuPostfachortDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuPostfachortDatenrichtung);
		vPartnerkomponenten.addElement(wbuPostfachortDatenrichtung);

		wtfZielPartnerPostfachort = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerPostfachort.setText("");
		wtfZielPartnerPostfachort.setColumnsMax(MAX_ORTNAME);
		wtfZielPartnerPostfachort.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPostfachort.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPostfachort.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPostfachort.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerPostfachort);
		vPartnerkomponenten.addElement(wtfZielPartnerPostfachort);

		wtfQuellPartnerPostfachort = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerPostfachort.setText("");
		wtfQuellPartnerPostfachort.setColumnsMax(MAX_ORTNAME);
		wtfQuellPartnerPostfachort.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPostfachort.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPostfachort.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPostfachort.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerPostfachort);
		vPartnerkomponenten.addElement(wtfQuellPartnerPostfachort);

		// Postfachnr
		wlaPostfachnr = new WrapperLabel();
		wlaPostfachnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.postfach.nr")
				+ ": ");
		wlaPostfachnr.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaPostfachnr.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaPostfachnr.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaPostfachnr);

		wbuPostfachnrDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuPostfachnrDatenrichtung.addActionListener(this);
		wbuPostfachnrDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuPostfachnrDatenrichtung);
		vPartnerkomponenten.addElement(wbuPostfachnrDatenrichtung);

		wtfZielPartnerPostfachnr = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerPostfachnr.setText("");
		wtfZielPartnerPostfachnr.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPostfachnr.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPostfachnr.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPostfachnr.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerPostfachnr);
		vPartnerkomponenten.addElement(wtfZielPartnerPostfachnr);

		wtfQuellPartnerPostfachnr = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerPostfachnr.setText("");
		wtfQuellPartnerPostfachnr.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPostfachnr.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPostfachnr.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPostfachnr.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerPostfachnr);
		vPartnerkomponenten.addElement(wtfQuellPartnerPostfachnr);

		// Kurzbezeichnung
		wlaKurzbez = new WrapperLabel();
		wlaKurzbez.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kurzbez")
				+ ": ");
		wlaKurzbez.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaKurzbez.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaKurzbez.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaKurzbez);

		wbuKurzbezDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuKurzbezDatenrichtung.addActionListener(this);
		wbuKurzbezDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuKurzbezDatenrichtung);
		vPartnerkomponenten.addElement(wbuKurzbezDatenrichtung);

		wtfZielPartnerKurzbez = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerKurzbez.setText("");
		wtfZielPartnerKurzbez.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerKurzbez.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerKurzbez.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerKurzbez.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerKurzbez);
		vPartnerkomponenten.addElement(wtfZielPartnerKurzbez);

		wtfQuellPartnerKurzbez = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerKurzbez.setText("");
		wtfQuellPartnerKurzbez.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerKurzbez.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerKurzbez.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerKurzbez.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerKurzbez);
		vPartnerkomponenten.addElement(wtfQuellPartnerKurzbez);

		// Partnerart
		wlaPartnerart = new WrapperLabel();
		wlaPartnerart.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.partnerart")
				+ ": ");
		wlaPartnerart.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaPartnerart.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaPartnerart.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaPartnerart);

		wbuPartnerartDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuPartnerartDatenrichtung.addActionListener(this);
		wbuPartnerartDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuPartnerartDatenrichtung);
		vPartnerkomponenten.addElement(wbuPartnerartDatenrichtung);

		wtfZielPartnerPartnerart = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerPartnerart.setText("");
		wtfZielPartnerPartnerart.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPartnerart.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPartnerart.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPartnerart.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerPartnerart);
		vPartnerkomponenten.addElement(wtfZielPartnerPartnerart);

		wtfQuellPartnerPartnerart = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerPartnerart.setText("");
		wtfQuellPartnerPartnerart.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPartnerart.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPartnerart.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPartnerart.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerPartnerart);
		vPartnerkomponenten.addElement(wtfQuellPartnerPartnerart);

		// UID
		wlaUid = new WrapperLabel();
		wlaUid.setText(LPMain.getInstance().getTextRespectUISPr("lp.uid")
				+ ": ");
		wlaUid.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaUid.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaUid.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaUid);

		wbuUidDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuUidDatenrichtung.addActionListener(this);
		wbuUidDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuUidDatenrichtung);
		vPartnerkomponenten.addElement(wbuUidDatenrichtung);

		wtfZielPartnerUid = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerUid.setText("");
		wtfZielPartnerUid.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerUid.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerUid.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerUid.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerUid);
		vPartnerkomponenten.addElement(wtfZielPartnerUid);

		wtfQuellPartnerUid = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerUid.setText("");
		wtfQuellPartnerUid.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerUid.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerUid.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerUid.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerUid);
		vPartnerkomponenten.addElement(wtfQuellPartnerUid);

		// Kommunikatinosspr
		wlaKommunikationsspr = new WrapperLabel();
		wlaKommunikationsspr.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.sprache.kommunikation")
				+ ": ");
		wlaKommunikationsspr.setMaximumSize(new Dimension(130, Defaults
				.getInstance().getControlHeight()));
		wlaKommunikationsspr.setPreferredSize(new Dimension(125, Defaults
				.getInstance().getControlHeight()));
		wlaKommunikationsspr.setMinimumSize(new Dimension(125, Defaults
				.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wlaKommunikationsspr);

		wbuKommunikationssprDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuKommunikationssprDatenrichtung.addActionListener(this);
		wbuKommunikationssprDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren
				.addElement(wbuKommunikationssprDatenrichtung);
		vPartnerkomponenten.addElement(wbuKommunikationssprDatenrichtung);

		wtfZielPartnerKommunikationsspr = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerKommunikationsspr.setText("");
		wtfZielPartnerKommunikationsspr.setMaximumSize(new Dimension(180,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerKommunikationsspr.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerKommunikationsspr.setMinimumSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerKommunikationsspr.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerKommunikationsspr);
		vPartnerkomponenten.addElement(wtfZielPartnerKommunikationsspr);

		wtfQuellPartnerKommunikationsspr = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerKommunikationsspr.setText("");
		wtfQuellPartnerKommunikationsspr.setMaximumSize(new Dimension(180,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerKommunikationsspr.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerKommunikationsspr.setMinimumSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerKommunikationsspr.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerKommunikationsspr);
		vPartnerkomponenten.addElement(wtfQuellPartnerKommunikationsspr);

		// ILN
		wlaIln = new WrapperLabel();
		wlaIln.setText(LPMain.getInstance().getTextRespectUISPr("lp.iln")
				+ ": ");
		wlaIln.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaIln.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaIln.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaIln);

		wbuIlnDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuIlnDatenrichtung.addActionListener(this);
		wbuIlnDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuIlnDatenrichtung);
		vPartnerkomponenten.addElement(wbuIlnDatenrichtung);

		wtfZielPartnerIln = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerIln.setText("");
		wtfZielPartnerIln.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerIln.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerIln.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerIln.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerIln);
		vPartnerkomponenten.addElement(wtfZielPartnerIln);

		wtfQuellPartnerIln = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerIln.setText("");
		wtfQuellPartnerIln.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerIln.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerIln.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerIln.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerIln);
		vPartnerkomponenten.addElement(wtfQuellPartnerIln);

		// Filialnr
		wlaFilialnr = new WrapperLabel();
		wlaFilialnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.filialnr")
				+ ": ");
		wlaFilialnr.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaFilialnr.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaFilialnr.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaFilialnr);

		wbuFilialnrDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuFilialnrDatenrichtung.addActionListener(this);
		wbuFilialnrDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuFilialnrDatenrichtung);
		vPartnerkomponenten.addElement(wbuFilialnrDatenrichtung);

		wtfZielPartnerFilialnr = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerFilialnr.setText("");
		wtfZielPartnerFilialnr.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFilialnr.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFilialnr.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFilialnr.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerFilialnr);
		vPartnerkomponenten.addElement(wtfZielPartnerFilialnr);

		wtfQuellPartnerFilialnr = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerFilialnr.setText("");
		wtfQuellPartnerFilialnr.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFilialnr.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFilialnr.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFilialnr.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerFilialnr);
		vPartnerkomponenten.addElement(wtfQuellPartnerFilialnr);

		// Gerichtsstand
		wlaGerichtsstand = new WrapperLabel();
		wlaGerichtsstand.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gerichtsstand")
				+ ": ");
		wlaGerichtsstand.setMaximumSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wlaGerichtsstand.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaGerichtsstand.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wlaGerichtsstand);

		wbuGerichtsstandDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuGerichtsstandDatenrichtung.addActionListener(this);
		wbuGerichtsstandDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren
				.addElement(wbuGerichtsstandDatenrichtung);
		vPartnerkomponenten.addElement(wbuGerichtsstandDatenrichtung);

		wtfZielPartnerGerichtsstand = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerGerichtsstand.setText("");
		wtfZielPartnerGerichtsstand.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerGerichtsstand.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerGerichtsstand.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerGerichtsstand.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerGerichtsstand);
		vPartnerkomponenten.addElement(wtfZielPartnerGerichtsstand);

		wtfQuellPartnerGerichtsstand = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerGerichtsstand.setText("");
		wtfQuellPartnerGerichtsstand.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerGerichtsstand.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerGerichtsstand.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerGerichtsstand.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerGerichtsstand);
		vPartnerkomponenten.addElement(wtfQuellPartnerGerichtsstand);

		// Firmenbuchnr
		wlaFirmenbuchnr = new WrapperLabel();
		wlaFirmenbuchnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.firmenbuchnr")
				+ ": ");
		wlaFirmenbuchnr.setMaximumSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wlaFirmenbuchnr.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaFirmenbuchnr.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wlaFirmenbuchnr);

		wbuFirmenbuchnrDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuFirmenbuchnrDatenrichtung.addActionListener(this);
		wbuFirmenbuchnrDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuFirmenbuchnrDatenrichtung);
		vPartnerkomponenten.addElement(wbuFirmenbuchnrDatenrichtung);

		wtfZielPartnerFirmenbuchnr = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerFirmenbuchnr.setText("");
		wtfZielPartnerFirmenbuchnr.setColumnsMax(MAX_PARTNERFIRMENBUCHNR);
		wtfZielPartnerFirmenbuchnr.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFirmenbuchnr.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFirmenbuchnr.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFirmenbuchnr.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerFirmenbuchnr);
		vPartnerkomponenten.addElement(wtfZielPartnerFirmenbuchnr);

		wtfQuellPartnerFirmenbuchnr = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerFirmenbuchnr.setText("");
		wtfQuellPartnerFirmenbuchnr.setColumnsMax(MAX_PARTNERFIRMENBUCHNR);
		wtfQuellPartnerFirmenbuchnr.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFirmenbuchnr.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerFirmenbuchnr.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFirmenbuchnr.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerFirmenbuchnr);
		vPartnerkomponenten.addElement(wtfQuellPartnerFirmenbuchnr);

		// Partnerklasse
		wlaPartnerklasse = new WrapperLabel();
		wlaPartnerklasse.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.partnerklasse.label")
				+ ": ");
		wlaPartnerklasse.setMaximumSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wlaPartnerklasse.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaPartnerklasse.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wlaPartnerklasse);

		wbuPartnerklasseDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuPartnerklasseDatenrichtung.addActionListener(this);
		wbuPartnerklasseDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren
				.addElement(wbuPartnerklasseDatenrichtung);
		vPartnerkomponenten.addElement(wbuPartnerklasseDatenrichtung);

		wtfZielPartnerPartnerklasse = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerPartnerklasse.setText("");
		wtfZielPartnerPartnerklasse.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPartnerklasse.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerPartnerklasse.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerPartnerklasse.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerPartnerklasse);
		vPartnerkomponenten.addElement(wtfZielPartnerPartnerklasse);

		wtfQuellPartnerPartnerklasse = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerPartnerklasse.setText("");
		wtfQuellPartnerPartnerklasse.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPartnerklasse.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerPartnerklasse.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerPartnerklasse.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerPartnerklasse);
		vPartnerkomponenten.addElement(wtfQuellPartnerPartnerklasse);

		// Branche
		wlaBranche = new WrapperLabel();
		wlaBranche.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.branche")
				+ ": ");
		wlaBranche.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaBranche.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaBranche.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaBranche);

		wbuBrancheDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuBrancheDatenrichtung.addActionListener(this);
		wbuBrancheDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuBrancheDatenrichtung);
		vPartnerkomponenten.addElement(wbuBrancheDatenrichtung);

		wtfZielPartnerBranche = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerBranche.setText("");
		wtfZielPartnerBranche.setColumnsMax(MAX_BRANCHENAME);
		wtfZielPartnerBranche.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerBranche.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerBranche.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerBranche.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerBranche);
		vPartnerkomponenten.addElement(wtfZielPartnerBranche);

		wtfQuellPartnerBranche = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerBranche.setText("");
		wtfQuellPartnerBranche.setColumnsMax(MAX_BRANCHENAME);
		wtfQuellPartnerBranche.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerBranche.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerBranche.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerBranche.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerBranche);
		vPartnerkomponenten.addElement(wtfQuellPartnerBranche);

		// Telefon
		wlaTelefon = new WrapperLabel();
		wlaTelefon.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.telefon")
				+ ": ");
		wlaTelefon.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaTelefon.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaTelefon.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaTelefon);
		vAnsprechpartnerkomponenten.addElement(wlaTelefon);

		wbuTelefonDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuTelefonDatenrichtung.addActionListener(this);
		wbuTelefonDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuTelefonDatenrichtung);
		vPartnerkomponenten.addElement(wbuTelefonDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuTelefonDatenrichtung);

		wtfZielPartnerTelefon = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerTelefon.setText("");
		wtfZielPartnerTelefon.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfZielPartnerTelefon.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerTelefon.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerTelefon.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerTelefon.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerTelefon);
		vPartnerkomponenten.addElement(wtfZielPartnerTelefon);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerTelefon);

		wtfQuellPartnerTelefon = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerTelefon.setText("");
		wtfQuellPartnerTelefon.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfQuellPartnerTelefon.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerTelefon.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerTelefon.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerTelefon.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerTelefon);
		vPartnerkomponenten.addElement(wtfQuellPartnerTelefon);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerTelefon);

		// Handy
		wlaHandy = new WrapperLabel();
		wlaHandy.setText(LPMain.getInstance().getTextRespectUISPr("lp.handy")
				+ ": ");
		wlaHandy.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaHandy.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaHandy.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vAnsprechpartnerkomponenten.addElement(wlaHandy);

		wbuHandyDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuHandyDatenrichtung.addActionListener(this);
		wbuHandyDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuHandyDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuHandyDatenrichtung);

		wtfZielPartnerHandy = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerHandy.setText("");
		wtfZielPartnerHandy.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfZielPartnerHandy.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerHandy.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerHandy.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerHandy.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerHandy);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerHandy);

		wtfQuellPartnerHandy = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerHandy.setText("");
		wtfQuellPartnerHandy.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfQuellPartnerHandy.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerHandy.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerHandy.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerHandy.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerHandy);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerHandy);

		// Email
		wlaEmail = new WrapperLabel();
		wlaEmail.setText(LPMain.getInstance().getTextRespectUISPr("lp.email")
				+ ": ");
		wlaEmail.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaEmail.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaEmail.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaEmail);
		vAnsprechpartnerkomponenten.addElement(wlaEmail);

		wbuEmailDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuEmailDatenrichtung.addActionListener(this);
		wbuEmailDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuEmailDatenrichtung);
		vPartnerkomponenten.addElement(wbuEmailDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuEmailDatenrichtung);

		wtfZielPartnerEmail = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerEmail.setText("");
		wtfZielPartnerEmail.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfZielPartnerEmail.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerEmail.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerEmail.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerEmail.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerEmail);
		vPartnerkomponenten.addElement(wtfZielPartnerEmail);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerEmail);

		wtfQuellPartnerEmail = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerEmail.setText("");
		wtfQuellPartnerEmail.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfQuellPartnerEmail.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerEmail.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerEmail.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerEmail.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerEmail);
		vPartnerkomponenten.addElement(wtfQuellPartnerEmail);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerEmail);

		// Fax
		wlaFax = new WrapperLabel();
		wlaFax.setText(LPMain.getInstance().getTextRespectUISPr("lp.fax")
				+ ": ");
		wlaFax.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaFax.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaFax.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaFax);
		vAnsprechpartnerkomponenten.addElement(wlaFax);

		wbuFaxDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuFaxDatenrichtung.addActionListener(this);
		wbuFaxDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuFaxDatenrichtung);
		vPartnerkomponenten.addElement(wbuFaxDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuFaxDatenrichtung);

		wtfZielPartnerFax = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerFax.setText("");
		wtfZielPartnerFax.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfZielPartnerFax.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFax.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFax.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerFax.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerFax);
		vPartnerkomponenten.addElement(wtfZielPartnerFax);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerFax);

		wtfQuellPartnerFax = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerFax.setText("");
		wtfQuellPartnerFax.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfQuellPartnerFax.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFax.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFax.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerFax.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerFax);
		vPartnerkomponenten.addElement(wtfQuellPartnerFax);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerFax);

		// Homepage
		wlaHomepage = new WrapperLabel();
		wlaHomepage.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.homepage")
				+ ": ");
		wlaHomepage.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaHomepage.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaHomepage.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaHomepage);

		wbuHomepageDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuHomepageDatenrichtung.addActionListener(this);
		wbuHomepageDatenrichtung.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuHomepageDatenrichtung);
		vPartnerkomponenten.addElement(wbuHomepageDatenrichtung);

		wtfZielPartnerHomepage = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerHomepage.setText("");
		wtfZielPartnerHomepage.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfZielPartnerHomepage.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerHomepage.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerHomepage.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerHomepage.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerHomepage);
		vPartnerkomponenten.addElement(wtfZielPartnerHomepage);

		wtfQuellPartnerHomepage = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerHomepage.setText("");
		wtfQuellPartnerHomepage.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfQuellPartnerHomepage.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerHomepage.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerHomepage.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerHomepage.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerHomepage);
		vPartnerkomponenten.addElement(wtfQuellPartnerHomepage);

		// Direktfax
		wlaDirektfax = new WrapperLabel();
		wlaDirektfax.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.direktfax")
				+ ": ");
		wlaDirektfax.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaDirektfax.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaDirektfax.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vAnsprechpartnerkomponenten.addElement(wlaDirektfax);

		wbuDirektfaxDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuDirektfaxDatenrichtung.addActionListener(this);
		wbuDirektfaxDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuDirektfaxDatenrichtung);
		vAnsprechpartnerkomponenten.addElement(wbuDirektfaxDatenrichtung);

		wtfZielPartnerDirektfax = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerDirektfax.setText("");
		wtfZielPartnerDirektfax.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfZielPartnerDirektfax.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerDirektfax.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerDirektfax.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfZielPartnerDirektfax.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerDirektfax);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerDirektfax);

		wtfQuellPartnerDirektfax = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerDirektfax.setText("");
		wtfQuellPartnerDirektfax.setColumnsMax(MAX_PARTNERKOMMUNIKATIONSBEZ);
		wtfQuellPartnerDirektfax.setMaximumSize(new Dimension(180, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerDirektfax.setPreferredSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerDirektfax.setMinimumSize(new Dimension(150, Defaults
				.getInstance().getControlHeight()));
		wtfQuellPartnerDirektfax.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerDirektfax);
		vAnsprechpartnerkomponenten.addElement(wtfQuellPartnerDirektfax);

		// Partnerbemerkung
		wlaPartnerbemerkung = new WrapperLabel();
		wlaPartnerbemerkung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bemerkung")
				+ ": ");
		wlaPartnerbemerkung.setMaximumSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wlaPartnerbemerkung.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaPartnerbemerkung.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vAnsprechpartnerkomponenten.addElement(wlaPartnerbemerkung);
		vPartnerkomponenten.addElement(wlaPartnerbemerkung);

		wbuPartnerbemerkungDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuPartnerbemerkungDatenrichtung.addActionListener(this);
		wbuPartnerbemerkungDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		wbuPartnerbemerkungDatenrichtung.setButtonKombinierbar(true);
		vWrapperButtonsZusammenfuehren
				.addElement(wbuPartnerbemerkungDatenrichtung);
		vAnsprechpartnerkomponenten
				.addElement(wbuPartnerbemerkungDatenrichtung);
		vPartnerkomponenten.addElement(wbuPartnerbemerkungDatenrichtung);

		wtaZielPartnerPartnerbemerkung = new WrapperTextAreaZusammenfuehren();
		wtaZielPartnerPartnerbemerkung.setAutoscrolls(true);
		wtaZielPartnerPartnerbemerkung.setRows(3);
		wtaZielPartnerPartnerbemerkung.setColumns(15);
		wtaZielPartnerPartnerbemerkung.setActivatable(false);
		wtaZielPartnerPartnerbemerkung.setCaretPosition(0);
		jScrollPaneZielPartnerBemerkung = new javax.swing.JScrollPane();
		jScrollPaneZielPartnerBemerkung
				.setViewportView(wtaZielPartnerPartnerbemerkung);
		vZielfelder.addElement(wtaZielPartnerPartnerbemerkung);
		vAnsprechpartnerkomponenten.addElement(wtaZielPartnerPartnerbemerkung);
		vAnsprechpartnerkomponenten.addElement(jScrollPaneZielPartnerBemerkung);
		vPartnerkomponenten.addElement(wtaZielPartnerPartnerbemerkung);
		vPartnerkomponenten.addElement(jScrollPaneZielPartnerBemerkung);

		wtaQuellPartnerPartnerbemerkung = new WrapperTextAreaZusammenfuehren();
		wtaQuellPartnerPartnerbemerkung.setAutoscrolls(true);
		wtaQuellPartnerPartnerbemerkung.setRows(3);
		wtaQuellPartnerPartnerbemerkung.setColumns(15);
		wtaQuellPartnerPartnerbemerkung.setActivatable(false);
		wtaQuellPartnerPartnerbemerkung.setCaretPosition(0);
		jScrollPaneQuellPartnerBemerkung = new javax.swing.JScrollPane();
		jScrollPaneQuellPartnerBemerkung
				.setViewportView(wtaQuellPartnerPartnerbemerkung);
		vQuellfelder.addElement(wtaQuellPartnerPartnerbemerkung);
		vAnsprechpartnerkomponenten.addElement(wtaQuellPartnerPartnerbemerkung);
		vAnsprechpartnerkomponenten
				.addElement(jScrollPaneQuellPartnerBemerkung);
		vPartnerkomponenten.addElement(wtaQuellPartnerPartnerbemerkung);
		vPartnerkomponenten.addElement(jScrollPaneQuellPartnerBemerkung);

		// BLZ
		wlaBLZ = new WrapperLabel();
		wlaBLZ.setText(LPMain.getInstance().getTextRespectUISPr("lp.blz")
				+ ": ");
		wlaBLZ.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaBLZ.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaBLZ.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaBLZ);

		wbuBLZ = new WrapperButtonZusammenfuehren();
		wbuBLZ.addActionListener(this);
		wbuBLZ.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuBLZ);
		vPartnerkomponenten.addElement(wbuBLZ);

		wtfZielBLZ = new WrapperTextFieldZusammenfuehren();
		wtfZielBLZ.setText("");
		wtfZielBLZ.setColumnsMax(MAX_BLZ);
		wtfZielBLZ.setMaximumSize(new Dimension(180, Defaults.getInstance()
				.getControlHeight()));
		wtfZielBLZ.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfZielBLZ.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfZielBLZ.setActivatable(false);
		vZielfelder.addElement(wtfZielBLZ);
		vPartnerkomponenten.addElement(wtfZielBLZ);

		wtfQuellBLZ = new WrapperTextFieldZusammenfuehren();
		wtfQuellBLZ.setText("");
		wtfQuellBLZ.setColumnsMax(MAX_BLZ);
		wtfQuellBLZ.setMaximumSize(new Dimension(180, Defaults.getInstance()
				.getControlHeight()));
		wtfQuellBLZ.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfQuellBLZ.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfQuellBLZ.setActivatable(false);
		vQuellfelder.addElement(wtfQuellBLZ);
		vPartnerkomponenten.addElement(wtfQuellBLZ);

		// BIC
		wlaBIC = new WrapperLabel();
		wlaBIC.setText(LPMain.getInstance().getTextRespectUISPr("lp.bic")
				+ ": ");
		wlaBIC.setMaximumSize(new Dimension(115, Defaults.getInstance()
				.getControlHeight()));
		wlaBIC.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaBIC.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		vPartnerkomponenten.addElement(wlaBIC);

		wbuBIC = new WrapperButtonZusammenfuehren();
		wbuBIC.addActionListener(this);
		wbuBIC.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren.addElement(wbuBIC);
		vPartnerkomponenten.addElement(wbuBIC);

		wtfZielBIC = new WrapperTextFieldZusammenfuehren();
		wtfZielBIC.setText("");
		wtfZielBIC.setColumnsMax(MAX_BIC);
		wtfZielBIC.setMaximumSize(new Dimension(180, Defaults.getInstance()
				.getControlHeight()));
		wtfZielBIC.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfZielBIC.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfZielBIC.setActivatable(false);
		vZielfelder.addElement(wtfZielBIC);
		vPartnerkomponenten.addElement(wtfZielBIC);

		wtfQuellBIC = new WrapperTextFieldZusammenfuehren();
		wtfQuellBIC.setText("");
		wtfQuellBIC.setColumnsMax(MAX_BIC);
		wtfQuellBIC.setMaximumSize(new Dimension(180, Defaults.getInstance()
				.getControlHeight()));
		wtfQuellBIC.setPreferredSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfQuellBIC.setMinimumSize(new Dimension(150, Defaults.getInstance()
				.getControlHeight()));
		wtfQuellBIC.setActivatable(false);
		vQuellfelder.addElement(wtfQuellBIC);
		vPartnerkomponenten.addElement(wtfQuellBIC);

		// AnsprGeburtsdatum
		wlaAnsprGeburtsdatum = new WrapperLabel();
		wlaAnsprGeburtsdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.personalangehoerige.geburtsdatum")
				+ ": ");
		wlaAnsprGeburtsdatum.setMaximumSize(new Dimension(115, Defaults
				.getInstance().getControlHeight()));
		wlaAnsprGeburtsdatum.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaAnsprGeburtsdatum.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wlaAnsprGeburtsdatum);
		vAnsprechpartnerkomponenten.addElement(wlaAnsprGeburtsdatum);

		wbuAnsprGeburtsdatumDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuAnsprGeburtsdatumDatenrichtung.addActionListener(this);
		wbuAnsprGeburtsdatumDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren
				.addElement(wbuAnsprGeburtsdatumDatenrichtung);
		vPartnerkomponenten.addElement(wbuAnsprGeburtsdatumDatenrichtung);
		vAnsprechpartnerkomponenten
				.addElement(wbuAnsprGeburtsdatumDatenrichtung);

		wtfZielPartnerAnsprGeburtsdatum = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerAnsprGeburtsdatum.setText("");
		wtfZielPartnerAnsprGeburtsdatum.setMaximumSize(new Dimension(180,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerAnsprGeburtsdatum.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerAnsprGeburtsdatum.setMinimumSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerAnsprGeburtsdatum.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerAnsprGeburtsdatum);
		vPartnerkomponenten.addElement(wtfZielPartnerAnsprGeburtsdatum);
		vAnsprechpartnerkomponenten.addElement(wtfZielPartnerAnsprGeburtsdatum);

		wtfQuellPartnerAnsprGeburtsdatum = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerAnsprGeburtsdatum.setText("");
		wtfQuellPartnerAnsprGeburtsdatum.setMaximumSize(new Dimension(180,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerAnsprGeburtsdatum.setPreferredSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerAnsprGeburtsdatum.setMinimumSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerAnsprGeburtsdatum.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerAnsprGeburtsdatum);
		vPartnerkomponenten.addElement(wtfQuellPartnerAnsprGeburtsdatum);
		vAnsprechpartnerkomponenten
				.addElement(wtfQuellPartnerAnsprGeburtsdatum);

		// Ansprechpartnerfunktion
		wlaAnsprechpartnerfunktion = new WrapperLabel();
		wlaAnsprechpartnerfunktion.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.ansprechpartnerfkt")
				+ ": ");
		wlaAnsprechpartnerfunktion.setMaximumSize(new Dimension(135, Defaults
				.getInstance().getControlHeight()));
		wlaAnsprechpartnerfunktion.setPreferredSize(new Dimension(130, Defaults
				.getInstance().getControlHeight()));
		wlaAnsprechpartnerfunktion.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vAnsprechpartnerkomponenten.addElement(wlaAnsprechpartnerfunktion);

		wbuAnsprechpartnerfunktionDatenrichtung = new WrapperButtonZusammenfuehren();
		wbuAnsprechpartnerfunktionDatenrichtung.addActionListener(this);
		wbuAnsprechpartnerfunktionDatenrichtung
				.setActionCommand(ACTION_DATENRICHTUNG_WAEHLEN);
		vWrapperButtonsZusammenfuehren
				.addElement(wbuAnsprechpartnerfunktionDatenrichtung);
		vAnsprechpartnerkomponenten
				.addElement(wbuAnsprechpartnerfunktionDatenrichtung);

		wtfZielPartnerAnsprechpartnerfunktion = new WrapperTextFieldZusammenfuehren();
		wtfZielPartnerAnsprechpartnerfunktion.setText("");
		wtfZielPartnerAnsprechpartnerfunktion.setMaximumSize(new Dimension(180,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerAnsprechpartnerfunktion.setPreferredSize(new Dimension(
				150, Defaults.getInstance().getControlHeight()));
		wtfZielPartnerAnsprechpartnerfunktion.setMinimumSize(new Dimension(150,
				Defaults.getInstance().getControlHeight()));
		wtfZielPartnerAnsprechpartnerfunktion.setActivatable(false);
		vZielfelder.addElement(wtfZielPartnerAnsprechpartnerfunktion);
		vAnsprechpartnerkomponenten
				.addElement(wtfZielPartnerAnsprechpartnerfunktion);

		wtfQuellPartnerAnsprechpartnerfunktion = new WrapperTextFieldZusammenfuehren();
		wtfQuellPartnerAnsprechpartnerfunktion.setText("");
		wtfQuellPartnerAnsprechpartnerfunktion.setMaximumSize(new Dimension(
				180, Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerAnsprechpartnerfunktion.setPreferredSize(new Dimension(
				150, Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerAnsprechpartnerfunktion.setMinimumSize(new Dimension(
				150, Defaults.getInstance().getControlHeight()));
		wtfQuellPartnerAnsprechpartnerfunktion.setActivatable(false);
		vQuellfelder.addElement(wtfQuellPartnerAnsprechpartnerfunktion);
		vAnsprechpartnerkomponenten
				.addElement(wtfQuellPartnerAnsprechpartnerfunktion);

		// Automatisch zusammenfuehren
		jpaAutomatischZusammenfuehren = new JPanel();
		gridBagLayoutAutomatischZusammenfuehren = new GridBagLayout();
		jpaAutomatischZusammenfuehren
				.setLayout(gridBagLayoutAutomatischZusammenfuehren);
		vPartnerkomponenten.addElement(jpaAutomatischZusammenfuehren);

		wlaAutomatischZusammenfuehren = new WrapperLabel();
		wlaAutomatischZusammenfuehren.setText(LPMain.getInstance()
				.getTextRespectUISPr("part.zusammenfuehren.automatisch")
				+ ": ");
		wlaAutomatischZusammenfuehren.setMaximumSize(new Dimension(180,
				Defaults.getInstance().getControlHeight()));
		wlaAutomatischZusammenfuehren.setPreferredSize(new Dimension(160,
				Defaults.getInstance().getControlHeight()));
		wlaAutomatischZusammenfuehren.setMinimumSize(new Dimension(160,
				Defaults.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wlaAutomatischZusammenfuehren);

		wcbKundeZusammenfuehren = new WrapperCheckBox();
		wcbKundeZusammenfuehren.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.kunde"));
		wcbKundeZusammenfuehren.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wcbKundeZusammenfuehren);

		HelperClient.setToolTipTextMitRechtToComponent(wcbKundeZusammenfuehren,
				RechteFac.RECHT_PART_KUNDE_ZUSAMMENFUEHREN_ERLAUBT);
		boolean bDarfKundeZusammenfuehren = false;
		try {
			bDarfKundeZusammenfuehren = DelegateFactory.getInstance()
					.getTheJudgeDelegate().hatRecht(
							RechteFac.RECHT_PART_KUNDE_ZUSAMMENFUEHREN_ERLAUBT);
		} catch (Throwable ex) {
			handleException(ex, true);
		}
		if (bDarfKundeZusammenfuehren) {
			wcbKundeZusammenfuehren.setActivatable(true);
			wcbKundeZusammenfuehren.setEnabled(true);
			wcbKundeZusammenfuehren.setSelected(false);
		} else {
			wcbKundeZusammenfuehren.setActivatable(false);
			wcbKundeZusammenfuehren.setEnabled(false);
			wcbKundeZusammenfuehren.setSelected(false);
		}

		wcbLieferantZusammenfuehren = new WrapperCheckBox();
		wcbLieferantZusammenfuehren.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferant"));
		wcbLieferantZusammenfuehren.setPreferredSize(new Dimension(100,
				Defaults.getInstance().getControlHeight()));
		vPartnerkomponenten.addElement(wcbLieferantZusammenfuehren);
		HelperClient.setToolTipTextMitRechtToComponent(
				wcbLieferantZusammenfuehren,
				RechteFac.RECHT_PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT);
		boolean bDarfLieferantZusammenfuehren = false;
		try {
			bDarfLieferantZusammenfuehren = DelegateFactory
					.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(
							RechteFac.RECHT_PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT);
		} catch (Throwable ex) {
			handleException(ex, true);
		}
		if (bDarfLieferantZusammenfuehren) {
			wcbLieferantZusammenfuehren.setActivatable(true);
			wcbLieferantZusammenfuehren.setEnabled(true);
			wcbLieferantZusammenfuehren.setSelected(false);
		} else {
			wcbLieferantZusammenfuehren.setActivatable(false);
			wcbLieferantZusammenfuehren.setEnabled(false);
			wcbLieferantZusammenfuehren.setSelected(false);
		}

		setzeFelderVonVektorSichtbar(this.vAnsprechpartnerkomponenten, false);
		setzeFelderVonVektorSichtbar(this.vPartnerkomponenten, true);

		jpaWorkingOnScroll.add(wlaAutomatischZusammenfuehren,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaAutomatischZusammenfuehren.add(wcbKundeZusammenfuehren,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaAutomatischZusammenfuehren.add(wcbLieferantZusammenfuehren,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		/*
		 * jpaAutomatischZusammenfuehren.add(wcbPersonalZusammenfuehren, new
		 * GridBagConstraints(2, 0, 1, 1, 0.0, 0.0 , GridBagConstraints.CENTER,
		 * GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		 * 
		 * jpaAutomatischZusammenfuehren.add(wcbBankZusammenfuehren, new
		 * GridBagConstraints(3, 0, 1, 1, 0.0, 0.0 , GridBagConstraints.CENTER,
		 * GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		 */

		jpaWorkingOnScroll.add(jpaAutomatischZusammenfuehren,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wbuZielPartnerAuswaehlen,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuAuswahlReset, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuQuellPartnerAuswaehlen,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wbuZielPartnerAnsprechpartnerAuswaehlen,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuQuellPartnerAnsprechpartnerAuswaehlen,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaAnrede, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerAnrede, new GridBagConstraints(1,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuAnredeDatenrichtung, new GridBagConstraints(
				2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerAnrede, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaTitel, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerTitel, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuTitelDatenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerTitel, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaName1, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerName1, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuName1Datenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerName1, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaName2, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerName2, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuName2Datenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerName2, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaName3, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerName3, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuName3Datenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerName3, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaStrasse, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerStrasse, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuStrasseDatenrichtung, new GridBagConstraints(
				2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerStrasse, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaOrt, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerOrt, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		/*
		 * jpaWorkingOnScroll.add(wbuOrtDatenrichtung, new GridBagConstraints(2,
		 * iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		 * GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		 */

		jpaWorkingOnScroll.add(wtfQuellPartnerOrt, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaPostfachort, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerPostfachort,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuPostfachortDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerPostfachort,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaPostfachort, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerPostfachort,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuPostfachortDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerPostfachort,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaPostfachnr, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerPostfachnr,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuPostfachnrDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerPostfachnr,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaKurzbez, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerKurzbez, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuKurzbezDatenrichtung, new GridBagConstraints(
				2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerKurzbez, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaPartnerart, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerPartnerart,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuPartnerartDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerPartnerart,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaUid, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerUid, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		/*
		 * jpaWorkingOnScroll.add(wbuUidDatenrichtung, new GridBagConstraints(2,
		 * iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		 * GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		 */

		jpaWorkingOnScroll.add(wtfQuellPartnerUid, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaKommunikationsspr, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerKommunikationsspr,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuKommunikationssprDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerKommunikationsspr,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaIln, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerIln, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuIlnDatenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerIln, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaFilialnr, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerFilialnr, new GridBagConstraints(
				1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuFilialnrDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerFilialnr, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaGerichtsstand, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerGerichtsstand,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuGerichtsstandDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerGerichtsstand,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaFirmenbuchnr, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerFirmenbuchnr,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuFirmenbuchnrDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerFirmenbuchnr,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaBLZ, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielBLZ, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuBLZ, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellBLZ, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaBIC, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielBIC, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuBIC, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellBIC, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaPartnerklasse, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerPartnerklasse,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuPartnerklasseDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerPartnerklasse,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaBranche, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerBranche, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuBrancheDatenrichtung, new GridBagConstraints(
				2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerBranche, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaTelefon, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerTelefon, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuTelefonDatenrichtung, new GridBagConstraints(
				2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerTelefon, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaHandy, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerHandy, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuHandyDatenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerHandy, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaFax, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerFax, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuFaxDatenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerFax, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaDirektfax, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerDirektfax, new GridBagConstraints(
				1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuDirektfaxDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerDirektfax,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaEmail, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerEmail, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuEmailDatenrichtung, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerEmail, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaHomepage, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerHomepage, new GridBagConstraints(
				1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wbuHomepageDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerHomepage, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaPartnerbemerkung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(jScrollPaneZielPartnerBemerkung,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuPartnerbemerkungDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(jScrollPaneQuellPartnerBemerkung,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaAnsprGeburtsdatum, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerAnsprGeburtsdatum,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuAnsprGeburtsdatumDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerAnsprGeburtsdatum,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		// Zeile
		iZeile++;

		jpaWorkingOnScroll.add(wlaAnsprechpartnerfunktion,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfZielPartnerAnsprechpartnerfunktion,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wbuAnsprechpartnerfunktionDatenrichtung,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jpaWorkingOnScroll.add(wtfQuellPartnerAnsprechpartnerfunktion,
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		jScrollPanePartner.setViewportView(jpaWorkingOnScroll);
		jScrollPanePartner.setAutoscrolls(true);

		jpaWorkingOn.add(jScrollPanePartner, new GridBagConstraints(0, iZeile,
				1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		saveButton = createAndSaveButton(
				"/com/lp/client/res/check2.png",
				LPMain
						.getTextRespectUISPr("part.partnerzusammenfuehren.ausfuehren"),
				ACTION_SPECIAL_SAVE, KeyStroke.getKeyStroke('S',
						java.awt.event.InputEvent.CTRL_MASK), null);

		String[] aWhichButtonIUse = { ACTION_SPECIAL_SAVE };

		enableButtonAction(aWhichButtonIUse);

		/* ToDo: weiss nicht, was da hin muss */
		LockStateValue lockstateValue = new LockStateValue(null, null,
				PanelBasis.LOCK_NO_LOCKING);
		updateButtons(lockstateValue);

		befuelleFelderMitPartnerZielDto(this.partnerZielDto);
		setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren,
				this.vZielfelder, this.vQuellfelder, true);
		// PJ 17636

		this.partnerQuellDto = this.partnerZielDto;

		setzeFelderVonVektorSichtbar(this.vAnsprechpartnerkomponenten, false);
		setzeFelderVonVektorSichtbar(this.vPartnerkomponenten, true);
		befuelleFelderMitPartnerQuellDto(this.partnerQuellDto);
		setSaveButtonStatus();
	}

	/**
	 * setzt die Komponenten, welche im &uuml;bergebenen Vector enthalten sind,
	 * sichtbar/unsichtbar
	 * 
	 * @param vector
	 *            Vector
	 * @param boole
	 *            Boolean
	 */
	private void setzeFelderVonVektorSichtbar(Vector<JComponent> vector,
			Boolean boole) {
		int i = 0;
		for (Enumeration<JComponent> e2 = vector.elements(); e2
				.hasMoreElements(); e2.nextElement()) {
			if (vector.get(i) instanceof WrapperTextFieldZusammenfuehren) {
				((WrapperTextFieldZusammenfuehren) vector.get(i))
						.setActivatable(boole);
				((WrapperTextFieldZusammenfuehren) vector.get(i))
						.setVisible(boole);
			} else if (vector.get(i) instanceof WrapperLabel) {
				((WrapperLabel) vector.get(i)).setEnabled(boole);
				((WrapperLabel) vector.get(i)).setVisible(boole);
			} else if (vector.get(i) instanceof WrapperButtonZusammenfuehren) {
				((WrapperButtonZusammenfuehren) vector.get(i))
						.setActivatable(boole);
				((WrapperButtonZusammenfuehren) vector.get(i))
						.setEnabled(boole);
				((WrapperButtonZusammenfuehren) vector.get(i))
						.setVisible(boole);
			} else if (vector.get(i) instanceof WrapperTextAreaZusammenfuehren) {
				((WrapperTextAreaZusammenfuehren) vector.get(i))
						.setActivatable(boole);
				((WrapperTextAreaZusammenfuehren) vector.get(i))
						.setEnabled(boole);
				((WrapperTextAreaZusammenfuehren) vector.get(i))
						.setVisible(boole);
			} else if (vector.get(i) instanceof javax.swing.JScrollPane) {
				((JScrollPane) vector.get(i)).setEnabled(boole);
				((JScrollPane) vector.get(i)).setVisible(boole);
			} else if (vector.get(i) instanceof WrapperCheckBox) {
				((WrapperCheckBox) vector.get(i)).setActivatable(boole);
				((WrapperCheckBox) vector.get(i)).setEnabled(boole);
				((WrapperCheckBox) vector.get(i)).setVisible(boole);
			}
			i++;
		}
	}

	/**
	 * setzt die Komponenten f&auml;r den Ansprechpartnervergleich
	 * sichtbar/unsichtbar
	 * 
	 * @param boole
	 *            Boolean
	 */
	/*
	 * private void setzeFelderAnsprechpartnerSichtbar(Boolean boole) { int i =
	 * 0; for (Enumeration e2 = this.vAnsprechpartnerkomponenten.elements();
	 * e2.hasMoreElements(); e2.nextElement()) { if
	 * (this.vAnsprechpartnerkomponenten.get(i) instanceof
	 * WrapperTextFieldZusammenfuehren) { ( (WrapperTextFieldZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setActivatable(boole); (
	 * (WrapperTextFieldZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setVisible(boole); } else if
	 * (this.vAnsprechpartnerkomponenten.get(i) instanceof WrapperLabel) { (
	 * (WrapperLabel)
	 * this.vAnsprechpartnerkomponenten.get(i)).setEnabled(boole); (
	 * (WrapperLabel)
	 * this.vAnsprechpartnerkomponenten.get(i)).setVisible(boole); } else if
	 * (this.vAnsprechpartnerkomponenten.get(i) instanceof
	 * WrapperButtonZusammenfuehren) { ( (WrapperButtonZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setActivatable(boole); (
	 * (WrapperButtonZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setEnabled(boole); (
	 * (WrapperButtonZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setVisible(boole); } else if
	 * (this.vAnsprechpartnerkomponenten.get(i) instanceof
	 * WrapperTextAreaZusammenfuehren) { ( (WrapperTextAreaZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setActivatable(boole); (
	 * (WrapperTextAreaZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setEnabled(boole); (
	 * (WrapperTextAreaZusammenfuehren)
	 * this.vAnsprechpartnerkomponenten.get(i)).setVisible(boole); } else if
	 * (this.vAnsprechpartnerkomponenten.get(i) instanceof
	 * javax.swing.JScrollPane) { ( (JScrollPane)
	 * this.vAnsprechpartnerkomponenten.get(i)).setEnabled(boole); (
	 * (JScrollPane) this.vAnsprechpartnerkomponenten.get(i)).setVisible(boole);
	 * } else if (this.vPartnerkomponenten.get(i) instanceof WrapperCheckBox) {
	 * ( (WrapperCheckBox)
	 * this.vAnsprechpartnerkomponenten.get(i)).setActivatable(boole); (
	 * (WrapperCheckBox)
	 * this.vAnsprechpartnerkomponenten.get(i)).setEnabled(boole); (
	 * (WrapperCheckBox)
	 * this.vAnsprechpartnerkomponenten.get(i)).setVisible(boole); } i++; } }
	 */

	/**
	 * wenn die daten einer seite ausgewaehlt werden, so werden sie farblich
	 * hinterlegt angezeigt, wenn nicht ausgewaehlt, dann werden sie grau
	 * hinterlegt angezeigt
	 * 
	 * @param vButtons
	 *            Vector etnhaelt alle Buttons, au&szlig;er dem zentralen, welche die
	 *            Datenrichtung bestimmen
	 * @param vZielfelder
	 *            Vector enthaelt alle Felder und Checkboxen der linken Seite
	 * @param vQuellfelder
	 *            Vector enthaelt alle Felder und Checkboxen der rechten Seite
	 * @param initial
	 *            boolean beim initialen starten wird die linke seite
	 *            ausgewaehlt
	 */
	private void setzeFarbenVonTextfeldern(
			Vector<WrapperButtonZusammenfuehren> vButtons,
			Vector<JComponent> vZielfelder, Vector<JComponent> vQuellfelder,
			boolean initial) {
		int i = 0;
		for (Enumeration<JComponent> e2 = vQuellfelder.elements(); e2
				.hasMoreElements(); e2.nextElement()) {
			if (initial) {
				// nur die linken selektieren
				if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
					((WrapperTextFieldZusammenfuehren) vZielfelder.get(i))
							.setSelectedData(true);
					((WrapperTextFieldZusammenfuehren) vQuellfelder.get(i))
							.setSelectedData(false);
				} else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
					((WrapperTextAreaZusammenfuehren) vZielfelder.get(i))
							.setSelectedData(true);
					((WrapperTextAreaZusammenfuehren) vQuellfelder.get(i))
							.setSelectedData(false);
				} else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
					((WrapperCheckBoxZusammenfuehren) vZielfelder.get(i))
							.setSelectedData(true);
					((WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i))
							.setSelectedData(false);
				} else {
					// fehlt
				}
			} else {
				if (vButtons.get(i).getISelection() == WrapperButtonZusammenfuehren.SELECT_LINKEN_DATENINHALT) {
					if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
						((WrapperTextFieldZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(true);
						((WrapperTextFieldZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(false);
					} else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
						((WrapperTextAreaZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(true);
						((WrapperTextAreaZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(false);
					} else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
						((WrapperCheckBoxZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(true);
						((WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(false);
					} else {
						// fehlt
					}
				} else if (vButtons.get(i).getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
					if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
						((WrapperTextFieldZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(false);
						((WrapperTextFieldZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(true);
					} else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
						((WrapperTextAreaZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(false);
						((WrapperTextAreaZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(true);
					} else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
						((WrapperCheckBoxZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(false);
						((WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(true);
					} else {
						// fehlt
					}
				} else if (vButtons.get(i).getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
					if (vZielfelder.get(i) instanceof WrapperTextFieldZusammenfuehren) {
						((WrapperTextFieldZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(true);
						((WrapperTextFieldZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(true);
					} else if (vZielfelder.get(i) instanceof WrapperTextAreaZusammenfuehren) {
						((WrapperTextAreaZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(true);
						((WrapperTextAreaZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(true);
					} else if (vZielfelder.get(i) instanceof WrapperCheckBoxZusammenfuehren) {
						((WrapperCheckBoxZusammenfuehren) vZielfelder.get(i))
								.setSelectedData(true);
						((WrapperCheckBoxZusammenfuehren) vQuellfelder.get(i))
								.setSelectedData(true);
					} else {
						// fehlt
					}
				}
			}
			i++;
		}
	}

	private void befuelleFelderMitPartnerZielDto(PartnerDto pzDto)
			throws Throwable {
		if (pzDto.getAnredeCNr() != null) {
			wtfZielPartnerAnrede.setText(pzDto.getAnredeCNr());
			wtfZielPartnerAnrede.setCaretPosition(0);
			wtfZielPartnerAnrede.setToolTipText(pzDto.getAnredeCNr());
		} else {
			wtfZielPartnerAnrede.setText("");
			wtfZielPartnerAnrede.setToolTipText("");
		}

		if (pzDto.getCTitel() != null && pzDto.getCTitel() != "") {
			wtfZielPartnerTitel.setText(pzDto.getCTitel());
			wtfZielPartnerTitel.setCaretPosition(0);
			wtfZielPartnerTitel.setToolTipText(pzDto.getCTitel());
		} else {
			wtfZielPartnerTitel.setText("");
			wtfZielPartnerTitel.setToolTipText("");
		}

		if (pzDto.getCName1nachnamefirmazeile1() != null
				&& pzDto.getCName1nachnamefirmazeile1() != "") {
			wtfZielPartnerName1.setText(pzDto.getCName1nachnamefirmazeile1());
			wtfZielPartnerName1.setCaretPosition(0);
			wtfZielPartnerName1.setToolTipText(pzDto
					.getCName1nachnamefirmazeile1());
		} else {
			wtfZielPartnerName1.setText("");
			wtfZielPartnerName1.setToolTipText("");
		}

		if (pzDto.getCName2vornamefirmazeile2() != null
				&& pzDto.getCName2vornamefirmazeile2() != "") {
			wtfZielPartnerName2.setText(pzDto.getCName2vornamefirmazeile2());
			wtfZielPartnerName2.setCaretPosition(0);
			wtfZielPartnerName2.setToolTipText(pzDto
					.getCName2vornamefirmazeile2());
		} else {
			wtfZielPartnerName2.setText("");
			wtfZielPartnerName2.setToolTipText("");
		}

		if (pzDto.getCName3vorname2abteilung() != null
				&& pzDto.getCName3vorname2abteilung() != "") {
			wtfZielPartnerName3.setText(pzDto.getCName3vorname2abteilung());
			wtfZielPartnerName3.setCaretPosition(0);
			wtfZielPartnerName3.setToolTipText(pzDto
					.getCName3vorname2abteilung());
		} else {
			wtfZielPartnerName3.setText("");
			wtfZielPartnerName3.setToolTipText("");
		}

		if (pzDto.getCStrasse() != null && pzDto.getCStrasse() != "") {
			wtfZielPartnerStrasse.setText(pzDto.getCStrasse());
			wtfZielPartnerStrasse.setCaretPosition(0);
			wtfZielPartnerStrasse.setToolTipText(pzDto.getCStrasse());
		} else {
			wtfZielPartnerStrasse.setText("");
			wtfZielPartnerStrasse.setToolTipText("");
		}

		if (pzDto.getBankDto() != null) {
			wtfZielBIC.setText(pzDto.getBankDto().getCBic());
			wtfZielBLZ.setText(pzDto.getBankDto().getCBlz());
		} else {
			wtfZielBIC.setText("");
			wtfZielBLZ.setText("");
		}

		if (pzDto.getLandplzortDto() != null
				&& pzDto.getLandplzortDto().formatLandPlzOrt() != null
				&& pzDto.getLandplzortDto().formatLandPlzOrt() != "") {
			wtfZielPartnerOrt.setText(pzDto.getLandplzortDto()
					.formatLandPlzOrt());
			wtfZielPartnerOrt.setCaretPosition(0);
			wtfZielPartnerOrt.setToolTipText(pzDto.getLandplzortDto()
					.formatLandPlzOrt());
		} else {
			wtfZielPartnerOrt.setText("");
			wtfZielPartnerOrt.setToolTipText("");
		}

		if (pzDto.getLandplzortDto_Postfach() != null
				&& pzDto.getLandplzortDto_Postfach().formatLandPlzOrt() != null
				&& pzDto.getLandplzortDto_Postfach().formatLandPlzOrt() != "") {
			wtfZielPartnerPostfachort.setText(pzDto.getLandplzortDto_Postfach()
					.formatLandPlzOrt());
			wtfZielPartnerPostfachort.setCaretPosition(0);
			wtfZielPartnerPostfachort.setToolTipText(pzDto
					.getLandplzortDto_Postfach().formatLandPlzOrt());
		} else {
			wtfZielPartnerPostfachort.setText("");
			wtfZielPartnerPostfachort.setToolTipText("");
		}

		if (pzDto.getCPostfach() != null && pzDto.getCPostfach() != "") {
			wtfZielPartnerPostfachnr.setText(pzDto.getCPostfach());
			wtfZielPartnerPostfachnr.setCaretPosition(0);
			wtfZielPartnerPostfachnr.setToolTipText(pzDto.getCPostfach());
		} else {
			wtfZielPartnerPostfachnr.setText("");
			wtfZielPartnerPostfachnr.setToolTipText("");
		}

		if (pzDto.getCKbez() != null && pzDto.getCKbez() != "") {
			wtfZielPartnerKurzbez.setText(pzDto.getCKbez());
			wtfZielPartnerKurzbez.setCaretPosition(0);
			wtfZielPartnerKurzbez.setToolTipText(pzDto.getCKbez());
		} else {
			wtfZielPartnerKurzbez.setText("");
			wtfZielPartnerKurzbez.setToolTipText("");
		}

		if (pzDto.getPartnerartCNr() != null && pzDto.getPartnerartCNr() != "") {
			wtfZielPartnerPartnerart.setText(pzDto.getPartnerartCNr());
			wtfZielPartnerPartnerart.setCaretPosition(0);
			wtfZielPartnerPartnerart.setToolTipText(pzDto.getPartnerartCNr());
		} else {
			wtfZielPartnerPartnerart.setText("");
			wtfZielPartnerPartnerart.setToolTipText("");
		}

		if (pzDto.getCUid() != null && pzDto.getCUid() != "") {
			wtfZielPartnerUid.setText(pzDto.getCUid());
			wtfZielPartnerUid.setCaretPosition(0);
			wtfZielPartnerUid.setToolTipText(pzDto.getCUid());
		} else {
			wtfZielPartnerUid.setText("");
			wtfZielPartnerUid.setToolTipText("");
		}

		if (pzDto.getLocaleCNrKommunikation() != null
				&& pzDto.getLocaleCNrKommunikation() != "") {
			wtfZielPartnerKommunikationsspr.setText(pzDto
					.getLocaleCNrKommunikation());
			wtfZielPartnerKommunikationsspr.setCaretPosition(0);
			wtfZielPartnerKommunikationsspr.setToolTipText(pzDto
					.getLocaleCNrKommunikation());
		} else {
			wtfZielPartnerKommunikationsspr.setText("");
			wtfZielPartnerKommunikationsspr.setToolTipText("");
		}

		if (pzDto.getCIln() != null && pzDto.getCIln() != "") {
			wtfZielPartnerIln.setText(pzDto.getCIln());
			wtfZielPartnerIln.setCaretPosition(0);
			wtfZielPartnerIln.setToolTipText(pzDto.getCIln());
		} else {
			wtfZielPartnerIln.setText("");
			wtfZielPartnerIln.setToolTipText("");
		}

		if (pzDto.getCFilialnummer() != null && pzDto.getCFilialnummer() != "") {
			wtfZielPartnerFilialnr.setText(pzDto.getCFilialnummer());
			wtfZielPartnerFilialnr.setCaretPosition(0);
			wtfZielPartnerFilialnr.setToolTipText(pzDto.getCFilialnummer());
		} else {
			wtfZielPartnerFilialnr.setText("");
			wtfZielPartnerFilialnr.setToolTipText("");
		}

		if (pzDto.getCGerichtsstand() != null
				&& pzDto.getCGerichtsstand() != "") {
			wtfZielPartnerGerichtsstand.setText(pzDto.getCGerichtsstand());
			wtfZielPartnerGerichtsstand.setCaretPosition(0);
			wtfZielPartnerGerichtsstand.setToolTipText(pzDto
					.getCGerichtsstand());
		} else {
			wtfZielPartnerGerichtsstand.setText("");
			wtfZielPartnerGerichtsstand.setToolTipText("");
		}

		if (pzDto.getCFirmenbuchnr() != null && pzDto.getCFirmenbuchnr() != "") {
			wtfZielPartnerFirmenbuchnr.setText(pzDto.getCFirmenbuchnr());
			wtfZielPartnerFirmenbuchnr.setCaretPosition(0);
			wtfZielPartnerFirmenbuchnr.setToolTipText(pzDto.getCFirmenbuchnr());
		} else {
			wtfZielPartnerFirmenbuchnr.setText("");
			wtfZielPartnerFirmenbuchnr.setToolTipText("");
		}

		if (pzDto.getPartnerklasseIId() != null) {
			PartnerklasseDto partnerklasseDto = DelegateFactory.getInstance()
					.getPartnerDelegate().partnerklasseFindByPrimaryKey(
							pzDto.getPartnerklasseIId());
			wtfZielPartnerPartnerklasse.setText(partnerklasseDto
					.getBezeichnung());
			wtfZielPartnerPartnerklasse.setCaretPosition(0);
			wtfZielPartnerPartnerklasse.setToolTipText(partnerklasseDto
					.getBezeichnung());
		} else {
			wtfZielPartnerPartnerklasse.setText("");
			wtfZielPartnerPartnerklasse.setToolTipText("");
		}

		if (pzDto.getBrancheIId() != null) {
			BrancheDto brancheDto = DelegateFactory.getInstance()
					.getPartnerDelegate().brancheFindByPrimaryKey(
							pzDto.getBrancheIId());
			wtfZielPartnerBranche.setText(brancheDto.getBezeichnung());
			wtfZielPartnerBranche.setCaretPosition(0);
			wtfZielPartnerBranche.setToolTipText(brancheDto.getBezeichnung());
		} else {
			wtfZielPartnerBranche.setText("");
			wtfZielPartnerBranche.setToolTipText("");
		}

		if (pzDto.getCTelefon() != null) {

			wtfZielPartnerTelefon.setText(pzDto.getCTelefon());
			wtfZielPartnerTelefon.setCaretPosition(0);
			wtfZielPartnerTelefon.setToolTipText(pzDto.getCTelefon());
		} else {
			wtfZielPartnerTelefon.setText("");
			wtfZielPartnerTelefon.setToolTipText("");
		}

		if (pzDto.getCEmail() != null) {

			wtfZielPartnerEmail.setText(pzDto.getCEmail());
			wtfZielPartnerEmail.setCaretPosition(0);
			wtfZielPartnerEmail.setToolTipText(pzDto.getCEmail());
		} else {
			wtfZielPartnerEmail.setText("");
			wtfZielPartnerEmail.setToolTipText("");
		}

		if (pzDto.getCFax() != null) {

			wtfZielPartnerFax.setText(pzDto.getCFax());
			wtfZielPartnerFax.setCaretPosition(0);
			wtfZielPartnerFax.setToolTipText(pzDto.getCFax());
		} else {
			wtfZielPartnerFax.setText("");
			wtfZielPartnerFax.setToolTipText("");
		}

		if (pzDto.getCHomepage() != null) {

			wtfZielPartnerHomepage.setText(pzDto.getCHomepage());
			wtfZielPartnerHomepage.setCaretPosition(0);
			wtfZielPartnerHomepage.setToolTipText(pzDto.getCHomepage());
		} else {
			wtfZielPartnerHomepage.setText("");
			wtfZielPartnerHomepage.setToolTipText("");
		}

		if (pzDto.getXBemerkung() != null) {
			wtaZielPartnerPartnerbemerkung.setText(pzDto.getXBemerkung());
			wtaZielPartnerPartnerbemerkung.setCaretPosition(0);
			wtaZielPartnerPartnerbemerkung
					.setToolTipText(pzDto.getXBemerkung());
		} else {
			wtaZielPartnerPartnerbemerkung.setText("");
			wtaZielPartnerPartnerbemerkung.setToolTipText("");
		}

		if (pzDto.getDGeburtsdatumansprechpartner() != null) {
			wtfZielPartnerAnsprGeburtsdatum.setText(Helper.formatDatum(pzDto
					.getDGeburtsdatumansprechpartner(), Locale.getDefault()));
			wtfZielPartnerAnsprGeburtsdatum.setCaretPosition(0);
			wtfZielPartnerAnsprGeburtsdatum.setToolTipText(Helper.formatDatum(
					pzDto.getDGeburtsdatumansprechpartner(), Locale
							.getDefault()));
		} else {
			wtfZielPartnerAnsprGeburtsdatum.setText("");
			wtfZielPartnerAnsprGeburtsdatum.setToolTipText("");
		}

		setzeStatusVonButtonDatenrichtung();
		// setzeStatusVonTextfeldernMehrfach(this.vTextfelderZiel,
		// this.vTextfelderQuelle);
		setzeStatusButtonResetDatenrichtung();
		setzeVisibilityVonAnsprechpartnerZielBtn();
	}

	/**
	 * je nach inhaltsvergleich der textfelder/checkboxen wird der
	 * datenauswahlbutton in der mitte aktiv oder deaktiv geschaltet
	 */
	private void setzeStatusVonButtonDatenrichtung() {
		if (this.partnerQuellDto != null && this.partnerZielDto != null) {
			if ((wtfQuellPartnerAnrede.getText() != null
					&& wtfZielPartnerAnrede.getText() != null && wtfQuellPartnerAnrede
					.getText().equals(wtfZielPartnerAnrede.getText()))
					|| (wtfQuellPartnerAnrede.getText() == null && wtfZielPartnerAnrede
							.getText() == null)) {
				wbuAnredeDatenrichtung.setEnabled(false);
			} else {
				wbuAnredeDatenrichtung.setEnabled(true);
			}

			if ((wtfQuellPartnerTitel.getText() != null
					&& wtfZielPartnerTitel.getText() != null && wtfQuellPartnerTitel
					.getText().equals(wtfZielPartnerTitel.getText()))
					|| (wtfQuellPartnerTitel.getText() == null && wtfZielPartnerTitel
							.getText() == null)) {
				wbuTitelDatenrichtung.setEnabled(false);
			} else {
				wbuTitelDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerName1.getText() != null
					&& wtfQuellPartnerName1.getText() != null && wtfQuellPartnerName1
					.getText().equals(wtfZielPartnerName1.getText()))
					|| (wtfZielPartnerName1.getText() == null && wtfQuellPartnerName1
							.getText() == null)) {
				wbuName1Datenrichtung.setEnabled(false);
			} else {
				wbuName1Datenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerName2.getText() != null
					&& wtfQuellPartnerName2.getText() != null && wtfQuellPartnerName2
					.getText().equals(wtfZielPartnerName2.getText()))
					|| (wtfZielPartnerName2.getText() == null && wtfQuellPartnerName2
							.getText() == null)) {
				wbuName2Datenrichtung.setEnabled(false);
			} else {
				wbuName2Datenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerName3.getText() != null
					&& wtfQuellPartnerName3.getText() != null && wtfQuellPartnerName3
					.getText().equals(wtfZielPartnerName3.getText()))
					|| (wtfZielPartnerName3.getText() == null && wtfQuellPartnerName3
							.getText() == null)) {
				wbuName3Datenrichtung.setEnabled(false);
			} else {
				wbuName3Datenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerStrasse.getText() != null
					&& wtfQuellPartnerStrasse.getText() != null && wtfQuellPartnerStrasse
					.getText().equals(wtfZielPartnerStrasse.getText()))
					|| (wtfZielPartnerStrasse.getText() == null && wtfQuellPartnerStrasse
							.getText() == null)) {
				wbuStrasseDatenrichtung.setEnabled(false);
			} else {
				wbuStrasseDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerOrt.getText() != null
					&& wtfQuellPartnerOrt.getText() != null && wtfQuellPartnerOrt
					.getText().equals(wtfZielPartnerOrt.getText()))
					|| (wtfZielPartnerOrt.getText() == null && wtfQuellPartnerOrt
							.getText() == null)) {
				wbuOrtDatenrichtung.setEnabled(false);
			} else {
				wbuOrtDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerPostfachort.getText() != null
					&& wtfQuellPartnerPostfachort.getText() != null && wtfQuellPartnerPostfachort
					.getText().equals(wtfZielPartnerPostfachort.getText()))
					|| (wtfZielPartnerPostfachort.getText() == null && wtfQuellPartnerPostfachort
							.getText() == null)) {
				wbuPostfachortDatenrichtung.setEnabled(false);
			} else {
				wbuPostfachortDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerPostfachnr.getText() != null
					&& wtfQuellPartnerPostfachnr.getText() != null && wtfQuellPartnerPostfachnr
					.getText().equals(wtfZielPartnerPostfachnr.getText()))
					|| (wtfZielPartnerPostfachnr.getText() == null && wtfQuellPartnerPostfachnr
							.getText() == null)) {
				wbuPostfachnrDatenrichtung.setEnabled(false);
			} else {
				wbuPostfachnrDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerKurzbez.getText() != null
					&& wtfQuellPartnerKurzbez.getText() != null && wtfQuellPartnerKurzbez
					.getText().equals(wtfZielPartnerKurzbez.getText()))
					|| (wtfZielPartnerKurzbez.getText() == null && wtfQuellPartnerKurzbez
							.getText() == null)) {
				wbuKurzbezDatenrichtung.setEnabled(false);
			} else {
				wbuKurzbezDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerAnsprechpartnerfunktion.getText() != null
					&& wtfQuellPartnerAnsprechpartnerfunktion.getText() != null && wtfQuellPartnerAnsprechpartnerfunktion
					.getText().equals(
							wtfZielPartnerAnsprechpartnerfunktion.getText()))
					|| (wtfZielPartnerAnsprechpartnerfunktion.getText() == null && wtfQuellPartnerAnsprechpartnerfunktion
							.getText() == null)) {
				wbuAnsprechpartnerfunktionDatenrichtung.setEnabled(false);
			} else {
				wbuAnsprechpartnerfunktionDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerPartnerart.getText() != null
					&& wtfQuellPartnerPartnerart.getText() != null && wtfQuellPartnerPartnerart
					.getText().equals(wtfZielPartnerPartnerart.getText()))
					|| (wtfZielPartnerPartnerart.getText() == null && wtfQuellPartnerPartnerart
							.getText() == null)) {
				wbuPartnerartDatenrichtung.setEnabled(false);
			} else {
				wbuPartnerartDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerUid.getText() != null
					&& wtfQuellPartnerUid.getText() != null && wtfQuellPartnerUid
					.getText().equals(wtfZielPartnerUid.getText()))
					|| (wtfZielPartnerUid.getText() == null && wtfQuellPartnerUid
							.getText() == null)) {
				wbuUidDatenrichtung.setEnabled(false);
			} else {
				wbuUidDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerKommunikationsspr.getText() != null
					&& wtfQuellPartnerKommunikationsspr.getText() != null && wtfQuellPartnerKommunikationsspr
					.getText()
					.equals(wtfZielPartnerKommunikationsspr.getText()))
					|| (wtfZielPartnerKommunikationsspr.getText() == null && wtfQuellPartnerKommunikationsspr
							.getText() == null)) {
				wbuKommunikationssprDatenrichtung.setEnabled(false);
			} else {
				wbuKommunikationssprDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerIln.getText() != null
					&& wtfQuellPartnerIln.getText() != null && wtfQuellPartnerIln
					.getText().equals(wtfZielPartnerIln.getText()))
					|| (wtfZielPartnerIln.getText() == null && wtfQuellPartnerIln
							.getText() == null)) {
				wbuIlnDatenrichtung.setEnabled(false);
			} else {
				wbuIlnDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerFilialnr.getText() != null
					&& wtfQuellPartnerFilialnr.getText() != null && wtfQuellPartnerFilialnr
					.getText().equals(wtfZielPartnerFilialnr.getText()))
					|| (wtfZielPartnerFilialnr.getText() == null && wtfQuellPartnerFilialnr
							.getText() == null)) {
				wbuFilialnrDatenrichtung.setEnabled(false);
			} else {
				wbuFilialnrDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerGerichtsstand.getText() != null
					&& wtfQuellPartnerGerichtsstand.getText() != null && wtfQuellPartnerGerichtsstand
					.getText().equals(wtfZielPartnerGerichtsstand.getText()))
					|| (wtfZielPartnerGerichtsstand.getText() == null && wtfQuellPartnerGerichtsstand
							.getText() == null)) {
				wbuGerichtsstandDatenrichtung.setEnabled(false);
			} else {
				wbuGerichtsstandDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerFirmenbuchnr.getText() != null
					&& wtfQuellPartnerFirmenbuchnr.getText() != null && wtfQuellPartnerFirmenbuchnr
					.getText().equals(wtfZielPartnerFirmenbuchnr.getText()))
					|| (wtfZielPartnerFirmenbuchnr.getText() == null && wtfQuellPartnerFirmenbuchnr
							.getText() == null)) {
				wbuFirmenbuchnrDatenrichtung.setEnabled(false);
			} else {
				wbuFirmenbuchnrDatenrichtung.setEnabled(true);
			}
			if ((wtfZielBIC.getText() != null && wtfQuellBIC.getText() != null && wtfQuellBIC
					.getText().equals(wtfZielBIC.getText()))
					|| (wtfZielBIC.getText() == null && wtfQuellBIC.getText() == null)) {
				wbuBIC.setEnabled(false);
			} else {
				wbuBIC.setEnabled(true);
			}
			if ((wtfZielBLZ.getText() != null && wtfQuellBLZ.getText() != null && wtfQuellBLZ
					.getText().equals(wtfZielBLZ.getText()))
					|| (wtfZielBLZ.getText() == null && wtfQuellBLZ.getText() == null)) {
				wbuBLZ.setEnabled(false);
			} else {
				wbuBLZ.setEnabled(true);
			}

			if ((wtfZielPartnerPartnerklasse.getText() != null
					&& wtfQuellPartnerPartnerklasse.getText() != null && wtfQuellPartnerPartnerklasse
					.getText().equals(wtfZielPartnerPartnerklasse.getText()))
					|| (wtfZielPartnerPartnerklasse.getText() == null && wtfQuellPartnerPartnerklasse
							.getText() == null)) {
				wbuPartnerklasseDatenrichtung.setEnabled(false);
			} else {
				wbuPartnerklasseDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerBranche.getText() != null
					&& wtfQuellPartnerBranche.getText() != null && wtfQuellPartnerBranche
					.getText().equals(wtfZielPartnerBranche.getText()))
					|| (wtfZielPartnerBranche.getText() == null && wtfQuellPartnerBranche
							.getText() == null)) {
				wbuBrancheDatenrichtung.setEnabled(false);
			} else {
				wbuBrancheDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerTelefon.getText() != null
					&& wtfQuellPartnerTelefon.getText() != null && wtfQuellPartnerTelefon
					.getText().equals(wtfZielPartnerTelefon.getText()))
					|| (wtfZielPartnerTelefon.getText() == null && wtfQuellPartnerTelefon
							.getText() == null)) {
				wbuTelefonDatenrichtung.setEnabled(false);
			} else {
				wbuTelefonDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerHandy.getText() != null
					&& wtfQuellPartnerHandy.getText() != null && wtfQuellPartnerHandy
					.getText().equals(wtfZielPartnerHandy.getText()))
					|| (wtfZielPartnerHandy.getText() == null && wtfQuellPartnerHandy
							.getText() == null)) {
				wbuHandyDatenrichtung.setEnabled(false);
			} else {
				wbuHandyDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerFax.getText() != null
					&& wtfQuellPartnerFax.getText() != null && wtfQuellPartnerFax
					.getText().equals(wtfZielPartnerFax.getText()))
					|| (wtfZielPartnerFax.getText() == null && wtfQuellPartnerFax
							.getText() == null)) {
				wbuFaxDatenrichtung.setEnabled(false);
			} else {
				wbuFaxDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerDirektfax.getText() != null
					&& wtfQuellPartnerDirektfax.getText() != null && wtfQuellPartnerDirektfax
					.getText().equals(wtfZielPartnerDirektfax.getText()))
					|| (wtfZielPartnerDirektfax.getText() == null && wtfQuellPartnerDirektfax
							.getText() == null)) {
				wbuDirektfaxDatenrichtung.setEnabled(false);
			} else {
				wbuDirektfaxDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerEmail.getText() != null
					&& wtfQuellPartnerEmail.getText() != null && wtfQuellPartnerEmail
					.getText().equals(wtfZielPartnerEmail.getText()))
					|| (wtfZielPartnerEmail.getText() == null && wtfQuellPartnerEmail
							.getText() == null)) {
				wbuEmailDatenrichtung.setEnabled(false);
			} else {
				wbuEmailDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerHomepage.getText() != null
					&& wtfQuellPartnerHomepage.getText() != null && wtfQuellPartnerHomepage
					.getText().equals(wtfZielPartnerHomepage.getText()))
					|| (wtfZielPartnerHomepage.getText() == null && wtfQuellPartnerHomepage
							.getText() == null)) {
				wbuHomepageDatenrichtung.setEnabled(false);
			} else {
				wbuHomepageDatenrichtung.setEnabled(true);
			}

			if ((wtaQuellPartnerPartnerbemerkung.getText() != null
					&& wtaZielPartnerPartnerbemerkung.getText() != null && wtaQuellPartnerPartnerbemerkung
					.getText().equals(wtaZielPartnerPartnerbemerkung.getText()))
					|| (wtaQuellPartnerPartnerbemerkung.getText() == null && wtaZielPartnerPartnerbemerkung
							.getText() == null)) {
				wbuPartnerbemerkungDatenrichtung.setEnabled(false);
			} else {
				wbuPartnerbemerkungDatenrichtung.setEnabled(true);
			}

			if ((wtfZielPartnerAnsprGeburtsdatum.getText() != null
					&& wtfQuellPartnerAnsprGeburtsdatum.getText() != null && wtfQuellPartnerAnsprGeburtsdatum
					.getText()
					.equals(wtfZielPartnerAnsprGeburtsdatum.getText()))
					|| (wtfZielPartnerAnsprGeburtsdatum.getText() == null && wtfQuellPartnerAnsprGeburtsdatum
							.getText() == null)) {
				wbuAnsprGeburtsdatumDatenrichtung.setEnabled(false);
			} else {
				wbuAnsprGeburtsdatumDatenrichtung.setEnabled(true);
			}

		} else {
			setzeStatusAllerButtonDatenrichtung(this.vWrapperButtonsZusammenfuehren);
		}
	}

	/**
	 * enabled/disabled abhaengig vom vorhandensein der quell/zieldtos alle
	 * datenrichtung-buttons
	 * 
	 * @param vButtons
	 *            Vector
	 */
	private void setzeStatusAllerButtonDatenrichtung(
			Vector<WrapperButtonZusammenfuehren> vButtons) {
		if (this.partnerQuellDto != null && this.partnerZielDto != null) {
			// wbuAuswahlReset.setEnabled(true);
			for (Enumeration<WrapperButtonZusammenfuehren> e = vButtons
					.elements(); e.hasMoreElements();) {
				((WrapperButtonZusammenfuehren) e.nextElement())
						.setEnabled(true);
			}
		} else {
			// wbuAuswahlReset.setEnabled(false);
			for (Enumeration<WrapperButtonZusammenfuehren> e = vButtons
					.elements(); e.hasMoreElements();) {
				((WrapperButtonZusammenfuehren) e.nextElement())
						.setEnabled(false);
			}
		}
	}

	/**
	 * je nach PartnerDtos/AnsprechpartnerDtos wird der zentrale
	 * Datenrichtungsbutton enabled oder disabled gesetzt
	 */
	private void setzeStatusButtonResetDatenrichtung() {
		if (this.sActionToDoBeimZusammenfuehren
				.equals(PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_PARTNER)) {
			if (this.partnerQuellDto != null
					&& this.partnerZielDto != null
					&& this.partnerQuellDto.getIId().intValue() != this.partnerZielDto
							.getIId().intValue()) {
				wbuAuswahlReset.setEnabled(true);
			} else {
				wbuAuswahlReset.setEnabled(false);
			}
		} else if (this.sActionToDoBeimZusammenfuehren
				.equals(PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_ANSPRECHPARTNER)) {
			if (this.ansprechpartnerQuellDto != null
					&& this.ansprechpartnerZielDto != null
					&& this.ansprechpartnerQuellDto.getIId().intValue() != this.ansprechpartnerZielDto
							.getIId().intValue()) {
				wbuAuswahlReset.setEnabled(true);
			} else {
				wbuAuswahlReset.setEnabled(false);
			}
		}
	}

	/**
	 * Wenn der Quellpartner Ansprechpartnereintraege hat, dann wird der Button
	 * aktiv, sonst inaktiv
	 */
	private void setzeVisibilityVonAnsprechpartnerQuellBtn() throws Throwable {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = DelegateFactory
				.getInstance().getAnsprechpartnerDelegate()
				.ansprechpartnerFindByPartnerIIdOhneExc(
						this.partnerQuellDto.getIId());
		if (aAnsprechpartnerDtos != null && aAnsprechpartnerDtos.length > 0) {
			this.wbuQuellPartnerAnsprechpartnerAuswaehlen.setEnabled(true);
			this.wbuQuellPartnerAnsprechpartnerAuswaehlen.setActivatable(true);
		} else {
			this.wbuQuellPartnerAnsprechpartnerAuswaehlen.setEnabled(false);
			this.wbuQuellPartnerAnsprechpartnerAuswaehlen.setActivatable(false);
		}
	}

	/**
	 * Wenn der Zielpartner Ansprechpartnereintraege hat, dann wird der Button
	 * aktiv, sonst inaktiv
	 */
	private void setzeVisibilityVonAnsprechpartnerZielBtn() throws Throwable {
		AnsprechpartnerDto[] aAnsprechpartnerDtos = DelegateFactory
				.getInstance().getAnsprechpartnerDelegate()
				.ansprechpartnerFindByPartnerIIdOhneExc(
						this.partnerZielDto.getIId());
		if (aAnsprechpartnerDtos != null && aAnsprechpartnerDtos.length > 0) {
			this.wbuZielPartnerAnsprechpartnerAuswaehlen.setEnabled(true);
			this.wbuZielPartnerAnsprechpartnerAuswaehlen.setActivatable(true);
		} else {
			this.wbuZielPartnerAnsprechpartnerAuswaehlen.setEnabled(false);
			this.wbuZielPartnerAnsprechpartnerAuswaehlen.setActivatable(false);
		}
	}

	private void befuelleFelderMitAnsprechpartnerQuellDto(
			AnsprechpartnerDto aqDto) throws Throwable {
		PartnerDto partnerDto = DelegateFactory.getInstance()
				.getPartnerDelegate().partnerFindByPrimaryKey(
						aqDto.getPartnerIIdAnsprechpartner());

		if (partnerDto.getAnredeCNr() != null) {
			wtfQuellPartnerAnrede.setText(partnerDto.getAnredeCNr());
			wtfQuellPartnerAnrede.setCaretPosition(0);
			wtfQuellPartnerAnrede.setToolTipText(partnerDto.getAnredeCNr());
		} else {
			wtfQuellPartnerAnrede.setText("");
			wtfQuellPartnerAnrede.setToolTipText("");
		}

		if (partnerDto.getCTitel() != null && partnerDto.getCTitel() != "") {
			wtfQuellPartnerTitel.setText(partnerDto.getCTitel());
			wtfQuellPartnerTitel.setCaretPosition(0);
			wtfQuellPartnerTitel.setToolTipText(partnerDto.getCTitel());
		} else {
			wtfQuellPartnerTitel.setText("");
			wtfQuellPartnerTitel.setToolTipText("");
		}

		if (partnerDto.getCName1nachnamefirmazeile1() != null
				&& partnerDto.getCName1nachnamefirmazeile1() != "") {
			wtfQuellPartnerName1.setText(partnerDto
					.getCName1nachnamefirmazeile1());
			wtfQuellPartnerName1.setCaretPosition(0);
			wtfQuellPartnerName1.setToolTipText(partnerDto
					.getCName1nachnamefirmazeile1());
		} else {
			wtfQuellPartnerName1.setText("");
			wtfQuellPartnerName1.setToolTipText("");
		}

		if (partnerDto.getCName2vornamefirmazeile2() != null
				&& partnerDto.getCName2vornamefirmazeile2() != "") {
			wtfQuellPartnerName2.setText(partnerDto
					.getCName2vornamefirmazeile2());
			wtfQuellPartnerName2.setCaretPosition(0);
			wtfQuellPartnerName2.setToolTipText(partnerDto
					.getCName2vornamefirmazeile2());
		} else {
			wtfQuellPartnerName2.setText("");
			wtfQuellPartnerName2.setToolTipText("");
		}

		if (partnerDto.getDGeburtsdatumansprechpartner() != null) {
			wtfQuellPartnerAnsprGeburtsdatum.setText(Helper.formatDatum(
					partnerDto.getDGeburtsdatumansprechpartner(), Locale
							.getDefault()));
			wtfQuellPartnerAnsprGeburtsdatum.setCaretPosition(0);
			wtfQuellPartnerAnsprGeburtsdatum.setToolTipText(Helper.formatDatum(
					partnerDto.getDGeburtsdatumansprechpartner(), Locale
							.getDefault()));
		} else {
			wtfQuellPartnerAnsprGeburtsdatum.setText("");
			wtfQuellPartnerAnsprGeburtsdatum.setToolTipText("");
		}

		if (aqDto.getCTelefon() != null) {

			wtfQuellPartnerTelefon.setText(aqDto.getCTelefon());
			wtfQuellPartnerTelefon.setCaretPosition(0);
			wtfQuellPartnerTelefon.setToolTipText(aqDto.getCTelefon());
		} else {
			wtfQuellPartnerTelefon.setText("");
			wtfQuellPartnerTelefon.setToolTipText("");
		}

		if (aqDto.getCHandy() != null) {

			wtfQuellPartnerHandy.setText(aqDto.getCHandy());
			wtfQuellPartnerHandy.setCaretPosition(0);
			wtfQuellPartnerHandy.setToolTipText(aqDto.getCHandy());
		} else {
			wtfQuellPartnerHandy.setText("");
			wtfQuellPartnerHandy.setToolTipText("");
		}

		if (aqDto.getCEmail() != null) {

			wtfQuellPartnerEmail.setText(aqDto.getCEmail());
			wtfQuellPartnerEmail.setCaretPosition(0);
			wtfQuellPartnerEmail.setToolTipText(aqDto.getCEmail());
		} else {
			wtfQuellPartnerEmail.setText("");
			wtfQuellPartnerEmail.setToolTipText("");
		}

		if (aqDto.getCFax() != null) {

			wtfQuellPartnerFax.setText(aqDto.getCFax());
			wtfQuellPartnerFax.setCaretPosition(0);
			wtfQuellPartnerFax.setToolTipText(aqDto.getCFax());
		} else {
			wtfQuellPartnerFax.setText("");
			wtfQuellPartnerFax.setToolTipText("");
		}

		if (aqDto.getCDirektfax() != null) {

			wtfQuellPartnerDirektfax.setText(aqDto.getCDirektfax());
			wtfQuellPartnerDirektfax.setCaretPosition(0);
			wtfQuellPartnerDirektfax.setToolTipText(aqDto.getCDirektfax());
		} else {
			wtfQuellPartnerDirektfax.setText("");
			wtfQuellPartnerDirektfax.setToolTipText("");
		}

		if (aqDto.getXBemerkung() != null) {
			wtaQuellPartnerPartnerbemerkung.setText(aqDto.getXBemerkung());
			wtaQuellPartnerPartnerbemerkung.setCaretPosition(0);
			wtaQuellPartnerPartnerbemerkung.setToolTipText(aqDto
					.getXBemerkung());
		} else {
			wtaQuellPartnerPartnerbemerkung.setText("");
			wtaQuellPartnerPartnerbemerkung.setToolTipText("");
		}

		if (aqDto.getAnsprechpartnerfunktionIId() != null) {
			AnsprechpartnerfunktionDto ansprechfktDto = DelegateFactory
					.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerfunktionFindByPrimaryKey(
							aqDto.getAnsprechpartnerfunktionIId());
			wtfQuellPartnerAnsprechpartnerfunktion.setText(ansprechfktDto
					.getBezeichnung());
			wtfQuellPartnerAnsprechpartnerfunktion.setCaretPosition(0);
			wtfQuellPartnerAnsprechpartnerfunktion
					.setToolTipText(ansprechfktDto.getBezeichnung());
		} else {
			wtfQuellPartnerAnsprechpartnerfunktion.setText("");
			wtfQuellPartnerAnsprechpartnerfunktion.setToolTipText("");
		}

		setzeStatusVonButtonDatenrichtung();
		setzeStatusButtonResetDatenrichtung();
	}

	private void befuelleFelderMitAnsprechpartnerZielDto(
			AnsprechpartnerDto azDto) throws Throwable {
		PartnerDto partnerDto = DelegateFactory.getInstance()
				.getPartnerDelegate().partnerFindByPrimaryKey(
						azDto.getPartnerIIdAnsprechpartner());

		if (partnerDto.getAnredeCNr() != null) {
			wtfZielPartnerAnrede.setText(partnerDto.getAnredeCNr());
			wtfZielPartnerAnrede.setCaretPosition(0);
			wtfZielPartnerAnrede.setToolTipText(partnerDto.getAnredeCNr());
		} else {
			wtfZielPartnerAnrede.setText("");
			wtfZielPartnerAnrede.setToolTipText("");
		}

		if (partnerDto.getCTitel() != null && partnerDto.getCTitel() != "") {
			wtfZielPartnerTitel.setText(partnerDto.getCTitel());
			wtfZielPartnerTitel.setCaretPosition(0);
			wtfZielPartnerTitel.setToolTipText(partnerDto.getCTitel());
		} else {
			wtfZielPartnerTitel.setText("");
			wtfZielPartnerTitel.setToolTipText("");
		}

		if (partnerDto.getCName1nachnamefirmazeile1() != null
				&& partnerDto.getCName1nachnamefirmazeile1() != "") {
			wtfZielPartnerName1.setText(partnerDto
					.getCName1nachnamefirmazeile1());
			wtfZielPartnerName1.setCaretPosition(0);
			wtfZielPartnerName1.setToolTipText(partnerDto
					.getCName1nachnamefirmazeile1());
		} else {
			wtfZielPartnerName1.setText("");
			wtfZielPartnerName1.setToolTipText("");
		}

		if (partnerDto.getCName2vornamefirmazeile2() != null
				&& partnerDto.getCName2vornamefirmazeile2() != "") {
			wtfZielPartnerName2.setText(partnerDto
					.getCName2vornamefirmazeile2());
			wtfZielPartnerName2.setCaretPosition(0);
			wtfZielPartnerName2.setToolTipText(partnerDto
					.getCName2vornamefirmazeile2());
		} else {
			wtfZielPartnerName2.setText("");
			wtfZielPartnerName2.setToolTipText("");
		}

		if (partnerDto.getDGeburtsdatumansprechpartner() != null) {
			wtfZielPartnerAnsprGeburtsdatum.setText(Helper.formatDatum(
					partnerDto.getDGeburtsdatumansprechpartner(), Locale
							.getDefault()));
			wtfZielPartnerAnsprGeburtsdatum.setCaretPosition(0);
			wtfZielPartnerAnsprGeburtsdatum.setToolTipText(Helper.formatDatum(
					partnerDto.getDGeburtsdatumansprechpartner(), Locale
							.getDefault()));
		} else {
			wtfZielPartnerAnsprGeburtsdatum.setText("");
			wtfZielPartnerAnsprGeburtsdatum.setToolTipText("");
		}

		if (azDto.getCTelefon() != null) {

			wtfZielPartnerTelefon.setText(azDto.getCTelefon());
			wtfZielPartnerTelefon.setCaretPosition(0);
			wtfZielPartnerTelefon.setToolTipText(azDto.getCTelefon());
		} else {
			wtfZielPartnerTelefon.setText("");
			wtfZielPartnerTelefon.setToolTipText("");
		}

		if (azDto.getCHandy() != null) {

			wtfZielPartnerHandy.setText(azDto.getCHandy());
			wtfZielPartnerHandy.setCaretPosition(0);
			wtfZielPartnerHandy.setToolTipText(azDto.getCHandy());
		} else {
			wtfZielPartnerHandy.setText("");
			wtfZielPartnerHandy.setToolTipText("");
		}

		if (azDto.getCEmail() != null) {

			wtfZielPartnerEmail.setText(azDto.getCEmail());
			wtfZielPartnerEmail.setCaretPosition(0);
			wtfZielPartnerEmail.setToolTipText(azDto.getCEmail());
		} else {
			wtfZielPartnerEmail.setText("");
			wtfZielPartnerEmail.setToolTipText("");
		}

		if (azDto.getCFax() != null) {

			wtfZielPartnerFax.setText(azDto.getCFax());
			wtfZielPartnerFax.setCaretPosition(0);
			wtfZielPartnerFax.setToolTipText(azDto.getCFax());
		} else {
			wtfZielPartnerFax.setText("");
			wtfZielPartnerFax.setToolTipText("");
		}

		if (azDto.getCDirektfax() != null) {

			wtfZielPartnerDirektfax.setText(azDto.getCDirektfax());
			wtfZielPartnerDirektfax.setCaretPosition(0);
			wtfZielPartnerDirektfax.setToolTipText(azDto.getCDirektfax());
		} else {
			wtfZielPartnerDirektfax.setText("");
			wtfZielPartnerDirektfax.setToolTipText("");
		}

		if (azDto.getXBemerkung() != null) {
			wtaZielPartnerPartnerbemerkung.setText(azDto.getXBemerkung());
			wtaZielPartnerPartnerbemerkung.setCaretPosition(0);
			wtaZielPartnerPartnerbemerkung
					.setToolTipText(azDto.getXBemerkung());
		} else {
			wtaZielPartnerPartnerbemerkung.setText("");
			wtaZielPartnerPartnerbemerkung.setToolTipText("");
		}

		if (azDto.getAnsprechpartnerfunktionIId() != null) {
			AnsprechpartnerfunktionDto ansprechfktDto = DelegateFactory
					.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerfunktionFindByPrimaryKey(
							azDto.getAnsprechpartnerfunktionIId());
			wtfZielPartnerAnsprechpartnerfunktion.setText(ansprechfktDto
					.getBezeichnung());
			wtfZielPartnerAnsprechpartnerfunktion.setCaretPosition(0);
			wtfZielPartnerAnsprechpartnerfunktion.setToolTipText(ansprechfktDto
					.getBezeichnung());
		} else {
			wtfZielPartnerAnsprechpartnerfunktion.setText("");
			wtfZielPartnerAnsprechpartnerfunktion.setToolTipText("");
		}

		setzeStatusVonButtonDatenrichtung();
		setzeStatusButtonResetDatenrichtung();

	}

	private void befuelleFelderMitPartnerQuellDto(PartnerDto pqDto)
			throws Throwable {
		if (this.partnerQuellDto != null && this.partnerZielDto != null) {
			if (pqDto.getAnredeCNr() != null) {
				wtfQuellPartnerAnrede.setText(pqDto.getAnredeCNr());
				wtfQuellPartnerAnrede.setCaretPosition(0);
				wtfQuellPartnerAnrede.setToolTipText(pqDto.getAnredeCNr());
			} else {
				wtfQuellPartnerAnrede.setText("");
				wtfQuellPartnerAnrede.setToolTipText("");
			}

			if (pqDto.getCTitel() != null) {
				wtfQuellPartnerTitel.setText(pqDto.getCTitel());
				wtfQuellPartnerTitel.setCaretPosition(0);
				wtfQuellPartnerTitel.setToolTipText(pqDto.getCTitel());
			} else {
				wtfQuellPartnerTitel.setText("");
				wtfQuellPartnerTitel.setToolTipText("");
			}

			if (pqDto.getCName1nachnamefirmazeile1() != null) {
				wtfQuellPartnerName1.setText(pqDto
						.getCName1nachnamefirmazeile1());
				wtfQuellPartnerName1.setCaretPosition(0);
				wtfQuellPartnerName1.setToolTipText(pqDto
						.getCName1nachnamefirmazeile1());
			} else {
				wtfQuellPartnerName1.setText("");
				wtfQuellPartnerName1.setToolTipText("");
			}

			if (pqDto.getCName2vornamefirmazeile2() != null) {
				wtfQuellPartnerName2.setText(pqDto
						.getCName2vornamefirmazeile2());
				wtfQuellPartnerName2.setCaretPosition(0);
				wtfQuellPartnerName2.setToolTipText(pqDto
						.getCName2vornamefirmazeile2());
			} else {
				wtfQuellPartnerName2.setText("");
				wtfQuellPartnerName2.setToolTipText("");
			}

			if (pqDto.getCName3vorname2abteilung() != null) {
				wtfQuellPartnerName3
						.setText(pqDto.getCName3vorname2abteilung());
				wtfQuellPartnerName3.setCaretPosition(0);
				wtfQuellPartnerName3.setToolTipText(pqDto
						.getCName3vorname2abteilung());
			} else {
				wtfQuellPartnerName3.setText("");
				wtfQuellPartnerName3.setToolTipText("");
			}

			if (pqDto.getCStrasse() != null) {
				wtfQuellPartnerStrasse.setText(pqDto.getCStrasse());
				wtfQuellPartnerStrasse.setCaretPosition(0);
				wtfQuellPartnerStrasse.setToolTipText(pqDto.getCStrasse());
			} else {
				wtfQuellPartnerStrasse.setText("");
				wtfQuellPartnerStrasse.setToolTipText("");
			}

			if (pqDto.getLandplzortDto() != null
					&& pqDto.getLandplzortDto().formatLandPlzOrt() != null) {
				wtfQuellPartnerOrt.setText(pqDto.getLandplzortDto()
						.formatLandPlzOrt());
				wtfQuellPartnerOrt.setCaretPosition(0);
				wtfQuellPartnerOrt.setToolTipText(pqDto.getLandplzortDto()
						.formatLandPlzOrt());
			} else {
				wtfQuellPartnerOrt.setText("");
				wtfQuellPartnerOrt.setToolTipText("");
			}

			if (pqDto.getLandplzortDto_Postfach() != null
					&& pqDto.getLandplzortDto_Postfach().formatLandPlzOrt() != null) {
				wtfQuellPartnerPostfachort.setText(pqDto
						.getLandplzortDto_Postfach().formatLandPlzOrt());
				wtfQuellPartnerPostfachort.setCaretPosition(0);
				wtfQuellPartnerPostfachort.setToolTipText(pqDto
						.getLandplzortDto_Postfach().formatLandPlzOrt());
			} else {
				wtfQuellPartnerPostfachort.setText("");
				wtfQuellPartnerPostfachort.setToolTipText("");
			}

			if (pqDto.getCPostfach() != null) {
				wtfQuellPartnerPostfachnr.setText(pqDto.getCPostfach());
				wtfQuellPartnerPostfachnr.setCaretPosition(0);
				wtfQuellPartnerPostfachnr.setToolTipText(pqDto.getCPostfach());
			} else {
				wtfQuellPartnerPostfachnr.setText("");
				wtfQuellPartnerPostfachnr.setToolTipText("");
			}

			if (pqDto.getCKbez() != null) {
				wtfQuellPartnerKurzbez.setText(pqDto.getCKbez());
				wtfQuellPartnerKurzbez.setCaretPosition(0);
				wtfQuellPartnerKurzbez.setToolTipText(pqDto.getCKbez());
			} else {
				wtfQuellPartnerKurzbez.setText("");
				wtfQuellPartnerKurzbez.setToolTipText("");
			}

			if (pqDto.getPartnerartCNr() != null) {
				wtfQuellPartnerPartnerart.setText(pqDto.getPartnerartCNr());
				wtfQuellPartnerPartnerart.setCaretPosition(0);
				wtfQuellPartnerPartnerart.setToolTipText(pqDto
						.getPartnerartCNr());
			} else {
				wtfQuellPartnerPartnerart.setText("");
				wtfQuellPartnerPartnerart.setToolTipText("");
			}

			if (pqDto.getCUid() != null) {
				wtfQuellPartnerUid.setText(pqDto.getCUid());
				wtfQuellPartnerUid.setCaretPosition(0);
				wtfQuellPartnerUid.setToolTipText(pqDto.getCUid());
			} else {
				wtfQuellPartnerUid.setText("");
				wtfQuellPartnerUid.setToolTipText("");
			}

			if (pqDto.getLocaleCNrKommunikation() != null) {
				wtfQuellPartnerKommunikationsspr.setText(pqDto
						.getLocaleCNrKommunikation());
				wtfQuellPartnerKommunikationsspr.setCaretPosition(0);
				wtfQuellPartnerKommunikationsspr.setToolTipText(pqDto
						.getLocaleCNrKommunikation());
			} else {
				wtfQuellPartnerKommunikationsspr.setText("");
				wtfQuellPartnerKommunikationsspr.setToolTipText("");
			}

			if (pqDto.getCIln() != null) {
				wtfQuellPartnerIln.setText(pqDto.getCIln());
				wtfQuellPartnerIln.setCaretPosition(0);
				wtfQuellPartnerIln.setToolTipText(pqDto.getCIln());
			} else {
				wtfQuellPartnerIln.setText("");
				wtfQuellPartnerIln.setToolTipText("");
			}

			if (pqDto.getCFilialnummer() != null) {
				wtfQuellPartnerFilialnr.setText(pqDto.getCFilialnummer());
				wtfQuellPartnerFilialnr.setCaretPosition(0);
				wtfQuellPartnerFilialnr
						.setToolTipText(pqDto.getCFilialnummer());
			} else {
				wtfQuellPartnerFilialnr.setText("");
				wtfQuellPartnerFilialnr.setToolTipText("");
			}

			if (pqDto.getCGerichtsstand() != null) {
				wtfQuellPartnerGerichtsstand.setText(pqDto.getCGerichtsstand());
				wtfQuellPartnerGerichtsstand.setCaretPosition(0);
				wtfQuellPartnerGerichtsstand.setToolTipText(pqDto
						.getCGerichtsstand());
			} else {
				wtfQuellPartnerGerichtsstand.setText("");
				wtfQuellPartnerGerichtsstand.setToolTipText("");
			}

			if (pqDto.getCFirmenbuchnr() != null) {
				wtfQuellPartnerFirmenbuchnr.setText(pqDto.getCFirmenbuchnr());
				wtfQuellPartnerFirmenbuchnr.setCaretPosition(0);
				wtfQuellPartnerFirmenbuchnr.setToolTipText(pqDto
						.getCFirmenbuchnr());
			} else {
				wtfQuellPartnerFirmenbuchnr.setText("");
				wtfQuellPartnerFirmenbuchnr.setToolTipText("");
			}

			if (pqDto.getBankDto() != null) {
				wtfQuellBIC.setText(pqDto.getBankDto().getCBic());
				wtfQuellBLZ.setText(pqDto.getBankDto().getCBlz());
			} else {
				wtfQuellBIC.setText("");
				wtfQuellBLZ.setText("");
			}

			if (pqDto.getPartnerklasseIId() != null) {
				wtfQuellPartnerPartnerklasse.setText(DelegateFactory
						.getInstance().getPartnerDelegate()
						.partnerklasseFindByPrimaryKey(
								pqDto.getPartnerklasseIId()).getBezeichnung());
				wtfQuellPartnerPartnerklasse.setCaretPosition(0);
				wtfQuellPartnerPartnerklasse.setToolTipText(DelegateFactory
						.getInstance().getPartnerDelegate()
						.partnerklasseFindByPrimaryKey(
								pqDto.getPartnerklasseIId()).getBezeichnung());

			} else {
				wtfQuellPartnerPartnerklasse.setText("");
				wtfQuellPartnerPartnerklasse.setToolTipText("");

			}

			if (pqDto.getBrancheIId() != null) {
				wtfQuellPartnerBranche.setText(DelegateFactory.getInstance()
						.getPartnerDelegate().brancheFindByPrimaryKey(
								pqDto.getBrancheIId()).getBezeichnung());
				wtfQuellPartnerBranche.setCaretPosition(0);
				wtfQuellPartnerBranche.setToolTipText(DelegateFactory
						.getInstance().getPartnerDelegate()
						.brancheFindByPrimaryKey(pqDto.getBrancheIId())
						.getBezeichnung());

			} else {
				wtfQuellPartnerBranche.setText("");
				wtfQuellPartnerBranche.setToolTipText("");

			}

			if (pqDto.getCTelefon() != null) {
				wtfQuellPartnerTelefon.setText(pqDto.getCTelefon());
				wtfQuellPartnerTelefon.setCaretPosition(0);
				wtfQuellPartnerTelefon.setToolTipText(pqDto.getCTelefon());
			} else {
				wtfQuellPartnerTelefon.setText("");
				wtfQuellPartnerTelefon.setToolTipText("");
			}

			if (pqDto.getCFax() != null) {
				wtfQuellPartnerFax.setText(pqDto.getCFax());
				wtfQuellPartnerFax.setCaretPosition(0);
				wtfQuellPartnerFax.setToolTipText(pqDto.getCFax());
			} else {
				wtfQuellPartnerFax.setText("");
				wtfQuellPartnerFax.setToolTipText("");
			}

			if (pqDto.getCEmail() != null) {
				wtfQuellPartnerEmail.setText(pqDto.getCEmail());
				wtfQuellPartnerEmail.setCaretPosition(0);
				wtfQuellPartnerEmail.setToolTipText(pqDto.getCEmail());
			} else {
				wtfQuellPartnerEmail.setText("");
				wtfQuellPartnerEmail.setToolTipText("");
			}

			if (pqDto.getCHomepage() != null) {
				wtfQuellPartnerHomepage.setText(pqDto.getCHomepage());
				wtfQuellPartnerHomepage.setCaretPosition(0);
				wtfQuellPartnerHomepage.setToolTipText(pqDto.getCHomepage());
			} else {
				wtfQuellPartnerHomepage.setText("");
				wtfQuellPartnerHomepage.setToolTipText("");
			}

			if (pqDto.getXBemerkung() != null) {
				wtaQuellPartnerPartnerbemerkung.setText(pqDto.getXBemerkung());
				wtaQuellPartnerPartnerbemerkung.setCaretPosition(0);
				wtaQuellPartnerPartnerbemerkung.setToolTipText(pqDto
						.getXBemerkung());
			} else {
				wtaQuellPartnerPartnerbemerkung.setText("");
				wtaQuellPartnerPartnerbemerkung.setToolTipText("");
			}

			if (pqDto.getDGeburtsdatumansprechpartner() != null) {
				wtfQuellPartnerAnsprGeburtsdatum.setText(Helper.formatDatum(
						pqDto.getDGeburtsdatumansprechpartner(), Locale
								.getDefault()));
				wtfQuellPartnerAnsprGeburtsdatum.setCaretPosition(0);
				wtfQuellPartnerAnsprGeburtsdatum.setToolTipText(Helper
						.formatDatum(pqDto.getDGeburtsdatumansprechpartner(),
								Locale.getDefault()));
			} else {
				wtfQuellPartnerAnsprGeburtsdatum.setText("");
				wtfQuellPartnerAnsprGeburtsdatum.setToolTipText("");
			}

			setzeStatusVonButtonDatenrichtung();
		}

		else {
			setzeStatusAllerButtonDatenrichtung(vWrapperButtonsZusammenfuehren);
		}
		setzeVisibilityVonAnsprechpartnerQuellBtn();
		setzeStatusButtonResetDatenrichtung();

	}

	/*
	 * // ACTION_LEEREN geht hier nicht - loeschen protected void
	 * eventItemchanged(EventObject eI) throws Throwable { ItemChangedEvent e =
	 * (ItemChangedEvent) eI;
	 * 
	 * if (e.getID() == ItemChangedEvent.ACTION_LEEREN) { if (e.getSource() ==
	 * panelAnsprechpartner) { //ansprechpartnerDto = new AnsprechpartnerDto();
	 * //wtfAnsprechpartner.setText(""); } } }
	 */

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand()
						.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			bHandleEventInSuperklasse = true;

			if (bHandleEventInSuperklasse) {
				super.eventActionSpecial(e); // close Dialog
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			boolean erfolgreich = false;

			if (this.sActionToDoBeimZusammenfuehren
					.equals(PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_PARTNER)) {
				// ToDo beim Partnerzusammenfuehren

				boolean bKundeMitverdichten = false;
				boolean bLieferantMitverdichten = false;
				boolean bBankMitverdichten = false;
				if (this.wcbKundeZusammenfuehren.getShort().intValue() == 1) {
					bKundeMitverdichten = true;
				}
				if (this.wcbLieferantZusammenfuehren.getShort().intValue() == 1) {
					bLieferantMitverdichten = true;
				}

				if (this.partnerQuellDto != null
						&& this.partnerZielDto != null
						&& this.partnerQuellDto.getIId().intValue() != this.partnerZielDto
								.getIId().intValue()) {

					schreibeRichtungInZielPartner();
					try {
						DelegateFactory.getInstance().getPartnerDelegate()
								.zusammenfuehrenPartner(this.partnerZielDto,
										this.partnerQuellDto.getIId(),
										bKundeMitverdichten,
										bLieferantMitverdichten,
										bBankMitverdichten);
						erfolgreich = true;
					} catch (ExceptionLP ex) {
						boolean b = handleOwnException(ex);
					}
				} else {
					// warnung, quelle und ziel sind identisch
					String sText = LPMain
							.getInstance()
							.getTextRespectUISPr(
									"part.zusammenfuehren.warnung.quelle.ziel.identisch");
					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"part.partnerzusammenfuehren"), sText);
				}

			} else if (this.sActionToDoBeimZusammenfuehren
					.equals(PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_ANSPRECHPARTNER)) {
				// ToDo beim Ansprechpartnerzusammenfuehren

				if (this.ansprechpartnerQuellDto != null
						&& this.ansprechpartnerZielDto != null
						&& this.ansprechpartnerQuellDto.getIId().intValue() != this.ansprechpartnerZielDto
								.getIId().intValue()) {

					schreibeRichtungInZielAnsprechpartner(); // auch
					// partnerdaten
					// 4 zeilen
					// uebernehmen
					// nach
					// partnerVonAnsprechpartnerZielDto
					try {
						DelegateFactory.getInstance()
								.getAnsprechpartnerDelegate()
								.zusammenfuehrenAnsprechpartner(
										this.ansprechpartnerZielDto,
										this.ansprechpartnerQuellDto.getIId(),
										this.partnerVonAnsprechpartnerZielDto);
						erfolgreich = true;
					} catch (ExceptionLP ex) {
						boolean b = handleOwnException(ex);
					}
				} else {
					// warnung, quelle und ziel sind identisch
					String sText = LPMain
							.getInstance()
							.getTextRespectUISPr(
									"part.zusammenfuehren.warnung.ansprechpartner.quelle.ziel.identisch");
					DialogFactory.showModalDialog(LPMain.getInstance()
							.getTextRespectUISPr(
									"part.ansprechpartnerzusammenfuehren"),
							sText);
				}

			}

			if (erfolgreich) {
				// AuswahlFLR refreshen
				((InternalFramePartner) getInternalFrame()).getTpPartner()
						.getPanelPartnerQP1().eventYouAreSelected(true);
				getInternalFrame().closePanelDialog();
			}
		}

		if (e.getActionCommand().equals(ACTION_ZIELPARTNER_AUSWAEHLEN)) {
			this.sActionToDoBeimZusammenfuehren = PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_PARTNER;

			Integer selectedId = null;
			if (partnerZielDto != null) {
				selectedId = partnerZielDto.getIId();
			}

			panelPartner = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame(), selectedId,
							false);
			DialogQuery dialogQueryPartner = new DialogQuery(panelPartner);

			if (panelPartner.getSelectedId() != null) {

				Object oKey = panelPartner.getSelectedId();
				PartnerDto partnerDtoTmp = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(
								(Integer) oKey);
				partnerDtoTmp.setBankDto(DelegateFactory.getInstance()
						.getPartnerbankDelegate().bankFindByPrimaryKeyOhneExc(
								(Integer) oKey));
				this.partnerZielDto = partnerDtoTmp;
				setzeFelderVonVektorSichtbar(this.vAnsprechpartnerkomponenten,
						false);
				setzeFelderVonVektorSichtbar(this.vPartnerkomponenten, true);
				befuelleFelderMitPartnerZielDto(this.partnerZielDto);
				setSaveButtonStatus();
			}
		}

		if (e.getActionCommand().equals(ACTION_QUELLPARTNER_AUSWAEHLEN)) {
			this.sActionToDoBeimZusammenfuehren = PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_PARTNER;

			Integer selectedId = null;
			if (partnerQuellDto != null) {
				selectedId = partnerQuellDto.getIId();
			}

			panelPartner = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame(), selectedId,
							false);
			DialogQuery dialogQueryPartner = new DialogQuery(panelPartner);

			if (panelPartner.getSelectedId() != null) {

				Object oKey = panelPartner.getSelectedId();
				PartnerDto partnerDtoTmp = DelegateFactory.getInstance()
						.getPartnerDelegate().partnerFindByPrimaryKey(
								(Integer) oKey);
				partnerDtoTmp.setBankDto(DelegateFactory.getInstance()
						.getPartnerbankDelegate().bankFindByPrimaryKeyOhneExc(
								(Integer) oKey));

				this.partnerQuellDto = partnerDtoTmp;
				setzeFelderVonVektorSichtbar(this.vAnsprechpartnerkomponenten,
						false);
				setzeFelderVonVektorSichtbar(this.vPartnerkomponenten, true);
				befuelleFelderMitPartnerQuellDto(this.partnerQuellDto);
				setSaveButtonStatus();
			}

		}

		if (e.getActionCommand().equals(
				ACTION_ZIELPARTNERANSPRECHPARTNER_AUSWAEHLEN)) {
			this.sActionToDoBeimZusammenfuehren = PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_ANSPRECHPARTNER;
			panelAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(getInternalFrame(),
							this.partnerZielDto.getIId(), null, false, false);
			DialogQuery dialogQueryAnsprechpartner = new DialogQuery(
					panelAnsprechpartner);

			Object oKey = panelAnsprechpartner.getSelectedId();
			if (oKey != null) {
				AnsprechpartnerDto ansprechpartnerDtoTmp = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) oKey);
				if (this.ansprechpartnerQuellDto != null
						&& this.ansprechpartnerQuellDto.getIId() != null
						&& this.ansprechpartnerQuellDto.getIId().intValue() == ansprechpartnerDtoTmp
								.getIId().intValue()) {
					String sMsg = LPMain
							.getInstance()
							.getTextRespectUISPr(
									"part.zusammenfuehren.warnung.ansprechpartner.bereits.ausgewaehlt");
					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"part.partnerzusammenfuehren"), sMsg);
				} else {
					this.ansprechpartnerZielDto = ansprechpartnerDtoTmp;
					setzeFelderVonVektorSichtbar(this.vPartnerkomponenten,
							false);
					setzeFelderVonVektorSichtbar(
							this.vAnsprechpartnerkomponenten, true);
					befuelleFelderMitAnsprechpartnerZielDto(this.ansprechpartnerZielDto);
					setSaveButtonStatus();
				}
			} else {
				this.ansprechpartnerZielDto = null;
			}
		}

		if (e.getActionCommand().equals(
				ACTION_QUELLPARTNERANSPRECHPARTNER_AUSWAEHLEN)) {
			this.sActionToDoBeimZusammenfuehren = PartnerFac.PART_ZUSAMMENFUEHREN_MODUS_ANSPRECHPARTNER;
			panelAnsprechpartner = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(getInternalFrame(),
							this.partnerQuellDto.getIId(), null, false, false);
			DialogQuery dialogQueryAnsprechpartner = new DialogQuery(
					panelAnsprechpartner);

			Object oKey = panelAnsprechpartner.getSelectedId();
			if (oKey != null) {
				AnsprechpartnerDto ansprechpartnerDtoTmp = DelegateFactory
						.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) oKey);
				if (this.ansprechpartnerZielDto != null
						&& this.ansprechpartnerZielDto.getIId() != null
						&& this.ansprechpartnerZielDto.getIId().intValue() == ansprechpartnerDtoTmp
								.getIId().intValue()) {
					String sMsg = LPMain
							.getInstance()
							.getTextRespectUISPr(
									"part.zusammenfuehren.warnung.ansprechpartner.bereits.ausgewaehlt");
					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"part.partnerzusammenfuehren"), sMsg);
				} else {
					this.ansprechpartnerQuellDto = ansprechpartnerDtoTmp;
					setzeFelderVonVektorSichtbar(this.vPartnerkomponenten,
							false);
					setzeFelderVonVektorSichtbar(
							this.vAnsprechpartnerkomponenten, true);
					befuelleFelderMitAnsprechpartnerQuellDto(this.ansprechpartnerQuellDto);
					setSaveButtonStatus();
				}
			} else {
				this.ansprechpartnerQuellDto = null;
			}
		}

		if (e.getActionCommand().equals(ACTION_DATENRICHTUNG_WAEHLEN)) {
			// ToDo: Pfeil nach links, Pfeil nach rechts oder Kombinationsringe
			// (unendlich zeichen)
			if (e.getSource() instanceof WrapperButtonZusammenfuehren) {
				((WrapperButtonZusammenfuehren) e.getSource())
						.setNextSelection();
			}
			setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren,
					this.vZielfelder, this.vQuellfelder, false);
		}

		if (e.getActionCommand().equals(ACTION_ALLE_DATENRICHTUNGEN_WAEHLEN)) {
			// ToDo: Pfeil nach links, Pfeil nach rechts oder Kombinationsringe
			// (unendlich zeichen)
			if (e.getSource() instanceof WrapperButtonZusammenfuehren) {
				((WrapperButtonZusammenfuehren) e.getSource())
						.setNextSelection();
				/*
				 * hier alle weiteren zusammenfuehrenButtons auf die oben
				 * gesetzte nextSelection setzen
				 */
				System.out.println("ButtonVectorSize: "
						+ vWrapperButtonsZusammenfuehren.size());
				int iAktSel = ((WrapperButtonZusammenfuehren) e.getSource())
						.getISelection();
				for (Enumeration<WrapperButtonZusammenfuehren> el = vWrapperButtonsZusammenfuehren
						.elements(); el.hasMoreElements();) {
					((WrapperButtonZusammenfuehren) el.nextElement())
							.setISelection(iAktSel);
				}
				setzeFarbenVonTextfeldern(this.vWrapperButtonsZusammenfuehren,
						this.vZielfelder, this.vQuellfelder, false);
			}
		}

	}

	/**
	 * Schreibt die selektierten Ansprechpartner- und Partnerdaten in die
	 * ansprechpartnerZielDto bzw. partnerVonAnsprechpartnerZielDto Einige
	 * Dateninhalte sind nicht kombinierbar
	 * 
	 * @throws Throwable
	 */
	private void schreibeRichtungInZielAnsprechpartner() throws Throwable {

		String sTmp = "";

		this.partnerVonAnsprechpartnerZielDto = DelegateFactory.getInstance()
				.getPartnerDelegate().partnerFindByPrimaryKey(
						this.ansprechpartnerZielDto
								.getPartnerIIdAnsprechpartner());

		// 'Ansprechpartner' und Partner
		if (wbuAnredeDatenrichtung.isEnabled()) {
			if (wbuAnredeDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerVonAnsprechpartnerZielDto
						.setAnredeCNr(this.ansprechpartnerQuellDto
								.getPartnerDto().getAnredeCNr());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuTitelDatenrichtung.isEnabled()) {
			if (wbuTitelDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerVonAnsprechpartnerZielDto
						.setCTitel(this.ansprechpartnerQuellDto.getPartnerDto()
								.getCTitel());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuName1Datenrichtung.isEnabled()) {
			if (wbuName1Datenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerVonAnsprechpartnerZielDto
						.setCName1nachnamefirmazeile1(this.ansprechpartnerQuellDto
								.getPartnerDto().getCName1nachnamefirmazeile1());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuName2Datenrichtung.isEnabled()) {
			if (wbuName2Datenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerVonAnsprechpartnerZielDto
						.setCName2vornamefirmazeile2(this.ansprechpartnerQuellDto
								.getPartnerDto().getCName2vornamefirmazeile2());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuAnsprGeburtsdatumDatenrichtung.isEnabled()) {
			if (wbuAnsprGeburtsdatumDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerVonAnsprechpartnerZielDto
						.setDGeburtsdatumansprechpartner(this.ansprechpartnerQuellDto
								.getPartnerDto()
								.getDGeburtsdatumansprechpartner());
			}
		}

		// Ansprechpartner und Partner
		if (wbuTelefonDatenrichtung.isEnabled()) {
			if (wbuTelefonDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setCTelefon(this.ansprechpartnerQuellDto.getCTelefon());

			}
		}

		// Ansprechpartner
		if (wbuHandyDatenrichtung.isEnabled()) {
			if (wbuHandyDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setCHandy(this.ansprechpartnerQuellDto.getCHandy());
			}
		}

		// Ansprechpartner und Partner
		if (wbuFaxDatenrichtung.isEnabled()) {
			if (wbuFaxDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setCFax(this.ansprechpartnerQuellDto.getCFax());

			}
		}

		// Ansprechpartner
		if (wbuDirektfaxDatenrichtung.isEnabled()) {
			if (wbuDirektfaxDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setCDirektfax(this.ansprechpartnerQuellDto
								.getCDirektfax());

			}
		}

		// Ansprechpartner und Partner
		if (wbuEmailDatenrichtung.isEnabled()) {
			if (wbuEmailDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setCEmail(this.ansprechpartnerQuellDto.getCEmail());

			}
		}

		if (wbuPartnerbemerkungDatenrichtung.isEnabled()) {
			if (wbuPartnerbemerkungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setXBemerkung(this.ansprechpartnerQuellDto
								.getXBemerkung());
			} else if (wbuPartnerbemerkungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
				String sTmpText = this.ansprechpartnerZielDto.getXBemerkung()
						+ " " + this.ansprechpartnerQuellDto.getXBemerkung();
				if (sTmpText.length() > MAX_PARTNERBEMERKUNG) {
					sTmpText = sTmpText.substring(0, MAX_PARTNERBEMERKUNG);
				}
				this.ansprechpartnerZielDto.setXBemerkung(sTmpText);
			}
		}

		// Ansprechpartner
		if (wbuAnsprechpartnerfunktionDatenrichtung.isEnabled()) {
			if (wbuAnsprechpartnerfunktionDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.ansprechpartnerZielDto
						.setAnsprechpartnerfunktionIId(this.ansprechpartnerQuellDto
								.getAnsprechpartnerfunktionIId());
			}
		}

	}

	/**
	 * Schreibt die selektierten Partnerdaten in die zielpartnerDto Einige
	 * Dateninhalte wie z.B. Anrede, Ort, etc... sind nicht kombinierbar
	 * 
	 * @throws Throwable
	 */
	private void schreibeRichtungInZielPartner() throws Throwable {
		/*
		 * alle buttons vergleichen: wenn der btn nach li zeigt: nix wenn der
		 * button nach rechts zeigt & aktiv ist: schreibe von quelle diese daten
		 * in ziel wenn der button kombiniert zeigt & aktiv ist: fuege daten von
		 * quelle zu ziel hinzu (evtl. laenge pruefen)
		 */
		String sTmp = "";

		// 'Ansprechpartner' und Partner
		if (wbuAnredeDatenrichtung.isEnabled()) {
			if (wbuAnredeDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setAnredeCNr(this.partnerQuellDto
						.getAnredeCNr());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuTitelDatenrichtung.isEnabled()) {
			if (wbuTitelDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCTitel(this.partnerQuellDto.getCTitel());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuName1Datenrichtung.isEnabled()) {
			if (wbuName1Datenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto
						.setCName1nachnamefirmazeile1(this.partnerQuellDto
								.getCName1nachnamefirmazeile1());
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuName2Datenrichtung.isEnabled()) {
			if (wbuName2Datenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto
						.setCName2vornamefirmazeile2(this.partnerQuellDto
								.getCName2vornamefirmazeile2());
			}
		}

		if (wbuName3Datenrichtung.isEnabled()) {
			if (wbuName3Datenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto
						.setCName3vorname2abteilung(this.partnerQuellDto
								.getCName3vorname2abteilung());
			}
		}

		if (wbuStrasseDatenrichtung.isEnabled()) {
			if (wbuStrasseDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCStrasse(this.partnerQuellDto
						.getCStrasse());
			}
		}

		if (wbuOrtDatenrichtung.isEnabled()) {
			if (wbuOrtDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setLandplzortIId(this.partnerQuellDto
						.getLandplzortIId());
			}
		}

		if (wbuPostfachortDatenrichtung.isEnabled()) {
			if (wbuPostfachortDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto
						.setLandplzortIIdPostfach(this.partnerQuellDto
								.getLandplzortIIdPostfach());
			}
		}

		if (wbuPostfachnrDatenrichtung.isEnabled()) {
			if (wbuPostfachnrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCPostfach(this.partnerQuellDto
						.getCPostfach());
			}
		}

		if (wbuKurzbezDatenrichtung.isEnabled()) {
			if (wbuKurzbezDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCKbez(this.partnerQuellDto.getCKbez());
			}
		}

		if (wbuPartnerartDatenrichtung.isEnabled()) {
			if (wbuPartnerartDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setPartnerartCNr(this.partnerQuellDto
						.getPartnerartCNr());
			}
		}

		if (wbuUidDatenrichtung.isEnabled()) {
			if (wbuUidDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCUid(this.partnerQuellDto.getCUid());
			}
		}

		if (wbuKommunikationssprDatenrichtung.isEnabled()) {
			if (wbuKommunikationssprDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto
						.setLocaleCNrKommunikation(this.partnerQuellDto
								.getLocaleCNrKommunikation());
			}
		}

		if (wbuIlnDatenrichtung.isEnabled()) {
			if (wbuIlnDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCIln(this.partnerQuellDto.getCIln());
			}
		}

		if (wbuFilialnrDatenrichtung.isEnabled()) {
			if (wbuFilialnrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCFilialnummer(this.partnerQuellDto
						.getCFilialnummer());
			}
		}

		if (wbuGerichtsstandDatenrichtung.isEnabled()) {
			if (wbuGerichtsstandDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCGerichtsstand(this.partnerQuellDto
						.getCGerichtsstand());
			}
		}

		if (wbuFirmenbuchnrDatenrichtung.isEnabled()) {
			if (wbuFirmenbuchnrDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCFirmenbuchnr(this.partnerQuellDto
						.getCFirmenbuchnr());
			}
		}
		if (wbuBIC.isEnabled()) {
			if (wbuBIC.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				if (this.partnerZielDto.getBankDto() != null
						&& this.partnerQuellDto.getBankDto() != null) {
					this.partnerZielDto.getBankDto().setCBic(
							this.partnerQuellDto.getBankDto().getCBic());
				}
			}
		}
		if (wbuBLZ.isEnabled()) {
			if (wbuBLZ.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				if (this.partnerZielDto.getBankDto() != null
						&& this.partnerQuellDto.getBankDto() != null) {
					this.partnerZielDto.getBankDto().setCBlz(
							this.partnerQuellDto.getBankDto().getCBlz());
				}
			}
		}
		if (wbuPartnerklasseDatenrichtung.isEnabled()) {
			if (wbuPartnerklasseDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setPartnerklasseIId(this.partnerQuellDto
						.getPartnerklasseIId());
			}
		}

		if (wbuBrancheDatenrichtung.isEnabled()) {
			if (wbuBrancheDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setBrancheIId(this.partnerQuellDto
						.getBrancheIId());
			}
		}

		// Ansprechpartner und Partner
		if (wbuTelefonDatenrichtung.isEnabled()) {
			if (wbuTelefonDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCTelefon(this.partnerQuellDto
						.getCTelefon());

			}
		}

		// Ansprechpartner und Partner
		if (wbuFaxDatenrichtung.isEnabled()) {
			if (wbuFaxDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCFax(this.partnerQuellDto.getCFax());

			}
		}

		// Asnprechpartner und Partner
		if (wbuEmailDatenrichtung.isEnabled()) {
			if (wbuEmailDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCEmail(this.partnerQuellDto.getCEmail());
			}
		}

		if (wbuHomepageDatenrichtung.isEnabled()) {
			if (wbuHomepageDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setCHomepage(this.partnerQuellDto
						.getCHomepage());
			}
		}

		if (wbuPartnerbemerkungDatenrichtung.isEnabled()) {
			if (wbuPartnerbemerkungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto.setXBemerkung(this.partnerQuellDto
						.getXBemerkung());
			} else if (wbuPartnerbemerkungDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_KOMBINIERTE_DATENINHALTE) {
				String sTmpText = this.partnerZielDto.getXBemerkung() + " "
						+ this.partnerQuellDto.getXBemerkung();
				if (sTmpText.length() > MAX_PARTNERBEMERKUNG) {
					sTmpText = sTmpText.substring(0, MAX_PARTNERBEMERKUNG);
				}
				this.partnerZielDto.setXBemerkung(sTmpText);
			}
		}

		// 'Ansprechpartner' und Partner
		if (wbuAnsprGeburtsdatumDatenrichtung.isEnabled()) {
			if (wbuAnsprGeburtsdatumDatenrichtung.getISelection() == WrapperButtonZusammenfuehren.SELECT_RECHTEN_DATENINHALT) {
				this.partnerZielDto
						.setDGeburtsdatumansprechpartner(this.partnerQuellDto
								.getDGeburtsdatumansprechpartner());
			}
		}

	}

	private String handleExceptionBereitsVorhandenLieferant(
			ArrayList<?> alInfoForTheClient) {
		String sMsg = "";
		String s0Lieferant = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s1Lieferantenname = "";
		String s2Mandant = LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant");
		String s3Mandantcnr = "";
		String s4Lieferant = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s5Lieferantenname = "";
		String s6Mandant = LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant");
		String s7Mandantencrn = "";
		// [0] ist zB lp.lieferant, [1] ist lieferant1, [2] ist lieferant2
		for (int i = 1; i < alInfoForTheClient.size(); i++) {
			if (i == alInfoForTheClient.size() - 1) {
				// letzter eintrag, kein umbruch
				LieferantDto lieferantDto = null;
				try {
					lieferantDto = DelegateFactory.getInstance()
							.getLieferantDelegate().lieferantFindByPrimaryKey(
									Integer.valueOf(
											alInfoForTheClient.get(i)
													.toString()).intValue());
				} catch (Throwable ex) {
					// nothing
				}
				if (lieferantDto != null) {
					if (lieferantDto.getPartnerDto() != null) {
						s5Lieferantenname = lieferantDto.getPartnerDto()
								.formatName();
					}
					s7Mandantencrn = lieferantDto.getMandantCNr();
				}
			} else {
				LieferantDto lieferantDto = null;
				try {
					lieferantDto = DelegateFactory.getInstance()
							.getLieferantDelegate().lieferantFindByPrimaryKey(
									Integer.valueOf(
											alInfoForTheClient.get(i)
													.toString()).intValue());
				} catch (Throwable ex) {
					// nothing
				}
				if (lieferantDto != null) {
					if (lieferantDto.getPartnerDto() != null) {
						s1Lieferantenname = lieferantDto.getPartnerDto()
								.formatName();
					}
					s3Mandantcnr = lieferantDto.getMandantCNr();
				}
			}
		}
		MessageFormat mf = new MessageFormat(
				LPMain
						.getTextRespectUISPr("part.zusammenfuehren.warnung.bereits.vorhanden"));
		try {
			mf.setLocale(LPMain.getTheClient().getLocUi());
		} catch (Throwable ex1) {
		}
		Object pattern[] = { s0Lieferant, s1Lieferantenname, s2Mandant,
				s3Mandantcnr, s4Lieferant, s5Lieferantenname, s6Mandant,
				s7Mandantencrn };
		sMsg = mf.format(pattern);
		return sMsg;

	}

	private String handleExceptionBereitsVorhandenKunde(
			ArrayList<?> alInfoForTheClient) {
		String sMsg = "";
		String s0Kunde = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s1Kundenname = "";
		String s2Mandant = LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant");
		String s3Mandantcnr = "";
		String s4Kunde = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s5Kundenname = "";
		String s6Mandant = LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant");
		String s7Mandantencrn = "";
		for (int i = 1; i < alInfoForTheClient.size(); i++) {
			if (i == alInfoForTheClient.size() - 1) {
				// letzter eintrag, kein umbruch
				KundeDto kundeDto = null;
				try {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(
									Integer.valueOf(
											alInfoForTheClient.get(i)
													.toString()).intValue());
				} catch (Throwable ex) {
					// nothing
				}
				if (kundeDto != null) {
					if (kundeDto.getPartnerDto() != null) {
						s5Kundenname = kundeDto.getPartnerDto().formatName();
					}
					s7Mandantencrn = kundeDto.getMandantCNr();
				}
			} else {
				KundeDto kundeDto = null;
				try {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(
									Integer.valueOf(
											alInfoForTheClient.get(i)
													.toString()).intValue());
				} catch (Throwable ex) {
					// nothing
				}
				if (kundeDto != null) {
					if (kundeDto.getPartnerDto() != null) {
						s1Kundenname = kundeDto.getPartnerDto().formatName();
					}
					s3Mandantcnr = kundeDto.getMandantCNr();
				}
			}
		}

		MessageFormat mf = new MessageFormat(
				LPMain
						.getTextRespectUISPr("part.zusammenfuehren.warnung.bereits.vorhanden"));
		try {
			mf.setLocale(LPMain.getTheClient().getLocUi());
		} catch (Throwable ex1) {
		}

		Object pattern[] = { s0Kunde, s1Kundenname, s2Mandant, s3Mandantcnr,
				s4Kunde, s5Kundenname, s6Mandant, s7Mandantencrn };
		sMsg = mf.format(pattern);

		return sMsg;
	}

	private String handleExceptionBereitsVorhandenPersonal(
			ArrayList<?> alInfoForTheClient) {
		String sMsg = "";
		String s0Personal = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s1Personalname = "";
		String s2Mandant = LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant");
		String s3Mandantcnr = "";
		String s4Personal = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s5Personalname = "";
		String s6Mandant = LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant");
		String s7Mandantencrn = "";
		for (int i = 1; i < alInfoForTheClient.size(); i++) {
			if (i == alInfoForTheClient.size() - 1) {
				// letzter eintrag, kein umbruch
				PersonalDto personalDto = null;
				try {
					personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate().personalFindByPrimaryKey(
									Integer.valueOf(
											alInfoForTheClient.get(i)
													.toString()).intValue());
				} catch (Throwable ex) {
					// nothing
				}
				if (personalDto != null) {
					if (personalDto.getPartnerDto() != null) {
						s5Personalname = personalDto.getPartnerDto()
								.formatName();
					}
					s7Mandantencrn = personalDto.getMandantCNr();
				}
			} else {
				PersonalDto personalDto = null;
				try {
					personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate().personalFindByPrimaryKey(
									Integer.valueOf(
											alInfoForTheClient.get(i)
													.toString()).intValue());
				} catch (Throwable ex) {
					// nothing
				}
				if (personalDto != null) {
					if (personalDto.getPartnerDto() != null) {
						s1Personalname = personalDto.getPartnerDto()
								.formatName();
					}
					s3Mandantcnr = personalDto.getMandantCNr();
				}
			}
		}

		MessageFormat mf = new MessageFormat(
				LPMain
						.getTextRespectUISPr("part.zusammenfuehren.warnung.bereits.vorhanden"));
		try {
			mf.setLocale(LPMain.getTheClient().getLocUi());
		} catch (Throwable ex1) {
		}
		Object pattern[] = { s0Personal, s1Personalname, s2Mandant,
				s3Mandantcnr, s4Personal, s5Personalname, s6Mandant,
				s7Mandantencrn };
		sMsg = mf.format(pattern);
		return sMsg;
	}

	private String handleExceptionBereitsVorhandenBank(
			ArrayList<?> alInfoForTheClient) {
		String sMsg = "";
		String s0Bank = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s1Blz = LPMain.getInstance().getTextRespectUISPr("lp.blz")
				+ ": ";
		String s2Partner = LPMain.getInstance().getTextRespectUISPr(
				"part.partner");
		String s3Partnername = this.partnerQuellDto.formatName();
		;
		String s4Bank = LPMain.getInstance().getTextRespectUISPr(
				alInfoForTheClient.get(0).toString());
		String s5Blz = LPMain.getInstance().getTextRespectUISPr("lp.blz")
				+ ": ";
		String s6Partner = LPMain.getInstance().getTextRespectUISPr(
				"part.partner");
		String s7Partnername = this.partnerZielDto.formatName();
		;
		for (int i = 1; i < alInfoForTheClient.size(); i++) {
			if (i == alInfoForTheClient.size() - 1) {
				// letzter eintrag, kein umbruch
				BankDto bankDto = null;
				try {
					bankDto = DelegateFactory.getInstance()
							.getPartnerbankDelegate().bankFindByPrimaryKey(
									this.partnerZielDto.getIId());
				} catch (Throwable ex) {
					// nothing
				}
				if (bankDto != null) {
					s5Blz = s5Blz + bankDto.getCBlz().trim(); // bank-blz und
					// evtl. noch
					// partnernamen
				}
			} else {
				BankDto bankDto = null;
				try {
					// finde bank zur uebergebenen partnerid
					bankDto = DelegateFactory.getInstance()
							.getPartnerbankDelegate().bankFindByPrimaryKey(
									this.partnerQuellDto.getIId());
				} catch (Throwable ex) {
					// nothing
				}
				if (bankDto != null) {
					s1Blz = s1Blz + bankDto.getCBlz().trim();
				}
			}
		}
		MessageFormat mf = new MessageFormat(
				LPMain
						.getTextRespectUISPr("part.zusammenfuehren.warnung.bereits.vorhanden"));
		try {
			mf.setLocale(LPMain.getTheClient().getLocUi());
		} catch (Throwable ex1) {
		}
		Object pattern[] = { s0Bank, s1Blz, s2Partner, s3Partnername, s4Bank,
				s5Blz, s6Partner, s7Partnername };
		sMsg = mf.format(pattern);
		return sMsg;
	}

	/* wenn bereits ein kunde, lieferant, ... an den zielpartner gebunden ist */
	private void handleExceptionBereitsVorhanden(ExceptionLP exfc) {
		ArrayList<?> alInfoForTheClient = exfc.getAlInfoForTheClient();

		String sMsg = "";

		if (alInfoForTheClient != null && alInfoForTheClient.size() > 0) {
			String sBetrifft = LPMain.getInstance().getTextRespectUISPr(
					"lp.betrifft")
					+ ": "
					+ LPMain.getInstance().getTextRespectUISPr(
							alInfoForTheClient.get(0).toString()) + "\n";
			sMsg = sMsg + sBetrifft;
			if (alInfoForTheClient.get(0).toString().equals("lp.lieferant")) {
				sMsg = sMsg
						+ handleExceptionBereitsVorhandenLieferant(alInfoForTheClient);
			} else if (alInfoForTheClient.get(0).toString().equals("lp.kunde")) {
				sMsg = sMsg
						+ handleExceptionBereitsVorhandenKunde(alInfoForTheClient);
			} else if (alInfoForTheClient.get(0).toString().equals(
					"button.personal.tooltip")) {
				sMsg = sMsg
						+ handleExceptionBereitsVorhandenPersonal(alInfoForTheClient);
			} else if (alInfoForTheClient.get(0).toString().equals("lp.bank")) {
				sMsg = sMsg
						+ handleExceptionBereitsVorhandenBank(alInfoForTheClient);
			} else {
				// unbekannt oder unbehandelt
				new DialogError(LPMain.getInstance().getDesktop(), exfc,
						DialogError.TYPE_ERROR);
			}
		} else {
			sMsg = exfc.getSMsg(); // hier steht vorne ein 'java lang
			// exception:'
			StringTokenizer token = new StringTokenizer(sMsg, ":");
			while (token.hasMoreTokens()) {
				sMsg = token.nextToken();
			}
		}
		DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
				"part.partnerzusammenfuehren"), sMsg);
		// evtl besser: new DialogError(LPMain.getInstance().getDesktop(), exfc,
		// DialogError.TYPE_ERROR);
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		switch (exfc.getICode()) {

		case EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH:
			handleExceptionBereitsVorhanden(exfc);
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		if (!bErrorErkannt) {
			new DialogError(LPMain.getInstance().getDesktop(), exfc,
					DialogError.TYPE_INFORMATION);
		}

		return bErrorErkannt;
	}

}
