/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author Anatoly
 */
public class SubTree {
    //constructors
    public SubTree(int minDepth, int maxDepth, int minBranch, int maxBranch){
        
        boolean hasChilds = getRandInt(minDepth, maxDepth) != 0;
        if (hasChilds){
            int childCount = getRandInt(minBranch, maxBranch);
            for (int i=0; i < childCount; i++){
                SubTree child = new SubTree(minDepth>0 ? minDepth-1:0, maxDepth-1, minBranch, maxBranch);
                childNodes.add(child);
            }
        }
        tag = "tratata";
    }
    public void setAttributes(int minAttr, int maxAttr, String attrNameRegularExpr){
        int attrCount = getRandInt(minAttr, maxAttr);
        for (int i=0; i < attrCount; i++){
            String attribute = getStringByRegularExpression(attrNameRegularExpr);
            attributes.add(attribute);
        }
        for (int i=0; i < childNodes.size(); i++){
            childNodes.get(i).setAttributes(minAttr, maxAttr, attrNameRegularExpr);
        } 
    }
    //fields
    private String tag = new String();
    private String textContent = new String();
    private ArrayList<SubTree> childNodes = new ArrayList<>();
    private ArrayList<String> attributes = new ArrayList<>();
    
    //methods
    private int getRandInt(int from, int to){
        return from + (new Random()).nextInt(to-from+1);
    }
    private String getStringByRegularExpression(String regularExpression){
        //will be soon
        return regularExpression;
    }
    private static int shift = 0;
    private void showAsTree()
    {
        for (int i = 0; i < shift; i++) System.out.print("\t");
        System.out.print(tag);
        System.out.println(attributes);
        shift++;
        for (SubTree ch: childNodes) 
            ch.showAsTree();
        shift--;
    }
    public static void main(String[] argv){
        SubTree test = new SubTree(1,4,1,4);
        test.setAttributes(1, 3, "dfvc");
        test.showAsTree();
    }
}
