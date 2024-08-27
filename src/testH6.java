
public class testH6 {
	public static void main(String[] args) {
		AuctionTable t = new AuctionTable();
		
		Auction a = new Auction("511601118", 6200.0, "cubsfantony", "gosha555@excite.com", 53, "Pentium III 933 System - 256MB PC133 SDram");
		t.putAuction(a.getAuctionID(), a);
		
		Auction b = new Auction("511448507", 620.0, "ct-inc", "petitjc@yahoo.com", 54, "Pentium III 800EB-MHz Coppermine CPU - 256");
		t.putAuction(b.getAuctionID(), b);
		
		
		Auction c = new Auction("511364992", 625.0, "bestbuys4systems", "wizbang4", 53, "Genuine Intel Pentium III 1000MHz Processo");
		t.putAuction(c.getAuctionID(), c);
		
		
		t.printTable();
		
		System.out.println("OPEN\n    Current Bid: $ " + String.format("%,.2f", 1500.0));
		System.out.println("    Current Bid: $ 1,500.00");
//		Auction get = t.getAuction("511448507");
//		
//		System.out.println("\n" + get);
		
//		t.letTimePass(53);
//		
//		t.printTable();
//		//try {
//		t.removeExpiredAuctions();
//		
//		t.printTable();
//		} catch(Exception ex) {
//			System.out.println(ex);
//		}
	}
}
