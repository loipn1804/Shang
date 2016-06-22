package digimatic.shangcommerce.model;

/**
 * Created by USER on 05/04/2016.
 */
public class RedeemableItem {

    private String RedeemRuleDetailID;
    private double Amount;
    private String VoucherTypeCode;
    private String VoucherName;
    private double RewardsRequired;
    private double RewardsRequiredMax;
    private String ActiveFrom;
    private String ActiveTo;
    private int MaxRedeems;
    private boolean isCheck;
    private int Quantity;

    public RedeemableItem(String redeemRuleDetailID, double amount, String voucherTypeCode, String voucherName, double rewardsRequired, double rewardsRequiredMax, String activeFrom, String activeTo, int maxRedeems, boolean isCheck, int quantity) {
        RedeemRuleDetailID = redeemRuleDetailID;
        Amount = amount;
        VoucherTypeCode = voucherTypeCode;
        VoucherName = voucherName;
        RewardsRequired = rewardsRequired;
        RewardsRequiredMax = rewardsRequiredMax;
        ActiveFrom = activeFrom;
        ActiveTo = activeTo;
        MaxRedeems = maxRedeems;
        this.isCheck = isCheck;
        Quantity = quantity;
    }

    public String getRedeemRuleDetailID() {
        return RedeemRuleDetailID;
    }

    public void setRedeemRuleDetailID(String redeemRuleDetailID) {
        RedeemRuleDetailID = redeemRuleDetailID;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getVoucherTypeCode() {
        return VoucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        VoucherTypeCode = voucherTypeCode;
    }

    public String getVoucherName() {
        return VoucherName;
    }

    public void setVoucherName(String voucherName) {
        VoucherName = voucherName;
    }

    public double getRewardsRequired() {
        return RewardsRequired;
    }

    public void setRewardsRequired(double rewardsRequired) {
        RewardsRequired = rewardsRequired;
    }

    public double getRewardsRequiredMax() {
        return RewardsRequiredMax;
    }

    public void setRewardsRequiredMax(double rewardsRequiredMax) {
        RewardsRequiredMax = rewardsRequiredMax;
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

    public int getMaxRedeems() {
        return MaxRedeems;
    }

    public void setMaxRedeems(int maxRedeems) {
        MaxRedeems = maxRedeems;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
