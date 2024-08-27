/**
 * This class conatins a system of auctions.
 * @author jasonbokinz, ID: 1125537, R:03
 */
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
public class AuctionSystem implements Serializable {
	
	public static void printMenu() {
		System.out.println("\nMenu:");
		System.out.println(String.format("%30s", "(D) - Import Data from URL"));
		System.out.println(String.format("%30s", "(A) - Create a New Auction"));
		System.out.println(String.format("%24s", "(B) - Bid on an Item"));
		System.out.println(String.format("%29s", "(I) - Get Info on Auction"));
		System.out.println(String.format("%28s", "(P) - Print All Auctions"));
		System.out.println(String.format("%33s", "(R) - Remove Expired Auctions"));
		System.out.println(String.format("%23s", "(T) - Let Time Pass"));
		System.out.println(String.format("%14s", "(Q) - Quit"));
	}
	public static void main(String[] args) throws ClosedAuctionException, IOException, ClassNotFoundException {
		AuctionTable table = new AuctionTable();
		Scanner input = new Scanner(System.in);
		String selection = "";
		System.out.println("Starting...");
		table.readSerialization();
		System.out.println("Please select a username: ");
		String username = input.nextLine();
		
		try {
		
			while (!selection.equals("Q")) {
				printMenu();
				System.out.println("Please select an option: ");
				selection = input.nextLine().toUpperCase();
			
				switch(selection) {
			
				case "D":
					System.out.println("Please enter a URL: ");
				String url = input.nextLine();
				table = AuctionTable.buildFromURL(url);
				System.out.println("\nLoading...");
				System.out.println("Auction data loaded successfully!");
				break;
				
			case "A":
				System.out.println("Creating new Auction as " + username + ".");
				System.out.println("Please enter an Auction ID: ");
				String newID = input.nextLine();
				System.out.println("Please enter an Auction time (hours): ");
				String newTimeStr = input.nextLine();
				int newTime = Integer.parseInt(newTimeStr);
				System.out.println("Please enter some Item Info: ");
				String newInfo = input.nextLine();
				Auction newAuction = new Auction(newID, 0.0, username, "", newTime, newInfo );
				table.putAuction(newID, newAuction);
				System.out.println("\nAuction " + newID + " inserted into table.");
				break;
				
			case "B":
				System.out.println("Please enter an Auction ID: ");
				String id = input.nextLine();
				Auction bid = table.getAuction(id);
				System.out.print("Auction " + bid.getAuctionID() + " is ");
				if (bid.getTimeRemaining() == 0) {
					if (bid.getCurrentBid() != 0.0) {
						System.out.println("CLOSED\n    Current Bid: $ " + String.format("%,.2f", bid.getCurrentBid()));
					}
					else {
						System.out.println("CLOSED\n    Current Bid: None");
					}
					System.out.println("You can no longer bid on this item.");
				}
				else {
					if (bid.getCurrentBid() != 0.0) {
						System.out.println("OPEN\n    Current Bid: $ " + String.format("%,.2f", bid.getCurrentBid()));
					}
					else {
						System.out.println("OPEN\n    Current Bid: None");
					}
					System.out.println("What would you like to bid?: ");
					String newBidStr = input.nextLine();
					Double newBid = Double.parseDouble(newBidStr);
					if (newBid <= bid.getCurrentBid()) {
						System.out.println("Bid was not accepted.");
					}
					else {
						System.out.println("Bid accepted.");
						table.getAuction(id).newBid(username, newBid);
					}
				}
				break;
				
			case "I":
				System.out.println("Please enter an Auction ID: ");
				String searchID = input.nextLine();
				Auction found = table.getAuction(searchID);
				System.out.println("Auction " + searchID + ":");
				System.out.println("    Seller: " + found.getSellerName());
				System.out.println("    Buyer: " + found.getBuyerName());
				System.out.println("    Time: " + found.getTimeRemaining() + " hours");
				System.out.println("    Info: " + found.getItemInfo());
				break;
				
			case "P":
				table.printTable();
				break;
				
			case "R":
				System.out.println("Removing expired auctions...");
				table.removeExpiredAuctions();
				System.out.println("All expired auctions removed.");
				break;
			
			case "T":
				System.out.println("How many hours should pass: ");
				String timePassedStr = input.nextLine();
				int timePassed = Integer.parseInt(timePassedStr);
				table.letTimePass(timePassed);
				System.out.println("\nTime passing...\nAuction times updated.");
				break;
				
			case "Q":
				table.writeSerialization();
				System.out.println("Writing Auction Table to file...");
				System.out.println("Done!");
				break;
				}
			
		}
		}catch (Exception ex) {
			System.out.println(ex);
		}
		System.out.println("Goodbye.");
	}
}
