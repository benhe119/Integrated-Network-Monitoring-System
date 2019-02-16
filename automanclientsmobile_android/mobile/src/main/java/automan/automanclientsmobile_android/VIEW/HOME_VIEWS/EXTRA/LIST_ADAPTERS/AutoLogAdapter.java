package automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.autologs;

/**
 * Created by mr.amo-addai on 1/5/18.
 */

public class AutoLogAdapter extends ArrayAdapter<AutoLog> {

    public AutoLogAdapter(AutomanEnumerations sth, Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        ListAdapters.setupStuff(sth, context, "", new ArrayList<AutoLog>());
    }

    public AutoLogAdapter(AutomanEnumerations sth, Context context, int resource, List<AutoLog> items) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, "", items);
    }

    public AutoLogAdapter(AutomanEnumerations sth, Context context, int resource, List<AutoLog> items, String option) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    public AutoLogAdapter(AutomanEnumerations sth, Context context, int resource, int textviewId, List<AutoLog> items, String option) {
        super(context, resource, textviewId, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        return ListAdapters.genericGetView(autologs, v, parent, false, position);
    }
}
