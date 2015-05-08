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

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.service.StklImportSpezifikation;

public class StklImportPage2View extends AssistentPageView {
	
	private static final long serialVersionUID = 9166128337607135933L;
	
	private StklImportPage2Ctrl controller;

	private JSpinner fromRow;
	private WrapperRadioButton separated;
	private WrapperRadioButton fixedWidth;
	private JTextField separatorChar;
	private WrapperComboBox montageart;
	private ColumnDefinitionTable table;
	private FixedWidthDefinitionField fwdField;
	private JPanel viewPanel;
	private Listener l = new Listener();
	private JPanel columnTypePanel;
	private DraggableLabel draggingLabel;
	private JLayeredPane layeredPane;
	private IColumnTypeDefiner definer;

	public StklImportPage2View(StklImportPage2Ctrl controller, InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
	}

	@Override
	public void dataUpdated() {
		final StklImportSpezifikation spezifikation = getController().getImportSpezifikation();
		fromRow.setValue(spezifikation.getFromRow());
		separated.setSelected(!spezifikation.isFixedWidth());
		fixedWidth.setSelected(spezifikation.isFixedWidth());
		montageart.setSelectedItem(getController().getMontageart());
		
		if(!separatorChar.getText().equals(getController().getImportSpezifikation().getSeparator()))
			separatorChar.setText(spezifikation.getSeparator());
		
		separatorChar.setEditable(!spezifikation.isFixedWidth());
		if(spezifikation.isFixedWidth()) {
			Rectangle viewRect = fwdField != null ? fwdField.getVisibleRect() : null;
			initFixedWidthDefinitionField();
			fwdField.setSpezifikation(spezifikation);
			fwdField.setLines(getController().getImportLines());
			if(viewRect != null)
				fwdField.scrollRectToVisible(viewRect);
		} else {
			Rectangle viewRect = table != null ? table.getVisibleRect() : null;
			initTable();
			table.setModel(getController().getTableModel());
			table.setSpezifikation(spezifikation);
			if(viewRect != null)
				table.scrollRectToVisible(viewRect);
		}
		updateColumnTypes();
	}
	
	protected void updateColumnTypes() {
		columnTypePanel.removeAll();
		try {
			for(String type : getController().getAvailableColumnTypes()) {
				columnTypePanel.add(createDraggableType(type, type));
			} 
		} catch(Throwable t) {
			getInternalFrame().handleException(t, true);
		}
		columnTypePanel.invalidate();
		columnTypePanel.validate();
		
		
	}

