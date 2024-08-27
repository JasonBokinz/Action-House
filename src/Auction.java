/**
 * This class represents an active auction currently in the database.
 * @author jasonbokinz, ID: 1125537, R:03
 */
import java.io.Serializable;
public class Auction implements Serializable {
	/**
	 * @param auctionID
	 * ID number of the auction
	 * @param sellerName
	 * name of the auction's seller
	 * @param buyerName
	 * name of the current buyer
	 * @param itemInfo
	 * information of the auction
	 * @param timeRemaining
	 * time remaining for the auction
	 * @param currentBid
	 * current bid of the auction
	 */
	private String auctionID, sellerName, buyerName, itemInfo;
	private int timeRemaining;
	private double currentBid;
	/**
	 * This creates a default constructor of an auction object.
	 */
	public Auction() {
		auctionID = null;
		sellerName = null;
		buyerName = null;
		itemInfo = null;
		timeRemaining = 0;
		currentBid = 0.0;
	}
	/**
	 * This creates a constructor of an auction object.
	 * @param auctionID
	 * ID number of the auction
	 * @param currentBid
	 * current bid of the auction
	 * @param sellerName
	 * name of the auction's seller
	 * @param buyerName
	 * name of the auction's current buyer
	 * @param timeRemaining
	 * time remaining for the auction
	 * @param itemInfo
	 * information of the auction
	 */
	public Auction(String auctionID, double currentBid, String sellerName, String buyerName, int timeRemaining,String itemInfo) {
		this.auctionID = auctionID;
		this.sellerName = sellerName;
		this.buyerName = buyerName;
		this.itemInfo = itemInfo;
		this.timeRemaining = timeRemaining;
		this.currentBid = currentBid;
	}
	/**
	 * This method is used to access the auction's ID number
	 * @return
	 * ID number of the auction
	 */
	public String getAuctionID() {
		return auctionID;
	}
	/**
	 * This method is used to access the seller's name
	 * @return
	 * name of the auction's seller
	 */
	public String getSellerName() {
		return sellerName;
	}
	/**
	 * This method is used to access the auction's buyer name
	 * @return
	 * name of the auction's current buyer
	 */
	public String getBuyerName() {
		return buyerName;
	}
	/**
	 * This method is used to access the auction's item information
	 * @return
	 * information of the auction item
	 */
	public String getItemInfo() {
		return itemInfo;
	}
	/**
	 * This method is used to access the auction's time remaining
	 * @return
	 * time remaining for the auction
	 */
	public int getTimeRemaining() {
		return timeRemaining;
	}
	/**
	 * This method is used to access the auction's current bid
	 * @return
	 * current bid of the auction
	 */
	public double getCurrentBid() {
		return currentBid;
	}
	/**
	 * This method is used to decrement the auction's time remaining
	 * @param time
	 * time to subtract from the time remaining
	 */
	public void decrementTimeRemaining(int time) {
		if (time > timeRemaining) {
			timeRemaining = 0;
		}
		else {
			timeRemaining -= time;
		}
	}
	/**
	 * This method is used to create a new bid for the auction
	 * @param bidderName
	 * new buyer name of the auction
	 * @param bidAmt
	 * amount they are bidding
	 * @throws ClosedAuctionException
	 * thrown if the auction's time remaining is 0
	 */
	public void newBid(String bidderName, double bidAmt) throws ClosedAuctionException {
		if (timeRemaining == 0) {
			throw new ClosedAuctionException("Auction is closed and no more bids can be placed.");
		}
		if (currentBid < bidAmt) {
			buyerName = bidderName;
			currentBid = bidAmt;
		}
	}
	/**This method is used to format the information of the auction neatly
	 * @overrides toString
	 * @return
	 * neat string of the object
	 */
	public String toString() {
		if (currentBid != 0.0) {
			return String.format("%11s | $%,9.2f | %-21s |  %-23s| %9s | %s", auctionID, currentBid, sellerName, buyerName, timeRemaining + " hours", itemInfo.length() > 42 ? itemInfo.substring(0, 42) : itemInfo);
		}
		else {
			return String.format("%11s | %10s | %-21s |  %-23s| %9s | %s", auctionID, "", sellerName, buyerName, timeRemaining + " hours", itemInfo.length() > 42 ? itemInfo.substring(0, 42) : itemInfo);
		}
	}
	
}
