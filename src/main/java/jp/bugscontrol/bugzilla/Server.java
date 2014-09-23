/*
 *  BugsControl
 *  Copyright (C) 2013  Jon Ander Peñalba
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jp.bugscontrol.bugzilla;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.util.Util.Listener;

public class Server extends jp.bugscontrol.server.Server {
    public Server(final String name, final String url) {
        super(name, url);
    }

    public Server(final jp.bugscontrol.db.Server server) {
        super(server);
    }

    @Override
    protected void loadProducts() {
        // Get all the products' ids and pass it to loadProductsFromIds()
        final BugzillaTask task = new BugzillaTask(this, "Product.get_accessible_products", new Listener() {
            @Override
            public void callback(final String s) {
                try {
                    final JSONObject object = new JSONObject(s);
                    loadProductsFromIds(object.getJSONObject("result").getString("ids"));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute();
    }

    private void loadProductsFromIds(final String productIds) {
        final Server server = this;
        final BugzillaTask task = new BugzillaTask(this, "Product.get", "'ids':" + productIds + ",'include_fields':['id', 'name', 'description']",  new Listener() {
            @Override
            public void callback(final String s) {
                try {
                    final JSONObject object = new JSONObject(s);
                    final JSONArray productsJson = object.getJSONObject("result").getJSONArray("products");
                    products.clear();
                    for (int i = 0; i < productsJson.length(); ++i) {
                        final JSONObject p = productsJson.getJSONObject(i);
                        products.add(new Product(server, p));
                    }
                    productsListUpdated();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute();
    }
}
