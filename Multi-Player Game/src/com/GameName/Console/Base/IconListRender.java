package com.GameName.Console.Base;
import java.awt.Component;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;


public class IconListRender<E> extends DefaultListCellRenderer {
	private static final long serialVersionUID = 4818723415882059631L;
	private Map<E, Icon> icons = null;
	
	public IconListRender(Map<E, Icon> icons) {
		this.icons = icons;
	}
	
	public void addIcon(E key, Icon icon) {
		icons.put(key, icon);
	}
	
	@SuppressWarnings("rawtypes")
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		Icon icon = icons.get(value);
		label.setIcon(icon);
		return label;
	}
}
