package AuctioningSystem;
/*
	Code: frontend server		FrontEndImpl. java

*/


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Message.TransientFlag;
import org.jgroups.util.Rsp;
import org.jgroups.util.Util;




public class FrontEndImpl extends java.rmi.server.UnicastRemoteObject implements BuyingInterface, SellerInterface, Receiver, MembershipListener {
  private RpcDispatcher disp;
  private RspList rsp_list;
  private JChannel channel;
  private RequestOptions opts;
  private Address a;
  private Map<Integer, byte[]> randomNumbers;
   private View current_view;

  public FrontEndImpl() throws java.rmi.RemoteException, Exception {
        randomNumbers = new HashMap<Integer, byte[]>();
        opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
        opts.setTransientFlags(TransientFlag.DONT_LOOPBACK);

        channel=new JChannel();
        channel.setReceiver(this);

        disp=new RpcDispatcher(channel, this, this, this);
        //this.channel.setDiscardOwnMessages(true);
        channel.connect("AuctionCluster");
    }
  
  @Override
    public void receive(Message message){
      System.out.println(message);
    }

    public void viewAccepted(View view){
        System.out.println(view.getMembers());
        current_view = view;
    }

public byte[] sign(byte[] b) 
        throws java.rmi.RemoteException{ //server signs byte array
            byte[] sig = null;
            try{
                //reads in private key
                File fileIn = new File("serverprivate.key"); 
                FileInputStream fis = new FileInputStream(fileIn);
                ObjectInputStream ois = new ObjectInputStream(fis);
                PrivateKey priv = (PrivateKey)ois.readObject();
                Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
                dsa.initSign(priv);
                dsa.update(b);
                sig = dsa.sign();
                
            } catch(Exception e){
                System.out.println(e.toString());
            }
            return sig;
        
    }

    public boolean returnSignedNumber(byte[] b, int uid){
        boolean outcome = false;
        try{
            //checks user's signature with user's public key
            File fileIn = new File("public" + uid + ".key");
            FileInputStream fis = new FileInputStream(fileIn);
            ObjectInputStream ois = new ObjectInputStream(fis);
            PublicKey pub = (PublicKey)ois.readObject();
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pub);
            sig.update(randomNumbers.get(uid));
            outcome = sig.verify(b);
        } catch(Exception e){
            System.out.println(e.toString());
        }
        return outcome;
    }
    public Rsp filterResponses(RspList rsps){
        //filter responses
        if(rsps == null) return null;
        Object previous = rsps.getFirst();
        boolean mismatch = false;
        
        for(int i = 1 ; i < rsps.size(); i++){
            if(!previous.equals(rsps.get(i))){
                mismatch = true;
                break;
            }
            previous = rsps.get(i);
        }
        if(mismatch){
            return null;
        }
        else{
            return (Rsp) previous;
        }
        
    }

    public byte[] authUser(int uid) 
        throws java.rmi.RemoteException{
            Random rand = new Random();
            byte[] nonse = new byte[10];
            rand.nextBytes(nonse);
            randomNumbers.put(uid,nonse);
            return nonse;
    }

    public Collection<Auction> listAllAuctions()
        throws java.rmi.RemoteException{
        try {
          rsp_list = disp.callRemoteMethods(null, "listAllAuctions", null, null, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
      } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
       return (Collection<Auction>) rsp_list.getResults().get(0);
            
        }
    
    public User getUser(int uid){
        try {
          rsp_list = disp.callRemoteMethods(null, "getUser", new Object[]{uid}, new Class[]{int.class}, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
      } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
       return (User) rsp_list.getResults().get(0);
    }

    public int bid(int auctionId, Float bid, int uid)
        throws java.rmi.RemoteException{
          try {
          rsp_list = disp.callRemoteMethods(null, "bid", new Object[]{auctionId, bid, uid}, new Class[]{int.class, Float.class, int.class}, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
      } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
       return Integer.valueOf(rsp_list.getResults().get(0).toString());
            
        }

    //Method that creates a new auction and returns the ID
    public int newAuction(Item item)
        throws java.rmi.RemoteException{
          try {
          rsp_list = disp.callRemoteMethods(null, "newAuction", new Object[]{item}, new Class[]{Item.class}, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
      } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
       return Integer.valueOf(rsp_list.getResults().get(0).toString());
        }  

    public int removeItem(int auctionId)
        throws java.rmi.RemoteException{
          try {
          rsp_list = disp.callRemoteMethods(null, "removeItem", new Object[]{auctionId}, new Class[]{int.class}, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
        } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return Integer.valueOf(rsp_list.getResults().get(0).toString());
        }
    //Method that returns highestbid
    public Float getHighestBid(int auctionId)
        throws java.rmi.RemoteException{
          try {
                rsp_list = disp.callRemoteMethods(null, "getHighestBid", new Object[]{auctionId}, new Class[]{int.class}, opts);
                System.out.println("Responses: " + rsp_list);
                System.out.println("ha" + rsp_list.getResults());
              } catch (Exception ex) {
                    Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
       return Float.valueOf(rsp_list.getResults().get(0).toString());
            
        }
    //Method that takes the id and returns a string depending on the state the auction is at when its closed.
    public String closeAuction(int auctionId, int userID)
        throws java.rmi.RemoteException{
          try {
          rsp_list = disp.callRemoteMethods(null, "closeAuction", new Object[]{auctionId, userID}, new Class[]{int.class, int.class}, opts);
          System.out.println("Responses: " + rsp_list);
          System.out.println("ha" + rsp_list.getResults());
      } catch (Exception ex) {
          Logger.getLogger(FrontEndImpl.class.getName()).log(Level.SEVERE, null, ex);
      }
       return rsp_list.getResults().get(0).toString();

        }

  @Override
        public void getState(OutputStream output) throws Exception{
    }

  @Override
    public void setState(InputStream input) throws Exception{
    }


    @Override
    public void suspect(Address adrs) {
        System.out.println(adrs + " is dead");
    }

    @Override
    public void block() {
    }

    @Override
    public void unblock() {
    }
}