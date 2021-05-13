package com.mac.gymtracker.ui.exerciselist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo

class ExerciseListViewModel(repository: LocalExerciselistRepo, exerciseId:Int): ViewModel() {
    private val _exercisedList = MutableLiveData<LiveData<List<ExerciseListModle>>>().apply {
        value = repository.getExerciseListById(exerciseId)
    }
    val exerciseList:LiveData<LiveData<List<ExerciseListModle>>> = _exercisedList

}

class ExerciseListViewModelFactory(private val repository: LocalExerciselistRepo,
                                   private val exerciseId:Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExerciseListViewModel(repository, exerciseId) as T
    }
}