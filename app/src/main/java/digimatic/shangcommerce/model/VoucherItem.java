package digimatic.shangcommerce.model;

/**
 * Created by USER on 05/05/2016.
 */
public class VoucherItem {

    private String ConversionLogID;
    private String VoucherCode;
    private int Type;
    private String VoucherName;
    private String ValidFrom;
    private String ValidTo;
    private double Amount;
    private double ValidSpendAmountFrom;
    private double ValidSpendAmountTo;
    private int ValidQuantityFrom;
    private int ValidQuantityTo;
    private boolean isCheck;
    private boolean isUsed;

    public VoucherItem(String conversionLogID, String voucherCode, int type, String voucherName, String validFrom, String validTo, double amount, double validSpendAmountFrom, double validSpendAmountTo, int validQuantityFrom, int validQuantityTo, boolean isCheck, boolean isUsed) {
        ConversionLogID = conversionLogID;
        VoucherCode = voucherCode;
        Type = type;
        VoucherName = voucherName;
        ValidFrom = validFrom;
        ValidTo = validTo;
        Amount = amount;
        ValidSpendAmountFrom = validSpendAmountFrom;
        ValidSpendAmountTo = validSpendAmountTo;
        ValidQuantityFrom = validQuantityFrom;
        ValidQuantityTo = validQuantityTo;
        this.isCheck = isCheck;
        this.isUsed = isUsed;
    }

    public String getConversionLogID() {
        return ConversionLogID;
    }

    public void setConversionLogID(String conversionLogID) {
        ConversionLogID = conversionLogID;
    }

    public String getVoucherCode() {
        return VoucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        VoucherCode = voucherCode;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getVoucherName() {
        return VoucherName;
    }

    public void setVoucherName(String voucherName) {
        VoucherName = voucherName;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(String validFrom) {
        ValidFrom = validFrom;
    }

    public String getValidTo() {
        return ValidTo;
    }

    public void setValidTo(String validTo) {
        ValidTo = validTo;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public double getValidSpendAmountFrom() {
        return ValidSpendAmountFrom;
    }

    public void setValidSpendAmountFrom(double validSpendAmountFrom) {
        ValidSpendAmountFrom = validSpendAmountFrom;
    }

    public double getValidSpendAmountTo() {
        return ValidSpendAmountTo;
    }

    public void setValidSpendAmountTo(double validSpendAmountTo) {
        ValidSpendAmountTo = validSpendAmountTo;
    }

    public int getValidQuantityFrom() {
        return ValidQuantityFrom;
    }

    public void setValidQuantityFrom(int validQuantityFrom) {
        ValidQuantityFrom = validQuantityFrom;
    }

    public int getValidQuantityTo() {
        return ValidQuantityTo;
    }

    public void setValidQuantityTo(int validQuantityTo) {
        ValidQuantityTo = validQuantityTo;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
}
