package AuctioningSystem;
/*
	Code: auction server		AuctionServer.java

	Server code for hosting the auctionImpl object
*/


import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Naming;	//Import naming classes to bind to rmiregistry
import java.io.Serializable;
import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;




public class FrontEndImpl extends java.rmi.server.UnicastRemoteObject implements BuyingInterface, SellerInterface, Receiver, Serializable {
  private RpcDispatcher disp;
  private RspList rsp_list;
  private JChannel channel;
  private RequestOptions opts;
  private Address a;

  public FrontEndImpl() throws java.rmi.RemoteException, Exception {

        opts = new RequestOptions(ResponseMode.GET_ALL, 5000);

        channel=new JChannel();
        channel.setReceiver(this);
        a = channel.getAddress();

        disp=new RpcDispatcher(channel, this);

        channel.connect("AuctionCluster");
    }
  
    public void receive(Message message){
      System.out.println(message);
    }

    public void viewAccepted(View view){

    }


  public byte[] sign(byte[] b) 
        throws java.rmi.RemoteException{
      try {
          rsp_list = disp.callRemoteMethods(null, "sign", new Object[]{b}, new Class[]{byte.class}, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
      } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
          return null;
    }

    public boolean returnSignedNumber(byte[] b, int uid){
      System.out.println("Hey");
      return false;
    }


    public byte[] authUser(int uid) 
        throws java.rmi.RemoteException{
          return null;

    }


    public Collection<Auction> listAllAuctions()
        throws java.rmi.RemoteException{
          return null;
            
        }
    
    public User getUser(int uid){
        return null;
    }

    public int bid(int auctionId, Float bid, int uid)
        throws java.rmi.RemoteException{
          return 0;
            
        }

    //Method that creates a new auction and returns the ID
    public int newAuction(Item item)
        throws java.rmi.RemoteException{
          return 0;
            
        }  

    public int removeItem(int auctionId)
        throws java.rmi.RemoteException{
          return 0;
                        }
    //Method that returns highestbid
    public Float getHighestBid(int auctionId)
        throws java.rmi.RemoteException{
          return null;
            
        }
    //Method that takes the id and returns a string depending on the state the auction is at when its closed.
    public String closeAuction(int auctionId, int userID)
        throws java.rmi.RemoteException{
          return null;

        }

    @Override
    public void getState(OutputStream out) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState(InputStream in) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void suspect(Address adrs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void block() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unblock() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}