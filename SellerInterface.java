package AuctioningSystem;
/*
	Code: auction Interface	auction.java
	Date: 10th October 2000

	The auction interface provides a description of the 5 remote 
	arithmetic methods available as part of the service provided
	by the remote object auctionimpl. Note carefully that the interface
	extends remote and each methods throws a remote exception.
*/	


public interface SellerInterface 
          extends java.rmi.Remote {	

    public int newAuction(Item item)
        throws java.rmi.RemoteException;

    public int removeItem(int auctionId)
        throws java.rmi.RemoteException;

    public Float getHighestBid(int auctionId)
        throws java.rmi.RemoteException;

    public String closeAuction(int auctionId, int uid)
        throws java.rmi.RemoteException;
    public byte[] sign(byte[] b) throws java.rmi.RemoteException;
    public byte[] authUser(int uid) throws java.rmi.RemoteException;
    public boolean returnSignedNumber(byte[] b, int uid) throws java.rmi.RemoteException;
}
