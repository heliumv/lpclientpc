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
package com.lp.client.system;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDokumentenablage;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperJTree;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class TabbedPaneDokumente extends TabbedPane {


	private PanelSplit panelSplitBelegart = null;
	private PanelQuery panelQueryBelegart = null;
	private PanelDokumenteBelegart panelBottomBelegart = null;
	
	private PanelSplit panelSplitGruppe = null;
	private PanelQuery panelQueryGruppe = null;
	private PanelDokumenteGruppe panelBottomGruppe = null;
	
	private static final long serialVersionUID = 1L;
	private final static int IDX_UEBERSICHT = 0;
	private final static int IDX_BELEGART = 1;
	private final static int IDX_GRUPPEN = 2;
	
	private static final String MENU_ACTION_DATEI_UEBERLEITEN = "MENU_ACTION_DATEI_UEBERLEITEN";
	private static final String MENU_ACTION_DATEI_VERSANDAUFTRAEGE_KOPIEREN = "MENU_ACTION_DATEI_VERSANDAUFTRAEGE_KOPIEREN";
	private static final String MENU_ACTION_DATEI_TREE_EXPANDALL = "MENU_ACTION_DATEI_TREE_EXPANDALL";
	private static final String MENU_ACTION_DATEI_TREE_COMPRESSALL = "MENU_ACTION_DATEI_TREE_COMPRESSALL";
	private static final String MENU_ACTION_DATEI_TREE_PRINT = "MENU_ACTION_DATEI_TREE_PRINT";

	
	public TabbedPaneDokumente(InternalFrame internalFrameI)
	throws Throwable {
		super(internalFrameI,
				LPMain.getInstance().getTextRespectUISPr("lp.dokumente"));
		jbInit();
		initComponents();
	}
	
	public TabbedPaneDokumente(InternalFrame internalFrameI, String addTitleI) {
		super(internalFrameI, addTitleI);
		jbInit();
		initComponents();
	}

	private void jbInit() {
		insertTab(LPMain.getTextRespectUISPr("lp.system.jcr.tab.oben.uebersicht.title"),
	              null,
	              null,
	              LPMain.getTextRespectUISPr("lp.system.jcr.tab.oben.uebersicht.tooltip"),
	              IDX_UEBERSICHT);
		insertTab(LPMain.getTextRespectUISPr("lp.system.jcr.tab.oben.belegart.title"),
	              null,
	              null,
	              LPMain.getTextRespectUISPr("lp.system.jcr.tab.oben.belegart.tooltip"),
	              IDX_BELEGART);		
		insertTab(LPMain.getTextRespectUISPr("lp.system.jcr.tab.oben.gruppen.title"),
	              null,
	              null,
	              LPMain.getTextRespectUISPr("lp.system.jcr.tab.oben.gruppen.tooltip"),
	              IDX_GRUPPEN);	
		
		
		addChangeListener(this);
	    getInternalFrame().addItemChangedListener(this);
	    
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);
			JMenu jmModul = (JMenu) wrapperMenuBar
			.getComponent(WrapperMenuBar.MENU_MODUL);
	
			jmModul.add(new JSeparator(), 0);

			JMenuItem menuItemDateiueberleiten = new JMenuItem(LPMain
					.getTextRespectUISPr("system.jcr.dokumente.ueberleiten"));
			menuItemDateiueberleiten.addActionListener(this);
			menuItemDateiueberleiten.setActionCommand(MENU_ACTION_DATEI_UEBERLEITEN);
			jmModul.add(menuItemDateiueberleiten,0);
			
			JMenuItem menuItemDateiVersandauftragkopie = new JMenuItem(LPMain
					.getTextRespectUISPr("system.jcr.dokumente.versandueberleiten"));
			menuItemDateiVersandauftragkopie.addActionListener(this);
			menuItemDateiVersandauftragkopie.setActionCommand(MENU_ACTION_DATEI_VERSANDAUFTRAEGE_KOPIEREN);
			jmModul.add(menuItemDateiVersandauftragkopie,1);

			jmModul.add(new JSeparator(),2);
			
			JMenuItem menuItemDateiTreeExpandAll = new JMenuItem(
				LPMain.getTextRespectUISPr("system.jcr.dokumente.expandtree"));
			menuItemDateiTreeExpandAll.addActionListener(this);
			menuItemDateiTreeExpandAll.setActionCommand(MENU_ACTION_DATEI_TREE_EXPANDALL);
			jmModul.add(menuItemDateiTreeExpandAll,3);

			JMenuItem menuItemDateiTreeCompressAll = new JMenuItem(
				LPMain.getTextRespectUISPr("system.jcr.dokumente.compresstree"));
			menuItemDateiTreeCompressAll.addActionListener(this);
			menuItemDateiTreeCompressAll.setActionCommand(MENU_ACTION_DATEI_TREE_COMPRESSALL);
			jmModul.add(menuItemDateiTreeCompressAll,4);

			JMenuItem menuItemDateiTreePrint = new JMenuItem(
				LPMain.getTextRespectUISPr("system.jcr.dokumente.printtree"));
			menuItemDateiTreePrint.addActionListener(this);
			menuItemDateiTreePrint.setActionCommand(MENU_ACTION_DATEI_TREE_PRINT);
			jmModul.add(menuItemDateiTreePrint, 5);

			return wrapperMenuBar;
	}
	
	public void lPEventObjectChanged(ChangeEvent e)
    throws Throwable {
		super.lPEventObjectChanged(e);
	    int selectedIndex = this.getSelectedIndex();

	    switch (selectedIndex) {
	      case IDX_UEBERSICHT: {
	    	  PanelDokumentenablage panelDokumentenablage = new PanelDokumentenablage(getInternalFrame(),
	  				LPMain.getInstance().getTextRespectUISPr("lp.dokumente"),null);
	    	  this.setComponentAt(IDX_UEBERSICHT, panelDokumentenablage);
	    	  getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
	  				panelDokumentenablage.getAdd2Title());
		      break;
	      }
	      case IDX_BELEGART: {
	    	  createPanelbelegart();
	          panelSplitBelegart.eventYouAreSelected(false);
	          panelQueryBelegart.updateButtons();
		      break;  
	      }
	      case IDX_GRUPPEN:{
	    	  createPanelGruppe();
	          panelSplitGruppe.eventYouAreSelected(false);
	          panelQueryGruppe.updateButtons();
	    	  break;
	      }
	    }
	}
	
	private void createPanelGruppe()
	throws Throwable {

		if (panelQueryGruppe == null) {
			String[] aWhichButtonIUse = {
					PanelBasis.ACTION_NEW};

			FilterKriterium[] filters = SystemFilterFactory.getInstance().
			createFKPaneldokumenteGruppe();

			panelQueryGruppe = new PanelQuery(
					null,
					filters,
					QueryParameters.UC_ID_DOKUMENTGRUPPE,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.gruppe"), true);

			panelBottomGruppe = new PanelDokumenteGruppe( getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.gruppe"), null);

			panelSplitGruppe = new PanelSplit(
					getInternalFrame(),
					panelBottomGruppe,
					panelQueryGruppe,
					350);

			setComponentAt(IDX_GRUPPEN, panelSplitGruppe);
		}
		else {
			//filter refreshen.
			panelQueryBelegart.setDefaultFilter(
					SystemFilterFactory.getInstance().createFKPaneldokumenteGruppe());
		}
	}
	
	private void createPanelbelegart()
	throws Throwable {

		if (panelQueryBelegart == null) {
			String[] aWhichButtonIUse = {
					PanelBasis.ACTION_NEW};

			FilterKriterium[] filters = SystemFilterFactory.getInstance().
			createFKPaneldokumenteBelegart();

			panelQueryBelegart = new PanelQuery(
					null,
					filters,
					QueryParameters.UC_ID_DOKUMENTBELEGART,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.belegart"), true);

			panelBottomBelegart = new PanelDokumenteBelegart( getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"lp.belegart"), null);
			

			panelSplitBelegart = new PanelSplit(
					getInternalFrame(),
					panelBottomBelegart,
					panelQueryBelegart,
					350);

			setComponentAt(IDX_BELEGART, panelSplitBelegart);
		}
		else {
			//filter refreshen.
			panelQueryBelegart.setDefaultFilter(
					SystemFilterFactory.getInstance().createFKPaneldokumenteBelegart());
		}
	}

	
	public void lPEventItemChanged(ItemChangedEvent eI)
	throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryBelegart) {
				Object key = ( (ISourceEvent) eI.getSource()).getIdSelected();
				panelBottomBelegart.setKeyWhenDetailPanel(key);
				panelBottomBelegart.eventYouAreSelected(false);
				panelQueryBelegart.updateButtons();
			}else if(eI.getSource() == panelQueryGruppe){
				Object key = ( (ISourceEvent) eI.getSource()).getIdSelected();
				panelBottomGruppe.setKeyWhenDetailPanel(key);
				panelBottomGruppe.eventYouAreSelected(false);
				panelQueryGruppe.updateButtons();
			}
			

		}
		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == this.panelQueryGruppe) {
				this.panelBottomGruppe.updateButtons(new LockStateValue(PanelBasis.
						LOCK_FOR_NEW));
				panelBottomGruppe.eventActionNew(eI, true, false);
			}else if (eI.getSource() == this.panelQueryBelegart) {
				this.panelBottomBelegart.updateButtons(new LockStateValue(PanelBasis.
						LOCK_FOR_NEW));
				panelBottomBelegart.eventActionNew(eI, true, false);
			}
		}
		else if(eI.getID() == ItemChangedEvent.ACTION_SAVE){
			if (eI.getSource() == this.panelBottomGruppe) {
				panelSplitGruppe.eventYouAreSelected(true);
			}else if (eI.getSource() == this.panelBottomBelegart) {
				panelSplitBelegart.eventYouAreSelected(false);
			}
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_DATEI_UEBERLEITEN)) {
			DelegateFactory.getInstance().getJCRDocDelegate().kopiereAlteDokumenteInJCR();
		} else if(e.getActionCommand().equals(MENU_ACTION_DATEI_VERSANDAUFTRAEGE_KOPIEREN)){
			DelegateFactory.getInstance().getJCRDocDelegate().kopiereVersandauftraegeInJCR();
		} else if(e.getActionCommand().equals(MENU_ACTION_DATEI_TREE_PRINT)) {
			printTree();
		} else if(e.getActionCommand().equals(MENU_ACTION_DATEI_TREE_EXPANDALL)) {
			expandAllTree();
		} else if(e.getActionCommand().equals(MENU_ACTION_DATEI_TREE_COMPRESSALL)) {
			compressAllTree();
		}

	}
	
	private void expandAllTree() {
		PanelDokumentenablage pda=(PanelDokumentenablage)this.getComponentAt(IDX_UEBERSICHT);
		if (pda == null) return;
		
		WrapperJTree tree = pda.getTree();
		if (tree == null) return;

		tree.expandAll(tree.getPathForRow(0), true);
	}

	private void compressAllTree() {
		PanelDokumentenablage pda=(PanelDokumentenablage)this.getComponentAt(IDX_UEBERSICHT);
		if (pda == null) return;
		
		WrapperJTree tree = pda.getTree();
		if (tree == null) return;

		tree.expandAll(tree.getPathForRow(0), false);
	}

	private void printTree() {

		PanelDokumentenablage pda=(PanelDokumentenablage)this.getComponentAt(IDX_UEBERSICHT);
		if (pda == null) return;
		
		WrapperJTree tree = pda.getTree();
		if (tree == null) return;

		tree.printContent();
		/*
		final int MAX_HEIGHT = 20000;   
		int treeWidth = 800;
		JTable printTree = new JTable(0, 1);   
		printTree.setSize(treeWidth, MAX_HEIGHT);   
		printTree.getColumnModel().getColumn(0).setWidth(treeWidth);   
		printTree.setGridColor(Color.WHITE);   
		DefaultTableModel printModel = (DefaultTableModel) printTree.getModel();   
		StringBuffer rowElement = new StringBuffer();  
		PanelDokumentenablage pda=(PanelDokumentenablage)this.getComponentAt(IDX_UEBERSICHT);
		if (pda == null) return;
		
		JTree tree = pda.getTree();
		if (tree == null) return;
		
		for (int i = 0; i < tree.getRowCount(); i++) {   
			TreePath path = tree.getPathForRow(i);   
			int level = path.getPathCount();   
			rowElement.delete(0, rowElement.length());   
			for (int j = 0; j < level; j++) {   
				rowElement.append("    ");   
			}   
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();   
			if(!node.isLeaf()){   
				rowElement.append(tree.isCollapsed(i) ? "+ " : "- ");   
			}   
			rowElement.append(node.toString());   
			Object[] rowData = new Object[]{rowElement.toString()};   
			printModel.addRow(rowData);   
		}   
		try {   
			printTree.print();//since 1.5   
		} catch (PrinterException ex) {   
			ex.printStackTrace();   
		}   
*/
	}
	

}
