package lab3;

import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;


/** 
 *   Ledger defines an ledger in the ledger model of bitcoins
 *     it extends UserAmount
 */

public class Ledger extends UserAmount {




   
    /** 
     *
     *  Task 8 Check a transaction is valid.
     *
     *  this means that 
     *    the sum of outputs is less than or equal the sum of inputs
     *    all signatures are valid
     *    and the inputs can be deducted from the ledger.

     *    This method has been set to true so that the code compiles - that should
     *    be changed
     */    

    public boolean checkTransactionValid(Transaction tx){
	// to be replaced by the correct code
    	return tx.checkSignaturesValid() && tx.checkTransactionAmountsValid() && checkDeductableFromLedger(this);
    };


    /** 
     * Task 9 Fill in the method processTransaction
     *
     * Process a transaction
     *    by first deducting all the inputs
     *    and then adding all the outputs.
     * Requires that the transaction is valid
     */    
    
    public void processTransaction(Transaction tx){
    	if (tx.checkTransactionAmountsValid()) {
    		tx.toInputs().subtractFromLedger(this);
    		tx.toOutputs().addToLedger(this);
    	}
    };

    
    /** 
     * Prints the current state of the ledger. 
     */

    public void print(PublicKeyMap pubKeyMap) {
	for (PublicKey publicKey : publicKeyList ) {
	    Integer value = getBalance(publicKey);
	    System.out.println("The balance for " +
			       pubKeyMap.getUser(publicKey) + " is " + value); 
	}

    }



    /** 
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

    public static void test()
	throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

	Wallet exampleWallet = SampleWallet.generate(new String[]{ "Alice"});
	byte[] exampleMessage = KeyUtils.integer2ByteArray(1);
	byte[] exampleSignature = exampleWallet.signMessage(exampleMessage,"Alice");


	/***   Task 10
               add  to the test case the test as described in the lab sheet
                
               you can use the above exampleSignature, when a sample signature is needed
	       which cannot be computed from the data.

	**/
	
	printer("Create a sample wallet for Alice containing keys with names A1, A2; a (separate)\n"
			+ "sample wallet for Bob containing keynames B1, B2; one for Carol containing\n"
			+ "keynames C1, C2, C3; and one for David containing keyname D1 by using the\n"
			+ "method generate of SampleWallet.");

	Wallet AliceWallet = SampleWallet.generate(new String[] {"A1", "A2"});
	Wallet BobWallet = SampleWallet.generate(new String[] {"B1", "B2"});
	Wallet CarolWallet = SampleWallet.generate(new String[] {"C1", "C2", "C3"});
	Wallet DavidWallet = SampleWallet.generate(new String[] {"D1"});
	
	printer("Print out the wallet for Alice");
	
	AliceWallet.print();
    
	printer("Compute the PublicKeyMap containing the public keys of all these wallets.\n"
			+ "Print out the resulting PublicKeyMap.");
	
	PublicKeyMap keyMap = new PublicKeyMap();
	
	keyMap.addKey("A1", AliceWallet.getPublicKey("A1"));
	keyMap.addKey("A2", AliceWallet.getPublicKey("A2"));
	keyMap.addKey("B1", BobWallet.getPublicKey("B1"));
	keyMap.addKey("B2", BobWallet.getPublicKey("B2"));
	keyMap.addKey("C1", CarolWallet.getPublicKey("C1"));
	keyMap.addKey("C2", CarolWallet.getPublicKey("C2"));
	keyMap.addKey("C3", CarolWallet.getPublicKey("C3"));
	keyMap.addKey("D1", DavidWallet.getPublicKey("D1"));
		
	keyMap.print();
	
	printer("Create an empty Ledger and add to it the public keys for the keynames of the\n"
			+ "wallets created before initialised with the amount 0 for each key.");
	
	Ledger testLedger = new Ledger();
	
	for (PublicKey k: keyMap.getUser2PublicKey().values()) {
		testLedger.addAccount(k, 0);
	}
	
	testLedger.print(keyMap);
	
	printer("Set the balance for A1 to 40.\n"
			+ "Add 15 to the balance for B1.\n"
			+ "Add 5 to the balance for A2\n"
			+ "Subtract 15 from the balance for B1.\n"
			+ "Set the balance for C1 to 10.");
	
	testLedger.setBalance(keyMap.getPublicKey("A1"), 40);
	
	testLedger.addBalance(keyMap.getPublicKey("B1"), 15);
	
	testLedger.addBalance(keyMap.getPublicKey("A2"), 5);
	
	testLedger.subtractFromBalance(keyMap.getPublicKey("B1"), 15);
	
	testLedger.setBalance(keyMap.getPublicKey("C1"), 10);
		
	testLedger.print(keyMap);
	
	printer("Check whether the InputList inList1 giving A1 40 units, and C1 10 units (with\n"
			+ "sample signatures used) can be deducted.");
	
	InputList inList1 = new InputList();
	
	inList1.addEntry(keyMap.getPublicKey("A1"), 40, exampleSignature);
	inList1.addEntry(keyMap.getPublicKey("C1"), 10, exampleSignature);
	
	inList1.print(keyMap);
	System.out.println("inList1 - Deducatble? " + inList1.checkDeductableFromLedger(testLedger));
	
