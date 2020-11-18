package mszcz.universe.attendancecheck.ui.history

import androidx.lifecycle.ViewModel
import io.reactivex.Single
import mszcz.universe.attendancecheck.Utilities.HistoryWithClasses
import mszcz.universe.attendancecheck.db.AppDatabase

class HistoryViewModel(val db: AppDatabase) : ViewModel() {

    /**
     * Getting attendance history
     */
    fun getHistory(): Single<List<HistoryWithClasses>>{
        return db.attendanceHistoryInterface().getHistory()
    }


}