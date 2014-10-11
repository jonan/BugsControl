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

package jp.bugscontrol.general;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.ui.AdapterProduct;

public abstract class Server {
    protected final List<Product> products = new ArrayList<Product>();
    protected final String name;
    protected final String url;
    protected String user;
    protected String password;

    private AdapterProduct adapter;
    private Activity activity;

    private jp.bugscontrol.db.Server databaseServer = null;

    public Server(final String name, final String url) {
        this.name = name;
        this.url = url;
        user = "";
        password = "";
    }

    public Server(final jp.bugscontrol.db.Server server) {
        databaseServer = server;
        name = server.name;
        url = server.url;
        user = server.user;
        password = server.password;
    }

    public void setUser(final String user, final String password) {
        this.user = user;
        this.password = password;
    }

    public void setAdapterProduct(final AdapterProduct adapter, final Activity activity) {
        this.adapter = adapter;
        this.activity = activity;

        activity.setProgressBarIndeterminateVisibility(true);
        loadProducts();
    }

    public Product getProductFromId(final int productId) {
        for (Product p : products) {
            if (p.getId() == productId) {
                return p;
            }
        }

        return null;
    }

    public Bug getBugFromId(final int bugId) {
        for (Product p : products) {
            for (Bug b : p.bugs) {
                if (b.getId() == bugId) {
                    return b;
                }
            }
        }

        return null;
    }

    public void save() {
        if (databaseServer == null) {
            databaseServer = new jp.bugscontrol.db.Server();
        }
        databaseServer.name = name;
        databaseServer.url = url;
        databaseServer.user = user;
        databaseServer.password = password;
        databaseServer.save();
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasUser() {
        return !TextUtils.isEmpty(user);
    }

    protected abstract void loadProducts();

    protected void productsListUpdated() {
        adapter.notifyDataSetChanged();
        activity.setProgressBarIndeterminateVisibility(false);
    }
}
