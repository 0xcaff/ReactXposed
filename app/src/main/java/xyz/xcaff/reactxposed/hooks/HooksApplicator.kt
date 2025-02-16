package xyz.xcaff.reactxposed.hooks

import android.app.Application
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class HooksApplicator : IXposedHookLoadPackage {
    override fun handleLoadPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        println("handleLoadPackage!!!")
        GlobalScope.launch {
            println("getting application!!!")
            val application = getApplication()
            Log.e("test!!", application.packageName)
        }
    }
}

suspend fun getApplication(): Application =
    suspendCancellableCoroutine {
        XposedHelpers.findAndHookMethod(
            Application::class.java,
            "onCreate",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)

                    val application = param.thisObject as Application
                    it.resume(application)
                }
            }
        )
    }