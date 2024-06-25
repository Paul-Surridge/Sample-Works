package Shared;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import History.Entry;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Formatter implements Constants{
    
    //Internal -----------------------------------------------------------------
    
    //Formatting
    private static String findMonthNumber(String sMonth)
    {
        if(sMonth.contains("Jan"))          return "01";
        else if(sMonth.contains("Feb"))     return "02";
        else if(sMonth.contains("Mar"))     return "03";
        else if(sMonth.contains("Apr"))     return "04";
        else if(sMonth.contains("May"))     return "05";
        else if(sMonth.contains("Jun"))     return "06";
        else if(sMonth.contains("Jul"))     return "07";
        else if(sMonth.contains("Aug"))     return "08";
        else if(sMonth.contains("Sep"))     return "09";
        else if(sMonth.contains("Oct"))     return "10";
        else if(sMonth.contains("Nov"))     return "11";
        else if(sMonth.contains("Dec"))     return "12";
        
        return "";
    }
    private static String findMonthName(String sMonth)
    {
        switch(sMonth)
        {
            case "01":      return "Jan";
            case "02":      return "Feb";
            case "03":      return "Mar";
            case "04":      return "Apr";
            case "05":      return "May";
            case "06":      return "Jun";
            case "07":      return "Jul";
            case "08":      return "Aug";
            case "09":      return "Sep";
            case "10":      return "Oct";
            case "11":      return "Nov";
            case "12":      return "Dec";
        }
        
        return "";
    }
    private static String findMonthName(int iMonth)
    {
        switch(iMonth)
        {
            case 1:     return "Jan";
            case 2:     return "Feb";
            case 3:     return "Mar";
            case 4:     return "Apr";
            case 5:     return "May";
            case 6:     return "Jun";
            case 7:     return "Jul";
            case 8:     return "Aug";
            case 9:     return "Sep";
            case 10:    return "Oct";
            case 11:    return "Nov";
            case 12:    return "Dec";
        }
        
        return "";
    }
    private static String insertCommas(String sAmount)
    {
        int iInitial = sAmount.length() % 3;
        String sBuild;
        
        sBuild = sAmount.substring(0, iInitial);
        
        for(int i = iInitial ; i<sAmount.length() ; i = i+3)
        {
            if(i>0)
                sBuild += ",";
            sBuild += sAmount.substring(i, i+3);
        }
        
        return sBuild;
    }
    private static String removeLeadingZeros(String s)
    {
        for(int i = 0 ; i<s.length() ; i++)
            if(s.charAt(i) != '0')
                return s.substring(i);
        return s;
    }
    private static String padFraction(String s)
    {
        if(s.isEmpty())
            return s;
        
        if(s.charAt(s.length()-2) == '.')
            s += "0";
        
        return s;
    }
    private static boolean fractionContainNonZero(String s)
    {
        int iPointPosition = 0;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(s.charAt(i) == '.')
                iPointPosition = i;
                
        for(int i = iPointPosition+1 ; i<s.length() ; i++)
            if(s.charAt(i) != '0')
                return true;
        
        return false;
    }
    
    //Chart Title
    private static String sortAndBuildTitle(List<String> lst)
    {
        List<String> lstSorted = new ArrayList<>();
        String s = "";
        
        for(Account a : Accounts.getAccounts())
            if(lst.contains(a.getNameAbbreviated()))
                lstSorted.add(a.getNameAbbreviated());
        
        for(int i = 0 ; i<lstSorted.size() ; i++)
                if(i == 0)                              s += lstSorted.get(i);
                else if(i == lstSorted.size()-1)        s += " and " + lstSorted.get(i);
                else                                    s += ", " + lstSorted.get(i);
        
        return s;
    }
    
    //Currency -----------------------------------------------------------------
    
    //Convert Currency from Database to...
    private static String convertCurrencyFromDatabaseToTableview(int iAmount)
    {
        String sAmount;
        String sPence;
        String sPounds;
        String sSign = "";
        
        if(iAmount < 0)
        {
            sSign = "-";
            iAmount *= -1;
        }
        
        if(iAmount == 0)
            return sSign + "0.00";
        
        if(iAmount < 10)
            return sSign + "0.0" + iAmount;
        
        if(iAmount < 100)
            return sSign + "0." + iAmount;
        
        sAmount = String.valueOf(iAmount);
        sPence = sAmount.substring(sAmount.length()-2);
        sPounds = insertCommas(sAmount.substring(0, sAmount.length()-2));
        
        return sSign + sPounds + "." + sPence;
    }
    private static int convertCurrencyFromChartToDatabase(double dAmount)
    {
        return (int)(round(2, dAmount) * 100);
    }
    private static double convertCurrencyFromDatabaseToChart(int iAmount)
    {
        int iPounds;
        int iPence;
        String sResult;
        
        iPence = iAmount % 100;
        iPounds = (iAmount - iPence) / 100;
        
        if(iAmount<0)
            iPence *= -1;
        
        sResult = iPounds + "." + iPence;
        
        return Double.valueOf(sResult);
    }
    
    //Convert Currency...
    private static int convertCurrencyFromStatementToDatabase(String s)
    {
        int iPence = 0;
        int iPower = 0;
        
        for(int i = s.length()-1 ; i>=0 ; i--)
            if( (s.charAt(i) >= 48) && (s.charAt(i) <= 57) )
            {
                iPence += (Integer.valueOf(s.charAt(i))-48) * Math.pow(10, iPower);
                iPower++;
            }
        
        return s.charAt(0) == 45 ? (iPence * -1) : iPence;
    }
    private static int convertCurrencyFromPoundsToDatabase(String s)
    {
        return Integer.valueOf(s) * 100;
    }
    
    //Date ---------------------------------------------------------------------
    
    //Convert Date from Database to...
    private static String convertDateFromDatabaseToTableview(int iDate)
    {
        String sDate = String.valueOf(iDate);
        
        return sDate.substring(6) + " " + findMonthName(sDate.substring(4, 6)) + " " + sDate.substring(2, 4);
    }
    private static String convertDateFromDatabaseToChart(int iDate)
    {
        String sDate = String.valueOf(iDate);
        
        return removeLeadingZeros(sDate.substring(6)) + "/" + removeLeadingZeros(sDate.substring(4, 6)) + "/" + sDate.substring(2, 4);
    }
    private static String convertDateFromDatabaseToStackedBarIntervalWeeks(int iDate)
    {
        String sDate = String.valueOf(iDate);
        LocalDate ld = LocalDate.of(Integer.valueOf(sDate.substring(0, 4)), Integer.valueOf(sDate.substring(4, 6)), Integer.valueOf(sDate.substring(6)));
        
        while(ld.getDayOfWeek().getValue() != 7)
            ld.minusDays(1);
        
        return String.valueOf(ld.getDayOfMonth()) + "/" + ld.getMonth().name().substring(0, 4) + "+"; 
    }
    private static String convertDateFromDatabaseToStackedBarIntervalMonths(int iDate)
    {
        String sDate = String.valueOf(iDate);
        
        return findMonthName(sDate.substring(4, 6)) + " " + sDate.substring(2, 4); 
    }
    private static String convertDateFromDatabaseToStackedBarIntervalYears(int iDate)
    {
        return String.valueOf(iDate).substring(0, 4); 
    }
    private static String convertDateFromDatabaseToDayMonthYear(int iDate, int iPeriod)
    {
        int iDateYear, iDateMonth, iDateDay;
        
        iDateYear = iDate / 10000;
        iDateMonth = iDate % 10000;
        iDateDay = iDateMonth % 100;
        iDateMonth = iDateMonth / 100;
        
        switch(iPeriod)
        {
            case DAY:       return String.valueOf(iDateDay);
            case MONTH:     return String.valueOf(iDateMonth);
            case YEAR:      return String.valueOf(iDateYear);
        }
        
        return "";
    }
    
    //Convert Date to...
    private static int convertDateFromStatementToDatabase(String sDate)
    {
        String[] sFragments = sDate.split(" ");
        
        return Integer.valueOf("20" + sFragments[2] + findMonthNumber(sFragments[1]) + sFragments[0]);
    }
    private static int convertDateFromChartToDatabase(String sDate)
    {
        String[] sFragments = sDate.split("/");
        
        return Integer.valueOf("20" + sFragments[2] + padLeadingZero(sFragments[1]) + padLeadingZero(sFragments[0]));
    }
    private static int convertDateFromLocalDateToDatabase(LocalDate ld)
    {
        return (ld.getYear() * 10000) + (ld.getMonthValue() * 100) + (ld.getDayOfMonth());
    }
    
    //External API -------------------------------------------------------------
    
    //Conversion
    public static String convert(int iType, int iFrom, int iTo, int iValue)
    {
        if(iValue == NOT_DEFINED)
            return "";
        
        if      (iType == CURRENCY  && iFrom == DATABASE    && iTo == TABLEVIEW)            return convertCurrencyFromDatabaseToTableview(iValue);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == TABLEVIEW)            return convertDateFromDatabaseToTableview(iValue);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == CHART)                return convertDateFromDatabaseToChart(iValue);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == INTERVAL_WEEKS)       return convertDateFromDatabaseToStackedBarIntervalWeeks(iValue);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == INTERVAL_MONTHS)      return convertDateFromDatabaseToStackedBarIntervalMonths(iValue);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == INTERVAL_YEARS)       return convertDateFromDatabaseToStackedBarIntervalYears(iValue);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == DAY)                  return convertDateFromDatabaseToDayMonthYear(iValue, DAY);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == MONTH)                return convertDateFromDatabaseToDayMonthYear(iValue, MONTH);
        else if (iType == DATE      && iFrom == DATABASE    && iTo == YEAR)                 return convertDateFromDatabaseToDayMonthYear(iValue, YEAR);
        else                                                                                return "";
    }
    public static String convert(int iType, int iFrom, int iTo, double dValue)
    {
        if(dValue == NOT_DEFINED)
            return "";
        
        if      (iType == CURRENCY  && iFrom == CHART    && iTo == TABLEVIEW)               return convertCurrencyFromDatabaseToTableview(convertCurrencyFromChartToDatabase(dValue));
        else                                                                                return "";
    }
    public static int convert(int iType, int iFrom, int iTo, String sValue)
    {
        if(sValue == null || sValue.equals("") || sValue.equals(DASH))
            return NOT_DEFINED;
        
        if      (iType == CURRENCY      && iFrom == STATEMENT   && iTo == DATABASE)         return convertCurrencyFromStatementToDatabase(sValue);
        else if (iType == CURRENCY      && iFrom == FILE        && iTo == DATABASE)         return Integer.valueOf(sValue);
        else if (iType == CURRENCY      && iFrom == POUNDS      && iTo == DATABASE)         return convertCurrencyFromPoundsToDatabase(sValue);
        else if (iType == DATE          && iFrom == STATEMENT   && iTo == DATABASE)         return convertDateFromStatementToDatabase(sValue);
        else if (iType == DATE          && iFrom == FILE        && iTo == DATABASE)         return Integer.valueOf(sValue);
        else if (iType == DATE          && iFrom == CHART       && iTo == DATABASE)         return convertDateFromChartToDatabase(sValue);
        else                                                                                return NOT_DEFINED;
    }
    public static double convert(int iFrom, int iTo, int iValue)
    {
        if(iValue == NOT_DEFINED)
            return NOT_DEFINED;
        
        if      (iFrom == DATABASE  && iTo == CHART)                                        return convertCurrencyFromDatabaseToChart(iValue);
        else                                                                                return NOT_DEFINED;
    }
    public static int convert(LocalDate ld)
    {
        return convertDateFromLocalDateToDatabase(ld);
    }
    public static String convert(LocalDate ld, int iTo)
    {
        switch(iTo)
        {
            case CHART:
            case INTERVAL_WEEKS:        return ld.getDayOfMonth() + "/" + ld.getMonthValue() + "/" + String.valueOf(ld.getYear()).substring(2);
            case INTERVAL_MONTHS:       return findMonthName(ld.getMonthValue()) + " " + String.valueOf(ld.getYear()).substring(2);
            case INTERVAL_YEARS:        return String.valueOf(ld.getYear());
        }
        
        return "";
    }
    public static LocalDate convert(int iDate)
    {
        if(iDate == NOT_DEFINED)
            return LocalDate.now();
        
        return convert(String.valueOf(iDate));
    }
    public static LocalDate convert(String sDate)
    {
        int iDate;
        
        if(sDate.equals(""))
            return LocalDate.now();
        
        iDate = Integer.valueOf(sDate);

        int iYear = iDate / 10000;
        iDate -= iYear * 10000;
        int iMonth = iDate / 100;
        iDate -= iMonth * 100;
        int iDay = iDate;

        return LocalDate.of(iYear, iMonth, iDay);
    }
    
    //Intervals
    public static String findIntervalLabel(Entry e, int iInterval)
    {
        return findIntervalLabel(e.getDateDay(), e.getDateMonth(), e.getDateYear(), iInterval);
    }
    public static String findIntervalLabel(int iDay, int iMonth, int iYear, int iInterval)
    {
        if(String.valueOf(iYear).length() == 2)
            iYear = 2000 + iYear;
        
        switch(iInterval)
        {
            case INTERVAL_WEEKS:
            {
                LocalDate ld = LocalDate.of(iYear, iMonth, iDay);
        
                while(ld.getDayOfWeek().getValue() != 7)
                    ld = ld.minusDays(1);
                
                return String.valueOf(ld.getDayOfMonth()) + "/" + String.valueOf(ld.getMonthValue()) + "/" + String.valueOf(ld.getYear()).substring(2);
            }
            case INTERVAL_MONTHS:       return findMonthName(iMonth) + " " + String.valueOf(iYear).substring(2);
            case INTERVAL_YEARS:        return String.valueOf(iYear);
        }
        
        return "";
    }
    
    //Formatting
    public static String setMonthAsCamelCase(String sDate)
    {
        if(sDate.isEmpty())
            return "";
        
        String sMonth = sDate.substring(3, 6);
        
        sMonth = sMonth.substring(0, 1).toUpperCase() + sMonth.substring(1, sMonth.length()).toLowerCase();
        
        return sDate.substring(0, 3) + sMonth + sDate.substring(6);
    }
    public static String formatPercentageValue(double d)
    {
        String s = "";
        
        for(int iDP = 2 ; iDP<5 ; iDP++)
        {
            s = String.valueOf(round(iDP, d));

            if(fractionContainNonZero(s))   break;
            else if(iDP == 4)               s = padFraction(String.valueOf(round(2, d)));
        }
        
        return s;
    }
    public static String getMonthName(int i)
    {
        return findMonthName(i);
    }
    
    //Titles
    public static String buildTitleBaseForAccounts(List<Account> lst)
    {
        String s = "";
        
        if(lst.size() == 1)
            return lst.get(0).getName();
        else
            for(int i = 0 ; i<lst.size() ; i++)
                if(i == 0)                  s += lst.get(i).getNameAbbreviated();
                else if(i == lst.size()-1)  s += " and " + lst.get(i).getNameAbbreviated();
                else                        s += ", " + lst.get(i).getNameAbbreviated();
        
        return s;
    }
    public static String buildTitleBaseForCategories(List<Category> lst)
    {
        List<String> lstTitle = new ArrayList<>();

        if(lst.get(0).isAccountProperty())
            return "";
        
        for(Category c : lst)
            if(!lstTitle.contains(c.getAccountNameAbbreviated()))
                lstTitle.add(c.getAccountNameAbbreviated());

        if(lstTitle.size() == 1)    return lst.get(0).getAccountName();
        else                        return sortAndBuildTitle(lstTitle);
    }
    public static String buildTitleBaseForEntries(List<Entry> lst)
    {
        List<String> lstTitle = new ArrayList<>();

        for(Entry e : lst)
            if(!lstTitle.contains(e.getAccountNameAbbreviated()))
                lstTitle.add(e.getAccountNameAbbreviated());

        if(lstTitle.size() == 1)    return lst.get(0).getAccountName();
        else                        return sortAndBuildTitle(lstTitle);
    }

    //Miscellaneous
    public static int getCurrentDate()
    {
        LocalDate ld = LocalDate.now();
        return Integer.valueOf(String.valueOf(ld.getYear()) + findMonthNumber(ld.getMonth().toString()) + String.valueOf(ld.getDayOfMonth()));
    }
    public static int findLastDayOfCalendarMonth(Entry e)
    {
        LocalDate ldEnd = Formatter.convert(e.getDate());
        
        if(ldEnd.getDayOfMonth() == 1)
            ldEnd = ldEnd.plusDays(1);
        
        while(ldEnd.getDayOfMonth() != 1)
            ldEnd = ldEnd.plusDays(1);
        
        return Formatter.convert(ldEnd.minusDays(1));
    }
    public static int findQuarterPeriodStart(Entry e)
    {
        LocalDate ldStart = Formatter.convert(e.getDate());

        if(ldStart.getDayOfMonth() == 1)
            ldStart = ldStart.plusDays(1);
        
        while(ldStart.getDayOfMonth() != 1)
            ldStart = ldStart.minusDays(1);
        
        ldStart = ldStart.minusDays(1);
        
        while(ldStart.getDayOfMonth() != 1)
            ldStart = ldStart.minusDays(1);

        return Formatter.convert(ldStart);
    }
    public static int findQuarterPeriodEnd(Entry e)
    {
        LocalDate ldEnd = Formatter.convert(e.getDate());
        
        if(ldEnd.getDayOfMonth() == 1)
            ldEnd = ldEnd.minusDays(1);
        
        while(ldEnd.getDayOfMonth() != 1)
            ldEnd = ldEnd.plusDays(1);
        
        ldEnd = ldEnd.plusDays(1);
        
        while(ldEnd.getDayOfMonth() != 1)
            ldEnd = ldEnd.plusDays(1);
        
        return Formatter.convert(ldEnd.minusDays(1));
    }
    public static String getMonthYear(LocalDate ld)
    {
        return findMonthName(ld.getMonthValue()) + " " + ld.getYear();
    }
    public static double round(int iNumberOfDecimalPlaces, double d)
    {
        BigDecimal bd = BigDecimal.valueOf(d);
            
        bd = bd.setScale(iNumberOfDecimalPlaces, RoundingMode.HALF_UP);
        
        return bd.doubleValue();
    }
    public static List<String> getWeekDayNames()
    {
        List<String> lst = new ArrayList<>();
        
        lst.add("Sun");
        lst.add("Mon");
        lst.add("Tue");
        lst.add("Wed");
        lst.add("Thu");
        lst.add("Fri");
        lst.add("Sat");
        
        return lst;
    }
    public static List<String> getMonthNames()
    {
        List<String> lst = new ArrayList<>();
        
        for(int i = 1 ; i<=12 ; i++)
            lst.add(findMonthName(i));
        
        return lst;
    }
    public static String getDayOfWeek(String sDay, String sMonth, String sYear)
    {
        List<String> lst = getWeekDayNames();
        int iDayOfWeek;
        
        if(sYear.length() == 2)
            sYear = "20" + sYear;
        
        iDayOfWeek = LocalDate.of(Integer.valueOf(sYear), Integer.valueOf(sMonth), Integer.valueOf(sDay)).getDayOfWeek().getValue();
        
        return iDayOfWeek == 7 ? lst.get(0) : lst.get(iDayOfWeek);
    }
    public static String getMonthName(String sMonth)
    {
        return findMonthName(padLeadingZero(sMonth));
    }
    public static String getYear(String sYear)
    {
        return (sYear.length() == 2) ? "20" + sYear : sYear;
    }
    public static String padLeadingZero(String s)
    {
        if(s.length() == 1)
            return "0" + s;
        return s;
    }
}
