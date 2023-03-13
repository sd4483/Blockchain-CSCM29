package lab2;

import java.util.ArrayList;

/** EntryList
 *  defines a list of Entries
 *  can be used as the list of inputs or list of outputs of a 
 *  transaction
 */

public class EntryList{


    /** 
      * the underlying list of user amounts
      */
    
    private ArrayList<Entry> txEntryList;

    /** 
      * add a user and an amount to the list
      */
    
    public void addEntry(String user, int amount){
	txEntryList.add(new Entry(user,amount));
    }

    /** 
      * constructor for the empty EntryList
      */
    
    public EntryList(){
	txEntryList =  new ArrayList<Entry>();
    }

    /** 
      * constructor for the EntryList containing one entry
        consisting of a user and an amount
      */    
    
    public EntryList(String user,int amount){
	txEntryList = new ArrayList<Entry>();
	addEntry(user,amount);
    }


    /** 
      * constructor for the EntryList containing two entries
        each consisting of a user and an amount
      */        

    public EntryList(String user1,int amount1,String user2,int amount2)
    {
	txEntryList = new ArrayList<Entry>();
	addEntry(user1,amount1);
	addEntry(user2,amount2);	
    }

    /** 
      * constructor for the EntryList containing three entries
        each consisting of a user and an amount
      */        

    public EntryList(String user1,int amount1,
		     String user2,int amount2,
		     String user3, int amount3)
    {
	txEntryList = new ArrayList<Entry>();
	addEntry(user1,amount1);
	addEntry(user2,amount2);
	addEntry(user3,amount3);		
    }
    


    /** 
      * obtain the underlying list
      */
    
    public ArrayList<Entry> toList(){
	return(txEntryList);
    };


    /** 
      * compute the sum of entries in the list
      */

    public int toSum(){
	int result = 0;
	for (Entry  entry : toList()){
	    result += entry.getAmount();
		};
	return result;
    };	


    /**   function  to print all items in the User Mmaount List
     *    in the form 
     *      word1  <user> word2 <amount>  
     */
    
    public void print(String word1, String word2) {
	for (Entry entry : txEntryList) {
	    entry.print(word1,word2);
	}
    }

    /** 
     * Default way of printing out the useramount
     */

    public void print() {
	print("User: "," value:  ");
    }

    /** 
     * Generic Test cases, providing a headline
     *    printing out the user amount list
     *    and printing out the sum of amounts in the user amount list.
     */


    /** 
     *
     *  Task 2: Fill in the method checkDeductableFromLedger
     *          You need to replace the dummy value true by the correct 
     *            calculation
     *
     *  It checks that the current EntryList (which will later be used for
     *     inputs of a transaction) can be deducted from ledger
     *
     *   done by first converting the current EntryList into a UserAmount
     *     and then checking that the resulting UserAmount can be deducted.
     *   
     */    


    public boolean checkDeductableFromLedger(Ledger ledger){
	// Needs to be replaced by proper code
    	UserAmount userAmount = new UserAmount(this);
    	return userAmount.checkDeductableFromLedger(ledger);
    };


    /** 
     *  Task 3: Fill in the methods subtractFromLedger and  addToLedger
     *
     *   subtractFromLedger(Ledger ledger)  
     *   subtracts the current List of TxEntries from ledger 
     *   requires that the list to be deducted is deductable.
     *   
     */    
    

    public void subtractFromLedger(Ledger ledger){
	// Needs to be filled in
    	if (checkDeductableFromLedger(ledger)) {
    		for(Entry entry: txEntryList) {
    			ledger.subtractBalance(entry.getUser(), entry.getAmount());
    		}
    	}
    }


    /** 
     *
     *   addToLedger(Ledger ledger)  
     *   adds the current List of TxEntries to ledger 
     *
     */    

    public void addToLedger(Ledger ledger){
	// Needs to be filled in
    	for(Entry entry: txEntryList) {
    		ledger.addBalance(entry.getUser(), entry.getAmount());
    	}
    }

    
    
    

    public void testCase(String header){
	System.out.println(header);
	print();
	System.out.println("transformed to User Accounts:");
	(new UserAmount(this)).print();
	System.out.println("Sum of Amounts = " + toSum());	
	System.out.println();	
    };

    /** 
     * Test cases
     */            
    
    public static void test() {
	EntryList l;
	(new EntryList("Alice",10)).testCase("Test Alice 10");

	(new EntryList("Bob",20)).testCase("Test Bob 20");
	
	(new EntryList("Alice",10,"Alice",10)).testCase("Alice twice 10");

	l = new EntryList("Alice",10,"Bob",20);
	l.testCase("Test Alice 10 and Bob  20");
	
	System.out.println("Same List but with words User and spends");	
	l.print("User "," spends ");		
	
    }
    

    /** 
     * main function running test cases
     */            
    
    public static void main(String[] args) {
	EntryList.test();
    }    

};    
