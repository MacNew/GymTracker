package com.mac.gymtracker.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mac.gymtracker.databinding.FragmentSyncBinding

class FragmentSync : Fragment() {
    private var _binding: FragmentSyncBinding? = null
    private  val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Sync data"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding!!.root
    }
}