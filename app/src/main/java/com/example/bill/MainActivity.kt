package com.example.bill

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bill.databinding.ActivityMainBinding
import com.example.bill.fragment.FixFragmentNavigator
import com.example.bill.utils.showToast
import com.google.android.material.navigation.NavigationBarView
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.OnBarListener
import com.gyf.immersionbar.OnKeyboardListener
import com.permissionx.guolindev.PermissionX
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() , NavigationBarView.OnItemSelectedListener{
    private lateinit var binding: ActivityMainBinding

    private var lastFragment = 0
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //设置沉浸式状态栏
        ImmersionBar.with(this)
            .statusBarColor(R.color.orange_77) //状态栏颜色，不写默认透明色
            .fitsSystemWindows(true) //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
            .init() //必须调用方可应用以上所配置的参数


        //申请权限

//        val requestList = ArrayList<String>()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestList.add(Manifest.permission.READ_MEDIA_IMAGES)
//            requestList.add(Manifest.permission.READ_MEDIA_AUDIO)
//            requestList.add(Manifest.permission.READ_MEDIA_VIDEO)
//        }
//        if (requestList.isNotEmpty()) {
//            PermissionX.init(this)
//                .permissions(requestList)
//                .onExplainRequestReason { scope, deniedList ->
//                    val message = "PermissionX需要您同意以下权限才能正常使用"
//                    scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
//                }
//                .request { allGranted, grantedList, deniedList ->
//                    if (allGranted) {
//                        "所有申请的权限都已通过".showToast()
//                    } else {
//                        "您拒绝了如下权限：$deniedList".showToast()
//                    }
//                }
//        }

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    //初始化数据库
                } else {
                    AlertDialog.Builder(this).apply {
                        setTitle("尊敬的用户")
                        setMessage("拒绝读写权限后无法存储数据，请授权！")
                        setPositiveButton("授权") { _, _ ->
                            //申请权限小米手机
                            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                            intent.putExtra("extra_pkgname", packageName)
                            val componentName = ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
                            intent.component = componentName
                            context.startActivity(intent)

                        }
                        setNegativeButton("退出应用"){_, _ ->
                            exitProcess(0)
                        }
                    }.create().show()

                }
            }
        requestPermissionLauncher.launch(READ_MEDIA_IMAGES)


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragmentNavigator = FixFragmentNavigator(this, supportFragmentManager, fragment.id)
        //添加自定义的FixFragmentNavigator
        navController.navigatorProvider.addNavigator(fragmentNavigator)

        //通过代码将导航资源文件设置进去
        navController.setGraph(R.navigation.fix_nav_graph)
        binding.bottomNavView.setupWithNavController(navController)

        //BottomNavigationView自带图标切换颜色风格，如何去掉呢
        binding.bottomNavView.itemIconTintList=null
        binding.bottomNavView.setOnItemSelectedListener(this)

    }

    private fun setTabSelection(index: Int) {
        when (index) {
            0 -> {
                if (lastFragment == 1) {//上一个界面是在记录界面，要返回首页
                    navController.navigate(R.id.action_historyFragment_to_homeFragment)
                } else {//上一个界面是在我的界面，要返回首页
                    navController.navigate(R.id.action_mineFragment_to_homeFragment)
                }
                lastFragment = 0
            }

            1 -> {
                if (lastFragment == 0) {//上一个界面是在首页界面，要跳转记录
                    navController.navigate(R.id.action_homeFragment_to_historyFragment)
                } else {
                    navController.navigate(R.id.action_mineFragment_to_historyFragment)
                }
                lastFragment = 1
            }

            2 -> {
                if (lastFragment == 0) {
                    navController.navigate(R.id.action_homeFragment_to_mineFragment)
                } else {
                    navController.navigate(R.id.action_historyFragment_to_mineFragment)
                }
                lastFragment = 2
            }

            else -> {
                if (lastFragment == 1) {
                    navController.navigate(R.id.action_historyFragment_to_homeFragment)
                } else {
                    navController.navigate(R.id.action_mineFragment_to_homeFragment)
                }
                lastFragment = 0
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeFragment -> {
                setTabSelection(0)
            }
            R.id.historyFragment -> {
                setTabSelection(1)
            }
            R.id.mineFragment -> {
                setTabSelection(2)
            }
        }
        return true
    }

    private var exitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                "再按一次退出程序".showToast()
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}