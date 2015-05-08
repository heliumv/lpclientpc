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
package com.lp.client.frame.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.jms.LPAsynchSubscriber;

public class DelegateFactory {
	private static DelegateFactory delegateFactory = null;
	private int iZugriffsCounter = 0;

	private DelegateFactory() {
		// Singleton.
	}

	private LieferscheinDelegate lieferscheinDelegate = null;
	private LieferscheinpositionDelegate lieferscheinpositionDelegate = null;
	private LieferscheinReportDelegate lieferscheinReportDelegate = null;
	private LieferscheinServiceDelegate lieferscheinServiceDelegate = null;
	private AuftragDelegate auftragDelegate = null;
	private AuftragpositionDelegate auftragpositionDelegate = null;
	private AuftragteilnehmerDelegate auftragteilnehmerDelegate = null;
	private AuftragServiceDelegate auftragServiceDelegate = null;
	private AuftragReportDelegate auftragReportDelegate = null;
	private AuftragRahmenAbrufDelegate auftragRahmenAbrufDelegate = null;
	private BestellungDelegate bestellungDelegate = null;
	private BestellungServiceDelegate bestellungServiceDelegate = null;
	private WareneingangDelegate wareneingangDelegate = null;
	private BestellvorschlagDelegate bestellvorschlagDelegate = null;
	private KundeDelegate kundeDelegate = null;
	private LieferantDelegate lieferantDelegate = null;
	private LagerDelegate lagerDelegate = null;
	private ArtikelDelegate artikelDelegate = null;
	private BenutzerDelegate benutzerDelegate = null;
	private VkPreisfindungDelegate vkPreisfindungDelegate = null;
	private PartnerDelegate partnerDelegate = null;
	private PersonalDelegate personalDelegate = null;
	private MandantDelegate mandantDelegate = null;
	private LocaleDelegate localeDelegate = null;
	private FinanzDelegate finanzDelegate = null;
	private ReservierungDelegate reservierungDelegate = null;
	private RahmenbedarfeDelegate rahmenbedarfeDelegate = null;
	private BuchenDelegate buchenDelegate = null;
	private RechnungDelegate rechnungDelegate = null;
	private MaterialDelegate materialDelegate = null;
	private SystemDelegate systemDelegate = null;
	private SystemReportDelegate systemReportDelegate = null;
	private KundesachbearbeiterDelegate kundeSachbearbeiterDelegate = null;
	private ParameterDelegate parameterDelegate = null;
	private MahnwesenDelegate mahnwesenDelegate = null;
	private BSMahnwesenDelegate bsmahnwesenDelegate = null;
	private AnsprechpartnerDelegate ansprechpartnerDelegate = null;
	private EingangsrechnungDelegate eingangsrechnungDelegate = null;
	private ZeiterfassungDelegate zeiterfassungDelegate = null;
	private VersandDelegate versandDelegate = null;
	private PartnerbankDelegate partnerbankDelegate = null;
	private PartnerServicesDelegate partnerServicesDelegate = null;
	// private PartnerklasseDelegate partnerklasseDelegate = null;
	private MediaDelegate mediaDelegate = null;
	private ArtikelbestelltDelegate artikelbestelltDelegate = null;
	private RechnungServiceDelegate rechnungServiceDelegate = null;
	private FinanzServiceDelegate finanzServiceDelegate = null;
	private FinanzReportDelegate finanzReportDelegate = null;
	private AnfrageDelegate anfrageDelegate = null;
	private AnfragepositionDelegate anfragepositionDelegate = null;
	private AnfrageServiceDelegate anfrageServiceDelegate = null;
	private AnfrageReportDelegate anfrageReportDelegate = null;
	private RechteDelegate rechteDelegate = null;
	private AngebotDelegate angebotDelegate = null;
	private AngebotpositionDelegate angebotpositionDelegate = null;
	private AngebotServiceDelegate angebotServiceDelegate = null;
	private AngebotReportDelegate angebotReportDelegate = null;
	private InventurDelegate inventurDelegate = null;
	private FertigungServiceDelegate fertigungServiceDelegate = null;
	private StuecklisteDelegate stuecklisteDelegate = null;
	private StuecklisteReportDelegate stuecklisteReportDelegate = null;
	private TheJudgeDelegate theJudgeDelegate = null;
	private LieferantServicesDelegate lieferantServicesDelegate = null;
	private FertigungDelegate fertigungDelegate = null;
	private FehlmengeDelegate fehlmengeDelegate = null;
	private ArtikelkommentarDelegate artikelkommentarDelegate = null;
	private AngebotstklDelegate angebotstklDelegate = null;
	private AngebotstklpositionDelegate angebotstklpositionDelegate = null;
	private FibuExportDelegate fibuExportDelegate = null;
	private ArtikelReportDelegate artikelReportDelegate = null;
	private PanelDelegate panelDelegate = null;
	private LagerReportDelegate lagerReportDelegate = null;
	private TheClientDelegate theClientDelegate = null;
	private LogonDelegate logonDelegate = null;
	private DruckerDelegate druckerDelegate = null;
	private LPAsynchSubscriber lPAsynchSubscriber = null;
	private KundensokoDelegate kundesokoDelegate = null;
	private ZeiterfassungReportDelegate zeiterfassungReportDelegate = null;
	private ProjektDelegate projektDelegate = null;
	private ProjektServiceDelegate projektServiceDelegate = null;
	private DokumenteDelegate dokumenteDelegate = null;
	private ZutrittDelegate zutrittDelegate = null;
	private ZutrittReportDelegate zutrittReportDelegate = null;
	private ReklamationDelegate reklamtionDelegate = null;
	private ReklamationReportDelegate reklamtionReportDelegate = null;
	private BelegpostionkonvertierungDelegate belegpostionkonvertierungDelegate = null;
	private AutomatikDelegate automatikDelegate = null;
	private ServerDruckerDelegate serverDruckerDelegate = null;
	private AutoFehlmengenDruckDelegate autoFehlmengenDruckDelegate = null;
	private AutoMahnungsversandDelegate autoMahnungsversandDelegate = null;
	private AutoBestellvorschlagDelegate autoBestellvorschlagDelegate = null;
	private JCRDocDelegate jcrDocDelegate = null;
	private AutoMahnenDelegate autoMahnenDelegate = null;
	private AutoRahmendetailbedarfdruckDelegate autoRahmendetailbedarfdruckDelegate = null;
	private KuecheDelegate kuecheDelegate = null;
	private InstandhaltungDelegate instandhaltungDelegate = null;
	private InstandhaltungReportDelegate instandhaltungReportDelegate = null;
	private KuecheReportDelegate kuecheReportDelegate = null;
	private AutoPaternosterDelegate autoPaternosterDelegate = null;
	private BenutzerServicesDelegate benutzerServicesDelegate = null;
	private InseratDelegate inseratDelegate = null;
	private PflegeDelegate pflegeDelegate = null;
	private MaschineDelegate maschineDelegate = null;

