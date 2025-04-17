package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.WeatherApiService
import com.example.myapplication.databinding.FragmentStatisticsBinding
import org.json.JSONObject
import kotlin.math.roundToInt

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    // Инициализируем API сервисы
    private lateinit var apiService: ApiService
    private lateinit var weatherApiService: WeatherApiService
    
    // Город по умолчанию (можно сделать настраиваемым в настройках)
    private val defaultCity = "Москва" 

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

        // Инициализируем API сервисы
        apiService = ApiService(requireContext())
        weatherApiService = WeatherApiService(requireContext())

        // Получаем данные из Bundle
        val steps = arguments?.getString("key_steps") ?: "0"

        // Отображаем данные
        binding.tvSteps.text = "Шаги: $steps"

        // Обработка нажатия на кнопку для загрузки погоды
        binding.btnLoadWeather.setOnClickListener {
            loadWeatherData()
        }

        // Обработка нажатия на кнопку для загрузки данных с jsonplaceholder
        binding.btnLoadData.setOnClickListener {
            loadPostDataFromApi()
        }

        // Обработка нажатия на кнопку для возврата данных
        binding.btnSendDataBack.setOnClickListener {
            val result = "Данные из StatisticsFragment: $steps шагов"
            parentFragmentManager.setFragmentResult("requestKey", Bundle().apply {
                putString("bundleKey", result)
            })
            parentFragmentManager.popBackStack() // Возврат к предыдущему фрагменту
        }
        
        // Загружаем погоду при создании фрагмента
        loadWeatherData()
    }

    /**
     * Загрузка данных о погоде
     */
    private fun loadWeatherData() {
        // Показываем состояние загрузки
        binding.tvCityName.text = "Загрузка..."
        binding.tvWeatherDescription.text = ""
        binding.tvTemperature.text = "--°C"
        binding.tvWeatherDetails.text = "Загрузка данных..."
        binding.tvActivityRecommendation.text = ""
        
        // ID города Москва
        val moscowCityId = 524901
        
        // Вызываем API для получения данных о погоде по ID города
        // Это более надежный метод, так как исключает проблемы с кодировкой названия
        weatherApiService.getCurrentWeatherByCityId(
            cityId = moscowCityId,
            successListener = { weatherData ->
                // Обновляем UI с полученными данными о погоде
                binding.tvCityName.text = weatherData.cityName
                binding.tvWeatherDescription.text = weatherData.weatherDescription
                binding.tvTemperature.text = "${weatherData.temperature.roundToInt()}°C"
                
                // Форматируем детали погоды
                val details = "Ощущается как: ${weatherData.feelsLike.roundToInt()}°C, " +
                        "Влажность: ${weatherData.humidity}%, " +
                        "Давление: ${weatherData.pressure} гПа, " +
                        "Ветер: ${weatherData.windSpeed} м/с"
                binding.tvWeatherDetails.text = details
                
                // Устанавливаем рекомендацию по активности
                binding.tvActivityRecommendation.text = weatherData.getActivityRecommendation()
                
                // Загружаем иконку погоды
                loadWeatherIcon(weatherData.getIconUrl())
                
                showToast("Данные о погоде успешно загружены")
            },
            errorListener = { errorMessage ->
                // Обработка ошибки
                binding.tvCityName.text = defaultCity
                binding.tvWeatherDescription.text = "Ошибка загрузки"
                binding.tvWeatherDetails.text = errorMessage
                binding.tvActivityRecommendation.text = "Не удалось получить рекомендации"
                
                showToast("Ошибка: $errorMessage")
            }
        )
    }
    
    /**
     * Загрузка иконки погоды
     */
    private fun loadWeatherIcon(iconUrl: String) {
        Log.d(TAG, "Loading weather icon from: $iconUrl")
        
        val imageRequest = ImageRequest(
            iconUrl,
            { bitmap ->
                binding.ivWeatherIcon.setImageBitmap(bitmap)
            },
            0, 0, null, null,
            { error ->
                Log.e(TAG, "Error loading weather icon: ${error.message}")
            }
        )
        
        Volley.newRequestQueue(requireContext()).add(imageRequest)
    }

    /**
     * Загрузка данных о посте через jsonplaceholder API (оставлено для демонстрации)
     */
    private fun loadPostDataFromApi() {
        // Показываем состояние загрузки
        binding.tvApiTitle.text = "Загрузка данных..."
        binding.tvApiBody.text = ""

        // Выбираем случайный ID поста от 1 до 100
        val randomPostId = (1..100).random()

        // Вызываем API для получения данных
        apiService.getPost(
            postId = randomPostId,
            successListener = { response ->
                // Обработка успешного ответа в основном потоке
                val title = response.getString("title")
                val body = response.getString("body")
                
                // Обновляем UI с полученными данными
                binding.tvApiTitle.text = title
                binding.tvApiBody.text = body
                
                // Показываем сообщение об успехе
                showToast("Данные успешно загружены")
            },
            errorListener = { errorMessage ->
                // Обработка ошибки
                binding.tvApiTitle.text = "Ошибка загрузки"
                binding.tvApiBody.text = errorMessage
                
                // Показываем сообщение об ошибке
                showToast("Ошибка: $errorMessage")
            }
        )
    }

    /**
     * Показ всплывающего сообщения
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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