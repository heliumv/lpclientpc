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
package com.lp.client.rechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.rechnung.service.GutschrifttextDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel zum Bearbeiten der Rechnungskonditionen</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>22. 11. 2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version 2.0
 */
public class PanelRechnungKonditionen extends PanelKonditionen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneRechnungAll tabbedPaneRechnungAll = null;
	protected WrapperLabel wlaProvision = new WrapperLabel();
	protected WrapperLabel wlaProzent3 = new WrapperLabel();
	protected WrapperTextField wtfProvisionText = new WrapperTextField();
	protected WrapperNumberField wnfProvisionProzent = new WrapperNumberField();
	protected WrapperCheckBox wcbMindermengenzuschlag = new WrapperCheckBox();
	protected WrapperNumberField wnfVersteckterAufschlag = new WrapperNumberField();
	protected WrapperLabel wlaVersteckterAufschlag = new WrapperLabel();
	protected WrapperLabel wlaProzent1 = new WrapperLabel();
	
	protected WrapperLabel wlaZollbeleg1 = new WrapperLabel();
	protected WrapperLabel wlaZollbeleg2 = new WrapperLabel();

	public final static String MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_ZOLLIMPORTPAPIER_ERHALTEN";

	public PanelRechnungKonditionen(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneRechnungAll tabbedPaneRechnungAll) throws Throwable {

		super(internalFrame, add2TitleI, key);
		this.tabbedPaneRechnungAll = tabbedPaneRechnungAll;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {
		RechnungtextDto kopftextDto = DelegateFactory
				.getInstance()
				.getRechnungServiceDelegate()
				.rechnungtextFindByMandantLocaleCNr(
						tabbedPaneRechnungAll.getKundeDto().getPartnerDto()
								.getLocaleCNrKommunikation(),
						MediaFac.MEDIAART_KOPFTEXT);
		RechnungtextDto fusstextDto = DelegateFactory
				.getInstance()
				.getRechnungServiceDelegate()
				.rechnungtextFindByMandantLocaleCNr(
						tabbedPaneRechnungAll.getKundeDto().getPartnerDto()
								.getLocaleCNrKommunikation(),
						MediaFac.MEDIAART_FUSSTEXT);
		if (kopftextDto == null) {
			kopftextDto = DelegateFactory
					.getInstance()
					.getRechnungServiceDelegate()
					.createDefaultRechnungtext(
							MediaFac.MEDIAART_KOPFTEXT,
							RechnungServiceFac.RECHNUNG_DEFAULT_KOPFTEXT,
							tabbedPaneRechnungAll.getKundeDto().getPartnerDto()
									.getLocaleCNrKommunikation());
		}
		if (fusstextDto == null) {
			fusstextDto = DelegateFactory
					.getInstance()
					.getRechnungServiceDelegate()
					.createDefaultRechnungtext(
							MediaFac.MEDIAART_FUSSTEXT,
							RechnungServiceFac.RECHNUNG_DEFAULT_FUSSTEXT,
							tabbedPaneRechnungAll.getKundeDto().getPartnerDto()
									.getLocaleCNrKommunikation());
		}

		wefFusstext.setDefaultText(fusstextDto.getCTextinhalt());
		wefKopftext.setDefaultText(kopftextDto.getCTextinhalt());
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// PJ 0013943
		wtfLieferart.setMandatoryField(true);
		wtfSpedition.setMandatoryField(true);
		wtfZahlungsziel.setMandatoryField(true);
		wlaProvision.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.provision"));
		wcbMindermengenzuschlag.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.mindermengenzuschlag"));
		wlaVersteckterAufschlag.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.versteckteraufschlag"));
		wlaAllgemeinerRabattText.setText(LPMain.getInstance()
				.getTextRespectUISPr("rech.label.allgrabattwarnung"));
		wlaProzent1.setText("%");
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent3.setText("%");
		wlaProzent3.setHorizontalAlignment(SwingConstants.LEFT);
		wnfProvisionProzent.setActivatable(true);
		wtfProvisionText.setActivatable(true);
		wnfProvisionProzent.setMinimumSize(new Dimension(10, Defaults
				.getInstance().getControlHeight()));
		wnfProvisionProzent.setPreferredSize(new Dimension(10, Defaults
				.getInstance().getControlHeight()));
		wnfVersteckterAufschlag.setActivatable(true);
		wnfVersteckterAufschlag.setMaximumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfVersteckterAufschlag.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfVersteckterAufschlag.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaProzent1.setMinimumSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));
		wlaProzent1.setPreferredSize(new Dimension(25, Defaults.getInstance()
				.getControlHeight()));

		jPanelWorkingOn.add(wlaVersteckterAufschlag, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfVersteckterAufschlag, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent1, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaProvision, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfProvisionProzent, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent3, new GridBagConstraints(2, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfProvisionText, new GridBagConstraints(3, iZeile,
				2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaAllgemeinerRabatt, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAllgemeinerRabatt, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent2, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaAllgemeinerRabattText, new GridBagConstraints(3,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wcbMindermengenzuschlag, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaZollbeleg1, new GridBagConstraints(4, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jPanelWorkingOn.add(wlaZollbeleg2, new GridBagConstraints(4, iZeile,
				2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		
		

		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		createAndSaveAndShowButton(
				"/com/lp/client/res/document_preferences.png",
				LPMain.getTextRespectUISPr("rech.zollpapiere.erhalten"),
				MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN, null);
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));
		getToolsPanel().add(new WrapperLabel(""));

	}

	private TabbedPaneRechnungAll getTabbedPaneRechnungAll() {
		return tabbedPaneRechnungAll;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(
				MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN)) {
			// PJ 17696
			if (getTabbedPaneRechnungAll().getRechnungDto() != null
					&& getTabbedPaneRechnungAll().getRechnungDto().getIId() != null) {

				String cPapiere = null;
				if (getTabbedPaneRechnungAll().getRechnungDto()
						.getTZollpapier() == null) {
					cPapiere = (String) JOptionPane
							.showInputDialog(
									getInternalFrame(),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"er.eingangsrechnung.frage.zollimportpapiere"),
									LPMain.getInstance().getTextRespectUISPr(
											"lp.hinweis"),
									JOptionPane.PLAIN_MESSAGE);
					if (cPapiere != null && cPapiere.length() > 40) {
						cPapiere = cPapiere.substring(0, 39);
					}

					if (cPapiere == null) {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.error"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"er.eingangsrechnung.frage.zollimportpapiere.error"));
						return;
					}
					

				}
				DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.toggleZollpapiereErhalten(
								getTabbedPaneRechnungAll().getRechnungDto()
										.getIId(), cPapiere);
				 getTabbedPaneRechnungAll()
					.setRechnungDto(DelegateFactory.getInstance().getRechnungDelegate().rechnungFindByPrimaryKey(getTabbedPaneRechnungAll().getRechnungDto()
							.getIId()));
				
				eventYouAreSelected(false);
			}
		}
		super.eventActionSpecial(e);
	}

	protected void components2Dto() throws Throwable {
		// den bestehenden Dto verwenden
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (rechnungDto != null) {
			rechnungDto.setFVersteckterAufschlag(wnfVersteckterAufschlag
					.getDouble());
			rechnungDto.setFAllgemeinerRabattsatz(wnfAllgemeinerRabatt
					.getDouble());
			rechnungDto.setBMindermengenzuschlag(Helper
					.boolean2Short(wcbMindermengenzuschlag.isSelected()));
			rechnungDto.setBMwstallepositionen(Helper
					.boolean2Short(!isPositionskontierung));

			if (!isPositionskontierung) {
				rechnungDto.setMwstsatzIId((Integer) wcoMehrwertsteuer
						.getKeyOfSelectedItem());
			} else {
				rechnungDto.setMwstsatzIId(null);
			}
			rechnungDto.setNProvision(wnfProvisionProzent.getBigDecimal());
			rechnungDto.setCProvisiontext(wtfProvisionText.getText());
			rechnungDto.setCLieferartort(wtfLieferartort.getText());

			if (zahlungszielDto != null) {
				rechnungDto.setZahlungszielIId(zahlungszielDto.getIId());
			} else {
				rechnungDto.setZahlungszielIId(null);
			}
			if (lieferartDto != null) {
				rechnungDto.setLieferartIId(lieferartDto.getIId());
			} else {
				rechnungDto.setLieferartIId(null);
			}
			if (spediteurDto != null) {
				rechnungDto.setSpediteurIId(spediteurDto.getIId());
			} else {
				rechnungDto.setSpediteurIId(null);
			}
			rechnungDto.setCFusstextuebersteuert(wefFusstext.getText());
			rechnungDto.setCKopftextuebersteuert(wefKopftext.getText());
		}
	}

	protected void dto2Components() throws Throwable {
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (rechnungDto != null) {
			wnfVersteckterAufschlag.setDouble(rechnungDto
					.getFVersteckterAufschlag());
			wnfAllgemeinerRabatt.setDouble(rechnungDto
					.getFAllgemeinerRabattsatz());
			if (rechnungDto.getBMindermengenzuschlag() != null) {
				wcbMindermengenzuschlag.setSelected(Helper
						.short2boolean(rechnungDto.getBMindermengenzuschlag()));
			}
			wtfLieferartort.setText(rechnungDto.getCLieferartort());
			wnfProvisionProzent.setBigDecimal(rechnungDto.getNProvision());
			wtfProvisionText.setText(rechnungDto.getCProvisiontext());
			// Das Zahlungsziel
			holeZahlungsziel(rechnungDto.getZahlungszielIId());
			// Lieferart
			holeLieferart(rechnungDto.getLieferartIId());
			// Spedition
			holeSpediteur(rechnungDto.getSpediteurIId());
			if (rechnungDto.getCFusstextuebersteuert() != null) {
				wefFusstext.setText(rechnungDto.getCFusstextuebersteuert());
			} else {
				wefFusstext.setText(wefFusstext.getDefaultText());
			}
			if (rechnungDto.getCKopftextuebersteuert() != null) {
				wefKopftext.setText(rechnungDto.getCKopftextuebersteuert());
			} else {
				wefKopftext.setText(wefKopftext.getDefaultText());
			}
			this.setStatusbarPersonalIIdAnlegen(rechnungDto
					.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(rechnungDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(rechnungDto
					.getPersonalIIdAendern());
			this.setStatusbarTAendern(rechnungDto.getTAendern());
			this.setStatusbarStatusCNr(rechnungDto.getStatusCNr());
		} else {
			clearStatusbar();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			RechnungDto rechnungDto = getTabbedPaneRechnungAll()
					.getRechnungDto();
			if (rechnungDto != null) {
				rechnungDto = DelegateFactory.getInstance()
						.getRechnungDelegate().updateRechnung(rechnungDto);
				getTabbedPaneRechnungAll().setRechnungDto(rechnungDto);
				// das Panel aktualisieren
				dto2Components();
			}
			super.eventActionSave(e, true);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			RechnungDto rechnungDto = getTabbedPaneRechnungAll()
					.getRechnungDto();
			if (rechnungDto != null) {

				LPButtonAction ba = getHmOfButtons().get(
						MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN);
				ba.getButton().setVisible(false);

				// PJ17912
				if (getTabbedPaneRechnungAll().getKundeDto() != null
						&& getTabbedPaneRechnungAll().getKundeDto()
								.getPartnerIId() != null) {
					String sLaenderart = DelegateFactory
							.getInstance()
							.getFinanzServiceDelegate()
							.getLaenderartZuPartner(
									getTabbedPaneRechnungAll().getKundeDto()
											.getPartnerIId());
					if (sLaenderart != null
							&& sLaenderart
									.equals(FinanzFac.LAENDERART_DRITTLAND)) {
						ba.getButton().setVisible(true);
					}

				}

				
				String text = "";
				String text2 = "";
				
				if (rechnungDto.getTZollpapier() != null) {
					text = LPMain
							.getTextRespectUISPr("rech.zollpapiere.erhalten.persondatum")
							+ " "
							+ Helper.formatDatumZeit(rechnungDto.getTZollpapier(),
									LPMain.getTheClient().getLocUi());
				}
				if (rechnungDto.getPersonalIIdZollpapier() != null) {
					text += "("
							+ DelegateFactory
									.getInstance()
									.getPersonalDelegate()
									.personalFindByPrimaryKey(
											rechnungDto.getPersonalIIdZollpapier())
									.getCKurzzeichen() + ")";
				}
				if (rechnungDto.getCZollpapier() != null) {
					text2=  LPMain
							.getTextRespectUISPr("lp.zollbelegnummer") + " " + rechnungDto.getCZollpapier();
				}
				wlaZollbeleg1.setText(text);
				wlaZollbeleg2.setText(text2);
				
				rechnungDto = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.rechnungFindByPrimaryKey(rechnungDto.getIId());
				getTabbedPaneRechnungAll().setRechnungDto(rechnungDto);
				dto2Components();
				if (getTabbedPaneRechnungAll().getRechnungstyp().equals(
						RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
					GutschrifttextDto gutschrifttextDtoKopftext = DelegateFactory
							.getInstance()
							.getRechnungServiceDelegate()
							.gutschrifttextFindByMandantLocaleCNr(
									tabbedPaneRechnungAll.getKundeDto()
											.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT);
					GutschrifttextDto gutschrifttextDtoFusstext = DelegateFactory
							.getInstance()
							.getRechnungServiceDelegate()
							.gutschrifttextFindByMandantLocaleCNr(
									tabbedPaneRechnungAll.getKundeDto()
											.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT);
					if (gutschrifttextDtoKopftext != null) {
						wefKopftext.setDefaultText(gutschrifttextDtoKopftext
								.getCTextinhalt());
					}
					if (gutschrifttextDtoFusstext != null) {
						wefFusstext.setDefaultText(gutschrifttextDtoFusstext
								.getCTextinhalt());
					}
				} else {
					// Texte in der Sprache des Kunden
					RechnungtextDto rechnungtextDtoKopftext = DelegateFactory
							.getInstance()
							.getRechnungServiceDelegate()
							.rechnungtextFindByMandantLocaleCNr(
									tabbedPaneRechnungAll.getKundeDto()
											.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_KOPFTEXT);
					RechnungtextDto rechnungtextDtoFusstext = DelegateFactory
							.getInstance()
							.getRechnungServiceDelegate()
							.rechnungtextFindByMandantLocaleCNr(
									tabbedPaneRechnungAll.getKundeDto()
											.getPartnerDto()
											.getLocaleCNrKommunikation(),
									MediaFac.MEDIAART_FUSSTEXT);
					if (rechnungtextDtoKopftext != null) {
						wefKopftext.setDefaultText(rechnungtextDtoKopftext
								.getCTextinhalt());
					}
					if (rechnungtextDtoFusstext != null) {
						wefFusstext.setDefaultText(rechnungtextDtoFusstext
								.getCTextinhalt());
					}
					enableComponentsAbhaengigArt();
				}
			}
		}
	}

	private void enableComponentsAbhaengigArt() {
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		if (rechnungDto.getAuftragIId() != null) {
			// Auftrag bezogen
			try {
				AuftragDto aDto = DelegateFactory.getInstance()
						.getAuftragDelegate()
						.auftragFindByPrimaryKey(rechnungDto.getAuftragIId());
				if (aDto.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
					wefKopftext.setEditable(false);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		RechnungDto rechnungDto = getTabbedPaneRechnungAll().getRechnungDto();
		boolean allowed = ((InternalFrameRechnung) getInternalFrame())
				.isUpdateAllowedForRechnungDto(rechnungDto);
		if (allowed) {
			super.eventActionUpdate(aE, !allowed);
		}
		enableComponentsAbhaengigArt();
		// this.eventYouAreSelected(false);
	}
}