	static public DelegateFactory getInstance() {
		if (delegateFactory == null) {
			delegateFactory = new DelegateFactory();
		}
		delegateFactory.iZugriffsCounter++;
		return delegateFactory;
	}

	public int getIZugriffsCounter() {
		// dieser zugriff geht auch ueber getInstance(), darf aber nicht
		// mitgezaehlt werden
		return --iZugriffsCounter;
	}

	private EmailMediaDelegate emailMediaDelegate = null;
	private JCRMediaDelegate jcrMediaDelegate = null;

	public void resetIZugriffsCounter() {
		iZugriffsCounter = 0;
	}

	public LogonDelegate getLogonDelegate() throws Exception {
		if (logonDelegate == null) {
			logonDelegate = new LogonDelegate();
		}
		return logonDelegate;
	}

	public FertigungDelegate getFertigungDelegate() throws Throwable {
		if (fertigungDelegate == null) {
			fertigungDelegate = new FertigungDelegate();
		}
		return fertigungDelegate;
	}

	public FehlmengeDelegate getFehlmengeDelegate() throws Throwable {
		if (fehlmengeDelegate == null) {
			fehlmengeDelegate = new FehlmengeDelegate();
		}
		return fehlmengeDelegate;
	}

	public PartnerbankDelegate getPartnerbankDelegate() throws Throwable {
		if (partnerbankDelegate == null) {
			partnerbankDelegate = new PartnerbankDelegate();
		}
		return partnerbankDelegate;
	}

