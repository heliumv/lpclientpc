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
 package com.lp.client.stueckliste.importassistent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.SwingWorker;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;

public class StklImportPage4Ctrl extends AssistentPageController {

	protected static final int LINES_PER_SERVER_CALL = 5;
	
	private StklImportModel model;
	private Throwable importThrowable = null;
	private boolean isImporting = true;
	private SwingWorker<Integer, Void> importWorker;
	private BoundedRangeModel progressModel;
	private int progress = 0;
	
	public StklImportPage4Ctrl(StklImportModel model) {
		this.model = model;
		setProgressModel(new DefaultBoundedRangeModel(0, 0, 0, 100));
	}

	@Override
	public boolean isNextAllowed() {
		if(isImporting) return false;
		if(model.getSavingName() != null
				&& model.getSavingName().isEmpty()) return false;
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return false;
	}

	@Override
	public boolean isCancelAllowed() {
		return false;
	}

	@Override
	public void activateByNext() {
		isImporting = true;
		setImportProgress(0);
		
		setImportThrowable(null);
		if(importWorker != null) importWorker.cancel(true);
		importWorker = new ImportWorker();
		importWorker.addPropertyChangeListener(new Listener());
		importWorker.execute();
		
		setSaveByName(model.getSelectedSpezifikation().getName());
	}

	@Override
	public void activateByPrev() {
	}
	
	@Override
	public void finishedAssistent() throws ExceptionLP, Throwable {
		if(!model.isSave()) return;
		model.getSelectedSpezifikation().setName(model.getSavingName());
		model.getSelectedSpezifikation().setMandantCnr(LPMain.getTheClient().getMandant());
		if(model.getImportSpezifikationen().containsKey(model.getSavingName())) {
			DelegateFactory.getInstance().getStuecklisteDelegate().updateStklImportSpez(
					model.getSelectedSpezifikation());
		} else {
			DelegateFactory.getInstance().getStuecklisteDelegate().createStklImportSpez(
					model.getSelectedSpezifikation());
		}
	}

	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		return false;
	}
	
	/**
	 * Falls ein Fehler aufgetreten ist, gibt diese Methode den
	 * @return das {@link Throwable} falls ein Fehler aufgetreten ist, sonst <code>null</code>
	 */
	public Throwable getImportThrowable() {
		return importThrowable;
	}
	
	protected void setImportThrowable(Throwable t) {
		importThrowable = t;
	}
	
	public boolean isImporting() {
		return isImporting;
	}
	
	protected void setImporting(boolean isImporting) {
		this.isImporting = isImporting;
	}
	
	public BoundedRangeModel getProgressModel() {
			if(progressModel == null) progressModel = new DefaultBoundedRangeModel(progress, 1, 0, 100);
			return progressModel;
	}
	
	protected void setImportProgress(int progress) {
		this.progress = progress;
		getProgressModel().setValue(progress);
		fireDataUpdateEvent();
	}

	public void setProgressModel(BoundedRangeModel progressModel) {
		this.progressModel = progressModel;
	}
	
	public Integer getAnzahlImportiertePositionen() {
		return model.getAnzahlImportiertePositionen();
	}
	
	public Integer getAnzahlGesamtePositionen() {
		return model.getResults().size();
	}

	protected class ImportWorker extends SwingWorker<Integer,Void> {

		@Override
		protected Integer doInBackground() throws Exception {
			int positionen = 0;
			for(int from = 0; from < model.getResults().size(); from+=LINES_PER_SERVER_CALL) {
				int to = from+LINES_PER_SERVER_CALL;
				to = (to >= model.getResults().size() ?
						model.getResults().size() : to);
				
				try {
					positionen += DelegateFactory.getInstance().getStuecklisteDelegate()
							.importiereStklImportResults(
									model.getSelectedSpezifikation(),
									new ArrayList<IStklImportResult>(model.getResults().subList(from, to)),
									model.getUpdateArtikel());
				} catch (Throwable t) {
					setImportThrowable(t);
					return positionen;
				}
				setProgress(to*100/model.getResults().size());
			}
			return positionen;
		}
		
		@Override
		protected void done() {
			setImporting(false);
			try {
				model.setAnzahlImportiertePositionen(get());
			} catch (InterruptedException e) {
				model.setAnzahlImportiertePositionen(null);
				setImportThrowable(e);
			} catch (ExecutionException e) {
				model.setAnzahlImportiertePositionen(null);
				setImportThrowable(e);
			}
			fireDataUpdateEvent();
			fireNavigationUpdateEvent();
		}
		
	}
	
	protected class Listener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if("progress".equals(evt.getPropertyName())) {
				setImportProgress((Integer) evt.getNewValue());
			}
		}
	}
	
	/**
	 * Setzt den Namen, unter welchem die Importspezifikation gespeichert werden soll.
	 * @param name der Name, oder null falls nicht gespeichert werden soll.
	 */
	public void setSaveByName(String name) {
		model.setSavingName(name);
		fireDataUpdateEvent();
		fireNavigationUpdateEvent();
	}
	
	public void setSave(boolean save) {
		model.setSave(save);
		fireDataUpdateEvent();
		fireNavigationUpdateEvent();
	}
	
	public boolean isSaving() {
		return model.isSave();
	}
	
	public String getSpezName() {
		return model.getSavingName();
	}
	
	public List<String> getImportSpezNames() {
		return model.getImportSpezifikationen() == null ? new ArrayList<String>()
				: new ArrayList<String>(model.getImportSpezifikationen().keySet());
	}

}
