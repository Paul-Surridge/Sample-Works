package Administration;

import Shared.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Windows implements Constants{
    
    private SplitPane sp;
    private Timeline tl = new Timeline();
    private int iSelectedWindow;
    
    public Windows(SplitPane sp)
    {
        this(sp, NOT_DEFINED);
    }
    public Windows(SplitPane sp, int iWindow)
    {
        this.sp = sp;
        
        iSelectedWindow = iWindow;
        
        resetDividers();
    }
    
    //Internal -----------------------------------------------------------------
    
    //Window expand/collapse
    private void addKeyFrame(int iDivider, double dPosition)
    {
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(CHART_SIZE_TRANSLATE_DURATION),    new KeyValue(sp.getDividers().get(iDivider).positionProperty(),   dPosition)));
    }
    private void setTimeline(double... dPositions)
    {
        for(int i = 0 ; i<dPositions.length ; i++)
            addKeyFrame(i, dPositions[i]);
    }
    private void refreshWindows()
    {
        tl.getKeyFrames().clear();
        
        switch(sp.getDividers().size())
        {
            case 1:
            {
                switch(iSelectedWindow)
                {
                    case 0:     setTimeline(0.919);             break;
                    case 1:     setTimeline(0);                 break;
                    default:    setTimeline(0.5);               break;
                }
                break;
            }
            case 2:
            {
                switch(iSelectedWindow)
                {
                    case 0:     setTimeline(0.836,  0.919);     break;
                    case 1:     setTimeline(0,      0.919);     break;
                    case 2:     setTimeline(0,      0.093);     break;
                    default:    setTimeline(0.33,   0.66);      break;
                }
                
                break;
            }
            case 3:
            {
                switch(iSelectedWindow)
                {
                    case 0:     setTimeline(0.753,  0.836,  0.919);     break;
                    case 1:     setTimeline(0,      0.836,  0.919);     break;
                    case 2:     setTimeline(0,      0.093,  0.919);     break;
                    case 3:     setTimeline(0,      0.093,  0.143);     break;
                    default:    setTimeline(0.25,   0.50,   0.75);      break;
                }
                
                break;
            }
        }

        tl.setCycleCount(1);
        tl.play();
    }
    
    //Reset dividers to default positions
    private void resetDividers()
    {
        switch(sp.getDividers().size())
        {
            case 1:
            {
                switch(iSelectedWindow)
                {
                    case 0:     sp.getDividers().get(0).setPosition(0.919);     break;
                    case 1:     sp.getDividers().get(0).setPosition(0);         break;
                    default:    sp.getDividers().get(0).setPosition(0.5);       break;
                }
                
                break;
            }
            case 2:
            {
                switch(iSelectedWindow)
                {
                    case 0:
                    {
                        sp.getDividers().get(0).setPosition(0.836);
                        sp.getDividers().get(1).setPosition(0.919);
                        
                        break;
                    }
                    case 1:
                    {
                        sp.getDividers().get(0).setPosition(0);
                        sp.getDividers().get(1).setPosition(0.919);
                        
                        break;
                    }
                    case 2:
                    {
                        sp.getDividers().get(0).setPosition(0);
                        sp.getDividers().get(1).setPosition(0.093);
                        
                        break;
                    }
                    default:
                    {
                        sp.getDividers().get(0).setPosition(0.33);
                        sp.getDividers().get(1).setPosition(0.66);
                        
                        break;
                    }
                }
                
                break;
            }
            case 3:
            {
                switch(iSelectedWindow)
                {
                    case 0:
                    {
                        sp.getDividers().get(0).setPosition(0.753);
                        sp.getDividers().get(1).setPosition(0.836);
                        sp.getDividers().get(2).setPosition(0.919);
                        
                        break;
                    }
                    case 1:
                    {
                        sp.getDividers().get(0).setPosition(0);
                        sp.getDividers().get(1).setPosition(0.836);
                        sp.getDividers().get(2).setPosition(0.919);
                        
                        break;
                    }
                    case 2:
                    {
                        sp.getDividers().get(0).setPosition(0);
                        sp.getDividers().get(1).setPosition(0.093);
                        sp.getDividers().get(2).setPosition(0.919);
                        
                        break;
                    }
                    case 3:
                    {
                        sp.getDividers().get(0).setPosition(0);
                        sp.getDividers().get(1).setPosition(0.093);
                        sp.getDividers().get(2).setPosition(0.143);
                        
                        break;
                    }
                    default:
                    {
                        sp.getDividers().get(0).setPosition(0.25);
                        sp.getDividers().get(1).setPosition(0.50);
                        sp.getDividers().get(2).setPosition(0.75);
                        
                        break;
                    }
                }
                
                break;
            }
        }
    }
    
    //Add/remove windows
    private void appendWindow(Table tb)
    {
        int iLastDivider = sp.getDividers().size()-1;
        double dPos;
        
        if(sp.getDividers().isEmpty())
            dPos = 0.5;
        else
        {
            dPos = sp.getDividers().get(iLastDivider).getPosition();
            dPos = dPos + ((1 - dPos) / 2);
        }
        
        sp.getItems().add(new AnchorPane(tb.getTableView()));
        sp.getDividers().get(iLastDivider+1).setPosition(dPos);
    }
    private void replaceWindow(Table tb, int iPosition)
    {
        double[] dPositions = sp.getDividerPositions();
        
        sp.getItems().remove(iPosition);
        sp.getItems().add(iPosition, tb.getTableView());
        
        for(int i = 0 ; i<sp.getDividers().size() ; i++)
            sp.getDividers().get(i).setPosition(dPositions[i]);
    }
    
    //External API -------------------------------------------------------------
    
    //Window expand/collapse
    public void toggleWindow(int iWindow)
    {
        if(iSelectedWindow == iWindow)      setWindow(NOT_DEFINED);
        else                                setWindow(iWindow);
    }
    public void setWindow(int iWindow)
    {
        iSelectedWindow = iWindow;
        
        refreshWindows();
    }
    
    //Set divider positions
    public void setDivider(int iDivider, double dPosition)
    {
        tl.getKeyFrames().clear();
        
        for(int i = 0 ; i<sp.getDividers().size() ; i++)
            if(i == iDivider)   addKeyFrame(i, dPosition);
            else                addKeyFrame(i, sp.getDividerPositions()[i]);
                
        tl.setCycleCount(1);
        tl.play();
    }
    public void setDividers(int iDivider, double... dPositions)
    {
        tl.getKeyFrames().clear();
        
        for(int i = 0 ; i<sp.getDividers().size() ; i++)
            if(i == iDivider)   addKeyFrame(i, dPositions[i]);
            else                addKeyFrame(i, sp.getDividerPositions()[i]);
                
        tl.setCycleCount(1);
        tl.play();
    }
    
    //Reset dividers to default positions
    public void reset()
    {
        iSelectedWindow = NOT_DEFINED;
        
        resetDividers();
    }
    public void reset(int iWindow)
    {
        iSelectedWindow = iWindow;
        
        resetDividers();
    }
    
    //Add/remove windows
    public void addWindow(Table tb, int iPosition)
    {
        int iNumberOfWindows = sp.getItems().size();
        
        if(iPosition >= iNumberOfWindows)   appendWindow(tb);
        else                                replaceWindow(tb, iPosition);
    }
    public void removeWindow(int iPosition)
    {
        if(iPosition < sp.getItems().size())
            sp.getItems().remove(iPosition);
    }
    public void removeAllWindows()
    {
        while(sp.getItems().size()>1)
            sp.getItems().remove(sp.getItems().size()-1);
    }
    public int numberOfWindows()
    {
        return sp.getItems().size();
    }
}
