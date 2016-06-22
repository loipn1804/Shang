package digimatic.shangcommerce.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.connection.callback.UICallback;
import digimatic.shangcommerce.connection.threadconnection.execute.CustomerExecute;
import digimatic.shangcommerce.daocontroller.CustomerController;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Customer;

/**
 * Created by user on 3/7/2016.
 */
public class AccountEditActivity extends BaseActivity implements View.OnClickListener {

    private int CAMERA_REQUEST = 127;
    private int PICK_IMAGE_REQUEST = 128;

    private String FOLDER = "Shang";
    private static String FILENAME = "";
    private String FILETYPE = ".jpg";

    private int REQUEST_UPDATE = 111;

    private RelativeLayout rltBack;
    private TextView txtTitle;
    private TextView txtSave;

    private LinearLayout lnlSettingAddress;
    private TextView txtSettingAddress;

    private TextView txtChangeAvatar;
    private TextView txtChangePassword;

    private ImageView imvAvatar;
    private TextView txtUsername;
    private TextView txtEmail;
    private EditText edtFirstName;
    private EditText edtLastName;
    private TextView edtBirthday;
    private EditText edtPhoneNumber;
    private EditText edtDefaultAddress;

    private RelativeLayout rltBirthday;
    private ImageView imvBirthday;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnLoading(R.drawable.avatar_default)
                .cacheOnDisk(true).build();

