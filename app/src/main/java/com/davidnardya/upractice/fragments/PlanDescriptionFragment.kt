package com.davidnardya.upractice.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.davidnardya.upractice.R
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PLAN_DESCRIPTION = "planDescription"

/**
 * A simple [Fragment] subclass.
 * Use the [PlanDescriptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlanDescriptionFragment : Fragment() {
    private var planDescription: String? = null
    private var planDescriptionTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            planDescription = it.getString(PLAN_DESCRIPTION)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_plan_description, container, false)

        planDescriptionTextView = view.findViewById(R.id.plan_description_fragment_textview)

        if (planDescriptionTextView != null) {
            planDescriptionTextView?.text = planDescription
            planDescriptionTextView?.requestFocus()
        }

        return view
    }

    companion object {

        @JvmStatic fun newInstance(planDescription: String) =
                PlanDescriptionFragment().apply {
                    arguments = Bundle().apply {
                        putString(PLAN_DESCRIPTION, planDescription)
                    }
                }
    }

}