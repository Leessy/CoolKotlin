package com.leessy.coolkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.SQL.SqlLogData
import com.leessy.adapter.SqlLogsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_sql.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.concurrent.TimeUnit
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.leessy.SQL.RecycleViewDivider


class MySQLActivity : AppCompatActivity() {
    var con: Connection? = null
    val TAG = "MySQLActivity"
    val paraCount = 5
    var paras = arrayOfNulls<String>(paraCount)
    val sqlDatas = ArrayList<SqlLogData>()


    var mAdapter: SqlLogsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sql)

        RxView.clicks(connectBt)
            .observeOn(Schedulers.io())
            .subscribe { connetSQL() }

        RxView.clicks(getDataBt)
            .filter {
                (con != null && !con!!.isClosed).let {
                    if (!it) {
                        showToast("数据库未连接")
                    }
                    it
                }
            }
            .throttleFirst(2, TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .map { getData() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { mAdapter?.setNewData(sqlDatas) }

        initListView()
    }


    //连接数据库
    private fun connetSQL() {
        if (con != null) return
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://39.108.67.218:3306/log_server?useUnicode=true&amp;characterEncoding=utf-8"
            con = DriverManager.getConnection(url, "root", "123456")
            AndroidSchedulers.mainThread().scheduleDirect {
                if (con?.isClosed!!) {
                    showToast("连接失败")
                } else {
                    showToast("连接成功")
                }
            }
            Log.d(TAG, "run: 连接成功   isClosed==${con?.isClosed}")
        } catch (e: SQLException) {
            Log.d(TAG, "run: 连接 e 1==$e")
            e.printStackTrace()
        } catch (e: Exception) {
            Log.d(TAG, "run: 连接 e 2==$e")
            e.printStackTrace()
        }
    }


    //获取数据
    /**
    '%a'     //以a结尾的数据
    'a%'     //以a开头的数据
    '%a%'    //含有a的数据
    '_a_'    //三位且中间字母是a的
    '_a'     //两位且结尾字母是a的
    'a_'     //两位且开头字母是a的
     */
    private fun getData() {
//        var q = "select * from log_records  ORDER BY createTime desc"
        var q = "select * from log_records WHERE sn like 'F901%' ORDER BY createTime desc"
        var pst: PreparedStatement = con!!.prepareStatement(q)//准备执行语句
        var retsult = pst.executeQuery()//执行语句，得到结果集

        sqlDatas.clear()
        while (retsult.next()) {
            sqlDatas.add(SqlLogData(retsult.getString(4)).apply {
                this.id = retsult.getLong(1)
                this.filename = retsult.getString(2)
                this.Url = retsult.getString(3)
                this.createTime = retsult.getString(5)
            })
//            for (i in 0..paraCount - 1) {
//                paras[i] = retsult.getString(i + 1)
//            }
//            System.out.println(Arrays.toString(paras))
        }
        retsult.close()
        pst.close()
    }

    //recycleview
    private fun initListView() {
        mAdapter = mRecyclerview.run {
            //添加默认分割线：高度为2px，颜色为灰色
            addItemDecoration(RecycleViewDivider(this@MySQLActivity, LinearLayoutManager.HORIZONTAL))
            layoutManager = GridLayoutManager(this.context, 1)
            adapter = SqlLogsAdapter(0)
            adapter as SqlLogsAdapter
        }.apply {
            this.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                showToast("点击 ${sqlDatas[position].createTime}")
            }
        }
    }


    fun showToast(s: String) {
        Toast.makeText(this@MySQLActivity, s, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        con?.let {
            it.close()
        }
    }
}
