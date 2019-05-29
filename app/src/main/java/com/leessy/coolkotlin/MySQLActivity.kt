package com.leessy.coolkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding2.view.RxView
import com.mysql.jdbc.PreparedStatement
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_sql.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class MySQLActivity : AppCompatActivity() {
    lateinit var con: Connection
    val TAG = "MySQLActivity"
    val paraCount = 5
    var paras = arrayOfNulls<String>(paraCount)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sql)

        RxView.clicks(connectBt)
            .observeOn(Schedulers.io())
            .subscribe { connetSQL() }

        RxView.clicks(getDataBt)
            .observeOn(Schedulers.io())
            .subscribe { getData() }
    }

    //获取数据
    private fun getData() {
        var q = "select * from log_records"
        var pst: java.sql.PreparedStatement = con.prepareStatement(q)//准备执行语句
        var retsult = pst.executeQuery()//执行语句，得到结果集
        while (retsult.next()) {
            for (i in 1..paraCount) {
                paras[i] = retsult.getString(i )
            }
            System.out.println(Arrays.toString(paras))

        }//显示数据
        retsult.close()
        con.close()//关闭连接
        pst.close()
    }

    //连接数据库
    private fun connetSQL() {
        try {
            //                Class.forName("net.sourceforge.jtds.jdbc.Driver");//sql server使用
            //                con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":1433/" + db + ";charset=utf8", user, pwd);
            Class.forName("com.mysql.jdbc.Driver")
            //引用代码此处需要修改，address为数据IP，Port为端口号，DBName为数据名称，UserName为数据库登录账户，Password为数据库登录密码
            val url = "jdbc:mysql://39.108.67.218:3306/log_server?useUnicode=true&amp;characterEncoding=utf-8"
            con = DriverManager.getConnection(url, "root", "123456")
            Log.d(TAG, "run: 连接成功==$con")
            Log.d(TAG, "run erro : " + con.getWarnings())
            //                con = DriverManager.getConnection("jdbc:mysql://address:Port/DBName", UserName, Password);
        } catch (e: SQLException) {
            // TODO Auto-generated catch block
            Log.d(TAG, "run: 连接1==$e")
            e.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            Log.d(TAG, "run: 连接2==$e")
            e.printStackTrace()
        }
    }
}
