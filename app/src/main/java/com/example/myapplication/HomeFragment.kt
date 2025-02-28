package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "HomeFragment" // Тег для логов
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated") // Лог вызова onViewCreated

        // Обработка нажатия на кнопку для перехода в StatisticsFragment
        binding.btnGoToStatistics.setOnClickListener {
            // Создаем Bundle для передачи данных
            val bundle = Bundle().apply {
                putString("key_steps", "5,000") // Пример данных
            }

            // Создаем экземпляр StatisticsFragment и передаем данные
            val statisticsFragment = StatisticsFragment().apply {
                arguments = bundle
            }

            // Заменяем текущий фрагмент на StatisticsFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, statisticsFragment)
                .addToBackStack(null) // Добавляем в back stack для возврата
                .commit()
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