package Accounts;

import AutoCategories.AutoCategory;
import Categories.Categories;
import Categories.Category;
import History.Entry;
import Shared.Constants;
import Database.Database;
import History.EntryInternalTransfer;
import Refunds.Refund;
import Shared.Formatter;
import Watches.Watch;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Account implements AccountsTableInsertable, Constants{
    
    private Deque<Entry> dqEntries = new ArrayDeque<>();
    private Deque<Entry> dqEntriesIn = new ArrayDeque<>();
    private Deque<Entry> dqEntriesOut = new ArrayDeque<>();
    
    private Map<String, Category> mpCategoriesIn = new TreeMap<>();
    private Map<String, Category> mpCategoriesOut = new TreeMap<>();
    private Map<String, AutoCategory> mpAutoCategoriesIn = new TreeMap<>();
    private Map<String, AutoCategory> mpAutoCategoriesOut = new TreeMap<>();
    private Map<String, Watch> mpWatchesIn = new TreeMap<>();
    private Map<String, Watch> mpWatchesOut = new TreeMap<>();
    private List<Refund> lstRefunds = new ArrayList<>();
    private List<EntryInternalTransfer> lstInternalTransferEntries = new ArrayList<>();
    
    private List<BalanceProperty> lstBalanceProperties = new ArrayList<>();
    private List<AccountProperty> lstAccountProperties = new ArrayList<>();
    
    //Balance
    private BalanceProperty bpBalanceAtStart                = new BalanceProperty("Balance Start");
    private BalanceProperty bpIncoming                      = new BalanceProperty("Incoming");
    private BalanceProperty bpOutgoing                      = new BalanceProperty("Outgoing");
    private BalanceProperty bpBalanceChange                 = new BalanceProperty("Balance Change");
    private BalanceProperty bpBalanceAtEnd                  = new BalanceProperty("Balance End");
    
    //Incoming
    private AccountProperty apIncomingIncome                = new AccountProperty(this, "Income", IN,                               AP_INCOMING_INCOME);
    private AccountProperty apIncomingInternalTransfers     = new AccountProperty(this, "Internal Transfers", IN,                   AP_INCOMING_INTERNAL_TRANSFER);
    private AccountProperty apIncomingTotal                 = new AccountProperty(this, "Total Incoming", IN,                       AP_INCOMING_TOTAL);
    
    //Outgoing
    private AccountProperty apOutgoingPurchases             = new AccountProperty(this, "Purchases", OUT,                           AP_OUTGOING_PURCHASE);
    private AccountProperty apOutgoingDDSO                  = new AccountProperty(this, "Direct Debit and Standing Orders", OUT,    AP_OUTGOING_DDSO);
    private AccountProperty apOutgoingOther                 = new AccountProperty(this, "Other", OUT,                               AP_OUTGOING_OTHER);
    private AccountProperty apOutgoingSpend                 = new AccountProperty(this, "Total Spend", OUT,                         AP_OUTGOING_SPEND);
    private AccountProperty apOutgoingInternalTransfers     = new AccountProperty(this, "Internal Transfers", OUT,                  AP_OUTGOING_INTERNAL_TRANSFER);
    private AccountProperty apOutgoingTotal                 = new AccountProperty(this, "Total Outgoing", OUT,                      AP_OUTGOING_TOTAL);

    private AccountChartBars acbChartBars;
    private AccountChartLines aclChartLines;
    
    private CheckBox cbView = new CheckBox();
    private int iID;
    private String sName, sNameAbbreviated, sNameDatabaseTablePrefix;
    private boolean bView;
    
    public Account(int iID, String sName, String sNameAbbreviated, String sNameDatabaseTablePrefix)
    {
        this.iID = iID;
        this.sName = sName;
        this.sNameAbbreviated = sNameAbbreviated;
        this.sNameDatabaseTablePrefix = sNameDatabaseTablePrefix;
        this.bView = true;
        
        lstBalanceProperties.add(bpBalanceAtStart);
        lstBalanceProperties.add(bpIncoming);
        lstBalanceProperties.add(bpOutgoing);
        lstBalanceProperties.add(bpBalanceChange);
        lstBalanceProperties.add(bpBalanceAtEnd);
        
        lstAccountProperties.add(apIncomingIncome);
        lstAccountProperties.add(apIncomingInternalTransfers);
        lstAccountProperties.add(apIncomingTotal);
        
        lstAccountProperties.add(apOutgoingPurchases);
        lstAccountProperties.add(apOutgoingDDSO);
        lstAccountProperties.add(apOutgoingOther);
        lstAccountProperties.add(apOutgoingSpend);
        lstAccountProperties.add(apOutgoingInternalTransfers);
        lstAccountProperties.add(apOutgoingTotal);
        
        loadFromDatabase();
    }

    //Internal -----------------------------------------------------------------
    
    //Database
    private void loadFromDatabase()
    {
        //Categories
        for(Category c : Database.getCategories(this))
            addCategoryToMap(c);
        
        addCategoryToMap(Categories.getUndefined(this, IN));
        addCategoryToMap(Categories.getUndefined(this, OUT));
        
        //Auto Categories
        for(AutoCategory ac : Database.getAutoCategories(this))
            addAutoCategoryToMap(ac);
        
        refreshAutoCategoriesFirstLastFrequency();
        
        //Watches
        for(Watch w : Database.getWatches(this))
            addWatchToMap(w);
        
        //Refunds
        for(Refund r : Database.getRefunds(this))
            addRefundToList(r);
    }
    private void setAutoCategoryFirstLastFrequency(AutoCategory ac)
    {
        ac.setFirst(Database.getFirstOccurence(this, ac));
        ac.setLast(Database.getLastOccurence(this, ac));
        ac.setFrequency(Database.getFrequency(this, ac));
    }
    
    //Categories
    private void addCategoryToMap(Category c)
    {
        switch(c.getDirection())
        {
            case IN:        mpCategoriesIn.put(c.getName().toLowerCase(), c);     break;
            case OUT:       mpCategoriesOut.put(c.getName().toLowerCase(), c);    break;
        }
    }
    private Category getCategoryFromMap(Entry e)
    {
        switch(e.getDirection())
        {
            case IN:    return mpCategoriesIn.get(e.getCategoryName().toLowerCase());
            case OUT:   return mpCategoriesOut.get(e.getCategoryName().toLowerCase());
        }
        
        return null;
    }
    private Category getCategoryFromMap(String sName, int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return mpCategoriesIn.get(sName.toLowerCase());
            case OUT:   return mpCategoriesOut.get(sName.toLowerCase());
        }
        
        return null;
    }
    private void clearCategoryTotals()
    {
        for(Category c : mpCategoriesIn.values())
            c.clearTotal();

        for(Category c : mpCategoriesOut.values())
            c.clearTotal();
    }
    private void addCategoryToMapAndDatabase(Category c)
    {
        addCategoryToMap(c);
        Database.addCategory(c);
    }
    private void deleteCategoryFromMapAndDatabase(Category c)
    {
        switch(c.getDirection())
        {
            case IN:        mpCategoriesIn.remove(c.getName().toLowerCase());      break;
            case OUT:       mpCategoriesOut.remove(c.getName().toLowerCase());     break;
        }
        
        Database.deleteCategory(c);
    }
    private void deleteAllCategoriesFromMapAndDatabase(int iDirection)
    {
        switch(iDirection)
        {
            case IN:        mpCategoriesIn.clear();     break;
            case OUT:       mpCategoriesOut.clear();    break;
        }
        
        Database.deleteAllCategories(this, iDirection);
    }
    private void renameCategoryInMapAndDatabase(Category c, String sNewName)
    {
        Map<String, Category> mp;
        
        String sOldName = c.getName();
        
        if(c.getDirection() == IN)  mp = mpCategoriesIn;
        else                        mp = mpCategoriesOut;
        
        mp.get(c.getName().toLowerCase()).setName(sNewName);
        mp.put(sNewName.toLowerCase(), c);
        mp.remove(sOldName.toLowerCase(), c);
        
        Database.renameCategory(c, sOldName);
    }
    
    //AutoCategories
    private void addAutoCategoryToMap(AutoCategory ac)
    {
        switch(ac.getDirection())
        {
            case IN:        mpAutoCategoriesIn.put(ac.getEntryName(), ac);      break;
            case OUT:       mpAutoCategoriesOut.put(ac.getEntryName(), ac);     break;
        }
    }
    private AutoCategory getAutoCategoryFromMap(String sEntryName, int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return mpAutoCategoriesIn.get(sEntryName);
            case OUT:   return mpAutoCategoriesOut.get(sEntryName);
        }
        
        return null;
    }
    private boolean isAutoCategoryInMap(AutoCategory ac)
    {
        switch(ac.getDirection())
        {
            case IN:    return mpAutoCategoriesIn.containsKey(ac.getEntryName());
            case OUT:   return mpAutoCategoriesOut.containsKey(ac.getEntryName());
        }
        
        return false;
    }
    private void addAutoCategoryToMapAndDatabase(AutoCategory ac)
    {
        addAutoCategoryToMap(ac);
        setAutoCategoryFirstLastFrequency(ac);
        
        Database.addAutoCategory(ac);
    }
    private void deleteAutoCategoryFromMapAndDatabase(AutoCategory ac)
    {
        switch(ac.getDirection())
        {
            case IN:        mpAutoCategoriesIn.remove(ac.getEntryName());       break;
            case OUT:       mpAutoCategoriesOut.remove(ac.getEntryName());      break;
        }
        
        Database.deleteAutoCategory(ac);
    }
    private void deleteAllAutoCategoriesFromMapAndDatabase(int iDirection)
    {
        switch(iDirection)
        {
            case IN:        mpAutoCategoriesIn.clear();     break;
            case OUT:       mpAutoCategoriesOut.clear();    break;
        }
        
        Database.deleteAllAutoCategories(this, iDirection);
    }
    private void setAutoCategoryInMapAndDatabase(AutoCategory ac)
    {
        switch(ac.getDirection())
        {
            case IN:    mpAutoCategoriesIn.get(ac.getEntryName()).setCategoryName(ac.getCategoryName());    break;
            case OUT:   mpAutoCategoriesOut.get(ac.getEntryName()).setCategoryName(ac.getCategoryName());   break;
        }
        
        setAutoCategoryFirstLastFrequency(ac);
        
        Database.setAutoCategory(ac);
    }
    
    //Internal Transfers
    private String findInternalTransferName(Account aDestination, int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return sNameAbbreviated + " Receive From " + aDestination.getNameAbbreviated();
            case OUT:   return sNameAbbreviated + " Send To " + aDestination.getNameAbbreviated();
        }
        
        return "";
    }
    private Map<String, Integer> buildInternalTransferNamesMap()
    {
        Map<String, Integer> mp = new HashMap<>();
        
        for(Account a : Accounts.getAccounts())
            if(a != this)
            {
                mp.put(findInternalTransferName(a, IN), a.getAccountID());
                mp.put(findInternalTransferName(a, OUT), a.getAccountID());
            }
        
        return mp;
    }
    
    //Watches
    private void addWatchToMap(Watch w)
    {
        switch(w.getDirection())
        {
            case IN:        mpWatchesIn.put(w.getCategoryName(), w);        break;
            case OUT:       mpWatchesOut.put(w.getCategoryName(), w);       break;
        }
    }
    private void addWatchToMapAndDatabase(Watch w)
    {
        addWatchToMap(w);
        Database.addWatch(w);
    }
    private boolean isWatchInMap(Category c)
    {
        switch(c.getDirection())
        {
            case IN:    return mpWatchesIn.containsKey(c.getName());
            case OUT:   return mpWatchesOut.containsKey(c.getName());
        }
        
        return false;
    }
    private boolean isWatchInMap(String sCategoryName, int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return mpWatchesIn.containsKey(sCategoryName);
            case OUT:   return mpWatchesOut.containsKey(sCategoryName);
        }
        
        return false;
    }
    private void deleteWatchFromMapAndDatabase(Watch w)
    {
        switch(w.getDirection())
        {
            case IN:        mpWatchesIn.remove(w.getCategoryName());        break;
            case OUT:       mpWatchesOut.remove(w.getCategoryName());       break;
        }
        
        Database.deleteWatch(w);
    }
    private void deleteAllWatchesFromMapAndDatabase()
    {
        mpWatchesIn.clear();
        mpWatchesOut.clear();
        
        Database.deleteAllWatches(this);
    }
    
    //Refunds
    private void addRefundToList(Refund r)
    {
        lstRefunds.add(r);
    }
    private void addRefundToListAndDatabase(Refund r)
    {
        lstRefunds.add(r);
        Database.addRefund(r);
    }
    private void deleteRefundFromListAndDatabase(Refund r)
    {
        lstRefunds.remove(r);
        Database.deleteRefund(r);
    }
    private void deleteAllRefundsFromListAndDatabase()
    {
        lstRefunds.clear();
        Database.deleteAllRefunds(this);
    }
    private void updateRefundInDatabase(Refund r)
    {
        Database.updateRefund(this, r);
    }
    private void updateRefundReceivedInDatabase(Refund r)
    {
        Database.updateRefundReceived(this, r);
    }
    
    //Chart Bars and Lines
    private void clear(boolean bClearEntries)
    {
        if(bClearEntries)
        {
            dqEntries.clear();
            dqEntriesIn.clear();
            dqEntriesOut.clear();
        }
        
        clearProperties();
        clearChartData();
        
        clearCategoryTotals();
        clearCategoryChartData();
    }
    private void clearProperties()
    {
        for(AccountProperty ap : lstAccountProperties)
            ap.clear();
        
        for(BalanceProperty bp : lstBalanceProperties)
            bp.clear();
    }
    private void clearChartData()
    {
        acbChartBars = new AccountChartBars(this);
        aclChartLines = new AccountChartLines(this);
    }
    private void clearCategoryChartData()
    {
        for(Category c : getCategories(IN))
            c.clearChartData();
        
        for(Category c : getCategories(OUT))
            c.clearChartData();
    }
    private void recalculatePropertiesAndCategoryTotals()
    {
        clear(false);
        
        Accounts.getAll().clearChartData();
        
        if(dqEntries.size()>0)
        {
            Category c;
            Entry eFirst = dqEntries.getFirst();
            int iEntryDirection;
            
            clearCategoryTotals();

            switch(eFirst.getDirection())
            {
                case IN:    bpBalanceAtStart.add(eFirst.getBalance() - eFirst.getAmount());     break;
                case OUT:   bpBalanceAtStart.add(eFirst.getBalance() + eFirst.getAmount());     break;
            }
            
            bpBalanceAtEnd.add(dqEntries.getLast().getBalance());
            bpBalanceChange.add(bpBalanceAtEnd.getValue()- bpBalanceAtStart.getValue());
            
            for(Entry e : dqEntries)
            {
                c = getCategoryFromMap(e);

                if(c == null)
                {
                    c = getCategoryFromMap(UNDEFINED_TEXT, e.getDirection());
                    e.setCategoryName(UNDEFINED_TEXT, true);
                }
                
                iEntryDirection = e.getDirection();
                
                switch(iEntryDirection)
                {
                    case IN:    bpIncoming.add(e.getIn());      break;
                    case OUT:   bpOutgoing.add(e.getOut());     break;
                }
                
                switch(iEntryDirection)
                {
                    case IN:    c.addTotal(e.getIn());          break;
                    case OUT:   c.addTotal(e.getOut());         break;
                }
                
                switch(iEntryDirection)
                {
                    case IN:    apIncomingTotal.add(e);           break;
                    case OUT:   apOutgoingTotal.add(e);           break;
                }
                
                if(iEntryDirection == OUT && !e.isInternalTransfer())
                    apOutgoingSpend.add(e);
                
                lstAccountProperties.get(e.getAccountProperty()).add(e);
                
                c.incFrequency();
            }

            for(AccountProperty ap : lstAccountProperties)
                ap.set();
            
            for(BalanceProperty bp : lstBalanceProperties)
                bp.set();
        }
    }
    
    //External API -------------------------------------------------------------
    
    //Accounts Tableview
    @Override
    public SimpleStringProperty balanceAtStartProperty()
    {
        return bpBalanceAtStart.getProperty();
    }
    @Override
    public SimpleStringProperty incomingTotalProperty()
    {
        return bpIncoming.getProperty();
    }
    @Override
    public SimpleStringProperty outgoingTotalProperty()
    {
        return bpOutgoing.getProperty();
    }
    @Override
    public SimpleStringProperty balanceChangeProperty()
    {
        return bpBalanceChange.getProperty();
    }
    @Override
    public SimpleStringProperty balanceAtEndProperty()
    {
        return bpBalanceAtEnd.getProperty();
    }
    
    //ID
    public int getAccountID()
    {
        return iID;
    }
    @Override
    public String getName()
    {
        return sName;
    }
    public String getNameAbbreviated()
    {
        return sNameAbbreviated;
    }
    public String getNameDatabaseTablePrefix()
    {
        return sNameDatabaseTablePrefix;
    }
    
    //Balance Properties
    public List<BalanceProperty> getBalanceProperties()
    {
        return lstBalanceProperties;
    }
    public BalanceProperty getBalanceProperty(int iID)
    {
        return iID < lstBalanceProperties.size() ? lstBalanceProperties.get(iID) : null;
    }
    public void hideBalanceProperties()
    {
        for(BalanceProperty bp : lstBalanceProperties)
            bp.hide();
    }
    public void showBalanceProperties()
    {
        for(BalanceProperty bp : lstBalanceProperties)
            bp.show();
    }
    
    //Account Properties
    public List<AccountProperty> getAccountProperties()
    {
        return lstAccountProperties;
    }
    public AccountProperty getAccountProperty(int iID)
    {
        return iID < lstAccountProperties.size() ? lstAccountProperties.get(iID) : null;
    }
    
    //Viewability
    @Override
    public boolean getView()
    {
        return bView;
    }
    @Override
    public void initViewCheckbox(CheckBox cb)
    {
        cbView = cb;
        cbView.setSelected(bView);
        cbView.selectedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                bView = (boolean)newVal;
                
                Accounts.refresh(Accounts.get(iID));
            }
        });
    }
    
    //Entries
    public Deque<Entry> getEntries()
    {
        return new ArrayDeque<>(dqEntries);
    }
    public Deque<Entry> getEntriesByDirection(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return new ArrayDeque<>(dqEntriesIn);
            case OUT:   return new ArrayDeque<>(dqEntriesOut);
        }
        
        return new ArrayDeque<>();
    }
    public Deque<Entry> getEntriesByAccountProperty(int iAccountProperty)
    {
        return new ArrayDeque<>(lstAccountProperties.get(iAccountProperty).getEntries());
    }
    public Deque<Entry> getEntriesViewed(int iDirection)
    {
        Deque<Entry> dq = new ArrayDeque<>();
        
        switch(iDirection)
        {
            case IN:    if(bpIncoming.getValue()== 0)                                       return dq;
            case OUT:   if(bpOutgoing.getValue()== 0)                                       return dq;
            case BOTH:  if((bpIncoming.getValue()== 0) && (bpOutgoing.getValue()== 0))      return dq;
        }
        
        for(Entry e : dqEntries)
            if(iDirection == BOTH || e.getDirection() == iDirection)
                if(getCategoryFromMap(e).getView())
                    dq.add(e);
        
        return dq;
    }
    public boolean isEmpty()
    {
        return dqEntries.isEmpty();
    }
    public int getNumberOfEntries(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return dqEntriesIn.size();
            case OUT:   return dqEntriesOut.size();
        }
        
        return dqEntries.size();
    }
    
    //Rebuild
    public void rebuild(LocalDate ldFrom, LocalDate ldTo)
    {
        clear(true);
        
        dqEntries.addAll(Database.getEntries(this, ldFrom, ldTo));
        
        for(Entry e : dqEntries)
            switch(e.getDirection())
            {
                case IN:    dqEntriesIn.add(e);     break;
                case OUT:   dqEntriesOut.add(e);    break;
            }
        
        for(Entry e : dqEntries)
            e.initIntervalLabels();
        
        recalculatePropertiesAndCategoryTotals();
    }
    public void refresh()
    {
        recalculatePropertiesAndCategoryTotals();
    }
    
    //Categories
    public List<Category> getCategories(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return new ArrayList<>(mpCategoriesIn.values());
            case OUT:   return new ArrayList<>(mpCategoriesOut.values());
        }
        
        return new ArrayList<>();
    }
    public List<Category> getCategoriesNonZero(int iDirection)
    {
        List<Category> lst = new ArrayList<>();
        
        for(Category c : getCategories(iDirection))
            if(c.getTotal() > 0)
                lst.add(c);
        
        return lst;
    }
    public List<Category> getCategoriesNonZero(int iDirection, int iType)
    {
        List<Category> lst = new ArrayList<>();
        
        for(Category c : getCategories(iDirection))
            if(c.getType() == iType && c.getTotal() > 0)
                lst.add(c);
        
        return lst;
    }
    public Category getCategory(String sName, int iDirection)
    {
        return getCategoryFromMap(sName, iDirection);
    }
    public boolean containsCategory(Category c)
    {
        if(getCategoryFromMap(c.getName(), c.getDirection()) == null)
            return false;

        return true;
    }
    public boolean containsCategory(String sName, int iDirection)
    {
        if(getCategoryFromMap(sName, iDirection) == null)
            return false;

        return true;
    }
    public void addCategory(Category c)
    {
        addCategoryToMapAndDatabase(c);
    }
    public void deleteCategory(Category c)
    {
        deleteCategoryFromMapAndDatabase(c);
        
        for(Entry e : dqEntries)
            if(e.getCategoryName().equals(c.getName()))
                e.setCategoryName(Categories.getUndefinedName(), false);
        
        for(AutoCategory ac : getAutoCategories(c.getDirection()))
            if(ac.getCategoryName().equals(c.getName()))
                deleteAutoCategory(ac);
        
        for(Watch w : getWatches(c.getDirection()))
            if(w.getCategoryName().equals(c.getName()))
                deleteWatch(w);
        
        recalculatePropertiesAndCategoryTotals();
    }
    public void deleteAllCategories(int iDirection)
    {
        deleteAllCategoriesFromMapAndDatabase(iDirection);
        
        for(Entry e : dqEntries)
            if(e.getDirection() == iDirection)
                e.setCategoryName(Categories.getUndefinedName(), false);
        
        for(AutoCategory ac : getAutoCategories(iDirection))
            deleteAutoCategory(ac);
        
        for(Watch w : getWatches(iDirection))
            deleteWatch(w);
        
        recalculatePropertiesAndCategoryTotals();
    }
    public void renameCategory(Category c, String sNewName)
    {
        String sOldName = c.getName();
        
        renameCategoryInMapAndDatabase(c, sNewName);
        
        for(Entry e : dqEntries)
            if(e.getCategoryName().equals(sOldName))
                e.setCategoryName(sNewName, false);
        
        for(AutoCategory ac : getAutoCategories(c.getDirection()))
            if(ac.getCategoryName().equals(sOldName))
                ac.setCategoryName(sNewName);
        
        for(Watch w : getWatches(c.getDirection()))
            if(w.getCategoryName().equals(sOldName))
                w.setCategoryName(sNewName);
        
        recalculatePropertiesAndCategoryTotals();
    }
    public void setCategory(List<Entry> lst, Category c)
    {
        for(Entry e : lst)
            e.setCategoryName(c.getName(), true);
        
        recalculatePropertiesAndCategoryTotals();

        Categories.refresh();
        Categories.rebuildContextMenus();
    }
    
    //Internal Transfers
    public void loadInternalTransfers()
    {
        for(Account a : Accounts.getAccounts())
            if(a != this)
            {
                addCategoryToMap(new Category(iID, findInternalTransferName(a, IN), IN, CATEGORY_TYPE_INTERNAL_TRANSFER));
                addCategoryToMap(new Category(iID, findInternalTransferName(a, OUT), OUT, CATEGORY_TYPE_INTERNAL_TRANSFER));
            }
    }
    public boolean isInternalTransferName(String sName, int iDirection)
    {
        String s = sName.toLowerCase();
        
        switch(iDirection)
        {
            case IN:
            {
                if(mpCategoriesIn.containsKey(s))
                    return mpCategoriesIn.get(s).getType() == CATEGORY_TYPE_INTERNAL_TRANSFER;
            }
            case OUT:
            {
                if(mpCategoriesOut.containsKey(s))
                    return mpCategoriesOut.get(s).getType() == CATEGORY_TYPE_INTERNAL_TRANSFER;
            }
        }
        
        return false;
    }
    
    //Internal Transfers - Integrity Check
    public void initInternalTransfersEntries()
    {
        Map<String, Integer> mpInternalTransferNames = buildInternalTransferNamesMap();
        
        lstInternalTransferEntries.clear();
        
        for(Entry e : Database.getEntries(this))
            if(mpInternalTransferNames.containsKey(e.getCategoryName()))
                lstInternalTransferEntries.add(new EntryInternalTransfer(e, mpInternalTransferNames.get(e.getCategoryName())));
    }
    public List<EntryInternalTransfer> getInternalTransferEntries()
    {
        return lstInternalTransferEntries;
    }
    public List<String> getInternalTransferNames()
    {
        return new ArrayList<String>(buildInternalTransferNamesMap().keySet());
    }
    
    //AutoCategories
    public List<AutoCategory> getAutoCategories(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return new ArrayList<>(mpAutoCategoriesIn.values());
            case OUT:   return new ArrayList<>(mpAutoCategoriesOut.values());
        }
        
        return null;
    }
    public String getAutoCategory(String sEntryName, int iDirection)
    {
        AutoCategory ac = getAutoCategoryFromMap(sEntryName, iDirection);
        
        if(ac == null)      return UNDEFINED_TEXT;
        else                return ac.getCategoryName();
    }
    public void setAutoCategory(AutoCategory ac)
    {
        if(isAutoCategoryInMap(ac))     setAutoCategoryInMapAndDatabase(ac);
        else                            addAutoCategoryToMapAndDatabase(ac);
        
        Database.setCategoryOnAllEntries(ac);
        
        for(Entry e : dqEntries)
            if(e.getDirection() == ac.getDirection() && e.getName().equals(ac.getEntryName()))
                e.setCategoryName(ac.getCategoryName(), true);
    }
    public void deleteAutoCategory(AutoCategory ac)
    {
        deleteAutoCategoryFromMapAndDatabase(ac);
    }
    public void deleteAllAutoCategories(int iDirection)
    {
        deleteAllAutoCategoriesFromMapAndDatabase(iDirection);
    }
    public void refreshAutoCategoriesFirstLastFrequency()
    {
        for(AutoCategory ac : getAutoCategories(IN))
            setAutoCategoryFirstLastFrequency(ac);
        
        for(AutoCategory ac : getAutoCategories(OUT))
            setAutoCategoryFirstLastFrequency(ac);
    }
    public int numberOfAutoCategories(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return mpAutoCategoriesIn.size();
            case OUT:   return mpAutoCategoriesOut.size();
        }
        
        return NOT_DEFINED;
    }
    
    //Watches
    public List<Watch> getWatches(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return new ArrayList<>(mpWatchesIn.values());
            case OUT:   return new ArrayList<>(mpWatchesOut.values());
        }
        
        return null;
    }
    public Watch getWatch(String sCategoryName, int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return mpWatchesIn.get(sCategoryName);
            case OUT:   return mpWatchesOut.get(sCategoryName);
        }
        
        return null;
    }
    public void addWatch(Watch w)
    {
        addWatchToMapAndDatabase(w);
        update(w);
    }
    public void deleteWatch(Watch w)
    {
        deleteWatchFromMapAndDatabase(w);
    }
    public void deleteAllWatches()
    {
        deleteAllWatchesFromMapAndDatabase();
    }
    public boolean isWatched(Category c)
    {
        return isWatchInMap(c);
    }
    public boolean isWatched(String sCategoryName, int iDirection)
    {
        return isWatchInMap(sCategoryName, iDirection);
    }
    public void update(Watch w)
    {
        Entry e = Database.getLastWatchedOccurence(this, w);
        
        if(e == null)
        {
            w.setLastDate(NOT_DEFINED);
            w.setLastAmount(NOT_DEFINED);
        }
        else
        {
            w.setLastDate(e.getDate());
            w.setLastAmount(e.getAmount());
        }
        
        Database.updateLastDateAndLastAmount(this, w);
    }
    public int numberOfWatches(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return mpWatchesIn.size();
            case OUT:   return mpWatchesOut.size();
        }
        
        return NOT_DEFINED;
    }
    
    //Refunds
    public List<Refund> getRefunds()
    {
        return lstRefunds;
    }
    public void addRefund(Refund r)
    {
        addRefundToListAndDatabase(r);
    }
    public void deleteRefund(Refund r)
    {
        deleteRefundFromListAndDatabase(r);
    }
    public void deleteAllRefunds()
    {
        deleteAllRefundsFromListAndDatabase();
    }
    public void updateRefund(Refund r, int iDate, String sName, String sKeywordsSerial, String sPounds, String sPence)
    {
        r.setDate(iDate);
        r.setName(sName);
        r.setKeywordsSerialised(sKeywordsSerial);
        r.setAmount((Integer.valueOf(sPounds) * 100) + Integer.valueOf(sPence));
        
        updateRefundInDatabase(r);
    }
    public void check(Refund r)
    {
        r.clearPotentials();
        
        for(Entry e : Database.getEntriesWithDirection(this, r.getDate(), IN))
            for(String sKeyword : r.getKeywords())
                if(e.getName().toLowerCase().contains(sKeyword.toLowerCase()))
                {
                    r.addPotential(e);
                    break;
                }
        
        if(r.isReceived())
            r.setStatus(REFUND_RECEIVED_PREFIX + " " + Formatter.convert(DATE, DATABASE, TABLEVIEW, r.getReceivedDate()));
        else
            switch(r.getNumberOfPotentials())
            {
                case 0:     r.setStatus("No potential refunds since date");                             break;
                case 1:     r.setStatus("1 potential refund since date");                               break;
                default:    r.setStatus(r.getNumberOfPotentials() + " potential refunds since date");   break;
            }
    }
    public void refundReceived(Refund r, Entry e)
    {
        r.received(e);
        r.setStatus(REFUND_RECEIVED_PREFIX + " " + Formatter.convert(DATE, DATABASE, TABLEVIEW, r.getReceivedDate()));
        updateRefundReceivedInDatabase(r);
    }
    public int numberOfRefunds()
    {
        return lstRefunds.size();
    }
    
    //Chart Bars and Lines
    public AccountChartBars getChartBars()
    {
        return acbChartBars;
    }
    public AccountChartLines getChartLines()
    {
        return aclChartLines;
    }
}