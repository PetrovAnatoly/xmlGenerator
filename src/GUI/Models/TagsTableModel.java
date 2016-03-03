/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anatoly
 */
public class TagsTableModel extends DefaultTableModel{
    public TagsTableModel(){ setColumnIdentifiers(headers);}
    private static final Object[] headers = {"tag name", "amount in level", "levels"};
    private static final Object[] rowData = {"","",""};
    public void addRow(){ this.addRow(rowData);}
}
