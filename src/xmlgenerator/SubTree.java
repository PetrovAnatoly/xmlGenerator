/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Anatoly
 */
public class SubTree {
    //constructors
    private static int counter = 0;
    public SubTree(int minDepth, int maxDepth, int minBranch, int maxBranch){
        
        boolean hasChilds = getRandInt(minDepth, maxDepth) != 0;
        if (hasChilds){
            int childCount = getRandInt(minBranch, maxBranch);
            for (int i=0; i < childCount; i++){
                SubTree child = new SubTree(minDepth>0 ? minDepth-1:0, maxDepth-1, minBranch, maxBranch);
                childNodes.add(child);
            }
        }
        tag = "tag" + String.valueOf(counter);
        counter++;
    }

    public SubTree() {
        tag = "root";
    }
    public SubTree(String rootTag){
        tag = rootTag;
    }
    
    public void setAttributes(int minAttr, int maxAttr/*, String attrNameRegularExpr, String attrValueRegularExpr*/){
        int attrCount = getRandInt(minAttr, maxAttr);
        for (int i=0; i < attrCount; i++){
            //String attribute = getStringByRegularExpression(attrNameRegularExpr);
            //String value = getStringByRegularExpression(attrValueRegularExpr);
            attributes.put("attr"+String.valueOf(i), "val"+String.valueOf(i));
        }
        for (SubTree childNode : childNodes) {
            childNode.setAttributes(minAttr, maxAttr/*, String attrNameRegularExpr, String attrValueRegularExpr*/);
        }
    }
    //fields
    private String tag = new String();
    private String textContent = new String();
    private ArrayList<SubTree> childNodes = new ArrayList<>();
    private HashMap<String, String> attributes = new HashMap<>();
    
    //methods
    public void addChild(SubTree chld) {
        childNodes.add(chld);
    }
    public void removeChild(SubTree chld) { childNodes.remove(chld);}
    public void fill(int minDepth, int maxDepth, int minBranch, int maxBranch){
        boolean hasChilds = getRandInt(minDepth, maxDepth) != 0;
        if (hasChilds){
            int childCount = getRandInt(minBranch, maxBranch);
            for (int i=0; i < childCount; i++){
                SubTree child = new SubTree("tag" + String.valueOf(counter));
                counter++;
                child.fill(minDepth>0 ? minDepth-1:0, maxDepth-1, minBranch, maxBranch);
                childNodes.add(child);
            }
        }
    }
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
        test.setAttributes(1, 3);
        test.showAsTree();
    }
    public Document toDocument() throws ParserConfigurationException{
        Document document = XMLGenerator.getDocument("root");
        Element root = document.getDocumentElement();
        insertIntoElement(document, root);
        return document;
    }
    private void insertIntoElement(Document document, Element element){
        Element newElem = document.createElement(tag);
        for (String key: attributes.keySet()){
            newElem.setAttribute(key, attributes.get(key));
        }
        newElem.setTextContent(textContent);
        element.appendChild(newElem);
        for (SubTree childNode : childNodes) {
            childNode.insertIntoElement(document, newElem);
        }
    }

    public ArrayList<SubTree> getChildNodes() { return childNodes;}
    public String getTag() { return tag;}
    public String getTextContent(){ return textContent;}

    public String getAttributes() {
        return attributes.toString();
    }
    
}
