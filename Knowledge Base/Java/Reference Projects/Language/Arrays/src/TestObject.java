public class TestObject {
    
    int iVal;
    
    TestObject(int iVal)
    {
        this.iVal = iVal;
    }
    void setValue(int iVal)
    {
        this.iVal = iVal;
    }
    @Override
    public String toString()
    {
        return "Test Object:" + iVal;
    }
}
