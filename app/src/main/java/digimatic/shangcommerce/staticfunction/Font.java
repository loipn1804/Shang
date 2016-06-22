package digimatic.shangcommerce.staticfunction;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by USER on 3/3/2016.
 */
public class Font {

    public Typeface light;
    public Typeface bold;
    public Typeface boldIt;
    public Typeface it;

    public Font(Context context) {
        light = Typeface.createFromAsset(context.getAssets(), "fonts/GOTHIC.TTF");
        bold = Typeface.createFromAsset(context.getAssets(), "fonts/GOTHICB.TTF");
        boldIt = Typeface.createFromAsset(context.getAssets(), "fonts/GOTHICBI.TTF");
        it = Typeface.createFromAsset(context.getAssets(), "fonts/GOTHICI.TTF");
    }

    public void overrideFontsLight(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLight(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(light);
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsBold(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBold(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(bold);
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsBoldIt(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBoldIt(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(boldIt);
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsIt(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsIt(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(it);
            }
        } catch (Exception e) {
        }
    }
}
