/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author Anatoly
 */
public class ArrayListForNames <T extends Object> extends ArrayList{

    ArrayListForNames(TreeSet<String> nmes) {
        super(nmes);
    }

    ArrayListForNames() {
        super();
    }
    @Override
    public boolean equals(Object arg){
        return this == arg;
    }
}
