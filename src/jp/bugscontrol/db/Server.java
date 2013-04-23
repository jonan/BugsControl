package jp.bugscontrol.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Servers")
public class Server extends Model {
    @Column(name = "Name")
    public String name;
    @Column(name = "Url")
    public String url;
    @Column(name = "User")
    public String user;
    @Column(name = "Password")
    public String password;
}
