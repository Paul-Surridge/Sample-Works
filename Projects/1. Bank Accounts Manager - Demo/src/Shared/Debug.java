package Shared;

import Accounts.Account;
import AutoCategories.AutoCategory;
import Categories.Category;
import Charts.LineChart.Line;
import Charts.StackedBarChart.Bars;
import History.Entry;
import History.EntryInternalTransfer;
import Watches.Watch;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javafx.scene.chart.XYChart;

public class Debug {
    
    public static void print(Number n)
    {
        System.out.println(n);
    }
    public static void print(int i)
    {
        System.out.println(i);
    }
    public static void print(double d)
    {
        System.out.println(d);
    }
    public static void print(String s)
    {
        System.out.println(s);
    }
    public static void print(boolean b)
    {
        System.out.println(b);
    }
    public static void print(List<Account> lst)
    {
        for(int i = 0 ; i<lst.size() ; i++)
            print(lst.get(i).getName());
    }
    public static void print(double[] dArr)
    {
        for(double d : dArr)
            System.out.println(d + " ");
    }
    public static void print(Bars b)
    {
        for(XYChart.Data<String, Double> di : b.getSeries().getData())
            print("X: " + String.valueOf(di.getXValue()) + " Y: " + String.valueOf(di.getYValue()));
    }
    public static void print(Line l)
    {
        for(XYChart.Data<String, Double> di : l.getSeries().getData())
            print("X: " + String.valueOf(di.getXValue()) + " Y: " + String.valueOf(di.getYValue()));
    }
    public static void print(Entry e)
    {
        print(e.getName());
        print(e.getDate());
        print(e.getAmount());
    }
    public static void print(LocalDate ld)
    {
        print(ld.getDayOfMonth() + " " + ld.getMonthValue() + " " + ld.getYear());
    }
    public static void printListString(List<String> lst)
    {
        int iCounter = 1;
        
        for(String s : lst)
        {
            print(iCounter + ". " + s);
            iCounter++;
        }
    }
    public static void printListAccount(List<Account> lst)
    {
        int iCounter = 1;
        
        for(Account a : lst)
        {
            print(iCounter + ". " + a.getName());
            iCounter++;
        }
    }
    public static void printListEntry(List<Entry> lst)
    {
        int iCounter = 1;
        
        for(Entry e : lst)
        {
            print(iCounter + ". " + e.getDate() + " " + e.getName()  + " " + e.getType() + " " + e.getCategoryName() + " " + e.getIn() + " " + e.getOut() + " " + e.getBalance());
            iCounter++;
        }
    }
    public static void printListCategory(List<Category> lst)
    {
        int iCounter = 1;
        
        for(Category c : lst)
        {
            print(iCounter + ". " + c.getName() + " " + c.getDirectionText());
            iCounter++;
        }
    }
    public static void printListAutoCategory(List<AutoCategory> lst)
    {
        int iCounter = 1;
        
        for(AutoCategory ac : lst)
        {
            print(iCounter + ". " + ac.getEntryName() + " " + ac.getCategoryName() + " " + ac.getDirectionText());
            iCounter++;
        }
    }
    public static void printListWatch(List<Watch> lst)
    {
        int iCounter = 1;
        
        for(Watch w : lst)
        {
            print(iCounter + ". " + w.getCategoryName() + " " + w.getDirectionText());
            iCounter++;
        }
    }
    public static void printListError(List<Error> lst)
    {
        for(Error e : lst)
        {
            print(e.numberProperty() + " " + e.errorProperty().get()  + " " + e.detailsProperty().get());
        }
    }
    public static void printListInternalTransfers(List<EntryInternalTransfer> lst)
    {
        for(EntryInternalTransfer it : lst)
        {
            print(it.getDate() + " " + it.getSourceEntry().getAccountName() + " " + it.getDestinationAccountNameAbbreviated()  + " " + it.getAmount());
        }
    }
    public static void printMapAsInteger(Map<String, Integer> mp)
    {
        int iCounter = 1;
        for(String s : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + s + "\t" + "Value: " + mp.get(s));
            iCounter++;
        }
    }
    public static void printMapAsIntegerInteger(Map<Integer, Integer> mp)
    {
        int iCounter = 1;
        for(Integer i : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + i + "\t" + "Value: " + mp.get(i));
            iCounter++;
        }
    }
    public static void printMapAsIntegerString(Map<Integer, String> mp)
    {
        int iCounter = 1;
        for(int i : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + i + "\t" + "Value: " + mp.get(i));
            iCounter++;
        }
    }
    public static void printMapAsDouble(Map<String, Double> mp)
    {
        int iCounter = 1;
        for(String s : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + s + "\t" + "Value: " + mp.get(s));
            iCounter++;
        }
    }
    public static void printMapAsBoolean(Map<String, Boolean> mp)
    {
        int iCounter = 1;
        for(String s : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + s + "\t" + "Value: " + mp.get(s));
            iCounter++;
        }
    }
    public static void printMapStringDouble(Map<String, Double> mp)
    {
        int iCounter = 1;
        for(String s : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + s + "\t" + "Value: " + mp.get(s));
            iCounter++;
        }
    }
    public static void printMapStringCategory(Map<String, Category> mp)
    {
        int iCounter = 1;
        for(String s : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + s + "\t" + "Value: " + mp.get(s).getName());
            iCounter++;
        }
    }
    public static void printMapStringAutoCategory(Map<String, AutoCategory> mp)
    {
        int iCounter = 1;
        for(String s : mp.keySet())
        {
            print(iCounter + "." + "\t" + "Key: " + s + "\t" + "Value: " + mp.get(s).getCategoryName());
            iCounter++;
        }
    }
}
