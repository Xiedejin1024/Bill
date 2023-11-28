package com.example.bill.fragment


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bill.BillApplication
import com.example.bill.adapter.RecordAdapter
import com.example.bill.databinding.FragmentHistoryBinding
import com.example.bill.db.RecordDao
import com.example.bill.db.RecordDatabase
import com.example.bill.utils.ExcelUtil.createDateExcel
import com.example.bill.utils.LogUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.concurrent.thread


class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecordAdapter
    private lateinit var recordDao:RecordDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LogUtil.i("it520com","HistoryFragment_onCreateView")
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        EventBus.getDefault().register(this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordDao = RecordDatabase.getDatabase(BillApplication.context).recordDao()
        loadData()

        binding.ivExport.setOnClickListener {
            //暂时只走全部导出**
            thread {
                var listdatas= recordDao.loadAllRecord()
                //获取时间
                var sdr = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val exportTime = sdr.format(Date())
                createDateExcel(listdatas,exportTime)
                requireActivity().runOnUiThread {
                    val dialog = AlertDialog.Builder(requireActivity())
                    dialog.setTitle( "数据导出成功")
                    dialog.setMessage("是否打开文件！")
                    dialog.setPositiveButton("是") { _, _ ->
                        //进行打开文件的逻辑，要先检查有没有安装打开的文件。
                    }
                    dialog.setNegativeButton("否", null)
                    dialog.show()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(string: String) {
        if (string == "refreshRecordList") {
            loadData()
        }
    }

    private fun loadData() {
        thread {
            var listdatas= recordDao.loadAllRecord()
            requireActivity().runOnUiThread {
                val layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.layoutManager = layoutManager
                adapter = RecordAdapter(listdatas)
                binding.recyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}