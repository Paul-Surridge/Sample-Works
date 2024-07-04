package Refunds;

import Database.Administration.DatabaseAdministrationRefundsTableInsertable;
import Shared.Constants;
import Shared.Formatter;
import javafx.beans.property.SimpleStringProperty;
import History.Entry;
import java.util.ArrayList;
import java.util.List;

public class Refund implements RefundsTableInsertable, DatabaseAdministrationRefundsTableInsertable, Constants{
    
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspRecordID = new SimpleStringProperty();
    private SimpleStringProperty sspDate = new SimpleStringProperty();
    private SimpleStringProperty sspName = new SimpleStringProperty();
    private SimpleStringProperty sspKeywords = new SimpleStringProperty();
    private SimpleStringProperty sspAmount = new SimpleStringProperty();
    private SimpleStringProperty sspStatus = new SimpleStringProperty();
    private SimpleStringProperty sspReceivedDate = new SimpleStringProperty();
    private SimpleStringProperty sspReceivedPrimaryKey = new SimpleStringProperty();
    
    private List<String> lstKeywords = new ArrayList<>();
    private List<Entry> lstPotentials = new ArrayList<>();
    
    private long lPrimaryKey, lRecordID, lReceivedPrimaryKey;
    private int iAccountID, iDate, iAmount;
    private int iPounds, iPence, iReceivedDate;
    
