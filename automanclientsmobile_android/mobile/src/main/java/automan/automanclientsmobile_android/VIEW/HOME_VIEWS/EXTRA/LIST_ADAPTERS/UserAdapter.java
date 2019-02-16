package automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.MODEL.AUTOSECURITY.AUTOAUDITING.AutoLog;
import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.USER_DATA.User;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.users;

/**
 * Created by mr.amo-addai on 1/5/18.
 */

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(AutomanEnumerations sth, Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        ListAdapters.setupStuff(sth, context, "", new ArrayList<User>());
    }

    public UserAdapter(AutomanEnumerations sth, Context context, int resource, List<User> items) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, "", items);
    }

    public UserAdapter(AutomanEnumerations sth, Context context, int resource, List<User> items, String option) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    public UserAdapter(AutomanEnumerations sth, Context context, int resource, int textviewId, List<User> items, String option) {
        super(context, resource, textviewId, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        return ListAdapters.genericGetView(users, v, parent, true, position);
    }
}
