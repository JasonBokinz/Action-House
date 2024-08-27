/**
 * This class contains methods to help form a hash map to store auctions.
 * @author jasonbokinz, ID: 1125537, R:03
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import big.data.*;

public class AuctionTable extends HashMap implements Serializable {
	/**
	 * @param map
	 * hash map of auctions with a string key
	 */
	private HashMap<String, Auction> map;
	
	public AuctionTable() {
		 map = new HashMap<String, Auction>();
	}
	/**
	 * This creates a constructor that creates an auction table object
	 * @param auctions
	 * Array of auctions from the URL
	 */
	public AuctionTable(Auction[] auctions) {
		map = new HashMap<String, Auction>();
		 for (int i = 0; i < auctions.length; i++) {
			 map.put(auctions[i].getAuctionID(), auctions[i]);
		 }
	}
	/**
	 * This method adds a new auction to the hash map
	 * @param auctionID
	 * key for the hash map (ID number)
	 * @param auction
	 * value for the hash map (auction)
	 * @throws IllegalArgumentException
	 * thrown if the hash map already contains the key
	 */
	public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException {
		if (this.containsKey(auctionID))
			throw new IllegalArgumentException("Acution already contains this ID.");
		map.put(auctionID, auction);
		
	}
	/**
	 * This method is used to access an auction in the hash map
	 * @param auctionID
	 * key for the hash map (ID number)
	 * @return
	 * auction with the matching auction ID
	 */
	public Auction getAuction(String auctionID) {
		return map.get(auctionID);
	}
	/**
	 * This method is used to decrement all of the auction's time remaining in the hash map
	 * @param numHours
	 * time to subtract from the time remaining
	 * @throws IllegalArgumentException
	 * thrown if the auction's time remaining is 0
	 */
	public void letTimePass(int numHours) throws IllegalArgumentException {
		if (numHours < 0) {
			throw new IllegalArgumentException("Time to pass is negative.");
		}
		for(Entry<String, Auction> entry: map.entrySet()) {
		      entry.getValue().decrementTimeRemaining(numHours);
		}
	}
	/**
	 * This method removes all of the auction's with a time remaining of 0 in the hash map
	 */
	public void removeExpiredAuctions() {
		ArrayList<String> expired = new ArrayList<String>();
		for(Entry<String, Auction> entry: map.entrySet()) {
		      if (entry.getValue().getTimeRemaining() == 0) {
		    	  String r = entry.getValue().getAuctionID();
		    	  expired.add(r);
		      }
		}
		for (int i = 0; i < expired.size(); i++) {
			map.remove(expired.get(i));
		}
	}
	/**
	 * This method prints the whole auction table
	 */
	public void printTable() {
		System.out.println(String.format("\n%11s%2s%9s%4s%14s%10s%15s%11s%8s%4s%11s", "Auction ID", "|", "Bid", "|", "Seller", "|", "Buyer", "|", "Time", "|", "Item Info"));
		System.out.println("===================================================================================================================================");
		for(Entry<String, Auction> entry: map.entrySet()) {
			System.out.println(entry.getValue());
		}
	}
	/**
	 * This method saves the hash map into a file so it can be read in again later.
	 * @throws IOException
	 * Error in writing occurred
	 */
	public void writeSerialization() throws IOException {
		try {
			FileOutputStream file = new FileOutputStream("auction.obj");
			ObjectOutputStream outStream = new ObjectOutputStream(file);
			outStream.writeObject(map);
			
			outStream.close();
			file.close();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
	/**
	 * This method is used to read a previously saved hash in file if there is one to begin with.
	 * @throws IOException
	 * thrown if no previous table is detected
	 * @throws ClassNotFoundException
	 * thrown if the class is not found
	 */
	public void readSerialization() throws IOException, ClassNotFoundException {
		try {
			FileInputStream file = new FileInputStream("auction.obj");
			ObjectInputStream inStream = new ObjectInputStream(file);
			map = (HashMap)inStream.readObject();
			System.out.println("Loading previous Auction Table...\n");
			
			inStream.close();
			file.close();
			
		} catch (IOException ex) {
			System.out.println("No previous auction table detected.");
			System.out.println("Creating new table...\n");
		} catch (ClassNotFoundException ex) {
			System.out.println(ex);
		}
	}
	/**
	 * This method uses the BigData library to construct an AuctionTable from a remote data source.
	 * @param URL
	 * URL of the XML file
	 * @return
	 * auction table of auctions from the URL
	 * @throws IllegalArgumentException
	 * thrown if the URL doesn't represent a valid data source
	 */
	public static AuctionTable buildFromURL(String URL) throws IllegalArgumentException {
		AuctionTable table;
		Auction[] auctions;
		DataSource ds = DataSource.connect(URL).load();
		
		String[] auctionIDs = ds.fetchStringArray("listing/auction_info/id_num");
		String[] buyerNames = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
		String[] sellerNames = ds.fetchStringArray("listing/seller_info/seller_name");
		for (int i = 0; i < auctionIDs.length; i++) {
			sellerNames[i] = sellerNames[i].replace("\n", "");
			sellerNames[i] = sellerNames[i].replace("\r", "");
			
			buyerNames[i] = buyerNames[i].replace("\n", "");
			buyerNames[i] = buyerNames[i].replace("\r", "");
		}
		
		String[] currentBidsStr = ds.fetchStringArray("listing/auction_info/current_bid");
		Double[] currentBids = new Double[currentBidsStr.length];
		
		for (int i = 0; i < currentBidsStr.length; i++) {
			currentBidsStr[i] = currentBidsStr[i].substring(1).replaceAll(",", "");
			currentBids[i] = Double.parseDouble(currentBidsStr[i]);
		}
		String[] timeLeftStr = ds.fetchStringArray("listing/auction_info/time_left");
		int[] timeLeft = new int[timeLeftStr.length];
		for (int i = 0; i < timeLeftStr.length; i++) {
			String timeStr;
			int time;
			String[] s = timeLeftStr[i].split(",");
			for (int j = 0; j < s.length; j++) {
				if (s[j].contains("day")) {
					timeStr = s[j].trim().substring(0, s[j].indexOf(" "));
					time = Integer.parseInt(timeStr) * 24;
					timeLeft[i] += time;
				}
				else {
					s[j] = s[j].trim();
					timeStr = s[j].substring(0, s[j].indexOf(" "));
					time = Integer.parseInt(timeStr);
					timeLeft[i] += time;
				}
			}
		}
		String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
		String[] memory = ds.fetchStringArray("listing/item_info/memory");
		String[] hardDrive = ds.fetchStringArray("listing/item_info/hard_drive");
		String[] itemInfo = new String[cpu.length];
		for (int i = 0; i < itemInfo.length; i++) {
			if (cpu[i].equals("")) {
				cpu[i] = "N/A";
			}
			if (memory[i].equals("")) {
				memory[i] = "N/A";
			}
			if (hardDrive[i].equals("")) {
				hardDrive[i] = "N/A";
			}
			String res = cpu[i];
			res += " - "  + memory[i] + " - " + hardDrive[i];
			itemInfo[i] = res;
		}
		auctions = new Auction[cpu.length];
		for (int i = 0; i < cpu.length; i++) {
			if (auctionIDs[i].equals("")) {
				auctionIDs[i] = "N/A";
			}
			if (sellerNames[i].equals("")) {
				sellerNames[i] = "N/A";
			}
			if (buyerNames[i].equals("")) {
				buyerNames[i] = "N/A";
			}
			if (sellerNames[i].equals("")) {
				sellerNames[i] = "N/A";
			}
			Auction newAuction = new Auction(auctionIDs[i], currentBids[i], sellerNames[i], buyerNames[i], timeLeft[i],itemInfo[i]);
			auctions[i] = newAuction;
		}
		table = new AuctionTable(auctions);
		return table;
	}
}
