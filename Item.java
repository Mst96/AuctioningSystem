package AuctioningSystem;
/*
    Code: auction client     auctionClient.java
    Date: 10th October 2000

    Class that creates an item object which holds item information like the description, starting price and reserve price and seller information.


*/

import java.io.Serializable;

public class Item implements Serializable {
    private String description;
    private Float startingPrice;
    private Float reservePrice;
    private Float highestBid;
    private User seller;


    public Item(User user, Float sPrice, Float rPrice, String desc){
        seller = user;
        description = desc;
        startingPrice = sPrice;
        reservePrice = rPrice;
    }

    public String getSellerName(){
        String name = seller.getName();
        return name;
    }

    public User getSeller(){
        return seller;
    }

    public String getDescription(){
        return description;
    }

    public Float getStartingPrice(){
        return startingPrice;
    }

    public Float getReservePrice(){
        return reservePrice;
    }

    public Float getHighestBid(){
        return highestBid;
    }

    public void setHighestBid(Float bid){
        highestBid = bid;
    }


}
