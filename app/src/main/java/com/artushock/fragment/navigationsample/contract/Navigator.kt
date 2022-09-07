package com.artushock.fragment.navigationsample.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.artushock.fragment.navigationsample.model.Options

typealias ResultListener<T> = (T) -> Unit

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun openBoxFragment(options: Options)

    fun openOptionsFragment(options: Options)

    fun openSuccessFragment()

    fun openAboutFragment()

    fun goBack()

    fun goMainMenu()

    fun <T: Parcelable> publishResult(result: T)

    fun <T: Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>)
}