	printer("Check whether the InputList inList2 giving A1 25 units, and giving A1 again\n"
			+ "25 units (with sample signatures used) can be deducted.");
	
	InputList inList2 = new InputList();
	
	inList2.addEntry(keyMap.getPublicKey("A1"), 25, exampleSignature);
	inList2.addEntry(keyMap.getPublicKey("A1"), 25, exampleSignature);
	
	inList2.print(keyMap);
	System.out.println("inList2 - Deducatble? " + inList2.checkDeductableFromLedger(testLedger));
	
	printer("Deduct inList1 from the Ledger.");
	
	inList1.subtractFromLedger(testLedger);
	
	testLedger.print(keyMap);
	
	printer("Create a OutputList corresponding to inList2 which gives A1 twice 25 Units,\n"
			+ "and add it to the Ledger.");
	
	OutputList outList2 = new OutputList();
	
	outList2.addEntry(keyMap.getPublicKey("A1"), 25);
	outList2.addEntry(keyMap.getPublicKey("A1"), 25);
	
	outList2.addToLedger(testLedger);
	
	outList2.print(keyMap);
	
	printer("Create a correctly signed input, where A1 is spending 50, referring to an output\n"
			+ "list giving B2 20 and C3 30. The output list is needed in order to create the\n"
			+ "message to be signed (consisting of A1 spending 50, B2 receiving 20 and C3\n"
			+ "receiving 30). Check whether the signature is valid for this signed input. Use the\n"
			+ "wallet for Alice to create the signatures.");
	
	OutputList outList3 = new OutputList();
	InputList inList3 = new InputList();
	
	outList3.addEntry(keyMap.getPublicKey("B2"), 20);
	outList3.addEntry(keyMap.getPublicKey("C3"), 30);
		
	outList3.print(keyMap);
	
	inList3.addEntry(keyMap.getPublicKey("A1"), 50, outList3, AliceWallet);
	
	inList3.print(keyMap);
	
	System.out.println("Check Signature: " + inList3.checkSignature(outList3));
	
	printer("Create a wrongly signed input, which gives A1 50, and uses instead of the correctly\n"
			+ "created signature an example signature (example signatures are provided in the\n"
			+ "code). Check whether the signature is valid for this signed input, and the same\n"
			+ "output as before.");
	
	InputList inList6 = new InputList();
	OutputList outList6 = new OutputList();
	
	inList6.addEntry(keyMap.getPublicKey("A1"), 50, exampleSignature);
	inList6.checkSignature(outList6);
	
	inList6.print(keyMap);
	
	System.out.println("Check Signature: " + inList6.checkSignature(outList6));
	
	printer("Create a transaction tx1 which takes as input for A1 50 units and gives B1 20, B2 20, and C2 10.");
	
	OutputList outList4 = new OutputList();
	
	outList4.addEntry(keyMap.getPublicKey("B1"), 20);
	outList4.addEntry(keyMap.getPublicKey("B2"), 20);
	outList4.addEntry(keyMap.getPublicKey("C2"), 10);
	
	InputList inList4 = new InputList();
	
	inList4.addEntry(keyMap.getPublicKey("A1"), 50, AliceWallet.getSignature(keyMap.getPublicKey("A1"), 50, outList4));
	
	Transaction tx1 = new Transaction(inList4, outList4);
		
	tx1.print(keyMap);
	
	printer("Check whether the signature is approved for the transaction input.");
	
	System.out.println("Check Signature: " + tx1.checkSignaturesValid());
	
	printer("Now check whether the complete transaction is valid (which will check again the\n"
			+ "validity of the signature). Then update the Ledger using that transaction.");
	
	System.out.println("Check Transaction: " + testLedger.checkTransactionValid(tx1));
	
	testLedger.processTransaction(tx1);
	
	printer("Create a transaction tx2 which takes as inputs from B2 20, C2 10, and as outputs\n"
			+ "given C3 10 and D1 20.");
    
    InputList inList5 = new InputList();
	OutputList outList5 = new OutputList();
	
	inList5.addEntry(keyMap.getPublicKey("B2"), 20, BobWallet.getSignature(keyMap.getPublicKey("B2"), 20, outList5));
	inList5.addEntry(keyMap.getPublicKey("C2"), 10, CarolWallet.getSignature(keyMap.getPublicKey("C2"), 10, outList5));	
	
	outList5.addEntry(keyMap.getPublicKey("C3"), 10);
	outList5.addEntry(keyMap.getPublicKey("D1"), 20);
	
	Transaction tx2 = new Transaction(inList5, outList5);
	
	tx2.print(keyMap);
	
	printer("Check whether the signature is approved for the transaction input, and then\n"
			+ "whether the transaction is valid (which includes a check for the signature). Then\n"
			+ "update the Ledger using that transaction.");
	
	System.out.println("Check Signature: " + tx2.checkSignaturesValid());
	
	System.out.println("Check Transaction: " + testLedger.checkTransactionValid(tx2));
	
	testLedger.processTransaction(tx2);
	
	printer("Final Ledger");
	
	testLedger.print(keyMap);
	
	}

    /** 
     * main function running test cases
     */            

    public static void main(String[] args)
	throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
	Ledger.test();
    }
}
