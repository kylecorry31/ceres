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
import com.kylecorry.andromeda.core.tryOrLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsyncImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs),
    LifecycleEventObserver {

    private var imageLoader: ControlledRunner<Unit> = ControlledRunner()
    private var lastBitmap: Bitmap? = null

    fun setImageBitmap(lifecycleOwner: LifecycleOwner, provider: suspend () -> Bitmap) {
        // Remove and add the observer to ensure only one is added
        lifecycleOwner.lifecycle.removeObserver(this)
        lifecycleOwner.lifecycle.addObserver(this)

        lifecycleOwner.lifecycleScope.launchWhenResumed {
            imageLoader.cancelPreviousThenRun {
                withContext(Dispatchers.Main) {
                    clearBitmap()
                }
                lastBitmap = withContext(Dispatchers.IO) {
                    provider.invoke()
                }
                withContext(Dispatchers.Main) {
                    if (lastBitmap?.isRecycled == false) {
                        tryOrLog {
                            super.setImageBitmap(lastBitmap)
                        }
                    }
                }
            }
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        imageLoader.cancel()
        clearBitmap()
        super.setImageBitmap(bm)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        imageLoader.cancel()
        clearBitmap()
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        imageLoader.cancel()
        clearBitmap()
        super.setImageResource(resId)
    }

    override fun setImageURI(uri: Uri?) {
        imageLoader.cancel()
        clearBitmap()
        super.setImageURI(uri)
    }

    override fun setImageIcon(icon: Icon?) {
        imageLoader.cancel()
        clearBitmap()
        super.setImageIcon(icon)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            imageLoader.cancel()
            clearBitmap()
        }
    }

    private fun clearBitmap() {
        tryOrLog {
            super.setImageDrawable(null)
            lastBitmap?.recycle()
            lastBitmap = null
        }
    }

}