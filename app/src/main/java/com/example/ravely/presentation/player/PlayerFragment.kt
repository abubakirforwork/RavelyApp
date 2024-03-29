package com.example.ravely.presentation.player

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.SeekBar
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ravely.R
import com.example.ravely.databinding.FragmentPlayerBinding
import com.example.ravely.domain.model.MusicModel
import com.example.ravely.presentation.SharedViewModel
import com.example.ravely.utils.formatDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private val binding: FragmentPlayerBinding by lazy {
        FragmentPlayerBinding.inflate(layoutInflater)
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: PlayerViewModel by viewModel()
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val musicList = mutableListOf<MusicModel>()
    private val sharedPreference: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
    }
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStatusBarColor(getColor(requireContext(), R.color.gray))
        initObserve()
        initButtons()
        initSeekBar()
    }

    private fun initStatusBarColor(statusBarColor: Int) {
        val window = activity?.window
        window?.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = statusBarColor
    }

    private fun initObserve() {
        sharedViewModel.musicList.observe(viewLifecycleOwner) { newListMusic ->
            musicList.addAll(newListMusic)
        }

        sharedViewModel.position.observe(viewLifecycleOwner) { newPosition ->
            position = musicList.indexOfFirst { it.id == newPosition }

            initMusicData()
        }
    }

    private fun initMusicData() {
        if (musicList.isNotEmpty() && position in musicList.indices) {
            val music = musicList[position]
            with(binding) {

                Glide.with(ivImage.context).load(music.album).into(ivImage)

                tvMusicName.text = music.title
                tvMusicDuration.text = formatDuration(music.duration)
                sbMusic.max = music.duration.toInt()

                updateSeekBar()
            }
        }
    }

    private fun updateSeekBar() {
        lifecycleScope.launch {
            while (mediaPlayer.isPlaying) {
                val musicPosition = mediaPlayer.currentPosition
                binding.sbMusic.progress = musicPosition
                binding.tvMusicTime.text = formatDuration(musicPosition.toLong())
                delay(1000)
            }
        }
    }

    private fun initSeekBar() {
        binding.sbMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initButtons() {
        with(binding) {
            btnPlayTogglePause.setOnClickListener { musicToggle() }
            btnPrevious.setOnClickListener { musicPrevious() }
            btnNext.setOnClickListener { musicNext() }
        }
    }

    private fun musicToggle() {
        with(binding) {
            if (mediaPlayer.isPlaying) {
                musicPosition()
                mediaPlayer.pause()
                btnPlayTogglePause.setIconResource(R.drawable.ic_play)
            } else {
                musicPosition()
                playMusic(musicList[position].id)
                btnPlayTogglePause.setIconResource(R.drawable.ic_pause)
            }
        }
    }

    private fun musicPrevious() {
        if (position > 0) {
            position--
            resetMusicPosition()
            musicFromPosition()
        }
    }

    private fun musicNext() {
        if (position < musicList.size - 1) {
            position++
            resetMusicPosition()
            musicFromPosition()
        }
    }

    private fun musicPosition() {
        val preference = sharedPreference.edit()
        preference.putInt("position", position)
        preference.putInt("seekBarPosition", mediaPlayer.currentPosition)
        preference.apply()
    }

    private fun resetMusicPosition() {
        val preference = sharedPreference.edit()
        preference.putInt("seekBarPosition", 0)
        preference.apply()
    }

    private fun musicFromPosition() {
        playMusic(musicList[position].id)
        initMusicData()
        binding.btnPlayTogglePause.setIconResource(R.drawable.ic_pause)
    }

    private fun playMusic(musicId: Long) {
        val pos = sharedPreference.getInt("seekBarPosition", 0)

        mediaPlayer.apply {
            reset()
            val musicUri = Uri.parse(viewModel.getMusicById(musicId))
            setDataSource(requireContext(), musicUri)
            prepareAsync()
            setOnPreparedListener {
                seekTo(pos)
                start()
                updateSeekBar()
            }
            setOnCompletionListener { musicNext() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        initStatusBarColor(getColor(requireContext(), R.color.dark))
        mediaPlayer.release()
    }
}