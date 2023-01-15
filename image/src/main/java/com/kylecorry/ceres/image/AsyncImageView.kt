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
                        super.setImageBitmap(lastBitmap)
                    }
                }
            }
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        imageLoader.cancel()
        super.setImageBitmap(bm)
        lastBitmap?.recycle()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        imageLoader.cancel()
        super.setImageDrawable(drawable)
        lastBitmap?.recycle()
    }

    override fun setImageResource(resId: Int) {
        imageLoader.cancel()
        super.setImageResource(resId)
        lastBitmap?.recycle()

    }

    override fun setImageURI(uri: Uri?) {
        imageLoader.cancel()
        super.setImageURI(uri)
        lastBitmap?.recycle()

    }

    override fun setImageIcon(icon: Icon?) {
        imageLoader.cancel()
        super.setImageIcon(icon)
        lastBitmap?.recycle()

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            imageLoader.cancel()
        }
    }

}