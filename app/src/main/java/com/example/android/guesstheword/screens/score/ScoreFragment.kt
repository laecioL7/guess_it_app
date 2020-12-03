/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding
import com.example.android.guesstheword.screens.game.GameViewModel

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    lateinit var scoreViewFactory : ScoreViewModelFactory;

    lateinit var viewModel : ScoreViewModel;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )

        //data from view model
        scoreViewFactory = ScoreViewModelFactory(0)

        //requisita a viewModel do fragmento
        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)

        //coloca a view model na variavel do xml
        binding.scoreViewModel = viewModel

        /*passing in the ViewModel factory, you're telling ViewModelProvider
        to use this factory to create ScoreViewModel*/
        ViewModelProvider(this, scoreViewFactory).get(ScoreViewModel::class.java)

        // Get args using by navArgs property delegate
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        binding.scoreText.text = scoreFragmentArgs.score.toString()
        binding.playAgainButton.setOnClickListener { onPlayAgain() }

        // Add observer for score
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        //quando clica atualiza o objeto live data
        binding.playAgainButton.setOnClickListener {
            viewModel.onPlayAgain()
        }

        //observer do evento observando um dado q indica se acabou ou não
        //metodo anonimo com parametro que é o próprio objeto
        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer {
            playAgain ->
            if(playAgain)
            {
                onPlayAgain()
                viewModel.onPlayAgainComplete()
            }
        })


        return binding.root
    }

    private fun onPlayAgain() {
        findNavController().navigate(ScoreFragmentDirections.actionRestart())
    }
}
