package com.yomlaiolo.f1widget.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yomlaiolo.f1widget.data.F1ApiService
import com.yomlaiolo.f1widget.data.models.ConstructorStanding
import com.yomlaiolo.f1widget.data.models.DriverStanding
import com.yomlaiolo.f1widget.data.models.Race
import com.yomlaiolo.f1widget.data.repository.F1Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class MainUiState(
    val isLoading: Boolean = true,
    val nextRace: F1Repository.RaceWithContext? = null,
    val driverStandings: List<DriverStanding> = emptyList(),
    val constructorStandings: List<ConstructorStanding> = emptyList(),
    val seasonCalendar: List<Race> = emptyList(),
    val error: String? = null
)

data class StandingsUiState(
    val isLoading: Boolean = false,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val driverStandings: List<DriverStanding> = emptyList(),
    val constructorStandings: List<ConstructorStanding> = emptyList(),
    val error: String? = null
)

class MainViewModel : ViewModel() {
    
    private val repository = F1Repository(F1ApiService.create())
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    private val _standingsState = MutableStateFlow(StandingsUiState())
    val standingsState: StateFlow<StandingsUiState> = _standingsState.asStateFlow()

    init {
        loadData()
    }
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Charger toutes les données en parallèle
                val nextRace = repository.getUpcomingRace()
                val driverStandings = repository.getDriverStandings()
                val constructorStandings = repository.getConstructorStandings()
                val seasonCalendar = repository.getSeasonCalendar()
                
                _uiState.value = MainUiState(
                    isLoading = false,
                    nextRace = nextRace,
                    driverStandings = driverStandings,
                    constructorStandings = constructorStandings,
                    seasonCalendar = seasonCalendar
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun loadStandingsForYear(year: Int) {
        if (year == _standingsState.value.selectedYear && _standingsState.value.driverStandings.isNotEmpty()) return

        viewModelScope.launch {
            _standingsState.value = _standingsState.value.copy(
                isLoading = true,
                selectedYear = year,
                error = null
            )

            try {
                val driverStandings = repository.getDriverStandingsByYear(year)
                val constructorStandings = repository.getConstructorStandingsByYear(year)

                _standingsState.value = StandingsUiState(
                    isLoading = false,
                    selectedYear = year,
                    driverStandings = driverStandings,
                    constructorStandings = constructorStandings
                )
            } catch (e: Exception) {
                _standingsState.value = _standingsState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur de chargement"
                )
            }
        }
    }
}