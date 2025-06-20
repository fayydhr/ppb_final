// app/src/main/java/com/example/gymbuddy/ui/main/fragments/NewsFragment.kt
package com.example.gymbuddy.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymbuddy.data.local.NewsArticle
import com.example.gymbuddy.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadDummyNews()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNewsArticles.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadDummyNews() {
        val dummyNewsList = listOf(
            NewsArticle(
                "Manfaat Latihan Kekuatan untuk Kesehatan Jantung",
                "Latihan kekuatan tidak hanya membangun otot, tetapi juga sangat penting untuk menjaga kesehatan jantung dan mengurangi risiko penyakit kardiovaskular.",
                "20 Juni 2025"
            ),
            NewsArticle(
                "Panduan Nutrisi: Protein Terbaik untuk Pemulihan Otot",
                "Pemulihan otot yang efektif sangat bergantung pada asupan protein yang cukup. Pelajari sumber protein terbaik dan kapan harus mengonsumsinya.",
                "18 Juni 2025"
            ),
            NewsArticle(
                "Tips Meningkatkan Fleksibilitas dan Mencegah Cedera",
                "Fleksibilitas sering diabaikan dalam rutinitas kebugaran. Ikuti tips ini untuk meningkatkan jangkauan gerak Anda dan mengurangi risiko cedera saat berolahraga.",
                "15 Juni 2025"
            ),
            NewsArticle(
                "Cara Tetap Termotivasi dengan Tujuan Kebugaran Anda",
                "Menjaga motivasi bisa menjadi tantangan. Temukan strategi efektif untuk tetap berkomitmen pada tujuan kebugaran Anda, bahkan saat menghadapi rintangan.",
                "12 Juni 2025"
            ),
            NewsArticle(
                "Tren Kebugaran 2025: Apa yang Harus Anda Coba?",
                "Dari virtual workouts hingga outdoor fitness, kami membahas tren kebugaran teratas yang diperkirakan akan mendominasi tahun 2025. Bersiaplah untuk mencoba hal baru!",
                "10 Juni 2025"
            )
        )
        newsAdapter.submitList(dummyNewsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}