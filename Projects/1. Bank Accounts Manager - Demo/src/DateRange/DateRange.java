package DateRange;

import Accounts.Account;
import Accounts.Accounts;
import Shared.Constants;
import Database.Database;
import Shared.Formatter;
import Shared.Popup;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class DateRange implements Constants{
    
    private static DatePicker   dpDateRangeFrom, dpDateRangeTo;
    private static Label        labDateRange;
    private static Button[]     btDateRangeQuickJumpYears;
    
    private static List<List<String>>   lstIntervals = new ArrayList<>();
    private static DateTimeFormatter    dtfFormat;
    private static int                  iMode, iRecommendedIntervals, iMostRecentPeriod, iPeriod, iPeriodMaximum, iNumberOfDays;
    private static boolean              bInitialised;
    
    public DateRange(DatePicker dpDateRangeFrom, DatePicker dpDateRangeTo, Rectangle recDateRange, Label labDateRange, Button... btDateRangeQuickJumpYears)
    {
        this.dpDateRangeFrom                = dpDateRangeFrom;
        this.dpDateRangeTo                  = dpDateRangeTo;
        this.labDateRange                   = labDateRange;
        this.btDateRangeQuickJumpYears      = btDateRangeQuickJumpYears;
        
        init();
        setMode(DATE_RANGE_MODE_CUSTOM);
        setQuickJumpButtons();
    }
    
    //Internal -----------------------------------------------------------------
    private static void init()
    {
        dpDateRangeFrom.setValue(LocalDate.now());
        dpDateRangeTo.setValue(LocalDate.now());
        
        bInitialised = false;
        
        all();
        
        labDateRange.setText("-");
    }
    
    //Date Range
    private static void findPeriodRange(int iDirection)
    {
        LocalDate ld, ldF, ldT;
        
        switch(iDirection)
        {
            case PERIOD_PREVIOUS:   ld = getFrom().minusDays(1);                    break;
            case PERIOD_NEXT:       ld = getTo().plusDays(1);                       break;
            default:                ld = LocalDate.now();
        }
        
        switch(iPeriod)
        {
            case PERIOD_MONTH:      ldF = ld.withDayOfMonth(1);                     break;
            case PERIOD_YEAR:       ldF = LocalDate.of(ld.getYear(), 1, 1);         break;
            default:                ldF = LocalDate.now();
        }
        
        switch(iPeriod)
        {
            case PERIOD_MONTH:      ldT = ld.withDayOfMonth(ld.lengthOfMonth());    break;
            case PERIOD_YEAR:       ldT = LocalDate.of(ld.getYear(), 12, 31);       break;
            default:                ldT = LocalDate.now();
        }
        
        dpDateRangeFrom.setValue(ldF);
        dpDateRangeTo.setValue(ldT);
    }
    private static void findPeriodAndMode()
    {
        LocalDate ldF = getFrom();
        LocalDate ldT = getTo();
        
        if(ldF.getYear() != ldT.getYear())                                                                                                                                  iPeriod = NOT_DEFINED;
        else if(ldF.getDayOfMonth() == 1 && ldF.getMonthValue() == 1 && ldT.getDayOfMonth() == 31 && ldT.getMonthValue() == 12)                                             iPeriod = PERIOD_YEAR;
        else if(ldF.getYear() == ldT.getYear() && ldF.getMonthValue() == ldT.getMonthValue() && ldF.getDayOfMonth() == 1 && ldT.getDayOfMonth() == ldF.lengthOfMonth())     iPeriod = PERIOD_MONTH;
        else                                                                                                                                                                iPeriod = NOT_DEFINED;

        if(iPeriod == NOT_DEFINED)      iMode = DATE_RANGE_MODE_CUSTOM;
        else                            iMode = DATE_RANGE_MODE_PERIOD;
    }
    private static void setDateLabel()
    {
        LocalDate ldFrom = getFrom();
        LocalDate ldTo = getTo();
        
        switch(iMode)
        {
            case DATE_RANGE_MODE_CUSTOM:
            {
                if(ldFrom.isEqual(ldTo))                dtfFormat = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
                else
                {
                    switch(iPeriod)
                    {
                        case PERIOD_MONTH:              dtfFormat = DateTimeFormatter.ofPattern("MMMM yyyy");               break;
                        case PERIOD_YEAR:               dtfFormat = DateTimeFormatter.ofPattern("yyyy");                    break;
                        default:                        dtfFormat = DateTimeFormatter.ofPattern("EEE dd MMM yyyy");
                    }
                }
                
                break;
            }
            case DATE_RANGE_MODE_PERIOD:
            {
                switch(iPeriod)
                {
                    case PERIOD_MONTH:                  dtfFormat = DateTimeFormatter.ofPattern("MMMM yyyy");               break;
                    case PERIOD_YEAR:                   dtfFormat = DateTimeFormatter.ofPattern("yyyy");                    break;
                    default:                            dtfFormat = DateTimeFormatter.ofPattern("EEE dd MMM yyyy");         break;
                }
                
                break;
            }
        }   
        
        switch(iMode)
        {
            case DATE_RANGE_MODE_CUSTOM:
            {
                if(ldFrom.isEqual(ldTo))                labDateRange.setText(ldFrom.format(dtfFormat));
                else if(iPeriod == NOT_DEFINED)         labDateRange.setText(ldFrom.format(dtfFormat) + "   -   " + ldTo.format(dtfFormat));
                else                                    labDateRange.setText(ldFrom.format(dtfFormat));
                
                break;
            }
            case DATE_RANGE_MODE_PERIOD:                labDateRange.setText(ldFrom.format(dtfFormat));
        }
    }
    private static void setMode(int i)
    {
        iMode = i;
    }
    
    //Intervals
    private static void addInterval(int iInterval, LocalDate ld)
    {
        lstIntervals.get(iInterval).add(Formatter.convert(ld, iInterval));
    }
    private static void checkMonthYearInterval(LocalDate ld)
    {
        List<String> lst;
        
        for(int i = 1 ; i<lstIntervals.size() ; i++)
        {
            lst = lstIntervals.get(i);
            if(lst.isEmpty() || !lst.contains(Formatter.convert(ld, i)))
                addInterval(i, ld);
        }
    }
    private static void addAndCheckAllIntervals(LocalDate ld)
    {
        addInterval(INTERVAL_WEEKS, ld);
        
        checkMonthYearInterval(ld);
    }
    private static void buildIntervals()
    {
        LocalDate ldF = getFrom();
        LocalDate ldT = getTo();
        
        lstIntervals = new ArrayList<>();
        
        for(int i = 0 ; i<NUMBER_OF_INTERVAL_SIZE ; i++)
            lstIntervals.add(new ArrayList<>());
        
        while(ldF.getDayOfWeek().getValue() != 7)
            ldF = ldF.minusDays(1);
        
        addInterval(INTERVAL_WEEKS, ldF);
        
        ldF = getFrom();
        
        checkMonthYearInterval(ldF);
        
        if(!ldF.isEqual(ldT))
        {
            while(ldF.getDayOfWeek().getValue() != 6)
            {
                ldF = ldF.plusDays(1);
                checkMonthYearInterval(ldF);
                
                if(ldF.isEqual(ldT))
                    return;
            }

            ldF = ldF.plusDays(1);
            
            do
            {
                addAndCheckAllIntervals(ldF);
                
                if(ldF.isEqual(ldT))    return;
                else                    ldF = ldF.plusWeeks(1);
            }
            while(ldF.isBefore(ldT) || ldF.isEqual(ldT));
            
            ldF = ldF.minusWeeks(1);
            
            while(!ldF.isEqual(ldT))
            {
                ldF = ldF.plusDays(1);
                checkMonthYearInterval(ldF);
            }
        }
    }
    private static void findRecommendedIntervals()
    {
        LocalDate ldF = getFrom();
        LocalDate ldT = getTo();
        
        ldF = ldF.plusMonths(3);
        
        if(ldF.equals(ldT) || ldF.isAfter(ldT))         iRecommendedIntervals = INTERVAL_WEEKS;
        else
        {
            ldF = ldF.plusMonths(33);
            
            if(ldF.equals(ldT) || ldF.isAfter(ldT))     iRecommendedIntervals = INTERVAL_MONTHS;
            else                                        iRecommendedIntervals = INTERVAL_YEARS;
        }
    }
    
    //Number of Days
    private static void findNumberOfDays()
    {
        LocalDate ldF = getFrom();
        LocalDate ldT = getTo();
        
        iNumberOfDays = 0;
        
        for( ; !ldF.isEqual(ldT) ; ldF = ldF.plusDays(1))
            iNumberOfDays++;
    }
    private static void findPeriodMaximum()
    {
        LocalDate ldF = getFrom();
        LocalDate ldT = getTo();
        
        if(ldF.equals(ldT))                                                         iPeriodMaximum = NOT_DEFINED;
        else if(ldF.getDayOfWeek().getValue() >= ldT.getDayOfWeek().getValue())     iPeriodMaximum = INTERVAL_WEEKS;
        
        if(ldF.getYear() == ldT.getYear())
        {
            if(ldF.getMonthValue() == ldT.getMonthValue())                                                                              iPeriodMaximum = INTERVAL_WEEKS;
            else if(ldF.getDayOfMonth() == 1 && ldF.getMonthValue() == 1 && ldT.getDayOfMonth() == 31 && ldT.getMonthValue() == 12)     iPeriodMaximum = INTERVAL_YEARS;
            else                                                                                                                        iPeriodMaximum = INTERVAL_MONTHS;
        }
        else
            iPeriodMaximum = INTERVAL_YEARS;
    }
    
    //External API -------------------------------------------------------------
    
    //State
    public static boolean set()
    {
        if(bInitialised && !isRangeValid())
            return false;
        
        findPeriodAndMode();
        setDateLabel();
        buildIntervals();
        findRecommendedIntervals();
        findNumberOfDays();
        findPeriodMaximum();
        
        bInitialised = true;
        
        return true;
    }
    public static void all()
    {
        List<Account> lst = Accounts.getAccountsViewed();
        
        if(lst.isEmpty())
        {    
            dpDateRangeFrom.setValue(Formatter.convert(Database.getEntryDateFromAccounts(Accounts.getAccounts(), EARLIEST)));
            dpDateRangeTo.setValue(Formatter.convert(Database.getEntryDateFromAccounts(Accounts.getAccounts(), LATEST)));
        }
        else
        {
            dpDateRangeFrom.setValue(Formatter.convert(Database.getEntryDateFromAccounts(lst, EARLIEST)));
            dpDateRangeTo.setValue(Formatter.convert(Database.getEntryDateFromAccounts(lst, LATEST)));
        }
            
        setMode(DATE_RANGE_MODE_CUSTOM);
    }
    public static int getMode()
    {
        return iMode;
    }
    public static boolean isPeriod()
    {
        return iMode == DATE_RANGE_MODE_PERIOD;
    }
    public static String getLabelText()
    {
        return labDateRange.getText();
    }
    
    //Date Range
    public static void setYear(int iYear)
    {
        dpDateRangeFrom.setValue(LocalDate.of(iYear, 1, 1));
        dpDateRangeTo.setValue(LocalDate.of(iYear, 12, 31));
    }
    public static void setMonth(int iMonth)
    {
        LocalDate ldFrom = getFrom();
        LocalDate ldTo;
        
        ldFrom = LocalDate.of(ldFrom.getYear(), iMonth, 1);
        ldTo = LocalDate.of(ldFrom.getYear(), iMonth, ldFrom.getMonth().length(ldFrom.isLeapYear()));
        
        dpDateRangeFrom.setValue(ldFrom);
        dpDateRangeTo.setValue(ldTo);
    }
    public static void setMostRecent(int iPeriod)
    {
        List<Account> lst = Accounts.getAccountsViewed();
        LocalDate ldF, ldT;
        
        if(lst.isEmpty())   ldF = Formatter.convert(Database.getEntryDateFromAccounts(Accounts.getAccounts(), LATEST));
        else                ldF = Formatter.convert(Database.getEntryDateFromAccounts(lst, LATEST));
        
        ldT = LocalDate.of(ldF.getYear(), ldF.getMonth(), ldF.getDayOfMonth());
        
        iMostRecentPeriod = iPeriod;
        
        switch(iMostRecentPeriod)
        {
            case DATE_RANGE_MOST_RECENT_MONTH_3:        ldF = ldF.minusMonths(2);      break;
            case DATE_RANGE_MOST_RECENT_MONTH_6:        ldF = ldF.minusMonths(5);      break;
            case DATE_RANGE_MOST_RECENT_MONTH_12:       ldF = ldF.minusMonths(11);     break;
            case DATE_RANGE_MOST_RECENT_MONTH_18:       ldF = ldF.minusMonths(17);     break;
        }
        
        ldF = LocalDate.of(ldF.getYear(), ldF.getMonth(), 1);
        ldT = LocalDate.of(ldT.getYear(), ldT.getMonth(), 1);
        
        dpDateRangeFrom.setValue(ldF);
        dpDateRangeTo.setValue(ldT.plusMonths(1).minusDays(1));
        
        Popup.hide(POPUP_DATE_RANGE_MOST_RECENT);
    }
    public static LocalDate getFrom()
    {
        return dpDateRangeFrom.getValue();
    }
    public static LocalDate getTo()
    {
        return dpDateRangeTo.getValue();
    }
    public static boolean isRangeValid()
    {
        LocalDate ldFrom = getFrom();
        LocalDate ldTo = getTo();
        
        return bInitialised && (ldFrom.isBefore(ldTo) || ldFrom.isEqual(ldTo));
    }
    public static boolean areIntervalsBuilt()
    {
        return !lstIntervals.isEmpty();
    }
    public static int getRecommendedIntervals()
    {
        return iRecommendedIntervals;
    }
    public static void previousFixedPeriod()
    {
        if(iMode == DATE_RANGE_MODE_PERIOD)
        {
            findPeriodRange(PERIOD_PREVIOUS);
            set();
        }
    }
    public static void nextFixedPeriod()
    {
        if(iMode == DATE_RANGE_MODE_PERIOD)
        {
            findPeriodRange(PERIOD_NEXT);
            set();
        }
    }
    
    //Intervals
    public static List<String> getIntervals(int iInterval)
    {
        return isRangeValid() ? lstIntervals.get(iInterval) : new ArrayList<>();
    }
    
    //Span
    public static int getNumberOfDays()
    {
        return iNumberOfDays;
    }
    public static int getPeriodMaximum()
    {
        return iPeriodMaximum;
    }
    
    //QuickJump Buttons
    public static void setQuickJumpButtons()
    {
        List<Account> lst = Accounts.getAccounts();
        boolean bHasEntries = false;
        
        for(Account a : lst)
            if(Database.hasEntries(a))
                bHasEntries = true;
        
        if(bHasEntries)
        {
            int iYearEarliest = Formatter.convert(Database.getEntryDateFromAccounts(lst, EARLIEST)).getYear() - 2010;
            int iYearLatest = Formatter.convert(Database.getEntryDateFromAccounts(lst, LATEST)).getYear() - 2010;
            
            for(int i = 0 ; i<btDateRangeQuickJumpYears.length ; i++)
                btDateRangeQuickJumpYears[i].setDisable(i < iYearEarliest || i > iYearLatest);
        }
        else
            for(int i = 0 ; i<btDateRangeQuickJumpYears.length ; i++)
                btDateRangeQuickJumpYears[i].setDisable(true);
    }
}
