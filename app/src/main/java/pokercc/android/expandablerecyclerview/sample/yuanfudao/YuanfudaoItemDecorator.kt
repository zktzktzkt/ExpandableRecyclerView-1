package pokercc.android.expandablerecyclerview.sample.yuanfudao

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import pokercc.android.expandablerecyclerview.ExpandableRecyclerView
import pokercc.android.expandablerecyclerview.sample.dpToPx

private const val LOG_TAG = "YuanfudaoItemDecorator"

internal class YuanfudaoItemDecorator : RecyclerView.ItemDecoration() {
    private val circlePaint = Paint().apply {
        isAntiAlias = true
        color = 0xff297be8.toInt()
        style = Paint.Style.FILL
    }
    private val linePaint1 = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val linePaint2 = Paint().apply {
        color = 0x7a7a7a7a.toInt()
        style = Paint.Style.FILL
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val expandableAdapter = (parent as? ExpandableRecyclerView)?.requireAdapter() ?: return
        for (i in 0 until parent.childCount) {
            val view = parent[i]
            val viewHolder = parent.getChildViewHolder(view)
            val groupPosition = expandableAdapter.getGroupPosition(viewHolder)
            if (expandableAdapter.isGroup(viewHolder.itemViewType)) {
                // 绘制group装饰器
                val groupViewHolder = viewHolder as? YuanfudaoGroupViewHolder ?: continue
                // 绘制一个5dp的原点
                val titleText = groupViewHolder.binding.titleText
                val cx = groupViewHolder.binding.root.x + titleText.x / 2
                val cy = groupViewHolder.binding.root.y + titleText.y + titleText.height / 2
                val radius = 8.dpToPx(parent.context)
                c.drawCircle(cx, cy, radius, circlePaint)
                val expand = expandableAdapter.isExpand(groupPosition)
                linePaint1.strokeWidth = 1.5.dpToPx(parent.context)
                linePaint2.strokeWidth = 1.dpToPx(parent.context)
                val lineLength = 4.dpToPx(parent.context)
                if (expand) {
                    c.drawLine(
                        cx - lineLength,
                        cy,
                        cx + lineLength,
                        cy,
                        linePaint1
                    )
                    c.drawLine(
                        cx,
                        cy + radius + 2.dpToPx(parent.context),
                        cx,
                        groupViewHolder.binding.root.y + groupViewHolder.binding.root.height,
                        linePaint2
                    )
                } else {
                    c.drawLine(
                        cx,
                        cy - lineLength,
                        cx,
                        cy + lineLength,
                        linePaint1
                    )
                    c.drawLine(
                        cx - lineLength,
                        cy,
                        cx + lineLength,
                        cy,
                        linePaint1
                    )
                }
            } else {
                // 绘制child装饰器
                val childViewHolder = viewHolder as? YuanfudaoChildViewHolder ?: continue
                val titleText = childViewHolder.binding.titleText
                val cx = childViewHolder.binding.root.x + titleText.x / 2
                val cy = childViewHolder.binding.root.y + titleText.y + titleText.height / 2
                val radius = 6.dpToPx(parent.context)
                c.drawCircle(cx, cy, radius, circlePaint)
                // 绘制线
                val childPosition = expandableAdapter.getChildPosition(childViewHolder)
                val childCount = expandableAdapter.getChildCount(groupPosition)
                if (childPosition != childCount - 1) {
                    c.drawLine(
                        cx,
                        cy + radius + 2.dpToPx(parent.context),
                        cx,
                        childViewHolder.binding.root.y + childViewHolder.binding.root.height,
                        linePaint2
                    )
                }
                c.drawLine(
                    cx,
                    childViewHolder.binding.root.y,
                    cx,
                    cy - radius - 2.dpToPx(parent.context),
                    linePaint2
                )
            }
        }
    }
}