	public PartnerServicesDelegate getPartnerServicesDelegate()
			throws Throwable {
		if (partnerServicesDelegate == null) {
			partnerServicesDelegate = new PartnerServicesDelegate();
		}
		return partnerServicesDelegate;
	}

	/*
	 * public PartnerServicesDelegate getPartnerklassenDelegate() throws
	 * Throwable { if (partnerklassenDelegate == null) { partnerklassenDelegate
	 * = new PartnerklasseServicesDelegate(); } return partnerklassenDelegate; }
	 */

	public LieferscheinDelegate getLsDelegate() throws ExceptionLP {
		if (lieferscheinDelegate == null) {
			lieferscheinDelegate = new LieferscheinDelegate();
		}
		return lieferscheinDelegate;
	}

	public AutoPaternosterDelegate getAutoPaternosterDelegate()
			throws ExceptionLP {
		if (autoPaternosterDelegate == null) {
			autoPaternosterDelegate = new AutoPaternosterDelegate();
		}
		return autoPaternosterDelegate;
	}

	public LieferscheinpositionDelegate getLieferscheinpositionDelegate()
			throws ExceptionLP {
		if (lieferscheinpositionDelegate == null) {
			lieferscheinpositionDelegate = new LieferscheinpositionDelegate();
		}
		return lieferscheinpositionDelegate;
	}

	public LieferscheinReportDelegate getLieferscheinReportDelegate()
			throws ExceptionLP {
		if (lieferscheinReportDelegate == null) {
			lieferscheinReportDelegate = new LieferscheinReportDelegate();
		}
		return lieferscheinReportDelegate;
	}

	public LieferscheinServiceDelegate getLieferscheinServiceDelegate()
			throws ExceptionLP {
		if (lieferscheinServiceDelegate == null) {
			lieferscheinServiceDelegate = new LieferscheinServiceDelegate();
		}
		return lieferscheinServiceDelegate;
	}

	public ArtikelDelegate getArtikelDelegate() throws Throwable {
		if (artikelDelegate == null) {
			artikelDelegate = new ArtikelDelegate();
		}
		return artikelDelegate;
	}

	public ArtikelReportDelegate getArtikelReportDelegate() throws Throwable {
		if (artikelReportDelegate == null) {
			artikelReportDelegate = new ArtikelReportDelegate();
		}
		return artikelReportDelegate;
	}

	public ZeiterfassungReportDelegate getZeiterfassungReportDelegate()
			throws Throwable {
		if (zeiterfassungReportDelegate == null) {
			zeiterfassungReportDelegate = new ZeiterfassungReportDelegate();
		}
		return zeiterfassungReportDelegate;
	}

	public ArtikelkommentarDelegate getArtikelkommentarDelegate()
			throws Throwable {
		if (artikelkommentarDelegate == null) {
			artikelkommentarDelegate = new ArtikelkommentarDelegate();
		}
		return artikelkommentarDelegate;
	}

	public TheJudgeDelegate getTheJudgeDelegate() throws Throwable {

		if (theJudgeDelegate == null) {
			theJudgeDelegate = new TheJudgeDelegate();
		}
		return theJudgeDelegate;
	}

	public BenutzerDelegate getBenutzerDelegate() throws Throwable {
		if (benutzerDelegate == null) {
			benutzerDelegate = new BenutzerDelegate();
		}
		return benutzerDelegate;
	}

	public BenutzerServicesDelegate getBenutzerServicesDelegate()
			throws Throwable {
		if (benutzerServicesDelegate == null) {
			benutzerServicesDelegate = new BenutzerServicesDelegate();
		}
		return benutzerServicesDelegate;
	}

	public KundeDelegate getKundeDelegate() throws Throwable {
		if (kundeDelegate == null) {
			kundeDelegate = new KundeDelegate();
		}
		return kundeDelegate;
	}

