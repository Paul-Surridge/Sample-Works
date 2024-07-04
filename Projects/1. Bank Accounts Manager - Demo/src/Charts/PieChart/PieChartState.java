package Charts.PieChart;

import Accounts.Account;
import Categories.Category;
import Charts.Charts;
import DateRange.DateRange;
import History.Entry;
import Shared.Constants;
import Shared.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartState implements Constants{

    private List<Account> lstAccounts;
    private List<Category> lstCategories;
    private List<Entry> lstEntries;
    private Map<Integer, Integer> mpPieID = new HashMap<>();
    private int iSource, iDirection;
    private boolean bShowLegend;
    
    public PieChartState()
    {
        iSource = NOT_DEFINED;
        iDirection = NOT_DEFINED;
        bShowLegend = false;
        
        initPieIDMap();
    }
    
    //Internal -----------------------------------------------------------------
    private void initPieIDMap()
    {
        mpPieID.put(ACCOUNT, CHART_PIE_ID_INCOMING_V_OUTGOING);
        mpPieID.put(ALL, CHART_PIE_ID_INCOMING_V_OUTGOING);
        mpPieID.put(CATEGORY, CHART_PIE_CATEGORIES_V_LOCAL);
        mpPieID.put(ENTRY, CHART_PIE_ENTRIES_V_LOCAL);
    }
    private void init(int iSource)
    {
        this.iDirection = findDirection(iSource);
        this.iSource = iSource;
    }
    private int findDirection(int iSource)
    {
        switch(iSource)
        {
            case CATEGORY:      return lstCategories.get(0).getDirection();
            case ENTRY:         return lstEntries.get(0).getDirection();
        }
        
        return NOT_DEFINED;
    }
    private String buildTitle()
    {
        String sTitleBuild = Charts.getTitleBase(iSource) + " - ";
        
        switch(iSource)
        {
            case ACCOUNT:
            case ALL:
            {
                if(iSource == ACCOUNT)
                    sTitleBuild = Charts.refreshAccountTitleBase(lstAccounts) + " - ";
                
                switch(getPieID(iSource))
                {
                    case CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_START:        sTitleBuild = "All Accounts - Balance Distribution on " + Formatter.convert(DateRange.getFrom(), CHART);    break;
                    case CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_END:          sTitleBuild = "All Accounts - Balance Distribution on " + Formatter.convert(DateRange.getTo(), CHART);      break;
                    case CHART_PIE_ID_INCOMING_CATEGORIES:                  sTitleBuild += "Incoming Categories";                                                                       break;
                    case CHART_PIE_ID_OUTGOING_CATEGORIES:                  sTitleBuild += "Outgoing Categories";                                                                       break;
                    case CHART_PIE_ID_INCOME_V_SPEND:                       sTitleBuild += "Income / Spend";                                                                            break;
                    case CHART_PIE_ID_INCOMING_V_OUTGOING:                  sTitleBuild += "Incoming / Outgoing";                                                                       break;
                }
                
                break;
            }
            case CATEGORY:
            {
                String s = lstCategories.size() == 1 ? lstCategories.get(0).getNameInTable() : "Categories";
                
                switch(getPieID(iSource))
                {
                    case CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOME:         sTitleBuild += s + " / Income";                 break;
                    case CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOMING:       sTitleBuild += s + " / Total Incoming";         break;
                    case CHART_PIE_ID_OUTGOING_CATEGORIES_V_SPEND:          sTitleBuild += s + " / Spend";                  break;
                    case CHART_PIE_ID_OUTGOING_CATEGORIES_V_OUTGOING:       sTitleBuild += s + " / Total Outgoing";         break;
                }
        
                break;
            }
            case ENTRY:
            {
                String s = lstEntries.size() == 1 ? lstEntries.get(0).getName() : "Entries";
                
                switch(getPieID(iSource))
                {
                    case CHART_PIE_ID_INCOMING_ENTRIES_V_INCOME:            sTitleBuild += s + " / Income";                 break;
                    case CHART_PIE_ID_INCOMING_ENTRIES_V_INCOMING:          sTitleBuild += s + " / Total Incoming";         break;
                    case CHART_PIE_ID_OUTGOING_ENTRIES_V_SPEND:             sTitleBuild += s + " / Spend";                  break;
                    case CHART_PIE_ID_OUTGOING_ENTRIES_V_OUTGOING:          sTitleBuild += s + " / Total Outgoing";         break;
                }
                
                break;
            }
        }
        
        return sTitleBuild;
    }
        
    //External API -------------------------------------------------------------
    
    //State
    public String getTitle()
    {
        return buildTitle();
    }
    public int getSource()
    {
        return iSource;
    }
    public int getDirection()
    {
        return iDirection;
    }
    public int getPieID(int iSource)
    {
        switch(iSource)
        {
            case ACCOUNT:
            case ALL:           return mpPieID.get(iSource);
            case CATEGORY:
            {
                switch(mpPieID.get(iSource))
                {
                    case CHART_PIE_CATEGORIES_V_LOCAL:      return iDirection == IN ? CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOME     : CHART_PIE_ID_OUTGOING_CATEGORIES_V_SPEND;
                    case CHART_PIE_CATEGORIES_V_ALL:        return iDirection == IN ? CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOMING   : CHART_PIE_ID_OUTGOING_CATEGORIES_V_OUTGOING;
                }
            }
            case ENTRY:
            {
                switch(mpPieID.get(iSource))
                {
                    case CHART_PIE_ENTRIES_V_LOCAL:         return iDirection == IN ? CHART_PIE_ID_INCOMING_ENTRIES_V_INCOME     : CHART_PIE_ID_OUTGOING_ENTRIES_V_SPEND;
                    case CHART_PIE_ENTRIES_V_ALL:           return iDirection == IN ? CHART_PIE_ID_INCOMING_ENTRIES_V_INCOMING   : CHART_PIE_ID_OUTGOING_ENTRIES_V_OUTGOING;
                }
            }
        }
        
        return NOT_DEFINED;
    }
    public void setPieID(int iSource, int iPieID)
    {
        mpPieID.replace(iSource, iPieID);
        
        switch(iSource)
        {
            case ACCOUNT:   mpPieID.replace(ALL, iPieID);           break;
            case ALL:       mpPieID.replace(ACCOUNT, iPieID);       break;
            case CATEGORY:
            {
                if(iPieID == CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOME || iPieID == CHART_PIE_ID_OUTGOING_CATEGORIES_V_SPEND)       mpPieID.replace(CATEGORY, CHART_PIE_CATEGORIES_V_LOCAL);
                else                                                                                                                mpPieID.replace(CATEGORY, CHART_PIE_CATEGORIES_V_ALL);
                
                break;
            }
            case ENTRY:
            {
                if(iPieID == CHART_PIE_ID_INCOMING_ENTRIES_V_INCOME || iPieID == CHART_PIE_ID_OUTGOING_ENTRIES_V_SPEND)             mpPieID.replace(ENTRY, CHART_PIE_ENTRIES_V_LOCAL);
                else                                                                                                                mpPieID.replace(ENTRY, CHART_PIE_ENTRIES_V_ALL);
                
                break;
            }
        }
    }
    public boolean getLegend()
    {
        return bShowLegend;
    }
    public void setLegend(boolean bState)
    {
        bShowLegend = bState;
    }
    
    //Source Components
    public void setAccounts(List<Account> lst)
    {
        lstAccounts = lst;
        init(ACCOUNT);
    }
    public List<Account> getAccounts()
    {
        return lstAccounts;
    }
    public void setAll()
    {
        init(ALL);
    }
    public void setCategories(List<Category> lst)
    {
        lstCategories = lst;
        init(CATEGORY);
    }
    public List<Category> getCategories()
    {
        return lstCategories;
    }
    public void setEntries(List<Entry> lst)
    {
        lstEntries = lst;
        init(ENTRY);
    }
    public List<Entry> getEntries()
    {
        return lstEntries;
    }
    
    //Properties
    public boolean areMultipleAccountsViewed()
    {
        switch(iSource)
        {
            case ACCOUNT:       return !lstAccounts.isEmpty();
            case CATEGORY:
            {
                Map<Integer, Integer> mp = new HashMap<>();
        
                for(Category c : lstCategories)
                    if(!mp.containsKey(c.getAccountID()))
                        mp.put(c.getAccountID(), 0);

                return mp.size() > 1;
            }
            case ALL:           return true;
        }
        
        return false;
    }
}
