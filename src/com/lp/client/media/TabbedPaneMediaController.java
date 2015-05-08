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
 *******************************************************************************/
package com.lp.client.media;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedEventDropDone;
import com.lp.client.frame.delegate.AnsprechpartnerDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PartnerDelegate;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.media.service.HvCancelQueueSender;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.media.service.MediaStoreBelegDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.JCRMediaDto;
import com.lp.server.system.service.LocaleFac;

public class TabbedPaneMediaController {
	protected final LpLogger log = (LpLogger) LpLogger.getInstance(this.getClass());

	private InternalFrame internalFrame = null ;
	private Integer inboxId = null ;
	private MediaEmailMetaDto emailMetaDto = null ;
	private JCRMediaDto jcrMediaDto = null ;
	private HvCancelQueueSender cancelQueueSender ;
	
	private Integer belegReferenzId = null ;
	private MediaStoreBelegDto belegStoreDto = null ;
	
	private boolean isRetrievingEmails ;
	
	public TabbedPaneMediaController(InternalFrame internalFrame) {
		this.internalFrame = internalFrame ;
		cancelQueueSender = new HvCancelQueueSender(EmailMediaFac.CANCEL_QUEUENAME) ;
	}

	protected InternalFrame getInternalFrame() {
		return internalFrame ;
	}
	
	public MediaEmailMetaDto getEmailMetaDto() {
		return emailMetaDto ;
	}
	
	public MediaEmailMetaDto getTransferableEmailMetaDto() {
		return emailMetaDto ;
	}

	public JCRMediaDto getJCRMediaDto() {
		return jcrMediaDto ;
	}
	
	public boolean hasInboxEntry() {
		return inboxId != null ;
	}
	
	protected void setInboxId(Integer inboxId) {
		this.inboxId = inboxId ;
	}
	
	protected Integer getInboxId() {
		return inboxId ;
	}
	
	protected void setEmailMetaDto(MediaEmailMetaDto emailMetaDto) {
		log.info("Setting new emailMetaDto " + emailMetaDto);
		this.emailMetaDto = emailMetaDto ;
	}

	protected void setJCRMediaDto(JCRMediaDto jcrMediaDto) {
		log.info("Setting new jcrMediaDto " + jcrMediaDto); 
		this.jcrMediaDto = jcrMediaDto ;
	}
	
	protected Integer getBelegReferenzId() {
		return belegReferenzId;
	}

	protected void setBelegReferenzId(Integer belegReferenzId) {
		this.belegReferenzId = belegReferenzId;
	}

	protected MediaStoreBelegDto getBelegStoreDto() {
		return belegStoreDto;
	}

	protected void setBelegStoreDto(MediaStoreBelegDto belegStoreDto) {
		this.belegStoreDto = belegStoreDto;
	}	

	public void removeInboxEntry() throws Throwable {
		removeInboxEntry(false) ;
	}
	
	public void removeInboxEntry(boolean hardDelete) throws Throwable {
		if(hardDelete) {
			MediaEmailMetaDto metaDto = DelegateFactory.getInstance()
					.getEmailMediaDelegate().emailMetaFindByInboxId(getInboxId()) ;

			DelegateFactory.getInstance().getEmailMediaDelegate().removeInboxEntry(getInboxId()) ;
			DelegateFactory.getInstance().getEmailMediaDelegate().removeEmail(metaDto.getUuid()) ;
			
		} else {
			DelegateFactory.getInstance().getEmailMediaDelegate().versteckeInboxEntry(getInboxId()) ;
		}
	
		setEmailMetaDto(null) ;
		setJCRMediaDto(null) ;
	}
	
	public void loadEmailMetaFromServer(Integer inboxId) throws Throwable {
		log.info("Loading emailMetaDto from server with Id (" + inboxId + ").") ;

		setInboxId(inboxId) ;
		
		MediaEmailMetaDto metaDto = inboxId == null ? null : DelegateFactory.getInstance()
				.getEmailMediaDelegate().emailMetaFindByInboxId(inboxId) ;
		setEmailMetaDto(metaDto) ;

		JCRMediaDto jcrDto = metaDto == null ? null : DelegateFactory.getInstance()
				.getJCRMediaDelegate().findEmailByUUID(metaDto.getUuid(), true) ;
		setJCRMediaDto(jcrDto);
		
		// Wir wollen die ganze E-Mail sehen
		if(metaDto != null && jcrDto != null) {
			metaDto.setXContent(new String(jcrDto.getContent()));
		}
	}
	
	public void loadEmailReferenceFromServer(Integer referenceId) throws Throwable {
		log.info("Loading emailReference from server with Id (" + referenceId + ").") ;

		setBelegReferenzId(referenceId) ;
		
		MediaStoreBelegDto belegDto = referenceId == null ? null : DelegateFactory.getInstance()
				.getEmailMediaDelegate().belegReferenzFindByReferenceId(referenceId, true) ;
		setBelegStoreDto(belegDto);
		if(belegDto != null && belegDto.getEmailMetaDto() != null) {
			JCRMediaDto jcrDto = DelegateFactory.getInstance()
					.getJCRMediaDelegate().findEmailByUUID(belegDto.getEmailMetaDto().getUuid(), true) ;
			belegDto.getEmailMetaDto().setXContent(new String(jcrDto.getContent()));
		}
	}
	
