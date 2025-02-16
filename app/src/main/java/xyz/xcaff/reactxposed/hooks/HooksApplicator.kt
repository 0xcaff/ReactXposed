package xyz.xcaff.reactxposed.hooks

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HooksApplicator : IXposedHookLoadPackage {
    override fun handleLoadPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        val method = loadPackageParam.classLoader.loadClass("com.facebook.react.bridge.JavaModuleWrapper").methods.find { it.name == "invoke" }!!
        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val moduleWrapperInstance = param.thisObject
                val name = (moduleWrapperInstance.javaClass.methods.find { it.name == "getName" }!!).invoke(moduleWrapperInstance) as String
                if (listOf("UIManager", "NativeAnimatedModule", "ReanimatedModule").contains(name)) {
                    return
                }

                val methods = (moduleWrapperInstance.javaClass.methods.find { it.name == "getMethodDescriptors" }!!).invoke(moduleWrapperInstance) as List<*>
                val methodIdx = param.args[0] as Int
                val method = methods[methodIdx]!!

                val nameField = method.javaClass.getDeclaredField("name")
                nameField.isAccessible = true
                val methodName = nameField.get(method) as String

                val signatureField = method.javaClass.getDeclaredField("signature")
                signatureField.isAccessible = true
                val signature = signatureField.get(method) as String?

                val typeField = method.javaClass.getDeclaredField("type")
                typeField.isAccessible = true
                val type = typeField.get(method) as String?

                val params = param.args[1]
                val paramsList = params.javaClass.getMethod("toArrayList").invoke(params) as ArrayList<*>

                println("crossing bridge $name.$methodName $signature $type $paramsList")
            }
        })
    }
}