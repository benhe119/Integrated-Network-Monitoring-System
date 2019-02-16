package automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoEvent;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autoevents;

/**
 * Created by mr.amo-addai on 1/5/18.
 */


public class AutoEventAdapter extends ArrayAdapter<AutoEvent> {


    public AutoEventAdapter(AutomanEnumerations sth, Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        ListAdapters.setupStuff(sth, context, "", new ArrayList<AutoEvent>());
    }

    public AutoEventAdapter(AutomanEnumerations sth, Context context, int resource, List<AutoEvent> items) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, "", items);
    }

    public AutoEventAdapter(AutomanEnumerations sth, Context context, int resource, List<AutoEvent> items, String option) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    public AutoEventAdapter(AutomanEnumerations sth, Context context, int resource, int textviewId, List<AutoEvent> items, String option) {
        super(context, resource, textviewId, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        return ListAdapters.genericGetView(autoevents, v, parent, false, position);
    }
}