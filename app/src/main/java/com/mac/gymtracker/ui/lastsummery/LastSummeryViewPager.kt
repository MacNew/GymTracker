package com.mac.gymtracker.ui.lastsummery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mac.gymtracker.databinding.LastSummaryViewpagerFragmentBinding

private const val NUM_PAGES = 2;

class NewLastSummaryFragment : Fragment() {
    private var _binding: LastSummaryViewpagerFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LastSummaryViewpagerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = LastSummeryViewPager(childFragmentManager)
        binding.pager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)
    }

    private inner class LastSummeryViewPager(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getPageTitle(position: Int): CharSequence? {
            if (position == 0) {
                return "Last one week summery"
            } else {
                return "Pie chart"
            }
        }

        override fun getItem(position: Int): Fragment {
            val fragment: Fragment
            if (position == 0) {
                fragment = LastSummeryFragment()
            } else {
                fragment = PieChartFragment()
            }
            return fragment
        }
    }
}

private const val ARG_OBJECT = "object"

