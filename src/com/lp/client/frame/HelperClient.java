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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.layout.ConstraintListener;

import com.lp.client.frame.component.CompoundIcon;
import com.lp.client.frame.component.IDirektHilfe;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.ReportViewer;
import com.lp.client.pc.LPMain;
import com.lp.client.util.ClientConfiguration;
import com.lp.client.util.dtable.DistributedTableModel;
import com.lp.client.util.logger.LpLogger;
import com.lp.editor.LpEditor;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.DoppelIcon;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diverse Helper fuer den Client.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004/2005
 * </p>
 * 
 * <p>
 * Organisation:
 * </p>
 * 
 * verantwortlich: Martin Bluehweis
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.62 $
 */
@SuppressWarnings("static-access")
public class HelperClient {
	// stdcrud: 2 Alle XML-Dateinamen fuer das StammdatenCRUD.
	public static final String SCRUD_BELEGART_FILE = "belegart.xml";
	public static final String SCRUD_LIEFERART_FILE = "lieferart.xml";
	public static final String SCRUD_EINHEIT_FILE = "einheit.xml";
	public static final String SCRUD_BESTELLUNGART_FILE = "bestellungart.xml";
	public static final String SCRUD_BESTELLUNGSTATUS_FILE = "bestellungstatus.xml";
	public static final String SCRUD_STATUS_FILE = "status.xml";
	public static final String SCRUD_LIEFERSCHEINTEXT_FILE = "lieferscheintext.xml";
	public static final String SCRUD_LIEFERSCHEINART_FILE = "lieferscheinart.xml";
	public static final String SCRUD_FUNKTION_FILE = "funktion.xml";
	public static final String SCRUD_POSITIONSART_FILE = "positionsart.xml";
	public static final String SCRUD_EINGANGSRECHNUNGSART_FILE = "eingangsrechnungart.xml";
	public static final String SCRUD_EINGANGSRECHNUNGSSTATUS_FILE = "eingangsrechnungsstatus.xml";
	public static final String SCRUD_RECHNUNGSSTATUS_FILE = "rechnungstatus.xml";
	public static final String SCRUD_RECHNUNGSART_FILE = "rechnungart.xml";
	public static final String SCRUD_ANSPRECHPARTNERFKT_FILE = "ansprechpartnerfkt.xml";
	public static final String SCRUD_SPEDITEUR_FILE = "spediteur.xml";
	public static final String SCRUD_RECHNUNGPOSITIONSART_FILE = "rechnungpositionsart.xml";
	public static final String SCRUD_GUTSCHRIFTPOSITIONSART_FILE = "gutschriftpositionsart.xml";
	public static final String SCRUD_PROFORMARECHNUNGPOSITIONSART_FILE = "proformarechnungpositionsart.xml";
	public static final String SCRUD_RECHNUNGTYP_FILE = "rechnungtyp.xml";
	public static final String SCRUD_ZAHLUNGSART_FILE = "zahlungsart.xml";
	public static final String SCRUD_ZAHLUNGSZIEL_FILE = "zahlungsziel.xml";
	public static final String SCRUD_MWSTSATZ_FILE = "mwstsatz.xml";
	public static final String SCRUD_MWSTSATZBEZ_FILE = "mwstsatzbez.xml";
	public static final String SCRUD_MEDIAART_FILE = "mediaart.xml";
	public static final String SCRUD_AUFTRAGTEXT_FILE = "auftragtext.xml";
	public static final String SCRUD_AUFTRAGART_FILE = "auftragart.xml";
	public static final String SCRUD_MEILENSTEIN_FILE = "meilenstein.xml";
	public static final String SCRUD_AUFTRAGSERIENNUMMER_FILE = "auftragseriennummer.xml";
	public static final String SCRUD_RECHNUNGTEXT_FILE = "rechnungtext.xml";
	public static final String SCRUD_GUTSCHRIFTTEXT_FILE = "gutschrifttext.xml";
	public static final String SCRUD_GUTSCHRIFTGRUND_FILE = "gutschriftgrund.xml";
	public static final String SCRUD_MAHNTEXT_FILE = "mahntext.xml";
	public static final String SCRUD_KOSTENSTELLE_FILE = "kostenstelle.xml";
	public static final String SCRUD_PARTNERBRANCHE_FILE = "partnerbranche.xml";
	public static final String SCRUD_PARTNERANREDE_FILE = "partneranrede.xml";
	public static final String SCRUD_PARTNERKLASSE_FILE = "partnerklasse.xml";
	public static final String SCRUD_PARTNERART_FILE = "partnerart.xml";
	public static final String SCRUD_KOMMUNIKATIONSART_FILE = "kommunikationsart.xml";
	public static final String SCRUD_BESTELLUNGTEXT_FILE = "bestellungtext.xml";
	public static final String SCRUD_ANFRAGETEXT_FILE = "anfragetext.xml";
	public static final String SCRUD_ANGEBOTTEXT_FILE = "angebottext.xml";
	public static final String SCRUD_ANGEBOTART_FILE = "angebotart.xml";
	public static final String SCRUD_ANGEBOTERLEDIGUNGSGRUND_FILE = "angeboterledigungsgrund.xml";
	public static final String SCRUD_KONTOART_FILE = "kontoart.xml";
	public static final String SCRUD_UVAART_FILE = "uvaart.xml";
	public static final String SCRUD_LAENDERART_FILE = "laenderart.xml";
	public static final String SCRUD_LOSKLASSE_FILE = "losklasse.xml";
	public static final String SCRUD_LOSBEREICH_FILE = "losbereich.xml";
	public static final String SCRUD_LOSSTATUS_FILE = "losstatus.xml";
	public static final String SCRUD_MAHNSTUFE_FILE = "mahnstufe.xml";
	public static final String SCRUD_BSMAHNSTUFE_FILE = "bsmahnstufe.xml";
	public static final String SCRUD_BSMAHNTEXT_FILE = "bsmahntext.xml";
	public static final String SCRUD_ANGEBOTPOSITIONART_FILE = "angebotpositionart.xml";
	public static final String SCRUD_ANFRAGEPOSITIONART_FILE = "anfragepositionart.xml";
	public static final String SCRUD_LIEFERSCHEINPOSITIONART_FILE = "lieferscheinpositionart.xml";
	public static final String SCRUD_AUFTRAGPOSITIONART_FILE = "auftragpositionart.xml";
	public static final String SCRUD_LIEFERGRUPPEN_FILE = "liefergruppen.xml";
	public static final String SCRUD_ANFRAGEART_FILE = "anfrageart.xml";
	public static final String SCRUD_BESTELLPOSITIONART_FILE = "bestellpositionart.xml";
	public static final String SCRUD_SELEKTION_FILE = "selektion.xml";
	public static final String SCRUD_KATEGORIE_FILE = "kategorie.xml";
	public static final String SCRUD_TYP_FILE = "projekttyp.xml";
	public static final String SCRUD_KONTAKTART_FILE = "projektkontaktart.xml";
	public static final String SCRUD_PROJEKTSTATUS_FILE = "projektstatus.xml";