	public LieferantDelegate getLieferantDelegate() throws Throwable {
		if (lieferantDelegate == null) {
			lieferantDelegate = new LieferantDelegate();
		}
		return lieferantDelegate;
	}

	public AuftragDelegate getAuftragDelegate() throws Throwable {
		if (auftragDelegate == null) {
			auftragDelegate = new AuftragDelegate();
		}
		return auftragDelegate;
	}

	public AuftragpositionDelegate getAuftragpositionDelegate()
			throws ExceptionLP {
		if (auftragpositionDelegate == null) {
			auftragpositionDelegate = new AuftragpositionDelegate();
		}
		return auftragpositionDelegate;
	}

	public AuftragteilnehmerDelegate getAuftragteilnehmerDelegate()
			throws ExceptionLP {
		if (auftragteilnehmerDelegate == null) {
			auftragteilnehmerDelegate = new AuftragteilnehmerDelegate();
		}
		return auftragteilnehmerDelegate;
	}

	public AuftragServiceDelegate getAuftragServiceDelegate()
			throws ExceptionLP {
		if (auftragServiceDelegate == null) {
			auftragServiceDelegate = new AuftragServiceDelegate();
		}
		return auftragServiceDelegate;
	}

	public AuftragReportDelegate getAuftragReportDelegate() throws ExceptionLP {
		if (auftragReportDelegate == null) {
			auftragReportDelegate = new AuftragReportDelegate();
		}
		return auftragReportDelegate;
	}

	public LagerReportDelegate getLagerReportDelegate() throws ExceptionLP {
		if (lagerReportDelegate == null) {
			lagerReportDelegate = new LagerReportDelegate();
		}
		return lagerReportDelegate;
	}

	public TheClientDelegate getTheClientDelegate() throws ExceptionLP {
		if (theClientDelegate == null) {
			theClientDelegate = new TheClientDelegate();
		}
		return theClientDelegate;
	}

	public AuftragRahmenAbrufDelegate getAuftragRahmenAbrufDelegate()
			throws ExceptionLP {
		if (auftragRahmenAbrufDelegate == null) {
			auftragRahmenAbrufDelegate = new AuftragRahmenAbrufDelegate();
		}
		return auftragRahmenAbrufDelegate;
	}

	public BestellungDelegate getBestellungDelegate() throws Throwable {
		if (bestellungDelegate == null) {
			bestellungDelegate = new BestellungDelegate();
		}
		return bestellungDelegate;
	}

	public ReklamationDelegate getReklamationDelegate() throws Throwable {
		if (reklamtionDelegate == null) {
			reklamtionDelegate = new ReklamationDelegate();
		}
		return reklamtionDelegate;
	}

	public ReklamationReportDelegate getReklamationReportDelegate()
			throws Throwable {
		if (reklamtionReportDelegate == null) {
			reklamtionReportDelegate = new ReklamationReportDelegate();
		}
		return reklamtionReportDelegate;
	}

	public BestellungServiceDelegate getBestellungServiceDelegate()
			throws Throwable {
		if (bestellungServiceDelegate == null) {
			bestellungServiceDelegate = new BestellungServiceDelegate();
		}

		return bestellungServiceDelegate;
	}

	public WareneingangDelegate getWareneingangDelegate() throws Throwable {
		if (wareneingangDelegate == null) {
			wareneingangDelegate = new WareneingangDelegate();
		}
		return wareneingangDelegate;
	}

	public BestellvorschlagDelegate getBestellvorschlagDelegate()
			throws Throwable {
		if (bestellvorschlagDelegate == null) {
			bestellvorschlagDelegate = new BestellvorschlagDelegate();
		}
		return bestellvorschlagDelegate;
	}

	public LagerDelegate getLagerDelegate() throws Throwable {
		if (lagerDelegate == null) {
			lagerDelegate = new LagerDelegate();
		}
		return lagerDelegate;
	}

	public AnsprechpartnerDelegate getAnsprechpartnerDelegate()
			throws Throwable {
		if (ansprechpartnerDelegate == null) {
			ansprechpartnerDelegate = new AnsprechpartnerDelegate();
		}
		return ansprechpartnerDelegate;
	}

