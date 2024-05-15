package poker;

class TemporaryEvaluationCard implements Constants{

    private int iSuit, iValue, iTablePosition;
    
    int getSuit()
    {
        return iSuit;
    }
    int getValue()
    {
        return iValue;
    }
    int getTablePosition()
    {
        return iTablePosition;
    }
    void setSuit(int iSuit)
    {
        this.iSuit = iSuit;
    }
    void setValue(int iValue)
    {
        this.iValue = iValue;
    }
    void setTablePosition(int iTablePosition)
    {
        this.iTablePosition = iTablePosition;
    }
}
