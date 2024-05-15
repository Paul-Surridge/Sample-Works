/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.codeEditor;

import SampleCode.shared.Constants;
/**
 *
 * @author PaulSu
 */
public class SearchItem implements Constants {

    private final int iAnchor, iRows, iLength;        

    public SearchItem(int iAnchor, int iRows, int iLength)
    {
        this.iAnchor    = iAnchor;
        this.iRows      = iRows;
        this.iLength    = iLength;
    }

    
    //External Interface
    public int getAnchor()
    {
        return iAnchor;
    }
    public int getRows()
    {
        return iRows;
    }
    public int getLength()
    {
        return iLength;
    }
}