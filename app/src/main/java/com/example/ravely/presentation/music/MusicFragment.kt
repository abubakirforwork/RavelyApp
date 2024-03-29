package com.example.ravely.presentation.music

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ravely.R
import com.example.ravely.databinding.FragmentMusicBinding
import com.example.ravely.domain.model.MusicModel
import com.example.ravely.presentation.SharedViewModel
import com.example.ravely.presentation.player.PlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicFragment : Fragment() {

    private val binding by lazy { FragmentMusicBinding.inflate(layoutInflater) }
    private val viewModel: MusicViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val musicAdapter: MusicAdapter by lazy { MusicAdapter() }
    private val musicList = mutableListOf<MusicModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPermission()
        initViewModel()
        initRecyclerView()
        initObserve()
    }

    private fun initPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    loadMusic()
                } else {
                    Toast.makeText(
                        requireContext(), "Need permission for music", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        when {
            checkSelfPermission(
                requireContext(), READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED -> {
                loadMusic()
            }

            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }

            else -> {
                requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun initViewModel() {
        viewModel.musicList.observe(viewLifecycleOwner) { newMusicList ->
            if (newMusicList.isNotEmpty()) {
                musicList.addAll(newMusicList)
                musicAdapter.submitList(newMusicList)
                binding.progressBar.visibility = View.GONE
                binding.progressBarBg.visibility = View.GONE
                binding.rvMusic.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvMusic.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = musicAdapter.apply {
                onItemClick = { position ->
                    sharedViewModel.setMusicList(musicList)
                    sharedViewModel.setPosition(musicList[position].id)

                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, PlayerFragment())
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }
    }

    private fun loadMusic() {
        viewModel.getAllMusic()
    }

    private fun initObserve() {
        viewModel.musicList.observe(viewLifecycleOwner) { newMusicList ->
            musicList.addAll(newMusicList)
            musicAdapter.submitList(newMusicList)
        }
    }
}