    //Via Database
    public Refund(int iAccountID, long lPrimaryKey, long lRecordID, int iDate, String sName, String sKeywords, int iAmount, int iReceivedDate, long lReceivedPrimaryKey)
    {
        this.iAccountID = iAccountID;
        this.lPrimaryKey = lPrimaryKey;
        this.lRecordID = lRecordID;
        this.iDate = iDate;
        this.iAmount = iAmount;
        this.iReceivedDate = iReceivedDate;
        this.lReceivedPrimaryKey = lReceivedPrimaryKey;
        
        initSimpleStringProperties(lPrimaryKey, lRecordID, iDate, sName, sKeywords, iAmount, iReceivedDate, lReceivedPrimaryKey);
        extractKeywords(sKeywords);
        extractPoundsAndPence();
    }
    //Via Datebase Import
    public Refund(int iAccountID, int iDate, String sName, String sKeywords, int iAmount, int iReceivedDate, long lReceivedPrimaryKey)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, iDate, sName, sKeywords, iAmount, iReceivedDate, lReceivedPrimaryKey);
    }
    //Via Context Menu Create
    public Refund(int iAccountID, int iDate, String sName, String sKeywords, String sPounds, String sPence)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, iDate, sName, sKeywords, (Integer.valueOf(sPounds) * 100) + Integer.valueOf(sPence), NOT_DEFINED, NOT_DEFINED);
    }
    
    //Internal -----------------------------------------------------------------
    private void initSimpleStringProperties(long lPrimaryKey, long lRecordID, int iDate, String sName, String sKeywords, int iAmount, int iReceivedDate, long lReceivedPrimaryKey)
    {
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
        sspRecordID.set(String.valueOf(lRecordID));
        sspDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
        sspName.set(sName);
        sspKeywords.set(sKeywords);
        sspAmount.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iAmount));
        sspStatus.set(DASH);
        sspReceivedDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iReceivedDate));
        sspReceivedPrimaryKey.set(String.valueOf(lReceivedPrimaryKey));
    }
    private void extractKeywords(String sKeywordsSerial)
    {
        String[] sFragments = sKeywordsSerial.split(" ");
        
        lstKeywords.clear();
        
        for(String s : sFragments)
            if(!s.equals(""))
                lstKeywords.add(s.trim());
        
        sspKeywords.set(sKeywordsSerial);
    }
    private void extractPoundsAndPence()
    {
        if(iAmount == NOT_DEFINED)
        {
            iPounds = NOT_DEFINED;
            iPence = NOT_DEFINED;
        }
        else
        {
            iPounds = iAmount / 100;
            iPence = iAmount % 100;
        }
    }
    private void refreshAmount()
    {
        setAmount((iPounds * 100) + iPence);
    }
    
    //External API -------------------------------------------------------------
    
    //Auto Categories Tableview 
    @Override
    public SimpleStringProperty primaryKeyProperty()
    {
        return sspPrimaryKey;
    }
    @Override
    public SimpleStringProperty recordIDProperty()
    {
        return sspRecordID;
    }
    @Override
    public SimpleStringProperty dateProperty()
    {
        return sspDate;
    }
    @Override
    public SimpleStringProperty nameProperty()
    {
        return sspName;
    }
    @Override
    public SimpleStringProperty keywordsProperty()
    {
        return sspKeywords;
    }
    @Override
    public SimpleStringProperty amountProperty()
    {
        return sspAmount;
    }
    @Override
    public SimpleStringProperty statusProperty()
    {
        return sspStatus;
    }
    @Override
    public SimpleStringProperty receivedDateProperty()
    {
        return sspReceivedDate;
    }
    @Override
    public SimpleStringProperty receivedPrimaryKeyProperty()
    {
        return sspReceivedPrimaryKey;
    }
    
    //ID
    public int getAccountID()
    {
        return iAccountID;
    }
    public long getPrimaryKey()
    {
        return lPrimaryKey;
    }
    public void setPrimaryKey(long lKey)
    {
        lPrimaryKey = lKey;
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
    }
    public long getRecordID()
    {
        return lRecordID;
    }
    public void setRecordID(long lID)
    {
        lRecordID = lID;
        sspRecordID.set(String.valueOf(lRecordID));
    }
    
    //Refund
    public int getDate()
    {
        return iDate;
    }
    public String getDateTextForTableView()
    {
        return Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate);
    }
    public void setDate(int i)
    {
        iDate = i;
        
        if(iDate == NOT_DEFINED)    sspDate.set("-");
        else                        sspDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
    }
    public String getName()
    {
        return sspName.get();
    }
    public void setName(String sName)
    {
        sspName.set(sName);
    }
    public List<String> getKeywords()
    {
        return lstKeywords;
    }
    public void setKeywordsSerialised(String sKeywordsSerial)
    {
        extractKeywords(sKeywordsSerial);
    }
    public String getKeywordsSerialised()
    {
        String sBuild = "";
        
        for(String s : lstKeywords)
            sBuild += s + " ";
        
        return sBuild.trim();
    }
    public int getAmount()
    {
        return iAmount;
    }
    public String getAmountText()
    {
        return Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, getAmount());
    }
    public void setAmount(int iAmount)
    {
        this.iAmount = iAmount;
        
        if(iAmount == NOT_DEFINED)  sspAmount.set("-");
        else                        sspAmount.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iAmount));
    }
    public int getPounds()
    {
        return iPounds;
    }
    public void setPounds(int i)
    {
        iPounds = i;
        
        refreshAmount();
    }
    public int getPence()
    {
        return iPence;
    }
    public void setPence(int i)
    {
        iPence = i;
        
        refreshAmount();
    }
    public List<Entry> getPotentials()
    {
        return lstPotentials;
    }
    public void addPotential(Entry e)
    {
        lstPotentials.add(e);
    }
    public int getNumberOfPotentials()
    {
        return lstPotentials.size();
    }
    
    //Status
    public void clearPotentials()
    {
        lstPotentials.clear();
    }
    public String getStatus()
    {
        return sspStatus.get();
    }
    public void setStatus(String sStatus)
    {
        sspStatus.set(sStatus);
    }
    
    //Received
    public int getReceivedDate()
    {
        return iReceivedDate;
    }
    public long getReceivedPrimaryKey()
    {
        return lReceivedPrimaryKey;
    }
    public boolean isReceived()
    {
        return lReceivedPrimaryKey != NOT_DEFINED;
    }
    public void received(Entry e)
    {
        iReceivedDate = e.getDate();
        sspReceivedDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iReceivedDate));
        
        lReceivedPrimaryKey = e.getPrimaryKey();
        sspReceivedPrimaryKey.set(String.valueOf(lReceivedPrimaryKey));
    }
}
