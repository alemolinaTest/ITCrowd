package org.herventure.android.commons

import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.amolina.weather.clima.R


/**
 * Created by amolina on 7/01/18.
 */
class CustomDialog : DialogFragment() {

    var confirmListener: (() -> Unit)? = null
    var cancelListener: (() -> Unit)? = null
    var targetActionId: Int = 0

    companion object {
        const val ARGUMENT_TITLE = "title"
        const val ARGUMENT_DESCRIPTION = "description"
        const val ARGUMENT_POSITIVE_TEXT = "positive_text"
        const val ARGUMENT_NEGATIVE_TEXT = "negative_text"

        fun newInstance(title: String, description: String, negativeText: String, positiveText: String): CustomDialog {
            val fragment = CustomDialog()
            val arguments = Bundle()
            arguments.putString(ARGUMENT_TITLE, title)
            arguments.putString(ARGUMENT_DESCRIPTION, description)
            arguments.putString(ARGUMENT_POSITIVE_TEXT, positiveText)
            arguments.putString(ARGUMENT_NEGATIVE_TEXT, negativeText)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onResume() {
        super.onResume()

        // set window width to 75%
        val window = dialog.window
        val size = Point()

        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)

        val width = size.x

        window.setLayout((width * 0.75).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(ARGUMENT_TITLE) ?: ""
        val description = arguments?.getString(ARGUMENT_DESCRIPTION) ?: ""
        val positiveText = arguments?.getString(ARGUMENT_POSITIVE_TEXT) ?: ""
        val negativeText = arguments?.getString(ARGUMENT_NEGATIVE_TEXT) ?: ""

        view.findViewById<TextView>(R.id.title).text = title
        view.findViewById<TextView>(R.id.description).text = description
        val confirmButton = view.findViewById<Button>(R.id.confirm)
        val cancelButton = view.findViewById<Button>(R.id.cancel)
        confirmButton.text = positiveText
        cancelButton.text = negativeText
        confirmButton.setOnClickListener {
            confirmListener?.invoke()
            dismiss()
        }
        cancelButton.setOnClickListener {
            cancelListener?.invoke()
            dismiss()
        }
    }
}