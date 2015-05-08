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
package com.lp.client.pc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class LPMessages {
	private static Map<Integer, String> simpleErrorMsgs = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_LAND_IM_KUNDEN,
					"fb.error.keinlandimkunden");
			put(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGE_STEUERBUCHUNG,
					"fb.error.ungueltigesteuerbuchung");
			put(EJBExceptionLP.FEHLER_STATUS, "lp.error.status");
			put(EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN,
					"lp.error.beleg.keinemengenbehaftetenpositionen");
			put(EJBExceptionLP.FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT,
					"lp.report.belegwurdeinzwischengeaendert");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST,
					"fb.error.kontolaenderverweistaufsichselbst");
			put(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE,
					"lp.dokumente.offline");
			put(EJBExceptionLP.FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN,
					"rechnung.essindbereitszahlungeneingetragen");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_KEINE_POS_VORHANDEN,
					"lp.error.positionen.keineweiterenpositionen");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS,
					"anf.error.positionenverschieben");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_VONBIS,
					"lp.error.positionen.vonnachbis");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_IN_SICH_SELBST,
					"lp.error.positionen.sichselbstbeinhalten");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_INSTALLATION,
					"lp.error.ungueltigeinstallation");
			put(EJBExceptionLP.FEHLER_SYSTEM_KOSTENSTELLE_IN_VERWENDUNG,
					"lp.error.kostenstelleinverwendung");
			put(EJBExceptionLP.FEHLER_SYSTEM_STUECK_KANN_NICHT_GELOESCHT_WERDEN,
					"lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_SYSTEM_SEKUNDE_KANN_NICHT_GELOESCHT_WERDEN,
					"lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_SYSTEM_STUNE_KANN_NICHT_GELOESCHT_WERDEN,
					"lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_SYSTEM_MINUTE_KANN_NICHT_GELOESCHT_WERDEN,
					"lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, "lp.error.anlegen");
			put(EJBExceptionLP.FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS,
					"lp.error.anlegen.entityexists");
			put(EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY,
					"lp.error.doppelterprimarykey");
			put(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					"lp.error.doppelterunique");
			put(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN, "lp.error.zahlzuklein");
			put(EJBExceptionLP.FEHLER_ZAHL_ZU_GROSS, "lp.error.zahlzugross");
			put(EJBExceptionLP.FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN,
					"lp.error.seriennummernbuchungmusseinssein");
			put(EJBExceptionLP.ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH,
					"lp.error.wechsellagerbewirtschaftetnichtmoeglich");
			put(EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN,
					"lp.error.darfmirnichtselbstzuordnen");
			put(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
					"lp.error.mandantparameterfehlt");
			put(EJBExceptionLP.FEHLER_PARTNER_LKZ_AENDERUNG_NICHT_MOEGLICH,
					"part.error.lkzdarfnichtmehrgeaendertwerden");
			put(EJBExceptionLP.ARTIKEL_WECHSEL_CHARGENNUMMERNTRAGEND_NICHT_MOEGLICH,
					"lp.error.wechselchargennummerntragendnichtmoeglich");
			put(EJBExceptionLP.ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH,
					"lp.error.wechselseriennummerntragendnichtmoeglich");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT,
					"lp.error.mehrfacheskommt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT,
					"lp.error.mehrfachesgeht");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_OHNE_KOMMT,
					"lp.error.gehtohnekommt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT,
					"lp.error.taetigkeitvorkommt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_FEHLT,
					"lp.error.gehtfehlt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN,
					"zeiterfassung.sondertaetigkeitbeenden");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_RELATIVE_NICHT_MOEGLICH,
					"zeiterfassung.relativenichtmoeglich");
			put(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"lp.error.fehlerbeifindbyprimarykey");
			put(EJBExceptionLP.WARNUNG_99_KUNDEN_PRO_BUCHSTABE,
					"part.kunde.overflow");
			put(EJBExceptionLP.FEHLER_BEIM_UPDATE, "lp.error.fehlerbeiupdate");
			put(EJBExceptionLP.FEHLER_FLR, "lp.error.fehlerbeiflr");
			put(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED,
					"lp.error.fehlerisalreadylocked");
			put(EJBExceptionLP.FEHLER_LOCK_NOTFOUND,
					"lp.error.fehlerlocknotfound");
			put(EJBExceptionLP.FEHLER_TRANSAKTION_NICHT_DURCHGEFUEHRT__ROLLBACK,
					"lp.error.rollback");
			put(EJBExceptionLP.FEHLER_KEINE_VERBINDUNG_ZUM_JBOSS,
					"lp.error.keineverbindungzumjboss");
			put(EJBExceptionLP.FEHLER_DRUCKEN_FALSCHE_VERSION,
					"lp.error.falscheversion");
			put(EJBExceptionLP.FEHLER_PERSONAL_DUPLICATE_AUSWEIS,
					"pers.error.doppelteausweisnummern");
			put(EJBExceptionLP.FEHLER_AUSWEISNUMMER_ZESTIFT,
					"pers.error.stiftzuordnung");
			put(EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DATEN_ZU_DRUCKEN,
					"lp.hint.nopages");
			put(EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
					"lp.error.reportnichtgefunden");
			put(EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT,
					"lp.error.fehlerimreport");
			put(EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND,
					"lp.error.reportfilenotfound");
			put(EJBExceptionLP.FEHLER_FORMAT_NUMBER, "lp.error.belegwerte");
			put(EJBExceptionLP.FEHLER_NULLPOINTEREXCEPTION, "lp.error.fatal");
			put(EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					"lp.error.mahnstufe123darfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FINANZ_MAHNTEXTE_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					"lp.error.mahntext123darfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH,
					"lp.error.seriennummernmengeungleich");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_ZIFFERNTEIL_UNGLEICH,
					"lp.error.seriennummernziffernteilungleich");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_PREFIX_UNGLEICH,
					"lp.error.seriennummernprefixungleich");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MUSS_MIT_ZIFFERNTEIL_ENDEN,
					"lp.error.seriennummermussmitziffernteilenden");
			put(EJBExceptionLP.FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
					"artikel.error.hauptlagerbeimandant");
			put(EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
					"finanz.error.mahnungenausmahnlaufgedruckt");
			put(EJBExceptionLP.FEHLER_RECHNUNG_MAHNSPERRE,
					"rechnung.error.rechnunginmahnsperre");
			put(EJBExceptionLP.FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE,
					"rechnung.error.mahnstufegroesser");
			put(EJBExceptionLP.LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT,
					"artikel.error.keinupdateaufartikelmoeglich");
			put(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
					"lp.drucken.fehlerbeimerstellen");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN,
					"finanz.error.keinemahnstufeeingetragen");
			put(EJBExceptionLP.FEHLER_GEHT_VOR_ENDE, "lp.error.gehtvorende");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_LAGERENTNAHME_DARF_NICHT_GELOESCHT_WERDEN,
					"fert.error.lagerentnahmedarfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN,
					"fert.error.darffuermateriallistenichtdurchgefuehrtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_AUS_STUECKLISTE_DARF_NICHT_GELOESCHT_WERDEN,
					"fert.error.materialausstuecklistedarfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_POSITION_AUS_ARBEITSPLAN_DARF_NICHT_GELOESCHT_WERDEN,
					"fert.error.positionausarbeitsplandarfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_LOSABLIEFERUNG_FEHLER_MENGE,
					"fert.losablieferung.error.update.menge");
			put(EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
					"stkl.error.geraetesnr.mengemusseinssein");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT,
					"los.error.losteilerledigt");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_AUF_DEM_LOS_IST_MATERIAL_AUSGEGEBEN,
					"los.error.bereitsmaterialausgegben");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN,
					"los.error.losausgegeben");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT,
					"los.error.losgestoppt");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_STUECKLISTE_ARBEITSPLAN_WURDE_GEAENDERT,
					"stkl.error.arbeitsplaninstuecklisteveraendert");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_STUECKLISTE_MATERIAL_WURDE_GEAENDERT,
					"stkl.error.stuecklisteveraendert");
			put(EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN,
					"lp.error.keinrechtzumaktivieren");
			put(EJBExceptionLP.FEHLER_GERAETESNR_BEREITS_ZUGEBUCHT,
					"artikel.error.geraetesnrbereitsgebucht");
			put(EJBExceptionLP.FEHLER_ARTIKEL_IST_NICHT_LAGERBEWIRTSCHAFTET,
					"artikel.error.artikelnichtlagerbewirtschaftet");
			put(EJBExceptionLP.ARTIKEL_DEADLOCK,
					"artikel.error.artieklinuebergeordnetemartikel");
			put(EJBExceptionLP.FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK,
					"artikel.error.ersatzartikeldeadlock");
			put(EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN,
					"lp.error.warenzugangnichtstorniert");
			put(EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT,
					"bes.bestellvorschlagerstellengesperrt");
			put(EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT,
					"zeiterfassung.error.relativezeitbuchungnichtmoeglich");
			put(EJBExceptionLP.FEHLER_RECHNUNG_WERT_DARF_NICHT_NEGATIV_SEIN,
					"rech.error.wertdarfnichtkleinernullsein");
			put(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_INS_NAECHSTE_GJ_DATIERT_WERDEN,
					"error.vordatiereninspaeteresgeschaeftsjahr");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_ES_IST_NOCH_MATERIAL_AUSGEGEBEN,
					"fert.error.nochmaterialausgegeben");
			put(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_INS_VORLETZTE_GJ_DATIERT_WERDEN,
					"error.vordatiereninzurueckliegendesgeschaeftsjahr");
			put(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_BILANZ,
					"finanz.error.zykel.bilanzkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_SKONTO,
					"finanz.error.zykel.skontokonto");
			put(EJBExceptionLP.FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN,
					"auft.error.abruf.aendern");
			put(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_UST,
					"finanz.error.zykel.ustkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM,
					"finanz.error.belegausserhalbgueltigemzeitraum");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_HAT_KEINE_MENGENPOSITIONEN,
					"bes.warning.keinemengenbehaftetenpositionen");
			put(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
					"lp.error.belegdarfnichtinanderesgjumdatiertwerden");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
					"lp.error.ungueltigeemailadresse");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN,
					"fert.losgroesseaendern.error");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_FAXNUMMER,
					"lp.error.ungueltigefaxnummer");
			put(EJBExceptionLP.FEHLER_SERIENNUMMER_MUSS_UEBER_ALLE_ARTIKEL_EINDEUTIG_SEIN,
					"artikel.error.seriennumereindeutig");
			put(EJBExceptionLP.FEHLER_KUNDE_STANDARDPREISLISTE_HAT_FALSCHE_WAEHRUNG,
					"lp.error.falschewaehrung.kundebeleg");
			put(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT,
					"lp.error.endsummeexistiert");
			put(EJBExceptionLP.FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION,
					"lp.error.endsummeverschieben");
			put(EJBExceptionLP.FEHLER_RECHNUNG_LIEFERSCHEIN_MUSS_IM_SELBEN_GESCHAEFTSJAHR_LIEGEN,
					"rech.error.lieferscheinundrechnungimgleichengj");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_ZEITEINGABE,
					"lp.error.ungueltigezeiteingabe");
			put(EJBExceptionLP.FEHLER_ZUTRITTSOBJEKT_VERWENDUNGSUEBERSCHREITUNG,
					"lp.error.verwendungsueberschreitung");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_ZAHLENEINGABE,
					"lp.error.ungueltigezahleneingabe");
			put(EJBExceptionLP.FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH,
					"artikel.error.mehrerelagerplaetzeprolagernichtmoeglich");
			put(EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
					"fb.error.belegbereitsverbucht");
			put(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_UVAVERPROBUNG,
					"fb.error.buchungnichterlaubtuva");
			put(EJBExceptionLP.FEHLER_GUTSCHRIFT_WECHSEL_WERTGUTSCHRIFT_FEHLER,
					"rech.error.wechselwertgutschrift");
			put(EJBExceptionLP.WARNUNG_KUNDEN_UID_NUMMER_NICHT_HINTERLEGT,
					"lp.error.kundenuidnummerfehlt");
			put(EJBExceptionLP.FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT,
					"part.error.nurkommunikationsdatendeseigenenmandantenaendern");
			put(EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINE_POSITIONEN,
					"rech.keinepositionen");
			put(EJBExceptionLP.FEHLER_ZUVIELE_LAGERORTE,
					"lp.error.zuvielelagerorte");
			put(EJBExceptionLP.FEHLER_CUD_TAETIGKEIT_TELEFON_NICHT_ERLAUBT,
					"zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden");
			put(EJBExceptionLP.FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN,
					"artikel.error.inventur.lager");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_TEXTINKONZERNDATENSPRACHENICHTHINTERLEGT,
					"ls.warning.textkonzerndatensprache");
			put(EJBExceptionLP.FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT,
					"vkpf.error.datumsbereich");
			put(EJBExceptionLP.FEHLER_UNZUREICHENDE_RECHTE,
					"lp.unzureichenderechte");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
					"rechnung.zahlung.rechnungiststorniert1");
			put(EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT,
					"lp.drucken.keindruckerinstalliert");
			put(EJBExceptionLP.FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER,
					"lp.error.lagerbuchung.keineberechtigung");
			put(EJBExceptionLP.FEHLER_NOCLASSDEFFOUNDERROR,
					"lp.error.noclassdeffounderror");
			put(EJBExceptionLP.FEHLER_THECLIENT_WURDE_GELOESCHT,
					"lp.error.theclientgeloescht");
			put(EJBExceptionLP.FEHLER_BELEG_IST_BEREITS_AKTIVIERT,
					"lp.error.belegistbereitsaktiviert");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN,
					"lp.error.bespos.artikelnichtmehraenderbar");
			put(EJBExceptionLP.FEHLER_ARTIKELSET_KANN_NICHT_VERSCHOBEN_WERDEN,
					"lp.error.artikelset.kannnichtverschobenwerden");
			put(EJBExceptionLP.FEHLER_ARTIKEL_KANN_NICHT_IN_ARTIKELSET_VERSCHOBEN_WERDEN,
					"lp.error.artike.kannnichtinartikelsetverschobenwerden");
			put(EJBExceptionLP.ARTIKEL_ANZAHLCHARGENNUMMERNNICHTKORREKT,
					"lp.error.anzahlchargennummernnichtkorrekt");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_PARTNER_UST_LKZ_UNGLEICH_FINANZAMT_LKZ,
					"fb.error.partnerlkzungleichfinanzamtlkz");
			put(EJBExceptionLP.FEHLER_SERIENCHARGENNUMMER_ENTHAELT_NICHT_ERLAUBTE_ZEICHEN,
					"artikel.error.snrchnrungueltig");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_FEHLER_ZEITDATEN,
					"zeiterfassung.error.fehler");
			put(EJBExceptionLP.FEHLER_ABBUCHUNG_SNRCHNR_ABGEBROCHEN,
					"fert.error.ausgabeabgebrochen");
			put(EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
					"bestellung.fehler.keinemailadressedefiniert");
			put(EJBExceptionLP.FEHLER_TRANSACTION_TIMEOUT,
					"lp.transaktiontimeout");
			put(EJBExceptionLP.FEHLER_FEHLER_BEIM_DRUCKEN, "lp.druckerfehler");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEINE_IN_VERSCHIEDENE_LAENDER,
					"rech.error.verschiedenelieferscheinlaender");
			put(EJBExceptionLP.FEHLER_JCR_KNOTEN_EXISTIERT_BEREITS,
					"jcr.error.knotenvorhanden");
			put(EJBExceptionLP.FEHLER_JCR_DATEI_KONNTE_NICHT_GELESEN_WERDEN,
					"jcr.error.fehlerbeimlesen");
			put(EJBExceptionLP.FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT,
					"jcr.error.nichtgespeichert");
			put(EJBExceptionLP.FEHLER_JCR_KEINE_AUFTRAEGE_ZU_KOPIEREN,
					"jcr.error.nichtszukopieren");
			put(EJBExceptionLP.FEHLER_KEIN_EIGENTUMSVORBEHALT_DEFINIERT,
					"auftrag.eigentumsvorbehalt.definieren");
			put(EJBExceptionLP.FEHLER_KEINE_LIEFERBEDINGUNGEN_DEFINIERT,
					"auftrag.lieferbedingungen.definieren");
			put(EJBExceptionLP.FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH,
					"lp.error.steuersaetzeungleich");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_INTERNE_BESTELLUNG_ZU_VIELE_UNTERSTUECKLISTEN,
					"fert.internebestellung.maximaleStuecklistentiefe.erreicht");
			put(EJBExceptionLP.FEHLER_MENGENAENDERUNG_UNTER_ABGERUFENE_MENGE_NICHT_ERLAUBT,
					"auft.fehler.mengenaenderungunterabrufmenge");
			put(EJBExceptionLP.FEHLER_ARTIKELAENDERUNG_BEI_RAHMENPOSUPDATE_NICHT_ERLAUBT,
					"auft.fehler.artikeliidnichtaenderbar");
			put(EJBExceptionLP.FEHLER_RECHNUNG_FAELLIGKEIT_NICHT_BERECHENBAR,
					"rech.fehler.faelligkeitnichtberechenbar");
			put(EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH,
					"fert.stueckliste.warnung.setartikel");
			put(EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN,
					"fert.stueckliste.warnung.darfkeinestuecklisteenthalten");
			put(EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT,
					"lp.default.az.nichtdinfiniert");
			put(EJBExceptionLP.FEHLER_RECHNUNG_NOCH_NICHT_AKTIVIERT,
					"rech.fehler.nochnichtaktiviert");
			put(EJBExceptionLP.FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE,
					"rech.unterschiedliche.mwstsaetze");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR,
					"bes.error.nurkopfartikel.zubuchbar");
			put(EJBExceptionLP.FEHLER_FINANZ_STORNIEREN_NICHT_MOEGLICH,
					"finanz.stornieren.error.bereitsverwendet");
			put(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT,
					"lp.error.rundungsartikelnichtdefiniert");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS,
					"rech.error.zwsvonkleinerbis");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH,
					"rech.error.zwsmwstsatzunterschiedlich");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF,
					"fert.losnummerberech.ueberlauf");
			put(EJBExceptionLP.FEHLER_KEINE_ANZAHLUNGEN_VORHANDEN,
					"rech.schlussrechnung.keineanzahlungen");
			put(EJBExceptionLP.FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT,
					"er.zusatzkosten.fehler.wiederholungerledigt");
			put(EJBExceptionLP.FEHLER_FINANZ_UVA_AUF_GANZES_JAHR_NICHT_ERLAUBT,
					"finanz.error.uva.aufganzesjahrnichterlaubt");
			put(EJBExceptionLP.FEHLER_RAHMENAUFTRAG_IST_IM_STATUS_ANGELEGT,
					"auft.rahmenauftrag.statuswechsel.angelegtteilerledigt.ungueltig");
			put(EJBExceptionLP.FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT,
					"er.zusatzkosten.fehler.wiederholungerledigt");
			put(EJBExceptionLP.FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN,
					"lp.error.zusammenfuehren.partner.unterschiedliche.debitoren");
			put(EJBExceptionLP.FEHLER_LIEFERANT_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_KREDITOREN,
					"lp.error.zusammenfuehren.partner.unterschiedliche.kreditoren");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					"lp.error.zusammenfuehren.partner.unterschiedliche.mwst");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_LKZ,
					"lp.error.zusammenfuehren.partner.unterschiedliche.lkz");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_ABW_UST,
					"lp.error.zusammenfuehren.partner.unterschiedliche.abwustland");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_UID,
					"lp.error.zusammenfuehren.partner.unterschiedliche.uid");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_BANKVERBINDUNG_IN_FIBU_VORHANDEN,
					"lp.error.partner.zusammenfuehren.bankverbindung");
			put(EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN,
					"artikel.werbeabgabepflicht.darfnichtlagerbewirtschaftetsein");
			put(EJBExceptionLP.FEHLER_INSERAT_EIN_KUNDE_MUSS_VORHANDEN_SEIN,
					"iv.inseratkunde.loeschennichtmoeglich");
			put(EJBExceptionLP.FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN,
					"artikel.lieferant.einzelpreis.loeschen.error");
			put(EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS,
					"zeiterfassung.error.buchungzwischenvonbisvorhanden");
			put(EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT,
					"zeiterfassung.error.einfuegenzwischenvonbisnichterlaubt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_AUF_FERTIGEN_ARBEITSGANG_NICHT_MOEGLICH,
					"fert.zeitbuchung.ag.fertig.error");
			put(EJBExceptionLP.FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN,
					"artikel.error.bestellmengeneinheiten.bereitsvorhanden");

			put(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT,
					"part.ansprechpartner.email.error1");
			put(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG,
					"part.ansprechpartner.email.error");

			put(EJBExceptionLP.FEHLER_PJ18612_BENUTZER_MUSS_AN_MANDANT_002_ANGEMELDET_SEIN,
					"lp.error.pj18612");

			put(EJBExceptionLP.FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN,
					"proj.error.zeitdatenvorhanden");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_PROJEKT_NICHT_MOEGLICH,
					"proj.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_INTERN_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH,
					"proj.error.zeitbuchungnichtmoeglich.internerledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ANGELEGTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.angelegt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH,
					"proj.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_ANGEBOT_NICHT_MOEGLICH,
					"angb.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_ANGEBOT_NICHT_MOEGLICH,
					"angb.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_GESTOPPTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.gestoppt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTER_AUFTRAG_NICHT_MOEGLICH,
					"auft.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTER_AUFTRAG_NICHT_MOEGLICH,
					"auft.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ANDERN_MANDANT_NACHFUELLEN_MANDANT_KEIN_KUNDE,
					"ls.fuelle.fehlmengen.anderesmandanten.nach.error.mandantkeinkunde");
			put(EJBExceptionLP.FEHLER_LIEFERADRESSE_NUR_AENDERBAR_WENN_KEINE_PREISE_ERFASST,
					"bes.readresse.aendern.error");
			put(EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT,
					"auft.mandant.hauptlager_fehlt");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_HAT_KEINEN_AUFTRAGSBEZUG,
					"ls.error.keinauftragsbezug") ;
			put(EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC, "rech.fehler.gleichermwstsatznotwendigbeireversecharge") ;
		}
	};

	private static Map<Integer, String> clientInfoErrorMsgs = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(EJBExceptionLP.FEHLER_KONTIERUNG_ZUGEORDNET,
					"fb.buchungsjournal.export.kontierungaufsteuerkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE,
					"finanz.error.keinmwstcode");
			put(EJBExceptionLP.FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
					"rech.anzahlung.schlussrechnungbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
					"rech.schlussrechnung.bereitsvorhanden");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT,
					"fb.error.keinanzahlungskontodefiniert");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT,
					"fb.error.keinesteuerkategoriehinterlegt");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
					"fb.error.keinesteuerkategoriehinterlegt");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
					"fb.error.keinesteuerkategoriereversehinterlegt");
			put(EJBExceptionLP.FEHLER_AR_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND,
					"rech.error.anzahlungreversechargeabweichend");
			put(EJBExceptionLP.FEHLER_ER_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND,
					"er.error.anzahlungreversechargeabweichend");
			put(EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG,
					"lp.error.artikelnrzulang");
			put(EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN,
					"artikel.error.verpackungseanbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN,
					"artikel.error.verkaufseanbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
					"los.error.losbereitserledigt");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
					"los.error.losnichtausgegeben");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
					"los.error.losstorniert");
			put(EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT,
					"system.error.keindivisorfuereinheit");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_LOS_OHNE_KUNDE,
					"los.error.losohnekunde");
			put(EJBExceptionLP.STUECKLISTE_DEADLOCK,
					"stkl.error.artikelinuebergeordneterstueckliste");
			put(EJBExceptionLP.FEHLER_CHARGENNUMMER_ZU_KURZ,
					"artikel.error.chnrzukurz");
			put(EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_KURZ,
					"artikel.error.snrzukurz");
			put(EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_LANG,
					"artikel.error.snrzulang");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_EINS,
					"rech.error.zwsvonkleinereins");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_BIS_KLEINER_VON,
					"rech.error.zwsbiskleinervon");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_POSITIONSNUMMER,
					"rech.error.zwspositionsnummer");
			put(EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG,
					"system.error.fehlerinflrvorlage");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_IN_PROFORMARECHNUNG_DOPPELT_VERRECHNET,
					"rech.error.lsinproformadoppeltverrechnet");
			put(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG,
					"fb.error.zahlungvorauszahlungsbetrag");
			put(EJBExceptionLP.FEHLER_JCR_ROOT_EXISTIERT_NICHT,
					"lp.error.jcrrootnode");
			put(EJBExceptionLP.FEHLER_RECHNUNG_GS_AUF_ANZAHLUNG_NICHT_MOEGLICH,
					"rech.error.gsaufanzahlungnichtmoeglich.wennfibu");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ENTHAELT_VERKETTETE_LIEFERSCHEINE,
					"ls.stornieren.nichtmoeglich.enthaelt.verkettete");

		}
	};

	private static Map<String, IErrorAction> clientInfoErrorActionMsgs = new HashMap<String, IErrorAction>() {
		private static final long serialVersionUID = 1L;

		{
			put("bes_bsmahnung", new BestellpositionMahnungError());
		}
	};

	private static Map<Integer, String> argumentMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN,
					"re.error.lsmussgeliefertsein");
			put(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_AUF_MITLAUFENDES_KONTO_NICHT_ERLAUBT,
					"fb.error.buchungaufmitlaufendeskontonichtmoeglich");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET,
					"fb.error.kontoinsteuerkategorieinanderermwstvorhanden");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR,
					"fb.error.keinekontonummerfuerbereichverfuegbar");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTONUMMER_AUSSERHALB_DEFINITION,
					"fb.error.kontonummerausserhalbdeserlaubtenbereiches");
			put(EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG,
					"lp.error.zwsunvollstaendig");
			put(EJBExceptionLP.FEHLER_FINANZ_GESCHAEFTSJAHR_EXISTIERT_NICHT,
					"finanz.error.geschaeftsjahrexistiertnicht");
		}
	};

	private static Map<Integer, String> causeMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT,
					"finanz.error.debitorenkontonichtdefiniert");
			put(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					"finanz.error.exportformatnichtimplementiert");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT,
					"finanz.error.keinerloeskonto");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO,
					"finanz.error.keinkursverlustkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
					"finanz.error.keinkursgewinnkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_STEUERKONTO,
					"finanz.erorr.keinsteuerkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_SKONTOKONTO,
					"finanz.error.keinskontokonto");
			put(EJBExceptionLP.FEHLER_SCRIPT_NICHT_GEFUNDEN,
					"pers.reisezeiten.kein.script.gefunden");
			put(EJBExceptionLP.FEHLER_SCRIPT_NICHT_AUSFUEHRBAR,
					"pers.reisezeiten.script.nicht.ausfuehrbar") ;
		}
	};

	private static Map<Integer, String> exMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(EJBExceptionLP.FEHLER_HTTP_POST_IO, "lp.error.http.postio");
			put(EJBExceptionLP.FEHLER_KEYSTORE_MANAGMENT,
					"lp.error.keystore.managment");
			put(EJBExceptionLP.FEHLER_KEYSTORE_RECOVER,
					"lp.error.keystore.recover");
			put(EJBExceptionLP.FEHLER_KEYSTORE_ALGORITHMEN,
					"lp.error.keystore.algorithmen");
			put(EJBExceptionLP.FEHLER_KEYSTORE_CERTIFICATE,
					"lp.error.keystore.certificate");
			put(EJBExceptionLP.FEHLER_KEYSTORE, "lp.error.keystore");
		}
	};

	static private String getMsgFehlerBeimLoeschen(ExceptionLP ec) {
		List<?> info = ec.getAlInfoForTheClient();
		String msg = null;
		if (info != null && info.size() > 1 && info.get(1) instanceof String) {
			IErrorAction action = clientInfoErrorActionMsgs.get(info.get(1));
			if (action != null) {
				msg = action.getMsg(info);
			}
		}
		if (msg == null) {
			msg = LPMain.getTextRespectUISPr("lp.hint.loeschen");
			if (info != null && info.size() > 0) {
				msg = msg + "\n\n" + ec.getAlInfoForTheClient().get(0);
			}
		}
		return msg;
	}

	protected String getMsgWithoutInfo(ExceptionLP ec) {
		String simpleMsg = simpleErrorMsgs.get(ec.getICode());
		if (null == simpleMsg)
			return simpleMsg;

		return LPMain.getTextRespectUISPr(simpleMsg);
	}

	protected String getMsgWithClientInfo0(ExceptionLP ec) {
		String simpleMsg = clientInfoErrorMsgs.get(ec.getICode());
		if (null == simpleMsg)
			return simpleMsg;

		String sMsg = LPMain.getTextRespectUISPr(simpleMsg);
		ArrayList<?> al = ec.getAlInfoForTheClient();
		if (al != null && al.size() > 0) {
			sMsg += " (" + al.get(0) + ")";
		}
		return sMsg;
	}

	protected String getMsgWithCauseMessage(ExceptionLP ec) {
		String errorToken = causeMessages.get(ec.getICode());
		if (errorToken == null)
			return errorToken;

		String sMsg = LPMain.getTextRespectUISPr(errorToken);
		sMsg = sMsg + "\n(" + ec.getCause().getMessage() + ")";
		return sMsg;
	}

	protected String getMsgWithArguments(ExceptionLP ec) {
		String errorToken = argumentMessages.get(ec.getICode());
		if (errorToken == null)
			return errorToken;

		return LPMain.getMessageTextRespectUISPr(errorToken, ec
				.getAlInfoForTheClient().toArray());
	}

	protected String getMsgWithExceptionMessage(ExceptionLP ec) {
		String errorToken = exMessages.get(ec.getICode());
		if (errorToken == null)
			return errorToken;

		String sMsg = LPMain.getTextRespectUISPr(errorToken);
		sMsg = sMsg + "\n(" + ec.getLocalizedMessage() + ")";
		return sMsg;
	}

	public String getMsg(ExceptionLP ec) {
		int iCode = ec.getICode();
		String sMsg = null;

		// Einfach Info "Errorcode -> Text"
		sMsg = getMsgWithoutInfo(ec);
		if (null != sMsg)
			return sMsg;

		// ErrorCode -> Text + ClientInfo.get(0)
		sMsg = getMsgWithClientInfo0(ec);
		if (null != sMsg)
			return sMsg;

		sMsg = getMsgWithExceptionMessage(ec);
		if (null != sMsg)
			return sMsg;

		sMsg = getMsgWithCauseMessage(ec);
		if (null != sMsg)
			return sMsg;

		sMsg = getMsgWithArguments(ec);
		if (null != sMsg)
			return sMsg;

		switch (iCode) {
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_CHN_SNR_ZU_WENIG_AUF_LAGER: {
			sMsg = LPMain
					.getTextRespectUISPr("lager.error.mengenreduzierungnichtmoeglich");
			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += LPMain.getTextRespectUISPr("lp.gewuenschtemenge")
						+ " (" + al.get(0) + ") ";
				if (al.size() == 3)
					sMsg += LPMain
							.getTextRespectUISPr("ls.warning.vorhandenemengemitcharge")
							+ al.get(2) + " (" + al.get(1) + ")";
				if (al.size() == 2)
					sMsg += LPMain
							.getTextRespectUISPr("ls.warning.vorhandenemenge")
							+ "(" + al.get(1) + ")";
			}
			break;
		}
		case EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.zuwenigauflager");
			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {

				sMsg += " "
						+ LPMain.getTextRespectUISPr("artikel.artikelnummer");

				sMsg += ": " + al.get(0);

				// SP1943 Wenn SnrChnr angegeben

				if (al.size() > 1) {
					sMsg += " ("
							+ LPMain.getTextRespectUISPr("lp.seriennrchargennr")
							+ ": " + al.get(1) + ")";
				}

			}
			break;
		}
		case EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT: {
			sMsg = LPMain
					.getTextRespectUISPr("lp.error.zeicheninartikelnummernichterlaubt");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(1) + " '"
						+ ec.getAlInfoForTheClient().get(0) + "')";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_SERIENNUMMER_ENTHAELT_NICHT_NUMERISCHE_ZEICHEN: {
			sMsg = LPMain
					.getTextRespectUISPr("lp.error.zeicheninsnrnummernichterlaubt");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(1) + " '"
						+ ec.getAlInfoForTheClient().get(0) + "')";
			}
			break;
		}
		case EJBExceptionLP.FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN: {
			sMsg = LPMain
					.getTextRespectUISPr("fert.sollarbeitsplan.error.hilsstueckliste");
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(0) + ")";
			}
			break;
		}

		case EJBExceptionLP.FEHLER_PERSONAL_ZEICHEN_IN_PERSONALNUMMER_NICHT_ERLAUBT: {
			sMsg = LPMain
					.getTextRespectUISPr("lp.error.zeicheninpersonalnummernichterlaubt");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(1) + " '"
						+ ec.getAlInfoForTheClient().get(0) + "')";
			}

			break;
		}

		case EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG: {
			sMsg = LPMain
					.getTextRespectUISPr("fert.los.materialvollstaendig.error");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (Los: " + ec.getAlInfoForTheClient().get(0)
						+ ") ";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.artikelnrzukurz");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (Mindestens "
						+ ec.getAlInfoForTheClient().get(0) + " Stellen | "
						+ ec.getAlInfoForTheClient().get(1) + ") ";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT: {
			sMsg = LPMain
					.getTextRespectUISPr("system.error.keindivisorfuereinheit");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + ": " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_IST_VERKETTET: {
			sMsg = LPMain
					.getTextRespectUISPr("ls.stornieren.nichtmoeglich.istverkettet");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + ": " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_IST_BEREITS_VERKETTET: {
			sMsg = LPMain
					.getTextRespectUISPr("ls.verketten.nichtmoeglich.istverkettet");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_PARTNERART_DARF_NICHT_GELOESCHT_GEAENDERT_WERDEN: {
			sMsg = LPMain
					.getTextRespectUISPr("part.error.partnerart.darf.nicht.geloeschtwerden");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM: {
			sMsg = LPMain
					.getTextRespectUISPr("lp.error.personalfehlerbeieintrittsdatum");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				if (al.get(0) instanceof Integer) {

					Integer personalIId = (Integer) al.get(0);

					try {
						String sZusatz = new PersonalDelegate()
								.personalFindByPrimaryKey(personalIId)
								.formatAnrede();
						sMsg += " " + sZusatz;

					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			}

			break;
		}

		case EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT: {
			sMsg = LPMain
					.getTextRespectUISPr("artikel.preispflege.import.fehler");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				if (al.get(0) instanceof String) {

					sMsg += " " + al.get(0);

				}
			}

			break;
		}

		case EJBExceptionLP.FEHLER_IMPORT_NX_STKL_LAENGE_UNTERSCHIEDLICH: {
			sMsg = LPMain
					.getTextRespectUISPr("stkl.importnx.laengeunterschiedlich");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " ("
						+ ((ArtikelDto) al.get(0)).formatArtikelbezeichnung()
						+ ")";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_IMPORT_NX_STKL_UNTERSCHIEDLICH: {
			sMsg = LPMain
					.getTextRespectUISPr("stkl.importnx.stklposunterschiedlich");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				sMsg += " (Stkl: "
						+ ((ArtikelDto) al.get(0)).formatArtikelbezeichnung()
						+ ", Artikel: "
						+ ((ArtikelDto) al.get(1)).formatArtikelbezeichnung()
						+ ")";
			}

		}
			break;
		case EJBExceptionLP.FEHLER_SONDERZEITENIMPORT_DOPPELTER_EINTRAG: {
			sMsg = LPMain
					.getTextRespectUISPr("pers.sondrzeitenimport.error.doppelt");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += al.get(0);
			}

		}
			break;
		case EJBExceptionLP.FEHLER_IN_ZEITDATEN: {
			try {
				ArrayList<?> al = ec.getAlInfoForTheClient();
				String s = "";
				if (al != null && al.size() > 1) {
					if (al.get(0) instanceof Integer) {
						PersonalDto personalDto = DelegateFactory.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey((Integer) al.get(0));
						s += " ("
								+ personalDto.getCPersonalnr()
								+ " "
								+ personalDto.getPartnerDto()
										.formatFixName2Name1();
						// s += " (" + personalDto.getPartnerDto().
						// formatFixAnredeTitelName2Name1();
					}
					if (al.get(1) instanceof java.sql.Timestamp) {
						s += ", "
								+ Helper.formatDatum((java.sql.Timestamp) al
										.get(1), LPMain.getTheClient()
										.getLocUi()) + ")";
					}

				}
				sMsg = LPMain
						.getTextRespectUISPr("pers.error.fehlerinzeitdaten")
						+ s;
			} catch (Throwable ex) {
				ex.printStackTrace();
			}

			break;
		}

		case EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT: {
			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("lp.error.keinwechselkurshinterlegt"));

			Locale locUi = null;
			String cNrWaehrungVon = null;

			try {
				locUi = LPMain.getTheClient().getLocUi();
				cNrWaehrungVon = LPMain.getTheClient().getSMandantenwaehrung();
			} catch (Throwable t2) {
				// @todo boese, boese PJ 5379
			}

			mf.setLocale(locUi);

			ArrayList<?> al = ec.getAlInfoForTheClient();
			Object[] pattern = null;
			if (al != null && al.size() > 0) {
				pattern = new Object[] { cNrWaehrungVon, (String) al.get(0) };
			} else {
				pattern = new Object[] { cNrWaehrungVon };
			}

			sMsg = mf.format(pattern);
			break;
		}

		case EJBExceptionLP.FEHLER_PERSONAL_URLAUBSBERECHNUNG_ZU_DATUM_KEINE_SOLLZEIT_DEFINIERT: {
			sMsg = LPMain
					.getTextRespectUISPr("personal.error.keinesollzeitzudatum")
					+ " " + ec.getMessage();
			break;
		}
		case EJBExceptionLP.FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER: {
			sMsg = LPMain.getTextRespectUISPr("fb.mehrerefinanzaemter");
			sMsg += "\n(" + ec.getCause().getMessage() + ")";
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_LF_HAT_KEINE_BANKVERBINDUNG: {
			sMsg = LPMain
					.getTextRespectUISPr("fb.lieferanthatkeinebankverbindung");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_BANK_HAT_KEINEN_ORT: {
			sMsg = LPMain.getTextRespectUISPr("fb.bankhatkeinenort");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}

		case EJBExceptionLP.FEHLER_STORNIEREN_ANZAHLUNG_SCHLUSSRECHNUNG_VORHANDEN: {
			sMsg = LPMain
					.getTextRespectUISPr("rech.anhzahlung.stornieren.error")
					+ " ";
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + item + "\n";
			}
			break;
		}

		case EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN: {
			sMsg = LPMain.getTextRespectUISPr("stkl.import.fehler.allgemein");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_BEREITS_IN_PROFORMARECHNUNG: {
			sMsg = LPMain
					.getTextRespectUISPr("rech.error.ls.bereitsin.proformarechnung");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}

		case EJBExceptionLP.FEHLER_LOSGUTSCHLECHT_VORHANDEN: {
			sMsg = LPMain
					.getTextRespectUISPr("pers.zeiterfassung.error.buchungnichtloeschbar");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINEN_WERT: {
			sMsg = LPMain.getTextRespectUISPr("rechnung.error.keinwert");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter
					.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH: {
			sMsg = LPMain
					.getTextRespectUISPr("lager.error.mengenreduzierungnichtmoeglich");
			sMsg += "\n "
					+ LPMain.getTextRespectUISPr("label.ident")
					+ ": "
					+ ec.getAlInfoForTheClient().get(0)
					+ ", "
					+ LPMain.getTextRespectUISPr("artikel.lager.bereitsverbraucht")
					+ ": " + ec.getAlInfoForTheClient().get(1);
			break;
		}

		case EJBExceptionLP.FEHLER_RECHNUNG_ZAHLUNGSZIEL_KEINE_TAGE: {
			sMsg = LPMain
					.getTextRespectUISPr("rechnung.error.zahlungszielhatkeinetage");
			sMsg += ":\n" + LPMain.getTextRespectUISPr("label.zahlungsziel")
					+ ": " + ec.getAlInfoForTheClient().get(0) + "\n"
					+ LPMain.getTextRespectUISPr("lp.rechnung.modulname")
					+ ": " + ec.getAlInfoForTheClient().get(1);
			break;
		}

		case EJBExceptionLP.FEHLER_RECHNUNG_POSITIONLS_EXISTIERT: {
			RechnungPositionDto rePosDto;
			RechnungDto rechnungDto = null;
			try {
				rePosDto = DelegateFactory
						.getInstance()
						.getRechnungDelegate()
						.rechnungPositionFindByLieferscheinIId(
								(Integer) ec.getAlInfoForTheClient().get(0));
				rechnungDto = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.rechnungFindByPrimaryKey(rePosDto.getRechnungIId());
			} catch (Throwable e) {
				e.printStackTrace();
			}

			sMsg = LPMain
					.getTextRespectUISPr("rechnung.error.lieferscheinpositionexistiert")
					+ ": " + rechnungDto.getCNr();
			break;
		}

		case EJBExceptionLP.FEHLER_BESTELLUNG_FALSCHER_STATUS: {
			sMsg = LPMain
					.getTextRespectUISPr("bestellung.error.falscherstatus")
					+ " " + ec.getAlInfoForTheClient().get(2) + "\n";
			sMsg = sMsg
					+ LPMain.getTextRespectUISPr("bestellung.error.statusdurchaenderung")
					+ " " + ec.getAlInfoForTheClient().get(0) + "\n";
			sMsg = sMsg
					+ LPMain.getTextRespectUISPr("bestellung.error.statusnachdaten")
					+ "\n" + ec.getAlInfoForTheClient().get(1) + "\n";
			break;
		}

		case EJBExceptionLP.LAGER_SERIENNUMMER_SCHON_VORHANDEN: {
			if (ec.getAlInfoForTheClient() != null
					&& ec.getAlInfoForTheClient().size() > 0) {
				sMsg = LPMain
						.getTextRespectUISPr("lp.error.seriennummerschonauflager")
						+ ": " + ec.getAlInfoForTheClient().get(0);
			} else {
				sMsg = LPMain
						.getTextRespectUISPr("lp.error.seriennummerschonauflager");
			}

			break;
		}

		case EJBExceptionLP.FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN: {
			sMsg = LPMain
					.getTextRespectUISPr("fert.seriennummerngenerator.error")
					+ ": " + ec.getAlInfoForTheClient().get(0);
			break;
		}
		case EJBExceptionLP.FEHLER_CHARGENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN: {
			sMsg = LPMain.getTextRespectUISPr("fert.chargengenerator.error")
					+ ": " + ec.getAlInfoForTheClient().get(0);
			break;
		}
		case EJBExceptionLP.FEHLER_CHARGENNUMMER_NICHT_NUMERISCH: {
			sMsg = LPMain
					.getTextRespectUISPr("fert.chargennummernichtnumerisch.error")
					+ ": " + ec.getAlInfoForTheClient().get(0);
			break;
		}

		case EJBExceptionLP.FEHLER_BESTELLUNG_WEPOS_PREIS_NOCH_NICHT_ERFASST: {
			sMsg = LPMain.getTextRespectUISPr("bes.error.preisnichterfasst");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " Lieferscheinnummer: " + al.get(0)
						+ ", Positionsnummer: " + al.get(1);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ZEITEN_BEREITS_ABGESCHLOSSEN: {
			sMsg = LPMain
					.getTextRespectUISPr("pers.zeiterfassung.zeitenbereitsabgeschlossen.bis");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {

				try {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("pers.zeiterfassung.zeitenbereitsabgeschlossen.bis"));
					mf.setLocale(LPMain.getTheClient().getLocUi());

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(((java.sql.Timestamp) al.get(0))
							.getTime());
					c.get(Calendar.WEEK_OF_YEAR);
					Object pattern[] = { c.get(Calendar.WEEK_OF_YEAR) };

					sMsg = mf.format(pattern);
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}

			break;
		}
		case EJBExceptionLP.FEHLER_FLRDRUCK_SPALTE_NICHT_VORHANDEN: {
			sMsg = LPMain
					.getTextRespectUISPr("lp.error.flrdruck.spaltenichtvorhanden");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_NICHT_VORHANDEN: {
			sMsg = LPMain
					.getTextRespectUISPr("artikel.werbeabgabepflichtnichtvorhanden");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += ":  Artikelnummer: " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_IN_REISEZEITEN: {
			sMsg = LPMain.getTextRespectUISPr("pers.error.fehlerinreisezeiten");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				sMsg += " " + al.get(0) + ", ";
				try {
					sMsg += Helper.formatDatumZeit((java.util.Date) al.get(1),
							LPMain.getTheClient().getLocUi());
				} catch (Throwable t2) {
					//
				}
			}

			break;
		}

		case EJBExceptionLP.FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT: {
			sMsg = LPMain.getTextRespectUISPr("bes.error.keindokument");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += "\r\n" + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ANZAHLUNGEN_NICHT_BEZAHLT: {
			sMsg = LPMain
					.getTextRespectUISPr("rech.schlussrechnung.anzahlungennichtbezahlt");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += "\r\n" + al.get(0);
			}
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN: {
			sMsg = LPMain
					.getTextRespectUISPr("finanz.error.mahntextnichtgefunden");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {

				sMsg += ":\r\n" + LPMain.getTextRespectUISPr("label.lieferant")
						+ ": " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEINE_MIT_VERSCHIEDENEN_PROJEKTEN: {
			sMsg = LPMain.getTextRespectUISPr("rech.error.verschiede.projekte");

			ArrayList<?> al = ec.getAlInfoForTheClient();
			for (int i = 0; i < al.size(); i++) {

				sMsg += " " + al.get(i);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_GESCHAEFTSJAHR_GESPERRT: {
			Object jahr = null;
			if (ec.getAlInfoForTheClient() != null
					&& ec.getAlInfoForTheClient().size() > 0)
				jahr = ec.getAlInfoForTheClient().get(0);

			sMsg = LPMain.getMessageTextRespectUISPr(
					"finanz.error.geschaeftsjahrgesperrt", jahr);
			break;
		}
		case EJBExceptionLP.FEHLER_ALLE_LOSE_BERUECKSICHTIGEN_UND_SAMMELLIEFERSCHIEN_MEHRERE_AUFTRAEGE: {
			Object auftrag = null;
			if (ec.getAlInfoForTheClient() != null
					&& ec.getAlInfoForTheClient().size() > 0)
				auftrag = ec.getAlInfoForTheClient().get(0);
			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("auftrag.nachkalkulation.allels.andererauftrag.error"));

			Object pattern[] = { auftrag };

			sMsg = mf.format(pattern);
			break;
		}
		case EJBExceptionLP.FEHLER_FERTIGUNG_FERTIGUNGSGRUPPE_SOFORTVERBRAUCH_NICHT_VORHANDEN: {
			sMsg = LPMain
					.getMessageTextRespectUISPr(
							"fert.error.fertigungsgruppe.sofortverbrauch.nicthvorhanden",
							ec.getMessage());
			break;
		}

		case EJBExceptionLP.FEHLER_ARTIKELLIEFERANT_PREIS_IST_NULL: {
			ArrayList<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr(
					"artikel.error.artikellieferant.preisistnull", al.get(0),
					al.get(1));
			break;
		}

		case EJBExceptionLP.FEHLER_BEIM_LOESCHEN: {
			sMsg = getMsgFehlerBeimLoeschen(ec);
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_DATUM_NICHT_LETZTER_TAG_DES_MONATS: {
			sMsg = LPMain.getMessageTextRespectUISPr(
					"finanz.error.buchungsdatum.nichtletztertag",
					ec.getMessage());
			break;
		}

		}

		return sMsg;
	}
}