	public PartnerDelegate getPartnerDelegate() throws ExceptionLP {
		if (partnerDelegate == null) {
			partnerDelegate = new PartnerDelegate();
		}
		return partnerDelegate;
	}

	public MandantDelegate getMandantDelegate() throws Throwable {
		if (mandantDelegate == null) {
			mandantDelegate = new MandantDelegate();
		}
		return mandantDelegate;
	}

	// stdcrud: 5 InternalFrame -> getDelegate
	public LocaleDelegate getLocaleDelegate() throws Throwable {
		if (localeDelegate == null) {
			localeDelegate = new LocaleDelegate();
		}
		return localeDelegate;
	}

	public ProjektServiceDelegate getProjektServiceDelegate() throws Throwable {
		if (projektServiceDelegate == null) {
			projektServiceDelegate = new ProjektServiceDelegate();
		}
		return projektServiceDelegate;
	}

	public LieferantServicesDelegate getLieferantServicesDelegate()
			throws Throwable {
		if (lieferantServicesDelegate == null) {
			lieferantServicesDelegate = new LieferantServicesDelegate();
		}
		return lieferantServicesDelegate;
	}

	public BuchenDelegate getBuchenDelegate() throws Throwable {
		if (buchenDelegate == null) {
			buchenDelegate = new BuchenDelegate();
		}
		return buchenDelegate;
	}

	public FinanzDelegate getFinanzDelegate() throws ExceptionLP {
		if (finanzDelegate == null) {
			finanzDelegate = new FinanzDelegate();
		}
		return finanzDelegate;
	}

	public BelegpostionkonvertierungDelegate getBelegpostionkonvertierungDelegate()
			throws ExceptionLP {
		if (belegpostionkonvertierungDelegate == null) {
			belegpostionkonvertierungDelegate = new BelegpostionkonvertierungDelegate();
		}
		return belegpostionkonvertierungDelegate;
	}

	public ReservierungDelegate getReservierungDelegate() throws Throwable {
		if (reservierungDelegate == null) {
			reservierungDelegate = new ReservierungDelegate();
		}
		return reservierungDelegate;
	}

	public RahmenbedarfeDelegate getRahmenbedarfeDelegate() throws Throwable {
		if (rahmenbedarfeDelegate == null) {
			rahmenbedarfeDelegate = new RahmenbedarfeDelegate();
		}
		return rahmenbedarfeDelegate;
	}

	public ArtikelbestelltDelegate getArtikelbestelltDelegate()
			throws Throwable {
		if (artikelbestelltDelegate == null) {
			artikelbestelltDelegate = new ArtikelbestelltDelegate();
		}
		return artikelbestelltDelegate;
	}

	public RechnungDelegate getRechnungDelegate() throws Throwable {
		if (rechnungDelegate == null) {
			rechnungDelegate = new RechnungDelegate();
		}
		return rechnungDelegate;
	}

	public MaterialDelegate getMaterialDelegate() throws Throwable {
		if (materialDelegate == null) {
			materialDelegate = new MaterialDelegate();
		}
		return materialDelegate;
	}

	public SystemDelegate getSystemDelegate() throws Throwable {
		if (systemDelegate == null) {
			systemDelegate = new SystemDelegate();
		}
		return systemDelegate;
	}

	public SystemReportDelegate getSystemReportDelegate() throws Throwable {
		if (systemReportDelegate == null) {
			systemReportDelegate = new SystemReportDelegate();
		}
		return systemReportDelegate;
	}

	public RechteDelegate getRechteDelegate() throws Throwable {
		if (rechteDelegate == null) {
			rechteDelegate = new RechteDelegate();
		}
		return rechteDelegate;
	}

	public AngebotDelegate getAngebotDelegate() throws Throwable {
		if (angebotDelegate == null) {
			angebotDelegate = new AngebotDelegate();
		}
		return angebotDelegate;
	}

	public DokumenteDelegate getDokumenteDelegate() throws Throwable {
		if (dokumenteDelegate == null) {
			dokumenteDelegate = new DokumenteDelegate();
		}
		return dokumenteDelegate;
	}

