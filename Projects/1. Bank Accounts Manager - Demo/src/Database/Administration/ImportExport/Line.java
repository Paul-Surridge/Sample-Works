package Database.Administration.ImportExport;

import Shared.Constants;
import Shared.Formatter;
import java.util.ArrayList;
import java.util.List;

public class Line implements Constants
{
    List<String> lstErrors = new ArrayList<>();
    String sLine, sDate, sEntryName, sType, sCategoryName, sOut, sIn, sBalance, sDirection, sLastDate, sLastAmount;
    String sRefundName, sKeywords, sAmount, sReceivedDate, sReceivedPrimaryKey;
    int iType;
    int iDate, iIn, iOut, iBalance, iDirection, iLastDate, iLastAmount, iAmount, iReceivedDate, iDateAlert;
    long lReceivedPrimaryKey;
    
    public Line(int iType, String sLine)
    {
        this.iType = iType;
        this.sLine = sLine;
        
        parseLine();
        
        if(!hasError())
            checkFieldsFormat();
        
        if(!hasError())
            switch(iType)
            {
                case DATABASE_IMPORT_TYPE_ENTRY:            checkInOutAndSetDirection();
                case DATABASE_IMPORT_TYPE_CATEGORY: 
                case DATABASE_IMPORT_TYPE_AUTO_CATEGORY:
                case DATABASE_IMPORT_TYPE_WATCH:            setDirection();
            }
    }
    
    //Internal -----------------------------------------------------------------
    private List<String> extractFields(String sLine)
    {
        List<String> lstFields = new ArrayList<>();
        String[] sFragments = sLine.split("\t");
        
        for(String s : sFragments)
            if(!s.equals(""))
                lstFields.add(s.trim());
        
        return lstFields;
    }
    private void addError(String sError)
    {
        lstErrors.add(sError);
    }
    
