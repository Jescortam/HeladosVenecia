package layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.heladosvenecia.R

class SaleAdapter: RecyclerView.Adapter<SaleAdapter.ViewHolder>() {
    public lateinit var labels: Array<String>;

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var button: Button = view.findViewById(R.id.saleButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.sale_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.button.text = labels[position]
    }

    override fun getItemCount() = labels.size
}
