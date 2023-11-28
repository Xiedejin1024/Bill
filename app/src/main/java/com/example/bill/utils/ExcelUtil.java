package com.example.bill.utils;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import com.example.bill.BillApplication;
import com.example.bill.db.Record;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Administrator on 2023/11/23. 完成对excel表格的读取、写入及保存等操作
 */
public class ExcelUtil {
    private static String excelDate = "日常账单.xls";

    private static String[] DateArray = new String[]{"收入/支出", "金额", "用途", "时间", "备注"};

    public static void createDateExcel(List<Record> recordList, String time) {
        exportExcel(excelDate, "日常账单收支表", DateArray, recordList, time);
    }

    /*
     * 创建新表并填入表头 filePath：文件路径 sheetName：工作表名 title：表头数组
     */
    public static void exportExcel(String fileName, String sheetName, String[] title, List<Record> dateList, String time) {
        WritableSheet ws = null;
        File file = new File(exportExcelDir() + File.separator + (time + fileName));
        try {
            if (!file.exists()) {
                WritableWorkbook wwb = Workbook.createWorkbook(file);
                ws = wwb.createSheet(sheetName, 0); // 创建工作表1
                // 在指定单元格插入表头数据
                for (int i = 0; i < title.length; i++) {
                    Label label = new Label(i, 0, title[i]); // 前面为列，后面为行
                    ws.addCell(label);
                }
                for (Record record : dateList) {
                    String[] content = new String[]{
                            record.isPay() == 0 ? "支出" : "收入",
                            record.getMoney(),
                            record.getPurpose(),
                            record.getTime(),
                            record.getDesc(),
                    };
                    // 当前总行数
                    int row = ws.getRows();
                    for (int i = 0; i < content.length; i++) {
                        Label label = new Label(i, row, content[i]); // 前面为列，后面为行
                        ws.addCell(label);
                    }
                }
                // 写入内存文件中
                wwb.write();
                wwb.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Excel", "e=" + e);
        }
    }

    //获取Excel文件所在文件夹
    private static String exportExcelDir() {
        // SD卡指定文件夹
        File dir = new File(BillApplication.context.getDataDir(), "ExportData");
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e("Excel", "保存路径不可读写");
        }
        if (!dir.exists()) {
            dir.mkdirs();  //不存在则创建
        }
        return dir.toString();
    }
}
