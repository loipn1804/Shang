package digimatic.shangcommerce.model;

/**
 * Created by USER on 05/05/2016.
 */
public class RedeemHistoryItem {

    private String RedeemLogID;
    private int RedeemType;
    private String RedeemTime;
    private String ItemCode;
    private String ItemName;
    private double ItemValue;
    private String ItemImageURL;
    private int RedeemedQuantity;
    private double RewardDeduct;
    private int RewardType;

    public RedeemHistoryItem(String redeemLogID, int redeemType, String redeemTime, String itemCode, String itemName, double itemValue, String itemImageURL, int redeemedQuantity, double rewardDeduct, int rewardType) {
        RedeemLogID = redeemLogID;
        RedeemType = redeemType;
        RedeemTime = redeemTime;
        ItemCode = itemCode;
        ItemName = itemName;
        ItemValue = itemValue;
        ItemImageURL = itemImageURL;
        RedeemedQuantity = redeemedQuantity;
        RewardDeduct = rewardDeduct;
        RewardType = rewardType;
    }

    public String getRedeemLogID() {
        return RedeemLogID;
    }

    public void setRedeemLogID(String redeemLogID) {
        RedeemLogID = redeemLogID;
    }

    public int getRedeemType() {
        return RedeemType;
    }

    public void setRedeemType(int redeemType) {
        RedeemType = redeemType;
    }

    public String getRedeemTime() {
        return RedeemTime;
    }

    public void setRedeemTime(String redeemTime) {
        RedeemTime = redeemTime;
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

    public double getItemValue() {
        return ItemValue;
    }

    public void setItemValue(double itemValue) {
        ItemValue = itemValue;
    }

    public String getItemImageURL() {
        return ItemImageURL;
    }

    public void setItemImageURL(String itemImageURL) {
        ItemImageURL = itemImageURL;
    }

    public int getRedeemedQuantity() {
        return RedeemedQuantity;
    }

    public void setRedeemedQuantity(int redeemedQuantity) {
        RedeemedQuantity = redeemedQuantity;
    }

    public double getRewardDeduct() {
        return RewardDeduct;
    }

    public void setRewardDeduct(double rewardDeduct) {
        RewardDeduct = rewardDeduct;
    }

    public int getRewardType() {
        return RewardType;
    }

    public void setRewardType(int rewardType) {
        RewardType = rewardType;
    }
}
