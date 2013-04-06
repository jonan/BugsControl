package jp.bugscontrol;

import java.util.List;

import jp.bugscontrol.server.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterProduct extends ArrayAdapter<Product> {
    LayoutInflater inflater;

    public AdapterProduct(Context context, List<Product> values) {
        super(context, R.layout.adapter_product, values);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_product, parent, false);

        Product item = getItem(position);
        ((TextView) view.findViewById(R.id.name)).setText(item.getName());

        return view;
    }
}
