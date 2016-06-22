package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(3, "greendao");

        Entity category = schema.addEntity("Category");
        category.addLongProperty("entity_id").primaryKey();
        category.addStringProperty("name");
        category.addIntProperty("position");
        category.addStringProperty("image_url");
        category.addStringProperty("thumbnail_url");
        category.addIntProperty("product_count");

        Entity brand = schema.addEntity("Brand");
        brand.addLongProperty("brand_id").primaryKey();
        brand.addStringProperty("name");
        brand.addStringProperty("short_description");
        brand.addStringProperty("image_url");
        brand.addStringProperty("thumbnail_url");

        Entity customer = schema.addEntity("Customer");
        customer.addLongProperty("entity_id").primaryKey();
        customer.addStringProperty("firstname");
        customer.addStringProperty("middlename");
        customer.addStringProperty("lastname");
        customer.addStringProperty("email");
        customer.addStringProperty("dob");
        customer.addStringProperty("facebook_id");
        customer.addBooleanProperty("is_login_fb");
        customer.addStringProperty("picture_url");
        customer.addIntProperty("cart_items_count");
        customer.addIntProperty("wishlist_items_count");
        customer.addIntProperty("unread_alerts_count");
        customer.addStringProperty("shipping_address");
        customer.addStringProperty("billing_address");
        customer.addIntProperty("is_notification_sound");
        customer.addIntProperty("orders_count");
        customer.addIntProperty("reward_points");
        customer.addDoubleProperty("evoucher_amount");
        customer.addIntProperty("log_num");
        customer.addStringProperty("card_code");

        Entity product = schema.addEntity("Product");
        product.addLongProperty("entity_id").primaryKey();
        product.addStringProperty("type_id");
        product.addStringProperty("name");
        product.addStringProperty("sku");
        product.addStringProperty("manufacturer");
        product.addStringProperty("short_description");
        product.addFloatProperty("price");
        product.addFloatProperty("special_price");
        product.addStringProperty("small_image_url");
        product.addStringProperty("thumbnail_url");
        product.addStringProperty("news_from_date");
        product.addStringProperty("news_to_date");
        product.addStringProperty("created_at");
        product.addStringProperty("updated_at");
        product.addIntProperty("is_product_new");
        product.addIntProperty("is_product_sale");

        Entity wishlist = schema.addEntity("Wishlist");
        wishlist.addLongProperty("wishlist_item_id").primaryKey();
        wishlist.addLongProperty("product_id");
        wishlist.addStringProperty("name");
        wishlist.addStringProperty("sku");
        wishlist.addStringProperty("manufacturer");
        wishlist.addFloatProperty("price");
        wishlist.addStringProperty("image_url");
        wishlist.addStringProperty("added_at");
        wishlist.addStringProperty("type_id");

        Entity cart_detail = schema.addEntity("CartDetail");
        cart_detail.addLongProperty("entity_id").primaryKey();
        cart_detail.addIntProperty("items_count");
        cart_detail.addIntProperty("items_qty");
        cart_detail.addFloatProperty("subtotal");
        cart_detail.addFloatProperty("grand_total");
        cart_detail.addStringProperty("coupon_code");
        cart_detail.addLongProperty("customer_id");
        cart_detail.addStringProperty("created_at");
        cart_detail.addStringProperty("updated_at");
        cart_detail.addFloatProperty("tax_amount");
        cart_detail.addFloatProperty("shipping_amount");
        cart_detail.addFloatProperty("discount_amount");
        cart_detail.addFloatProperty("use_reward_points");
        cart_detail.addStringProperty("discount_description");
        cart_detail.addStringProperty("voucher_list");

        Entity cart_item = schema.addEntity("CartItem");
        cart_item.addLongProperty("item_id").primaryKey();
        cart_item.addLongProperty("product_id");
        cart_item.addStringProperty("name");
        cart_item.addStringProperty("sku");
        cart_item.addStringProperty("manufacturer");
        cart_item.addFloatProperty("price");
        cart_item.addStringProperty("image_url");
        cart_item.addIntProperty("qty");
        cart_item.addFloatProperty("row_total");
        cart_item.addStringProperty("options");
        cart_item.addFloatProperty("discount_amount");
        cart_item.addFloatProperty("tax_amount");
        cart_item.addStringProperty("type_id");

        Entity country = schema.addEntity("Country");
        country.addStringProperty("value").primaryKey();
        country.addStringProperty("label");
        country.addIntProperty("set_default");

        Entity config_attribute = schema.addEntity("ConfigAttribute");
        config_attribute.addLongProperty("attribute_id").primaryKey();
        config_attribute.addStringProperty("code");
        config_attribute.addStringProperty("label");
        config_attribute.addIntProperty("position");

        Entity config_option = schema.addEntity("ConfigOption");
        config_option.addLongProperty("option_id").primaryKey();
        config_option.addStringProperty("label");
        config_option.addFloatProperty("price");
        config_option.addIntProperty("position");

        Entity config_product = schema.addEntity("ConfigProduct");
        config_product.addLongProperty("product_id").primaryKey();
        config_product.addStringProperty("base_image");
        config_product.addStringProperty("images");
        config_product.addIntProperty("position");

        Entity config_merge = schema.addEntity("ConfigMerge");
        config_merge.addLongProperty("id").primaryKey();
        config_merge.addLongProperty("attribute_id");
        config_merge.addLongProperty("option_id");
        config_merge.addLongProperty("product_id");
        config_merge.addIntProperty("is_chosen_att");
        config_merge.addIntProperty("is_chosen_opt");
        config_merge.addIntProperty("position");

        Entity notification = schema.addEntity("Notification");
        notification.addLongProperty("id").primaryKey();
        notification.addLongProperty("account_id");
        notification.addLongProperty("type_id");
        notification.addStringProperty("type");
        notification.addStringProperty("title");
        notification.addStringProperty("message");
        notification.addIntProperty("is_read");
        notification.addIntProperty("is_remove");
        notification.addStringProperty("created_at");
        notification.addStringProperty("updated_at");

        Entity alert_notified = schema.addEntity("AlertNotified");
        alert_notified.addLongProperty("id").primaryKey();

        new DaoGenerator().generateAll(schema, args[0]);
    }
}