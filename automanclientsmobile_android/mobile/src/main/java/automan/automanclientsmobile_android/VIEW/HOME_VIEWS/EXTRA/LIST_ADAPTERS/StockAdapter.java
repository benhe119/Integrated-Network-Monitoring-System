package automan.automanclientsmobile_android.VIEW.HOME_VIEWS.EXTRA.LIST_ADAPTERS;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations;
import automan.automanclientsmobile_android.MODEL.Stock;

import static automan.automanclientsmobile_android.MODEL.ENUMERATIONS.AutomanEnumerations.stocks;

/**
 * Created by mr.amo-addai on 3/26/18.
 */

public class StockAdapter extends ArrayAdapter<Stock> {

    public StockAdapter(AutomanEnumerations sth, Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        ListAdapters.setupStuff(sth, context, "", new ArrayList<Stock>());
    }

    public StockAdapter(AutomanEnumerations sth, Context context, int resource, List<Stock> items) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, "", items);
    }

    public StockAdapter(AutomanEnumerations sth, Context context, int resource, List<Stock> items, String option) {
        super(context, resource, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    public StockAdapter(AutomanEnumerations sth, Context context, int resource, int textviewId, List<Stock> items, String option) {
        super(context, resource, textviewId, items);
        ListAdapters.setupStuff(sth, context, option, items);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        return ListAdapters.genericGetView(stocks, v, parent, false, position);
    }
}