	// lockme
	public static final String LOCKME_SACHBEARBEITER = "lockme_sachbearbeiter";
	public static final String LOCKME_KUNDE = "lockme_kunde";
	public static final String LOCKME_PARTNER = "lockme_partner";
	public static final String LOCKME_HANDLAGERBEWEGUNG = "lockme_handlagerbewegung";
	public static final String LOCKME_AUFTRAG = "lockme_auftrag";
	public static final String LOCKME_AUFTRAGPOSITION = "lockme_auftragposition";
	public static final String LOCKME_AUFTRAGTEILNEHMER = "lockme_auftragteilnehmer";
	public static final String LOCKME_AUFTRAGART = "lockme_auftragart";
	public static final String LOCKME_MEILENSTEIN = "lockme_meilenstein";
	public static final String LOCKME_AUFTRAGSERIENNUMMERN = "lockme_auftragseriennummern";
	public static final String LOCKME_LIEFERSCHEIN = "lockme_lieferschein";
	public static final String LOCKME_LIEFERSCHEINPOSITION = "lockme_lieferscheinposition";
	public static final String LOCKME_RECHNUNG = RechnungFac.LOCKME_RECHNUNG;
	public static final String LOCKME_EINGANGSRECHNUNG = EingangsrechnungFac.LOCKME_EINGANGSRECHNUNG;
	public static final String LOCKME_FINANZ_KONTO = "lockme_finanz_konto";
	public static final String LOCKME_FINANZ_KASSENBUCH = "lockme_finanz_kassenbuch";
	public static final String LOCKME_FINANZ_MAHNLAUF = "lockme_finanz_mahnlauf";
	public static final String LOCKME_FINANZ_BANKVERBINDUNG = "lockme_finanz_bankverbindung";
	public static final String LOCKME_FINANZ_ERGEBNISGRUPPE = "lockme_finanz_ergebnisgruppe";
	public static final String LOCKME_FINANZ_FINANZAMT = "lockme_finanz_finanzamt";
	public static final String LOCKME_FINANZ_BUCHUNGDETAIL = "lockme_finanz_buchungdetail";
	public static final String LOCKME_FINANZ_BUCHUNGSREGEL = "lockme_finanz_buchungsregel";
	public static final String LOCKME_VKPF_PREISLISTENNAME = "lockme_vkpf_preislistenname";
	public static final String LOCKME_HERSTELLER = "lockme_hersteller";
	public static final String LOCKME_ARTIKELGRUPPE = "lockme_artikelgruppe";
	public static final String LOCKME_LAGER = "lockme_lager";
	public static final String LOCKME_ARTIKELKLASSE = "lockme_artikelklasse";
	public static final String LOCKME_MATERIAL = "lockme_material";
	public static final String LOCKME_SHOPGRUPPE = "lockme_shopgruppe";
	public static final String LOCKME_ARTIKEL = "lockme_artikel";
	public static final String LOCKME_THEMA = "lockme_thema";
	public static final String LOCKME_THEMAROLLE = "lockme_themarolle";
	public static final String LOCKME_ARTIKELLIEFERANT = "lockme_artikellieferant";
	public static final String LOCKME_KATALOG = "lockme_katalog";
	public static final String LOCKME_SYSTEM = "lockme_system";
	public static final String LOCKME_ORT = "lockme_ort";
	public static final String LOCKME_LAND = "lockme_land";
	public static final String LOCKME_LANDPLZORT = "lockme_landplzort";
	public static final String LOCKME_BESTELLUNG = "lockme_bestellung";
	public static final String LOCKME_BESTELLVORSCHLAG = BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG;
	public static final String LOCKME_PERSONAL = "lockme_personal";
	public static final String LOCKME_RELIGION = "lockme_religion";
	public static final String LOCKME_LOHNGRUPPE = "lockme_lohngruppe";
	public static final String LOCKME_PENDLERPAUSCHALE = "lockme_pendlerpauschale";
	public static final String LOCKME_BERUF = "lockme_beruf";
	public static final String LOCKME_KOLLEKTIV = "lockme_kollektiv";
	public static final String LOCKME_FAHRZEUG = "lockme_fahrzeug";
	public static final String LOCKME_LIEFERANT = "lockme_lieferant";
	public static final String LOCKME_ZEITMODELL = "lockme_zeitmodell";
	public static final String LOCKME_MANDANT = "lockme_mandant";
	public static final String LOCKME_BETRIEBSKALENDER = "lockme_betriebskalender";
	public static final String LOCKME_ZEITDATEN = "lockme_zeitdaten";
	public static final String LOCKME_BELEGART = "lockme_belegart";
	public static final String LOCKME_TAETIGKEITEN = "lockme_taetigkeiten";
	public static final String LOCKME_BANK = "lockme_bank";
	public static final String LOCKME_SYSTEMROLLE = "lockme_systemrolle";
	public static final String LOCKME_BENUTZER = "lockme_benutzer";
	public static final String LOCKME_NACHRICHTART = "lockme_nachrichtart";
	public static final String LOCKME_AUFTRAGDOKUMENT = "lockme_auftragdokumant";
	public static final String LOCKME_BENUTZERMANDANT = "lockme_benutzermandant";
	public static final String LOCKME_MEDIASTANDARD = "lockme_mediastandard";
	public static final String LOCKME_LIEFERART = "lockme_lieferart";
	public static final String LOCKME_BESTELLUNGART = "lockme_bestellungart";
	public static final String LOCKME_BESTELLUNGSTATUS = "lockme_bestellungstatus";
	public static final String LOCKME_STATUS = "lockme_status";
	public static final String LOCKME_FUNKTION = "lockme_funktion";
	public static final String LOCKME_POSITIONSART = "lockme_positionsart";
	public static final String LOCKME_LIEFERSCHEINTEXT = "lockme_lieferscheintext";
	public static final String LOCKME_EINHEIT = "lockme_einheit";
	public static final String LOCKME_EINHEITKONVERTIERUNG = "lockme_einheitkonvertierung";
	public static final String LOCKME_EINGANGSRECHNUNGSART = "lockme_eingangsrechnungart";
	public static final String LOCKME_EINGANGSRECHNUNGSSTATUS = "lockme_eingangsrechnungsstatus";
	public static final String LOCKME_RECHNUNGSSTATUS = "lockme_rechnungsstatus";
	public static final String LOCKME_RECHNUNGSART = "lockme_rechnungsart";
	public static final String LOCKME_ANSPRECHPARTNERFKT = "lockme_ansprechpartnerfkt";
	public static final String LOCKME_SPEDITEUR = "lockme_spediteur";
	public static final String LOCKME_RECHNUNGPOSITIONSART = "lockme_rechnungpositionsart";
	public static final String LOCKME_GUTSCHRIFTPOSITIONSART = "lockme_gutschriftpositionsart";
	public static final String LOCKME_PROFORMARECHNUNGPOSITIONSART = "lockme_proformarechnungpositionsart";
	public static final String LOCKME_RECHNUNGTYP = "lockme_rechnungtyp";
	public static final String LOCKME_ZAHLUNGSART = "lockme_zahlungsart";
	public static final String LOCKME_ZAHLUNGSZIEL = "lockme_zahlungsziel";
	public static final String LOCKME_MWSTSATZ = "lockme_mwstsatz";
	public static final String LOCKME_MWSTSATZBEZ = "lockme_mwstsatzbez";
	public static final String LOCKME_MEDIAART = "lockme_mediaart";
	public static final String LOCKME_AUFTRAGTEXT = "lockme_auftragtext";
	public static final String LOCKME_RECHNUNGTEXT = "lockme_rechnungtext";
	public static final String LOCKME_GUTSCHRIFTGRUND = "lockme_gutschriftgrund";
	public static final String LOCKME_MAHNTEXT = "lockme_mahntext";
	public static final String LOCKME_KOSTENSTELLE = "lockme_kostenstelle";
	public static final String LOCKME_PARTNERBRANCHE = "lockme_partnerbranche";
	public static final String LOCKME_PARTNERANREDE = "lockme_partneranrede";
	public static final String LOCKME_PARTNERKLASSE = "lockme_partnerklasse";
	public static final String LOCKME_PARTNERART = "lockme_partnerart";
	public static final String LOCKME_KOMMUNIKATIONSART = "lockme_kommunikationsart";
	public static final String LOCKME_BESTELLUNGTEXT = "lockme_bestellungtext";
	public static final String LOCKME_PARAMETERANWENDER = "lockme_parameteranwender";
	public static final String LOCKME_PARAMETERMANDANT = "lockme_parametermandant";
	public static final String LOCKME_ANFRAGE = "lockme_anfrage";
	public static final String LOCKME_ANFRAGETEXT = "lockme_anfragetext";
	public static final String LOCKME_WAEHRUNG = "lockme_waehrung";
	public static final String LOCKME_LOCKME = "lockme_lockme";
	public static final String LOCKME_ANGEBOT = "lockme_angebot";
	public static final String LOCKME_ANGEBOTEXT = "lockme_angebottext";
	public static final String LOCKME_ANGEBOTART = "lockme_angebotart";
	public static final String LOCKME_ANGEBOTERLEDIGUNGSGRUND = "lockme_angeboterledigungsgrund";
	public static final String LOCKME_INVENTUR = "lockme_inventur";
	public static final String LOCKME_KONTOART = "lockme_kontoart";
	public static final String LOCKME_UVAART = "lockme_uvaart";
	public static final String LOCKME_LAENDERART = "lockme_laenderart";
	public static final String LOCKME_LOSKLASSE = "lockme_losklasse";
	public static final String LOCKME_LOSBEREICH = "lockme_losbereich";
	public static final String LOCKME_LOSSTATUS = "lockme_losstatus";
	public static final String LOCKME_LIEFERSCHEINART = "lockme_lieferscheinart";
	public static final String LOCKME_TAGESART = "lockme_tagesart";
	public static final String LOCKME_SONDERZEITEN = "lockme_sonderzeiten";
	public static final String LOCKME_MONTAGEART = "lockme_montageart";
	public static final String LOCKME_FERTIGUNGSGRUPPE = "lockme_fertigungsgruppet";
	public static final String LOCKME_AUFSCHLAG = "lockme_aufschlag";
	public static final String LOCKME_KOMMENTARIMPORT = "lockme_kommentarimport";
	public static final String LOCKME_STUECKLISTEEIGENSCHAFTART = "lockme_stuecklisteeigenschaftart";
	public static final String LOCKME_BEREICH = "lockme_bereich";
	public static final String LOCKME_STUECKLISTE = "lockme_stueckliste";
	public static final String LOCKME_STUECKLISTEPOSITION = "lockme_stuecklisteposition";
	public static final String LOCKME_VERSANDAUFTRAG = "lockme_versandauftrag";
	public static final String LOCKME_MAHNSTUFE = "lockme_mahnstufe";
	public static final String LOCKME_ANGEBOTPOSITIONART = "lockme_angebotpositionart";
	public static final String LOCKME_ANFRAGEPOSITIONART = "lockme_anfragepositionart";
	public static final String LOCKME_LIEFERSCHEINPOSITIONART = "lockme_lieferscheinpositionart";
	public static final String LOCKME_AUFTRAGPOSITIONART = "lockme_auftragpositionart";
	public static final String LOCKME_LIEFERGRUPPEN = "lockme_liefergruppen";
	public static final String LOCKME_KONTOLAENDERART = "lockme_kontolaenderart";
	public static final String LOCKME_ANFRAGEART = "lockme_anfrageart";
	public static final String LOCKME_LOS = "lockme_los";
	public static final String LOCKME_INTERNEBESTELLUNG = "lockme_internebestellung";
	public static final String LOCKME_ZULAGE = "lockme_zulage";
	public static final String LOCKME_LOHNARTSTUNDENFAKTOR = "lockme_lohnartstundenfaktor";
	public static final String LOCKME_ARTIKELZULAGE = "lockme_artikelzulage";
	public static final String LOCKME_BESTELLPOSITIONART = "lockme_bestellpositionart";
	public static final String LOCKME_BESTELLUNG_MAHNTEXT = "lockme_bestellung_mahntext";
	public static final String LOCKME_BESTELLUNG_MAHNSTUFE = "lockme_bestellung_mahnstufe";
	public static final String LOCKME_SELEKTION = "lockme_selektion";
	public static final String LOCKME_SERIENBRIEF = "lockme_serienbrief";
	public static final String LOCKME_PANELDATEN = "lockme_paneldaten";
	public static final String LOCKME_PANELBESCHREIBUNG = "lockme_panelbeschreibung";
	public static final String LOCKME_AGSTKL = "lockme_agstkl";
	public static final String LOCKME_THECLIENT = "lockme_theclient";
	public static final String LOCKME_VKPFMENGENSTAFFEL = "lockme_vkpfmengenstaffel";
	public static final String LOCKME_KUNDESOKO = "lockme_kundesoko";
	public static final String LOCKME_KUNDESOKOMENGENSTAFFEL = "lockme_kundesokomengenstaffel";
	public static final String LOCKME_ZEITSTIFT = "lockme_zeitstift";
	public static final String LOCKME_MASCHINE = "lockme_maschine";
	public static final String LOCKME_PROJEKT = "lockme_projekt";
	public static final String LOCKME_ARTIKELFEHLMENGE = "lockme_artikelfehlmenge";
	public static final String LOCKME_ZUTRITTSMODELL = "lockme_zutrittsmodell";
	public static final String LOCKME_ZUTRITTSMODELLTAG = "lockme_zutrittsmodelltag";
	public static final String LOCKME_ZUTRITTSMODELLTAGDETAIL = "lockme_zutrittsmodelltagdetail";
	public static final String LOCKME_ZUTRITTSCONTROLLER = "lockme_zutrittscontroller";
	public static final String LOCKME_ZUTRITTSOBJEKT = "lockme_zutrittsobjekt";
	public static final String LOCKME_ZUTRITTSOBJEKTVERWENDUNG = "lockme_zutrittsobjektverwendung";
	public static final String LOCKME_ZUTRITTSKLASSEOBJEKT = "lockme_zutrittsklasseobjekt";
	public static final String LOCKME_PERSONALZUTRITTSKLASSE = "lockme_personalzutrittsklasse";
	public static final String LOCKME_ZUTRITTSKLASSE = "lockme_zutrittsklasse";
	public static final String LOCKME_FARBCODE = "lockme_farbcode";
	public static final String LOCKME_REPORTVARIANTE = "lockme_reportvariante";
	public static final String LOCKME_VORSCHLAGSTEXT = "lockme_vorschlagstext";
	public static final String LOCKME_HISTORYART = "lockme_historyart";
	public static final String LOCKME_BEGRUENUNG = "lockme_begruendung";
	public static final String LOCKME_AUFTRAGBEGRUENDUNG = "lockme_auftragbegruendung";
	public static final String LOCKME_MASCHINENGRUPPE = "lockme_maschinengruppe";
	public static final String LOCKME_PERSONALGRUPPE = "lockme_personalgruppe";
	public static final String LOCKME_ZAHLUNGSVORSCHLAG = "lockme_zahlungsvorschlag";
	public static final String LOCKME_REISE = "lockme_reise";
	public static final String LOCKME_FLRGOTO = "lockme_flrgoto";
	public static final String LOCKME_EXTRALISTE = "lockme_extraliste";
	public static final String LOCKME_TELEFONZEITEN = "lockme_telefonzeiten";
	public static final String LOCKME_DIAETEN = "lockme_diaeten";
	public static final String LOCKME_SPERREN = "lockme_sperren";
	public static final String LOCKME_AUTOMATIK = "lockme_automatik";
	public static final String LOCKME_AUTOMATIKTIMER = "lockme_automatiktimer";
	public static final String LOCKME_WIEDERHOLENDELOSE = "lockme_wiederholendelose";
	public static final String LOCKME_MASSNAHME = "lockme_massnahme";
	public static final String LOCKME_FEHLER = "lockme_fehler";
	public static final String LOCKME_FEHLERANGABE = "lockme_fehlerangabe";
	public static final String LOCKME_AUFNAHMEART = "lockme_aufnahmeart";
	public static final String LOCKME_REKLAMATION = "lockme_reklamation";
	public static final String LOCKME_EINKAUFSANGEBOT = "lockme_einkaufsangebot";
	public static final String LOCKME_DOKUMENTBELEGART = "lockme_dokumentbelegart";
	public static final String LOCKME_DOKUMENTGRUPPE = "lockme_dokumentgruppe";
	public static final String LOCKME_WIRKSAMKEIT = "lockme_wirksamkeit";
	public static final String LOCKME_SPEISEPLAN = "lockme_speiseplan";
	public static final String LOCKME_KONTAKTART = "lockme_kontaktart";
	public static final String LOCKME_KUECHEUMRECHNUNG = "lockme_kuecheumrechnung";
	public static final String LOCKME_STEUERKATEGORIE = "lockme_steuerkategorie";
	public static final String LOCKME_INSTANDHALTUNG = "lockme_instandhaltung";
	public static final String LOCKME_HALLE = "lockme_halle";
	public static final String LOCKME_WEBSHOP = "lockme_webshop";
	public static final String LOCKME_GERAETETHISTORIE = "lockme_geraetehistorie";
	public static final String LOCKME_INSERAT = "lockme_inserat";
	public static final String LOCKME_BEREITSCHAFTART = "lockme_bereitschaftart";
	public static final String LOCKME_LOSSOLLMATERIAL = "lockme_lossollmaterial";

