/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anatoly
 */
public class TextContentTableModel extends DefaultTableModel{
    public TextContentTableModel(){ setColumnIdentifiers(headers);}
    private static final Object[] headers = {"levels/tags", "content"};
    private static final Object[] rowData = {"",""};
    public void addRow(){ this.addRow(rowData);}
}