        initView();
        initData();
    }

    private void initView() {
        rltBack = (RelativeLayout) findViewById(R.id.rltBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSave = (TextView) findViewById(R.id.txtSave);

        lnlSettingAddress = (LinearLayout) findViewById(R.id.lnlSettingAddress);
        txtSettingAddress = (TextView) findViewById(R.id.txtSettingAddress);

        txtChangeAvatar = (TextView) findViewById(R.id.txtChangeAvatar);
        txtChangePassword = (TextView) findViewById(R.id.txtChangePassword);

        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtBirthday = (TextView) findViewById(R.id.edtBirthday);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtDefaultAddress = (EditText) findViewById(R.id.edtDefaultAddress);

        rltBirthday = (RelativeLayout) findViewById(R.id.rltBirthday);
        imvBirthday = (ImageView) findViewById(R.id.imvBirthday);

        rltBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        lnlSettingAddress.setOnClickListener(this);
        txtChangeAvatar.setOnClickListener(this);
        txtChangePassword.setOnClickListener(this);
        rltBirthday.setOnClickListener(this);
        imvBirthday.setOnClickListener(this);

        Font font = new Font(this);
        font.overrideFontsLight(findViewById(R.id.root));
        font.overrideFontsBold(txtTitle);
        font.overrideFontsBold(findViewById(R.id.txtUsername));
        font.overrideFontsBold(txtChangeAvatar);
        font.overrideFontsBold(txtChangePassword);
        font.overrideFontsBold(txtSettingAddress);

        edtPhoneNumber.setVisibility(View.GONE);
        edtDefaultAddress.setVisibility(View.GONE);
    }

    private void initData() {

    }

    private void setData() {
        if (CustomerController.isLogin(this)) {
            Customer customer = CustomerController.getCurrentCustomer(this);
            String full = customer.getFirstname();
            String middle = customer.getMiddlename();
            if (middle.length() > 0) {
                full += " " + middle;
            }
            String last = customer.getLastname();
            if (last.length() > 0) {
                full += " " + last;
            }
            txtUsername.setText(full);
            txtEmail.setText(customer.getEmail());
            edtFirstName.setText(customer.getFirstname());
            edtLastName.setText(last);
            if (customer.getIs_login_fb()) {
                imageLoader.displayImage("https://graph.facebook.com/" + customer.getFacebook_id() + "/picture?height=200&width=200", imvAvatar, options);
                txtChangePassword.setVisibility(View.INVISIBLE);
                txtChangeAvatar.setVisibility(View.INVISIBLE);
            } else {
                imageLoader.displayImage(customer.getPicture_url(), imvAvatar, options);
                txtChangePassword.setVisibility(View.VISIBLE);
                txtChangeAvatar.setVisibility(View.VISIBLE);
            }
//            try {
//                JSONObject shipping_address = new JSONObject(customer.getShipping_address());
//                edtPhoneNumber.setText(shipping_address.getString("telephone"));
//                String addr = "";
//                JSONArray street = shipping_address.getJSONArray("street");
//                for (int i = 0; i < street.length(); i++) {
//                    addr += street.getString(i) + " ";
//                }
//                addr += shipping_address.getString("city") + " ";
//                addr += shipping_address.getString("postcode");
//                edtDefaultAddress.setText(addr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            if (customer.getDob().length() > 0) {
                SimpleDateFormat formatY = new SimpleDateFormat("yyyy-MM-dd");
                String dateInString = customer.getDob().substring(0, 10);
                Date date = null;
                try {
                    SimpleDateFormat formatD = new SimpleDateFormat("dd/MM/yyyy");
                    date = formatY.parse(dateInString);
                    edtBirthday.setText(formatD.format(date));
                } catch (java.text.ParseException e) {

                    e.printStackTrace();
                }
            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltBack:
                finish();
                break;
            case R.id.txtSave:
                updateProfile();
                break;
            case R.id.lnlSettingAddress:
                Intent intentAddress = new Intent(this, SettingAddressActivity.class);
                startActivityForResult(intentAddress, REQUEST_UPDATE);
                break;
            case R.id.txtChangeAvatar:
                showPopupPickImage();
                break;
            case R.id.txtChangePassword:
                Intent intentChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.imvBirthday:
                if (edtBirthday.getText().toString().length() == 0) {
                    SimpleDateFormat formatD = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();

                    showDatePicker(formatD.format(c.getTime()));
                } else {
                    showDatePicker(edtBirthday.getText().toString());
                }
                break;
            case R.id.rltBirthday:
                if (edtBirthday.getText().toString().length() == 0) {
                    SimpleDateFormat formatD = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();

                    showDatePicker(formatD.format(c.getTime()));
                } else {
                    showDatePicker(edtBirthday.getText().toString());
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void showDatePicker(String dateInString) {
        final SimpleDateFormat formatD = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatD.parse(dateInString);
        } catch (java.text.ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat ftDay = new SimpleDateFormat("dd");
        SimpleDateFormat ftMonth = new SimpleDateFormat("MM");
        SimpleDateFormat ftYear = new SimpleDateFormat("yyyy");

        DatePickerDialog dp = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Date date = null;
                        try {
                            date = formatD.parse(dateInString);
                        } catch (java.text.ParseException e) {

                            e.printStackTrace();
                        }

                        edtBirthday.setText(formatD.format(date));
                    }

                }, Integer.parseInt(ftYear.format(date)),
                Integer.parseInt(ftMonth.format(date)) - 1,
                Integer.parseInt(ftDay.format(date)));
        dp.setTitle("Calendar");
        dp.setMessage("Select Date of Birth");

        dp.show();
    }

    private void getCustomerDetail() {
        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        String firstname = object.getString("firstname");
                        if (firstname.equalsIgnoreCase("null")) firstname = "";
                        String lastname = object.getString("lastname");
                        if (lastname.equalsIgnoreCase("null")) lastname = "";
                        String dob = object.getString("dob");
                        if (dob.equalsIgnoreCase("null")) dob = "";
                        String shipping_address = object.getString("shipping_address");
                        String billing_address = object.getString("billing_address");

                        Customer customer = CustomerController.getCurrentCustomer(AccountEditActivity.this);
                        customer.setFirstname(firstname);
                        customer.setLastname(lastname);
                        customer.setDob(dob);
                        customer.setShipping_address(shipping_address);
                        customer.setBilling_address(billing_address);
                        CustomerController.insertOrUpdate(AccountEditActivity.this, customer);

                        setData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        CustomerExecute.getCustomerDetail(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id());
    }

    private void updateProfile() {
        String firstname = edtFirstName.getText().toString().trim();
        String lastname = edtLastName.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        if (firstname.length() == 0) {
            showToastError(getString(R.string.blank_firstname));
        } else if (lastname.length() == 0) {
            showToastError(getString(R.string.blank_lastname));
        } else {
            String body = "<?xml version=\"1.0\"?>";
            body += "<magento_api>";
            body += "<firstname>" + firstname + "</firstname>";
            body += "<lastname>" + lastname + "</lastname>";
            if (birthday.length() > 0) {
                String[] temp = birthday.split("/");
                String birthday_update = "";
                if (temp.length == 3) {
                    birthday_update = temp[1] + "-" + temp[0] + "-" + temp[2];
                }
                body += "<dob>" + birthday_update + "</dob>";
            }
            body += "</magento_api>";
            UICallback callback = new UICallback() {
                @Override
                public void onResult(boolean success, String data) {
                    if (success) {
                        showToastOk("Update profile successfully!");
                        getCustomerDetail();
                    } else {
                        showToastError("Update profile fail!");
                        hideProgressDialog();
                    }
                }
            };
            CustomerExecute.updateProfile(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id(), body);
            showProgressDialog(false);
        }
    }

    private void uploadAvatar(Bitmap bitmap) {
        String base = StaticFunction.encodeToBase64(bitmap);
        JSONObject object = new JSONObject();
        try {
            object.put("file_mime_type", "image/jpeg");
            object.put("file_content", base);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UICallback callback = new UICallback() {
            @Override
            public void onResult(boolean success, String data) {
                if (success) {
                    try {
                        JSONObject object = new JSONObject(data);
                        String picture_url = object.getString("picture_url");
                        if (picture_url.equalsIgnoreCase("null")) picture_url = "";

                        Customer customer = CustomerController.getCurrentCustomer(AccountEditActivity.this);
                        customer.setPicture_url(picture_url);
                        CustomerController.insertOrUpdate(AccountEditActivity.this, customer);

                        imageLoader.clearDiskCache();
                        imageLoader.displayImage(customer.getPicture_url(), imvAvatar, options);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastError(e.getMessage());
                    }
                } else {
                    showToastError(data);
                }
                hideProgressDialog();
            }
        };
        CustomerExecute.uploadAvatar(this, callback, CustomerController.getCurrentCustomer(this).getEntity_id(), object.toString());
        showProgressDialog(false);
    }

    ///// choose image
    public void showPopupPickImage() {
        // custom dialog
        final Dialog dialog = new Dialog(this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_pick_image);

        LinearLayout root = (LinearLayout) dialog.findViewById(R.id.root);
        LinearLayout lnlCamera = (LinearLayout) dialog.findViewById(R.id.lnlCamera);
        LinearLayout lnlGallery = (LinearLayout) dialog.findViewById(R.id.lnlGallery);

        lnlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.dismiss();
            }
        });

        lnlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                dialog.dismiss();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openCamera() {
        FILENAME = FOLDER + "_" + Settings.Secure.ANDROID_ID + "_" + System.currentTimeMillis() + FILETYPE;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createUriForCamera());
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {

        }
    }

    private Uri createUriForCamera() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, FILENAME);
        Uri uri = Uri.fromFile(file);

//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    private Uri getFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + FOLDER);
        File file = new File(myDir, FILENAME);
        Uri uri = Uri.fromFile(file);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            getCustomerDetail();
            showProgressDialog(false);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    beginCrop(uri);
                } catch (Exception e) {
                    showToastError(e.getMessage());
                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri uri = getFile();
            if (uri != null) {
                try {
                    beginCrop(uri);
                } catch (Exception e) {
                    showToastError(e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = Crop.getOutput(result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                if (bitmap.getHeight() > 200) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                }

                Matrix matrix = new Matrix();
                matrix.postRotate(getCameraPhotoOrientation(this, selectedImageUri, selectedImageUri.getPath()));
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

                uploadAvatar(bitmap);
            } catch (Exception e) {

            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
    ///// choose image
}
