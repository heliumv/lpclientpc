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
package com.lp.client.frame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelDialogPreisvorschlagDto;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Dialog zur Auswahl von Verkaufspreisen.</I>
 * </p>
 * <br>
 * 
 * @todo Anzeige von Artikelbez und Artikelzusatzbez in der Kundensprache. PJ
 *       4948 <br>
 * @todo Preisrecht des Benutzers beruecksichtigen PJ 4948
 *       <p>
 *       Copyright Logistik Pur Software GmbH (c) 2004-2008
 *       </p>
 *       <p>
 *       Erstellungsdatum <I>20.06.2006</I>
 *       </p>
 *       <p>
 *       </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.21 $
 */
@SuppressWarnings("static-access")
public abstract class PanelDialogPreisvorschlag extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Formatierung des Panels
	private WrapperLabel wlaFormat1 = null;
	private WrapperLabel wlaFormat2 = null;
	private WrapperLabel wlaFormat3 = null;
	private WrapperLabel wlaFormat4 = null;
	private WrapperLabel wlaFormat5 = null;
	private WrapperLabel wlaFormat6 = null;
	private WrapperLabel wlaFormat7 = null;

	private WrapperLabel wlaNichtRabattierbar = null;
	private WrapperLabel wlaIdent = null;
	private WrapperTextField wtfIdent = null;
	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperLabel wlaZusatzbezeichnung = null;
	private WrapperTextField wtfZusatzbezeichnung = null;

	private WrapperLabel wlaGestehungspreis = null;
	private WrapperNumberField wnfGestehungspreis = null;
	private WrapperLabel wlaWaehrungGestehungspreis = null;
	private WrapperLabel wlaMinverkaufspreis = null;
	protected WrapperNumberField wnfMinverkaufspreis = null;
	private WrapperLabel wlaWaehrungMinverkaufspreis = null;
	private WrapperLabel wlaVerkaufspreisbasis = null;
	protected WrapperNumberField wnfVerkaufspreisbasis = null;
	private WrapperLabel wlaWaehrungVerkaufspreisbasis = null;
	private WrapperLabel wlaMengeFuer = null;
	private WrapperLabel wlaStaffelmenge = null;

	private WrapperLabel wlaEk1 = null;
	private WrapperNumberField wnfEk1 = null;
	private WrapperLabel wlaWaehrungEk1 = null;
	private WrapperLabel wlaEk2 = null;
	private WrapperNumberField wnfEk2 = null;
	private WrapperLabel wlaWaehrungEk2 = null;
	private WrapperLabel wlaEk3 = null;
	private WrapperNumberField wnfEk3 = null;
	private WrapperLabel wlaWaehrungEk3 = null;

	public boolean bDialogWirdGeradeAngezeigt = false;

	public boolean isbDialogWirdGeradeAngezeigt() {
		return bDialogWirdGeradeAngezeigt;
	}

	public void setbDialogWirdGeradeAngezeigt(boolean bDialogWirdGeradeAngezeigt) {
		this.bDialogWirdGeradeAngezeigt = bDialogWirdGeradeAngezeigt;
	}

	protected ButtonGroup jbgAuswahlliste = null;

	// diese beiden RadioButtons gibt es immer an den unteren beiden Positionen
	// der Liste
	protected WrapperRadioButton wrbHandeingabeFixpreis = null;
	protected WrapperNumberField wnfHandeingabeFixpreis = null;
	protected WrapperLabel wlaHandeingabeFixpreisWaehrung = null;
	protected WrapperRadioButton wrbHandeingabeRabattsatz = null;
	protected WrapperNumberField wnfHandeingabeRabattsatz = null;
	protected WrapperLabel wlaHandeingabeRabattsatzProzent = null;
	protected WrapperRadioButton wrbVerkaufspreisbasis = null;
	protected WrapperNumberField wnfHandeingabeFuerMenge = null;

	protected WrapperLabel wlaMaterialzuschlag = null;
	protected WrapperLabel wlaMaterialzuschlagWaehrung = null;
	protected WrapperNumberField wnfMaterialzuschlag = null;

	protected final static String ACTION_SPECIAL_HANDEINGABE_FIXPREIS = "action_special_handeingabe_fixpreis";
	protected final static String ACTION_SPECIAL_HANDEINGABE_RABATTSATZ = "action_special_handeingabe_rabattsatz";
	protected final static String ACTION_SPECIAL_PREISLISTE_GEWAEHLT = "action_special_preisliste_gewaehlt";
	protected final static String ACTION_SPECIAL_VKPFSTUFE1 = "action_special_vkpfstufe1";
	protected final static String ACTION_SPECIAL_VKPFSTUFE2 = "action_special_vkpfstufe2";
	protected final static String ACTION_SPECIAL_VKPFSTUFE3 = "action_special_vkpfstufe3";
	protected final static String ACTION_SPECIAL_VKPB_GEWAEHLT = "action_special_vkpb_gewaehlt";
	/** Saemtliche Parameter, die den aktuellen Preisvorschlag bestimmten. */
	protected PanelDialogPreisvorschlagDto panelDialogPreisvorschlagDto = null;

	/** Der Gestehungspreis des Artikels. */
	private BigDecimal nGestehungspreisInBelegwaehrung = null;
	/** Der minimale Verkaufspreis fuer diesen Artikel. */
	private BigDecimal nMinverkaufspreisInBelegwaehrung = null;
	/** Die VK-Basis des Artikels. */
	protected BigDecimal nVkbasisInBelegwaehrung = null;

	/** Button fuer die Anzeige einer zweiten VK-Stufe. */
	private WrapperButton wbuZweiteVkstufe = null;
	/** Button fuer die Anzeige einer dritten VK-Stufe. */
	private WrapperButton wbuDritteVkstufe = null;

	private PanelDialogPreisvorschlag pdPreisvorschlagVkstufe1 = null;
	private PanelDialogPreisvorschlag pdPreisvorschlagVkstufe2 = null;
	private PanelDialogPreisvorschlag pdPreisvorschlagVkstufe3 = null;

	protected int iPreiseUINachkommastellen = 2;
	protected int iMengeUINachkommastellen = 2;

	public FocusListener wnfFocusListener = null;

	private boolean bVKPreisbasis_ist_lief1preis = false;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrameI
	 *            wir sitzen in diesem InternalFrame
	 * @param title
	 *            der Titel des Fensters
	 * @param panelDialogPreisvorschlagDtoI
	 *            PanelDialogPreisvorschlagDto
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelDialogPreisvorschlag(InternalFrame internalFrameI,
			String title,
			PanelDialogPreisvorschlagDto panelDialogPreisvorschlagDtoI)
			throws Throwable {
		super(internalFrameI, title);
		bDialogWirdGeradeAngezeigt = true;
		// Mandantenparameter fuer Preis Nachkommastellen
		iPreiseUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseVK();
		// Mandantenparameter fuer Mengen Nachkommastellen
		iMengeUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenMenge();

		panelDialogPreisvorschlagDto = panelDialogPreisvorschlagDtoI;

		jbInit();
		setDefaults();
		initComponents();

	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */

	private void jbInit() throws Throwable {
		// diese Zeile formatiert das Panel
		wlaFormat1 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaFormat1, 120);
		wlaFormat2 = new WrapperLabel();
		wlaFormat3 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaFormat3, 30);
		wlaFormat4 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaFormat4, 120);
		wlaFormat5 = new WrapperLabel();
		wlaFormat6 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaFormat6, 30);
		wlaFormat7 = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaFormat7, 120);

		wlaIdent = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.ident"));
		wtfIdent = new WrapperTextField();
		wtfIdent.setEditable(false);

		wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.setEditable(false);

		wlaZusatzbezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wtfZusatzbezeichnung = new WrapperTextField();
		wtfZusatzbezeichnung.setEditable(false);

		wlaGestehungspreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.gestehungspreis"));
		wnfGestehungspreis = new WrapperNumberField();
		wnfGestehungspreis.setEditable(false);
		wnfGestehungspreis.setFractionDigits(iPreiseUINachkommastellen);

		wlaWaehrungGestehungspreis = new WrapperLabel();
		wlaWaehrungGestehungspreis
				.setHorizontalAlignment(SwingConstants.LEADING);

		wlaMinverkaufspreis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.minverkaufspreis"));
		wnfMinverkaufspreis = new WrapperNumberField();
		wnfMinverkaufspreis.setEditable(false);
		wnfMinverkaufspreis.setFractionDigits(iPreiseUINachkommastellen);

		wlaWaehrungMinverkaufspreis = new WrapperLabel();
		wlaWaehrungMinverkaufspreis
				.setHorizontalAlignment(SwingConstants.LEADING);

		wlaVerkaufspreisbasis = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("vkpf.verkaufspreisbasis"));
		wnfVerkaufspreisbasis = new WrapperNumberField();
		wnfVerkaufspreisbasis.setEditable(false);
		wnfVerkaufspreisbasis.setFractionDigits(iPreiseUINachkommastellen);

		wlaWaehrungVerkaufspreisbasis = new WrapperLabel();
		wlaWaehrungVerkaufspreisbasis
				.setHorizontalAlignment(SwingConstants.LEADING);

		if (panelDialogPreisvorschlagDto.getNMenge() != null) {
			wlaMengeFuer = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("vkpf.preisfuer")
					+ " "
					+ panelDialogPreisvorschlagDto.getNMenge());
		} else {
			wlaMengeFuer = new WrapperLabel();
		}
		wlaMengeFuer.setHorizontalAlignment(SwingConstants.CENTER);

		if (panelDialogPreisvorschlagDto.getNMenge() != null) {
			wlaStaffelmenge = new WrapperLabel(LPMain.getInstance()
					.getTextRespectUISPr("vkpf.preisfuer"));
		}
		wlaStaffelmenge.setHorizontalAlignment(SwingConstants.CENTER);

		wlaEk1 = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"vkpf.ek1"));
		wnfEk1 = new WrapperNumberField();
		wnfEk1.setEditable(false);
		wnfEk1.setFractionDigits(iPreiseUINachkommastellen);

		wlaWaehrungEk1 = new WrapperLabel();
		wlaWaehrungEk1.setHorizontalAlignment(SwingConstants.LEADING);

		wlaEk2 = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"vkpf.ek2"));
		wnfEk2 = new WrapperNumberField();
		wnfEk2.setEditable(false);
		wnfEk2.setFractionDigits(iPreiseUINachkommastellen);

		wlaWaehrungEk2 = new WrapperLabel();
		wlaWaehrungEk2.setHorizontalAlignment(SwingConstants.LEADING);

		wlaEk3 = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"vkpf.ek3"));
		wnfEk3 = new WrapperNumberField();
		wnfEk3.setEditable(false);
		wnfEk3.setFractionDigits(iPreiseUINachkommastellen);

		wlaWaehrungEk3 = new WrapperLabel();
		wlaWaehrungEk3.setHorizontalAlignment(SwingConstants.LEADING);

		wbuZweiteVkstufe = new WrapperButton();
		wbuZweiteVkstufe.addActionListener(this);
		initZweiteVkstufe();

		wbuDritteVkstufe = new WrapperButton();
		wbuDritteVkstufe.addActionListener(this);
		initDritteVkstufe();

		wlaNichtRabattierbar = new WrapperLabel();
		wlaNichtRabattierbar.setForeground(Color.RED);

		String sAnzeige = LPMain.getInstance().getTextRespectUISPr(
				"vkpf.verkaufspreisbasis");
		wrbVerkaufspreisbasis = new WrapperRadioButton(true, sAnzeige);
		wrbVerkaufspreisbasis.setActionCommand(ACTION_SPECIAL_VKPB_GEWAEHLT);
		wrbVerkaufspreisbasis.addActionListener(this);

		// Zeile
		jpaWorkingOn.add(wlaFormat1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormat2, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormat3, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormat4, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormat5, new GridBagConstraints(4, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormat6, new GridBagConstraints(5, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFormat7, new GridBagConstraints(6, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaIdent, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfIdent, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaNichtRabattierbar, new GridBagConstraints(2,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 4,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaZusatzbezeichnung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wtfZusatzbezeichnung, new GridBagConstraints(1,
				iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaGestehungspreis, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGestehungspreis, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungGestehungspreis, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEk1, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfEk1, new GridBagConstraints(4, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungEk1, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaMinverkaufspreis, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMinverkaufspreis, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungMinverkaufspreis, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEk2, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfEk2, new GridBagConstraints(4, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungEk2, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		ParametermandantDto p = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
		bVKPreisbasis_ist_lief1preis = false;
		if (p != null && p.getCWert().equals("1"))
			bVKPreisbasis_ist_lief1preis = true;

		if (bVKPreisbasis_ist_lief1preis) {
			jpaWorkingOn.add(wlaStaffelmenge, new GridBagConstraints(1, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(12, 2, 2, 2), 0, 0));
		} else {
			jpaWorkingOn.add(wrbVerkaufspreisbasis, new GridBagConstraints(0,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
			jpaWorkingOn.add(wnfVerkaufspreisbasis, new GridBagConstraints(1,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
			jpaWorkingOn.add(wlaWaehrungVerkaufspreisbasis,
					new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 10, 2), 0, 0));
		}
		jpaWorkingOn.add(wlaEk3, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wnfEk3, new GridBagConstraints(4, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungEk3, new GridBagConstraints(5, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
		if (!bVKPreisbasis_ist_lief1preis) {
			jpaWorkingOn.add(wlaMengeFuer, new GridBagConstraints(6, iZeile, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(12, 2, 2, 2), 0, 0));
		}

		if (bVKPreisbasis_ist_lief1preis) {
			// Zeile
			iZeile++;
			jpaWorkingOn.add(wrbVerkaufspreisbasis, new GridBagConstraints(0,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
			jpaWorkingOn.add(wnfVerkaufspreisbasis, new GridBagConstraints(1,
					iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
			jpaWorkingOn.add(wlaWaehrungVerkaufspreisbasis,
					new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 10, 2), 0, 0));
			jpaWorkingOn.add(wlaMengeFuer, new GridBagConstraints(6, iZeile, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 10, 2), 0, 0));
		}

		wnfFocusListener = this;
	}

	private void setDefaults() throws Throwable {
		// saemtliche Waehrungsfelder bekommen die Waehrung des Belegs
		wlaWaehrungGestehungspreis.setText(panelDialogPreisvorschlagDto
				.getCNrWaehrung());
		wlaWaehrungMinverkaufspreis.setText(panelDialogPreisvorschlagDto
				.getCNrWaehrung());
		wlaWaehrungVerkaufspreisbasis.setText(panelDialogPreisvorschlagDto
				.getCNrWaehrung());

		wlaWaehrungEk1.setText(panelDialogPreisvorschlagDto.getCNrWaehrung());
		wlaWaehrungEk2.setText(panelDialogPreisvorschlagDto.getCNrWaehrung());
		wlaWaehrungEk3.setText(panelDialogPreisvorschlagDto.getCNrWaehrung());

		// allgemeine Artikeleigenschaften anzeigen
		wtfIdent.setText(panelDialogPreisvorschlagDto.getArtikelDto().getCNr());

		if (Helper.short2Boolean(panelDialogPreisvorschlagDto.getArtikelDto()
				.getBRabattierbar())) {
			wlaNichtRabattierbar.setText("");
		} else {
			wlaNichtRabattierbar.setText(LPMain.getInstance()
					.getTextRespectUISPr("artikel.nichtrabattierbar"));
		}

		if (panelDialogPreisvorschlagDto.getArtikelDto().getArtikelsprDto() != null) {
			wtfBezeichnung.setText(panelDialogPreisvorschlagDto.getArtikelDto()
					.getArtikelsprDto().getCBez());
			wtfZusatzbezeichnung.setText(panelDialogPreisvorschlagDto
					.getArtikelDto().getArtikelsprDto().getCZbez());
		}

		// den Gestehungspreis (in Mandantenwaehrung) des Artikels in
		// Belegwaehrung anzeigen
		BigDecimal nGestehungspreis = DelegateFactory
				.getInstance()
				.getLagerDelegate()
				.getGemittelterGestehungspreisEinesLagers(
						panelDialogPreisvorschlagDto.getArtikelDto().getIId(),
						panelDialogPreisvorschlagDto.getLagerIId());

		nGestehungspreisInBelegwaehrung = nGestehungspreis
				.multiply(new BigDecimal(panelDialogPreisvorschlagDto
						.getDdWechselkurs().doubleValue()));

		wnfGestehungspreis.setBigDecimal(nGestehungspreisInBelegwaehrung);

		// den minimalen Verkaufspreis (in Mandantenwaehrung) des Artikels
		// anzeigen
		nMinverkaufspreisInBelegwaehrung = new BigDecimal(0);

		try {
			BigDecimal nMinverkaufspreis = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getMindestverkaufspreis(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getIId(),
							panelDialogPreisvorschlagDto.getLagerIId(),
							panelDialogPreisvorschlagDto.getNMenge());

			nMinverkaufspreisInBelegwaehrung = nMinverkaufspreis
					.multiply(new BigDecimal(panelDialogPreisvorschlagDto
							.getDdWechselkurs().doubleValue()));
		} catch (Throwable t) {
			// wenn der minVerkaufspreis nicht gefunden wird, z.B. der Artikel
			// liegt nicht am Hauptlager -> 0 anzeigen
		}

		wnfMinverkaufspreis.setBigDecimal(nMinverkaufspreisInBelegwaehrung);

		// die Verkaufspreisbasis des Artikels anzeigen

		try {
			// PJ 15000: richtige Preisbasis verwenden
			nVkbasisInBelegwaehrung = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.ermittlePreisbasis(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getIId(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							panelDialogPreisvorschlagDto
									.getIIdPreislisteZuletztGewaehlt(),
							panelDialogPreisvorschlagDto.getNMenge(),
							panelDialogPreisvorschlagDto.getCNrWaehrung());

		} catch (Throwable t) {
			// wenn kein Einzelverkaufspreis hinterlegt ist, der Wert 0
			// verwenden

		}

		if (nVkbasisInBelegwaehrung == null) {
			nVkbasisInBelegwaehrung = new BigDecimal(0);
		}
		wnfVerkaufspreisbasis.setBigDecimal(nVkbasisInBelegwaehrung);

		BigDecimal nStaffelmenge = null;
		if (bVKPreisbasis_ist_lief1preis) {
			nStaffelmenge = DelegateFactory
					.getInstance()
					.getVkPreisfindungDelegate()
					.ermittlePreisbasisStaffelmenge(
							panelDialogPreisvorschlagDto.getArtikelDto()
									.getIId(),
							panelDialogPreisvorschlagDto
									.getDatGueltigkeitsdatumFuerPreise(),
							panelDialogPreisvorschlagDto.getNMenge());
		}

		if (nStaffelmenge != null) {
			wlaStaffelmenge.setText(LPMain.getInstance().getTextRespectUISPr(
					"vkpf.preisbasisfuer")
					+ " "
					+ Helper.formatZahl(nStaffelmenge,
							iMengeUINachkommastellen, LPMain.getTheClient()
									.getLocUi()));
		}

		// die Artikellieferanten des Artikels feststellen und ihre Preise
		// anzeigen;
		// die Preise des Artikellieferanten sind in Lieferantenwaehrung
		// hinterlegt check CK
		ArtikellieferantDto[] aArtikellieferantDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikellieferantFindByArtikelIId(
						panelDialogPreisvorschlagDto.getArtikelDto().getIId());

		// die Einkaufspreise 1 - 3 anzeigen, wenn moeglich mit den Daten des
		// Artikellieferanten
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case 0:

				// EK 1
				BigDecimal nEinkaufspreis1 = new BigDecimal(0);

				if (aArtikellieferantDto != null
						&& aArtikellieferantDto.length > 0) {
					LieferantDto lieferantDto = DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(
									aArtikellieferantDto[0].getLieferantIId());

					if (aArtikellieferantDto[0].getLief1Preis() != null) {
						nEinkaufspreis1 = DelegateFactory
								.getInstance()
								.getLocaleDelegate()
								.rechneUmInAndereWaehrung(
										aArtikellieferantDto[0].getLief1Preis(),
										lieferantDto.getWaehrungCNr(),
										panelDialogPreisvorschlagDto
												.getCNrWaehrung());
					}

					wlaEk1.setText(lieferantDto.getPartnerDto()
							.getCName1nachnamefirmazeile1());
				}

				wnfEk1.setBigDecimal(nEinkaufspreis1);
				break;

			case 1:

				// EK 2
				BigDecimal nEinkaufspreis2 = new BigDecimal(0);

				if (aArtikellieferantDto != null
						&& aArtikellieferantDto.length > 1) {
					LieferantDto lieferantDto = DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(
									aArtikellieferantDto[1].getLieferantIId());

					if (aArtikellieferantDto[1].getLief1Preis() != null) {
						nEinkaufspreis2 = DelegateFactory
								.getInstance()
								.getLocaleDelegate()
								.rechneUmInAndereWaehrung(
										aArtikellieferantDto[1].getLief1Preis(),
										lieferantDto.getWaehrungCNr(),
										panelDialogPreisvorschlagDto
												.getCNrWaehrung());
					}

					wlaEk2.setText(lieferantDto.getPartnerDto()
							.getCName1nachnamefirmazeile1());
				}

				wnfEk2.setBigDecimal(nEinkaufspreis2);
				break;

			case 2:

				// EK 3
				BigDecimal nEinkaufspreis3 = new BigDecimal(0);

				if (aArtikellieferantDto != null
						&& aArtikellieferantDto.length > 2) {
					LieferantDto lieferantDto = DelegateFactory
							.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(
									aArtikellieferantDto[2].getLieferantIId());

					if (aArtikellieferantDto[2].getLief1Preis() != null) {
						nEinkaufspreis3 = DelegateFactory
								.getInstance()
								.getLocaleDelegate()
								.rechneUmInAndereWaehrung(
										aArtikellieferantDto[2].getLief1Preis(),
										lieferantDto.getWaehrungCNr(),
										panelDialogPreisvorschlagDto
												.getCNrWaehrung());
					}

					wlaEk3.setText(lieferantDto.getPartnerDto()
							.getCName1nachnamefirmazeile1());
				}

				wnfEk3.setBigDecimal(nEinkaufspreis3);
				break;
			}
		}

		jbgAuswahlliste = new ButtonGroup();
		jbgAuswahlliste.add(wrbVerkaufspreisbasis);

		// RadioButton fuer Handeingabe Fixpreis
		String sAnzeige = LPMain.getInstance().getTextRespectUISPr(
				"vkpf.handeingabefixpreis");
		wrbHandeingabeFixpreis = new WrapperRadioButton(true, sAnzeige);
		wrbHandeingabeFixpreis
				.setActionCommand(ACTION_SPECIAL_HANDEINGABE_FIXPREIS);
		wrbHandeingabeFixpreis.addActionListener(this);
		jbgAuswahlliste.add(wrbHandeingabeFixpreis);

		wnfHandeingabeFixpreis = new WrapperNumberField();
		wnfHandeingabeFixpreis.setEditable(false);
		wnfHandeingabeFixpreis.setFractionDigits(iPreiseUINachkommastellen);
		wlaHandeingabeFixpreisWaehrung = new WrapperLabel(
				panelDialogPreisvorschlagDto.getCNrWaehrung());
		wlaHandeingabeFixpreisWaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);

		// RadioButton fuer Handeingabe Rabattsatz
		sAnzeige = LPMain.getInstance().getTextRespectUISPr(
				"vkpf.handeingaberabattsatz");
		wrbHandeingabeRabattsatz = new WrapperRadioButton(true, sAnzeige);
		wrbHandeingabeRabattsatz
				.setActionCommand(ACTION_SPECIAL_HANDEINGABE_RABATTSATZ);
		wrbHandeingabeRabattsatz.addActionListener(this);
		jbgAuswahlliste.add(wrbHandeingabeRabattsatz);

		wnfHandeingabeRabattsatz = new WrapperNumberField();
		wnfHandeingabeRabattsatz.setEditable(false);
		wlaHandeingabeRabattsatzProzent = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.prozent"));
		wlaHandeingabeRabattsatzProzent
				.setHorizontalAlignment(SwingConstants.LEADING);

		wnfHandeingabeFuerMenge = new WrapperNumberField();
		wnfHandeingabeFuerMenge.setEditable(false);
		wnfHandeingabeFuerMenge.setFractionDigits(iPreiseUINachkommastellen);

		// Materialzuschlag
		
			sAnzeige = LPMain.getInstance().getTextRespectUISPr(
					"lp.materialzuschlag");
			wlaMaterialzuschlag = new WrapperLabel(sAnzeige);

			wnfMaterialzuschlag = new WrapperNumberField();
			wnfMaterialzuschlag.setEditable(false);
			wnfMaterialzuschlag.setFractionDigits(iPreiseUINachkommastellen);
			wlaMaterialzuschlagWaehrung = new WrapperLabel(
					panelDialogPreisvorschlagDto.getCNrWaehrung());
			wlaMaterialzuschlagWaehrung
					.setHorizontalAlignment(SwingConstants.LEADING);
		

	}

	protected void eventActionAlt(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ALTF)) {
			wrbHandeingabeFixpreis.setSelected(true);
			wnfHandeingabeFixpreis.setEditable(true);
			wnfHandeingabeFixpreis.setMandatoryField(true);
			// wnfHandeingabeRabattsatz.setText("");
			wnfHandeingabeRabattsatz.setEditable(false);
			wnfHandeingabeRabattsatz.setMandatoryField(false);
		}
		if (e.getActionCommand().equals(ALTR)) {
			wrbHandeingabeRabattsatz.setSelected(true);
			wnfHandeingabeRabattsatz.setEditable(true);
			wnfHandeingabeRabattsatz.setMandatoryField(true);
			// wnfHandeingabeRabattsatz.setText("");
			wnfHandeingabeFixpreis.setEditable(false);
			wnfHandeingabeFixpreis.setMandatoryField(false);
		}
		if (e.getActionCommand().equals(ALTB)) {
			wrbVerkaufspreisbasis.setSelected(true);
			wnfHandeingabeRabattsatz.setEditable(false);
			wnfHandeingabeRabattsatz.setMandatoryField(false);
			// wnfHandeingabeRabattsatz.setText("");
			wnfHandeingabeFixpreis.setEditable(false);
			wnfHandeingabeFixpreis.setMandatoryField(false);
		} else if (e.getActionCommand().equals(ALT1)
				|| e.getActionCommand().equals(ALT2)
				|| e.getActionCommand().equals(ALT3)
				|| e.getActionCommand().equals(ALT4)
				|| e.getActionCommand().equals(ALT5)
				|| e.getActionCommand().equals(ALT6)
				|| e.getActionCommand().equals(ALT7)
				|| e.getActionCommand().equals(ALT8)
				|| e.getActionCommand().equals(ALT9)) {
			wnfHandeingabeFixpreis.setEditable(false);
			wnfHandeingabeRabattsatz.setEditable(false);
			wnfHandeingabeRabattsatz.setMandatoryField(false);
			wnfHandeingabeFixpreis.setMandatoryField(false);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			bDialogWirdGeradeAngezeigt = false;
			if (wrbHandeingabeFixpreis.isSelected()
					&& (wnfHandeingabeFixpreis.getText() == null || wnfHandeingabeFixpreis
							.getText().length() == 0)) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("vkpf.fixpreiseingeben"));
			} else if (wrbHandeingabeRabattsatz.isSelected()
					&& (wnfHandeingabeRabattsatz.getText() == null || wnfHandeingabeRabattsatz
							.getText().length() == 0)) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
						.getTextRespectUISPr("vkpf.rabattsatzeingeben"));
			} else {
				// das Eingabe des Benutzers hinterlegen
				buildMeinErgebnis();

				// den aktuellen Verkaufspreis bestimmen, wenn eine Preisliste
				// gewaehlt wurde
				BigDecimal bdAktuellerVerkaufspreis = panelDialogPreisvorschlagDto
						.getAktuellerVerkaufspreisDto().nettopreis;

				// auf Unterpreisigkeit pruefen
				if (bdAktuellerVerkaufspreis != null) {

					// auf Unterpreisigkeit pruefen
					boolean bIstUnterpreisig = DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.liegtVerkaufpreisUnterMinVerkaufspreis(
									panelDialogPreisvorschlagDto
											.getArtikelDto().getIId(),
									bdAktuellerVerkaufspreis,
									panelDialogPreisvorschlagDto
											.getDdWechselkurs(),
									panelDialogPreisvorschlagDto.getNMenge());
					/**
					 * @todo MB->?? das gehoert unbedingt auf den server!!!
					 */
					// Spezialfall: Wenn Mandantenwaehrung und Belegwaehrung
					// voneinander abweichen,
					// kann es sein, dass ein Unterschied in den
					// Nachkommastellen aufgrund der Rundung
					// auf vier Stellen nicht erkannt wird
					if (panelDialogPreisvorschlagDto.getDdWechselkurs()
							.doubleValue() != 1 && !bIstUnterpreisig) {
						// beim Preisvergleich muss sichergestellt sein, dass
						// beide Zahlen dieselbe
						// Anzahl von Nachkommastellen besitzen
						nMinverkaufspreisInBelegwaehrung = Helper
								.rundeKaufmaennisch(
										nMinverkaufspreisInBelegwaehrung, 4);
						bdAktuellerVerkaufspreis = Helper.rundeKaufmaennisch(
								bdAktuellerVerkaufspreis, 4);

						if (bdAktuellerVerkaufspreis
								.compareTo(nMinverkaufspreisInBelegwaehrung) == -1) {
							bIstUnterpreisig = true;
						}
					}

					if (bIstUnterpreisig) {
						if (DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("lp.warning.unterpreisigkeit"),
										LPMain.getTextRespectUISPr("lp.warning"),
										JOptionPane.WARNING_MESSAGE,
										JOptionPane.NO_OPTION)) {
							bIstUnterpreisig = false;
						}
					}

					if (!bIstUnterpreisig) {
						// den Dialog verlassen und die Kriterien uebernehmen
						super.eventActionSpecial(e);
					}
				} else {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							"$Bitte w\u00E4hlen Sie eine Preisliste.$");
				}
			}
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			bDialogWirdGeradeAngezeigt = false;
			super.eventActionSpecial(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_HANDEINGABE_FIXPREIS)) {
			if (this.wrbHandeingabeFixpreis.isSelected()) {
				wnfHandeingabeFixpreis.setEditable(true);
				wnfHandeingabeFixpreis.setMandatoryField(true);
				// wnfHandeingabeRabattsatz.setText("");
				wnfHandeingabeRabattsatz.setEditable(false);
				wnfHandeingabeRabattsatz.setMandatoryField(false);
				UpdateFixpreisFuerMenge();
			}
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_HANDEINGABE_RABATTSATZ)) {
			if (wrbHandeingabeRabattsatz.isSelected()) {
				wnfHandeingabeRabattsatz.setEditable(true);
				wnfHandeingabeRabattsatz.setMandatoryField(true);
				// wnfHandeingabeFixpreis.setText("");
				wnfHandeingabeFixpreis.setEditable(false);
				wnfHandeingabeFixpreis.setMandatoryField(false);
				UpdateFixpreisFuerMenge();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VKPB_GEWAEHLT)) {
			if (wrbVerkaufspreisbasis.isSelected()) {
				wnfHandeingabeRabattsatz.setEditable(false);
				wnfHandeingabeRabattsatz.setMandatoryField(false);
				// wnfHandeingabeFixpreis.setText("");
				wnfHandeingabeFixpreis.setEditable(false);
				wnfHandeingabeFixpreis.setMandatoryField(false);
			}
		}

		// alle anderen RadioButtons
		else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_PREISLISTE_GEWAEHLT)) {
			// wnfHandeingabeFixpreis.setText("");
			wnfHandeingabeFixpreis.setEditable(false);
			// wnfHandeingabeRabattsatz.setText("");
			wnfHandeingabeRabattsatz.setEditable(false);
			wnfHandeingabeRabattsatz.setMandatoryField(false);
			wnfHandeingabeFixpreis.setMandatoryField(false);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VKPFSTUFE1)) {
			panelDialogPreisvorschlagDto.setBEditable(false);

			pdPreisvorschlagVkstufe1 = new PanelDialogPreisvorschlagPreisliste(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"lp.preisvorschlag.kundepreisliste"),
					panelDialogPreisvorschlagDto);

			getInternalFrame().showPanelDialog(pdPreisvorschlagVkstufe1);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VKPFSTUFE2)) {
			panelDialogPreisvorschlagDto.setBEditable(false);

			pdPreisvorschlagVkstufe2 = new PanelDialogPreisvorschlagVkmengenstaffel(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"lp.preisvorschlag.vkmengenstaffel"),
					panelDialogPreisvorschlagDto);

			getInternalFrame().showPanelDialog(pdPreisvorschlagVkstufe2);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VKPFSTUFE3)) {
			panelDialogPreisvorschlagDto.setBEditable(false);

			pdPreisvorschlagVkstufe3 = new PanelDialogPreisvorschlagKundesokomengenstaffel(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.preisvorschlag.kundesokomengenstaffel"),
					panelDialogPreisvorschlagDto);

			getInternalFrame().showPanelDialog(pdPreisvorschlagVkstufe3);
		}
	}

	private void UpdateFixpreisFuerMenge() throws Throwable {
		if (wnfHandeingabeFixpreis.getBigDecimal() != null)
			wnfHandeingabeFuerMenge.setBigDecimal(wnfHandeingabeFixpreis
					.getBigDecimal().multiply(
							panelDialogPreisvorschlagDto.getNMenge()));
	}

	public void focusLost(FocusEvent e) {
		super.focusLost(e);
		try {
			UpdateFixpreisFuerMenge();
		} catch (Throwable e1) {
			//
		}
	}

	public int getMnemonicRBArry(int iNummer) {
		switch (iNummer) {
		case 1:
			return KeyEvent.VK_1;
		case 2:
			return KeyEvent.VK_2;
		case 3:
			return KeyEvent.VK_3;
		case 4:
			return KeyEvent.VK_4;
		case 5:
			return KeyEvent.VK_5;
		case 6:
			return KeyEvent.VK_6;
		case 7:
			return KeyEvent.VK_7;
		case 8:
			return KeyEvent.VK_8;
		case 9:
			return KeyEvent.VK_9;
		default:
			return 0;
		}
	}

	// Abstrakte Methoden
	// ========================================================

	/**
	 * Hier wird das Ergebnis zusammengebaut, das von aussen abgeholt werden
	 * kann.
	 * 
	 * @throws Throwable
	 */
	protected abstract void buildMeinErgebnis() throws Throwable;

	/**
	 * Wenn der beabsichtigte Preis unter dem Min-VKP liegt, wird er in rot
	 * angezeigt.
	 * 
	 * @param index
	 *            Index der Zeile, die farbig markiert werden soll
	 * @throws Throwable
	 */
	protected abstract void setColorBerechneterPreis(int index)
			throws Throwable;

	/**
	 * Der Button fuer die Anzeige einer zweiten VK-Stufe muss initialisert
	 * werden.
	 * 
	 * @throws Throwable
	 */
	protected abstract void initZweiteVkstufe() throws Throwable;

	/**
	 * Der Button fuer die Anzeige einer dritten VK-Stufe muss initialisert
	 * werden.
	 * 
	 * @throws Throwable
	 */
	protected abstract void initDritteVkstufe() throws Throwable;

	// Getter, Setter
	// ============================================================

	public VerkaufspreisDto getVerkaufspreisDto() {
		return panelDialogPreisvorschlagDto.getAktuellerVerkaufspreisDto();
	}

	public Integer getIIdPreislisteGewaehlt() {
		return panelDialogPreisvorschlagDto.getIIdPreislisteZuletztGewaehlt();
	}

	public Integer getIIdKundesokomengenstaffelZuletztGewaehlt() {
		return panelDialogPreisvorschlagDto
				.getIIdKundesokomengenstaffelZuletztGewaehlt();
	}

	public Integer getIIdVkmengenstaffelZuletztGewaehlt() {
		return panelDialogPreisvorschlagDto
				.getIIdVkmengenstaffelZuletztGewaehlt();
	}

	public WrapperButton getWbuZweiteStufe() {
		return wbuZweiteVkstufe;
	}

	public WrapperButton getWbuDritteStufe() {
		return wbuDritteVkstufe;
	}

	public BigDecimal getNVkbasisInBelegwaehrung() {
		return nVkbasisInBelegwaehrung;
	}
}
