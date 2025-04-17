package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "StatisticsFragment" // Тег для логов
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate") // Лог вызова onCreate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView") // Лог вызова onCreateView
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated") // Лог вызова onViewCreated

        // Получаем данные из Bundle
        val steps = arguments?.getString("key_steps") ?: "0"

        // Отображаем данные
        binding.tvSteps.text = "Шаги: $steps"

        // Обработка нажатия на кнопку для возврата данных
        binding.btnSendDataBack.setOnClickListener {
            val result = "Данные из StatisticsFragment: $steps шагов"
            parentFragmentManager.setFragmentResult("requestKey", Bundle().apply {
                putString("bundleKey", result)
            })
            parentFragmentManager.popBackStack() // Возврат к предыдущему фрагменту
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart") // Лог вызова onStart
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume") // Лог вызова onResume
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause") // Лог вызова onPause
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop") // Лог вызова onStop
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView") // Лог вызова onDestroyView
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy") // Лог вызова onDestroy
    }
}