	@Override
	public StklImportPage2Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport");
	}
	
	protected void initTable() {
		table = new ColumnDefinitionTable(controller.getTableModel());
		setComponentToViewPanel(new JScrollPane(table));
		setDefiner(table);
	}
	
	protected void initFixedWidthDefinitionField() {
		fwdField = new FixedWidthDefinitionField();
		setComponentToViewPanel(new JScrollPane(fwdField));
		setDefiner(fwdField);
	}
	
	protected void setDefiner(IColumnTypeDefiner definer) {
		this.definer = definer;
		this.definer.addDefinitionListener(l);
	}
	
	protected void setComponentToViewPanel(Component c) {
		viewPanel.removeAll();
		viewPanel.setLayout(new MigLayout("fill", "[fill]", "[fill]"));
		viewPanel.add(c);
		viewPanel.invalidate();
		viewPanel.validate();
	}

	@Override
	protected void initViewImpl() {
		JInternalFrame iFrame = (JInternalFrame)SwingUtilities.getAncestorOfClass(JInternalFrame.class, this);
		layeredPane = iFrame.getLayeredPane();
		fromRow = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		separated = new WrapperRadioButton(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.trennzeichen"));
		fixedWidth = new WrapperRadioButton(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.fixebreite"));
		separatorChar = new JTextField(2);
		separatorChar.setToolTipText(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.tooltiptrennzeichen"));
		viewPanel = new JPanel();
		columnTypePanel = new JPanel(new FlowLayout());
		try {
			montageart = new WrapperComboBox(getController().getMontagearten());
			montageart.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = -2161585903464659240L;

				@Override
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					MontageartDto montageart = (MontageartDto) value;
					if(montageart != null) value = montageart.getCBez();
					return super.getListCellRendererComponent(list, value, index, isSelected,
							cellHasFocus);
				}
			});
			montageart.setMandatoryField(true);
		} catch (Throwable e) {
			getInternalFrame().handleException(e, true);
			return;
		}
		
		ButtonGroup bg = new ButtonGroup();		
		bg.add(separated);
		bg.add(fixedWidth);

		fromRow.addChangeListener(l);
		separated.addActionListener(l);
		fixedWidth.addActionListener(l);
		separatorChar.addKeyListener(l);
		separatorChar.addFocusListener(l);
		montageart.addItemListener(l);
		
		setLayout(new MigLayout("wrap 3, hidemode 3, fillx", "[fill][fill]push[]", "[][][][][fill, grow]"));
		
		add(new JLabel(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.abzeile")));
		add(fromRow);
		add(columnTypePanel, "span 1 4, w 0:pref:max, growy");
		
		add(separated);
		add(separatorChar, "wrap");
		
		add(fixedWidth, "wrap");
		if(getController().showMontagearten()) {
			add(new JLabel(LPMain.getTextRespectUISPr("stkl.montageart")));
			add(montageart, "wrap");
			add(viewPanel, "span");
		} else {
			add(viewPanel, "newline, span");
		}
		

	}
	
	protected DraggableLabel createDraggableType(String text, final String key) {
		DraggableLabel label = new DraggableLabel(text, key);
		label.addMouseListener(l);
		return label;
	}
	
	@Override
	protected boolean mustInitView() {
		return true;
	}
	
	protected void highlightColumnAt(Point p) {
		definer.highlightColumnAt(p);
	}
	
	protected void setColumnTypeAt(Point p, String type) {
		definer.setColumnTypeAt(p, type);
	}
	
	private class Listener implements ActionListener, ChangeListener, KeyListener,
	FocusListener, MouseListener, MouseMotionListener, IDefinitionListener, ItemListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			getController().setFixedWidth(fixedWidth.isSelected());
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			getController().setFromRow((Integer)fromRow.getValue());
		}

		@Override
		public void keyPressed(KeyEvent arg0) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				getController().setSeparatorChar(separatorChar.getText());
		}

		@Override
		public void keyTyped(KeyEvent arg0) {}

		@Override
		public void focusGained(FocusEvent arg0) {}

		@Override
		public void focusLost(FocusEvent arg0) {
			getController().setSeparatorChar(separatorChar.getText());
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {
			if(draggingLabel!=null && e.getSource() == layeredPane) {
				Point p = SwingUtilities.convertPoint(draggingLabel, e.getPoint(), layeredPane);
				p.x-=draggingLabel.getClickPoint().x;
				p.y-=draggingLabel.getClickPoint().y;
				draggingLabel.setLocation(p);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getSource() != draggingLabel && e.getSource() instanceof DraggableLabel) {
				if(draggingLabel != null)
					layeredPane.remove(draggingLabel);
				draggingLabel = (DraggableLabel) e.getSource();
				draggingLabel.setMinimumSize(draggingLabel.getSize());
				draggingLabel.setPreferredSize(draggingLabel.getSize());
				draggingLabel.setClickPoint(e.getPoint());
				
				Point p = SwingUtilities.convertPoint(draggingLabel.getParent(), draggingLabel.getLocation(), layeredPane);
				
				layeredPane.add(draggingLabel, JLayeredPane.DRAG_LAYER, -1);
				layeredPane.moveToFront(draggingLabel);
				draggingLabel.setBounds(p.x, p.y, draggingLabel.getWidth(), draggingLabel.getHeight());
				draggingLabel.addMouseListener(l);
				draggingLabel.addMouseMotionListener(l);
				layeredPane.repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getSource() == draggingLabel) {
				Point pView = SwingUtilities.convertPoint(draggingLabel, e.getPoint(), (Component)definer);
				highlightColumnAt(null);
				setColumnTypeAt(pView, draggingLabel.getKey());
				layeredPane.remove(draggingLabel);
				layeredPane.repaint();
				updateColumnTypes();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(e.getSource() == draggingLabel) {
				Point p = SwingUtilities.convertPoint(draggingLabel, e.getPoint(), layeredPane);
				p.x-=draggingLabel.getClickPoint().x;
				p.y-=draggingLabel.getClickPoint().y;
				draggingLabel.setLocation(p);
				Point pView = SwingUtilities.convertPoint(draggingLabel, e.getPoint(), (Component)definer);
				highlightColumnAt(pView);
				layeredPane.repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void addedColumnSeparator(Integer column) {
			getController().toggleColumnWidth(column);
		}

		@Override
		public void removedColumnSeparator(Integer column) {
			getController().toggleColumnWidth(column);
		}
		
		@Override
		public void updateColumnType(int index, String type) {
			getController().setColumnType(index, type);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getSource() == montageart) {
				getController().setMontageart((MontageartDto)montageart.getSelectedItem());
			}
		}
	}
	

}
