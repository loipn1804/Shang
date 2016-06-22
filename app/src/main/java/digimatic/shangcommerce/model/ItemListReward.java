package digimatic.shangcommerce.model;

/**
 * Created by USER on 05/04/2016.
 */
public class ItemListReward {

    private String RedeemRuleID;
    private String ItemCode;
    private String ItemName;
    private String ItemDescription;
    private String ItemURL;
    private String ItemImageURL;
    private int MinRewardtoDeduct;
    private int MaxRewardtoDeduct;
    private String ActiveFrom;
    private String ActiveTo;
    private int MaxRedeem;

    public ItemListReward(String redeemRuleID, String itemCode, String itemName, String itemDescription, String itemURL, String itemImageURL, int minRewardtoDeduct, int maxRewardtoDeduct, String activeFrom, String activeTo, int maxRedeem) {
        RedeemRuleID = redeemRuleID;
        ItemCode = itemCode;
        ItemName = itemName;
        ItemDescription = itemDescription;
        ItemURL = itemURL;
        ItemImageURL = itemImageURL;
        MinRewardtoDeduct = minRewardtoDeduct;
        MaxRewardtoDeduct = maxRewardtoDeduct;
        ActiveFrom = activeFrom;
        ActiveTo = activeTo;
        MaxRedeem = maxRedeem;
    }

    public String getRedeemRuleID() {
        return RedeemRuleID;
    }

    public void setRedeemRuleID(String redeemRuleID) {
        RedeemRuleID = redeemRuleID;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemURL() {
        return ItemURL;
    }

    public void setItemURL(String itemURL) {
        ItemURL = itemURL;
    }

    public String getItemImageURL() {
        return ItemImageURL;
    }

    public void setItemImageURL(String itemImageURL) {
        ItemImageURL = itemImageURL;
    }

    public int getMinRewardtoDeduct() {
        return MinRewardtoDeduct;
    }

    public void setMinRewardtoDeduct(int minRewardtoDeduct) {
        MinRewardtoDeduct = minRewardtoDeduct;
    }

    public int getMaxRewardtoDeduct() {
        return MaxRewardtoDeduct;
    }

    public void setMaxRewardtoDeduct(int maxRewardtoDeduct) {
        MaxRewardtoDeduct = maxRewardtoDeduct;
    }

    public String getActiveFrom() {
        return ActiveFrom;
    }

    public void setActiveFrom(String activeFrom) {
        ActiveFrom = activeFrom;
    }

    public String getActiveTo() {
        return ActiveTo;
    }

    public void setActiveTo(String activeTo) {
        ActiveTo = activeTo;
    }

    public int getMaxRedeem() {
        return MaxRedeem;
    }

    public void setMaxRedeem(int maxRedeem) {
        MaxRedeem = maxRedeem;
    }
}
