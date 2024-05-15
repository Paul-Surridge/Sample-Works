package Statement;

import Shared.Constants;
import Shared.Formatter;
import java.util.ArrayList;
import java.util.List;

public class Line implements Constants
{
    private List<String> lstErrors = new ArrayList<>();
    private String sLine, sDate, sType, sName, sOut, sIn, sBalance;
    private int iOut, iIn, iBalance, iDirection, iNumberOfFields;
    
    public Line(String sLine)
    {
        this.sLine = sLine;
        
        parseLine();
        
        if(!hasError())
            checkFieldsFormat();
        
        if(!hasError())
            iDirection = checkInOutAndFindDirection();
    }
    
    //Internal -----------------------------------------------------------------
    private boolean isBlankSpace(String s)
    {
        for(int i = 0 ; i<s.length() ; i++)
            if(s.charAt(i) != ' ')
                return false;
        
        return true;
    }
    private List<String> extractFields(String sLine)
    {
        List<String> lstFields = new ArrayList<>();
        String[] sFragments = sLine.split("\t");
        
        for(String s : sFragments)
            if(!s.equals("") && !isBlankSpace(s))
                lstFields.add(s.trim());
        
        return lstFields;
    }
    private void addError(String sError)
    {
        lstErrors.add(sError);
    }
    
    //Field Format Checking
    private boolean checkCurrencyFormat(String sField, String s)
    {
        for(int i = s.length()-1, iCounter = 0 ; i>=0 ; i--, iCounter++)
        {
            char c = s.charAt(i);
            
            if(iCounter == 2)
            {
                if(c != '.')
                {
                    addError(sField + " does not have a decimal point in expected location");
                    return false;
                }
            }
            else if(((iCounter - 6) % 4) == 0)
            {
                if(c != ',')
                {
                    addError(sField + " does not have a comma in expected location");
                    return false;
                }
            }
            else if(!Character.isDigit(c))
            {
                addError(sField + " does not have a numerical value 0-9 in expected location");
                return false;
            }
        }
        
        return true;
    }
    private boolean isMonth(String s)
    {
        switch(s.toLowerCase())
        {
            case "jan":
            case "feb":
            case "mar":
            case "apr":
            case "may":
            case "jun":
            case "jul":
            case "aug":
            case "sep":
            case "oct":
            case "nov":
            case "dec":     return true;
            default:        return false;
        }
    }
    private boolean isDate(String s)
    {
        if(s.isEmpty())
            return true;
        
        if(s.length() != 9)
            return false;
        
        for(int i = 0 ; i<2 ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        if(!isMonth(s.substring(3, 6)))
            return false;
        
        for(int i = 7 ; i<9 ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    private boolean isInOut(String sField, String s)
    {
        if(s.equals("") || s.equals("-"))
            return true;
        
        if(s.contains("-"))
        {
            addError(sField + " contains a minus sign");
            return false;
        }
        
        if(s.equals("0.00"))
        {
            addError(sField + " contains no value (0.00)");
            return false;
        }
        
        if(s.length()<4)
        {
            addError(sField + " is less than minimum expected length of x4 characters");
            return false;
        }
        
        return checkCurrencyFormat(sField, s);
    }
    private boolean isBalance(String sField, String s)
    {
        if(s.equals("") || s.equals("-"))
            return true;
        
        if(s.length()<4)
        {
            addError(sField + " is less than minimum expected length of x4 characters");
            return false;
        }
        
        if(s.charAt(0) == '-')
            s = s.substring(1);
        
        return checkCurrencyFormat(sField, s);
    }
    private boolean isType(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_8;
    }
    private boolean isName(String s)
    {
        return s.length() <= DATABASE_RECORD_MAXIMUM_LENGTH_256;
    }
    private boolean isDash(String s)
    {
        return s.equals(DASH);
    }
    private void checkFieldsFormat()
    {
        if(isDate(sDate))           sDate = Formatter.setMonthAsCamelCase(sDate);
        else                        addError("Date incorrectly formatted");
        
        if(!isType(sType))
            addError("Type exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_8);
        if(!isName(sName))
            addError("Name exceed maximum length of " + DATABASE_RECORD_MAXIMUM_LENGTH_256);
        
        if(isInOut("Out", sOut))
            iOut = Formatter.convert(CURRENCY, STATEMENT, DATABASE, sOut);
        
        if(isInOut("In", sIn))
            iIn = Formatter.convert(CURRENCY, STATEMENT, DATABASE, sIn);
        
        if(isBalance("Balance", sBalance))
            iBalance = Formatter.convert(CURRENCY, STATEMENT, DATABASE, sBalance);
    }
    private int checkInOutAndFindDirection()
    {
        if(isDash(sOut) && !isDash(sIn))  return IN;
        if(!isDash(sOut) && isDash(sIn))  return OUT;
        
        if(isDash(sOut) && isDash(sIn))
            addError("In/Out fields are both undefined");
        if(!isDash(sOut) && !isDash(sIn))
            addError("In/Out fields are both values, only x1 should be undefined");
        
        return NOT_DEFINED;
    }
    private void parseLine()
    {
        List<String> lstFields = extractFields(sLine);
        
        switch(lstFields.size())
        {
            case 4:
            {
                sDate       = "";
                sType       = lstFields.get(0);
                sName       = lstFields.get(1);
                sOut        = lstFields.get(2);
                sIn         = lstFields.get(3);
                sBalance    = "";
                
                break;
            }
            case 5:
            {
                if(sLine.charAt(0) == ' ' || sLine.charAt(0) == '\t')
                {
                    sDate       = "";
                    sType       = lstFields.get(0);
                    sName       = lstFields.get(1);
                    sOut        = lstFields.get(2);
                    sIn         = lstFields.get(3);
                    sBalance    = lstFields.get(4);
                }
                else
                {
                    sDate       = lstFields.get(0);
                    sType       = lstFields.get(1);
                    sName       = lstFields.get(2);
                    sOut        = lstFields.get(3);
                    sIn         = lstFields.get(4);
                    sBalance    = "";
                }
                
                break;
            }
            case 6:
            {
                sDate       = lstFields.get(0);
                sType       = lstFields.get(1);
                sName       = lstFields.get(2);
                sOut        = lstFields.get(3);
                sIn         = lstFields.get(4);
                sBalance    = lstFields.get(5);

                break;
            }
            default:
            {
                addError("Unexpected number of fields: " + lstFields.size());
            }
        }
        
        iNumberOfFields = lstFields.size();
    }
    
    //External API -------------------------------------------------------------
    
    //State
    public String getDate()
    {
        return sDate;
    }
    public void setDate(String sDate)
    {
        this.sDate = sDate;
    }
    public String getType()
    {
        return sType;
    }
    public String getName()
    {
        return sName;
    }
    public int getOut()
    {
        return iOut;
    }
    public int getIn()
    {
        return iIn;
    }
    public int getBalance()
    {
        return iBalance;
    }
    public void setBalance(int iBalance)
    {
        this.iBalance = iBalance;
    }
    public int getNumberOfFields()
    {
        return iNumberOfFields;
    }
    public int getDirection()
    {
        return iDirection;
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
