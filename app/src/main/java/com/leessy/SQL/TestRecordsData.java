package com.leessy.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Created by 刘承. on 2019/6/25
 * business@onfacemind.com
 */
public class TestRecordsData {


    public static int insert(Connection connection, dev_test_records_Table table) {
        PreparedStatement prepar = null;
        try {
            prepar = connection.prepareStatement("insert into dev_test_records" +
                            " (system_id,system_version,dev_type,dev_configtype,dev_sn,test_sn,mobile_mac,blu_mac,eth_mac,wifi_mac,dev_imei," +
                            "dev_imsi,serial,cpuinfo,test_result,test_jsondata,time)" +
                            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);// 参数RETURN_GENERATED_KEYS ---> 返回自增key== pid
            prepar.setString(1, table.system_id);
            prepar.setString(2, table.system_version);
            prepar.setString(3, table.dev_type);
            prepar.setString(4, table.dev_configtype);
            prepar.setString(5, table.dev_sn);
            prepar.setString(6, table.test_sn);
            prepar.setString(7, table.mobile_mac);
            prepar.setString(8, table.blu_mac);
            prepar.setString(9, table.eth_mac);
            prepar.setString(10, table.wifi_mac);
            prepar.setString(11, table.dev_imei);
            prepar.setString(12, table.dev_imsi);
            prepar.setString(13, table.serial);
            prepar.setString(14, table.cpuinfo);
            prepar.setBoolean(15, table.test_result);
            prepar.setString(16, table.test_jsondata);
            prepar.setString(17, table.time);
            int i = prepar.executeUpdate();
            if (i == 1) {//插入成功
//                ResultSet rs = null; //获取自增长pid结果
                ResultSet rs = prepar.getGeneratedKeys();
                if (rs.next()) {
                    int autoInckey = rs.getInt(1);//取得ID
                    return autoInckey;
                } else {// throw an exception from here
                    return 0;
                }
            }
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepar != null) prepar.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    //设备测试记录表
    public static class dev_test_records_Table {
        public long id;//	bigint
        public String system_id;
        public String system_version;
        public String dev_type;
        public String dev_configtype;
        public String dev_sn;
        public String test_sn;
        public String mobile_mac;
        public String blu_mac;
        public String eth_mac;
        public String wifi_mac;
        public String dev_imei;
        public String dev_imsi;
        public String serial;
        public String cpuinfo;
        public boolean test_result;
        public String test_jsondata;
        public String time;

        @Override
        public String toString() {
            return "dev_test_records_Table{" +
                    "id=" + id +
                    ", system_id='" + system_id + '\'' +
                    ", system_version='" + system_version + '\'' +
                    ", dev_type='" + dev_type + '\'' +
                    ", dev_configtype='" + dev_configtype + '\'' +
                    ", dev_sn='" + dev_sn + '\'' +
                    ", test_sn='" + test_sn + '\'' +
                    ", mobile_mac='" + mobile_mac + '\'' +
                    ", blu_mac='" + blu_mac + '\'' +
                    ", eth_mac='" + eth_mac + '\'' +
                    ", wifi_mac='" + wifi_mac + '\'' +
                    ", dev_imei='" + dev_imei + '\'' +
                    ", dev_imsi='" + dev_imsi + '\'' +
                    ", serial='" + serial + '\'' +
                    ", cpuinfo='" + cpuinfo + '\'' +
                    ", test_result=" + test_result +
                    ", test_jsondata='" + test_jsondata + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }
}
