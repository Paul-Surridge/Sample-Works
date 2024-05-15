@interface Author
{
    //'Annotation Type Element Declarations'

    String 	Author();
    String 	Date();
    int 	CurrentRevision() 	default 1;
    String 	LastModified() 		default "N/A";
    String 	LastModifiedBy() 	default "N/A";

    // Array
    String[] Reviewers();
}
@interface MethodCalled
{}
@Author
(
    Author 		= "John Doe",
    Date 		= "3/17/2002",
    CurrentRevision 	= 6,
    LastModified 	= "4/12/2004",
    LastModifiedBy 	= "Jane Doe",

    // Array
    Reviewers 		= {"Alice", "Bob", "Cindy"}
)
class ClassA
{
    int i;
    
    @interface NestedAnnotation
    {
        //'Annotation Type Element Declarations'

        String 	Author();
        String 	Date();
        int 	CurrentRevision() 	default 1;
        String 	LastModified() 		default "N/A";
        String 	LastModifiedBy() 	default "N/A";

        // Array
        String[] Reviewers();
    }
    
    @MethodCalled
    void method()
    {
        
    }
}