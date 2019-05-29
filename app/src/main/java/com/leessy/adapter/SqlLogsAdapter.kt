package com.leessy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.leessy.SQL.SqlLogData
import com.leessy.coolkotlin.R

class SqlLogsAdapter(dataSize: Int) :
    BaseQuickAdapter<SqlLogData, BaseViewHolder>(R.layout.sqldata_item_layout) {

    override fun convert(helper: BaseViewHolder, item: SqlLogData) {
        helper.setText(R.id.SN, "SN: ${item.SN}")
        helper.setText(R.id.time, "Time: ${item.createTime}")
    }
}