package com.leessy.coolkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_list_demo.*

class ListDemoActivity : AppCompatActivity() {

    var list = arrayListOf<Data>()
    var listH = arrayListOf<List<Data>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_demo)
        initView()
    }

    lateinit var mAdapter: QuickAdapter
    private fun initView() {
        list = arrayListOf()
        for (i in 1..50) {
            list.add(Data("$i").apply {
                o = i - 1
            })
            val l = arrayListOf<Data>()
            for (i in 65..92) {
                l.add(Data(i.toChar().toString()))
            }
            listH.add(l)
        }




        mAdapter = mrecyclerview.run {
            layoutManager = GridLayoutManager(this.context, 1)
            adapter = QuickAdapter(0)
            adapter as QuickAdapter
        }.apply {
            setNewData(list)
//            onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
//                Toast.makeText(this@ListDemoActivity, "点击：" + list.get(position).ss, Toast.LENGTH_SHORT).show()
//                list.get(position).o = 1
//                notifyItemChanged(position)
//            }
        }
    }


    inner class QuickAdapter(dataSize: Int) :
        BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item_layout) {

        override fun convert(helper: BaseViewHolder, item: Data) {
            helper.getView<RecyclerView>(R.id.itemRecyclerview).apply {
                layoutManager = GridLayoutManager(this.context, 1).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
            }.run {
                adapter = QuickAdapter2(0)
                adapter as QuickAdapter2
            }.apply {
                setNewData(listH.get(item.o))
            }
        }
    }

    inner class QuickAdapter2(dataSize: Int) :
        BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item_layout) {

        override fun convert(helper: BaseViewHolder, item: Data) {
            helper.getView<RecyclerView>(R.id.itemRecyclerview).apply {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (recyclerView.layoutManager != null) {
                            val layoutManager = layoutManager as LinearLayoutManager?
                            //获取可视的第一个view
                            val topView = layoutManager!!.getChildAt(0)
                            if (topView != null) {
                                //获取与该view的顶部的偏移量
                                item.lastOffset = topView.top
                                //得到该View的数组位置
                                item.lastPosition = layoutManager.getPosition(topView)
                            }
                        }
                    }
                })
                layoutManager?.scrollToPosition(item.lastPosition)
                visibility = View.GONE
                adapter?.notifyDataSetChanged()
            }
            var color = getResources().getColor(R.color.colorAccent)

            helper.setText(R.id.textView, item.ss)
            if (item.o > 0) {
                helper.setBackgroundColor(R.id.textView, color)
            } else {
                helper.setBackgroundColor(R.id.textView, getResources().getColor(R.color.design_default_color_primary))
            }

        }
    }

    inner class Data(var ss: String) {
        var o: Int = 0
        var quickAdapter: QuickAdapter2? = null

        var lastOffset = 0
        var lastPosition = 0

    }
}
