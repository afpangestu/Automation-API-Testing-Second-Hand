package pojo;

public class OfferItem {
    private Integer price;
    private Integer productId;
    private Integer toId;
    private String status;

    public void setPrice(Integer price){
        this.price = price;
    }

    public Integer getPrice(){
        return price;
    }

    public void setProductId(Integer productId){
        this.productId = productId;
    }

    public Integer getProductId(){
        return productId;
    }

    public void setToId(Integer toId){
        this.toId = toId;
    }

    public Integer getToId(){
        return toId;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