	public AngebotpositionDelegate getAngebotpositionDelegate()
			throws Throwable {
		if (angebotpositionDelegate == null) {
			angebotpositionDelegate = new AngebotpositionDelegate();
		}
		return angebotpositionDelegate;
	}

	public AngebotServiceDelegate getAngebotServiceDelegate() throws Throwable {
		if (angebotServiceDelegate == null) {
			angebotServiceDelegate = new AngebotServiceDelegate();
		}
		return angebotServiceDelegate;
	}

	public AngebotstklDelegate getAngebotstklDelegate() throws Throwable {
		if (angebotstklDelegate == null) {
			angebotstklDelegate = new AngebotstklDelegate();
		}
		return angebotstklDelegate;
	}

	public AngebotstklpositionDelegate getAngebotstklpositionDelegate()
			throws Throwable {
		if (angebotstklpositionDelegate == null) {
			angebotstklpositionDelegate = new AngebotstklpositionDelegate();
		}
		return angebotstklpositionDelegate;
	}

	public AngebotReportDelegate getAngebotReportDelegate() throws Throwable {
		if (angebotReportDelegate == null) {
			angebotReportDelegate = new AngebotReportDelegate();
		}
		return angebotReportDelegate;
	}

	public InventurDelegate getInventurDelegate() throws Throwable {
		if (inventurDelegate == null) {
			inventurDelegate = new InventurDelegate();
		}
		return inventurDelegate;
	}

	public StuecklisteDelegate getStuecklisteDelegate() throws Throwable {
		if (stuecklisteDelegate == null) {
			stuecklisteDelegate = new StuecklisteDelegate();
		}
		return stuecklisteDelegate;
	}

	public StuecklisteReportDelegate getStuecklisteReportDelegate()
			throws Throwable {
		if (stuecklisteReportDelegate == null) {
			stuecklisteReportDelegate = new StuecklisteReportDelegate();
		}
		return stuecklisteReportDelegate;
	}

	public ZeiterfassungDelegate getZeiterfassungDelegate() throws Throwable {
		if (zeiterfassungDelegate == null) {
			zeiterfassungDelegate = new ZeiterfassungDelegate();
		}
		return zeiterfassungDelegate;
	}

	public ZutrittDelegate getZutrittDelegate() throws Throwable {
		if (zutrittDelegate == null) {
			zutrittDelegate = new ZutrittDelegate();
		}
		return zutrittDelegate;
	}

	public ZutrittReportDelegate getZutrittReportDelegate() throws Throwable {
		if (zutrittReportDelegate == null) {
			zutrittReportDelegate = new ZutrittReportDelegate();
		}
		return zutrittReportDelegate;
	}

	public KundesachbearbeiterDelegate getKundesachbearbeiterDelegate()
			throws ExceptionLP {
		if (kundeSachbearbeiterDelegate == null) {
			kundeSachbearbeiterDelegate = new KundesachbearbeiterDelegate();
		}
		return kundeSachbearbeiterDelegate;
	}

	public ParameterDelegate getParameterDelegate() throws Throwable {
		if (parameterDelegate == null) {
			parameterDelegate = new ParameterDelegate();
		}
		return parameterDelegate;
	}

	public VkPreisfindungDelegate getVkPreisfindungDelegate() throws Throwable {
		if (vkPreisfindungDelegate == null) {
			vkPreisfindungDelegate = new VkPreisfindungDelegate();
		}
		return vkPreisfindungDelegate;
	}

	public MahnwesenDelegate getMahnwesenDelegate() throws Throwable {
		if (mahnwesenDelegate == null) {
			mahnwesenDelegate = new MahnwesenDelegate();
		}
		return mahnwesenDelegate;
	}

	public BSMahnwesenDelegate getBSMahnwesenDelegate() throws Throwable {
		if (bsmahnwesenDelegate == null) {
			bsmahnwesenDelegate = new BSMahnwesenDelegate();
		}
		return bsmahnwesenDelegate;
	}

	public EingangsrechnungDelegate getEingangsrechnungDelegate()
			throws Throwable {
		if (eingangsrechnungDelegate == null) {
			eingangsrechnungDelegate = new EingangsrechnungDelegate();
		}
		return eingangsrechnungDelegate;
	}

