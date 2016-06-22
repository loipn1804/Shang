package digimatic.shangcommerce.model;

/**
 * Created by USER on 3/15/2016.
 */
public class Address {

    public long entity_id;
    public String firstname;
    public String middlename;
    public String lastname;
    public String company;
    public String city;
    public String country_id;
    public String region;
    public String postcode;
    public String telephone;
    public String fax;
    public String street;
    public String street_2;
    public int is_default_billing;
    public int is_default_shipping;

    public Address(long entity_id, String firstname, String middlename, String lastname, String company, String city, String country_id, String region, String postcode, String telephone, String fax, String street, String street_2, int is_default_billing, int is_default_shipping) {
        this.entity_id = entity_id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.company = company;
        this.city = city;
        this.country_id = country_id;
        this.region = region;
        this.postcode = postcode;
        this.telephone = telephone;
        this.fax = fax;
        this.street = street;
        this.street_2 = street_2;
        this.is_default_billing = is_default_billing;
        this.is_default_shipping = is_default_shipping;
    }

    public long getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(long entity_id) {
        this.entity_id = entity_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_2() {
        return street_2;
    }

    public void setStreet_2(String street_2) {
        this.street_2 = street_2;
    }

    public int getIs_default_billing() {
        return is_default_billing;
    }

    public void setIs_default_billing(int is_default_billing) {
        this.is_default_billing = is_default_billing;
    }

    public int getIs_default_shipping() {
        return is_default_shipping;
    }

    public void setIs_default_shipping(int is_default_shipping) {
        this.is_default_shipping = is_default_shipping;
    }
}
