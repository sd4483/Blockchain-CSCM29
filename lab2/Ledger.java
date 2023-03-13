package lab2;



/** 
 *   Ledger defines for each user the balance at a given time
     in the ledger model of bitcoins
     and contains methods for checking and updating the ledger
     including processing a transaction
 */

public class Ledger extends UserAmount{


    /** 
     *
     *  Task 4: Fill in the method checkTransactionValid
     *          You need to replace the dummy value true by the correct calculation
     *
     * Check a transaction is valid:
     *    the sum of outputs is less than or equal the sum of inputs
     *    and the inputs can be deducted from the ledger.
     *
     */    
    
    public boolean checkTxValid(Transaction tx){
	// Needs to be replaced by correct solution
    	return tx.checkTransactionAmountsValid() && tx.toInputs().checkDeductableFromLedger(this);
    };

    /** 
     *
     *  Task 5: Fill in the method processTransaction
     *
     * Process a transaction
     *    by first deducting all the inputs
     *    and then adding all the outputs.
     *
     */    
    

    public void processTransaction(Transaction tx){
	// Needs to be filled in.
    	if(checkTxValid(tx)) {
    		tx.toInputs().subtractFromLedger(this);
        	tx.toOutputs().addToLedger(this);
    	}
    };
    
    
    


    /** 
     *  Task 6: Fill in the testcases as described in the labsheet
     *    
     * Testcase
     */
    
    public static void printer(String s) {
    	for(int i = 0; i < 80; i++) {
    		System.out.print("*");
    	}
    	System.out.println("\n" + s);
    	for(int i = 0; i < 80; i++) {
    		System.out.print("*");
    	}
    	System.out.println(" ");
    }
    
    public static void test() {
	// Needs to be filled in
    	printer("Create an empty Ledger and add to it users Alice, Bob, Carol, David, initialised with\n"
    			+"the amount 0 for each user");
    	
    	Ledger testLedger = new Ledger();
    	
    	testLedger.addAccount("Alice", 0);
    	testLedger.addAccount("Bob", 0);
    	testLedger.addAccount("Carol", 0);
    	testLedger.addAccount("David", 0);
    	
    	testLedger.print();
    	
    	printer("Set the balance for Alice to 10\n" + "Set the balance for Bob to 20\n" + "Add to balance of Alice to 10\n" + "Subtract 5 from the balance of Alice");
    	
    	testLedger.setBalance("Alice", 10);
    	testLedger.setBalance("Bob", 20);
    	testLedger.addBalance("Alice", 10);
    	testLedger.subtractBalance("Alice", 5);
    	
    	testLedger.print();
    	
    	printer("check whether the EntryList el1 giving Alice 15 units, and Bob 10 units can be\n"
    			+ "deducted");
    	
    	EntryList el1 = new EntryList();
    	
    	el1.addEntry("Alice", 15);
    	el1.addEntry("Bob", 10);
    	
    	el1.print();
    	
    	System.out.println("Check deductable : " + el1.checkDeductableFromLedger(testLedger));
    	
    	printer("check whether the EntryList el2 giving Alice 10 units, giving Alice again 10 units,\n"
    			+ "and giving Bob 5 units, can be deducted (so Alice gets twice 10 units)");
    	
    	EntryList el2 = new EntryList();
    	
    	el2.addEntry("Alice", 10);
    	el2.addEntry("Alice", 10);
    	el2.addEntry("Bob", 5);
    	
    	el2.print();
    	
    	System.out.println("Check deductable : " + el2.checkDeductableFromLedger(testLedger));
    	
    	printer("deduct el1 from the ledger");
    	
    	el1.subtractFromLedger(testLedger);
    	
    	testLedger.print();
    	
    	printer("add el2 to the ledger");
    	
    	el2.addToLedger(testLedger);
    	
    	testLedger.print();
    	
    	printer("Create a transaction tx1 which takes as input for Alice 30 units and gives Bob 20 and\n"
    			+"Carol 20 units.");
    	
    	
    	Transaction tx1 = new Transaction(new EntryList("Alice", 30), new EntryList("Bob", 20, "Carol", 20));
    	
    	System.out.println("Check transaction valid : " + testLedger.checkTxValid(tx1));
    	
    	printer("Create a transaction tx2 which takes as input for Alice 20 units and gives Bob 5 and\n"
    			+ "Carol 15 units");
    	
    	Transaction tx2 = new Transaction(new EntryList("Alice", 20), new EntryList("Bob", 5, "Carol", 15));
    	
    	System.out.println("Check transaction valid : " + testLedger.checkTxValid(tx2));
    	    	
    	printer("Create a transaction tx3 which takes as input for Alice 25 units and gives Bob 10 and\n"
    			+ "Carol 15 units. Update Ledger by processing tx3");
    	
    	Transaction tx3 = new Transaction(new EntryList("Alice", 25), new EntryList("Bob", 10, "Carol", 15));
    	
    	System.out.println("Check transaction valid : " + testLedger.checkTxValid(tx3));
    	
    	testLedger.processTransaction(tx3);
    	    	
    	printer("Create a transaction tx4 which takes as inputs for Alice twice 5 units, and as output\n"
    			+ "to Bob 10 units. Update Ledger by processing tx4.");
    	
    	EntryList el4 = new EntryList();
    	
    	el4.addEntry("Alice", 5);
    	el4.addEntry("Alice", 5);
    	
    	Transaction tx4 = new Transaction(el4, new EntryList("Bob", 10));
    	
    	testLedger.processTransaction(tx4);
    	
    	testLedger.print();
    	
    }


    
    /** 
     * main function running test cases
     */            

    public static void main(String[] args) {
	Ledger.test();	
    }
}