	public KuecheDelegate getKuecheDelegate() throws Throwable {
		if (kuecheDelegate == null) {
			kuecheDelegate = new KuecheDelegate();
		}
		return kuecheDelegate;
	}

	public InseratDelegate getInseratDelegate() throws Throwable {
		if (inseratDelegate == null) {
			inseratDelegate = new InseratDelegate();
		}
		return inseratDelegate;
	}

	public PflegeDelegate getPflegeDelegate() throws Throwable {
		if (pflegeDelegate == null) {
			pflegeDelegate = new PflegeDelegate();
		}
		return pflegeDelegate;
	}

	public MaschineDelegate getMaschineDelegate() throws Throwable {
		if (maschineDelegate == null) {
			maschineDelegate = new MaschineDelegate();
		}
		return maschineDelegate;
	}

	public InstandhaltungDelegate getInstandhaltungDelegate() throws Throwable {
		if (instandhaltungDelegate == null) {
			instandhaltungDelegate = new InstandhaltungDelegate();
		}
		return instandhaltungDelegate;
	}

	public InstandhaltungReportDelegate getInstandhaltungReportDelegate()
			throws Throwable {
		if (instandhaltungReportDelegate == null) {
			instandhaltungReportDelegate = new InstandhaltungReportDelegate();
		}
		return instandhaltungReportDelegate;
	}

	public KuecheReportDelegate getKuecheReportDelegate() throws Throwable {
		if (kuecheReportDelegate == null) {
			kuecheReportDelegate = new KuecheReportDelegate();
		}
		return kuecheReportDelegate;
	}

	public VersandDelegate getVersandDelegate() throws Throwable {
		if (versandDelegate == null) {
			versandDelegate = new VersandDelegate();
		}
		return versandDelegate;
	}

	public MediaDelegate getMediaDelegate() throws Throwable {
		if (mediaDelegate == null) {
			mediaDelegate = new MediaDelegate();
		}
		return mediaDelegate;
	}

	public RechnungServiceDelegate getRechnungServiceDelegate()
			throws Throwable {
		if (rechnungServiceDelegate == null) {
			rechnungServiceDelegate = new RechnungServiceDelegate();
		}
		return rechnungServiceDelegate;
	}

	public FinanzServiceDelegate getFinanzServiceDelegate() throws Throwable {
		if (finanzServiceDelegate == null) {
			finanzServiceDelegate = new FinanzServiceDelegate();
		}
		return finanzServiceDelegate;
	}

	public FinanzReportDelegate getFinanzReportDelegate() throws Throwable {
		if (finanzReportDelegate == null) {
			finanzReportDelegate = new FinanzReportDelegate();
		}
		return finanzReportDelegate;
	}

	public AnfrageDelegate getAnfrageDelegate() throws Throwable {
		if (anfrageDelegate == null) {
			anfrageDelegate = new AnfrageDelegate();
		}
		return anfrageDelegate;
	}

	public PanelDelegate getPanelDelegate() throws Throwable {
		if (panelDelegate == null) {
			panelDelegate = new PanelDelegate();
		}
		return panelDelegate;
	}

	public AnfragepositionDelegate getAnfragepositionDelegate()
			throws Throwable {
		if (anfragepositionDelegate == null) {
			anfragepositionDelegate = new AnfragepositionDelegate();
		}
		return anfragepositionDelegate;
	}

	public AnfrageServiceDelegate getAnfrageServiceDelegate() throws Throwable {
		if (anfrageServiceDelegate == null) {
			anfrageServiceDelegate = new AnfrageServiceDelegate();
		}
		return anfrageServiceDelegate;
	}

	public AnfrageReportDelegate getAnfrageReportDelegate() throws Throwable {
		if (anfrageReportDelegate == null) {
			anfrageReportDelegate = new AnfrageReportDelegate();
		}
		return anfrageReportDelegate;
	}

	public FertigungServiceDelegate getFertigungServiceDelegate()
			throws Throwable {
		if (fertigungServiceDelegate == null) {
			fertigungServiceDelegate = new FertigungServiceDelegate();
		}
		return fertigungServiceDelegate;
	}

