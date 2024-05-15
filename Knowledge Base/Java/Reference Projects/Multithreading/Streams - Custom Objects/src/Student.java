public class Student {
    
    private String sName;
    private boolean bLocal;

    public Student(String sName, boolean bLocal) {
        this.sName  = sName;
        this.bLocal = bLocal;
    }

    public String getName() {
        return sName;
    }

    public boolean isLocal() {
        return bLocal;
    }
}
