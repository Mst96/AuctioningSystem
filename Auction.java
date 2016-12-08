package AuctioningSystem;
/*
    Code: auction     auction.java

    Class that creates an auction object which holds seller and bidder information as well as the item information.

*/

import java.io.Serializable;

public class Auction implements Serializable {
    private User seller;
    private Item item;
    private User highestBidder;
    private Float highestBid;
    private int id;



    public Auction(Item i){
        item = i;
        highestBid = new Float(0);
        seller = i.getSeller();
    }

    //Setshighest bid by checking if the bid is higher than the current highest or the starting price
    public int setHighestBid(Float bid, User bidder){
        if(bid > highestBid && bid > item.getStartingPrice()){
            highestBid = bid;
            highestBidder = bidder;
            return 1;
        }
        else{
            System.out.println("Bid was too low");
            return 0;
        }
    }

    public String getSellerName(){
        return seller.getName();
    }

    public int getSellerID(){
        return seller.getID();
    }

    public Float getHighestBid(){
        if(highestBid < item.getStartingPrice()){
            return item.getStartingPrice();
        } else{
            return highestBid;
        }
    }

    public User getHighestBidder(){
        return highestBidder;
    }

    public void setId(int number){
        id = number;
    }
    public int getId(){
        return id;
    }
    public Item getItem(){
        return item;
    }

    public void close(int auctionID){
        if(highestBid > item.getReservePrice()){
            System.out.println("The winner is: " + highestBidder.getName() + " Email: " + highestBidder.getEmail());
        }
        else{
            System.out.println("The reserve price of this bid was not reached, nobody has won this auction.");
        }
    }
}