	public void retrieveEmails() throws Throwable {
		synchronized (this) {
			isRetrievingEmails = true ;
		}
		
		DelegateFactory.getInstance().getEmailMediaDelegate().retrieveEmails() ;

		synchronized (this) {
			isRetrievingEmails = false ;
		}
	}
	
	public void cancelRetrieveEmails() throws Throwable {
		cancelQueueSender.cancelRetrieveEmail();
		log.info("Cancel retrieve Email posted") ;

		synchronized (this) {
			isRetrievingEmails = false ;
		}
	}
	
	public boolean isRetrievingEmail() {
		return isRetrievingEmails ;
	}
	
	public void removeEmail(String uuid) throws Throwable {
		DelegateFactory.getInstance().getEmailMediaDelegate().removeEmail(uuid) ;
	}
	
	public void createKurzbrief() throws Throwable {
		if(getEmailMetaDto() == null) return ;

		String email = getEmailMetaDto().getCFrom() ;
		if(email == null || email.trim().length() == 0) return ;
		email = sanitizedEmail(email) ;
		
		String xContent = getEmailMetaDto().getXContent() ;
		if(getJCRMediaDto() != null) {
			xContent = new String(getJCRMediaDto().getContent()) ;
		}
		
		PartnerDelegate partnerDelegate = DelegateFactory.getInstance().getPartnerDelegate() ;
		PartnerDto[] partners = partnerDelegate.partnerFindByEmail(email) ;
		if(partners != null && partners.length == 1) {
			KurzbriefDto kurzbriefDto = new KurzbriefDto() ;
			kurzbriefDto.setPartnerIId(partners[0].getIId());
			kurzbriefDto.setCBetreff(getEmailMetaDto().getCSubject()); 
			kurzbriefDto.setXText(xContent); 
			kurzbriefDto.setBelegartCNr(LocaleFac.BELEGART_PARTNER);
			kurzbriefDto.setTAendern(getEmailMetaDto().getTEmailDate()) ;
			kurzbriefDto.setBHtml(getEmailMetaDto().getBHtml());
			Integer id = partnerDelegate.createKurzbrief(kurzbriefDto) ;
			
			log.info("Created kurzbrief for partner " + 
					kurzbriefDto.getPartnerIId() + " with id " + id + ".");
			return ;
		}
		
		AnsprechpartnerDelegate ansprechDelegate = DelegateFactory.getInstance().getAnsprechpartnerDelegate() ;
		AnsprechpartnerDto[] ansprechpartners = ansprechDelegate.ansprechpartnerFindByEmail(email) ;
		if(ansprechpartners != null && ansprechpartners.length == 1) {
			KurzbriefDto kurzbriefDto = new KurzbriefDto() ;
			kurzbriefDto.setPartnerIId(ansprechpartners[0].getPartnerIId());
			kurzbriefDto.setAnsprechpartnerIId(ansprechpartners[0].getIId());
			kurzbriefDto.setCBetreff(getEmailMetaDto().getCSubject()); 
			kurzbriefDto.setXText(xContent); 
			kurzbriefDto.setBelegartCNr(LocaleFac.BELEGART_PARTNER);
			kurzbriefDto.setTAendern(getEmailMetaDto().getTEmailDate()) ;
			kurzbriefDto.setBHtml(getEmailMetaDto().getBHtml());
			Integer id = partnerDelegate.createKurzbrief(kurzbriefDto) ;
			
			log.info("Created kurzbrief for ansprechpartner " +
					kurzbriefDto.getAnsprechpartnerIId() + " partner " + 
					kurzbriefDto.getPartnerIId() + " with id " + id + ".");
			return ;			
		}	
	}
	
	private String sanitizedEmail(String email) {
		int indexBracketB = email.indexOf("<") ;
		if(indexBracketB == -1) return email.trim() ;
		
		int indexBracketE = email.indexOf(">", indexBracketB + 1) ;
		if(indexBracketE == -1) {
			return email.substring(indexBracketB + 1) ; 
		}
		
		return email.substring(indexBracketB + 1, indexBracketE) ;
	}
	
	public void moveMyInboxElementToOtherInbox(Integer personalId) throws Throwable {
		if(getInboxId() == null) return ;
		log.info("Moving inboxid " + getInboxId() + " to personalId = " + personalId);
		DelegateFactory.getInstance().getEmailMediaDelegate().moveInboxEntryTo(
				getInboxId(), personalId);		
	}
	
	
	public void dropDone(JComponent c, Transferable data, int action) throws Throwable {
		if(getInboxId() == null) return ;
		
		if(TransferHandler.MOVE == action) {
			removeInboxEntry();
		}

		if(action > 0) {
			ItemChangedEvent ev = new ItemChangedEventDropDone(c, emailMetaDto, TransferHandler.COPY == action) ;
			getInternalFrame().fireItemChanged(ev);
		}
	}

}
