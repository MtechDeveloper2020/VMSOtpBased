package mtech.com.vmsotpbased;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by cd02232 on 21-01-2017.
 */

public class DBConnection {

    String ip = "", db = "", un = "", ps = "";

    public DBConnection(String ip, String db, String un, String ps) {
//        this.ip = "103.14.97.220";
//        this.db = "mtechinn_webmacs";
//        this.un = "MtechGPRS";
//        this.ps = "Mtech@GPRS$";
//        this.ip = ip;
//        this.db = db;
//        this.un = un;
//        this.ps = ps;
    }
    public DBConnection() {
        this.ip = "103.14.97.220";
        this.db = "mtechinn_webmacs";
        this.un = "MtechGPRS";
        this.ps = "Mtech@GPRS$";
//        this.ip = ip;
//        this.db = db;
//        this.un = un;
//        this.ps = ps;
    }

    //---------------------------------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + "/" + db + ";user=" + un + ";password=" + ps + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch(SQLException se){
            Log.e("error here 1 : ", se.getMessage());
        } catch(ClassNotFoundException e){
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
//------------------------------------------------------------------------------------------------
}
