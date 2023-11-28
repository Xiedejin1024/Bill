package com.example.bill.fragment

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bill.BillApplication
import com.example.bill.databinding.FragmentHomeBinding
import com.example.bill.db.Record
import com.example.bill.db.RecordDatabase
import com.example.bill.utils.LogUtil
import com.example.bill.utils.setOnClickListener
import com.example.bill.utils.showToast
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.concurrent.thread


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    //标志位，分辨收入还是支出
    private var isPay = 0

    //标志位，分辨自定义输入还是spinner选择
    private var isSpinner = true
    private var timeText = ""
    private val items = listOf("餐饮", "衣服", "会员", "报班", "洗漱品", "化妆品","自定义")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textSelect(binding.tvPay)
        val recordDao = RecordDatabase.getDatabase(BillApplication.context).recordDao()

        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        binding.spinner.adapter = adapter

        //获取时间
        var sdr = SimpleDateFormat("yyyy-MM-dd HH:mm")
        binding.tvTime.text = sdr.format(Date())

        initListener()

        //****此处只是暂时效果方案，要适配可以完成自定义adapter采用Edit和Imageview进行popuwindow就能实现了
        //如果spinner是自定义，那么需要指定收入
        binding.spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position:Int, id: Long) {
                val s = items[position]
                if ("自定义"==items[position]){
                    isSpinner=false
                    binding.etFangxiang.visibility=View.VISIBLE
                    binding.etFangxiang.requestFocus()
                }else{
                    isSpinner=true
                    binding.etFangxiang.visibility=View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        binding.btnSave.setOnClickListener {
            var spinnerText=""
            val moneyText = binding.etMoney.text.toString()
            //获取参数并判断
            if (binding.etMoney.text.toString() == "") {
                "请输入金额数".showToast()
                return@setOnClickListener
            }
            //获取spinner的文本并判断
            spinnerText = if (isSpinner) {
                binding.spinner.selectedItem.toString()
            } else{
                binding.etFangxiang.text.toString()
            }
            val timeText = binding.tvTime.text.toString()
            val descText = binding.etDesc.text.toString()

            thread {
                val record = Record(
                    isPay,
                    moneyText,
                    spinnerText,
                    timeText,
                    descText
                )
                recordDao.insertRecord(record)
                EventBus.getDefault().post("refreshRecordList")
            }
        }

    }

    private fun initListener() {
        setOnClickListener(
            binding.tvPay, binding.tvIncome, binding.tvTime,
            binding.llEtDesc, binding.btnSave
        ) {
            when (this) {
                binding.tvPay -> {
                    textUnSelect(binding.tvIncome)
                    textSelect(binding.tvPay)
                    isPay = 0
                }

                binding.tvIncome -> {
                    textUnSelect(binding.tvPay)
                    textSelect(binding.tvIncome)
                    isPay = 1
                }

                binding.tvTime -> {
                    showDatePickerDialog()
                }

                binding.llEtDesc -> {
                    binding.etDesc.requestFocus()
                    binding.etDesc.setSelection(binding.etDesc.text.length)
                    //输入框
                    var inputManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.showSoftInput(binding.etDesc, 0)
                }
            }
        }
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance();
        val year = calendar.get(Calendar.YEAR);
        val month = calendar.get(Calendar.MONTH);
        val day = calendar.get(Calendar.DAY_OF_MONTH);

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                timeText = "${year}-${(monthOfYear + 1)}-${dayOfMonth} "
                showTimePickerDialog()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY);
        val minute = calendar.get(Calendar.MINUTE);

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
                binding.tvTime.text = "$timeText${hourOfDay}:${minute}"
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }


    private fun textSelect(view: TextView) {
        view.isSelected = true
        view.textSize = 24.0f
    }

    private fun textUnSelect(view: TextView) {
        view.isSelected = false
        view.textSize = 18.0f
    }

}