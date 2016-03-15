/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author Anatoly
 */
public class XMLGenerator {
    public static Document getDocument(String rootName) throws ParserConfigurationException{
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.appendChild(document.createElement(rootName));
        return document;
    }
    private static ArrayList<String> foundTags = new ArrayList<>();
    public static ArrayList<Integer> getIntegers(String[] intervalsAndPoints){
        foundTags = new ArrayList<>();
        ArrayList<Integer> rtrn = new ArrayList<>();
        for (String s: intervalsAndPoints){
            if (s.contains("-")){
                String rightStr = s.substring(s.indexOf("-")+1);
                String leftStr = s.replace("-"+rightStr, "");
                Integer left, right;
                try{
                    left = Integer.valueOf(leftStr.trim());
                    right = Integer.valueOf(rightStr.trim());
                    for (int i = left; i<=right; i++)
                        rtrn.add(i);
                }
                catch(NumberFormatException exc){foundTags.add(s.trim());}
            }
            else{
                try{
                    Integer lvl = Integer.valueOf(s.trim());
                    rtrn.add(lvl);
                }
                catch(NumberFormatException exc){if (!s.trim().isEmpty()) foundTags.add(s.trim());}
            }
        }
        return rtrn;
    }
    public static ArrayList<String> getTagsInLastGetIntegerCalling(){
        return foundTags;
    }
    private static int getRandInt(int from, int to){
        return from + (new Random()).nextInt(to-from+1);
    }
    public static int getRandomInt(String[] intervalsAndPoints){
        int rtrn = 0;
        int count = intervalsAndPoints.length;
        String selectedStr = intervalsAndPoints[getRandInt(0, count-1)];
        if (selectedStr.contains("-")){
            String rightStr = selectedStr.substring(selectedStr.indexOf("-") + 1);
            String leftStr = selectedStr.replace("-"+rightStr, "");
            Integer left = Integer.valueOf(leftStr.trim());
            Integer right = Integer.valueOf(rightStr.trim());
            rtrn = getRandInt(left, right);
        }
        else
            rtrn = Integer.valueOf(selectedStr.trim());
        return rtrn;
    }
    public static void writeDocument(String path,Document document) throws Exception {
        
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream(path);
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        
    }
}
