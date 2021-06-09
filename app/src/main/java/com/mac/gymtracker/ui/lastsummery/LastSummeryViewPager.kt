package com.mac.gymtracker.ui.lastsummery

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mac.gymtracker.R
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val pagerAdapter = LastSummeryViewPager(childFragmentManager)
        binding.pager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Log.e(TAG, "onPageScrolled")
            }

            override fun onPageSelected(position: Int) {
                if (PieChartFragment.flage) {
                    piSummeryFragment.onPageCnangeListner()
                    Thread.sleep(500)
                    lastSummeryFragment.onPageCnangeListner()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private var lastSummeryFragment = LastSummeryFragment()
    private var piSummeryFragment = PieChartFragment()


    private inner class LastSummeryViewPager(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getPageTitle(position: Int): CharSequence? =
            if (position == 0) {
                "Last Summery"
            } else {
                "Pie chart"
            }

        override fun getItem(position: Int): Fragment {
            val fragment: Fragment = if (position == 0) {
                lastSummeryFragment
            } else {
                piSummeryFragment
            }
            return fragment
        }
    }
}

interface Page {
    fun onPageCnangeListner()
}

private const val TAG = "lastSummery"
