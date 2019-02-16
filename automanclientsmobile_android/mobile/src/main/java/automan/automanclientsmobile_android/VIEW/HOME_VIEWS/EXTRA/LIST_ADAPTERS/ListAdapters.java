package automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.GENERAL.AutoObject;
import automan.automanclientsmobile_android.MODEL.Stock;
import automan.automanclientsmobile_android.MODEL.USER_DATA.User;
import automan.automanclientsmobile_android.R;
import automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS.AutoLogDetails;

import java.util.HashMap;
import java.util.List;

public class ListAdapters {
    ///////////////////////////////////////////////////////////////////////////////////////////
    private static TextView maintext, subtext;
    private static ImageView imgview;
    private static LinearLayout listitemcontext;

    private static HashMap<AutomanEnumerations, Context> contexts = new HashMap<>();
    private static HashMap<AutomanEnumerations, String> options = new HashMap<>();
    private static HashMap<AutomanEnumerations, List<AutoObject>> items = new HashMap<>();

    public static <T> void setupStuff(AutomanEnumerations sth, Context context, String option, List<T> items) {
        ListAdapters.contexts.put(sth, context);
        ListAdapters.options.put(sth, option);
        ListAdapters.items.put(sth, (List<AutoObject>) items);
    }

    public static View genericGetView(AutomanEnumerations sth, View v, ViewGroup parent, Boolean isPerson, int position) {
        String option = ListAdapters.options.get(sth);
        List<AutoObject> items = ListAdapters.items.get(sth);
        Context context = ListAdapters.contexts.get(sth);
        //
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = null;
        if (option.equalsIgnoreCase("object")) { // JUST WORK WITH SPINNER HERE
            item = inf.inflate(R.layout.spinnerlistitem, parent, false);
            maintext = (TextView) item.findViewById(R.id.name);
        } else {
//            if(isPerson) Log.e(sth.toPluralUpperCase(sth), "USER ADAPTER, SO USER LIST ITEM SET");
            item = inf.inflate(isPerson ? R.layout.homeemployeelistitem : R.layout.homelistitem, parent, false);
            maintext = (TextView) item.findViewById(R.id.name);
            subtext = (TextView) item.findViewById(R.id.details);
        }
        // NOW, YOU CAN WORK WITH item VARIABLE
        listitemcontext = (LinearLayout) item.findViewById(R.id.listitemcontext);
        listitemcontext.setLongClickable(true);

        // SET IMAGE FOR imgview
        // imgview = (ImageView) item.findViewById(R.id.imgview);

        try {
            final AutoObject obj = items.get(position);
            String primary = "", secondary = "";
            switch (sth) {
                case autologs:
                    primary = ((AutoLog) obj).name();
                    secondary = obj.details();
                    break;
                case autoevents:
                    primary = ((AutoEvent) obj).name();
                    secondary = obj.details();
                    break;
                case users:
                    primary = ((User) obj).fullName();
                    secondary = obj.details();
                    break;
                case stocks:
                    primary = ((Stock) obj).name();
                    secondary = obj.details();
                    break;
            }

            maintext.setText(primary); // BUT FOR subtext, ONLY SET IT IF option != "object" && subtext != null
            if (!option.equalsIgnoreCase("object") && subtext != null)
                subtext.setText(secondary);

            // NOW, YOU CAN EVEN SET THE ON-ITEM-CLICK LISTENER
            final AutomanEnumerations finalSth = sth;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = null;
                        Class c = Class.forName("automan.automanclientsmobile_android.VIEW.DETAILS_VIEWS." + finalSth.toSingularCamelCase(finalSth) + "Details");
                        Log.e(finalSth.toSingularUpperCase(finalSth) + " CLICKED -> ", "" + obj.id() + "; " + c.getName() + "; " + c.toString());
                        i = new Intent(AutomanApp.getApp().getCurrentActivity(), c);
                        i.putExtra("id", obj.id() + "");
                        AutomanApp.getApp().getCurrentActivity().startActivity(i);
                    } catch (ClassNotFoundException e) {
                        Log.e("NOTICE", "SOME ERROR HAS OCCURED ON A CLICK EVENT");
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

        return item;

    }
}