	public FibuExportDelegate getFibuExportDelegate() throws Throwable {
		if (fibuExportDelegate == null) {
			fibuExportDelegate = new FibuExportDelegate();
		}
		return fibuExportDelegate;
	}

	public PersonalDelegate getPersonalDelegate() throws Throwable {
		if (personalDelegate == null) {
			personalDelegate = new PersonalDelegate();
		}
		return personalDelegate;
	}

	public DruckerDelegate getDruckerDelegate() throws Throwable {
		if (druckerDelegate == null) {
			druckerDelegate = new DruckerDelegate();
		}
		return druckerDelegate;
	}

	/**
	 * jmsqueue: anlegen der queue
	 * 
	 * @return LPAsynchSubscriber
	 * @throws Throwable
	 */
	public LPAsynchSubscriber getLPAsynchSubscriber() throws Throwable {
		if (lPAsynchSubscriber == null) {
			lPAsynchSubscriber = new LPAsynchSubscriber();
		}
		return lPAsynchSubscriber;
	}

	public KundensokoDelegate getKundesokoDelegate() throws Throwable {
		if (kundesokoDelegate == null) {
			kundesokoDelegate = new KundensokoDelegate();
		}
		return kundesokoDelegate;
	}

	public ProjektDelegate getProjektDelegate() throws Throwable {
		if (projektDelegate == null) {
			projektDelegate = new ProjektDelegate();
		}
		return projektDelegate;
	}

	public AutomatikDelegate getAutomatikDelegate() throws ExceptionLP {
		if (automatikDelegate == null) {
			automatikDelegate = new AutomatikDelegate();
		}
		return automatikDelegate;
	}

	public ServerDruckerDelegate getServerDruckerDelegate() throws ExceptionLP {
		if (serverDruckerDelegate == null) {
			serverDruckerDelegate = new ServerDruckerDelegate();
		}
		return serverDruckerDelegate;
	}

	public AutoFehlmengenDruckDelegate getAutoFehlmengenDruckDelegate()
			throws Throwable {
		if (autoFehlmengenDruckDelegate == null) {
			autoFehlmengenDruckDelegate = new AutoFehlmengenDruckDelegate();
		}
		return autoFehlmengenDruckDelegate;
	}

	public AutoMahnungsversandDelegate getAutoMahnungsversandDelegate()
			throws Throwable {
		if (autoMahnungsversandDelegate == null) {
			autoMahnungsversandDelegate = new AutoMahnungsversandDelegate();
		}
		return autoMahnungsversandDelegate;
	}

	public AutoBestellvorschlagDelegate getautoBestellvorschlagDelegate()
			throws CreateException, RemoteException, Throwable {
		if (autoBestellvorschlagDelegate == null) {
			autoBestellvorschlagDelegate = new AutoBestellvorschlagDelegate();
		}
		return autoBestellvorschlagDelegate;
	}

	public AutoRahmendetailbedarfdruckDelegate getautoRahmendetailbedarfdruckDelegate()
			throws NamingException {
		if (autoRahmendetailbedarfdruckDelegate == null) {
			autoRahmendetailbedarfdruckDelegate = new AutoRahmendetailbedarfdruckDelegate();
		}
		return autoRahmendetailbedarfdruckDelegate;
	}

	public AutoMahnenDelegate getAutoMahnenDelegate() throws Throwable {
		if (autoMahnenDelegate == null) {
			autoMahnenDelegate = new AutoMahnenDelegate();
		}
		return autoMahnenDelegate;
	}

	public JCRDocDelegate getJCRDocDelegate() throws Throwable {
		if (jcrDocDelegate == null) {
			jcrDocDelegate = new JCRDocDelegate();
		}
		return jcrDocDelegate;
	}

	public JCRMediaDelegate getJCRMediaDelegate() throws Throwable {
		if (jcrMediaDelegate == null) {
			jcrMediaDelegate = new JCRMediaDelegate();
		}
		return jcrMediaDelegate;
	}

	public EmailMediaDelegate getEmailMediaDelegate() throws Throwable {
		if (emailMediaDelegate == null) {
			emailMediaDelegate = new EmailMediaDelegate();
		}
		return emailMediaDelegate;
	}
}
