package mszcz.universe.attendancecheck.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mszcz.universe.attendancecheck.db.AppDatabase

class HistoryViewModel(val db: AppDatabase) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}