	public static final String LOCKME_ZUSATZSTATUS = "lockme_zusatzstatus";
	public static final String LOCKME_SCHWERE = "lockme_schwere";
	public static final String LOCKME_LAGERPLATZ = "lockme_lagerplatz";
	public static final String LOCKME_KASSAARTIKEL = "lockme_kassaartikel";
	public static final String LOCKME_BEDIENERLAGER = "lockme_bedienerlager";
	public static final String LOCKME_ZERTIFIKATART = "lockme_zertifikatart";
	public static final String LOCKME_NACHRICHTARCHIV = "lockme_nachrichtarchiv";
	public static final String LOCKME_VERLEIH = "lockme_verleih";
	public static final String LOCKME_PROJEKTERLEDIGUNGSGRUND = "lockme_projekterledigungsgrund";
	public static final String LOCKME_FEIERTAG = "lockme_feiertag";
	public static final String LOCKME_REACH = "lockme_reach";
	public static final String LOCKME_ROHS = "lockme_rohs";
	public static final String LOCKME_AUTOMOTIVE = "lockme_automotive";
	public static final String LOCKME_MEDICAL = "lockme_medical";
	public static final String LOCKME_MEDIAINBOX = "lockme_mediainbox" ;
	public static final String LOCKME_MEDIASTOREBELEG = "lockme_mediastorebeleg" ;
	public static final String LOCKME_MASCHINENZEITMODELL = "lockme_maschinenzeitmodell";
	public static final String LOCKME_VORZUG = "lockme_vorzug";
	public static final String LOCKME_ALLERGEN = "lockme_alergen";
	public static final String LOCKME_ANFRAGEERLEDIGUNGSGRUND = "lockme_anfrageerledigungsgrund";
	public static final String LOCKME_PARTNERKOMMENTARART = "lockme_partnerkommentarart";
	
	
	// Strings fuer File-Association
	public static final String FILE_ASSOC_OPEN = "open";
	public static final String FILE_ASSOC_PRINT = "print";
	public static final String FILE_ASSOC_PRINT_TO = "printto";

	// Strings for Component-Names
	// Notwendig fuer Testumgebung
	public static final String COMP_NAME_MENUBAR = "menubar";
	public static final String COMP_PRAEFIX_MENU = "menu_";
	public static final String COMP_PRAEFIX_MENUITEM = "menuItem_";
	public static final String COMP_PRAEFIX_TOOLBARBUTTON = "toolbarButton_";

	public static final String COMP_NAME_MENU_PROGRAM = COMP_PRAEFIX_MENU
			+ "Program";
	public static final String COMP_NAME_MENU_WARENWIRTSCHAFT = COMP_PRAEFIX_MENU
			+ "Warenwirtschaft";
	public static final String COMP_NAME_MENU_EINKAUF = COMP_PRAEFIX_MENU
			+ "Einkauf";
	public static final String COMP_NAME_MENU_FERTIGUNG = COMP_PRAEFIX_MENU
			+ "Fertigung";
	public static final String COMP_NAME_MENU_VERKAUF = COMP_PRAEFIX_MENU
			+ "Verkauf";
	public static final String COMP_NAME_MENU_MANAGEMENT = COMP_PRAEFIX_MENU
			+ "Management";
	public static final String COMP_NAME_MENU_EXTRAS = COMP_PRAEFIX_MENU
			+ "Extras";
	public static final String COMP_NAME_MENU_ANSICHT = COMP_PRAEFIX_MENU
			+ "Ansicht";
	public static final String COMP_NAME_MENU_HILFE = COMP_PRAEFIX_MENU
			+ "Hilfe";
	public static final String COMP_NAME_MENU_EMAIL = COMP_PRAEFIX_MENU
			+ "Email";
	
	protected static LpLogger myLogger = (LpLogger) LpLogger
			.getLogger("HelperClient");
	private static ConstraintListener constraintListener;
	private static Color colorOnFocus;

	private static final String DOUBLE_RIGHT_ARROWS = "\u00BB";
	private static final String DOUBLE_RIGHT_ARROWS_SPACED = " \u00BB ";

	public static final String COLOR_NORMAL = "normal";
	public static final String COLOR_RED_GREEN_BLINDNESS = "redgreenblindness";

	private HelperClient() {
		// only static helperfunctions
	}

	/**
	 * Mache einen Button.
	 * 
	 * @param icon
	 *            Icon
	 * @param tooltip
	 *            String
	 * @param ac
	 *            String
	 * @return JButton
	 */
	static public JButton createButton(Icon icon, String tooltip, String ac) {

		JButton button = new JButton();

		button.setToolTipText(tooltip);
		button.setActionCommand(ac);
		button.setIcon(icon);
		button.setEnabled(false);

		return button;
	}

	static public WrapperButton createWrapperButton(Icon icon, String tooltip,
			String ac) {

		WrapperButton button = new WrapperButton();

		button.setToolTipText(tooltip);
		button.setActionCommand(ac);
		button.setIcon(icon);
		button.setEnabled(false);

		return button;
	}

	/**
	 * Umsetzung der LP5 GUI - Design - Richtlinien.
	 * 
	 * @param c
	 *            JComponent
	 */
	public static void setDefaultsToComponent(JComponent c) {
		
		// Defaults fuer alle Komponenten
		Dimension minimumDimension = new Dimension(10, Defaults.getInstance()
				.getControlHeight());
		Dimension maximumDimension = new Dimension(10000, Defaults
				.getInstance().getControlHeight());
		c.setMinimumSize(minimumDimension);
		c.setMaximumSize(maximumDimension);
		c.setPreferredSize(minimumDimension);
	}
	
	/**
	 * Erm&ouml;glicht das &Auml;ndern der MigLayout Constraints zur Laufzeit.
	 * Ist der Benutzer als LPAdmin angemeldet, erh&auml;lt man mit einem Rechtsklick
	 * auf die <code>JComponent c</code> einen Eingabedialog.
	 * @param c 
	 */
	public static void addConstraintListener(Container c) {
		if(!LPMain.getInstance().isLPAdmin()) return;
		if(!Defaults.getInstance().isbDebugGUI()) return;
		// damit wir ihn nicht mehrfach adden
		c.removeMouseListener(getConstraintListener());
		c.addMouseListener(getConstraintListener());
		
		for(Component child : c.getComponents()) {
			if(child instanceof Container)
			addConstraintListener((Container)child);
		}
	}
	
	private static ConstraintListener getConstraintListener() {
		if(constraintListener == null) {
			constraintListener = new ConstraintListener();
		}
		return constraintListener;
	}

	/**
	 * Einer Component die Default Hoehe und eine fixe Breite geben.
	 * 
	 * @param c
	 *            JComponent
	 * @param iBreite
	 *            die fixe Breite
	 */
	public static void setDefaultsToComponent(JComponent c, int iBreite) {
		// Defaults fuer alle Komponenten
		Dimension dimension = new Dimension(Defaults.getInstance()
				.bySizeFactor(iBreite), Defaults.getInstance()
				.getControlHeight());

		c.setMinimumSize(dimension);
		c.setMaximumSize(dimension);
		c.setPreferredSize(dimension);
	}

	public static void updateComponentHeight(JComponent c) {
		for (Component child : c.getComponents()) {
			if (child instanceof JComponent) {
				updateComponentHeight((JComponent) child);
				child.setMinimumSize(child.getMinimumSize());
				child.setPreferredSize(child.getPreferredSize());
				child.setMaximumSize(child.getMaximumSize());
			}
		}
	}

	/**
	 * Liefert die Farbe des Rahmens fuer Pflichtfelder.<br>
	 * Der Parameter <code>colorVision</code> legt fest, ob eine Farbe f&uuml;r
	 * Menschen mit eingeschr&auml;nkter Farbwahrnehmung gew&auml;hlt werden
	 * soll.
	 * 
	 * @param colorVision
	 *            zB.: HelperClient.COLOR_RED_GREEN_BLINDNESS,
	 *            HelperClient.COLOR_NORMAL
	 * @return Color
	 */
	public static Color getMandatoryFieldBorderColor(String colorVision) {
		return ClientConfiguration.getMandatoryFieldBorderColor(colorVision) ;
	}

	/**
	 * Liefert die Hintergrundfarbe fuer abhaengige Felder.
	 * 
	 * @return Color
	 */
	public static Color getDependenceFieldBackgroundColor() {
		/**
		 * @todo MB aus lp.properties PJ 4689
		 */
		return new Color(220, 220, 255); // ein nicht so helles blau

	}

	/**
	 * Liefert die Hintergrundfarbe fuer abhaengige Felder in Zustand disabled.
	 * 
	 * @return Color
	 */
	public static Color getDependenceFieldBackgroundColorDisabled() {
		/**
		 * @todo MB aus lp.properties PJ 4689
		 */
		return new Color(230, 230, 255); // ein helles blau
	}

	/**
	 * Liefert die Hintergrundfarbe fuer nicht editierbare Felder.
	 * 
	 * @return Color
	 */
	public static Color getNotEditableColor() {
		return ClientConfiguration.getNotEditableColor() ;
	}

	/**
	 * Liefert die Hintergrundfarbe fuer selektiere Felder - speziell beim
	 * Zusammenfuehren.
	 * 
	 * @return Color
	 */
	public static Color getSelectedBgColor() {
		// Color color = new Color(100, 150, 210); // mittel bis dunkelblau
		Color color = new Color(180, 195, 255); // helles blau
		return color;
	}

	/**
	 * Liefert die Hintergrundfarbe nicht editierbare Felder.
	 * 
	 * @return Color
	 */
	public static Color getEditableColor() {
		return ClientConfiguration.getEditableColor() ;
	}
	
	public static Color stringToColor(String s) {
		int iColor = Integer.parseInt(s);
		return new Color(iColor);
	}
	
	public static Color getColorOnFocus() {
		if(colorOnFocus == null) {
			colorOnFocus = ClientConfiguration.getColorOnFocus() ;
		}
		return colorOnFocus;
	}

	public static Dimension getInternalFrameSize() {
		int iSizeX = ClientConfiguration.getInternalFrameSizeWidth() ;
		int iSizeY = ClientConfiguration.getInternalFrameSizeHeight();

		return new java.awt.Dimension(Defaults.getInstance().bySizeFactor(
				iSizeX), Defaults.getInstance().bySizeFactor(iSizeY));
	}

