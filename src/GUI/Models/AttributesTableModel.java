/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anatoly
 */
public class AttributesTableModel extends DefaultTableModel{
    public AttributesTableModel(){ setColumnIdentifiers(headers);}
    private static final Object[] headers = {"name", "value", "levels/tags"};
    private static final Object[] rowData = {"","",""};
    public void addRow(){ this.addRow(rowData);}
}
