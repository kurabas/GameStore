package com.example.madlevel5task2.undo.viewBinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException

/**
 * Holds the binding for a view that is bindable.
 *
 * @source https://android.jlelse.eu/viewbinding-in-fragments-the-clean-easy-way-2f0ce68aee22
 */
class ViewBindingHolder<V: ViewBinding>: IViewBindingHolder<V>, LifecycleObserver {

    override var binding: V? = null
    var lifecycle: Lifecycle? = null

    private lateinit var fragmentName: String

    /** Destroys the binding when the bound view is destroyed. */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyView() {
        lifecycle?.removeObserver(this)

        lifecycle = null
        binding = null
    }

    /**
     * Gets the binding. This can be hooked on to a void function
     * or assigned to a variable when needed.
     */
    override fun requireBinding(block: (V.() -> Unit)?): V = binding?.apply {
        block?.invoke(this)
    } ?: throw IllegalStateException("Accessing binding outside of Fragment lifecycle: $fragmentName")

    /** Initializes the binding. */
    override fun initBinding(binding: V, fragment: Fragment, onBound: (V.() -> Unit)?): View {
        this.binding = binding
        lifecycle = fragment.viewLifecycleOwner.lifecycle
        fragmentName = fragment::class.simpleName ?: "N/A"

        lifecycle?.addObserver(this)
        onBound?.invoke(binding)

        return binding.root
    }
}