	public static void memberVariablenEinerKlasseBenennen(Component component)
			throws Throwable {
		// nur dann, wenn der Abbot auch laeuft

		Field[] fields = component.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String sName = fields[i].getName();
			try {
				// Alle final bzw. static konstanten ignorieren
				if (!(Modifier.isFinal(fields[i].getModifiers()) && Modifier
						.isStatic(fields[i].getModifiers()))) {
					// Methode setName(String name) laden
					Method method = fields[i].getDeclaringClass().getMethod(
							"setName", new Class[] { String.class });
					boolean bIsAccessible = fields[i].isAccessible();
					if (!bIsAccessible) {
						// Zugriff sichern
						fields[i].setAccessible(true);
					}
					// Objekt laden
					Object oComponent = fields[i].get(component);
					if (oComponent instanceof java.awt.Component) {
						// Warnung fuer Felder, die nicht private oder
						// protected sind
						if (!Modifier.isPrivate(fields[i].getModifiers())
								&& !Modifier.isProtected(fields[i]
										.getModifiers())) {
							myLogger.warn(sName + " in "
									+ component.getClass().getName()
									+ " is not private or protected");
						}

						method.invoke(oComponent, new Object[] { sName });
					} else if (oComponent instanceof WrapperIdentField) {
						((WrapperIdentField) oComponent)
								.setComponentNames(fields[i].getName());
					}
					// alte Verfuegbarkeit wiederherstellen
					fields[i].setAccessible(bIsAccessible);
				}
			} catch (NoSuchMethodException ex) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER, ex);
			}
		}

	}

	public static Color getHintergrundColor() {
		return new Color(230, 230, 230); // @todo aus lp.properties PJ 4689
	}

	public static Color getInvalidDataColor() {
		return new Color(255, 0, 0); // @todo aus lp.properties PJ 4689
	}

	/**
	 * Einen Renderer fuer Date holen.
	 * 
	 * @return DateRenderer
	 */
	public static DateRenderer getDateRenderer() {
		return new DateRenderer();
	}

	public static DoubleFloatRenderer getDoubleFloatRenderer() {
		return new DoubleFloatRenderer();
	}

	public static BigDecimalRenderer getBigDecimalRenderer() {
		return new BigDecimalRenderer();
	}

	public static BigDecimal6Renderer getBigDecimal6Renderer() {
		return new BigDecimal6Renderer();
	}

	public static BigDecimal3Renderer getBigDecimal3Renderer() {
		return new BigDecimal3Renderer();
	}

	public static BigDecimal4Renderer getBigDecimal4Renderer() {
		return new BigDecimal4Renderer();
	}

	public static BigDecimalFinanzRenderer getBigDecimalFinanzRenderer() {
		return new BigDecimalFinanzRenderer();
	}

	/**
	 * Einen Renderer fuer Number holen.
	 * 
	 * @return NumberRenderer
	 */
	public static NumberRenderer getNumberRenderer() {
		return new NumberRenderer();
	}

	/**
	 * Einen Renderer fuer einen Betrag vom Typ BigDecimal holen.
	 * 
	 * @return BigDecimalRenderer
	 */
	public static BetragRenderer getBetragRenderer() {
		return new BetragRenderer();
	}

	/**
	 * Einen Renderer fuer Timestamp holen.
	 * 
	 * @return TimestampRenderer
	 */
	public static TimestampRenderer getTimestampRenderer() {
		return new TimestampRenderer();
	}

	/**
	 * Einen Renderer fuer Short holen.
	 * 
	 * @return ShortRenderer
	 */
	public static ShortRenderer getShortRenderer() {
		return new ShortRenderer();
	}

	/**
	 * Einen Renderer fuer Labels holen.
	 * 
	 * @return TimestampRenderer
	 */
	public static LabelRenderer getLabelRenderer() {
		return new LabelRenderer();
	}

	public static ImageIconRenderer getImageIconRenderer() {
		return new ImageIconRenderer();
	}

	public static StatusIconRenderer getStatusIconRenderer() {
		return new StatusIconRenderer();
	}

	public static SperrenIconRenderer getSperrenIconRenderer() {
		return new SperrenIconRenderer();
	}

	public static ImageIconHeaderRenderer getImageIconHeaderRenderer() {
		return new ImageIconHeaderRenderer();
	}

	/**
	 * Renderer-Klasse fuer Boolean.
	 * @return new Instance of {@link BooleanRenderer}
	 */
	public static BooleanRenderer getBooleanRenderer() {
		return new BooleanRenderer();
	}

	/**
	 * Renderer-Klasse fuer Date.
	 */
	static class DateRenderer extends LPDefaultTableCellRenderer {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;

			if (value != null) {
				if (value instanceof java.sql.Date) {
					try {
						sShow = Helper.formatDatum((java.sql.Date) value,
								LPMain.getInstance().getTheClient().getLocUi());
					} catch (Throwable t) {
						sShow = t.getMessage();
					}
				} else if (value instanceof java.util.Date) {
					try {
						sShow = Helper.formatDatum((java.util.Date) value,
								LPMain.getInstance().getTheClient().getLocUi());
					} catch (Throwable t) {
						sShow = t.getMessage();
					}
				}
			}

			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle links ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.LEFT;
		}
	}

	/**
	 * Renderer-Klasse fuer Short.
	 */
	static class ShortRenderer extends LPDefaultTableCellRenderer {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			super.setValue(Helper.short2Boolean((Short) value));
			// Boolean bbShow = null;
			//
			// if (value != null) {
			// bbShow = new Boolean(Helper.short2boolean((Short) value));
			// }

			// setValue((bbShow == null) ? null : bbShow); // jetzt wird der
			// DefaultRenderer fuer
			// Boolean aufgerufen
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.CENTER;
		}
	}

	/**
	 * Renderer-Klasse fuer BigDecimal. <br>
	 * Die Anzahl der Nachkommastellen wurde bereits bestimmt.
	 */
	static class BigDecimalRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;
			if ((value != null) && (!value.getClass().equals(BigDecimal.class))) {
				// System.out.println("Wrong cast");
				// do nothing
			} else {
				if (value != null && ((BigDecimal) value).signum() != 0) {
					sShow = Helper.formatZahl(
							Helper.rundeKaufmaennisch((BigDecimal) value, 2),
							2, Defaults.getInstance().getLocUI());
				}

				setText((sShow == null) ? "" : sShow);
			}
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	/**
	 * Renderer-Klasse fuer BigDecimal mit 6 Nachkommastellen.
	 */
	static class BigDecimal6Renderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;

			if (value != null && ((BigDecimal) value).doubleValue() != 0) {
				sShow = Helper.formatZahl((BigDecimal) value, 6, Defaults
						.getInstance().getLocUI());
			}

			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	/**
	 * Renderer-Klasse fuer BigDecimal mit 3 Nachkommastellen.
	 */
	static class BigDecimal3Renderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;
			if (value != null && ((BigDecimal) value).doubleValue() != 0) {
				sShow = Helper.formatZahl((BigDecimal) value, 3, Defaults
						.getInstance().getLocUI());
			}
			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	/**
	 * Renderer-Klasse fuer BigDecimal mit 4 Nachkommastellen.
	 */
	static class BigDecimal4Renderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;
			if (value != null && ((BigDecimal) value).doubleValue() != 0) {
				sShow = Helper.formatZahl((BigDecimal) value, 4, Defaults
						.getInstance().getLocUI());
			}
			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	static class BigDecimalFinanzRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;
			if ((value != null) && (!value.getClass().equals(BigDecimal.class))) {
				// System.out.println("Wrong cast");
				// do nothing
			} else {
				if (value != null) {
					sShow = Helper.formatZahl(
							Helper.rundeKaufmaennisch((BigDecimal) value, 2),
							2, Defaults.getInstance().getLocUI());
				}

				setText((sShow == null) ? "" : sShow);
			}
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	/**
	 * Renderer-Klasse fuer Double, Float. <br>
	 * Darstellung immer mit zwei Nachkommastellen.
	 */
	static class DoubleFloatRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;

			if (value != null && ((Number) value).doubleValue() != 0) {
				sShow = Helper.formatZahl((Number) value, 2, Defaults
						.getInstance().getLocUI());
			}

			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	/**
	 * Renderer-Klasse fuer Timestamp.
	 */
	static class TimestampRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;

			if (value != null) {
				sShow = Helper.formatTimestamp((Timestamp) value, Defaults
						.getInstance().getLocUI());
			}

			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.LEFT;
		}
	}

	/**
	 * Feststellen, ob ein bestimmtes Datum lt. Betriebskalender ein Feiertag
	 * ist.
	 * 
	 * @param date
	 *            Date
	 * @return boolean
	 */
	public static boolean isFeiertag(java.util.Date date) {
		Timestamp t = new Timestamp(date.getTime());
		t = Helper.cutTimestamp(t);
		return Defaults.getInstance().getFeiertage().containsKey(t);
	}

	/**
	 * Feststellen, ob ein bestimmtes Datum lt. Betriebskalender ein Feiertag
	 * ist.
	 * 
	 * @param date
	 *            Date
	 * @return boolean
	 */
	public static String getFeiertagBez(java.util.Date date) {
		Timestamp t = new Timestamp(date.getTime());
		t = Helper.cutTimestamp(t);
		return Defaults.getInstance().getFeiertage().get(t);
	}

	/**
	 * Renderer-Klasse fuer Number.
	 */
	static class NumberRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;

			if (value != null) {
				try {
					sShow = Helper.formatZahl((Number) value, LPMain
							.getInstance().getTheClient().getLocUi());
				} catch (Throwable t) {
					sShow = t.getMessage();
				}
			}

			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	static class BooleanRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			boolean enabled = false;
			if (value != null) {
				enabled = ((Boolean) value).booleanValue();
			}

			final JCheckBox box = new JCheckBox();
			box.setHorizontalAlignment(SwingConstants.CENTER);
			box.setSelected(enabled);

			if (isSelected) {
				Color c = super.getSpecialForecolor(
						(DistributedTableModel) table.getModel(), row);
				if (c != null)
					box.setBackground(c);
				else
					box.setBackground(table.getSelectionBackground());
			} else {
				box.setBackground(UIManager.getColor("Table.background"));
			}
			return box;
		}

	}

	/**
	 * Renderer-Klasse fuer Number.
	 */
	static class LabelRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			// Original.
			// JLabel jlaShow = null;
			//
			// if (value != null) {
			// try {
			// jlaShow = (JLabel) value;
			// } catch (Throwable t) {
			// jlaShow.setText(t.getMessage());
			// }
			// }
			//
			// setForeground(jlaShow.getForeground());
			//
			// setText((jlaShow.getText() == null) ? "" : jlaShow.getText());
			// Original.

			JLabel jlaShow = null;
			if (value instanceof JLabel) {
				jlaShow = (JLabel) value;
			} else {
				jlaShow = new JLabel("");
			}

			setForeground(jlaShow.getForeground());
			setText((jlaShow.getText() == null) ? "" : jlaShow.getText());
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	// private static ImageIcon iconStatusGestoppt = null;
	// private static ImageIcon iconStatusAngelegt = null;
	// private static ImageIcon iconStatusOffen = null;
	// private static ImageIcon iconStatusTeilerledigt = null;
	// private static ImageIcon iconStatusErledigt = null;
	// private static ImageIcon iconStatusStorniert = null;
	// private static ImageIcon iconStatus = null;
	//
	// private static JLabel jlaIconStatusGestoppt = null;
	// private static JLabel jlaIconStatusAngelegt = null;
	// private static JLabel jlaIconStatusOffen = null;
	// private static JLabel jlaIconStatusTeilerledigt = null;
	// private static JLabel jlaIconStatusErledigt = null;
	// private static JLabel jlaIconStatusStorniert = null;
	// private static JLabel jlaIconStatus = null;

	private static HashMap<?, ?> hmStatiIcons = null;
	private static HashMap<?, ?> hmSperrenIcons = null;
	private static HashMap<String, CompoundIcon> hmStatiIconsCompoundIcon = null;
	private static HashMap<String, CompoundIcon> hmSperrenIconsCompoundIcon = null;

	public static JLabel getImageIconStatus(String cNrStatusI) throws Throwable {
		if (hmStatiIcons == null) {
			hmStatiIcons = DelegateFactory.getInstance().getLocaleDelegate()
					.getAllStatiIcon();
		}
		JLabel jlaStatus = null;

		if (hmStatiIconsCompoundIcon == null) {
			hmStatiIconsCompoundIcon = new HashMap<String, CompoundIcon>();
		}

		CompoundIcon ci = null;

		if (hmStatiIconsCompoundIcon.containsKey(cNrStatusI)) {
			ci = hmStatiIconsCompoundIcon.get(cNrStatusI);
		} else {
			byte[] oBild = (byte[]) hmStatiIcons.get(cNrStatusI);
			ci = new CompoundIcon(new javax.swing.ImageIcon(oBild));
			hmStatiIconsCompoundIcon.put(cNrStatusI, ci);
		}

		if (ci != null) {
			jlaStatus = new JLabel(ci);
			jlaStatus.setFont(getDefaultFont());
		} else {
			jlaStatus = new JLabel(cNrStatusI);
		}

		/*
		 * if (cNrStatusI.equals(LocaleFac.STATUS_ANGELEGT)) { if
		 * (iconStatusAngelegt == null) { iconStatusAngelegt = new
		 * javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status_angelegt.png"));
		 * jlaIconStatusAngelegt = new JLabel(iconStatusAngelegt); }
		 * 
		 * jlaStatus = jlaIconStatusAngelegt; } else if
		 * (cNrStatusI.equals(LocaleFac.STATUS_OFFEN)) { if (iconStatusOffen ==
		 * null) { iconStatusOffen = new javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status_offen.png"));
		 * jlaIconStatusOffen = new JLabel(iconStatusOffen); }
		 * 
		 * jlaStatus = jlaIconStatusOffen; } else if
		 * (cNrStatusI.equals(LocaleFac.STATUS_TEILERLEDIGT)) { if
		 * (iconStatusTeilerledigt == null) { iconStatusTeilerledigt = new
		 * javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status_teilerledigt.gif"
		 * )); jlaIconStatusTeilerledigt = new JLabel(iconStatusTeilerledigt); }
		 * 
		 * jlaStatus = jlaIconStatusTeilerledigt; } else if
		 * (cNrStatusI.equals(LocaleFac.STATUS_ERLEDIGT)) { if
		 * (iconStatusErledigt == null) { iconStatusErledigt = new
		 * javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status_erledigt.gif"));
		 * jlaIconStatusErledigt = new JLabel(iconStatusErledigt); }
		 * 
		 * jlaStatus = jlaIconStatusErledigt; } else if
		 * (cNrStatusI.equals(LocaleFac.STATUS_STORNIERT)) { if
		 * (iconStatusStorniert == null) { iconStatusStorniert = new
		 * javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status_storniert.png"));
		 * jlaIconStatusStorniert = new JLabel(iconStatusStorniert); }
		 * 
		 * jlaStatus = jlaIconStatusStorniert; } else if
		 * (cNrStatusI.equals(LocaleFac.STATUS_GESTOPPT)) { if
		 * (iconStatusGestoppt == null) { iconStatusGestoppt = new
		 * javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status_gestoppt.png"));
		 * jlaIconStatusGestoppt = new JLabel(iconStatusGestoppt); }
		 * 
		 * jlaStatus = jlaIconStatusGestoppt; } else if (cNrStatusI.equals( //
		 * 
		 * @todo muss zu Server passen PJ 4698
		 * LPMain.getInstance().getTextRespectUISPr("lp.status"))) { if
		 * (iconStatus == null) { iconStatus = new javax.swing.ImageIcon(
		 * Object.class.getResource("/com/lp/client/res/status.gif"));
		 * jlaIconStatus = new JLabel(iconStatus); }
		 * 
		 * jlaStatus = jlaIconStatus; }
		 */

		return jlaStatus;
	}

	public static JLabel getImageIconSperren(String cBezSperre)
			throws Throwable {
		if (hmSperrenIcons == null) {
			hmSperrenIcons = DelegateFactory.getInstance().getArtikelDelegate()
					.getAllSperrenIcon();
		}
		JLabel jlaStatus = null;

		if (hmSperrenIconsCompoundIcon == null) {
			hmSperrenIconsCompoundIcon = new HashMap<String, CompoundIcon>();
		}

		CompoundIcon ci = null;

		if (hmSperrenIconsCompoundIcon.containsKey(cBezSperre)) {
			ci = hmSperrenIconsCompoundIcon.get(cBezSperre);
		} else {

			byte[] oBild = (byte[]) hmSperrenIcons.get(cBezSperre);
			if (oBild == null) {
				return null;
			}
			ci = new CompoundIcon(new javax.swing.ImageIcon(oBild));
			hmSperrenIconsCompoundIcon.put(cBezSperre, ci);
		}
		if (ci != null) {
			jlaStatus = new JLabel(ci);
		}

		return jlaStatus;
	}

	static class ImageIconRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel jlaIcon = null;

			try {
				if (value instanceof String) {
					jlaIcon = getImageIconStatus((String) value);

					// wenn kein Image hinterlegt ist, ein normales JLabel
					// erzeugen
					if (jlaIcon == null) {
						jlaIcon = new JLabel(value.toString());
					}
				}
			} catch (Throwable t) {
				jlaIcon = new JLabel(t.getMessage());
			}

			return jlaIcon;
		}
	}

	static Icon createImageIconImpl(String iconName) {
		// Der Original-Classloader muss wegen Webstart erhalten bleiben.
		// LPMain befindet sich im Webstart jar
		URL imageUrl = LPMain.class.getResource("/com/lp/client/res/"
				+ iconName);
		Icon icon = new ImageIcon(imageUrl);
		return icon;
	}

	static class StatusIconRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel jlaIcon = null;
			boolean keinLabel = false;
			try {
				if (value == null) {
					jlaIcon = new JLabel();
				}
				if (value instanceof String) {
					jlaIcon = getImageIconStatus((String) value);
					jlaIcon.setToolTipText(value.toString().trim());

					// SK: 14081,09
					// Neben der Statusbezeichnung auch den Tooltip uebersetzt
					// anzeigen
				} else if (value instanceof DoppelIcon) {
					DoppelIcon doppelIcon = (DoppelIcon) value;

					JLabel jlaIcon1 = new JLabel();
					JLabel jlaIcon2 = new JLabel();

					jlaIcon1 = getImageIconStatus(doppelIcon.getIcon1());

					String tooltip="";
					if(doppelIcon.getTooltip1()!=null){
						tooltip=doppelIcon.getTooltip1();
					}
					
					if (doppelIcon.getIcon2() != null) {

						try {
							jlaIcon2 = getImageIconStatus(doppelIcon.getIcon2());
							CompoundIcon ci = new CompoundIcon(jlaIcon1.getIcon(),
									jlaIcon2.getIcon());
							jlaIcon = new JLabel(ci);
							
							if(doppelIcon.getTooltip2()!=null){
								tooltip+=" ("+doppelIcon.getTooltip2()+")";
							}
							jlaIcon.setToolTipText(tooltip);
						} catch (NullPointerException e) {
							Icon icon = createImageIconImpl("leer.png");
							CompoundIcon ci = new CompoundIcon(jlaIcon1.getIcon(),
									icon);
							jlaIcon = new JLabel(ci);
							jlaIcon.setToolTipText(tooltip);
						}

						
						
					} else {

						Icon icon = createImageIconImpl("leer.png");
						CompoundIcon ci = new CompoundIcon(jlaIcon1.getIcon(),
								icon);
						jlaIcon = new JLabel(ci);
						jlaIcon.setToolTipText(tooltip);
					}

				} else if (value instanceof Object[]) {
					Object[] avalue = (Object[]) value;
					if (avalue[0] != null) {

						try {
							jlaIcon = getImageIconStatus((String) avalue[0]);
						} catch (Exception e) {
							jlaIcon = new JLabel((String) avalue[0]);
							jlaIcon.setOpaque(true);
						}
						if (avalue[1] != null) {
							jlaIcon.setToolTipText(avalue[1].toString().trim());
						} else {
							jlaIcon.setToolTipText(avalue[0].toString().trim());
						}

					} else {
						// Icon icon = new ImageIcon(
						// Object.class
						// .getResource("/com/lp/client/res/leer.png"));
						Icon icon = createImageIconImpl("leer.png");
						CompoundIcon ci = new CompoundIcon(icon, icon);
						jlaIcon = new JLabel(ci);
						jlaIcon.setToolTipText("");
					}

					// mit versanddatum und versandtyp
					if (avalue.length == 4) {
						if (avalue[2] != null) {
							String sTyp = (String) avalue[3];
							if (sTyp == null)
								sTyp = DOUBLE_RIGHT_ARROWS_SPACED;
							else
								sTyp = sTyp.substring(0, 1);
							jlaIcon.setOpaque(true);
							Icon icon = null;
							String sIcon = "";
							switch (sTyp.toCharArray()[0]) {
							case 'D': // DRUCKER
								sIcon = "printer.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (gedruckt)");
								break;
							case 'P': // PREVIEW
								sIcon = "printer_view.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (Vorschau)");
								break;
							case 'M': // MAIL
								sIcon = "mail.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (Mailversand)");
								break;
							case 'F': // FAX
								sIcon = "fax.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (Faxversand)");
								break;
							case 'C': // CSV
								sIcon = "document_out.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (CSV-Export)");
								break;
							case 'S': // SAVE
								sIcon = "save.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (gespeichert)");
								break;
							case 'R': // SAVE
								sIcon = "exchange16x16.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (offene Reklamationen)");
								break;
							}

							if (sIcon.length() != 0) {
								icon = createImageIconImpl(sIcon);

								// icon = new ImageIcon(
								// Object.class
								// .getResource("/com/lp/client/res/"
								// + sIcon));
								CompoundIcon ci = new CompoundIcon(
										jlaIcon.getIcon(), icon);
								jlaIcon.setIcon(ci);
							} else {
								jlaIcon.setText(sTyp);
							}
							if (isSelected) {
								jlaIcon.setBackground(table
										.getSelectionBackground());
								jlaIcon.setForeground(table
										.getSelectionForeground());
							} else {
								jlaIcon.setBackground(table.getBackground());
								jlaIcon.setForeground(table.getForeground());
							}
						} else {
							jlaIcon.setText(DOUBLE_RIGHT_ARROWS_SPACED);
							jlaIcon.setForeground(Color.WHITE);
							keinLabel = true;
						}
					}

					// mit versanddatum ohne versandtyp
					if (avalue.length == 3) {
						jlaIcon.setText(DOUBLE_RIGHT_ARROWS);
						if (avalue[2] != null) {
							jlaIcon.setForeground(Color.BLACK);
						} else {
							jlaIcon.setForeground(Color.WHITE);
							keinLabel = true;
						}
					}
				}

			} catch (Throwable t) {
				jlaIcon = new JLabel(t.getMessage());
			}
			if (jlaIcon != null) {
				jlaIcon.setOpaque(true);
				if (isSelected) {
					Color forecolor = super.getSpecialForecolor(
							(DistributedTableModel) table.getModel(), row);
					if (forecolor != null) {
						jlaIcon.setBackground(forecolor);
						if (keinLabel)
							jlaIcon.setForeground(forecolor);
					} else {
						jlaIcon.setBackground(table.getSelectionBackground());
						if (keinLabel)
							jlaIcon.setForeground(jlaIcon.getBackground());
					}
				} else
					jlaIcon.setBackground(UIManager
							.getColor("Table.background"));
			}
			return jlaIcon;
		}
	}

	static class SperrenIconRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel jlaIcon = null;
			boolean keinLabel = false;
			try {
				if (value == null) {
					jlaIcon = new JLabel();
				}
				if (value instanceof String && value != null) {
					jlaIcon = getImageIconSperren((String) value);
					if (jlaIcon == null) {
						jlaIcon = getImageIconStatus(LocaleFac.STATUS_GESPERRT);
					}
					jlaIcon.setToolTipText(value.toString().trim());

					// SK: 14081,09
					// Neben der Statusbezeichnung auch den Tooltip uebersetzt
					// anzeigen
				} else if (value instanceof Object[]) {
					Object[] avalue = (Object[]) value;
					if (avalue[0] != null) {

						try {

							jlaIcon = getImageIconSperren((String) avalue[0]);
							if (jlaIcon == null) {
								jlaIcon = getImageIconStatus(LocaleFac.STATUS_GESPERRT);
							}
						} catch (Exception e) {
							jlaIcon = new JLabel((String) avalue[0]);
							jlaIcon.setOpaque(true);
						}
						if (avalue[1] != null) {
							jlaIcon.setToolTipText(avalue[1].toString().trim());
						} else {
							jlaIcon.setToolTipText(avalue[0].toString().trim());
						}

					} else {
						// Icon icon = new ImageIcon(
						// Object.class
						// .getResource("/com/lp/client/res/leer.png"));
						Icon icon = createImageIconImpl("leer.png");
						CompoundIcon ci = new CompoundIcon(icon, icon);
						jlaIcon = new JLabel(ci);
						jlaIcon.setToolTipText("");
					}

					// mit versanddatum und versandtyp
					if (avalue.length == 4) {
						if (avalue[2] != null) {
							String sTyp = (String) avalue[3];
							if (sTyp == null)
								sTyp = DOUBLE_RIGHT_ARROWS_SPACED;
							else
								sTyp = sTyp.substring(0, 1);
							jlaIcon.setOpaque(true);
							Icon icon = null;
							String sIcon = "";
							switch (sTyp.toCharArray()[0]) {
							case 'D': // DRUCKER
								sIcon = "printer.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (gedruckt)");
								break;
							case 'P': // PREVIEW
								sIcon = "printer_view.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (Vorschau)");
								break;
							case 'M': // MAIL
								sIcon = "mail.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (Mailversand)");
								break;
							case 'F': // FAX
								sIcon = "fax.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (Faxversand)");
								break;
							case 'C': // CSV
								sIcon = "document_out.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (CSV-Export)");
								break;
							case 'S': // SAVE
								sIcon = "save.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (gespeichert)");
								break;
							case 'R': // SAVE
								sIcon = "exchange16x16.png";
								jlaIcon.setToolTipText(jlaIcon.getToolTipText()
										+ " (offene Reklamationen)");
								break;
							}
							if (sIcon.length() != 0) {
								// icon = new ImageIcon(
								// Object.class
								// .getResource("/com/lp/client/res/"
								// + sIcon));
								icon = createImageIconImpl(sIcon);
								CompoundIcon ci = new CompoundIcon(
										jlaIcon.getIcon(), icon);
								jlaIcon.setIcon(ci);
							} else {
								jlaIcon.setText(sTyp);
							}
							if (isSelected) {
								jlaIcon.setBackground(table
										.getSelectionBackground());
								jlaIcon.setForeground(table
										.getSelectionForeground());
							} else {
								jlaIcon.setBackground(table.getBackground());
								jlaIcon.setForeground(table.getForeground());
							}
						} else {
							jlaIcon.setText(DOUBLE_RIGHT_ARROWS_SPACED);
							jlaIcon.setForeground(Color.WHITE);
							keinLabel = true;
						}
					}

					// mit versanddatum ohne versandtyp
					if (avalue.length == 3) {
						jlaIcon.setText(DOUBLE_RIGHT_ARROWS);
						if (avalue[2] != null) {
							jlaIcon.setForeground(Color.BLACK);
						} else {
							jlaIcon.setForeground(Color.WHITE);
							keinLabel = true;
						}
					}
				}

			} catch (Throwable t) {
				jlaIcon = new JLabel(t.getMessage());
			}
			if (jlaIcon != null) {
				jlaIcon.setOpaque(true);
				if (isSelected) {
					Color forecolor = super.getSpecialForecolor(
							(DistributedTableModel) table.getModel(), row);
					if (forecolor != null) {
						jlaIcon.setBackground(forecolor);
						if (keinLabel)
							jlaIcon.setForeground(forecolor);
					} else {
						jlaIcon.setBackground(table.getSelectionBackground());
						if (keinLabel)
							jlaIcon.setForeground(jlaIcon.getBackground());
					}
				} else
					jlaIcon.setBackground(UIManager
							.getColor("Table.background"));
			}
			return jlaIcon;
		}
	}

	static class ImageIconHeaderRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel jlaIcon = null;

			try {
				if (value instanceof String) {
					jlaIcon = getImageIconStatus((String) value);

					// wenn kein Image hinterlegt ist, ein normales JLabel
					// erzeugen
					if (jlaIcon == null) {
						jlaIcon = new JLabel(value.toString());
					}
				}
			} catch (Throwable t) {
				// jlaIcon.setText(t.getMessage());
				jlaIcon = new JLabel(t.getMessage());
			}

			return jlaIcon;
		}
	}

	/**
	 * Renderer-Klasse fuer BigDecimal.
	 */
	static class BetragRenderer extends LPDefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Wert formatiert setzen und einfaerben.
		 * 
		 * @param value
		 *            Object
		 */
		public void setValue(Object value) {
			String sShow = null;

			try {
				if (value != null) {
					sShow = Helper.formatZahl((BigDecimal) value, LPMain
							.getInstance().getTheClient().getLocUi());
				}
			} catch (Throwable t) {
				sShow = t.getMessage();
			}

			setText((sShow == null) ? "" : sShow);
		}

		/**
		 * Zelle rechts ausrichten.
		 * 
		 * @return int
		 */
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}
	}

	public static int getReportZoom() {
		int iZoom;
		try {
			iZoom = ClientConfiguration.getReportZoom() ; 
		} catch (Throwable ex) {
			iZoom = ReportViewer.DEFAULT_ZOOM;
		}
		return iZoom;
	}

	public static Dimension getToolsPanelButtonDimension() {
		return new Dimension(28, 23);
	}

	public static Font getDefaultFont() {
		Font font = null;
		try {
			String sFontName = ClientConfiguration.getUiFontName() ;
			String sFontStyle = ClientConfiguration.getUiFontStyle() ;
			String sFontSize = ClientConfiguration.getUiFontSize() ;

			int style = Font.PLAIN; // default
			if (sFontStyle.equalsIgnoreCase("bold")) {
				style = Font.BOLD;
			} else if (sFontStyle.equalsIgnoreCase("italic")) {
				style = Font.ITALIC;
			}
			int iFontSize = Integer.parseInt(sFontSize);
			font = new Font(sFontName, style, iFontSize);
		} catch (Throwable e) {
			// default, falls das properties-file nicht stimmt
			font = new Font("Tahoma", Font.PLAIN, 11);
		}
		return font;
	}

	/**
	 * BigDecimals in der DB muessen das Format 15, 4 haben. Es ist moeglich,
	 * dass das Ergebnis einer Berechnung am Client mehr als 15 Vorkommastellen
	 * hat. In diesem Fall wird dem Benutzer eine entsprechende Meldung
	 * angezeigt. checknumberformat: 3
	 * 
	 * @param bdNumberI
	 *            die Zahl
	 * @return boolean true, wenn die Zahl gueltig ist
	 * @throws Throwable
	 *             Ausnahme
	 */
	public static boolean checkNumberFormat(BigDecimal bdNumberI)
			throws Throwable {
		boolean bFormatgueltig = true;

		if (bdNumberI.doubleValue() > SystemFac.MAX_N_NUMBER
				|| bdNumberI.doubleValue() < SystemFac.MIN_N_NUMBER) {
			bFormatgueltig = false;

			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.warning"),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.warning.ergebniskannnichtgespeichertwerden"));
		}

		return bFormatgueltig;
	}

	public static Icon getIconForType(int messageType) {
		if (messageType < 0 || messageType > 3) {
			return null;
		}
		switch (messageType) {
		case JOptionPane.ERROR_MESSAGE:
			return UIManager.getIcon("OptionPane.errorIcon");
		case JOptionPane.INFORMATION_MESSAGE:
			return UIManager.getIcon("OptionPane.informationIcon");
		case JOptionPane.WARNING_MESSAGE:
			return UIManager.getIcon("OptionPane.warningIcon");
		case JOptionPane.QUESTION_MESSAGE:
			return UIManager.getIcon("OptionPane.questionIcon");
		}
		return null;
	}

	public static String getBlankString(String value) {
		return value == null ? "" : value;
	}

	public static void vCardAlsDateiExportieren(PartnerDto partnerDto,
			AnsprechpartnerDto ansprechpartnerDto) throws Throwable {

		String crlf = System.getProperty("line.separator");

		StringBuffer sbVcard = new StringBuffer();

		sbVcard.append("BEGIN:VCARD" + crlf);
		sbVcard.append("VERSION:3.0" + crlf);

		// Name

		// String cname1 = partnerDto.getCName1nachnamefirmazeile1();

		// String cname2 =
		// getBlankString(partnerDto.getCName2vornamefirmazeile2()) ;

		String cname3 = getBlankString(partnerDto.getCName3vorname2abteilung());

		// String titel = getBlankString(partnerDto.getCTitel()) ;

		sbVcard.append("ORG:" + partnerDto.formatFixName1Name2() + ";" + cname3
				+ crlf);

		// Adresse

		String cStrasseFirma = getBlankString(partnerDto.getCStrasse());

		String landFirma = "";

		String plzFirma = "";
		String ortFirma = "";

		if (partnerDto.getLandplzortDto() != null) {
			plzFirma = getBlankString(partnerDto.getLandplzortDto().getCPlz());
			landFirma = getBlankString(partnerDto.getLandplzortDto()
					.getLandDto().getCName());
			ortFirma = getBlankString(partnerDto.getLandplzortDto().getOrtDto()
					.getCName());
		}
		sbVcard.append("ADR;WORK:;;" + cStrasseFirma + ";" + ortFirma + ";;"
				+ plzFirma + ";" + landFirma + crlf);

		// Ansprechpartner
		if (ansprechpartnerDto != null) {

			sbVcard.append("FN:"
					+ ansprechpartnerDto.getPartnerDto().formatFixName2Name1()
					+ crlf);

			String nachname = ansprechpartnerDto.getPartnerDto()
					.getCName1nachnamefirmazeile1();

			String vorname = getBlankString(ansprechpartnerDto.getPartnerDto()
					.getCName2vornamefirmazeile2());

			String vorname2 = getBlankString(ansprechpartnerDto.getPartnerDto()
					.getCName3vorname2abteilung());

			String titelAnsp = getBlankString(ansprechpartnerDto
					.getPartnerDto().getCTitel());

			sbVcard.append("N:" + nachname + ";" + vorname + ";;" + titelAnsp
					+ crlf);

			String cStrasse = getBlankString(ansprechpartnerDto.getPartnerDto()
					.getCStrasse());
			String land = "";

			String plz = "";
			String ort = "";

			if (ansprechpartnerDto.getPartnerDto().getLandplzortDto() != null) {
				plz = getBlankString(ansprechpartnerDto.getPartnerDto()
						.getLandplzortDto().getCPlz());
				land = getBlankString(ansprechpartnerDto.getPartnerDto()
						.getLandplzortDto().getLandDto().getCName());
				ort = getBlankString(ansprechpartnerDto.getPartnerDto()
						.getLandplzortDto().getOrtDto().getCName());
			}
			sbVcard.append("ADR;HOME:;;" + cStrasse + ";" + ort + ";"
					+ vorname2 + ";" + plz + ";" + land + crlf);

			String telefonPrivat = getBlankString(ansprechpartnerDto
					.getPartnerDto().getCTelefon());
			sbVcard.append("TEL;HOME;VOICE:" + telefonPrivat + crlf);

			String faxPrivat = getBlankString(ansprechpartnerDto
					.getPartnerDto().getCFax());
			sbVcard.append("TEL;HOME;FAX:" + faxPrivat + crlf);

			// Hier noch einbauen -> DW zusammenbasteln oder Hauptnummer

			String telefon = getBlankString(DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerkommFindOhneExec(
							partnerDto.getIId(),
							ansprechpartnerDto.getPartnerIIdAnsprechpartner(),
							PartnerFac.KOMMUNIKATIONSART_TELEFON,
							LPMain.getInstance().getTheClient()
									.getSMandantenwaehrung()));
			sbVcard.append("TEL;WORK;VOICE:" + telefon + crlf);

			String fax = getBlankString(DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.partnerkommFindOhneExec(
							partnerDto.getIId(),
							ansprechpartnerDto.getPartnerIIdAnsprechpartner(),
							PartnerFac.KOMMUNIKATIONSART_FAX,
							LPMain.getInstance().getTheClient()
									.getSMandantenwaehrung()));
			sbVcard.append("TEL;WORK;FAX:" + fax + crlf);

			String homepage = getBlankString(partnerDto.getCHomepage());
			sbVcard.append("URL;WORK:" + homepage + crlf);
			String homepagePrivate = getBlankString(ansprechpartnerDto
					.getPartnerDto().getCHomepage());
			sbVcard.append("URL;HOME:" + homepagePrivate + crlf);

			String handy = getBlankString(ansprechpartnerDto.getCHandy());
			sbVcard.append("TEL;CELL;VOICE:" + handy + crlf);

			if (ansprechpartnerDto.getCEmail() != null) {
				sbVcard.append("EMAIL;TYPE=INTERNET,PREF:"
						+ ansprechpartnerDto.getCEmail() + crlf);
			}
			if (partnerDto.getCEmail() != null) {
				sbVcard.append("EMAIL;TYPE=INTERNET:" + partnerDto.getCEmail()
						+ crlf);
			}

			if (ansprechpartnerDto.getPartnerDto()
					.getDGeburtsdatumansprechpartner() != null) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				sbVcard.append("BDAY:"
						+ simpleDateFormat.format(ansprechpartnerDto
								.getPartnerDto()
								.getDGeburtsdatumansprechpartner()) + crlf);
				// BDAY:1996-04-15

			}

		} else {

			sbVcard.append("FN:" + partnerDto.formatFixTitelName1Name2() + crlf);

			String homepage = getBlankString(partnerDto.getCHomepage());
			sbVcard.append("URL;WORK:" + homepage + crlf);

			String telefon = getBlankString(partnerDto.getCTelefon());

			sbVcard.append("TEL;WORK;VOICE:" + telefon + crlf);

			String fax = getBlankString(partnerDto.getCFax());
			sbVcard.append("TEL;WORK;FAX:" + fax + crlf);

			String email = getBlankString(partnerDto.getCEmail());
			sbVcard.append("EMAIL;TYPE=INTERNET:" + email + crlf);
		}

		sbVcard.append("END:VCARD" + crlf);

		JFileChooser fc = new JFileChooser();
		fc.setDialogType(JFileChooser.SAVE_DIALOG);

		fc.setApproveButtonText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		fc.setDialogTitle(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));

		String fileName = partnerDto.getCName1nachnamefirmazeile1();
		if (ansprechpartnerDto != null) {

			fileName = ansprechpartnerDto.getPartnerDto().formatFixName2Name1()
					+ ", " + fileName;
		}

		fc.setSelectedFile(new File(fileName + ".vcf"));

		fc.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".vcf")
						|| f.isDirectory();
			}

			public String getDescription() {
				return "vCard-Dateien";
			}
		});

		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			Writer output = null;

			try {
				output = new BufferedWriter(new FileWriter(file));

				output.write(sbVcard.toString());
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static PartnerkommunikationDto erzeugePartnerkommunikationDto(
			PartnerkommunikationDto partnerkommunikationDto,
			String sPartnerkommBez, String sPartnerkommInhalt,
			Integer iIdPartner, boolean bMandantenabhaengig) throws Throwable {

		if (partnerkommunikationDto == null) {
			partnerkommunikationDto = new PartnerkommunikationDto();
			if (bMandantenabhaengig) {
				partnerkommunikationDto.setCNrMandant(LPMain.getInstance()
						.getTheClient().getMandant());
			}
		}
		partnerkommunikationDto.setKommunikationsartCNr(sPartnerkommBez);
		partnerkommunikationDto.setCInhalt(sPartnerkommInhalt);
		partnerkommunikationDto.setPartnerIId(iIdPartner);
		partnerkommunikationDto.setCBez(sPartnerkommBez);
		return partnerkommunikationDto;
	}

	public static void setToolTipTextMitRechtToComponent(JComponent compI,
			String rechtCNr) {
		// Das erforderliche Benutzerrecht im Tooltip anzeigen.
		if (rechtCNr != null && Defaults.getInstance().darfRechteSehen()) {
			String sTooltip = compI.getToolTipText();
			String sRecht = LPMain.getTextRespectUISPr("lp.benutzerrecht")
					+ ": " + rechtCNr;
			if (sTooltip == null) {
				sTooltip = sRecht;
			} else {
				sTooltip = "<html>" + sTooltip + "<br>" + sRecht + "</html>";
			}
			compI.setToolTipText(sTooltip);
		}
	}

	/**
	 * Setzt die Namen fuer alle menus and menuItems mit dem Namen des
	 * Action-kommandos.
	 * 
	 * @param jMenuBar
	 *            JMenuBar
	 */
	public static final void setComponentNamesMenuBar(JMenuBar jMenuBar) {
		Component[] components = jMenuBar.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JMenu) {
				setComponentNamesMenu((JMenu) components[i]);
			}
		}
	}

	/**
	 * Setzt die Namen fuer ein Menue. Ruft sich fuer Untermenues rekursiv auf.
	 * 
	 * @param jMenu
	 *            JMenu
	 */
	public static final void setComponentNamesMenu(JMenu jMenu) {
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			Component[] menuComponents = jMenu.getMenuComponents();
			for (int i = 0; i < menuComponents.length; i++) {
				if (menuComponents[i] instanceof JMenuItem) {
					JMenuItem jMenuItem = (JMenuItem) menuComponents[i];
					boolean isMenu = (jMenuItem instanceof JMenu);

					if (jMenuItem.getActionCommand() != null) {
						if (isMenu) {
							jMenuItem.setName(COMP_PRAEFIX_MENU
									+ jMenuItem.getActionCommand().trim());
						} else {
							jMenuItem.setName(COMP_PRAEFIX_MENUITEM
									+ jMenuItem.getActionCommand().trim());
						}
					}
					// rekursiv aufrufen fuer Untermenues
					if (isMenu) {
						setComponentNamesMenu((JMenu) jMenuItem);
					}
				}
			}
		}
	}

	/**
	 * Alle Member-Variablen der objects vom Typ java.awt.Component erhalten als
	 * Name den Variablennamen. Notwendig fuer Testumgebungen wie qftest, abbot,
	 * etc.
	 * 
	 * @param object
	 *            das object, fuer dessen Membervariablen setName aufgerufen
	 *            werden soll
	 */
	public static final void setComponentNames(Object object) {
		setComponentNames(object, null);
	}

	/**
	 * Alle Member-Variablen der objects vom Typ java.awt.Component erhalten als
	 * Name den Variablennamen. Notwendig fuer Testumgebungen wie qftest, abbot,
	 * etc.
	 * 
	 * @param object
	 *            Object
	 * @param sPraefix
	 *            String
	 */
	public static final void setComponentNames(Object object, String sPraefix) {
		try {
			setComponentNames(object, sPraefix, false);
		} catch (Throwable ex) {
			// @ToDo: KF->KF log
		}
	}

	/**
	 * Alle Member-Variablen der objects vom Typ java.awt.Component erhalten als
	 * Name den Variablennamen. Notwendig fuer Testumgebungen wie qftest, abbot,
	 * etc. InternalFrames werden ausgenommen. Fuer diese von deren
	 * initComponents direkt auf den Klassennamen mit kleinem Anfangsbuchstaben
	 * gesetzt, da scheinbar nicht alle panels die internalFrames referenzieren
	 * und daher der Name sonst ggf. nicht gesetzt wuerde.
	 * 
	 * @see InternalFrame#setName(String)
	 * @param object
	 *            das object, fuer dessen Membervariablen setName aufgerufen
	 *            werden soll.
	 * @param sPraefix
	 *            String
	 * @param bGenerateUniqueNames
	 *            wenn true, dann eindeutige Namen erzeugen.
	 * @throws Throwable
	 */
	private static final void setComponentNames(Object object, String sPraefix,
			boolean bGenerateUniqueNames) throws Throwable {
		// nur dann, wenn die Testumgebung laeuft
		if (Defaults.getInstance().isComponentNamingEnabled()) {
//			long tStart = System.currentTimeMillis();
			// Field[] fields = object.getClass().getDeclaredFields();
			Field[] fields = getAllGuiFields(object);
			for (int i = 0; i < fields.length; i++) {
				String sName = fields[i].getName();
				// Alle final bzw. static konstanten ignorieren
				if (!(Modifier.isFinal(fields[i].getModifiers()) && Modifier
						.isStatic(fields[i].getModifiers()))) {
					boolean bIsAccessible = fields[i].isAccessible();
					if (!bIsAccessible) {
						// Zugriff sichern
						fields[i].setAccessible(true);
					}
					// Objekt laden
					Object oComponent = fields[i].get(object);
					if ((oComponent instanceof java.awt.Component)) {
						// Warnung fuer Felder, die nicht private oder protected
						// sind
						if (!Modifier.isPrivate(fields[i].getModifiers())
								&& !Modifier.isProtected(fields[i]
										.getModifiers())) {
							myLogger.warn(sName + " in "
									+ object.getClass().getName()
									+ " is not private or protected");
						}

						// internalFrames ausnehmen. (setzen ihren Namen
						// selbst)
						// einstufige zirkulaere referenz ausnehmen
						if ((!(oComponent instanceof InternalFrame))) {

							if (sPraefix != null) {
								sName = sPraefix + sName;
							}

							// Den Klassennamen anhaengen, damit eindeutige
							// Namen vergeben werden.
							if (bGenerateUniqueNames) {
								sName = object.getClass().getSimpleName() + "."
										+ sName;
							}

							java.awt.Component component = (java.awt.Component) oComponent;

							if (component.getName() == null) {
								component.setName(sName);
								if (component instanceof IDirektHilfe) {
									IDirektHilfe dh = (IDirektHilfe) component;
									if (dh.getToken() == null)
										dh.setToken(sName);
								}
								if(component instanceof Container)
									addConstraintListener((Container)component);

								// falls ein JPanel, dann rekursiv aufrufen.
								if (oComponent instanceof JPanel
										|| oComponent instanceof JScrollPane) {
									setComponentNames(oComponent, sPraefix,
											bGenerateUniqueNames);
								}
							}
						}
					} else if (oComponent instanceof WrapperIdentField) {
						((WrapperIdentField) oComponent)
								.setComponentNames(fields[i].getName());
					}
					// alte Verfuegbarkeit wiederherstellen
					fields[i].setAccessible(bIsAccessible);
				}
			}
			// Dauer loggen
//			long tEnd = System.currentTimeMillis();
//			myLogger.debug("setComponentNames for "
//					+ object.getClass().getName() + " dauerte "
//					+ (tEnd - tStart) + " ms.");
		}
	}

	/**
	 * Ersetzt die Punkte in einem Token durch underlines.
	 * 
	 * @param sDottedString
	 *            String
	 * @return String
	 */
	public static final String replaceDotsByUnderline(String sDottedString) {
		return sDottedString.replaceAll("\\.", "_");
	}

	/**
	 * Sucht alle Felder die entweder von java.awt.Component erben oder vom Typ
	 * WrapperIdentField sind aus der Klasse des angegebenen Objekts und auch
	 * geerbte Felder.
	 * 
	 * @param object
	 *            Object
	 * @return Field[]
	 */
	public static final Field[] getAllGuiFields(Object object) {
		Class<?> cls = object.getClass();
		List<Field> allFields = new LinkedList<Field>();
		while (cls != null) {
			Field[] fields = cls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// Objekt laden
				boolean bIsAccessible = fields[i].isAccessible();
				if (!bIsAccessible) {
					// Zugriff sichern
					fields[i].setAccessible(true);
				}

				Object oField = null;
				try {
					oField = fields[i].get(object);
				} catch (Exception ex) {
					myLogger.debug("Object for " + fields[i].toString()
							+ " could not be loaded!");
				}

				fields[i].setAccessible(bIsAccessible);
				if ((oField instanceof java.awt.Component)
						|| (oField instanceof WrapperIdentField)) {
					allFields.add(fields[i]);
				}
			}
			if (((object instanceof JPanel) && (!cls.getSimpleName().equals(
					"JPanel")))
					|| ((object instanceof JInternalFrame) && (!cls
							.getSimpleName().equals("JInternalFrame")))
					|| ((object instanceof JTabbedPane) && (!cls
							.getSimpleName().equals("JTabbedPane")))) {
				cls = cls.getSuperclass();
			} else {
				cls = null; // exit loop
			}
		}
		return (Field[]) allFields.toArray(new Field[allFields.size()]);

	}

	public static final void pruefeMemberVariableAufKonvention(Object object,
			Component c, String sName) {
		String sPraefix;
		if (c instanceof WrapperButton) {
			sPraefix = "wbu";
		} else if (c instanceof WrapperCheckBox) {
			sPraefix = "wcb";
		} else if (c instanceof WrapperComboBox) {
			sPraefix = "wco";
		} else if (c instanceof WrapperDateField) {
			sPraefix = "wdf";
		} else if (c instanceof WrapperLabel) {
			sPraefix = "wla";
		} else if (c instanceof WrapperNumberField) {
			sPraefix = "wnf";
		} else if (c instanceof WrapperRadioButton) {
			sPraefix = "wrb";
		} else if (c instanceof WrapperSpinner) {
			sPraefix = "wsp";
		} else if (c instanceof WrapperTextArea) {
			sPraefix = "wta";
		} else if (c instanceof WrapperTextField) {
			sPraefix = "wtf";
		} else if (c instanceof WrapperTextNumberField) {
			sPraefix = "wtnf";
		} else if (c instanceof WrapperDateRangeController) {
			sPraefix = "wdr";
		} else if (c instanceof WrapperMediaControl) {
			sPraefix = "wmc";
		} else if (c instanceof PanelQuery) {
			sPraefix = "pq";
		} else if (c instanceof LpEditor) {
			sPraefix = "lpE";
		} else if (c instanceof PanelBasis) {
			sPraefix = "panel";
		} else if (c instanceof JPanel) {
			sPraefix = "jpa";
		} else if (c instanceof JScrollPane) {
			sPraefix = "jsp";
		} else if (c instanceof JTable) {
			sPraefix = "jta";
		} else if (c instanceof TabbedPane) {
			sPraefix = "tabbedPane";
		} else if (c instanceof JTabbedPane) {
			sPraefix = "jtp";
		} else if (c instanceof InternalFrame) {
			sPraefix = "internalFrame";
		} else {
			sPraefix = "";
		}
		if (!sName.startsWith(sPraefix)) {
			myLogger.warn(sName + " in Klasse " + object.getClass().getName()
					+ " entspricht nicht den Codierrichtlinien. Muss mit \""
					+ sPraefix + "\" beginnen");
		}
	}

	public final static String FILE_FILTER_CSV = "csv";
	public final static String FILE_FILTER_XLS = "xls";

	public static File[] chooseFile(Component parent, String file_filter,
			boolean multiselect) {
		JFileChooser chooser = new JFileChooser();
		if (FILE_FILTER_CSV.equals(file_filter))
			chooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory())
						return true;
					return f.getName().toLowerCase().endsWith("csv");
				}

				public String getDescription() {
					return "CSV";
				}
			});
		chooser.setMultiSelectionEnabled(multiselect);
		String directory = LPMain.getInstance().getLastImportDirectory();
		if (directory != null)
			chooser.setCurrentDirectory(new File(directory));
		File[] f = null;
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			if (multiselect)
				f = chooser.getSelectedFiles();
			else
				f = new File[] { chooser.getSelectedFile() };
		}
		try {
			LPMain.getInstance().setLastImportDirectory(
					chooser.getCurrentDirectory().getPath());
		} catch (Exception e) {
			// ignore
		}
		return f;
	}

	/**
	 * Zeigt einen Datei-&Ouml;ffnen Dialog an und gibt die Datei zur&uuml;ck
	 * @param presetFile 
	 * @param parent
	 * @param multiselect 
	 * @param postfixes 
	 * @return null wenn das &Ouml;ffnen abgebrochen wurde/nicht m&ouml;glich
	 *         war, sonst die gew&auml;hlte(n) Datei(en)
	 */
	public static List<File> showOpenFileDialog(File presetFile,
			Component parent, boolean multiselect, String... postfixes) {
		if (presetFile == null) {
			try {
				presetFile = new File(LPMain.getInstance()
						.getLastImportDirectory());
			} catch (Exception e) {
			}
			;
		}

		JFileChooser fc = createFileChooser(presetFile, postfixes);
		fc.setMultiSelectionEnabled(multiselect);
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			try {
				LPMain.getInstance().setLastImportDirectory(
						fc.getCurrentDirectory().getPath());
			} catch (Exception e) {
			}
			;
			List<File> list = new ArrayList<File>();
			if (multiselect)
				list.addAll(Arrays.asList(fc.getSelectedFiles()));
			else {
				list.add(fc.getSelectedFile());
			}
			return list;
		}
		return null;
	}

	private static JFileChooser createFileChooser(File presetFile,
			final String... postfixes) {
		JFileChooser fc = new JFileChooser();
		if (presetFile != null)
			fc.setSelectedFile(presetFile);
		fc.setAcceptAllFileFilterUsed(postfixes == null
				|| postfixes.length == 0);

		if (postfixes == null)
			return fc;

		for (final String postfix : postfixes) {
			if (postfix == null)
				fc.setAcceptAllFileFilterUsed(true);
			fc.addChoosableFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return (postfix == null ? "" : postfix);
				}

				@Override
				public boolean accept(File f) {
					if (f.isDirectory())
						return true;
					if (postfix != null && !f.getName().endsWith(postfix)) {
						return false;
					}
					return true;
				}
			});
		}
		return fc;
	}

	/**
	 * Zeigt einen Datei-Speichern Dialog an und gibt die neu angelegte Datei
	 * zurueck
	 * 
	 * @param file
	 *            die zu Speichernde Datei
	 * @param presetFile der/die vorbesetzte Pfad/Datei
	 * @param parent
	 * @param postfix
	 *            wie die Datei enden muss, wenn null keine Filterung.
	 * @return null wenn das Speichern abgebrochen wurde/nicht moeglich war,
	 *         sonst die neue Datei
	 */
	public static File showSaveFileDialog(File file, File presetFile,
			Component parent, final String postfix) {
		JFileChooser fc = createFileChooser(presetFile, postfix);
		if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File dir = fc.getSelectedFile();
			if (postfix != null && !dir.getName().endsWith(postfix)) {
				dir = new File(dir.getPath() + postfix);
			}
			if (dir.exists()) {
				int answer = JOptionPane.showConfirmDialog(parent, LPMain
						.getTextRespectUISPr("lp.frage.dateiueberschreiben"),
						LPMain.getTextRespectUISPr("lp.warning"),
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION)
					dir.delete();
				else
					return null;
			}
			FileInputStream src = null;
			FileOutputStream dest = null;
			try {
				dir.createNewFile();
				src = new FileInputStream(file);
				dest = new FileOutputStream(dir);
				FileChannel srcChannel = src.getChannel();
				FileChannel destChannel = dest.getChannel();
				long size = srcChannel.size();
				srcChannel.transferTo(0, size, destChannel);
				return dir;
			} catch (IOException e) {
				JOptionPane
						.showMessageDialog(
								parent,
								LPMain.getTextRespectUISPr("lp.dokumente.fehlerbeimspeichern"),
								LPMain.getTextRespectUISPr("lp.error"),
								JOptionPane.ERROR_MESSAGE);
			} finally {
				if (src != null)
					try {
						src.close();
					} catch (IOException e) {
					}
				if (dest != null)
					try {
						dest.close();
					} catch (IOException e) {
					}
			}
		}
		return null;
	}

	public static void desktopOpenEx(File file) throws IOException {
		// PJ 15451
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			Runtime.getRuntime().exec(
					"rundll32 url.dll,FileProtocolHandler " + file);
		} else {
			java.awt.Desktop.getDesktop().open(file);
		}
	}

	/**
	 * Trys to open a file with the system default application. If no default
	 * application is set for the filetype, a save-file-dialog will be shown.
	 * 
	 * @param file
	 *            file to open
	 * @param parent
	 *            parent component (for dialog ownership, may be null)
	 * @return null if opened, else the saved file
	 * @throws IOException
	 */
	public static File desktopTryToOpenElseSave(File file, Component parent)
			throws IOException {
		try {
			HelperClient.desktopOpenEx(file);
		} catch (IOException dex) {
			// catch Exception Windows throws if no application is associated
			if (dex.getMessage().startsWith("Failed to open file")) {
				return showSaveFileDialog(file, null, parent, null);
			} else {
				// catch exception thrown by mac if no application is associated
				if (dex.getMessage()
						.startsWith(
								"Failed to launch the associated application with the specified file")) {
					return showSaveFileDialog(file, null, parent, null);
				} else {
					throw dex;
				}
			}
		}
		return null;
	}

	public static ImageIcon mergeIcons(Icon i1, Icon i2, int offsetRechtsOben) {
		Image image1 = ((ImageIcon) i1).getImage();
		Image image2 = ((ImageIcon) i2).getImage();

		Image image = new BufferedImage(i1.getIconWidth(), i1.getIconWidth(),
				BufferedImage.TRANSLUCENT);
		Graphics g = image.getGraphics();
		g.drawImage(image1, 0, 0, null);
		g.drawImage(image2, i1.getIconWidth() - i2.getIconWidth()
				- offsetRechtsOben, offsetRechtsOben, null);
		g.dispose();

		return new ImageIcon(image);
	}

	/**
	 * Erzeugt ein neues ImageIcon aus dem angegebenen Resourcenamen</br>
	 * Beispiel: ("/com/lp/client/res/closeToolTip.png")
	 * 
	 * @param resourceName
	 * @return ein ImageIcon
	 */
	public static ImageIcon createImageIconFullPath(String resourceName) {
		return new ImageIcon(HelperClient.class.getResource(resourceName));
	}

	/**
	 * ERzeugt ein neues ImageIcon mit dem kurzen Resourcename.</br> Der
	 * komplette Pfad wird dann "/com/lp/client/res" + resourcename
	 * 
	 * @param shortResourceName
	 * @return ein ImageIcon
	 */
	public static ImageIcon createImageIcon(String shortResourceName) {
		return new ImageIcon(
				HelperClient.class.getResource("/com/lp/client/res/"
						+ shortResourceName));
	}

	/**
	 * Berechnet ob schwarz oder wei&szlig als Hintergrundfarbe f&uuml;r das
	 * menschliche Auge besser lesbar ist, wenn ein dar&uuml;ber liegender Text
	 * die Farbe <code>c</code> hat, und liefert diese zur&uuml;ck.
	 * 
	 * @param c
	 *            die Farbe des Textes
	 * @return <code>Color.black</code> oder <code>Color.white</code>, je
	 *         nachdem was als Hintergrund besser geeignet ist.
	 */
	public static Color getContrastYIQ(Color c) {
		return (c.getRed() * 299 + c.getGreen() * 587 + c.getBlue() * 114) / 1000 >= 128 ? Color.black
				: Color.white;
	}
	
	public static String getServerName() {
		  String server = System.getProperty("java.naming.provider.url");

		  try {
		     int iB = server.indexOf("//") + 2;
		     int iM = server.lastIndexOf(":");
		     
		     server = server.substring(iB, iM);
		  }catch (Exception ex) {
		       server = "?";
		  }
		  
		  return server;		
	}
	
	
	/**
	 * Zwei Objekte auf equals vergleichen und dabei null behandeln.
	 * <p>sind beide Objekte == null wird true geliefert</p>
	 * <p>ansonsten wird das !null Objekt als equals Empfaenger benutzt.
	 * 
	 * @param o1
	 * @param o2
	 * @return true wenn beide null oder o1.equals(o2) ;
	 */
	public static boolean nullableEquals(Object o1, Object o2) {
		if(o1 == null && o2 == null) return true ;
		if(o1 == null) return o2.equals(o1) ;
		return o1.equals(o2) ;
	}	
}
