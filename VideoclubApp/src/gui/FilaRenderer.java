package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FilaRenderer extends DefaultTableCellRenderer{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		Component componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(isSelected){
			componente.setBackground(table.getSelectionBackground());
			componente.setForeground(table.getSelectionForeground());
		}
		else {
			if(row %2 ==0) {
				componente.setBackground(Color.WHITE);
			}
			else {
				componente.setBackground(Color.LIGHT_GRAY);
			}
		}
		return componente;
	}
}
