package com.kylecorry.ceres.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.kylecorry.andromeda.core.coroutines.ControlledRunner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsyncImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs),
    LifecycleEventObserver {

    private var imageLoader: ControlledRunner<Unit> = ControlledRunner()
    private var lastBitmap: Bitmap? = null

    private var shouldClearBitmap = true

    fun setImageBitmap(lifecycleOwner: LifecycleOwner, provider: suspend () -> Bitmap) {
        lifecycleOwner.lifecycle.removeObserver(this)
        lifecycleOwner.lifecycle.addObserver(this)

        lifecycleOwner.lifecycleScope.launchWhenResumed {
            imageLoader.cancelPreviousThenRun {
                withContext(Dispatchers.Main) {
                    super.setImageDrawable(null)
                }
                lastBitmap = withContext(Dispatchers.IO) {
                    lastBitmap?.recycle()
                    provider.invoke()
                }
                withContext(Dispatchers.Main) {
                    if (lastBitmap?.isRecycled == false) {
                        shouldClearBitmap = false
                        super.setImageBitmap(lastBitmap)
                        shouldClearBitmap = true
                    }
                }
            }
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        imageLoader.cancel()
        super.setImageBitmap(bm)
        tryClear()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        imageLoader.cancel()
        super.setImageDrawable(drawable)
        tryClear()
    }

    override fun setImageResource(resId: Int) {
        imageLoader.cancel()
        super.setImageResource(resId)
        tryClear()
    }

    override fun setImageURI(uri: Uri?) {
        imageLoader.cancel()
        super.setImageURI(uri)
        tryClear()
    }

    override fun setImageIcon(icon: Icon?) {
        imageLoader.cancel()
        super.setImageIcon(icon)
        tryClear()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            imageLoader.cancel()
        }
    }

    private fun tryClear(){
        if (shouldClearBitmap) {
            lastBitmap?.recycle()
        }
    }

}