    //Field Format Checking
    private boolean isDate(String s)
    {
        if(s.length() != 8)
            return false;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    private boolean isBalance(String s)
    {
        int iPosStart = 0;
        
        if(s.isEmpty() || isDash(s))
            return false;
        
        if(s.charAt(0) == '-')
            iPosStart = 1;
        
        for(int i = iPosStart ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    private boolean isInOut(String s)
    {
        if(s.isEmpty())
            return false;
        
        if(isDash(s))
            return true;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    private boolean isType(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_4;
    }
    private boolean isEntryName(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_256;
    }
    private boolean isCategoryName(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_256;
    }
    private boolean isRefundName(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_256;
    }
    private boolean isKeywords(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_1024;
    }
    private boolean isBlankableDate(String s)
    {
        if(s.equals(DASH))
            return true;
        
        if(s.length() != 8)
            return false;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    private boolean isPrimaryKey(String s)
    {
        if(s.equals(DASH))
            return true;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    private boolean isDash(String s)
    {
        return s.equals(DASH);
    }
    private boolean isDirection(String s)
    {
        return s.equals(IN_TEXT) || s.equals(OUT_TEXT);
    }
    private void checkFieldsFormat()
    {
        String sPrefix = "";
        
        switch(iType)
        {
            case DATABASE_IMPORT_TYPE_ENTRY:                sPrefix = "(Entry): ";          break;
            case DATABASE_IMPORT_TYPE_AUTO_CATEGORY:        sPrefix = "(Auto Cat.): ";      break;
            case DATABASE_IMPORT_TYPE_CATEGORY:             sPrefix = "(Category): ";       break;
            case DATABASE_IMPORT_TYPE_WATCH:                sPrefix = "(Watch): ";          break;
            case DATABASE_IMPORT_TYPE_REFUND:               sPrefix = "(Refund): ";         break;
        }
        
        switch(iType)
        {
            case DATABASE_IMPORT_TYPE_ENTRY:
            {
                if(!isEntryName(sEntryName))            addError(sPrefix + "Entry name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                if(!isType(sType))                      addError(sPrefix + "Type exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_4);
                if(!isCategoryName(sCategoryName))      addError(sPrefix + "Category name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                
                if(isDate(sDate))                       iDate = Formatter.convert(DATE, FILE, DATABASE, sDate);
                else                                    addError(sPrefix + "Date incorrectly formatted");
                
                if(isInOut(sIn))                        iIn = Formatter.convert(CURRENCY, FILE, DATABASE, sIn);
                else                                    addError(sPrefix + "In incorrectly formatted");
                
                if(isInOut(sOut))                       iOut = Formatter.convert(CURRENCY, FILE, DATABASE, sOut);
                else                                    addError(sPrefix + "Out incorrectly formatted");
                
                if(isBalance(sBalance))                 iBalance = Formatter.convert(CURRENCY, FILE, DATABASE, sBalance);
                else                                    addError(sPrefix + "Balance incorrectly formatted");
                
                break;
            }
            case DATABASE_IMPORT_TYPE_AUTO_CATEGORY:
            {
                if(!isEntryName(sEntryName))            addError(sPrefix + "Entry name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                if(!isDirection(sDirection))            addError(sPrefix + "Direction unknown");
                if(!isCategoryName(sCategoryName))      addError(sPrefix + "Category name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                
                break;
            }
            case DATABASE_IMPORT_TYPE_CATEGORY:
            {
                if(!isCategoryName(sCategoryName))      addError(sPrefix + "Category name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                if(!isDirection(sDirection))            addError(sPrefix + "Direction unknown");
                
                break;
            }
            case DATABASE_IMPORT_TYPE_WATCH:
            {
                if(!isCategoryName(sCategoryName))      addError(sPrefix + "Category name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                if(!isDirection(sDirection))            addError(sPrefix + "Direction unknown");
                
                if(isBlankableDate(sLastDate))          iLastDate = Formatter.convert(DATE, FILE, DATABASE, sLastDate);
                else                                    addError(sPrefix + "Last Date incorrectly formatted");
                
                if(isInOut(sLastAmount))                iLastAmount = Formatter.convert(CURRENCY, FILE, DATABASE, sLastAmount);
                else                                    addError(sPrefix + "Last Amount incorrectly formatted");
                
                break;
            }
            case DATABASE_IMPORT_TYPE_REFUND:
            {
                if(isDate(sDate))                       iDate = Formatter.convert(DATE, FILE, DATABASE, sDate);
                else                                    addError(sPrefix + "Date incorrectly formatted");
                
                if(!isRefundName(sRefundName))          addError(sPrefix + "Refund name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
                if(!isKeywords(sKeywords))              addError(sPrefix + "Keywords exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_1024);
                
                if(isInOut(sAmount))                    iAmount = Formatter.convert(CURRENCY, FILE, DATABASE, sAmount);
                else                                    addError(sPrefix + "Amount incorrectly formatted");
                
                if(isBlankableDate(sReceivedDate))      iReceivedDate = Formatter.convert(DATE, FILE, DATABASE, sReceivedDate);
                else                                    addError(sPrefix + "Received Date incorrectly formatted");
                
                if(isPrimaryKey(sReceivedPrimaryKey))   lReceivedPrimaryKey = sReceivedPrimaryKey.equals(DASH) ? NOT_DEFINED : Long.valueOf(sReceivedPrimaryKey);
                else                                    addError(sPrefix + "Received Primary Key incorrectly formatted");
                
                break;
            }
        }
    }
    private void checkInOutAndSetDirection()
    {
        if(!isDash(sOut) && isDash(sIn))
        {
            iDirection = OUT;
            sDirection = OUT_TEXT;
        }
        else if (isDash(sOut) && !isDash(sIn))
        {
            iDirection = IN;
            sDirection = IN_TEXT;
        }
        else if (isDash(sOut) &&  isDash(sIn))    addError("In and Out are both undefined (" + DASH + ")");
        else if (!isDash(sOut) && !isDash(sIn))   addError("In/Out are both values, x1 should be undefined (" + DASH + ")");
    }
    private void setDirection()
    {
        switch(sDirection)
        {
            case IN_TEXT:   iDirection = IN;            break;
            case OUT_TEXT:  iDirection = OUT;           break;
            default:        iDirection = NOT_DEFINED;
        }
    }
    private void parseLine()
    {
        List<String> lstFields = extractFields(sLine);
        
        switch(iType)
        {
            case DATABASE_IMPORT_TYPE_ENTRY:
            {
                if(lstFields.size() == 8)
                {
                    sDate           = lstFields.get(1);
                    sEntryName      = lstFields.get(2);
                    sType           = lstFields.get(3);
                    sCategoryName   = lstFields.get(4);
                    sIn             = lstFields.get(5);
                    sOut            = lstFields.get(6);
                    sBalance        = lstFields.get(7);
                }
                else
                    addError("Entry fields of unexpected number in total: " + lstFields.size());
                
                break;
            }
            case DATABASE_IMPORT_TYPE_CATEGORY:
            {
                if(lstFields.size() == 3)
                {
                    sCategoryName   = lstFields.get(1);
                    sDirection      = lstFields.get(2);
                }
                else
                    addError("Category fields of unexpected number in total: " + lstFields.size());
                
                break;
            }
            case DATABASE_IMPORT_TYPE_AUTO_CATEGORY:
            {
                if(lstFields.size() == 4)
                {
                    sEntryName      = lstFields.get(1);
                    sDirection      = lstFields.get(2);
                    sCategoryName   = lstFields.get(3);
                }
                else
                    addError("Category fields of unexpected number in total: " + lstFields.size());
                
                break;
            }
            case DATABASE_IMPORT_TYPE_WATCH:
            {
                if(lstFields.size() == 5)
                {
                    sCategoryName   = lstFields.get(1);
                    sDirection      = lstFields.get(2);
                    sLastDate       = lstFields.get(3);
                    sLastAmount     = lstFields.get(4);
                }
                else
                    addError("Watch fields of unexpected number in total: " + lstFields.size());
                
                break;
            }
            case DATABASE_IMPORT_TYPE_REFUND:
            {
                if(lstFields.size() == 7)
                {
                    sDate                   = lstFields.get(1);
                    sRefundName             = lstFields.get(2);
                    sKeywords               = lstFields.get(3);
                    sAmount                 = lstFields.get(4);
                    sReceivedDate           = lstFields.get(5);
                    sReceivedPrimaryKey     = lstFields.get(6);
                }
                else
                    addError("Refund fields of unexpected number in total: " + lstFields.size());
                
                break;
            }
        }
    }
        
    //External API -------------------------------------------------------------
    public int getDate()
    {
        return iDate;
    }
    public String getEntryName()
    {
        return sEntryName;
    }
    public String getType()
    {
        return sType;
    }
    public String getCategoryName()
    {
        return sCategoryName;
    }
    public int getIn()
    {
        return iIn;
    }
    public int getOut()
    {
        return iOut;
    }
    public int getBalance()
    {
        return iBalance;
    }
    public int getDirection()
    {
        return iDirection;
    }
    public String getDirectionText()
    {
        return sDirection;
    }
    public int getLastDate()
    {
        return iLastDate;
    }
    public int getLastAmount()
    {
        return iLastAmount;
    }
    public String getRefundName()
    {
        return sRefundName;
    }
    public String getKeywords()
    {
        return sKeywords;
    }
    public int getAmount()
    {
        return iAmount;
    }
    public int getReceivedDate()
    {
        return iReceivedDate;
    }
    public long getReceivedPrimaryKey()
    {
        return lReceivedPrimaryKey;
    }
    public int getDateAlert()
    {
        return iDateAlert;
    }
    
    //Generated Errors
    public List<String> getErrors()
    {
        return lstErrors;
    }
    public final boolean hasError()
    {
        return !lstErrors.isEmpty();
    }
}
