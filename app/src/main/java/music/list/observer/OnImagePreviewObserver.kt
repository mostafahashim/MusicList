package music.list.observer

import android.widget.ImageView

interface OnImagePreviewObserver {
    fun onOpenViewer(startPosition: Int, imageView: ImageView)
}